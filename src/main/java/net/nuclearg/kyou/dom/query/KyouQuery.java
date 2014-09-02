package net.nuclearg.kyou.dom.query;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.dom.KyouItem;

import org.apache.commons.lang.StringUtils;

/**
 * 查询，用来选择某种满足条件的报文节点
 * <p>
 * 支持如下选择方式：
 * <li>绝对路径 #.a.b</li>
 * <li>类型 document struct array field</li>
 * <li>基于属性查询 struct[a=3]</li>
 * <li>由前三种规则组合而成的各种查询</li>
 * <p>
 * 下面详细说一下：
 * <li>绝对路径</li> 绝对路径是指从
 * 
 * @author ng
 * 
 */
public class KyouQuery {
    /**
     * 查询
     */
    private final String query;
    /**
     * 实际的查询实现
     */
    private final QueryImpl impl;

    public KyouQuery(String query) {
        if (StringUtils.isBlank(query))
            throw new KyouException("query is blank");

        this.query = query;
        this.impl = QueryImpl.parse(query);
    }

    /**
     * 检查指定的报文节点是否满足当前查询条件
     * 
     * @param item
     *            报文节点
     * @return 是否满足查询条件
     */
    public boolean matches(KyouItem item) {
        if (item == null)
            throw new KyouException("item is null");

        return this.impl.matches(item);
    }

    @Override
    public String toString() {
        return this.query;
    }
}
