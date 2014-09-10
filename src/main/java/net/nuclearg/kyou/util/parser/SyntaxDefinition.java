package net.nuclearg.kyou.util.parser;

import java.util.List;

import net.nuclearg.kyou.util.lexer.Token;
import net.nuclearg.kyou.util.lexer.TokenDefinition;
import net.nuclearg.kyou.util.lexer.TokenString;

interface SyntaxDefinition<T extends Enum<T> & TokenDefinition> {
    public boolean matches(TokenString tokenStr);

    public List<Token<T>> tokens();
}
