/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.sample.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.module.sample.vo.SampleStyleColorVo;
import com.base.sbc.module.sample.vo.StyleUploadVo;
import org.apache.ibatis.annotations.Mapper;
import com.base.sbc.module.sample.entity.SampleStyleColor;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：样衣-款式配色 dao类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.sample.dao.SampleStyleColorDao
 * @email XX.com
 * @date 创建时间：2023-6-28 15:02:46
 */
@Mapper
public interface SampleStyleColorMapper extends BaseMapper<SampleStyleColor> {
// 自定义方法区 不替换的区域【other_start】

    /*查询大货款号是否存在*/
    int isStyleNoExist(String styleNo);

    /*查询款式下的额配色*/
    int getStyleColorNumber(String sampleDesignId);

    /**
     *查询款式下的配色
     */
    List<SampleStyleColorVo> getSampleStyleColorList(@Param(Constants.WRAPPER) QueryWrapper qw ,@Param("meetFlag") String meetFlag);

//    修改大货款号
    Boolean reviseAllStyleNo(@Param("styleNo") String styleNo ,@Param("nweStyleNo") String nweStyleNo);

    /**
     * 获取款式图上传信息
     * @param styleColorId
     * @return
     */
    StyleUploadVo getStyleUploadInfo(String styleColorId);

// 自定义方法区 不替换的区域【other_end】
}