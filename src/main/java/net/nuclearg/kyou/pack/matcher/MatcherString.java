package net.nuclearg.kyou.pack.matcher;

import static net.nuclearg.kyou.util.parser.SyntaxRule.lex;
import static net.nuclearg.kyou.util.parser.SyntaxRule.nul;
import static net.nuclearg.kyou.util.parser.SyntaxRule.or;
import static net.nuclearg.kyou.util.parser.SyntaxRule.ref;
import static net.nuclearg.kyou.util.parser.SyntaxRule.rep;
import static net.nuclearg.kyou.util.parser.SyntaxRule.seq;

import java.util.List;
import java.util.regex.Pattern;

import net.nuclearg.kyou.util.lexer.LexDefinition;
import net.nuclearg.kyou.util.parser.SyntaxDefinition;
import net.nuclearg.kyou.util.parser.SyntaxRule;
import net.nuclearg.kyou.util.parser.SyntaxString;
import net.nuclearg.kyou.util.parser.SyntaxTreeNode;

/**
 * 匹配字符串，对应组包样式中match的部分
 * <p>
 * 总体上依照jquery语法，有五种形式：
 * <li>绝对路径 是以#开头的完整的路径表示形式，表示与完整的路径匹配</li>
 * <li>元素类型 可以指定document、struct、field、array其中之一，表示与指定的报文元素类型匹配</li>
 * <li>元素名称 以#开头，后面跟着元素的名称，表示与名称相同的报文元素匹配</li>
 * <li>属性 由中括号括起来，总体上类似于[k=='v']的语法，其中的运算符提供了很多种可供不同场景下选择，表示与属性满足条件的报文元素匹配</li>
 * <li>过滤器 由冒号引导，总体上类似于:xxx的语法。其中有一些是可以加参数的，用小括号括起来，表示与满足特定条件的报文元素匹配</li>
 * </p>
 * 
 * @author ng
 * 
 */
class MatcherString {
    private final String str;

    MatcherString(String str) {
        this.str = str;
    }

    /**
     * 将字符串解析为一棵匹配器信息的树，如果解析失败则抛异常
     * 
     * @return 解析出来的匹配器信息树
     */
    MatcherInfo parseMatcherInfo() {
        SyntaxString<Lex, Syntax> syntaxStr = new SyntaxString<>(this.str);
        SyntaxTreeNode<Lex, Syntax> root = syntaxStr.parse(Syntax.Root);

        return this.parsePipeMatcherInfo(root, Syntax.Root);
    }

    /**
     * 将语法树节点翻译为匹配器信息
     * 
     * @param node
     *            语法树节点
     * @return 匹配器信息
     */
    private MatcherInfo parseMatcherInfo(SyntaxTreeNode<Lex, Syntax> node) {
        switch (node.type) {
            case MatcherListItem:
            case MatcherGroup:
                return this.parsePipeMatcherInfo(node, node.type);
            case Matcher:
                return this.parseMatcherInfo(node.children.get(0));
            case AbsolutePath:
                return this.parseAbsolutePathMatcherInfo(node);
            case NodeType:
                return new MatcherInfo(MatcherType.NodeType, node.token.str);
            case NodeName:
                return new MatcherInfo(MatcherType.NodeName, node.children.get(1).token.str);
            case Attribute:
                return this.parseAttributeMatcherInfo(node);
            case Filter:
                return this.parseFilterMatcherInfo(node);
            default:
                throw new UnsupportedOperationException("syntax tree node type " + node.type);
        }
    }

    /**
     * 解析带管道的匹配器节点。这个方法是把多个匹配器信息拼成树的核心方法
     * 
     * @param parent
     * @param nodeType
     * @return
     */
    private MatcherInfo parsePipeMatcherInfo(SyntaxTreeNode<Lex, Syntax> parent, Syntax nodeType) {
        SyntaxTreeNode<Lex, Syntax> firstChild = parent.children.get(0);
        MatcherInfo current = parseMatcherInfo(firstChild);

        // 判断有多少个MatcherListItem，这些节点之间应当用管道连接起来
        List<SyntaxTreeNode<Lex, Syntax>> restChildren = parent.children.get(1).children;
        for (SyntaxTreeNode<Lex, Syntax> node : restChildren) {
            SyntaxTreeNode<Lex, Syntax> pipeNode = node.children.get(0);
            SyntaxTreeNode<Lex, Syntax> childNode = node.children.get(1);

            MatcherInfo child = this.parseMatcherInfo(childNode);

            // 如果token为null表示这是一个And，需要特殊处理一下
            String pipeName = pipeNode.token == null ? "" : pipeNode.token.str;
            if (pipeName.length() > 0) {
                pipeName = pipeName.trim(); // 把前后的空格trim掉
                if (pipeName.length() == 0)// 如果trim光了，说明这是一个Ancestor，特殊处理一下
                    pipeName = " ";
            }

            MatcherInfo pipe = new MatcherInfo(pipeName, current, child);
            current = pipe;
        }

        return current;
    }

