package com.base.sbc.open.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.ccm.service.CcmService;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.module.basicsdatum.entity.*;
import com.base.sbc.module.basicsdatum.service.*;
import com.base.sbc.open.dto.SmpOpenMaterialDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.base.sbc.open.dto.SmpOpenMaterialDto.QuotItem;

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

    private final BasicsdatumMaterialPriceDetailService basicsdatumMaterialPriceDetailService;

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

                        list.add("http://60.191.75.218:23480/CMSDocs/Material/" + smpOpenMaterialDto.getCode() + "." + split[1]);
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
        List<QuotItem> quotItemsList = new ArrayList<>();
        if (quotItems != null) {
            JSONArray quotItemsChildren = quotItems.getJSONArray("quotItemsChildren");
            if (quotItemsChildren != null) {

                for (Object quotItemsChild : quotItemsChildren) {
                    JSONObject json = (JSONObject) JSON.toJSON(quotItemsChild);
                    QuotItem quotItem = JSON.toJavaObject(json, QuotItem.class);
                    quotItem.setC8_SupplierItemRev_MLeadTime(json.getBigDecimal("c8SupplierItemRevMLeadTime"));
                    quotItem.setLeadTime(json.getBigDecimal("leadTime"));
                    quotItemsList.add(quotItem);
                }


                smpOpenMaterialDto.setQuotItems(quotItemsList);
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
            List<String> codes = new ArrayList<>();
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
            if (specifications != null && specifications.size() > 0) {
                basicsdatumMaterial.setWidthGroup(specifications.get(0).getCode());
                basicsdatumMaterial.setWidthGroupName(specifications.get(0).getName());
            }
            basicsdatumMaterialWidthService.addAndUpdateAndDelList(basicsdatumMaterialWidths, new QueryWrapper<BasicsdatumMaterialWidth>().eq("material_code", basicsdatumMaterial.getMaterialCode()));

        }

        if (!smpOpenMaterialDto.getQuotItems().isEmpty()) {
            List<BasicsdatumMaterialPrice> basicsdatumMaterialPrices = new ArrayList<>();
            AtomicInteger index = new AtomicInteger();
            smpOpenMaterialDto.getQuotItems().forEach(quotItem -> {
                BasicsdatumMaterialPrice basicsdatumMaterialPrice = new BasicsdatumMaterialPrice();

                basicsdatumMaterialPrice.setWidthName(quotItem.getSUPPLIERSIZE());
                if (!smpOpenMaterialDto.getMODELITEMS().isEmpty()) {
                    for (SmpOpenMaterialDto.ModelItem modelitem : smpOpenMaterialDto.getMODELITEMS()) {
                        if (basicsdatumMaterialPrice.getWidthName().equals(modelitem.getSIZECODE())){
                            basicsdatumMaterialPrice.setWidth(modelitem.getCODE());
                        }
                    }
                }


                basicsdatumMaterialPrice.setMaterialCode(basicsdatumMaterial.getMaterialCode());
                basicsdatumMaterialPrice.setSupplierId(quotItem.getSupplierCode());
                basicsdatumMaterialPrice.setSupplierName(quotItem.getSupplierName());
                basicsdatumMaterialPrice.setColor(quotItem.getSUPPLIERCOLORID());
                basicsdatumMaterialPrice.setQuotationPrice(quotItem.getFOBFullPrice());
                basicsdatumMaterialPrice.setOrderDay(quotItem.getLeadTime());
                basicsdatumMaterialPrice.setProductionDay(quotItem.getC8_SupplierItemRev_MLeadTime());
                basicsdatumMaterialPrice.setMinimumOrderQuantity(quotItem.getMOQInitial());
                basicsdatumMaterialPrice.setColorName(quotItem.getSUPPLIERCOLORNAME());

                basicsdatumMaterialPrice.setSupplierMaterialCode(quotItem.getSupplierMaterial());
                basicsdatumMaterialPrice.setCompanyCode(BaseConstant.DEF_COMPANY_CODE);
                basicsdatumMaterialPrice.setSelectFlag(quotItem.getDefaultQuote());
                basicsdatumMaterialPrice.setUpdateName("外部系统推送");
                basicsdatumMaterialPrice.setIndex(String.valueOf(index.get()));
                basicsdatumMaterialPrices.add(basicsdatumMaterialPrice);
                index.getAndIncrement();
            });
            List<BasicsdatumMaterialPrice> list = this.merge(basicsdatumMaterialPrices);
            basicsdatumMaterialPriceService.addAndUpdateAndDelList(list, new QueryWrapper<BasicsdatumMaterialPrice>().eq("material_code", basicsdatumMaterial.getMaterialCode()));
            List<BasicsdatumMaterialPriceDetail> basicsdatumMaterialPriceDetails = new ArrayList<>();
            for (BasicsdatumMaterialPrice basicsdatumMaterialPrice : list) {

                for (BasicsdatumMaterialPrice materialPrice : basicsdatumMaterialPrices) {
                    if (basicsdatumMaterialPrice.getIndexList().contains(materialPrice.getIndex())) {
                        BasicsdatumMaterialPriceDetail basicsdatumMaterialPriceDetail = new BasicsdatumMaterialPriceDetail();
                        BeanUtil.copyProperties(materialPrice, basicsdatumMaterialPriceDetail);
                        basicsdatumMaterialPriceDetail.setPriceId(basicsdatumMaterialPrice.getId());
                        basicsdatumMaterialPriceDetails.add(basicsdatumMaterialPriceDetail);
                    }
                }
                //basicsdatumMaterialPriceDetailService.addAndUpdateAndDelList(basicsdatumMaterialPriceDetails, new QueryWrapper<BasicsdatumMaterialPriceDetail>().eq("price_id", basicsdatumMaterialPrice.getId()));
            }
            basicsdatumMaterialPriceDetailService.remove(new QueryWrapper<BasicsdatumMaterialPriceDetail>().eq("material_code", basicsdatumMaterial.getMaterialCode()));
            basicsdatumMaterialPriceDetailService.saveBatch(basicsdatumMaterialPriceDetails);

        }
        basicsdatumMaterialService.saveOrUpdate(basicsdatumMaterial, new QueryWrapper<BasicsdatumMaterial>().eq("material_code", basicsdatumMaterial.getMaterialCode()));
    }

    //供应商合并
    private List<BasicsdatumMaterialPrice> merge(List<BasicsdatumMaterialPrice> list) {
        ////排除第一轮
        Map<String, BasicsdatumMaterialPrice> map = new HashMap<>();
        for (BasicsdatumMaterialPrice item : list) {
            String key = item.getSupplierName() + item.getQuotationPrice() + item.getColorName();
            if (map.containsKey(key)) {
                BasicsdatumMaterialPrice existingItem = map.get(key);

                //规格
                existingItem.setWidthName(existingItem.getWidthName() + "," + item.getWidthName());
                HashSet<String> hashSet = new HashSet<>(Arrays.asList(existingItem.getWidthName().split(",")));
                existingItem.setWidthName(String.join(",", hashSet));

                //规格
                existingItem.setWidth(existingItem.getWidth() + "," + item.getWidth());
                HashSet<String> hashSet1 = new HashSet<>(Arrays.asList(existingItem.getWidth().split(",")));
                existingItem.setWidth(String.join(",", hashSet1));

                //颜色id
                existingItem.setColor(existingItem.getColor() + "," + item.getColor());
                HashSet<String> hashSet2 = new HashSet<>(Arrays.asList(existingItem.getColor().split(",")));
                existingItem.setColor(String.join(",", hashSet2));

                //颜色名称
                existingItem.setColorName(existingItem.getColorName() + "," + item.getColorName());
                HashSet<String> hashSet3 = new HashSet<>(Arrays.asList(existingItem.getColorName().split(",")));
                existingItem.setColorName(String.join(",", hashSet3));


                //索引
                if (existingItem.getIndexList() == null) {
                    Set<String> set = new HashSet<>();
                    set.add(item.getIndex());
                    set.add(existingItem.getIndex());
                    existingItem.setIndexList(set);
                } else {
                    existingItem.getIndexList().add(item.getIndex());
                    existingItem.getIndexList().add(existingItem.getIndex());
                }

                map.put(key, existingItem);
            } else {
                if (item.getIndexList() == null) {
                    Set<String> set = new HashSet<>();
                    set.add(item.getIndex());
                    set.add(item.getIndex());
                    item.setIndexList(set);
                } else {
                    item.getIndexList().add(item.getIndex());
                    item.getIndexList().add(item.getIndex());
                }

                map.put(key, item);
            }
        }

        List<BasicsdatumMaterialPrice> mergedList = new ArrayList<>();
        for (Map.Entry<String, BasicsdatumMaterialPrice> entry : map.entrySet()) {
            mergedList.add(entry.getValue());
        }
        //Set<String> set1=new HashSet<>();
        //排除第二轮
        Map<String, BasicsdatumMaterialPrice> map1 = new HashMap<>();
        for (BasicsdatumMaterialPrice item : mergedList) {
            String key = item.getSupplierName() + item.getWidthName();
            if (map1.containsKey(key)) {
                BasicsdatumMaterialPrice existingItem = map1.get(key);

                //颜色id
                existingItem.setColor(existingItem.getColor() + "," + item.getColor());
                HashSet<String> hashSet2 = new HashSet<>(Arrays.asList(existingItem.getColor().split(",")));
                existingItem.setColor(String.join(",", hashSet2));

                //颜色名称
                existingItem.setColorName(existingItem.getColorName() + "," + item.getColorName());
                HashSet<String> hashSet3 = new HashSet<>(Arrays.asList(existingItem.getColorName().split(",")));
                existingItem.setColorName(String.join(",", hashSet3));


                //索引
                if (existingItem.getIndexList() == null) {
                    Set<String> set = new HashSet<>();
                    set.add(item.getIndex());
                    set.add(existingItem.getIndex());
                    set.addAll(existingItem.getIndexList());
                    existingItem.setIndexList(set);
                } else {
                    existingItem.getIndexList().add(item.getIndex());
                    existingItem.getIndexList().addAll(existingItem.getIndexList());
                    existingItem.getIndexList().addAll(item.getIndexList());
                    existingItem.getIndexList().add(existingItem.getIndex());
                }

                map1.put(key, existingItem);
            } else {
                if (item.getIndexList() == null) {
                    Set<String> set = new HashSet<>();
                    set.add(item.getIndex());
                    set.add(item.getIndex());
                    set.addAll(item.getIndexList());
                    item.setIndexList(set);
                } else {
                    item.getIndexList().add(item.getIndex());
                    item.getIndexList().addAll(item.getIndexList());
                    item.getIndexList().add(item.getIndex());
                }
                map1.put(key, item);
            }
        }
        List<BasicsdatumMaterialPrice> mergedList1 = new ArrayList<>();
        for (Map.Entry<String, BasicsdatumMaterialPrice> entry : map1.entrySet()) {
            BasicsdatumMaterialPrice value = entry.getValue();
            mergedList1.add(value);
        }

        return mergedList1;
    }

}
