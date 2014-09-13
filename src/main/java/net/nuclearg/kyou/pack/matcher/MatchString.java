package net.nuclearg.kyou.pack.matcher;

import static net.nuclearg.kyou.util.parser.SyntaxRule.lex;
import static net.nuclearg.kyou.util.parser.SyntaxRule.seq;

import java.util.List;
import java.util.regex.Pattern;

import net.nuclearg.kyou.util.lexer.LexDefinition;
import net.nuclearg.kyou.util.parser.SyntaxDefinition;
import net.nuclearg.kyou.util.parser.SyntaxRule;

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

    List<MatcherInfo> parseMatcherInfo() {
        // TODO
        throw new UnsupportedOperationException();
    }

    static enum MatcherCategory {
        Basic,
        Filter,
        Pipe,
        Attribute,
    }

    static class MatcherInfo {

    }

    private static enum Lex implements LexDefinition {
        /**
         * 空白
         */
        Space("\\s+"),

        /**
         * 整数
         */
        Integer("\\-?\\d+"),

        /**
         * 字符串开始
         */
        StringStart("\'"),
        /**
         * 字符串值
         */
        StringValue("((\\')|[^'])*"),
        /**
         * 字符串结束
         */
        StringEnd("\'"),

        /**
         * 井号
         */
        HashToken("\\#"),
        /**
         * 点号
         */
        DotToken("\\."),

        /**
         * 报文节点名称
         */
        NodeName("[_a-zA-Z][-_0-9a-zA-Z]*"),
        /**
         * 报文节点类型
         */
        NodeType("document|field|struct|array"),

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
         * 过滤器名称
         */
        FilterName("\\w+"),
        /**
         * 过滤器参数开始
         */
        FilterParamStart("\\("),
        /**
         * 过滤器参数结束
         */
        FilterParamEnd("\\)"),

        ;

        private final Pattern regex;

        private Lex(String regex) {
            this.regex = Pattern.compile(regex);
        }

        @Override
        public Pattern regex() {
            return this.regex;
        }
    }

    @SuppressWarnings("unchecked")
    private static enum Syntax implements SyntaxDefinition<Lex> {
        Integer(lex(Lex.Integer)),
        String(seq(
                lex(Lex.StringStart),
                lex(Lex.StringValue),
                lex(Lex.StringEnd))),

        // TODO 定义语法
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
