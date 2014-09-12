package net.nuclearg.kyou.util.parser;

import net.nuclearg.kyou.util.lexer.LexDefinition;
import net.nuclearg.kyou.util.lexer.LexString;

/**
 * 语法规则
 * <p>
 * 语法规则用于描述语法定义。
 * <p>
 * 语法规则以词法定义{@link LexDefinition}为基础，语法规则之间可以基于BNF范式指定的几种方式进行组合，包括顺序排列、重复、可为空等。
 * </p>
 * 
 * @author ng
 * 
 * @param <L>
 */
public abstract class SyntaxRule<L extends LexDefinition> {
    /**
     * 尝试将词法字符串与语法规则进行匹配
     * 
     * @param tokenStr
     *            进行语法分析的字符串
     * @return 如果给定的字符串满足语法规则，则返回分析结果，否则返回null
     */
    abstract <S extends SyntaxDefinition<L>> SyntaxTreeNode<L, S> tryMatch(LexString<L> tokenStr);

    @Override
    public abstract String toString();

    /**
     * 创建一个基于词的语法规则
     * 
     * @param tokenType
     *            词法定义
     * @return 基于词的语法规则
     */
    public static <L extends LexDefinition> SyntaxRule<L> lex(L tokenType) {
        return new LexRule<L>(tokenType);
    }

    /**
     * 创建一个引用其它语法定义的语法规则
     * 
     * @param type
     *            其它的语法规则
     * @return 引用其它语法规则的语法规则
     */
    public static <L extends LexDefinition> SyntaxRule<L> ref(SyntaxDefinition<L> type) {
        return new RefRule<L>(type);
    }

    /**
     * 创建一个顺序连接多个语法规则的语法规则
     * 
     * @param elements
     *            子元素的语法规则列表
     * @return 顺序连接多个语法规则的语法规则
     */
    public static <L extends LexDefinition> SyntaxRule<L> seq(SyntaxRule<L>... elements) {
        return new SequenceRule<L>(elements);
    }

    /**
     * 创建一个可以出现0或1次指定语法规则的语法规则
     * 
     * @param body
     *            作为本体的语法规则
     * @return 可以出现0或1次指定语法规则的语法规则
     */
    public static <L extends LexDefinition> SyntaxRule<L> opt(SyntaxRule<L> body) {
        return new OptionalRule<L>(body);
    }

    /**
     * 创建一个表示或的语法规则
     * 
     * @param conditions
     *            作为选项的语法规则列表
     * @return 表示或的语法规则
     */
    public static <L extends LexDefinition> SyntaxRule<L> or(SyntaxRule<L>... conditions) {
        return new OrRule<L>(conditions);
    }

    /**
     * 创建一个可以出现0次或无数次指定语法规则的语法规则
     * 
     * @param body
     *            作为本体的语法规则
     * @return 可以出现0次或无数次指定语法规则的语法规则
     */
    public static <L extends LexDefinition> SyntaxRule<L> rep(SyntaxRule<L> body) {
        return new RepeatRule<L>(body);
    }

    /**
     * 创建一个空的语法规则
     * 
     * @param cls
     *            该语法规则对应的词法定义的类型
     * @return 一个空的语法规则
     */
    public static <L extends LexDefinition> SyntaxRule<L> empty(Class<L> cls) {
        return new EmptyRule<L>();
    }

}
