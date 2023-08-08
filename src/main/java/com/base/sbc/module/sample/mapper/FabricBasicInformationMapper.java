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
import com.base.sbc.module.sample.entity.FabricBasicInformation;
import com.base.sbc.module.sample.vo.FabricInformationVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：面料基本信息 dao类
 * @address com.base.sbc.module.sample.dao.FabricBasicInformationDao
 * @author lxl  
 * @email  lxl.fml@gmail.com
 * @date 创建时间：2023-4-19 18:23:26 
 * @version 1.0  
 */
@Mapper
//@DataIsolation(groups={"'getFabricInformationList'"},defaultState=false)
public interface FabricBasicInformationMapper extends BaseMapper<FabricBasicInformation> {
/** 自定义方法区 不替换的区域【other_start】 **/


  /**
   * 查询面料调样单
   * @param qw
   * @return
   */
  List<FabricInformationVo>  getFabricInformationList(@Param(Constants.WRAPPER) QueryWrapper qw);

/** 自定义方法区 不替换的区域【other_end】 **/
}