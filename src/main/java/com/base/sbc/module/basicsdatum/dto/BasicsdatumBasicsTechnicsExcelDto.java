/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：导入导出基础资料-基础工艺 dto类
 * @address com.base.sbc.module.basicsdatum.dto.BasicsdatumBasicsTechnics
 * @author mengfanjiang
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-19 19:15:00
 * @version 1.0
 */
@Data
@ApiModel("基础资料-基础工艺 BasicsdatumBasicsTechnics")
public class BasicsdatumBasicsTechnicsExcelDto  {
    @Excel(name = "id")
    private String id;
    /** 编码 */
    @ApiModelProperty(value = "编码"  )
    @Excel(name = "编码")
    private String coding;
    /** 工艺类型 */
    @ApiModelProperty(value = "工艺类型"  )
    @Excel(name = "工艺类型")
    private String technicsType;
    /** 工艺项目 */
    @ApiModelProperty(value = "工艺项目"  )
    @Excel(name = "工艺项目")
    private String technicsProject;
    /** 描述 */
    @ApiModelProperty(value = "描述"  )
    @Excel(name = "描述")
    private String description;
    /** 图片 */
    @ApiModelProperty(value = "图片"  )
    @Excel(name = "图片",type = 2)
    private String picture;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    @Excel(name  = "可用的" , replace = {"true_0", "false_1"} )
    private String status;
}
