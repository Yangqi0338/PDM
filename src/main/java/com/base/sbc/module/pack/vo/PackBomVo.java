package com.base.sbc.module.pack.vo;

import com.base.sbc.module.pack.entity.PackBom;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * 类描述：资料包-物料清单
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.vo.PackBomVersionVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-01 16:45
 */
@Data
@ApiModel("资料包-物料清单 PackBomVo")
public class PackBomVo extends PackBom {

    @ApiModelProperty(value = "尺码信息")
    private List<PackBomSizeVo> packBomSizeList;
}
