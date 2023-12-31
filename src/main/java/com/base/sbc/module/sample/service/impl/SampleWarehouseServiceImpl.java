/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import com.base.sbc.config.common.IdGen;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.sample.dto.SampleWarehousePageDto;
import com.base.sbc.module.sample.entity.SampleWarehouse;
import com.base.sbc.module.sample.mapper.SampleWarehouseMapper;
import com.base.sbc.module.sample.service.SampleWarehouseService;
import com.base.sbc.module.sample.vo.SampleWarehouseVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：样衣仓库 service类
 * @address com.base.sbc.module.sample.service.SampleWarehouseServiceImpl
 */
@Service
public class SampleWarehouseServiceImpl extends BaseServiceImpl<SampleWarehouseMapper, SampleWarehouse> implements SampleWarehouseService {
    @Autowired
    SampleWarehouseMapper mapper;

    private IdGen idGen = new IdGen();

    @Override
    public SampleWarehouseVo save(SampleWarehousePageDto dto) {
        return null;
    }

    @Override
    public SampleWarehouseVo getDetail(String id) {
        SampleWarehouseVo vo = mapper.getDetail(id);

        SampleWarehousePageDto dto = new SampleWarehousePageDto();
        dto.setId(id);
        List<SampleWarehouseVo> list = mapper.getList(dto);

        return list.get(0);
    }

    @Override
    public PageInfo queryPageInfo(SampleWarehousePageDto dto) {
        dto.setCompanyCode(getCompanyCode());

        Page<SampleWarehouseVo> objects = PageHelper.startPage(dto);
        getBaseMapper().getList(dto);

        return objects.toPageInfo();
    }
}