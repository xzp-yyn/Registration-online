package com.xzp.model.hosp;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import com.xzp.model.base.BaseEntity;
import com.xzp.validat.SaveGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(description = "医院设置")
@TableName("hosptial")
public class HospitalSet extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "医院名称")
	@TableField("hosname")
	@NotBlank(groups = {SaveGroup.class},message = "医院名称不能为空")
	private String hosname;

	@ApiModelProperty(value = "医院编号")
	@TableField("hoscode")
	@NotBlank(groups = {SaveGroup.class},message = "医院编号不能为空")
	private String hoscode;

	@ApiModelProperty(value = "api基础路径")
	@TableField("api_url")
	@NotBlank(groups = {SaveGroup.class},message = "url不能为空")
	private String apiUrl;

	@ApiModelProperty(value = "签名秘钥")
	@TableField("sign_key")
	private String signKey;

	@ApiModelProperty(value = "联系人姓名")
	@NotBlank(groups = {SaveGroup.class},message = "姓名不能为空")
	@TableField("contact_name")
	private String contactName;

	@ApiModelProperty(value = "联系人手机")
	@TableField("contact_phone")
	@Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$",message = "手机号格式不正确")
	private String contactPhone;

	@ApiModelProperty(value = "状态0不可用，1可用",required = false)
	@TableField("status")
	private Integer status;

}

