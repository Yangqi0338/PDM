package com.base.sbc.module.pushrecords.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.resttemplate.RestTemplateService;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.pushrecords.dto.PushRecordsDto;
import com.base.sbc.module.pushrecords.entity.PushRecords;
import com.base.sbc.module.pushrecords.service.PushRecordsService;
import com.base.sbc.module.smp.dto.HttpResp;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 卞康
 * @date 2023/7/11 10:46:08
 * @mail 247967116@qq.com
 */
@RestController
@Api(tags = "推送日志")
@RequiredArgsConstructor
@RequestMapping(value = BaseController.SAAS_URL + "/pushRecords", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PushRecordsController extends BaseController {
    private final PushRecordsService pushRecordsService;
    private final RestTemplateService restTemplateService;

    @GetMapping("/queryPage")
    public ApiResult queryPage(PushRecordsDto pushRecordsDto) {
        BaseQueryWrapper<PushRecords> queryWrapper = new BaseQueryWrapper<>();
        queryWrapper.notEmptyIn("module_name",pushRecordsDto.getModuleName());
        queryWrapper.notEmptyIn("function_name",pushRecordsDto.getFunctionName());
        queryWrapper.notEmptyIn("related_id",pushRecordsDto.getRelatedId());
        queryWrapper.notEmptyIn("related_name",pushRecordsDto.getRelatedName());
        queryWrapper.notEmptyIn("create_name",pushRecordsDto.getCreateName());
        queryWrapper.between("create_date",pushRecordsDto.getCreateDate());
        queryWrapper.orderByDesc("create_date");
        PageHelper.startPage(pushRecordsDto);
        List<PushRecords> list = pushRecordsService.list(queryWrapper);
        return selectSuccess(new PageInfo<>(list));
    }


    /**
     * 重推
     */
    @PostMapping("/rePush")
    public ApiResult rePush(String id){
        PushRecords pushRecords = pushRecordsService.getById(id);
        HttpResp httpResp = restTemplateService.spmPost(pushRecords.getPushAddress(), pushRecords.getPushContent());
        pushRecords.setPushStatus(httpResp.isSuccess() ? "成功" : "失败");
        pushRecords.setResponseMessage(httpResp.getMessage());
        pushRecords.setResponseStatusCode(httpResp.getCode());
        pushRecordsService.updateById(pushRecords);
        ApiResult apiResult = new ApiResult();
        if (httpResp.isSuccess()){
            apiResult.setMessage("重推成功");
            apiResult.setSuccess(true);
        }else {
            apiResult.setMessage("重推失败");
            apiResult.setSuccess(false);
        }
        return apiResult;
    }

    /**
     * 获取查询条件列表
     */
    @GetMapping("/getColumnList")
    public ApiResult getColumnList(String column){
        QueryWrapper<PushRecords> queryWrapper = new QueryWrapper<>();
        String underScoreCase = StringUtils.toUnderScoreCase(column);
        queryWrapper.select(underScoreCase);
        queryWrapper.groupBy(underScoreCase);
        List<PushRecords> list = pushRecordsService.list(queryWrapper);
        List<Map<String, Object>> collect = list.stream().map(item -> {
            Map<String, Object> one = new HashMap<>();
            Object val=BeanUtil.getProperty(item,column);
            one.put("value",val);
            one.put("label",val);
            return one;
        }).collect(Collectors.toList());
        return selectSuccess(collect);
    }
}
