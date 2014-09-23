package com.github.nuclearg.kyou.pack.expr;

import com.github.nuclearg.kyou.KyouException;
import com.github.nuclearg.kyou.dom.KyouContainer;
import com.github.nuclearg.kyou.dom.KyouItem;
import com.github.nuclearg.kyou.pack.PackContext;
import com.github.nuclearg.kyou.pack.expr.ExprDescription.ExprPostfix;
import com.github.nuclearg.kyou.util.value.Value;
import com.github.nuclearg.kyou.util.value.ValueType;

/**
 * 计算当前报文元素的所有子元素组包的结果
 * <p>
 * 这将遍历该报文，对其中的每个成员进行组包，并把组包好的报文直接拼到一起返回来
 * </p>
 * 
 * @in 要被计算子元素的报文字段
 * @out 报文容器中所有子元素组包组出来的字节流。如果输入的报文元素不是一个容器则报错，如果容器为空则返回一个空的字节数组
 * 
 * @author ng
 */
@ExprDescription(name = "m", postfix = ExprPostfix.None, typeIn = ValueType.Dom, typeOut = ValueType.Bytes)
class Member extends SimplePostfixExpr {

    @Override
    public Value calc(Value input, PackContext context, Value postfix) {
        KyouItem item = input.domValue;
        if (!(item instanceof KyouContainer))
            throw new KyouException("KyouContainer expected. path: " + item.path());

        KyouContainer container = (KyouContainer) item;

        return new Value(context.packer.packMember(container, context));
    }
}
