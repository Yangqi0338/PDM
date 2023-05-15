package com.base.sbc.module.sample.vo;


import com.base.sbc.module.common.entity.Attachment;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.sample.entity.Sample;
import com.base.sbc.module.sample.entity.Technology;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：样衣明细
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.vo.SampleVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-05-11 11:48
 */
@Data
@ApiModel("样衣明细返回 SampleVo ")
public class SampleVo extends Sample {
    @ApiModelProperty(value = "工艺信息")
    private Technology technology;

    @ApiModelProperty(value = "附件")
    private List<AttachmentVo> attachmentList;

    @ApiModelProperty(value = "关联的素材库")
    private List<MaterialVo> materialList;
}
