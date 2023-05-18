package com.base.sbc.client.amc.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.client.amc.TeamVo;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.constant.BaseConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述： 用户信息
 * @address com.base.sbc.client.amc.service.AmcFeignService
 * @author lixianglin
 * @email li_xianglin@126.com
 * @date 创建时间：2023-04-12 13:17
 * @version 1.0
 */
@Service
@Slf4j
public class AmcFeignService {

    @Resource
    private AmcService amcService;

    /**
     * 获取用户头像
     * @param ids
     * @return
     */
    public Map<String,String> getUserAvatar(String ids){
        Map<String,String> userAvatarMap=new HashMap<>(16);
        String userAvatarStr = amcService.getUserAvatar(ids);
        JSONObject jsonObject = JSON.parseObject(userAvatarStr);
        if(jsonObject.getBoolean(BaseConstant.SUCCESS)){
            JSONArray data = jsonObject.getJSONArray(BaseConstant.DATA);
            List<UserCompany> userCompanies = data.toJavaList(UserCompany.class);
            if(CollUtil.isNotEmpty(userCompanies)){
                for (UserCompany userCompany : userCompanies) {
                    userAvatarMap.put(userCompany.getId(),userCompany.getAvatar());
                }
            }
        }
        return userAvatarMap;
    }

    /**
     * 获取团队信息
     * @param seasonId 产品季节id
     * @return
     */
    public List<TeamVo> getTeamBySeasonId(String seasonId){
        try {
            String str = amcService.getTeamBySeasonId(seasonId);
            JSONObject jsonObject = JSON.parseObject(str);
            if(jsonObject.getBoolean("success")){
              return  jsonObject.getJSONArray("data").toJavaList(TeamVo.class);
            }
        } catch (Exception e) {
            log.error("获取产品季团队异常",e);
        }
        return null;
    }

}
