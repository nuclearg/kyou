package net.nuclearg.kyou.pack.expr;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.dom.KyouField;
import net.nuclearg.kyou.pack.Expr;
import net.nuclearg.kyou.pack.Expr.ExprDescription;
import net.nuclearg.kyou.pack.Expr.ExprDescription.ExprPostfix;
import net.nuclearg.kyou.pack.PackContext;
import net.nuclearg.kyou.util.KyouValue;
import net.nuclearg.kyou.util.KyouValueType;

import org.apache.commons.lang.StringUtils;

/**
 * 求报文字段的值
 * 
 * @author ng
 * 
 */
@ExprDescription(name = "v", postfix = ExprPostfix.None, typeIn = KyouValueType.Dom, typeOut = KyouValueType.String)
public class ValueExpr extends Expr {

    @Override
    protected KyouValue eval(KyouValue input, PackContext context) {
        if (!(input.domValue instanceof KyouField))
            throw new KyouException("KyouField expected");

        KyouField field = (KyouField) input.domValue;

        String value = field.value();
        if (value == null)
            value = StringUtils.EMPTY;

        return new KyouValue(value);
    }

}
