package com.zisheng.Annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({ com.zisheng.Config.MybatisPlusInterceptorConfig.class,
        com.zisheng.MyUtils.JWTUtils.class,
        com.zisheng.Config.MyRedisTemplateConfig.class,
        com.zisheng.Config.WebMVCConfiguration.class})
public @interface MyAnnotation {
}
