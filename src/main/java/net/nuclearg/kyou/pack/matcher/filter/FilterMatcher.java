package net.nuclearg.kyou.pack.matcher.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.dom.KyouItem;
import net.nuclearg.kyou.pack.matcher.Matcher;
import net.nuclearg.kyou.util.ClassUtils;

/**
 * 过滤匹配符，判断当前报文节点是否满足过滤条件
 * 
 * @author ng
 * 
 */
public class FilterMatcher extends Matcher {
    private static final Map<String, Class<? extends Filter>> FILTER_CLASSES;

    /**
     * 过滤器名称
     */
    private final String name;
    /**
     * 实际的过滤器
     */
    private final Filter impl;

    static {
        Map<String, Class<? extends Filter>> classes = new HashMap<String, Class<? extends Filter>>();

        for (Class<?> cls : ClassUtils.searchClassesWithAnnotation(FilterDescription.class)) {
            FilterDescription desc = cls.getAnnotation(FilterDescription.class);
            String name = desc.value();

            if (classes.containsKey(name))
                throw new KyouException("filter name duplicated. old: " + classes.get(name) + ", new: " + cls);

            classes.put(name, cls.asSubclass(Filter.class));
        }

        FILTER_CLASSES = Collections.unmodifiableMap(classes);
    }

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

    public static Matcher buildFilterMatcher(String filterName, String param) {
        return null;
    }
}
