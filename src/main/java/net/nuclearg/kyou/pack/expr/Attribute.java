package net.nuclearg.kyou.pack.expr;

import net.nuclearg.kyou.pack.PackContext;
import net.nuclearg.kyou.pack.expr.ExprDescription.ExprPostfix;
import net.nuclearg.kyou.util.value.Value;
import net.nuclearg.kyou.util.value.ValueType;

import org.apache.commons.lang.StringUtils;

/**
 * 求报文元素的某个属性的值
 * 
 * @in 要被获取属性值的报文元素
 * @out 报文元素对应属性的值，如果该属性不存在则返回一个空字符串
 * @postfix 要获取的属性的名称
 * 
 * @author ng
 * 
 */
@ExprDescription(name = "a", postfix = ExprPostfix.String, typeIn = ValueType.Dom, typeOut = ValueType.String)
class Attribute extends Expr {

    @Override
    public Value eval(Value input, PackContext context) {
        return new Value(input.domValue.attr(this.postfix, StringUtils.EMPTY));
    }

}
