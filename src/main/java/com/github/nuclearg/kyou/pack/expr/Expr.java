package com.github.nuclearg.kyou.pack.expr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.github.nuclearg.kyou.KyouException;
import com.github.nuclearg.kyou.pack.PackContext;
import com.github.nuclearg.kyou.pack.expr.ExprDescription.ComplexPostfixField;
import com.github.nuclearg.kyou.pack.expr.ExprDescription.ExprPostfix;
import com.github.nuclearg.kyou.pack.expr.ExprString.ExprInfo;
import com.github.nuclearg.kyou.pack.expr.ExprString.ExprPostfixValueInfo;
import com.github.nuclearg.kyou.util.ClassUtils;
import com.github.nuclearg.kyou.util.ClassUtils.AnnotationNameParser;
import com.github.nuclearg.kyou.util.value.Value;
import com.github.nuclearg.kyou.util.value.ValueType;

/**
 * 表示在参数中可以定义的表达式
 * <p>
 * 表达式用于将报文元素的有关信息通过指定的方式计算出来后传给相应的参数段以输出到报文流中。表达式获取一个值，经过计算之后输出另一个值，由多个表达式连接起来最终生成报文中的一个片段
 * <p>
 * 各种表达式可以在保证类型正确的前提下自由组合，组合方法是将表达式前后并列，以空格分隔。<br/>
 * 此时后面的表达式的计算结果将会传给前面的表达式。即计算顺序是从后往前的。<br/>
 * 表达式有各自的输入类型和输出类型，需要确保前后相邻的表达式的输出类型和输入类型匹配。<br/>
 * <li>需要确保整个表达式链的最初始的表达式的输入类型是{@link ValueType#Dom}</li>
 * <li>需要确保整个表达式链的最终结的表达式的输出类型是{@link ValueType#Bytes}或{@link ValueType#Backspace}</li>
 * <li>特别的，可以使用整数字面量。整数字面量将作为一个输入类型为{@link ValueType#Dom}，输出类型为{@link ValueType#Integer}的表达式参与运算</li>
 * </p>
 * <p>
 * 例：
 * <li>n 表示求当前报文元素的名称</li>
 * <li>v 表示求当前报文元素的值</li>
 * <li>lens n 表示求当前报文元素的名称的长度</li>
 * <li>lenb s2b.utf8 v 表示求当前报文元素的值，并以utf8编码转成字节数组，并返回这个字节数组的长度</li>
 * </p>
 * 
 * @author ng
 */
public abstract class Expr {
    private static final Map<String, Class<? extends Expr>> EXPR_CLASSES = ClassUtils.buildAnnotatedClassMap(ExprDescription.class, Expr.class, new AnnotationNameParser<ExprDescription>() {

        @Override
        public String parseName(ExprDescription annotation) {
            return annotation.name();
        }
    });

    /**
     * 表达式的后缀 如果未提供后缀则为null
     */
    protected Value postfix;
    /**
     * 表达式的后缀，如果是{@link ExprPostfix#Complex}类型则将后缀解析为map，否则为null
     */
    protected Map<String, Value> postfixMap;

    /**
     * 计算该表达式
     * 
     * @param input
     *            输入值
     * @param context
     *            组包上下文
     * @return 表达式的计算结果
     */
    public abstract Value eval(Value input, PackContext context);

    /**
     * 检查当前表达式是否存在问题，以及和前一个表达式之间的衔接是否有问题
     * 
     * @param prev
     *            前一个表达式，即输出结果将作为当前表达式的输入的表达式
     */
    protected void check(Expr prev) {
        ExprDescription annotation = this.getClass().getAnnotation(ExprDescription.class);

        // 检查自身的后缀是否有问题
        try {
            if (annotation.postfix() != ExprPostfix.Complex)
                checkPostfixValue(this.postfix, annotation.postfix());
            else
                checkComplexPostfix(this.postfixMap, annotation);
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

            if (prevType != thisType)
                // TODO 对Auto类型进行支持
                throw new KyouException("expr input type mismatch. expr: " + this + ", prev: " + prev);
        }
    }

