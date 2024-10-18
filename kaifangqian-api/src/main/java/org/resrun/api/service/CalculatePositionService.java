package org.resrun.api.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.resrun.api.enums.APIResultEnum;
import org.resrun.api.enums.BusinessErrorEnum;
import org.resrun.api.exception.BusinessException;
import org.resrun.api.service.pojo.RealPositionProperty;
import org.resrun.api.service.pojo.SelectKeywords;
import org.resrun.api.service.pojo.SourcePositionProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 用于计算各种控件的位置
 * @Package: org.resrun.modules.sign.service.tool
 * @ClassName: CalculatePositionService
 * @author: FengLai_Gong
 */
@Service
public class CalculatePositionService {



    /**
     * @Description #批量计算真实签署位置
     * @Param [sourcePositionProperties]
     * @return java.util.List<org.resrun.modules.sign.service.tool.pojo.RealPositionProperty>
     **/
    public List<RealPositionProperty> calculatePositions(List<SourcePositionProperty> sourcePositionProperties, byte[] pdfFileByte){
        List<RealPositionProperty> realPositionProperties = new ArrayList<>();


        try {
            PDDocument document = Loader.loadPDF(pdfFileByte);
            if(document == null){
                throw new BusinessException(APIResultEnum.BUSINESS_ERROR, BusinessErrorEnum.SIGN_POSITION_CALCULATE_ERROR);
            }
            //将pdf文件读入PdfReader工具类
            for(SourcePositionProperty sourcePositionProperty : sourcePositionProperties){
                RealPositionProperty realPositionProperty = new RealPositionProperty();
                PDPage page = document.getPage(sourcePositionProperty.getPage()-1);
                if(page == null){
                    throw new BusinessException(APIResultEnum.BUSINESS_ERROR, BusinessErrorEnum.SIGN_POSITION_CALCULATE_ERROR);
                }
                PDRectangle mediaBox = page.getMediaBox();
                if(mediaBox == null){
                    throw new BusinessException(APIResultEnum.BUSINESS_ERROR, BusinessErrorEnum.SIGN_POSITION_CALCULATE_ERROR);
                }
                //获取真实pdf文件指定页的真实文档宽高
                float realPdfHeight = mediaBox.getHeight();
                float realPdfWidth = mediaBox.getWidth();
                if(page.getRotation() == 90 || page.getRotation() == 270){
                    realPdfWidth = page.getCropBox().getHeight();
                    realPdfHeight = page.getCropBox().getWidth();
                }

                //获取页面上文档的宽高
                float sourcePageWidth = sourcePositionProperty.getPageWidth();
                float sourcePageHeight = sourcePositionProperty.getPageHeight();
                //计算真实文档的宽高和页面文档的宽高的比率
                float rateHeight = realPdfHeight / sourcePageHeight;
                float rateWidth = realPdfWidth / sourcePageWidth;
                //计算页面上的横纵坐标,由于页面上给出的是左上角的坐标，所以需要再转换计算一下
                //左下角
                float pageStartX = sourcePositionProperty.getOffsetX();
                float pageStartY = sourcePositionProperty.getOffsetY() + sourcePositionProperty.getHeight() ;
                //右上角
                float pageEndX = sourcePositionProperty.getOffsetX() + sourcePositionProperty.getWidth();
                float pageEndY = sourcePositionProperty.getOffsetY();
                //根据比率去计算真实文档上的坐标位置
                float startX = pageStartX * rateWidth ;
                float startY = pageStartY * rateHeight;
                float endX = pageEndX * rateWidth ;
                float endY = pageEndY * rateHeight ;
                //由于页面的纵坐标和pdf的纵坐标是相反的，所以真实的pdf的纵坐标在计算的时候需要再反转一下
                startY = realPdfHeight - startY ;
                endY = realPdfHeight - endY ;
                //封装返回数据
                realPositionProperty.setStartx(startX);
                realPositionProperty.setStarty(startY);
                realPositionProperty.setEndx(endX);
                realPositionProperty.setEndy(endY);
                realPositionProperty.setPageNum(sourcePositionProperty.getPage());


                realPositionProperty.setValue(sourcePositionProperty.getValue());
                realPositionProperty.setAlign(sourcePositionProperty.getAlign());
                realPositionProperty.setFontFamily(sourcePositionProperty.getFontFamily());
                realPositionProperty.setFontSize(sourcePositionProperty.getFontSize());

                realPositionProperty.setRealPdfWidth(realPdfWidth);
                realPositionProperty.setRealPdfHeight(realPdfHeight);

                realPositionProperties.add(realPositionProperty);
            }
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return realPositionProperties ;
    }



    /**
     * @Description #单独计算真实签署位置
     * @Param [sourcePositionProperty]
     * @return org.resrun.modules.sign.service.tool.pojo.RealPositionProperty
     **/
    public RealPositionProperty calculatePosition(SourcePositionProperty sourcePositionProperty, byte[] pdfFileByte){
        RealPositionProperty realPositionProperty = new RealPositionProperty();
        try {
            //将pdf文件读入PdfReader工具类
            PDDocument document = Loader.loadPDF(pdfFileByte);
            if(document == null){
                throw new BusinessException(APIResultEnum.BUSINESS_ERROR, BusinessErrorEnum.SIGN_POSITION_CALCULATE_ERROR);
            }
            PDPage page = document.getPage(sourcePositionProperty.getPage());
            if(page == null){
                throw new BusinessException(APIResultEnum.BUSINESS_ERROR, BusinessErrorEnum.SIGN_POSITION_CALCULATE_ERROR);
            }
            PDRectangle mediaBox = page.getMediaBox();
            if(mediaBox == null){
                throw new BusinessException(APIResultEnum.BUSINESS_ERROR, BusinessErrorEnum.SIGN_POSITION_CALCULATE_ERROR);
            }
            //获取真实pdf文件指定页的真实文档宽高
            float realPdfHeight = mediaBox.getHeight();
            float realPdfWidth = mediaBox.getWidth();

            if(page.getRotation() == 90 || page.getRotation() == 270){
                realPdfWidth = page.getCropBox().getHeight();
                realPdfHeight = page.getCropBox().getWidth();
            }

            //获取页面上文档的宽高
            float sourcePageWidth = sourcePositionProperty.getPageWidth();
            float sourcePageHeight = sourcePositionProperty.getPageHeight();
            //计算真实文档的宽高和页面文档的宽高的比率
            float rateHeight = realPdfHeight / sourcePageHeight;
            float rateWidth = realPdfWidth / sourcePageWidth;
            //计算页面上的横纵坐标,由于页面上给出的是左上角的坐标，所以需要再转换计算一下
            //左下角
            float pageStartX = sourcePositionProperty.getOffsetX();
            float pageStartY = sourcePositionProperty.getOffsetY() + sourcePositionProperty.getHeight() ;
            //右上角
            float pageEndX = sourcePositionProperty.getOffsetX() + sourcePositionProperty.getWidth();
            float pageEndY = sourcePositionProperty.getOffsetY();
            //根据比率去计算真实文档上的坐标位置
            float startX = pageStartX * rateWidth ;
            float startY = pageStartY * rateHeight;
            float endX = pageEndX * rateWidth ;
            float endY = pageEndY * rateHeight ;
            //由于页面的纵坐标和pdf的纵坐标是相反的，所以真实的pdf的纵坐标在计算的时候需要再反转一下
            startY = realPdfHeight - startY ;
            endY = realPdfHeight - endY ;
            //封装返回数据
            realPositionProperty.setStartx(startX);
            realPositionProperty.setStarty(startY);
            realPositionProperty.setEndx(endX);
            realPositionProperty.setEndy(endY);
            realPositionProperty.setPageNum(sourcePositionProperty.getPage());
            realPositionProperty.setValue(sourcePositionProperty.getValue());
            realPositionProperty.setAlign(sourcePositionProperty.getAlign());
            realPositionProperty.setFontFamily(sourcePositionProperty.getFontFamily());
            realPositionProperty.setFontSize(sourcePositionProperty.getFontSize());

            realPositionProperty.setRealPdfWidth(realPdfWidth);
            realPositionProperty.setRealPdfHeight(realPdfHeight);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        log.info("====================================================================================================================");
//        log.info("原始页面坐标：{" + JSON.toJSONString(sourcePositionProperty) + "}    ,   真实文件坐标：{" + JSON.toJSONString(realPositionProperty) + "}");
//        log.info("====================================================================================================================");
        return realPositionProperty ;
    }




    /**
     * 通过查询关键字来获得签名位置信息<br/>
     *
     * 同一个关键字出现在多处会一次性全部找出
     *
     * @param pdfFile 签署源文件
     * @param keyWords 关键字
     * @param width 签章宽度
     * @param height 签章高度
     * @return 签署位置信息
     */
    public List<RealPositionProperty> getAllPositionByKeyWords(byte[] pdfFile,String keyWords,int width,int height) {
        List<RealPositionProperty> positions = new ArrayList<RealPositionProperty>();
        //调用通过关键字查询位置的方法
        List<float[]> results = null;
        try {
            results = new SelectKeywords().selectAllKeyword(pdfFile, keyWords);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(results !=null && results.size()>0){
            PDDocument document = null;
            try {
                document = Loader.loadPDF(pdfFile);
                if(document == null){
                    throw new BusinessException(APIResultEnum.BUSINESS_ERROR, BusinessErrorEnum.SIGN_POSITION_CALCULATE_ERROR);
                }

                for (float[] result : results) {
                    RealPositionProperty positionProperty = new RealPositionProperty();

                    PDPage page = document.getPage((int)result[2] - 1);

                    if(page == null){
                        throw new BusinessException(APIResultEnum.BUSINESS_ERROR, BusinessErrorEnum.SIGN_POSITION_CALCULATE_ERROR);
                    }
                    PDRectangle mediaBox = page.getMediaBox();
                    if(mediaBox == null){
                        throw new BusinessException(APIResultEnum.BUSINESS_ERROR, BusinessErrorEnum.SIGN_POSITION_CALCULATE_ERROR);
                    }

                    //获取真实pdf文件指定页的真实文档宽高
                    float realPdfHeight = mediaBox.getHeight();
                    float realPdfWidth = mediaBox.getWidth();
                    int newWidth = width,newHeight = height;
                    if(page.getRotation() == 90 || page.getRotation() == 270){
                        realPdfWidth = page.getCropBox().getHeight();
                        realPdfHeight = page.getCropBox().getWidth();
                        newWidth = height;
                        newHeight = width;
                    }

                    positionProperty.setStartx(result[0]);
                    positionProperty.setStarty(result[1]+height/4);
                    positionProperty.setPageNum((int)result[2]);
                    positionProperty.setEndx(result[0]+width/2);
                    positionProperty.setEndy(result[1]-height/4);



                    positionProperty.setRealPdfHeight(realPdfHeight);
                    positionProperty.setRealPdfWidth(realPdfWidth);
                    positions.add(positionProperty);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return positions;
    }
}