package com.base.sbc.module.purchase.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WarehouseingPageDTO extends Page {
    @ApiModelProperty(value = "单据状态（0正常 1作废）")
    private String orderStatus;

    @ApiModelProperty(value = "审核状态（0待审核 1审核通过 2驳回）")
    private String status;
}
