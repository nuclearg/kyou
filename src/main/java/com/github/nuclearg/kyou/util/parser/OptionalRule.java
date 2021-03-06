package com.github.nuclearg.kyou.util.parser;

import com.github.nuclearg.kyou.util.lexer.LexDefinition;
import com.github.nuclearg.kyou.util.lexer.LexString;

/**
 * 表示可选项
 * 
 * @author ng
 * 
 * @param <L>
 */
class OptionalRule<L extends LexDefinition> extends SyntaxRule<L> {
    private final SyntaxRule<L> body;

    OptionalRule(SyntaxRule<L> body) {
        this.body = body;
    }

    @Override
    <S extends SyntaxDefinition<L>> SyntaxTreeNode<L, S> tryMatch(LexString<L> tokenStr) {
        SyntaxTreeNode<L, S> result = this.body.tryMatch(tokenStr);

        if (result != null)
            return result;
        else
            return new SyntaxTreeNode<>(null, null, null);
    }

    @Override
    public String toString() {
        return "[" + this.body + "]";
    }
}
