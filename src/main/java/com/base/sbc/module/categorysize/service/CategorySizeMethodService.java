package com.base.sbc.module.categorysize.service;

import com.base.sbc.module.categorysize.entity.CategorySizeMethod;
import com.base.sbc.module.common.service.BaseService;

import java.util.List;

/**
 * @author 卞康
 * @data 2023/4/6 15:22
 */

public interface CategorySizeMethodService extends BaseService<CategorySizeMethod> {
    Integer updateList(List<CategorySizeMethod> categorySizeMethodList, String categoryName);
}
