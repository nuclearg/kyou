package net.nuclearg.kyou.pack;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.pack.Expr.ExprDescription;
import net.nuclearg.kyou.pack.ExprListString.ExprInfo;
import net.nuclearg.kyou.util.ByteOutputStream;
import net.nuclearg.kyou.util.value.Value;
import net.nuclearg.kyou.util.value.ValueType;

import org.apache.commons.lang.StringUtils;

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
     */
    abstract void export(PackContext context, ByteOutputStream os);

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

        List<StyleFormatSegment> segments = new ArrayList<StyleFormatSegment>();
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
    private static class BytesSegment extends StyleFormatSegment {
        private final byte[] text;

        BytesSegment(byte[] text) {
            this.text = text;
        }

        @Override
        void export(PackContext context, ByteOutputStream os) {
            os.write(this.text);
        }
    }

    /**
     * 表示一个参数
     * 
     * @author ng
     * 
     */
    private static class ParamSegment extends StyleFormatSegment {
        private final List<Expr> exprChain;

        ParamSegment(String paramStr) {
            if (StringUtils.isBlank(paramStr))
                throw new KyouException("param is blank");

            List<Expr> exprChain = new ArrayList<Expr>();

            // 解析参数字符串
            ExprListString paramString = new ExprListString(paramStr);
            for (ExprInfo exprInfo : paramString.parseExprInfo())
                exprChain.add(Expr.buildExpr(exprInfo));
            // 因为在实际组包时，是从最后一个表达式开始计算的，为省事现在在这里直接先倒序一下
            Collections.reverse(exprChain);

            // 检查各个表达式的正确性，以及与前一个表达式的衔接是否有问题
            for (int i = 0; i < exprChain.size(); i++) {
                Expr expr = exprChain.get(i);
                Expr prev = i > 0 ? exprChain.get(i - 1) : null;

                expr.check(prev);
            }
            // 检查最后一个表达式的输出是不是字节数组或Backspace
            Expr last = exprChain.get(exprChain.size() - 1);
            ValueType lastOutput = last.getClass().getAnnotation(ExprDescription.class).typeOut();
            if (lastOutput != ValueType.Bytes && lastOutput != ValueType.Backspace)
                throw new KyouException("last expr must return Bytes or Backspace but " + lastOutput);

            this.exprChain = Collections.unmodifiableList(exprChain);
        }

        @Override
        void export(PackContext context, ByteOutputStream os) {
            // 向表达式链输入的最初的值是正被组包的当前报文节点
            Value value = new Value(context.item);

            // 沿着表达式链一直计算，最开始的输入为空，
            for (Expr expr : this.exprChain)
                value = expr.eval(value, context);

            switch (value.type) {
                case Bytes:
                    // 如果输出为Bytes，则输出
                    os.write(value.bytesValue);
                    break;
                case Backspace:
                    // 如果输出为Backspace，则回退
                    os.backspace(value.intValue);
                    break;
                default:
                    throw new UnsupportedOperationException("unsupported value type: " + value.type);
            }
        }
    }
}
