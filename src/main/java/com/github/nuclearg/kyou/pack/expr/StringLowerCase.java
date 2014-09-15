package com.github.nuclearg.kyou.pack.expr;

import com.github.nuclearg.kyou.pack.PackContext;
import com.github.nuclearg.kyou.pack.expr.ExprDescription.ExprPostfix;
import com.github.nuclearg.kyou.util.value.Value;
import com.github.nuclearg.kyou.util.value.ValueType;

/**
 * 将字符串变为小写
 * 
 * @in 待处理的字符串
 * @out 全部转为小写的字符串
 * 
 * @author ng
 * 
 */
@ExprDescription(name = "lowercases", postfix = ExprPostfix.NoneOrString, typeIn = ValueType.String, typeOut = ValueType.String)
class StringLowerCase extends Expr {

    @Override
    public Value eval(Value input, PackContext context) {
        return new Value(input.strValue.toLowerCase());
    }

}
