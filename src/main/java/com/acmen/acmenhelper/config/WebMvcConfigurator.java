package com.acmen.acmenhelper.config;

import com.acmen.acmenhelper.common.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author gaowenfeng
 * @date 2018/5/18
 */
@Configuration
public class WebMvcConfigurator implements WebMvcConfigurer {

    @Bean
    public RequestInterceptor requestInterceptor(){
        return new RequestInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor());
    }
}
