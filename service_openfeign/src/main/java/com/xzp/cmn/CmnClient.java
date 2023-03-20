package com.xzp.cmn;

import com.xzp.fallback.CmnClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 时间未到，资格未够，继续努力！
 * @Author xuezhanpeng
 * @Date 2022/11/2 16:26
 * @Version 1.0
 */
@FeignClient(value = "cmnserver",fallback = CmnClientFallback.class)
@Repository
public interface CmnClient {

    @GetMapping("/admin/cmn/{value}")
    public String getNameByvalue(@PathVariable(value = "value") Integer value);

    @GetMapping("/admin/cmn/crawn")
    public boolean crawnOrNot(@RequestParam("value") String value);
}
