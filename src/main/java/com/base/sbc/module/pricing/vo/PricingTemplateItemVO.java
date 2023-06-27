/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.vo;

import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：核价模板明细表
 *
 * @author xhj
 * @version 1.0
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-16 15:09:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("核价模板明细表")
public class PricingTemplateItemVO extends BaseDataEntity<String> {

    private static final long serialVersionUID = -8147674504146230587L;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remarks;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 公式
     */
    @ApiModelProperty(value = "公式")
    private String expression;
    /**
     * 默认数值
     */
    @ApiModelProperty(value = "默认数值")
    private String defaultNum;
    /**
     * 小数位数
     */
    @ApiModelProperty(value = "小数位数")
    private String decimalPlaces;
    /**
     * 是否修改:0.否, 1.是
     */
    @ApiModelProperty(value = "是否修改:0.否, 1.是")
    private String updateFlag;
    /**
     * 是否显示:0.否, 1.是
     */
    @ApiModelProperty(value = "是否显示:0.否, 1.是")
    private String showFlag;
    /**
     * 排序行
     */
    @ApiModelProperty(value = "排序行")
    private Integer sort;
    /**
     * 描述说明
     */
    @ApiModelProperty(value = "描述说明")
    private String describeSay;
    /**
     * 模板id
     */
    @ApiModelProperty(value = "模板id")
    private String pricingTemplateId;
    /**
     * 公式中字段显示或隐藏
     */
    @ApiModelProperty(value = "公式中字段显示或隐藏")
    private String expressionIsShow;
    /**
     * 公式(逗号拼接,用于前端回显)
     */
    @ApiModelProperty(value = "公式(逗号拼接,用于前端回显)")
    private String expressionShow;

    /**
     * 计算结果
     */
    @ApiModelProperty(value = "计算结果")
    private Double countResult;

}
