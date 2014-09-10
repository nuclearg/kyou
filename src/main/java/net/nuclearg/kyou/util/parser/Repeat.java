package net.nuclearg.kyou.util.parser;

import java.util.ArrayList;
import java.util.List;

import net.nuclearg.kyou.util.lexer.Token;
import net.nuclearg.kyou.util.lexer.TokenDefinition;
import net.nuclearg.kyou.util.lexer.TokenString;

/**
 * 可以重复0次或无数次
 * 
 * @author ng
 * 
 * @param <T>
 */
public class Repeat<T extends Enum<T> & TokenDefinition> implements SyntaxDefinition<T> {
    private final SyntaxDefinition<T> body;

    private List<Token<T>> result = new ArrayList<Token<T>>();

    public Repeat(SyntaxDefinition<T> body) {
        this.body = body;
    }

    @Override
    public boolean matches(TokenString tokenStr) {
        while (this.body.matches(tokenStr))
            this.result.addAll(this.body.tokens());

        return true;
    }

    @Override
    public List<Token<T>> tokens() {
        return this.result;
    }
}
