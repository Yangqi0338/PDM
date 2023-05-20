package com.base.sbc.module.material.service.impl;

import cn.afterturn.easypoi.cache.manager.IFileLoader;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.service.AmcFeignService;
import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.client.flowable.service.FlowableService;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.utils.CommonUtils;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.common.service.impl.ServicePlusImpl;
import com.base.sbc.module.material.dto.MaterialQueryDto;
import com.base.sbc.module.material.dto.MaterialSaveDto;
import com.base.sbc.module.material.entity.*;
import com.base.sbc.module.material.mapper.MaterialMapper;
import com.base.sbc.module.material.service.*;
import com.base.sbc.module.material.vo.MaterialVo;
import com.base.sbc.module.planning.service.PlanningCategoryItemMaterialService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 类描述：素材库 service实现类
 *
 * @author 卞康
 * @version 1.0
 * @date 创建时间：2023-4-1 16:26:15
 */
@Service
@RequiredArgsConstructor
public class MaterialServiceImpl extends ServicePlusImpl<MaterialMapper, Material> implements MaterialService {

    private final MaterialMapper materialMapper;

    private final UserUtils userUtils;

    private final MaterialLabelService materialLabelService;

    private final MaterialCollectService materialCollectService;

    private final MaterialSizeService materialSizeService;

    private final MaterialColorService materialColorService;

    private final AmcFeignService amcFeignService;

    private final PlanningCategoryItemMaterialService planningCategoryItemMaterialService;

    private final RedisTemplate<String,Object> redisTemplate;


    /**
     * 为了解决太多表关联查询太慢的问题
     * 相关联的表，生成查询条件
     */
    @Transactional
    public void addQuery(MaterialQueryDto materialQueryDto) {
        // TODO: 2023/3/31 后期优化，写sql语句优化查询返回字段
        HashSet<String> collectSet = null;
        HashSet<String> labelSet = null;
        HashSet<String> sizeSet = null;
        HashSet<String> colorSet = null;
        //我的收藏筛选条件
        if (materialQueryDto.isCollect()) {
            collectSet = new HashSet<>();
            QueryWrapper<MaterialCollect> qc = new QueryWrapper<>();
            qc.eq("user_id", materialQueryDto.getUserId());
            List<MaterialCollect> materialCollects = materialCollectService.list(qc);
            for (MaterialCollect materialCollect : materialCollects) {
                collectSet.add(materialCollect.getMaterialId());
            }
        }

        //标签筛选条件
        if (materialQueryDto.getLabelIds() != null && materialQueryDto.getLabelIds().length > 0) {
            labelSet = new HashSet<>();
            List<MaterialLabel> materialLabels = materialLabelService.getByLabelIds(Arrays.asList(materialQueryDto.getLabelIds()));
            for (MaterialLabel materialLabel : materialLabels) {
                labelSet.add(materialLabel.getMaterialId());
            }
        }

        //尺码筛选条件
        if (!StringUtils.isEmpty(materialQueryDto.getSizeId())) {
            sizeSet = new HashSet<>();
            List<MaterialSize> materialSizes = materialSizeService.getBySizeId(materialQueryDto.getSizeId());
            for (MaterialSize materialSize : materialSizes) {
                sizeSet.add(materialSize.getMaterialId());
            }
        }

        //颜色筛选条件
        if (!StringUtils.isEmpty(materialQueryDto.getColorId())) {
            colorSet = new HashSet<>();
            List<MaterialColor> materialColors = materialColorService.getColorId(materialQueryDto.getColorId());
            for (MaterialColor materialColor : materialColors) {
                colorSet.add(materialColor.getMaterialId());
            }
        }

        //如果有集合不为null，则说明有筛选条件
        if (collectSet != null || labelSet != null || sizeSet != null || colorSet != null) {
            //取所有条件相交的
            Set<String> ids = CommonUtils.findCommonElements(collectSet, labelSet, sizeSet);
            if (ids.size() == 0) {
                //说明无筛选结果，给个0让查询不到数据
                ids = new HashSet<>();
                ids.add("0");
            }
            materialQueryDto.setIds(new ArrayList<>(ids));
        }
    }

