/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.pdm.entity;
import com.base.sbc.config.common.base.BaseDataEntity;


/**
 * 类描述： 实体类
 * @address com.base.sbc.pdm.entity.Tag
 * @author lile
 * @email lilemyemail@163.com
 * @date 创建时间：2023-3-18 10:05:23
 * @version 1.0
 */
public class Tag extends BaseDataEntity<String> {

	private static final long serialVersionUID = 1L;
	/**********************************实体存放的其他字段区  不替换的区域 【other_start】******************************************/
	/**
	 * id集合
	 */
	private String[] ids;

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}
	/**********************************实体存放的其他字段区 【other_end】******************************************/

    /*****************************数据库字段区 不包含父类公共字段(属性)***********************************/
    /** 标签名称 */
    private String tagName;
    /** 标签分组id */
    private String groupId;
    /*******************************************getset方法区************************************/
    public String getTagName() {
		return tagName;
	}
	public Tag setTagNameAnd(String tagName) {
		this.tagName = tagName;
		return this;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
    public String getGroupId() {
		return groupId;
	}
	public Tag setGroupIdAnd(String groupId) {
		this.groupId = groupId;
		return this;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}


}
