package com.github.nuclearg.kyou.pack.expr;

import org.apache.commons.lang.StringUtils;

import com.github.nuclearg.kyou.pack.PackContext;
import com.github.nuclearg.kyou.pack.expr.ExprDescription.ExprPostfix;
import com.github.nuclearg.kyou.util.value.Value;
import com.github.nuclearg.kyou.util.value.ValueType;

/**
 * 求报文元素的某个属性的值
 * <p>
 * 如果属性的名称符合节点名称的语法，则可以直接获取，例如a.attr1<br/>
 * 否则需要使用一个字符串，例如a.'属性A'
 * </p>
 * 
 * @in 要被获取属性值的报文元素
 * @out 报文元素对应属性的值，如果该属性不存在则返回一个空字符串
 * @postfix 要获取的属性的名称
 * 
 * @example a.aaa
 *          对于field[aaa='3', bbb='4']，返回"3"的字符串
 * @example a.'属性A'
 *          对于field['属性A'='3', '属性B'='4']，返回"3"的字符串
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
