package net.nuclearg.kyou.pack.expr;

import net.nuclearg.kyou.pack.PackContext;
import net.nuclearg.kyou.pack.expr.ExprDescription.ExprPostfix;
import net.nuclearg.kyou.util.value.Value;
import net.nuclearg.kyou.util.value.ValueType;

import org.apache.commons.lang.math.NumberUtils;

/**
 * 定义一个整数字面量
 * <p>
 * 在很多时候需要一个确定的整数，例如组包时需要回退一个字节，这个“1”就需要想办法传给bk。
 * <p>
 * Kyou支持整数字面量。对于一个独立的数字，Kyou会将其解析为一个向外输出整数的表达式。数值的上限和下限与Integer类型表示的范围相同
 * </p>
 * 
 * @in 无
 * @out 输出一个由字面量表示的整数
 * @postfix 无
 * 
 * @author ng
 * 
 */
@ExprDescription(name = "<literal integer>", postfix = ExprPostfix.Int, typeIn = ValueType.Null, typeOut = ValueType.Integer)
class LiteralInteger extends Expr {

    public LiteralInteger(String value) {
        super.postfix = value;
        super.postfixi = NumberUtils.toInt(value);
        super.postfixMap = null;
    }

    @Override
    public Value eval(Value input, PackContext context) {
        return new Value(this.postfixi);
    }

}