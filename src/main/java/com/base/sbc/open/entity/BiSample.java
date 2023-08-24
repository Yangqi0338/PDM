package com.base.sbc.open.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 表名：bi_sample
 * 款式样品  样衣看板
 */
@Data
@TableName("bi_sample")
public class BiSample {

    /**
     * 主键
     */
    private String id;

    /**
     * 款式样品
     */
    private String sampleName;

    /**
     * 版师工作量评分
     */
    private String c8SamplePaperPatternScore;

    /**
     * 版师质量评分
     */
    private String c8SampleDesignerScore;

    /**
     * 裁剪件数
     */
    private String c8ProductSampleCjqty;

    /**
     * 裁剪开始时间
     */
    private String c8ProductSampleCutterStartDate;

    /**
     * 裁剪师
     */
    private String c8ProductSampleCutter;

    /**
     * 裁剪完成时间
     */
    private String c8ProductSampleCutterFinDate;

    /**
     * 采购的产品
     */
    private String samplePOProducts;

    /**
     * 采购的颜色
     */
    private String samplePOColors;

    /**
     * 参考分类*
     */
    private String c8ProductSampleReferenceCategory;

    /**
     * 查版日期
     */
    private String c8SampleChaBanData;

    /**
     * 产品
     */
    private String sampleSRLineItem;

    /**
     * __Parent__
     */
    private String parent;

    /**
     * 车缝开始日期
     */
    private String c8ProductSampleActStartData;

    /**
     * 车缝完成日期
     */
    private String c8ProductSampleSweiningFinData;

    /**
     * 尺码
     */
    private String productSize;

    /**
     * 打样设计师
     */
    private String c8ProductSampleProofingDesigner;

    /**
     * 打样设计师 用户登录
     */
    private String proofingDesignerID;

    /**
     * 打样需求数量
     */
    private String c8SampleSampleQty;

    /**
     * 放码日期
     */
    private String c8SampleFangMaData;

    /**
     * 放码师
     */
    private String c8ProductSampleFangMaShi;

    /**
     * 负责人
     */
    private String responsibleUsers;

    /**
     * 改版原因
     */
    private String c8SampleCauseOfReversion;

    /**
     * 工价/件
     */
    private String c8SamplePriceCost;

    /**
     * 工艺单完成日期
     */
    private String c8SampleTechPackData;

    /**
     * 后技术备注说明
     */
    private String c8ProductSampleProComment;

    /**
     * 计划开始时间
     */
    private String c8ProductSamplePlanStartData;

    /**
     * 计提收入
     */
    private String c8SamplePriceIncomet;

    /**
     * 技术收到日期
     */
    private String sampleReceivedDate;

    /**
     * 类型
     */
    private String sampleType;

    /**
     * 面辅料信息
     */
    private String c8SampleMaterialInfo;

    /**
     * 面辅料信息(含格式)
     */
    private String c8SampleMaterialInfo2;

    /**
     * 面料检测单日期
     */
    private String c8SampleMaterialDetData;

    /**
     * 配色
     */
    private String productColor;

    /**
     * 齐套日期*
     */
    private String c8ProductSampleSamplingDate;

    /**
     * 前技术确认是否齐套
     */
    private String c8SampleTechIfQitao;

    /**
     * 样衣需求完成日期*
     */
    private String sampleNotes;

    /**
     * 收到正确样日期
     */
    private String c8ProductSampleMatLackNote;

    /**
     * 数据表单
     */
    private String c8SampleIfQitao;

    /**
     * 实际收到数量
     */
    private String c8SampleSampleRecQty;

    /**
     * 收到正确样日期
     */
    private String c8SampleRecivedCorrectData;

    /**
     * 样衣条码
     */
    private String sampleDataSheets;

    /**
     * 替代产品
     */
    private String sampleProductAlternative;

    /**
     * 完成件数
     */
    private String c8ProductSampleSampleFinQty;

    /**
     * 下发给版师时间*
     */
    private String c8ProductSampleHO2SDTime;

    /**
     * 下发给版师状态*
     */
    private String c8ProductSampleHO2SDState;

    /**
     * 下发给样衣组长时间*
     */
    private String c8ProductSampleHO2STime;

    /**
     * 下发给样衣组长状态*
     */
    private String c8ProductSampleHO2SState;

    /**
     * 需求数量*
     */
    private String requestedQty;

    /**
     * 样品工厂
     */
    private String c8SampleRequestDate;

    /**
     * 样板号
     */
    private String c8SampleSampleNumber;

    /**
     * 样品存储
     */
    private String sampleStorage;

    /**
     * 样品存储 Bin Number
     */
    private String storageBinNumber;

    /**
     * 样品存储名称
     */
    private String storageName;

    /**
     * 样品工厂
     */
    private String sampleFactory;

    /**
     * 请求编号
     */
    private String requestNumber;

    /**
     * 样衣工工作量评分
     */
    private String c8SampleSampleScore;

    /**
     * 样衣工质量评分
     */
    private String c8SamplePatternScore;

    /**
     * 样衣师
     */
    private String c8ProductSampleSeiwer;

    /**
     * 样衣实际完成日期
     */
    private String c8SampleSampleFinDate;

    /**
     * 样衣完成
     */
    private String c8SampleIfFinished;

    /**
     * 改版意见
     */
    private String c8SampleWhyModify;

    /**
     * 已创建款式
     */
    private String createdStyles;

    /**
     * 纸样完成件数
     */
    private String c8ProductSamplePatternFinQty;

    /**
     * 纸样完成时间
     */
    private String c8ProductSamplePatternFinData;

    /**
     * 状态
     */
    private String sampleStatus;

    /**
     * 主搭配
     */
    private String mainMaterialsList;

    /**
     * Dimensions
     */
    private String dimensions;

    /**
     * 面辅料齐套
     */
    private String c8ProductSampleMatIfQitao;

    /**
     * 纸样需求完成日期*
     */
    private String c8ProductSamplePatternReqDate;

    /**
     * Style PLM ID
     */
    private String c8StylePLMID;

    /**
     * Colorway PLM ID
     */
    private String c8ColorwayPLMID;

    /**
     * 供应商 供应商编码
     */
    private String supplierNumber;

    /**
     * 延迟打板原因
     */
    private String c8ProductSampleDelayedReason;

    /**
     * 打版难度
     */
    private String c8SamplePatDiff;

    /**
     * 打样顺序
     */
    private String c8SamplePatSeq;

    /**
     * 样品 PLM ID
     */
    private String c8SamplePLMID;

    /**
     * Style URL
     */
    private String c8ProductSampleStyleURL;

    /**
     * 样品 MC Date
     */
    private String c8SampleMCDate;

    /**
     * 样品 BExt Auxiliary
     */
    private String c8SampleBExtAuxiliary;

    /**
     * 样品 EA Valid From
     */
    private String c8SampleEAValidFrom;

    /**
     * 样品 EA Valid To
     */
    private String c8SampleEAValidTo;

    /**
     * 样品条码
     */
    private String c8SampleBarcode;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 修改时间
     */
    private Date modifiedAt;

    /**
     * 修改者
     */
    private String modifiedBy;

    // ... 所有其他字段的getter和setter方法 ...
}
