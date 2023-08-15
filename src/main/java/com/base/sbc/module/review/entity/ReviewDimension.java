/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.review.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：评审会-评审维度配置 实体类
 * @address com.base.sbc.module.review.entity.ReviewDimension
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-14 15:40:34
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_review_dimension")
@ApiModel("评审会-评审维度配置 ReviewDimension")
public class ReviewDimension extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 启用 0 禁用 1 */
    @ApiModelProperty(value = "启用 0 禁用 1"  )
    private String status;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /** 部门id */
    @ApiModelProperty(value = "部门id"  )
    private String departmentId;
    /** 部门名称 */
    @ApiModelProperty(value = "部门名称"  )
    private String departmentName;
    /** 评审维度 */
    @ApiModelProperty(value = "评审维度"  )
    private String reviewDimension;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
