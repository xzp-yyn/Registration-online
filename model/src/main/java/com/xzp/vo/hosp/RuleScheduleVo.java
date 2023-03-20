package com.xzp.vo.hosp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/11/5 9:18
 * @Version 1.0
 */
@Data
public class RuleScheduleVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "排班日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date workDate;

    @ApiModelProperty(value = "排班时间（0：上午 1：下午）")
    private Integer workTime;

    @ApiModelProperty(value = "就诊医生人数")
    private Integer docCount;

    @ApiModelProperty(value = "星期几")
    private String weekday;

    @ApiModelProperty(value = "总预约数")
    private Integer reservedNumber;

    @ApiModelProperty(value = "剩余预约数")
    private Integer availableNumber;

    @ApiModelProperty(value = "排班状态（-1：停诊 0：停约 1：可约）")
    private Integer status;

}
