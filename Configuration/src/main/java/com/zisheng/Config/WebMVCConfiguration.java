package com.zisheng.Config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 静态资源映射：
 * 步骤：
 * 第一步：定义一个类，继承WebMvcConfigurationSupport类
 * 第二步：重写addResourceHandlers方法，设置映射关系
 */
@Slf4j
@Configuration
public class WebMVCConfiguration extends WebMvcConfigurationSupport {
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
            log.info("开始进行静态资源映射");
            registry.addResourceHandler("/backend/**")// 设置映射关系，/**表示路径下的任意文件
                    .addResourceLocations("classpath:/backend/");//classpath表示resources文件的的位置
            registry.addResourceHandler("/front/**")// 设置映射关系，/**表示路径下的任意文件
                .addResourceLocations("classpath:/front/");//classpath表示resources文件的的位置
    }
}
