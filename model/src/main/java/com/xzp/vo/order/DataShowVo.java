package com.xzp.vo.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/11/21 18:28
 * @Version 1.0
 */
@Data
@ApiModel(description = "动态显示order的vo")
public class DataShowVo implements Serializable {

    @ApiModelProperty("预约日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date reserveDate;

    @ApiModelProperty("预约数量")
    private Integer count;

    @ApiModelProperty("医院名称")
    private String hosname;
}
