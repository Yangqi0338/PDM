package com.base.sbc.module.planning.dto;


import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：产品季总览-波段企划查询
 * @address com.base.sbc.module.planning.dto.ProductSeasonBandSearchDto
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-04-20 14:52
 * @version 1.0
 */
@Data
@ApiModel("产品季总览-分配设计师dto ProductSetDesignDto")
public class ProductSetDesignDto extends Page {

    @ApiModelProperty(value = "坑位id" ,required = true,example = "122222")
    @NotBlank(message = "坑位id")
    private String id;


}
