package com.base.sbc.module.basicsdatum.service;

import com.base.sbc.module.basicsdatum.dto.ProcessDatabasePageDto;
import com.base.sbc.module.basicsdatum.entity.ProcessDatabase;
import com.base.sbc.module.common.service.IServicePlus;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author 卞康
 * @date 2023/6/5 9:23:54
 * @mail 247967116@qq.com
 */
public interface ProcessDatabaseService extends IServicePlus<ProcessDatabase> {
    /**
     * 导入
     * @param file 文件
     * @return 成功或者失败
     */
    Boolean importExcel(MultipartFile file) throws Exception;

    /**
     * 分页查询
     * @param pageDto 查询条件对象
     * @return 分页对象
     */
    PageInfo<ProcessDatabase> listPage(ProcessDatabasePageDto pageDto);
}