    /**
     * 检查给定的值是否满足类型的要求
     * 
     * @param value
     * @param type
     * @return 如果给定的值可以转为数字，则返回对应的数字，否则返回-1
     */
    private static void checkPostfixValue(Value value, ExprPostfix type) {
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
    }

    /**
     * 检查复杂后缀
     */
    private static void checkComplexPostfix(Map<String, Value> map, ExprDescription desc) {
        for (ComplexPostfixField field : desc.complexPostfixFields()) {
            Value value = map.get(field.name());

            if (field.type() == ExprPostfix.Int || field.type() == ExprPostfix.NoneOrInt) {
                if (value != null)
                    map.put(field.name(), value = new Value(NumberUtils.toInt(value.strValue)));
            }

            try {
                checkPostfixValue(value, field.type());
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

    @Override
    public String toString() {
        String name = this.getClass().getAnnotation(ExprDescription.class).name();
        if (this.postfix == null)
            return name;
        else
            return name + "." + this.postfix;
    }

    /**
     * 解析参数字符串并创建一系列表达式
     * 
     * @param str
     *            参数字符串
     * @return 解析出来的表达式列表
     */
    public static List<Expr> parseExprList(String str) {
        if (StringUtils.isBlank(str))
            throw new KyouException("param is blank");

        List<Expr> exprChain = new ArrayList<>();

        // 解析参数字符串
        ExprString paramStr = new ExprString(str);
        for (ExprInfo exprInfo : paramStr.parseExprInfo())
            exprChain.add(Expr.buildExpr(exprInfo));

        // 因为在实际组包时，是从最后一个表达式开始计算的，为省事现在在这里直接先倒序一下
        Collections.reverse(exprChain);

        // 检查各个表达式的正确性，以及与前一个表达式的衔接是否有问题
        for (int i = 0; i < exprChain.size(); i++) {
            Expr expr = exprChain.get(i);
            Expr prev = i > 0 ? exprChain.get(i - 1) : null;

            expr.check(prev);
        }
        // 检查最后一个表达式的输出是不是字节数组或Backspace
        Expr last = exprChain.get(exprChain.size() - 1);
        ValueType lastOutput = last.getClass().getAnnotation(ExprDescription.class).typeOut();
        if (lastOutput != ValueType.Bytes && lastOutput != ValueType.Backspace)
            throw new KyouException("last expr must return Bytes or Backspace but " + lastOutput);

        return Collections.unmodifiableList(exprChain);
    }

    /**
     * 根据本体的字符串形式构建一个表达式实例
     * 
     * @param exprInfo
     *            表达式信息
     * @return 表达式实例
     */
    static Expr buildExpr(ExprInfo exprInfo) {
        // 判断是否是整数字面量
        if (StringUtils.isNumeric(exprInfo.name))
            return new LiteralInteger(exprInfo.name);

        // 创建expr实例
        try {
            Expr expr = ClassUtils.newInstance(EXPR_CLASSES, exprInfo.name);
            if (expr == null)
                throw new KyouException("expression unsupported. name: " + exprInfo.name);

            if (exprInfo.postfix == null)
                expr.postfix = null;
            else if (exprInfo.postfix.ref < 0)
                expr.postfix = new Value(exprInfo.postfix.value);
            else
                expr.postfix = new Value(ValueType.RefParam, null, exprInfo.postfix.ref, null, null);

            if (exprInfo.complexPostfix == null)
                return expr;

            expr.postfixMap = new HashMap<>();
            for (Entry<String, ExprPostfixValueInfo> entry : exprInfo.complexPostfix.entrySet())
                if (entry.getValue().ref < 0)
                    expr.postfixMap.put(entry.getKey(), new Value(entry.getValue().value));
                else
                    expr.postfixMap.put(entry.getKey(), new Value(ValueType.RefParam, null, entry.getValue().ref, null, null));
            return expr;
        } catch (Exception ex) {
            throw new KyouException("init expression fail. name: " + exprInfo.name, ex);
        }
    }

}
