package com.xzp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xzp.enums.OrderStatusEnum;
import com.xzp.model.order.Order;
import com.xzp.result.Result;
import com.xzp.service.OrderService;
import com.xzp.vo.order.OrderQueryVo;
import com.xzp.vo.order.DataShowVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/11/18 16:04
 * @Version 1.0
 */
@RestController
@RequestMapping("/admin/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/orders/{page}/{limit}")
    public Result listOrder(@PathVariable int page,
                            @PathVariable int limit,
                            OrderQueryVo queryVo){
        Page<Order> objectPage = new Page<>(page, limit);
        IPage<Order> iPage = orderService.findall(objectPage, queryVo);
        return Result.success(iPage);
    }
    @GetMapping("/allstatus")
    public Result allstatus(){
        return Result.success(OrderStatusEnum.getStatusList());
    }
    @ApiOperation("根据id查询订单")
    @GetMapping("/{id}")
    public Result getOrderOne(@PathVariable Long id){
        Order order= orderService.getOrderByid(id);
        return Result.success(order);
    }

    @ApiOperation("订单数据")
    @PostMapping("/orderData")
    public Result orderDatas(@RequestBody DataShowVo dataShowVo){
        Map<String, Object> map = orderService.selectOrderList(dataShowVo);
        return Result.success(map);
    }
}

