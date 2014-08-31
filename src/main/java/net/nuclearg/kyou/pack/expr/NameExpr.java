package net.nuclearg.kyou.pack.expr;

import net.nuclearg.kyou.pack.Expr;
import net.nuclearg.kyou.pack.PackContext;
import net.nuclearg.kyou.pack.Expr.ExprDescription;
import net.nuclearg.kyou.pack.Expr.ExprDescription.ExprPostfix;
import net.nuclearg.kyou.util.value.KyouValue;
import net.nuclearg.kyou.util.value.KyouValueType;

/**
 * 求当前报文元素的名称
 * 
 * @author ng
 */
@ExprDescription(name = "n", postfix = ExprPostfix.None, typeIn = KyouValueType.Dom, typeOut = KyouValueType.String)
public class NameExpr extends Expr {

    @Override
    protected KyouValue eval(KyouValue input, PackContext context) {
        return new KyouValue(input.domValue.name());
    }
}
