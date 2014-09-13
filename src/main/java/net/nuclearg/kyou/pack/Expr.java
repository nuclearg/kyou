package net.nuclearg.kyou.pack;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.pack.Expr.ExprDescription.ComplexPostfixField;
import net.nuclearg.kyou.pack.Expr.ExprDescription.ExprPostfix;
import net.nuclearg.kyou.pack.ExprListString.ExprInfo;
import net.nuclearg.kyou.util.ClassUtils;
import net.nuclearg.kyou.util.value.Value;
import net.nuclearg.kyou.util.value.ValueType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

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
    private static final Map<String, Class<? extends Expr>> EXPR_CLASSES;
    /**
     * 表达式的后缀 如果未提供后缀则为null
     */
    protected String postfix;
    /**
     * 表达式的后缀，整数形式。如果未提供后缀，或后缀不是整形则为-1
     */
    protected int postfixi;
    /**
     * 表达式的后缀，如果是{@link ExprPostfix#Complex}类型则将后缀解析为map，否则为null
     */
    protected Map<String, Object> postfixMap;

    /**
     * 计算该表达式
     * 
     * @param input
     *            输入值
     * @param context
     *            组包上下文
     * @return 表达式的计算结果
     */
    protected abstract Value eval(Value input, PackContext context);

    static {
        Map<String, Class<? extends Expr>> exprClasses = new HashMap<>();

        Set<Class<?>> classes = ClassUtils.searchClassesWithAnnotation(ExprDescription.class);
        for (Class<?> cls : classes) {
            ExprDescription desc = cls.getAnnotation(ExprDescription.class);
            if (desc == null)
                continue;

            String name = desc.name();

            if (exprClasses.containsKey(name))
                throw new KyouException("expr name duplicated. old: " + exprClasses.get(name) + ", new: " + cls);

            exprClasses.put(name, cls.asSubclass(Expr.class));
        }

        EXPR_CLASSES = Collections.unmodifiableMap(exprClasses);
    }

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
                checkValue(this.postfix, this.postfixi, annotation.postfix());
            else
                checkComplexPostfix(this.postfixMap, annotation);
        } catch (Exception ex) {
            throw new KyouException("expr postfix syntax error. expr: " + this, ex);
        }

        // 检查和前一环节的表达式之间的衔接是否有问题
        if (prev == null) {
            if (annotation.typeIn() != ValueType.Dom)
                throw new KyouException("expr requires input. expr: " + this);
        } else if (annotation.typeIn() != prev.getClass().getAnnotation(ExprDescription.class).typeOut())
            throw new KyouException("expr input type mismatch. expr: " + this + ", prev: " + prev);
    }

    /**
     * 检查给定的值是否满足类型的要求
     * 
     * @param value
     * @param type
     * @return 如果给定的值可以转为数字，则返回对应的数字，否则返回-1
     */
    private static void checkValue(String value, int valuei, ExprPostfix type) {
        switch (type) {
            case None:
                ensure(value == null, "value must empty");
                break;
            case Int:
                ensure(value != null, "value must not empty");
                ensure(valuei >= 0, "value must non negative integer");
                break;
            case String:
                ensure(value != null, "value must not empty");
                break;
            case NoneOrInt:
                ensure(value == null || valuei > 0, "value must empty or non negative integer");
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
    private static void checkComplexPostfix(Map<String, Object> map, ExprDescription desc) {
        for (ComplexPostfixField field : desc.complexPostfixFields()) {
            String value = (String) map.get(field.name());
            int valuei = NumberUtils.toInt(value, -1);

            try {
                checkValue(value, valuei, field.type());
            } catch (Exception ex) {
                throw new KyouException("postfix check fail. field: " + field.name() + ", type: " + field.type() + ", value: " + value, ex);
            }

            if (field.type() == ExprPostfix.Int || field.type() == ExprPostfix.NoneOrInt)
                map.put(field.name(), valuei);
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
     * 根据本体的字符串形式构建一个表达式实例
     * 
     * @param exprInfo
     *            表达式信息
     * @return 表达式实例
     */
    static Expr buildExpr(ExprInfo exprInfo) {
        // 判断是否是整数字面量
        if (StringUtils.isNumeric(exprInfo.name))
            return new IntegerExpr(exprInfo.name);

        // 根据body找到对应的类型
        Class<? extends Expr> exprClass = EXPR_CLASSES.get(exprInfo.name);
        if (exprClass == null)
            throw new KyouException("expression unsupported. name: " + exprInfo.name);

        // 创建expr实例
        Expr expr;
        try {
            expr = ClassUtils.newInstance(exprClass);

            expr.postfix = exprInfo.postfix;
            expr.postfixi = NumberUtils.toInt(expr.postfix, -1);

            if (exprInfo.complexPostfix == null)
                return expr;

            expr.postfixMap = new HashMap<>();
            for (Entry<String, String> entry : exprInfo.complexPostfix.entrySet())
                expr.postfixMap.put(entry.getKey(), entry.getValue());

            return expr;
        } catch (Exception ex) {
            throw new KyouException("init expression fail. name: " + exprInfo.name, ex);
        }
    }

    /**
     * 标记在{@link Expr}的实现类上，为{@link Expr}的实现类提供一些静态信息。
     * 
     * @author ng
     */
    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface ExprDescription {
        /**
         * 表达式的名称
         */
        String name();

        /**
         * 表达式需要的后缀类型
         */
        ExprPostfix postfix();

        /**
         * 复杂表达式的字段列表
         */
        ComplexPostfixField[] complexPostfixFields() default {};

        /**
         * 输入数据的类型
         */
        ValueType typeIn();

        /**
         * 输出数据的类型
         */
        ValueType typeOut();

        /**
         * 定义了表达式的后缀类型
         * 
         * @author ng
         */
        public static enum ExprPostfix {
            /**
             * 表达式不应有后缀
             */
            None,

            /**
             * 表达式应当有一个字符串类型的后缀
             */
            String,
            /**
             * 表达式应当有一个整数类型的后缀
             */
            Int,

            /**
             * 表达式可以没有后缀，或有一个字符串类型的后缀
             */
            NoneOrString,
            /**
             * 表达式可以没有后缀，或有一个整数类型的后缀
             */
            NoneOrInt,
            /**
             * 表达式拥有一个复杂类型的后缀
             */
            Complex,
        }

        /**
         * 复杂后缀的描述
         * 
         * @author ng
         * 
         */
        public static @interface ComplexPostfixField {
            String name();

            ExprPostfix type();
        }
    }

    /**
     * 输出一个立即数
     * 
     * @author ng
     * 
     */
    @ExprDescription(name = "", postfix = ExprPostfix.Int, typeIn = ValueType.Dom, typeOut = ValueType.Integer)
    private static class IntegerExpr extends Expr {

        public IntegerExpr(String value) {
            super.postfix = value;
            super.postfixi = NumberUtils.toInt(value);
            super.postfixMap = null;
        }

        @Override
        protected Value eval(Value input, PackContext context) {
            return new Value(this.postfixi);
        }

    }

}
