package com.github.nuclearg.kyou.pack;

import static com.github.nuclearg.kyou.util.parser.SyntaxRule.lex;
import static com.github.nuclearg.kyou.util.parser.SyntaxRule.nul;
import static com.github.nuclearg.kyou.util.parser.SyntaxRule.opt;
import static com.github.nuclearg.kyou.util.parser.SyntaxRule.or;
import static com.github.nuclearg.kyou.util.parser.SyntaxRule.ref;
import static com.github.nuclearg.kyou.util.parser.SyntaxRule.rep;
import static com.github.nuclearg.kyou.util.parser.SyntaxRule.seq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.github.nuclearg.kyou.util.lexer.LexDefinition;
import com.github.nuclearg.kyou.util.parser.SyntaxDefinition;
import com.github.nuclearg.kyou.util.parser.SyntaxRule;
import com.github.nuclearg.kyou.util.parser.SyntaxString;
import com.github.nuclearg.kyou.util.parser.SyntaxTreeNode;

/**
 * 表达式字符串，对应于组包样式中param的部分
 * 
 * @author ng
 * 
 */
class StyleExprString {
    /**
     * 原始的字符串
     */
    private final String str;

    StyleExprString(String str) {
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
                result.add(new ExprInfo(exprUnit.token.str, (ExprPostfixValueInfo) null));
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
                    exprInfo = new ExprInfo(name, (ExprPostfixValueInfo) null);
                    break;
                case SimplePostfix:
                    // 简单后缀
                    SyntaxTreeNode<Lex, Syntax> simplePostfixNode = postfixNode.children.get(1);
                    ExprPostfixValueInfo postfix;

                    if (simplePostfixNode.token != null) // lex(Identifier)
                        postfix = new ExprPostfixValueInfo(simplePostfixNode.token.str);
                    else if (simplePostfixNode.type == Syntax.String)
                        postfix = new ExprPostfixValueInfo(simplePostfixNode.children.get(1).token.str);
                    else
                        // RefParamPostfixValue
                        postfix = new ExprPostfixValueInfo(NumberUtils.toInt(simplePostfixNode.children.get(1).token.str));

                    exprInfo = new ExprInfo(name, postfix);
                    break;
                case ComplexPostfix:
                    // 解析复杂后缀
                    Map<String, ExprPostfixValueInfo> complexPostfixMap = new HashMap<>();

                    // size小于3表示start和end是紧挨着的，即postfix为空
                    for (SyntaxTreeNode<Lex, Syntax> postfixFieldUnit : postfixNode.children.get(1).children) {
                        if (postfixFieldUnit.type != Syntax.ComplexPostfixField)
                            continue;

                        String k = postfixFieldUnit.children.get(0).token.str;
                        ExprPostfixValueInfo v;

                        SyntaxTreeNode<Lex, Syntax> postfixFieldValueNode = postfixFieldUnit.children.get(2);
                        if (postfixFieldValueNode.type == Syntax.String)
                            v = new ExprPostfixValueInfo(postfixFieldValueNode.children.get(1).token.str);
                        else
                            v = new ExprPostfixValueInfo(NumberUtils.toInt(postfixFieldValueNode.children.get(1).token.str));

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
        final ExprPostfixValueInfo postfix;
        /**
         * 复杂后缀
         */
        final Map<String, ExprPostfixValueInfo> complexPostfix;

        ExprInfo(String name, ExprPostfixValueInfo postfix) {
            this.name = name;
            this.postfix = postfix;
            this.complexPostfix = null;
        }

        ExprInfo(String name, Map<String, ExprPostfixValueInfo> complexPostfix) {
            this.name = name;
            this.postfix = null;
            this.complexPostfix = complexPostfix;
        }
    }

    /**
     * 表达式后缀值
     * 
     * @author ng
     * 
     */
    static class ExprPostfixValueInfo {
        /**
         * 后缀项的值，如果ref不为0报错
         */
        final String value;
        /**
         * 引用的参数下标，如果为-1表示无引用，使用值
         */
        final int ref;

        private ExprPostfixValueInfo(String value) {
            this.value = value;
            this.ref = -1;
        }

        private ExprPostfixValueInfo(int ref) {
            this.value = null;
            this.ref = ref;
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

        /**
         * 一个百分号
         */
        PercentToken("%"), ;

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

        RefParamPostfixValue(
                seq(
                        lex(Lex.PercentToken),
                        ref(Integer))),

        ComplexPostfixField(
                seq(
                        lex(Lex.Identifier),
                        lex(Lex.ComplexPostfixNVDelimiter),
                        or(
                                ref(String),
                                ref(RefParamPostfixValue)
                        ),
                        opt(lex(Lex.ComplexPostfixDelimiter)))),

        NoPostfix(
                nul(Lex.class)),
        SimplePostfix(
                seq(
                        lex(Lex.SimplePostfixDelimiter),
                        or(
                                lex(Lex.Identifier),
                                ref(Integer),
                                ref(String),
                                ref(RefParamPostfixValue)))),
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
