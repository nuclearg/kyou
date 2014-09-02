package net.nuclearg.kyou.util;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.nuclearg.kyou.KyouException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 格式字符串
 * <p>
 * 格式字符串是kyou自定义的方式，用来表示组包格式。其中使用\作为转义符，支持字节的16进制表示。
 * <li>%表示参数</li>
 * <li>\%表示%字符</li>
 * <li>\r表示回车</li>
 * <li>\n表示换行</li>
 * <li>\xx表示值为xx的字节</li>
 * <li>\\表示\本身</li>
 * </p>
 * <p>
 * 例：
 * <li>asdf - a s d f</li>
 * <li>as\00df - a s \0 d f</li>
 * <li>as\\df - a s \ d f</li>
 * <li>as%db - a s 参数 d f</li>
 * <li>as\%df - a s % d f</li>
 * </p>
 * 
 * @author ng
 * 
 */
public class FormatString implements Iterable<byte[]> {
    private static final Logger logger = Logger.getLogger(FormatString.class);

    /**
     * 原始字符串形式
     */
    private final String formatStr;
    /**
     * 段列表
     * <p>
     * 其中文本段存储为其字面量，参数段存储为null
     * </p>
     */
    private final List<byte[]> segments;

    /**
     * 构造一个格式字符串
     * 
     * @param formatStr
     *            格式字符串
     * @param encoding
     *            编码
     */
    public FormatString(String formatStr, Charset encoding) {
        logger.debug("parse kyou format string, str=" + formatStr + ", encoding=" + encoding);

        if (StringUtils.isEmpty(formatStr))
            throw new KyouException("str is empty");
        if (encoding == null)
            throw new KyouException("encoding is null");

        this.formatStr = formatStr;

        List<byte[]> segments = new LinkedList<byte[]>();
        PosString s = new PosString(formatStr);

        // 解析FormatString
        try {
            while (s.hasRemaining())
                segments.add(this.parseSegment(s, encoding));

            logger.debug("parse kyou format string finished. size: " + segments.size() + ", contents: " + segments);
        } catch (Exception ex) {
            throw new KyouException("parse format string fail. str: " + formatStr, ex);
        }

        this.segments = Collections.unmodifiableList(segments);
    }

    /**
     * 遍历每个段，文本段返回对应的字节数组，参数段返回null
     */
    @Override
    public Iterator<byte[]> iterator() {
        return this.segments.iterator();
    }

    @Override
    public String toString() {
        return this.formatStr;
    }

    /**
     * 从KyouString的头部解析出一个段
     * 
     * @param formatStr
     *            格式字符串
     * @param encoding
     *            编码
     * @return 解析出来的段，如果是文本段则返回该段的字面量，如果是参数段则返回null
     */
    private byte[] parseSegment(PosString formatStr, Charset encoding) {
        // 首先判断首字母是不是%，这表示一个参数段
        if (formatStr.attempt('%') != null)
            // 如果是参数段，则按照约定返回一个null
            return null;

        ByteOutputStream os = new ByteOutputStream();
        StringBuilder buffer = new StringBuilder();

        try {
            while (formatStr.hasRemaining()) {
                // 取下一个字符
                char c = formatStr.get();

                // 判断是不是转义符
                if (c == '\\') {
                    // 如果是转义符则先把buffer中的东西写到流中
                    if (buffer.length() != 0) {
                        os.write(buffer.toString().getBytes(encoding));
                        buffer = new StringBuilder();
                    }
                    // 解析这个转义符
                    byte b = this.parseEscape(formatStr, encoding);
                    // 写到流中
                    os.write(b);

                    // 继续解析后面的字符
                    continue;
                }

                // 判断是不是参数
                if (c == '%') {
                    // 如果是参数，则先把已经取出来的这个'%'字符再退回去
                    formatStr.pushback();
                    // 中断解析过程，把已经解析出来的部分作为一个文本段返回去
                    break;
                }

                // 不是这两种情况，就是普通的字符，则加到buffer中
                buffer.append(c);
            }

            // 解析完毕，如果buffer中有东西，则加到流中
            if (buffer.length() != 0)
                os.write(buffer.toString().getBytes(encoding));

            return os.export();
        } catch (Exception ex) {
            throw new KyouException("format syntax error. str: " + formatStr, ex);
        } finally {
            os.close();
        }
    }

    /**
     * 从KyouString的头部解析一个转义符
     * 
     * @param formatStr
     *            格式字符串
     * @param encoding
     *            编码
     * @return 转义符表示的字节
     */
    private byte parseEscape(PosString formatStr, Charset encoding) {
        if (!formatStr.hasRemaining())
            throw new KyouException("format syntax error. eol: " + formatStr.pos());

        // 取出下一个字符
        char c = formatStr.get();
        switch (c) {
            case '\\': // '\'
                return '\\';
            case 'r': // '\r'
                return '\r';
            case 'n': // '\n'
                return '\n';
            case '%': // '%'
                return '%';
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F': // HEX
                // 取下一个字符
                if (!formatStr.hasRemaining())
                    throw new KyouException("hex syntax error. eol: " + formatStr.pos());
                char next = formatStr.get();

                // 算出16进制的值
                try {
                    int v = hex2dec(c) * 16 + hex2dec(next);
                    return (byte) v;
                } catch (Exception ex) {
                    throw new KyouException("hex syntax error. \\" + c + next + " at " + (formatStr.pos() - 2));
                }
            default: // ERROR
                throw new KyouException("unsupported escape sequence \\" + c + " at " + formatStr.pos());
        }
    }

    /**
     * 尝试将一个字符按照16进制解释成数字
     */
    private int hex2dec(char c) {
        if (c >= '0' && c <= '9')
            return c - '0';
        if (c >= 'a' && c <= 'f')
            return c - 'a' + 10;
        if (c >= 'A' && c <= 'F')
            return c - 'A' + 10;

        throw new KyouException("hex syntax error. char: " + c);
    }

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

        PosString(String str) {
            if (str == null)
                throw new KyouException("str is null");

            this.chars = str.toCharArray();
        }

        /**
         * 从{@link PosString}中获取当前位置的字符，并将pos向前推进一格
         * 
         * @return 当前位置的字符
         */
        char get() {
            return this.chars[pos++];
        }

        /**
         * 向{@link PosString}中推回一个位置的字符
         */
        void pushback() {
            this.pos--;
            if (this.pos < 0)
                throw new IndexOutOfBoundsException(String.valueOf(this.pos));
        }

        /**
         * 获取当前位置
         * 
         * @return 当前位置
         */
        int pos() {
            return this.pos;
        }

        /**
         * 判断该{@link PosString}的pos是否小于length
         * <p>
         * 这通常表示是否还可以进行get()操作
         * </p>
         * 
         * @return 该{@link PosString}的pos是否小于length
         */
        boolean hasRemaining() {
            return this.pos < this.chars.length;
        }

        /**
         * 判断{@link PosString}的pos位置处的字符是不是给定字符中的一个，如果是则取出该字符
         * 
         * @param options
         *            所有可能的字符列表
         * @return 找到的字符，或为null，表示没有找到给定的字符
         */
        Character attempt(char... options) {
            if (!this.hasRemaining())
                return null;

            // 看当前位置的字符是不是在给定的字符列表中
            if (ArrayUtils.contains(options, this.chars[this.pos]))
                return this.get();
            else
                return null;
        }
    }

}
