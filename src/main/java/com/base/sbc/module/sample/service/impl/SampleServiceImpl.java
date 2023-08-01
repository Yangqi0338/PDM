/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.module.basicsdatum.dto.ColorModelNumberExcelDto;
import com.base.sbc.module.basicsdatum.entity.ColorModelNumber;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.patternmaking.mapper.PatternMakingMapper;
import com.base.sbc.module.sample.dto.SamplePageDto;
import com.base.sbc.module.sample.dto.SampleSaveDto;
import com.base.sbc.module.sample.dto.SampleSearchDTO;
import com.base.sbc.module.sample.entity.Sample;
import com.base.sbc.module.sample.entity.SampleDesign;
import com.base.sbc.module.sample.entity.SampleItem;
import com.base.sbc.module.sample.mapper.SampleDesignMapper;
import com.base.sbc.module.sample.mapper.SampleItemMapper;
import com.base.sbc.module.sample.mapper.SampleMapper;
import com.base.sbc.module.sample.service.SampleItemLogService;
import com.base.sbc.module.sample.service.SampleService;
import com.base.sbc.module.sample.vo.SampleItemVO;
import com.base.sbc.module.sample.vo.SamplePageByDesignNoVo;
import com.base.sbc.module.sample.vo.SampleVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 类描述：样衣 service类
 *
 * @address com.base.sbc.module.sample.service.SampleService
 */
@Service
public class SampleServiceImpl extends BaseServiceImpl<SampleMapper, Sample> implements SampleService {
    @Autowired
    SampleMapper mapper;
    @Autowired
    PatternMakingMapper patternMakingMapper;
    @Autowired
    SampleDesignMapper sampleDesignMapper;
    @Autowired
    SampleItemMapper sampleItemMapper;
    @Autowired
    SampleItemLogService sampleItemLogService;

    private IdGen idGen = new IdGen();

    @Override
    public SampleVo save(SampleSaveDto dto) {
        String id = "";

        // 获取制版单
        PatternMaking pm = patternMakingMapper.selectById(dto.getPatternMakingId());
        // 获取样衣设计
        SampleDesign sd = sampleDesignMapper.selectById(pm.getSampleDesignId());
        if (pm != null && sd != null) {
            Sample sample = CopyUtil.copy(dto, Sample.class);

            if (StringUtil.isEmpty(sample.getId())) {
                sample.setId(idGen.nextIdStr());
                id = sample.getId();
            } else {
                id = dto.getId();
            }

            sample.setPatternMakingId(pm.getId());
            sample.setPatternMakingCode(pm.getCode());
            sample.setSampleType(pm.getSampleType());
            sample.setStyleName(sd.getStyleName());
            sample.setDesignNo(sd.getDesignNo());
            sample.setSampleDesignId(pm.getSampleDesignId());
            sample.setProdCategory(sd.getProdCategory());
            sample.setProdCategory1st(sd.getProdCategory1st());
            sample.setProdCategory2nd(sd.getProdCategory2nd());
            sample.setProdCategory3rd(sd.getProdCategory3rd());

            sample.setProdCategoryName(sd.getProdCategoryName());
            sample.setProdCategory1stName(sd.getProdCategory1stName());
            sample.setProdCategory2ndName(sd.getProdCategory2ndName());
            sample.setProdCategory3rdName(sd.getProdCategory3rdName());

            sample.setPatternDesignId(pm.getPatternDesignId());
            sample.setPatternDesignName(pm.getPatternDesignName());
            sample.setCompleteStatus(2); //库存状态：0-完全借出，1-部分借出，2-全部在库
            sample.setExamineStatus(0);  //审核状态：0-草稿，1-待审核、2-审核通过、3-驳回

            for (SampleItem item : dto.getSampleItemList()) {
                item.setCount(1);
                if (StringUtil.isEmpty(item.getId())) {
                    item.setId(idGen.nextIdStr());
                    item.setSampleId(sample.getId());
                    item.setBorrowCount(0);
                    item.setCode("YY" + System.currentTimeMillis() + (int) ((Math.random() * 9 + 1) * 1000));
                    sampleItemMapper.insert(item);

                    // 保存样衣操作日志
                    sampleItemLogService.save(item.getId(), 1, "新增样衣明细：id-" + item.getId());
                } else {
                    SampleItem si = sampleItemMapper.selectById(item.getId());
                    String beforRemark = "状态：" + si.getStatus() + "颜色：" + si.getColor() + "尺码：" + si.getSize()
                            + "价格：" + si.getPrice() + "位置：" + si.getPosition() + "备注：" + si.getRemarks();

                    sampleItemMapper.updateById(item);

                    String afterRemark = "状态：" + item.getStatus() + "，颜色：" + item.getColor() + "，尺码：" + item.getSize()
                            + "，价格：" + item.getPrice() + "，位置：" + item.getPosition() + "，备注：" + item.getRemarks();

                    String remarks = "更新样衣明细：id-" + item.getId() + "，变更前：【" + beforRemark + "】，变更后：【" + afterRemark + "】";
                    sampleItemLogService.save(item.getId(), 2, remarks);
                }
            }
            sample.setCount(CollectionUtils.isEmpty(dto.getSampleItemList()) ? 0 : dto.getSampleItemList().size());
            sample.setBorrowCount(0);
            if (StringUtil.isEmpty(dto.getId())) {
                mapper.insert(sample);
            } else {
                mapper.updateById(sample);
            }
        }

        return mapper.getDetail(id);
    }

