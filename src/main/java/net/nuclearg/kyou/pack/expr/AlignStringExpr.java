package net.nuclearg.kyou.pack.expr;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.pack.Expr;
import net.nuclearg.kyou.pack.Expr.ExprDescription;
import net.nuclearg.kyou.pack.Expr.ExprDescription.ComplexPostfixField;
import net.nuclearg.kyou.pack.Expr.ExprDescription.ExprPostfix;
import net.nuclearg.kyou.pack.PackContext;
import net.nuclearg.kyou.util.value.Value;
import net.nuclearg.kyou.util.value.ValueType;

import org.apache.commons.lang.StringUtils;

/**
 * 将字符串向左侧或右侧对齐。如果字符串的长度小于期望长度则将在空位处补上指定的字符串，如果源字符串的长度超过期望的长度则将超出部分截断
 * <p>
 * 例如:<br/>
 * 指定align='r', padding=' ', len=5，给定的字符串为abc，则结果为 abc<br/>
 * 指定align='r', padding='xy',len=8，给定的字符串为123，则结果为xyxyx123<br/>
 * 指定align='l', padding='a', len=2，给定的字符串为qwerty，则结果为qw
 * </p>
 * 
 * 
 * @in 待对齐的字符串
 * @out 对齐之后的字符串
 * @postfix align 对齐方式，可选项为'l'或'r'，分别表示向左对齐还是向右对齐
 * @postfix padding 用于填补空位的字符串
 * @postfix len 期望的最终字符串长度
 * 
 * @author ng
 * 
 */
@ExprDescription(name = "aligns", postfix = ExprPostfix.Complex, typeIn = ValueType.String, typeOut = ValueType.String,
        complexPostfixFields = {
                @ComplexPostfixField(name = "align", type = ExprPostfix.String),
                @ComplexPostfixField(name = "padding", type = ExprPostfix.String),
                @ComplexPostfixField(name = "len", type = ExprPostfix.Int) })
class AlignStringExpr extends Expr {

    @Override
    protected Value eval(Value input, PackContext context) {
        String align = (String) this.postfixMap.get("align");
        String padding = (String) this.postfixMap.get("padding");
        int len = (Integer) this.postfixMap.get("len");

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
    protected void check(Expr prev) {
        super.check(prev);

        String align = (String) this.postfixMap.get("align");
        if (!align.equals("l") && !align.equals("r"))
            throw new KyouException("align must be 'l' or 'r'. align: " + align);

        String padding = (String) this.postfixMap.get("padding");
        if (padding.isEmpty())
            throw new KyouException("padding is empty");
    }

}
