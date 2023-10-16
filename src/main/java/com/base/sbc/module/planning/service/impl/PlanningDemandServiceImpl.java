/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.planning.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.formType.dto.QueryFieldManagementDto;
import com.base.sbc.module.formType.entity.FieldManagement;
import com.base.sbc.module.formType.entity.FieldOptionConfig;
import com.base.sbc.module.formType.entity.FormType;
import com.base.sbc.module.formType.mapper.FieldManagementMapper;
import com.base.sbc.module.formType.mapper.FieldOptionConfigMapper;
import com.base.sbc.module.formType.mapper.FormTypeMapper;
import com.base.sbc.module.formType.vo.FieldManagementVo;
import com.base.sbc.module.formType.vo.FieldOptionConfigVo;
import com.base.sbc.module.planning.dto.PlanningBoardSearchDto;
import com.base.sbc.module.planning.dto.QueryDemandDto;
import com.base.sbc.module.planning.dto.SaveDelDemandDto;
import com.base.sbc.module.planning.entity.PlanningDemand;
import com.base.sbc.module.planning.entity.PlanningDemandProportionData;
import com.base.sbc.module.planning.entity.PlanningDemandProportionSeat;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.mapper.PlanningDemandMapper;
import com.base.sbc.module.planning.mapper.PlanningDemandProportionDataMapper;
import com.base.sbc.module.planning.mapper.PlanningSeasonMapper;
import com.base.sbc.module.planning.service.PlanningDemandService;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.planning.utils.PlanningUtils;
import com.base.sbc.module.planning.vo.PlanningDemandProportionDataVo;
import com.base.sbc.module.planning.vo.PlanningDemandVo;
import com.base.sbc.module.planning.vo.PlanningSummaryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：企划-需求维度表 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.planning.service.PlanningDemandDimensionalityService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-26 17:42:18
 */
@Service
public class PlanningDemandServiceImpl extends BaseServiceImpl<PlanningDemandMapper, PlanningDemand> implements PlanningDemandService {
    @Autowired
    private BaseController baseController;
    @Autowired
    private FieldManagementMapper fieldManagementMapper;
    @Autowired
    private FormTypeMapper formTypeMapper;
    @Autowired
    private PlanningDemandProportionDataMapper planningDemandProportionDataMapper;

    @Autowired
    private FieldOptionConfigMapper fieldOptionConfigMapper;
    @Autowired
    private PlanningSeasonMapper planningSeasonMapper;
    @Autowired
    private PlanningSeasonService planningSeasonService;

    @Override
    public List<PlanningDemandVo> getDemandListById(Principal user, QueryDemandDto queryDemandDimensionalityDto) {
        BaseQueryWrapper queryWrapper = new BaseQueryWrapper<>();
        if (StrUtil.isNotBlank(queryDemandDimensionalityDto.getProdCategory2nd())) {
            queryWrapper.eq("prod_category2nd", queryDemandDimensionalityDto.getProdCategory2nd());
        } else {
            queryWrapper.isNull("prod_category2nd");
            queryWrapper.eq("prod_category", queryDemandDimensionalityDto.getProdCategory());
        }
        queryWrapper.eq("channel", queryDemandDimensionalityDto.getChannel());
        queryWrapper.eq("planning_season_id", queryDemandDimensionalityDto.getPlanningSeasonId());
        queryWrapper.eq("company_code", baseController.getUserCompany());
        queryWrapper.eq("del_flag", BaseGlobal.DEL_FLAG_NORMAL);
        queryWrapper.eq(StrUtil.isNotBlank(queryDemandDimensionalityDto.getFieldId()), "field_id", queryDemandDimensionalityDto.getFieldId());
        /*选中的需求*/
        List<PlanningDemandVo> list = BeanUtil.copyToList(baseMapper.selectList(queryWrapper), PlanningDemandVo.class);
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        List<String> stringList1 = list.stream().map(PlanningDemandVo::getFieldId).collect(Collectors.toList());
        List<FieldManagementVo> fieldManagementVoList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(stringList1)){
            /*查询表单数据*/
            QueryFieldManagementDto queryFieldManagementDto = new QueryFieldManagementDto();
            queryFieldManagementDto.setCompanyCode(baseController.getUserCompany());
            queryFieldManagementDto.setIds(stringList1);
            /*查询所有关联的字段*/
            fieldManagementVoList = fieldManagementMapper.getFieldManagementList(queryFieldManagementDto);
        }

