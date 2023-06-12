package com.base.sbc.config.common.base;

import cn.hutool.core.lang.Opt;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Youkehai
 * @data 创建时间:2021/4/2
 */

@Data
@ApiModel("分页组件")
public class Page implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int PAGE_NUM = 1;
    public static final int PAGE_SIZE = 10;


    @ApiModelProperty(value = "第几页", example = "1")
    @NotNull(message = "不能为空")
    private int pageNum;
    @ApiModelProperty(value = "每页数量", example = "10")
    @NotNull(message = "不能为空")
    private int pageSize;
    @ApiModelProperty(value = "排序(单表)", example = "create_date desc")
    private String orderBy;
    @ApiModelProperty(value = "关键字搜索", example = "")
    private String search;
    @ApiModelProperty(value = "状态", example = "")
    private String status;

    public String getSql() {
        return null;
    }

    public Boolean isAsc() {
        return Opt.ofBlankAble(orderBy).map(o -> o.toUpperCase().contains("ASC")).orElse(false);
    }

    public String getOrderByColumn() {
        return Opt.ofBlankAble(orderBy).map(o -> o.toUpperCase().replace("ASC", "").replace("DESC", "")).orElse(null);
    }

}
