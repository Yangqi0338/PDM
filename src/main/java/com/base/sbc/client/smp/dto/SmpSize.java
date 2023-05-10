package com.base.sbc.client.smp.dto;

import lombok.Data;

/**
 * @author 卞康
 * @date 2023/5/9 17:50:56
 * @mail 247967116@qq.com
 */
@Data
public class  SmpSize {
    /**尺寸(XS、S)*/
    private String size;
    /**尺寸编号(5、1)*/
    private String sizeNumber;
    /**尺寸描述*/
    private String sizeDescription;
    /**吊牌显示*/
    private String productSizeName;
    /**基码标识*/
    private String baseSize;
    /**充绒量/克重*/
    private String SKUFiller;
    /**特殊规格*/
    private String specialSpec;
}
