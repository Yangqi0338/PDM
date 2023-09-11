/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.basicsdatum.service;

import com.base.sbc.module.basicsdatum.dto.AddRevampBasicsdatumModelTypeDto;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumModelType;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumModelTypeVo;
import com.base.sbc.module.common.service.BaseService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 类描述：基础资料-号型类型 service类
 *
 * @author mengfanjiang
 * @version 1.0
 * @address com.base.sbc.module.basicsdatum.service.BasicsdatumModelTypeService
 * @email 2915350015@qq.com
 * @date 创建时间：2023-5-20 9:31:14
 */
public interface BasicsdatumModelTypeService extends BaseService<BasicsdatumModelType> {

/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 方法描述：分页查询部件
     *
     * @param queryDto 查询条件
     * @return PageInfo<BasicsdatumComponentVo>
     */
    PageInfo getBasicsdatumModelTypeList(QueryDto queryDto);


    /**
     * 基础资料-号型类型导入
     *
     * @param file
     * @return
     */
    Boolean basicsdatumModelTypeImportExcel(MultipartFile file) throws IOException, Exception;

    /**
     * 基础资料-号型类型导出
     *
     * @param response
     * @return
     */
    void basicsdatumModelTypeDeriveExcel(HttpServletResponse response) throws Exception;


    /**
     * 方法描述：新增修改基础资料-号型类型
     *
     * @param addRevampBasicsdatumModelTypeDto 部件Dto类
     * @return boolean
     */
    Boolean addRevampBasicsdatumModelType(AddRevampBasicsdatumModelTypeDto addRevampBasicsdatumModelTypeDto);


    /**
     * 方法描述：删除基础资料-号型类型
     *
     * @param id （多个用，）
     * @return boolean
     */
    Boolean delBasicsdatumModelType(String id);


    /**
     * 方法描述：启用停止基础资料-号型类型
     *
     * @param startStopDto 启用停止Dto类
     * @return boolean
     */
    Boolean startStopBasicsdatumModelType(StartStopDto startStopDto);

    /**
     * 通过id获取名称
     *
     * @param id
     * @return
     */
    String getNameById(String id);

    /**
     * 通过code查询号型类型
     *
     * @param code
     * @return
     */
    List<BasicsdatumModelType> queryByCode(String companyCode, String code);
/** 自定义方法区 不替换的区域【other_end】 **/


}
