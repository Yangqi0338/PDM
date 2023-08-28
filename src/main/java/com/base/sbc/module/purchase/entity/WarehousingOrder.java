/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.purchase.entity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 类描述：入库单 实体类
 * @address com.base.sbc.module.purchase.entity.WarehousingOrder
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-26 14:33:37
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_warehousing_order")
@ApiModel("入库单 WarehousingOrder")
public class WarehousingOrder extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
    /** 入库单明细 */
    @TableField(exist = false)
    private List<WarehousingOrderDetail> orderDetailList;

	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /** 单号入库 */
    @ApiModelProperty(value = "单号入库"  )
    private String code;
    /** 单据状态（0正常 1作废） */
    @ApiModelProperty(value = "单据状态（0正常 1作废）"  )
    private String orderStatus;
    /** 状态（0草稿 1待审核 2审核通过 -1驳回） */
    @ApiModelProperty(value = "状态（0草稿 1待审核 2审核通过 -1驳回）"  )
    private String status;
    /** 单据类型（0采购单入库 1手工） */
    @ApiModelProperty(value = "单据类型（0采购单入库 1手工）"  )
    private String orderType;
    /** 来源单据id */
    @ApiModelProperty(value = "来源单据id"  )
    private String sourceId;
    /** 来源单据编号 */
    @ApiModelProperty(value = "来源单据编号"  )
    private String sourceCode;
    /** 供应商id */
    @ApiModelProperty(value = "供应商id"  )
    private String supplierId;
    /** 供应商名称 */
    @ApiModelProperty(value = "供应商名称"  )
    private String supplierName;
    /** 供应商联系人 */
    @ApiModelProperty(value = "供应商联系人"  )
    private String supplierContacts;
    /** 入库仓库id */
    @ApiModelProperty(value = "入库仓库id"  )
    private String warehouseId;
    /** 入库仓库code */
    @ApiModelProperty(value = "入库仓库code"  )
    private String warehouseCode;
    /** 入库仓库名称 */
    @ApiModelProperty(value = "入库仓库名称"  )
    private String warehouseName;
    /** 仓库联系人 */
    @ApiModelProperty(value = "仓库联系人"  )
    private String warehouseContacts;
    /** 仓库联系人电话 */
    @ApiModelProperty(value = "仓库联系人电话"  )
    private String warehousePhone;
    /** 仓库地址 */
    @ApiModelProperty(value = "仓库地址"  )
    private String warehouseAddress;
    /** 送货数量 */
    @ApiModelProperty(value = "送货数量"  )
    private BigDecimal deliveryQuantity;
    /** 入库金额 */
    @ApiModelProperty(value = "入库金额"  )
    private BigDecimal money;
    /** 经办人id */
    @ApiModelProperty(value = "经办人id"  )
    private String agentId;
    /** 经办人名称 */
    @ApiModelProperty(value = "经办人名称"  )
    private String agentName;
    /** 入库时间 */
    @ApiModelProperty(value = "入库时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date warehousingDate;
    /** 驳回理由 */
    @ApiModelProperty(value = "驳回理由"  )
    private String rejectReason;
    /** 审核人id */
    @ApiModelProperty(value = "审核人id"  )
    private String reviewerId;
    /** 审核人 */
    @ApiModelProperty(value = "审核人"  )
    private String reviewerName;
    /** 审核时间 */
    @ApiModelProperty(value = "审核时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date reviewDate;
    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

