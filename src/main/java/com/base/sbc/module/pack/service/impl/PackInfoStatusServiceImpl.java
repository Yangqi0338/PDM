/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.entity.PackInfoStatus;
import com.base.sbc.module.pack.mapper.PackInfoStatusMapper;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.service.PackInfoStatusService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * 类描述：资料包-状态 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackInfoStatusService
 * @email your email
 * @date 创建时间：2023-7-13 9:17:47
 */
@Service
public class PackInfoStatusServiceImpl extends PackBaseServiceImpl<PackInfoStatusMapper, PackInfoStatus> implements PackInfoStatusService {


// 自定义方法区 不替换的区域【other_start】

    @Resource
    private FlowableService flowableService;
    @Resource
    private PackInfoService packInfoService;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PackInfoStatus newStatus(String foreignId, String packType) {
        PackInfoStatus pack = new PackInfoStatus();
        pack.setForeignId(foreignId);
        pack.setPackType(packType);
        pack.setBomStatus(BasicNumber.ZERO.getNumber());
        save(pack);
        return pack;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean lockTechSpec(String foreignId, String packType) {
        PackInfoStatus packInfoStatus = get(foreignId, packType);
        packInfoStatus.setTechSpecLockFlag(BaseGlobal.YES);
        updateById(packInfoStatus);
        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean unlockTechSpec(String foreignId, String packType) {
        PackInfoStatus packInfoStatus = get(foreignId, packType);
        packInfoStatus.setTechSpecLockFlag(BaseGlobal.NO);
        updateById(packInfoStatus);
        return true;
    }

    @Override
    public boolean startApprovalForTechSpec(String foreignId, String packType) {
        PackInfoStatus packInfoStatus = get(foreignId, packType);
        PackInfo packInfo = packInfoService.getById(foreignId);
        if (packInfo == null) {
            throw new OtherException("资料包数据不存在,请先保存");
        }
        Map<String, Object> variables = BeanUtil.beanToMap(packInfoStatus);
        packInfoStatus.setTechSpecConfirmStatus(BaseGlobal.STOCK_STATUS_WAIT_CHECK);
        updateById(packInfoStatus);
        boolean flg = flowableService.start(FlowableService.pack_tech_pdn + "[" + packInfo.getCode() + "]",
                FlowableService.pack_tech_pdn,
                packInfoStatus.getId(),
                "/pdm/api/saas/packTechSpec/approval",
                "/pdm/api/saas/packTechSpec/approval",
                "/pdm/api/saas/packTechSpec/approval",
                StrUtil.format("/styleManagement/dataPackage?id={}&styleId={}&style={}", packInfo.getId(), packInfo.getForeignId(), packInfo.getDesignNo()),
                variables);
        return true;
    }

    @Override
    public boolean approvalForTechSpec(AnswerDto dto) {
        PackInfoStatus packInfoStatus = getById(dto.getBusinessKey());
        if (packInfoStatus != null) {
            packInfoStatus.setSizeConfirmSay(dto.getConfirmSay());
            //通过
            if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_PASS)) {
                packInfoStatus.setTechSpecConfirmStatus(BaseGlobal.STOCK_STATUS_CHECKED);
                packInfoStatus.setBulkProdTechConfirm(BaseGlobal.YES);
                packInfoStatus.setBulkProdTechConfirmDate(new Date());
            }
            //驳回
            else if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_REJECT)) {
                packInfoStatus.setTechSpecConfirmStatus(BaseGlobal.STOCK_STATUS_REJECT);
                packInfoStatus.setBulkProdTechConfirm(BaseGlobal.NO);
                packInfoStatus.setBulkProdTechConfirmDate(new Date());
            } else {
                packInfoStatus.setTechSpecConfirmStatus(BaseGlobal.STOCK_STATUS_DRAFT);
            }
            updateById(packInfoStatus);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean washSkippingFlagSetting(String foreignId, String packType, String washSkippingFlag) {
        PackInfoStatus packInfoStatus = get(foreignId, packType);
        packInfoStatus.setWashSkippingFlag(washSkippingFlag);
        return updateById(packInfoStatus);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean enableFlagSetting(String foreignId, String packType, String enableFlag) {
        PackInfoStatus packInfoStatus = get(foreignId, packType);
        packInfoStatus.setEnableFlag(enableFlag);
        return updateById(packInfoStatus);
    }

    @Override
    public boolean lockSize(String foreignId, String packType) {
        PackInfoStatus packInfoStatus = get(foreignId, packType);
        packInfoStatus.setSizeLockFlag(BaseGlobal.YES);
        updateById(packInfoStatus);
        return true;
    }

    @Override
    public boolean unlockSize(String foreignId, String packType) {
        PackInfoStatus packInfoStatus = get(foreignId, packType);
        packInfoStatus.setSizeLockFlag(BaseGlobal.NO);
        updateById(packInfoStatus);
        return true;
    }

    @Override
    public boolean startApprovalForSize(String foreignId, String packType) {
        PackInfoStatus packInfoStatus = get(foreignId, packType);
        PackInfo packInfo = packInfoService.getById(foreignId);
        if (packInfo == null) {
            throw new OtherException("资料包数据不存在,请先保存");
        }
        packInfoStatus.setSizeConfirmStatus(BaseGlobal.STOCK_STATUS_WAIT_CHECK);
        updateById(packInfoStatus);
        Map<String, Object> variables = BeanUtil.beanToMap(packInfoStatus);
        boolean flg = flowableService.start(FlowableService.pack_tech_pdn + "[" + packInfo.getCode() + "]",
                FlowableService.pack_tech_pdn,
                packInfoStatus.getId(),
                "/pdm/api/saas/packSize/approval",
                "/pdm/api/saas/packSize/approval",
                "/pdm/api/saas/packSize/approval",
                StrUtil.format("/styleManagement/dataPackage?id={}&styleId={}&style={}", packInfo.getId(), packInfo.getForeignId(), packInfo.getDesignNo()),
                variables);
        return true;
    }

    @Override
    public boolean approvalForSize(AnswerDto dto) {
        PackInfoStatus packInfoStatus = getById(dto.getBusinessKey());
        if (packInfoStatus != null) {
            packInfoStatus.setSizeConfirmSay(dto.getConfirmSay());
            //通过
            if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_PASS)) {
                packInfoStatus.setSizeConfirmStatus(BaseGlobal.STOCK_STATUS_CHECKED);

            }
            //驳回
            else if (StrUtil.equals(dto.getApprovalType(), BaseConstant.APPROVAL_REJECT)) {
                packInfoStatus.setSizeConfirmStatus(BaseGlobal.STOCK_STATUS_REJECT);
            } else {
                packInfoStatus.setSizeConfirmStatus(BaseGlobal.STOCK_STATUS_DRAFT);
            }
            updateById(packInfoStatus);
        }
        return true;
    }

    @Override
    public String getTechSpecFileIdByStyleNo(String styleNo) {
        return super.getBaseMapper().getTechSpecFileIdByStyleNo(styleNo);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PackInfoStatus get(String foreignId, String packType) {
        PackInfoStatus one = super.get(foreignId, packType);
        if (one != null) {
            return one;
        }
        return newStatus(foreignId, packType);
    }

    @Override
    String getModeName() {
        return "资料包";
    }

// 自定义方法区 不替换的区域【other_end】

}

