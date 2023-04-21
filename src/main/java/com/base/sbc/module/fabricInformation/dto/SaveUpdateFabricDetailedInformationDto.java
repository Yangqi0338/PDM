package com.base.sbc.module.fabricInformation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@ApiModel("保存修改面料详细信息 FabricDetailedInformationDto")
public class SaveUpdateFabricDetailedInformationDto {

    private String id;

    private String fabricBasicId;
    private String  fabricDetailedId;

    /** 面料是否可用(0是，1否) */
    @ApiModelProperty(value = "面料是否可用(0是，1否)"  )
    private String fabricIsUsable;
    /** 面料价格 */
    @ApiModelProperty(value = "面料价格"  )
    private Integer fabricPrice;
    /** 纱支规格 */
    @ApiModelProperty(value = "纱支规格"  )
    private String specification;
    /** 密度 */
    @ApiModelProperty(value = "密度"  )
    private String density;
    /** 面料成分 */
    @ApiModelProperty(value = "面料成分"  )
    private String ingredient;
    /** 货期 */
    @ApiModelProperty(value = "货期"  )
    private String leadtime;
    /** 起订量 */
    @ApiModelProperty(value = "起订量"  )
    private Integer minimumOrderQuantity;
    /** 门幅 */
    @ApiModelProperty(value = "门幅"  )
    private String larghezza;
    /** 克重 */
    @ApiModelProperty(value = "克重"  )
    private Integer gramWeight;
    /** 胚布情况 */
    @ApiModelProperty(value = "胚布情况"  )
    private String germinalCondition;
    /** 调样日期 */
    @ApiModelProperty(value = "调样日期"  )
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date atactiformDate;
    /** 预估到样时间 */
    @ApiModelProperty(value = "预估到样时间"  )
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date estimateAtactiformDate;
    /** 实际到样时间 */
    @ApiModelProperty(value = "实际到样时间"  )
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date practicalAtactiformDate;
    /** 留样送检时间 */
    @ApiModelProperty(value = "留样送检时间"  )
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inspectDate;
    /** 理化检测结果（0是1否 */
    @ApiModelProperty(value = "理化检测结果（0是1否"  )
    private String physicochemistryDetectionResult;
    /** 样衣试穿洗涤送检时间 */
    @ApiModelProperty(value = "样衣试穿洗涤送检时间"  )
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sampleWashingInspectionDate;
    /** 洗涤检测结果（0是1否 */
    @ApiModelProperty(value = "洗涤检测结果（0是1否"  )
    private String washDetectionResult;
    /** 图片地址 */
    @ApiModelProperty(value = "图片地址"  )
    private String imageUrl;
    /** 是否草稿（0是：1否 */
    @ApiModelProperty(value = "是否草稿（0是：1否"  )
    private String isDraft;
}
