package net.nuclearg.kyou.util.parser;

import net.nuclearg.kyou.util.lexer.LexTokenDefinition;
import net.nuclearg.kyou.util.lexer.LexTokenString;

public class SyntaxString<L extends LexTokenDefinition, S extends SyntaxUnitDefinition<L>> {
    private final String str;
    private final LexTokenString tokenStr;

    public SyntaxString(String str) {
        this.str = str;
        this.tokenStr = new LexTokenString(str);
    }

    public SyntaxUnit<L, S> parse(S syntax) {
        return syntax.syntax().match(this.tokenStr);
    }

    @Override
    public String toString() {
        return this.str;
    }
}
