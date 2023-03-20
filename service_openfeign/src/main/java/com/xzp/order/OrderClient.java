package com.xzp.order;

import com.xzp.fallback.CmnClientFallback;
import com.xzp.fallback.OrderFallback;
import com.xzp.result.Result;
import com.xzp.vo.order.DataShowVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/11/21 19:08
 * @Version 1.0
 */
@FeignClient(value = "orderserver",fallback = OrderFallback.class)
@Repository
public interface OrderClient {

    @PostMapping("/admin/order/orderData")
    public Result orderDatas(DataShowVo dataShowVo);
}
