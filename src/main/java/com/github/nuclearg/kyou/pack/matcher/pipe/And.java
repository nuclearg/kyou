package com.github.nuclearg.kyou.pack.matcher.pipe;

import com.github.nuclearg.kyou.dom.KyouItem;

/**
 * 表示“与”关系的管道匹配器
 * <p>
 * 检查报文节点是否同时满足两端的匹配器的要求
 * <p>
 * 当两个匹配器并列地写在一起，中间没有任何其它字符时，即表示两个匹配器是“与”的关系。与运算的优先级是最高的。
 * </p>
 * 
 * @example field#name[attrA='3']
 *          匹配一个报文域，并且名称为“name”，并且其拥有一个名为attrA的属性，且其值为“3”
 * 
 * @author ng
 * 
 */
@PipeMatcherDescription("")
class And extends PipeMatcher {

    @Override
    public boolean matches(KyouItem item) {
        return this.left.matches(item) && this.right.matches(item);
    }

    @Override
    public String toString() {
        return "" + this.left + this.right;
    }

}
