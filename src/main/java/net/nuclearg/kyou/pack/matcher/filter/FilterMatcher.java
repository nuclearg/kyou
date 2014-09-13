package net.nuclearg.kyou.pack.matcher.filter;

import java.util.Map;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.dom.KyouItem;
import net.nuclearg.kyou.pack.matcher.Matcher;
import net.nuclearg.kyou.util.ClassUtils;
import net.nuclearg.kyou.util.ClassUtils.AnnotationNameParser;

/**
 * 过滤匹配符，判断当前报文节点是否满足过滤条件
 * 
 * @author ng
 * 
 */
public class FilterMatcher extends Matcher {
    private static final Map<String, Class<? extends Filter>> FILTER_CLASSES = ClassUtils.buildAnnotatedClassMap(FilterDescription.class, Filter.class, new AnnotationNameParser<FilterDescription>() {

        @Override
        public String parseName(FilterDescription annotation) {
            return annotation.value();
        }
    });

    /**
     * 过滤器名称
     */
    private final String name;
    /**
     * 实际的过滤器
     */
    private final Filter impl;

    public FilterMatcher(String text) {
        this.name = text.substring(1);
        Class<? extends Filter> filterClass = FILTER_CLASSES.get(this.name);
        if (filterClass == null)
            throw new KyouException("filter unsupported. filter: " + this.name);

        try {
            this.impl = ClassUtils.newInstance(filterClass);
        } catch (Exception ex) {
            throw new KyouException("init filter fail. filter: " + this.name, ex);
        }
    }

    @Override
    public boolean matches(KyouItem item) {
        return this.impl.matches(item);
    }

    @Override
    public String toString() {
        return ":" + this.name;
    }

    public static Matcher buildFilterMatcher(String filterName) {
        return null;
    }

    public static Matcher buildFilterMatcher(String filterName, int param) {
        return null;
    }

    public static Matcher buildFilterMatcher(String filterName, String param) {
        return null;
    }
}
