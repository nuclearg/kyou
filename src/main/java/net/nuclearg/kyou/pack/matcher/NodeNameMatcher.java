package net.nuclearg.kyou.pack.matcher;

import net.nuclearg.kyou.dom.KyouItem;

/**
 * 匹配节点名称
 * 
 * @author ng
 * 
 */
class NodeNameMatcher extends Matcher {
    private final String name;

    NodeNameMatcher(String name) {
        this.name = name.substring(1);
    }

    @Override
    public boolean matches(KyouItem item) {
        return this.name.equals(item.name());
    }

    @Override
    public String toString() {
        return "#" + this.name;
    }

}