        Map<String, List<FieldManagementVo>> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(fieldManagementVoList)) {
            map = fieldManagementVoList.stream().collect(Collectors.groupingBy(FieldManagementVo::getId));
        }
        /*查询出这字段下的所有配置选项*/
        PlanningSeason season = planningSeasonMapper.selectById(queryDemandDimensionalityDto.getPlanningSeasonId());
        List<String> stringList2 = list.stream().map(PlanningDemandVo::getFieldId).collect(Collectors.toList());
        queryWrapper.clear();
        queryWrapper.in("field_management_id", stringList2);
        queryWrapper.eq("category_code", queryDemandDimensionalityDto.getProdCategory());
        queryWrapper.eq("season", season.getSeason());
        queryWrapper.like("brand",season.getBrand());
        List<FieldOptionConfig> optionConfigList =  fieldOptionConfigMapper.selectList(queryWrapper);

        /*使用字段id分组*/
        Map<String, List<FieldOptionConfig>> listMap = optionConfigList.stream().collect(Collectors.groupingBy(FieldOptionConfig::getFieldManagementId));
        for (PlanningDemandVo p : list) {
            List<FieldOptionConfig> configList =  listMap.get(p.getFieldId());
            if(!CollectionUtils.isEmpty(configList)){
                p.setConfigVoList( BeanUtil.copyToList(configList, FieldOptionConfigVo.class));
            }
            List<FieldManagementVo> list1 =  map.get(p.getFieldId());
            if(!CollectionUtils.isEmpty(list1)){
                p.setFieldManagementVo(list1.get(0));
            }
            /*查询需求数据*/
            QueryWrapper<PlanningDemandProportionData> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("demand_id", p.getId());
            List<PlanningDemandProportionDataVo> demandDimensionalityDataList = BeanUtil.copyToList(planningDemandProportionDataMapper.selectList(queryWrapper1), PlanningDemandProportionDataVo.class);
            p.setList(demandDimensionalityDataList);
            /*禁用下拉框已选中的值*/
            list1.forEach(fieldManagementVo -> {
                if (fieldManagementVo.getFieldTypeCoding().equals("select")) {
                    List<String> stringList = p.getList().stream().map(PlanningDemandProportionData::getClassify).collect(Collectors.toList());
                    fieldManagementVo.getOptionList().forEach(option -> {
                        if (stringList.contains(option.getOptionName())) {
                            option.setDisabled(true);
                        }
                    });
                }
            });
            //查询下单数、下单占比、缺口
            if (CollUtil.isNotEmpty(demandDimensionalityDataList) && StrUtil.isNotBlank(queryDemandDimensionalityDto.getOrderInfo())) {
                PlanningBoardSearchDto dto = BeanUtil.copyProperties(queryDemandDimensionalityDto, PlanningBoardSearchDto.class);
                dto.setFieldId(p.getFieldId());
                PlanningSummaryVo planningSummaryVo = planningSeasonService.planningSummary(user, dto, CollUtil.newArrayList(p));
                Map<String, List<PlanningDemandProportionSeat>> pdpsMap = planningSummaryVo.getSeatList().stream().collect(Collectors.groupingBy(PlanningDemandProportionSeat::getPlanningDemandProportionDataId));
                //总下单数
                Long orderTotalNum = Opt.ofNullable(planningSummaryVo.getSeatList())
                        .map(a -> a.stream().filter(b -> StrUtil.equalsAny(b.getMatchFlag(), "a1", "b2")).count())
                        .orElse(0L);
                for (PlanningDemandProportionDataVo pdpd : demandDimensionalityDataList) {
                    //下单数
                    Long orderNum = Opt.ofNullable(pdpsMap.get(pdpd.getId()))
                            .map(a -> a.stream().filter(b -> StrUtil.equalsAny(b.getMatchFlag(), "a1", "b2")).count())
                            .orElse(0L);
                    pdpd.setOrderNum(orderNum);
                    //下单占比
                    pdpd.setOrderRatio(orderTotalNum == 0 ? BigDecimal.ZERO : NumberUtil.div(orderNum, orderTotalNum));
                    // 缺口
                    pdpd.setOrderGap(pdpd.getNum() - orderNum);

                }
            }
        };
        return list;
    }

    @Override
    public ApiResult getFormDemand(QueryDemandDto queryDemandDimensionalityDto) {
        Map<String, List> map = new HashMap<>();
        /*查询表单的数据*/
        QueryWrapper<FormType> formTypeQueryWrapper = new QueryWrapper<>();
        formTypeQueryWrapper.eq("code", queryDemandDimensionalityDto.getFormCode());
        List<FormType> formTypeList = formTypeMapper.selectList(formTypeQueryWrapper);
        if (CollectionUtils.isEmpty(formTypeList)) {
            throw new OtherException("获取表单失败");
        }
        /*品类查询字段配置列表查询品类下的字段id*/
        BaseQueryWrapper qw = new BaseQueryWrapper();
        queryDemandDimensionalityDto.setFieldId(formTypeList.get(0).getId());
        PlanningUtils.fieldConfigQw(qw,queryDemandDimensionalityDto);
        /*查询字段配置中的数据*/
        List<FieldOptionConfig> optionConfigList = fieldOptionConfigMapper.selectList(qw);

        /*获取到这个品类下存在的字段*/
        List<String> fieldManagementIdList = optionConfigList.stream().map(FieldOptionConfig::getFieldManagementId).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(fieldManagementIdList)) {
            throw new OtherException("字段未配置选项");
        }
        /*配置的字段*/
        /**
         * 查询需求占比中依赖于字段id
         */
        List<FieldManagement> fieldManagementList = fieldManagementMapper.selectBatchIds(fieldManagementIdList);
        /*可选的数据，配置所有数据*/
        map.put("fieldManagement", fieldManagementList);
        QueryWrapper<PlanningDemand> queryWrapper1 = new QueryWrapper<>();
        if (StrUtil.isNotBlank(queryDemandDimensionalityDto.getProdCategory2nd())) {
            queryWrapper1.apply(StrUtil.isNotBlank(queryDemandDimensionalityDto.getProdCategory2nd()), "FIND_IN_SET({0},prod_category2nd)", queryDemandDimensionalityDto.getProdCategory2nd());
        } else {
            queryWrapper1.apply("(prod_category2nd IS NULL or prod_category2nd = '' )");
            queryWrapper1.apply(StrUtil.isNotBlank(queryDemandDimensionalityDto.getProdCategory()), "FIND_IN_SET({0},prod_category)", queryDemandDimensionalityDto.getProdCategory());
        }
        /*查询维度中已选择的数据*/
        queryWrapper1.eq("planning_season_id", queryDemandDimensionalityDto.getPlanningSeasonId());
        queryWrapper1.eq("channel", queryDemandDimensionalityDto.getChannel());
        List<PlanningDemand> planningDemandList = baseMapper.selectList(queryWrapper1);
        /*字段id*/
        List<String> stringList = planningDemandList.stream().map(PlanningDemand::getFieldId).collect(Collectors.toList());
        List<FieldManagement> fieldManagementList2 = new ArrayList<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        if (!CollectionUtils.isEmpty(stringList)) {
            queryWrapper.in("id", stringList);
            fieldManagementList2 = fieldManagementMapper.selectList(queryWrapper);
        }
        /*已选择的数据*/
        map.put("demandDimensionality", fieldManagementList2);
        return ApiResult.success("查询成功", map);
    }

    @Override
    @Transactional(readOnly = false)
    public ApiResult saveDel(List<SaveDelDemandDto> saveDelDemandDto) {
        /*查询已存在的*/
        BaseQueryWrapper<PlanningDemand> queryWrapper = new BaseQueryWrapper<>();
        if (StrUtil.isNotBlank(saveDelDemandDto.get(0).getProdCategory2nd())) {
            queryWrapper.eq("prod_category2nd", saveDelDemandDto.get(0).getProdCategory2nd());
        } else {
            queryWrapper.eq("prod_category", saveDelDemandDto.get(0).getProdCategory());
            queryWrapper.isNull("prod_category2nd");
        }
        queryWrapper.eq("planning_season_id", saveDelDemandDto.get(0).getPlanningSeasonId());
        queryWrapper.eq("channel", saveDelDemandDto.get(0).getChannel());

        List<PlanningDemand> list = baseMapper.selectList(queryWrapper);
        /*添加的数据*/
        List<SaveDelDemandDto> addList = new ArrayList<>();
        /*删除的数据*/
        List<PlanningDemand> delList = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            addList = saveDelDemandDto;
        } else {
            /*前端传的数据*/
            List<String> demandNameList2 = saveDelDemandDto.stream().map(SaveDelDemandDto::getDemandName).collect(Collectors.toList());
            delList = list.stream().filter(item -> !demandNameList2.contains(item.getDemandName())).collect(Collectors.toList());
            /*数据库查询的数据*/
            List<String> demandNameList = list.stream().map(PlanningDemand::getDemandName).collect(Collectors.toList());
            addList = saveDelDemandDto.stream().filter(item -> !StringUtils.isEmpty(item.getDemandName())&&!demandNameList.contains(item.getDemandName())   ).collect(Collectors.toList());
        }
        if (!CollectionUtils.isEmpty(delList)) {
            QueryWrapper<PlanningDemand> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.in("id", delList.stream().map(PlanningDemand::getId).collect(Collectors.toList()));
          /*  queryWrapper1.eq("category_id", saveDelDemandDto.get(0).getCategoryId());
            queryWrapper1.eq("planning_season_id", saveDelDemandDto.get(0).getPlanningSeasonId());*/
            /*删除需求*/
            baseMapper.delete(queryWrapper1);
            /*删除占比*/
            QueryWrapper<PlanningDemandProportionData> planningDemandProportionDataQueryWrapper = new QueryWrapper<>();
            planningDemandProportionDataQueryWrapper.in("demand_id", delList.stream().map(PlanningDemand::getId).collect(Collectors.toList()));
            planningDemandProportionDataQueryWrapper.eq("company_code", baseController.getUserCompany());
            List<PlanningDemandProportionData> planningDemandProportionDataList = planningDemandProportionDataMapper.selectList(planningDemandProportionDataQueryWrapper);
            if (!CollectionUtils.isEmpty(planningDemandProportionDataList)) {
                planningDemandProportionDataQueryWrapper.clear();
                planningDemandProportionDataQueryWrapper.in("id", planningDemandProportionDataList.stream().map(PlanningDemandProportionData::getId).collect(Collectors.toList()));
                planningDemandProportionDataMapper.delete(planningDemandProportionDataQueryWrapper);
            }
        }
        /*新增新增加的*/

        List<PlanningDemand> planningDemandList = BeanUtil.copyToList(addList, PlanningDemand.class);
        saveBatch(planningDemandList);

        return ApiResult.success("操作成功");
    }

    /**
     * 删除需求占比
     *
     * @param id
     * @return
     */
    @Override
    public Boolean delDemand(String id) {
        baseMapper.deleteById(id);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setImportantFlag(PlanningDemand planningDemand) {
        UpdateWrapper<PlanningDemand> uw = new UpdateWrapper<>();
        uw.lambda().set(PlanningDemand::getImportantFlag, planningDemand.getImportantFlag())
                .eq(PlanningDemand::getId, planningDemand.getId())
        ;
        update(uw);
        return true;
    }

/** 自定义方法区 不替换的区域【other_start】 **/


/** 自定义方法区 不替换的区域【other_end】 **/

}
