package com.condocam.condomanager.infra.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.condocam.condomanager.infra.interceptors.HeadersValidator;
import org.springframework.lang.NonNull;

@Configuration
public class InterceptorsRegistry implements WebMvcConfigurer {

    @Autowired
    private HeadersValidator headerValidatorInterceptor;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(headerValidatorInterceptor);
    }
}
