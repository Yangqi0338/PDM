package com.base.sbc.module.basicsdatum.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 物料档案主表保存实体
 * 
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年6月26日
 */
@Data
@ApiModel("物料档案主表保存实体")
public class BasicsdatumMaterialSaveDto {
	@NotBlank(message = "ID必填,新增-1")
	@ApiModelProperty(value = "id", required = true)
	private String id;
	/** 状态(0正常,1停用) */
	@ApiModelProperty(value = "状态(0正常,1停用)")
	private String status;
	/** 备注信息 */
	@ApiModelProperty(value = "备注信息")
	private String remarks;
	/** 类别（物料分类id） */
	@ApiModelProperty(value = "类别（物料分类id）")
	private String categoryId;
	/** 类别（物料分类ids） */
	@ApiModelProperty(value = "类别（物料分类ids）")
	private String categoryIds;
	/** 类别名称（物料分类名称） */
	@ApiModelProperty(value = "类别名称（物料分类名称）")
	private String categoryName;
	/** 物料属性 */
	@ApiModelProperty(value = "物料属性")
	private String materialType;
	/** 物料属性名称 */
	@ApiModelProperty(value = "物料属性名称")
	private String materialTypeName;
	/** 图片地址 */
	@ApiModelProperty(value = "图片地址")
	private String imageUrl;
	/** 材料 */
	@ApiModelProperty(value = "材料")
	private String materialCodeName;
	/** 物料名称 */
	@ApiModelProperty(value = "物料名称")
	private String materialName;
	/** 物料编号 */
	@ApiModelProperty(value = "物料编号")
	private String materialCode;
	/** 品牌 */
	@ApiModelProperty(value = "品牌")
	private String brand;
	/** 品牌名称 */
	@ApiModelProperty(value = "品牌名称")
	private String brandName;
	/** 旧料号 */
	@ApiModelProperty(value = "旧料号")
	private String materialCodeOld;
	/** 年份 */
	@ApiModelProperty(value = "年份")
	private String year;
	/** 年份名称 */
	@ApiModelProperty(value = "年份名称")
	private String yearName;
	/** 季节 */
	@ApiModelProperty(value = "季节")
	private String season;
	/** 季节名称 */
	@ApiModelProperty(value = "季节名称")
	private String seasonName;
	/** 材料类型 */
	@ApiModelProperty(value = "材料类型")
	private String materialCategory;
	/** 材料类型名称 */
	@ApiModelProperty(value = "材料类型名称")
	private String materialCategoryName;
	/** 形状功能描述 */
	@ApiModelProperty(value = "形状功能描述")
	private String shapeFunctionDescription;
	/** 色号和型号 */
	@ApiModelProperty(value = "色号和型号")
	private String colorAndModel;
	/** 厂家成分 */
	@ApiModelProperty(value = "厂家成分")
	private String factoryComposition;
	/** 面料成分 */
	@ApiModelProperty(value = "面料成分")
	private String ingredient;
	/** 面料成分说明 */
	@ApiModelProperty(value = "面料成分说明")
	private String ingredientSay;
	/** 门幅 */
	@ApiModelProperty(value = "门幅")
	private BigDecimal translate;
	/** 克重 */
	@ApiModelProperty(value = "克重")
	private BigDecimal gramWeight;
	/** 工艺要求 */
	@ApiModelProperty(value = "工艺要求")
	private String processRequire;
	/** 辅料材质 */
	@ApiModelProperty(value = "辅料材质")
	private String auxiliaryMaterial;
	/** 经缩 */
	@ApiModelProperty(value = "经缩")
	private BigDecimal longitudeShrink;
	/** 纬缩 */
	@ApiModelProperty(value = "纬缩")
	private BigDecimal latitudeShrink;
	/** 损耗% */
	@ApiModelProperty(value = "损耗%")
	private BigDecimal lossRate;
	/** 采购单位 */
	@ApiModelProperty(value = "采购单位")
	private String purchaseUnitCode;
	/** 采购单位 */
	@ApiModelProperty(value = "采购单位")
	private String purchaseUnitName;
	/** 库存单位 */
	@ApiModelProperty(value = "库存单位")
	private String stockUnitCode;
	/** 库存单位 */
	@ApiModelProperty(value = "库存单位")
	private String stockUnitName;
	/** 采购转库存 */
	@ApiModelProperty(value = "采购转库存")
	private String purchaseConvertStock;
	/** 默认供应商id */
	@ApiModelProperty(value = "默认供应商id")
	private String supplierId;
	/** 默认供应商名称 */
	@ApiModelProperty(value = "默认供应商名称")
	private String supplierName;
	/** 来源类型 */
	@ApiModelProperty(value = "来源类型")
	private String sourceType;
	/** 来源单号 */
	@ApiModelProperty(value = "来源单号")
	private String sourceBillCode;
	/** 成分确认 */
	@ApiModelProperty(value = "成分确认")
	private String ingredientConfirm;
	/** 送检单位 */
	@ApiModelProperty(value = "送检单位")
	private String checkCompany;
	/** 送检单位名称 */
	@ApiModelProperty(value = "送检单位名称")
	private String checkCompanyName;
	/** 质检结果 */
	@ApiModelProperty(value = "质检结果")
	private String checkResult;
	/** 质检日期 */
	@ApiModelProperty(value = "质检日期")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date checkDate;
	/** 公斤米数 */
	@ApiModelProperty(value = "公斤米数")
	private BigDecimal kgMNum;
	/** 长度 */
	@ApiModelProperty(value = "长度")
	private String length;
	/** 物料来源 */
	@ApiModelProperty(value = "物料来源")
	private String materialSource;
	/** 物料来源 */
	@ApiModelProperty(value = "物料来源")
	private String materialSourceName;
	/** 直径 */
	@ApiModelProperty(value = "直径")
	private String diameter;
	/** 询价编号 */
	@ApiModelProperty(value = "询价编号")
	private String inquiryNo;
	/** 开发员id */
	@ApiModelProperty(value = "开发员id")
	private String devId;
	/** 开发员 */
	@ApiModelProperty(value = "开发员")
	private String devName;
	/** 采购组 */
	@ApiModelProperty(value = "采购组")
	private String purchaseDept;
	/** 采购组名称 */
	@ApiModelProperty(value = "采购组名称")
	private String purchaseDeptName;
	/** 采购员id */
	@ApiModelProperty(value = "采购员id")
	private String purchaseId;
	/** 采购员 */
	@ApiModelProperty(value = "采购员")
	private String purchaseName;
	/** 纱支规格 */
	@ApiModelProperty(value = "纱支规格")
	private String specification;
	/** 密度 */
	@ApiModelProperty(value = "密度")
	private String density;
	/** 库存可用量 */
	@ApiModelProperty(value = "库存可用量")
	private BigDecimal inventoryAvailability;
	/** 门幅/规格组 */
	@ApiModelProperty(value = "门幅/规格组")
	private String widthGroup;
	/** 门幅/规格组名称 */
	@ApiModelProperty(value = "门幅/规格组名称")
	private String widthGroupName;
	/** 采购模式 */
	@ApiModelProperty(value = "采购模式")
	private String procMode;
	/** 采购模式名称 */
	@ApiModelProperty(value = "采购模式名称")
	private String procModeName;
	/** 领料方式 */
	@ApiModelProperty(value = "领料方式")
	private String pickingMethod;
	/** 领料方式名称 */
	@ApiModelProperty(value = "领料方式名称")
	private String pickingMethodName;
}
