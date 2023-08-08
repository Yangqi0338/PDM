/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.sample.dto.QueryDetailFabricDto;
import com.base.sbc.module.sample.dto.QueryFabricInformationDto;
import com.base.sbc.module.sample.dto.SaveUpdateFabricBasicInformationDto;
import com.base.sbc.module.sample.entity.FabricBasicInformation;
import com.base.sbc.module.sample.entity.FabricDetailedInformation;
import com.base.sbc.module.sample.mapper.FabricBasicInformationMapper;
import com.base.sbc.module.sample.mapper.FabricDetailedInformationMapper;
import com.base.sbc.module.sample.service.FabricBasicInformationService;
import com.base.sbc.module.sample.vo.FabricInformationVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

/**
 * 类描述：面料基本信息 service类
 * @address com.base.sbc.module.sample.service.FabricBasicInformationService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-19 18:23:26
 * @version 1.0  
 */
@Service
public class FabricBasicInformationServiceImpl extends BaseServiceImpl<FabricBasicInformationMapper, FabricBasicInformation> implements FabricBasicInformationService {

    @Autowired
    private BaseController baseController;

    @Autowired
    private FabricDetailedInformationMapper fabricDetailedInformationMapper;



    @Override
    public PageInfo<FabricInformationVo> getFabricInformationList(QueryFabricInformationDto queryFabricInformationDto) {
      if(queryFabricInformationDto.getPageNum() !=0 && queryFabricInformationDto.getPageSize()!=0){
          PageHelper.startPage(queryFabricInformationDto);
      }
        QueryWrapper queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(queryFabricInformationDto.getOriginate())){
            if(queryFabricInformationDto.getOriginate().equals("0")){
                queryWrapper.eq("tfbi.create_id", baseController.getUserId());
            }
        }
        queryWrapper.eq("tfbi.company_code",baseController.getUserCompany());
        queryWrapper.orderByAsc("tfbi.create_date");
        List<FabricInformationVo> list = baseMapper.getFabricInformationList(queryWrapper);
        return new PageInfo<>(list);
    }

    @Override
    @Transactional(readOnly = false)
    public ApiResult saveUpdateFabricBasic(SaveUpdateFabricBasicInformationDto saveUpdateFabricBasicDto) {
        FabricBasicInformation fabricBasicInformation = new FabricBasicInformation();
        if (StringUtils.isNotBlank(saveUpdateFabricBasicDto.getId())) {
            /*调整*/
            fabricBasicInformation=baseMapper.selectById(saveUpdateFabricBasicDto.getId());
            BeanUtils.copyProperties(saveUpdateFabricBasicDto,fabricBasicInformation );
            fabricBasicInformation.updateInit();
            baseMapper.updateById(fabricBasicInformation);
        } else {
            /*新增*/
            BeanUtils.copyProperties(saveUpdateFabricBasicDto,fabricBasicInformation );
            fabricBasicInformation.setCompanyCode(baseController.getUserCompany());
            fabricBasicInformation.setRegisterDate(new Date());
            baseMapper.insert(fabricBasicInformation);
        }
        return ApiResult.success("操作成功");
    }

    @Override
    public ApiResult delFabric(String id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("basic_information_id",id);
        FabricDetailedInformation fabricDetailedInformation = fabricDetailedInformationMapper.selectOne(queryWrapper);
        if(!ObjectUtils.isEmpty(fabricDetailedInformation)){
            throw new OtherException("该单存在面料详情无法删除");
        }
        baseMapper.deleteById(id);
        return ApiResult.success("操作成功");
    }

    @Override
    public ApiResult getById(QueryDetailFabricDto queryDetailFabricDto) {
        FabricBasicInformation fabricBasicInformation=   baseMapper.selectById(queryDetailFabricDto.getId());

        FabricInformationVo fabricInformationVo=new FabricInformationVo();
        BeanUtils.copyProperties(fabricBasicInformation,fabricInformationVo );
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("basic_information_id",fabricBasicInformation.getId());
        /*获取面料详情信息*/
        FabricDetailedInformation fabricDetailedInformation = fabricDetailedInformationMapper.selectOne(queryWrapper);
        if(!ObjectUtils.isEmpty(fabricDetailedInformation)){
            BeanUtils.copyProperties(fabricDetailedInformation, fabricInformationVo);
        }
        return ApiResult.success("查询成功",fabricInformationVo);
    }

/** 自定义方法区 不替换的区域【other_start】 **/



/** 自定义方法区 不替换的区域【other_end】 **/
	
}
