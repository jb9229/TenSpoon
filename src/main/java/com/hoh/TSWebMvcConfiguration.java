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
    public static final String FILESYSTEM_PATH              =   "/filesystem/";
    public static final String FILESYSTEM_IMG_PATH          =   FILESYSTEM_PATH+"img/";
    public static final String FILESYSTEM_THUMBNAILS_PATH   =   FILESYSTEM_IMG_PATH+"thumbnails/";

    private Environment environment;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");


        registry.addResourceHandler(FILESYSTEM_PATH+"**")
                .addResourceLocations(Application.FILESERVER);
    }
}
