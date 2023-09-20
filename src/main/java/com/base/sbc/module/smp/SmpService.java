package com.base.sbc.module.smp;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.restTemplate.RestTemplateService;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialColorQueryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialPriceQueryDto;
import com.base.sbc.module.basicsdatum.entity.*;
import com.base.sbc.module.basicsdatum.service.*;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialColorPageVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialPricePageVo;
import com.base.sbc.module.common.entity.UploadFile;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.utils.AttachmentTypeConstant;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.formType.vo.FieldManagementVo;
import com.base.sbc.module.hangTag.dto.UpdatePriceDto;
import com.base.sbc.module.pack.entity.*;
import com.base.sbc.module.pack.service.*;
import com.base.sbc.module.pack.utils.PackUtils;
import com.base.sbc.module.pack.vo.PackInfoListVo;
import com.base.sbc.module.patternmaking.entity.PatternMaking;
import com.base.sbc.module.patternmaking.service.PatternMakingService;
import com.base.sbc.module.pricing.service.StylePricingService;
import com.base.sbc.module.pricing.vo.StylePricingVO;
import com.base.sbc.module.pushRecords.service.PushRecordsService;
import com.base.sbc.module.sample.entity.PreProductionSampleTask;
import com.base.sbc.module.sample.service.PreProductionSampleTaskService;
import com.base.sbc.module.smp.dto.*;
import com.base.sbc.module.smp.entity.*;
import com.base.sbc.module.style.entity.Style;
import com.base.sbc.module.style.entity.StyleColor;
import com.base.sbc.module.style.service.StyleColorService;
import com.base.sbc.module.style.service.StyleService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.base.sbc.client.ccm.enums.CcmBaseSettingEnum.ISSUED_TO_EXTERNAL_SMP_SYSTEM_SWITCH;


/**
 * @author 卞康
 * @date 2023/5/8 15:27:43
 * @mail 247967116@qq.com
 * 对接下发Smp主数据
 */
@Service
@RequiredArgsConstructor
public class SmpService {

    private final DataSourceTransactionManager dataSourceTransactionManager;

    private final TransactionDefinition transactionDefinition;

    private final RestTemplateService restTemplateService;

    private final PushRecordsService pushRecordsService;

    private final BasicsdatumMaterialService basicsdatumMaterialService;

    private final BasicsdatumMaterialWidthService basicsdatumMaterialWidthService;

    private final AmcService amcService;

    private final UserUtils userUtils;

    private final PackInfoService packInfoService;

    private final StyleColorService sampleStyleColorService;

    private final PackBomService packBomService;
    private final PackBomVersionService packBomVersionService;

    private final BasicsdatumColourLibraryService basicsdatumColourLibraryService;

    private final PackBomSizeService packBomSizeService;

    private final BasicsdatumSizeService basicsdatumSizeService;

    private final StyleService styleService;
    private final StyleColorService styleColorService;

    private final CcmFeignService ccmFeignService;

    private final AttachmentService attachmentService;

    private final PackInfoStatusService packInfoStatusService;

    private final PackPricingService packPricingService;

    private final PackTechPackagingService packTechPackagingService;

    private final PackTechSpecService packTechSpecService;

    private final StylePricingService stylePricingService;

    private final PatternMakingService patternMakingService;

    private final BasicsdatumIngredientService basicsdatumIngredientService;
    private final BasicsdatumMaterialColorService basicsdatumMaterialColorService;
    private final PreProductionSampleTaskService preProductionSampleTaskService;
    private final UploadFileService uploadFileService;
    private final BasicsdatumMaterialPriceService basicsdatumMaterialPriceService;

    private static final String SMP_URL = "http://10.98.250.31:7006/pdm";
    //private static final String PDM_URL = "http://smp-i.eifini.com/service-manager/pdm";

    private static final String SCM_URL = "http://10.8.250.100:1980/escm-app/information/pdm";

    private static final String OA_URL = "http://10.8.240.161:40002/mps-interfaces/sample";


