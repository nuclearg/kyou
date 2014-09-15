package com.github.nuclearg.kyou.pack.matcher.filter;

import com.github.nuclearg.kyou.dom.KyouContainer;
import com.github.nuclearg.kyou.dom.KyouItem;
import com.github.nuclearg.kyou.pack.matcher.filter.FilterMatcherDescription.ParamType;

/**
 * 匹配所有兄弟节点的最后一个
 * 
 * @author ng
 * 
 */
@FilterMatcherDescription(name = "last", paramType = ParamType.None)
class Last extends FilterMatcher {

    @Override
    public boolean matches(KyouItem item) {
        KyouContainer parent = item.parent();
        if (parent == null || parent == item)
            return false;

        return parent.indexOf(item) == parent.size() - 1;
    }

}
