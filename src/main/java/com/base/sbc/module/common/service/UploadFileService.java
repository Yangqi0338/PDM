/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.common.service;

import com.base.sbc.module.common.dto.UploadStylePicDto;
import com.base.sbc.module.common.entity.UploadFile;
import com.base.sbc.module.common.vo.AttachmentVo;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * 类描述：上传文件 service类
 *
 * @author lxl
 * @version 1.0
 * @address com.base.sbc.module.common.service.UploadFileService
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-5-12 15:16:14
 */
public interface UploadFileService extends BaseService<UploadFile> {

/** 自定义方法区 不替换的区域【other_start】 **/

    /**
     * 上传到minio
     *
     * @param file
     * @return
     */
    AttachmentVo uploadToMinio(MultipartFile file);

    /**
     * 上传到minio
     *
     * @param bufferedImage
     * @return
     */
    AttachmentVo uploadToMinio(BufferedImage bufferedImage, String fileName);

    /**
     * 通过md5查找文件
     *
     * @param md5
     * @return
     */
    UploadFile findByMd5(String md5);

    /**
     * 获取文件id
     *
     * @param fileUrls
     * @return key = url,val=id
     */
    Map<String, String> findMapByUrls(List<String> fileUrls);

    String getIdByUrl(String url);

    String getUrlById(String id);

    boolean delByUrl(String url);

    /**
     * 上传款式图
     * @return
     */
    String uploadStyleImage(UploadStylePicDto uploadStylePicDto, Principal user) throws Exception;

/** 自定义方法区 不替换的区域【other_end】 **/


}
