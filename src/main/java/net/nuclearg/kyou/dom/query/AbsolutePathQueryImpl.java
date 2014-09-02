package net.nuclearg.kyou.dom.query;

import net.nuclearg.kyou.dom.KyouItem;

/**
 * 基于绝对路径进行查询
 * 
 * @author ng
 * 
 */
class AbsolutePathQueryImpl extends QueryImpl {
    private final String path;

    AbsolutePathQueryImpl(String path) {
        this.path = path;
    }

    @Override
    boolean matches(KyouItem item) {
        return this.path.equals(item.path());
    }

    @Override
    public String toString() {
        return this.path;
    }

}
