/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;

import com.base.sbc.module.basicsdatum.dto.AddRevampConfigPrintDto;
import com.base.sbc.module.basicsdatum.dto.QueryRevampConfigPrintDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.ConfigPrint;
import com.base.sbc.module.basicsdatum.vo.ConfigPrintVo;
import com.base.sbc.module.common.service.BaseService;
import com.github.pagehelper.PageInfo;

/** 
 * 类描述：打印配置 service类
 * @address com.base.sbc.module.basicsdatum.service.ConfigPrintService
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 创建时间：2023-6-21 10:29:11
 * @version 1.0  
 */
public interface ConfigPrintService extends BaseService<ConfigPrint> {


// 自定义方法区 不替换的区域【other_start】

	PageInfo<ConfigPrintVo> getConfigPrintList(QueryRevampConfigPrintDto queryRevampConfigPrintDto);

	Boolean startStopConfigPrint(StartStopDto startStopDto);

	Boolean addRevampConfigPrint(AddRevampConfigPrintDto addRevampConfigPrintDto);

	Boolean delConfigPrint(String id);

// 自定义方法区 不替换的区域【other_end】

	
}

