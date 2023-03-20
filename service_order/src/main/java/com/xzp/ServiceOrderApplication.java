package com.xzp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.xzp")
@EnableTransactionManagement
@EnableCircuitBreaker
public class ServiceOrderApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(ServiceOrderApplication.class,args);
    }
}
