package net.nuclearg.kyou.pack.matcher.pipe;

import net.nuclearg.kyou.pack.matcher.Matcher;

/**
 * 管道匹配器，基于不同的逻辑将两端的匹配器连接起来
 * 
 * @author ng
 * 
 */
public abstract class PipeMatcher extends Matcher {
    /**
     * 左侧的匹配器
     */
    public Matcher left;
    /**
     * 右侧的匹配器
     */
    public Matcher right;

    /**
     * 创建一个表示“与”关系的管道匹配器
     */
    public static PipeMatcher and(Matcher left, Matcher right) {
        And pipe = new And();
        pipe.left = left;
        pipe.right = right;
        return pipe;
    }

    /**
     * 创建一个{@link ParentPipeMatcher}
     */
    public static PipeMatcher parent() {
        return new Parent();
    }
}