    /**
     * 商品主数据下发
     */
    public Integer goods(String[] ids) {
        int i = 0;


        List<StyleColor> styleColors = sampleStyleColorService.listByIds(Arrays.asList(ids));

        for (StyleColor styleColor : styleColors) {
            SmpGoodsDto smpGoodsDto = styleColor.toSmpGoodsDto();
            //吊牌价为空或者等于0
            if (styleColor.getTagPrice()==null || styleColor.getTagPrice().compareTo(BigDecimal.ZERO)==0){
                throw new OtherException("吊牌价不能为空或者等于0");
            }
            Style style = styleService.getById(styleColor.getStyleId());
            smpGoodsDto.setMaxClassName(style.getProdCategory1stName());
            smpGoodsDto.setStyleBigClass(style.getProdCategory1st());
            smpGoodsDto.setCategoryName(style.getProdCategoryName());
            smpGoodsDto.setStyleCategory(style.getProdCategory());
            smpGoodsDto.setMiddleClassName(style.getProdCategory2ndName());
            smpGoodsDto.setStyleMiddleClass(style.getProdCategory2nd());
            smpGoodsDto.setMinClassName(style.getProdCategory3rdName());
            smpGoodsDto.setStyleSmallClass(style.getProdCategory3rd());

            smpGoodsDto.setBrandId(style.getBrand());
            smpGoodsDto.setBrandName(style.getBrandName());
            //String sizeRange = sampleDesign.getSizeRange();
            //BasicsdatumModelType basicsdatumModelType = basicsdatumModelTypeService.getById(sizeRange);
            //PlmStyleSizeParam param = new PlmStyleSizeParam();
            //param.setSizeCategory(sizeRange);
            //param.setSizeNum(basicsdatumModelType.getSizeIds().split(",").length);
            //param.setStyleNo(sampleDesign.getStyleNo());
            //Boolean style = this.style(param);
            //if(!style){
            //    throw new OtherException("当前号型类型已在单据中，不允许修改");
            //}

            // 款式图片
            List<AttachmentVo> stylePicList = attachmentService.findByforeignId(style.getId(), AttachmentTypeConstant.STYLE_MASTER_DATA_PIC);
            List<String> imgList = new ArrayList<>();
            for (AttachmentVo attachmentVo : stylePicList) {
                imgList.add(attachmentVo.getUrl());
            }
            smpGoodsDto.setImgList(imgList);

            //设计师id,下稿设计师,工艺员,工艺员id,版师名称,版师ID
            String designer = style.getDesigner();
            smpGoodsDto.setDesigner(StringUtils.isNotEmpty(designer) ? designer.split(",")[0] : "");
            smpGoodsDto.setTechnician(style.getTechnicianName());
            smpGoodsDto.setPatternMakerName(style.getPatternDesignName());

            String designerId = style.getDesignerId();
            String technicianId = style.getTechnicianId();
            String patternDesignId = style.getPatternDesignId();

            ArrayList<String> list = new ArrayList<>();
            list.add(designerId);
            list.add(technicianId);
            list.add(patternDesignId);

            Map<String, String> usernamesByIds = amcService.getUsernamesByIds(StringUtils.join(list, ","));
            smpGoodsDto.setDesignerId(usernamesByIds.get(designerId));
            smpGoodsDto.setTechnicianId(usernamesByIds.get(technicianId));
            smpGoodsDto.setYear(style.getYearName());
            smpGoodsDto.setPatternName("常规");
            smpGoodsDto.setPriorityId(style.getTaskLevel());
            smpGoodsDto.setPriorityName(style.getTaskLevelName());
            smpGoodsDto.setSeason(style.getSeason());
            smpGoodsDto.setTheme(style.getSubject());
            smpGoodsDto.setStyleName(style.getStyleName());
            smpGoodsDto.setTargetCost(style.getProductCost());
            smpGoodsDto.setShapeName(style.getPlateType());
            smpGoodsDto.setUniqueCode(styleColor.getWareCode());

            Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap("C8_Band");
            Map<String, String> map = dictInfoToMap.get("C8_Band");
            smpGoodsDto.setBandName(map.get(style.getBandCode()));

            //List<FieldVal> list1 = fieldValService.list(sampleDesign.getId(), FieldValDataGroupConstant.SAMPLE_DESIGN_TECHNOLOGY);


            //动态字段

            List<FieldManagementVo> fieldManagementVoList = styleColorService.getStyleColorDynamicDataById(styleColor.getId());
            if (!CollectionUtils.isEmpty(fieldManagementVoList)) {
                fieldManagementVoList.forEach(m -> {
                    if ("衣长分类".equals(m.getFieldName())) {
                        smpGoodsDto.setLengthRangeId(m.getVal());
                        smpGoodsDto.setLengthRangeName(m.getValName());
                    }
                    if ("衣长".equals(m.getFieldName())) {
                        smpGoodsDto.setCoatLength(m.getValName());
                    }
                    if ("腰型".equals(m.getFieldName())) {
                        smpGoodsDto.setWaistTypeId(m.getVal());
                        smpGoodsDto.setWaistTypeName(m.getValName());
                    }
                    if ("袖长".equals(m.getFieldName())) {
                        smpGoodsDto.setSleeveLengthId(m.getVal());
                        smpGoodsDto.setSleeveLengthName(m.getValName());
                    }
                    if ("袖型".equals(m.getFieldName())) {
                        smpGoodsDto.setSleeveId(m.getVal());
                        smpGoodsDto.setSleeveName(m.getValName());
                    }
                    if ("胸围".equals(m.getFieldName())) {
                        smpGoodsDto.setBust(m.getVal());
                    }
                    if ("门襟".equals(m.getFieldName())) {
                        smpGoodsDto.setPlacketId(m.getVal());
                        smpGoodsDto.setPlacketName(m.getValName());
                    }
                    if ("毛纱针型".equals(m.getFieldName())) {
                        smpGoodsDto.setYarnNeedleTypeId(m.getVal());
                        smpGoodsDto.setYarnNeedleTypeName(m.getValName());
                    }
                    if ("毛纱针法".equals(m.getFieldName())) {
                        smpGoodsDto.setYarnNeedleId(m.getVal());
                        smpGoodsDto.setYarnNeedleName(m.getValName());
                    }
                    if ("廓形".equals(m.getFieldName())) {
                        smpGoodsDto.setProfileId(m.getVal());
                        smpGoodsDto.setProfileName(m.getValName());
                    }
                    if ("花型".equals(m.getFieldName())) {
                        smpGoodsDto.setFlowerId(m.getVal());
                        smpGoodsDto.setFlowerName(m.getValName());
                    }
                    if ("领型".equals(m.getFieldName())) {
                        smpGoodsDto.setLingXingId(m.getVal());
                        smpGoodsDto.setLingXingName(m.getValName());
                    }
                    if ("材质".equals(m.getFieldName())) {
                        smpGoodsDto.setTextureId(m.getVal());
                        smpGoodsDto.setTextureName(m.getValName());
                    }
                });
            }


            smpGoodsDto.setBandName(style.getBandName());

            smpGoodsDto.setAccessories(StringUtils.isNotEmpty(styleColor.getAccessoryNo()));

            // 资料包
            PackTechPackaging packTechPackaging = packTechPackagingService.getOne(new QueryWrapper<PackTechPackaging>().eq("foreign_id", style.getId()).eq("pack_type", "packBigGoods"));
            if (packTechPackaging != null) {
                smpGoodsDto.setPackageType(packTechPackaging.getPackagingForm());
                smpGoodsDto.setPackageSize(packTechPackaging.getPackagingBagStandard());
            }

            smpGoodsDto.setProductTypeId(style.getStyleType());
            smpGoodsDto.setProductType(style.getStyleTypeName());

            smpGoodsDto.setSaleTime(styleColor.getNewDate());
            smpGoodsDto.setProdSeg(styleColor.getSubdivide());
            smpGoodsDto.setSizeGroupId(style.getSizeRange());
            smpGoodsDto.setSizeGroupName(style.getSizeRangeName());
            smpGoodsDto.setStyleCode(style.getDesignNo());

            //工艺说明
            long count = packTechSpecService.count(new QueryWrapper<PackTechSpec>().eq("pack_type", "packBigGoods").eq("foreign_id", style.getId()).eq("spec_type", "外辅工艺"));
            smpGoodsDto.setAuProcess(count > 0);


            smpGoodsDto.setUnit(style.getStyleUnitCode());
            PackInfoListVo packInfo=packInfoService.getByQw(new QueryWrapper<PackInfo>().eq("code", styleColor.getBom()).eq("pack_type", PackUtils.PACK_TYPE_DESIGN));
            String downContent = "";
            if (packInfo!=null) {
                PackPricing packPricing = packPricingService.get(packInfo.getId(), PackUtils.PACK_TYPE_DESIGN);
                // 核价
                if (packPricing != null) {
                    JSONObject jsonObject = JSON.parseObject(packPricing.getCalcItemVal());
                    smpGoodsDto.setCost(jsonObject.getBigDecimal("成本价") == null ? new BigDecimal(0) : jsonObject.getBigDecimal("成本价"));
                    smpGoodsDto.setLaborCosts(jsonObject.getBigDecimal("车缝加工费") == null ? new BigDecimal(0) : jsonObject.getBigDecimal("车缝加工费"));
                    smpGoodsDto.setMaterialCost(jsonObject.getBigDecimal("物料费") == null ? new BigDecimal(0) : jsonObject.getBigDecimal("物料费"));
                }
                //款式定价
                StylePricingVO stylePricingVO = stylePricingService.getByPackId(packInfo.getId(), style.getCompanyCode());


                if (stylePricingVO != null) {
                    smpGoodsDto.setBomPhase("0".equals(stylePricingVO.getBomStage()) ? "Sample" : "Production");
                    smpGoodsDto.setPriceConfirm("1".equals(stylePricingVO.getProductTagPriceConfirm()));
                    smpGoodsDto.setPlanCost(stylePricingVO.getPlanCost());
                    smpGoodsDto.setPlanningRate(stylePricingVO.getPlanningRatio());
                    try {

                        smpGoodsDto.setActualRate(stylePricingVO.getActualMagnification());

                    } catch (Exception e) {

                    }
                    downContent = stylePricingVO.getDownContent();
                    smpGoodsDto.setPlanActualRate(stylePricingVO.getPlanActualMagnification());
                    smpGoodsDto.setProcessCost(stylePricingVO.getCoordinationProcessingFee().add(stylePricingVO.getWoolenYarnProcessingFee()).add(stylePricingVO.getCoordinationProcessingFee()).add(stylePricingVO.getSewingProcessingFee()));
                    smpGoodsDto.setProductName(stylePricingVO.getProductName());
                    //吊牌
                    smpGoodsDto.setSeries(stylePricingVO.getSeries());
                    smpGoodsDto.setSeriesId(stylePricingVO.getSeries());
                    smpGoodsDto.setSeriesName(stylePricingVO.getSeriesName());
                    smpGoodsDto.setComposition(stylePricingVO.getIngredient());
                }
                List<PackBom> packBoms = packBomService.list(new QueryWrapper<PackBom>().eq("foreign_id", packInfo.getId()));

                if (packBoms != null && !packBoms.isEmpty()) {
                    PackInfoStatus packInfoStatus = packInfoStatusService.getOne(new QueryWrapper<PackInfoStatus>().eq("foreign_id", packInfo.getId()).eq("pack_type", packBoms.get(0).getPackType()));
                    smpGoodsDto.setIntegrityProduct("1".equals(packInfoStatus.getBulkOrderClerkConfirm()));
                }
                smpGoodsDto.setIntegritySample(StringUtils.isNoneEmpty(packInfo.getStyleColorId()));
            }
            //废弃
            //smpGoodsDto.setSeriesId(null);
            //smpGoodsDto.setSeriesName(null);
            //smpGoodsDto.setRegion(null);
            //smpGoodsDto.setSalesGroup(null);
            String sizeIds = style.getSizeIds();
            List<BasicsdatumSize> basicsdatumSizes = basicsdatumSizeService.listByIds(Arrays.asList(sizeIds.split(",")));
            if (basicsdatumSizes.isEmpty()){
                throw new OtherException("尺码不能为空");
            }
            List<SmpSize> smpSizes = new ArrayList<>();
            for (BasicsdatumSize basicsdatumSize : basicsdatumSizes) {
                SmpSize smpSize = new SmpSize();
                smpSize.setSize(basicsdatumSize.getModel());
                smpSize.setSizeNumber(basicsdatumSize.getCode());
                smpSize.setCode(basicsdatumSize.getSort());
                smpSize.setProductSizeName(basicsdatumSize.getHangtags());
                smpSize.setBaseSize(StringUtils.isNoneBlank(style.getDefaultSize()) && style.getDefaultSize().equals(basicsdatumSize.getHangtags()));

                smpSize.setSkuFiller(downContent);
                smpSize.setSpecialSpec(basicsdatumSize.getInternalSize());
                smpSizes.add(smpSize);
            }
            smpGoodsDto.setItemList(smpSizes);
            HttpResp httpResp = restTemplateService.spmPost(SMP_URL + "/goods", smpGoodsDto);
            Boolean aBoolean = pushRecordsService.pushRecordSave(httpResp, smpGoodsDto, "smp", "商品主数据下发");
            if (aBoolean) {
                i++;
                styleColor.setScmSendFlag("1");
            } else {
                styleColor.setScmSendFlag("2");
            }
            sampleStyleColorService.updateById(styleColor);
        }
        return i;
    }

