package com.github.nuclearg.kyou.pack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.github.nuclearg.kyou.KyouException;
import com.github.nuclearg.kyou.pack.StyleExprString.ExprInfo;
import com.github.nuclearg.kyou.pack.StyleExprString.ExprPostfixValueInfo;
import com.github.nuclearg.kyou.pack.expr.ExprDescription;
import com.github.nuclearg.kyou.pack.expr.ExprDescription.ExprPostfix;
import com.github.nuclearg.kyou.pack.expr.LiteralInteger;
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
public abstract class StyleExpr {
    private static final Map<String, Class<? extends StyleExpr>> EXPR_CLASSES = ClassUtils.buildAnnotatedClassMap(ExprDescription.class, StyleExpr.class, new AnnotationNameParser<ExprDescription>() {

        @Override
        public String parseName(ExprDescription annotation) {
            return annotation.name();
        }
    });

    /**
     * 表达式的后缀 如果未提供后缀则为null
     */
    Value postfix;
    /**
     * 表达式的后缀，如果是{@link ExprPostfix#Complex}类型则将后缀解析为map，否则为null
     */
    Map<String, Value> postfixMap;

    /**
     * 计算该表达式
     * 
     * @param input
     *            输入值
     * @param context
     *            组包上下文
     * @return 表达式的计算结果
     */
    Value calc(Value input, PackContext context) {
        if (this.postfixMap == null) { // 简单后缀
            Value postfix = this.postfix;

            // 解析引用
            if (postfix != null && postfix.type == ValueType.RefParam)
                postfix = context.packer.calcParamValue(postfix.intValue, context);

            // 执行计算
            return this.calc(input, context, postfix);
        } else {
            Map<String, Value> postfixMap = new LinkedHashMap<>();

            // 解析引用
            for (Entry<String, Value> entry : this.postfixMap.entrySet())
                if (entry.getValue() != null && entry.getValue().type == ValueType.RefParam)
                    postfixMap.put(entry.getKey(), context.packer.calcParamValue(entry.getValue().intValue, context));
                else
                    postfixMap.put(entry.getKey(), entry.getValue());

            // 执行计算
            return this.calc(input, context, postfixMap);
        }
    }

    /**
     * 计算表达式
     * 
     * @param input
     *            输入值
     * @param context
     *            组包上下文
     * @param postfix
     *            简单后缀
     * @return 计算结果
     */
    protected abstract Value calc(Value input, PackContext context, Value postfix);

    /**
     * 计算表达式
     * 
     * @param input
     *            输入值
     * @param context
     *            组包上下文
     * @param postfixMap
     *            复杂后缀
     * @return 计算结果
     */
    protected abstract Value calc(Value input, PackContext context, Map<String, Value> postfixMap);

    @Override
    public String toString() {
        String name = this.getClass().getAnnotation(ExprDescription.class).name();
        if (this.postfixMap != null)
            return name + this.postfixMap;
        else if (this.postfix == null)
            return name;
        else
            return name + "." + this.postfix;
    }

    /**
     * 解析参数字符串并创建一系列表达式
     * 
     * @param str
     *            参数字符串
     * @return 解析出来的表达式链
     */
    static List<StyleExpr> parseExprList(String str) {
        if (StringUtils.isBlank(str))
            throw new KyouException("param is blank");

        List<StyleExpr> exprChain = new ArrayList<>();

        // 解析参数字符串
        StyleExprString paramStr = new StyleExprString(str);
        for (ExprInfo exprInfo : paramStr.parseExprInfo()) {
            // 判断是否是整数字面量
            if (StringUtils.isNumeric(exprInfo.name)) {
                exprChain.add(new LiteralInteger(exprInfo.name));
                continue;
            }

            // 创建expr实例
            try {
                StyleExpr expr = ClassUtils.newInstance(EXPR_CLASSES, exprInfo.name);
                if (expr == null)
                    throw new KyouException("expression unsupported. name: " + exprInfo.name);

                if (exprInfo.postfix == null)
                    expr.postfix = null;
                else if (exprInfo.postfix.ref < 0)
                    expr.postfix = new Value(exprInfo.postfix.value);
                else
                    expr.postfix = new Value(ValueType.RefParam, null, exprInfo.postfix.ref, null, null);

                if (exprInfo.complexPostfix == null) {
                    exprChain.add(expr);
                    continue;
                }

                expr.postfixMap = new HashMap<>();
                for (Entry<String, ExprPostfixValueInfo> entry : exprInfo.complexPostfix.entrySet())
                    if (entry.getValue().ref < 0)
                        expr.postfixMap.put(entry.getKey(), new Value(entry.getValue().value));
                    else
                        expr.postfixMap.put(entry.getKey(), new Value(ValueType.RefParam, null, entry.getValue().ref, null, null));

                exprChain.add(expr);
            } catch (Exception ex) {
                throw new KyouException("init expression fail. name: " + exprInfo.name, ex);
            }
        }

        // 因为在实际组包时，是从最后一个表达式开始计算的，为省事现在在这里直接先倒序一下
        Collections.reverse(exprChain);

        return Collections.unmodifiableList(exprChain);
    }

}
