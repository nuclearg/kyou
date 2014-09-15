package com.github.nuclearg.kyou.pack.expr;

import com.github.nuclearg.kyou.pack.PackContext;
import com.github.nuclearg.kyou.pack.expr.ExprDescription.ExprPostfix;
import com.github.nuclearg.kyou.util.value.Value;
import com.github.nuclearg.kyou.util.value.ValueType;

/**
 * 求当前报文元素的名称
 * 
 * @in 要被获取名称的报文元素
 * @out 报文元素的名称。对于数组中的元素，这里取到的实际是元素的下标，如果想取对应数组的名称则应该取父元素的名称
 * 
 * @author ng
 */
@ExprDescription(name = "n", postfix = ExprPostfix.None, typeIn = ValueType.Dom, typeOut = ValueType.String)
class Name extends Expr {

    @Override
    public Value eval(Value input, PackContext context) {
        return new Value(input.domValue.name());
    }
}
