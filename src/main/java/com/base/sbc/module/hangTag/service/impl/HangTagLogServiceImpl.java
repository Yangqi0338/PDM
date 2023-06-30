/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.hangTag.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.enums.YesOrNoEnum;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.hangTag.entity.HangTagLog;
import com.base.sbc.module.hangTag.mapper.HangTagLogMapper;
import com.base.sbc.module.hangTag.service.HangTagLogService;
import com.base.sbc.module.hangTag.vo.HangTagLogVO;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：吊牌日志 service类
 *
 * @author xhj
 * @version 1.0
 * @address com.base.sbc.module.hangTag.service.HangTagLogService
 * @email ch.183.g1114@gmail.com
 * @date 创建时间：2023-6-26 17:15:57
 */
@Service
public class HangTagLogServiceImpl extends BaseServiceImpl<HangTagLogMapper, HangTagLog> implements HangTagLogService {
    // 自定义方法区 不替换的区域【other_start】
    @Override
    public void save(String hangTagId, String operationDescription, String userCompany) {
        HangTagLog hangTagLog = new HangTagLog();
        hangTagLog.insertInit();
        hangTagLog.setHangTagId(hangTagId);
        hangTagLog.setOperationDescription(operationDescription);
        hangTagLog.setCompanyCode(userCompany);
        super.save(hangTagLog);
    }

    @Override
    public void saveBatch(List<String> hangTagIds, String operationDescription, String userCompany) {
        List<HangTagLog> hangTagLogs = hangTagIds.stream()
                .map(hangTagId -> {
                    HangTagLog hangTagLog = new HangTagLog();
                    hangTagLog.insertInit();
                    hangTagLog.setHangTagId(hangTagId);
                    hangTagLog.setOperationDescription(operationDescription);
                    hangTagLog.setCompanyCode(userCompany);
                    return hangTagLog;
                }).collect(Collectors.toList());
        super.saveBatch(hangTagLogs);

    }

    @Override
    public List<HangTagLogVO> getByHangTagId(String hangTagId, String userCompany) {
        LambdaQueryWrapper<HangTagLog> queryWrapper = new QueryWrapper<HangTagLog>().lambda()
                .eq(HangTagLog::getHangTagId, hangTagId)
                .eq(HangTagLog::getCompanyCode, userCompany)
                .eq(HangTagLog::getDelFlag, YesOrNoEnum.NO.getValueStr())
                .orderByDesc(HangTagLog::getCreateDate);
        List<HangTagLog> hangTagLogs = super.list(queryWrapper);
        if (CollectionUtils.isEmpty(hangTagLogs)) {
            return Lists.newArrayList();
        }
        return hangTagLogs.stream()
                .map(e -> {
                    HangTagLogVO hangTagLogVO = new HangTagLogVO();
                    BeanUtils.copyProperties(e, hangTagLogVO);
                    return hangTagLogVO;
                }).collect(Collectors.toList());
    }


// 自定义方法区 不替换的区域【other_end】

}

