package com.github.nuclearg.kyou.pack.matcher.pipe;

import com.github.nuclearg.kyou.dom.KyouItem;

/**
 * 表示“或”关系的管道匹配器
 * <p>
 * 检查报文节点是否同时满足两端的匹配器其中之一的要求
 * <p>
 * 或运算的优先级是最低的
 * </p>
 * 
 * @example field#a, field[b='3'], array>field
 *          匹配名称为a的报文域，或者有一个名为“b”的属性且其值为“3”的报文域，或者匹配所有位于数组中的域
 * 
 * @author ng
 * 
 */
@PipeMatcherDescription(",")
class Or extends PipeMatcher {

    @Override
    public boolean matches(KyouItem item) {
        return this.left.matches(item) && this.right.matches(item);
    }

    @Override
    public String toString() {
        return "" + this.left + this.right;
    }

}
