/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
/**
 * 类描述：资料包-物料清单 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.entity.PackBom
 * @email your email
 * @date 创建时间：2023-7-10 17:29:24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pack_bom")
@ApiModel("资料包-物料清单 PackBom")
public class PackBom extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 主数据id
     */
    @ApiModelProperty(value = "主数据id")
    private String foreignId;
    /**
     * 资料包类型:packDesign:设计资料包/packBigGoods:标准资料包(大货资料包)
     */
    @ApiModelProperty(value = "资料包类型:packDesign:设计资料包/packBigGoods:标准资料包(大货资料包)")
    private String packType;
    /**
     * 版本id
     */
    @ApiModelProperty(value = "版本id")
    private String bomVersionId;
    /**
     * 物料档案id
     */
    @ApiModelProperty(value = "物料档案id")
    private String materialId;
    /**
     * 搭配
     */
    @ApiModelProperty(value = "搭配")
    private String categoryName;
    /**
     * 材料
     */
    @ApiModelProperty(value = "材料")
    private String materialCodeName;
    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    /**
     * 物料编号
     */
    @ApiModelProperty(value = "物料编号")
    private String materialCode;
    /**
     * 厂家成分
     */
    @ApiModelProperty(value = "厂家成分")
    private String factoryComposition;
    /**
     * 成分
     */
    @ApiModelProperty(value = "成分")
    private String ingredient;
    /**
     * 部位
     */
    @ApiModelProperty(value = "部位")
    private String part;
    /**
     * 门幅/规格（通用）
     */
    @ApiModelProperty(value = "门幅/规格（通用）")
    private String widthUniversal;
    /**
     * 颜色名称
     */
    @ApiModelProperty(value = "颜色名称")
    private String color;
    /**
     * 颜色hex
     */
    @ApiModelProperty(value = "颜色hex")
    private String colorHex;
    /**
     * 颜色代码
     */
    @ApiModelProperty(value = "颜色代码")
    private String colorCode;
    /**
     * 颜色图片
     */
    @ApiModelProperty(value = "颜色图片")
    private String colorPic;
    /**
     * 物料图片
     */
    @ApiModelProperty(value = "物料图片")
    private String imageUrl;
    /**
     * 单件用量
     */
    @ApiModelProperty(value = "单件用量")
    private BigDecimal unitUse;
    /**
     * 损耗%
     */
    @ApiModelProperty(value = "损耗%")
    private BigDecimal lossRate;
    /**
     * 成本
     */
    @ApiModelProperty(value = "成本")
    private BigDecimal cost;
    /**
     * 供应商报价
     */
    @ApiModelProperty(value = "供应商报价")
    private BigDecimal supplierPrice;
    /**
     * 供应商物料号
     */
    @ApiModelProperty(value = "供应商物料号")
    private String supplierMaterialCode;
    /**
     * 状态(0停用,1启用)
     */
    @ApiModelProperty(value = "状态(0停用,1启用)")
    private String status;
    /**
     * 发送状态(0未发送,1已发送)
     */
    @ApiModelProperty(value = "发送状态(0未发送,1已发送)")
    private String sendFlag;
    /**
     * 不能使用(0否,1是)
     */
    @ApiModelProperty(value = "不能使用(0否,1是)")
    private String unusableFlag;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;
    /**
     * 供应商id
     */
    @ApiModelProperty(value = "供应商id")
    private String supplierId;
    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unitCode;
    /**
     * 额定单耗
     */
    @ApiModelProperty(value = "额定单耗")
    private String ratedUnitConsumption;
    /**
     * 购买货币
     */
    @ApiModelProperty(value = "购买货币")
    private String purchaseCurrency;
    /**
     * 单价
     */
    @ApiModelProperty(value = "单价")
    private BigDecimal price;
    /**
     * 报价货币
     */
    @ApiModelProperty(value = "报价货币")
    private String quotationPriceCurrency;
    /**
     * 上次单价
     */
    @ApiModelProperty(value = "上次单价")
    private BigDecimal lastTimePrice;
    /**
     * 上次报价币种
     */
    @ApiModelProperty(value = "上次报价币种")
    private String lastTimeCurrency;
    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contacts;
    /**
     * 联系人手机号
     */
    @ApiModelProperty(value = "联系人手机号")
    private String contactsPhone;
    /**
     * 联系人地址
     */
    @ApiModelProperty(value = "联系人地址")
    private String contactsAddress;
    /**
     * 工段分组编码
     */
    @ApiModelProperty(value = "工段分组编码")
    private String workshopGroupCode;
    /**
     * 工段分组
     */
    @ApiModelProperty(value = "工段分组")
    private String workshopGroup;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

