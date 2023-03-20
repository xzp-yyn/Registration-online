package com.xzp.hosp;

import com.xzp.fallback.CmnClientFallback;
import com.xzp.fallback.HospClientFallback;
import com.xzp.result.Result;
import com.xzp.vo.order.DataShowVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/11/16 10:18
 * @Version 1.0
 */
@FeignClient(name = "hospserver",fallback = HospClientFallback.class)
@Repository
public interface HospClient {

    @PostMapping("/admin/hosp/depart/update")
    public Result updateScheduleById(Map<String,Object> requestmap);

    @GetMapping("/admin/hosp/depart/schedule/{scheduleId}")
    public Result selectScheduleById(@PathVariable(value = "scheduleId") String scheduleId);

    @GetMapping("/admin/hosp/findByhoscode/{hoscode}")
    public Result getHosByHoscode(@PathVariable(value = "hoscode") String hoscode);

    @GetMapping("/admin/hosp/mongohosp/byhoscode/{hoscode}")
    public Result getMongoHospByhosCode(@PathVariable(value = "hoscode") String hoscode);

    @PostMapping("/admin/hosp/mongohosp/hospdata")
    public Map<String,Object> hospDepartData(DataShowVo dataShowVo);
}
