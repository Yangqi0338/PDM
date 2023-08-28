package com.base.sbc.module.planning.controller;

import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.module.planning.dto.GetStyleNoListDto;
import com.base.sbc.module.planning.dto.PlanningBoardSearchDto;
import com.base.sbc.module.planning.service.PlanningSeasonService;
import com.base.sbc.module.planning.vo.PlanningSummaryDetailVo;
import com.base.sbc.module.planning.vo.PlanningSummaryVo;
import com.base.sbc.module.style.vo.StyleColorVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 类描述：企划看板相关接口
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.planning.controller.PlanningBoardController
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-27 09:23
 */
@RestController
@Api(tags = "企划看板-相关接口")
@RequestMapping(value = BaseController.SAAS_URL + "/planningBoard", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class PlanningBoardController {


    @Resource
    private PlanningSeasonService planningSeasonService;


    @ApiOperation(value = "企划汇总", notes = "")
    @GetMapping("/planningSummary")
    public PlanningSummaryVo planningSummary(@Valid PlanningBoardSearchDto dto) {
        return planningSeasonService.planningSummary(dto);
    }
    @ApiOperation(value = "获取大货款号", notes = "用于关联坑位")
    @PostMapping("/getStyleNoList")
    public List<StyleColorVo> getStyleNoList(@RequestBody GetStyleNoListDto dto){
        return planningSeasonService.getStyleNoList(dto);
    }

    @ApiOperation(value = "品类汇总", notes = "")
    @GetMapping("/bandSummary")
    public List bandSummary(PlanningBoardSearchDto dto) {
        return planningSeasonService.bandSummary(dto);
    }

    @ApiOperation(value = "参考历史款明细", notes = "")
    @GetMapping("/hisDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hisDesignNo", value = "历史款", required = true, paramType = "query")
    })
    public PlanningSummaryDetailVo hisDetail(@Valid @NotBlank(message = "历史款id不能为空") String hisDesignNo) {
        return planningSeasonService.hisDetail(hisDesignNo);
    }
}
