package net.nuclearg.kyou.pack;

import static net.nuclearg.kyou.util.parser.SyntaxRule.empty;
import static net.nuclearg.kyou.util.parser.SyntaxRule.lex;
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

/**
 * 表达式字符串，对应于组包样式中param的部分
 * 
 * @author ng
 * 
 */
class ExprListString {
    /**
     * 原始的字符串
     */
    private final String str;

    ExprListString(String str) {
        this.str = str;
    }

    /**
     * 将字符串解析为一系列的表达式信息。如果解析失败则抛异常
     * 
     * @return 解析出来的表达式信息列表
     */
    List<ExprInfo> parseExprInfo() {
        SyntaxString<Lex, Syntax> syntaxStr = new SyntaxString<Lex, Syntax>(this.str);
        SyntaxTreeNode<Lex, Syntax> root = syntaxStr.tryParse(Syntax.Root);

        List<ExprInfo> result = new ArrayList<ExprInfo>();

        // 遍历语法树，解析其中的每个expr
        for (SyntaxTreeNode<Lex, Syntax> exprUnit : root.children) {
            exprUnit = exprUnit.children.get(0);

            // 解析name
            SyntaxTreeNode<Lex, Syntax> nameUnit = exprUnit.children.get(0);
            String name = nameUnit.token.str;

            // 解析postfix
            SyntaxTreeNode<Lex, Syntax> postfixUnit = exprUnit.children.get(1);
            if (postfixUnit.type == null) {
                // 无后缀
                result.add(new ExprInfo(name, (String) null));
                continue;
            }

            if (postfixUnit.type == Syntax.SimplePostfix) {
                // 解析简单后缀
                String postfix = postfixUnit.children.get(1).token.str;

                result.add(new ExprInfo(name, postfix));
            } else {
                // 解析复杂后缀
                Map<String, String> complexPostfixMap = new HashMap<String, String>();

                // size小于3表示start和end是紧挨着的，即postfix为空
                if (postfixUnit.children.size() < 3) {
                    result.add(new ExprInfo(name, complexPostfixMap));
                    continue;
                }

                for (SyntaxTreeNode<Lex, Syntax> postfixFieldUnit : postfixUnit.children.get(1).children) {
                    String k = postfixFieldUnit.children.get(1).token.str;
                    String v = postfixFieldUnit.children.get(6).token.str;

                    complexPostfixMap.put(k, v);
                }

                result.add(new ExprInfo(name, complexPostfixMap));
            }
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

        ExprInfo(String body, String postfix) {
            this.name = body;
            this.postfix = postfix;
            this.complexPostfix = null;
        }

        ExprInfo(String body, Map<String, String> complexPostfix) {
            this.name = body;
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
         * 表达式名称
         */
        Name("[0-9a-z]+|\\d+"),

        /**
         * 表达式的简单后缀的开始标志
         */
        SimplePostfixDelimiter("\\."),
        /**
         * 表达式的简单后缀
         */
        SimplePostfix("\\w+"),

        /**
         * 表达式的复杂后缀的开始标志
         */
        ComplexPostfixStart("\\["),
        /**
         * 表达式的复杂后缀其中某一项的名称
         */
        ComplexPostfixName("[0-9a-zA-Z]+"),
        /**
         * 表达式的复杂后缀其中某一项的名称与值的分隔符
         */
        ComplexPostfixNVDelimiter("\\="),
        /**
         * 表达式的复杂后缀其中某一项的值的开始标志
         */
        ComplexPostfixValueStart("\\'"),
        /**
         * 表达式的复杂后缀其中某一项的值
         */
        ComplexPostfixValue("(\\w|\\s)*"), // TODO 这个正则不正确
        /**
         * 表达式的复杂后缀其中某一项的结束标志
         */
        ComplexPostfixValueEnd("\\'"),
        /**
         * 表达式的复杂后缀其中的各项之间的分隔符
         */
        ComplexPostfixDelimiter(","),
        /**
         * 表达式的复杂后缀的结束标志
         */
        ComplexPostfixEnd("\\]"),

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

    /**
     * 语法定义
     * 
     * @author ng
     * 
     */
    @SuppressWarnings("unchecked")
    private static enum Syntax implements SyntaxDefinition<Lex> {
        ExprName(
                lex(Lex.Name)),
        SimplePostfix(
                seq(
                        lex(Lex.SimplePostfixDelimiter),
                        lex(Lex.SimplePostfix))),
        ComplexPostfixField(
                seq(
                        opt(lex(Lex.Space)),
                        lex(Lex.ComplexPostfixName),
                        opt(lex(Lex.Space)),
                        lex(Lex.ComplexPostfixNVDelimiter),
                        opt(lex(Lex.Space)),
                        lex(Lex.ComplexPostfixValueStart),
                        lex(Lex.ComplexPostfixValue),
                        lex(Lex.ComplexPostfixValueEnd),
                        opt(lex(Lex.Space)),
                        opt(lex(Lex.ComplexPostfixDelimiter)),
                        opt(lex(Lex.Space)))),
        ComplexPostfix(
                seq(
                        lex(Lex.ComplexPostfixStart),
                        rep(ref(ComplexPostfixField)),
                        lex(Lex.ComplexPostfixEnd))),

        Expr(
                seq(
                        ref(ExprName),
                        or(
                                ref(SimplePostfix),
                                ref(ComplexPostfix),
                                empty(Lex.class)))),

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
