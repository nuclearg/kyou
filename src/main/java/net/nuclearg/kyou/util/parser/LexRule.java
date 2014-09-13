package net.nuclearg.kyou.util.parser;

import net.nuclearg.kyou.util.lexer.LexDefinition;
import net.nuclearg.kyou.util.lexer.LexString;
import net.nuclearg.kyou.util.lexer.LexToken;

/**
 * 对应一个词法单元的语法规则
 * 
 * @author ng
 * 
 * @param <L>
 */
class LexRule<L extends LexDefinition> extends SyntaxRule<L> {
    private final L tokenType;

    public LexRule(L tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    <S extends SyntaxDefinition<L>> SyntaxTreeNode<L, S> tryMatch(LexString<L> tokenStr) {
        LexToken<L> token = tokenStr.tryToken(this.tokenType);

        if (token == null)
            return null;

        return new SyntaxTreeNode<>(null, null, token);
    }

    @Override
    public String toString() {
        return this.tokenType.toString();
    }
}