package com.base.sbc.module.planning.dto;


import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("颜色企划")
public class ThemePlanningSearchDTO extends Page {

    @ApiModelProperty(value = "年份编码")
    private String year;
    @ApiModelProperty(value = "品牌编码")
    private String brand;
    private String companyCode;
    @ApiModelProperty(value = "产品季id")
    private String planningSeasonId;


}
