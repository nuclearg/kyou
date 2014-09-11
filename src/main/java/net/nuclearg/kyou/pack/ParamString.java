package net.nuclearg.kyou.pack;

import static net.nuclearg.kyou.util.parser.SyntaxRule.empty;
import static net.nuclearg.kyou.util.parser.SyntaxRule.lex;
import static net.nuclearg.kyou.util.parser.SyntaxRule.optional;
import static net.nuclearg.kyou.util.parser.SyntaxRule.or;
import static net.nuclearg.kyou.util.parser.SyntaxRule.ref;
import static net.nuclearg.kyou.util.parser.SyntaxRule.repeat;
import static net.nuclearg.kyou.util.parser.SyntaxRule.seq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.nuclearg.kyou.util.lexer.LexTokenDefinition;
import net.nuclearg.kyou.util.parser.SyntaxRule;
import net.nuclearg.kyou.util.parser.SyntaxString;
import net.nuclearg.kyou.util.parser.SyntaxUnit;
import net.nuclearg.kyou.util.parser.SyntaxUnitDefinition;

class ParamString {
    private final String str;

    ParamString(String str) {
        this.str = str;
    }

    List<ExprInfo> parseExprInfo() {
        SyntaxString<Lex, Syntax> syntaxStr = new SyntaxString<Lex, Syntax>(this.str);
        SyntaxUnit<Lex, Syntax> root = syntaxStr.parse(Syntax.ExprList);

        List<ExprInfo> result = new ArrayList<ExprInfo>();

        // 遍历exprlist，解析其中的每个expr
        for (SyntaxUnit<Lex, Syntax> exprUnit : root.children) {
            exprUnit = exprUnit.children.get(0);

            // 解析body
            SyntaxUnit<Lex, Syntax> bodyUnit = exprUnit.children.get(0);
            String body = bodyUnit.tokens.get(0).str;

            // 解析postfix
            SyntaxUnit<Lex, Syntax> postfixUnit = exprUnit.children.get(1);
            if (postfixUnit.type == null) {
                // 无后缀
                result.add(new ExprInfo(body, (String) null));
                continue;
            }

            postfixUnit = postfixUnit.children.get(0);
            if (postfixUnit.type == Syntax.SimplePostfix) {
                // 解析简单后缀
                String postfix = postfixUnit.tokens.get(0).str;

                result.add(new ExprInfo(body, postfix));
            } else {
                // 解析复杂后缀
                Map<String, String> complexPostfixMap = new HashMap<String, String>();
                for (SyntaxUnit<Lex, Syntax> postfixFieldUnit : exprUnit.children) {
                    if (postfixFieldUnit.type != Syntax.ComplexPostfixField)
                        continue;

                    String k = postfixFieldUnit.tokens.get(0).str;
                    String v = postfixFieldUnit.tokens.get(3).str;

                    complexPostfixMap.put(k, v);
                }

                result.add(new ExprInfo(body, complexPostfixMap));
            }
        }

        return result;
    }

    @Override
    public String toString() {
        return this.str;
    }

    private static enum Lex implements LexTokenDefinition {
        Space("\\s+"),
        Body("[0-9a-z]+|\\d+"),
        PostfixDelimiter("\\."),
        SimplePostfix("\\w+"),
        ComplexPostfixStart("\\["),
        ComplexPostfixName("[a-zA-Z]+"),
        ComplexPostfixNVDelimiter("\\["),
        ComplexPostfixValueStart("\\'"),
        ComplexPostfixValue("(\\w|\\\\\\')+"), // 所有字母、数字、下划线、汉字，或\'
        ComplexPostfixValueEnd("\\'"),
        ComplexPostfixDelimiter(","),
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

    @SuppressWarnings("unchecked")
    private static enum Syntax implements SyntaxUnitDefinition<Lex> {
        ExprBody(lex(Lex.Body)),
        SimplePostfix(lex(Lex.SimplePostfix)),
        ComplexPostfixField(
                seq(
                        lex(Lex.ComplexPostfixName),
                        lex(Lex.ComplexPostfixNVDelimiter),
                        lex(Lex.ComplexPostfixValueStart),
                        lex(Lex.ComplexPostfixValue),
                        lex(Lex.ComplexPostfixValueEnd),
                        optional(lex(Lex.ComplexPostfixDelimiter)),
                        optional(lex(Lex.Space)))),
        ComplexPostfix(
                seq(
                        lex(Lex.ComplexPostfixStart),
                        repeat(ref(ComplexPostfixField)),
                        lex(Lex.ComplexPostfixEnd))),

        Expr(
                seq(
                        ref(ExprBody),
                        or(
                                seq(
                                        lex(Lex.PostfixDelimiter),
                                        or(
                                                ref(ComplexPostfix),
                                                ref(SimplePostfix))),
                                empty(Lex.class)))),

        ExprList(
                repeat(
                seq(
                        ref(Expr),
                        optional(lex(Lex.Space))))), ;

        private final SyntaxRule<Lex> syntax;

        private Syntax(SyntaxRule<Lex> syntax) {
            this.syntax = syntax;
        }

        @Override
        public SyntaxRule<Lex> syntax() {
            return this.syntax;
        }
    }

    static class ExprInfo {
        final String body;
        final String postfix;
        final Map<String, String> complexPostfix;

        ExprInfo(String body, String postfix) {
            this.body = body;
            this.postfix = postfix;
            this.complexPostfix = null;
        }

        ExprInfo(String body, Map<String, String> complexPostfix) {
            this.body = body;
            this.postfix = null;
            this.complexPostfix = complexPostfix;
        }
    }
}
