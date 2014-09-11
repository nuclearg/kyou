package net.nuclearg.kyou.util.parser;

import net.nuclearg.kyou.KyouException;
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
        SyntaxUnit<L, S> result = syntax.syntax().match(this.tokenStr);

        if (!this.tokenStr.isEmpty())
            throw new KyouException("syntax error. character left. str: " + str + ", left: " + this.tokenStr + ", syntax: " + syntax);

        return result;
    }

    @Override
    public String toString() {
        return this.str;
    }
}
