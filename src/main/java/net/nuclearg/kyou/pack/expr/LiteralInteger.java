package net.nuclearg.kyou.pack.expr;

import net.nuclearg.kyou.pack.PackContext;
import net.nuclearg.kyou.pack.expr.ExprDescription.ExprPostfix;
import net.nuclearg.kyou.util.value.Value;
import net.nuclearg.kyou.util.value.ValueType;

import org.apache.commons.lang.math.NumberUtils;

/**
 * 输出一个立即数
 * <p>
 * </p>
 * 
 * @in
 * @out
 * @postfix
 * 
 * @author ng
 * 
 */
@ExprDescription(name = "<literal integer>", postfix = ExprPostfix.Int, typeIn = ValueType.Dom, typeOut = ValueType.Integer)
class LiteralInteger extends Expr {

    public LiteralInteger(String value) {
        super.postfix = value;
        super.postfixi = NumberUtils.toInt(value);
        super.postfixMap = null;
    }

    @Override
    public Value eval(Value input, PackContext context) {
        return new Value(this.postfixi);
    }

}