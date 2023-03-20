package com.xzp.vo.depart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/10/26 11:00
 * @Version 1.0
 */
@Data
@ApiModel(description = "科室查询实体")
public class DepartmentVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("bigcode")
    private String  depcode;

    @ApiModelProperty("bigname")
    private String  depname;

    @ApiModelProperty("children")
    private List<DepartmentVo> children;
}
