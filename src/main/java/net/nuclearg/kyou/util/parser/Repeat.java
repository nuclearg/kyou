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
class Repeat<T extends Enum<T> & TokenDefinition> extends SyntaxDefinition<T> {
    private final SyntaxDefinition<T> body;

    Repeat(SyntaxDefinition<T> body) {
        this.body = body;
    }

    @Override
    int matches(TokenString tokenStr) {
        int sum = 0;
        int len;
        while ((len = this.body.matches(tokenStr)) >= 0)
            sum += len;
        return sum;
    }

    @Override
    List<Token<T>> tokens(TokenString tokenStr) {
        List<Token<T>> result = new ArrayList<Token<T>>();

        int len;
        while ((len = this.body.matches(tokenStr)) >= 0)
            result.addAll(this.body.tokens(tokenStr.backspace(len)));

        return result;
    }
}
