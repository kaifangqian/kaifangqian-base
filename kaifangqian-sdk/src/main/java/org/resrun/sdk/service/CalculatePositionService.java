package org.resrun.sdk.service;

import org.resrun.sdk.enums.APIResultEnum;
import org.resrun.sdk.enums.BusinessErrorEnum;
import org.resrun.sdk.enums.ControlPropertyTypePageConfigEnum;
import org.resrun.sdk.exception.BusinessException;
import org.resrun.sdk.service.pojo.RealPositionProperty;
import org.resrun.sdk.service.pojo.SelectKeywords;
import org.resrun.sdk.service.pojo.SourcePositionProperty;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
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

                    positionProperty.setStartx(result[0]);
                    positionProperty.setStarty(result[1]+height/4);
                    positionProperty.setPageNum((int)result[2]);
                    positionProperty.setEndx(result[0]+width/2);
                    positionProperty.setEndy(result[1]-height/4);
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

                    if(page.getRotation() == 90 || page.getRotation() == 270){
                        realPdfWidth = page.getCropBox().getHeight();
                        realPdfHeight = page.getCropBox().getWidth();
                    }

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

    public List<RealPositionProperty> calculateChopStampPositions(SourcePositionProperty sourcePositionProperty, byte[] pdfFileByte,ControlPropertyTypePageConfigEnum pageConfigEnum,String pageValue,byte[] sealByte){
        List<RealPositionProperty> properties = new ArrayList<>();


        try {
            PDDocument document = Loader.loadPDF(pdfFileByte);
            if(document == null){
                throw new RuntimeException("pdf文件解析失败");
            }
            int numberOfPages = document.getNumberOfPages();
            //解析页码
            List<Integer> pageList = parsePage(pageConfigEnum, numberOfPages, pageValue);
            if(pageList == null || pageList.size() == 0){
                return properties ;
            }else {
                //获取签章数据
//                BufferedImage read = ImageIO.read(new ByteArrayInputStream(sealByte));
//                int width = read.getWidth();
                //计算骑缝之后的签章宽度
//                int chopStampWidth =  width / pageList.size() ;
                Float width = sourcePositionProperty.getWidth();
                Float pageWidth = sourcePositionProperty.getPageWidth();
                //计算出页面上签章和页面的大小比例
                float rateSourceImage = width / pageWidth;
                for(Integer pageNumber : pageList){
                    //将pdf文件读入PdfReader工具类
                    PDPage page = document.getPage(pageNumber);
                    if(page == null){
                        throw new RuntimeException("pdf文件解析失败，无页码");
                    }
                    PDRectangle mediaBox = page.getMediaBox();
                    if(mediaBox == null){
                        throw new RuntimeException("pdf文件解析失败");
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
                    //按照比例，计算出真实文件中签章的大小，再根据页数计算出进行切分后的宽度
                    float realImageWidth = (realPdfWidth * rateSourceImage) / pageList.size() ;
                    //计算页面上的横纵坐标,由于页面上给出的是左上角的坐标，所以需要再转换计算一下
                    //左下角
                    float pageStartY = sourcePositionProperty.getOffsetY() + sourcePositionProperty.getHeight() ;
                    //右上角
                    float pageEndY = sourcePositionProperty.getOffsetY();
                    //根据比率去计算真实文档上的坐标位置
                    float startY = pageStartY * rateHeight;
                    float endY = pageEndY * rateHeight ;
                    float startX = realPdfWidth - realImageWidth;
                    float endX = realPdfWidth ;
                    //由于页面的纵坐标和pdf的纵坐标是相反的，所以真实的pdf的纵坐标在计算的时候需要再反转一下
                    startY = realPdfHeight - startY ;
                    endY = realPdfHeight - endY ;

                    RealPositionProperty realPositionProperty = new RealPositionProperty();
                    //封装返回数据
                    //x开始坐标和结束坐标
                    realPositionProperty.setStartx(startX);
                    realPositionProperty.setEndx(endX);
                    //y开始坐标和结束坐标
                    realPositionProperty.setStarty(startY);
                    realPositionProperty.setEndy(endY);
                    //pdf文件真实宽高
                    realPositionProperty.setRealPdfWidth(realPdfWidth);
                    realPositionProperty.setRealPdfHeight(realPdfHeight);
                    //页码
                    realPositionProperty.setPageNum(pageNumber);

//                    realPositionProperty.setControlType(sourcePositionProperty.getControlType());
                    properties.add(realPositionProperty);
                }
            }


            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties ;
    }


    public List<Integer> parsePage(ControlPropertyTypePageConfigEnum pageConfigEnum,int numberOfPages,String value){
        List<Integer> pageNumberList = new ArrayList<>();
        if(pageConfigEnum.getCode().equals(ControlPropertyTypePageConfigEnum.CUSTOM.getCode())){
            String[] split = value.split(",");
            if(split != null && split.length > 0){
                for(String s : split){
                    if(s.contains("-")){
                        String[] splitInner = s.split("-");
                        if(splitInner != null && splitInner.length > 0){
                            int start = Integer.valueOf(splitInner[0]);
                            int end = Integer.valueOf(splitInner[1]);
                            for(int i = start ; i <= end ; i++){
                                pageNumberList.add(i - 1);
                            }
                        }
                    }else {
                        pageNumberList.add(Integer.valueOf(s) - 1);
                    }
                }
            }
        }else{
            for(int i = 0 ; i < numberOfPages ; i++){
                if(pageConfigEnum.getCode().equals(ControlPropertyTypePageConfigEnum.ODD_NUMBER.getCode())){
                    if(i % 2 == 0){
                        pageNumberList.add(i);
                    }
                }else if(pageConfigEnum.getCode().equals(ControlPropertyTypePageConfigEnum.EVEN_NUMBER.getCode())){
                    if(i % 2 != 0){
                        pageNumberList.add(i);
                    }
                }else if(pageConfigEnum.getCode().equals(ControlPropertyTypePageConfigEnum.ALL.getCode())){
                    pageNumberList.add(i);
                }
            }
        }
        return pageNumberList ;
    }



}