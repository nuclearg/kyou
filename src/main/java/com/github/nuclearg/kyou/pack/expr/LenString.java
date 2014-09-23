package com.github.nuclearg.kyou.pack.expr;

import com.github.nuclearg.kyou.pack.PackContext;
import com.github.nuclearg.kyou.pack.expr.ExprDescription.ExprPostfix;
import com.github.nuclearg.kyou.util.value.Value;
import com.github.nuclearg.kyou.util.value.ValueType;

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
    public Value calc(Value input, PackContext context) {
        return new Value(input.strValue.length());
    }

}
