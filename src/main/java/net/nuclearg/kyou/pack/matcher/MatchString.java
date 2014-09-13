package net.nuclearg.kyou.pack.matcher;

import static net.nuclearg.kyou.util.parser.SyntaxRule.empty;
import static net.nuclearg.kyou.util.parser.SyntaxRule.lex;
import static net.nuclearg.kyou.util.parser.SyntaxRule.or;
import static net.nuclearg.kyou.util.parser.SyntaxRule.ref;
import static net.nuclearg.kyou.util.parser.SyntaxRule.rep;
import static net.nuclearg.kyou.util.parser.SyntaxRule.seq;

import java.util.ArrayList;
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
class MatchString {
    private final String str;

    MatchString(String str) {
        this.str = str;
    }

    /**
     * 将字符串解析为一系列匹配器信息，如果解析失败则抛异常
     * 
     * @return 解析出来的匹配器信息列表
     */
    List<MatcherInfo> parseMatcherInfo() {
        SyntaxString<Lex, Syntax> syntaxStr = new SyntaxString<>(this.str);
        SyntaxTreeNode<Lex, Syntax> root = syntaxStr.parse(Syntax.Root);

        List<MatcherInfo> result = new ArrayList<>();
        System.out.println(result);

        // 遍历语法树，解析其中的每个匹配器
        for (SyntaxTreeNode<Lex, Syntax> matcherNode : root.children)
            switch (matcherNode.type) {
                case AbsolutePath:
                    break;
                case NodeType:
                    break;
                case NodeName:
                    break;
                case Attribute:
                    break;
                case Filter:
                    break;
                default:
                    throw new UnsupportedOperationException("matcher type " + matcherNode.type);
            }

        return result;
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

        MatcherInfo(MatcherType type, String text) {
            this.type = type;
            this.text = text;
            this.attrName = null;
            this.attrOperator = null;
            this.attrValue = null;
            this.filterParam = null;
        }

        MatcherInfo(String attrName, String attrOperator, String attrValue) {
            this.type = MatcherType.Attribute;
            this.text = null;
            this.attrName = attrName;
            this.attrOperator = attrOperator;
            this.attrValue = attrValue;
            this.filterParam = null;
        }

        MatcherInfo(MatcherType type, String filterName, String filterParam) {
            this.type = type;
            this.text = filterName;
            this.attrName = null;
            this.attrOperator = null;
            this.attrValue = null;
            this.filterParam = filterParam;
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
        AttributeStart("\\["),
        /**
         * 属性运算符
         */
        AttributeOperator("[=!~^$]?\\="),
        /**
         * 属性结束
         */
        AttributeEnd("\\]"),

        /**
         * 过滤器标志符
         */
        FilterSign("\\:"),
        /**
         * 过滤器参数开始
         */
        FilterParamStart("\\("),
        /**
         * 过滤器参数结束
         */
        FilterParamEnd("\\)"),

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
                seq(lex(Lex.HashToken),
                        rep(
                        seq(lex(Lex.DotToken),
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
                empty(Lex.class)),
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

        AndPipe(empty(Lex.class)),

        NormalPipe(lex(Lex.NormalPipeToken)),

        OrPipe(lex(Lex.OrPipeToken)),

        Matcher(
                seq(
                or(
                        ref(NodeType),
                        ref(NodeName),
                        ref(Attribute),
                        ref(Filter)))),
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

        Root(or(
                seq(
                        ref(MatcherListItem),
                        rep(seq(
                                ref(OrPipe),
                                ref(MatcherListItem)))),
                ref(AbsolutePath))),

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
