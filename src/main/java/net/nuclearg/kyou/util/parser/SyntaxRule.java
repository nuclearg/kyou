package net.nuclearg.kyou.util.parser;

import net.nuclearg.kyou.util.lexer.LexTokenDefinition;
import net.nuclearg.kyou.util.lexer.LexTokenString;

/**
 * 语法定义
 * 
 * @author ng
 * 
 * @param <T>
 */
public abstract class SyntaxRule<L extends LexTokenDefinition> {
    /**
     * 判断字符串是否与语法规则相匹配
     * 
     * @param tokenStr
     *            进行语法分析的字符串
     * @return 如果给定的字符串满足语法规则，则返回分析结果，否则返回null
     */
    abstract <S extends SyntaxUnitDefinition<L>> SyntaxUnit<L, S> match(LexTokenString tokenStr);

    @Override
    public abstract String toString();

    /**
     * 创建一个基于词的语法规则
     * 
     * @param tokenType
     *            词法定义
     * @return 基于词的语法规则
     */
    public static <L extends LexTokenDefinition> SyntaxRule<L> lex(L tokenType) {
        return new LexRule<L>(tokenType);
    }

    /**
     * 创建一个引用其它语法规则的语法规则
     * 
     * @param type
     *            其它的语法规则
     * @return 引用其它语法规则的语法规则
     */
    public static <L extends LexTokenDefinition> SyntaxRule<L> ref(SyntaxUnitDefinition<L> type) {
        return new RefRule<L>(type);
    }

    /**
     * 创建一个顺序连接多个语法规则的语法规则
     * 
     * @param elements
     *            子元素的语法规则列表
     * @return 顺序连接多个语法规则的语法规则
     */
    public static <L extends LexTokenDefinition> SyntaxRule<L> seq(SyntaxRule<L>... elements) {
        return new SequenceRule<L>(elements);
    }

    /**
     * 
     * @param tokenType
     * @return
     */
    public static <L extends LexTokenDefinition> SyntaxRule<L> optional(SyntaxRule<L> body) {
        return new OptionalRule<L>(body);
    }

    /**
     * 
     * @param conditions
     * @return
     */
    public static <L extends LexTokenDefinition> SyntaxRule<L> or(SyntaxRule<L>... conditions) {
        return new OrRule<L>(conditions);
    }

    /**
     * 
     * @param body
     * @return
     */
    public static <L extends LexTokenDefinition> SyntaxRule<L> repeat(SyntaxRule<L> body) {
        return new RepeatRule<L>(body);
    }

    /**
     * 创建一个空的语法规则
     * 
     * @param cls
     *            该语法规则对应的词法定义的类型
     * @return 一个空的语法规则
     */
    public static <L extends LexTokenDefinition> SyntaxRule<L> empty(Class<L> cls) {
        return new EmptyRule<L>();
    }

}