    /**
     * 解析绝对路径的匹配器信息
     * 
     * @param node
     * @return
     */
    private MatcherInfo parseAbsolutePathMatcherInfo(SyntaxTreeNode<Lex, Syntax> node) {
        StringBuilder builder = new StringBuilder("#");

        for (SyntaxTreeNode<Lex, Syntax> child : node.children.get(1).children)
            builder.append(child.children.get(0).token.str).append(child.children.get(1).token.str);

        return new MatcherInfo(MatcherType.AbsolutePath, builder.toString());
    }

    /**
     * 解析属性相关的匹配器信息
     * 
     * @param node
     * @return
     */
    private MatcherInfo parseAttributeMatcherInfo(SyntaxTreeNode<Lex, Syntax> node) {
        node = node.children.get(1);

        // 处理简单属性
        if (node.type == Syntax.SimpleAttribute) {
            String attrName;
            if (node.token != null)
                attrName = node.token.str;
            else
                attrName = node.children.get(1).token.str;
            return new MatcherInfo(attrName, (String) null, (String) null);
        }

        // 处理复杂属性
        String attrName;
        SyntaxTreeNode<Lex, Syntax> attrNameNode = node.children.get(0);
        if (attrNameNode.token != null)
            attrName = attrNameNode.token.str;
        else
            attrName = attrNameNode.children.get(1).token.str;

        // 读取运算符和属性值
        String attrOperator = node.children.get(1).token.str.trim();
        String attrValue = node.children.get(2).children.get(1).token.str;

        return new MatcherInfo(attrName, attrOperator, attrValue);
    }

    /**
     * 解析过滤器相关的匹配器信息
     * 
     * @param node
     * @return
     */
    private MatcherInfo parseFilterMatcherInfo(SyntaxTreeNode<Lex, Syntax> node) {
        throw new UnsupportedOperationException();
    }

    /**
     * 匹配器的类型
     * 
     * @author ng
     * 
     */
    static enum MatcherType {
        /**
         * 绝对路径
         */
        AbsolutePath,
        /**
         * 节点类型
         */
        NodeType,
        /**
         * 节点名称
         */
        NodeName,
        /**
         * 属性
         */
        Attribute,
        /**
         * 无参数的过滤器
         */
        FilterNoParam,
        /**
         * 整数参数的过滤器
         */
        FilterIntegerParam,
        /**
         * 字符串参数的过滤器
         */
        FilterStringParam,
        /**
         * 管道
         */
        Pipe,
    }

    /**
     * 匹配器信息
     * 
     * @author ng
     * 
     */
    static class MatcherInfo {
        /**
         * 匹配器的类型
         */
        final MatcherType type;
        /**
         * 匹配器的正文，绝对路径、节点类型、节点名称、管道符、过滤器名称（唯独不包括属性相关）
         */
        final String text;
        /**
         * 属性的名称
         */
        final String attrName;
        /**
         * 属性的运算符
         */
        final String attrOperator;
        /**
         * 属性的值
         */
        final String attrValue;
        /**
         * 过滤器的参数
         */
        final String filterParam;

        /**
         * 左侧节点
         */
        final MatcherInfo left;
        /**
         * 右侧节点
         */
        final MatcherInfo right;

        MatcherInfo(MatcherType type, String text) {
            this.type = type;
            this.text = text;
            this.attrName = this.attrOperator = this.attrValue = null;
            this.filterParam = null;
            this.left = this.right = null;
        }

        MatcherInfo(String attrName, String attrOperator, String attrValue) {
            this.type = MatcherType.Attribute;
            this.text = null;
            this.attrName = attrName;
            this.attrOperator = attrOperator;
            this.attrValue = attrValue;
            this.filterParam = null;
            this.left = this.right = null;
        }

        MatcherInfo(MatcherType type, String filterName, String filterParam) {
            this.type = type;
            this.text = filterName;
            this.attrName = this.attrOperator = this.attrValue = null;
            this.filterParam = filterParam;
            this.left = this.right = null;
        }

        MatcherInfo(String pipeName, MatcherInfo left, MatcherInfo right) {
            this.type = MatcherType.Pipe;
            this.text = pipeName;
            this.attrName = this.attrOperator = this.attrValue = null;
            this.filterParam = null;
            this.left = left;
            this.right = right;
        }
    }

    /**
     * 词法定义
     * 
     * @author ng
     * 
     */
    static enum Lex implements LexDefinition {
        /**
         * 空白
         */
        Space("\\s+"),
        /**
         * 一个空的占位符
         */
        Empty(""),

