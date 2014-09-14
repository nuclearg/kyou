package net.nuclearg.kyou.pack.expr;

import net.nuclearg.kyou.pack.PackContext;
import net.nuclearg.kyou.pack.expr.ExprDescription.ExprPostfix;
import net.nuclearg.kyou.util.value.Value;
import net.nuclearg.kyou.util.value.ValueType;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * 对字符串进行xml编码
 * 
 * @in 要进行xml编码的字符串
 * @out 经过了xml编码的字符串
 * 
 * @author ng
 * 
 */
@ExprDescription(name = "xmlencode", postfix = ExprPostfix.None, typeIn = ValueType.String, typeOut = ValueType.String)
public class EncodeXml extends Expr {

    @Override
    public Value eval(Value input, PackContext context) {
        return new Value(StringEscapeUtils.escapeXml(input.strValue));
    }
}
