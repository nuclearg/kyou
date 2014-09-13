package net.nuclearg.kyou.pack.expr;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.dom.KyouField;
import net.nuclearg.kyou.pack.PackContext;
import net.nuclearg.kyou.pack.expr.ExprDescription.ExprPostfix;
import net.nuclearg.kyou.util.value.Value;
import net.nuclearg.kyou.util.value.ValueType;

import org.apache.commons.lang.StringUtils;

/**
 * 求报文字段的值
 * 
 * @in 要被获取值的报文字段
 * @out 报文字段的值。如果输入的报文元素不是一个字段则报错，如果输入的报文字段的值为空则返回一个空字符串
 * 
 * @author ng
 * 
 */
@ExprDescription(name = "v", postfix = ExprPostfix.None, typeIn = ValueType.Dom, typeOut = ValueType.String)
class FieldValue extends Expr {

    @Override
    public Value eval(Value input, PackContext context) {
        if (!(input.domValue instanceof KyouField))
            throw new KyouException("KyouField expected. path: " + input.domValue.path());

        KyouField field = (KyouField) input.domValue;

        String value = field.value();
        if (value == null)
            value = StringUtils.EMPTY;

        return new Value(value);
    }

}
