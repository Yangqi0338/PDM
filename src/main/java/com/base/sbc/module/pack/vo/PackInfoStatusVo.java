package com.base.sbc.module.pack.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：资料包
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.vo.PackInfoStatusVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-07-15 15:34
 */
@Data
@ApiModel("资料包-状态 PackInfoStatusVo")
public class PackInfoStatusVo {


    /**
     * 状态:1启用,0未启用
     */
    @ApiModelProperty(value = "状态:1启用,0未启用")
    private String enableFlag;
    /**
     * SCM下发状态:0未下发,1已下发
     */
    @ApiModelProperty(value = "SCM下发状态:0未下发,1已下发")
    private String scmSendFlag;
    /**
     * bom状态:(0样品,1大货)
     */
    @ApiModelProperty(value = "bom状态:(0样品,1大货)")
    private String bomStatus;
    /**
     * 审核状态：待审核(1)、审核通过(2)、被驳回(-1)
     */
    @ApiModelProperty(value = "审核状态：待审核(1)、审核通过(2)、被驳回(-1)")
    private String confirmStatus;
    /**
     * 反审状态：待审核(1)、审核通过(2)、被驳回(-1)
     */
    @ApiModelProperty(value = "反审状态：待审核(1)、审核通过(2)、被驳回(-1)")
    private String reverseConfirmStatus;
    /**
     * 设计转后技术确认:(0未确认,1已确认)
     */
    @ApiModelProperty(value = "设计转后技术确认:(0未确认,1已确认)")
    private String designTechConfirm;
    /**
     * 设计转后技术确认时间
     */
    @ApiModelProperty(value = "设计转后技术确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date designTechConfirmDate;
    /**
     * 大货制单员确认:(0未确认,1已确认)
     */
    @ApiModelProperty(value = "大货制单员确认:(0未确认,1已确认)")
    private String bulkOrderClerkConfirm;
    /**
     * 大货制单员确认时间
     */
    @ApiModelProperty(value = "大货制单员确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date bulkOrderClerkConfirmDate;
    /**
     * 大货工艺员确认:(0未确认,1已确认)
     */
    @ApiModelProperty(value = "大货工艺员确认:(0未确认,1已确认)")
    private String bulkProdTechConfirm;
    /**
     * 大货工艺员确认时间
     */
    @ApiModelProperty(value = "大货工艺员确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date bulkProdTechConfirmDate;
    /**
     * 后技术确认:(0未确认,1已确认)
     */
    @ApiModelProperty(value = "后技术确认:(0未确认,1已确认)")
    private String postTechConfirm;
    /**
     * 后技术确认时间
     */
    @ApiModelProperty(value = "后技术确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date postTechConfirmDate;
    /**
     * 尺寸表锁定flg:(0未锁定,1锁定)
     */
    @ApiModelProperty(value = "尺寸表锁定flg:(0未锁定,1锁定)")
    private String sizeLockFlag;
    /**
     * 尺寸表审批意见
     */
    @ApiModelProperty(value = "尺寸表审批意见")
    private String sizeConfirmSay;
    /**
     * 尺寸表审核状态:待审核(1)、审核通过(2)、被驳回(-1)
     */
    @ApiModelProperty(value = "尺寸表审核状态:待审核(1)、审核通过(2)、被驳回(-1)")
    private String sizeConfirmStatus;
    /**
     * 工艺说明锁定flg:(0未锁定,1锁定)
     */
    @ApiModelProperty(value = "工艺说明锁定flg:(0未锁定,1锁定)")
    private String techSpecLockFlag;
    /**
     * 工艺说明审批意见
     */
    @ApiModelProperty(value = "工艺说明审批意见")
    private String techSpecConfirmSay;
    /**
     * 工艺说明审批状态:待审核(1)、审核通过(2)、被驳回(-1)
     */
    @ApiModelProperty(value = "工艺说明审批状态:待审核(1)、审核通过(2)、被驳回(-1)")
    private String techSpecConfirmStatus;
    /**
     * 尺寸表洗后尺寸跳码:(0关闭,1开启)
     */
    @ApiModelProperty(value = "尺寸表洗后尺寸跳码:(0关闭,1开启)")
    private String washSkippingFlag;

    @ApiModelProperty(value = "工艺说明文件id")
    private String techSpecFileId;
}
