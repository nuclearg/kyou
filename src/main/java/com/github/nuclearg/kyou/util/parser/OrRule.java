package com.github.nuclearg.kyou.util.parser;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.github.nuclearg.kyou.util.lexer.LexDefinition;
import com.github.nuclearg.kyou.util.lexer.LexString;

/**
 * 表示或的关系
 * 
 * @author ng
 * 
 * @param <L>
 */
class OrRule<L extends LexDefinition> extends SyntaxRule<L> {
    private final List<SyntaxRule<L>> conditions;

    @SuppressWarnings("unchecked")
    OrRule(SyntaxRule<L>... conditions) {
        this.conditions = Arrays.asList(conditions);
    }

    @Override
    <S extends SyntaxDefinition<L>> SyntaxTreeNode<L, S> tryMatch(LexString<L> tokenStr) {
        SyntaxTreeNode<L, S> result;
        for (SyntaxRule<L> condition : this.conditions)
            if ((result = condition.tryMatch(tokenStr)) != null)
                return result;

        return null;
    }

    @Override
    public String toString() {
        return "(" + StringUtils.join(this.conditions, " | ") + ")";
    }
}