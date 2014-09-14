package net.nuclearg.kyou.util.parser;

import java.util.ArrayList;
import java.util.List;

import net.nuclearg.kyou.util.lexer.LexDefinition;
import net.nuclearg.kyou.util.lexer.LexString;

/**
 * 可以重复0次或无数次
 * 
 * @author ng
 * 
 * @param <L>
 */
class RepeatRule<L extends LexDefinition> extends SyntaxRule<L> {
    private final SyntaxRule<L> body;

    RepeatRule(SyntaxRule<L> body) {
        this.body = body;
    }

    @Override
    <S extends SyntaxDefinition<L>> SyntaxTreeNode<L, S> tryMatch(LexString<L> tokenStr) {
        List<SyntaxTreeNode<L, S>> children = new ArrayList<SyntaxTreeNode<L, S>>();

        SyntaxTreeNode<L, S> child;

        while ((child = this.body.tryMatch(tokenStr)) != null)
            children.add(child);

        return new SyntaxTreeNode<>(null, children, null);
    }

    @Override
    public String toString() {
        return "{" + this.body + "}";
    }
}
