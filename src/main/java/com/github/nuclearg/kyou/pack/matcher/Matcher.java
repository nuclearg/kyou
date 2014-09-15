package com.github.nuclearg.kyou.pack.matcher;

import org.apache.commons.lang.StringUtils;

import com.github.nuclearg.kyou.KyouException;
import com.github.nuclearg.kyou.dom.KyouItem;
import com.github.nuclearg.kyou.pack.matcher.MatcherString.MatcherInfo;
import com.github.nuclearg.kyou.pack.matcher.attribute.AttributeMatcher;
import com.github.nuclearg.kyou.pack.matcher.basic.AbsolutePathMatcher;
import com.github.nuclearg.kyou.pack.matcher.basic.NodeNameMatcher;
import com.github.nuclearg.kyou.pack.matcher.basic.NodeTypeMatcher;
import com.github.nuclearg.kyou.pack.matcher.filter.FilterMatcher;
import com.github.nuclearg.kyou.pack.matcher.pipe.PipeMatcher;

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
     * @param str
     *            匹配字符串
     * @return 匹配器
     */
    public static Matcher parse(String str) {
        if (StringUtils.isBlank(str))
            throw new KyouException("match is blank");

        // 解析匹配字符串，构造匹配器树
        try {
            MatcherInfo root = new MatcherString(str).parseMatcherInfo();

            return buildMatcher(root);
        } catch (Exception ex) {
            throw new KyouException("parse match fail. match: " + str, ex);
        }
    }

    /**
     * 根据匹配器信息创建匹配器
     * 
     * @param info
     *            匹配器信息
     * @return 匹配器实例
     */
    private static Matcher buildMatcher(MatcherInfo info) {
        switch (info.type) {
            case AbsolutePath:
                return new AbsolutePathMatcher(info.text);
            case NodeType:
                return new NodeTypeMatcher(info.text);
            case NodeName:
                return new NodeNameMatcher(info.text);
            case Pipe:
                return PipeMatcher.buildPipeMatcher(info.text, buildMatcher(info.left), buildMatcher(info.right));
            case Attribute:
                return AttributeMatcher.buildAttributeMatcher(info.attrName, info.attrOperator, info.attrValue);
            case Filter:
                return FilterMatcher.buildFilterMatcher(info.text, info.filterParam);
            default:
                throw new UnsupportedOperationException("matcher type " + info.type);
        }
    }
}
