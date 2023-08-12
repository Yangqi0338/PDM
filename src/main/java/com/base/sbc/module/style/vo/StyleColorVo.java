/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.base.sbc.config.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 类描述：样衣-款式配色 Vo类
 * @address com.base.sbc.module.sample.vo.SampleStyleColor
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-6-28 15:02:46
 * @version 1.0
 */
@Data

@ApiModel("样衣-款式配色 SampleStyleColor")
public class StyleColorVo {

    private String id;

    @ApiModelProperty(value = "行id")
    private String issuerId;

    @ApiModelProperty(value = "bomId")
    private String packInfoId;

    private String styleId;
    /**
     * 款式图
     */
    @ApiModelProperty(value = "款式图")
    private String  stylePic;

    @ApiModelProperty(value = "款式图")
    private String  style;

    public String getStyle() {
        return designNo+ (StringUtils.isNotBlank(styleName)? "-"+styleName:"");
    }

    /*
        设计款号
        */
    @ApiModelProperty(value = "设计款号"  )
    private String   designNo;

    /*
     大类编码
     */
    @ApiModelProperty(value = "大类编码"  )
    private String  prodCategory1st;

    /*
    大类
    */
    @ApiModelProperty(value = "大类"  )
    private String  prodCategory1stName;

    /**
     * 品类编码
     */
    @ApiModelProperty(value = "品类编码")
    private String  prodCategory;
    /**
     * 品类
     */
    @ApiModelProperty(value = "品类")
    private String  prodCategoryName;


    /**
     * 历史款
     */
    @ApiModelProperty(value = "历史款")
    private String  hisDesignNo;

    @ApiModelProperty(value = "款式名称")
    private String styleName;

    /**
     * 样衣图(主图)
     */
    @ApiModelProperty(value = "样衣图(主图)")
    private String styleColorPic;

    /** 款式(大货款号)  */
    @ApiModelProperty(value = "款式(大货款号) "  )
    private String styleNo;

    /** 原大货款号  */
    @ApiModelProperty(value = "原大货款号"  )
    private String hisStyleNo;

    /*配色*/
    @ApiModelProperty(value = "配色"  )
    private String  colorName;

    /** 颜色规格 */
    @ApiModelProperty(value = "颜色规格"  )
    private String colorSpecification;

    /*颜色库id*/
    @ApiModelProperty(value = "颜色库id"  )
    private String colourLibraryId;

    /*颜色库编码*/
    @ApiModelProperty(value = "颜色库编码"  )
    private String colorCode;

    /*BOM阶段*/
    @ApiModelProperty(value = "BOM阶段"  )
    private String bomStatus;

    /*紧急程度*/
    @ApiModelProperty(value = "紧急程度"  )
    private String taskLevel;

    /*紧急程度名称*/
    @ApiModelProperty(value = "紧急程度名称"  )
    private String taskLevelName;

    /*款式类型*/
    @ApiModelProperty(value = "款式类型"  )
    private String styleType;

    /*款式类型名称*/
    @ApiModelProperty(value = "款式类型名称"  )
    private String styleTypeName;

    /** 生产类型 */
    @ApiModelProperty(value = "生产类型"  )
    private String devtType;

    /** 生产类型 */
    @ApiModelProperty(value = "生产类型"  )
    private String devtTypeName;

    /** 号型类型 */
    @ApiModelProperty(value = "号型类型"  )
    private String sizeRange;

    /** 号型类型名称 */
    @ApiModelProperty(value = "号型类型名称"  )
    private String sizeRangeName;

    /** 波段 */
    @ApiModelProperty(value = "波段"  )
    private String bandCode;

    /** 波段 */
    @ApiModelProperty(value = "波段"  )
    private String bandName;

    /** 设计师id */
    @ApiModelProperty(value = "设计师i"  )
    private String designerId;

    /** 设计师 */
    @ApiModelProperty(value = "设计师i"  )
    private String designer;

    /**工艺园 */
    @ApiModelProperty(value = "工艺园"  )
    private String technicianId;


    /** 工艺园*/
    @ApiModelProperty(value = "工艺园"  )
    private String technicianName;

    /** 轻奢款(0否,1:是) */
    @ApiModelProperty(value = "轻奢款(0否,1:是)"  )
    private String isLuxury;

