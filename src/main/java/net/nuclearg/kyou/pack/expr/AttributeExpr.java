package net.nuclearg.kyou.pack.expr;

import net.nuclearg.kyou.pack.Expr.ExprDescription;
import net.nuclearg.kyou.pack.Expr.ExprDescription.ExprPostfix;
import net.nuclearg.kyou.pack.Expr;
import net.nuclearg.kyou.pack.PackContext;
import net.nuclearg.kyou.util.value.KyouValue;
import net.nuclearg.kyou.util.value.KyouValueType;

/**
 * 求报文元素的某个属性的值
 * 
 * @author ng
 * 
 */
@ExprDescription(name = "a", postfix = ExprPostfix.String, typeIn = KyouValueType.Dom, typeOut = KyouValueType.String)
public class AttributeExpr extends Expr {

    @Override
    protected KyouValue eval(KyouValue input, PackContext context) {
        return new KyouValue(input.domValue.attr(this.postfix));
    }

}
