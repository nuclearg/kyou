package com.github.nuclearg.kyou.pack.matcher.attribute;

/**
 * 属性的运算符
 * 
 * @author ng
 * 
 */
abstract class Operator {
    /**
     * 判断当前节点是否满足基于属性运算出来的结果
     * 
     * @param exprValue
     *            用来进行运算的值
     * @param attrValue
     *            实际报文节点对应的属性值
     * @return 是否满足要求
     */
    abstract boolean matches(String exprValue, String attrValue);
}
