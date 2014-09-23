package com.github.nuclearg.kyou.pack;

import java.util.List;

import com.github.nuclearg.kyou.pack.expr.Expr;
import com.github.nuclearg.kyou.pack.expr.ExprDescription;
import com.github.nuclearg.kyou.util.value.Value;
import com.github.nuclearg.kyou.util.value.ValueType;

/**
 * 参数
 * 
 * @author ng
 * 
 */
public class StyleParam {
    /**
     * 实际的表达式列表
     */
    private final List<Expr> exprChain;

    StyleParam(String paramStr) {
        this.exprChain = Expr.parseExprList(paramStr);
    }

    /**
     * 计算参数的值
     * 
     * @return
     */
    public Value calc(PackContext context) {
        // 向表达式链输入的最初的值是正被组包的当前报文节点
        Value value = new Value(context.item);

        // 沿着表达式链一直计算，最开始的输入为空，
        for (Expr expr : this.exprChain)
            value = expr.calc(value, context);

        return value;
    }

    /**
     * 获取整个参数的输出类型
     */
    public ValueType typeOut() {
        Expr last = this.exprChain.get(this.exprChain.size() - 1);
        return last.getClass().getAnnotation(ExprDescription.class).typeOut();
    }

    /**
     * 
     * @param styleUnit
     */
    public void check(StyleUnit styleUnit) {
        // 检查各个表达式的正确性，以及与前一个表达式的衔接是否有问题
        for (int i = 0; i < this.exprChain.size(); i++) {
            Expr expr = exprChain.get(i);
            Expr prev = i > 0 ? exprChain.get(i - 1) : null;

            expr.check(prev, styleUnit);
        }

    }
}
