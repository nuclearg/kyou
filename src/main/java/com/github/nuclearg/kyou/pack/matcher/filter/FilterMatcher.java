package com.github.nuclearg.kyou.pack.matcher.filter;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import com.github.nuclearg.kyou.KyouException;
import com.github.nuclearg.kyou.pack.matcher.Matcher;
import com.github.nuclearg.kyou.util.ClassUtils;
import com.github.nuclearg.kyou.util.ClassUtils.AnnotationNameParser;

/**
 * 过滤匹配符，判断当前报文节点是否满足过滤条件
 * 
 * @author ng
 * 
 */
public abstract class FilterMatcher extends Matcher {
    private static final Map<String, Class<? extends FilterMatcher>> FILTER_CLASSES = ClassUtils.buildAnnotatedClassMap(FilterMatcherDescription.class, FilterMatcher.class, new AnnotationNameParser<FilterMatcherDescription>() {

        @Override
        public String parseName(FilterMatcherDescription annotation) {
            return annotation.name();
        }
    });

    /**
     * 参数
     */
    protected String param;
    /**
     * 参数的整数形式
     */
    protected int parami = -1;

    /**
     * 检查当前过滤器是否有问题
     */
    protected void check() {
        FilterMatcherDescription annotation = this.getClass().getAnnotation(FilterMatcherDescription.class);

        switch (annotation.paramType()) {
            case None:
                ensure(this.param == null && this.parami == -1, "param not expected. param: " + this.param);
                break;
            case Integer:
                ensure(this.parami >= 0, "param shoule be integer. param: " + this.param);
                break;
            case String:
                ensure(this.param != null, "param is empty");
                break;
            default:
                throw new UnsupportedOperationException("param type " + annotation.paramType());

        }
    }

    /**
     * 工具方法，不满足条件就抛异常
     */
    private static void ensure(boolean result, String err) {
        if (!result)
            throw new KyouException(err);
    }

    @Override
    public String toString() {
        FilterMatcherDescription annotation = this.getClass().getAnnotation(FilterMatcherDescription.class);

        switch (annotation.paramType()) {
            case None:
                return ":" + annotation.name();
            case Integer:
                return ":" + annotation.name() + "(" + this.parami + ")";
            case String:
                return ":" + annotation.name() + "(\"" + this.param + "\")";
            default:
                throw new UnsupportedOperationException("param type " + annotation.paramType());
        }
    }

    /**
     * 构建一个过滤器
     * 
     * @param name
     *            过滤器名称
     * @param param
     *            参数
     * @return 过滤器实例
     */
    public static Matcher buildFilterMatcher(String name, String param) {
        FilterMatcher filter = ClassUtils.newInstance(FILTER_CLASSES, name);
        if (filter == null)
            throw new KyouException("filter unsupported. name: " + name);

        filter.param = param;
        filter.parami = NumberUtils.toInt(param, -1);

        try {
            filter.check();
        } catch (Exception ex) {
            throw new KyouException("build filter fail. name: " + name + ", param: " + param, ex);
        }

        return filter;
    }

}
