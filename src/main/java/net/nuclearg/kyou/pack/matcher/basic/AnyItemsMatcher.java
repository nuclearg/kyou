package net.nuclearg.kyou.pack.matcher.basic;

import net.nuclearg.kyou.dom.KyouItem;
import net.nuclearg.kyou.pack.matcher.Matcher;

/**
 * 匹配任何报文节点
 * 
 * @author ng
 * 
 */
public class AnyItemsMatcher extends Matcher {

    @Override
    public boolean matches(KyouItem item) {
        return true;
    }

    @Override
    public String toString() {
        return "*";
    }
}
