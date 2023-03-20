package com.xzp.controller;

import com.xzp.hosp.HospClient;
import com.xzp.order.OrderClient;
import com.xzp.result.Result;
import com.xzp.vo.order.DataShowVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/11/21 19:35
 * @Version 1.0
 */
@RestController
@Api(tags = "后台可视化数据接口")
@RequestMapping("/admin/statisdata")
public class StaticController {

    @Autowired
    private OrderClient orderClient;

    @Autowired
    private HospClient hospClient;

    @ApiOperation("根据条件得到订单数据")
    @PostMapping
    public Result orderdatas(@RequestBody DataShowVo showVo){
        Result orderDatas = orderClient.orderDatas(showVo);
        return orderDatas;
    }

    @ApiOperation("根据条件得到医院数据")
    @PostMapping("/hosp")
    public Result hospData(@RequestBody DataShowVo showVo){
        Map<String, Object> map = hospClient.hospDepartData(showVo);
        return Result.success(map);
    }

}
