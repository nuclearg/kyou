package net.nuclearg.kyou.util.parser;

import net.nuclearg.kyou.util.lexer.LexDefinition;
import net.nuclearg.kyou.util.lexer.LexString;

/**
 * 表示空的语法单元
 * 
 * @author ng
 * 
 * @param <L>
 */
class EmptyRule<L extends LexDefinition> extends SyntaxRule<L> {

    @Override
    <S extends SyntaxDefinition<L>> SyntaxTreeNode<L, S> tryMatch(LexString<L> tokenStr) {
        return new SyntaxTreeNode<L, S>(null, null, null);
    }

    @Override
    public String toString() {
        return "~empty~";
    }
}