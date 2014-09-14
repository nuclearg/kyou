package net.nuclearg.kyou.pack.matcher.filter;

import net.nuclearg.kyou.dom.KyouContainer;
import net.nuclearg.kyou.dom.KyouItem;
import net.nuclearg.kyou.pack.matcher.filter.FilterMatcherDescription.ParamType;

/**
 * 匹配不包含任何元素的容器节点
 * 
 * @author ng
 * 
 */
@FilterMatcherDescription(name = "empty", paramType = ParamType.None)
class EmptyContainer extends FilterMatcher {

    @Override
    public boolean matches(KyouItem item) {
        if (!(item instanceof KyouContainer))
            return false;

        KyouContainer container = (KyouContainer) item;

        return container.size() == 0;
    }

}
