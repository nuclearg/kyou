package net.nuclearg.kyou.pack.matcher;

import java.util.ArrayList;
import java.util.List;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.dom.KyouItem;
import net.nuclearg.kyou.pack.matcher.attribute.AttributeMatcher;
import net.nuclearg.kyou.pack.matcher.basic.AbsolutePathMatcher;
import net.nuclearg.kyou.pack.matcher.basic.NodeNameMatcher;
import net.nuclearg.kyou.pack.matcher.basic.TypeMatcher;
import net.nuclearg.kyou.pack.matcher.filter.FilterMatcher;
import net.nuclearg.kyou.pack.matcher.pipe.PipeMatcher;

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

        /*
         * 匹配字符串对应的匹配器列表
         */
        List<Matcher> matchers = new ArrayList<Matcher>();

        String str = queryStr;
        while (!str.isEmpty()) {
            MatchExpressionToken token = MatchExpressionToken.tryParse(str);

            matchers.add(buildMatcher(token));

            str = str.substring(token.text.length());
        }

        /*
         * 整理一下matcher列表，组成一个单独的matcher
         */
        Matcher current = null; // 当前matcher
        for (Matcher next : matchers) {
            // 如果当前matcher为空，则直接把新的matcher设为当前matcher
            if (current == null) {
                if (next instanceof PipeMatcher) // 不能上来就是一个管道匹配器，这种情况直接报错
                    throw new KyouException("pipe matcher at first. matcher: " + next);

                current = next;
                continue;
            }

            // 当前的匹配器是否是独立的。为处理方便，两端都挂满了的管道匹配器也作为独立匹配器考虑
            boolean currAlone = !(current instanceof PipeMatcher)
                    || (current instanceof PipeMatcher && ((PipeMatcher) current).right != null);
            // 下一个匹配器是否是独立的。下一个匹配器永远不可能是两端都挂满了的管道匹配器
            boolean nextAlone = !(next instanceof PipeMatcher);

            // 如果当前matcher是独立匹配器，新来的这个也是一个独立匹配器
            // 或者当前matcher是管道匹配器，但左右都挂满了
            // 则用一个AndPipeMatcher把这两个连起来并设为当前matcher
            if (currAlone && nextAlone) {
                current = PipeMatcher.and(current, next);
                continue;
            }

            // 如果当前matcher是独立匹配器，新来的这个是一个管道匹配器，则把当前匹配器作为left，新来的这个作为right，并把当前matcher指向right
            if (currAlone && !nextAlone) {
                PipeMatcher pipe = (PipeMatcher) next;
                pipe.left = current;
                current = pipe;
                continue;
            }

            // 如果当前matcher是右侧悬空的管道匹配器，并且新来的这个是一个独立匹配器，则把右边的匹配器作为right挂进来
            if (!currAlone && nextAlone) {
                PipeMatcher pipe = (PipeMatcher) current;
                pipe.right = next;
                continue;
            }

            // 如果当前matcher是右侧悬空的管道匹配器，并且新来的这个是一个管道匹配器，则报错
            if (!currAlone && !nextAlone)
                throw new KyouException("pipe matchers together. current: " + current + ", next: " + next);
        }

        return current;
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
                return PipeMatcher.parent();
            case Type:
                return new TypeMatcher(token.text);
            default:
                throw new UnsupportedOperationException("token type: " + token.type);
        }
    }
}
