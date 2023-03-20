package com.xzp.vo.hosp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/11/17 15:41
 * @Version 1.0
 */
@Data
@ApiModel("更新排班预约数的mq")
public class ScheduleMqVo {

    @ApiModelProperty("排班ID")
    private String scheduleId;

    @ApiModelProperty("可预约数")
    private Integer reservedNumber;

    @ApiModelProperty("剩余预约数")
    private Integer availableNumber;
}
