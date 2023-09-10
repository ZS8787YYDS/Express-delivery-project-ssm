package com.zisheng.Config;

import com.zisheng.Converter.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

//@Slf4j
@Configuration
public class WebMVCConfiguration extends WebMvcConfigurationSupport {
    /**
     * 静态资源映射：
     * 步骤：
     * 第一步：定义一个类，继承WebMvcConfigurationSupport类
     * 第二步：重写addResourceHandlers方法，设置映射关系
     * @param registry
     */
    // 创建日志记录对象
    private static final Logger log = LoggerFactory.getLogger(WebMVCConfiguration.class);
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
            log.info("开始进行静态资源映射");
            registry.addResourceHandler("/backend/**")// 设置映射关系，/**表示路径下的任意文件
                    .addResourceLocations("classpath:/backend/");//classpath表示resources文件的的位置
            registry.addResourceHandler("/front/**")// 设置映射关系，/**表示路径下的任意文件
                .addResourceLocations("classpath:/front/");//classpath表示resources文件的的位置
    }

    /**
     * 扩展消息转换器,
     * 第一步：创建消息转换器对象，
     * 第二步：创建对象转换器对象，设置到消息转化器对象中
     * 第三步：将消息转换器添加到MVC框架的转换器集合当中，并且设置在第一位,保证优先使用自己设置的消息转换器
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        // 添加对象转换器对象
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        // 将消息转换器对象添加到MVC框架的转化器集合当中，并且放在第一位
        converters.add(0,messageConverter);
        log.info("消息转换器设置成功");
    }
}
