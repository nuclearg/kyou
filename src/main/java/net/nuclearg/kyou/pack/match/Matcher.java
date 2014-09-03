package net.nuclearg.kyou.pack.match;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.dom.KyouItem;

import org.apache.commons.lang.StringUtils;

/**
 * 匹配器，用来判断报文节点是否满足某个条件
 * 
 * @author ng
 * 
 */
public abstract class Matcher {

    /**
     * 检查指定的报文节点是否满足当前查询条件
     * 
     * @param item
     *            报文节点
     * @return 是否满足查询条件
     */
    public abstract boolean matches(KyouItem item);

    public static Matcher parse(String queryStr) {
        if (StringUtils.isBlank(queryStr))
            throw new KyouException("query is blank");

        // TODO 支持多种查询的组合
        MatchExpressionToken token = MatchExpressionToken.tryParse(queryStr);
        if (token == null || token.length != queryStr.length())
            throw new KyouException("query syntax error. query: " + queryStr);

        switch (token.type) {
            case AbsolutePath:
                return new AbsolutePathQuery(token.value);
            case Type:
                return new TypeMatcher(token.value);
            case NodeName:
                return new NodeNameMatcher(token.value);
            default:
                throw new UnsupportedOperationException("query token type: " + token.type);
        }
    }
}
