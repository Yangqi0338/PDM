package com.base.sbc.config.resttemplate;

import com.alibaba.fastjson.JSONObject;
import com.base.sbc.config.JsonStringUtils;
import com.base.sbc.module.smp.dto.HttpResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author 卞康
 * @date 2023/5/8 15:31:18
 * @mail 247967116@qq.com
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RestTemplateService {

    private final RestTemplate restTemplate;

    /**
     * smp系统对接post请求
     *
     * @param url 请求地址
     * @param jsonStr   请求对象
     * @return 返回的结果
     */
    public HttpResp spmPost(String url, String jsonStr) {
        HttpHeaders requestHeaders = new HttpHeaders();
        HttpResp httpResp = new HttpResp();
        try {
            requestHeaders.add("Content-Type", "application/json");

            HttpEntity<String> fromEntity = new HttpEntity<>(jsonStr, requestHeaders);
            ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, fromEntity, String.class);

            httpResp.setUrl(url);
            httpResp.setStatusCode(String.valueOf(stringResponseEntity.getStatusCodeValue()));

            String body = stringResponseEntity.getBody();

            JSONObject jsonObject = JSONObject.parseObject(body);


            if (jsonObject != null) {
                httpResp.setMessage(jsonObject.getString("message"));
                httpResp.setCode(jsonObject.getString("code"));
                httpResp.setSuccess(true);

                if ("0000000".equals(httpResp.getCode())) {
                    httpResp.setSuccess(true);
                } else {
                    httpResp.setSuccess(jsonObject.getBoolean("success"));
                }

                //);
            }
        } catch (Exception e) {
            e.printStackTrace();
            httpResp.setSuccess(false);
        }
        return httpResp;
    }


}
