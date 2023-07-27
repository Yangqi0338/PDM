package com.base.sbc.client.amc.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.client.amc.TeamVo;
import com.base.sbc.client.amc.enums.DataPermissionsBusinessTypeEnum;
import com.base.sbc.client.amc.vo.FieldDataPermissionVO;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.annotation.UserAvatar;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.constant.BaseConstant;
import com.base.sbc.config.exception.OtherException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 类描述： 用户信息
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.client.amc.service.AmcFeignService
 * @email li_xianglin@126.com
 * @date 创建时间：2023-04-12 13:17
 */
@Service
@Slf4j
public class AmcFeignService {

    @Resource
    private AmcService amcService;

    public static ThreadLocal<List<String>> userPlanningSeasonId = new ThreadLocal<>();
    /**
     * 获取用户头像
     *
     * @param ids
     * @return
     */
    public Map<String, String> getUserAvatar(String ids) {
        Map<String, String> userAvatarMap = new HashMap<>(16);
        String userAvatarStr = amcService.getUserAvatar(ids);
        JSONObject jsonObject = JSON.parseObject(userAvatarStr);
        if (jsonObject.getBoolean(BaseConstant.SUCCESS)) {
            JSONArray data = jsonObject.getJSONArray(BaseConstant.DATA);
            List<UserCompany> userCompanies = data.toJavaList(UserCompany.class);
            if (CollUtil.isNotEmpty(userCompanies)) {
                for (UserCompany userCompany : userCompanies) {
                    userAvatarMap.put(userCompany.getUserId(), Opt.ofBlankAble(userCompany.getAvatar()).orElse(userCompany.getAliasUserAvatar()));
                }
            }
        }
        return userAvatarMap;
    }

    public UserCompany getUserInfo(String userId) {
        return getUserInfo(userId, null);
    }

    /**
     * @param userId
     * @param dpj    非空 查询部门岗位角色
     * @return
     */
    public UserCompany getUserInfo(String userId, String dpj) {
        String responseStr = amcService.getCompanyUserInfoByUserIds(userId, dpj);
        JSONObject jsonObject = JSON.parseObject(responseStr);
        if (jsonObject.getBoolean(BaseConstant.SUCCESS)) {
            JSONArray data = jsonObject.getJSONArray(BaseConstant.DATA);
            List<UserCompany> userCompanies = data.toJavaList(UserCompany.class);
            if (CollUtil.isNotEmpty(userCompanies)) {
                return CollUtil.getLast(userCompanies);
            }
        }
        return null;
    }

    /**
     * 获取团队信息
     *
     * @param seasonId 产品季节id
     * @return
     */
    public List<TeamVo> getTeamBySeasonId(String seasonId) {
        try {
            String str = amcService.getTeamBySeasonId(seasonId);
            JSONObject jsonObject = JSON.parseObject(str);
            if (jsonObject.getBoolean("success")) {
                return jsonObject.getJSONArray("data").toJavaList(TeamVo.class);
            }
        } catch (Exception e) {
            log.error("获取产品季团队异常", e);
        }
        return null;
    }

    /**
     * 添加头像
     *
     * @param arr
     * @param userIdKey
     * @param avatarKey
     */
    public void addUserAvatarToList(List arr, String userIdKey, String avatarKey) {
        try {
            if (CollUtil.isEmpty(arr)) {
                return;
            }
            Set<String> userIds = new HashSet<>(arr.size());
            for (Object o : arr) {
                Object property = BeanUtil.getProperty(o, userIdKey);
                if (ObjUtil.isNotNull(property)) {
                    userIds.add(property.toString());
                }
            }
            if (CollUtil.isEmpty(userIds)) {
                return;
            }
            Map<String, String> userAvatar = getUserAvatar(CollUtil.join(userIds, StrUtil.COMMA));
            for (Object o : arr) {
                Object val = MapUtil.getStr(userAvatar, BeanUtil.getProperty(o, userIdKey), null);
                BeanUtil.setProperty(o, avatarKey, val);
            }
        } catch (Exception e) {
            log.error("获取头像失败", e);
        }

    }

    public List<UserCompany> getUsersBySeasonId(String planningSeasonId, String dpj, String post) {
        List<UserCompany> userList = null;
        try {
            String result = amcService.getUsersBySeasonId(planningSeasonId, dpj, post);
            JSONObject jsonObject = JSON.parseObject(result);
            userList = jsonObject.getJSONArray("data").toJavaList(UserCompany.class);
        } catch (Exception e) {
            e.printStackTrace();
            userList = new ArrayList<>(2);
        }
        return userList;
    }

    public List<UserCompany> getTeamUserListByPost(String planningSeasonId, String post) {
        List<UserCompany> userList = getUsersBySeasonId(planningSeasonId, BaseGlobal.YES, post);
        return userList;
    }

    public List<String> getPlanningSeasonIdByUserId(String userId) {
        List<String> userList = null;
        try {
            List<String> cacheIds = userPlanningSeasonId.get();
            if(CollUtil.isNotEmpty(cacheIds)){
                return cacheIds;
            }
            String result = amcService.getPlanningSeasonIdByUserId(userId);
            JSONObject jsonObject = JSON.parseObject(result);
            userList = jsonObject.getJSONArray("data").toJavaList(String.class);
            userPlanningSeasonId.set(userList);
        } catch (Exception e) {
            e.printStackTrace();
            userList = new ArrayList<>(2);
        }
        return userList;

    }

