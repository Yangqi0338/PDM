package com.base.sbc.config.utils;

import java.security.Principal;

import com.base.sbc.config.aspect.GetCurUserInfoAspect;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.client.oauth.entity.GroupUser;
import com.base.sbc.client.oauth.service.OauthService;
import com.base.sbc.config.redis.RedisUtils;
/**
 * @author Fred
 * @data 创建时间:2020/2/3
 */
@Component
public class UserUtils {

	@Autowired
	private RedisUtils redisUtils;

	@Autowired
	private OauthService oauthService;

	public static final String USER_ID = "userId:";
	public static final String NAME = "name:";

	public static final String USER_INFO ="USER_INFO_";

	public String getUserIdBy(Principal user) {
		String userName = user.getName();
		String userId = (String)redisUtils.get(USER_ID+userName);
		if(StringUtils.isBlank(userId)) {
			String retomeResult = oauthService.getUserInfo();
			JSONObject jsonx = JSON.parseObject(retomeResult);
			String data =  jsonx.getJSONObject("data").toJSONString();
			if(jsonx.getBoolean(Constants.SUCCESS)) {
				GroupUser gu = (GroupUser)JsonUtils.jsonToBean(data, GroupUser.class);
				redisUtils.set(USER_ID+userName, gu.getId());
				userId = gu.getId();
			}
		}
		return userId;
	}
	public GroupUser getUserBy(Principal user) {
		String userName = user.getName();
		GroupUser users = (GroupUser) redisUtils.get(USER_ID+userName);
		if(users==null) {
			String retomeResult = oauthService.getUserInfo();
			JSONObject jsonx = JSON.parseObject(retomeResult);
			String data =  jsonx.getJSONObject("data").toJSONString();
			if(jsonx.getBoolean(Constants.SUCCESS)) {
				users = (GroupUser) JsonUtils.jsonToBean(data, GroupUser.class);
				redisUtils.set(USER_ID+userName, users);
			}
		}
		return users;
	}

	/**
	 * 获取当前登录用户信息，传入参数必须为当前用户id，否则写入redis后，因为id值不一致可能会导致数据错乱
	 * @param userId  当前用户id
	 */
	public GroupUser getUser(String userId){
		GroupUser user = (GroupUser)redisUtils.get(USER_INFO +userId);
		if (user==null){
			String retomeResult = oauthService.getUserInfo();
			JSONObject jsonx = JSON.parseObject(retomeResult);
			String data =  jsonx.getJSONObject("data").toJSONString();
			if(jsonx.getBoolean(Constants.SUCCESS)) {
				user = (GroupUser) JsonUtils.jsonToBean(data, GroupUser.class);
				redisUtils.set(USER_INFO +userId, user);
			}
		}
		return user;
	}

	public String getUserId(){
		return this.getUserCompany().getUserId();
	}

	public String getAliasUserName(){
		return this.getUserCompany().getAliasUserName();
	}

	public String getCompanyCode(){
		return this.getUserCompany().getCompanyCode();
	}

	public UserCompany getUserCompany(){
		return GetCurUserInfoAspect.companyUserInfo.get();
	}

	public String getDeptName(){
		return this.getUserCompany().getDeptName();
	}
}
