package org.resrun;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.resrun.sdk.service.SDKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {OpenSignDemoApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestChopStampSign {

    @Autowired
    private SDKService service;
    @Test
    public void chopStampSign() throws IOException {

        byte [] pdf = FileUtils.readFileToByteArray(new File("E://work//tem//pdfbox//1.8m.pdf"));
        byte [] pfx = FileUtils.readFileToByteArray(new File("E://work//tem//cert//tw-test01.pfx"));
        byte [] png = FileUtils.readFileToByteArray(new File("E://work//tem//1.png"));
        byte [] signPDF = service.chopStampSign(pdf,pfx,"password",png);
        FileUtils.writeByteArrayToFile(new File("E://work//tem//pdfbox//chop.pdf"),signPDF);
    }

}
