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
    private static final Map<String, Class<? extends PipeMatcher>> PIPE_CLASSES = ClassUtils.buildAnnotatedClassMap(PipeDescription.class, PipeMatcher.class, new AnnotationNameParser<PipeDescription>() {

        @Override
        public String parseName(PipeDescription annotation) {
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
     * 
     * @param name
     * @param left
     * @param right
     * @return
     */
    public static PipeMatcher buildPipeMatcher(String name, Matcher left, Matcher right) {
        Class<? extends PipeMatcher> pipeClass = PIPE_CLASSES.get(name);
        if (pipeClass == null)
            throw new KyouException("unsupported pipe operator. op: " + name);

        PipeMatcher pipe = ClassUtils.newInstance(pipeClass);

        pipe.left = left;
        pipe.right = right;

        return pipe;
    }
}
