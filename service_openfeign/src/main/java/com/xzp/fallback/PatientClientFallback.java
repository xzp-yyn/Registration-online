package com.xzp.fallback;

import com.xzp.result.Result;
import com.xzp.result.ResultCode;
import com.xzp.user.PatientClient;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2023/1/14 9:33
 * @Version 1.0
 */
public class PatientClientFallback implements PatientClient {
    @Override
    public Result findByid(String id) {
        return Result.fail(ResultCode.FEIGN_TIMEOUT);
    }
}
