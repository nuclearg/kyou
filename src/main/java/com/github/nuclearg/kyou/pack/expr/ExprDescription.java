package com.github.nuclearg.kyou.pack.expr;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.nuclearg.kyou.pack.StyleExpr;
import com.github.nuclearg.kyou.util.value.ValueType;

/**
 * 标记在{@link StyleExpr}的实现类上，为{@link StyleExpr}的实现类提供一些静态信息。
 * 
 * @author ng
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExprDescription {
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
