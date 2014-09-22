package com.github.nuclearg.kyou.pack;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.github.nuclearg.kyou.KyouException;
import com.github.nuclearg.kyou.pack.expr.Expr;
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
abstract class StyleFormatSegment {
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
    static List<StyleFormatSegment> parseFormatString(String formatStr, Charset encoding, List<String> params) {
        StyleFormatString format = new StyleFormatString(formatStr, encoding);

        List<StyleFormatSegment> segments = new ArrayList<>();
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

                String param = params.get(paramId);
                try {
                    segments.add(new ParamSegment(param));
                    paramId++;
                } catch (Exception ex) {
                    throw new KyouException("parse param fail. format: " + formatStr + ", paramId: " + paramId + ", param: " + param, ex);
                }
            }

        return segments;
    }

    /**
     * 表示一段纯粹的字节流
     * 
     * @author ng
     * 
     */
    static class BytesSegment extends StyleFormatSegment {
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
    static class ParamSegment extends StyleFormatSegment {
        private final List<Expr> exprChain;

        ParamSegment(String paramStr) {
            this.exprChain = Expr.parseExprList(paramStr);
        }

        @Override
        Value export(PackContext context, ByteOutputStream os) {
            // 向表达式链输入的最初的值是正被组包的当前报文节点
            Value value = new Value(context.item);

            // 沿着表达式链一直计算，最开始的输入为空，
            for (Expr expr : this.exprChain)
                value = expr.eval(value, context);

            switch (value.type) {
                case Bytes:
                    // 如果输出为Bytes，则输出
                    os.write(value.bytesValue);
                    return value;
                case Backspace:
                    // 如果输出为Backspace，则回退
                    os.backspace(value.intValue);
                    return value;
                default:
                    return value;
            }
        }
    }
}
