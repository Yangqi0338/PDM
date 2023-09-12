/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.purchase.service;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.purchase.entity.OutboundOrderDetail;
import com.base.sbc.module.purchase.entity.PurchaseDemand;
import com.base.sbc.module.purchase.entity.PurchaseOrderDetail;

import java.util.List;

/** 
 * 类描述：采购-采购需求表 service类
 * @address com.base.sbc.module.purchase.service.PurchaseDemandService
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-8-2 14:29:52
 * @version 1.0  
 */
public interface PurchaseDemandService extends BaseService<PurchaseDemand>{
    ApiResult cancel(String companyCode, String ids);

    ApiResult generatePurchaseDemand(String companyCode, String id, String materialIds, String colors);

    ApiResult generatePurchaseOrder(UserCompany userCompany, String companyCode, List<PurchaseDemand> purchaseDemandList);

    void deleteRelationData(String companyCode, String id);

    void manipulatePlanNum(List<PurchaseOrderDetail> purchaseOrderDetailList, String type);

	void manipulateReadyNum(List<OutboundOrderDetail> outboundOrderDetailList, String type);
}

