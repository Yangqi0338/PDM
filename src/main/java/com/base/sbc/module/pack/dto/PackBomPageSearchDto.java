package com.base.sbc.module.pack.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述：bom 分页查询条件
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.dto.PackBomPageSearchDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-03 10:12
 */
@Data
@ApiModel("资料包-物料清单分页筛选条件 PackCommonSearchDto")
public class PackBomPageSearchDto extends PackCommonPageSearchDto {
    @ApiModelProperty(value = "版本id")
    @NotBlank(message = "版本id为空")
    private String bomVersionId;
    @ApiModelProperty(value = "颜色编码")
    private String styleColorCode;
}
