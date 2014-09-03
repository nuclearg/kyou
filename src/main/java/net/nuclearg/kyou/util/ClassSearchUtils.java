package net.nuclearg.kyou.util;

import java.lang.annotation.Annotation;
import java.util.Set;

import net.nuclearg.kyou.pack.expr.IntegerExpr;

import org.reflections.Reflections;

public class ClassSearchUtils {
    public static Set<Class<?>> searchClassesWithAnnotation(String packageName, Class<? extends Annotation> annotationClass) {
        return new Reflections(IntegerExpr.class.getPackage().getName()).getTypesAnnotatedWith(annotationClass);

    }

}
