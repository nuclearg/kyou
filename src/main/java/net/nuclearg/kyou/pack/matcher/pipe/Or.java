package net.nuclearg.kyou.pack.matcher.pipe;

import net.nuclearg.kyou.dom.KyouItem;

/**
 * 表示“或”关系的管道匹配器
 * <p>
 * 检查报文节点是否同时满足两端的匹配器的要求
 * </p>
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
