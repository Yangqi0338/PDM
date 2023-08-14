/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.entity.Dept;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.constant.TechnologyBoardConstant;
import com.base.sbc.config.enums.BasicNumber;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.redis.RedisUtils;
import com.base.sbc.config.utils.DateUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.common.utils.AttachmentTypeConstant;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.nodestatus.entity.NodeStatus;
import com.base.sbc.module.nodestatus.service.NodeStatusConfigService;
import com.base.sbc.module.nodestatus.service.NodeStatusService;
import com.base.sbc.module.patternmaking.dto.*;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.patternmaking.enums.EnumNodeStatus;
import com.base.sbc.module.patternmaking.mapper.PatternMakingMapper;
import com.base.sbc.module.patternmaking.service.PatternMakingService;
import com.base.sbc.module.patternmaking.vo.*;
import com.base.sbc.module.sample.vo.SampleUserVo;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.module.style.vo.StyleVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：打版管理 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.patternmaking.service.PatternMakingService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-29 13:33:05
 */
@Service
@RequiredArgsConstructor
public class PatternMakingServiceImpl extends BaseServiceImpl<PatternMakingMapper, PatternMaking> implements PatternMakingService {
    // 自定义方法区 不替换的区域【other_start】
    private final StyleService styleService;
    private final NodeStatusService nodeStatusService;
    private final AttachmentService attachmentService;
    private final AmcFeignService amcFeignService;

    private final CcmFeignService ccmFeignService;
    private final NodeStatusConfigService nodeStatusConfigService;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private RedisUtils redisUtils;


