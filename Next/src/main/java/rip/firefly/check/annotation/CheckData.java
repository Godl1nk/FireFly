package rip.firefly.check.annotation;

import rip.firefly.check.CheckType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckData {
    String name();
    String subType();
    String description() default "";
    boolean enabled() default true;
    boolean autoban() default true;
    boolean experimental() default false;
    int threshold() default 5;
    boolean enterprise() default false;
    CheckType type();
}