package com.github.nuclearg.kyou.pack.expr;

import com.github.nuclearg.kyou.pack.StyleExpr;
import com.github.nuclearg.kyou.pack.PackContext;
import com.github.nuclearg.kyou.util.value.Value;

abstract class ComplexPostfixExpr extends StyleExpr {

    @Override
    public Value calc(Value input, PackContext context, Value postfix) {
        throw new UnsupportedOperationException();
    }

}
