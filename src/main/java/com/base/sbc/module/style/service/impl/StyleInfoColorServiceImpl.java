/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.base.sbc.module.style.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.base.sbc.module.pack.entity.PackProcessPrice;
import com.base.sbc.module.pack.service.PackBomColorService;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackProcessPriceVo;
import com.base.sbc.module.style.dto.StyleInfoColorDto;
import com.base.sbc.module.style.dto.StyleSaveDto;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleInfoSku;
import com.base.sbc.module.style.mapper.StyleInfoColorMapper;
import com.base.sbc.module.style.entity.StyleInfoColor;
import com.base.sbc.module.style.service.StyleInfoColorService;
import com.base.sbc.module.style.service.StyleInfoSkuService;
import com.base.sbc.module.style.service.StyleService;
import com.base.sbc.module.style.vo.StyleInfoColorVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：款式设计详情颜色表 service类
 * @address com.base.sbc.module.style.service.StyleInfoColorService
 * @author LiZan
 * @email 2682766618@qq.com
 * @date 创建时间：2023-8-24 15:21:29
 * @version 1.0  
 */
@Service
public class StyleInfoColorServiceImpl extends BaseServiceImpl<StyleInfoColorMapper, StyleInfoColor> implements StyleInfoColorService {

    @Resource
    private StyleInfoSkuService styleInfoSkuService;
    @Resource
    private StyleService styleService;

    @Resource
    private PackBomColorService packBomColorService;

    @Override
    public PageInfo<StyleInfoColorVo> pageList(StyleInfoColorDto styleInfoColorDto) {
        QueryWrapper<StyleInfoColor> qw = new QueryWrapper();
        qw.eq("foreign_id", styleInfoColorDto.getForeignId());
        if (StrUtil.isBlank(styleInfoColorDto.getOrderBy())) {
            qw.orderByDesc("id");
        }
        if (StrUtil.isNotBlank(styleInfoColorDto.getColorName())) {
            qw.like("color_name" , styleInfoColorDto.getColorName());
        }
        if (StrUtil.isNotBlank(styleInfoColorDto.getColorCode())) {
            qw.like("color_code" , styleInfoColorDto.getColorCode());
        }
        if (StrUtil.isNotBlank(styleInfoColorDto.getPackType())) {
            qw.like("pack_type" , styleInfoColorDto.getPackType());
        }
        Page<StyleInfoColorVo> page = PageHelper.startPage(styleInfoColorDto);
        list(qw);
        PageInfo<StyleInfoColorVo> pageInfo = page.toPageInfo();
        return pageInfo;
    }

