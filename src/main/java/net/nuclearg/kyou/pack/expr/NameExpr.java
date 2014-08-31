package net.nuclearg.kyou.pack.expr;

import net.nuclearg.kyou.pack.Expr;
import net.nuclearg.kyou.pack.PackContext;
import net.nuclearg.kyou.pack.Expr.ExprDescription;
import net.nuclearg.kyou.pack.Expr.ExprDescription.ExprPostfix;
import net.nuclearg.kyou.util.value.Value;
import net.nuclearg.kyou.util.value.ValueType;

/**
 * 求当前报文元素的名称
 * 
 * @author ng
 */
@ExprDescription(name = "n", postfix = ExprPostfix.None, typeIn = ValueType.Dom, typeOut = ValueType.String)
public class NameExpr extends Expr {

    @Override
    protected Value eval(Value input, PackContext context) {
        return new Value(input.domValue.name());
    }
}
