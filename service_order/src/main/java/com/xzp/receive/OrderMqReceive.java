package com.xzp.receive;

import com.rabbitmq.client.Channel;
import com.xzp.Enum.MqConstant;
import com.xzp.model.order.Order;
import com.xzp.service.OrderService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/11/18 9:23
 * @Version 1.0
 */
@Component
public class OrderMqReceive {
    @Autowired
    private OrderService orderService;

    @RabbitListener(bindings = @QueueBinding(
            value =@Queue(value = MqConstant.QUEUE_CANCEL,durable = "true"),
            exchange = @Exchange(MqConstant.EXCHANGE_DIRECT_ORDER),
            key = {MqConstant.ROUTING_ORDER}
    ))
    public void cancelOrder(Order order, Message message, Channel channel){
        orderService.removeById(order.getId());
    }

}
