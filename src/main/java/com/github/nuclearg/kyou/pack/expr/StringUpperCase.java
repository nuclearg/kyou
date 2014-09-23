package com.github.nuclearg.kyou.pack.expr;

import com.github.nuclearg.kyou.pack.PackContext;
import com.github.nuclearg.kyou.pack.expr.ExprDescription.ExprPostfix;
import com.github.nuclearg.kyou.util.value.Value;
import com.github.nuclearg.kyou.util.value.ValueType;

/**
 * 将字符串变为大写
 * 
 * @in 待处理的字符串
 * @out 全部转为小写的字符串
 * 
 * @author ng
 * 
 */
@ExprDescription(name = "uppercases", postfix = ExprPostfix.NoneOrString, typeIn = ValueType.String, typeOut = ValueType.String)
class StringUpperCase extends SimplePostfixExpr {

    @Override
    public Value calc(Value input, PackContext context, Value postfix) {
        return new Value(input.strValue.toUpperCase());
    }

}
