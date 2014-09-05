package net.nuclearg.kyou.pack.expr;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.pack.Expr;
import net.nuclearg.kyou.pack.Expr.ExprDescription;
import net.nuclearg.kyou.pack.Expr.ExprDescription.ExprPostfix;
import net.nuclearg.kyou.pack.PackContext;
import net.nuclearg.kyou.util.value.Value;
import net.nuclearg.kyou.util.value.ValueType;

/**
 * 将字符串转为整形数字的表达式
 * <p>
 * 后缀如果提供，则表示用于进制，默认为10进制
 * </p>
 * 
 * @author ng
 * 
 */
@ExprDescription(name = "i2s", postfix = ExprPostfix.NoneOrInt, typeIn = ValueType.Integer, typeOut = ValueType.String)
class ConvertI2SExpr extends Expr {
    @Override
    protected Value eval(Value input, PackContext context) {
        return new Value(Integer.toString(input.intValue, this.postfixi));
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