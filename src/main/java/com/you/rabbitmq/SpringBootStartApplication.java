package com.you.rabbitmq;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 创建一个新的启动类，并且继承SpringBootServletInitializer类，重写configure方法
 * 
 * @author you
 * @date 2020/06/24
 */
public class SpringBootStartApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 此处的RabbitmqApplication.class为带有@SpringBootApplication注解的启动类
        return builder.sources(RabbitmqApplication.class);
    }

}
