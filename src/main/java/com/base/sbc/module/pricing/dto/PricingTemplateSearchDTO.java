package com.base.sbc.module.pricing.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 核价模板列表查询
 * @Author xhj
 * @Date 2023/6/16 15:16
 */
@Data
@ApiModel("核价模板列表查询")
public class PricingTemplateSearchDTO extends Page {
    @ApiModelProperty(value = "模板编码", example = "123")
    private String templateCode;
    @ApiModelProperty(value = "模板名称", example = "123")
    private String templateName;

}
