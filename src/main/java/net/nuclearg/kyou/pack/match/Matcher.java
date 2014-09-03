package net.nuclearg.kyou.pack.match;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public abstract String toString();

    /**
     * 解析匹配字符串
     * 
     * @param queryStr
     *            匹配字符串
     * @return 匹配器
     */
    public static Matcher parse(String queryStr) {
        if (StringUtils.isBlank(queryStr))
            throw new KyouException("query is blank");

        // 匹配字符串对应的匹配器列表
        List<Matcher> matchers = new ArrayList<Matcher>();

        String str = queryStr;
        while (!str.isEmpty()) {
            MatchExpressionToken token = MatchExpressionToken.tryParse(str);

            matchers.add(buildMatcher(token));

            str = str.substring(token.text.length());
        }

        // 整理一下matcher列表，组成一个单独的matcher
        return matchers.get(0);
    }

    /**
     * 根据词法元素创建匹配器
     * 
     * @param token
     *            词法元素
     * @return 与词法元素对应的匹配器
     */
    private static Matcher buildMatcher(MatchExpressionToken token) {
        switch (token.type) {
            case AbsolutePath:
                return new AbsolutePathMatcher(token.text);
            case Attribute:
                return new AttributeMatcher(token.text);
            case Filter:
                return new FilterMatcher(token.text);
            case NodeName:
                return new NodeNameMatcher(token.text);
            case Space:
                return new ParentPipeMatcher();
            case Type:
                return new TypeMatcher(token.text);
            default:
                throw new UnsupportedOperationException("token type: " + token.type);
        }
    }
}
