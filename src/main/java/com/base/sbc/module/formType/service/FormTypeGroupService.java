/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.formType.service;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.formType.dto.SaveUpdateFormTypeGroupDto;
import com.base.sbc.module.formType.entity.FormTypeGroup;

/** 
 * 类描述：表单类型分组 service类
 * @address com.base.sbc.module.formType.service.FormTypeGroupService
 * @author lxl
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-4-15 9:17:05
 * @version 1.0  
 */
public interface FormTypeGroupService extends BaseService<FormTypeGroup> {



    ApiResult saveUpdateGroup(SaveUpdateFormTypeGroupDto formTypeGroupDto);

    ApiResult  getGroupIsCoding();

	
}
