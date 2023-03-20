package com.xzp.fallback;

import com.xzp.cmn.CmnClient;
import com.xzp.result.ResultCode;
import org.springframework.stereotype.Component;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2023/1/14 9:23
 * @Version 1.0
 */
@Component
public class CmnClientFallback implements CmnClient {
    @Override
    public String getNameByvalue(Integer value) {
        return ResultCode.FEIGN_TIMEOUT.getMessage();
    }

    @Override
    public boolean crawnOrNot(String value) {
        return false;
    }
}
