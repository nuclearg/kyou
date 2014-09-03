package net.nuclearg.kyou.pack.matcher;

import net.nuclearg.kyou.dom.KyouContainer;
import net.nuclearg.kyou.dom.KyouItem;

/**
 * 匹配不包含任何元素的容器节点
 * 
 * @author ng
 * 
 */
@FilterDescription("empty")
public class EmptyContainerFilterMatcher extends FilterMatcherImpl {

    @Override
    boolean matches(KyouItem item) {
        if (!(item instanceof KyouContainer))
            return false;

        KyouContainer container = (KyouContainer) item;

        return container.size() == 0;
    }

}
