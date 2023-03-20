package com.xzp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xzp.model.order.Order;
import com.xzp.vo.order.OrderQueryVo;
import com.xzp.vo.order.DataShowVo;

import java.util.Map;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/11/16 9:51
 * @Version 1.0
 */
public interface OrderService extends IService<Order> {
    Long saveOrder(String patientid, String scheduleid);

    Order getOrderByid(Long id);

    void cancelStatus(Long id);

    IPage<Order> findall(Page<Order> page1, OrderQueryVo queryVo);

    Map<String,Object> selectOrderList(DataShowVo dataShowVo);


}
