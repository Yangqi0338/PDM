/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.process.service;
import com.base.sbc.module.basicsdatum.dto.QueryDto;
import com.github.pagehelper.PageInfo;
import com.base.sbc.module.common.service.IServicePlus;
import com.base.sbc.module.process.entity.ProcessNodeConditionFormula;
import com.base.sbc.module.process.vo.ProcessNodeConditionFormulaVo;
import com.base.sbc.module.process.dto.AddRevampProcessNodeConditionFormulaDto;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import com.base.sbc.module.basicsdatum.dto.StartStopDto;
import java.io.IOException;
import java.util.List;

/** 
 * 类描述：流程配置-节点条件公式 service类
 * @address com.base.sbc.module.process.service.ProcessNodeConditionFormulaService
 * @author mengfanjiang
 * @email lxl.fml@gmail.com
 * @date 创建时间：2023-6-7 15:47:31
 * @version 1.0  
 */
public interface ProcessNodeConditionFormulaService extends IServicePlus<ProcessNodeConditionFormula>{

// 自定义方法区 不替换的区域【other_start】

        /**
        * 方法描述：分页查询部件
        *
        * @param queryDto 查询条件
        * @return PageInfo<BasicsdatumComponentVo>
         */
        PageInfo<ProcessNodeConditionFormulaVo> getProcessNodeConditionFormulaList(QueryDto queryDto);




        /**
        * 方法描述：新增修改流程配置-节点条件公式
        *
        * @param addRevampProcessNodeConditionFormulaDto 部件Dto类
        * @return boolean
        */
        Boolean addRevampProcessNodeConditionFormula(AddRevampProcessNodeConditionFormulaDto addRevampProcessNodeConditionFormulaDto);

        /**
         * 方法描述：批量修改流程配置-节点条件公式
         *
         * @param list 部件Dto类
         * @return boolean
         */
        Boolean batchAddRevampProcessNodeConditionFormula(List<AddRevampProcessNodeConditionFormulaDto> list);

        /**
        * 方法描述：删除流程配置-节点条件公式
        *
        * @param id （多个用，）
        * @return boolean
        */
        Boolean delProcessNodeConditionFormula(String id);



        /**
        * 方法描述：启用停止流程配置-节点条件公式
        *
        * @param startStopDto 启用停止Dto类
        * @return boolean
        */
        Boolean startStopProcessNodeConditionFormula( StartStopDto startStopDto);


// 自定义方法区 不替换的区域【other_end】

	
}
