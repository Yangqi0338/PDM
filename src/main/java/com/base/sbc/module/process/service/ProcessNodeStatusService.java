/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.process.service;

import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.process.dto.AddRevampProcessNodeStatusDto;
import com.base.sbc.module.process.dto.QueryNodeStatusDto;
import com.base.sbc.module.process.entity.ProcessNodeStatus;
import com.base.sbc.module.process.vo.ProcessNodeStatusVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/** 
 * 类描述：流程配置-节点状态 service类
 * @address com.base.sbc.module.process.service.ProcessNodeStatusService
 * @author mengfanjiang
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-2 20:15:15
 * @version 1.0  
 */
public interface ProcessNodeStatusService extends BaseService<ProcessNodeStatus> {

// 自定义方法区 不替换的区域【other_start】

        /**
        * 方法描述：分页查询部件
        *
        * @param queryNodeStatusDto 查询条件
        * @return PageInfo<BasicsdatumComponentVo>
         */
        PageInfo<ProcessNodeStatusVo> getProcessNodeStatusList(QueryNodeStatusDto queryNodeStatusDto);




        /**
        * 方法描述：新增修改流程配置-节点状态
        *
        * @param addRevampProcessNodeStatusDto 部件Dto类
        * @return boolean
        */
        Boolean addRevampProcessNodeStatus(AddRevampProcessNodeStatusDto addRevampProcessNodeStatusDto);


        /**
         * 方法描述：批量新增修改流程配置-节点状态
         *
         * @param addRevampProcessNodeStatusDto 部件Dto类
         * @return boolean
         */
        List<ProcessNodeStatusVo>   batchAddRevamp(List<AddRevampProcessNodeStatusDto> addRevampProcessNodeStatusDto);


        /**
        * 方法描述：删除流程配置-节点状态
        *
        * @param id （多个用，）
        * @return boolean
        */
        Boolean delProcessNodeStatus(String id);



        /**
        * 方法描述：启用停止流程配置-节点状态
        *
        * @param startStopDto 启用停止Dto类
        * @return boolean
        */
        Boolean startStopProcessNodeStatus( StartStopDto startStopDto);


// 自定义方法区 不替换的区域【other_end】

	
}
