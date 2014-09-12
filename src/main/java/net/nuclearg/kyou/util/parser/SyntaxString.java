package net.nuclearg.kyou.util.parser;

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
     * 底层的词法字符串
     */
    private final LexString<L> tokenStr;

    public SyntaxString(String str) {
        this.tokenStr = new LexString<L>(str);
    }

    /**
     * 尝试将当前的字符串根据给定的语法定义进行解析，并返回一棵报文树
     * 
     * @param syntax
     *            描述整棵语法树的语法定义
     * @return ，如果解析失败则返回null
     */
    public SyntaxTreeNode<L, S> tryParse(S syntax) {
        return syntax.syntax().tryMatch(this.tokenStr);
    }

    /**
     * 判断当前是否仍有待解析的剩余的字符
     * 
     * @return 是否有剩余
     */
    public boolean hasRemaining() {
        return this.tokenStr.hasRemaining();
    }

    @Override
    public String toString() {
        return this.tokenStr.toString();
    }
}
