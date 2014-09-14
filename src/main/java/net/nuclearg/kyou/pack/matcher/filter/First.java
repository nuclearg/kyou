package net.nuclearg.kyou.pack.matcher.filter;

import net.nuclearg.kyou.dom.KyouContainer;
import net.nuclearg.kyou.dom.KyouItem;
import net.nuclearg.kyou.pack.matcher.filter.FilterMatcherDescription.ParamType;

/**
 * 匹配所有兄弟节点中的第一个
 * 
 * @author ng
 * 
 */
@FilterMatcherDescription(name = "first", paramType = ParamType.None)
class First extends FilterMatcher {

    @Override
    public boolean matches(KyouItem item) {
        KyouContainer parent = item.parent();
        if (parent == null || parent == item)
            return false;

        return parent.indexOf(item) == 0;
    }

}
