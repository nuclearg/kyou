package net.nuclearg.kyou.pack.expr;

import net.nuclearg.kyou.pack.Expr;
import net.nuclearg.kyou.pack.Expr.ExprDescription;
import net.nuclearg.kyou.pack.Expr.ExprDescription.ExprPostfix;
import net.nuclearg.kyou.pack.PackContext;
import net.nuclearg.kyou.util.value.Value;
import net.nuclearg.kyou.util.value.ValueType;

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
class StringLowerCaseExpr extends Expr {

    @Override
    protected Value eval(Value input, PackContext context) {
        return new Value(input.strValue.toLowerCase());
    }

}
