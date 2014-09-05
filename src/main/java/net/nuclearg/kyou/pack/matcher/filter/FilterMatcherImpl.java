package net.nuclearg.kyou.pack.matcher.filter;

import net.nuclearg.kyou.dom.KyouItem;

/**
 * 过滤器实现
 * 
 * @author ng
 * 
 */
abstract class FilterMatcherImpl {

    /**
     * 判断过滤器与给定的报文节点是否匹配
     * 
     * @param item
     *            报文节点
     * @return 当前过滤器与报文节点是否匹配
     */
    abstract boolean matches(KyouItem item);
}
