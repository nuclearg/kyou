package net.nuclearg.kyou.util.parser;

import java.util.Collections;
import java.util.List;

import net.nuclearg.kyou.util.lexer.Token;
import net.nuclearg.kyou.util.lexer.TokenDefinition;
import net.nuclearg.kyou.util.lexer.TokenString;

/**
 * 表示可选项
 * 
 * @author ng
 * 
 * @param <T>
 */
class Optional<T extends Enum<T> & TokenDefinition> extends SyntaxDefinition<T> {
    private final SyntaxDefinition<T> body;

    Optional(SyntaxDefinition<T> body) {
        this.body = body;
    }

    @Override
    int matches(TokenString tokenStr) {
        return this.body.matches(tokenStr);
    }

    @Override
    List<Token<T>> tokens(TokenString tokenStr) {
        int len;
        if ((len = this.body.matches(tokenStr)) >= 0)
            return this.body.tokens(tokenStr.backspace(len));
        else
            return Collections.emptyList();
    }
}
