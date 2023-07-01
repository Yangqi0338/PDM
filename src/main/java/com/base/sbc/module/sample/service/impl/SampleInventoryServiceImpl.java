/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.sample.dto.SampleInventoryPageDto;
import com.base.sbc.module.sample.dto.SampleInventorySaveDto;
import com.base.sbc.module.sample.entity.SampleInventory;
import com.base.sbc.module.sample.entity.SampleInventoryItem;
import com.base.sbc.module.sample.mapper.SampleInventoryItemMapper;
import com.base.sbc.module.sample.mapper.SampleInventoryMapper;
import com.base.sbc.module.sample.mapper.SampleItemMapper;
import com.base.sbc.module.sample.service.SampleInventoryService;
import com.base.sbc.module.sample.service.SampleItemLogService;
import com.base.sbc.module.sample.service.SampleItemService;
import com.base.sbc.module.sample.vo.SampleInventoryItemVo;
import com.base.sbc.module.sample.vo.SampleInventoryVo;
import com.base.sbc.module.sample.vo.SampleSaleVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：样衣盘点 service类
 * @address com.base.sbc.module.sample.service.SampleInventoryServiceImpl
 */
@Service
public class SampleInventoryServiceImpl extends BaseServiceImpl<SampleInventoryMapper, SampleInventory> implements SampleInventoryService {
    @Autowired
    SampleInventoryMapper mapper;
    @Autowired
    SampleInventoryItemMapper sampleInventoryItemMapper;
    @Autowired
    SampleItemLogService sampleItemLogService;
    @Autowired
    SampleItemMapper sampleItemMapper;
    @Autowired
    SampleItemService sampleItemService;

    private IdGen idGen = new IdGen();

    @Override
    public SampleInventoryVo save(SampleInventorySaveDto dto) {
        String id = "";
        SampleInventory inventory = CopyUtil.copy(dto, SampleInventory.class);

        if (inventory != null){
            if (StringUtil.isEmpty(inventory.getId())) {
                inventory.setId(idGen.nextIdStr());
                inventory.setCode("PD" + System.currentTimeMillis() + (int)((Math.random() * 9 + 1) * 1000));

                id = inventory.getId();
            } else {
                id = inventory.getId();
            }

            Integer count = 0, borrowCount = 0;
            for (SampleInventoryItem item : dto.getSampleItemList()){
                // 处理明细
                // 新增
                if (StringUtil.isEmpty(item.getId())){
                    item.setId(idGen.nextIdStr());
                    item.setCompanyCode(getCompanyCode());
                    item.setSampleInventoryId(id);

                    sampleInventoryItemMapper.insert(item);
                    // 修改
                } else {

                }

                // 处理样衣
                sampleItemService.updateCount(item.getSampleItemId(), 5, item.getAllocateCount(), "", "");

                // 日志
                String remarks = "样衣盘点：id-" + item.getSampleItemId() + ", 盘点单号：" + inventory.getCode() + ", 数量：" + item.getAllocateCount();
                sampleItemLogService.save(item.getId(), 2, remarks);
            }

            if (StringUtil.isEmpty(dto.getId())) {
                inventory.setCompanyCode(getCompanyCode());
                mapper.insert(inventory);
            } else {
                mapper.updateById(inventory);
            }
        }

        return mapper.getDetail(id);
    }

    @Override
    public SampleInventoryVo getDetail(String id) {
        SampleInventoryVo vo = mapper.getDetail(id);

        QueryWrapper<SampleInventoryVo> qw = new QueryWrapper<>();
        qw.eq("sii.company_code", getCompanyCode());
        qw.eq("si2.id", id);
        List<SampleInventoryItemVo> list = sampleInventoryItemMapper.getList(qw);
        vo.setSampleItemList(list);

        return vo;
    }

    @Override
    public PageInfo queryPageInfo(SampleInventoryPageDto dto) {
        QueryWrapper<SampleInventoryVo> qw = new QueryWrapper<>();
        qw.eq("si2.company_code", getCompanyCode());
        if (null != dto.getStartDate())
            qw.ge("si2.start_date", dto.getStartDate());
        if (null != dto.getEndDate())
            qw.le("si2.end_date", dto.getEndDate());
        if (null != dto.getSearch())
            qw.like("si2.name", dto.getSearch()).
                or().like("si2.code", dto.getSearch());
        qw.orderByDesc("si2.create_date");

        Page<SampleInventoryVo> objects = PageHelper.startPage(dto);
        getBaseMapper().getList(qw);

        return objects.toPageInfo();
    }

    @Override
    public PageInfo getListBySampleItem(SampleInventoryPageDto dto) {
        QueryWrapper<SampleInventoryVo> qw = new QueryWrapper<>();
        qw.eq("si2.company_code", getCompanyCode());
        if (null != dto.getStatus())
            qw.eq("si2.status", dto.getStatus());
        qw.orderByDesc("si2.create_date");

        Page<SampleSaleVo> objects = PageHelper.startPage(dto);
        sampleInventoryItemMapper.getList(qw);

        return objects.toPageInfo();
    }
}