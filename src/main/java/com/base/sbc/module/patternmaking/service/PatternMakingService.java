/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.service;
import com.base.sbc.module.common.service.IServicePlus;
import com.base.sbc.module.patternmaking.dto.PatternMakingDto;
import com.base.sbc.module.patternmaking.dto.SampleDesignSendDto;
import com.base.sbc.module.patternmaking.dto.SetPatternDesignDto;
import com.base.sbc.module.patternmaking.dto.TechnologyCenterTaskSearchDto;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.patternmaking.vo.PatternDesignVo;
import com.base.sbc.module.patternmaking.vo.PatternMakingVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/** 
 * 类描述：打版管理 service类
 * @address com.base.sbc.module.patternmaking.service.PatternMakingService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-29 13:33:05
 * @version 1.0  
 */
public interface PatternMakingService extends IServicePlus<PatternMaking>{

/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 初版样(头版样)值
     */
    String firstSampleType="初版样";
    /**
     * 通过样衣设计id 查找
     * @param sampleDesignId
     * @return
     */
    List<PatternMakingVo> findBySampleDesignId(String sampleDesignId);

    /**
     * 保存
     * @param dto
     * @return
     */
    PatternMaking savePatternMaking(PatternMakingDto dto);

    /**
     * 样衣设计下发
     * @param dto
     * @return
     */
    boolean sampleDesignSend(SampleDesignSendDto dto);

    /**
     * 节点状态改变
     * @param dto
     * @param node
     * @param status
     * @return
     */
    boolean nodeStatusChange(PatternMakingDto dto,String node,String status);

    /**
     * 版房主管下发
     * @param dto
     * @return
     */
    boolean prmSend(SetPatternDesignDto dto);

    /**
     * 技术中心看板-任务列表
     * @param dto
     * @return
     */
    PageInfo technologyCenterTaskList(TechnologyCenterTaskSearchDto dto);

    /**
     * 设置版师
     *
     * @param dto
     * @return
     */
    boolean setPatternDesign(SetPatternDesignDto dto);

    /**
     * 获取版师列表
     *
     * @param planningSeasonId
     * @return
     */
    List<PatternDesignVo> getPatternDesignList(String planningSeasonId);

    /**
     * 中断
     *
     * @param id
     * @return
     */
    boolean breakOff(String id);


/** 自定义方法区 不替换的区域【other_end】 **/


}

