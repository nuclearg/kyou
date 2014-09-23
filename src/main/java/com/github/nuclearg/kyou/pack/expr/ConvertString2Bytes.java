package com.github.nuclearg.kyou.pack.expr;

import java.nio.charset.Charset;

import com.github.nuclearg.kyou.pack.PackContext;
import com.github.nuclearg.kyou.pack.expr.ExprDescription.ExprPostfix;
import com.github.nuclearg.kyou.util.value.Value;
import com.github.nuclearg.kyou.util.value.ValueType;

/**
 * 将字符串转为字节数组
 * 
 * @in 要进行转换的字符串
 * @out 转换出来的字节数组
 * @postfix 如果提供，则表示用于这次转换的编码。如果不提供则使用报文本身的编码
 * 
 * @author ng
 */
@ExprDescription(name = "s2b", postfix = ExprPostfix.NoneOrString, typeIn = ValueType.String, typeOut = ValueType.Bytes)
class ConvertString2Bytes extends AbstractEncodingSupportedExpr {

    @Override
    protected Value eval(Value input, PackContext context, Charset encoding) {
        return new Value(input.strValue.getBytes(encoding));
    }
}
