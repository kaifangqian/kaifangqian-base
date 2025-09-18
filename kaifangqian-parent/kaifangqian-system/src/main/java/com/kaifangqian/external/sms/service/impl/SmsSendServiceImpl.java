package com.kaifangqian.external.sms.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.kaifangqian.external.base.CommonResult;
import com.kaifangqian.external.sms.request.MsgRequest;
import com.kaifangqian.modules.api.util.SignHeadersGenerator;
import com.kaifangqian.modules.cert.service.HttpUtils;
import com.kaifangqian.external.sms.service.SmsSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class SmsSendServiceImpl implements SmsSendService {
    @Value("${service.app-id}")
    private String appId ;

    @Value("${service.yundun-service-private-key}")
    private String privateKey ;

    @Value("${service.manage.yundun-send-sms-url}")
    private String sendSmsUrl ;

    @Override
    public CommonResult<?> sendMsg(MsgRequest msgRequest) {
        // 返回接口信息
        CommonResult<?> result = null;

        JSONObject params = (JSONObject)JSONObject.toJSON(msgRequest);

        try{
            String uri = "/yundun/api/v1/msg/send";
            Map<String,String> headers = SignHeadersGenerator.generateSignHeaders(appId, params.toJSONString(), uri, privateKey);
            String returnJson = HttpUtils.executePost(sendSmsUrl, params.toJSONString(), headers);
            result = JSONObject.parseObject(returnJson,new TypeReference<CommonResult<?>>(){});

        }catch (Exception e){
            log.error("{} http error：",sendSmsUrl,e);
        }

        return result;
    }
}
