package net.nuclearg.kyou.util.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.nuclearg.kyou.util.lexer.LexTokenDefinition;
import net.nuclearg.kyou.util.lexer.LexTokenString;

import org.apache.commons.lang.StringUtils;

/**
 * 语法定义
 * 
 * @author ng
 * 
 * @param <T>
 */
class SequenceRule<L extends LexTokenDefinition> extends SyntaxRule<L> {
    private final List<SyntaxRule<L>> elements;

    SequenceRule(SyntaxRule<L>... elements) {
        this.elements = Arrays.asList(elements);
    }

    @Override
    <S extends SyntaxUnitDefinition<L>> SyntaxUnit<L, S> match(LexTokenString tokenStr) {
        int pos = tokenStr.pos();

        List<SyntaxUnit<L, S>> children = new ArrayList<SyntaxUnit<L, S>>();

        SyntaxUnit<L, S> child;
        for (SyntaxRule<L> element : elements)
            if ((child = element.match(tokenStr)) != null)
                children.add(child);
            else {
                tokenStr.pos(pos);
                return null;
            }

        return new SyntaxUnit<L, S>(null, children, null);
    }

    @Override
    public String toString() {
        return StringUtils.join(this.elements, " ");
    }

}
