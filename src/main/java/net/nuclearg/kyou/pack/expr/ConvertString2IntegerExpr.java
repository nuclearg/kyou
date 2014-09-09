package net.nuclearg.kyou.pack.expr;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.pack.Expr;
import net.nuclearg.kyou.pack.Expr.ExprDescription;
import net.nuclearg.kyou.pack.Expr.ExprDescription.ExprPostfix;
import net.nuclearg.kyou.pack.PackContext;
import net.nuclearg.kyou.util.value.Value;
import net.nuclearg.kyou.util.value.ValueType;

/**
 * 将字符串转为整数
 * 
 * @in 要进行转换的字符串
 * @out 转换出来的整数，其上限为2^63-1，下限为-2^63。如果源字符串表示的数字大于这个范围则报错。
 * @postfix 如果提供，则表示用于进制，可选值为2~36（两端包含）。如果不提供，则取10进制
 * 
 * @author ng
 * 
 */
@ExprDescription(name = "s2i", postfix = ExprPostfix.NoneOrInt, typeIn = ValueType.String, typeOut = ValueType.Integer)
class ConvertString2IntegerExpr extends Expr {
    @Override
    protected Value eval(Value input, PackContext context) {
        return new Value(Integer.parseInt(input.strValue, this.postfixi));
    }

    @Override
    protected void check(Expr prev) {
        super.check(prev);

        if (this.postfixi < Character.MIN_RADIX)
            throw new KyouException();
        if (this.postfixi > Character.MAX_RADIX)
            throw new KyouException();

        if (this.postfixi < 0)
            this.postfixi = 10;
    }

}
