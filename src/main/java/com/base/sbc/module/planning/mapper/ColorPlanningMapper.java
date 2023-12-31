/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.planning.dto.ColorPlanningSearchDTO;
import com.base.sbc.module.planning.entity.ColorPlanning;
import com.base.sbc.module.planning.vo.ColorPlanningListVO;
import com.base.sbc.module.planning.vo.ColorPlanningVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：颜色企划 dao类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.planning.dao.ColorPlanningDao
 * @email your email
 * @date 创建时间：2023-8-15 13:58:50
 */
@Mapper
public interface ColorPlanningMapper extends BaseMapper<ColorPlanning> {
// 自定义方法区 不替换的区域【other_start】

    /**
     * 获取颜色企划列表
     *
     * @param dto
     * @return
     */
    List<ColorPlanningListVO> getColorPlanningList(@Param("dto") ColorPlanningSearchDTO dto);

    /**
     * 通过id获取详情
     *
     * @param id
     * @return
     */
    ColorPlanningVO getDetailById(@Param("id") String id);


// 自定义方法区 不替换的区域【other_end】
}

