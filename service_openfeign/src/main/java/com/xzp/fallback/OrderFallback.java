package com.xzp.fallback;

import com.xzp.order.OrderClient;
import com.xzp.result.Result;
import com.xzp.result.ResultCode;
import com.xzp.vo.order.DataShowVo;
import org.springframework.stereotype.Component;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2023/1/14 9:32
 * @Version 1.0
 */
@Component
public class OrderFallback implements OrderClient {
    @Override
    public Result orderDatas(DataShowVo dataShowVo) {
        return Result.fail(ResultCode.FEIGN_TIMEOUT);
    }
}
