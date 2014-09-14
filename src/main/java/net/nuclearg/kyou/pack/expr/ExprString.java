package net.nuclearg.kyou.pack.expr;

import static net.nuclearg.kyou.util.parser.SyntaxRule.lex;
import static net.nuclearg.kyou.util.parser.SyntaxRule.nul;
import static net.nuclearg.kyou.util.parser.SyntaxRule.opt;
import static net.nuclearg.kyou.util.parser.SyntaxRule.or;
import static net.nuclearg.kyou.util.parser.SyntaxRule.ref;
import static net.nuclearg.kyou.util.parser.SyntaxRule.rep;
import static net.nuclearg.kyou.util.parser.SyntaxRule.seq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.nuclearg.kyou.util.lexer.LexDefinition;
import net.nuclearg.kyou.util.parser.SyntaxDefinition;
import net.nuclearg.kyou.util.parser.SyntaxRule;
import net.nuclearg.kyou.util.parser.SyntaxString;
import net.nuclearg.kyou.util.parser.SyntaxTreeNode;

import org.apache.commons.lang.StringUtils;

/**
 * 表达式字符串，对应于组包样式中param的部分
 * 
 * @author ng
 * 
 */
class ExprString {
    /**
     * 原始的字符串
     */
    private final String str;

    ExprString(String str) {
        this.str = str;
    }

    /**
     * 将字符串解析为一系列的表达式信息。如果解析失败则抛异常
     * 
     * @return 解析出来的表达式信息列表
     */
    List<ExprInfo> parseExprInfo() {
        SyntaxString<Lex, Syntax> syntaxStr = new SyntaxString<>(this.str);
        SyntaxTreeNode<Lex, Syntax> root = syntaxStr.parse(Syntax.Root);

        List<ExprInfo> result = new ArrayList<>();

        // 遍历语法树，解析其中的每个expr
        for (SyntaxTreeNode<Lex, Syntax> exprUnit : root.children) {
            exprUnit = exprUnit.children.get(0);

            // 判断是不是整数字面量，如果是的话要单独处理
            if (exprUnit.token != null) {
                result.add(new ExprInfo(exprUnit.token.str, (String) null));
                continue;
            }

            // 解析name
            SyntaxTreeNode<Lex, Syntax> nameUnit = exprUnit.children.get(0);
            String name = nameUnit.token.str;

            // 解析postfix
            ExprInfo exprInfo;
            SyntaxTreeNode<Lex, Syntax> postfixNode = exprUnit.children.get(1);
            switch (postfixNode.type) {
                case NoPostfix:
                    // 无后缀
                    exprInfo = new ExprInfo(name, (String) null);
                    break;
                case SimplePostfix:
                    // 简单后缀
                    SyntaxTreeNode<Lex, Syntax> simplePostfixNode = postfixNode.children.get(1);
                    String postfix;

                    if (simplePostfixNode.token != null)
                        postfix = simplePostfixNode.token.str;
                    else
                        postfix = simplePostfixNode.children.get(1).token.str;
                    exprInfo = new ExprInfo(name, postfix);
                    break;
                case ComplexPostfix:
                    // 解析复杂后缀
                    Map<String, String> complexPostfixMap = new HashMap<>();

                    // size小于3表示start和end是紧挨着的，即postfix为空
                    for (SyntaxTreeNode<Lex, Syntax> postfixFieldUnit : postfixNode.children.get(1).children) {
                        if (postfixFieldUnit.type != Syntax.ComplexPostfixField)
                            continue;

                        String k = postfixFieldUnit.children.get(0).token.str;
                        String v = postfixFieldUnit.children.get(2).children.get(1).token.str;

                        complexPostfixMap.put(k, v);
                    }

                    exprInfo = new ExprInfo(name, complexPostfixMap);
                    break;
                default:
                    throw new UnsupportedOperationException("postfix type " + postfixNode.type);
            }

            result.add(exprInfo);
        }

        return result;
    }

    @Override
    public String toString() {
        return this.str;
    }

    /**
     * 表达式信息
     * 
     * @author ng
     * 
     */
    static class ExprInfo {
        /**
         * 表达式本体
         */
        final String name;
        /**
         * 简单后缀
         */
        final String postfix;
        /**
         * 复杂后缀
         */
        final Map<String, String> complexPostfix;

        ExprInfo(String name, String postfix) {
            this.name = name;
            this.postfix = postfix;
            this.complexPostfix = null;
        }

        ExprInfo(String name, Map<String, String> complexPostfix) {
            this.name = name;
            this.postfix = null;
            this.complexPostfix = complexPostfix;
        }
    }

    /**
     * 词法定义
     * 
     * @author ng
     * 
     */
    private static enum Lex implements LexDefinition {
        /**
         * 空白
         */
        Space("\\s+"),

        /**
         * 整数
         */
        Integer("\\d+"),

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
         *  标识符
         */
        Identifier("[_a-zA-Z][-_0-9a-zA-Z]*"),

        /**
         * 表达式的简单后缀的开始标志
         */
        SimplePostfixDelimiter("\\."),

        /**
         * 表达式的复杂后缀的开始标志
         */
        ComplexPostfixStart("\\[\\s*"),
        /**
         * 表达式的复杂后缀其中某一项的名称与值的分隔符
         */
        ComplexPostfixNVDelimiter("\\s*\\=\\s*"),
        /**
         * 表达式的复杂后缀其中的各项之间的分隔符
         */
        ComplexPostfixDelimiter("\\s*\\,\\s*"),
        /**
         * 表达式的复杂后缀的结束标志
         */
        ComplexPostfixEnd("\\s*\\]"),

        ;

        private final Pattern regex;

        private Lex(String regex) {
            this.regex = Pattern.compile(regex);
        }

        @Override
        public Pattern regex() {
            return this.regex;
        }

        @Override
        public String token(String selectedStr) {
            if (this == StringValue)
                return StringUtils.replace(selectedStr, "\\'", "'");
            else
                return selectedStr;
        }
    }

    /**
     * 语法定义
     * 
     * @author ng
     * 
     */
    private static enum Syntax implements SyntaxDefinition<Lex> {
        Integer(lex(Lex.Integer)),
        String(
                seq(
                        lex(Lex.StringStart),
                        lex(Lex.StringValue),
                        lex(Lex.StringEnd))),

        ExprName(
                lex(Lex.Identifier)),

        ComplexPostfixField(
                seq(
                        lex(Lex.Identifier),
                        lex(Lex.ComplexPostfixNVDelimiter),
                        ref(String),
                        opt(lex(Lex.ComplexPostfixDelimiter)))),

        NoPostfix(
                nul(Lex.class)),
        SimplePostfix(
                seq(
                        lex(Lex.SimplePostfixDelimiter),
                        or(
                                lex(Lex.Identifier),
                                ref(String)))),
        ComplexPostfix(
                seq(
                        lex(Lex.ComplexPostfixStart),
                        rep(ref(ComplexPostfixField)),
                        lex(Lex.ComplexPostfixEnd))),
        Expr(
                or(
                        seq(
                                ref(ExprName),
                                or(
                                        ref(SimplePostfix),
                                        ref(ComplexPostfix),
                                        ref(NoPostfix))),
                        ref(Integer))),

        Root(
                rep(
                seq(
                        ref(Expr),
                        opt(lex(Lex.Space))))), ;

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
