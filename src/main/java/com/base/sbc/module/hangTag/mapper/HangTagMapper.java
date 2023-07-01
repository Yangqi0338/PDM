/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangTag.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.hangTag.dto.HangTagSearchDTO;
import com.base.sbc.module.hangTag.entity.HangTag;
import com.base.sbc.module.hangTag.vo.HangTagListVO;
import com.base.sbc.module.hangTag.vo.HangTagVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：吊牌表 dao类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.hangTag.dao.HangTagDao
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-26 17:15:52
 */
@Mapper
public interface HangTagMapper extends BaseMapper<HangTag> {
// 自定义方法区 不替换的区域【other_start】

    /**
     * 列表分页查询
     *
     * @param dto
     * @return
     */
    List<HangTagListVO> queryList(@Param("dto") HangTagSearchDTO dto);

    /**
     * 通过id查询详情
     *
     * @param id
     * @param companyCode
     * @return
     */
    HangTagVO getDetailsById(@Param("id") String id, @Param("companyCode") String companyCode);

// 自定义方法区 不替换的区域【other_end】
}
