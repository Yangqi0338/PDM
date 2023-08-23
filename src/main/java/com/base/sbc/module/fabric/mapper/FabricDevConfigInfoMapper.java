/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fabric.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.fabric.entity.FabricDevConfigInfo;
import com.base.sbc.module.fabric.vo.FabricDevConfigInfoListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：面料开发配置信息 dao类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.fabric.dao.FabricDevConfigInfoDao
 * @email your email
 * @date 创建时间：2023-8-7 11:01:33
 */
@Mapper
public interface FabricDevConfigInfoMapper extends BaseMapper<FabricDevConfigInfo> {
// 自定义方法区 不替换的区域【other_start】

    /**
     * 查询列表
     *
     * @param companyCode
     * @return
     */
    List<FabricDevConfigInfoListVO> getDevConfigList(@Param("companyCode") String companyCode,
                                                     @Param("status") String status);


// 自定义方法区 不替换的区域【other_end】
}