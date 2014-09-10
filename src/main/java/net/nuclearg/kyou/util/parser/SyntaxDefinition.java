package net.nuclearg.kyou.util.parser;

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
public abstract class SyntaxDefinition<T extends Enum<T> & TokenDefinition> {
    /**
     * 
     * @param tokenStr
     * @return
     */
    abstract int matches(TokenString tokenStr);

    /**
     * 
     * @param tokenStr
     * @return
     */
    abstract List<Token<T>> tokens(TokenString tokenStr);

    /**
     * 
     * @param elements
     * @return
     */
    public static <T extends Enum<T> & TokenDefinition> SyntaxDefinition<T> define(SyntaxDefinition<T>... elements) {
        return new Define<T>(elements);
    }

    /**
     * 
     * @param tokenType
     * @return
     */
    public static <T extends Enum<T> & TokenDefinition> SyntaxDefinition<T> leaf(T tokenType) {
        return new Leaf<T>(tokenType);
    }

    /**
     * 
     * @param body
     * @return
     */
    public static <T extends Enum<T> & TokenDefinition> SyntaxDefinition<T> optional(SyntaxDefinition<T> body) {
        return new Optional<T>(body);
    }

    /**
     * 
     * @param conditions
     * @return
     */
    public static <T extends Enum<T> & TokenDefinition> SyntaxDefinition<T> or(SyntaxDefinition<T>... conditions) {
        return new Or<T>(conditions);
    }

    /**
     * 
     * @param body
     * @return
     */
    public static <T extends Enum<T> & TokenDefinition> SyntaxDefinition<T> repeat(SyntaxDefinition<T> body) {
        return new Repeat<T>(body);
    }
}
