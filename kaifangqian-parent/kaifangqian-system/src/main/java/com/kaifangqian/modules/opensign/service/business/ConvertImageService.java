/**
 * @description 签署文件图片转换服务
 *
 * Copyright (C) [2025] [版权所有者（北京资源律动科技有限公司）]. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * 注意：本代码基于 AGPLv3 协议发布。若通过网络提供服务（如 Web 应用），
 * 必须公开修改后的完整源代码（包括衍生作品），详见协议全文。
 */
package com.kaifangqian.modules.opensign.service.business;

import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.opensign.enums.SignFileEnum;
import com.kaifangqian.modules.opensign.service.tool.SignFileService;
import com.kaifangqian.modules.opensign.vo.base.ImageConvertVo;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Description: ConvertImageService
 * @Package: com.kaifangqian.modules.opensign.service.business
 * @ClassName: ConvertImageService
 * @author: FengLai_Gong
 */
@Service
public class ConvertImageService {

    @Autowired
    private SignFileService signFileService ;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private Float resolution = 144f ;
//    private Float resolution = 90f ;
    /**图片格式为jpg类型**/
    public static final String IMG_JPE = "jpg";
    /**图片格式为png类型**/
    public static final String IMG_PNG = "png";


    public List<ImageConvertVo> convertImageAsync(byte[] signFileByte){
        if(signFileByte == null){
            return null ;
        }
        List<ImageConvertVo> responseList = new ArrayList<>();
        PDDocument doc = null;
        try {
            //加载pdf文件
            doc = Loader.loadPDF(signFileByte);
//            doc = PDDocument.load(signFileByte);
//            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            if(pageCount == 0){
                return responseList;
            }
            doc.close();
            //创建计数器
            CountDownLatch countDownLatch = new CountDownLatch(pageCount);
            for (int i = 0; i < pageCount; ++i) {
                int page = i ;
                threadPoolTaskExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
//                            PDDocument load = PDDocument.load(signFileByte);
                            PDDocument load = Loader.loadPDF(signFileByte);

                            PDFRenderer renderer = new PDFRenderer(load);
                            BufferedImage image = renderer.renderImageWithDPI(page, resolution);
                            ByteArrayOutputStream stream =  new ByteArrayOutputStream();
                            ImageIO.write(image, IMG_PNG, stream);
                            byte[] bytes = stream.toByteArray();
                            if(bytes != null){
                                ImageConvertVo response = new ImageConvertVo();
                                response.setImageByte(bytes);
                                response.setPage(page);
                                PDPage pdPage = load.getPage(page);
                                PDRectangle mediaBox = pdPage.getMediaBox();
                                if(pdPage.getRotation() == 90 || pdPage.getRotation()== 270){
                                    response.setImageHeight(mediaBox.getWidth() + "");
                                    response.setImageWidth(mediaBox.getHeight() + "");
                                }else {
                                    response.setImageHeight(mediaBox.getHeight() + "");
                                    response.setImageWidth(mediaBox.getWidth() + "");
                                }

                                responseList.add(response);
                                countDownLatch.countDown();
                            }
                            load.close();
                        }catch (Exception e){

                        }
                    }
                });
            }
            countDownLatch.await();
        }catch (Exception e) {
            e.printStackTrace();
        }
        if(responseList.size() == 0){
            throw new PaasException("图片转换失败");
        }

        return responseList ;
    }


//    public List<ImageConvertVo> convertImageSyn(byte[] signFileByte){
//        if(signFileByte == null){
//            return null ;
//        }
//        List<ImageConvertVo> responseList = new ArrayList<>();
//        PDDocument doc = null;
//        try {
//            //加载pdf文件
////            doc = PDDocument.load(signFileByte);
//            doc = Loader.loadPDF(signFileByte);
//            PDFRenderer renderer = new PDFRenderer(doc);
//            int pageCount = doc.getNumberOfPages();
//            if(pageCount == 0){
//                return responseList;
//            }
//
//            for (int i = 0; i < pageCount; ++i) {
//                int page = i ;
//                BufferedImage image = renderer.renderImageWithDPI(page, resolution);
//                ByteArrayOutputStream stream =  new ByteArrayOutputStream();
//                ImageIO.write(image, IMG_PNG, stream);
//                byte[] bytes = stream.toByteArray();
//                if(bytes != null){
//                    ImageConvertVo response = new ImageConvertVo();
//                    response.setImageByte(bytes);
//                    response.setPage(page);
//                    responseList.add(response);
//
//                }
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        if(responseList.size() == 0){
//            throw new PaasException("图片转换失败");
//        }
//
//        return responseList ;
//    }




    public List<ImageConvertVo> convertImage(byte[] signFileByte){
        if(signFileByte == null){
            return null ;
        }
        List<ImageConvertVo> responseList = new ArrayList<>();
        PDDocument doc = null;
        try {
            //加载pdf文件
//            doc = PDDocument.load(signFileByte);
            doc = Loader.loadPDF(signFileByte);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            if(pageCount == 0){
                return responseList;
            }

            for (int i = 0; i < pageCount; ++i) {
                int page = i ;
                BufferedImage image = renderer.renderImageWithDPI(page, resolution);
                ByteArrayOutputStream stream =  new ByteArrayOutputStream();
                ImageIO.write(image, IMG_PNG, stream);
                byte[] bytes = stream.toByteArray();
                if(bytes != null){

                    ImageConvertVo response = new ImageConvertVo();
                    response.setImageByte(bytes);
                    response.setPage(page);

                    PDPage pdPage = doc.getPage(i);
                    PDRectangle mediaBox = pdPage.getMediaBox();
                    if(pdPage.getRotation() == 90 || pdPage.getRotation() == 270){
                        response.setImageHeight(mediaBox.getWidth() + "");
                        response.setImageWidth(mediaBox.getHeight() + "");
                    }else {
                        response.setImageHeight(mediaBox.getHeight() + "");
                        response.setImageWidth(mediaBox.getWidth() + "");
                    }


                    responseList.add(response);

                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        if(responseList.size() == 0){
            throw new PaasException("图片转换失败");
        }
        //创建计数器
        CountDownLatch countDownLatch = new CountDownLatch(responseList.size());
        for(ImageConvertVo vo : responseList){
            threadPoolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    String annexId = signFileService.saveAnnexStorage(vo.getImageByte(), SignFileEnum.SIGN_FILE_IMAGE, null);
                    if(annexId != null){
                        vo.setImageAnnexId(annexId);
                        countDownLatch.countDown();
                    }
                }
            });
        }
        try {
            countDownLatch.await();
        } catch (Exception e) {
            throw new PaasException("图片转换失败");
        }

        return responseList ;
    }


//    /**
//     * @Description #转换图片并且存入临时表
//     * @Param [docId, annexId]
//     * @return java.util.List<com.kaifangqian.modules.opensign.vo.response.DocImageConvertResponse>
//     **/
//    public List<ImageConvertVo> convertImage(String annexId){
//        byte[] signFileByte = signFileService.getByteById(annexId);
//        if(signFileByte == null){
//            return null ;
//        }
//        List<ImageConvertVo> imageConvertVos = convertImage(signFileByte);
//        return imageConvertVos ;
//
//    }


}