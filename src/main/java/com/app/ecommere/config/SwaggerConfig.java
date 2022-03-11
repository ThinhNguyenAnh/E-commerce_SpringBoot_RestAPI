package com.app.ecommere.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Configuration
public class SwaggerConfig {

    public static final String AUTHORIZATION_HEADER = AUTHORIZATION;

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Spring Boot E-Commerce REST API",
                "Spring Boot E-Commerce REST API Documentation",
                "1",
                "Terms of service",
                new Contact("Thinh Nguyen Anh", "Thinh.com", "Thinh@gmail.com"),
                "License of API",
                "API license URL",
                Collections.emptyList()
        );
    }

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
//                .securityContexts(Arrays.asList(securityContext()))
//                .securitySchemes(Arrays.asList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.any())  //select any api from this package
                .paths(PathSelectors.any())
                .build();
    }

}
