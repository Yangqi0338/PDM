/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.entity;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：基础资料-BOM模板物料档案 实体类
 * @address com.base.sbc.module.basicsdatum.entity.BasicsdatumBomTemplateMaterial
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-8-23 16:20:40
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_basicsdatum_bom_template_material")
@ApiModel("基础资料-BOM模板物料档案 BasicsdatumBomTemplateMaterial")
public class BasicsdatumBomTemplateMaterial extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** BOM模板id */
    @ApiModelProperty(value = "BOM模板id"  )
    private String bomTemplateId;
    /** 物料档案id */
    @ApiModelProperty(value = "物料档案id"  )
    private String materialId;
    /** 搭配名称 */
    @ApiModelProperty(value = "搭配名称"  )
    private String collocationCode;
    /** 搭配编码 */
    @ApiModelProperty(value = "搭配编码"  )
    private String collocationName;
    /** 主材料标识(0否,1是) */
    @ApiModelProperty(value = "主材料标识(0否,1是)"  )
    private String mainFlag;
    /** 物料图片 */
    @ApiModelProperty(value = "物料图片"  )
    private String imageUrl;
    /** 材料 */
    @ApiModelProperty(value = "材料"  )
    private String materialCodeName;
    /** 物料名称 */
    @ApiModelProperty(value = "物料名称"  )
    private String materialName;
    /** 物料编号 */
    @ApiModelProperty(value = "物料编号"  )
    private String materialCode;
    /** 供应商id */
    @ApiModelProperty(value = "供应商id"  )
    private String supplierId;
    /** 供应商名称 */
    @ApiModelProperty(value = "供应商名称"  )
    private String supplierName;
    /** 供应商厂家成分 */
    @ApiModelProperty(value = "供应商厂家成分"  )
    private String supplierFactoryIngredient;
    /** 成分 */
    @ApiModelProperty(value = "成分"  )
    private String ingredient;
    /** 供应商报价 */
    @ApiModelProperty(value = "供应商报价"  )
    private BigDecimal supplierPrice;
    /** 供应商物料号 */
    @ApiModelProperty(value = "供应商物料号"  )
    private String supplierMaterialCode;
    /** 单价 */
    @ApiModelProperty(value = "单价"  )
    private BigDecimal price;
    /** 部位编码 */
    @ApiModelProperty(value = "部位编码"  )
    private String partCode;
    /** 部位名称 */
    @ApiModelProperty(value = "部位名称"  )
    private String partName;
    /** 单位 */
    @ApiModelProperty(value = "单位"  )
    private String unitCode;
    /** 采购单位 */
    @ApiModelProperty(value = "采购单位"  )
    private String purchaseUnitCode;
    /** 采购单位名称 */
    @ApiModelProperty(value = "采购单位名称"  )
    private String purchaseUnitName;
    /** 库存单位 */
    @ApiModelProperty(value = "库存单位"  )
    private String stockUnitCode;
    /** 库存单位名称 */
    @ApiModelProperty(value = "库存单位名称"  )
    private String stockUnitName;
    /** 辅料材质 */
    @ApiModelProperty(value = "辅料材质"  )
    private String auxiliaryMaterial;
    /** 颜色名称 */
    @ApiModelProperty(value = "颜色名称"  )
    private String color;
    /** 颜色hex */
    @ApiModelProperty(value = "颜色hex"  )
    private String colorHex;
    /** 颜色代码 */
    @ApiModelProperty(value = "颜色代码"  )
    private String colorCode;
    /** 颜色图片 */
    @ApiModelProperty(value = "颜色图片"  )
    private String colorPic;
    /** 厂家有效门幅/规格 */
    @ApiModelProperty(value = "厂家有效门幅/规格"  )
    private String translate;
    /** 厂家有效门幅/规格编码 */
    @ApiModelProperty(value = "厂家有效门幅/规格编码"  )
    private String translateCode;
    /** 单件用量 */
    @ApiModelProperty(value = "单件用量"  )
    private BigDecimal unitUse;
    /** 损耗% */
    @ApiModelProperty(value = "损耗%"  )
    private BigDecimal lossRate;
    /** 成本 */
    @ApiModelProperty(value = "成本"  )
    private BigDecimal cost;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /** SCM下发状态:0未发送,1发送成功，2发送失败,3重新打开 */
    @ApiModelProperty(value = "SCM下发状态:0未发送,1发送成功，2发送失败,3重新打开"  )
    private String scmSendFlag;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /** 顺序 */
    @ApiModelProperty(value = "顺序"  )
    private Integer sort;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

