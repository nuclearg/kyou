package net.nuclearg.kyou.util.lexer;

/**
 * 词法元素
 * 
 * @author ng
 * 
 * @param <T>
 */
public class LexToken<T extends LexTokenDefinition> {
    /**
     * 词法元素的类型，对应于{@link LexTokenDefinition}中由用户指定的类型
     */
    public final T type;
    /**
     * 词法元素对应的字符串
     */
    public final String str;

    LexToken(T type, String str) {
        this.type = type;
        this.str = str;
    }

    @Override
    public String toString() {
        return "[\"" + this.str + "\": " + this.type + "] ";
    }
}