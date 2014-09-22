package com.github.nuclearg.kyou.pack.expr;

import com.github.nuclearg.kyou.pack.PackContext;
import com.github.nuclearg.kyou.pack.expr.ExprDescription.ExprPostfix;
import com.github.nuclearg.kyou.util.value.Value;
import com.github.nuclearg.kyou.util.value.ValueType;

@ExprDescription(name = "r", postfix = ExprPostfix.Int, typeIn = ValueType.Null, typeOut = ValueType.Auto)
class RefExpr extends Expr {

    @Override
    public Value eval(Value input, PackContext context) {
        return context.packer.calcParamValue(context.item, context, context.currentStyle, this.postfix.intValue);
    }

}
