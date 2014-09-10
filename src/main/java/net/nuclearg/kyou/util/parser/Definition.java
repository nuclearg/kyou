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
public class Definition<T extends Enum<T> & TokenDefinition> implements SyntaxDefinition<T> {
    private final List<SyntaxDefinition<T>> elements;

    private List<Token<T>> result = new ArrayList<Token<T>>();

    public Definition(SyntaxDefinition<T>... elements) {
        this.elements = Arrays.asList(elements);
    }

    @Override
    public boolean matches(TokenString tokenStr) {
        int pos = tokenStr.pos();

        for (SyntaxDefinition<T> element : this.elements)
            if (element.matches(tokenStr))
                this.result.addAll(element.tokens());
            else {
                tokenStr.pos(pos);
                return false;
            }

        return true;
    }

    @Override
    public List<Token<T>> tokens() {
        return this.result;
    }
}
