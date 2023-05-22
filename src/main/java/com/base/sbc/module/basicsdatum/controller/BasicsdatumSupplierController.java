/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.basicsdatum.controller;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.basicsdatum.dto.QueryRevampBasicsdatumSupplierDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSupplier;
import com.base.sbc.module.basicsdatum.service.BasicsdatumSupplierService;
import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumSupplierDto;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumSupplierVo;
import org.hibernate.validator.constraints.NotBlank;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.List;

/**
* 类描述：基础资料-供应商 Controller类
* @address com.base.sbc.module.basicsdatum.web.BasicsdatumSupplierController
* @author mengfanjiang
* @email 2915350015@qq.com
* @date 创建时间：2023-5-22 10:51:07
* @version 1.0
*/
@RestController
@Api(tags = "基础资料-供应商")
@RequestMapping(value = BaseController.SAAS_URL + "/basicsdatumSupplier", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class BasicsdatumSupplierController{

	@Autowired
	private BasicsdatumSupplierService basicsdatumSupplierService;

	@ApiOperation(value = "分页查询")
	@GetMapping("/getBasicsdatumSupplierList")
	public PageInfo<BasicsdatumSupplierVo> getBasicsdatumSupplierList(QueryRevampBasicsdatumSupplierDto queryRevampBasicsdatumSupplierDto) {
		return  basicsdatumSupplierService.getBasicsdatumSupplierList(queryRevampBasicsdatumSupplierDto);
	}

	@ApiOperation(value = "/导入")
	@PostMapping("/basicsdatumSupplierImportExcel")
	public Boolean basicsdatumSupplierImportExcel(@RequestParam("file") MultipartFile file) throws Exception {
	return basicsdatumSupplierService.basicsdatumSupplierImportExcel(file);
	}

	@ApiOperation(value = "/导出")
	@GetMapping("/basicsdatumSupplierDeriveExcel")
	public void basicsdatumSupplierDeriveExcel(QueryRevampBasicsdatumSupplierDto queryRevampBasicsdatumSupplierDto,HttpServletResponse response) throws Exception {
       basicsdatumSupplierService.basicsdatumSupplierDeriveExcel(queryRevampBasicsdatumSupplierDto,response);
	}

	@ApiOperation(value = "批量启用/停用", notes = "ids:, status:0启用1停用")
	@PostMapping("/startStopBasicsdatumSupplier")
	public Boolean startStopBasicsdatumSupplier(@Valid @RequestBody StartStopDto startStopDto) {
	return basicsdatumSupplierService.startStopBasicsdatumSupplier(startStopDto);
	}


	@ApiOperation(value = "新增修改基础资料-供应商")
	@PostMapping("/addRevampBasicsdatumSupplier")
	public Boolean addRevampBasicsdatumSupplier(@Valid @RequestBody AddRevampBasicsdatumSupplierDto addRevampBasicsdatumSupplierDto) {
	return basicsdatumSupplierService.addRevampBasicsdatumSupplier(addRevampBasicsdatumSupplierDto);
	}

	@ApiOperation(value = "删除基础资料-供应商")
	@DeleteMapping("/delBasicsdatumSupplier")
	public Boolean delBasicsdatumSupplier(@Valid @NotBlank(message = "编号id不能为空") String id) {
	return basicsdatumSupplierService.delBasicsdatumSupplier(id);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/{id}")
	public BasicsdatumSupplier getById(@PathVariable("id") String id) {
		return basicsdatumSupplierService.getById(id);
	}


}































