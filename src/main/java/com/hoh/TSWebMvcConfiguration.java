package com.hoh;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by jeong on 2016-03-16.
 */
@Configuration
public class TSWebMvcConfiguration extends WebMvcConfigurerAdapter {

    private Environment environment;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/fileserver/img/***")
                .addResourceLocations(Application.ROOT);

    }
}
