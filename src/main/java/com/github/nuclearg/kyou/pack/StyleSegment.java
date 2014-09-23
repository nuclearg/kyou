package com.github.nuclearg.kyou.pack;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.github.nuclearg.kyou.KyouException;
import com.github.nuclearg.kyou.util.ByteOutputStream;
import com.github.nuclearg.kyou.util.value.Value;
import com.github.nuclearg.kyou.util.value.ValueType;

/**
 * 组包片段，对应格式字符串{@link StyleFormatString}中的一段
 * <p>
 * 存在{@link BytesSegment}和{@link ParamSegment}两个子类，分别对应{@link StyleFormatString}中的字符串部分和参数部分
 * </p>
 * 
 * @author ng
 * 
 */
abstract class StyleSegment {
    /**
     * 将对应的字节输出到输出流中
     * 
     * @param context
     *            组包上下文
     * @param os
     *            输出流
     * @return 为了支持参数引用，参数段的输出并不一定必须是{@link ValueType#Bytes}类型的。
     *         <ul>
     *         <li>如果参数段的计算结果是{@link ValueType#Bytes}类型，则输出到os中</li>
     *         <li>如果参数段的计算结果是其它类型，则返回该计算结果</li>
     *         </ul>
     */
    abstract Value export(PackContext context, ByteOutputStream os);

    /**
     * 将格式字符串解析为多个组包段
     * 
     * @param formatStr
     *            格式字符串
     * @param encoding
     *            报文采取的编码
     * @param params
     *            参数列表
     * @return 这个格式字符串表示的多个组包段
     */
    static List<StyleSegment> parseFormatString(String formatStr, Charset encoding, List<StyleParam> params) {
        StyleFormatString format = new StyleFormatString(formatStr, encoding);

        List<StyleSegment> segments = new ArrayList<>();
        int paramId = 0;
        for (byte[] bytes : format)
            // 判断这个段的类型
            if (bytes != null)
                // 如果不为null表示这是一段字节
                segments.add(new BytesSegment(bytes));
            else {
                // 如果为null表示这是一个参数
                if (params.size() <= paramId)
                    throw new KyouException("insufficent params. format: " + formatStr + ", paramId: " + paramId);

                segments.add(new ParamSegment(params.get(paramId)));
                paramId++;
            }

        return segments;
    }

    /**
     * 表示一段纯粹的字节流
     * 
     * @author ng
     * 
     */
    static class BytesSegment extends StyleSegment {
        private final byte[] text;

        BytesSegment(byte[] text) {
            this.text = text;
        }

        @Override
        Value export(PackContext context, ByteOutputStream os) {
            os.write(this.text);
            return null;
        }
    }

    /**
     * 表示一个参数
     * 
     * @author ng
     * 
     */
    static class ParamSegment extends StyleSegment {
        final StyleParam param;

        ParamSegment(StyleParam param) {
            this.param = param;
        }

        @Override
        Value export(PackContext context, ByteOutputStream os) {
            Value value = this.param.calc(context);

            switch (value.type) {
                case Bytes:
                    os.write(value.bytesValue);
                    return value;
                case Backspace:
                    os.backspace(value.intValue);
                    return value;
                default:
                    return value;
            }
        }
    }
}
