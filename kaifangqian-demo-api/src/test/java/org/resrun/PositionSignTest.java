package org.resrun;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.resrun.sdk.service.SDKService;
import org.resrun.sdk.vo.base.Result;
import org.resrun.sdk.vo.base.SignLocation;
import org.resrun.sdk.vo.request.CertEventRequest;
import org.resrun.sdk.vo.request.DocumentSignRequest;
import org.resrun.sdk.vo.response.CertEventResponse;
import org.resrun.sdk.vo.response.DocumentSignResponse;
import org.resrun.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 位置签署测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {OpenSignDemoApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PositionSignTest {

    @Autowired
    private SDKService service;

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    public void positionSign() throws IOException {

        Resource resourcePDF = resourceLoader.getResource("classpath:example.pdf");
        Resource resourceSeal = resourceLoader.getResource("classpath:seal.png");
        byte [] pdf = FileUtils.readFileToByteArray(resourcePDF.getFile());
        byte [] png = FileUtils.readFileToByteArray(resourceSeal.getFile());


        //申请证书
        CertEventRequest certEventRequest = new CertEventRequest();
        certEventRequest.setCertPassword("123456");
        certEventRequest.setCertSubject("张三@123456");
        certEventRequest.setUniqueCode(UUID.randomUUID().toString());
        Result<CertEventResponse> result =  service.certEvent(certEventRequest);


        DocumentSignRequest request = new DocumentSignRequest();
        request.setDocumentFile(Base64.encode(pdf));
        request.setSignatureFile(Base64.encode(png));
        request.setSignType(2);
        request.setPfx(result.getData().getPfx());
        request.setCertPassword(certEventRequest.getCertPassword());
        List<SignLocation> signLocationList = new ArrayList<>();
        SignLocation signLocation = new SignLocation();
        signLocation.setPage(1);
        signLocation.setSignHeight(120);
        signLocation.setSignWidth(120);
        signLocation.setOffsetX(100);
        signLocation.setOffsetY(100);
        signLocation.setPageHeight(1131);
        signLocation.setPageWidth(800);
        signLocationList.add(signLocation);
        request.setSignLocationList(signLocationList);
        request.setUniqueCode(UUID.randomUUID().toString());

        Result<DocumentSignResponse>  signResult = service.documentSign(request);
        FileUtils.writeByteArrayToFile(new File("target/positionSign.pdf"),signResult.getData().getDocumentFile());
    }
}
