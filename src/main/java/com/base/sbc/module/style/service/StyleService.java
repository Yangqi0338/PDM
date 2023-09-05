/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service;

import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.module.common.dto.GetMaxCodeRedis;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.formType.vo.FieldManagementVo;
import com.base.sbc.module.pack.dto.PackInfoDto;
import com.base.sbc.module.pack.dto.PlanningDemandStatisticsResultVo;
import com.base.sbc.module.pack.vo.PackBomVo;
import com.base.sbc.module.planning.dto.PlanningBoardSearchDto;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.planning.entity.PlanningChannel;
import com.base.sbc.module.planning.vo.PlanningSummaryVo;
import com.base.sbc.module.planning.vo.ProductCategoryTreeVo;
import com.base.sbc.module.sample.vo.SampleUserVo;
import com.base.sbc.module.style.dto.*;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.vo.*;
import com.base.sbc.open.dto.OpenStyleDto;
import com.github.pagehelper.PageInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 类描述：款式设计 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.style.service.StyleService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-9 11:16:15
 */
public interface StyleService extends BaseService<Style> {


/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 保存样衣
     *
     * @param dto
     * @return
     */
    Style saveStyle(StyleSaveDto dto);

    /**
     * 新增款式设计
     *
     * @param dto
     * @return
     */
    Style saveNewStyle(StyleSaveDto dto);

    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    PageInfo queryPageInfo(StylePageDto dto);


    /**
     * 发起审批
     *
     * @param id
     * @return
     */
    boolean startApproval(String id);

    /**
     * 处理审批回调
     * @param dto
     * @return
     */
    boolean approval(AnswerDto dto);

    /**
     * 发送打板指令
     * @param dto
     * @return
     */
    boolean sendMaking(SendSampleMakingDto dto);

    Style checkedStyleExists(String id);

    /**
     * 查询明细数据
     *
     * @param id
     * @return
     */
    StyleVo getDetail(String id);

    /**
     * 设计档案左侧树（0级:年份季节,1级:波段,2级:大类,3级:品类）
     * @param designDocTreeVo
     * @return
     */
    List<DesignDocTreeVo> queryDesignDocTree(DesignDocTreeVo designDocTreeVo);

    /**
     * 查询维度标签
     *
     * @param dto
     * @return
     */
    List<FieldManagementVo> queryDimensionLabels(DimensionLabelsSearchDto dto);

    /**
     * 查询维度标签
     *
     * @param id 款式设计id
     * @return
     */
    List<FieldManagementVo> queryDimensionLabelsBySdId(String id);

    List<SampleUserVo> getDesignerList(String companyCode);

    /**
     * 产品季总览-波段汇总统计图表
     *
     * @param month
     * @return
     */
    List getBandChart(String month);

    List getCategoryChart(String category);

    Map getDesignDataOverview(String time);


    PlanningSummaryVo categoryBandSummary(PlanningBoardSearchDto dto);

    List<StyleBoardCategorySummaryVo> categorySummary(PlanningBoardSearchDto dto);

    CategoryStylePlanningVo categoryStylePlanning(PlanningBoardSearchDto dto);

    List<ProductCategoryTreeVo> getProductCategoryTree(ProductCategoryTreeVo vo);

    /**
     * 获取产品季全品类
     * @param vo
     * @return
     */
    List getProductAllCategory(ProductCategoryTreeVo vo);

    void updateBySeatChange(PlanningCategoryItem item);

    void updateByChannelChange(PlanningChannel planningChannel);

    PageInfo<PackBomVo> bomList(StyleBomSearchDto dto);

    Boolean delBom(String id);

    Boolean saveBom(StyleBomSaveDto dto);

    /**
     * 引用历史款
     *
     * @param id
     * @param historyStyleId
     * @return
     */
    StyleVo getDetail(String id, String historyStyleId);


    /**
     * 方法描述 验证款式号型类型是否可修改
     *
     * @param publicStyleColorDto
     * @return
     */
    Boolean checkColorSize(PublicStyleColorDto publicStyleColorDto);

    /**
     * 企划需求统计
     *
     * @param id 款式id
     * @return
     */
    PlanningDemandStatisticsResultVo planningDemandStatistics(String id);

    /**
     * 保存款式设计详情颜色
     * @param packInfoDto 资料包DTO
     * @return 款式设计详情颜色列表
     */
    List<StyleInfoColorVo> saveBomInfoColorList(PackInfoDto packInfoDto);

    /**
     * 领猫同步数据
     * @param companyCode
     * @return
     */
    List<OpenStyleDto> getStyleListForLinkMore(String companyCode);

    /**
     * 修改目标成本
     *
     * @param foreignId
     * @param productCost
     */
    void updateProductCost(String id, BigDecimal productCost);
/** 自定义方法区 不替换的区域【other_end】 **/


    /**
     * 获取下一个编码
     *
     * @return
     */
    String genDesignNo(Style style);

    /**
     * 获取下一个编码
     *
     * @return
     */
    String getMaxDesignNo(GetMaxCodeRedis data,String userCompany);

    void saveDesignNo(Style style);

}

