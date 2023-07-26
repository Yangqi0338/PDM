/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pricing.service.impl;

import com.base.sbc.config.utils.BigDecimalUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.pack.entity.PackPricingOtherCosts;
import com.base.sbc.module.pack.service.PackBomService;
import com.base.sbc.module.pack.service.PackPricingOtherCostsService;
import com.base.sbc.module.pack.vo.PackBomCalculateBaseVo;
import com.base.sbc.module.pricing.dto.StylePricingSearchDTO;
import com.base.sbc.module.pricing.entity.StylePricing;
import com.base.sbc.module.pricing.mapper.StylePricingMapper;
import com.base.sbc.module.pricing.service.StylePricingService;
import com.base.sbc.module.pricing.vo.StylePricingVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 类描述：款式定价 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pricing.service.StylePricingService
 * @email your email
 * @date 创建时间：2023-7-20 11:10:33
 */
@Service
public class StylePricingServiceImpl extends BaseServiceImpl<StylePricingMapper, StylePricing> implements StylePricingService {
    @Autowired
    private PackPricingOtherCostsService packPricingOtherCostsService;
    @Autowired
    private PackBomService packBomService;

    @Override
    public PageInfo<StylePricingVO> getPage(StylePricingSearchDTO dto) {
        com.github.pagehelper.Page<StylePricingVO> page = PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        List<StylePricingVO> stylePricingList = super.getBaseMapper().getStylePricingList(dto);
        if (CollectionUtils.isEmpty(stylePricingList)) {
            return page.toPageInfo();
        }

        List<String> packId = stylePricingList.stream()
                .map(StylePricingVO::getId)
                .collect(Collectors.toList());
        Map<String, BigDecimal> otherCostsMap = this.getOtherCosts(packId, dto.getCompanyCode());
        Map<String, List<PackBomCalculateBaseVo>> packBomCalculateBaseVoS = this.getPackBomCalculateBaseVoS(packId);


        stylePricingList.forEach(stylePricingVO -> {
            List<PackBomCalculateBaseVo> packBomCalculateBaseVos = packBomCalculateBaseVoS.get(stylePricingVO.getId() + stylePricingVO.getPackType());
            stylePricingVO.setMaterialCost(this.getMaterialCost(packBomCalculateBaseVos));
            stylePricingVO.setTotalCost(this.getMaterialAmount(packBomCalculateBaseVos));
            stylePricingVO.setPackagingFee(BigDecimalUtil.convertBigDecimal(otherCostsMap.get(stylePricingVO.getId() + "包装费")));
            stylePricingVO.setTestingFee(BigDecimalUtil.convertBigDecimal(otherCostsMap.get(stylePricingVO.getId() + "检测费")));
            stylePricingVO.setSewingProcessingFee(BigDecimalUtil.convertBigDecimal(otherCostsMap.get(stylePricingVO.getId() + "车缝加工费")));
            stylePricingVO.setWoolenYarnProcessingFee(BigDecimalUtil.convertBigDecimal(otherCostsMap.get(stylePricingVO.getId() + "毛纱加工费")));
            stylePricingVO.setCoordinationProcessingFee(BigDecimalUtil.convertBigDecimal(otherCostsMap.get(stylePricingVO.getId() + "外协加工费")));
            stylePricingVO.setExpectedSalesPrice(this.getExpectedSalesPrice(stylePricingVO.getPlanningRatio(), stylePricingVO.getTotalCost()));
            stylePricingVO.setPlanCost(this.getPlanCost(packBomCalculateBaseVos));
            //计控实际倍率 = 吊牌价/计控实际成本
            stylePricingVO.setPlanActualMagnification(BigDecimalUtil.div(stylePricingVO.getTagPrice(), stylePricingVO.getPlanCost()));
            //实际倍率 = 吊牌价/总成本
            stylePricingVO.setPlanActualMagnification(BigDecimalUtil.div(stylePricingVO.getTagPrice(), stylePricingVO.getTotalCost()));
        });
        return new PageInfo<>(stylePricingList);
    }