        /**
         * 井号
         */
        HashToken("\\#"),
        /**
         * 点号
         */
        DotToken("\\."),

        /**
         * 整数
         */
        Integer("\\-?\\d+"),

        /**
         * 字符串开始
         */
        StringStart("\\'"),
        /**
         * 字符串值
         */
        StringValue("(\\\\'|[^'])*"),
        /**
         * 字符串结束
         */
        StringEnd("\\'"),

        /**
         * 报文节点名称
         */
        NodeName("[_a-zA-Z][-_0-9a-zA-Z]*"),
        /**
         * 报文节点类型的关键字
         */
        NodeTypeKeyword("document|field|struct|array"),

        /**
         * 属性开始
         */
        AttributeStart("\\[\\s*"),
        /**
         * 属性运算符
         */
        AttributeOperator("\\s*[=!~^$]?\\=\\s*"),
        /**
         * 属性结束
         */
        AttributeEnd("\\s*\\]"),

        /**
         * 过滤器标志符
         */
        FilterSign("\\:"),
        /**
         * 过滤器参数开始
         */
        FilterParamStart("\\(\\s*"),
        /**
         * 过滤器参数结束
         */
        FilterParamEnd("\\s*\\)"),

        /**
         * 表示祖先的管道符
         */
        NormalPipeToken("(\\s*\\>\\s*)|(\\s*\\+\\s*)|(\\s+)"),
        /**
         * 表示或的管道符
         */
        OrPipeToken("\\s*\\,\\s*"), ;

        private final Pattern regex;

        private Lex(String regex) {
            this.regex = Pattern.compile(regex);
        }

        @Override
        public Pattern regex() {
            return this.regex;
        }
    }

    /**
     * 语法定义
     * 
     * @author ng
     * 
     */
    static enum Syntax implements SyntaxDefinition<Lex> {
        Integer(lex(Lex.Integer)),
        String(
                seq(
                        lex(Lex.StringStart),
                        lex(Lex.StringValue),
                        lex(Lex.StringEnd))),

        AbsolutePath(
                seq(
                        lex(Lex.HashToken),
                        rep(
                        seq(
                                lex(Lex.DotToken),
                                lex(Lex.NodeName))))),

        NodeType(lex(Lex.NodeTypeKeyword)),

        NodeName(
                seq(
                        lex(Lex.HashToken),
                        lex(Lex.NodeName))),

        SimpleAttribute(
                or(
                        ref(String),
                        lex(Lex.NodeName))),
        NormalAttribute(
                seq(
                        or(
                                ref(String),
                                lex(Lex.NodeName)),
                        lex(Lex.AttributeOperator),
                        ref(String))),
        Attribute(
                seq(
                        lex(Lex.AttributeStart),
                        or(
                                ref(NormalAttribute),
                                ref(SimpleAttribute)),
                        lex(Lex.AttributeEnd))),

        FiliterNoneParam(
                nul(Lex.class)),
        FiliterIntegerParam(
                seq(
                        lex(Lex.FilterParamStart),
                        ref(Integer),
                        lex(Lex.FilterParamEnd))),
        FilterStringParam(
                seq(
                        lex(Lex.FilterParamStart),
                        ref(String),
                        lex(Lex.FilterParamEnd))),
        Filter(
                seq(
                        lex(Lex.FilterSign),
                        lex(Lex.NodeName),
                        or(
                                ref(FiliterIntegerParam),
                                ref(FilterStringParam),
                                ref(FiliterNoneParam)
                        ))),

        AndPipe(nul(Lex.class)),

        NormalPipe(lex(Lex.NormalPipeToken)),

        OrPipe(lex(Lex.OrPipeToken)),

        Matcher(
                seq(
                or(
                        ref(NodeType),
                        ref(NodeName),
                        ref(Attribute),
                        ref(Filter),
                        ref(AbsolutePath)
                ))),
        MatcherGroup(
                seq(
                        ref(Matcher),
                        rep(
                        seq(
                                ref(AndPipe),
                                ref(Matcher))))),
        MatcherListItem(
                seq(
                        ref(MatcherGroup),
                        rep(
                        seq(
                                ref(NormalPipe),
                                ref(MatcherGroup))))),

        Root(
                seq(
                        ref(MatcherListItem),
                        rep(
                        seq(
                                ref(OrPipe),
                                ref(MatcherListItem))))),

        ;

        private final SyntaxRule<Lex> syntax;

        private Syntax(SyntaxRule<Lex> syntax) {
            this.syntax = syntax;
        }

        @Override
        public SyntaxRule<Lex> syntax() {
            return this.syntax;
        }
    }
}
