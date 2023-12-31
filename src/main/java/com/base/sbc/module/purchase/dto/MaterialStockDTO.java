package com.base.sbc.module.purchase.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("物料库存列表 or 物料库存日志列表")
public class MaterialStockDTO extends Page {
    @ApiModelProperty(value = "仓库")
    private String warehouseId;
    @ApiModelProperty(value = "出入库类型(0 入库, 1 出库)")
    private String type;
    @ApiModelProperty(value = "物料库存id")
    private String materialWarehouseId;
    @ApiModelProperty(value = "供应商id")
    private String defaultSupplierId;
    @ApiModelProperty(value = "颜色")
    private String materialColor;
}
