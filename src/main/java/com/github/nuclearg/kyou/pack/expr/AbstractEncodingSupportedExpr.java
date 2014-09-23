package com.github.nuclearg.kyou.pack.expr;

import java.nio.charset.Charset;

import com.github.nuclearg.kyou.KyouException;
import com.github.nuclearg.kyou.pack.PackContext;
import com.github.nuclearg.kyou.util.value.Value;

/**
 * 支持编码的表达式基类，支持将简单后缀理解为编码
 * 
 * @author ng
 * 
 */
abstract class AbstractEncodingSupportedExpr extends SimplePostfixExpr {

    @Override
    public Value calc(Value input, PackContext context, Value postfix) {
        Charset encoding;

        if (postfix == null)
            encoding = context.style.config.encoding;
        else
            try {
                encoding = Charset.forName(postfix.strValue);
            } catch (Exception ex) {
                throw new KyouException("encoding not found. encoding: " + postfix.strValue);
            }

        return this.eval(input, context, encoding);
    }

    /**
     * 执行基于编码的计算
     * 
     * @param input
     * @param context
     * @param encoding
     * @return
     */
    protected abstract Value eval(Value input, PackContext context, Charset encoding);

}
