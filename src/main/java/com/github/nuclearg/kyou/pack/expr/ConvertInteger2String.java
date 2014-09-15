package com.github.nuclearg.kyou.pack.expr;

import com.github.nuclearg.kyou.KyouException;
import com.github.nuclearg.kyou.pack.PackContext;
import com.github.nuclearg.kyou.pack.expr.ExprDescription.ExprPostfix;
import com.github.nuclearg.kyou.util.value.Value;
import com.github.nuclearg.kyou.util.value.ValueType;

/**
 * 将整数转为字符串
 * 
 * @out 要进行转换的整数
 * @in 转换出来的字符串
 * @postfix 如果提供，则表示用于进制，可选值为2~36（两端包含）。如果不提供，则取10进制
 * 
 * @author ng
 * 
 */
@ExprDescription(name = "i2s", postfix = ExprPostfix.NoneOrInt, typeIn = ValueType.Integer, typeOut = ValueType.String)
class ConvertInteger2String extends Expr {
    @Override
    public Value eval(Value input, PackContext context) {
        return new Value(Long.toString(input.intValue, this.postfixi));
    }

    @Override
    protected void check(Expr prev) {
        super.check(prev);

        if (this.postfixi < Character.MIN_RADIX)
            throw new KyouException("radix < " + Character.MIN_RADIX);
        if (this.postfixi > Character.MAX_RADIX)
            throw new KyouException("radix > " + Character.MAX_RADIX);

        if (this.postfixi < 0)
            this.postfixi = 10;
    }

}