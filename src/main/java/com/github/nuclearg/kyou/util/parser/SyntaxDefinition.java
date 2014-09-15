package com.github.nuclearg.kyou.util.parser;

import com.github.nuclearg.kyou.util.lexer.LexDefinition;

/**
 * 语法定义
 * <p>
 * 作为语法解析的基础，语法定义描述了一棵完整的语法树的每个部分的组成。
 * <p>
 * 一般地，语法定义不会是单独出现的，而是由多条组成一篇完整的语法定义，用以完备地描述期望的语法结构。
 * <p>
 * 一定要有一条作为根的语法定义，用以完整定义整篇语法结构。
 * </p>
 * 
 * @author ng
 * 
 * @param <T>
 */
public interface SyntaxDefinition<L extends LexDefinition> {
    /**
     * 语法定义
     */
    public SyntaxRule<L> syntax();
}