    @Override
    public List<PatternMakingListVo> findBySampleDesignId(String styleId) {
        QueryWrapper<PatternMaking> qw = new QueryWrapper<>();
        qw.eq("style_id", styleId);
        qw.eq("m.del_flag", BaseGlobal.NO);
        qw.orderBy(true, true, "create_date");
        List<PatternMakingListVo> patternMakingListVos = getBaseMapper().findBySampleDesignId(qw);
        return patternMakingListVos;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, OtherException.class})
    public PatternMaking savePatternMaking(PatternMakingDto dto) {
        Style style = styleService.getById(dto.getStyleId());
        if (style == null) {
            throw new OtherException("款式设计不存在");
        }
        QueryWrapper rQw = new QueryWrapper();
        rQw.eq("style_id", dto.getStyleId());
        rQw.eq("del_flag", BaseGlobal.NO);
        //出版样只能有一个
        if (StrUtil.equals("初版样", dto.getSampleType())) {

            rQw.eq("sample_type", dto.getSampleType());
            long count = count(rQw);
            if (count != 0) {
                throw new OtherException(dto.getSampleType() + "只能有一个");
            }
        } else {
            rQw.ne("sample_type", "初版样");
            long count = count(rQw);
            if (count >= 5) {
                throw new OtherException("只能新建6个打版指令:1个初版样、5个其他");
            }
        }
        //查询样衣
        PatternMaking patternMaking = BeanUtil.copyProperties(dto, PatternMaking.class);
        patternMaking.setCode(getNextCode(style));
        patternMaking.setPlanningSeasonId(style.getPlanningSeasonId());
        if (StrUtil.equals(dto.getTechnicianKitting(), BaseGlobal.YES)) {
            patternMaking.setTechnicianKittingDate(new Date());
        }

        patternMaking.setSglKitting(BaseGlobal.NO);
        patternMaking.setBreakOffPattern(BaseGlobal.NO);
        patternMaking.setBreakOffSample(BaseGlobal.NO);
        patternMaking.setPrmSendStatus(BaseGlobal.NO);
        patternMaking.setDesignSendStatus(BaseGlobal.NO);
        patternMaking.setSecondProcessing(BaseGlobal.NO);
        patternMaking.setSuspend(BaseGlobal.NO);
        patternMaking.setReceiveSample(BaseGlobal.NO);
        patternMaking.setExtAuxiliary(BaseGlobal.NO);
        patternMaking.setPatDiff(Opt.ofBlankAble(patternMaking.getPatDiff()).orElse(style.getPatDiff()));
        save(patternMaking);

        return patternMaking;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, OtherException.class})
    public boolean sampleDesignSend(StyleSendDto dto) {
        EnumNodeStatus enumNodeStatus = EnumNodeStatus.DESIGN_SEND;
        EnumNodeStatus enumNodeStatus2 = EnumNodeStatus.TECHNICAL_ROOM_RECEIVED;
        nodeStatusService.nodeStatusChange(dto.getId(), enumNodeStatus.getNode(), enumNodeStatus.getStatus(), BaseGlobal.YES, BaseGlobal.YES);
        NodeStatus nodeStatus = nodeStatusService.nodeStatusChange(dto.getId(), enumNodeStatus2.getNode(), enumNodeStatus2.getStatus(), BaseGlobal.YES, BaseGlobal.YES);
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.set("node", enumNodeStatus2.getNode());
        uw.set("status", enumNodeStatus2.getStatus());
        uw.set("design_send_date", nodeStatus.getStartDate());
        uw.set("design_send_status", BaseGlobal.YES);
        uw.eq("id", dto.getId());
        setUpdateInfo(uw);
        PatternMaking patternMaking = getById(dto.getId());
        //将款式设计状态改为已下发
        UpdateWrapper<Style> sdUw = new UpdateWrapper<>();
        sdUw.eq("id", patternMaking.getStyleId());
        sdUw.set("status", BasicNumber.TWO.getNumber());
        styleService.update(sdUw);
        // 修改单据
        return update(uw);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean nodeStatusChange(String userId, NodeStatusChangeDto dto, GroupUser groupUser) {
        nodeStatusService.nodeStatusChange(dto.getDataId(), dto.getNode(), dto.getStatus(), dto.getStartFlg(), dto.getEndFlg());
        // 修改单据
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.eq("id", dto.getDataId());
        if (CollUtil.isNotEmpty(dto.getUpdates())) {
            for (Map.Entry<String, Object> kv : dto.getUpdates().entrySet()) {
                uw.set(StrUtil.toUnderlineCase(kv.getKey()), kv.getValue());
            }
        }
        uw.set("node", dto.getNode());
        uw.set("status", dto.getStatus());
        setUpdateInfo(uw);
        setNodeStatusPmData(dto, groupUser, uw);
        // 修改单据
        return update(uw);
    }

    /**
     * 样衣状态改变时设置 相关的值
     *
     * @param dto
     * @param groupUser
     * @param uw
     */
    private void setNodeStatusPmData(NodeStatusChangeDto dto, GroupUser groupUser, UpdateWrapper<PatternMaking> uw) {
        EnumNodeStatus enumNodeStatus = EnumNodeStatus.byNodeStatus(dto.getNode(), dto.getStatus());
        if (enumNodeStatus == null) {
            return;
        }
        switch (enumNodeStatus) {
//            case GARMENT_CUTTING_RECEIVED:
//                uw.set("cutter_id", groupUser.getId());
//                uw.set("cutter_name", groupUser.getName());
//                uw.isNull("cutter_id");
//                break;
//            case GARMENT_SEWING_STARTED:
//                uw.set("stitcher_id", groupUser.getId());
//                uw.set("stitcher", groupUser.getName());
//                uw.isNull("stitcher_id");
//                break;
            case GARMENT_CUTTING_KITTING:
                uw.set("sgl_kitting", BaseGlobal.YES);
                uw.set("sgl_kitting_date", new Date());
                break;
            default:
                break;
        }

    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean prmSend(SetPatternDesignDto dto) {
        PatternMaking byId = getById(dto.getId());
        if (StrUtil.equals(BaseGlobal.YES, byId.getBreakOffPattern())) {
            throw new OtherException("已中断打版,不能下发");
        }
        EnumNodeStatus enumNodeStatus1 = EnumNodeStatus.TECHNICAL_ROOM_SEND;
        EnumNodeStatus enumNodeStatus2 = EnumNodeStatus.SAMPLE_TASK_WAITING_RECEIVE;
        nodeStatusService.nodeStatusChange(dto.getId(), enumNodeStatus1.getNode(), enumNodeStatus1.getStatus(), BaseGlobal.NO, BaseGlobal.YES);
        NodeStatus nodeStatus2 = nodeStatusService.nodeStatusChange(dto.getId(), enumNodeStatus2.getNode(), enumNodeStatus2.getStatus(), BaseGlobal.YES, BaseGlobal.NO);
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.set("node", enumNodeStatus2.getNode());
        uw.set("status", enumNodeStatus2.getStatus());
        uw.set("prm_send_date", nodeStatus2.getStartDate());
        uw.set("prm_send_status", BaseGlobal.YES);
        uw.set(StrUtil.isNotBlank(dto.getPatternDesignId()), "pattern_design_id", dto.getPatternDesignId());
        uw.set(StrUtil.isNotBlank(dto.getPatternDesignName()), "pattern_design_name", dto.getPatternDesignName());
        uw.eq("id", dto.getId());
        // 修改单据
        update(uw);
        return true;
    }

    @Override
    public PageInfo technologyCenterTaskList(TechnologyCenterTaskSearchDto dto) {
        QueryWrapper qw = new QueryWrapper();
        qw.like(StrUtil.isNotBlank(dto.getSearch()), "s.design_no", dto.getSearch());
        qw.eq(StrUtil.isNotBlank(dto.getYear()), "s.year", dto.getYear());
        qw.eq(StrUtil.isNotBlank(dto.getMonth()), "s.month", dto.getMonth());
        qw.eq(StrUtil.isNotBlank(dto.getSeason()), "s.season", dto.getSeason());
        qw.eq(StrUtil.isNotBlank(dto.getPatternDesignId()), "p.pattern_design_id", dto.getPatternDesignId());
        qw.eq("design_send_status", BaseGlobal.YES);
        qw.eq("s.del_flag", BaseGlobal.NO);
        qw.eq("p.del_flag", BaseGlobal.NO);
        amcFeignService.teamAuth(qw, "s.planning_season_id", getUserId());
        if (StrUtil.isBlank(dto.getOrderBy())) {
            dto.setOrderBy(" p.create_date desc ");
        } else {
            dto.setOrderBy(dto.getOrderBy());
        }

        Page<TechnologyCenterTaskVo> page = PageHelper.startPage(dto);
        List<TechnologyCenterTaskVo> list = getBaseMapper().technologyCenterTaskList(qw);
        //设置图片
        attachmentService.setListStylePic(list, "stylePic");
        // 设置版师列表
        if (CollUtil.isNotEmpty(list)) {
            Map<String, List<PatternDesignVo>> pdMap = new HashMap<>(16);
            for (TechnologyCenterTaskVo tct : list) {
                String key = tct.getPlanningSeasonId();
                if (pdMap.containsKey(key)) {
                    tct.setPdList(pdMap.get(key));
                } else {
                    List<PatternDesignVo> patternDesignList = getPatternDesignList(tct.getPlanningSeasonId());
                    tct.setPdList(patternDesignList);
                    pdMap.put(key, patternDesignList);
                }
            }
        }
        return page.toPageInfo();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean setPatternDesign(SetPatternDesignDto dto) {
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.set("pattern_design_id", dto.getPatternDesignId());
        uw.set("pattern_design_name", dto.getPatternDesignName());
        uw.eq("id", dto.getId());
        return update(uw);
//        return true;
    }

    @Override
    public List<PatternDesignVo> getPatternDesignList(String planningSeasonId) {
        List<UserCompany> userList = amcFeignService.getTeamUserListByPost(planningSeasonId, "版师");
        if (CollUtil.isEmpty(userList)) {
            return null;
        }
        List<String> userIds = userList.stream().map(UserCompany::getUserId).collect(Collectors.toList());
        Map<String, String> sampleTypes = ccmFeignService.getDictInfoToMap("SampleType").get("SampleType");
        QueryWrapper<PatternMaking> pmQw = new QueryWrapper<>();
        pmQw.in("pattern_design_id", userIds);
        // 查询已接受 已打版的数据
        pmQw.eq("node", EnumNodeStatus.SAMPLE_TASK_WAITING_RECEIVE.getNode());
        pmQw.in("status", CollUtil.newArrayList(
                EnumNodeStatus.SAMPLE_TASK_RECEIVED.getStatus(),
                EnumNodeStatus.SAMPLE_TASK_IN_VERSION.getStatus()
        ));
        List<PatternDesignSampleTypeQtyVo> qtyList = getBaseMapper().getPatternDesignSampleTypeCount(pmQw);
        List<PatternDesignVo> patternDesignVoList = new ArrayList<>();
        Map<String, List<PatternDesignSampleTypeQtyVo>> qtyMap = qtyList.stream().collect(Collectors.groupingBy(PatternDesignSampleTypeQtyVo::getPatternDesignId));
        Long total = null;
        for (UserCompany user : userList) {
            PatternDesignVo patternDesignVo = BeanUtil.copyProperties(user, PatternDesignVo.class);
            LinkedHashMap<String, Long> sampleTypeCount = new LinkedHashMap<>(16);
            total = 0L;
            for (Map.Entry<String, String> dict : sampleTypes.entrySet()) {
                Long qty = Optional.ofNullable(qtyMap).map(qtyMap1 -> qtyMap1.get(user.getUserId())).map(qtyList2 -> {
                    PatternDesignSampleTypeQtyVo one = CollUtil.findOne(qtyList2, a -> StrUtil.equals(a.getSampleType(), dict.getKey()));
                    return Optional.ofNullable(one).map(PatternDesignSampleTypeQtyVo::getQuantity).orElse(0L);
                }).orElse(0L);
                total = total + qty;
                sampleTypeCount.put(dict.getValue(), qty);
            }
            sampleTypeCount.put("总数", total);
            String deptName = Optional.ofNullable(user.getDeptList()).map(item -> {
                return item.stream().map(Dept::getName).collect(Collectors.joining(StrUtil.COMMA));
            }).orElse("");
            patternDesignVo.setDeptName(deptName);
            patternDesignVo.setSampleTypeCount(sampleTypeCount);
            patternDesignVoList.add(patternDesignVo);
        }
        return patternDesignVoList;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, OtherException.class})
    public boolean breakOffSample(String id) {
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.in("id", StrUtil.split(id, CharUtil.COMMA));
        uw.set("break_off_sample", BaseGlobal.YES);
        return update(uw);
    }

    @Override
    public boolean breakOffPattern(String id) {
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.in("id", StrUtil.split(id, CharUtil.COMMA));
        uw.set("break_off_pattern", BaseGlobal.YES);
        return update(uw);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Integer prmSendBatch(List<SetPatternDesignDto> dtos) {
        int i = 0;
        for (SetPatternDesignDto dto : dtos) {
            if (prmSend(dto)) {
                i++;
            }
        }
        return i;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean setPatternDesignBatch(List<SetPatternDesignDto> dtos) {
        for (SetPatternDesignDto dto : dtos) {
            setPatternDesign(dto);
        }
        return true;
    }

    @Override
    public PageInfo<PatternMakingTaskListVo> patternMakingTaskList(PatternMakingTaskSearchDto dto) {
        QueryWrapper qw = new QueryWrapper();
        qw.like(StrUtil.isNotBlank(dto.getSearch()), "s.design_no", dto.getSearch());
        qw.eq(StrUtil.isNotBlank(dto.getYear()), "s.year", dto.getYear());
        qw.eq(StrUtil.isNotBlank(dto.getMonth()), "s.month", dto.getMonth());
        qw.eq(StrUtil.isNotBlank(dto.getSeason()), "s.season", dto.getSeason());
        qw.eq(StrUtil.isNotBlank(dto.getNode()), "p.node", dto.getNode());
        qw.eq(StrUtil.isNotBlank(dto.getPatternDesignId()), "p.pattern_design_id", dto.getPatternDesignId());
        if (StrUtil.isNotBlank(dto.getIsBlackList())) {
            if (StrUtil.equals(dto.getIsBlackList(), BasicNumber.ONE.getNumber())) {
                //只查询黑单
                qw.eq("p.urgency", "0");
            } else {
                //排除黑单
                qw.ne("p.urgency", "0");

            }
        }
        amcFeignService.teamAuth(qw, "s.planning_season_id", getUserId());
        // 版房主管和设计师 看到全部，版师、裁剪工、车缝工、样衣组长看到自己,

        qw.orderByDesc("p.create_date");
        qw.orderByAsc("p.sort");
        Page<PatternMakingTaskListVo> objects = PageHelper.startPage(dto);
        List<PatternMakingTaskListVo> list = getBaseMapper().patternMakingTaskList(qw);
        //设置图片
        attachmentService.setListStylePic(list, "stylePic");
        // 设置节点状态
        nodeStatusService.setNodeStatus(list);
        return objects.toPageInfo();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Integer setSort(List<SetSortDto> dtoList) {
        int i = 0;
        boolean flg = false;
        for (SetSortDto setSortDto : dtoList) {
            UpdateWrapper<PatternMaking> puw = new UpdateWrapper<>();
            puw.set("sort", setSortDto.getSort());
            puw.eq("id", setSortDto.getId());
            setUpdateInfo(puw);
            flg = this.update(puw);
            if (flg) {
                i++;
            }
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean suspend(SuspendDto dto) {
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.set("suspend", BaseGlobal.YES);
        uw.set("suspend_status", dto.getSuspendStatus());
        uw.set("suspend_remarks", dto.getSuspendRemarks());
        uw.eq("id", dto.getId());
        setUpdateInfo(uw);
        return update(uw);
    }

    @Override
    public boolean cancelSuspend(String id) {
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.set("suspend", BaseGlobal.NO);
        uw.eq("id", id);
        setUpdateInfo(uw);
        return update(uw);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean setKitting(String p, SetKittingDto dto) {
        if (!StrUtil.equalsAny(dto.getKitting(), BaseGlobal.NO, BaseGlobal.YES)) {
            throw new OtherException("是否齐套值不符合:0 or 1");
        }
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        if (StrUtil.equals(dto.getKitting(), BaseGlobal.YES)) {
            uw.set(p + "kitting_date", new Date());
        } else {
            uw.set(p + "kitting_date", null);
        }
        uw.set(p + "kitting", dto.getKitting());
        uw.eq("id", dto.getId());
        return update(uw);
    }

    @Override
    public StylePmDetailVo getDetailById(String id) {
        PatternMaking byId = getById(id);
        if (byId == null) {
            return null;
        }

        PatternMakingVo vo = BeanUtil.copyProperties(byId, PatternMakingVo.class);
        //设置头像
        amcFeignService.setUserAvatarToObj(vo);
        //查询款式设计信息
        StyleVo sampleDesignVo = styleService.getDetail(vo.getStyleId());
        //设置头像
        amcFeignService.setUserAvatarToObj(sampleDesignVo);
        StylePmDetailVo result = BeanUtil.copyProperties(sampleDesignVo, StylePmDetailVo.class);
        result.setPatternMaking(vo);
        // 查询附件，纸样文件
        List<AttachmentVo> attachmentVoList = attachmentService.findByforeignId(vo.getId(), AttachmentTypeConstant.PATTERN_MAKING_PATTERN);
        vo.setAttachmentList(attachmentVoList);
        // 设置状态
        nodeStatusService.setNodeStatusToBean(vo, "nodeStatusList", "nodeStatus");
        return result;
    }

    @Override
    public boolean saveAttachment(SaveAttachmentDto dto) {
        attachmentService.saveAttachment(dto.getAttachmentList(), dto.getId(), AttachmentTypeConstant.PATTERN_MAKING_PATTERN);
        return true;
    }

    @Override
    public PageInfo patternMakingSteps(PatternMakingCommonPageSearchDto dto) {
        // 查询样衣信息
        QueryWrapper<Style> sdQw = new QueryWrapper<>();
        sdQw.like(StrUtil.isNotBlank(dto.getSearch()), "design_no", dto.getSearch());
        sdQw.eq(StrUtil.isNotBlank(dto.getSeason()), "season", dto.getSeason());
        sdQw.eq(StrUtil.isNotBlank(dto.getYear()), "year", dto.getYear());
        sdQw.eq(StrUtil.isNotBlank(dto.getMonth()), "month", dto.getMonth());
        sdQw.eq(StrUtil.isNotBlank(dto.getBandCode()), "band_code", dto.getBandCode());
        sdQw.eq(StrUtil.isNotBlank(dto.getDesignerId()), "designer_id", dto.getDesignerId());
        sdQw.eq(COMPANY_CODE, getCompanyCode());
        sdQw.eq("del_flag", BaseGlobal.NO);
        sdQw.eq("status", BasicNumber.TWO.getNumber());
        sdQw.exists("select id from t_pattern_making where style_id=t_style.id and del_flag='0'");
        if (StrUtil.isNotBlank(dto.getOrderBy())) {
            dto.setOrderBy("create_date desc");
        }
        Page page = PageHelper.startPage(dto);
        List<Style> sdList = styleService.list(sdQw);
        PageInfo pageInfo = page.toPageInfo();
        if (CollUtil.isEmpty(sdList)) {
            return null;
        }
        List<StyleStepVo> styleStepVos = BeanUtil.copyToList(sdList, StyleStepVo.class);
//        PageInfo pageInfo = BeanUtil.copyProperties(sdPageInfo, PageInfo.class, "list");
//        pageInfo.setList(sampleDesignStepVos);
        pageInfo.setList(styleStepVos);
        attachmentService.setListStylePic(styleStepVos, "stylePic");
        // 查询打版指令
        List<String> sdIds = styleStepVos.stream().map(StyleStepVo::getId).collect(Collectors.toList());
        QueryWrapper<PatternMaking> pmQw = new QueryWrapper<>();
        pmQw.in("style_id", sdIds);
        List<PatternMaking> pmList = this.list(pmQw);
        if (CollUtil.isEmpty(pmList)) {
            return pageInfo;
        }
        List<String> pmIds = pmList.stream().map(PatternMaking::getId).collect(Collectors.toList());
        List<StyleStepVo.PatternMakingStepVo> patternMakingStepVos = BeanUtil.copyToList(pmList, StyleStepVo.PatternMakingStepVo.class);
        //查询节点状态
        QueryWrapper<NodeStatus> nsQw = new QueryWrapper<>();
        nsQw.in("data_id", pmIds);
        List<NodeStatus> nsList = nodeStatusService.list(nsQw);
        if (CollUtil.isNotEmpty(nsList)) {
            Map<String, List<NodeStatus>> nsMap = nsList.stream().collect(Collectors.groupingBy(NodeStatus::getDataId));
            for (StyleStepVo.PatternMakingStepVo patternMakingStepVo : patternMakingStepVos) {
                Map<String, NodeStatus> stringNodeStatusMap = Optional.ofNullable(nsMap.get(patternMakingStepVo.getId())).map(item -> {
                    return item.stream().collect(Collectors.toMap(k -> k.getNode() + StrUtil.DASHED + k.getStatus(), v -> v, (a, b) -> {
                        if (DateUtil.compare(b.getStartDate(), a.getStartDate()) > 0) {
                            return b;
                        }
                        return a;
                    }));
                }).orElse(null);
                patternMakingStepVo.setNodeStatus(stringNodeStatusMap);
            }
        }
        LinkedHashMap<String, List<StyleStepVo.PatternMakingStepVo>> pmStepMap = patternMakingStepVos.stream().collect(Collectors.groupingBy(k -> k.getStyleId(), LinkedHashMap::new, Collectors.toList()));
        for (StyleStepVo styleStepVo : styleStepVos) {
            styleStepVo.setPatternMakingSteps(pmStepMap.get(styleStepVo.getId()));
        }

        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean nodeStatusChange(String userId, List<NodeStatusChangeDto> list, GroupUser groupUser) {
        for (NodeStatusChangeDto dto : list) {
            nodeStatusChange(userId, dto, groupUser);
        }
        return true;
    }

    @Override
    public PageInfo<SampleBoardVo> sampleBoardList(PatternMakingCommonPageSearchDto dto) {
        QueryWrapper qw = new QueryWrapper();
        qw.like(StrUtil.isNotBlank(dto.getSearch()), "s.design_no", dto.getSearch());
        qw.eq(StrUtil.isNotBlank(dto.getYear()), "s.year", dto.getYear());
        qw.eq(StrUtil.isNotBlank(dto.getMonth()), "s.month", dto.getMonth());
        qw.eq(StrUtil.isNotBlank(dto.getSeason()), "s.season", dto.getSeason());
        qw.in(StrUtil.isNotBlank(dto.getBandCode()), "s.band_code", StrUtil.split(dto.getBandCode(), StrUtil.COMMA));
        qw.eq(StrUtil.isNotBlank(dto.getPatternDesignId()), "p.pattern_design_id", dto.getPatternDesignId());
        Page<SampleBoardVo> objects = PageHelper.startPage(dto);
        List<SampleBoardVo> list = getBaseMapper().sampleBoardList(qw);
        attachmentService.setListStylePic(list, "stylePic");
        // 设置节点状态数据
        nodeStatusService.setNodeStatusToListBean(list, "patternMakingId", null, "nodeStatus");
        return objects.toPageInfo();
    }

    @Override
    public boolean receiveSample(String id) {
        UpdateWrapper<PatternMaking> uw = new UpdateWrapper<>();
        uw.in("id", StrUtil.split(id, StrUtil.COMMA));
        uw.set("receive_sample", BaseGlobal.YES);
        uw.set("receive_sample_date", new Date());
        return update(uw);
    }

    @Override
    public List<SampleUserVo> getAllPatternDesignList(String companyCode) {
        List<SampleUserVo> list = getBaseMapper().getAllPatternDesignList(companyCode);
        amcFeignService.setUserAvatarToList(list);
        return list;
    }

    @Override
    public List prmDataOverview(String time) {
        List result = new ArrayList(16);
        List<String> timeRange = StrUtil.split(time, CharUtil.COMMA);
        // 打版需求总数：版房内所有打版需求的总数，包括待打版、打版中、打版完成和暂停的任务。
        QueryWrapper allQw = new QueryWrapper<>();
        prmDataOverviewCommonQw(allQw, timeRange, BaseGlobal.NO);
        result.add(CollUtil.newArrayList("打版需求总数", count(allQw)));

        List<Map<String, Object>> nsCountList = getBaseMapper().nsCount(allQw);
        Map<String, Long> nsCountMap = new HashMap<>(16);
        for (Map<String, Object> nsCount : nsCountList) {
            if (!nsCount.containsKey("nodeStatus")) {
                continue;
            }
            nsCountMap.put(MapUtil.getStr(nsCount, "nodeStatus"), MapUtil.getLong(nsCount, "total", 0L));
        }
        result.add(CollUtil.newArrayList("待打版数量", MapUtil.getLong(nsCountMap, "打版任务-待接收", 0L)));
        result.add(CollUtil.newArrayList("打版中数量", MapUtil.getLong(nsCountMap, "打版任务-打版中", 0L)));
        result.add(CollUtil.newArrayList("打版完成", MapUtil.getLong(nsCountMap, "打版任务-打版完成", 0L)));
        QueryWrapper suspendQw = new QueryWrapper();
        prmDataOverviewCommonQw(suspendQw, timeRange, BaseGlobal.YES);
        result.add(CollUtil.newArrayList("打版暂停", count(suspendQw)));
        QueryWrapper sdQw = new QueryWrapper();
        prmDataOverviewCommonQw(sdQw, timeRange, BaseGlobal.NO);
        sdQw.eq("node", "样衣任务");
        result.add(CollUtil.newArrayList("样衣制作总数", count(sdQw)));
        result.add(CollUtil.newArrayList("裁剪中数量", MapUtil.getLong(nsCountMap, "样衣任务-裁剪开始", 0L)));
        result.add(CollUtil.newArrayList("车缝中", MapUtil.getLong(nsCountMap, "样衣任务-车缝开始", 0L)));
        return result;
    }

    @Override
    public JSONObject getNodeStatusConfig(GroupUser user, String node, String status, String dataId) {
        if (StrUtil.isNotBlank(dataId)) {
            PatternMaking bean = getById(dataId);
            JSONObject config = nodeStatusService.getNodeNextAndAuth(user, bean, NodeStatusConfigService.PATTERN_MAKING_NODE_STATUS, NodeStatusConfigService.NEXT);
            return config;
        }
        return nodeStatusService.getNodeStatusConfig(NodeStatusConfigService.PATTERN_MAKING_NODE_STATUS, node, status);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean assignmentUser(GroupUser groupUser, AssignmentUserDto dto) {
        PatternMaking byId = getById(dto.getId());
        byId.setStitcher(dto.getStitcher());
        byId.setStitcherId(dto.getStitcherId());
        //分配后进入下一节点
        nodeStatusService.nextOrPrev(groupUser, byId, NodeStatusConfigService.PATTERN_MAKING_NODE_STATUS, NodeStatusConfigService.NEXT);
        return updateById(byId);
    }

    @Override
    public List<PatternDesignVo> pdTaskDetail(String companyCode) {
        List<String> planningSeasonIdByUserId = amcFeignService.getPlanningSeasonIdByUserId(getUserId());
        if (CollUtil.isEmpty(planningSeasonIdByUserId)) {
            return null;
        }
        List<PatternDesignVo> patternDesignList = getPatternDesignList(CollUtil.join(planningSeasonIdByUserId, StrUtil.COMMA));
        return patternDesignList;
    }

    private void prmDataOverviewCommonQw(QueryWrapper qw, List timeRange, String suspend) {
        qw.ne("del_flag", BaseGlobal.YES);
        qw.eq(COMPANY_CODE, getCompanyCode());
        qw.eq(StrUtil.isNotBlank(suspend), "suspend", suspend);
        amcFeignService.teamAuth(qw, "planning_season_id", getUserId());
        qw.between(CollUtil.isNotEmpty(timeRange), "create_date", CollUtil.getFirst(timeRange), CollUtil.getLast(timeRange));
    }


    public String getNextCode(Style style) {
        String designNo = style.getDesignNo();
        QueryWrapper<PatternMaking> qw = new QueryWrapper<>();
        qw.eq("style_id", style.getId());
        long count = count(qw);
        String code = StrUtil.padPre(String.valueOf(count + 1), 3, "0");
        return designNo + StrUtil.DASHED + code;

    }


    @Override
    public PageInfo queryPageInfo(PatternMakingCommonPageSearchDto dto) {
        String companyCode = getCompanyCode();
        QueryWrapper qw = new QueryWrapper<>();
        qw.eq(StrUtil.isNotBlank(dto.getStatus()), "pk.status", dto.getStatus());
        qw.eq("pk.company_code", companyCode);

        Page<PatternMakingForSampleVo> objects = PageHelper.startPage(dto);
        getBaseMapper().getAllList(qw);
        List<PatternMakingForSampleVo> result = objects.getResult();

        return objects.toPageInfo();
    }



    @Override
    public ArrayList<ArrayList> versionComparisonViewWeekMonth(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto,String token) {
        // 1、获取缓存数据，过期时间，数据过期后会开启一个新的线程
        Map<String, Object> dataMap = this.redisGetData(patternMakingWeekMonthViewDto,token,TechnologyBoardConstant.VERSION_COMPARISON);
        // 2、缓存为空则直接返回，
        if(null == dataMap){
            return null;
        }
        return (ArrayList<ArrayList>)dataMap.get("dataLists");
    }

    /**
     * 品类汇总统计
     * @param patternMakingWeekMonthViewDto 技术看板DTO
     * @param token token
     * @return 结果集
     */
    @Override
    public ArrayList<ArrayList> categorySummaryCount(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto, String token) {
        // 1、获取缓存数据，过期时间，数据过期后会开启一个新的线程
        Map<String, Object> dataMap = this.redisGetData(patternMakingWeekMonthViewDto,token,TechnologyBoardConstant.CATEGORY_SUMMARY_COUNT);
        // 2、缓存为空则直接返回，
        if(null == dataMap){
            return null;
        }
        return (ArrayList<ArrayList>)dataMap.get("dataLists");
    }

    /**
     * 根据时间按周月 统计样衣产能总数
     * @param patternMakingWeekMonthViewDto 技术看板DTO
     * @param token 令牌
     * @return 返回集合数据
     */
    @Override
    public ArrayList<ArrayList> sampleCapacityTotalCount(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto, String token) {
        // 1、获取缓存数据，过期时间，数据过期后会开启一个新的线程
        Map<String, Object> dataMap = this.redisGetData(patternMakingWeekMonthViewDto,token,TechnologyBoardConstant.SAMPLE_CAPACITY_TOTAL);
        // 2、缓存为空则直接返回，
        if(null == dataMap){
            return null;
        }
        return (ArrayList<ArrayList>)dataMap.get("dataLists");
    }

    /**
     * 产能对比统计
     * @param patternMakingWeekMonthViewDto 技术看板DTO
     * @param token token
     * @return 返回集合数据
     */
    @Override
    public ArrayList<ArrayList> capacityContrastStatistics(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto, String token) {
        // 1、获取缓存数据，过期时间，数据过期后会开启一个新的线程
        Map<String, Object> dataMap = this.redisGetData(patternMakingWeekMonthViewDto, token, TechnologyBoardConstant.CAPACITY_CONTRAST);
        // 2、缓存为空则直接返回，
        if (null == dataMap) {
            return null;
        }
        return (ArrayList<ArrayList>) dataMap.get("dataLists");
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean nextOrPrev(Principal user, String id, String np) {
        PatternMaking pm = getById(id);
        GroupUser groupUser = userUtils.getUserBy(user);
        if (pm == null) {
            throw new OtherException("任务不存在");
        }
        //跳转
        boolean flg = nodeStatusService.nextOrPrev(groupUser, pm, NodeStatusConfigService.PATTERN_MAKING_NODE_STATUS, np);
        updateById(pm);
        return flg;
    }


    /**
     * 产能对比统计 查数据库
     *
     * @param patternMakingWeekMonthViewDto 技术看板DTO
     * @param key                           token
     * @return 返回集合数据
     */
    private Map<String, Object> capacityContrastStatisticsView(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto, String key) {
        // 1、拼接锁key
        String lockKey = TechnologyBoardConstant.CACHE_LOCK + TechnologyBoardConstant.CAPACITY_CONTRAST + patternMakingWeekMonthViewDto.getCompanyCode();
        try {
            // 1.1、缓存数据格式
            Map<String,Object> dataMap = Maps.newHashMap();
            // 2、返回数据集合
            ArrayList<ArrayList> dataLists = new ArrayList<>();
            // 2.1、添加表头
            dataLists.add(CollUtil.newArrayList("product", "打版需求数", "打版产能数"));
            // 3、参数校验
            this.paramCheck(patternMakingWeekMonthViewDto);
            // 4、查询数据库  打版需求
            List<PatternMakingWeekMonthViewVo> demandList = baseMapper.capacityContrastDemandStatistics(patternMakingWeekMonthViewDto);
            // 4.1、查询数据库  打版产能
            List<PatternMakingWeekMonthViewVo> contrastList = baseMapper.capacityContrastCapacityStatistics(patternMakingWeekMonthViewDto);
            // 4.2、都为空 都返回
            if(CollectionUtil.isEmpty(demandList) && CollectionUtil.isEmpty(contrastList)){
                return null;
            }
            // 5、取出两个集合的有数据的年份，然后汇总，去重有数据的年份
            List<String> yearWeekDemandList = demandList.stream().map(PatternMakingWeekMonthViewVo::getYearWeek).distinct().collect(Collectors.toList());
            List<String> yearWeekContrasList = contrastList.stream().map(PatternMakingWeekMonthViewVo::getYearWeek).distinct().collect(Collectors.toList());
            yearWeekDemandList.addAll(yearWeekContrasList);
            List<String> yearWeekList = yearWeekDemandList.stream().distinct().collect(Collectors.toList());
            // 6、打版需求列表转为map,以年份为key
            Map<String, PatternMakingWeekMonthViewVo> demandMap = this.getDataByYearWeek(demandList);
            // 6.1、打版产能列表转为map,以年份为key
            Map<String, PatternMakingWeekMonthViewVo> contrastMap = this.getDataByYearWeek(contrastList);
            // 7、 循环有数据的年份，拼接数据
            for (String yearWeek : yearWeekList) {
                ArrayList<String> arrayList = new ArrayList();
                // 添加年份
                arrayList.add(yearWeek);
                // 7.1、初版数据
                if(null != demandMap.get(yearWeek)){
                    PatternMakingWeekMonthViewVo patternMakingWeekMonthViewVo = demandMap.get(yearWeek);
                    arrayList.add(null != patternMakingWeekMonthViewVo.getRequirementNumSum() ? patternMakingWeekMonthViewVo.getRequirementNumSum() : String.valueOf(BaseGlobal.ZERO));
                } else {
                    arrayList.add(String.valueOf(BaseGlobal.ZERO));
                }
                // 7.2、改版样数据
                if(null != contrastMap.get(yearWeek)){
                    PatternMakingWeekMonthViewVo revisionTwo = contrastMap.get(yearWeek);
                    arrayList.add(null != revisionTwo.getRequirementNumSum() ? revisionTwo.getRequirementNumSum() : String.valueOf(BaseGlobal.ZERO));
                } else {
                    arrayList.add(String.valueOf(BaseGlobal.ZERO));
                }
                dataLists.add(arrayList);
            }

            // 8. 缓存数据
            this.setRedisData(dataMap,dataLists,key,lockKey,DateUtils.HOUR_MINUTES,DateUtils.MINUTES);
            return dataMap;
        } catch (Exception e){
            redisUtils.del(lockKey);
            log.error("设置缓存错误：", e);
            e.printStackTrace();
        } finally {
            redisUtils.del(lockKey);
        }
       return null;
    }

    /**
     * 根据时间按周月 统计样衣产能总数  查询数据库
     * @param patternMakingWeekMonthViewDto 技术看板DTO
     * @param key 缓存key
     * @return 返回集合数据
     */
    public Map<String,Object> querySampleCapacityTotalCount(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto, String key){
        // 1、缓存数据格式
        Map<String,Object> dataMap = Maps.newHashMap();
        // 2、返回数据集合
        ArrayList<ArrayList> dataLists = new ArrayList<>();
        // 2.1、添加表头
        dataLists.add(CollUtil.newArrayList("product", "裁剪数", "车缝数", "样衣完成总数"));
        // 3、参数校验
        this.paramCheck(patternMakingWeekMonthViewDto);
        // 5、参数样衣状态: 车缝完成、裁剪完成、样衣完成
        List<String> statusList = CollUtil.newArrayList(EnumNodeStatus.GARMENT_CUTTING_COMPLETE.getStatus(),
                EnumNodeStatus.GARMENT_SEWING_COMPLETE.getStatus(), EnumNodeStatus.GARMENT_COMPLETE.getStatus());
        patternMakingWeekMonthViewDto.setStatusList(statusList);
        List<PatternMakingWeekMonthViewVo> patternMakingWeekMonthViewVos = baseMapper.sampleCapacityTotalCount(patternMakingWeekMonthViewDto);
        if(CollectionUtil.isEmpty(patternMakingWeekMonthViewVos)){
            return null;
        }
        // 6、解析数据 取出有数据的年份
        List<String> yearWeekList = patternMakingWeekMonthViewVos.stream().map(PatternMakingWeekMonthViewVo::getYearWeek).distinct().collect(Collectors.toList());
        // 6.1、 根据节点状态分组
        Map<String, List<PatternMakingWeekMonthViewVo>> patternMakingAllMap = patternMakingWeekMonthViewVos.stream().filter(item -> StrUtil.isNotBlank(item.getStatus()))
                .collect(Collectors.groupingBy(item -> item.getStatus()));
        // 6.2、获取车缝完成数据
        Map<String, PatternMakingWeekMonthViewVo> garmentCuttingCompleteMap = Maps.newHashMap();
        if(null != patternMakingAllMap.get(EnumNodeStatus.GARMENT_CUTTING_COMPLETE.getStatus())){
            List<PatternMakingWeekMonthViewVo> garmentCuttingCompleteList = patternMakingAllMap.get(EnumNodeStatus.GARMENT_CUTTING_COMPLETE.getStatus());
            garmentCuttingCompleteMap = this.getDataByYearWeek(garmentCuttingCompleteList);
        }
        // 6.3、获取裁剪完成数据
        Map<String, PatternMakingWeekMonthViewVo> garmentSewingCompleteMap = Maps.newHashMap();
        if(null != patternMakingAllMap.get(EnumNodeStatus.GARMENT_SEWING_COMPLETE.getStatus())){
            List<PatternMakingWeekMonthViewVo> garmentCuttingCompleteList = patternMakingAllMap.get(EnumNodeStatus.GARMENT_SEWING_COMPLETE.getStatus());
            garmentSewingCompleteMap = this.getDataByYearWeek(garmentCuttingCompleteList);
        }
        // 6.4、获取样衣完成数据
        Map<String, PatternMakingWeekMonthViewVo> garmentCompleteMap = Maps.newHashMap();
        if(null != patternMakingAllMap.get(EnumNodeStatus.GARMENT_COMPLETE.getStatus())){
            List<PatternMakingWeekMonthViewVo> garmentCuttingCompleteList = patternMakingAllMap.get(EnumNodeStatus.GARMENT_COMPLETE.getStatus());
            garmentCompleteMap = this.getDataByYearWeek(garmentCuttingCompleteList);
        }
        // 7、拼接数据
        for (String yearWeek : yearWeekList) {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(yearWeek);
            // 7.1 拼接车缝完成数据 无数据默认为 0
            if(null != garmentCuttingCompleteMap && null != garmentCuttingCompleteMap.get(yearWeek)){
                PatternMakingWeekMonthViewVo patternMakingWeekMonthViewVo = garmentCuttingCompleteMap.get(yearWeek);
                arrayList.add(patternMakingWeekMonthViewVo.getNum());
            } else {
                arrayList.add(String.valueOf(BaseGlobal.ZERO));
            }
            // 7.2 拼接裁剪完成数据 无数据默认为 0
            if(null != garmentSewingCompleteMap && null != garmentSewingCompleteMap.get(yearWeek)){
                PatternMakingWeekMonthViewVo patternMakingWeekMonthViewVo = garmentSewingCompleteMap.get(yearWeek);
                arrayList.add(patternMakingWeekMonthViewVo.getNum());
            } else {
                arrayList.add(String.valueOf(BaseGlobal.ZERO));
            }
            // 7.3 拼接样衣完成数据 无数据默认为 0
            if(null != garmentCompleteMap && null != garmentCompleteMap.get(yearWeek)){
                PatternMakingWeekMonthViewVo patternMakingWeekMonthViewVo = garmentCompleteMap.get(yearWeek);
                arrayList.add(patternMakingWeekMonthViewVo.getNum());
            } else {
                arrayList.add(String.valueOf(BaseGlobal.ZERO));
            }
            dataLists.add(arrayList);
        }
        // 8、锁key
        String lockKey = TechnologyBoardConstant.CACHE_LOCK + TechnologyBoardConstant.SAMPLE_CAPACITY_TOTAL + patternMakingWeekMonthViewDto.getCompanyCode();
        // 8.1 缓存数据
        try {
            this.setRedisData(dataMap,dataLists,key,lockKey,DateUtils.HOUR_MINUTES,DateUtils.MINUTES);
        } catch (Exception e){
            redisUtils.del(lockKey);
            log.error("设置缓存错误：", e);
            e.printStackTrace();
        } finally {
            redisUtils.del(lockKey);
        }
        return dataMap;
    }


    /**
     *  品类汇总统计
     * @param patternMakingWeekMonthViewDto 参数
     * @param key 缓存key
     * @return 数据
     */
    private Map<String,Object> queryCategorySummaryCountData(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto,String key){
        // 1、缓存数据格式
        Map<String,Object> dataMap = Maps.newHashMap();
        // 2、返回数据集合
        ArrayList<ArrayList> dataLists = new ArrayList<>();
        // 参数校验
        this.paramCheck(patternMakingWeekMonthViewDto);
        // 5、参数状态集合
        List<String> statusList = new ArrayList<>();
        // 5.1、查询未打版数据
        statusList.add(EnumNodeStatus.SAMPLE_TASK_WAITING_RECEIVE.getStatus());
        statusList.add(EnumNodeStatus.SAMPLE_TASK_RECEIVED.getStatus());
        patternMakingWeekMonthViewDto.setSampleType("未打版");
        patternMakingWeekMonthViewDto.setStatusList(statusList);
        List<PatternMakingWeekMonthViewVo> noPatternDataList = baseMapper.categorySummaryCount(patternMakingWeekMonthViewDto);
        // 5.1.1、未打版数据转为Map用于组装数据返回前端
        Map<String,PatternMakingWeekMonthViewVo> noPatternDataMap = Maps.newHashMap();
        if(CollectionUtil.isNotEmpty(noPatternDataList)){
            noPatternDataMap = this.getDataByYearWeek(noPatternDataList);
        }
        // 5.1.2、清空查询数据
        statusList.clear();
        // 5.2、查询打版中数据
        statusList.add(EnumNodeStatus.SAMPLE_TASK_IN_VERSION.getStatus());
        patternMakingWeekMonthViewDto.setStatusList(statusList);
        patternMakingWeekMonthViewDto.setSampleType(EnumNodeStatus.SAMPLE_TASK_IN_VERSION.getStatus());
        List<PatternMakingWeekMonthViewVo> patternCentreDataList = baseMapper.categorySummaryCount(patternMakingWeekMonthViewDto);
        // 5.2.1 打版中数据转为Map用于组装数据返回前端
        Map<String,PatternMakingWeekMonthViewVo> patternCentreDataMap = Maps.newHashMap();
        if(CollectionUtil.isNotEmpty(noPatternDataList)){
            patternCentreDataMap = this.getDataByYearWeek(patternCentreDataList);
        }
        // 5.2.2 清空查询数据
        statusList.clear();
        // 5.3 查询已完成数据
        statusList.add(EnumNodeStatus.SAMPLE_TASK_VERSION_COMPLETE.getStatus());
        patternMakingWeekMonthViewDto.setStatusList(statusList);
        patternMakingWeekMonthViewDto.setSampleType(EnumNodeStatus.SAMPLE_TASK_VERSION_COMPLETE.getStatus());
        List<PatternMakingWeekMonthViewVo> completePatternDataList = baseMapper.categorySummaryCount(patternMakingWeekMonthViewDto);
        Map<String,PatternMakingWeekMonthViewVo> completePatternDataMap = Maps.newHashMap();
        // 5.3.1 已完成数据转为Map用于组装数据返回前端
        if(CollectionUtil.isNotEmpty(noPatternDataList)){
            completePatternDataMap = this.getDataByYearWeek(completePatternDataList);
        }
        // 5.3.2 清空查询数据
        statusList.clear();
        // 5.4 查询需求总数
        patternMakingWeekMonthViewDto.setStatusList(null);
        patternMakingWeekMonthViewDto.setSampleType("需求数总数");
        List<PatternMakingWeekMonthViewVo> requirementNumSumDataList = baseMapper.categorySummaryCount(patternMakingWeekMonthViewDto);
        Map<String,PatternMakingWeekMonthViewVo> requirementNumSumDataMap = Maps.newHashMap();
        // 5.4.1  已完成数据转为Map用于组装数据返回前端
        if(CollectionUtil.isNotEmpty(noPatternDataList)){
            requirementNumSumDataMap = this.getDataByYearWeek(requirementNumSumDataList);
        }
        // 6、合并所有数据，取出有数据的年份
        List<PatternMakingWeekMonthViewVo> patternDataAllList = new ArrayList<>();
        // 6.1 未开版数据
        patternDataAllList.addAll(noPatternDataList);
        // 6.2 开版中数据
        patternDataAllList.addAll(patternCentreDataList);
        // 6.3 已开版数据
        patternDataAllList.addAll(completePatternDataList);
        // 6.4 需求总数数据
        patternDataAllList.addAll(requirementNumSumDataList);
        // 6.5 取出有数据的年份并去重
        List<String> yearWeekList = patternDataAllList.stream().map(PatternMakingWeekMonthViewVo::getYearWeek).distinct().collect(Collectors.toList());
        // 7、拼接数据
        this.joinCategorySummaryData(yearWeekList,dataLists,noPatternDataMap,patternCentreDataMap,completePatternDataMap,requirementNumSumDataMap);
        // 8、锁key
        String lockKey = TechnologyBoardConstant.CACHE_LOCK + TechnologyBoardConstant.CATEGORY_SUMMARY_COUNT + patternMakingWeekMonthViewDto.getCompanyCode();
        // 8.1 缓存数据
        try {
            this.setRedisData(dataMap,dataLists,key,lockKey,DateUtils.HOUR_MINUTES,DateUtils.MINUTES);
        } catch (Exception e){
            redisUtils.del(lockKey);
            log.error("设置缓存错误：", e);
            e.printStackTrace();
        } finally {
            redisUtils.del(lockKey);
        }
        return dataMap;
    }


    /**
     *  根据时间按周月统计版类对比
     * @param patternMakingWeekMonthViewDto 技术看板DTO
     * @param key 缓存KEY
     * @return 根据周返回集合
     */
    public Map<String,Object> queryVersionComparisonView(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto, String key){
        // 1、缓存数据格式
        Map<String,Object> dataMap = Maps.newHashMap();
        // 2、返回数据集合
        ArrayList<ArrayList> dataLists = new ArrayList<>();
        // 2.1、添加表头
        dataLists.add(CollUtil.newArrayList("product", "头版数", "改版数"));
        // 参数校验
        this.paramCheck(patternMakingWeekMonthViewDto);
        // 5、从数据库查询数据
        List<PatternMakingWeekMonthViewVo> dataList = baseMapper.versionComparisonViewWeekMonth(patternMakingWeekMonthViewDto);
        // 6、判断数据是为空
        if(CollectionUtil.isNotEmpty(dataList)){
            // 6.1 取出所有的时间
            List<String> yearWeekList = dataList.stream().filter(item -> StrUtil.isNotBlank(item.getYearWeek()))
                    .map(m -> m.getYearWeek()).distinct().collect(Collectors.toList());

            // 6.2根据sampleType分类
            Map<Object, List<PatternMakingWeekMonthViewVo>> sampleTypeMap = dataList.stream().collect(Collectors.groupingBy(PatternMakingWeekMonthViewVo::getSampleType));

            // 6.3 取出初版数据
            List<PatternMakingWeekMonthViewVo> firstVersionList = null != sampleTypeMap.get("初版样") ? sampleTypeMap.get("初版样") : new ArrayList<>();
            //  6.3.1 根据时间转换为map
            Map<String, PatternMakingWeekMonthViewVo> firstVersionMap = Maps.newHashMap();
            if(CollectionUtil.isNotEmpty(firstVersionList)){
                firstVersionMap = firstVersionList.stream().filter(item -> StrUtil.isNotBlank(item.getYearWeek()))
                        .collect(Collectors.toMap(k -> k.getYearWeek(), v -> v, (a, b) -> b));
            }

            // 6.4 取出改版样数据
            List<PatternMakingWeekMonthViewVo> revisionList = null != sampleTypeMap.get("改版样") ? sampleTypeMap.get("改版样") : new ArrayList<>();
            //  6.4.1 根据时间把改版样数据转换为map
            Map<String,PatternMakingWeekMonthViewVo> revisionMap = Maps.newHashMap();
            if(CollectionUtil.isNotEmpty(revisionList)){
                revisionMap = revisionList.stream().filter(item -> StrUtil.isNotBlank(item.getYearWeek()))
                        .collect(Collectors.toMap(k -> k.getYearWeek(), v -> v, (a, b) -> b));
            }

            // 7、根据数据里的拼接返回的数据
            for (String yearWeek : yearWeekList) {
                ArrayList<String> arrayList = new ArrayList();
                arrayList.add(yearWeek);
                // 7.1、初版数据
                if(null != firstVersionMap.get(yearWeek)){
                    PatternMakingWeekMonthViewVo patternMakingWeekMonthViewVo = firstVersionMap.get(yearWeek);
                    arrayList.add(null != patternMakingWeekMonthViewVo.getNum() ? patternMakingWeekMonthViewVo.getNum() : String.valueOf(BaseGlobal.ZERO));
                } else {
                    arrayList.add(String.valueOf(BaseGlobal.ZERO));
                }
                // 7.2、改版样数据
                if(null != revisionMap.get(yearWeek)){
                    PatternMakingWeekMonthViewVo revisionTwo = revisionMap.get(yearWeek);
                    arrayList.add(null != revisionTwo.getNum() ? revisionTwo.getNum() : String.valueOf(BaseGlobal.ZERO));
                } else {
                    arrayList.add(String.valueOf(BaseGlobal.ZERO));
                }
                dataLists.add(arrayList);
            }
        }
        // 8、锁key
        String lockKey = TechnologyBoardConstant.CACHE_LOCK + TechnologyBoardConstant.VERSION_COMPARISON + patternMakingWeekMonthViewDto.getCompanyCode();
        // 8.1 缓存数据
        try {
            this.setRedisData(dataMap,dataLists,key,lockKey,DateUtils.HOUR_MINUTES,DateUtils.MINUTES);
        } catch (Exception e){
            redisUtils.del(lockKey);
            log.error("设置缓存错误：", e);
            e.printStackTrace();
        } finally {
            redisUtils.del(lockKey);
        }

        return dataMap;
    }

    /**
     * 获取缓存的数据
     * @param patternMakingWeekMonthViewDto 技术看板DTO
     * @param token token
     * @param countType 根据统计类型查询对应的方法
     * @return 数据
     */
    private Map<String,Object> redisGetData(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto,String token,String countType){
        // 1、组装缓存key
        StringBuffer key = new StringBuffer();
        key.append(countType);
        key.append(patternMakingWeekMonthViewDto.getCompanyCode());
        key.append(patternMakingWeekMonthViewDto.getWeeklyMonth());
        key.append(patternMakingWeekMonthViewDto.getStartTime());
        key.append(patternMakingWeekMonthViewDto.getEndTime());
        key.append(patternMakingWeekMonthViewDto.getNode());
        if(CollectionUtil.isNotEmpty(patternMakingWeekMonthViewDto.getCategoryIds())){
            key.append(StringUtils.convertListToString(patternMakingWeekMonthViewDto.getCategoryIds()));
        }
        Object redisData = redisUtils.get(key.toString());
        // 拼接锁KEY 区分企业缓存数据
        String lockKey =  TechnologyBoardConstant.CACHE_LOCK + countType + patternMakingWeekMonthViewDto.getCompanyCode();
        // 如果缓存没有直接返回null，开启线程查数据
        if(null == redisData){
            // 获取锁，只能查询一次
            if(null == redisUtils.get(lockKey)){
                try {
                    // 加锁
                    redisUtils.set(lockKey, token + patternMakingWeekMonthViewDto.getCompanyCode());
                    // 查询数据库数据再缓存
                    return this.getCountType(patternMakingWeekMonthViewDto,key.toString(),countType);
                }catch (Exception e){
                    log.error("统计接口报错:{}",e);
                    redisUtils.del(lockKey);
                } finally {
                    // 无论成功报错 都会清除锁
                    redisUtils.del(lockKey);
                }


            }
            return null;
        }
        // 获取缓存数据
        Map<String,Object> data = (Map<String,Object>)redisData;
        // 获取设置的超时时间
        String timeOut = data.get(TechnologyBoardConstant.TIME_OUT).toString();
        // 获取缓存时间
        String dateString = data.get(TechnologyBoardConstant.TIME).toString();
        // 获取设置的超时时间类型
        String timeType = data.get(TechnologyBoardConstant.TIME_TYPE).toString();
        // 获取数据缓存过期时间
        Date dateOut = DateUtils.getDateByDateType(dateString, timeOut, timeType);
        Date currentDate = new Date();
        // 设置数据的缓存时间已过 开启新的线程去查数据
        if(dateOut.getTime() - currentDate.getTime() < 0){
            // 设置锁，只能查询一次
            if(null == redisUtils.get(lockKey)){
                // 开启线程查数据
                this.getData(patternMakingWeekMonthViewDto,key.toString(),token,lockKey,countType);
            }
        }
        return data;
    }

    /**
     * 开启线程查数据
     * @param patternMakingWeekMonthViewDto 技术看板DTO
     * @param key 缓存key
     * @param token token
     * @param lockKey 拼接的锁key
     */
    private void getData(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto,String key,String token,String lockKey,String countType){
        new Thread(()->{
            try {
                redisUtils.set(lockKey, token + patternMakingWeekMonthViewDto.getCompanyCode());
                // 查询数据库数据再缓存
                this.getCountType(patternMakingWeekMonthViewDto,key.toString(),countType);
            }catch (Exception e){
                log.error("统计接口报错:{}",e);
                redisUtils.del(lockKey);
            } finally {
                // 无论成功报错 都会清除锁
                redisUtils.del(lockKey);
            }
        }).start();
    }

    /**
     * 根据类型调取那个方法
     * @param patternMakingWeekMonthViewDto 参数
     * @param key 缓存key
     * @param countType 统计类型
     * @return 数据
     */
    private Map<String,Object> getCountType(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto, String key, String countType){
        if(countType.equals(TechnologyBoardConstant.VERSION_COMPARISON)){
            // 版类对比统计
            return this.queryVersionComparisonView(patternMakingWeekMonthViewDto,key);
        } else if(countType.equals(TechnologyBoardConstant.CATEGORY_SUMMARY_COUNT)) {
            // 品类汇总统计
            return this.queryCategorySummaryCountData(patternMakingWeekMonthViewDto,key);
        } else if(countType.equals(TechnologyBoardConstant.SAMPLE_CAPACITY_TOTAL)) {
            // 样衣产能总数统计
            return this.querySampleCapacityTotalCount(patternMakingWeekMonthViewDto,key);
        } else if(countType.equals(TechnologyBoardConstant.CAPACITY_CONTRAST)) {
            // 产能对比统计
            return this.capacityContrastStatisticsView(patternMakingWeekMonthViewDto,key);
        }
        return null;
    }

    /**
     * 拼接数据 (方法建议不超过80行，超过则抽出来)
     * @param yearWeekList 年份列表
     * @param dataLists 返回数据列表
     * @param noPatternDataMap 未开版数据
     * @param patternCentreDataMap 开版中数据
     * @param completePatternDataMap 已开版数据
     * @param requirementNumSumDataMap 需求总数数据
     */
    private void joinCategorySummaryData(List<String> yearWeekList,ArrayList<ArrayList> dataLists, Map<String,PatternMakingWeekMonthViewVo> noPatternDataMap, Map<String,PatternMakingWeekMonthViewVo> patternCentreDataMap
            , Map<String,PatternMakingWeekMonthViewVo> completePatternDataMap, Map<String,PatternMakingWeekMonthViewVo> requirementNumSumDataMap){
        for (String yearWeek : yearWeekList) {
            ArrayList<String> arrayList = new ArrayList();
            arrayList.add(yearWeek);
            // 7.1 拼接未打版数据 无数据默认为 0
            if(null != noPatternDataMap && null != noPatternDataMap.get(yearWeek)){
                PatternMakingWeekMonthViewVo patternMakingWeekMonthViewVo = noPatternDataMap.get(yearWeek);
                arrayList.add(patternMakingWeekMonthViewVo.getNum());
            } else {
                arrayList.add(String.valueOf(BaseGlobal.ZERO));
            }
            // 7.2 拼接打版中数据 无数据默认为 0
            if(null != patternCentreDataMap && null != patternCentreDataMap.get(yearWeek)){
                PatternMakingWeekMonthViewVo patternMakingWeekMonthViewVo = patternCentreDataMap.get(yearWeek);
                arrayList.add(patternMakingWeekMonthViewVo.getNum());
            } else {
                arrayList.add(String.valueOf(BaseGlobal.ZERO));
            }
            // 7.3 拼接打版完成数据 无数据默认为 0
            if(null != completePatternDataMap && null != completePatternDataMap.get(yearWeek)){
                PatternMakingWeekMonthViewVo patternMakingWeekMonthViewVo = completePatternDataMap.get(yearWeek);
                arrayList.add(patternMakingWeekMonthViewVo.getNum());
            } else {
                arrayList.add(String.valueOf(BaseGlobal.ZERO));
            }
            // 7.4 拼接需求总数数据 无数据默认为 0
            if(null != requirementNumSumDataMap && null != requirementNumSumDataMap.get(yearWeek)){
                PatternMakingWeekMonthViewVo patternMakingWeekMonthViewVo = requirementNumSumDataMap.get(yearWeek);
                arrayList.add(patternMakingWeekMonthViewVo.getRequirementNumSum());
            } else {
                arrayList.add(String.valueOf(BaseGlobal.ZERO));
            }
            dataLists.add(arrayList);
        }
    }

    /**
     * list转为map
     * @param list 要转map的数据
     * @return map
     */
    private Map<String,PatternMakingWeekMonthViewVo> getDataByYearWeek(List<PatternMakingWeekMonthViewVo> list){
        if(CollectionUtil.isEmpty(list)){
            return null;
        }
        return list.stream().filter(item -> StrUtil.isNotBlank(item.getYearWeek()))
                .collect(Collectors.toMap(k -> k.getYearWeek(), v -> v, (a, b) -> b));
    }

    /**
     * 参数校验
     * @param patternMakingWeekMonthViewDto 校验的参数
     */
    private void paramCheck(PatternMakingWeekMonthViewDto patternMakingWeekMonthViewDto){
        // 3.1、时间为空 默认查询当前时间的前一个月数据
        if(StringUtils.isBlank(patternMakingWeekMonthViewDto.getStartTime()) || StringUtils.isBlank(patternMakingWeekMonthViewDto.getEndTime()) ){
            Date date = new Date();
            // 获取过去时间 默认一个月
            if(StringUtils.isNotBlank(patternMakingWeekMonthViewDto.getWeeklyMonth())){
                if(BaseGlobal.WEEK.equals(patternMakingWeekMonthViewDto.getWeeklyMonth())){
                    patternMakingWeekMonthViewDto.setStartTime(DateUtils.getMonthAgo(date));
                } else {
                    // 获取过去一年的时间
                    patternMakingWeekMonthViewDto.setStartTime(DateUtils.getYearAgo(date));
                }
            } else {
                // 获取过去一个月的时间
                patternMakingWeekMonthViewDto.setStartTime(DateUtils.getMonthAgo(date));
            }
            patternMakingWeekMonthViewDto.setEndTime(DateUtils.formatDateTime(date));
        }
        // 4、判断是否是根据周、月查询
        if(StringUtils.isBlank(patternMakingWeekMonthViewDto.getWeeklyMonth()) || (!patternMakingWeekMonthViewDto.getWeeklyMonth().equals(BaseGlobal.WEEK) && !patternMakingWeekMonthViewDto.getWeeklyMonth().equals(BaseGlobal.MONTH))){
            patternMakingWeekMonthViewDto.setWeeklyMonth( BaseGlobal.WEEK);
        }
        // 默认为打版任务节点
        if(StringUtils.isBlank(patternMakingWeekMonthViewDto.getNode())){
            patternMakingWeekMonthViewDto.setNode(EnumNodeStatus.SAMPLE_TASK_VERSION_COMPLETE.getNode());
        }

    }

    /**
     *  设置缓存数据
     * @param dataMap 缓存拼接的数据
     * @param dataLists 缓存数据
     * @param key 缓存key
     * @param lockKey 锁key
     * @param timeOut 缓存时间
     * @param timeType 缓存类型 DateUtils 如 分、时、天
     */
    private void setRedisData(Map<String,Object> dataMap, ArrayList<ArrayList> dataLists, String key,String lockKey,int timeOut,String timeType){
        // 8、放入Redis缓存在数据里面设置过期时间
        dataMap.put("dataLists",dataLists);
        // 8.1 数据里设置过期时间为一小时
        dataMap.put(TechnologyBoardConstant.TIME_OUT, timeOut);
        // 8.2 设置过期时间类型
        dataMap.put(TechnologyBoardConstant.TIME_TYPE, timeType);
        // 8.3 数据缓存时间
        dataMap.put(TechnologyBoardConstant.TIME, DateUtils.formatDateTime(new Date()));
        redisUtils.set(key,dataMap);
        // 9、解除缓存锁
        redisUtils.del(lockKey);
    }

    // 自定义方法区 不替换的区域【other_end】
}

