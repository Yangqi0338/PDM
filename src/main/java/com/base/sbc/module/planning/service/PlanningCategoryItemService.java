/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.ccm.entity.BasicStructureTreeVo;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.common.dto.GetMaxCodeRedis;
import com.base.sbc.module.common.service.IServicePlus;
import com.base.sbc.module.planning.dto.AllocationDesignDto;
import com.base.sbc.module.planning.dto.ProductCategoryItemSearchDto;
import com.base.sbc.module.planning.dto.ProductSeasonExpandByCategorySearchDto;
import com.base.sbc.module.planning.dto.SetTaskLevelDto;
import com.base.sbc.module.planning.entity.PlanningBand;
import com.base.sbc.module.planning.entity.PlanningCategory;
import org.springframework.transaction.annotation.Transactional;

import com.base.sbc.module.planning.entity.PlanningCategoryItem;

import java.util.*;

/**
 * 类描述：企划-坑位信息 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.planning.service.PlanningCategoryItemService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-3-31 13:40:49
 */
public interface PlanningCategoryItemService extends IServicePlus<PlanningCategoryItem>{
    public int saveCategoryItem(PlanningBand band, PlanningCategory category,List<PlanningCategoryItem> dbCategoryItemList);
    @Transactional(readOnly = false)
    public boolean delByPlanningCategory(String companyCode,List<String> ids);
    public boolean delByPlanningBand(String userCompany, String id);

    String selectMaxDesignNo(QueryWrapper qc);

    List<String> selectCategoryIdsByBand(QueryWrapper qw);

    /**
     * 修改/提交
     * @param planningBandId
     * @param item
     */
    void updateAndCommit(String planningBandId, List<PlanningCategoryItem> item);

    /**
     * 获取最大流水号
     * @param data
     * @param userCompany
     * @return
     */
    String getMaxDesignNo(GetMaxCodeRedis data, String userCompany);

    /**
     * 分配设计师
     * @param dtoList
     * @return
     */
    boolean allocationDesign(List<AllocationDesignDto> dtoList);

    /**
     * 设置任务等级
     * @param dtoList
     * @return
     */
    boolean setTaskLevel(List<SetTaskLevelDto> dtoList);

    /**
     * 查找坑位信息
     * @param dto
     * @return
     */
    ApiResult findProductCategoryItem(ProductCategoryItemSearchDto dto);

    /**
     * 按品类展开
     * @param dto
     * @return
     */
    List<BasicStructureTreeVo> expandByCategory(ProductSeasonExpandByCategorySearchDto dto);

    /**
     * 坑位信息下发
     * @param categoryItemList
     * @return
     */
    boolean send(List<PlanningCategoryItem> categoryItemList);
}
