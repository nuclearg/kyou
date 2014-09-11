package net.nuclearg.kyou.util.parser;

import net.nuclearg.kyou.util.lexer.LexTokenDefinition;

/**
 * 语法定义
 * 
 * @author ng
 * 
 * @param <T>
 */
public interface SyntaxUnitDefinition<L extends LexTokenDefinition> {
    /**
     * 语法定义
     */
    public SyntaxRule<L> syntax();
}
