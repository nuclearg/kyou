package net.nuclearg.kyou.util.parser;

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
public class Optional<T extends Enum<T> & TokenDefinition> implements SyntaxDefinition<T> {
    private final SyntaxDefinition<T> body;
    private List<Token<T>> result;

    public Optional(SyntaxDefinition<T> body) {
        this.body = body;
    }

    @Override
    public boolean matches(TokenString tokenStr) {
        if (this.body.matches(tokenStr))
            this.result = this.body.tokens();

        return true;
    }

    @Override
    public List<Token<T>> tokens() {
        return this.result;
    }
}
