package com.base.sbc.module.band.controller;

import cn.hutool.core.util.StrUtil;
import com.base.sbc.module.band.dto.BandSaveDto;
import com.base.sbc.module.band.dto.BandStartStopDto;
import com.base.sbc.module.band.vo.BandQueryReturnVo;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.QueryCondition;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.config.common.base.Page;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.band.entity.Band;
import com.base.sbc.module.band.service.BandService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/18 17:54:12
 */
@RestController
@Api(tags = "1.2 SAAS接口[标签]")
@RequestMapping(value = BaseController.SAAS_URL + "/band", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class BandController extends BaseController {
    @Resource
    private BandService bandService;

    /**
     * 新增波段
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增波段", notes = "id为空")
    public String add(@Valid @RequestBody BandSaveDto bandSaveDto) {
        return bandService.add(bandSaveDto);
    }

    /**
     * 条件查询列表
     */
    @GetMapping("/listQuery")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "search", value = "查询字段", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "排序", required = false, dataType = "String", paramType = "query")
    })
    public ApiResult listQuery(@RequestHeader(BaseConstant.USER_COMPANY) String userCompany, Page page) {
        QueryCondition qc = new QueryCondition();
        qc.andEqualTo("company_code", userCompany);
        qc.andEqualTo("del_flag", "0");
        if (StringUtils.isNotBlank(page.getSearch())) {
            qc.andLikeOr(page.getSearch(), "band_name", "code");
        }
        if (!StringUtils.isEmpty(page.getOrder())){
            qc.setOrderByClause(page.getOrder());
        }else {
            qc.setOrderByClause("create_date desc");
        }
        if (page.getPageNum() != 0 && page.getPageSize() != 0) {
            com.github.pagehelper.Page<BandQueryReturnVo> basicLabelUseScopePage = PageHelper.startPage(page.getPageNum(), page.getPageSize());
            bandService.findByCondition(qc);
            PageInfo<BandQueryReturnVo> pages = basicLabelUseScopePage.toPageInfo();
            List<BandQueryReturnVo> list = pages.getList();
            if (list != null && list.size() > 0) {
                return ApiResult.success("success", pages);
            }
            return ApiResult.error(HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND.value());
        } else {
            List<BandQueryReturnVo> bandQueryReturnVoList = bandService.selectList2("selectbaseList", qc);
            return ApiResult.success("success", bandQueryReturnVoList);
        }


    }

    /**
     * 根据id查询
     */
    @GetMapping("/getById")
    @ApiImplicitParams({  @ApiImplicitParam(name = "id", value = "波段id", required = true, dataType = "String"), })
    @ApiOperation(value = "id查询波段", notes = "id必填")
    public ApiResult getById(String id) {
        Band band=  bandService.getById(id);
        BandQueryReturnVo bandQueryReturnVo=new BandQueryReturnVo();
        BeanUtils.copyProperties(band,bandQueryReturnVo);
        return selectSuccess(bandQueryReturnVo);
    }

    /**
     * 批量删除
     */
    @DeleteMapping("delByIds")
    @ApiImplicitParams({  @ApiImplicitParam(name = "ids", value = "删除波段", required = true, dataType = "String[]"), })
    @ApiOperation(value = "删除波段", notes = "ids必填")
    public ApiResult delByIds(String[] ids) {
        return deleteSuccess(bandService.delByIds(ids));
    }
    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改波段", notes = "必填")
    public ApiResult update(@Valid @RequestBody BandSaveDto bandSaveDto) {
        if (StringUtils.isEmpty(bandSaveDto.getId())){
            throw new OtherException("Id不能为空");
        }
        Band band=  bandService.getById(bandSaveDto.getId());
        if (band==null){
            throw new OtherException("查无数据");
        }
        BeanUtils.copyProperties(bandSaveDto,band);
        return updateSuccess(bandService.update(band));
    }
    /**
     * 启动 停止
     */
    @ApiOperation(value = "批量启用/停用波段", notes = "ids:波段ids(多个用逗号拼接), status:0启用1停用")
    @PostMapping("bandStartStop")
    public ApiResult bandStartStop(@Valid @RequestBody BandStartStopDto bandStartStopDto) {
        return deleteSuccess(bandService.bandStartStop(bandStartStopDto));
    }

    @GetMapping("/queryBand")
    public ApiResult queryBand(BandSaveDto dto){
        QueryCondition qc=new QueryCondition(getUserCompany());
        if(StrUtil.isNotBlank(dto.getParticularYear())){
            qc.andEqualTo("particular_year",dto.getParticularYear());
        }
        if(StrUtil.isNotBlank(dto.getSeason())){
            qc.andEqualTo("season",dto.getSeason());
        }
        if(StrUtil.isNotBlank(dto.getCode())){
            qc.andEqualTo("code",dto.getCode());
        }
        if(StrUtil.isNotBlank(dto.getMonth())){
            qc.andEqualTo("month",dto.getMonth());
        }
        List<Band> bandList = bandService.findByCondition(qc);
        return selectSuccess(bandList);
    }

}