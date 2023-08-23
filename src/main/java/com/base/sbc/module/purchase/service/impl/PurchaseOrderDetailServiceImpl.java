/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.purchase.mapper.PurchaseOrderDetailMapper;
import com.base.sbc.module.purchase.entity.PurchaseOrderDetail;
import com.base.sbc.module.purchase.service.PurchaseOrderDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：采购-采购单-明细 service类
 * @address com.base.sbc.module.purchase.service.PurchaseOrderDetailService
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-4 9:43:21
 * @version 1.0
 */
@Service
public class PurchaseOrderDetailServiceImpl extends BaseServiceImpl<PurchaseOrderDetailMapper, PurchaseOrderDetail> implements PurchaseOrderDetailService {

// 自定义方法区 不替换的区域【other_start】

    @Override
    public List<PurchaseOrderDetail> selectPurchaseCode(QueryWrapper<PurchaseOrderDetail> qw) {
        return baseMapper.selectPurchaseCode(qw);
    }

// 自定义方法区 不替换的区域【other_end】

}
