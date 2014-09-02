package net.nuclearg.kyou.pack.expr;

import net.nuclearg.kyou.pack.Expr;
import net.nuclearg.kyou.pack.Expr.ExprDescription;
import net.nuclearg.kyou.pack.Expr.ExprDescription.ExprPostfix;
import net.nuclearg.kyou.pack.PackContext;
import net.nuclearg.kyou.util.value.Value;
import net.nuclearg.kyou.util.value.ValueType;

/**
 * 从字节流中回退指定的字节
 * 
 * @author ng
 * 
 */
@ExprDescription(name = "bk", postfix = ExprPostfix.None, typeIn = ValueType.Integer, typeOut = ValueType.Bytes)
public class BackspaceExpr extends Expr {

    @Override
    protected Value eval(Value input, PackContext context) {
        return new Value(ValueType.Backspace, null, input.intValue, null, null);
    }

}
