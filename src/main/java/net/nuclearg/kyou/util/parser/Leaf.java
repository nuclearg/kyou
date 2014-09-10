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
public class Leaf<T extends Enum<T> & TokenDefinition> implements SyntaxDefinition<T> {
    private final T type;
    private Token<T> result;

    public Leaf(T type) {
        this.type = type;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean matches(TokenString tokenStr) {
        this.result = tokenStr.next(this.type);
        return this.result != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Token<T>> tokens() {
        return Arrays.asList(this.result);
    }
}