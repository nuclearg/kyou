package net.nuclearg.kyou.pack.matcher.filter;

import net.nuclearg.kyou.dom.KyouContainer;
import net.nuclearg.kyou.dom.KyouItem;

/**
 * 匹配所有兄弟节点的最后一个
 * 
 * @author ng
 * 
 */
@FilterDescription("last")
class LastFilterMatcher extends FilterMatcherImpl {

    @Override
    boolean matches(KyouItem item) {
        KyouContainer parent = item.parent();
        if (parent == null || parent == item)
            return false;

        return parent.indexOf(item) == parent.size() - 1;
    }

}
