package com.xzp.vo.hosp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/10/26 11:00
 * @Version 1.0
 */
@Data
@ApiModel(description = "状态修改实体")
public class HospitalStatusVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @NotNull(message = "不能为空")
    private Long id;

    @ApiModelProperty("医院状态0:1")
    @NotNull(message = "不能为空")
    private Integer status;


}
