package net.nuclearg.kyou.util.parser;

import net.nuclearg.kyou.util.lexer.LexTokenDefinition;
import net.nuclearg.kyou.util.lexer.LexTokenString;

/**
 * 引用另一个语法单元定义
 * 
 * @author ng
 * 
 * @param <T>
 */
class RefRule<L extends LexTokenDefinition> extends SyntaxRule<L> {
    private final SyntaxUnitDefinition<L> type;

    public RefRule(SyntaxUnitDefinition<L> type) {
        this.type = type;
    }

    @Override
    @SuppressWarnings("unchecked")
    <S extends SyntaxUnitDefinition<L>> SyntaxUnit<L, S> match(LexTokenString tokenStr) {
        SyntaxUnit<L, S> result = this.type.syntax().match(tokenStr);

        if (result == null)
            return null;

        return new SyntaxUnit<L, S>((S) this.type, result.children, result.tokens);
    }

    @Override
    public String toString() {
        return "<" + this.type + ">";
    }
}