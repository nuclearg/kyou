package net.nuclearg.kyou.pack.matcher.filter;

import net.nuclearg.kyou.dom.KyouContainer;
import net.nuclearg.kyou.dom.KyouItem;
import net.nuclearg.kyou.pack.matcher.filter.FilterMatcherDescription.ParamType;

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
