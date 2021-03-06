package com.github.nuclearg.kyou.pack.expr;

import org.apache.commons.lang.StringEscapeUtils;

import com.github.nuclearg.kyou.pack.PackContext;
import com.github.nuclearg.kyou.pack.expr.ExprDescription.ExprPostfix;
import com.github.nuclearg.kyou.util.value.Value;
import com.github.nuclearg.kyou.util.value.ValueType;

/**
 * 对字符串进行xml编码
 * 
 * @in 要进行xml编码的字符串
 * @out 经过了xml编码的字符串
 * 
 * @author ng
 * 
 */
@ExprDescription(name = "xmlencodes", postfix = ExprPostfix.None, typeIn = ValueType.String, typeOut = ValueType.String)
public class EncodeXml extends SimplePostfixExpr {

    @Override
    public Value calc(Value input, PackContext context, Value postfix) {
        return new Value(StringEscapeUtils.escapeXml(input.strValue));
    }
}
