package com.base.sbc.module.pack.dto;

import com.base.sbc.module.pack.entity.PackPricingOtherCosts;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：资料包-核价信息-其他费用DTO
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.dto.PackPricingOtherCostsDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-10 14:06
 */
@Data
@ApiModel("资料包-核价信息-其他费用 PackPricingOtherCosts Dto")
public class PackPricingOtherCostsDto extends PackPricingOtherCosts {
    @ApiModelProperty(value = "主数据id")
    @NotBlank(message = "主数据id为空")
    private String foreignId;

    @ApiModelProperty(value = "资料包类型")
    @NotBlank(message = "资料包类型为空")
    private String packType;

    @ApiModelProperty(value = "类型:包装费/检测费/外协加工费/毛纱加工费/车缝加工费")
    @NotBlank(message = "类型不能为空:包装费/检测费/外协加工费/毛纱加工费/车缝加工费")
    private String costsItem;
}
