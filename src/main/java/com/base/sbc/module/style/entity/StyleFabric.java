/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：款式面料 实体类
 * @address com.base.sbc.module.style.entity.StyleFabric
 * @author your name
 * @email your email
 * @date 创建时间：2023-8-24 10:17:48
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_style_fabric")
@ApiModel("款式面料 StyleFabric")
public class StyleFabric extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 备注信息 */
    @ApiModelProperty(value = "备注信息"  )
    private String remarks;
    /** 面料企划明细id */
    @ApiModelProperty(value = "面料企划明细id"  )
    private String fabricPlanningItemId;
    /** 来源：1.新增，2.基础面料库、3.面料企划、4.其他 */
    @ApiModelProperty(value = "来源：1.新增，2.基础面料库、3.面料企划、4.其他"  )
    private String source;
    /** 基础面料id */
    @ApiModelProperty(value = "基础面料id"  )
    private String fabricLibraryId;
    /** 面料标签:1.新面料、2.长青面料、3.延续面料、4.库存面料； */
    @ApiModelProperty(value = "面料标签:1.新面料、2.长青面料、3.延续面料、4.库存面料；"  )
    private String fabricLabel;
    /** 款式id */
    @ApiModelProperty(value = "款式id"  )
    private String styleId;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
