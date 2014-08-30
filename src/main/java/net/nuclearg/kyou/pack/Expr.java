package net.nuclearg.kyou.pack;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.util.KyouValue;
import net.nuclearg.kyou.util.KyouValueType;

/**
 * 表示在参数中可以定义的表达式
 * <p>
 * 表达式用于将报文元素的有关信息通过指定的方式计算出来后传给相应的参数段以输出到报文流中。表达式获取一个值，经过计算之后输出另一个值，由多个表达式连接起来最终生成报文中的一个片段
 * <p>
 * 各种表达式可以在保证类型正确的前提下自由组合，组合方法是将表达式前后并列，以空格分隔。<br/>
 * 此时后面的表达式的计算结果将会传给前面的表达式。即计算顺序是从后往前的。<br/>
 * 表达式有各自的输入类型和输出类型，需要确保前后相邻的表达式的输出类型和输入类型匹配。<br/>
 * <li>需要确保整个表达式链的最初始的表达式的输入类型是{@link KyouValueType#Dom}</li>
 * <li>需要确保整个表达式链的最终结的表达式的输出类型是{@link KyouValueType#Bytes}或{@link KyouValueType#Backspace}</li>
 * <li>特别的，可以使用整数字面量。整数字面量将作为一个输入类型为{@link KyouValueType#Dom}，输出类型为{@link KyouValueType#Integer}的表达式参与运算</li>
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
    /**
     * 表达式的后缀 如果未提供后缀则为null
     */
    protected String postfix;
    /**
     * 表达式的后缀，整数形式。如果未提供后缀，或后缀不是整形则为-1
     */
    protected int postfixi;

    /**
     * 计算该表达式
     * 
     * @param input
     *            输入值
     * @param context
     *            组包上下文
     * @return 表达式的计算结果
     */
    protected abstract KyouValue eval(KyouValue input, PackContext context);

    /**
     * 检查当前表达式是否存在问题，以及和前一个表达式之间的衔接是否有问题
     * 
     * @param prev
     *            前一个表达式，即输出结果将作为当前表达式的输入的表达式
     */
    protected void check(Expr prev) {
        ExprDescription annotation = this.getClass().getAnnotation(ExprDescription.class);

        // 检查自身是否有问题
        switch (annotation.postfix()) {
            case None:
                check(this.postfix == null, "postfix must empty");
                break;
            case Int:
                check(this.postfix != null, "postfix must not empty");
                check(this.postfixi >= 0, "postfix must non negative integer");
                break;
            case String:
                check(this.postfix != null, "postfix must not empty");
                break;
            case NoneOrInt:
                check(this.postfix == null || this.postfixi > 0, "postfix must empty or non negative integer");
                break;
            case NoneOrString:
                // 无需任何判断
                break;
            default:
                throw new UnsupportedOperationException("expr postfix type: " + annotation.postfix());
        }

        // 检查和前一环节的表达式之间的衔接是否有问题
        if (prev == null && annotation.typeIn() != KyouValueType.Dom)
            throw new KyouException("expr requires input. expr: " + this);
        if (annotation.typeIn() != prev.getClass().getAnnotation(ExprDescription.class).typeOut())
            throw new KyouException("expr input type mismatch. expr: " + this + ", prev: " + prev);
    }

    /**
     * 工具方法，不满足条件就抛异常
     */
    private void check(boolean result, String err) {
        if (!result)
            throw new KyouException(err + " expr: " + this);
    }

    @Override
    public String toString() {
        String body = this.getClass().getAnnotation(ExprDescription.class).name();
        if (this.postfix == null)
            return body;
        else
            return body + "." + this.postfix;
    }

    /**
     * 标记在{@link Expr}的实现类上，为Param的实现类提供一些静态信息。
     * 
     * @author ng
     */
    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface ExprDescription {
        /**
         * 参数的主体
         */
        String name();

        /**
         * 参数需要的后缀类型
         */
        ExprPostfix postfix();

        /**
         * 输入的类型
         */
        KyouValueType typeIn();

        /**
         * 输出的类型
         */
        KyouValueType typeOut();

        /**
         * 定义了参数的后缀类型
         * 
         * @author ng
         */
        public static enum ExprPostfix {
            /**
             * 表示参数不应有后缀
             */
            None,

            /**
             * 表示参数应当有一个字符串类型的后缀
             */
            String,
            /**
             * 表示参数应当有一个整数类型的后缀
             */
            Int,

            /**
             * 表示参数可以没有后缀，或有一个字符串类型的后缀
             */
            NoneOrString,
            /**
             * 表示参数可以没有后缀，或有一个整数类型的后缀
             */
            NoneOrInt,
        }
    }

    /**
     * 将表达式字符串解析为body和postfix两部分
     * 
     * @param expr
     *            表达式字符串
     * @return 一个包含两项的数组，第0项表示body，第1项表示postfix。如果没有postfix则第1项为null
     */
    static String[] parseBodyPostfix(String expr) {
        String[] parts = new String[2];
        if (expr.contains(".")) {
            int pos = expr.indexOf('.');
            parts[0] = expr.substring(0, pos);
            parts[1] = expr.substring(pos + 1);
        } else {
            parts[0] = expr;
            parts[1] = null;
        }

        return parts;
    }

    /**
     * 将一个字符串解析为表达式
     * 
     * @param exprStr
     *            要解析为表达式的字符串
     * @return 表达式
     */
    static Expr parseExpr(String exprStr) {

        return null;
    }
}
