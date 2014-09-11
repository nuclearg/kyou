package net.nuclearg.kyou.util.parser;

import java.util.Arrays;
import java.util.List;

import net.nuclearg.kyou.util.lexer.LexTokenDefinition;
import net.nuclearg.kyou.util.lexer.LexTokenString;

import org.apache.commons.lang.StringUtils;

/**
 * 表示或的关系
 * 
 * @author ng
 * 
 * @param <T>
 */
class OrRule<L extends LexTokenDefinition> extends SyntaxRule<L> {
    private final List<SyntaxRule<L>> conditions;

    OrRule(SyntaxRule<L>... conditions) {
        this.conditions = Arrays.asList(conditions);
    }

    @Override
    <S extends SyntaxUnitDefinition<L>> SyntaxUnit<L, S> match(LexTokenString tokenStr) {
        SyntaxUnit<L, S> result;
        for (SyntaxRule<L> condition : this.conditions)
            if ((result = condition.match(tokenStr)) != null)
                return result;

        return null;
    }

    @Override
    public String toString() {
        return StringUtils.join(this.conditions, " | ");
    }
}