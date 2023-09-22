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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：采购-采购单-明细 实体类
 * @address com.base.sbc.module.purchase.entity.PurchaseOrderDetail
 * @author your name
 * @email your email
 * @date 创建时间：2023-9-22 10:25:55
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_purchase_order_detail")
@ApiModel("采购-采购单-明细 PurchaseOrderDetail")
public class PurchaseOrderDetail extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/

    /** 采购单编码 */
    @TableField(exist = false)
    private String code;

	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 采购单id */
    @ApiModelProperty(value = "采购单id"  )
    private String purchaseOrderId;
    /** 采购需求单id */
    @ApiModelProperty(value = "采购需求单id"  )
    private String demandId;
    /** 物料编码 */
    @ApiModelProperty(value = "物料编码"  )
    private String materialCode;
    /** 物料名称 */
    @ApiModelProperty(value = "物料名称"  )
    private String materialName;
    /** 设计款号 */
    @ApiModelProperty(value = "设计款号"  )
    private String designStyleCode;
    /** 制版号 */
    @ApiModelProperty(value = "制版号"  )
    private String plateBillCode;
    /** 物料规格 */
    @ApiModelProperty(value = "物料规格"  )
    private String materialSpecifications;
    /** 物料规格编码 */
    @ApiModelProperty(value = "物料规格编码"  )
    private String materialSpecificationsCode;
    /** 款式名称 */
    @ApiModelProperty(value = "款式名称"  )
    private String styleName;
    /** 经缩率 */
    @ApiModelProperty(value = "经缩率"  )
    private String warpShrinkage;
    /** 纬缩率 */
    @ApiModelProperty(value = "纬缩率"  )
    private String leftShrinkage;
    /** 纸筒 */
    @ApiModelProperty(value = "纸筒"  )
    private String paperTube;
    /** 空差 */
    @ApiModelProperty(value = "空差"  )
    private String spaceDifference;
    /** 供应商色号 */
    @ApiModelProperty(value = "供应商色号"  )
    private String supplierColor;
    /** 采购单位编码 */
    @ApiModelProperty(value = "采购单位编码"  )
    private String purchaseUnit;
    /** 采购单位名称 */
    @ApiModelProperty(value = "采购单位名称"  )
    private String purchaseUnitName;
    /** 采购转库存 */
    @ApiModelProperty(value = "采购转库存"  )
    private BigDecimal convertUnitRatio;
    /** 采购数量 */
    @ApiModelProperty(value = "采购数量"  )
    private BigDecimal purchaseNum;
    /** 物料颜色 */
    @ApiModelProperty(value = "物料颜色"  )
    private String materialColor;
    /** 物料颜色编码 */
    @ApiModelProperty(value = "物料颜色编码"  )
    private String materialColorCode;
    /** 成衣颜色 */
    @ApiModelProperty(value = "成衣颜色"  )
    private String productColor;
    /** 损耗 */
    @ApiModelProperty(value = "损耗"  )
    private BigDecimal loss;
    /** 单价 */
    @ApiModelProperty(value = "单价"  )
    private BigDecimal price;
    /** 总金额 */
    @ApiModelProperty(value = "总金额"  )
    private BigDecimal totalAmount;
    /** 物料交期 */
    @ApiModelProperty(value = "物料交期"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date deliveryDate;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /** 入库数量 */
    @ApiModelProperty(value = "入库数量"  )
    private BigDecimal warehouseNum;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

