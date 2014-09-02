package net.nuclearg.kyou.dom.query;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.dom.KyouItem;

/**
 * 查询逻辑实现
 * 
 * @author ng
 * 
 */
abstract class QueryImpl {
    /**
     * 检查指定的报文节点是否满足当前查询条件
     * 
     * @param item
     *            报文节点
     * @return 是否满足查询条件
     */
    abstract boolean matches(KyouItem item);

    @Override
    public abstract String toString();

    /**
     * 解析查询表达式
     * 
     * @param queryStr
     *            查询表达式
     * @return 查询逻辑
     */
    static QueryImpl parse(String queryStr) {
        // TODO 支持多种查询的组合
        QueryToken token = QueryToken.tryParse(queryStr, QueryTokenType.AbsolutePath, QueryTokenType.Type);
        if (token == null || token.length != queryStr.length())
            throw new KyouException("query syntax error. query: " + queryStr);

        switch (token.type) {
            case AbsolutePath:
                return new AbsolutePathQueryImpl(token.value);
            case Type:
                return new TypeQueryImpl(token.value);
            default:
                throw new UnsupportedOperationException("query token type: " + token.type);
        }
    }
}
