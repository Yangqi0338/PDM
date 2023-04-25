/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.fieldManagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.fieldManagement.dto.QueryFieldManagementDto;
import com.base.sbc.module.fieldManagement.vo.FieldManagementVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import com.base.sbc.module.fieldManagement.entity.FieldManagement;

import java.util.List;
import java.util.Map;

/**
 * 类描述：字段管理表 dao类
 * @address com.base.sbc.module.fieldManagement.dao.FieldManagementDao
 * @author lxl  
 * @email  lxl.fml@gmail.com
 * @date 创建时间：2023-4-15 18:33:51 
 * @version 1.0  
 */
@Mapper
public interface FieldManagementMapper extends BaseMapper<FieldManagement> {
/** 自定义方法区 不替换的区域【other_start】 **/

  List<FieldManagementVo> getFieldManagementList(QueryFieldManagementDto queryFieldManagementDto);

  @MapKey("COLUMN_NAME")
  List<Map<String,String>> getTableMessage(String coding);

/** 自定义方法区 不替换的区域【other_end】 **/
}