package com.github.nuclearg.kyou.pack.expr;

import com.github.nuclearg.kyou.KyouException;
import com.github.nuclearg.kyou.pack.PackContext;
import com.github.nuclearg.kyou.util.value.Value;

/**
 * 支持进制的表达式基类，支持把简单后缀理解为进制
 * 
 * @author ng
 * 
 */
abstract class AbstractRadixSupportedExpr extends Expr {
    /**
     * 进制，支持2～36
     */
    private int radix;

    @Override
    public Value eval(Value input, PackContext context) {
        return this.eval(input, context, this.radix);
    }

    /**
     * 执行基于进制的计算
     * 
     * @param input
     * @param context
     * @param radix
     * @return
     */
    protected abstract Value eval(Value input, PackContext context, int radix);

    @Override
    protected void check(Expr prev) {
        super.check(prev);

        this.radix = this.postfix == null ? 10 : this.postfix.intValue;

        if (radix < Character.MIN_RADIX)
            throw new KyouException("radix < " + Character.MIN_RADIX);
        if (radix > Character.MAX_RADIX)
            throw new KyouException("radix > " + Character.MAX_RADIX);
    }
}
