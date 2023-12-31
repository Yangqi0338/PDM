package com.base.sbc.module.material.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.material.entity.MaterialLabel;
import com.base.sbc.module.material.mapper.MaterialLabelMapper;
import com.base.sbc.module.material.service.MaterialLabelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/4/1 10:38:14
 */
@Service
public class MaterialLabelServiceImpl extends BaseServiceImpl<MaterialLabelMapper,MaterialLabel> implements MaterialLabelService {
    @Resource
    private MaterialLabelMapper materialLabelMapper;

    /**
     * 获取素材列表相关联的列表
     */
    @Override
    public List<MaterialLabel> getByMaterialIds(List<String> materialIds){
        QueryWrapper<MaterialLabel> queryWrapper =new QueryWrapper<>();
        queryWrapper.in("material_id",materialIds);
        return materialLabelMapper.selectList(queryWrapper);
    }

    /**
     * 根据label_id集合查询列表
     */
    @Override
    public List<MaterialLabel> getByLabelIds(List<String> labelId){
        QueryWrapper<MaterialLabel> queryWrapper =new QueryWrapper<>();
        queryWrapper.in("label_id",labelId);
        return materialLabelMapper.selectList(queryWrapper);
    }
}
