package net.nuclearg.kyou.pack.matcher;

import net.nuclearg.kyou.dom.KyouContainer;
import net.nuclearg.kyou.dom.KyouItem;

/**
 * 匹配位于奇数位置上的节点，这个是按直觉来的（从1开始计数）
 * 
 * @author ng
 * 
 */
@FilterDescription("odd")
public class IndexOddFilterMatcher extends FilterMatcherImpl {

    @Override
    boolean matches(KyouItem item) {
        KyouContainer parent = item.parent();
        if (parent == null || parent == item)
            return false;

        // indexOf是从0开始计算的，与直觉（从1开始）不符，所以需要反过来
        return parent.indexOf(item) % 2 == 0;
    }

}
