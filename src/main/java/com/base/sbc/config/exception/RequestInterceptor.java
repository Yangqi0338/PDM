package com.base.sbc.config.exception;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.sbc.config.aspect.GetCurUserInfoAspect;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.utils.UserCompanyUtils;
import com.base.sbc.module.common.service.HttpLogService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.Date;
import java.util.Enumeration;


/**
 * 请求日志打印
 * @author Fred
 * @data 创建时间:2020/2/3
 */
@RequiredArgsConstructor
@Component
public class RequestInterceptor implements HandlerInterceptor{


	Logger log = LoggerFactory.getLogger(getClass());

	private final HttpLogService httpLogService;
	//ContextHolder ctx = ContextHolder.ctx();

	private final UserCompanyUtils userCompanyUtils;

	ThreadLocal<UserCompany> companyUserInfo = GetCurUserInfoAspect.companyUserInfo;
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception arg3) throws Exception {

		try {

		}catch (Exception e){
			e.printStackTrace();

			//获取所有请求头
			JSONObject reqHeaders =new JSONObject();
			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()){
				String headerName = headerNames.nextElement();
				String header = request.getHeader(headerName);
				reqHeaders.put(headerName,header);
			}

			//获取请求体
			StringBuilder requestBody = new StringBuilder();
			BufferedReader reader = request.getReader();
			String line;
			while ((line = reader.readLine()) != null) {
				requestBody.append(line);
			}

			//获取所有请求参数
			String parameter = JSON.toJSONString(request.getParameterMap());
			UserCompany userCompany = companyUserInfo.get();
			userCompany.setReqBody(requestBody.toString());
			userCompany.setMethod(request.getMethod());
			userCompany.setUrl(request.getRequestURI());
			userCompany.setType(2);
			userCompany.setReqHeaders(reqHeaders.toJSONString());
			userCompany.setReqQuery(parameter);

			// 处理响应内容
			JSONObject respHeaders =new JSONObject();
			for (String headerName : response.getHeaderNames()) {
				String header = response.getHeader(headerName);
				respHeaders.put(headerName,header);
			}

			userCompany.setRespHeaders(respHeaders.toJSONString());
			userCompany.setStatusCode(response.getStatus());
			userCompany.setIntervalNum(System.currentTimeMillis()-userCompany.getCreateDate().getTime());

			httpLogService.save(userCompany);

		}finally {
			GetCurUserInfoAspect.companyUserInfo.remove();
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView arg3)
			throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		//当前登录者账号
		String username=authentication.getPrincipal().toString();
		UserCompany companyUser = userCompanyUtils.getCompanyUser();
		if(companyUser==null){
			companyUser=new UserCompany();
		}
		//设置账号信息
		companyUser.setUsername(username);

		Date date =new Date();
		companyUser.setStartTime(date);
		companyUser.setCreateDate(date);

		companyUserInfo.set(companyUser);


	    request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		Enumeration<String> enu=request.getParameterNames();
		while(enu.hasMoreElements()){
			String paraName= enu.nextElement();
			log.info("请求参数:"+paraName+": "+request.getParameter(paraName));
		}
		log.info("请求地址:"+request.getRequestURI());
		return true;
	}

}
