package com.xzp.vo.hosp;

import lombok.Data;

import java.io.Serializable;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/11/22 10:59
 * @Version 1.0
 */
@Data
public class HospStaticVo  implements Serializable {

    private String hosname;

    private Integer Count;

    private String depname;

    private Integer orderCount;

    private Integer scheduleCount;
}
