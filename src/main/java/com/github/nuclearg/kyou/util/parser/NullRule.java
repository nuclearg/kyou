package com.github.nuclearg.kyou.util.parser;

import com.github.nuclearg.kyou.util.lexer.LexDefinition;
import com.github.nuclearg.kyou.util.lexer.LexString;

/**
 * 表示空的语法单元
 * 
 * @author ng
 * 
 * @param <L>
 */
class NullRule<L extends LexDefinition> extends SyntaxRule<L> {

    @Override
    <S extends SyntaxDefinition<L>> SyntaxTreeNode<L, S> tryMatch(LexString<L> tokenStr) {
        return new SyntaxTreeNode<>(null, null, null);
    }

    @Override
    public String toString() {
        return "~empty~";
    }
}