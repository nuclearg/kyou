package net.nuclearg.kyou.util.lexer;

import java.util.regex.Pattern;

/**
 * 词法元素定义
 * <p>
 * 作为词法分析流程的基础，词法定义是最基本的东西，它定义了“词”的概念。Kyou支持基于正则表达式的词法定义。
 * <p>
 * 在对一个字符串执行词法解析时，通常需要提供一系列期望的词法元素定义，Kyou将会尝试将每个词法元素的正则与字符串进行匹配。<br/>
 * 如果匹配成功则把这个词截出来打包成一个{@link LexToken}返回，否则尝试给定的下一个词法元素定义。如果所有给定的词法元素定义均无法匹配则宣告词法解析失败。
 * </p>
 * 
 * @author ng
 * 
 */
public interface LexDefinition {
    /**
     * 该词法元素对应的正则表达式
     */
    public Pattern regex();

    /**
     * 经常有正则描述的字符串与实际期望的字符串不一致的情况，这里提供一个修改的后门
     */
    public String token(String selectedStr);
}