package net.nuclearg.kyou.util.parser;

import java.util.Arrays;
import java.util.List;

import net.nuclearg.kyou.util.lexer.Token;
import net.nuclearg.kyou.util.lexer.TokenDefinition;
import net.nuclearg.kyou.util.lexer.TokenString;

/**
 * 表示或的关系
 * 
 * @author ng
 * 
 */
class Or<T extends Enum<T> & TokenDefinition> extends SyntaxDefinition<T> {
    private final List<SyntaxDefinition<T>> conditions;

    Or(SyntaxDefinition<T>... conditions) {
        this.conditions = Arrays.asList(conditions);
    }

    @Override
    int matches(TokenString tokenStr) {
        int pos = tokenStr.pos();

        int len;
        for (SyntaxDefinition<T> condition : this.conditions)
            if ((len = condition.matches(tokenStr)) >= 0)
                return len;
            else
                tokenStr.pos(pos);

        return -1;
    }

    @Override
    List<Token<T>> tokens(TokenString tokenStr) {
        for (SyntaxDefinition<T> condition : this.conditions) {
            int pos = tokenStr.pos();

            int len;
            if ((len = condition.matches(tokenStr)) >= 0)
                return condition.tokens(tokenStr.backspace(len));
            else
                tokenStr.pos(pos);
        }

        throw new UnsupportedOperationException();
    }
}