/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.sample.controller;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.sample.dto.SamplePageDto;
import com.base.sbc.module.sample.dto.SampleSaveDto;
import com.base.sbc.module.sample.entity.Sample;
import com.base.sbc.module.sample.service.SampleService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

/**
* 类描述：样衣 Controller类
* @address com.base.sbc.module.sample.web.SampleController
* @author lxl
* @email lxl.fml@gmail.com
* @date 创建时间：2023-5-9 11:16:15
* @version 1.0
*/
@RestController
@Api(tags = "样衣设计相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/sample", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class SampleController{

	@Autowired
	private SampleService sampleService;

	@ApiOperation(value = "分页查询")
	@GetMapping()
	public PageInfo pageInfo(@Valid SamplePageDto dto){
		return sampleService.queryPageInfo(dto);
	}

	@ApiOperation(value = "保存")
	@PostMapping
	public Sample save(@RequestBody SampleSaveDto dto) {
		Sample  sample=sampleService.saveSample(dto);
		return sample;
	}

}































