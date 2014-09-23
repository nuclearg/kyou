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
abstract class AbstractRadixSupportedExpr extends SimplePostfixExpr {

    @Override
    public Value calc(Value input, PackContext context, Value postfix) {
        int radix = postfix == null ? 10 : postfix.intValue;

        if (radix < Character.MIN_RADIX)
            throw new KyouException("radix < " + Character.MIN_RADIX);
        if (radix > Character.MAX_RADIX)
            throw new KyouException("radix > " + Character.MAX_RADIX);

        return this.eval(input, context, radix);
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

}
