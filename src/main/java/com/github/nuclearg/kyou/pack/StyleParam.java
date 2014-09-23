package com.github.nuclearg.kyou.pack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.github.nuclearg.kyou.KyouException;
import com.github.nuclearg.kyou.pack.expr.ExprDescription;
import com.github.nuclearg.kyou.pack.expr.ExprDescription.ComplexPostfixField;
import com.github.nuclearg.kyou.pack.expr.ExprDescription.ExprPostfix;
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
    private final List<StyleExpr> exprChain;

    StyleParam(String paramStr) {
        this.exprChain = StyleExpr.parseExprList(paramStr);
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
        for (StyleExpr expr : this.exprChain)
            value = expr.calc(value, context);

        return value;
    }

    /**
     * 获取整个参数的输出类型
     */
    public ValueType typeOut() {
        StyleExpr last = this.exprChain.get(this.exprChain.size() - 1);
        return last.getClass().getAnnotation(ExprDescription.class).typeOut();
    }

    /**
     * 检查参数是否有问题
     */
    void check(StyleUnit styleUnit) {
        // 检查各个表达式的正确性，以及与前一个表达式的衔接是否有问题
        for (int i = 0; i < this.exprChain.size(); i++) {
            StyleExpr expr = exprChain.get(i);
            StyleExpr prev = i > 0 ? exprChain.get(i - 1) : null;

            ExprDescription annotation = expr.getClass().getAnnotation(ExprDescription.class);

            // 检查自身的后缀是否有问题
            try {
                if (annotation.postfix() != ExprPostfix.Complex)
                    expr.postfix = checkPostfixValue(expr.postfix, annotation.postfix());
                else
                    checkComplexPostfix(expr.postfixMap, annotation);
            } catch (Exception ex) {
                throw new KyouException("expr postfix syntax error. expr: " + this, ex);
            }

            // 检查和前一环节的表达式之间的衔接是否有问题
            if (prev == null) {
                if (annotation.typeIn() != ValueType.Dom && annotation.typeIn() != ValueType.Null)
                    throw new KyouException("expr requires input. expr: " + this);
            } else {
                ValueType prevType = prev.getClass().getAnnotation(ExprDescription.class).typeOut();
                ValueType thisType = annotation.typeIn();

                if (prevType == ValueType.RefParam)
                    prevType = styleUnit.getParamType(expr.postfix.intValue);
                if (prevType != thisType)
                    throw new KyouException("expr input type mismatch. expr: " + this + ", prev: " + prev);
            }
        }
    }

    @Override
    public String toString() {
        List<StyleExpr> copy = new ArrayList<>(this.exprChain);
        Collections.reverse(copy);
        return StringUtils.join(copy, " ");
    }

    /**
     * 检查给定的值是否满足类型的要求
     * 
     * @param value
     * @param type
     * @return 检查过的值，可能有字符串到数字的转换
     */
    private static Value checkPostfixValue(Value value, ExprPostfix type) {
        if (value != null && value.type == ValueType.RefParam)
            // 对RefParam的检查放到运行期
            return value;

        if (type == ExprPostfix.Int || type == ExprPostfix.NoneOrInt)
            if (value != null)
                value = new Value(NumberUtils.toInt(value.strValue));

        switch (type) {
            case None:
                ensure(value == null, "value must empty");
                break;
            case Int:
                ensure(value != null, "value must not empty");
                ensure(value.type == ValueType.Integer && value.intValue >= 0, "value must non negative integer");
                break;
            case String:
                ensure(value != null && value.type == ValueType.String, "value must not empty");
                break;
            case NoneOrInt:
                ensure(value == null || (value.type == ValueType.Integer && value.intValue > 0), "value must empty or non negative integer");
                break;
            case NoneOrString:
                // 无需任何判断
                break;
            default:
                throw new UnsupportedOperationException("expr postfix type: " + type);
        }

        return value;
    }

    /**
     * 检查复杂后缀
     */
    private static void checkComplexPostfix(Map<String, Value> map, ExprDescription desc) {
        for (ComplexPostfixField field : desc.complexPostfixFields()) {
            Value value = map.get(field.name());

            try {
                value = checkPostfixValue(value, field.type());
                map.put(field.name(), value);
            } catch (Exception ex) {
                throw new KyouException("postfix check fail. field: " + field.name() + ", type: " + field.type() + ", value: " + value, ex);
            }

        }
    }

    /**
     * 工具方法，不满足条件就抛异常
     */
    private static void ensure(boolean result, String err) {
        if (!result)
            throw new KyouException(err);
    }

}
