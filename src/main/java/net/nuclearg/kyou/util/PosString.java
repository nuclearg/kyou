package net.nuclearg.kyou.util;

import net.nuclearg.kyou.KyouException;

import org.apache.commons.lang.ArrayUtils;

/**
 * 向普通的字符串上加一个pos，主要用于各种解析过程
 * 
 * @author ng
 */
class PosString {
    /**
     * 字符数组
     */
    private final char[] chars;
    /**
     * 当前位置
     */
    private int pos;

    public PosString(String str) {
        if (str == null)
            throw new KyouException("str is null");

        this.chars = str.toCharArray();
    }

    /**
     * 从{@link PosString}中获取当前位置的字符，并将pos向前推进一格
     * 
     * @return 当前位置的字符
     */
    public char get() {
        return this.chars[pos++];
    }

    /**
     * 向{@link PosString}中推回一个位置的字符
     */
    public void pushback() {
        this.pos--;
        if (this.pos < 0)
            throw new IndexOutOfBoundsException(String.valueOf(this.pos));
    }

    /**
     * 获取当前位置
     * 
     * @return 当前位置
     */
    public int pos() {
        return this.pos;
    }

    /**
     * 设置当前位置
     * 
     * @param pos
     *            新位置
     */
    public void pos(int pos) {
        this.pos = pos;
    }

    /**
     * 获取总长度
     * 
     * @return 总长度
     */
    public int length() {
        return this.chars.length;
    }

    /**
     * 判断该{@link PosString}的pos是否小于length
     * <p>
     * 这通常表示是否还可以进行get()操作
     * </p>
     * 
     * @return 该{@link PosString}的pos是否小于length
     */
    public boolean hasRemaining() {
        return this.pos < this.chars.length;
    }

    /**
     * 判断{@link PosString}的pos位置处的字符是不是给定字符中的一个，如果是则取出该字符
     * 
     * @param options
     *            所有可能的字符列表
     * @return 找到的字符，或为null，表示没有找到给定的字符
     */
    public Character attempt(char... options) {
        if (!this.hasRemaining())
            return null;

        // 看当前位置的字符是不是在给定的字符列表中
        if (ArrayUtils.contains(options, this.chars[this.pos]))
            return this.get();
        else
            return null;
    }
}
