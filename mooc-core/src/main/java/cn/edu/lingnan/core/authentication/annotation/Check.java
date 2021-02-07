package cn.edu.lingnan.core.authentication.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Check {
    String value() default "";
}
