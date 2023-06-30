/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.process.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：流程配置-状态记录 实体类
 * @address com.base.sbc.module.process.entity.ProcessStateRecord
 * @author mengfanjiang
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-6 15:03:27
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_process_state_record")
@ApiModel("流程配置-状态记录 ProcessStateRecord")
public class ProcessStateRecord extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/


	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 业务实例id */
    @ApiModelProperty(value = "业务实例id"  )
    private String businessInstanceId;
    /** 节点记录id */
    @ApiModelProperty(value = "节点记录id"  )
    private String nodeRecordId;
    /** 原先状态 */
    @ApiModelProperty(value = "状态"  )
    private String status;

    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}
