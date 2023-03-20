package com.xzp.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/11/17 11:59
 * @Version 1.0
 */
@Service
public class RabbitService {

    @Autowired
    private RabbitTemplate template;
    /**
     * 发送mq消息
     * @param exchange   交换
     * @param routingKey 路由关键
     * @param message    消息
     * @return boolean
     */
    public boolean sendMqMessage(String exchange,String routingKey,Object message){
        template.convertAndSend(exchange,routingKey,message);
        return true;
    }

}
