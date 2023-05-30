/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.patternmaking.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.enums.BaseErrorEnum;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.patternmaking.dto.PatternMakingDto;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.patternmaking.service.PatternMakingService;
import com.base.sbc.module.patternmaking.vo.PatternMakingVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @ApiOperation(value = "通过样衣设计id查询")
    @GetMapping("/findBySampleDesignId")
    public List<PatternMakingVo> findBySampleDesignId(@NotBlank(message = "(sampleDesignId)样衣设计id不能为空") String sampleDesignId) {
        List<PatternMakingVo> list = patternMakingService.findBySampleDesignId(sampleDesignId);
        return list;
    }

    @ApiOperation(value = "明细-通过id查询")
    @GetMapping("/{id}")
    public PatternMaking getById(@PathVariable("id") String id) {
        return patternMakingService.getById(id);
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


    @ApiOperation(value = "样衣设计下发")
    @GetMapping("/sampleDesignSend")
    public boolean sampleDesignSend(@RequestBody  PatternMakingDto  dto) {
        return patternMakingService.sampleDesignSend(dto);
    }
}































