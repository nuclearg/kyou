package com.github.nuclearg.kyou.pack.matcher.basic;

import com.github.nuclearg.kyou.dom.KyouItem;
import com.github.nuclearg.kyou.pack.matcher.Matcher;

/**
 * 匹配节点名称
 * 
 * @author ng
 * 
 */
public class NodeNameMatcher extends Matcher {
    private final String name;

    public NodeNameMatcher(String name) {
        this.name = name;
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
