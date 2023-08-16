/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.basicsdatum.dto.QueryRevampBasicsdatumSupplierDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSupplier;
import com.base.sbc.module.basicsdatum.vo.SelectVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 类描述：基础资料-供应商 dao类
 * @address com.base.sbc.module.basicsdatum.dao.BasicsdatumSupplierDao
 * @author mengfanjiang  
 * @email  2915350015@qq.com
 * @date 创建时间：2023-5-22 10:51:07 
 * @version 1.0  
 */
@Mapper
public interface BasicsdatumSupplierMapper extends BaseMapper<BasicsdatumSupplier> {
/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 分页查询下拉框供应商
     * @param queryRevampBasicsdatumSupplierDto 参数
     * @return 下拉框VO
     */
    List<SelectVo> selectSupplierPage(QueryRevampBasicsdatumSupplierDto queryRevampBasicsdatumSupplierDto);

/** 自定义方法区 不替换的区域【other_end】 **/
}