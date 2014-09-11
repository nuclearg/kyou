package net.nuclearg.kyou.util.parser;

import java.util.List;

import net.nuclearg.kyou.util.lexer.LexToken;
import net.nuclearg.kyou.util.lexer.LexTokenDefinition;

import org.apache.commons.lang.SystemUtils;

public class SyntaxUnit<L extends LexTokenDefinition, S extends SyntaxUnitDefinition<L>> {
    public final S type;
    public final List<SyntaxUnit<L, S>> children;
    public final List<LexToken<L>> tokens;

    SyntaxUnit(S type, List<SyntaxUnit<L, S>> children, List<LexToken<L>> tokens) {
        this.type = type;
        this.children = children;
        this.tokens = tokens;
    }

    @Override
    public String toString() {
        return this.toString("");
    }

    private String toString(String prefix) {
        StringBuilder builder = new StringBuilder();
        builder.append(prefix);
        builder.append("+");
        builder.append(this.type == null ? "" : this.type);

        if (this.children != null)
            for (SyntaxUnit<L, S> child : this.children)
                builder.append(SystemUtils.LINE_SEPARATOR).append(child.toString(prefix + "  "));

        if (this.tokens != null)
            for (LexToken<L> token : this.tokens)
                builder.append(" ").append(token);

        return builder.toString();
    }
}
