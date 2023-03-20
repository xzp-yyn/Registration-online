package com.xzp.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author xuezhanpeng
 * @Date 2022/10/10 11:02
 * @Version 1.0
 */
@Configuration
@MapperScan(basePackages = "org.xzp.mapper")
public class MybatisPlusConfig {

    //MP拦截器添加了乐观锁的插件和分页插件
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

//    @Bean
//    public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor(){
//        return new OptimisticLockerInnerInterceptor();
//    }
}
