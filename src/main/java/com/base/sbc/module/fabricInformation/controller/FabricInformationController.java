/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.fabricInformation.controller;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.fabricInformation.dto.QueryFabricInformationDto;
import com.base.sbc.module.fabricInformation.dto.SaveUpdateFabricDetailedInformationDto;
import com.base.sbc.module.fabricInformation.dto.SaveUpdateFabricBasicInformationDto;
import com.base.sbc.module.fabricInformation.entity.FabricBasicInformation;
import com.base.sbc.module.fabricInformation.service.FabricBasicInformationService;
import com.base.sbc.module.fabricInformation.service.FabricDetailedInformationService;
import com.base.sbc.module.fabricInformation.vo.FabricInformationVo;
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
* 类描述：面料信息 Controller类
* @address com.base.sbc.module.fabricInformation.web.FabricBasicInformationController
* @author lxl
* @email lxl.fml@gmail.com
* @date 创建时间：2023-4-19 18:23:26
* @version 1.0
*/
@RestController
@Api(tags = "面料基本信息")
@RequestMapping(value = BaseController.SAAS_URL + "/fabricInformation", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class FabricInformationController {

	@Autowired
	private FabricBasicInformationService fabricBasicInformationService;
	@Autowired
	private FabricDetailedInformationService fabricDetailedInformationService;

	@ApiOperation(value = "分页查询面料信息 ")
	@GetMapping("/getFabricInformationList")
	public PageInfo<FabricInformationVo> getFabricInformationList(QueryFabricInformationDto queryFabricInformationDto) {
		return fabricBasicInformationService.getFabricInformationList(queryFabricInformationDto);
	}

	@ApiOperation(value = "明细-通过id查询")
	@GetMapping("/getById/{id}")
	public ApiResult getById(@PathVariable("id") String id) {
		return fabricBasicInformationService.getById(id);
	}

	@ApiOperation(value = "删除-通过id查询,多个逗号分开")
	@DeleteMapping("/delFabric")
	public ApiResult delFabric(String id) {
		return fabricBasicInformationService.delFabric(id);
	}


	/*设计师*/
	@ApiOperation(value = "保存修改面料基本信息")
	@PostMapping("/saveUpdateFabricBasic")
	public ApiResult saveUpdateFabricBasic(@Valid @RequestBody SaveUpdateFabricBasicInformationDto saveUpdateFabricBasicDto){
	return 	fabricBasicInformationService.saveUpdateFabricBasic(saveUpdateFabricBasicDto);
	}


	/*辅料专员*/
	@ApiOperation(value = "保存修改面料详细信息")
	@PostMapping("/saveUpdateFabricDetailed")
	public ApiResult saveUpdateFabricDetailed(@Valid @RequestBody SaveUpdateFabricDetailedInformationDto saveUpdateFabricBasicDto){
		return	fabricDetailedInformationService.saveUpdateFabricDetailed(saveUpdateFabricBasicDto);
	}


}































