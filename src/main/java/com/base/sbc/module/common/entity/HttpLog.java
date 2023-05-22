package com.base.sbc.module.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.base.sbc.config.SqlDataInfo;
import com.base.sbc.config.common.base.BaseDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/5/16 18:49:25
 * @mail 247967116@qq.com
 */
@Data
@TableName("t_http_log")
public class HttpLog extends BaseDataEntity<String> {

    //请求数据
    /** 请求开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**请求类型 (1: 发出的请求, 2: 收到的请求)*/
    private Integer type;

    /** IP地址 */
    private String ip;


    /** 物理地址 */
    private String address;

    /** 接口地址 */
    private String url;

    /** 方法类型 */
    private String method;

    /*** 请求头*/
    private String reqHeaders;

    /*** 请求参数*/
    private String reqQuery;

    /** 请求body */
    private String reqBody;

    /** 用户编码 */
    private String userCode;

    /** 团队编码 */
    private String teamCode;

    /** 线程Id */
    private String threadId;

    /** 文档名称 */
    private String reqName;

    /** 权限编码 */
    private String authCode;

    /** 菜单权限名称 */
    private String authName;

    /** sql 记录 */
    private String sqlLog;

    //响应数据
    /** 持续时间ms */
    private Long intervalNum;

    /** 响应数据 */
    private String respBody;

    /** 是否异常(0正常1异常) */
    private Integer exceptionFlag;

    /** 异常信息 */
    private String throwableException;

    ///** 请求的cookie */
    //private Map<String, String> cookieMap;

    /** 请求sql */
    @TableField(exist = false)
    private List<SqlDataInfo> sqlDataInfoList;

    /** 业务具体日志 */
    //private Map<String, Object> businessDataMap;


    /***响应状态码*/
    private Integer statusCode;

    /***响应头*/
    private String respHeaders;

    /***备注*/
    private String remarks;
}
