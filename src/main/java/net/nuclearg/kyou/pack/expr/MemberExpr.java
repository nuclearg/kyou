package net.nuclearg.kyou.pack.expr;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.dom.KyouContainer;
import net.nuclearg.kyou.dom.KyouItem;
import net.nuclearg.kyou.pack.Expr;
import net.nuclearg.kyou.pack.Expr.ExprDescription;
import net.nuclearg.kyou.pack.Expr.ExprDescription.ExprPostfix;
import net.nuclearg.kyou.pack.PackContext;
import net.nuclearg.kyou.util.value.Value;
import net.nuclearg.kyou.util.value.ValueType;

/**
 * 计算当前报文元素的所有子元素组包的结果
 * 
 * @author ng
 */
@ExprDescription(name = "m", postfix = ExprPostfix.None, typeIn = ValueType.Dom, typeOut = ValueType.Bytes)
class MemberExpr extends Expr {

    @Override
    protected Value eval(Value input, PackContext context) {
        KyouItem item = input.domValue;
        if (!(item instanceof KyouContainer))
            throw new KyouException("KyouContainer expected. path: " + item.path());

        KyouContainer container = (KyouContainer) item;

        return new Value(context.packer.packMember(container, context));
    }
}
