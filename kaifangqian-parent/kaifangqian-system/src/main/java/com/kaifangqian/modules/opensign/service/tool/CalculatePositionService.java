/**
 * @description 用于计算各种控件的位置
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
package com.kaifangqian.modules.opensign.service.tool;

import com.kaifangqian.modules.opensign.service.tool.pojo.ControlWidgetProperty;
import com.kaifangqian.modules.opensign.service.tool.pojo.RealPositionProperty;
import com.kaifangqian.modules.opensign.service.tool.pojo.SourcePositionProperty;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.opensign.enums.ControlPropertyTypePageConfigEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 用于计算各种控件的位置
 * @Package: com.kaifangqian.modules.sign.service.tool
 * @ClassName: CalculatePositionService
 * @author: FengLai_Gong
 */
@Slf4j
@Service
public class CalculatePositionService {


    public List<Integer> parseCustomPage(String value) {

        List<Integer> pageNumberList = new ArrayList<>();
        if (value != null && value.length() > 0) {
            String[] split = value.split(",");
            if (split != null) {
                for (String s : split) {
                    if (s.contains("-")) {
                        String[] splitInner = s.split("-");
                        if (splitInner != null && splitInner.length > 0) {
                            int start = Integer.valueOf(splitInner[0]);
                            int end = Integer.valueOf(splitInner[1]);
                            for (int i = start; i <= end; i++) {
                                pageNumberList.add(i - 1);
                            }
                        }
                    } else {
                        pageNumberList.add(Integer.valueOf(s) - 1);
                    }
                }
            }
        }

        return pageNumberList;

    }


    public List<Integer> parsePage(ControlPropertyTypePageConfigEnum pageConfigEnum, int numberOfPages, String value) {
        List<Integer> pageNumberList = new ArrayList<>();
        if (pageConfigEnum.getCode().equals(ControlPropertyTypePageConfigEnum.CUSTOM.getCode())) {
            List<Integer> customPage = parseCustomPage(value);
            pageNumberList.addAll(customPage);
        } else {
            for (int i = 0; i < numberOfPages; i++) {
                if (pageConfigEnum.getCode().equals(ControlPropertyTypePageConfigEnum.ODD_NUMBER.getCode())) {
                    if (i % 2 == 0) {
                        pageNumberList.add(i);
                    }
                } else if (pageConfigEnum.getCode().equals(ControlPropertyTypePageConfigEnum.EVEN_NUMBER.getCode())) {
                    if (i % 2 != 0) {
                        pageNumberList.add(i);
                    }
                } else if (pageConfigEnum.getCode().equals(ControlPropertyTypePageConfigEnum.ALL.getCode())) {
                    pageNumberList.add(i);
                }
            }
        }
        return pageNumberList;
    }


    /**
     * @Description #批量计算真实签署位置
     * @Param [sourcePositionProperties]
     * @return java.util.List<com.kaifangqian.modules.sign.service.tool.pojo.RealPositionProperty>
     **/
//    public List<RealPositionProperty> calculateChopStampPositions(SourcePositionProperty sourcePositionProperty, byte[] pdfFileByte,ControlPropertyTypePageConfigEnum pageConfigEnum,String pageValue,byte[] sealByte){
//        List<RealPositionProperty> properties = new ArrayList<>();
//
//
//        try {
//            PDDocument document = Loader.loadPDF(pdfFileByte);
//            if(document == null){
//                throw new PaasException("pdf文件解析失败");
//            }
//            int numberOfPages = document.getNumberOfPages();
//            //解析页码
//            List<Integer> pageList = parsePage(pageConfigEnum, numberOfPages, pageValue);
//            if(pageList == null || pageList.size() == 0){
//                return properties ;
//            }else {
//                Float width = sourcePositionProperty.getWidth();
//                Float pageWidth = sourcePositionProperty.getPageWidth();
//                //计算出页面上签章和页面的大小比例
//                float rateSourceImage = width / pageWidth;
//                for(Integer pageNumber : pageList){
//                    //将pdf文件读入PdfReader工具类
//                    PDPage page = document.getPage(pageNumber);
//                    if(page == null){
//                        throw new PaasException("pdf文件解析失败，无页码");
//                    }
//                    PDRectangle mediaBox = page.getMediaBox();
//                    if(mediaBox == null){
//                        throw new PaasException("pdf文件解析失败");
//                    }
//                    //获取真实pdf文件指定页的真实文档宽高
//                    float realPdfHeight = mediaBox.getHeight();
//                    float realPdfWidth = mediaBox.getWidth();
//                    if(page.getRotation() == 90 || page.getRotation() == 270){
//                        realPdfWidth = mediaBox.getHeight();
//                        realPdfHeight = mediaBox.getWidth();
//                    }
//
//                    //获取页面上文档的宽高
//                    float sourcePageWidth = sourcePositionProperty.getPageWidth();
//                    float sourcePageHeight = sourcePositionProperty.getPageHeight();
//                    //计算真实文档的宽高和页面文档的宽高的比率
//                    float rateHeight = realPdfHeight / sourcePageHeight;
//
//                    //按照比例，计算出真实文件中签章的大小，再根据页数计算出进行切分后的宽度
//                    float realImageWidth = (realPdfWidth * rateSourceImage) / pageList.size() ;
//                    //计算页面上的横纵坐标,由于页面上给出的是左上角的坐标，所以需要再转换计算一下
//                    //左下角
//                    float pageStartY = sourcePositionProperty.getOffsetY() + sourcePositionProperty.getHeight() ;
//                    //右上角
//                    float pageEndY = sourcePositionProperty.getOffsetY();
//                    //根据比率去计算真实文档上的坐标位置
//                    float startY = pageStartY * rateHeight;
//                    float endY = pageEndY * rateHeight ;
//                    float startX = realPdfWidth - realImageWidth;
//                    float endX = realPdfWidth ;
//                    //由于页面的纵坐标和pdf的纵坐标是相反的，所以真实的pdf的纵坐标在计算的时候需要再反转一下
//                    startY = realPdfHeight - startY ;
//                    endY = realPdfHeight - endY ;
//
//                    RealPositionProperty realPositionProperty = new RealPositionProperty();
//                    //封装返回数据
//                    //x开始坐标和结束坐标
//                    realPositionProperty.setStartx(startX);
//                    realPositionProperty.setEndx(endX);
//                    //y开始坐标和结束坐标
//                    realPositionProperty.setStarty(startY);
//                    realPositionProperty.setEndy(endY);
//                    //pdf文件真实宽高
//                    realPositionProperty.setRealPdfWidth(realPdfWidth);
//                    realPositionProperty.setRealPdfHeight(realPdfHeight);
//                    //页码
//                    realPositionProperty.setPageNum(pageNumber);
//
//                    realPositionProperty.setControlType(sourcePositionProperty.getControlType());
//                    properties.add(realPositionProperty);
//                }
//            }
//
//
//            document.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return properties ;
//    }

