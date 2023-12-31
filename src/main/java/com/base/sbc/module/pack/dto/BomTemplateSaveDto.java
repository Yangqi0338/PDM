package com.base.sbc.module.pack.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 用于模板新增
 */
@Data
public class BomTemplateSaveDto {

    @ApiModelProperty(value = "bom模板id"  )
    private String bomTemplateId;


    @ApiModelProperty(value = "版本id"  )
    private String    bomVersionId;


    @ApiModelProperty(value = "类型"  )
    private String type;

    @ApiModelProperty(value = "款式id"  )
    private String styleId;


    @ApiModelProperty(value = "BOM模板物料")
    private List<String>  materialList;

}
