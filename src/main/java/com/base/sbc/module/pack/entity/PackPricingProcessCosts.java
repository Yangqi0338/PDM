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
 * 类描述：资料包-加工费 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.entity.PackPricingProcessCosts
 * @email your email
 * @date 创建时间：2023-7-13 20:34:57
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pack_pricing_process_costs")
@ApiModel("资料包-加工费 PackPricingProcessCosts")
public class PackPricingProcessCosts extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】******
     * *****************************/
    /**
     * 主数据id
     */
    @ApiModelProperty(value = "主数据id")
    private String foreignId;
    /**
     * 资料包类型
     */
    @ApiModelProperty(value = "资料包类型")
    private String packType;
    /**
     * 序号
     */
    @ApiModelProperty(value = "序号")
    private String sort;
    /**
     * 部位
     */
    @ApiModelProperty(value = "部位")
    private String part;
    /**
     * 工序编号
     */
    @ApiModelProperty(value = "工序编号")
    private String processSort;
    /**
     * 工序名称
     */
    @ApiModelProperty(value = "工序名称")
    private String processName;
    /**
     * 工序等级
     */
    @ApiModelProperty(value = "工序等级")
    private String processLevel;
    /**
     * 末节点
     */
    @ApiModelProperty(value = "末节点")
    private String finallyNode;
    /**
     * GST工价
     */
    @ApiModelProperty(value = "GST工价")
    private BigDecimal gstProcessPrice;
    /**
     * 倍率
     */
    @ApiModelProperty(value = "倍率")
    private BigDecimal magnification;
    /**
     * 倍数
     */
    @ApiModelProperty(value = "倍数")
    private BigDecimal multiple;
    /**
     * IE工价
     */
    @ApiModelProperty(value = "IE工价")
    private BigDecimal ieProcessPrice;
    /**
     * 标准工时(秒)
     */
    @ApiModelProperty(value = "标准工时(秒)")
    private BigDecimal processDate;
    /**
     * 最高标准工价
     */
    @ApiModelProperty(value = "最高标准工价")
    private BigDecimal maxProcePrice;
    /**
     * 货币
     */
    @ApiModelProperty(value = "货币")
    private String currency;
    /**
     * 报价单价
     */
    @ApiModelProperty(value = "报价单价")
    private BigDecimal quotationPrice;
    /**
     * 报价货币
     */
    @ApiModelProperty(value = "报价货币")
    private String quotationPriceCurrency;
    /**
     * 标准工价
     */
    @ApiModelProperty(value = "标准工价")
    private BigDecimal processPrice;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;

    /**
     * 来源：0.新增、1.工序工价
     */
    @ApiModelProperty(value = "来源：0.新增、1.工序工价")
    private String sourceType;
    /**
     * 来源id
     */
    @ApiModelProperty(value = "来源id")
    private String sourceId;
    /**
     * 颜色编码
     */
    @ApiModelProperty(value = "颜色编码")
    private String colorCode;
    /**
     * 颜色名称
     */
    @ApiModelProperty(value = "颜色名称")
    private String colorName;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

