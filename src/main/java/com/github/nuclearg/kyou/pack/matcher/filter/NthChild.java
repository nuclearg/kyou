package com.github.nuclearg.kyou.pack.matcher.filter;

import com.github.nuclearg.kyou.dom.KyouContainer;
import com.github.nuclearg.kyou.dom.KyouItem;
import com.github.nuclearg.kyou.pack.matcher.filter.FilterMatcherDescription.ParamType;

/**
 * 匹配位于指定位置的元素
 * 
 * @author ng
 * 
 */
@FilterMatcherDescription(name = "nth-child", paramType = ParamType.Integer)
class NthChild extends FilterMatcher {

    @Override
    public boolean matches(KyouItem item) {
        KyouContainer parent = item.parent();
        if (parent == null || parent == item)
            return false;

        return parent.indexOf(item) == this.parami;
    }

}
