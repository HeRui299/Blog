package com.herui.blog.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Logininterceptor())
                 .addPathPatterns("/admin/**")
                 .excludePathPatterns("/admin")
                 .excludePathPatterns("/admin/login")
                 .excludePathPatterns("/admin/logout");
    }
}