    /**
     * 设置权限
     * 查询userId 所在的产品季id ，设置条件
     *
     * @param qw     QueryWrapper 条件构造器
     * @param column 产品季列名
     * @param userId 用户id
     */
    public void teamAuth(QueryWrapper qw, String column, String userId) {
//        List<String> planningSeasonIdByUserId = getPlanningSeasonIdByUserId(userId);
//        if (CollUtil.isEmpty(planningSeasonIdByUserId)) {
//            throw new OtherException("您不在团队里面");
//        }
//        qw.in(column, planningSeasonIdByUserId);
    }

    /**
     * 设置头像
     * 1先获取有UserAvatar注解的字段，拿到用户id属性，头像属性
     * 2 去amc 查
     * 3 设置头像
     *
     * @param obj
     */
    public void setUserAvatarToObj(Object obj) {
        try {
            Map<String, String> avatarUserIdKey = getUserAvatarMap(obj);
            setUserAvatarToObj(obj, avatarUserIdKey);
        } catch (Exception e) {
            log.error("获取头像失败", e);
        }
    }


    /**
     * 设置头像
     *
     * @param obj
     * @param avatarUserIdKey key 用户id 属性 ,val 头像属性
     */
    public void setUserAvatarToObj(Object obj, Map<String, String> avatarUserIdKey) {
        try {
            if (MapUtil.isEmpty(avatarUserIdKey)) {
                return;
            }
            List<String> userIds = new ArrayList<>(16);
            //获取用户id
            for (String s : avatarUserIdKey.values()) {
                String userId = BeanUtil.getProperty(obj, s);
                if (StrUtil.isNotBlank(userId)) {
                    userIds.add(userId);
                }
            }
            if (CollUtil.isEmpty(userIds)) {
                return;
            }
            // 查询头像
            Map<String, String> userAvatar = getUserAvatar(CollUtil.join(userIds, StrUtil.COMMA));
            // 设置头像值
            for (Map.Entry<String, String> kv : avatarUserIdKey.entrySet()) {
                BeanUtil.setProperty(obj, kv.getKey(), userAvatar.get(BeanUtil.getProperty(obj, kv.getValue())));
            }
        } catch (Exception e) {
            log.error("获取头像失败", e);
        }
    }

    public void setUserAvatarToList(List list, Map<String, String> avatarUserIdKey) {
        // 获取id
        if (MapUtil.isEmpty(avatarUserIdKey)) {
            return;
        }
        List<String> userIds = new ArrayList<>(16);
        //获取用户id
        for (Object obj : list) {
            for (String s : avatarUserIdKey.values()) {
                String userId = BeanUtil.getProperty(obj, s);
                if (StrUtil.isNotBlank(userId)) {
                    userIds.add(userId);
                }
            }

        }
        if (CollUtil.isEmpty(userIds)) {
            return;
        }
        // 查询头像
        Map<String, String> userAvatar = getUserAvatar(CollUtil.join(userIds, StrUtil.COMMA));
        for (Object obj : list) {
            if (obj == null) {
                continue;
            }
            for (Map.Entry<String, String> kv : avatarUserIdKey.entrySet()) {
                BeanUtil.setProperty(obj, kv.getKey(), userAvatar.get(BeanUtil.getProperty(obj, kv.getValue())));
            }
        }
    }

    public void setUserAvatarToList(List list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        Object obj = list.get(0);
        Map<String, String> avatarUserIdKey = getUserAvatarMap(obj);
        setUserAvatarToList(list, avatarUserIdKey);
    }


    private static Map<String, String> getUserAvatarMap(Object obj) {
        Map<String, String> avatarUserIdKey = new HashMap<>(16);
        Field[] fields = obj.getClass().getDeclaredFields();
        // 遍历字段
        for (Field field : fields) {
            // 检查字段上是否存在UserAvatar的注解
            if (field.isAnnotationPresent(UserAvatar.class)) {
                // 获取字段上的注解对象
                UserAvatar annotation = field.getAnnotation(UserAvatar.class);
                avatarUserIdKey.put(field.getName(), annotation.value());
            }
        }
        return avatarUserIdKey;
    }

    /**
     * 获取数据权限
     *
     * @param businessType
     * @return
     * @see DataPermissionsBusinessTypeEnum
     */
    public List<FieldDataPermissionVO> getDataPermissions(String businessType) {
        if (StringUtils.isEmpty(businessType)) {
            throw new OtherException("入参不可为空");
        }
        ApiResult apiResult = amcService.getDataPermissions(businessType);
        if (Objects.isNull(apiResult)) {
            throw new OtherException("获取用户数据权限异常");
        }
        return JSONArray.parseArray(JSON.toJSONString(apiResult.getData()), FieldDataPermissionVO.class);
    }

    /**
     * 获取数据权限
     *
     * @param businessType
     * @return
     * @see DataPermissionsBusinessTypeEnum
     */
    public void getDataPermissionsForQw(String businessType, AbstractWrapper qw) {
        List<FieldDataPermissionVO> dataPermissions = getDataPermissions(businessType);
        if (CollUtil.isNotEmpty(dataPermissions)) {
            for (FieldDataPermissionVO dataPermission : dataPermissions) {
                if (CollUtil.isNotEmpty(dataPermission.getFieldValueIds())) {
                    qw.in(dataPermission.getFieldName(), dataPermission.getFieldValueIds());
                }

            }
        }

    }
}
