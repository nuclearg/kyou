package net.nuclearg.kyou.pack.expr;

import java.nio.charset.Charset;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.pack.PackContext;
import net.nuclearg.kyou.pack.expr.ExprDescription.ExprPostfix;
import net.nuclearg.kyou.util.value.Value;
import net.nuclearg.kyou.util.value.ValueType;

/**
 * 将字节数组转为字符串
 * 
 * @in 要进行转换的字节数组
 * @out 转换出来的字符串
 * @postfix 如果提供，则表示用于这次转换的编码。如果不提供则使用报文本身的编码
 * 
 * @author ng
 */
@ExprDescription(name = "b2s", postfix = ExprPostfix.NoneOrString, typeIn = ValueType.Bytes, typeOut = ValueType.String)
class ConvertBytes2String extends Expr {
    /**
     * 转换时使用的编码
     */
    private Charset encoding;

    @Override
    public Value eval(Value input, PackContext context) {
        Charset encoding = this.encoding == null ? context.style.config.encoding : this.encoding;

        return new Value(new String(input.bytesValue, encoding));
    }

    @Override
    protected void check(Expr prev) {
        super.check(prev);

        // 如果后缀为空则表示不指定编码，和整篇报文的编码保持一致
        if (this.postfix == null)
            return;

        // 如果后缀不为空，则表示需要指定编码
        try {
            this.encoding = Charset.forName(this.postfix);
        } catch (Exception ex) {
            throw new KyouException();
        }
    }
}