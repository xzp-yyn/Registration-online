package com.xzp.vo.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/11/18 11:07
 * @Version 1.0
 */
@Data
@ApiModel("订单查询vo")
public class OrderQueryVo implements Serializable {

    @ApiModelProperty("用户Id")
    private Long userId;
    @ApiModelProperty("订单交易号")
    private String outTradeNo;
    @ApiModelProperty("患者id")
    private Long patientId;
    @ApiModelProperty("患者姓名")
    private String patientName;
    @ApiModelProperty("订单状态")
    private int orderStatus;
    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startTime;
    @ApiModelProperty("截止时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endTime;
    @ApiModelProperty("其它参数")
    private Map<String,Object> param;
}
