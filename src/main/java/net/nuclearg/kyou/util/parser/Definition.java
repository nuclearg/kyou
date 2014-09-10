package net.nuclearg.kyou.util.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.nuclearg.kyou.util.lexer.Token;
import net.nuclearg.kyou.util.lexer.TokenDefinition;
import net.nuclearg.kyou.util.lexer.TokenString;

/**
 * 语法定义
 * 
 * @author ng
 * 
 * @param <T>
 */
class Definition<T extends Enum<T> & TokenDefinition> extends SyntaxDefinition<T> {
    private final List<SyntaxDefinition<T>> elements;

    Definition(SyntaxDefinition<T>... elements) {
        this.elements = Arrays.asList(elements);
    }

    @Override
    int matches(TokenString tokenStr) {
        int pos = tokenStr.pos();

        for (SyntaxDefinition<T> element : this.elements)
            if (element.matches(tokenStr) < 0) {
                tokenStr.pos(pos);
                return -1;
            }

        return tokenStr.pos() - pos;
    }

    @Override
    List<Token<T>> tokens(TokenString tokenStr) {
        List<Token<T>> result = new ArrayList<Token<T>>();
        for (SyntaxDefinition<T> element : this.elements)
            result.addAll(element.tokens(tokenStr));
        return result;
    }
}
