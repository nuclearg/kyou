package net.nuclearg.kyou.util.parser;

import java.util.ArrayList;
import java.util.List;

import net.nuclearg.kyou.util.lexer.LexTokenDefinition;
import net.nuclearg.kyou.util.lexer.LexTokenString;

/**
 * 可以重复0次或无数次
 * 
 * @author ng
 * 
 * @param <T>
 */
class RepeatRule<L extends LexTokenDefinition> extends SyntaxRule<L> {
    private final SyntaxRule<L> body;

    RepeatRule(SyntaxRule<L> body) {
        this.body = body;
    }

    @Override
    <S extends SyntaxUnitDefinition<L>> SyntaxUnit<L, S> match(LexTokenString tokenStr) {
        List<SyntaxUnit<L, S>> children = new ArrayList<SyntaxUnit<L, S>>();

        SyntaxUnit<L, S> child;

        while ((child = this.body.match(tokenStr)) != null)
            children.add(child);

        return new SyntaxUnit<L, S>(null, children, null);
    }

    @Override
    public String toString() {
        return "{" + this.body + "}";
    }
}
