/******************************************************************************
* Copyright (C) 2018 广州尚捷科技有限责任公司
* All Rights Reserved.
* 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
* 不得使用、复制、修改或发布本软件.
*****************************************************************************/
package com.base.sbc.module.style.controller;

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
import com.base.sbc.module.style.dto.StyleOrderBookDesignUpdateDto;
import com.base.sbc.module.style.dto.StyleOrderBookPlanUpdateDto;
import com.base.sbc.module.style.dto.StyleOrderBookPriceUpdateDto;
import com.base.sbc.module.style.dto.StyleOrderBookProductPlanUpdateDto;
import com.base.sbc.module.style.dto.StyleOrderBookQueryDto;
import com.base.sbc.module.style.dto.StyleOrderBookSaveDto;
import com.base.sbc.module.style.dto.StyleOrderBookUpdateDto;
import com.base.sbc.module.style.dto.StyleOrderBookUserUpdateDto;
import com.base.sbc.module.style.service.StyleOrderBookService;
import com.base.sbc.module.style.vo.StyleOrderBookPageVo;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 类描述：款式订货本相关接口
 */
@RestController
@Api(tags = "款式订货本相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/styleOrderBook", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class StyleOrderBookController {

	@Autowired
	private StyleOrderBookService sampleStyleOrderBookService;

	@ApiOperation(value = "款式订货本列表:子表左关联主表和配色表等")
	@GetMapping("/getStyleOrderBookList")
	public PageInfo<StyleOrderBookPageVo> getStyleOrderBookList(StyleOrderBookQueryDto dto) {
		return sampleStyleOrderBookService.getStyleOrderBookList(dto);
	}

	@ApiOperation(value = "款式订货本列表:创建选择集合、添加新款到集合(订货本编码)")
	@PostMapping("/saveSampleStyleOrderBook")
	public Boolean saveSampleStyleOrderBook(@Valid @RequestBody StyleOrderBookSaveDto dto) {
		return sampleStyleOrderBookService.saveSampleStyleOrderBook(dto);
	}

	@ApiOperation(value = "款式订货本列表:修改图片/修改状态：锁定、解锁/修改状态：上会、撤销")
	@PostMapping("/updateSampleStyleOrderBook")
	public Boolean updateSampleStyleOrderBook(@Valid @RequestBody StyleOrderBookUpdateDto dto) {
		return sampleStyleOrderBookService.updateSampleStyleOrderBook(dto);
	}

	@ApiOperation(value = "款式订货本列表：删除明细")
	@DeleteMapping("/delSampleStyleOrderBookItem")
	public Boolean delSampleStyleOrderBookItem(@RequestParam(value = "id") @NotBlank(message = "id不能为空") String id) {
		return sampleStyleOrderBookService.delSampleStyleOrderBookItem(id);
	}

	@ApiOperation(value = "款式订货本列表:修改吊牌价（套装-配套/单款-配色）")
	@PostMapping("/updateSampleStyleOrderBookPrice")
	public Boolean updateSampleStyleOrderBookPrice(@Valid @RequestBody StyleOrderBookPriceUpdateDto dto) {
		return sampleStyleOrderBookService.updateSampleStyleOrderBookPrice(dto);
	}

	@ApiOperation(value = "款式订货本列表:分配接口")
	@PostMapping("/updateSampleStyleOrderBookUser")
	public Boolean updateSampleStyleOrderBookUser(@Valid @RequestBody StyleOrderBookUserUpdateDto dto) {
		return sampleStyleOrderBookService.updateSampleStyleOrderBookUser(dto);
	}

	@ApiOperation(value = "款式订货本列表:设计师提交")
	@PostMapping("/updateSampleStyleOrderBookDesign")
	public Boolean updateSampleStyleOrderBookDesign(@Valid @RequestBody StyleOrderBookDesignUpdateDto dto) {
		return sampleStyleOrderBookService.updateSampleStyleOrderBookDesign(dto);
	}

	@ApiOperation(value = "款式订货本列表:企划提交")
	@PostMapping("/updateSampleStyleOrderBookPlan")
	public Boolean updateSampleStyleOrderBookPlan(@Valid @RequestBody StyleOrderBookPlanUpdateDto dto) {
		return sampleStyleOrderBookService.updateSampleStyleOrderBookPlan(dto);
	}

	@ApiOperation(value = "款式订货本列表:商品企划提交")
	@PostMapping("/updateSampleStyleOrderBookProductPlan")
	public Boolean updateSampleStyleOrderBookProductPlan(
			@Valid @RequestBody StyleOrderBookProductPlanUpdateDto dto) {
		return sampleStyleOrderBookService.updateSampleStyleOrderBookProductPlan(dto);
	}

}