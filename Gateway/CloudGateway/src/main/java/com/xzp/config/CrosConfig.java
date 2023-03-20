package com.xzp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * 时间未到，资格未够，继续努力！
 * @Author xuezhanpeng
 * @Date 2022/11/7 11:00
 * @Version 1.0
 */
@Configuration
public class CrosConfig {
    @Bean
    public CorsWebFilter corsWebFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //1、配置跨域
        //允许哪些头进行跨域
        corsConfiguration.addAllowedHeader("*");
        //允许哪些请求方式进行跨域
        corsConfiguration.addAllowedMethod("*");
        //允许哪些请求来源进行跨域
        corsConfiguration.addAllowedOrigin("*");
        //是否允许携带cookie进行跨域，否则跨域请求会丢失cookie信息
        corsConfiguration.setAllowCredentials(true);
        source.registerCorsConfiguration("/**",corsConfiguration);

        return new CorsWebFilter(source);
    }
}
