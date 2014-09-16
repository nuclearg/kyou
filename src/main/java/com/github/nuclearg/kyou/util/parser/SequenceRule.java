package com.github.nuclearg.kyou.util.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.github.nuclearg.kyou.util.lexer.LexDefinition;
import com.github.nuclearg.kyou.util.lexer.LexString;

/**
 * 表示顺序出现
 * 
 * @author ng
 * 
 * @param <L>
 */
class SequenceRule<L extends LexDefinition> extends SyntaxRule<L> {
    private final List<SyntaxRule<L>> elements;

    @SuppressWarnings("unchecked")
    SequenceRule(SyntaxRule<L>... elements) {
        this.elements = Arrays.asList(elements);
    }

    @Override
    <S extends SyntaxDefinition<L>> SyntaxTreeNode<L, S> tryMatch(LexString<L> tokenStr) {
        int pos = tokenStr.pos();

        List<SyntaxTreeNode<L, S>> children = new ArrayList<>();

        SyntaxTreeNode<L, S> child;
        for (SyntaxRule<L> element : this.elements)
            if ((child = element.tryMatch(tokenStr)) != null)
                children.add(child);
            else {
                tokenStr.pos(pos);
                return null;
            }

        return new SyntaxTreeNode<>(null, children, null);
    }

    @Override
    public String toString() {
        return StringUtils.join(this.elements.toArray(), " ");
    }

}
