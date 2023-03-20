package com.xzp.user;

import com.xzp.fallback.PatientClientFallback;
import com.xzp.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 时间未到，资格未够，继续努力！
 * @Author xuezhanpeng
 * @Date 2022/11/16 10:16
 * @Version 1.0
 */
@FeignClient(name = "userserver",fallback = PatientClientFallback.class)
@Repository
public interface PatientClient {

    @GetMapping("/api/patient/auth/{id}")
    public Result findByid(@PathVariable(value = "id") String id);
}
