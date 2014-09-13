package net.nuclearg.kyou.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.nuclearg.kyou.KyouException;

import org.reflections.Reflections;

/**
 * 类型相关的工具类
 * 
 * @author ng
 * 
 */
public class ClassUtils {

    /**
     * 搜索所有打了指定的标，并继承自指定基类的类，构建一个名称到类型的map
     * 
     * @param annotationClass
     * @param targetBaseClass
     * @param nameParser
     *            从Annotation中获取名称的方法
     * @return 构造好的map
     */
    public static <T, A extends Annotation> Map<String, Class<? extends T>> buildAnnotatedClassMap(Class<A> annotationClass, Class<T> targetBaseClass, AnnotationNameParser<A> nameParser) {
        Map<String, Class<? extends T>> map = new LinkedHashMap<>();

        for (Class<? extends T> cls : searchClassesWithAnnotation(annotationClass, targetBaseClass)) {
            A annotation = cls.getAnnotation(annotationClass);
            String name = nameParser.parseName(annotation);

            if (map.containsKey(name))
                throw new KyouException("annotation name duplicated. name: " + name + ", old: " + map.get(name) + ", new: " + cls);

            map.put(name, cls);
        }

        return Collections.unmodifiableMap(map);
    }

    /**
     * 从Annotation中获取名称
     * 
     * @author ng
     * 
     * @param <A>
     */
    public static interface AnnotationNameParser<A extends Annotation> {
        /**
         * 从Annotation中获取名称
         * 
         * @param annotation
         *            要获取名称的Annotation
         * @return Annotation的名称
         */
        public String parseName(A annotation);
    }

    /**
     * 创建指定类型的实例，调用其无参构造函数
     * 
     * @param cls
     *            类型
     * @param args
     *            参数列表
     * @return 实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> cls, Object... args) {
        try {
            for (Constructor<?> constructor : cls.getDeclaredConstructors())
                if (constructor.getParameterTypes().length == args.length) {
                    constructor.setAccessible(true);
                    return (T) constructor.newInstance();
                }
        } catch (Exception ex) {
            throw new KyouException(ex);
        }

        throw new KyouException("no suitable constructor found. args: " + Arrays.toString(args));
    }

    /**
     * 搜索打了指定标的，并且继承自某个基类的类
     * <p>
     * 将在给定的Annotation的同名包中进行搜索
     * </p>
     * 
     * @param annotationClass
     *            满足条件的类上面需要打的标
     * @param targetBaseClass
     *            满足条件的类需要继承自的基类
     * @return 满足条件的类的列表
     */
    public static <T, A extends Annotation> List<Class<? extends T>> searchClassesWithAnnotation(Class<A> annotationClass, Class<T> targetBaseClass) {
        // TODO 把Reflections去掉
        Set<Class<?>> classes = new Reflections(annotationClass.getPackage().getName()).getTypesAnnotatedWith(annotationClass);

        List<Class<? extends T>> result = new ArrayList<>();
        for (Class<?> cls : classes)
            if (targetBaseClass.isAssignableFrom(cls))
                result.add(cls.asSubclass(targetBaseClass));

        return result;
    }
}
