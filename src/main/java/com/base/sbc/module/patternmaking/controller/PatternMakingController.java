/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.common.dto.IdDto;
import com.base.sbc.module.nodestatus.service.NodeStatusConfigService;
import com.base.sbc.module.patternmaking.dto.*;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.patternmaking.service.PatternMakingService;
import com.base.sbc.module.patternmaking.vo.*;
import com.base.sbc.module.sample.vo.SampleUserVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * 类描述：打版管理 Controller类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.web.PatternMakingController
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-29 13:33:05
 */
@RestController
@Api(tags = "打版管理")
@RequestMapping(value = BaseController.SAAS_URL + "/patternMaking", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PatternMakingController {

    @Autowired
    private PatternMakingService patternMakingService;
    @Autowired
    private UserUtils userUtils;

    @ApiOperation(value = "技术中心看板-任务列表")
    @GetMapping("/technologyCenterTaskList")
    public PageInfo technologyCenterTaskList(TechnologyCenterTaskSearchDto dto) {
        return patternMakingService.technologyCenterTaskList(dto);
    }

    @ApiOperation(value = "通过款式设计id查询")
    @GetMapping("/findBySampleDesignId")
    public List<PatternMakingListVo> findBySampleDesignId(@NotBlank(message = "(styleId)款式设计id不能为空") String styleId) {
        List<PatternMakingListVo> list = patternMakingService.findBySampleDesignId(styleId);
        return list;
    }

    @ApiOperation(value = "打版指令明细", notes = "通过id查询")
    @GetMapping("/{id}")
    public StylePmDetailVo getById(@PathVariable("id") String id) {
        return patternMakingService.getDetailById(id);
    }

    @ApiOperation(value = "保存附件-纸样文件")
    @PostMapping("/saveAttachment")
    public boolean saveAttachment(@Valid @RequestBody SaveAttachmentDto dto) {
        return patternMakingService.saveAttachment(dto);
    }

    @ApiOperation(value = "删除-通过id查询,多个逗号分开")
    @DeleteMapping("/{id}")
    public Boolean removeById(@PathVariable("id") String id) {
        List<String> ids = StringUtils.convertList(id);
        return patternMakingService.removeByIds(ids);
    }

    @ApiOperation(value = "保存")
    @PostMapping
    public PatternMaking save(@Valid @RequestBody PatternMakingDto dto) {
        PatternMaking patternMaking = patternMakingService.savePatternMaking(dto);
        return patternMaking;
    }

    @ApiOperation(value = "修改")
    @PutMapping
    public PatternMaking update(@RequestBody PatternMakingDto dto) {
        PatternMaking patternMaking = BeanUtil.copyProperties(dto, PatternMaking.class);
        patternMakingService.updateById(patternMaking);
        return patternMaking;
    }


    @ApiOperation(value = "款式设计下发")
    @PostMapping("/sampleDesignSend")
    public boolean sampleDesignSend(@Valid @RequestBody StyleSendDto dto) {
        return patternMakingService.sampleDesignSend(dto);
    }

    @ApiOperation(value = "版房主管下发")
    @PostMapping("/prmSend")
    public Integer prmSend(@Valid @RequestBody List<SetPatternDesignDto> dtos) {
        return patternMakingService.prmSendBatch(dtos);
    }


    @ApiOperation(value = "指定版师")
    @PostMapping("/setPatternDesign")
    public boolean setPatternDesign(@Valid @RequestBody SetPatternDesignDto dto) {
        return patternMakingService.setPatternDesign(dto);
    }

    @ApiOperation(value = "指定版师批量")
    @PostMapping("/setPatternDesignBatch")
    public boolean setPatternDesignBatch(@Valid @RequestBody List<SetPatternDesignDto> dto) {
        return patternMakingService.setPatternDesignBatch(dto);
    }

    @ApiOperation(value = "获取版师列表")
    @GetMapping("/getPatternDesignList")
    @ApiImplicitParams({@ApiImplicitParam(name = "planningSeasonId", value = "产品季节id", required = true, paramType = "query")})
    public List<PatternDesignVo> getPatternDesignList(@Valid @NotBlank(message = "产品季节id不能为空") String planningSeasonId) {
        return patternMakingService.getPatternDesignList(planningSeasonId);
    }

    @ApiOperation(value = "中断样衣")
    @GetMapping("/breakOffSample")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "编号", required = true, paramType = "query")})
    public boolean breakOffSample(@Valid @NotBlank(message = "id不能为空") String id) {
        return patternMakingService.breakOffSample(id);
    }

    @ApiOperation(value = "中断打版")
    @GetMapping("/breakOffPattern")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "编号", required = true, paramType = "query")})
    public boolean breakOffPattern(@Valid @NotBlank(message = "id不能为空") String id) {
        return patternMakingService.breakOffPattern(id);
    }


    @ApiOperation(value = "打版管理任务-列表", notes = "")
    @GetMapping("/patternMakingTaskList")
    public PageInfo<PatternMakingTaskListVo> patternMakingTaskList(PatternMakingTaskSearchDto dto) {
        return patternMakingService.patternMakingTaskList(dto);
    }

    @ApiOperation(value = "设置排序", notes = "")
    @PostMapping("/setSort")
    public Integer setSort(@Valid @RequestBody List<SetSortDto> dtoList) {
        return patternMakingService.setSort(dtoList);
    }

    @ApiOperation(value = "挂起", notes = "")
    @PostMapping("/suspend")
    public boolean suspend(@Valid @RequestBody SuspendDto dto) {
        return patternMakingService.suspend(dto);
    }

    @ApiOperation(value = "取消挂起", notes = "")
    @GetMapping("/cancelSuspend")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "编号", required = true, paramType = "query")})
    public boolean cancelSuspend(@Valid @NotBlank(message = "id不能为空") String id) {
        return patternMakingService.cancelSuspend(id);
    }

    @ApiOperation(value = "工艺员设置齐套", notes = "")
    @PostMapping("/technicianKitting")
    public boolean technicianKitting(@Valid @RequestBody SetKittingDto dto) {
        return patternMakingService.setKitting("technician_", dto);
    }

    @ApiOperation(value = "样衣组长设置齐套", notes = "")
    @PostMapping("/sglKitting")
    public boolean sglKitting(@Valid @RequestBody SetKittingDto dto) {
        return patternMakingService.setKitting("sgl_", dto);
    }

    @ApiOperation(value = "打版进度列表", notes = "")
    @GetMapping("/patternMakingSteps")
    public PageInfo patternMakingSteps(PatternMakingCommonPageSearchDto dto) {
        return patternMakingService.patternMakingSteps(dto);
    }

    @ApiOperation(value = "样衣看板列表", notes = "")
    @GetMapping("/sampleBoardList")
    public PageInfo<SampleBoardVo> sampleBoardList(PatternMakingCommonPageSearchDto dto) {
        return patternMakingService.sampleBoardList(dto);
    }

    @ApiOperation(value = "确认收到样衣", notes = "")
    @GetMapping("/receiveSample")
    public boolean receiveSample(@Valid @NotBlank(message = "id不能为空") String id) {
        return patternMakingService.receiveSample(id);
    }

    @ApiOperation(value = "版师列表", notes = "")
    @GetMapping("/getAllPatternDesignList")
    public List<SampleUserVo> getAllPatternDesignList(@RequestHeader(BaseConstant.USER_COMPANY) String companyCode) {
        return patternMakingService.getAllPatternDesignList(companyCode);
    }

    @ApiOperation(value = "板房数据总览", notes = "")
    @GetMapping("/prmDataOverview")
    public List prmDataOverview(String time) {
        return patternMakingService.prmDataOverview(time);
    }

    @ApiOperation(value = "版类对比统计", notes = "")
    @GetMapping("/versionComparisonViewWeekMonth")
    public ApiResult versionComparisonViewWeekMonth(@RequestHeader(BaseConstant.AUTHORIZATION)String token,@RequestHeader(BaseConstant.USER_COMPANY)String companyCode, PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto) {
        patternMakingWeekMonthViewDto.setCompanyCode(companyCode);
        return ApiResult.success("查询成功",patternMakingService.versionComparisonViewWeekMonth(patternMakingWeekMonthViewDto,token));
    }

    @ApiOperation(value = "获取节点状态配置", notes = "")
    @GetMapping("/getNodeStatusConfig")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "node", value = "节点", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "dataId", value = "打版id", required = false, dataType = "String", paramType = "query"),
    })
    public JSONObject getNodeStatusConfig(Principal user, String node, String status, String dataId) {
        GroupUser userBy = userUtils.getUserBy(user);
        return patternMakingService.getNodeStatusConfig(userBy, node, status, dataId);
    }

    @ApiOperation(value = "分配人员(车缝工)", notes = "")
    @PostMapping("/assignmentUser")
    public boolean assignmentUser(Principal user,@Valid @RequestBody AssignmentUserDto dto) {
        GroupUser groupUser = userUtils.getUserBy(user);
        return patternMakingService.assignmentUser(groupUser,dto);
    }
    @ApiOperation(value = "版师任务明细", notes = "")
    @GetMapping("/pdTaskDetail")
    public List<PatternDesignVo> pdTaskDetail(@RequestHeader(BaseConstant.USER_COMPANY) String companyCode){
        return patternMakingService.pdTaskDetail(companyCode);
    }

    @ApiOperation(value = "获取制版单-样衣新增", notes = "")
    @GetMapping("/getAllList")
    public PageInfo getAllList(PatternMakingCommonPageSearchDto dto){
        return patternMakingService.queryPageInfo(dto);
    }


    @ApiOperation(value = "品类汇总统计", notes = "")
    @PostMapping("/categorySummaryCount")
    public ApiResult categorySummaryCount(@RequestHeader(BaseConstant.AUTHORIZATION)String token,@RequestHeader(BaseConstant.USER_COMPANY)String companyCode, @RequestBody PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto) {
        patternMakingWeekMonthViewDto.setCompanyCode(companyCode);
        return ApiResult.success("查询成功",patternMakingService.categorySummaryCount(patternMakingWeekMonthViewDto,token));
    }
    @ApiOperation(value = "统计样衣产能总数", notes = "")
    @PostMapping("/sampleCapacityTotalCount")
    public ApiResult sampleCapacityTotalCount(@RequestHeader(BaseConstant.AUTHORIZATION)String token,@RequestHeader(BaseConstant.USER_COMPANY)String companyCode, @RequestBody PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto) {
        patternMakingWeekMonthViewDto.setCompanyCode(companyCode);
        return ApiResult.success("查询成功", patternMakingService.sampleCapacityTotalCount(patternMakingWeekMonthViewDto, token));
    }

    @ApiOperation(value = "产能对比", notes = "")
    @PostMapping("/capacityContrastStatistics")
    public ApiResult capacityContrastStatistics(@RequestHeader(BaseConstant.AUTHORIZATION) String token, @RequestHeader(BaseConstant.USER_COMPANY) String companyCode, @RequestBody PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto) {
        patternMakingWeekMonthViewDto.setCompanyCode(companyCode);
        return ApiResult.success("查询成功", patternMakingService.capacityContrastStatistics(patternMakingWeekMonthViewDto, token));
    }

    @ApiOperation(value = "前往下一个节点", notes = "")
    @GetMapping("/next")
    public boolean next(Principal user, @Validated IdDto idDto) {
        return patternMakingService.nextOrPrev(user, idDto.getId(), NodeStatusConfigService.NEXT);
    }

    @ApiOperation(value = "前往上一个节点", notes = "")
    @GetMapping("/prev")
    public boolean prev(Principal user, @Validated IdDto idDto) {
        return patternMakingService.nextOrPrev(user, idDto.getId(), NodeStatusConfigService.PREV);
    }
}































