package com.base.sbc.module.basicsdatum.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;


@Data
@ApiModel("启用停止 StartStopDto")
public class StartStopDto {

    @ApiModelProperty(name = "编号", value = "多个编号加,",  required = true, dataType = "String", example = "11,22")
    @NotNull(message = "编号必填")
    private String ids;

    @ApiModelProperty(value = "状态",  required = true, example = "0")
    @NotBlank(message = "状态必填")
    private String status;

}