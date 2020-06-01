package com.compasso.customersintegrator.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.base.Predicates;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.compasso.customersintegrator.controller"))
                .paths(Predicates.not(PathSelectors.regex("/error")))
                .build()
                .globalResponseMessage(RequestMethod.GET, Arrays.asList(
                        new ResponseMessageBuilder().code(HttpStatus.OK.value()).message(HttpStatus.OK.name()).build(),
                        new ResponseMessageBuilder().code(HttpStatus.BAD_REQUEST.value()).message(HttpStatus.BAD_REQUEST.name()).build(),
                        new ResponseMessageBuilder().code(HttpStatus.NOT_FOUND.value()).message(HttpStatus.NOT_FOUND.name()).build(),
                        new ResponseMessageBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(HttpStatus.INTERNAL_SERVER_ERROR.name()).build()
                )).globalResponseMessage(RequestMethod.POST, Arrays.asList(
                        new ResponseMessageBuilder().code(HttpStatus.CREATED.value()).message(HttpStatus.CREATED.name()).build(),
                        new ResponseMessageBuilder().code(HttpStatus.BAD_REQUEST.value()).message(HttpStatus.BAD_REQUEST.name()).build(),
                        new ResponseMessageBuilder().code(HttpStatus.CONFLICT.value()).message(HttpStatus.CONFLICT.name()).build(),
                        new ResponseMessageBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(HttpStatus.INTERNAL_SERVER_ERROR.name()).build()
                )).globalResponseMessage(RequestMethod.PUT, Arrays.asList(
                        new ResponseMessageBuilder().code(HttpStatus.NO_CONTENT.value()).message(HttpStatus.NO_CONTENT.name()).build(),
                        new ResponseMessageBuilder().code(HttpStatus.NOT_FOUND.value()).message(HttpStatus.NOT_FOUND.name()).build(),
                        new ResponseMessageBuilder().code(HttpStatus.BAD_REQUEST.value()).message(HttpStatus.BAD_REQUEST.name()).build(),
                        new ResponseMessageBuilder().code(HttpStatus.CONFLICT.value()).message(HttpStatus.CONFLICT.name()).build(),
                        new ResponseMessageBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(HttpStatus.INTERNAL_SERVER_ERROR.name()).build()
                )).globalResponseMessage(RequestMethod.PATCH, Arrays.asList(
                        new ResponseMessageBuilder().code(HttpStatus.OK.value()).message(HttpStatus.NO_CONTENT.name()).build(),
                        new ResponseMessageBuilder().code(HttpStatus.NOT_FOUND.value()).message(HttpStatus.NOT_FOUND.name()).build(),
                        new ResponseMessageBuilder().code(HttpStatus.BAD_REQUEST.value()).message(HttpStatus.BAD_REQUEST.name()).build(),
                        new ResponseMessageBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(HttpStatus.INTERNAL_SERVER_ERROR.name()).build()
                )).globalResponseMessage(RequestMethod.DELETE, Arrays.asList(
                        new ResponseMessageBuilder().code(HttpStatus.NO_CONTENT.value()).message(HttpStatus.NO_CONTENT.name()).build(),
                        new ResponseMessageBuilder().code(HttpStatus.NOT_FOUND.value()).message(HttpStatus.NOT_FOUND.name()).build(),
                        new ResponseMessageBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(HttpStatus.INTERNAL_SERVER_ERROR.name()).build()
                )).apiInfo(metadata());
    }

    private ApiInfo metadata() {
        return new ApiInfoBuilder().title("Customers Integrator API")
                .description("Customers Integrator Project - Spring Boot REST API for Customers management")
                .version("1.0.0")
                .build();
    }

}
