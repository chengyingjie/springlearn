package com.spring.core.annotation;

import java.lang.annotation.*;


@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {
    String value() default  "";
}
