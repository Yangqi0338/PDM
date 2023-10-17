package com.base.sbc.module.planning.vo;

import com.base.sbc.module.formType.vo.FieldManagementVo;
import com.base.sbc.module.formType.vo.FieldOptionConfigVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PlanningDemandVo {

    private String id;
    /** 品类id */
    @ApiModelProperty(value = "品类id"  )
    private String categoryId;
    /** 需求名称 */
    @ApiModelProperty(value = "需求名称"  )
    private String demandName;
    /** 状态(0需求,1维度标签) */
    @ApiModelProperty(value = "状态(0需求,1维度标签)"  )
    private String type;
    /** 渠道 */
    @ApiModelProperty(value = "渠道"  )
    private String channel;
    /** 渠道名称 */
    @ApiModelProperty(value = "渠道名称"  )
    private String channelName;

    private String formTypeId;
    /**
     * 字段id
     */
    @ApiModelProperty(value = "字段id")
    private String fieldId;

    List<PlanningDemandProportionDataVo> list;

    FieldManagementVo fieldManagementVo;

    List<FieldOptionConfigVo> configVoList;

    @ApiModelProperty(value = "重点维度(0:否,1是)")
    private String importantFlag;

}
