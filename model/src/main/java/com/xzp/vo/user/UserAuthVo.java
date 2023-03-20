package com.xzp.vo.user;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Map;

@Data
@ApiModel(description="会员认证对象")
public class UserAuthVo implements Serializable {

    @ApiModelProperty(value = "用户姓名")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "证件类型")
    @TableField("certificates_type")
    private String certificatesType;

    @ApiModelProperty(value = "证件编号")
    @TableField("certificates_no")
    private String certificatesNo;

    @ApiModelProperty(value = "证件路径")
    @TableField("certificates_url")
    private String certificatesUrl;

    @ApiModelProperty(value = "其它参数")
    @TableField(exist = false)
    private Map<String,Object> param;

}
