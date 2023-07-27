package com.base.sbc.module.pricing.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.math.BigDecimal;

@Data
public class StylePricingSaveDTO {
    /**
     * 款式定价id
     */
    private String id;
    /**
     * 资料包id
     */
    @NotBlank(message = "资料包id不可为空")
    private String packId;
    /**
     * 企划倍率
     */
    private BigDecimal planningRate;

    /**
     * 是否计控确认 0.否、1.是
     */
    @ApiModelProperty(value = "是否计控确认 0.否、1.是")
    private String controlConfirm;
    /**
     * 是否商品吊牌确认 0.否、1.是
     */
    @ApiModelProperty(value = "是否商品吊牌确认 0.否、1.是")
    private String productHangtagConfirm;
    /**
     * 是否计控吊牌确认 0.否、1.是
     */
    @ApiModelProperty(value = "是否计控吊牌确认 0.否、1.是")
    private String controlHangtagConfirm;
}
