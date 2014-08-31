package net.nuclearg.kyou.pack.expr;

import java.nio.charset.Charset;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.pack.Expr;
import net.nuclearg.kyou.pack.Expr.ExprDescription;
import net.nuclearg.kyou.pack.Expr.ExprDescription.ExprPostfix;
import net.nuclearg.kyou.pack.PackContext;
import net.nuclearg.kyou.util.value.KyouValue;
import net.nuclearg.kyou.util.value.KyouValueType;

/**
 * 将字符串转为字节数组的表达式
 * <p>
 * 后缀如果提供，则表示用于这次转换的编码
 * </p>
 * 
 * @author ng
 */
@ExprDescription(name = "s2b", postfix = ExprPostfix.NoneOrString, typeIn = KyouValueType.String, typeOut = KyouValueType.Bytes)
public class ConvertS2BExpr extends Expr {
    /**
     * 转换时使用的编码
     */
    private Charset encoding;

    @Override
    protected KyouValue eval(KyouValue input, PackContext context) {
        Charset encoding = this.encoding == null ? context.spec.config.encoding : this.encoding;

        return new KyouValue(input.strValue.getBytes(encoding));
    }

    protected void check(Expr prev) {
        super.check(prev);

        if (this.postfix != null)
            try {
                // 如果后缀不为空，则表示需要指定编码
                this.encoding = Charset.forName(this.postfix);
            } catch (Exception ex) {
                throw new KyouException();
            }
    }
}
