/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.formType.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类描述：字段值 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.formType.entity.FieldVal
 * @email your email
 * @date 创建时间：2023-9-11 10:36:12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_field_val")
@ApiModel("字段值 FieldVal")
public class FieldVal extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "删除标记（0：正常；1：删除；）")
    private String delFlag;

    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 关联id
     */
    @ApiModelProperty(value = "关联id")
    private String foreignId;
    /**
     * 数据分组
     */
    @ApiModelProperty(value = "数据分组")
    private String dataGroup;
    /**
     * 字段id
     */
    @ApiModelProperty(value = "字段id")
    private String fieldId;
    /**
     * 字段说明
     */
    @ApiModelProperty(value = "字段说明")
    private String fieldExplain;
    /**
     * 字段名称
     */
    @ApiModelProperty(value = "字段名称")
    private String fieldName;
    /**
     * 字段值
     */
    @ApiModelProperty(value = "字段值")
    private String val;
    /**
     * 字段值名称
     */
    @ApiModelProperty(value = "字段值名称")
    private String valName;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

