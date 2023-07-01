package com.base.sbc.module.basicsdatum.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.config.common.BaseQueryWrapper;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.CopyUtil;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.module.basicsdatum.dto.ColorModelNumberExcelDto;
import com.base.sbc.module.basicsdatum.entity.ColorModelNumber;
import com.base.sbc.module.basicsdatum.mapper.ColorModelNumberMapper;
import com.base.sbc.module.basicsdatum.service.ColorModelNumberService;
import com.base.sbc.module.basicsdatum.vo.ColorModelNumberBaseSelectVO;
import com.base.sbc.module.common.service.impl.BaseServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/6/26 10:04
 * @mail 247967116@qq.com
 */
@Service
public class ColorModelNumberServiceImpl extends BaseServiceImpl<ColorModelNumberMapper, ColorModelNumber> implements ColorModelNumberService {
    @Override
    public Boolean saveColorModelNumber(ColorModelNumber colorModelNumber) {
        if (StringUtils.isNotBlank(colorModelNumber.getId())) {
            //校验名称或者编码是否相同
            BaseQueryWrapper<ColorModelNumber> queryWrapper = new BaseQueryWrapper<>();
            queryWrapper.eq("code", colorModelNumber.getCode());
            ColorModelNumber one = this.getOne(queryWrapper);
            if (one != null && !one.getId().equals(colorModelNumber.getId())) {
                throw new OtherException("编码存在重复");
            }
//            BaseQueryWrapper<ColorModelNumber> queryWrapper1 = new BaseQueryWrapper<>();
//            queryWrapper1.eq("name", colorModelNumber.getName());
//            ColorModelNumber one1 = this.getOne(queryWrapper1);
//            if (one1 != null && !one1.getId().equals(colorModelNumber.getId())) {
//                throw new OtherException("名称存在重复");
//            }
            return this.updateById(colorModelNumber);
        } else {
            BaseQueryWrapper<ColorModelNumber> queryWrapper = new BaseQueryWrapper<>();
            queryWrapper.eq("code", colorModelNumber.getCode());
            ColorModelNumber one = this.getOne(queryWrapper);
            if (one != null) {
                throw new OtherException("编码存在重复");
            }
//            BaseQueryWrapper<ColorModelNumber> queryWrapper1 = new BaseQueryWrapper<>();
//            queryWrapper1.eq("name", colorModelNumber.getName());
//            ColorModelNumber one1 = this.getOne(queryWrapper1);
//            if (one1 != null) {
//                throw new OtherException("名称存在重复");
//            }
            return this.save(colorModelNumber);
        }
    }

    @Override
    public Boolean importExcel(MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String[] split = originalFilename.split("\\.");
        ImportParams params = new ImportParams();
        params.setNeedSave(false);

        List<ColorModelNumberExcelDto> list = ExcelImportUtil.importExcel(file.getInputStream(), ColorModelNumberExcelDto.class, params);

        List<ColorModelNumber> colorModelNumbers = BeanUtil.copyToList(list, ColorModelNumber.class);
        for (ColorModelNumber colorModelNumber : colorModelNumbers) {
            colorModelNumber.setFileName(split[0]);
            colorModelNumber.setStatus("1");
            QueryWrapper<ColorModelNumber> queryWrapper =new BaseQueryWrapper<>();
            queryWrapper.eq("code",colorModelNumber.getCode());
            this.saveOrUpdate(colorModelNumber,queryWrapper);
        }
        return true;
    }

    @Override
    public List<ColorModelNumberBaseSelectVO> getByDistCode(String distCode, String fileName, String userCompany) {
        LambdaQueryWrapper<ColorModelNumber> queryWrapper = new LambdaQueryWrapper<ColorModelNumber>()
                .like(ColorModelNumber::getMat2ndCategoryId, distCode)
                .eq(ColorModelNumber::getFileName, fileName)
                .eq(ColorModelNumber::getCompanyCode, userCompany)
                .select(ColorModelNumber::getCode, ColorModelNumber::getName);
        List<ColorModelNumber> list = super.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }
        return CopyUtil.copy(list, ColorModelNumberBaseSelectVO.class);
    }
}
