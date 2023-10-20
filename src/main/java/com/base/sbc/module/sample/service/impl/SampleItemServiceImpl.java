/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.sample.dto.SamplePageDto;
import com.base.sbc.module.sample.dto.SampleSearchDTO;
import com.base.sbc.module.sample.entity.Sample;
import com.base.sbc.module.sample.entity.SampleItem;
import com.base.sbc.module.sample.enums.SampleItemStatusEnum;
import com.base.sbc.module.sample.mapper.SampleItemMapper;
import com.base.sbc.module.sample.mapper.SampleMapper;
import com.base.sbc.module.sample.service.SampleItemService;
import com.base.sbc.module.sample.service.SampleService;
import com.base.sbc.module.sample.vo.SampleItemVO;
import com.base.sbc.module.sample.vo.SamplePageByItemVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 类描述：样衣明细 service类
 *
 * @address com.base.sbc.module.sample.service.SampleService
 */
@Service
public class SampleItemServiceImpl extends BaseServiceImpl<SampleItemMapper, SampleItem> implements SampleItemService {
    @Autowired
    SampleItemMapper mapper;
    @Autowired
    SampleMapper sampleMapper;
    @Autowired
    SampleService sampleService;

    private IdGen idGen = new IdGen();

    @Override
    @Deprecated
    public PageInfo queryPageInfo(SamplePageDto dto) {
        dto.setCompanyCode(getCompanyCode());

        Page<SamplePageByItemVo> objects = PageHelper.startPage(dto);
        getBaseMapper().getListByItemVo(dto);

        return objects.toPageInfo();
    }


    @Override
    public Boolean updateCount(String id, Integer type, Integer count, String toPositionId, String toPosition) {
        // 样衣明细表
        SampleItem si = mapper.selectById(id);
        // 样衣主表
        Sample sample = sampleMapper.selectById(si.getSampleId());
        // 标签
        Integer index = 0;

        if (type == 1) { //借
            if (si.getBorrowCount() + count <= si.getCount()) {
                si.setBorrowCount(si.getBorrowCount() + count);
                si.setStatus("2");
                index = mapper.updateById(si);

                sample.setBorrowCount(sample.getBorrowCount() + count);
                if (sample.getCount().equals(sample.getBorrowCount())) {
                    sample.setCompleteStatus(0);
                } else if (sample.getBorrowCount() < sample.getCount()) {
                    sample.setCompleteStatus(1);
                } else if (sample.getBorrowCount() == 0 && sample.getCount() > 0) {
                    sample.setCompleteStatus(2);
                }
                sampleMapper.updateById(sample);
            }
        } else if (type == 2) { // 还
            if (si.getBorrowCount() - count <= si.getCount()) {
                si.setBorrowCount(si.getBorrowCount() - count);
                si.setStatus("1");
                index = mapper.updateById(si);

                sample.setBorrowCount(sample.getBorrowCount() - count);
                if (sample.getCount().equals(sample.getBorrowCount())) {
                    sample.setCompleteStatus(0);
                } else if (sample.getBorrowCount() < sample.getCount()) {
                    sample.setCompleteStatus(1);
                } else if (sample.getBorrowCount() == 0 && sample.getCount() > 0) {
                    sample.setCompleteStatus(2);
                }
                sampleMapper.updateById(sample);
            }
        } else if (type == 3) { // 销售
            if (si.getCount() - count >= 0) {
                si.setCount(si.getCount() - count);
                si.setStatus("4");
                index = mapper.updateById(si);

                sample.setCount(sample.getCount() - count);
                sampleMapper.updateById(sample);
            }
        } else if (type == 4) {  // 调拨
            // 全调拨
            if (Objects.equals(si.getCount(), count) && si.getBorrowCount() == 0) {
                si.setPosition(toPosition);
                si.setPositionId(toPositionId);
                mapper.updateById(si);
                // 调拨部分
            } else {
                // 新对象
                SampleItem item = CopyUtil.copy(si, SampleItem.class);
                item.setId(idGen.nextIdStr());
                item.setCount(count);
                item.setBorrowCount(0);
                item.setPosition(toPosition);
                item.setPositionId(toPositionId);
                mapper.insert(item);

                // 老对象
                si.setCount(si.getCount() - count);
                mapper.updateById(si);
            }
        } else if (type == 5) { //盘点
            // 差额
            Integer differ = si.getCount() - count;
            si.setCount(count);
            si.setStatus("1");
            index = mapper.updateById(si);

            sample.setCount(sample.getCount() - differ);
            if (sample.getCount().equals(sample.getBorrowCount())) {
                sample.setCompleteStatus(0);
            } else if (sample.getBorrowCount() < sample.getCount()) {
                sample.setCompleteStatus(1);
            } else if (sample.getBorrowCount() == 0 && sample.getCount() > 0) {
                sample.setCompleteStatus(2);
            }
            sampleMapper.updateById(sample);
        }

        return index > 0;
    }

    @Override
    public List<SampleItemVO> getSampleItemList(SampleSearchDTO dto) {
        return mapper.getSampleItemList(dto);
    }

    @Override
    public List<SampleItem> getListBySampleId(SamplePageDto dto) {
        return mapper.getListBySampleId(dto);
    }

    @Override
    public void sampleReturnUpdateByIds(List<String> ids) {
        List<SampleItem> sampleItems = ids.stream()
                .map(id -> {
                    SampleItem sampleItem = new SampleItem();
                    sampleItem.setId(id);
                    sampleItem.setBorrowCount(0);
                    sampleItem.setStatus(SampleItemStatusEnum.IN_LIBRARY.getK());
                    return sampleItem;
                }).collect(Collectors.toList());
        super.updateBatchById(sampleItems);
    }

    @Override
    public void checkSampleStatus(List<String> ids, List<String> checkStatus) {
        QueryWrapper<SampleItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        queryWrapper.select("status");
        List<SampleItem> sampleItems = mapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(sampleItems)) {
            throw new OtherException("样衣获取为空");
        }
        boolean b = sampleItems.stream().anyMatch(e -> !checkStatus.contains(e.getStatus()));
        if (b) {
            throw new OtherException("样衣状态不正确");
        }
    }

    @Override
    public void enableOrDeactivate(String id, String enableFlag) {
        SampleItem sampleItem = mapper.selectById(id);
        if (Objects.isNull(sampleItem)) {
            throw new OtherException("样衣获取为空");
        }
        if (!SampleItemStatusEnum.IN_LIBRARY.getK().equals(sampleItem.getStatus())) {
            throw new OtherException("该样衣未在库，不可操作");
        }
        SampleItem updateSampleItem = new SampleItem();
        updateSampleItem.updateInit();
        updateSampleItem.setEnableFlag(enableFlag);
        updateSampleItem.setId(id);
        mapper.updateById(updateSampleItem);
    }
}

