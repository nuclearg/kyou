package com.github.nuclearg.kyou.pack.expr;

import java.util.Map;

import com.github.nuclearg.kyou.pack.StyleExpr;
import com.github.nuclearg.kyou.pack.PackContext;
import com.github.nuclearg.kyou.util.value.Value;

abstract class SimplePostfixExpr extends StyleExpr {

    @Override
    public Value calc(Value input, PackContext context, Map<String, Value> postfixMap) {
        throw new UnsupportedOperationException();
    }

}