    /** 状态(0正常,1停用) */
    @ApiModelProperty(value = "状态(0正常,1停用)"  )
    private String status;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    /** 创建人 */
    @ApiModelProperty(value = "创建人"  )
    private String createName;

    /*是否上会*/
    @ApiModelProperty(value = "上会"  )
    private String meetFlag;

    /** 细分 */
    @ApiModelProperty(value = "细分"  )
    private String subdivide;
    /*细分名称*/
    @ApiModelProperty(value = "细分名称"  )
    private String subdivideName;

    /** 产品细分 */
    @ApiModelProperty(value = "产品细分"  )
    private String productSubdivide;
    /** 产品细分 */
    @ApiModelProperty(value = "产品细分"  )
    private String productSubdivideName;

    /** bom */
    @ApiModelProperty(value = "bom"  )
    private String bom;
    /** 销售类型 */
    @ApiModelProperty(value = "销售类型"  )
    private String salesType;

    /** 销售类型名称 */
    @ApiModelProperty(value = "销售类型名称"  )
    private String  salesTypeName;

    /** SCM下发状态:0未发送,1发送成功，2发送失败,3重新打开 */
    @ApiModelProperty(value = "SCM下发状态:0未发送,1发送成功，2发送失败,3重新打开"  )
    private String  scmSendFlag;

    /** 是否配饰款 */
    @ApiModelProperty(value = "是否配饰款"  )
    private String isTrim;

    /** 主款 */
    @ApiModelProperty(value = "主款"  )
    private String principalStyle;

    /** 主款款号 */
    @ApiModelProperty(value = "主款款号"  )
    private String principalStyleNo;

    /** 配饰 */
    @ApiModelProperty(value = "配饰"  )
    private String accessory;

    /** 配饰款号 */
    @ApiModelProperty(value = "配饰款号"  )
    private String accessoryNo;

    /** 吊牌价 */
    @ApiModelProperty(value = "吊牌价"  )
    private String tagPrice;

    /** 产品风格 */
    @ApiModelProperty(value = "产品风格"  )
    private String  styleFlavourName;

    /** 设计下明细单 */
    @ApiModelProperty(value = "设计下明细单"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date designDetailDate;
    /** 设计下正确样 */
    @ApiModelProperty(value = "设计下正确样"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date designCorrectDate;

    /** 供应商 */
    @ApiModelProperty(value = "供应商"  )
    private String supplier;

    /** 供应商编码 */
    @ApiModelProperty(value = "供应商编码"  )
    private String supplierCode;

    /** 供应商简称 */
    @ApiModelProperty(value = "供应商简称"  )
    private String supplierAbbreviation;

    /** 供应商款号 */
    @ApiModelProperty(value = "供应商款号"  )
    private String supplierNo;

    /** 供应商颜色 */
    @ApiModelProperty(value = "供应商颜色"  )
    private String supplierColor;

    /** 次品编号 */
    @ApiModelProperty(value = "次品编号"  )
    private String defectiveNo;

    /** 次品名称 */
    @ApiModelProperty(value = "次品名称"  )
    private String defectiveName;

    /** 是否主推(0否,1:是) */
    @ApiModelProperty(value = "是否主推(0否,1:是)"  )
    private String isMainly;

    /** 品名 */
    @ApiModelProperty(value = "品名"  )
    private String productName;

    /** 计控吊牌确定 */
    @ApiModelProperty(value = "计控吊牌确定"  )
    private String productHangtagConfirm;

    /** 计控成本确认 */
    @ApiModelProperty(value = "计控成本确认"  )
    private String controlConfirm;

    /** 品控吊牌价确定 */
    @ApiModelProperty(value = "品控吊牌价确定"  )
    private String controlHangtagConfirm;

    /** 跟款设计师 */
    @ApiModelProperty(value = "跟款设计师"  )
    private String merchDesignName;

    /** 版式名称 */
    @ApiModelProperty(value = "版式名称"  )
    private String patternDesignName;

    /** 是否报次款0否 1是 */
    @ApiModelProperty(value = "是否报次款0否 1是"  )
    private String isDefective;


    /** 上新时间 */
    @ApiModelProperty(value = "上新时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date newDate;

    /** 备注 */
    @ApiModelProperty(value = "备注"  )
    private String remarks;

}
