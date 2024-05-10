 package org.resrun;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.resrun.enums.SealShapeStyleEnum;
import org.resrun.sdk.service.SDKService;
import org.resrun.sdk.vo.base.Result;
import org.resrun.sdk.vo.request.CertEventRequest;
import org.resrun.sdk.vo.response.CertEventResponse;
import org.resrun.service.image.PersonalSealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {OpenSignDemoApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PsersonSealTest {



    @Test
    public void seal() throws IOException {

        //生成个人印章的名字
        String name = "张三";
        byte [] sealByte = null;
        BufferedImage bufferedImage = null ;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        //生成的印章类型  详情请看SealShapeStyleEnum枚举中的定义
        Integer shapeStyle = SealShapeStyleEnum.RECTANGLE_FRAME.getCode();

        //1红色，2蓝色，3黑色
        Integer sealColor = 1;
        try {
            if (SealShapeStyleEnum.RECTANGLE_NO_FRAME.getCode().equals(shapeStyle)) {
                bufferedImage = PersonalSealService.drawRectangleNoFrame(name, sealColor);
            } else if (SealShapeStyleEnum.RECTANGLE_FRAME.getCode().equals(shapeStyle)) {

                bufferedImage = PersonalSealService.drawRectangleFrame(name, sealColor);
            } else if (SealShapeStyleEnum.SQUARE_NO_FRAME.getCode().equals(shapeStyle)) {

                bufferedImage = PersonalSealService.drawSquareNoFrame(name, sealColor);
            } else if (SealShapeStyleEnum.SQUARE_FRAME.getCode().equals(shapeStyle)) {
                bufferedImage = PersonalSealService.drawSquareFrame(name, sealColor);
            }
            ImageIO.write(bufferedImage, "png", output);
            sealByte = output.toByteArray();

            FileUtils.writeByteArrayToFile(new File("target/pserson_seal.png"),sealByte);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

