package org.resrun.api.utils;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConvertImage {


    /**
     * 图片清晰度
     */
    private Float resolution = 90f ;
    /**图片格式为jpg类型**/
    public static final String IMG_JPE = "jpg";
    /**图片格式为png类型**/
    public static final String IMG_PNG = "png";

    public List<byte[]> convertImage(byte[] signFileByte){
        if(signFileByte == null){
            return null ;
        }
        List<byte[]> responseList = new ArrayList<>();
        PDDocument doc = null;
        try {
            //加载pdf文件
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

                    responseList.add(bytes);

                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        if(responseList.size() == 0){
            throw new RuntimeException("图片转换失败");
        }


        return responseList ;
    }

    public static void main(String[] args) throws IOException {
        byte [] pdf = FileUtils.readFileToByteArray(new File("22222.pdf"));
        List<byte[]> images = new ConvertImage().convertImage(pdf);
        System.out.println("图片："+images.size());
    }

}