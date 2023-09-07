package com.zisheng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RuijiTakeoutProjectSpringBootApplication {
    private static final Logger log = LoggerFactory.getLogger(RuijiTakeoutProjectSpringBootApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(RuijiTakeoutProjectSpringBootApplication.class, args);
        log.info("SpringBoot项目启动成功！！！");
    }

}
