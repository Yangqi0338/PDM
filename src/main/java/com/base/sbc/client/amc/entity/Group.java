/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.client.amc.entity;

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/** 
 * 类描述：用户组 实体类
 * @address com.base.sbc.company.entity.Group
 * @author 卞康
 * @email 247967116@qq.com
 * @date 创建时间：2023-4-20 16:13:25
 * @version 1.0  
 */
public class Group extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
	
	
	/**********************************实体存放的其他字段区 【other_end】******************************************/
	
    /*****************************数据库字段区 不包含父类公共字段(属性)***********************************/
    /** 用户组名称 */
    private String name;
    /*******************************************getset方法区************************************/
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}

