package net.nuclearg.kyou.pack.matcher;

import net.nuclearg.kyou.dom.KyouItem;

/**
 * 匹配任何报文节点
 * 
 * @author ng
 * 
 */
class AnyItemsMatcher extends Matcher {

    @Override
    public boolean matches(KyouItem item) {
        return true;
    }

    @Override
    public String toString() {
        return "*";
    }
}