    @Override
    public SampleVo getDetail(String id) {
        SampleVo vo = mapper.getDetail(id);

        SamplePageDto dto = new SamplePageDto();
        dto.setSampleId(id);
        List<SampleItem> list = sampleItemMapper.getListBySampleId(dto);
        vo.setSampleItemList(list);

        return vo;
    }

    @Override
    public PageInfo queryPageInfo(SamplePageDto dto) {
        dto.setCompanyCode(getCompanyCode());

        Page<SamplePageByDesignNoVo> objects = PageHelper.startPage(dto);
        getBaseMapper().getListByDesignNo(dto);

        return objects.toPageInfo();
    }

    @Override
    public Boolean importExcel(MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String[] split = originalFilename.split("\\.");
        ImportParams params = new ImportParams();
        params.setNeedSave(false);

        List<ColorModelNumberExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), ColorModelNumberExcelDto.class, params);

        List<ColorModelNumber> colorModelNumbers = BeanUtil.copyToList(list, ColorModelNumber.class);
        for (ColorModelNumber colorModelNumber : colorModelNumbers) {
            colorModelNumber.setFileName(split[0]);
            colorModelNumber.setStatus("1");
            QueryWrapper<ColorModelNumber> queryWrapper = new BaseQueryWrapper<>();
            queryWrapper.eq("code", colorModelNumber.getCode());
            // this.saveOrUpdate(colorModelNumber,queryWrapper);
        }
        return true;
    }

    @Override
    public SampleVo updateStatus(SampleSaveDto dto) {
        Sample s = mapper.selectById(dto.getId());
        s.setStatus(dto.getStatus());
        mapper.updateById(s);

        SampleVo vo = mapper.getDetail(dto.getId());
        SamplePageDto dto2 = new SamplePageDto();
        dto2.setSampleId(dto.getId());
        List<SampleItem> list = sampleItemMapper.getListBySampleId(dto2);
        vo.setSampleItemList(list);

        return vo;
    }

    @Override
    public PageInfo<SampleItemVO> getSampleItemList(SampleSearchDTO dto) {
        PageHelper.startPage(dto);
        List<SampleItemVO> sampleItemList = sampleItemMapper.getSampleItemList(dto);
        return new PageInfo<>(sampleItemList);
    }
}