package net.nuclearg.kyou.pack.expr;

import java.nio.charset.Charset;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.pack.Expr;
import net.nuclearg.kyou.pack.PackContext;
import net.nuclearg.kyou.util.value.Value;

/**
 * 支持编码的表达式基类，支持将简单后缀理解为编码
 * 
 * @author ng
 * 
 */
abstract class AbstractEncodingSupportedExpr extends Expr {
    /**
     * 使用的编码
     */
    private Charset encoding;

    @Override
    protected Value eval(Value input, PackContext context) {
        Charset encoding = this.encoding == null ? context.style.config.encoding : this.encoding;

        return this.eval(input, context, encoding);
    }

    /**
     * 执行基于编码的计算
     * 
     * @param input
     * @param context
     * @param encoding
     * @return
     */
    protected abstract Value eval(Value input, PackContext context, Charset encoding);

    protected void check(Expr prev) {
        super.check(prev);

        // 如果后缀为空则表示不指定编码，和整篇报文的编码保持一致
        if (this.postfix == null)
            return;

        // 如果后缀不为空，则表示需要指定编码
        try {
            this.encoding = Charset.forName(this.postfix);
        } catch (Exception ex) {
            throw new KyouException("encoding not found. encoding: " + encoding);
        }
    }
}