    /**
     * 条件查询
     *
     * @param materialQueryDto 请求封装对象
     * @return 返回的封装对象
     */
    @Override
    public PageInfo<MaterialVo> listQuery(MaterialQueryDto materialQueryDto) {
        materialQueryDto.setCompanyCode(userUtils.getCompanyCode());
        materialQueryDto.setUserId(userUtils.getUserId());
        this.addQuery(materialQueryDto);

        PageHelper.startPage(materialQueryDto);
        List<MaterialVo> materialAllDtolist = materialMapper.listQuery(materialQueryDto);

        if (materialAllDtolist == null || materialAllDtolist.size() == 0) {
            return new PageInfo<>();
        }

        List<String> ids = new ArrayList<>();
        List<String> userIds = new ArrayList<>();

        for (MaterialVo materialVo : materialAllDtolist) {
            userIds.add(materialVo.getCreateId());
            ids.add(materialVo.getId());
        }

        //查询关联标签
        List<MaterialLabel> materialLabelList = materialLabelService.getByMaterialIds(ids);
        //查询关联尺码信息
        List<MaterialSize> materialSizeList = materialSizeService.getByMaterialIds(ids);
        //查询关联颜色信息
        List<MaterialColor> materialColorList = materialColorService.getByMaterialIds(ids);
        //查询收藏数量
        List<Map<String, Integer>> mapList = materialCollectService.numList(ids);
        //查询引用数量
        List<Map<String, Integer>> maps = planningCategoryItemMaterialService.numList(ids);
        //获取用户头像
        Map<String, String> userAvatarMap = amcFeignService.getUserAvatar(CollUtil.join(userIds, ","));


        for (MaterialVo materialVo : materialAllDtolist) {
            materialVo.setUserAvatar(userAvatarMap.get(materialVo.getCreateId()));
            //标签放入对象
            List<MaterialLabel> labels = new ArrayList<>();
            for (MaterialLabel materialLabel : materialLabelList) {
                if (materialVo.getId().equals(materialLabel.getMaterialId())) {
                    labels.add(materialLabel);
                }
            }
            materialVo.setLabels(labels);

            //尺码放入对象
            List<MaterialSize> materialSizes = new ArrayList<>();
            for (MaterialSize materialSize : materialSizeList) {
                if (materialVo.getId().equals(materialSize.getMaterialId())) {
                    materialSizes.add(materialSize);
                }
            }
            materialVo.setSizes(materialSizes);

            //颜色放入对象
            List<MaterialColor> materialColors = new ArrayList<>();
            for (MaterialColor materialColor : materialColorList) {
                if (materialVo.getId().equals(materialColor.getMaterialId())) {
                    materialColors.add(materialColor);
                }
            }
            materialVo.setColors(materialColors);

            //收藏数量
            materialVo.setCollectNum("0");
            for (Map<String, Integer> map : mapList) {
                if (materialVo.getId().equals(String.valueOf(map.get("materialId")))) {
                    materialVo.setCollectNum(String.valueOf(map.get("collectNum")));
                }
            }

            //引用数量
            materialVo.setQuoteNum("0");
            for (Map<String, Integer> map : maps) {
                if (materialVo.getId().equals(String.valueOf(map.get("materialId")))) {
                    materialVo.setQuoteNum(String.valueOf(map.get("collectNum")));
                }
            }
        }
        return new PageInfo<>(materialAllDtolist);
    }

    @Override
    @Transactional
    public boolean toExamine(AnswerDto dto) {
        Material material = this.getById(dto.getBusinessKey());
        if (BaseConstant.APPROVAL_PASS.equals(dto.getApprovalType())) {
            //审核通过
            material.setStatus("2");
            this.updateById(material);
            return true;
        } else {
            // TODO: 2023/5/20 临时逻辑，恢复原来的
            MaterialSaveDto materialSaveDto = (MaterialSaveDto) redisTemplate.opsForValue().get("MTUP-" + material.getId());
            if (materialSaveDto!=null){
                QueryWrapper<MaterialLabel> labelQueryWrapper = new QueryWrapper<>();
                labelQueryWrapper.eq("material_id", materialSaveDto.getId());
                materialLabelService.addAndUpdateAndDelList(materialSaveDto.getLabels(), labelQueryWrapper);
                redisTemplate.delete("MTUP-" + material.getId());
               this.updateById(materialSaveDto);
            }else {
                material.setStatus("3");
            }
            this.updateById(material);
            return false;
        }

    }

    /**
     * 新增
     *
     * @param materialSaveDto materialSaveDto类
     */
    @Override
    @Transactional
    public String add(MaterialSaveDto materialSaveDto) {
        this.save(materialSaveDto);

        List<MaterialLabel> labels = materialSaveDto.getLabels();

        //新增关联标签
        if (labels != null) {
            for (MaterialLabel label : labels) {
                label.setMaterialId(materialSaveDto.getId());
                materialLabelService.save(label);
            }
        }

        //新增关联尺码
        List<MaterialSize> sizes = materialSaveDto.getSizes();
        if (sizes != null) {
            for (MaterialSize size : sizes) {
                size.setMaterialId(materialSaveDto.getId());
                materialSizeService.save(size);
            }
        }

        //新增关联颜色
        List<MaterialColor> colors = materialSaveDto.getColors();
        if (colors != null) {
            for (MaterialColor color : colors) {
                color.setMaterialId(materialSaveDto.getId());
                materialColorService.save(color);
            }
        }

        return materialSaveDto.getId();
    }

    /**
     * 批量修改素材
     *
     * @param materialSaveDtoList 素材列表
     * @return 影响的条数
     */
    @Override
    @Transactional
    public Integer updateList(List<MaterialSaveDto> materialSaveDtoList) {
        for (MaterialSaveDto materialSaveDto : materialSaveDtoList) {
            //修改关联标签
            QueryWrapper<MaterialLabel> labelQueryWrapper = new QueryWrapper<>();
            labelQueryWrapper.eq("material_id", materialSaveDto.getId());
            materialLabelService.addAndUpdateAndDelList(materialSaveDto.getLabels(), labelQueryWrapper);
            this.updateById(materialSaveDto);
        }

        ////修改关联尺码
        //QueryWrapper<MaterialSize> sizeQueryWrapper = new QueryWrapper<>();
        //sizeQueryWrapper.eq("material_id", materialSaveDto.getId());
        //materialSizeService.addAndUpdateAndDelList(materialSaveDto.getSizes(),sizeQueryWrapper);
        //
        ////修改关联颜色
        //QueryWrapper<MaterialColor> colorQueryWrapper = new QueryWrapper<>();
        //colorQueryWrapper.eq("material_id", materialSaveDto.getId());
        //materialColorService.addAndUpdateAndDelList(materialSaveDto.getColors(),colorQueryWrapper);
        return materialSaveDtoList.size();
    }
}
