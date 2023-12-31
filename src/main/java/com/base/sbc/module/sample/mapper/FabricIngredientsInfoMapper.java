/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.module.sample.vo.FabricIngredientsInfoVo;
import org.apache.ibatis.annotations.Mapper;
import com.base.sbc.module.sample.entity.FabricIngredientsInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：调样-辅料信息 dao类
 * @address com.base.sbc.module.sample.dao.FabricIngredientsInfoDao
 * @author mengfanjiang  
 * @email  XX.com
 * @date 创建时间：2023-7-14 17:32:38 
 * @version 1.0  
 */
@Mapper
public interface FabricIngredientsInfoMapper extends BaseMapper<FabricIngredientsInfo> {
// 自定义方法区 不替换的区域【other_start】

  List<FabricIngredientsInfoVo> getSelectList(@Param(Constants.WRAPPER) QueryWrapper qw);

// 自定义方法区 不替换的区域【other_end】
}

