/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.formType.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.formType.dto.FieldOptionConfigDto;
import com.base.sbc.module.formType.dto.QueryFieldManagementDto;
import com.base.sbc.module.formType.dto.QueryFieldOptionConfigDto;
import com.base.sbc.module.formType.dto.SaveUpdateFieldManagementDto;
import com.base.sbc.module.formType.entity.FieldManagement;
import com.base.sbc.module.formType.service.FieldManagementService;
import com.base.sbc.module.formType.service.FieldOptionConfigService;
import com.base.sbc.module.formType.vo.FieldManagementVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
* 类描述：字段管理表 Controller类
* @address com.base.sbc.module.formType.web.FieldManagementController
* @author lxl
* @email lxl.fml@gmail.com
* @date 创建时间：2023-4-15 18:33:51
* @version 1.0
*/
@RestController
@Api(tags = "字段管理表")
@RequestMapping(value = BaseController.SAAS_URL + "/fieldManagement", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class FieldManagementController {

	@Autowired
	private FieldManagementService fieldManagementService;

	@Autowired
	private FieldOptionConfigService fieldOptionConfigService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getFieldManagementList")
	public PageInfo<FieldManagementVo> getFieldManagementList(@Valid QueryFieldManagementDto queryFieldManagementDto) {
		return fieldManagementService.getFieldManagementList(queryFieldManagementDto);
	}

	@ApiOperation(value = "查询维度-字段有配置的选项")
	@GetMapping("/getFieldConfigList")
	public List<FieldManagementVo> getFieldConfigList(@Valid QueryFieldManagementDto queryFieldManagementDto) {
		return fieldManagementService.getFieldConfigList(queryFieldManagementDto);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public FieldManagement getById(@PathVariable("id") String id) {
		return fieldManagementService.getById(id);
	}

	@ApiOperation(value = "保存修改字段管理表")
	@PostMapping("/saveUpdateField")
	public ApiResult saveUpdateField(@Valid @RequestBody SaveUpdateFieldManagementDto saveUpdateFieldManagementDto) {
		return fieldManagementService.saveUpdateField(saveUpdateFieldManagementDto);
	}

	@ApiOperation(value = "删除-通过id查询,多个逗号分开")
	@DeleteMapping("delField/{id}")
	public Boolean removeById(@PathVariable("id") String id) {
		return fieldManagementService.removeById(id);
	}

	@ApiOperation(value = "调整顺序")
	@PostMapping("/adjustmentOrder")
	public ApiResult adjustmentOrder(@Valid @RequestBody QueryFieldManagementDto queryFieldManagementDto) {
		return fieldManagementService.adjustmentOrder(queryFieldManagementDto);
	}

	@ApiOperation(value = "添加配置选项")
	@PostMapping("/addFieldOptionConfig")
	public Boolean addFieldOptionConfig(@Valid @RequestBody List<FieldOptionConfigDto> optionConfigDtoList) {
		return fieldOptionConfigService.addFieldOptionConfig(optionConfigDtoList);
	}

	@ApiOperation(value = "查询配置选项")
	@GetMapping("/getFieldOptionConfigList")
	public PageInfo getFieldOptionConfigList(@Valid QueryFieldOptionConfigDto queryFieldOptionConfigDto) {
		return fieldOptionConfigService.getFieldOptionConfigList(queryFieldOptionConfigDto);
	}

	@ApiOperation(value = "删除-配置选项")
	@DeleteMapping("delFieldOptionConfig/{id}")
	public Boolean delFieldOptionConfig(@PathVariable("id") String id) {
		return fieldOptionConfigService.delFieldOptionConfig(id);
	}

	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStopFieldOptionConfig")
	public Boolean startStopFieldOptionConfig(@Valid @RequestBody StartStopDto startStopDto) {
		return fieldOptionConfigService.startStopFieldOptionConfig(startStopDto);
	}
}































