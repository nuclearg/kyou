package net.nuclearg.kyou.pack.expr;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.dom.KyouContainer;
import net.nuclearg.kyou.pack.Expr;
import net.nuclearg.kyou.pack.Expr.ExprDescription;
import net.nuclearg.kyou.pack.Expr.ExprDescription.ExprPostfix;
import net.nuclearg.kyou.pack.PackContext;
import net.nuclearg.kyou.util.value.Value;
import net.nuclearg.kyou.util.value.ValueType;

/**
 * 取当前节点的父节点
 * 
 * @author ng
 * 
 */
@ExprDescription(name = "p", postfix = ExprPostfix.None, typeIn = ValueType.Dom, typeOut = ValueType.Dom)
class ParentExpr extends Expr {

    @Override
    protected Value eval(Value input, PackContext context) {
        KyouContainer parent = input.domValue.parent();

        if (parent == null || parent == input.domValue)
            throw new KyouException("item has no parent. item: " + input.domValue);

        return new Value(input.domValue.parent());
    }

}
