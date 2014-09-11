package net.nuclearg.kyou.util.parser;

import net.nuclearg.kyou.util.lexer.LexTokenDefinition;
import net.nuclearg.kyou.util.lexer.LexTokenString;

/**
 * 表示可选项
 * 
 * @author ng
 * 
 * @param <T>
 */
class OptionalRule<L extends LexTokenDefinition> extends SyntaxRule<L> {
    private final SyntaxRule<L> body;

    OptionalRule(SyntaxRule<L> body) {
        this.body = body;
    }

    @Override
    <S extends SyntaxUnitDefinition<L>> SyntaxUnit<L, S> match(LexTokenString tokenStr) {
        SyntaxUnit<L, S> result = this.body.match(tokenStr);

        if (result != null)
            return result;
        else
            return new SyntaxUnit<L, S>(null, null, null);
    }

    @Override
    public String toString() {
        return "[" + this.body + "]";
    }
}
