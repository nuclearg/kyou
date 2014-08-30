package net.nuclearg.kyou.util;

/**
 * 只有一个入参的回调函数
 * 
 * @author ng
 * 
 * @param <T>
 *            参数类型
 */
public interface KyouFunction<T> {
    public void action(T arg);
}