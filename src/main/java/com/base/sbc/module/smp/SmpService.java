package com.base.sbc.module.smp;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.service.AmcService;
import com.base.sbc.client.ccm.service.CcmFeignService;
import com.base.sbc.client.ccm.service.CcmService;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.restTemplate.RestTemplateService;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialColorQueryDto;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialPriceQueryDto;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumColourLibrary;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterial;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumMaterialWidth;
import com.base.sbc.module.basicsdatum.entity.BasicsdatumSize;
import com.base.sbc.module.basicsdatum.mapper.BasicsdatumModelTypeMapper;
import com.base.sbc.module.basicsdatum.service.*;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialColorPageVo;
import com.base.sbc.module.basicsdatum.vo.BasicsdatumMaterialPricePageVo;
import com.base.sbc.module.common.service.AttachmentService;
import com.base.sbc.module.common.utils.AttachmentTypeConstant;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.module.formType.vo.FieldManagementVo;
import com.base.sbc.module.pack.entity.PackBom;
import com.base.sbc.module.pack.entity.PackBomSize;
import com.base.sbc.module.pack.entity.PackInfo;
import com.base.sbc.module.pack.entity.PackInfoStatus;
import com.base.sbc.module.pack.service.PackBomService;
import com.base.sbc.module.pack.service.PackBomSizeService;
import com.base.sbc.module.pack.service.PackInfoService;
import com.base.sbc.module.pack.service.PackInfoStatusService;
import com.base.sbc.module.pushRecords.service.PushRecordsService;
import com.base.sbc.module.sample.entity.SampleDesign;
import com.base.sbc.module.sample.entity.SampleStyleColor;
import com.base.sbc.module.sample.service.SampleDesignService;
import com.base.sbc.module.sample.service.SampleStyleColorService;
import com.base.sbc.module.sample.vo.SampleDesignVo;
import com.base.sbc.module.smp.dto.*;
import com.base.sbc.module.smp.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.util.*;


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

    private final CcmService ccmService;

    private final AmcService amcService;

    private final UserUtils userUtils;

    private final PackInfoService packInfoService;

    private final SampleStyleColorService sampleStyleColorService;

    private final PackBomService packBomService;

    private final BasicsdatumColourLibraryService basicsdatumColourLibraryService;

    private final PackBomSizeService packBomSizeService;

    private final BasicsdatumSizeService basicsdatumSizeService;

    private final SampleDesignService sampleDesignService;

    private final CcmFeignService ccmFeignService;

    private final AttachmentService attachmentService;

    private final BasicsdatumModelTypeService basicsdatumModelTypeService;

    private final PackInfoStatusService packInfoStatusService;


    private static final String SMP_URL = "http://10.98.250.31:7006/pdm";
    //private static final String PDM_URL = "http://smp-i.eifini.com/service-manager/pdm";

    private static final String SCM_URL = "http://10.8.250.100:1980/escm-app/information/pdm";


    /**
     * 商品主数据下发
     */
    public Integer goods(String[] ids) {
        int i = 0;
        List<SampleStyleColor> sampleStyleColors = sampleStyleColorService.listByIds(Arrays.asList(ids));

        for (SampleStyleColor sampleStyleColor : sampleStyleColors) {
            SmpGoodsDto smpGoodsDto = sampleStyleColor.toSmpGoodsDto();

            SampleDesignVo sampleDesign = sampleDesignService.getDetail(sampleStyleColor.getSampleDesignId());

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
            List<AttachmentVo> stylePicList = attachmentService.findByforeignId(sampleDesign.getId(), AttachmentTypeConstant.SAMPLE_DESIGN_FILE_STYLE_PIC);
            List<String> imgList = new ArrayList<>();
            for (AttachmentVo attachmentVo : stylePicList) {
                imgList.add(attachmentVo.getUrl());
            }
            smpGoodsDto.setImgList(imgList);

            //设计师id,下稿设计师,工艺员,工艺员id,版师名称,版师ID
            smpGoodsDto.setDesigner(sampleDesign.getDesigner());
            smpGoodsDto.setTechnician(sampleDesign.getTechnicianName());
            smpGoodsDto.setPatternMakerName(sampleDesign.getPatternDesignName());

            String designerId = sampleDesign.getDesignerId();
            String technicianId = sampleDesign.getTechnicianId();
            String patternDesignId = sampleDesign.getPatternDesignId();

            ArrayList<String> list = new ArrayList<>();
            list.add(designerId);
            list.add(technicianId);
            list.add(patternDesignId);

            Map<String, String> usernamesByIds = amcService.getUsernamesByIds(StringUtils.join(list, ","));
            smpGoodsDto.setDesignerId(usernamesByIds.get(designerId));
            smpGoodsDto.setTechnicianId(usernamesByIds.get(technicianId));
            smpGoodsDto.setPatternMakerId(usernamesByIds.get(patternDesignId));
            smpGoodsDto.setYear(sampleDesign.getYear());
            smpGoodsDto.setPatternName(sampleDesign.getDevtType());
            smpGoodsDto.setPriorityId(sampleDesign.getTaskLevel());
            smpGoodsDto.setPriorityName("1".equals(sampleDesign.getTaskLevel()) ? "普通" : "2".equals(sampleDesign.getTaskLevel()) ? "紧急" : "非常紧急");
            smpGoodsDto.setSeason(sampleDesign.getSeason());
            smpGoodsDto.setTheme(sampleDesign.getSubject());
            smpGoodsDto.setStyleName(sampleDesign.getStyleName());
            smpGoodsDto.setTargetCost(sampleDesign.getProductCost());
            smpGoodsDto.setShapeName(sampleDesign.getPlateType());


            Map<String, Map<String, String>> dictInfoToMap = ccmFeignService.getDictInfoToMap("C8_Band");
            Map<String, String> map = dictInfoToMap.get("C8_Band");
            smpGoodsDto.setBandName(map.get(sampleDesign.getBandCode()));

            //List<FieldVal> list1 = fieldValService.list(sampleDesign.getId(), FieldValDataGroupConstant.SAMPLE_DESIGN_TECHNOLOGY);


            //动态字段
            smpGoodsDto.setLengthRangeId(null);
            smpGoodsDto.setLengthRangeName(null);
            smpGoodsDto.setCoatLength(null);
            smpGoodsDto.setWaistTypeId(null);
            smpGoodsDto.setWaistTypeName(null);
            smpGoodsDto.setSleeveLengthId(null);
            smpGoodsDto.setSleeveLengthName(null);
            smpGoodsDto.setSleeveId(null);
            smpGoodsDto.setSleeveName(null);
            smpGoodsDto.setBust(null);
            smpGoodsDto.setPlacketId(null);
            smpGoodsDto.setPlacketName(null);
            smpGoodsDto.setYarnNeedleTypeId(null);
            smpGoodsDto.setYarnNeedleTypeName(null);
            smpGoodsDto.setYarnNeedleId(null);
            smpGoodsDto.setYarnNeedleName(null);
            smpGoodsDto.setProfileId(null);
            smpGoodsDto.setProfileName(null);
            smpGoodsDto.setFlowerId(null);
            smpGoodsDto.setFlowerName(null);
            smpGoodsDto.setLingXingId(null);
            smpGoodsDto.setLingXingName(null);

            if (!CollectionUtils.isEmpty(sampleDesign.getDimensionLabels())) {
                List<FieldManagementVo> fieldManagementVoList = sampleDesign.getDimensionLabels();
                fieldManagementVoList.forEach(m -> {
                    try {
                        BeanUtil.setProperty(smpGoodsDto, m.getFieldName(), m.getVal());
                    } catch (Exception e) {

                    }
                });
            }

            //try {
            //    for (FieldVal fieldVal : list1) {
            //        if ("袖型".equals(fieldVal.getFieldName())){
            //            smpGoodsDto.setSleeveId(new ObjectMapper().readValue(fieldVal.getVal(), String[].class)[0]);
            //        }
            //    }
            //}catch (Exception e){
            //    e.printStackTrace();
            //}

            //if (true) {
            //    return null;
            //}
            // 有
            smpGoodsDto.setBandName(null);//样衣设计

            smpGoodsDto.setAccessories(null);//配色
            // 待确认
            // 核价
            smpGoodsDto.setCost(null);
            smpGoodsDto.setLaborCosts(null);
            smpGoodsDto.setMaterialCost(null);
            //
            // 资料包 吊牌
            smpGoodsDto.setPackageType(null);
            smpGoodsDto.setPackageSize(null);
            // 无
            smpGoodsDto.setProductTypeId(null);
            smpGoodsDto.setProductType(null);
            smpGoodsDto.setUnit(null);
            smpGoodsDto.setPlanningRate(null);
            smpGoodsDto.setRegion(null);
            smpGoodsDto.setSalesGroup(null);
            smpGoodsDto.setSizeGroupId(null);
            smpGoodsDto.setSizeGroupName(null);
            smpGoodsDto.setStyleCode(null);

            smpGoodsDto.setTextureId(null);
            smpGoodsDto.setTextureName(null);
            smpGoodsDto.setPriceConfirm(null);

            smpGoodsDto.setPlanCost(null);
            smpGoodsDto.setActualRate(null);
            smpGoodsDto.setPlanActualRate(null);
            smpGoodsDto.setProcessCost(null);

            smpGoodsDto.setProductName(null);
            smpGoodsDto.setUniqueCode(null);
            smpGoodsDto.setSeries(null);
            smpGoodsDto.setSaleTime(null);
            smpGoodsDto.setSeriesId(null);
            smpGoodsDto.setSeriesName(null);
            smpGoodsDto.setBomPhase(null);
            smpGoodsDto.setAuProcess(null);
            smpGoodsDto.setProdSeg(null);
            smpGoodsDto.setComposition(null);

            smpGoodsDto.setIntegritySample(null);
            smpGoodsDto.setIntegrityProduct(null);

            HttpResp httpResp = restTemplateService.spmPost(SMP_URL + "/goods", smpGoodsDto);
            Boolean aBoolean = pushRecordsService.pushRecordSave(httpResp, smpGoodsDto, "smp", "商品主数据下发");
            if (aBoolean) {
                i++;
            }
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
            ApiResult apiResult = ccmService.listByIds(basicsdatumMaterial.getCategoryId());
            List<HashMap<String, String>> hashMapList = (List<HashMap<String, String>>) apiResult.getData();
            for (HashMap<String, String> hashMap : hashMapList) {
                if (basicsdatumMaterial.getCategoryId().equals(hashMap.get("id"))) {
                    smpMaterialDto.setThirdLevelCategory(hashMap.get("value"));
                }
            }
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

            //获取材料尺码集合
            QueryWrapper<BasicsdatumMaterialWidth> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("material_code", basicsdatumMaterial.getMaterialCode());
            queryWrapper1.eq("company_code", userUtils.getCompanyCode());
            List<SmpModuleSize> moduleSizeList = new ArrayList<>();
            for (BasicsdatumMaterialWidth basicsdatumMaterialWidth : basicsdatumMaterialWidthService.list(queryWrapper1)) {
                SmpModuleSize smpModuleSize = new SmpModuleSize();
                smpModuleSize.setActive("0".equals(basicsdatumMaterialWidth.getStatus()));
                smpModuleSize.setSizeCode(basicsdatumMaterialWidth.getWidthCode());
                smpModuleSize.setSizeUrl(null);
                smpModuleSize.setSizeName(basicsdatumMaterialWidth.getName());
                smpModuleSize.setCode(null);
                moduleSizeList.add(smpModuleSize);
            }


            if (moduleSizeList.size() == 0) {
                throw new OtherException("规格不能为空");
            }
            smpMaterialDto.setModuleSizeList(moduleSizeList);

            //获取报价集合

            BasicsdatumMaterialPriceQueryDto dto = new BasicsdatumMaterialPriceQueryDto();
            dto.setMaterialCode(basicsdatumMaterial.getMaterialCode());

            List<SmpQuot> quotList = new ArrayList<>();
            for (BasicsdatumMaterialPricePageVo basicsdatumMaterialPricePageVo : basicsdatumMaterialService.getBasicsdatumMaterialPriceList(dto).getList()) {
                SmpQuot smpQuot = new SmpQuot();
                smpQuot.setSupplierSize(basicsdatumMaterialPricePageVo.getWidth());
                smpQuot.setSizeUrl(null);
                smpQuot.setSupplierColorId(basicsdatumMaterialPricePageVo.getColor());
                smpQuot.setSupplierColorName(basicsdatumMaterialPricePageVo.getColorName());
                smpQuot.setOrderGoodsDay(basicsdatumMaterialPricePageVo.getOrderDay());
                smpQuot.setProductionDay(basicsdatumMaterialPricePageVo.getProductionDay());
                smpQuot.setMoqInitial(basicsdatumMaterialPricePageVo.getMinimumOrderQuantity());
                smpQuot.setFobFullPrice(basicsdatumMaterialPricePageVo.getQuotationPrice());
                smpQuot.setSupplierMaterial(basicsdatumMaterialPricePageVo.getSupplierMaterialCode());
                smpQuot.setSupplierCode(basicsdatumMaterialPricePageVo.getSupplierId());
                smpQuot.setSupplierName(basicsdatumMaterialPricePageVo.getSupplierName());
                smpQuot.setComment(null);
                smpQuot.setTradeTermKey(null);
                if (basicsdatumMaterialPricePageVo.getSupplierId().equals(basicsdatumMaterial.getSupplierId())) {
                    smpQuot.setDefaultQuote(true);
                } else {
                    smpQuot.setDefaultQuote(false);
                }

                smpQuot.setTradeTermName(null);
                smpQuot.setMaterialUom(basicsdatumMaterialPricePageVo.getWidth());
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
            SmpBomDto smpBomDto = packBom.toSmpBomDto();
            smpBomDto.setMainMaterial(true);
            //bom主表
            PackInfo packInfo = packInfoService.getById(packBom.getForeignId());
            //样衣-款式配色
            SampleStyleColor sampleStyleColor = sampleStyleColorService.getById(packInfo.getSampleStyleColorId());
            smpBomDto.setPColorCode(sampleStyleColor.getColorCode());
            smpBomDto.setPColorName(sampleStyleColor.getColorName());
            smpBomDto.setBulkNumber(packInfo.getStyleNo());
            smpBomDto.setBomCode(packInfo.getCode());

            List<BomMaterial> bomMaterials = new ArrayList<>();

            BomMaterial bomMaterial = packBom.toBomMaterial();
            bomMaterial.setBomId(packInfo.getCode());
            bomMaterials.add(bomMaterial);
            smpBomDto.setMainMaterial(false);
            smpBomDto.setBomMaterials(bomMaterials);

            List<SmpSizeQty> sizeQtyList = new ArrayList<>();
            for (PackBomSize packBomSize : packBomSizeService.list(new QueryWrapper<PackBomSize>().eq("foreign_id", packBom.getForeignId()))) {
                SmpSizeQty smpSizeQty = packBomSize.toSmpSizeQty();
                //根据尺码id查询尺码
                BasicsdatumSize basicsdatumSize = basicsdatumSizeService.getById(packBomSize.getSizeId());
                if (basicsdatumSize != null) {
                    smpSizeQty.setPSizeCode(basicsdatumSize.getInternalSize());
                    smpSizeQty.setSizeCode(basicsdatumSize.getCode());
                    smpSizeQty.setItemSize(basicsdatumSize.getInternalSize());
                    smpSizeQty.setMatSizeUrl(basicsdatumSize.getCode());
                    sizeQtyList.add(smpSizeQty);
                }

            }
            smpBomDto.setSizeQtyList(sizeQtyList);

            HttpResp httpResp = restTemplateService.spmPost(SMP_URL + "/bom", smpBomDto);
            Boolean aBoolean = pushRecordsService.pushRecordSave(httpResp, smpBomDto, "smp", "bom下发");
            packBom.setScmSendFlag("1");
            packBomService.updateById(packBom);
            if (aBoolean) {
                i++;
            }

            PackInfoStatus packInfoStatus = packInfoStatusService.getOne(new QueryWrapper<PackInfoStatus>().eq("foreign_id", packInfo.getId()));
            long count = packBomService.count(new QueryWrapper<PackBom>().eq("foreign_id", packInfo.getId()).eq("scm_send_flag", "1"));

            long count1 = packBomService.count(new QueryWrapper<PackBom>().eq("foreign_id", packInfo.getId()));
            if (count==0){
                packInfoStatus.setScmSendFlag("0");
            }else if(count1==count){
                packInfoStatus.setScmSendFlag("1");
            }else {
                packInfoStatus.setScmSendFlag("2");
            }
            packInfoStatusService.updateById(packInfoStatus);
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

        int i = 0;
        for (BasicsdatumColourLibrary basicsdatumColourLibrary : basicsdatumColourLibraries) {
            SmpColorDto smpColorDto = basicsdatumColourLibrary.toSmpColorDto();

            HttpResp httpResp = restTemplateService.spmPost(SMP_URL + "/color", smpColorDto);
            Boolean aBoolean = pushRecordsService.pushRecordSave(httpResp, smpColorDto, "smp", "颜色主数据下发");
            if (aBoolean) {
                i++;
                basicsdatumColourLibrary.setScmSendFlag("1");
                basicsdatumColourLibraryService.updateById(basicsdatumColourLibrary);
            }
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

            smpProcessSheetDto.setSyncId(id);
            smpProcessSheetDto.setId(id);
            HttpResp httpResp = restTemplateService.spmPost(SMP_URL + "/processSheet", smpProcessSheetDto);
            Boolean aBoolean = pushRecordsService.pushRecordSave(httpResp, smpProcessSheetDto, "smp", "工艺单下发");
            if (aBoolean) {
                i++;
            }

        }
        return i;
    }

    /**
     * 样衣下发
     */
    public Integer sample(String[] ids) {
        int i = 0;
        for (SampleDesign sampleDesign : sampleDesignService.listByIds(Arrays.asList(ids))) {
            SmpSampleDto smpSampleDto = sampleDesign.toSmpSampleDto();
            String designerId = sampleDesign.getDesignerId();
            String technicianId = sampleDesign.getTechnicianId();
            String patternDesignId = sampleDesign.getPatternDesignId();

            String merchDesignId = sampleDesign.getMerchDesignId();
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

            HttpResp httpResp = restTemplateService.spmPost(SMP_URL + "/sample", smpSampleDto);
            Boolean aBoolean = pushRecordsService.pushRecordSave(httpResp, smpSampleDto, "smp", "样衣下发");
            if (aBoolean) {
                i++;
            }
        }

        return i;
    }

    /**
     * 面料成分名称码表下发
     */
    public Integer fabricComposition(String[] ids) {
        int i = 0;
        for (BasicsdatumMaterial basicsdatumMaterial : basicsdatumMaterialService.listByIds(Arrays.asList(ids))) {
            FabricCompositionDto fabricCompositionDto = new FabricCompositionDto();
            fabricCompositionDto.setName(basicsdatumMaterial.getMaterialName());
            fabricCompositionDto.setMaterialCode(basicsdatumMaterial.getMaterialCode());
            fabricCompositionDto.setId(fabricCompositionDto.getId());
            String[] split = basicsdatumMaterial.getIngredient().split(", ");
            List<String> list = new ArrayList<>();
            try {
                for (String s : split) {
                    String[] split1 = s.split(" ");
                    list.add(split1[1]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            fabricCompositionDto.setIngredient(String.join(",", list));


            HttpResp httpResp = restTemplateService.spmPost(SCM_URL + "/materialElement", fabricCompositionDto);
            Boolean aBoolean = pushRecordsService.pushRecordSave(httpResp, fabricCompositionDto, "scm", "面料成分名称码表下发");
            if (aBoolean) {
                i++;
            }
        }
        return i;
    }

    /**
     * 修改商品尺码的时候验证
     */
    public Boolean checkStyleSize(PlmStyleSizeParam param) {
        HttpResp httpResp = restTemplateService.spmPost(SCM_URL + "/checkStyleSize", param);
        return pushRecordsService.pushRecordSave(httpResp, param, "scm", "修改尺码的时候验证");
    }

    /**
     * 修改商品颜色的时候验证
     */
    public Boolean checkColorSize(PdmStyleCheckParam param) {
        HttpResp httpResp = restTemplateService.spmPost(SCM_URL + "/checkColorSize", param);
        return pushRecordsService.pushRecordSave(httpResp, param, "scm", "修改商品颜色的时候验证");
    }
}

