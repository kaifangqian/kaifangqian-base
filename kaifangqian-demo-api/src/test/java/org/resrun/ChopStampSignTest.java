package org.resrun;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.resrun.sdk.enums.ControlPropertyTypePageConfigEnum;
import org.resrun.sdk.service.SDKService;
import org.resrun.sdk.service.pojo.SourcePositionProperty;
import org.resrun.sdk.vo.base.Result;
import org.resrun.sdk.vo.request.CertEventRequest;
import org.resrun.sdk.vo.response.CertEventResponse;
import org.resrun.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 骑缝签测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {OpenSignDemoApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChopStampSignTest {

    @Autowired
    private SDKService service;

    @Autowired
    private ResourceLoader resourceLoader;


    /**
     * 骑缝签默认签署功能
     * @throws IOException
     */
    @Test
    public void chopStampSign() throws IOException {

        //加载PDF文件和签章图片
        Resource resourcePDF = resourceLoader.getResource("classpath:example.pdf");
        Resource resourceSeal = resourceLoader.getResource("classpath:seal.png");
        byte [] pdf = FileUtils.readFileToByteArray(resourcePDF.getFile());
        byte [] seal = FileUtils.readFileToByteArray(resourceSeal.getFile());

        //构造申请证书对象
        CertEventRequest certEventRequest = new CertEventRequest();
        certEventRequest.setCertPassword("123456");
        certEventRequest.setCertSubject("张三@123456");
        certEventRequest.setUniqueCode(UUID.randomUUID().toString());
        //开始申请证书
        Result<CertEventResponse> result =  service.certEvent(certEventRequest);

        //进行签名
        byte [] signPDF = service.chopStampSign(pdf, Base64.decode(result.getData().getPfx()),certEventRequest.getCertPassword(),seal);

        FileUtils.writeByteArrayToFile(new File("target/example.pdf"),signPDF);
    }


    /**
     * 骑缝签扩展功能
     * @throws IOException
     */
    @Test
    public void chopStampExtendSign() throws IOException {
        //加载PDF文件和签章图片
        Resource resourcePDF = resourceLoader.getResource("classpath:example.pdf");
        Resource resourceSeal = resourceLoader.getResource("classpath:seal.png");
        byte [] pdf = FileUtils.readFileToByteArray(resourcePDF.getFile());
        byte [] seal = FileUtils.readFileToByteArray(resourceSeal.getFile());

        //构造申请证书对象
        CertEventRequest certEventRequest = new CertEventRequest();
        certEventRequest.setCertPassword("123456");
        certEventRequest.setCertSubject("张三@123456");
        certEventRequest.setUniqueCode(UUID.randomUUID().toString());
        //开始申请证书
        Result<CertEventResponse> result =  service.certEvent(certEventRequest);

        //构造扩展签名信息
        SourcePositionProperty sourcePositionProperty = new SourcePositionProperty();

        //前端显示PDF图片的宽高  非标准A4
        sourcePositionProperty.setPageHeight(1131.f);
        sourcePositionProperty.setPageWidth(800.f);

        //签名图图片的宽高
        sourcePositionProperty.setHeight(160.f);
        sourcePositionProperty.setWidth(160.f);

        //设置骑缝印章与顶部的距离  请勿超出 页面高度 - 签名图片的高度  否则签章会飞出PDF的可见区域
        sourcePositionProperty.setOffsetY(100.f);

        //进行扩展签名
        byte [] signPDF = service.chopStampSign(pdf, Base64.decode(result.getData().getPfx()),
                certEventRequest.getCertPassword(),seal, ControlPropertyTypePageConfigEnum.CUSTOM,"2,3,4",sourcePositionProperty);

        FileUtils.writeByteArrayToFile(new File("target/example_extend.pdf"),signPDF);

    }


}
