/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.service;

import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.fabric.dto.FabricPlanningSaveDTO;
import com.base.sbc.module.fabric.dto.FabricPlanningSearchDTO;
import com.base.sbc.module.fabric.entity.FabricPlanning;
import com.base.sbc.module.fabric.vo.FabricPlanningListVO;
import com.base.sbc.module.fabric.vo.FabricPlanningVO;
import com.github.pagehelper.PageInfo;

/**
 * 类描述：面料企划 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.service.FabricPlanningService
 * @email your email
 * @date 创建时间：2023-8-23 11:03:00
 */
public interface FabricPlanningService extends BaseService<FabricPlanning> {
// 自定义方法区 不替换的区域【other_start】

    /**
     * 获取列表
     *
     * @param dto
     * @return
     */
    PageInfo<FabricPlanningListVO> getFabricPlanningList(FabricPlanningSearchDTO dto);

    /**
     * 保存
     *
     * @param dto
     */
    void save(FabricPlanningSaveDTO dto);


    /**
     * 获取详情
     *
     * @param id
     * @return
     */
    FabricPlanningVO getDetail(String id);

    /**
     * 审核处理
     * @param dto
     * @return
     */
    boolean approval(AnswerDto dto);


// 自定义方法区 不替换的区域【other_end】


}
