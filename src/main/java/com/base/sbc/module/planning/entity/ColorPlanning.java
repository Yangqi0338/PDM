/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：颜色企划 实体类
 * @address com.base.sbc.module.planning.entity.ColorPlanning
 * @author your name
 * @email your email
 * @date 创建时间：2023-8-15 13:58:50
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_color_planning")
@ApiModel("颜色企划 ColorPlanning")
public class ColorPlanning extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 备注信息 */
    @ApiModelProperty(value = "备注信息"  )
    private String remarks;
    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;
    /** 颜色企划名称 */
    @ApiModelProperty(value = "颜色企划名称"  )
    private String colorPlanningName;
    /** 品牌编码 */
    @ApiModelProperty(value = "品牌编码"  )
    private String brandCode;
    /** 品牌 */
    @ApiModelProperty(value = "品牌"  )
    private String brand;
    /** 年份 */
    @ApiModelProperty(value = "年份"  )
    private String year;
    /** 年份编码 */
    @ApiModelProperty(value = "年份编码"  )
    private String yearCode;
    /** 季节 */
    @ApiModelProperty(value = "季节"  )
    private String season;
    /** 季节编码 */
    @ApiModelProperty(value = "季节编码"  )
    private String seasonCode;
    /**
     * 产品季id
     */
    @ApiModelProperty(value = "产品季id")
    private String planningSeasonId;
    /**
     * 产品季
     */
    @ApiModelProperty(value = "产品季")
    private String planningSeason;
    /** 审核状态：0：未提交，1：待审核，2：审核通过，-1：驳回） */
    @ApiModelProperty(value = "审核状态：0：未提交，1：待审核，2：审核通过，-1：驳回）"  )
    private String confirmStatus;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

