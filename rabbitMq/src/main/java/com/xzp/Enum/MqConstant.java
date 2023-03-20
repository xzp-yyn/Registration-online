package com.xzp.Enum;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/11/17 14:46
 * @Version 1.0
 */
public class MqConstant {
    /**
     * 预约下单
     */
    public static final String EXCHANGE_DIRECT_ORDER = "exchange.direct.order";
    public static final String ROUTING_ORDER = "order";
    //队列
    public static final String QUEUE_ORDER  = "queue.order";
    /**
     * 取消预约下单
     */
    public static final String EXCHANGE_DIRECT_CANCEL = "exchange.direct.cancel";
    public static final String ROUTING_CANCEL = "cancel";
    public static final String QUEUE_CANCEL="queue.cancel";

    /**
     * 定时任务
     */
    public static final String EXCHANGE_DIRECT_TASK = "exchange.direct.task";
    public static final String ROUTING_TASK_8 = "task.8";
    //队列
    public static final String QUEUE_TASK_8 = "queue.task.8";
}
