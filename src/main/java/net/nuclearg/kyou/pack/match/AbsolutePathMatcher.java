package net.nuclearg.kyou.pack.match;

import net.nuclearg.kyou.dom.KyouItem;

/**
 * 基于绝对路径进行查询
 * 
 * @author ng
 * 
 */
class AbsolutePathMatcher extends Matcher {
    /**
     * 要匹配的绝对路径
     */
    private final String path;

    AbsolutePathMatcher(String path) {
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
