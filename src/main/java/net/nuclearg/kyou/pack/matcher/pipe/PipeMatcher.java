package net.nuclearg.kyou.pack.matcher.pipe;

import java.util.Map;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.pack.matcher.Matcher;
import net.nuclearg.kyou.util.ClassUtils;
import net.nuclearg.kyou.util.ClassUtils.AnnotationNameParser;

/**
 * 管道匹配器，基于不同的逻辑将两端的匹配器连接起来
 * 
 * @author ng
 * 
 */
public abstract class PipeMatcher extends Matcher {
    private static final Map<String, Class<? extends PipeMatcher>> PIPE_CLASSES = ClassUtils.buildAnnotatedClassMap(PipeMatcherDescription.class, PipeMatcher.class, new AnnotationNameParser<PipeMatcherDescription>() {

        @Override
        public String parseName(PipeMatcherDescription annotation) {
            return annotation.value();
        }
    });

    /**
     * 左侧的匹配器
     */
    protected Matcher left;
    /**
     * 右侧的匹配器
     */
    protected Matcher right;

    /**
     * 构建一个管道匹配器，用于把两个匹配器按照指定的逻辑关系连接起来
     * 
     * @param name
     *            管道匹配器的名称
     * @param left
     *            左边的匹配器
     * @param right
     *            右边的匹配器
     * @return 管道匹配器实例
     */
    public static PipeMatcher buildPipeMatcher(String name, Matcher left, Matcher right) {
        PipeMatcher pipe = ClassUtils.newInstance(PIPE_CLASSES, name);
        if (pipe == null)
            throw new KyouException("unsupported pipe operator. op: " + name);

        pipe.left = left;
        pipe.right = right;

        return pipe;
    }
}
