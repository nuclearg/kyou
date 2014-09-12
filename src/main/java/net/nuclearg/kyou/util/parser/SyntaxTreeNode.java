package net.nuclearg.kyou.util.parser;

import java.util.List;

import net.nuclearg.kyou.util.lexer.LexDefinition;
import net.nuclearg.kyou.util.lexer.LexToken;

import org.apache.commons.lang.SystemUtils;

/**
 * 语法树节点
 * <p>
 * 语法分析的结果就是一棵完整的语法树。这棵语法树的节点就是由{@link SyntaxTreeNode}构成的。<br/>
 * 节点有两种状态：表示一个词法单元，或是表示其它语法单元的组合。
 * <p>
 * 例如，if Expression then Action 这句语法定义，对应的语法树应当如下：
 * <li>关键字if，对应一个词法单元</li>
 * <li>Expression节点，对应一个子语法树结构</li>
 * <li>关键字then，对应另一个词法单元</li>
 * <li>Action节点，对应另一个子语法树结构</li>
 * </p>
 * 
 * @author ng
 * 
 * @param <L>
 *            词法定义的类型
 * @param <S>
 *            语法定义的类型
 */
public class SyntaxTreeNode<L extends LexDefinition, S extends SyntaxDefinition<L>> {
    /**
     * 这个节点对应的语法定义
     * <p>
     * 可能为null
     * </p>
     */
    public final S type;
    /**
     * 子节点列表
     * <p>
     * 可能为null
     * </p>
     */
    public final List<SyntaxTreeNode<L, S>> children;
    /**
     * 对应的词法单元
     * <p>
     * 可能为null
     * </p>
     */
    public final LexToken<L> token;

    SyntaxTreeNode(S type, List<SyntaxTreeNode<L, S>> children, LexToken<L> token) {
        this.type = type;
        this.children = children;
        this.token = token;
    }

    @Override
    public String toString() {
        return this.toString("");
    }

    private String toString(String prefix) {
        StringBuilder builder = new StringBuilder();
        builder.append(prefix);
        builder.append("+");
        builder.append(this.type == null ? "" : this.type);

        if (this.children != null)
            for (SyntaxTreeNode<L, S> child : this.children)
                builder.append(SystemUtils.LINE_SEPARATOR).append(child.toString(prefix + "  "));

        if (this.token != null)
            builder.append(" ").append(token);

        return builder.toString();
    }
}
