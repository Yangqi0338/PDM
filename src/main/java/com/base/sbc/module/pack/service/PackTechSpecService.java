/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service;

import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.pack.dto.PackTechSpecDto;
import com.base.sbc.module.pack.dto.PackTechSpecSavePicDto;
import com.base.sbc.module.pack.dto.PackTechSpecSearchDto;
import com.base.sbc.module.pack.entity.PackTechSpec;
import com.base.sbc.module.pack.vo.PackTechSpecVo;

import java.util.List;

/**
 * 类描述：资料包-工艺说明 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackTechSpecService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-7-5 15:41:45
 */
public interface PackTechSpecService extends BaseService<PackTechSpec> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 列表
     *
     * @param dto
     * @return
     */
    List<PackTechSpecVo> list(PackTechSpecSearchDto dto);

    /**
     * 保存
     *
     * @param dto
     * @return
     */
    PackTechSpecVo saveByDto(PackTechSpecDto dto);

    /**
     * 排序
     *
     * @param id
     * @return
     */
    boolean sort(String id);

    /**
     * 图片列表
     *
     * @param dto
     * @return
     */
    List<AttachmentVo> picList(PackTechSpecSearchDto dto);

    /**
     * 保存图片
     *
     * @param dto
     * @return
     */
    AttachmentVo savePic(PackTechSpecSavePicDto dto);

// 自定义方法区 不替换的区域【other_end】


}
