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
 * @Date 2022/11/5 17:09
 * @Version 1.0
 */
@Data
public class ScheduleQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "排班日期")
//    @JsonFormat(pattern = "yyyy-MM-dd")
    private String workDate;

    private String hoscode;

    private String depcode;

}
