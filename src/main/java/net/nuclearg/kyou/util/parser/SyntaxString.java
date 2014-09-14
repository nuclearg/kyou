package net.nuclearg.kyou.util.parser;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.util.lexer.LexDefinition;
import net.nuclearg.kyou.util.lexer.LexString;

/**
 * 支持语法解析的字符串，可以根据给定的语法定义将整个字符串拆成一棵语法树
 * <p>
 * 用户首先应当通过实现{@link SyntaxDefinition}的方式定义一系列的语法定义，这些语法定义应当组成一套完整的语法定义，可以完备地描述期望的语法结构。
 * <p>
 * 然后用户可以尝试使用这套语法定义对字符串进行解析。如果解析成功则可以拿到一棵由{@link SyntaxTreeNode}组成的完整的语法树
 * </p>
 * 
 * @author ng
 * 
 * @param <L>
 *            词法定义的类型
 * @param <S>
 *            语法定义的类型
 */
public class SyntaxString<L extends LexDefinition, S extends SyntaxDefinition<L>> {
    /**
     * 原始的待解析的字符串
     */
    private final String str;

    public SyntaxString(String str) {
        this.str = str;
    }

    /**
     * 尝试将当前的字符串根据给定的语法定义进行解析，并返回一棵报文树
     * 
     * @param syntax
     *            描述整棵语法树的语法定义
     * @return 解析出来的语法树。果解析失败则报错
     */
    public SyntaxTreeNode<L, S> parse(S syntax) {
        LexString<L> tokenStr = new LexString<>(this.str);
        SyntaxTreeNode<L, S> result = syntax.syntax().tryMatch(tokenStr);

        if (result == null)
            throw new KyouException("syntax parse fail. text: " + this.str + ", syntax: " + syntax);

        if (tokenStr.hasRemaining())
            throw new KyouException("syntax parse fail. unexpected character left. str: " + this.str + ", left: " + tokenStr + ", tree: " + result);

        return result;
    }

    @Override
    public String toString() {
        return this.str;
    }
}
