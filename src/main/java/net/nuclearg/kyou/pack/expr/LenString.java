package net.nuclearg.kyou.pack.expr;

import net.nuclearg.kyou.pack.PackContext;
import net.nuclearg.kyou.pack.expr.ExprDescription.ExprPostfix;
import net.nuclearg.kyou.util.value.Value;
import net.nuclearg.kyou.util.value.ValueType;

/**
 * 求字符串的长度
 * 
 * @in 被求长度的字符串
 * @out 字符串的长度
 * 
 * @author ng
 * 
 */
@ExprDescription(name = "lens", postfix = ExprPostfix.None, typeIn = ValueType.String, typeOut = ValueType.Integer)
class LenString extends Expr {

    @Override
    public Value eval(Value input, PackContext context) {
        return new Value(input.strValue.length());
    }

}
