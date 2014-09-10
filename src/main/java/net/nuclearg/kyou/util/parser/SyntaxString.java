package net.nuclearg.kyou.util.parser;

import java.util.List;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.util.lexer.Token;
import net.nuclearg.kyou.util.lexer.TokenDefinition;
import net.nuclearg.kyou.util.lexer.TokenString;

public class SyntaxString<T extends Enum<T> & TokenDefinition> {
    private final String str;

    public SyntaxString(String str) {
        this.str = str;
    }

    public List<Token<T>> parse(SyntaxDefinition<T> syntax) {
        TokenString tokenStr = new TokenString(str);
        if (syntax.matches(tokenStr))
            return syntax.tokens();

        throw new KyouException("syntax parse fail. text: " + this.str);
    }
}
