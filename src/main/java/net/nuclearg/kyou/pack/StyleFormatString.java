package net.nuclearg.kyou.pack;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.util.ByteOutputStream;
import net.nuclearg.kyou.util.lexer.LexDefinition;
import net.nuclearg.kyou.util.lexer.LexString;
import net.nuclearg.kyou.util.lexer.LexToken;

/**
 * 格式字符串，对应于组包样式中format的部分
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
class StyleFormatString implements Iterable<byte[]> {
    /**
     * 原始的字符串
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
    StyleFormatString(String formatStr, Charset encoding) {
        if (encoding == null)
            throw new KyouException("encoding is null");

        this.formatStr = formatStr;

        // 解析样式字符串
        LexString<Lex> tokenStr = new LexString<>(formatStr);

        // 将每个词法元素对应到段上
        List<byte[]> segments = new LinkedList<>();

        ByteOutputStream os = new ByteOutputStream();
        StringBuilder builder = new StringBuilder();

        while (tokenStr.hasRemaining())
            try {
                LexToken<Lex> token = tokenStr.tryToken(Lex.values());
                switch (token.type) {
                    case ParamChar:
                        // 如果遇到一个参数段，则把之前的东西压成一个段
                        pushTextToBytes(builder, os, encoding);
                        pushBytesToSegments(os, segments);

                        // 再压进去一个参数段
                        segments.add(null);
                        break;
                    case HexChar:
                        // 如果遇到一个十六进制字节则把之前的文本压到字节流里
                        pushTextToBytes(builder, os, encoding);

                        // 解析十六进制
                        String hex = token.str.substring(1);
                        byte b = (byte) Integer.parseInt(hex, 16);
                        os.write(b);

                        break;
                    case EscapeChar:
                        // 如果是转义符，则根据转义符做判断
                        switch (token.str.charAt(1)) {
                            case '\\':
                                builder.append('\\');
                                break;
                            case '%':
                                builder.append('%');
                                break;
                            case 'r':
                                builder.append('\r');
                                break;
                            case 'n':
                                builder.append('\n');
                                break;
                            default:
                                throw new UnsupportedOperationException("escape char " + token.str);
                        }
                        break;
                    case SimpleChar:
                        // 将字符缀到builder里
                        builder.append(token.str);
                        break;
                    default:
                        throw new UnsupportedOperationException("token type = " + token.type);
                }
            } catch (Exception ex) {
                throw new KyouException("format syntax error. format: " + formatStr, ex);
            }

        // 把剩余的东西压成一个段
        pushTextToBytes(builder, os, encoding);
        pushBytesToSegments(os, segments);

        this.segments = Collections.unmodifiableList(segments);
    }

    /**
     * 把builder里的东西压到os里，并清空builder
     * 
     * @param builder
     * @param os
     * @param encoding
     */
    private static void pushTextToBytes(StringBuilder builder, ByteOutputStream os, Charset encoding) {
        if (builder.length() <= 0)
            return;

        byte[] bytes = builder.toString().getBytes(encoding);
        os.write(bytes);

        builder.delete(0, builder.length());
    }

    /**
     * 把os里的东西压到segments里，并清空os
     * 
     * @param os
     * @param segments
     */
    private static void pushBytesToSegments(ByteOutputStream os, List<byte[]> segments) {
        byte[] bytes = os.export();
        if (bytes.length <= 0)
            return;

        segments.add(bytes);

        os.backspace(os.export().length);
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
        return this.formatStr.toString();
    }

    /**
     * 词法定义
     * 
     * @author ng
     * 
     */
    private static enum Lex implements LexDefinition {
        /**
         * 参数字符
         */
        ParamChar("\\%"),
        /**
         * 16进制字符
         */
        HexChar("\\\\[0-9a-fA-F]{2}"),
        /**
         * 转义字符
         */
        EscapeChar("\\\\[\\\\%rn]"),
        /**
         * 普通的字符
         */
        SimpleChar("."),

        ;

        private final Pattern regex;

        private Lex(String regex) {
            this.regex = Pattern.compile(regex);
        }

        @Override
        public Pattern regex() {
            return this.regex;
        }

        @Override
        public String token(String selectedStr) {
            return selectedStr;
        }
    }
}
