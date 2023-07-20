package com.base.sbc.module.planning.dto;

import com.base.sbc.module.planning.entity.PlanningBand;
import com.base.sbc.module.planning.entity.PlanningCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

@Data
@ApiModel("商品企划-波段企划 PlanningBandDto")
public class PlanningBandDto extends PlanningBand {
    @ApiModelProperty(value = "编号", example = "689467740238381056")
    private String id;
    @ApiModelProperty(value = "产品季编号", example = "689467740238381051", required = true)
    @NotBlank(message = "企划id不能为空")
    private String planningSeasonId;
    /**
     * 波段名称
     */
    @ApiModelProperty(value = "波段企划名称", example = "23年冬常规产品1B企划")
    @NotBlank(message = "波段企划名称不能为空")
    private String name;
    /**
     * 性别
     */
    @ApiModelProperty(value = "性别" ,example = "男")
    @NotBlank(message = "性别不能为空")
    private String sex;
    /** 波段 */

    @ApiModelProperty(value = "月份" ,required = true,example = "11月")
    private String month;
    @ApiModelProperty(value = "波段" ,example = "1b")
    @NotBlank(message = "波段不能为空")
    private String bandCode;
    /** 生成模式 */
    @ApiModelProperty(value = "生产模式" ,example = "CMT")
    @NotBlank(message = "生产模式不能为空")
    private String devtType;
    /** 渠道 */
    @ApiModelProperty(value = "渠道" ,example = "线下")
    @NotBlank(message = "渠道不能为空")
    private String channel;

    @ApiModelProperty(value = "草稿(0)、待审核(1)、审核通过(2)、被驳回(-1)"  )
    private String status;
    @ApiModelProperty(value = "品类信息" )
    private List<PlanningCategory> categoryData;

}
