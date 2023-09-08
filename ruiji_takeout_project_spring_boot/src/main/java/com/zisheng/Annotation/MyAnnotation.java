package com.zisheng.Annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({com.zisheng.Inteceptor.LoginInterceptor.class,com.zisheng.MyUtils.JWTUtils.class})
public @interface MyAnnotation {
}
