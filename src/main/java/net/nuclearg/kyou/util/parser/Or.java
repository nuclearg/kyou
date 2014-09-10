package net.nuclearg.kyou.util.parser;

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
public class Or<T extends Enum<T> & TokenDefinition> implements SyntaxDefinition<T> {
    private final SyntaxDefinition<T> left;
    private final SyntaxDefinition<T> right;

    private List<Token<T>> result;

    public Or(SyntaxDefinition<T> left, SyntaxDefinition<T> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean matches(TokenString tokenStr) {
        int pos = tokenStr.pos();

        // 尝试左侧
        if (this.left.matches(tokenStr)) {
            this.result = this.left.tokens();
            return true;
        }

        // 如果左侧不行，则重置解析位置，尝试右侧
        tokenStr.pos(pos);
        if (this.right.matches(tokenStr)) {
            this.result = this.right.tokens();
            return true;
        }

        // 如果右侧也不行，则重置解析位置，解析失败
        tokenStr.pos(pos);
        this.result = null;
        return false;
    }

    @Override
    public List<Token<T>> tokens() {
        return this.result;
    }
}