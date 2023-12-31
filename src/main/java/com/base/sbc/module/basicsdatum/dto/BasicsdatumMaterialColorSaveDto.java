package com.base.sbc.module.basicsdatum.dto;

import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialColor;
import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 *
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年6月26日
 */
@Data
@ApiModel("保存物料颜色")
public class BasicsdatumMaterialColorSaveDto extends BasicsdatumMaterialColor {

	@NotBlank(message = "ID必填,新增-1")
	@ApiModelProperty(value = "id", required = true)
	private String id;
	/** 状态(0正常,1停用) */
	@ApiModelProperty(value = "状态(0正常,1停用)")
	private String status;
	/** 备注信息 */
	@ApiModelProperty(value = "备注信息")
	private String remarks;
	/** 物料编号 */
	@ApiModelProperty(value = "物料编号")
	private String materialCode;
	/** 颜色代码 */
	@ApiModelProperty(value = "颜色代码")
	private String colorCode;
	/** 颜色 */
	@ApiModelProperty(value = "颜色")
	private String colorName;
	/** 供应商色号 */
	@ApiModelProperty(value = "供应商色号")
	private String supplierColorCode;
	/** 颜色图片 */
	@ApiModelProperty(value = "颜色图片")
	private String picture;
	/**
	 * 下发状态:0未下发,1已下发
	 */
	@ApiModelProperty(value = "下发状态:0未下发,1已下发")
	private String scmStatus;


	private String parentId;

}
