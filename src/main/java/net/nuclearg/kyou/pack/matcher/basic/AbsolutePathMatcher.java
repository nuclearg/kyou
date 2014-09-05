package net.nuclearg.kyou.pack.matcher.basic;

import net.nuclearg.kyou.dom.KyouItem;
import net.nuclearg.kyou.pack.matcher.Matcher;

/**
 * 基于绝对路径进行匹配
 * 
 * @author ng
 * 
 */
public class AbsolutePathMatcher extends Matcher {
    /**
     * 要匹配的绝对路径
     */
    private final String path;

    public AbsolutePathMatcher(String path) {
        this.path = path;
    }

    @Override
    public boolean matches(KyouItem item) {
        return this.path.equals(item.path());
    }

    @Override
    public String toString() {
        return this.path;
    }

}
