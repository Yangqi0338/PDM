package com.base.sbc.open.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 款式样品实体类
 * @address com.base.sbc.open.entity.BiStyle
 * @version 1.0  样衣表
 */
@Data
@TableName("bi_style")
public class BiStyle {
    /** 季节 品牌 */
    private String c8SeasonBrand;
    /** 季节 年度 */
    private String c8SeasonYear;
    /** 季节 季节 */
    private String c8SeasonQuarter;
    /** 设计款号 */
    private String styleCode;
    /** 启用 */
    private String styleActive;
    /** 大类 */
    private String style1stCategory;
    /** 品类 */
    private String c8StyleProdCategory;
    /** 中类 */
    private String c8Style2ndCategory;
    /** 小类 */
    private String c8Style3rdCategory;
    /** 样品数 */
    private String c8StyleCntSample;
    /** 模式 */
    private String developmentMode;
    /** 款式类型 */
    private String productType;
    /** 生产类型 */
    private String developmentType;
    /** 紧急状态 */
    private String c8StyleStatus;
    /** 款式名称 */
    private String styleName;
    /** 廓形 */
    private String c8StylerKuoXing;
    /** 设计师 */
    private String c8StyleAttrDesigner;
    /** 设计师 用户登录 */
    private String c8StyleAttrDesignerID;
    /** 跟款设计师 */
    private String c8StyleAttrMerchDesigner;
    /** 跟款设计师 用户登录 */
    private String c8StyleAttrMerchDesignerID;
    /** 工艺员 */
    private String c8StyleAttrTechnician;
    /** 工艺员 用户登录 */
    private String c8StyleAttrTechnicianID;
    /** 版师 */
    private String c8StyleAttrPatternMaker;
    /** 版师 用户登录 */
    private String c8StyleAttrPatternMakerID;
    /** 材料专员 */
    private String c8StyleAttrFabDevelope;
    /** 材料专员 用户登录 */
    private String c8StyleAttrFabDevelopeID;
    /** 实际出稿时间 */
    private String c8StyleAttrActualDesignTime;
    /** 单位 */
    private String c8StyleAttrUOM;
    /** 材质 */
    private String c8StyleAttrCaiZhi;
    /** 毛纱针法 */
    private String c8StyleAttrYarnNeedle;
    /** 毛纱针型 */
    private String c8StyleAttrYarnNeedleType;
    /** 肩宽 */
    private String c8StyleAttrJianKuan;
    /** 胸围 */
    private String c8StyleAttrXiongWei;
    /** 腰型 */
    private String c8StyleAttrYaoXing;
    /** 花型 */
    private String c8StyleAttrPrintting;
    /** 衣长(CM) */
    private String c8StyleAttrCoatLength;
    /** 衣长分类 */
    private String c8StyleAttrLengthRange;
    /** 袖型 */
    private String c8StyleAttrXiuXing;
    /** 袖长 */
    private String c8StyleAttrXiuChang;
    /** 门襟 */
    private String c8StyleAttrMenJIng;
    /** 领型 */
    private String c8StyleAttrLingXing;
    /** 克重 */
    private String c8StyleAttrKezhong;
    /** 开发分类 */
    private String c8StyleAttrDevClass;
    /** 款式工艺 */
    private String c8StyleAttrProductSpecs;
    /** 是否大货 */
    private String c8SyncIfProduction;
    /** 是否上会 */
    private String c8SyncIfSalesconfernce;
    /** 打版难度 */
    private String c8StylePatDiff;
    /** 市场调研 */
    private String competitiveStyles;
    /** Style URL */
    private String c8StylePLMID;
    /** 创建时间 */
    private String createdAt;
    /** 创建人 */
    private String createdBy;
    /** 修改时间 */
    private String modifiedAt;
    /** 修改人 */
    private String modifiedBy;
    /** 复制自 */
    private String copiedFrom;
    /** 改款设计师 */
    private String c8SARevisedDesigner;
    /** 改款设计师 用户登录 */
    private String c8SARevisedDesignerID;
    /** 审版设计师 */
    private String c8SAReviewedDesigner;
    /** 审版设计师 用户登录 */
    private String c8SAReviewedDesignerID;
    /** 下稿设计师 */
    private String c8SADataDesigner;
    /** 下稿设计师 用户登录 */
    private String c8SADataDesignerID;
    /** 款式来源 */
    private String c8StyleOrigin;
    /** 款式定位 */
    private String c8StyleAttrOrientation;
    /** 款式风格 */
    private String c8StyleAttrFlavour;
}