    /**
     * 物料主数据下发
     */
    public Integer materials(String[] ids) {
        if (ids.length == 0) {
            return 0;
        }
        List<BasicsdatumMaterial> list = basicsdatumMaterialService.listByIds(Arrays.asList(ids));
        int i = 0;
        for (BasicsdatumMaterial basicsdatumMaterial : list) {
            if (this.sendMaterials(basicsdatumMaterial)) {
                i++;
            }
        }

        return i;
    }

    public Boolean sendMaterials(BasicsdatumMaterial basicsdatumMaterial) {
        TransactionStatus transactionStatus = null;
        try {
            SmpMaterialDto smpMaterialDto = basicsdatumMaterial.toSmpMaterialDto();


            //获取颜色集合
            BasicsdatumMaterialColorQueryDto basicsdatumMaterialColorQueryDto = new BasicsdatumMaterialColorQueryDto();
            basicsdatumMaterialColorQueryDto.setMaterialCode(basicsdatumMaterial.getMaterialCode());
            List<BasicsdatumMaterialColorPageVo> list1 = basicsdatumMaterialService.getBasicsdatumMaterialColorList(basicsdatumMaterialColorQueryDto).getList();
            List<SmpColor> colorList = new ArrayList<>();
            for (BasicsdatumMaterialColorPageVo basicsdatumMaterialColorPageVo : list1) {
                SmpColor smpColor = new SmpColor();
                smpColor.setColorCode(basicsdatumMaterialColorPageVo.getColorCode());
                smpColor.setColorName(basicsdatumMaterialColorPageVo.getColor());
                smpColor.setActive("0".equals(basicsdatumMaterialColorPageVo.getStatus()));
                smpColor.setSupplierMatColor(basicsdatumMaterialColorPageVo.getSupplierColorCode());
                colorList.add(smpColor);
            }
            if (colorList.size() == 0) {
                throw new OtherException("颜色不能为空");
            }
            smpMaterialDto.setColorList(colorList);

            //修改颜色为下发状态
            List<String> collect = list1.stream().map(BasicsdatumMaterialColorPageVo::getId).collect(Collectors.toList());
            UpdateWrapper<BasicsdatumMaterialColor> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("scm_status", "1").in("id", collect);
            basicsdatumMaterialColorService.update(updateWrapper);


            //获取材料尺码集合
            QueryWrapper<BasicsdatumMaterialWidth> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("material_code", basicsdatumMaterial.getMaterialCode());
            queryWrapper1.eq("company_code", userUtils.getCompanyCode());
            List<SmpModuleSize> moduleSizeList = new ArrayList<>();
            List<BasicsdatumMaterialWidth> list2 = basicsdatumMaterialWidthService.list(queryWrapper1);
            for (BasicsdatumMaterialWidth basicsdatumMaterialWidth : list2) {
                SmpModuleSize smpModuleSize = new SmpModuleSize();
                smpModuleSize.setActive("0".equals(basicsdatumMaterialWidth.getStatus()));
                smpModuleSize.setSizeCode(basicsdatumMaterialWidth.getName());
                smpModuleSize.setSizeUrl(basicsdatumMaterialWidth.getWidthCode());

                smpModuleSize.setSizeName(basicsdatumMaterialWidth.getSizeName());
                smpModuleSize.setSortCode(basicsdatumMaterialWidth.getSortCode());
                smpModuleSize.setCode(basicsdatumMaterialWidth.getCode());
                moduleSizeList.add(smpModuleSize);
            }


            if (moduleSizeList.size() == 0) {
                throw new OtherException("规格不能为空");
            }
            smpMaterialDto.setModuleSizeList(moduleSizeList);

            //修改规格为下发状态
            List<String> collect1 = list2.stream().map(BasicsdatumMaterialWidth::getId).collect(Collectors.toList());
            UpdateWrapper<BasicsdatumMaterialWidth> updateWrapper1 = new UpdateWrapper<>();
            updateWrapper1.set("scm_status", "1").in("id", collect1);
            basicsdatumMaterialWidthService.update(updateWrapper1);

            //获取报价集合

            BasicsdatumMaterialPriceQueryDto dto = new BasicsdatumMaterialPriceQueryDto();
            dto.setMaterialCode(basicsdatumMaterial.getMaterialCode());

            List<SmpQuot> quotList = new ArrayList<>();

            List<BasicsdatumMaterialPricePageVo> materialPricePageVoList = new ArrayList<>();
            List<BasicsdatumMaterialPricePageVo> list = basicsdatumMaterialService.getBasicsdatumMaterialPriceList(dto).getList();
            List<String> priceIds = list.stream().map(BasicsdatumMaterialPricePageVo::getId).collect(Collectors.toList());
            //拆分sku
            for (BasicsdatumMaterialPricePageVo basicsdatumMaterialPricePageVo : list) {
                String[] widths = basicsdatumMaterialPricePageVo.getWidth().split(",");
                String[] colors = basicsdatumMaterialPricePageVo.getColor().split(",");

                for (int i = 0; i < colors.length; i++) {
                    for (int j = 0; j < widths.length; j++) {
                        BasicsdatumMaterialPricePageVo basicsdatumMaterialPricePageVo1 = new BasicsdatumMaterialPricePageVo();
                        BeanUtil.copyProperties(basicsdatumMaterialPricePageVo, basicsdatumMaterialPricePageVo1);
                        basicsdatumMaterialPricePageVo1.setColor(colors[i]);
                        basicsdatumMaterialPricePageVo1.setWidth(widths[j]);
                        try {
                            for (SmpColor smpColor : colorList) {
                                if (colors[i].equals(smpColor.getColorCode())) {
                                    basicsdatumMaterialPricePageVo1.setColorName(smpColor.getColorName());
                                }
                            }
                            for (SmpModuleSize smpModuleSize : moduleSizeList) {
                                if (widths[j].equals(smpModuleSize.getSizeUrl())) {
                                    basicsdatumMaterialPricePageVo1.setWidthName(smpModuleSize.getSizeCode());
                                }
                            }

                        } catch (Exception ignored) {

                        }
                        materialPricePageVoList.add(basicsdatumMaterialPricePageVo1);
                    }
                }
            }

            for (BasicsdatumMaterialPricePageVo basicsdatumMaterialPricePageVo : materialPricePageVoList) {
                SmpQuot smpQuot = new SmpQuot();
                smpQuot.setSupplierSize(basicsdatumMaterialPricePageVo.getWidthName());
                smpQuot.setSizeUrl(basicsdatumMaterialPricePageVo.getWidth());
                smpQuot.setSupplierColorId(basicsdatumMaterialPricePageVo.getColor());
                smpQuot.setSupplierColorName(basicsdatumMaterialPricePageVo.getColorName());
                smpQuot.setOrderGoodsDay(basicsdatumMaterialPricePageVo.getOrderDay());
                smpQuot.setProductionDay(basicsdatumMaterialPricePageVo.getProductionDay());
                smpQuot.setMoqInitial(basicsdatumMaterialPricePageVo.getMinimumOrderQuantity());
                smpQuot.setFobFullPrice(basicsdatumMaterialPricePageVo.getQuotationPrice());
                smpQuot.setSupplierMaterial(basicsdatumMaterialPricePageVo.getSupplierMaterialCode());
                smpQuot.setSupplierCode(basicsdatumMaterialPricePageVo.getSupplierId());
                smpQuot.setSupplierName(basicsdatumMaterialPricePageVo.getSupplierName());
                smpQuot.setComment(basicsdatumMaterialPricePageVo.getRemarks());
                smpQuot.setTradeTermKey(null);
                smpQuot.setTradeTermName(null);
                smpQuot.setDefaultQuote(basicsdatumMaterialPricePageVo.getSelectFlag());


                smpQuot.setMaterialUom(basicsdatumMaterial.getStockUnitCode());
                quotList.add(smpQuot);
            }
            if (quotList.size() == 0) {
                throw new OtherException("报价不能为空");
            }
            smpMaterialDto.setQuotList(quotList);


            //下发并记录推送日志
            HttpResp httpResp = restTemplateService.spmPost(SMP_URL + "/materials", smpMaterialDto);

            //获取事务
            transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
            pushRecordsService.pushRecordSave(httpResp, smpMaterialDto, "smp", "物料主数据下发");

            //修改状态为已下发
            basicsdatumMaterial.setDistribute("1");
            basicsdatumMaterialService.updateById(basicsdatumMaterial);
            basicsdatumMaterialPriceService.update(new UpdateWrapper<BasicsdatumMaterialPrice>().set("scm_status", "1").in("id", priceIds));
            //提交事务
            dataSourceTransactionManager.commit(transactionStatus);
        } catch (OtherException otherException) {
            if (transactionStatus != null) {
                //回滚事务
                dataSourceTransactionManager.rollback(transactionStatus);
            }
            throw new OtherException(otherException.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            if (transactionStatus != null) {
                //回滚事务
                dataSourceTransactionManager.rollback(transactionStatus);
            }
            return false;
        }

        return true;
    }

    /**
     * bom下发
     */
    public Integer bom(String[] ids) {

        int i = 0;
        List<PackBom> list = packBomService.listByIds(Arrays.asList(ids));
        for (PackBom packBom : list) {

            packBomVersionService.checkBomDataEmptyThrowException(packBom);
            SmpBomDto smpBomDto = packBom.toSmpBomDto();


            //bom主表
            PackInfo packInfo = packInfoService.getById(packBom.getForeignId());
            if (StringUtils.isEmpty(packInfo.getStyleNo())) {
                throw new OtherException(packBom.getMaterialName() + "未关联大货(配色)信息,无法下发");
            }
            Style style = styleService.getOne(new QueryWrapper<Style>().eq("id", packInfo.getForeignId()));
            StylePricingVO stylePricingVO = stylePricingService.getByPackId(packInfo.getId(), style.getCompanyCode());
            //"0".equals(stylePricingVO.getBomStage()) ? "Sample" : "Production"
            smpBomDto.setBomStage(stylePricingVO.getBomStage());
            //样衣-款式配色
            StyleColor styleColor = sampleStyleColorService.getById(packInfo.getStyleColorId());
            if (styleColor == null) {
                throw new OtherException("未关联配色,无法下发");

            }
            smpBomDto.setPColorCode(styleColor.getColorCode());
            smpBomDto.setPColorName(styleColor.getColorName());
            smpBomDto.setBulkNumber(packInfo.getStyleNo());
            smpBomDto.setBomCode(packInfo.getCode());
            smpBomDto.setCode(packInfo.getCode());
            //List<BomMaterial> bomMaterials = new ArrayList<>();
            //
            //BomMaterial bomMaterial = packBom.toBomMaterial();
            //bomMaterial.setBomId(packInfo.getCode());
            //bomMaterials.add(bomMaterial);
            //smpBomDto.setBomMaterials(bomMaterials);

            List<SmpSizeQty> sizeQtyList = new ArrayList<>();
            for (PackBomSize packBomSize : packBomSizeService.list(new QueryWrapper<PackBomSize>().eq("bom_id", packBom.getId()))) {
                packBomVersionService.checkBomSizeDataEmptyThrowException(packBomSize);
                SmpSizeQty smpSizeQty = packBomSize.toSmpSizeQty();
                //根据尺码id查询尺码
                BasicsdatumSize basicsdatumSize = basicsdatumSizeService.getById(packBomSize.getSizeId());
                if (basicsdatumSize != null) {
                    smpSizeQty.setPSizeCode(basicsdatumSize.getSort());
                    smpSizeQty.setItemSize(basicsdatumSize.getInternalSize());

                    sizeQtyList.add(smpSizeQty);
                }

            }
            smpBomDto.setSizeQtyList(sizeQtyList);


            HttpResp httpResp = restTemplateService.spmPost(SMP_URL + "/bom", smpBomDto);
            Boolean aBoolean = pushRecordsService.pushRecordSave(httpResp, smpBomDto, "smp", "bom下发");

            if (aBoolean) {
                packBom.setScmSendFlag("1");
                i++;
            } else {
                packBom.setScmSendFlag("2");
            }
            packBomService.updateById(packBom);
            //PackInfoStatus packInfoStatus = packInfoStatusService.getOne(new QueryWrapper<PackInfoStatus>().eq("foreign_id", packInfo.getId()).eq("pack_type", packBom.getPackType()));
            //long count = packBomService.count(new QueryWrapper<PackBom>().eq("foreign_id", packInfo.getId()).eq("scm_send_flag", "1"));
            //
            //long count1 = packBomService.count(new QueryWrapper<PackBom>().eq("foreign_id", packInfo.getId()));
            //if (count == 0) {
            //    packInfoStatus.setScmSendFlag("0");
            //} else if (count1 == count) {
            //    packInfoStatus.setScmSendFlag("1");
            //} else {
            //    packInfoStatus.setScmSendFlag("2");
            //}
            //packInfoStatusService.updateById(packInfoStatus);
        }

        return i;
    }

    /**
     * 颜色主数据下发
     */
    public Integer color(String[] ids) {
        if (ids.length == 0) {
            return 0;
        }
        List<BasicsdatumColourLibrary> basicsdatumColourLibraries = basicsdatumColourLibraryService.list(new QueryWrapper<BasicsdatumColourLibrary>().in("id", Arrays.asList(ids)));
        Boolean issuedToExternalSmpSystemSwitch = ccmFeignService.getSwitchByCode(ISSUED_TO_EXTERNAL_SMP_SYSTEM_SWITCH.getKeyCode());

        int i = 0;
        for (BasicsdatumColourLibrary basicsdatumColourLibrary : basicsdatumColourLibraries) {
            if (issuedToExternalSmpSystemSwitch) {
                SmpColorDto smpColorDto = basicsdatumColourLibrary.toSmpColorDto();
                HttpResp httpResp = restTemplateService.spmPost(SMP_URL + "/color", smpColorDto);
                Boolean aBoolean = pushRecordsService.pushRecordSave(httpResp, smpColorDto, "smp", "颜色主数据下发");
                if (aBoolean) {
                    i++;
                    basicsdatumColourLibrary.setScmSendFlag("1");
                } else {
                    basicsdatumColourLibrary.setScmSendFlag("2");
                }
            } else {
                basicsdatumColourLibrary.setScmSendFlag("1");
            }

            basicsdatumColourLibraryService.updateById(basicsdatumColourLibrary);
        }
        return i;
    }

    /**
     * 工艺单下发
     */
    public Integer processSheet(List<SmpProcessSheetDto> sheetDtoList) {
        int i = 0;
        IdGen idGen = new IdGen();
        for (SmpProcessSheetDto smpProcessSheetDto : sheetDtoList) {
            String id = String.valueOf(idGen.nextId());
            PackInfoStatus packInfoStatus = packInfoStatusService.get(smpProcessSheetDto.getForeignId(), smpProcessSheetDto.getPackType());
            smpProcessSheetDto.setCode(smpProcessSheetDto.getBulkNumber());
            smpProcessSheetDto.setSyncId(id);
            smpProcessSheetDto.setId(id);
            HttpResp httpResp = restTemplateService.spmPost(SMP_URL + "/processSheet", smpProcessSheetDto);
            Boolean aBoolean = pushRecordsService.pushRecordSave(httpResp, smpProcessSheetDto, "smp", "工艺单下发");
            if (aBoolean) {
                i++;
                packInfoStatus.setTechScmSendFlag("1");
            } else {
                packInfoStatus.setTechScmSendFlag("2");
            }
            packInfoStatusService.updateById(packInfoStatus);
        }
        return i;
    }

    /**
     * 样衣下发,打板管理
     */
    public Integer sample(String[] ids) {
        int i = 0;
        for (PatternMaking patternMaking : patternMakingService.listByIds(Arrays.asList(ids))) {
            Style style = styleService.getById(patternMaking.getStyleId());
            SmpSampleDto smpSampleDto = style.toSmpSampleDto();
            //QueryWrapper<Sample> queryWrapper = new QueryWrapper<Sample>().eq("pattern_making_id", patternMaking.getId());
            //queryWrapper.orderByAsc("update_date");
            //queryWrapper.last("limit 1");
            //Sample sample = sampleService.getOne(queryWrapper);
            //if (sample == null) {
            //    throw new OtherException("请先在样衣编辑界面绑定打板指令");
            //}
            try {
                UploadFile uploadFile = uploadFileService.getById(style.getStylePic());
                smpSampleDto.setImgList(Arrays.asList(uploadFile.getUrl().split(",")));
            } catch (Exception ignored) {
            }

            smpSampleDto.setSampleType(patternMaking.getSampleType());
            smpSampleDto.setSampleTypeName(patternMaking.getSampleTypeName());

            //取跟款设计师，如果跟款设计师不存在就取设计师
            smpSampleDto.setProofingDesigner(style.getMerchDesignName() == null ? style.getDesigner() : style.getMerchDesignName());
            smpSampleDto.setPatDiff(style.getPatDiff());
            smpSampleDto.setSampleNumber(patternMaking.getCode());
            smpSampleDto.setColorwayCode(style.getStyleNo());
            smpSampleDto.setCode(patternMaking.getCode());
            smpSampleDto.setColorwayPlmId(style.getStyleNo());
            smpSampleDto.setSampleStatus(style.getStatus());
            smpSampleDto.setSampleStatusName("0".equals(style.getStatus()) ? "未开款" : "1".equals(style.getStatus()) ? "已开款" : "已下发打板(完成)");
            smpSampleDto.setBrandCode(style.getBrand());
            smpSampleDto.setBrandName(style.getBrandName());

            String designerId = style.getDesignerId();
            String technicianId = style.getTechnicianId();
            String patternDesignId = style.getPatternDesignId();
            smpSampleDto.setYear(style.getYear());
            smpSampleDto.setDesigner(style.getDesigner());
            smpSampleDto.setTechnician(style.getTechnicianName());
            smpSampleDto.setStyleUrl(style.getStylePic());


            smpSampleDto.setNodeName(style.getStyleName());


            String merchDesignId = style.getMerchDesignId();
            if (merchDesignId == null) {
                merchDesignId = designerId;
            }
            ArrayList<String> list = new ArrayList<>();
            list.add(designerId);
            list.add(technicianId);
            list.add(patternDesignId);

            list.add(merchDesignId);

            Map<String, String> usernamesByIds = amcService.getUsernamesByIds(StringUtils.join(list, ","));
            smpSampleDto.setDesignerId(usernamesByIds.get(designerId));
            smpSampleDto.setTechnicianId(usernamesByIds.get(technicianId));
            smpSampleDto.setPatternMakerId(usernamesByIds.get(patternDesignId));
            smpSampleDto.setProofingDesignerId(usernamesByIds.get(merchDesignId));

            smpSampleDto.setSupplier(patternMaking.getPatternRoom());
            smpSampleDto.setSupplierNumber(patternMaking.getPatternRoom());

            smpSampleDto.setPatSeqName(patternMaking.getPatSeqName());
            smpSampleDto.setPatSeq(patternMaking.getPatSeq());
            smpSampleDto.setEAValidFromTime(patternMaking.getExtAuxiliaryDispatchDate());
            smpSampleDto.setEAValidToTime(patternMaking.getExtAuxiliaryReceiveDate());
            smpSampleDto.setFinished("1".equals(patternMaking.getEndFlg()));
            smpSampleDto.setMCDate(patternMaking.getSglKittingDate());
            smpSampleDto.setPmlId(patternMaking.getId());
            smpSampleDto.setBExtAuxiliary("1".equals(patternMaking.getExtAuxiliary()));
            smpSampleDto.setSampleNumberName(patternMaking.getCode());
            smpSampleDto.setBarcode(patternMaking.getSampleBarCode());

            HttpResp httpResp = restTemplateService.spmPost(OA_URL + "/setSampleTask", smpSampleDto.toSampleBean());
            Boolean aBoolean = pushRecordsService.pushRecordSave(httpResp, smpSampleDto.toSampleBean(), "oa", "样衣下发");
            if (aBoolean) {
                i++;
                patternMaking.setScmSendFlag("1");
            } else {
                patternMaking.setScmSendFlag("2");
            }
            patternMakingService.updateById(patternMaking);
        }
        return i;
    }

    /**
     * 面料成分名称码表下发
     */
    public Integer fabricComposition(String[] ids) {
        int i = 0;
        for (BasicsdatumIngredient basicsdatumIngredient : basicsdatumIngredientService.listByIds(Arrays.asList(ids))) {
            FabricCompositionDto fabricCompositionDto = new FabricCompositionDto();
            fabricCompositionDto.setName(basicsdatumIngredient.getIngredient());
            fabricCompositionDto.setCode(basicsdatumIngredient.getCode());
            fabricCompositionDto.setId(fabricCompositionDto.getId());
            fabricCompositionDto.setIngredient(basicsdatumIngredient.getIngredient());

            HttpResp httpResp = restTemplateService.spmPost(SCM_URL + "/materialElement", fabricCompositionDto);
            Boolean aBoolean = pushRecordsService.pushRecordSave(httpResp, fabricCompositionDto, "scm", "面料成分名称码表下发");
            if (aBoolean) {
                i++;
                basicsdatumIngredient.setScmSendFlag("1");
            } else {
                basicsdatumIngredient.setScmSendFlag("2");
            }
            basicsdatumIngredientService.updateById(basicsdatumIngredient);
        }
        return i;
    }

    /**
     * 修改商品尺码的时候验证
     */
    public Boolean checkStyleSize(PlmStyleSizeParam param) {
        param.setCode(param.getStyleNo());
        HttpResp httpResp = restTemplateService.spmPost(SCM_URL + "/checkStyleGroup", param);
        return pushRecordsService.pushRecordSave(httpResp, param, "scm", "修改尺码的时候验证");
    }

    /**
     * 修改商品颜色的时候验证
     */
    public Boolean checkColorSize(PdmStyleCheckParam param) {
        HttpResp httpResp = restTemplateService.spmPost(SCM_URL + "/checkColorSize", param);
        return pushRecordsService.pushRecordSave(httpResp, param, "scm", "修改商品颜色的时候验证");
    }

    /**
     * 停用物料的规格和颜色的时候校验
     */
    public Boolean checkSizeAndColor(String materialCode, String type, String code) {
        CheckMaterial checkMaterial = new CheckMaterial();
        List<CheckMaterial.CheckSku> checkSkuList = new ArrayList<>();
        checkMaterial.setMaterialCode(materialCode);
        checkMaterial.setCode(materialCode);
        if ("1".equals(type)) {
            for (BasicsdatumMaterialColor basicsdatumMaterialColor : basicsdatumMaterialColorService.list(new QueryWrapper<BasicsdatumMaterialColor>().eq("material_code", materialCode))) {
                CheckMaterial.CheckSku checkSku = new CheckMaterial.CheckSku();
                checkSku.setColorCode(basicsdatumMaterialColor.getColorCode());
                checkSku.setSizeCode(code);
                checkSkuList.add(checkSku);
            }
            checkMaterial.setCheckSkuList(checkSkuList);
        }
        if ("2".equals(type)) {
            for (BasicsdatumMaterialWidth basicsdatumMaterialWidth : basicsdatumMaterialWidthService.list(new QueryWrapper<BasicsdatumMaterialWidth>().eq("material_code", materialCode))) {
                CheckMaterial.CheckSku checkSku = new CheckMaterial.CheckSku();
                checkSku.setColorCode(code);
                checkSku.setSizeCode(basicsdatumMaterialWidth.getWidthCode());
                checkSkuList.add(checkSku);
            }
            checkMaterial.setCheckSkuList(checkSkuList);
        }
        HttpResp httpResp = restTemplateService.spmPost(SCM_URL + "/checkMaterialsStopAndStock", checkMaterial);
        return pushRecordsService.pushRecordSave(httpResp, checkMaterial, "scm", "停用物料尺码和颜色的时候验证");

    }


    /**
     * 修改吊牌价的时候验证(暂不需要)
     */
    public Boolean checkUpdatePrice(UpdatePriceDto updatePriceDto) {
        HttpResp httpResp = restTemplateService.spmPost(SCM_URL + "/checkUpdatePrice", updatePriceDto);
        return pushRecordsService.pushRecordSave(httpResp, updatePriceDto, "smp", "修改吊牌价的时候验证");
    }

    /**
     * 产前样下发
     *
     * @param ids 产前样任务id集合
     * @return 下发成功的数量
     */
    public Integer antenatalSample(String[] ids) {
        int i = 0;
        IdGen idGen = new IdGen();
        for (PreProductionSampleTask preProductionSampleTask : preProductionSampleTaskService.listByIds(Arrays.asList(ids))) {
            Style style = styleService.getById(preProductionSampleTask.getStyleId());
            SmpSampleDto smpSampleDto = new SmpSampleDto();
            // 跟款设计师
            smpSampleDto.setProofingDesigner(style.getMerchDesignName() == null ? style.getDesigner() : style.getMerchDesignName());
            // 跟款设计师工号
            smpSampleDto.setProofingDesignerId(style.getMerchDesignId() == null ? style.getDesignerId() : style.getMerchDesignId());

            // 样衣编号
            smpSampleDto.setBarcode(preProductionSampleTask.getSampleBarCode());


            // 样衣结束标志
            smpSampleDto.setFinished("1".equals(preProductionSampleTask.getFinishFlag()));

            // 打样URL
            smpSampleDto.setPmlId(preProductionSampleTask.getId());

            // 打样顺序名称
            smpSampleDto.setPatSeqName(preProductionSampleTask.getPatSeq());
            // 版号
            smpSampleDto.setSampleNumber(preProductionSampleTask.getCode());
            // 版号名称
            smpSampleDto.setSampleNumberName(preProductionSampleTask.getCode());
            PackInfo packInfo = packInfoService.getById(preProductionSampleTask.getPackInfoId());
            StyleColor styleColor = styleColorService.getById(packInfo.getStyleColorId());

            // 大货款号
            smpSampleDto.setColorwayCode(styleColor.getStyleNo());
            // 大货款号唯一标识
            smpSampleDto.setColorwayPlmId(styleColor.getId());
            // 样衣名
            smpSampleDto.setNodeName(style.getStyleName());
            // 设计收样日期
            smpSampleDto.setSampleReceivedDate(style.getCreateDate());
            // 状态
            smpSampleDto.setSampleStatus(style.getStatus());
            // 状态名称
            smpSampleDto.setSampleStatusName("0".equals(style.getStatus()) ? "未开款" : "1".equals(style.getStatus()) ? "已开款" : "已下发打板(完成)");
            // 样衣类型
            smpSampleDto.setSampleType("产前样");
            // 样衣类型名称
            smpSampleDto.setSampleTypeName("产前样");
            // 大类
            smpSampleDto.setMajorCategories(style.getProdCategory1st());
            // 大类名称
            smpSampleDto.setMajorCategoriesName(style.getProdCategory1stName());
            // 品类
            smpSampleDto.setCategory(style.getProdCategory());
            // 品类名称
            smpSampleDto.setCategoryName(style.getProdCategoryName());
            // 品牌code
            smpSampleDto.setBrandCode(style.getBrand());
            // 品牌名称
            smpSampleDto.setBrandName(style.getBrandName());
            // 季节code
            smpSampleDto.setQuarterCode(style.getSeason());
            // 年份
            smpSampleDto.setYear(style.getYearName());
            // 设计师
            smpSampleDto.setDesigner(style.getDesigner());
            // 设计师工号
            smpSampleDto.setDesignerId(style.getDesignerId());
            // 版师
            smpSampleDto.setPatternMaker(style.getPatternDesignName());
            // 版师id
            smpSampleDto.setPatternMakerId(style.getPatternDesignId());
            // 工艺员
            smpSampleDto.setTechnician(preProductionSampleTask.getTechnologistName());
            // 工艺员id
            smpSampleDto.setTechnicianId(preProductionSampleTask.getTechnologistId());
            // 中类id
            smpSampleDto.setMiddleClassId(style.getProdCategory2nd());
            // 中类名称
            smpSampleDto.setMiddleClassName(style.getProdCategory2ndName());
            // 款式URL
            smpSampleDto.setStyleUrl(style.getStylePic());
            // 设计款号
            smpSampleDto.setStyleCode(style.getDesignNo());
            // 供应商：打样部门
            smpSampleDto.setSupplier(preProductionSampleTask.getPatternRoom());
            // 打样部门编号
            smpSampleDto.setSupplierNumber(preProductionSampleTask.getPatternRoomId());
            // 图片集合
            UploadFile uploadFile = uploadFileService.getById(style.getStylePic());
            smpSampleDto.setImgList(Arrays.asList(uploadFile.getUrl().split(",")));

            smpSampleDto.setId(preProductionSampleTask.getId()); // ID
            smpSampleDto.setCode(preProductionSampleTask.getCode()); // Code
            smpSampleDto.setName(null); // Name
            smpSampleDto.setCreator(preProductionSampleTask.getCreateName()); // 创建人
            smpSampleDto.setCreateTime(preProductionSampleTask.getCreateDate()); // 创建时间
            smpSampleDto.setModifiedPerson(preProductionSampleTask.getUpdateName()); // 修改人
            smpSampleDto.setModifiedTime(preProductionSampleTask.getUpdateDate()); // 修改时间
            String id = String.valueOf(idGen.nextId());
            smpSampleDto.setPlmId(id); // PLMID
            smpSampleDto.setSyncId(id); // 同步ID
            smpSampleDto.setActive(true); // 是否启用

            //样衣条码    款式id 打版编码
            PatternMaking patternMaking = patternMakingService.getOne(new QueryWrapper<PatternMaking>().eq("style_id", style.getId()).orderByDesc("create_date").last("limit 1"));
            if (patternMaking != null) {
                // 打版难度编号
                smpSampleDto.setPatDiff(patternMaking.getPatDiff());
                // 打版难度名称
                smpSampleDto.setPatDiffName(patternMaking.getPatDiffName());
                // 打样顺序编号
                smpSampleDto.setPatSeq(patternMaking.getPatSeq());
                // 外辅标志
                smpSampleDto.setBExtAuxiliary("1".equals(patternMaking.getExtAuxiliary()));
                // 外辅发出时间
                smpSampleDto.setEAValidFromTime(patternMaking.getExtAuxiliaryDispatchDate());
                // 外辅接收时间
                smpSampleDto.setEAValidToTime(patternMaking.getExtAuxiliaryReceiveDate());
            }
            // 齐套时间
            smpSampleDto.setMCDate("1".equals(preProductionSampleTask.getKitting()) ? preProductionSampleTask.getKittingTime() : null);

            HttpResp httpResp = restTemplateService.spmPost(OA_URL + "/setSampleTask", smpSampleDto.toSampleBean());
            Boolean aBoolean = pushRecordsService.pushRecordSave(httpResp, smpSampleDto.toSampleBean(), "oa", "样衣下发");
            if (aBoolean) {
                i++;
                preProductionSampleTask.setScmSendFlag("1");
            } else {
                preProductionSampleTask.setScmSendFlag("2");
            }
            preProductionSampleTaskService.updateById(preProductionSampleTask);
        }

        return i;
    }
}

