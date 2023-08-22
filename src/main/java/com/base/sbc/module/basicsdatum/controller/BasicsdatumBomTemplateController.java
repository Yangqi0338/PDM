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
import com.base.sbc.module.basicsdatum.dto.AddRevampBomTemplateDto;
import com.base.sbc.module.basicsdatum.dto.QueryBomTemplateDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumBomTemplate;
import com.base.sbc.module.basicsdatum.service.BasicsdatumBomTemplateMaterialService;
import com.base.sbc.module.basicsdatum.service.BasicsdatumBomTemplateService;
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
* 类描述：基础资料-BOM模板 Controller类
* @address com.base.sbc.module.basicsdatum.web.BasicsdatumBomTemplateController
* @author mengfanjiang
* @email XX.com
* @date 创建时间：2023-8-22 17:27:44
* @version 1.0
*/
@RestController
@Api(tags = "基础资料-BOM模板")
@RequestMapping(value = BaseController.SAAS_URL + "/basicsdatumBomTemplate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class BasicsdatumBomTemplateController{

	@Autowired
	private BasicsdatumBomTemplateService basicsdatumBomTemplateService;

	@Autowired
	private BasicsdatumBomTemplateMaterialService basicsdatumBomTemplateMaterialServicel;

	@ApiOperation(value = "分页查询BOM模板")
	@GetMapping("/getBomTemplateList")
	public PageInfo getBomTemplateList(QueryBomTemplateDto queryBomTemplateDto) {
		return basicsdatumBomTemplateService.getBomTemplateList(queryBomTemplateDto);
	}

	@ApiOperation(value = "分页查询模板物料信息")
	@GetMapping("/getBomTemplateMateriaList")
	public PageInfo getBomTemplateMateriaList(QueryBomTemplateDto queryBomTemplateDto) {
		return basicsdatumBomTemplateMaterialServicel.getBomTemplateMateriaList(queryBomTemplateDto);
	}




	@ApiOperation(value = "新增或修改BOM模板")
	@PostMapping("/addRevampBomTemplate")
	public Boolean addRevampBomTemplate(@Valid @RequestBody AddRevampBomTemplateDto addRevampBomTemplateDto) {
		return basicsdatumBomTemplateService.addRevampBomTemplate(addRevampBomTemplateDto);
	}



	@ApiOperation(value = "删除基础资料-BOM模板")
	@DeleteMapping("/delBomTemplate")
	public Boolean delBomTemplate(@Valid @NotBlank(message = "编号id不能为空") String id) {
		return basicsdatumBomTemplateService.delBomTemplate(id);
	}






}































