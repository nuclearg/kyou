package net.nuclearg.kyou.pack.expr;

import net.nuclearg.kyou.pack.PackContext;
import net.nuclearg.kyou.pack.expr.ExprDescription.ExprPostfix;
import net.nuclearg.kyou.util.value.Value;
import net.nuclearg.kyou.util.value.ValueType;

/**
 * 从最终输出的报文字节流中回退指定的字节
 * <p>
 * 在处理数组或结构体时，有很多情况下需要面对最后一个元素的行为与其它元素不同的情况，例如json中最后一个元素的未尾没有逗号。<br/>
 * 这时比较简单的办法就是直接把最后生成的那个逗号回退掉，所以Kyou提供了这个表达式，用于从报文流中回退指定数量的字节
 * </p>
 * 
 * @in 要回退的字节数
 * @out 回退的动作。表示该“回退”动作的输出无法作为任何其它表达式的输入
 * 
 * @author ng
 * 
 */
@ExprDescription(name = "bk", postfix = ExprPostfix.None, typeIn = ValueType.Integer, typeOut = ValueType.Backspace)
class Backspace extends Expr {

    @Override
    public Value eval(Value input, PackContext context) {
        return new Value(ValueType.Backspace, null, input.intValue, null, null);
    }

}
