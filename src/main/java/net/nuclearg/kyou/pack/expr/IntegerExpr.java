package net.nuclearg.kyou.pack.expr;

import net.nuclearg.kyou.pack.Expr;
import net.nuclearg.kyou.pack.PackContext;
import net.nuclearg.kyou.pack.Expr.ExprDescription;
import net.nuclearg.kyou.pack.Expr.ExprDescription.ExprPostfix;
import net.nuclearg.kyou.util.KyouValue;
import net.nuclearg.kyou.util.KyouValueType;

/**
 * 输出一个立即数
 * 
 * @author ng
 * 
 */
@ExprDescription(name = "", postfix = ExprPostfix.None, typeIn = KyouValueType.Dom, typeOut = KyouValueType.Integer)
public class IntegerExpr extends Expr {

    @Override
    protected KyouValue eval(KyouValue input, PackContext context) {
        return new KyouValue(this.postfixi);
    }

}
