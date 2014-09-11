package net.nuclearg.kyou.util.lexer;

import java.util.regex.Matcher;

import net.nuclearg.kyou.KyouException;

/**
 * 支持词法解析的字符串，可以按词法元素获取下一个词
 * 
 * @author ng
 * 
 */
public class LexTokenString {
    /**
     * 将被解析词法的字符串
     */
    private final String str;
    /**
     * 解析词法元素的当前位置
     */
    private int pos;

    public LexTokenString(String str) {
        if (str == null)
            throw new KyouException("string to parse token is null");

        this.str = str;
    }

    /**
     * 获取由{@link LexTokenDefinition}描述的下一个{@link LexToken}
     * 
     * @param definition
     *            词法定义，如果字符串的当前位置满足该词法定义则返回该词法元素
     * @return 匹配出来的词法元素，如果无法匹配任何一个给定的词法元素定义则返回null
     */
    public <T extends LexTokenDefinition> LexToken<T> next(T definition) {
        Matcher m = definition.regex().matcher(this.str);

        if (m.find(this.pos) && m.start() == pos) {
            pos += m.end() - m.start();

            String str = this.str.substring(m.start(), m.end());
            return new LexToken<T>(definition, str);
        }

        return null;
    }

    /**
     * 获取由{@link LexTokenDefinition}描述的下一个{@link LexToken}
     * 
     * @param definitions
     *            词法定义的列表，如果字符串的当前位置满足任何一个词法定义则将返回该词法元素
     * @return 匹配出来的词法元素，如果无法匹配任何一个给定的词法元素定义则返回null
     */
    public <T extends LexTokenDefinition> LexToken<T> next(T... definitions) {
        LexToken<T> token;
        for (T definition : definitions)
            if ((token = this.next(definition)) != null)
                return token;

        return null;
    }

    /**
     * 判断字符串是否为空，即当前是否仍有剩余的字符用于匹配词法元素
     * 
     * @return 是否为空
     */
    public boolean isEmpty() {
        return this.pos >= this.str.length();
    }

    /**
     * 获取当前解析到的位置
     * 
     * @return 当前解析到的位置
     */
    public int pos() {
        return this.pos;
    }

    /**
     * 设置当前解析到的位置
     * 
     * @param pos
     *            新的位置
     * @return this
     */
    public LexTokenString pos(int pos) {
        this.pos = pos;
        return this;
    }

    /**
     * 将当前解析位置回退指定的字符数
     * 
     * @param bk
     *            回退的字符数
     * @return this
     */
    public LexTokenString backspace(int bk) {
        this.pos -= bk;
        return this;
    }

    @Override
    public String toString() {
        return this.str.substring(this.pos);
    }

}
