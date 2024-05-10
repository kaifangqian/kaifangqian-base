package org.resrun;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.resrun.sdk.service.SDKService;
import org.resrun.sdk.vo.base.Result;
import org.resrun.sdk.vo.request.CertEventRequest;
import org.resrun.sdk.vo.response.CertEventResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {OpenSignDemoApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplyCertTest {

    @Autowired
    private SDKService service;

    @Test
    public void apply() throws IOException {

        CertEventRequest certEventRequest = new CertEventRequest();
        certEventRequest.setCertPassword("123456");
        certEventRequest.setCertSubject("北京测试科技有限公司@123456789987654321");
        certEventRequest.setUniqueCode(UUID.randomUUID().toString());
        Result<CertEventResponse> result =  service.certEvent(certEventRequest);
        System.out.println(result.getData().getPfx());
        System.out.println(JSONObject.toJSONString(result.getData()));

    }
}
