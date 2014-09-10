package net.nuclearg.kyou.util.parser;

import java.util.Arrays;
import java.util.List;

import net.nuclearg.kyou.util.lexer.Token;
import net.nuclearg.kyou.util.lexer.TokenDefinition;
import net.nuclearg.kyou.util.lexer.TokenString;

/**
 * 组成语法树最底层的叶子节点，对应一个词法单元
 * 
 * @author ng
 * 
 * @param <T>
 */
class Leaf<T extends Enum<T> & TokenDefinition> extends SyntaxDefinition<T> {
    private final T type;

    public Leaf(T type) {
        this.type = type;
    }

    @Override
    @SuppressWarnings("unchecked")
    int matches(TokenString tokenStr) {
        Token<T> token = tokenStr.next(this.type);

        if (token == null)
            return -1;
        else
            return token.str.length();
    }

    @Override
    @SuppressWarnings("unchecked")
    List<Token<T>> tokens(TokenString tokenStr) {
        Token<T> token = tokenStr.next(this.type);
        return Arrays.asList(token);
    }

}