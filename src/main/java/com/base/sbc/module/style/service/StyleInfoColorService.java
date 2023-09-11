/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.pack.dto.PackInfoDto;
import com.base.sbc.module.style.dto.StyleInfoColorDto;
import com.base.sbc.module.style.dto.StyleSaveDto;
import com.base.sbc.module.style.entity.StyleInfoColor;
import com.base.sbc.module.style.vo.StyleInfoColorVo;
import com.github.pagehelper.PageInfo;

/** 
 * 类描述：款式设计详情颜色表 service类
 * @address com.base.sbc.module.style.service.StyleInfoColorService
 * @author LiZan
 * @email 2682766618@qq.com
 * @date 创建时间：2023-8-24 15:21:29
 * @version 1.0  
 */
public interface StyleInfoColorService extends BaseService<StyleInfoColor>{

// 自定义方法区 不替换的区域【other_start】

    /**
     * 分页查询 款式设计详情颜色
     * @param styleInfoColorDto 款式设计详情颜色DTO
     * @return 分页款式设计详情颜色
     */
    public PageInfo<StyleInfoColorVo> pageList(StyleInfoColorDto styleInfoColorDto);
    /**
     * 根据颜色code删除款式设计详情颜色
     * @param styleInfoColorDto 式设计详情颜色DTO
     */
    void delStyleInfoColorById( StyleInfoColorDto styleInfoColorDto);

    /**
     * 根据id修改款式设计详情颜色
     * @param styleInfoColorDto 款式设计详情颜色DTO
     */
    void updateStyleInfoColorById(StyleInfoColorDto styleInfoColorDto);

    /**
     * 添加款式设计详情颜色
     * @param packInfoDto 资料包dto
     */
    void insertStyleInfoColorList(PackInfoDto packInfoDto);

    /**
     * 更新颜色核价状态
     * @param id
     * @param pricingFlag
     */
    void updatePricingFlag(String id, String pricingFlag);

// 自定义方法区 不替换的区域【other_end】

	
}
