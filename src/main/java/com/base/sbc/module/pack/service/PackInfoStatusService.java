/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.pack.service;

import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.module.pack.entity.PackInfoStatus;

/**
 * 类描述：资料包-状态 service类
 *
 * @author your name
 * @version 1.0
 * @address com.base.sbc.module.pack.service.PackInfoStatusService
 * @email your email
 * @date 创建时间：2023-7-13 9:17:47
 */
public interface PackInfoStatusService extends PackBaseService<PackInfoStatus> {

// 自定义方法区 不替换的区域【other_start】

    /**
     * 资料包状态
     *
     * @param foreignId
     * @param packType
     * @return
     */
    PackInfoStatus newStatus(String foreignId, String packType);

    /**
     * 锁定工艺信息
     *
     * @param foreignId
     * @param packType
     * @return
     */
    boolean lockTechSpec(String foreignId, String packType);

    /**
     * 解锁
     *
     * @param foreignId
     * @param packType
     * @return
     */
    boolean unlockTechSpec(String foreignId, String packType);

    /**
     * 工艺说明开始审批
     *
     * @param foreignId
     * @param packType
     * @return
     */
    boolean startApprovalForTechSpec(String foreignId, String packType);

    /**
     * 工艺说明 处理审批
     *
     * @param dto
     * @return
     */
    boolean approvalForTechSpec(AnswerDto dto);


    boolean enableFlagSetting(String foreignId, String packType, String enableFlag);

    boolean lockSize(String foreignId, String packType);

    boolean unlockSize(String foreignId, String packType);

    boolean startApprovalForSize(String foreignId, String packType);

    boolean approvalForSize(AnswerDto dto);

    /**
     * 通过大货款号获取工艺说明文件id
     *
     * @param styleNo
     * @return
     */
    String getTechSpecFileIdByStyleNo(String styleNo);


    /**
     * 检查是否锁定
     *
     * @param foreignId
     * @param packType
     * @param lockField
     * @return
     */
    void checkLock(String foreignId, String packType, String lockField);
// 自定义方法区 不替换的区域【other_end】


}

