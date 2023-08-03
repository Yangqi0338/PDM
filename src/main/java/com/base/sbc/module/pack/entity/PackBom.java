/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.module.smp.dto.SmpBomDto;
import com.base.sbc.module.smp.entity.BomMaterial;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 类描述：资料包-物料清单 实体类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.entity.PackBom
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-8-3 17:16:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_pack_bom")
@ApiModel("资料包-物料清单 PackBom")
public class PackBom extends BaseDataEntity<String> {

    private static final long serialVersionUID = 1L;

    /**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
    public SmpBomDto toSmpBomDto() {
        SmpBomDto smpBomDto = new SmpBomDto();
        smpBomDto.setColorName(color);
        smpBomDto.setColorCode(colorCode);
        smpBomDto.setMainMaterial("1".equals(mainFlag));
        smpBomDto.setMaterialCode(materialCode);
        smpBomDto.setMaterialName(materialName);
        smpBomDto.setMaterialUnit(unitCode);
        smpBomDto.setPlaceOfUse(part);
        smpBomDto.setLossRate(lossRate);
        smpBomDto.setSupplierMaterialCode(supplierMaterialCode);
        smpBomDto.setQuotationSupplierCode(supplierId);
        smpBomDto.setCollocation(collocationName);
        smpBomDto.setBomLineItemId(id);
        smpBomDto.setId(id);
        IdGen idGen = new IdGen();
        smpBomDto.setSyncId(String.valueOf(idGen.nextId()));
        smpBomDto.setActive("0".equals(status));
        smpBomDto.setCreator(getCreateName());
        smpBomDto.setCreateTime(getCreateDate());
        smpBomDto.setModifiedPerson(getUpdateName());
        smpBomDto.setModifiedTime(getUpdateDate());
        return smpBomDto;
    }


    public BomMaterial toBomMaterial() {
        BomMaterial bomMaterial = new BomMaterial();
        bomMaterial.setCode(materialCode);
        bomMaterial.setMatColorCode(colorCode);
        bomMaterial.setMaterialUom(unitCode);
        bomMaterial.setPosition(part);
        bomMaterial.setCostRate(lossRate);
        bomMaterial.setActive("0".equals(status));
        bomMaterial.setPlacementName(collocationName);
        return bomMaterial;
    }

    /**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性) 【start】***********************************/
    /**
     * code(行id)
     */
    @ApiModelProperty(value = "code(行id)")
    private String code;
    /**
     * 主数据id
     */
    @ApiModelProperty(value = "主数据id")
    private String foreignId;
    /**
     * 资料包类型:packDesign:设计资料包/packBigGoods:标准资料包(大货资料包)
     */
    @ApiModelProperty(value = "资料包类型:packDesign:设计资料包/packBigGoods:标准资料包(大货资料包)")
    private String packType;
    /**
     * 状态(0停用,1启用)
     */
    @ApiModelProperty(value = "状态(0停用,1启用)")
    private String status;
    /**
     * 主材料标识(0否,1是)
     */
    @ApiModelProperty(value = "主材料标识(0否,1是)")
    private String mainFlag;
    /**
     * 版本id
     */
    @ApiModelProperty(value = "版本id")
    private String bomVersionId;
    /**
     * 物料档案id
     */
    @ApiModelProperty(value = "物料档案id")
    private String materialId;
    /**
     * 物料类别
     */
    @ApiModelProperty(value = "物料类别")
    private String categoryName;
    /**
     * 材料
     */
    @ApiModelProperty(value = "材料")
    private String materialCodeName;
    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    /**
     * 物料编号
     */
    @ApiModelProperty(value = "物料编号")
    private String materialCode;
    /**
     * 成分
     */
    @ApiModelProperty(value = "成分")
    private String ingredient;
    /**
     * 部位
     */
    @ApiModelProperty(value = "部位")
    private String part;
    /**
     * 厂家有效门幅/规格
     */
    @ApiModelProperty(value = "厂家有效门幅/规格")
    private String translate;
    /**
     * 厂家有效门幅/规格编码
     */
    @ApiModelProperty(value = "厂家有效门幅/规格编码")
    private String translateCode;
    /**
     * 颜色名称
     */
    @ApiModelProperty(value = "颜色名称")
    private String color;
    /**
     * 颜色hex
     */
    @ApiModelProperty(value = "颜色hex")
    private String colorHex;
    /**
     * 颜色代码
     */
    @ApiModelProperty(value = "颜色代码")
    private String colorCode;
    /**
     * 颜色图片
     */
    @ApiModelProperty(value = "颜色图片")
    private String colorPic;
    /**
     * 物料图片
     */
    @ApiModelProperty(value = "物料图片")
    private String imageUrl;
    /**
     * 单件用量
     */
    @ApiModelProperty(value = "单件用量")
    private BigDecimal unitUse;
    /**
     * 损耗%
     */
    @ApiModelProperty(value = "损耗%")
    private BigDecimal lossRate;
    /**
     * 成本
     */
    @ApiModelProperty(value = "成本")
    private BigDecimal cost;
    /**
     * 供应商报价
     */
    @ApiModelProperty(value = "供应商报价")
    private BigDecimal supplierPrice;
    /**
     * 供应商物料号
     */
    @ApiModelProperty(value = "供应商物料号")
    private String supplierMaterialCode;
    /**
     * 不能使用(0否,1是)
     */
    @ApiModelProperty(value = "不能使用(0否,1是)")
    private String unusableFlag;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;
    /**
     * 供应商id
     */
    @ApiModelProperty(value = "供应商id")
    private String supplierId;
    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    /**
     * 供应商厂家成分
     */
    @ApiModelProperty(value = "供应商厂家成分")
    private String supplierFactoryIngredient;
    /**
     * 辅料材质
     */
    @ApiModelProperty(value = "辅料材质")
    private String auxiliaryMaterial;
    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unitCode;
    /**
     * 额定单耗
     */
    @ApiModelProperty(value = "额定单耗")
    private String ratedUnitConsumption;
    /**
     * 购买币种
     */
    @ApiModelProperty(value = "购买币种")
    private String purchaseCurrency;
    /**
     * 单价
     */
    @ApiModelProperty(value = "单价")
    private BigDecimal price;
    /**
     * 报价货币
     */
    @ApiModelProperty(value = "报价货币")
    private String quotationPriceCurrency;
    /**
     * 上次单价
     */
    @ApiModelProperty(value = "上次单价")
    private BigDecimal lastTimePrice;
    /**
     * 上次报价币种
     */
    @ApiModelProperty(value = "上次报价币种")
    private String lastTimeCurrency;
    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contacts;
    /**
     * 联系人手机号
     */
    @ApiModelProperty(value = "联系人手机号")
    private String contactsPhone;
    /**
     * 联系人地址
     */
    @ApiModelProperty(value = "联系人地址")
    private String contactsAddress;
    /**
     * 工段分组编码
     */
    @ApiModelProperty(value = "工段分组编码")
    private String workshopGroupCode;
    /**
     * 工段分组
     */
    @ApiModelProperty(value = "工段分组")
    private String workshopGroup;
    /**
     * 设计单件用量
     */
    @ApiModelProperty(value = "设计单件用量")
    private BigDecimal designUnitUse;
    /**
     * 大货单件用量
     */
    @ApiModelProperty(value = "大货单件用量")
    private BigDecimal bulkUnitUse;
    /**
     * 设计单价
     */
    @ApiModelProperty(value = "设计单价")
    private BigDecimal designPrice;
    /**
     * 大货单价
     */
    @ApiModelProperty(value = "大货单价")
    private BigDecimal bulkPrice;
    /**
     * 单价税点
     */
    @ApiModelProperty(value = "单价税点")
    private BigDecimal priceTax;
    /**
     * 总成本税点
     */
    @ApiModelProperty(value = "总成本税点")
    private BigDecimal totalCostTax;
    /**
     * 金额
     */
    @ApiModelProperty(value = "金额")
    private BigDecimal amount;
    /**
     * 搭配名称
     */
    @ApiModelProperty(value = "搭配名称")
    private String collocationCode;
    /**
     * 搭配编码
     */
    @ApiModelProperty(value = "搭配编码")
    private String collocationName;
    /**
     * SCM下发状态:0未下发,1已下发
     */
    @ApiModelProperty(value = "SCM下发状态:0未下发,1已下发")
    private String scmSendFlag;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;
    /**
     * 采购单位
     */
    @ApiModelProperty(value = "采购单位")
    private String purchaseUnitCode;
    /**
     * 采购单位名称
     */
    @ApiModelProperty(value = "采购单位名称")
    private String purchaseUnitName;
    /**
     * 库存单位
     */
    @ApiModelProperty(value = "库存单位")
    private String stockUnitCode;
    /**
     * 库存单位名称
     */
    @ApiModelProperty(value = "库存单位名称")
    private String stockUnitName;
    /*****************************数据库字段区 不包含父类公共字段(属性) 【end】 ***********************************/
}

