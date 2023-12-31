package com.zisheng;

import com.zisheng.Annotation.MyAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@MyAnnotation
@ServletComponentScan// 开启Servlet组件支持
@EnableCaching// 开启缓存注解功能
public class RuijiTakeoutProjectSpringBootApplication {
    private static final Logger log = LoggerFactory.getLogger(RuijiTakeoutProjectSpringBootApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(RuijiTakeoutProjectSpringBootApplication.class, args);
        log.info("SpringBoot项目启动成功！！！");
    }

}
