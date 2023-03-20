package com.xzp.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/11/9 17:02
 * @Version 1.0
 */
@Configuration
@MapperScan("com.xzp.mapper")
@ComponentScan("com.xzp")
public class UserConfig {
}
