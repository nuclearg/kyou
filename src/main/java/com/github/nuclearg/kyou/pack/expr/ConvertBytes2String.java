package com.github.nuclearg.kyou.pack.expr;

import java.nio.charset.Charset;

import com.github.nuclearg.kyou.pack.PackContext;
import com.github.nuclearg.kyou.pack.expr.ExprDescription.ExprPostfix;
import com.github.nuclearg.kyou.util.value.Value;
import com.github.nuclearg.kyou.util.value.ValueType;

/**
 * 将字节数组转为字符串
 * 
 * @in 要进行转换的字节数组
 * @out 转换出来的字符串
 * @postfix 如果提供，则表示用于这次转换的编码。如果不提供则使用报文本身的编码
 * 
 * @author ng
 */
@ExprDescription(name = "b2s", postfix = ExprPostfix.NoneOrString, typeIn = ValueType.Bytes, typeOut = ValueType.String)
class ConvertBytes2String extends AbstractEncodingSupportedExpr {

    @Override
    protected Value eval(Value input, PackContext context, Charset encoding) {
        return new Value(new String(input.bytesValue, encoding));
    }
}
