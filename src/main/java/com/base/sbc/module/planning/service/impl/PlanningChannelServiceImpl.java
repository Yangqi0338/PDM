/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.planning.dto.PlanningChannelDto;
import com.base.sbc.module.planning.dto.PlanningChannelSearchDto;
import com.base.sbc.module.planning.entity.PlanningChannel;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.mapper.PlanningChannelMapper;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import com.base.sbc.module.planning.service.PlanningChannelService;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.planning.vo.PlanningChannelVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：企划-渠道 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.planning.service.PlanningChannelService
 * @email your email
 * @date 创建时间：2023-7-21 16:00:35
 */
@Service
public class PlanningChannelServiceImpl extends BaseServiceImpl<PlanningChannelMapper, PlanningChannel> implements PlanningChannelService {


// 自定义方法区 不替换的区域【other_start】

    @Autowired
    PlanningSeasonService planningSeasonService;
    @Autowired
    PlanningCategoryItemService planningCategoryItemService;
    @Autowired
    AmcFeignService amcFeignService;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PlanningChannelVo saveByDto(PlanningChannelDto dto) {
//        checkedRepeat(dto);
        if (CommonUtils.isInitId(dto.getId())) {
            PlanningSeason planningSeason = planningSeasonService.getById(dto.getPlanningSeasonId());
            if (planningSeason == null) {
                throw new OtherException("产品季信息为空");
            }
            PlanningChannel planningChannel = BeanUtil.copyProperties(dto, PlanningChannel.class);
            BeanUtil.copyProperties(planningSeason, planningChannel);
            CommonUtils.resetCreateUpdate(planningChannel);
            planningChannel.setId(null);
            save(planningChannel);
            return BeanUtil.copyProperties(planningChannel, PlanningChannelVo.class);
        } else {
            PlanningChannel planningChannel = getById(dto.getId());
            if (planningChannel == null) {
                throw new OtherException(BaseErrorEnum.ERR_UPDATE_DATA_NOT_FOUND);
            }
            planningChannel.setChannel(dto.getChannel());
            planningChannel.setChannelName(dto.getChannelName());
            planningChannel.setSex(dto.getSex());
            planningChannel.setSexName(dto.getSexName());
            updateById(planningChannel);
            return BeanUtil.copyProperties(planningChannel, PlanningChannelVo.class);
        }
    }

    @Override
    public void checkedRepeat(PlanningChannelDto dto) {
        QueryWrapper<PlanningChannel> qw = new QueryWrapper();
        qw.lambda().eq(PlanningChannel::getPlanningSeasonId, dto.getPlanningSeasonId())
                .eq(PlanningChannel::getChannel, dto.getChannel())
                .eq(PlanningChannel::getSex, dto.getSex())
        ;
        if (!CommonUtils.isInitId(dto.getId())) {
            qw.ne("id", dto.getId());
        }
        long count = count(qw);
        if (count > 0) {
            throw new OtherException(BaseErrorEnum.ERR_INSERT_DATA_REPEAT);
        }
    }

    @Override
    public PageInfo<PlanningChannelVo> channelPageInfo(PlanningChannelSearchDto dto) {
        BaseQueryWrapper<PlanningChannel> qw = new BaseQueryWrapper<>();
        qw.lambda().eq(StrUtil.isNotBlank(dto.getPlanningSeasonId()), PlanningChannel::getPlanningSeasonId, dto.getPlanningSeasonId());
        qw.orderByDesc("id");
        Page<PlanningChannel> page = PageHelper.startPage(dto);
        list(qw);
        PageInfo<PlanningChannelVo> pageInfo = CopyUtil.copy(page.toPageInfo(), PlanningChannelVo.class);
        List<PlanningChannelVo> list = pageInfo.getList();
        if (CollUtil.isNotEmpty(list)) {
            amcFeignService.setUserAvatarToList(list);
            //统计skc
            List<String> channelIds = list.stream().map(PlanningChannelVo::getId).collect(Collectors.toList());
            Map<String, Long> channelTotal = planningCategoryItemService.totalSkcByChannel(channelIds);
            for (PlanningChannelVo planningChannelVo : list) {
                planningChannelVo.setSkcCount(channelTotal.getOrDefault(planningChannelVo.getId(), 0L));
            }
        }


        return pageInfo;
    }

// 自定义方法区 不替换的区域【other_end】

}
