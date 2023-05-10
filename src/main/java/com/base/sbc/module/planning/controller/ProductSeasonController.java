package com.base.sbc.module.planning.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.client.ccm.entity.BasicStructureTreeVo;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.module.common.dto.AdTree;
import com.base.sbc.module.common.vo.UserInfoVo;
import com.base.sbc.module.planning.dto.*;
import com.base.sbc.module.planning.entity.PlanningBand;
import com.base.sbc.module.planning.entity.PlanningCategoryItem;
import com.base.sbc.module.planning.entity.PlanningSeason;
import com.base.sbc.module.planning.service.PlanningBandService;
import com.base.sbc.module.planning.service.PlanningCategoryItemService;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.planning.vo.PlanningBandSummaryInfoVo;
import com.base.sbc.module.planning.vo.YearSeasonVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Struct;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述：
 * @address com.base.sbc.module.planning.controller.ProductSeasonController
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-04-20 13:47
 * @version 1.0
 */
@RestController
@Api(tags = "产品季总览相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/productSeason", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class ProductSeasonController extends BaseController {

    @Resource
    private PlanningSeasonService planningSeasonService;
    @Resource
    private PlanningBandService planningBandService;

    @Resource
    private CcmFeignService ccmFeignService;
    @Resource
    PlanningCategoryItemService planningCategoryItemService;



    @ApiOperation(value = "查询产品季-分页查询")
    @GetMapping
    public PageInfo query(PlanningSeasonSearchDto dto){
        return planningSeasonService.queryByPage(dto,getUserCompany());
    }



    @ApiOperation(value = "查询年份季节")
    @GetMapping("/queryYs")
    public List<YearSeasonVo> queryYs(){
        List<YearSeasonVo> result=new ArrayList<>(16);
        List<PlanningSeason> planningSeasons = planningSeasonService.queryYs(getUserCompany());
        Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap("C8_Year,C8_Quarter");
        if(CollUtil.isNotEmpty(planningSeasons)){
            for (PlanningSeason planningSeason : planningSeasons) {
                YearSeasonVo yearSeasonVo = BeanUtil.copyProperties(planningSeason, YearSeasonVo.class);
                yearSeasonVo.setSeasonDesc(Optional.ofNullable(dictInfoToMap.get("C8_Quarter"))
                        .map(item->item.get(planningSeason.getSeason()))
                        .orElse(""));
                yearSeasonVo.setYearDesc(Optional.ofNullable(dictInfoToMap.get("C8_Year"))
                        .map(item->item.get(planningSeason.getYear()))
                        .orElse(""));
                result.add(yearSeasonVo);
            }
        }
        return result;
    }

    @ApiOperation(value = "按波段展开-分页查询")
    @GetMapping("/expandByBand")
    public PageInfo expandByBand(@Valid  ProductSeasonExpandByBandSearchDto dto){
        return planningBandService.expandByBand(dto,getUserCompany());
    }


    @ApiOperation(value = "按品类展开")
    @GetMapping("/expandByCategory")
    public List<BasicStructureTreeVo> expandByCategory(@Valid ProductSeasonExpandByCategorySearchDto dto){
        return planningCategoryItemService.expandByCategory(dto);
    }


    @ApiOperation(value = "查询坑位列表")
    @PostMapping("/findProductCategoryItem")
    public ApiResult findProductCategoryItem(@Valid @RequestBody ProductCategoryItemSearchDto dto){
        if(CollUtil.isEmpty(dto.getCategoryIds())&&StrUtil.isBlank(dto.getPlanningBandId())){
            throw  new OtherException("品类和波段必须有一个不为空");
        }
        return planningCategoryItemService.findProductCategoryItem(dto);
    }


    @ApiOperation(value = "设置任务等级")
    @PostMapping("/setTaskLevel")
    public boolean setTaskLevel(@Valid @RequestBody List<SetTaskLevelDto> dtoList){
        // 查询数据是否存在
        return planningCategoryItemService.setTaskLevel(dtoList);
    }


    @ApiOperation(value = "分配设计师")
    @PostMapping("/allocationDesign")
    public boolean allocationDesign(@Valid @RequestBody List<AllocationDesignDto> dtoList){
        return planningCategoryItemService.allocationDesign(dtoList);

    }

    /**
     * 获取企业和产品季的树形结构
     */
    @GetMapping("/getTree")
    public ApiResult getTree(){
        List<AdTree> list = planningSeasonService.getTree();
        return selectSuccess(list);
    }


    @ApiOperation(value = "坑位信息下发")
    @PostMapping("/send")
    public boolean send(@RequestBody List<PlanningCategoryItem> categoryItemList){
        if(CollUtil.isEmpty(categoryItemList)){
            throw  new OtherException("数据为空");
        }
         // 校验
        for (PlanningCategoryItem planningCategoryItem : categoryItemList) {
            if(StrUtil.hasBlank(planningCategoryItem.getDesigner(),planningCategoryItem.getDesignerId())){
                throw  new OtherException("未分配设计师");
            }
            if(StrUtil.isBlank(planningCategoryItem.getTaskLevel())){
                throw  new OtherException("请设置任务等级");
            }
        }
        return planningCategoryItemService.send(categoryItemList);

    }


}
