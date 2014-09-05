package net.nuclearg.kyou.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import org.reflections.Reflections;

/**
 * 类型相关的工具类
 * 
 * @author ng
 * 
 */
public class ClassUtils {
    /**
     * 创建指定类型的实例，调用其无参构造函数
     * 
     * @param cls
     *            类型
     * @return 实例
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> T newInstance(Class<T> cls) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<T> constructor = cls.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    /**
     * 搜索打了指定标的类
     * 
     * @param annotationClass
     * @return
     */
    public static Set<Class<?>> searchClassesWithAnnotation(Class<? extends Annotation> annotationClass) {
        return new Reflections(annotationClass.getPackage().getName()).getTypesAnnotatedWith(annotationClass);

    }

}
