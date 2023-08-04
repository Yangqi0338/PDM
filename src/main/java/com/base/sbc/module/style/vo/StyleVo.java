package com.base.sbc.module.style.vo;


import com.base.sbc.config.common.annotation.UserAvatar;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.formType.vo.FieldManagementVo;
import com.base.sbc.module.sample.vo.MaterialVo;
import com.base.sbc.module.style.entity.Style;
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
@ApiModel("款式设计明细返回 SampleVo ")
public class StyleVo extends Style {

    @ApiModelProperty(value = "设计工艺员头像")
    @UserAvatar("technicianId")
    private String technicianAvatar;

    @ApiModelProperty(value = "材料专员头像")
    @UserAvatar("fabDevelopeId")
    private String fabDevelopeAvatar;
    @ApiModelProperty(value = "版师头像")
    @UserAvatar("patternDesignId")
    private String patternDesignAvatar;
    @ApiModelProperty(value = "跟款设计师头像")
    @UserAvatar("merchDesignId")
    private String merchDesignAvatar;
    @ApiModelProperty(value = "审版设计师头像")
    @UserAvatar("reviewedDesignId")
    private String reviewedDesignAvatar;
    @ApiModelProperty(value = "改版设计师头像")
    @UserAvatar("revisedDesignId")
    private String revisedDesignAvatar;
    @ApiModelProperty(value = "设计师头像")

    @UserAvatar("designerId")
    private String designerAvatar;
    @ApiModelProperty(value = "波段名称")
    private String bandName;
    @ApiModelProperty(value = "号型类型名称")
    private String sizeRangeName;

    @ApiModelProperty(value = "维度信息")
    private List<FieldManagementVo> dimensionLabels;

    @ApiModelProperty(value = "附件")
    private List<AttachmentVo> attachmentList;
    @ApiModelProperty(value = "款式图片信息")
    private List<AttachmentVo> stylePicList;
    @ApiModelProperty(value = "关联的素材库")
    private List<MaterialVo> materialList;

    @ApiModelProperty(value = "款式")
    public String getStyle() {
        return getDesignNo() + getStyleName();
    }
}
