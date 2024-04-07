package org.resrun;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.resrun.sdk.service.SDKService;
import org.resrun.sdk.vo.base.Result;
import org.resrun.sdk.vo.request.CertEventRequest;
import org.resrun.sdk.vo.response.CertEventResponse;
import org.resrun.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {OpenSignDemoApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestChopStampSign {

    @Autowired
    private SDKService service;
    @Test
    public void chopStampSign() throws IOException {

        byte [] pdf = FileUtils.readFileToByteArray(new File("E://work//tem//pdfbox//123.pdf"));
        byte [] pfx = FileUtils.readFileToByteArray(new File("E://work//tem//cert//tw-test01.pfx"));
        byte [] png = FileUtils.readFileToByteArray(new File("E://work//tem//1.png"));

        //byte [] signPDF = service.chopStampSign(pdf,pfx,"password",png);
        CertEventRequest certEventRequest = new CertEventRequest();
        certEventRequest.setCertPassword("123456");
        certEventRequest.setCertSubject("张三@123456");
//        certEventRequest.setCertSubject("北京资源律动科技有限公司@91110108MA01MNU448");
        certEventRequest.setUniqueCode(UUID.randomUUID().toString());
        Result<CertEventResponse> result =  service.certEvent(certEventRequest);

        byte [] signPDF = service.chopStampSign(pdf, Base64.decode(result.getData().getPfx()),"123456",png);

        FileUtils.writeByteArrayToFile(new File("E://work//tem//pdfbox//chop.pdf"),signPDF);
    }

}
