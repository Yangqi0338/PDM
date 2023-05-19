package com.base.sbc.client.ccm.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.entity.BasicStructureTreeVo;
import com.base.sbc.config.constant.BaseConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述：ccm 远程调用
 * @address com.base.sbc.client.ccm.service.CcmFeignService
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-04-20 16:36
 * @version 1.0
 */
@Service
public class CcmFeignService {

    @Autowired
    private CcmService ccmService;

    public List<BasicStructureTreeVo> findStructureTreeByCategoryIds(String categoryIds){
        String str = ccmService.findStructureTreeByCategoryIds(categoryIds);
        if(StrUtil.isBlank(str)){
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(str);
        if(jsonObject.getBoolean(BaseConstant.SUCCESS)){
            List<BasicStructureTreeVo> data = jsonObject.getJSONArray("data").toJavaList(BasicStructureTreeVo.class);
            return data;
        }
        return null;
    }

    /**
     * 通过id 获取结构树名称
     * @param categoryIds
     * @return
     */
    public Map<String,String> findStructureTreeNameByCategoryIds(String categoryIds){
        String str = ccmService.findStructureTreeNameByCategoryIds(categoryIds);
        Map<String,String> result=new HashMap<>(16);
        if(StrUtil.isBlank(str)){
            return result;
        }
        JSONObject jsonObject = JSON.parseObject(str);
        if(jsonObject.getBoolean(BaseConstant.SUCCESS)){
             return (Map<String, String>) jsonObject.get("data");

        }
        return result;
    }
    /**
     * ccm 查询字典
     * @param types
     * @return
     */
    public Map<String,Map<String,String>> getDictInfoToMap(String types){
        Map<String,Map<String,String>> result=new LinkedHashMap<>(16);
        String dictInfo = ccmService.getDictInfo(types);
        System.out.println(dictInfo);
        JSONObject jsonObject = JSON.parseObject(dictInfo);
        if(jsonObject.getBoolean(BaseConstant.SUCCESS)){
            List<BasicBaseDict> data = jsonObject.getJSONArray("data").toJavaList(BasicBaseDict.class);
            if(CollUtil.isNotEmpty(data)){
                for (BasicBaseDict dict : data) {
                    Map<String, String> dictMap = result.get(dict.getType());
                    if(dictMap==null){
                        dictMap=new LinkedHashMap<>(16);
                        result.put(dict.getType(),dictMap);
                    }
                    dictMap.put(dict.getValue(),dict.getName());
                }
            }
        }
        return  result;
    }

}
