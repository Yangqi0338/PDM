/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangtag.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.hangtag.dto.HangTagIngredientDTO;
import com.base.sbc.module.hangtag.entity.HangTagIngredient;
import com.base.sbc.module.hangtag.vo.HangTagIngredientVO;

import java.util.List;

/**
 * 类描述：吊牌成分表 service类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.hangTag.service.HangTagIngredientService
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-26 17:15:55
 */
public interface HangTagIngredientService extends BaseService<HangTagIngredient> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 保存
     *
     * @param hangTagIngredients
     * @param hangTagId
     * @param userCompany
     */
    void save(List<HangTagIngredientDTO> hangTagIngredients, String hangTagId, String userCompany);

    /**
     * 通过吊牌id查询
     *
     * @param hangTagId
     * @param userCompany
     * @return
     */
    List<HangTagIngredientVO> getIngredientListByHangTagId(String hangTagId, String userCompany);


// 自定义方法区 不替换的区域【other_end】


}

