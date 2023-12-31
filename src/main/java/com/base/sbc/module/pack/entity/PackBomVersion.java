/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：资料包-物料清单-物料版本 实体类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.entity.PackBomVersion
 * @email your email
 * @date 创建时间：2023-7-13 20:34:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pack_bom_version")
@ApiModel("资料包-物料清单-物料版本 PackBomVersion")
public class PackBomVersion extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;
    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * 主数据id
     */
    @ApiModelProperty(value = "主数据id")
    private String foreignId;
    /**
     * 资料包类型:packDesign:设计资料包
     */
    @ApiModelProperty(value = "资料包类型:packDesign:设计资料包")
    private String packType;
    /**
     * 版本
     */
    @ApiModelProperty(value = "版本")
    private String version;
    /**
     * 状态:(1启用,0停用)
     */
    @ApiModelProperty(value = "状态:(1启用,0停用)")
    private String status;
    /**
     * 锁定状态(0正常,1锁定)
     */
    @ApiModelProperty(value = "锁定状态(0正常,1锁定)")
    private String lockFlag;
    /**
     * 发送状态(0未发送,1已发送)
     */
    @ApiModelProperty(value = "发送状态(0未发送,1已发送)")
    private String scmSendFlag;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;
    /**
     * 审核状态：待审核(1)、审核通过(2)、被驳回(-1)
     */
    @ApiModelProperty(value = "审核状态：待审核(1)、审核通过(2)、被驳回(-1)")
    private String confirmStatus;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