    /**
     * 根据id删除款式设计详情颜色
     * @param codes 款式设计详情颜色id
     * @param companyCode 公司编码
     * @param foreignId 主数据id
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, OtherException.class})
    public void delStyleInfoColorById(String codes, String companyCode, String foreignId) {
        List<String> codeList = StringUtils.convertList(codes);
        // 查找数据是否存在
        List<StyleInfoColor> styleInfoColors = baseMapper.selectList(new QueryWrapper<StyleInfoColor>().in("color_code", codeList)
                .eq("company_code", companyCode).eq("foreign_id", foreignId));
        if (CollectionUtil.isEmpty(styleInfoColors)) {
            throw new OtherException(codes + "未找到数据，删除失败");
        }
        // 删除颜色对应的SKU数据
        styleInfoColors.forEach( styleInfoColor -> {
            if (StringUtils.isNotEmpty(styleInfoColor.getColorCode()) && StringUtils.isNotEmpty(styleInfoColor.getForeignId())) {
                QueryWrapper<StyleInfoSku> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("color_code", styleInfoColor.getColorCode());
                queryWrapper.eq("company_code", companyCode);
                queryWrapper.eq("foreign_id", styleInfoColor.getForeignId());
                styleInfoSkuService.remove(queryWrapper);
            }
        });
        // 查找未删除的颜色数据，拼接数据修改款式设计里的颜色code、颜色名称集合
        QueryWrapper<StyleInfoColor> qwColor = new QueryWrapper<>();
        qwColor.notIn("color_code", codeList);
        qwColor.eq("company_code", companyCode);
        qwColor.eq("foreign_id", styleInfoColors.get(BaseGlobal.ZERO).getForeignId());
        qwColor.select("id","color_code","color_name", "foreign_id");
        List<StyleInfoColor> styleInfoColorList = baseMapper.selectList(qwColor);
        Style style = new Style();
        style.setId(styleInfoColors.get(BaseGlobal.ZERO).getForeignId());
        if (CollectionUtil.isNotEmpty(styleInfoColorList)) {
            // 颜色code集合逗号分隔
            String colorCodes = styleInfoColorList.stream().map(StyleInfoColor::getColorCode).collect(Collectors.joining(BaseGlobal.D));
            // 颜色名称集合逗号分隔
            String productColors = styleInfoColorList.stream().map(StyleInfoColor::getColorName).collect(Collectors.joining(BaseGlobal.D));
            style.setColorCodes(colorCodes);
            style.setProductColors(productColors);
            styleService.updateById(style);
        } else {
            // 删除所有数据 把款式设计里的颜色清空
            LambdaUpdateWrapper<Style> lambdaUpdate = Wrappers.lambdaUpdate();
            lambdaUpdate.eq(Style::getId, styleInfoColors.get(BaseGlobal.ZERO).getForeignId());
            lambdaUpdate.set(Style::getColorCodes, null);
            lambdaUpdate.set(Style::getProductColors, null);
            style.updateInit();
            styleService.update(style,lambdaUpdate);
        }

        // 删除相关数据
        baseMapper.deleteBatchIds(styleInfoColors.stream().map(StyleInfoColor::getId).collect(Collectors.toList()));

    }

    /**
     * 根据id修改款式设计详情颜色
     * @param styleInfoColorDto 款式设计详情颜色DTO
     */
    @Override
    public void updateStyleInfoColorById(StyleInfoColorDto styleInfoColorDto) {
        StyleInfoColor styleInfoColorInfo = baseMapper.selectById(styleInfoColorDto.getId());
        if (null == styleInfoColorInfo) {
            throw new OtherException(styleInfoColorDto.getId() + "删除颜色数据未找到！！！");
        }
        StyleInfoColor styleInfoColor = BeanUtil.copyProperties(styleInfoColorDto, StyleInfoColor.class);
        styleInfoColor.updateInit();
        baseMapper.updateById(styleInfoColor);
    }


    /**
     * 添加款式设计详情颜色
     * @param styleSaveDto 样衣dto
     */    @Override
    public void insertStyleInfoColorList(StyleSaveDto styleSaveDto, String sourcePackType, String targetPackType) {
        if(null == styleSaveDto || null == styleSaveDto.getStyleInfoColorDtoList()){
                return;
        }
        QueryWrapper delWrapper = new QueryWrapper<>();
        delWrapper.eq("company_code", this.getCompanyCode());
        delWrapper.eq("foreign_id", styleSaveDto.getId());
        delWrapper.eq("pack_type", targetPackType);
        // 删除款式设计详情颜色
        this.remove(delWrapper);
        // 删除款式设计详情颜色
        styleInfoSkuService.remove(delWrapper);
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_code", this.getCompanyCode());
        queryWrapper.eq("foreign_id", styleSaveDto.getId());
        queryWrapper.eq("pack_type", sourcePackType);
        List<StyleInfoColor> styleInfoColorList = this.list(queryWrapper);
        if (CollectionUtil.isEmpty(styleInfoColorList)) {
            return;
        }
        for (StyleInfoColor styleInfoColor : styleInfoColorList) {
            styleInfoColor.setId(null);
            styleInfoColor.setPackType(PackUtils.PACK_TYPE_BIG_GOODS);
            styleInfoColor.insertInit();
        }
        // 保存款式设计详情颜色
        boolean flg = this.saveBatch(styleInfoColorList);
        if (!flg) {
            throw new OtherException("新增大货款式设计详情颜色失败，请联系管理员");
        }
        List<StyleInfoSku> styleInfoSkuList = styleInfoSkuService.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(styleInfoSkuList)) {
            for (StyleInfoSku styleInfoSku : styleInfoSkuList) {
                styleInfoSku.setId(null);
                styleInfoSku.setPackType(PackUtils.PACK_TYPE_BIG_GOODS);
                styleInfoSku.insertInit();
            }
            // 保存款式SKU数据
            boolean skuFlg = styleInfoSkuService.saveBatch(styleInfoSkuList);
            if (!skuFlg) {
                throw new OtherException("新增款式SKU失败，请联系管理员");
            }
        }
    }

// 自定义方法区 不替换的区域【other_start】



// 自定义方法区 不替换的区域【other_end】
	
}
