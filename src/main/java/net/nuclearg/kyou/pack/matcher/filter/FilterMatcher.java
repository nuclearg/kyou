package net.nuclearg.kyou.pack.matcher.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.nuclearg.kyou.KyouException;
import net.nuclearg.kyou.dom.KyouItem;
import net.nuclearg.kyou.pack.matcher.Matcher;

import org.reflections.Reflections;

/**
 * 过滤匹配符，判断当前报文节点是否满足过滤条件
 * 
 * @author ng
 * 
 */
public class FilterMatcher extends Matcher {
    private static final Map<String, Class<? extends FilterMatcherImpl>> FILTER_CLASSES;

    /**
     * 过滤器名称
     */
    private final String name;
    /**
     * 实际的过滤器
     */
    private final FilterMatcherImpl impl;

    static {
        Map<String, Class<? extends FilterMatcherImpl>> filterClasses = new HashMap<String, Class<? extends FilterMatcherImpl>>();

        Set<Class<?>> classes = new Reflections(FilterMatcherImpl.class.getPackage().getName()).getTypesAnnotatedWith(FilterDescription.class);
        for (Class<?> cls : classes) {
            FilterDescription desc = cls.getAnnotation(FilterDescription.class);
            if (desc == null)
                continue;

            String name = desc.value();

            if (filterClasses.containsKey(name))
                throw new KyouException("filter name duplicated. old: " + filterClasses.get(name) + ", new: " + cls);

            filterClasses.put(name, cls.asSubclass(FilterMatcherImpl.class));
        }

        FILTER_CLASSES = Collections.unmodifiableMap(filterClasses);
    }

    public FilterMatcher(String text) {
        this.name = text.substring(1);
        Class<? extends FilterMatcherImpl> filterClass = FILTER_CLASSES.get(name);
        if (filterClass == null)
            throw new KyouException("filter unsupported. filter: " + name);

        try {
            this.impl = filterClass.newInstance();
        } catch (Exception ex) {
            throw new KyouException("init filter fail. filter: " + name, ex);
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
}
