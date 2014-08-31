package net.nuclearg.kyou.pack.expr;

import net.nuclearg.kyou.pack.Expr;
import net.nuclearg.kyou.pack.PackContext;
import net.nuclearg.kyou.pack.Expr.ExprDescription;
import net.nuclearg.kyou.pack.Expr.ExprDescription.ExprPostfix;
import net.nuclearg.kyou.util.value.Value;
import net.nuclearg.kyou.util.value.ValueType;

/**
 * 输出一个立即数
 * 
 * @author ng
 * 
 */
@ExprDescription(name = "", postfix = ExprPostfix.None, typeIn = ValueType.Dom, typeOut = ValueType.Integer)
public class IntegerExpr extends Expr {

    @Override
    protected Value eval(Value input, PackContext context) {
        return new Value(this.postfixi);
    }

}