    /**
     * @return java.util.List<com.kaifangqian.modules.sign.service.tool.pojo.RealPositionProperty>
     * @Description #批量计算真实签署位置
     * @Param [sourcePositionProperties]
     **/
    public List<RealPositionProperty> calculateChopStampPositionsReal(SourcePositionProperty sourcePositionProperty, byte[] pdfFileByte, ControlPropertyTypePageConfigEnum pageConfigEnum, String pageValue, byte[] sealByte) {
        List<RealPositionProperty> properties = new ArrayList<>();


        try {
            PDDocument document = Loader.loadPDF(pdfFileByte);
            if (document == null) {
                throw new PaasException("pdf文件解析失败");
            }
            int numberOfPages = document.getNumberOfPages();
            //解析页码
            List<Integer> pageList = parsePage(pageConfigEnum, numberOfPages, pageValue);
            if (pageList == null || pageList.size() == 0) {
                return properties;
            } else {
                Float width = sourcePositionProperty.getWidth();
//                Float height = sourcePositionProperty.getHeight();
                int size = pageList.size();
                width = width / size;
                //计算出页面上签章和页面的大小比例
                for (Integer pageNumber : pageList) {
                    //将pdf文件读入PdfReader工具类
                    PDPage page = document.getPage(pageNumber);
                    if (page == null) {
                        throw new PaasException("pdf文件解析失败，无页码");
                    }
                    PDRectangle mediaBox = page.getMediaBox();
                    if (mediaBox == null) {
                        throw new PaasException("pdf文件解析失败");
                    }
                    //获取真实pdf文件指定页的真实文档宽高
                    float realPdfHeight = mediaBox.getHeight();
                    float realPdfWidth = mediaBox.getWidth();

                    if (page.getRotation() == 90 || page.getRotation() == 270) {
                        realPdfWidth = mediaBox.getHeight();
                        realPdfHeight = mediaBox.getWidth();
                    }

                    float startX = realPdfWidth - width;
                    float endX = realPdfWidth;
                    //由于页面的纵坐标和pdf的纵坐标是相反的，所以真实的pdf的纵坐标在计算的时候需要再反转一下
                    float startY = realPdfHeight - sourcePositionProperty.getOffsetY() - sourcePositionProperty.getHeight();
                    float endY = realPdfHeight - sourcePositionProperty.getOffsetY();

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

                    realPositionProperty.setControlType(sourcePositionProperty.getControlType());
                    properties.add(realPositionProperty);
                }
            }


            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

//    public List<RealPositionProperty> calculateSignPositions(SourcePositionProperty sourcePositionProperty, byte[] pdfFileByte,ControlPropertyTypePageConfigEnum pageConfigEnum,String pageValue,byte[] sealByte ){
//        List<RealPositionProperty> properties = new ArrayList<>();
//        try {
//            PDDocument document = Loader.loadPDF(pdfFileByte);
//            if(document == null){
//                throw new PaasException("pdf文件解析失败");
//            }
//            int numberOfPages = document.getNumberOfPages();
//            List<Integer> pageList = parsePage(pageConfigEnum, numberOfPages, pageValue);
//            if(pageList == null || pageList.size() == 0){
//                return properties ;
//            }else {
//                //获取签章数据
//                BufferedImage read = ImageIO.read(new ByteArrayInputStream(sealByte));
//                int sealImageWidth = read.getWidth();
//                int sealImageHeight = read.getHeight();
//                System.out.println("真实图片宽高：sealImageWidth ：" + sealImageWidth + "，sealImageHeight ：" + sealImageHeight);
//                for(Integer pageNumber : pageList){
//                    //将pdf文件读入PdfReader工具类
//                    PDPage page = document.getPage(pageNumber);
//                    if(page == null){
//                        throw new PaasException("pdf文件解析失败，无页码");
//                    }
//                    PDRectangle mediaBox = page.getMediaBox();
//                    if(mediaBox == null){
//                        throw new PaasException("pdf文件解析失败");
//                    }
//
//                    //获取真实pdf文件指定页的真实文档宽高
//                    float realPdfHeight = mediaBox.getHeight();
//                    float realPdfWidth = mediaBox.getWidth();
//                    if(page.getRotation() == 90 || page.getRotation() == 270){
//                        realPdfWidth = mediaBox.getHeight();
//                        realPdfHeight = mediaBox.getWidth();
//                    }
//
//                    //获取页面上文档的宽高
//                    float sourcePageWidth = sourcePositionProperty.getPageWidth();
//                    float sourcePageHeight = sourcePositionProperty.getPageHeight();
//                    //计算真实文档的宽高和页面文档的宽高的比率
//                    float rateHeight = realPdfHeight / sourcePageHeight;
//                    float rateWidth = realPdfWidth / sourcePageWidth;
//                    //计算页面上的横纵坐标,由于页面上给出的是左上角的坐标，所以需要再转换计算一下
//                    //左下角
//                    float pageStartX = sourcePositionProperty.getOffsetX();
//                    float pageStartY = sourcePositionProperty.getOffsetY() + sourcePositionProperty.getHeight() ;
//                    //右上角
//                    float pageEndX = sourcePositionProperty.getOffsetX() + sourcePositionProperty.getWidth();
//                    float pageEndY = sourcePositionProperty.getOffsetY();
//                    //根据比率去计算真实文档上的坐标位置
//                    float startX = pageStartX * rateWidth ;
//                    float startY = pageStartY * rateHeight;
//                    float endX = pageEndX * rateWidth ;
//                    float endY = pageEndY * rateHeight ;
//                    //由于页面的纵坐标和pdf的纵坐标是相反的，所以真实的pdf的纵坐标在计算的时候需要再反转一下
//                    startY = realPdfHeight - startY ;
//                    endY = realPdfHeight - endY ;

    /// /                    //根据真实图片宽高计算签署位置宽高
    /// /                    BigDecimal multiply = new BigDecimal(sealImageWidth).divide(new BigDecimal(sealImageHeight), RoundingMode.HALF_UP).multiply(new BigDecimal(Math.abs(endY - startY)));
    /// /                    float realImageWidth = multiply.floatValue();
    /// /                    endX = startX + realImageWidth ;
//
//
//                    RealPositionProperty realPositionProperty = new RealPositionProperty();
//                    //封装返回数据
//                    realPositionProperty.setStartx(startX);
//                    realPositionProperty.setStarty(startY);
//                    realPositionProperty.setEndx(endX);
//                    realPositionProperty.setEndy(endY);
//                    realPositionProperty.setPageNum(pageNumber);
//
//                    realPositionProperty.setRealPdfWidth(realPdfWidth);
//                    realPositionProperty.setRealPdfHeight(realPdfHeight);
//
//                    realPositionProperty.setControlType(sourcePositionProperty.getControlType());
//
//                    properties.add(realPositionProperty);
//                }
//            }
//
//
//            document.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return properties ;
//    }
    public List<RealPositionProperty> calculateSignPositionsReal(SourcePositionProperty sourcePositionProperty, byte[] pdfFileByte, ControlPropertyTypePageConfigEnum pageConfigEnum, String pageValue, byte[] sealByte) {
        List<RealPositionProperty> properties = new ArrayList<>();
        try {
            PDDocument document = Loader.loadPDF(pdfFileByte);
            if (document == null) {
                throw new PaasException("pdf文件解析失败");
            }
            int numberOfPages = document.getNumberOfPages();
            List<Integer> pageList = parsePage(pageConfigEnum, numberOfPages, pageValue);
            if (pageList == null || pageList.size() == 0) {
                return properties;
            } else {
                for (Integer pageNumber : pageList) {
                    //将pdf文件读入PdfReader工具类
                    PDPage page = document.getPage(pageNumber);
                    if (page == null) {
                        throw new PaasException("pdf文件解析失败，无页码");
                    }
                    PDRectangle mediaBox = page.getMediaBox();
                    if (mediaBox == null) {
                        throw new PaasException("pdf文件解析失败");
                    }

                    //获取真实pdf文件指定页的真实文档宽高
                    float realPdfHeight = mediaBox.getHeight();
                    float realPdfWidth = mediaBox.getWidth();
                    if (page.getRotation() == 90 || page.getRotation() == 270) {
                        realPdfWidth = mediaBox.getHeight();
                        realPdfHeight = mediaBox.getWidth();
                    }

                    Float startX = sourcePositionProperty.getOffsetX();
                    Float endX = sourcePositionProperty.getOffsetX() + sourcePositionProperty.getWidth();

                    //由于页面的纵坐标和pdf的纵坐标是相反的，所以真实的pdf的纵坐标在计算的时候需要再反转一下
                    float startY = realPdfHeight - sourcePositionProperty.getOffsetY() - sourcePositionProperty.getHeight();
                    float endY = realPdfHeight - sourcePositionProperty.getOffsetY();

//                    Float startY = sourcePositionProperty.getOffsetY() - sourcePositionProperty.getHeight();
//                    Float endY = sourcePositionProperty.getOffsetY();
//
//                    //由于页面的纵坐标和pdf的纵坐标是相反的，所以真实的pdf的纵坐标在计算的时候需要再反转一下
//                    startY = realPdfHeight - startY ;
//                    endY = realPdfHeight - endY ;

                    Float control_w_new = 0f;
                    Float control_h_new = 0f;
                    //获取图片宽高
                    BufferedImage read = ImageIO.read(new ByteArrayInputStream(sealByte));
                    int imageWidth = read.getWidth();
                    int imageHeight = read.getHeight();
                    //计算图片宽高比例
                    Float imageRate = new BigDecimal(imageWidth).divide(new BigDecimal(imageHeight), 2, RoundingMode.HALF_UP).floatValue();
                    //获取控件宽高
//                    Float control_width = Math.abs(startY - startX) ;
//                    Float control_height = Math.abs(endY - endX);
                    Float control_width = sourcePositionProperty.getWidth();
                    Float control_height = sourcePositionProperty.getHeight();
                    //计算控件宽高比例
                    Float controlRate = new BigDecimal(control_width).divide(new BigDecimal(control_height), 2, RoundingMode.HALF_UP).floatValue();
                    //计算最终控件宽高
                    if (imageRate > controlRate) {
                        //签章图片宽高比大于控件宽高比
                        control_w_new = control_width;
                        control_h_new = (imageHeight * control_w_new) / imageWidth;

                        endX = startX + control_w_new;
                        endY = startY - control_h_new;
                    } else if (imageRate < controlRate) {
                        //签章图片宽高比小于控件宽高比
                        control_h_new = control_height;
                        control_w_new = (imageWidth * control_h_new) / imageHeight;
                        endX = startX + control_w_new;
                        endY = startY - control_h_new;
                    }
//                    System.out.println("控件类型：" + sourcePositionProperty.getControlType() + ", 控件原始宽度：" + sourcePositionProperty.getWidth() + ", 控件原始高度:" + sourcePositionProperty.getHeight() + ", 控件最终高度：" + control_h_new + "，控件最终宽度：" + control_w_new + "，图片宽度：" + imageWidth + "，图片高度：" + imageHeight);
                    RealPositionProperty realPositionProperty = new RealPositionProperty();
                    //封装返回数据
                    realPositionProperty.setStartx(startX);
                    realPositionProperty.setStarty(startY);
                    realPositionProperty.setEndx(endX);
                    realPositionProperty.setEndy(endY);
                    realPositionProperty.setPageNum(pageNumber);

                    realPositionProperty.setRealPdfWidth(realPdfWidth);
                    realPositionProperty.setRealPdfHeight(realPdfHeight);

                    realPositionProperty.setControlType(sourcePositionProperty.getControlType());

                    properties.add(realPositionProperty);
                }
            }


            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }


//    /**
//     * @Description #批量计算真实签署位置
//     * @Param [sourcePositionProperties]
//     * @return java.util.List<com.kaifangqian.modules.sign.service.tool.pojo.RealPositionProperty>
//     **/
//    public RealPositionProperty calculatePositions(SourcePositionProperty sourcePositionProperty, byte[] pdfFileByte){
//        RealPositionProperty realPositionProperty = new RealPositionProperty();
//        try {
//            PDDocument document = Loader.loadPDF(pdfFileByte);
//            if(document == null){
//                throw new PaasException("pdf文件解析失败");
//            }
//            //将pdf文件读入PdfReader工具类
//
//            PDPage page = document.getPage(sourcePositionProperty.getPage());
//            if(page == null){
//                throw new PaasException("pdf文件解析失败，无页码");
//            }
//            PDRectangle mediaBox = page.getMediaBox();
//            if(mediaBox == null){
//                throw new PaasException("pdf文件解析失败");
//            }
//            //获取真实pdf文件指定页的真实文档宽高
//            float realPdfHeight = mediaBox.getHeight();
//            float realPdfWidth = mediaBox.getWidth();
//
//            //获取页面上文档的宽高
//            float sourcePageWidth = sourcePositionProperty.getPageWidth();
//            float sourcePageHeight = sourcePositionProperty.getPageHeight();
//            //计算真实文档的宽高和页面文档的宽高的比率
//            float rateHeight = realPdfHeight / sourcePageHeight;
//            float rateWidth = realPdfWidth / sourcePageWidth;
//            //计算页面上的横纵坐标,由于页面上给出的是左上角的坐标，所以需要再转换计算一下
//            //左下角
//            float pageStartX = sourcePositionProperty.getOffsetX();
//            float pageStartY = sourcePositionProperty.getOffsetY() + sourcePositionProperty.getHeight() ;
//            //右上角
//            float pageEndX = sourcePositionProperty.getOffsetX() + sourcePositionProperty.getWidth();
//            float pageEndY = sourcePositionProperty.getOffsetY();
//            //根据比率去计算真实文档上的坐标位置
//            float startX = pageStartX * rateWidth ;
//            float startY = pageStartY * rateHeight;
//            float endX = pageEndX * rateWidth ;
//            float endY = pageEndY * rateHeight ;
//            //由于页面的纵坐标和pdf的纵坐标是相反的，所以真实的pdf的纵坐标在计算的时候需要再反转一下
//            startY = realPdfHeight - startY ;
//            endY = realPdfHeight - endY ;
//            //封装返回数据
//            realPositionProperty.setStartx(startX);
//            realPositionProperty.setStarty(startY);
//            realPositionProperty.setEndx(endX);
//            realPositionProperty.setEndy(endY);
//            realPositionProperty.setPageNum(sourcePositionProperty.getPage());
//
//
//            realPositionProperty.setValue(sourcePositionProperty.getValue());
//            realPositionProperty.setAlign(sourcePositionProperty.getAlign());
//            realPositionProperty.setFontFamily(sourcePositionProperty.getFontFamily());
//            realPositionProperty.setFontSize(sourcePositionProperty.getFontSize());
//
//            realPositionProperty.setRealPdfWidth(realPdfWidth);
//            realPositionProperty.setRealPdfHeight(realPdfHeight);
//
//            realPositionProperty.setControlType(sourcePositionProperty.getControlType());
//
//            document.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return realPositionProperty ;
//    }


    /**
     * @Description #批量计算真实签署位置
     * @Param [sourcePositionProperties]
     * @return java.util.List<com.kaifangqian.modules.sign.service.tool.pojo.RealPositionProperty>
     **/
//    public List<RealPositionProperty> calculateWritePositions(List<SourcePositionProperty> sourcePositionProperties, byte[] pdfFileByte){
//        List<RealPositionProperty> realPositionProperties = new ArrayList<>();
//
//
//        try {
//            PDDocument document = Loader.loadPDF(pdfFileByte);
//            if(document == null){
//                throw new PaasException("pdf文件解析失败");
//            }
//            //将pdf文件读入PdfReader工具类
//            for(SourcePositionProperty sourcePositionProperty : sourcePositionProperties){
//                RealPositionProperty realPositionProperty = new RealPositionProperty();
//                PDPage page = document.getPage(sourcePositionProperty.getPage());
//                if(page == null){
//                    throw new PaasException("pdf文件解析失败，无页码");
//                }
//                PDRectangle mediaBox = page.getMediaBox();
//                if(mediaBox == null){
//                    throw new PaasException("pdf文件解析失败");
//                }
//
//                //获取真实pdf文件指定页的真实文档宽高
//                float realPdfHeight = mediaBox.getHeight();
//                float realPdfWidth = mediaBox.getWidth();
//                if(page.getRotation() == 90 || page.getRotation() == 270){
//                    realPdfWidth = mediaBox.getHeight();
//                    realPdfHeight = mediaBox.getWidth();
//                }
//
//                //获取页面上文档的宽高
//                float sourcePageWidth = sourcePositionProperty.getPageWidth();
//                float sourcePageHeight = sourcePositionProperty.getPageHeight();
//                //计算真实文档的宽高和页面文档的宽高的比率
//                float rateHeight = realPdfHeight / sourcePageHeight;
//                float rateWidth = realPdfWidth / sourcePageWidth;
//                //计算页面上的横纵坐标,由于页面上给出的是左上角的坐标，所以需要再转换计算一下
//                //左下角
//                float pageStartX = sourcePositionProperty.getOffsetX();
//                float pageStartY = sourcePositionProperty.getOffsetY() + sourcePositionProperty.getHeight() ;
//                //右上角
//                float pageEndX = sourcePositionProperty.getOffsetX() + sourcePositionProperty.getWidth();
//                float pageEndY = sourcePositionProperty.getOffsetY();
//                //根据比率去计算真实文档上的坐标位置
//                float startX = pageStartX * rateWidth ;
//                float startY = pageStartY * rateHeight;
//                float endX = pageEndX * rateWidth ;
//                float endY = pageEndY * rateHeight ;
//                //由于页面的纵坐标和pdf的纵坐标是相反的，所以真实的pdf的纵坐标在计算的时候需要再反转一下
//                startY = realPdfHeight - startY ;
//                endY = realPdfHeight - endY ;
//                //封装返回数据
//                realPositionProperty.setStartx(startX);
//                realPositionProperty.setStarty(startY);
//                realPositionProperty.setEndx(endX);
//                realPositionProperty.setEndy(endY);
//                realPositionProperty.setPageNum(sourcePositionProperty.getPage());
//                if(sourcePositionProperty.getWidgetPropertyList() != null && sourcePositionProperty.getWidgetPropertyList().size() > 0){
//                    for(ControlWidgetProperty widgetProperty : sourcePositionProperty.getWidgetPropertyList()){
//                        if(widgetProperty.getX() != null){
//                            BigDecimal multiply = new BigDecimal(widgetProperty.getX()).multiply(new BigDecimal(rateWidth));
//                            widgetProperty.setX(multiply.intValue());
//                        }
//                        if(widgetProperty.getY() != null){
//                            BigDecimal multiply = new BigDecimal(widgetProperty.getY()).multiply(new BigDecimal(rateHeight));
//                            BigDecimal subtract = new BigDecimal(realPdfHeight).subtract(multiply);
//                            widgetProperty.setY(subtract.intValue());
//                        }
//                        if(widgetProperty.getW() != null){
//                            BigDecimal multiply = new BigDecimal(widgetProperty.getW()).multiply(new BigDecimal(rateWidth));
//                            widgetProperty.setW(multiply.intValue());
//                        }
//                        if(widgetProperty.getH() != null){
//                            BigDecimal multiply = new BigDecimal(widgetProperty.getH()).multiply(new BigDecimal(rateHeight));
//                            widgetProperty.setH(multiply.intValue());
//                        }
//
//                    }
//                }
//                if(sourcePositionProperty.getFontSize() != null){
//                    BigDecimal multiply = new BigDecimal(sourcePositionProperty.getFontSize()).multiply(new BigDecimal(rateWidth));
//                    realPositionProperty.setFontSize(multiply.toString());
//                }
//
//
//                realPositionProperty.setValue(sourcePositionProperty.getValue());
//                realPositionProperty.setAlign(sourcePositionProperty.getAlign());
//                realPositionProperty.setFontFamily(sourcePositionProperty.getFontFamily());
//
//
//                realPositionProperty.setRealPdfWidth(realPdfWidth);
//                realPositionProperty.setRealPdfHeight(realPdfHeight);
//
//                realPositionProperty.setControlType(sourcePositionProperty.getControlType());
//                //填写控件内部属性
//                realPositionProperty.setWidgetPropertyList(sourcePositionProperty.getWidgetPropertyList());
//
//                realPositionProperties.add(realPositionProperty);
//            }
//            document.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return realPositionProperties ;
//    }


    /**
     * @return java.util.List<com.kaifangqian.modules.sign.service.tool.pojo.RealPositionProperty>
     * @Description #批量计算真实签署位置
     * @Param [sourcePositionProperties]
     **/
    public List<RealPositionProperty> calculateWritePositionsReal(List<SourcePositionProperty> sourcePositionProperties, byte[] pdfFileByte) {
        List<RealPositionProperty> realPositionProperties = new ArrayList<>();


        try {
            PDDocument document = Loader.loadPDF(pdfFileByte);
            if (document == null) {
                throw new PaasException("pdf文件解析失败");
            }
            //将pdf文件读入PdfReader工具类
            for (SourcePositionProperty sourcePositionProperty : sourcePositionProperties) {
                RealPositionProperty realPositionProperty = new RealPositionProperty();
                PDPage page = document.getPage(sourcePositionProperty.getPage());
                if (page == null) {
                    throw new PaasException("pdf文件解析失败，无页码");
                }
                PDRectangle mediaBox = page.getMediaBox();
                if (mediaBox == null) {
                    throw new PaasException("pdf文件解析失败");
                }

                //获取真实pdf文件指定页的真实文档宽高
                float realPdfHeight = mediaBox.getHeight();
                float realPdfWidth = mediaBox.getWidth();
                if (page.getRotation() == 90 || page.getRotation() == 270) {
                    realPdfWidth = mediaBox.getHeight();
                    realPdfHeight = mediaBox.getWidth();
                }

                Float startX = sourcePositionProperty.getOffsetX();
                Float endX = sourcePositionProperty.getOffsetX() + sourcePositionProperty.getWidth();

                //由于页面的纵坐标和pdf的纵坐标是相反的，所以真实的pdf的纵坐标在计算的时候需要再反转一下
                float startY = realPdfHeight - sourcePositionProperty.getOffsetY() - sourcePositionProperty.getHeight();
                float endY = realPdfHeight - sourcePositionProperty.getOffsetY();


//                Float startY = sourcePositionProperty.getOffsetY() - sourcePositionProperty.getHeight();
//                Float endY = sourcePositionProperty.getOffsetY();
//                //由于页面的纵坐标和pdf的纵坐标是相反的，所以真实的pdf的纵坐标在计算的时候需要再反转一下
//                startY = realPdfHeight - startY ;
//                endY = realPdfHeight - endY ;

                //封装返回数据
                realPositionProperty.setStartx(startX);
                realPositionProperty.setStarty(startY);
                realPositionProperty.setEndx(endX);
                realPositionProperty.setEndy(endY);
                realPositionProperty.setPageNum(sourcePositionProperty.getPage());
                if (sourcePositionProperty.getFontSize() != null) {
                    realPositionProperty.setFontSize(sourcePositionProperty.getFontSize() + "");
                }


                realPositionProperty.setValue(sourcePositionProperty.getValue());
                realPositionProperty.setAlign(sourcePositionProperty.getAlign());
                realPositionProperty.setFontFamily(sourcePositionProperty.getFontFamily());


                realPositionProperty.setRealPdfWidth(realPdfWidth);
                realPositionProperty.setRealPdfHeight(realPdfHeight);

                realPositionProperty.setControlType(sourcePositionProperty.getControlType());
                //填写控件内部属性
                if (sourcePositionProperty.getWidgetPropertyList() != null) {
                    List<ControlWidgetProperty> realWidgetPropertyList = new ArrayList<>();
                    for (ControlWidgetProperty sourceControlWidgetProperty : sourcePositionProperty.getWidgetPropertyList()) {

                        ControlWidgetProperty realControlWidgetProperty = new ControlWidgetProperty();
                        BeanUtils.copyProperties(sourceControlWidgetProperty, realControlWidgetProperty);

                        if (realControlWidgetProperty.getY() != null && realControlWidgetProperty.getH() != null) {
                            Integer y = realControlWidgetProperty.getY();
                            Integer h = realControlWidgetProperty.getH();
                            Integer newY = (int) realPdfHeight - y - h;
                            realControlWidgetProperty.setY(newY);
                        }
                        realWidgetPropertyList.add(realControlWidgetProperty);
                    }


                    realPositionProperty.setWidgetPropertyList(realWidgetPropertyList);
                }


                realPositionProperties.add(realPositionProperty);
            }
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return realPositionProperties;
    }


//    /**
//     * @Description #批量计算真实签署位置
//     * @Param [sourcePositionProperties]
//     * @return java.util.List<com.kaifangqian.modules.sign.service.tool.pojo.RealPositionProperty>
//     **/
//    public List<RealPositionProperty> calculatePositions(List<SourcePositionProperty> sourcePositionProperties, byte[] pdfFileByte){
//        List<RealPositionProperty> realPositionProperties = new ArrayList<>();
//
//
//        PdfReader reader = null ;
//        try {
//            //将pdf文件读入PdfReader工具类
//            reader = new PdfReader(pdfFileByte);
//            for(SourcePositionProperty sourcePositionProperty : sourcePositionProperties){
//                RealPositionProperty realPositionProperty = calculatePosition(sourcePositionProperty,pdfFileByte);
//                Document document = new Document(reader.getPageSize(sourcePositionProperty.getPage()));
//                //获取真实pdf文件指定页的真实文档宽高
//                float realPdfHeight = document.getPageSize().getHeight();
//                float realPdfWidth = document.getPageSize().getWidth();
//                //获取页面上文档的宽高
//                float sourcePageWidth = sourcePositionProperty.getPageWidth();
//                float sourcePageHeight = sourcePositionProperty.getPageHeight();
//                //计算真实文档的宽高和页面文档的宽高的比率
//                float rateHeight = realPdfHeight / sourcePageHeight;
//                float rateWidth = realPdfWidth / sourcePageWidth;
//                //计算页面上的横纵坐标,由于页面上给出的是左上角的坐标，所以需要再转换计算一下
//                //左下角
//                float pageStartX = sourcePositionProperty.getOffsetX();
//                float pageStartY = sourcePositionProperty.getOffsetY() + sourcePositionProperty.getHeight() ;
//                //右上角
//                float pageEndX = sourcePositionProperty.getOffsetX() + sourcePositionProperty.getWidth();
//                float pageEndY = sourcePositionProperty.getOffsetY();
//                //根据比率去计算真实文档上的坐标位置
//                float startX = pageStartX * rateWidth ;
//                float startY = pageStartY * rateHeight;
//                float endX = pageEndX * rateWidth ;
//                float endY = pageEndY * rateHeight ;
//                //由于页面的纵坐标和pdf的纵坐标是相反的，所以真实的pdf的纵坐标在计算的时候需要再反转一下
//                startY = realPdfHeight - startY ;
//                endY = realPdfHeight - endY ;
//                //封装返回数据
//                realPositionProperty.setStartx(startX);
//                realPositionProperty.setStarty(startY);
//                realPositionProperty.setEndx(endX);
//                realPositionProperty.setEndy(endY);
//                realPositionProperty.setPageNum(sourcePositionProperty.getPage());
//
//
//                realPositionProperty.setValue(sourcePositionProperty.getValue());
//                realPositionProperty.setAlign(sourcePositionProperty.getAlign());
//                realPositionProperty.setFontFamily(sourcePositionProperty.getFontFamily());
//                realPositionProperty.setFontSize(sourcePositionProperty.getFontSize());
//
//                document.close();
//                realPositionProperties.add(realPositionProperty);
//            }
//
//            reader.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return realPositionProperties ;
//    }
//
//
//
//    /**
//     * @Description #单独计算真实签署位置
//     * @Param [sourcePositionProperty]
//     * @return com.kaifangqian.modules.sign.service.tool.pojo.RealPositionProperty
//     **/
//    public RealPositionProperty calculatePosition(SourcePositionProperty sourcePositionProperty, byte[] pdfFileByte){
//        RealPositionProperty realPositionProperty = new RealPositionProperty();
//        PdfReader reader = null ;
//        Document document = null ;
//        try {
//            //将pdf文件读入PdfReader工具类
//            reader = new PdfReader(pdfFileByte);
//            document = new Document(reader.getPageSize(sourcePositionProperty.getPage()));
//            //获取真实pdf文件指定页的真实文档宽高
//            float realPdfHeight = document.getPageSize().getHeight();
//            float realPdfWidth = document.getPageSize().getWidth();
//            //获取页面上文档的宽高
//            float sourcePageWidth = sourcePositionProperty.getPageWidth();
//            float sourcePageHeight = sourcePositionProperty.getPageHeight();
//            //计算真实文档的宽高和页面文档的宽高的比率
//            float rateHeight = realPdfHeight / sourcePageHeight;
//            float rateWidth = realPdfWidth / sourcePageWidth;
//            //计算页面上的横纵坐标,由于页面上给出的是左上角的坐标，所以需要再转换计算一下
//            //左下角
//            float pageStartX = sourcePositionProperty.getOffsetX();
//            float pageStartY = sourcePositionProperty.getOffsetY() + sourcePositionProperty.getHeight() ;
//            //右上角
//            float pageEndX = sourcePositionProperty.getOffsetX() + sourcePositionProperty.getWidth();
//            float pageEndY = sourcePositionProperty.getOffsetY();
//            //根据比率去计算真实文档上的坐标位置
//            float startX = pageStartX * rateWidth ;
//            float startY = pageStartY * rateHeight;
//            float endX = pageEndX * rateWidth ;
//            float endY = pageEndY * rateHeight ;
//            //由于页面的纵坐标和pdf的纵坐标是相反的，所以真实的pdf的纵坐标在计算的时候需要再反转一下
//            startY = realPdfHeight - startY ;
//            endY = realPdfHeight - endY ;
//            //封装返回数据
//            realPositionProperty.setStartx(startX);
//            realPositionProperty.setStarty(startY);
//            realPositionProperty.setEndx(endX);
//            realPositionProperty.setEndy(endY);
//            realPositionProperty.setPageNum(sourcePositionProperty.getPage());
//
//            document.close();
//            reader.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        log.info("====================================================================================================================");
//        log.info("原始页面坐标：{" + JSON.toJSONString(sourcePositionProperty) + "}    ,   真实文件坐标：{" + JSON.toJSONString(realPositionProperty) + "}");
//        log.info("====================================================================================================================");
//        return realPositionProperty ;
//    }
//
//
//    public RealPositionProperty calculatePosition(SourcePositionProperty sourcePositionProperty){
//        RealPositionProperty realPositionProperty = new RealPositionProperty();
//        //获取真实pdf文件指定页的真实文档宽高
//        float realPdfHeight = sourcePositionProperty.getRealHeight();
//        float realPdfWidth = sourcePositionProperty.getRealWidth();
//        //获取页面上文档的宽高
//        float sourcePageWidth = sourcePositionProperty.getPageWidth();
//        float sourcePageHeight = sourcePositionProperty.getPageHeight();
//        //计算真实文档的宽高和页面文档的宽高的比率
//        float rateHeight = realPdfHeight / sourcePageHeight;
//        float rateWidth = realPdfWidth / sourcePageWidth;
//        //计算页面上的横纵坐标,由于页面上给出的是左上角的坐标，所以需要再转换计算一下
//        //左下角
//        float pageStartX = sourcePositionProperty.getOffsetX();
//        float pageStartY = sourcePositionProperty.getOffsetY() + sourcePositionProperty.getHeight() ;
//        //右上角
//        float pageEndX = sourcePositionProperty.getOffsetX() + sourcePositionProperty.getWidth();
//        float pageEndY = sourcePositionProperty.getOffsetY();
//        //根据比率去计算真实文档上的坐标位置
//        float startX = pageStartX * rateWidth ;
//        float startY = pageStartY * rateHeight;
//        float endX = pageEndX * rateWidth ;
//        float endY = pageEndY * rateHeight ;
//        //由于页面的纵坐标和pdf的纵坐标是相反的，所以真实的pdf的纵坐标在计算的时候需要再反转一下
//        startY = realPdfHeight - startY ;
//        endY = realPdfHeight - endY ;
//        //封装返回数据
//        realPositionProperty.setStartx(startX);
//        realPositionProperty.setStarty(startY);
//        realPositionProperty.setEndx(endX);
//        realPositionProperty.setEndy(endY);
//        realPositionProperty.setPageNum(sourcePositionProperty.getPage());
//        realPositionProperty.setValue(sourcePositionProperty.getValue());
//        realPositionProperty.setAlign(sourcePositionProperty.getAlign());
//        realPositionProperty.setFontFamily(sourcePositionProperty.getFontFamily());
//        realPositionProperty.setFontSize(sourcePositionProperty.getFontSize());
//        return realPositionProperty ;
//    }


}