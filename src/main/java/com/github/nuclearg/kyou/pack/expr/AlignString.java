package com.github.nuclearg.kyou.pack.expr;

import org.apache.commons.lang.StringUtils;

import com.github.nuclearg.kyou.KyouException;
import com.github.nuclearg.kyou.pack.PackContext;
import com.github.nuclearg.kyou.pack.StyleUnit;
import com.github.nuclearg.kyou.pack.expr.ExprDescription.ComplexPostfixField;
import com.github.nuclearg.kyou.pack.expr.ExprDescription.ExprPostfix;
import com.github.nuclearg.kyou.util.value.Value;
import com.github.nuclearg.kyou.util.value.ValueType;

/**
 * 将字符串向左侧或右侧对齐。如果字符串的长度小于期望长度则将在空位处补上指定的字符串，如果源字符串的长度超过期望的长度则将超出部分截断
 * 
 * @in 待对齐的字符串
 * @out 对齐之后的字符串
 * @postfix align 对齐方式，可选项为'l'或'r'，分别表示向左对齐还是向右对齐
 * @postfix padding 用于填补空位的字符串
 * @postfix len 期望的最终字符串长度
 * 
 * @example align='r', padding=' ', len=5
 *          给定的字符串为abc，则结果为 abc
 * @example align='r', padding='xy',len=8
 *          给定的字符串为123，则结果为xyxyx123
 * @example align='l', padding='a', len=2
 *          给定的字符串为qwerty，则结果为qw
 * 
 * @author ng
 * 
 */
@ExprDescription(name = "aligns", postfix = ExprPostfix.Complex, typeIn = ValueType.String, typeOut = ValueType.String,
        complexPostfixFields = {
                @ComplexPostfixField(name = "align", type = ExprPostfix.String),
                @ComplexPostfixField(name = "padding", type = ExprPostfix.String),
                @ComplexPostfixField(name = "len", type = ExprPostfix.Int) })
class AlignString extends Expr {

    @Override
    public Value calc(Value input, PackContext context) {
        String align = this.postfixMap.get("align").strValue;
        String padding = this.postfixMap.get("padding").strValue;
        int len = this.postfixMap.get("len").intValue;

        String str = input.strValue;

        if (align.equals("l"))
            // 左对齐
            str = this.alignLeft(str, padding, len);
        else
            str = this.alignRight(str, padding, len);

        return new Value(str);
    }

    /**
     * 将字符串左对齐
     * 
     * @param str
     * @param padding
     * @param len
     * @return
     */
    private String alignLeft(String str, String padding, int len) {
        if (str.length() > len)
            // 如果长度超出期望则将右侧截掉
            return str.substring(0, len);
        else
            // 如果长度小于期望则向右边填补
            return StringUtils.rightPad(str, len, padding);
    }

    /**
     * 将字符串右对齐
     * 
     * @param str
     * @param padding
     * @param len
     * @return
     */
    private String alignRight(String str, String padding, int len) {
        if (str.length() > len)
            // 如果长度超出长度则将左侧截掉
            return str.substring(str.length() - len, str.length());
        else
            // 如果长度小于期望则向左侧填补
            return StringUtils.leftPad(str, len, padding);
    }

    @Override
    public void check(Expr prev, StyleUnit styleUnit) {
        super.check(prev, styleUnit);

        String align = this.postfixMap.get("align").strValue;
        if (!align.equals("l") && !align.equals("r"))
            throw new KyouException("align must be 'l' or 'r'. align: " + align);

        String padding = this.postfixMap.get("padding").strValue;
        if (padding.isEmpty())
            throw new KyouException("padding is empty");
    }

}
