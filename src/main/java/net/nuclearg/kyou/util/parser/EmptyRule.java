package net.nuclearg.kyou.util.parser;

import net.nuclearg.kyou.util.lexer.LexTokenDefinition;
import net.nuclearg.kyou.util.lexer.LexTokenString;

/**
 * 表示空的语法单元
 * 
 * @author ng
 * 
 * @param <T>
 */
class EmptyRule<L extends LexTokenDefinition> extends SyntaxRule<L> {

    @Override
    <S extends SyntaxUnitDefinition<L>> SyntaxUnit<L, S> match(LexTokenString tokenStr) {
        return new SyntaxUnit<L, S>(null, null, null);
    }

    @Override
    public String toString() {
        return "~empty~";
    }
}