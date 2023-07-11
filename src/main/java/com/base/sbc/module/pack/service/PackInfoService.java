/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service;

import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.module.operaLog.entity.OperaLogEntity;
import com.base.sbc.module.pack.dto.PackCommonPageSearchDto;
import com.base.sbc.module.pack.dto.PackCommonSearchDto;
import com.base.sbc.module.pack.dto.PackInfoSearchPageDto;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.vo.PackInfoListVo;
import com.base.sbc.module.pack.vo.SampleDesignPackInfoListVo;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 类描述：资料包 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackInfoService
 * @email your email
 * @date 创建时间：2023-7-6 17:13:01
 */
public interface PackInfoService extends PackBaseService<PackInfo> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 通过样衣设计查询
     *
     * @param pageDto
     * @return
     */
    PageInfo<SampleDesignPackInfoListVo> pageBySampleDesign(PackInfoSearchPageDto pageDto);

    /**
     * 通过样衣设计创建BOM基础信息
     *
     * @param id
     * @return
     */
    PackInfoListVo createBySampleDesign(String id);

    /**
     * 查询
     *
     * @param foreignIds
     * @return
     */
    Map<String, List<PackInfoListVo>> queryListToMapGroupByForeignId(List<String> foreignIds);

    /**
     * 修改日志
     *
     * @param pageDto
     * @return
     */
    PageInfo<OperaLogEntity> operationLog(PackCommonPageSearchDto pageDto);

    /**
     * 技术BOM 转 大货
     *
     * @param dto@return
     */
    boolean toBigGoods(PackCommonSearchDto dto);

    /**
     * 资料包拷贝
     *
     * @param sourceForeignId 源主数据id
     * @param sourcePackType  源资料包类型
     * @param targetForeignId 目标主数据id
     * @param targetPackType  目标资料包类型
     * @return
     */
    boolean copyPack(String sourceForeignId, String sourcePackType, String targetForeignId, String targetPackType);

    /**
     * 开启审批
     *
     * @param id
     * @return
     */
    boolean startApproval(String id);

    /**
     * 处理反审
     *
     * @param dto
     * @return
     */
    boolean approval(AnswerDto dto);


// 自定义方法区 不替换的区域【other_end】


}
