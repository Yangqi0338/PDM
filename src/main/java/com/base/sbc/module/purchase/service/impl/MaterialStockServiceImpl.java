/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.purchase.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.utils.BigDecimalUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.basicsdatum.service.BasicsdatumMaterialService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.purchase.entity.MaterialStockLog;
import com.base.sbc.module.purchase.entity.WarehousingOrder;
import com.base.sbc.module.purchase.entity.WarehousingOrderDetail;
import com.base.sbc.module.purchase.mapper.MaterialStockMapper;
import com.base.sbc.module.purchase.entity.MaterialStock;
import com.base.sbc.module.purchase.service.MaterialStockLogService;
import com.base.sbc.module.purchase.service.MaterialStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：物料库存 service类
 * @address com.base.sbc.module.purchase.service.MaterialStockService
 * @author tzy
 * @email 974849633@qq.com
 * @date 创建时间：2023-9-13 15:44:13
 * @version 1.0  
 */
@Service
public class MaterialStockServiceImpl extends BaseServiceImpl<MaterialStockMapper, MaterialStock> implements MaterialStockService {
    @Autowired
    private MaterialStockService materialStockService;

    @Autowired
    private BasicsdatumMaterialService basicsdatumMaterialService;

    @Autowired
    private MaterialStockLogService materialStockLogService;

    /**
     * 入库单 操作 物料库存
     *
     * @param order  入库单
     * @param orderDetailList  入库单明细
     * @param operation  操作 0 增加 1 减少
     * */
    @Override
    public void warehousingMaterialStock(WarehousingOrder order, List<WarehousingOrderDetail> orderDetailList, String operation) {
        IdGen idGen = new IdGen();

        List<String> materialCodeList = orderDetailList.stream().map(WarehousingOrderDetail::getMaterialCode).collect(Collectors.toList());

        QueryWrapper<MaterialStock> materialQw = new QueryWrapper<>();
        materialQw.in("material_code", materialCodeList);
        materialQw.eq("warehouse_id", order.getWarehouseId());
        List<MaterialStock> materialStockList = materialStockService.list(materialQw);
        Map<String, MaterialStock> materialStockMap = materialStockList.stream().collect(Collectors.toMap(MaterialStock::getMaterialCode, item -> item));

        QueryWrapper<BasicsdatumMaterial> basicQw = new QueryWrapper<>();
        basicQw.in("material_code", materialCodeList);
        List<BasicsdatumMaterial> basicsdatumMaterialList = basicsdatumMaterialService.list(basicQw);
        Map<String, BasicsdatumMaterial> materialMap = basicsdatumMaterialList.stream().collect(Collectors.toMap(BasicsdatumMaterial::getMaterialCode, item -> item, (k, v) -> k));

        List<MaterialStock> addList = new ArrayList<>();
        List<MaterialStock> updateList = new ArrayList<>();
        List<MaterialStockLog> materialStockLogList = new ArrayList<>();
        for(WarehousingOrderDetail orderDetail : orderDetailList){
            BigDecimal beforeValue = new BigDecimal(0.0);
            BigDecimal afterValue = new BigDecimal(0.0);

            MaterialStock materialStock = materialStockMap.get(orderDetail.getMaterialCode());
            BasicsdatumMaterial material = materialMap.get(orderDetail.getMaterialCode());
            if(materialStock == null){
                //物料库存中不存在此物料，初始化物料
                materialStock = new MaterialStock(order, orderDetail, material);
                materialStock.setId(idGen.nextIdStr());
                materialStock.setCompanyCode(order.getCompanyCode());

                if(StringUtils.equals(operation, "0")) {
                    materialStock.setStockQuantity(orderDetail.getWarehouseNum());
                    afterValue = orderDetail.getWarehouseNum();
                }else{
                    materialStock.setStockQuantity(orderDetail.getWarehouseNum().negate());
                    afterValue = orderDetail.getWarehouseNum().negate();
                }
                addList.add(materialStock);
            }else{
                beforeValue = materialStock.getStockQuantity();
                if(StringUtils.equals(operation, "0")) {
                    BigDecimal result = BigDecimalUtil.add(materialStock.getStockQuantity(), orderDetail.getWarehouseNum());
                    materialStock.setStockQuantity(result);
                    afterValue = result;
                }else{
                    BigDecimal result = BigDecimalUtil.sub(materialStock.getStockQuantity(), orderDetail.getWarehouseNum());
                    materialStock.setStockQuantity(result);
                    afterValue = result;
                }
                updateList.add(materialStock);
            }

            MaterialStockLog materialStockLog = new MaterialStockLog(order, orderDetail, material, beforeValue, orderDetail.getWarehouseNum(), afterValue);
            materialStockLog.setId(idGen.nextIdStr());
            materialStockLog.setCompanyCode(order.getCompanyCode());
            materialStockLog.setType("0");
            materialStockLogList.add(materialStockLog);
        }

        if(CollectionUtil.isNotEmpty(addList)){
            materialStockService.saveBatch(addList);
        }

        if(CollectionUtil.isNotEmpty(updateList)){
            materialStockService.updateBatchById(updateList);
        }

        if(CollectionUtil.isNotEmpty(materialStockLogList)){
            materialStockLogService.saveBatch(materialStockLogList);
        }
    }

}