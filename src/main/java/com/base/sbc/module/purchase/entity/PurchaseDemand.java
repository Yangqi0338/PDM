/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.purchase.entity;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.pack.entity.PackBom;
import com.base.sbc.module.pack.entity.PackInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：采购-采购需求表 实体类
 * @address com.base.sbc.module.purchase.entity.PurchaseDemand
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-4 16:41:44
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_purchase_demand")
@ApiModel("采购-采购需求表 PurchaseDemand")
public class PurchaseDemand extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
    /** 仓库id */
    @TableField(exist = false)
    private String warehouseId;
    /** 采购员id */
    @TableField(exist = false)
    private String purchaserId;
    /** 采购员 */
    @TableField(exist = false)
    private String purchaserName;
    /** 交货日期 */
    @TableField(exist = false)
    private Date deliveryDate;

    public PurchaseDemand(){

    }

    public PurchaseDemand(PackInfo packInfo, PackBom packBom, BasicsdatumMaterial material, String specifications, BigDecimal needNum){
        this.materialImage = material.getImageUrl();
        this.designStyleCode = packInfo.getDesignNo();
        this.plateBillCode = packInfo.getDesignNo() + packInfo.getStyleName();
        this.styleName = packInfo.getStyleName();
        this.category = packInfo.getProdCategoryName();
        this.materialCode = material.getMaterialCode();
        this.materialType = packBom.getCategoryName();
        this.materialName = packBom.getMaterialName();
        this.supplierCode = packBom.getSupplierId();
        this.supplierName = packBom.getSupplierName();
        this.supplierColor = material.getSupplierColorNo();
        this.component = packBom.getSupplierFactoryIngredient();
        this.materialColor = packBom.getColor();
        this.needNum = needNum;
        this.price = packBom.getPrice();
        this.unit = material.getPurchaseUnitCode();
        this.usePosition = packBom.getPartName();
        this.materialSpecifications = specifications;
        this.loss = packBom.getLossRate();
    }

	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 单据状态（0 正常 1作废） */
    @ApiModelProperty(value = "单据状态（0 正常 1作废）"  )
    private String orderStatus;
    /** 状态（0未审核 1审核通过 2驳回） */
    @ApiModelProperty(value = "状态（0未审核 1审核通过 2驳回）"  )
    private String status;
    /** 物料图片 */
    @ApiModelProperty(value = "物料图片"  )
    private String materialImage;
    /** 设计款号 */
    @ApiModelProperty(value = "设计款号"  )
    private String designStyleCode;
    /** 制版号 */
    @ApiModelProperty(value = "制版号"  )
    private String plateBillCode;
    /** 款式名称 */
    @ApiModelProperty(value = "款式名称"  )
    private String styleName;
    /** 品类 */
    @ApiModelProperty(value = "品类"  )
    private String category;
    /** 需求交期 */
    @ApiModelProperty(value = "需求交期"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date needDate;
    /** 物料编号 */
    @ApiModelProperty(value = "物料编号"  )
    private String materialCode;
    /** 物料分类 */
    @ApiModelProperty(value = "物料分类"  )
    private String materialType;
    /** 物料名称 */
    @ApiModelProperty(value = "物料名称"  )
    private String materialName;
    /** 供应商id */
    @ApiModelProperty(value = "供应商id"  )
    private String supplierId;
    /** 供应商编码 */
    @ApiModelProperty(value = "供应商编码"  )
    private String supplierCode;
    /** 供应商名称 */
    @ApiModelProperty(value = "供应商名称"  )
    private String supplierName;
    /** 供应商色号 */
    @ApiModelProperty(value = "供应商色号"  )
    private String supplierColor;
    /** 成分 */
    @ApiModelProperty(value = "成分"  )
    private String component;
    /** 物料颜色 */
    @ApiModelProperty(value = "物料颜色"  )
    private String materialColor;
    /** 需求数量 */
    @ApiModelProperty(value = "需求数量"  )
    private BigDecimal needNum;
    /** 单价 */
    @ApiModelProperty(value = "单价"  )
    private BigDecimal price;
    /** 单位 */
    @ApiModelProperty(value = "单位"  )
    private String unit;
    /** 使用部位 */
    @ApiModelProperty(value = "使用部位"  )
    private String usePosition;
    /** 物料规格 */
    @ApiModelProperty(value = "物料规格"  )
    private String materialSpecifications;
    /** 损耗 */
    @ApiModelProperty(value = "损耗"  )
    private BigDecimal loss;
    /** 已采购数量 */
    @ApiModelProperty(value = "已采购数量"  )
    private BigDecimal purchasedNum;
    /** 驳回理由 */
    @ApiModelProperty(value = "驳回理由"  )
    private String rejectReason;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

