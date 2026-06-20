package com.yo.yoprj;

import com.yo.yoprj.config.AppJwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppJwtProperties.class)
public class YoprjApplication {

    public static void main(String[] args) {
        SpringApplication.run(YoprjApplication.class, args);
    }

}

/*
* @Repository, @Service, @Component
* @Controller, @RestController
* @Configuration
*
* @Bean
* */