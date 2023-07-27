package com.base.sbc.open.service;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.ccm.service.CcmService;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.minio.MinioUtils;
import com.base.sbc.config.utils.OpenSmpFtpUtils;
import com.base.sbc.module.basicsdatum.dto.BasicsdatumMaterialWidthGroupSaveDto;
import com.base.sbc.module.basicsdatum.entity.*;
import com.base.sbc.module.basicsdatum.service.*;
import com.base.sbc.module.common.service.UploadFileService;
import com.base.sbc.module.common.vo.AttachmentVo;
import com.base.sbc.open.dto.SmpOpenMaterialDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/7/23 14:30:40
 * @mail 247967116@qq.com
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OpenSmpService {
    private final BasicsdatumMaterialService basicsdatumMaterialService;

    private final BasicsdatumMaterialColorService basicsdatumMaterialColorService;

    private final BasicsdatumMaterialWidthService basicsdatumMaterialWidthService;

    private final BasicsdatumMaterialPriceService basicsdatumMaterialPriceService;

    private final SpecificationGroupService specificationGroupService;

    private final MinioUtils minioUtils;

    private final CcmService ccmService;


    @Transactional(rollbackFor = Exception.class)
    public void smpMaterial(JSONObject smpOpenMaterialDtoJson) {
        JSONObject jsonObject1 = smpOpenMaterialDtoJson.getJSONObject("params");
        JSONArray jSON = jsonObject1.getJSONArray("jSON");
        JSONObject jsonObject = jSON.getJSONObject(0);
        SmpOpenMaterialDto smpOpenMaterialDto = jsonObject.toJavaObject(SmpOpenMaterialDto.class);
        //初步逻辑：关联编码的，先去查询编码是否存在，如果不存在则返回错误，字段不存在。
        JSONObject images = jsonObject.getJSONObject("images");
        if (images != null) {
            JSONArray imagesChildren = images.getJSONArray("imagesChildren");
            if (imagesChildren != null) {
                List<String> list = new ArrayList<>();
                for (Object imagesChild : imagesChildren) {
                    JSONObject imagesChild1 = (JSONObject) JSONObject.toJSON(imagesChild);
                    String string = imagesChild1.getString("filename");

                    try {
                        //转换图片地址
                        //String objectName = System.currentTimeMillis() + "." + FileUtil.extName(string);
                        //String contentType = "image/jpeg";
                        //InputStream inputStream = OpenSmpFtpUtils.download(string);
                        //String s = minioUtils.uploadFile(inputStream, objectName, contentType);

                        String[] split = string.split("\\.");

                        list.add("http://60.191.75.218:23480/CMSDocs/Material/"+smpOpenMaterialDto.getCode()+"."+split[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                smpOpenMaterialDto.setImages(list);
            }
        }
        //if (true){
        //    return;
        //}

        JSONObject quotItems = jsonObject.getJSONObject("quotItems");
        if (quotItems != null) {
            JSONArray quotItemsChildren = quotItems.getJSONArray("quotItemsChildren");
            if (quotItemsChildren != null) {
                List<SmpOpenMaterialDto.QuotItem> list = new ArrayList<>();
                for (Object quotItemsChild : quotItemsChildren) {
                    JSONObject json = (JSONObject) JSON.toJSON(quotItemsChild);
                    SmpOpenMaterialDto.QuotItem quotItem = JSON.toJavaObject(json, SmpOpenMaterialDto.QuotItem.class);
                    quotItem.setC8_SupplierItemRev_MLeadTime(json.getBigDecimal("c8SupplierItemRevMLeadTime"));
                    quotItem.setLeadTime(json.getBigDecimal("leadTime"));

                    list.add(quotItem);
                }
                smpOpenMaterialDto.setQuotItems(list);
            }
        }
        JSONObject mODELITEMS = jsonObject.getJSONObject("mODELITEMS");
        if (mODELITEMS != null) {
            JSONArray mODELITEMSNANAnnotation = mODELITEMS.getJSONArray("mODELITEMSChildren");
            if (mODELITEMSNANAnnotation != null) {
                List<SmpOpenMaterialDto.ModelItem> list = new ArrayList<>();
                for (Object object : mODELITEMSNANAnnotation) {
                    JSONObject json = (JSONObject) JSON.toJSON(object);
                    SmpOpenMaterialDto.ModelItem modelItem = JSON.toJavaObject(json, SmpOpenMaterialDto.ModelItem.class);
                    list.add(modelItem);
                }
                smpOpenMaterialDto.setMODELITEMS(list);
            }
        }


        JSONObject cOLORITEMS = jsonObject.getJSONObject("cOLORITEMS");
        if (cOLORITEMS != null) {
            JSONArray cOLORITEMSNANAnnotation = cOLORITEMS.getJSONArray("cOLORITEMSChildren");
            if (cOLORITEMSNANAnnotation != null) {
                List<SmpOpenMaterialDto.ColorItem> list = new ArrayList<>();
                for (Object object : cOLORITEMSNANAnnotation) {
                    JSONObject json = (JSONObject) JSON.toJSON(object);
                    SmpOpenMaterialDto.ColorItem colorItem = JSON.toJavaObject(json, SmpOpenMaterialDto.ColorItem.class);
                    list.add(colorItem);
                }
                smpOpenMaterialDto.setCOLORITEMS(list);
            }

        }

        BasicsdatumMaterial basicsdatumMaterial = smpOpenMaterialDto.toBasicsdatumMaterial();
        basicsdatumMaterial.setCompanyCode(BaseConstant.DEF_COMPANY_CODE);
        basicsdatumMaterial.setUpdateName("外部系统推送");

        String c8ProcMode = ccmService.getOpenDictInfo(BaseConstant.DEF_COMPANY_CODE, "C8_ProcMode");
        JSONArray data = JSONObject.parseObject(c8ProcMode).getJSONArray("data");
        for (int i = 0; i < data.size(); i++) {
            JSONObject obj = data.getJSONObject(i);
            if (obj.getString("value").equals(basicsdatumMaterial.getProcMode())) {
                basicsdatumMaterial.setProcModeName(obj.getString("name"));
            }
        }


        String c8PickingMethod = ccmService.getOpenDictInfo(BaseConstant.DEF_COMPANY_CODE, "C8_PickingMethod");
        JSONArray data2 = JSONObject.parseObject(c8PickingMethod).getJSONArray("data");
        for (int i = 0; i < data2.size(); i++) {
            JSONObject obj = data2.getJSONObject(i);
            if (obj.getString("value").equals(basicsdatumMaterial.getPickingMethod())) {
                basicsdatumMaterial.setPickingMethodName(obj.getString("name"));
            }
        }


        try {
            String categorySByNameAndLevel = ccmService.getOpenCategorySByNameAndLevel("材料", smpOpenMaterialDto.getC8_Material_2ndCategory(), "1");
            String str2 = JSON.parseObject(categorySByNameAndLevel).getJSONArray("data").getJSONObject(0).getString("name");
            basicsdatumMaterial.setCategoryName(basicsdatumMaterial.getCategoryName() + "-" + str2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String categorySByNameAndLevel = ccmService.getOpenCategorySByNameAndLevel("材料", smpOpenMaterialDto.getC8_Material_3rdCategory(), "2");
            String str3 = JSON.parseObject(categorySByNameAndLevel).getJSONArray("data").getJSONObject(0).getString("name");
            basicsdatumMaterial.setCategoryName(basicsdatumMaterial.getCategoryName() + "-" + str3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //    if (true){
        //        return ;
        //}


        if (!smpOpenMaterialDto.getCOLORITEMS().isEmpty()) {
            List<BasicsdatumMaterialColor> basicsdatumMaterialColors = new ArrayList<>();
            //转成颜色集合
            smpOpenMaterialDto.getCOLORITEMS().forEach(colorItem -> {
                BasicsdatumMaterialColor basicsdatumMaterialColor = new BasicsdatumMaterialColor();
                basicsdatumMaterialColor.setColorCode(colorItem.getColorCode());
                basicsdatumMaterialColor.setColorName(colorItem.getColorName());
                basicsdatumMaterialColor.setStatus(colorItem.isActive() ? "0" : "1");
                basicsdatumMaterialColor.setSupplierColorCode(colorItem.getMatColor4Supplier());
                basicsdatumMaterialColor.setMaterialCode(basicsdatumMaterial.getMaterialCode());
                basicsdatumMaterialColor.setCompanyCode(BaseConstant.DEF_COMPANY_CODE);
                basicsdatumMaterialColor.setUpdateName("外部系统推送");
                basicsdatumMaterialColors.add(basicsdatumMaterialColor);
            });
            basicsdatumMaterialColorService.addAndUpdateAndDelList(basicsdatumMaterialColors, new QueryWrapper<BasicsdatumMaterialColor>().eq("material_code", basicsdatumMaterial.getMaterialCode()));

        }

        if (!smpOpenMaterialDto.getMODELITEMS().isEmpty()) {
            List<BasicsdatumMaterialWidth> basicsdatumMaterialWidths = new ArrayList<>();
            List<String> codes =new ArrayList<>();
            smpOpenMaterialDto.getMODELITEMS().forEach(modelItem -> {
                BasicsdatumMaterialWidth basicsdatumMaterialWidth = new BasicsdatumMaterialWidth();
                basicsdatumMaterialWidth.setStatus(modelItem.isActive() ? "0" : "1");
                basicsdatumMaterialWidth.setWidthCode(modelItem.getCODE());
                basicsdatumMaterialWidth.setName(modelItem.getSIZECODE());
                basicsdatumMaterialWidth.setMaterialCode(basicsdatumMaterial.getMaterialCode());
                basicsdatumMaterialWidth.setCompanyCode(BaseConstant.DEF_COMPANY_CODE);
                basicsdatumMaterialWidth.setUpdateName("外部系统推送");
                basicsdatumMaterialWidths.add(basicsdatumMaterialWidth);
                codes.add(basicsdatumMaterialWidth.getWidthCode());
            });

            List<SpecificationGroup> specifications = specificationGroupService.list(new QueryWrapper<SpecificationGroup>().eq("specification_ids", String.join(",", codes)));
            if (specifications!=null && specifications.size() > 0){
                basicsdatumMaterial.setWidthGroup(specifications.get(0).getCode());
                basicsdatumMaterial.setWidthGroupName(specifications.get(0).getName());
            }
            basicsdatumMaterialWidthService.addAndUpdateAndDelList(basicsdatumMaterialWidths, new QueryWrapper<BasicsdatumMaterialWidth>().eq("material_code", basicsdatumMaterial.getMaterialCode()));

        }

        if (!smpOpenMaterialDto.getQuotItems().isEmpty()) {
            List<BasicsdatumMaterialPrice> basicsdatumMaterialPrices = new ArrayList<>();
            smpOpenMaterialDto.getQuotItems().forEach(quotItem -> {
                BasicsdatumMaterialPrice basicsdatumMaterialPrice = new BasicsdatumMaterialPrice();
                basicsdatumMaterialPrice.setWidth(quotItem.getSUPPLIERSIZE());
                basicsdatumMaterialPrice.setMaterialCode(basicsdatumMaterial.getMaterialCode());
                basicsdatumMaterialPrice.setSupplierId(quotItem.getSupplierCode());
                basicsdatumMaterialPrice.setSupplierName(quotItem.getSupplierName());
                basicsdatumMaterialPrice.setColor(quotItem.getSUPPLIERCOLORID());
                basicsdatumMaterialPrice.setQuotationPrice(quotItem.getFOBFullPrice());
                basicsdatumMaterialPrice.setOrderDay(quotItem.getLeadTime());
                basicsdatumMaterialPrice.setProductionDay(quotItem.getC8_SupplierItemRev_MLeadTime());
                basicsdatumMaterialPrice.setMinimumOrderQuantity(quotItem.getMOQInitial());
                basicsdatumMaterialPrice.setColorName(quotItem.getSUPPLIERCOLORNAME());
                basicsdatumMaterialPrice.setWidthName(quotItem.getSUPPLIERSIZE());
                basicsdatumMaterialPrice.setSupplierMaterialCode(quotItem.getSupplierMaterial());
                basicsdatumMaterialPrice.setCompanyCode(BaseConstant.DEF_COMPANY_CODE);
                basicsdatumMaterialPrice.setSelectFlag(quotItem.getDefaultQuote());
                basicsdatumMaterialPrice.setUpdateName("外部系统推送");
                basicsdatumMaterialPrices.add(basicsdatumMaterialPrice);

            });
            basicsdatumMaterialPriceService.addAndUpdateAndDelList(basicsdatumMaterialPrices, new QueryWrapper<BasicsdatumMaterialPrice>().eq("material_code", basicsdatumMaterial.getMaterialCode()));

        }
        basicsdatumMaterialService.saveOrUpdate(basicsdatumMaterial, new QueryWrapper<BasicsdatumMaterial>().eq("material_code", basicsdatumMaterial.getMaterialCode()));
    }


}
