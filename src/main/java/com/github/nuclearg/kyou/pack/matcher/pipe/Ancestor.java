package com.github.nuclearg.kyou.pack.matcher.pipe;

import com.github.nuclearg.kyou.dom.KyouContainer;
import com.github.nuclearg.kyou.dom.KyouItem;

/**
 * 祖先节点匹配器
 * <p>
 * 在右侧匹配器与当前报文节点相匹配的前提下，检查左侧匹配器是否与当前报文节点的祖先节点成功匹配。或者说，检查是否能够找到一个祖先节点，满足左侧的匹配器。
 * </p>
 * 
 * @example struct#head field
 *          表示匹配在名为head的struct下面的所有域，不管其实际位置与head相差多少级
 * 
 * @author ng
 * 
 */
@PipeMatcherDescription(" ")
class Ancestor extends PipeMatcher {

    @Override
    public boolean matches(KyouItem item) {
        if (!this.right.matches(item))
            return false;

        KyouContainer parent = item.parent();
        while (parent != null && parent != parent.parent()) {
            if (this.left.matches(parent))
                return true;
            parent = parent.parent();
        }
        return false;
    }

    @Override
    public String toString() {
        return this.left + " " + this.right;
    }

}
