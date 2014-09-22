package com.github.nuclearg.kyou.pack.expr;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.apache.commons.lang.StringUtils;

import com.github.nuclearg.kyou.KyouException;
import com.github.nuclearg.kyou.pack.PackContext;
import com.github.nuclearg.kyou.pack.expr.ExprDescription.ExprPostfix;
import com.github.nuclearg.kyou.util.value.Value;
import com.github.nuclearg.kyou.util.value.ValueType;

/**
 * 对字符串进行url编码
 * <p>
 * 特别地，该方法将会把空格编码为%20（这与java标准的URLEncoder的行为不同）
 * </p>
 * 
 * @in 要进行url编码的字符串
 * @out 经过了url编码的字符串
 * @postfix 如果提供，则表示用于这次转换的编码。如果不提供则使用报文本身的编码
 * 
 * @author ng
 * 
 */
@ExprDescription(name = "urlencodes", postfix = ExprPostfix.NoneOrString, typeIn = ValueType.String, typeOut = ValueType.String)
public class EncodeUrl extends AbstractEncodingSupportedExpr {

    @Override
    protected Value eval(Value input, PackContext context, Charset encoding) {
        try {
            String value = URLEncoder.encode(input.strValue, encoding.name());

            // 将空格转为%20
            value = StringUtils.replace(value, "+", "%20");

            return new Value(value);
        } catch (UnsupportedEncodingException ex) {
            throw new KyouException(ex);
        }
    }
}
