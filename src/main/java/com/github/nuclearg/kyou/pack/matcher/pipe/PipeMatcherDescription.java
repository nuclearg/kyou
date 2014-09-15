package com.github.nuclearg.kyou.pack.matcher.pipe;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示一个类是{@link PipeMatcher}
 * 
 * @author ng
 * 
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PipeMatcherDescription {
    String value();
}
