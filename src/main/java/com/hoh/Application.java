package com.hoh;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.util.Properties;

/**
 * Created by test on 2015-10-29.
 */
@SpringBootApplication
public class Application {

    public static final String FILESERVER                   =   "D:/1_work/fileserver/";
    public static final String FILESERVER_IMG               =   "D:/1_work/fileserver/img"; //String webappRoot = servletContext.getRealPath("/");
    public static final String FILESERVER_IMG_PROFILE       =   "D:/1_work/fileserver/img/profile";
    public static final String FILESERVER_IMG_THUMBNAILS    =   "D:/1_work/fileserver/img/thumbnails/";

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
//        return application.sources(Application.class);
//    }


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner init() {
        return (String[] args) -> {
            new File(FILESERVER_IMG).mkdir();
            new File(FILESERVER_IMG_PROFILE).mkdir();
            new File(FILESERVER_IMG_THUMBNAILS).mkdir();
        };
    }
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JavaMailSenderImpl javaMailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setPort(587);
        javaMailSender.setDefaultEncoding("UTF-8");
        javaMailSender.setUsername("jb9229@gmail.com");
        javaMailSender.setPassword("wjdwlsqja83");


        Properties javaMailProperties   =   new Properties();
        javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");
        javaMailProperties.setProperty("mail.smtp.auth", "true");

        javaMailSender.setJavaMailProperties(javaMailProperties);

        return javaMailSender;
    }
}
