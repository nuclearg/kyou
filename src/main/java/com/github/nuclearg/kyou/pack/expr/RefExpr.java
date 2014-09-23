package com.github.nuclearg.kyou.pack.expr;

import com.github.nuclearg.kyou.pack.PackContext;
import com.github.nuclearg.kyou.pack.expr.ExprDescription.ExprPostfix;
import com.github.nuclearg.kyou.util.value.Value;
import com.github.nuclearg.kyou.util.value.ValueType;

@ExprDescription(name = "r", postfix = ExprPostfix.None, typeIn = ValueType.Integer, typeOut = ValueType.RefParam)
class RefExpr extends SimplePostfixExpr {

    @Override
    public Value calc(Value input, PackContext context, Value postfix) {
        return context.packer.calcParamValue(input.intValue, context);
    }

}
