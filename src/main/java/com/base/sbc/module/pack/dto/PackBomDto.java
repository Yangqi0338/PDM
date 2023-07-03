package com.base.sbc.module.pack.dto;

import com.base.sbc.module.pack.entity.PackBom;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * 类描述：资料包-物料清单dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.vo.PackSizeVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-01 10:33
 */
@Data
@ApiModel("资料包-物料清单dto PackBomDto")
public class PackBomDto extends PackBom {

    @ApiModelProperty(value = "版本id")
    @NotBlank(message = "版本id不能为空")
    private String bomVersionId;

    @ApiModelProperty(value = "尺码信息")
    List<PackBomSizeDto> packBomSizeList;
}
