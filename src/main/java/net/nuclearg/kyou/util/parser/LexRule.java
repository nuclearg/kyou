package net.nuclearg.kyou.util.parser;

import java.util.ArrayList;
import java.util.List;

import net.nuclearg.kyou.util.lexer.LexToken;
import net.nuclearg.kyou.util.lexer.LexTokenDefinition;
import net.nuclearg.kyou.util.lexer.LexTokenString;

/**
 * 组成语法树最底层的叶子节点，对应一个词法单元
 * 
 * @author ng
 * 
 * @param <T>
 */
class LexRule<L extends LexTokenDefinition> extends SyntaxRule<L> {
    private final L tokenType;

    public LexRule(L tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    <S extends SyntaxUnitDefinition<L>> SyntaxUnit<L, S> match(LexTokenString tokenStr) {
        LexToken<L> token = tokenStr.next(this.tokenType);

        if (token == null)
            return null;

        List<LexToken<L>> tokens = new ArrayList<LexToken<L>>();
        tokens.add(token);

        return new SyntaxUnit<L, S>(null, null, tokens);
    }

    @Override
    public String toString() {
        return this.tokenType.toString();
    }
}