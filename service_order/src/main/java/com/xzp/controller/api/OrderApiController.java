package com.xzp.controller.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xzp.enums.OrderStatusEnum;
import com.xzp.model.order.Order;
import com.xzp.result.Result;
import com.xzp.service.OrderService;
import com.xzp.vo.order.OrderQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/11/16 9:59
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/order/auth")
@Api(tags = "订单接口")
public class OrderApiController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("新增订单")
    @GetMapping("/save/{patientid}/{scheduleid}")
    public Result saveOrder(@PathVariable(value = "patientid") String patientid,
                            @PathVariable(value = "scheduleid") String scheduleid
                            ){
        Long orderId=orderService.saveOrder(patientid,scheduleid);
        return Result.success(orderId);
    }

    @ApiOperation("查询所有订单")
    @PostMapping("/listall/{page}/{limit}")
    public Result findAll(@PathVariable int page,
                          @PathVariable int limit,
                          @RequestBody OrderQueryVo queryVo
                          ){
        Page<Order> page1 = new Page<>(page, limit);
        IPage<Order> model= orderService.findall(page1,queryVo);
        return Result.success(model);
    }

    @ApiOperation("根据id查询订单")
    @GetMapping("/{id}")
    public Result getOrderByid(@PathVariable Long id){
        Order order= orderService.getOrderByid(id);
        return Result.success(order);
    }
    @ApiOperation("取消预约订单")
    @GetMapping("/cancel/{id}")
    public Result cancelOrder(@PathVariable Long id){
        orderService.cancelStatus(id);
        return Result.success().message("已取消订单");
    }

    @ApiOperation("获取所有订单状态")
    @GetMapping("/orderStatus")
    public Result getAllstatus(){
        return Result.success(OrderStatusEnum.getStatusList());
    }

}
