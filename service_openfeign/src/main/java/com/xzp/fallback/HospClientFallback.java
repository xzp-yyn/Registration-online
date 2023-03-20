package com.xzp.fallback;

import com.xzp.hosp.HospClient;
import com.xzp.result.Result;
import com.xzp.result.ResultCode;
import com.xzp.vo.order.DataShowVo;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2023/1/14 9:27
 * @Version 1.0
 */
@Component
public class HospClientFallback implements HospClient {
    @Override
    public Result updateScheduleById(Map<String, Object> requestmap) {
        return  Result.fail(ResultCode.FEIGN_TIMEOUT);
    }

    @Override
    public Result selectScheduleById(String scheduleId) {
        return  Result.fail(ResultCode.FEIGN_TIMEOUT);
    }

    @Override
    public Result getHosByHoscode(String hoscode) {
        return  Result.fail(ResultCode.FEIGN_TIMEOUT);
    }

    @Override
    public Result getMongoHospByhosCode(String hoscode) {
        return  Result.fail(ResultCode.FEIGN_TIMEOUT);
    }

    @Override
    public Map<String, Object> hospDepartData(DataShowVo dataShowVo) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("null","服务超时未响应出错");
        return map;
    }
}
