package com.base.sbc.open.controller;

import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseController;
import com.base.sbc.open.entity.BiSizeChart;
import com.base.sbc.open.service.BiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/7/24 10:06:19
 * @mail 247967116@qq.com
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = BaseController.OPEN_URL + "/bi", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class BiController extends BaseController{
    private final BiService biService;
    @GetMapping("/sizeChart")
    public ApiResult sizeChart(){
        List<BiSizeChart> biSizeCharts = biService.sizeChart();
        return selectSuccess(biSizeCharts);
    }
}
