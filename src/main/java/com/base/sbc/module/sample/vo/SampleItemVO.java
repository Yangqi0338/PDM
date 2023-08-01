package com.base.sbc.module.sample.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("样衣明细")
@Data
public class SampleItemVO {
    /**
     * 状态
     */
    @ApiModelProperty("状态：0-未入库，1-在库，2-借出，3-删除，4-售出，5-盘点中")
    private String status;

    /**
     * 样衣编码
     */
    @ApiModelProperty("样衣编码")
    private String code;
    /**
     * 颜色
     */
    @ApiModelProperty("颜色")
    private String color;
    /**
     * 尺码
     */
    @ApiModelProperty("尺码")
    private String size;
    /**
     * 入库时间
     */
    @ApiModelProperty("入库时间")
    private String storeDate;
    /**
     * 位置
     */
    @ApiModelProperty("样衣编码")
    private String position;
    /**
     * 图片
     */
    @ApiModelProperty("图片")
    private String images;
    /**
     * 设计款号
     */
    @ApiModelProperty("设计款号")
    private String designNo;
    /**
     * 款式名称
     */
    @ApiModelProperty("款式名称")
    private String styleName;
    /**
     * 款式品类
     */
    @ApiModelProperty("款式品类")
    private String categoryName;
    /**
     * 样衣类型
     */
    @ApiModelProperty("样衣类型：1-内部研发，2-外采，2-ODM提供")
    private String type;
    /**
     * 样衣来源
     */
    @ApiModelProperty("样衣来源：1-新增，2-导入，3-外部")
    private String fromType;

}
