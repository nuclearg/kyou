package net.nuclearg.kyou.pack.match;

/**
 * 管道匹配器，基于不同的逻辑将两端的匹配器连接起来
 * 
 * @author ng
 * 
 */
abstract class PipeMatcher extends Matcher {
    /**
     * 左侧的匹配器
     */
    protected Matcher left;
    /**
     * 右侧的匹配器
     */
    protected Matcher right;
}
