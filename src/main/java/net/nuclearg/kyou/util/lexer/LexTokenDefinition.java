package net.nuclearg.kyou.util.lexer;

import java.util.regex.Pattern;

/**
 * 词法元素定义，实现这个接口的应当是一个枚举
 * 
 * @author ng
 * 
 */
public interface LexTokenDefinition {
    /**
     * 该词法元素对应的正则表达式
     */
    public Pattern regex();
}