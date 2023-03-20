package com.xzp.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import  java.util.function.Predicate;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/10/25 17:12
 * @Version 1.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket docket2(){
        //需要指定swagger的版本
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        //swagger 具体的信息对象
        ApiInfo apiInfo=new ApiInfoBuilder()
                .contact(new Contact("XZP","http://192.168.137.1:9999/","3210625646@qq.com"))
                .title("医通后端的Swagger帮助文档")
                .version("1.1")
                .description("Swagger是一款使用OpenApi规范的Rest风格接口帮助文档")
                .build();
        docket.apiInfo(apiInfo);
        docket.groupName("后端API");
        docket= docket.select()
                .apis(RequestHandlerSelectors.basePackage("com.xzp.controller"))
                .paths(PathSelectors.regex("/admin/.*"))
                .build();
        return docket;
    }

    @Bean
    public Docket docket(){
        //需要指定swagger的版本
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        //swagger 具体的信息对象
        ApiInfo apiInfo=new ApiInfoBuilder()
                .contact(new Contact("XZP","http://192.168.137.1:9999/","3210625646@qq.com"))
                .title("医通前端的Swagger帮助文档")
                .version("1.1")
                .description("Swagger是一款使用OpenApi规范的Rest风格接口帮助文档")
                .build();
        docket.apiInfo(apiInfo);

        docket.groupName("前端API");
        docket= docket.select()
                .apis(RequestHandlerSelectors.basePackage("com.xzp.controller"))
                .paths(PathSelectors.regex("/api/.*"))
                .build();
        return docket;
    }


}
