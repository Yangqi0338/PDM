/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：面料池明细 实体类
 * @address com.base.sbc.module.fabric.entity.FabricPoolItem
 * @author your name
 * @email your email
 * @date 创建时间：2023-8-23 11:02:45
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_fabric_pool_item")
@ApiModel("面料池明细 FabricPoolItem")
public class FabricPoolItem extends BaseDataEntity<String> {

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
    /** 来源：1.新增，2.基础面料库、3.面料开发申请、4.物料档案 */
    @ApiModelProperty(value = "来源：1.新增，2.基础面料库、3.面料开发申请、4.物料档案"  )
    private String source;
    /** 来源id */
    @ApiModelProperty(value = "来源id"  )
    private String sourceId;
    /** 面料标签:1.新面料、2.长青面料、3.延续面料、4.库存面料； */
    @ApiModelProperty(value = "面料标签:1.新面料、2.长青面料、3.延续面料、4.库存面料；"  )
    private String fabricLabel;
    /** 面料池id */
    @ApiModelProperty(value = "面料池id"  )
    private String fabricPoolId;
    /** 物料编码*/
    @ApiModelProperty(value = "物料编码"  )
    private String materialCode;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
