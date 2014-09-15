package com.github.nuclearg.kyou.pack.expr;

import com.github.nuclearg.kyou.KyouException;
import com.github.nuclearg.kyou.dom.KyouContainer;
import com.github.nuclearg.kyou.pack.PackContext;
import com.github.nuclearg.kyou.pack.expr.ExprDescription.ExprPostfix;
import com.github.nuclearg.kyou.util.value.Value;
import com.github.nuclearg.kyou.util.value.ValueType;

/**
 * 求当前节点的父节点
 * 
 * @in 要被获取父节点的报文元素
 * @out 输入节点的父节点。如果该节点没有父节点（报文文档树的树根）则报错
 * 
 * @author ng
 * 
 */
@ExprDescription(name = "p", postfix = ExprPostfix.None, typeIn = ValueType.Dom, typeOut = ValueType.Dom)
class Parent extends Expr {

    @Override
    public Value eval(Value input, PackContext context) {
        KyouContainer parent = input.domValue.parent();

        if (parent == null || parent == input.domValue)
            throw new KyouException("item has no parent. item: " + input.domValue);

        return new Value(input.domValue.parent());
    }

}
