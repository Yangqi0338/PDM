/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumLavationReminder;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumLavationReminderVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumWashIconVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类描述：基础资料-洗涤图标与温馨提示 dao类
 * @address com.base.sbc.module.basicsdatum.dao.BasicsdatumLavationReminderDao
 * @author mengfanjiang  
 * @email  2915350015@qq.com
 * @date 创建时间：2023-5-19 19:15:00 
 * @version 1.0  
 */
@Mapper
public interface BasicsdatumLavationReminderMapper extends BaseMapper<BasicsdatumLavationReminder> {
/** 自定义方法区 不替换的区域【other_start】 **/

List<BasicsdatumLavationReminderVo> getLavationReminderList(@Param(Constants.WRAPPER) QueryWrapper<BasicsdatumLavationReminder> qw);



/** 自定义方法区 不替换的区域【other_end】 **/
}