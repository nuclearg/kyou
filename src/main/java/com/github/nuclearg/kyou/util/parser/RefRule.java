package com.github.nuclearg.kyou.util.parser;

import com.github.nuclearg.kyou.util.lexer.LexDefinition;
import com.github.nuclearg.kyou.util.lexer.LexString;

/**
 * 引用另一个语法单元定义
 * 
 * @author ng
 * 
 * @param <L>
 */
class RefRule<L extends LexDefinition> extends SyntaxRule<L> {
    private final SyntaxDefinition<L> type;

    public RefRule(SyntaxDefinition<L> type) {
        this.type = type;
    }

    @Override
    @SuppressWarnings("unchecked")
    <S extends SyntaxDefinition<L>> SyntaxTreeNode<L, S> tryMatch(LexString<L> tokenStr) {
        SyntaxTreeNode<L, S> result = this.type.syntax().tryMatch(tokenStr);

        if (result == null)
            return null;

        return new SyntaxTreeNode<>((S) this.type, result.children, result.token);
    }

    @Override
    public String toString() {
        return "<" + this.type + ">";
    }
}