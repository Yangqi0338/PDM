/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.sample.controller;

import java.util.List;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.sample.dto.SampleStyleGroupItemSaveDto;
import com.base.sbc.module.sample.dto.SampleStyleGroupQueryDto;
import com.base.sbc.module.sample.dto.SampleStyleGroupSaveDto;
import com.base.sbc.module.sample.service.SampleStyleGroupService;
import com.base.sbc.module.sample.vo.SampleStyleGroupPageVo;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 类描述：样衣款式搭配相关接口
 */
@RestController
@Api(tags = "样衣款式搭配相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/styleGroup", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class SampleStyleGroupController {

	@Autowired
	private SampleStyleGroupService sampleStyleGroupService;

	@ApiOperation(value = "款式搭配列表:主表左关联配色表")
	@GetMapping("/getStyleGroupList")
	public PageInfo<SampleStyleGroupPageVo> getStyleGroupList(SampleStyleGroupQueryDto dto) {
		return sampleStyleGroupService.getStyleGroupList(dto);
	}

	@ApiOperation(value = "款式搭配列表：删除")
	@DeleteMapping("/delSampleStyleGroup")
	public Boolean delSampleStyleGroup(@RequestParam(value = "id") @NotBlank(message = "id不能为空") String id) {
		return sampleStyleGroupService.delSampleStyleGroup(id);
	}

	@ApiOperation(value = "款式搭配详情：保存主信息")
	@PostMapping("/saveSampleStyleGroup")
	public SampleStyleGroupPageVo saveSampleStyleGroup(@Valid @RequestBody SampleStyleGroupSaveDto dto) {
		return sampleStyleGroupService.saveSampleStyleGroup(dto);
	}

	@ApiOperation(value = "查询款式搭配详情(包括主款)")
	@GetMapping("/getSampleStyleGroup")
	public SampleStyleGroupPageVo getSampleStyleGroup(
			@RequestParam(value = "id") @NotBlank(message = "ID不能为空") String id) {
		return sampleStyleGroupService.getSampleStyleGroup(id);
	}


	@ApiOperation(value = "款式搭配详情表格:详情左关联配色表(排除主款)")
	@GetMapping("/getStyleGroupItemByGroupCode")
	public List<SampleStyleGroupPageVo> getStyleGroupItemByGroupCode(
			@RequestParam(value = "groupCode") @NotBlank(message = "搭配编码不能为空") String groupCode) {
		return sampleStyleGroupService.getStyleGroupItemByGroupCode(groupCode);
	}

	@ApiOperation(value = "款式搭配详情：添加保存明细")
	@PostMapping("/saveSampleStyleGroupItem")
	public Boolean saveSampleStyleGroupItem(@Valid @RequestBody SampleStyleGroupItemSaveDto dto) {
		return sampleStyleGroupService.saveSampleStyleGroupItem(dto);
	}

	@ApiOperation(value = "款式搭配详情表格：删除")
	@DeleteMapping("/delSampleStyleGroupItem")
	public Boolean delSampleStyleGroupItem(@RequestParam(value = "id") @NotBlank(message = "id不能为空") String id) {
		return sampleStyleGroupService.delSampleStyleGroupItem(id);
	}
}