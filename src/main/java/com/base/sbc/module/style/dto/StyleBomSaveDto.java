package com.base.sbc.module.style.dto;

import com.base.sbc.module.pack.dto.PackBomDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：款式设计物料清单保存Dto
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.style.dto.SampleDesignBomSaveDto
 * @email li_xianglin@126.com
 * @date 创建时间：2023-08-03 14:00
 */
@Data
@ApiModel("款式设计物料清单保存Dto SampleDesignBomSaveDto")
public class StyleBomSaveDto {

    @ApiModelProperty(value = "款式设计id")
    private String styleId;

    @ApiModelProperty(value = "保存类型:1覆盖,0或者空为不覆盖（追加）")
    private String overlayFlg;
    @ApiModelProperty(value = "物料信息")
    private List<PackBomDto> bomList;


}
