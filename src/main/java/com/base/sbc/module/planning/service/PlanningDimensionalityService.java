/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.planning.dto.QueryPlanningDimensionalityDto;
import com.base.sbc.module.planning.dto.SaveDelDimensionalityDto;
import com.base.sbc.module.planning.dto.UpdateDimensionalityDto;
import com.base.sbc.module.planning.entity.PlanningDimensionality;

import java.util.List;

/**
 * 类描述：企划-维度表 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.planning.service.PlanningDimensionalityService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-27 11:15:30
 */
public interface PlanningDimensionalityService extends BaseService<PlanningDimensionality> {

    /**
     * 自定义方法区 不替换的区域【other_start】
     **/

    ApiResult getDimensionalityList(QueryPlanningDimensionalityDto queryPlanningDimensionalityDto);

    ApiResult getFormDimensionality(QueryPlanningDimensionalityDto queryPlanningDimensionalityDto);


    ApiResult saveDelDimensionality(List<SaveDelDimensionalityDto> list);

    ApiResult delDelDimensionality(String id);


    ApiResult saveDimensionality(UpdateDimensionalityDto updateDimensionalityDto);

/** 自定义方法区 不替换的区域【other_end】 **/


}