    @Override
    public void insertOrUpdate() {

    }

    @Override
    public void confirm() {

    }

    /**
     * 获取预计销售价 企划倍率*总成本（如果无，则直接是总成本）
     *
     * @param planningRatio
     * @param totalCost
     * @return
     */
    private BigDecimal getExpectedSalesPrice(BigDecimal planningRatio, BigDecimal totalCost) {
        if (Objects.isNull(planningRatio)) {
            return totalCost;
        }
        return BigDecimalUtil.mul(4, planningRatio, totalCost);
    }

    /**
     * 获取物料总金额
     *
     * @param packBomCalculateBaseVos
     * @return
     */
    private BigDecimal getMaterialAmount(List<PackBomCalculateBaseVo> packBomCalculateBaseVos) {
        if (CollectionUtils.isEmpty(packBomCalculateBaseVos)) {
            return BigDecimal.ZERO;
        }
        return packBomCalculateBaseVos.stream()
                .map(PackBomCalculateBaseVo::getAmount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

    }

    /**
     * 获取计控实际成本
     * 物料大货用量*物料大货单价*（1+损耗率) / (1+单价税点)
     *
     * @param list
     * @return
     */
    private BigDecimal getPlanCost(List<PackBomCalculateBaseVo> list) {
        if (CollectionUtils.isEmpty(list)) {
            return BigDecimal.ZERO;
        }
        return list.stream()
                .map(packBom -> {
                    BigDecimal lossRate = BigDecimalUtil.add(BigDecimal.ONE, BigDecimalUtil.div(packBom.getLossRate(), BigDecimal.valueOf(100), 2), 2);
                    BigDecimal priceTax = BigDecimalUtil.add(BigDecimal.ONE, BigDecimalUtil.div(packBom.getPriceTax(), BigDecimal.valueOf(100), 2), 2);
                    BigDecimal cost = BigDecimalUtil.mul(2, packBom.getBulkUnitUse(), packBom.getBulkPrice(), lossRate);
                    return BigDecimalUtil.div(cost, priceTax);
                })
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    /**
     * 获取物料费用 物料大货用量*物料大货单价*（1+损耗率)
     *
     * @param list
     * @return
     */
    private BigDecimal getMaterialCost(List<PackBomCalculateBaseVo> list) {
        if (CollectionUtils.isEmpty(list)) {
            return BigDecimal.ZERO;
        }
        return list.stream()
                .map(packBom -> {
                    BigDecimal lossRate = BigDecimalUtil.add(BigDecimal.ONE, BigDecimalUtil.div(packBom.getLossRate(), BigDecimal.valueOf(100), 2), 2);
                    return BigDecimalUtil.mul(2, packBom.getBulkUnitUse(), packBom.getBulkPrice(), lossRate);
                })
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private Map<String, List<PackBomCalculateBaseVo>> getPackBomCalculateBaseVoS(List<String> packId) {
        List<PackBomCalculateBaseVo> packBomCalculateBaseVo = packBomService.getPackBomCalculateBaseVo(packId);
        if (CollectionUtils.isEmpty(packBomCalculateBaseVo)) {
            return new HashMap<>();
        }
        return packBomCalculateBaseVo.stream()
                .collect(Collectors.groupingBy(e -> e.getForeignId() + e.getPackType()));
    }


    /**
     * 获取其他费用
     *
     * @param packId
     * @param companyCode
     * @return
     */
    private Map<String, BigDecimal> getOtherCosts(List<String> packId, String companyCode) {
        List<PackPricingOtherCosts> packPricingOtherCosts = packPricingOtherCostsService.getPriceSumByForeignIds(packId, companyCode);
        if (CollectionUtils.isEmpty(packPricingOtherCosts)) {
            return new HashMap<>();
        }
        return packPricingOtherCosts.stream()
                .collect(Collectors.toMap(e -> e.getForeignId() + e.getCostsItem(), PackPricingOtherCosts::getPrice, (k1, k2) -> k1));

    }

// 自定义方法区 不替换的区域【other_start】


// 自定义方法区 不替换的区域【other_end】

}
