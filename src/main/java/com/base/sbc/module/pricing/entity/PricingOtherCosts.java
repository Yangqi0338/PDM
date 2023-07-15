/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 类描述：其他费用 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pricing.entity.PricingOtherCosts
 * @email your email
 * @date 创建时间：2023-7-15 10:22:06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pricing_other_costs")
@ApiModel("其他费用 PricingOtherCosts")
public class PricingOtherCosts extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;
    /**
     * 报价单编码
     */
    @ApiModelProperty(value = "报价单编码")
    private String pricingCode;
    /**
     * 费用类型id
     */
    @ApiModelProperty(value = "费用类型id")
    private String costsTypeId;
    /**
     * 费用类型
     */
    @ApiModelProperty(value = "费用类型")
    private String costsType;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 价格
     */
    @ApiModelProperty(value = "价格")
    private BigDecimal price;
    /**
     * 货币
     */
    @ApiModelProperty(value = "货币")
    private String currency;
    /**
     * 货币编码
     */
    @ApiModelProperty(value = "货币编码")
    private String currencyCode;
    /**
     * 报价货币编码
     */
    @ApiModelProperty(value = "报价货币编码")
    private String quotationPriceCurrencyCode;
    /**
     * 报价货币
     */
    @ApiModelProperty(value = "报价货币")
    private String quotationPriceCurrency;
    /**
     * 报价单价
     */
    @ApiModelProperty(value = "报价单价")
    private BigDecimal quotationPrice;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

