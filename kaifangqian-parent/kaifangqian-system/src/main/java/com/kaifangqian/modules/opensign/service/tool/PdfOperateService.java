//package com.kaifangqian.modules.opensign.service.tool;
//
//import com.itextpdf.text.*;
//import com.itextpdf.text.pdf.*;
//import com.kaifangqian.config.FileProperties;
//import com.kaifangqian.modules.opensign.entity.SignDocControl;
//import com.kaifangqian.modules.opensign.enums.ControlAlignment;
//import com.kaifangqian.modules.opensign.service.tool.pojo.RealPositionProperty;
//import com.kaifangqian.modules.opensign.service.tool.pojo.SourcePositionProperty;
//import org.apache.commons.io.IOUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.io.*;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
///**
// * @Description: pdf文件操作类
// * @Package: com.kaifangqian.modules.sign.service.tool
// * @ClassName: PdfOperateService
// * @author: FengLai_Gong
// */
//@Service
//public class PdfOperateService {
//
//
//    @Autowired
//    private CalculatePositionService calculatePositionService;
//
////    @Value("${fontPath}")
////    private String fontPath;
//    @Autowired
//    private FileProperties fileProperties;
//
//
//    public static void main(String[] args) {
////        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM曰dd日");
////        String format = simpleDateFormat.format(new Date());
////        System.out.println(format);
//        List<RealPositionProperty> realPositionProperties = new ArrayList<>();
//        for(int i = 1 ; i < 4 ; i++){
//            RealPositionProperty positionProperty = new RealPositionProperty();
//            positionProperty.setStartx((i * 50) + 100f);
//            positionProperty.setStarty((i * 50) + 500f);
//            positionProperty.setEndx((i * 50) + 200f);
//            positionProperty.setEndy((i * 50) + 700f);
//            positionProperty.setPageNum(1);
//            realPositionProperties.add(positionProperty);
//        }
//
//
//
//        try {
//            byte[] imageFile = IOUtils.toByteArray(new FileInputStream(new File("/Users/gongfenglai/Desktop/test/ent_ccccc.png")));
//            byte[] pdfFile = IOUtils.toByteArray(new FileInputStream(new File("/Users/gongfenglai/Desktop/test/testContract01.pdf")));
//            byte[] newPdfFile = insertImage(pdfFile, imageFile, realPositionProperties);
//            IOUtils.write(newPdfFile,new FileOutputStream(new File("/Users/gongfenglai/Desktop/test/11-27-02.pdf")));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//
//
//
//    }
//
//    public static byte[] insertImage(byte[] pdfFile, byte[] imageFile, List<RealPositionProperty> realPositionProperties){
//        byte[] newPdfFile = null ;
//        try {
//            // 读取本地模板文件
//            ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfFile);
//            PdfReader reader = new PdfReader(inputStream);
//            // 合成后的文件
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            PdfStamper stamper = new PdfStamper(reader, outputStream);
//            for(RealPositionProperty realPositionProperty : realPositionProperties){
//                // 获取要操作的指定页面，也可以通过获取pdf文件的总页数，遍历出来操作每一个页面
//                PdfContentByte over = stamper.getOverContent(realPositionProperty.getPageNum());
//                // 读取待插入的图片
//                Image contractSealImg = Image.getInstance(imageFile);
//                // 这里也可以插入一张空白图片当成是要插入图片的背景，注意：位置和大小需要与待插入的图片一致
//                over.saveState();
//                PdfGState pdfGState = new PdfGState();
//                pdfGState.setFillOpacity(0.9F); // 给图片设置透明度
//                over.setGState(pdfGState);
//                // 设置图片位置
//                contractSealImg.setAbsolutePosition(realPositionProperty.getStartx(), realPositionProperty.getStarty());
//                // 设置图片大小
//                contractSealImg.scaleAbsolute(
//                        realPositionProperty.getEndx() - realPositionProperty.getStartx(),
//                        realPositionProperty.getEndy() - realPositionProperty.getStarty());
//
//                over.addImage(contractSealImg); // 将图片添加到pdf文件
//                over.restoreState();
//            }
//
//            stamper.setFormFlattening(true);
//            stamper.close();
//            reader.close();
//            //返回文件数据
//            newPdfFile = outputStream.toByteArray();
//            outputStream.close();
//            inputStream.close();
//            System.out.println("success！");
//        }catch (Exception e){
//            e.printStackTrace();
//
//        }
//        return newPdfFile ;
//    }
//
//    public byte[] insertImage(byte[] pdfFile, byte[] imageFile, Float offsetX, Float offsetY, Integer pageNum){
//        byte[] imageByte = null ;
//        try {
//            // 读取本地模板文件
//            ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfFile);
//            PdfReader reader = new PdfReader(inputStream);
//            // 合成后的文件
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            PdfStamper stamper = new PdfStamper(reader, outputStream);
//            // 获取要操作的指定页面，也可以通过获取pdf文件的总页数，遍历出来操作每一个页面
//            PdfContentByte over = stamper.getOverContent(pageNum);
//            // 读取待插入的图片
//            Image contractSealImg = Image.getInstance(imageFile);
//            // 这里也可以插入一张空白图片当成是要插入图片的背景，注意：位置和大小需要与待插入的图片一致
//            over.saveState();
//            PdfGState pdfGState = new PdfGState();
//            pdfGState.setFillOpacity(0.9F); // 给图片设置透明度
//            over.setGState(pdfGState);
////            contractSealImg.setAbsolutePosition(225, 158); // 设置图片位置
////            contractSealImg.scaleAbsolute(70, 70); // 设置图片大小
//
//            contractSealImg.setAbsolutePosition(offsetX, offsetY); // 设置图片位置
//            contractSealImg.scaleAbsolute(70, 70); // 设置图片大小
//
//            over.addImage(contractSealImg); // 将图片添加到pdf文件
//            over.restoreState();
//            stamper.setFormFlattening(true);
//            stamper.close();
//            reader.close();
//            //返回文件数据
//            imageByte = outputStream.toByteArray();
//            outputStream.close();
//            inputStream.close();
//            System.out.println("success！");
//        }catch (Exception e){
//
//        }
//        return imageByte ;
//    }
//
//
//
//    public byte[] insertContextByPosition(byte[] pdfFile, String value, RealPositionProperty realPositionProperty){
//        byte[] newFileByte = null ;
//        PdfReader reader = null ;
//        ByteArrayOutputStream outputStream = null ;
//        PdfStamper stamper = null ;
//        try {
//            // 读取本地模板文件
//            reader = new PdfReader(pdfFile);
//            // 合成后的文件
//            outputStream = new ByteArrayOutputStream();
//            stamper = new PdfStamper(reader, outputStream);
//            // 获取要操作的指定页面，也可以通过获取pdf文件的总页数，遍历出来操作每一个页面
//            PdfContentByte over = stamper.getOverContent(realPositionProperty.getPageNum());
//            // 这里也可以插入一张空白图片当成是要插入图片的背景，注意：位置和大小需要与待插入的图片一致
////            over.saveState();
//            ColumnText columnText = new ColumnText(over);
//            // llx 左侧边框距离 lly下侧边框距离 urx 右侧边框距离 ury 上测边框距离
//            columnText.setSimpleColumn(realPositionProperty.getStartx(), realPositionProperty.getStarty(), realPositionProperty.getEndx(), realPositionProperty.getEndy());
//            Paragraph elements = new Paragraph(value);
//
//            // baseFont不支持字体样式设定.但是font字体要求操作系统支持此字体会带来移植问题.
//            // 这个字体是itext-asian.jar中自带的 所以不用考虑操作系统环境问题.
//            BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//            Font font = new Font(bf, 10f, Font.NORMAL, BaseColor.BLACK);
//            elements.setFont(font);
//            columnText.addElement(elements);
//            columnText.go();
//            //返回文件数据
//            newFileByte = outputStream.toByteArray();
//            //关闭流
//            stamper.close();
//            reader.close();
//            outputStream.close();
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return newFileByte ;
//    }
//
//    public byte[] insertContextByRealPositions(byte[] pdfFile, List<RealPositionProperty> realPositionProperties){
//
//        byte[] pdfFileByte = null ;
//
//        PdfReader reader = null ;
//        ByteArrayOutputStream outputStream = null ;
//        PdfStamper stamper = null ;
//        ByteArrayInputStream inputStream = null ;
//        try {
//
//            // 读取本地模板文件
//            inputStream = new ByteArrayInputStream(pdfFile);
//            reader = new PdfReader(inputStream);
//            // 合成后的文件
//            outputStream = new ByteArrayOutputStream();
//            stamper = new PdfStamper(reader, outputStream);
//
//            for(RealPositionProperty realPositionProperty : realPositionProperties){
//                // 获取要操作的指定页面，也可以通过获取pdf文件的总页数，遍历出来操作每一个页面
//                PdfContentByte over = stamper.getOverContent(realPositionProperty.getPageNum());
//                // 这里也可以插入一张空白图片当成是要插入图片的背景，注意：位置和大小需要与待插入的图片一致
////                over.saveState();
//
//                // 这个字体是itext-asian.jar中自带的 所以不用考虑操作系统环境问题.
////                BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//                BaseFont bf = null ;
////                String path = this.getClass().getResource("/").getPath() ;
////                System.out.println("项目路径---------------------" + path);
////                if("宋体".equals(realPositionProperty.getFontFamily())){
////                    bf = BaseFont.createFont(path + "font/STSONG.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
////                }else if("楷体".equals(realPositionProperty.getFontFamily())){
////                    bf = BaseFont.createFont(path + "font/SIMSUNB.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
////                }else if("微软雅黑".equals(realPositionProperty.getFontFamily())){
////                    bf = BaseFont.createFont(path + "font/SIMSUNB.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
////                }else {
////                    bf = BaseFont.createFont(path + "font/STSONG.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
////                }
//                if("宋体".equals(realPositionProperty.getFontFamily())){
//                    bf = BaseFont.createFont(fileProperties.getPath().getPath() + "STSONG.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//                }else if("楷体".equals(realPositionProperty.getFontFamily())){
//                    bf = BaseFont.createFont(fileProperties.getPath().getPath() + "SIMSUNB.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//                }else if("微软雅黑".equals(realPositionProperty.getFontFamily())){
//                    bf = BaseFont.createFont(fileProperties.getPath().getPath() + "SIMSUNB.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//                }else {
//                    bf = BaseFont.createFont(fileProperties.getPath().getPath() + "STSONG.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//                }
//
//
//                // baseFont不支持字体样式设定.但是font字体要求操作系统支持此字体会带来移植问题.
//                Font font = new Font(bf, realPositionProperty.getFontSize());
//                font.setColor(BaseColor.BLACK);
//                // 开始写入文本
//                over.beginText();
//                // 设置字体和大小
//                over.setFontAndSize(font.getBaseFont(), font.getSize());
//                // 设置字体颜色
//                over.setColorFill(font.getColor());
//                // 要输出的text 对齐方式 写的字 设置字体的输出位置 字体是否旋转
////                over.showTextAligned(0, realPositionProperty.getValue(), realPositionProperty.getStartx(), realPositionProperty.getStarty() , 0);
////                over.rectangle(realPositionProperty.getStartx(),realPositionProperty.getStarty(),
////                        realPositionProperty.getEndx() - realPositionProperty.getStartx(),realPositionProperty.getEndy() - realPositionProperty.getStarty());
//                ColumnText ct = new ColumnText(over);
//                ct.setSimpleColumn(new Rectangle(realPositionProperty.getStartx(),realPositionProperty.getStarty(),
//                        realPositionProperty.getEndx(),realPositionProperty.getEndy()));
//                //创建段落
//                Paragraph element = new Paragraph();
//                //设置段落字体
//                Chunk chunk = new Chunk(realPositionProperty.getValue(), font);
//                System.out.println("value+++++++++++++++++++++++++" + realPositionProperty.getValue());
//                element.add(chunk);
//                if(realPositionProperty.getAlign().equals(ControlAlignment.ALIGN_LEFT.getName())){
//                    element.setAlignment(Paragraph.ALIGN_LEFT);
//                }else if(realPositionProperty.getAlign().equals(ControlAlignment.ALIGN_RIGHT.getName())){
//                    element.setAlignment(Paragraph.ALIGN_RIGHT);
//                }else if(realPositionProperty.getAlign().equals(ControlAlignment.ALIGN_CENTER.getName())){
//                    element.setAlignment(Paragraph.ALIGN_CENTER);
//                }
//                ct.addElement(element);
//                ct.go();
//                over.endText();
//            }
//
//            stamper.setFormFlattening(true);
//            stamper.close();
//            reader.close();
//            //返回文件数据
//            pdfFileByte = outputStream.toByteArray();
//            //关闭流
//            inputStream.close();
//            outputStream.close();
//            System.out.println("success！");
//        }catch (Exception e){
//            String message = e.getMessage();
//            System.out.println("+++++++++++++++++++++++++" + message);
//            e.printStackTrace();
//        }
//        return pdfFileByte ;
//    }
//
//    public byte[] insertContextByPositions(byte[] pdfFile, List<SignDocControl> signDocControls){
//        byte[] pdfFileByte = null ;
//
//        PdfReader reader = null ;
//        ByteArrayOutputStream outputStream = null ;
//        PdfStamper stamper = null ;
//        ByteArrayInputStream inputStream = null ;
//        try {
//            // 读取本地模板文件
//            inputStream = new ByteArrayInputStream(pdfFile);
//            reader = new PdfReader(inputStream);
//            List<RealPositionProperty> realPositionProperties = new ArrayList<>();
//            for(SignDocControl signDocControl : signDocControls) {
//                //创建签署位置属性对象列表
//                SourcePositionProperty sourcePositionProperty = new SourcePositionProperty();
//                sourcePositionProperty.setOffsetX(Float.valueOf(signDocControl.getOffsetX()));
//                sourcePositionProperty.setOffsetY(Float.valueOf(signDocControl.getOffsetY()));
//                sourcePositionProperty.setHeight(Float.valueOf(signDocControl.getHeight()));
//                sourcePositionProperty.setWidth(Float.valueOf(signDocControl.getWidth()));
//                sourcePositionProperty.setPageHeight(Float.valueOf(signDocControl.getPageHeight()));
//                sourcePositionProperty.setPageWidth(Float.valueOf(signDocControl.getPageWidth()));
//                sourcePositionProperty.setPage(signDocControl.getPage() + 1);
//                Document document = new Document(reader.getPageSize(signDocControl.getPage() + 1));
//                sourcePositionProperty.setRealHeight(document.getPageSize().getHeight());
//                sourcePositionProperty.setRealWidth(document.getPageSize().getWidth());
//                sourcePositionProperty.setValue(signDocControl.getValue());
//                sourcePositionProperty.setAlign(signDocControl.getTextAlign());
//                sourcePositionProperty.setFontFamily(signDocControl.getFontFamily());
//                sourcePositionProperty.setFontSize(signDocControl.getFontSize());
//                RealPositionProperty realPositionProperty = calculatePositionService.calculatePosition(sourcePositionProperty);
//                realPositionProperties.add(realPositionProperty);
//            }
//            // 合成后的文件
//            outputStream = new ByteArrayOutputStream();
//            stamper = new PdfStamper(reader, outputStream);
//
//            for(RealPositionProperty realPositionProperty : realPositionProperties){
//                // 获取要操作的指定页面，也可以通过获取pdf文件的总页数，遍历出来操作每一个页面
//                PdfContentByte over = stamper.getOverContent(realPositionProperty.getPageNum());
//                // 这里也可以插入一张空白图片当成是要插入图片的背景，注意：位置和大小需要与待插入的图片一致
////                over.saveState();
//
//                // 这个字体是itext-asian.jar中自带的 所以不用考虑操作系统环境问题.
////                BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//                BaseFont bf = null ;
////                String path = this.getClass().getResource("/").getPath();
////                String path = "/resrun/paas-sign2/";
//                if("宋体".equals(realPositionProperty.getFontFamily())){
//                    bf = BaseFont.createFont(fileProperties.getPath().getPath() + "STSONG.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//                }else if("楷体".equals(realPositionProperty.getFontFamily())){
//                    bf = BaseFont.createFont(fileProperties.getPath().getPath() + "SIMSUNB.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//                }else if("微软雅黑".equals(realPositionProperty.getFontFamily())){
//                    bf = BaseFont.createFont(fileProperties.getPath().getPath() + "SIMSUNB.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//                }else {
//                    bf = BaseFont.createFont(fileProperties.getPath().getPath() + "STSONG.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//                }
//
//
//                // baseFont不支持字体样式设定.但是font字体要求操作系统支持此字体会带来移植问题.
//                Font font = new Font(bf, realPositionProperty.getFontSize());
//                font.setColor(BaseColor.BLACK);
//                // 开始写入文本
//                over.beginText();
//                // 设置字体和大小
//                over.setFontAndSize(font.getBaseFont(), font.getSize());
//                // 设置字体颜色
//                over.setColorFill(font.getColor());
//                // 要输出的text 对齐方式 写的字 设置字体的输出位置 字体是否旋转
////                over.showTextAligned(0, realPositionProperty.getValue(), realPositionProperty.getStartx(), realPositionProperty.getStarty() , 0);
////                over.rectangle(realPositionProperty.getStartx(),realPositionProperty.getStarty(),
////                        realPositionProperty.getEndx() - realPositionProperty.getStartx(),realPositionProperty.getEndy() - realPositionProperty.getStarty());
//                ColumnText ct = new ColumnText(over);
//                ct.setSimpleColumn(new Rectangle(realPositionProperty.getStartx(),realPositionProperty.getStarty(),
//                        realPositionProperty.getEndx(),realPositionProperty.getEndy()));
//                //创建段落
//                Paragraph element = new Paragraph();
//                //设置段落字体
//                Chunk chunk = new Chunk(realPositionProperty.getValue(), font);
//                element.add(chunk);
//                if(realPositionProperty.getAlign().equals(ControlAlignment.ALIGN_LEFT.getName())){
//                    element.setAlignment(Paragraph.ALIGN_LEFT);
//                }else if(realPositionProperty.getAlign().equals(ControlAlignment.ALIGN_RIGHT.getName())){
//                    element.setAlignment(Paragraph.ALIGN_RIGHT);
//                }else if(realPositionProperty.getAlign().equals(ControlAlignment.ALIGN_CENTER.getName())){
//                    element.setAlignment(Paragraph.ALIGN_CENTER);
//                }
//                ct.addElement(element);
//                ct.go();
//                over.endText();
//            }
//
//            stamper.setFormFlattening(true);
//            stamper.close();
//            reader.close();
//            //返回文件数据
//            pdfFileByte = outputStream.toByteArray();
//            //关闭流
//            inputStream.close();
//            outputStream.close();
//            System.out.println("success！");
//        }catch (Exception e){
//            String message = e.getMessage();
//            System.out.println("+++++++++++++++++++++++++" + message);
//            e.printStackTrace();
//        }
//        return pdfFileByte ;
//    }
//
//    public byte[] insertContext(byte[] pdfFile,String value,Float offsetX,Float offsetY, Integer pageNum){
//        byte[] pdfFileByte = null ;
//
//        PdfReader reader = null ;
//        ByteArrayOutputStream outputStream = null ;
//        PdfStamper stamper = null ;
//        ByteArrayInputStream inputStream = null ;
//        try {
//            // 读取本地模板文件
//            inputStream = new ByteArrayInputStream(pdfFile);
//            reader = new PdfReader(inputStream);
//            // 合成后的文件
//            outputStream = new ByteArrayOutputStream();
//            stamper = new PdfStamper(reader, outputStream);
//            // 获取要操作的指定页面，也可以通过获取pdf文件的总页数，遍历出来操作每一个页面
//            PdfContentByte over = stamper.getOverContent(pageNum);
//            // 这里也可以插入一张空白图片当成是要插入图片的背景，注意：位置和大小需要与待插入的图片一致
//            over.saveState();
//
//            // 这个字体是itext-asian.jar中自带的 所以不用考虑操作系统环境问题.
//            BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//            // baseFont不支持字体样式设定.但是font字体要求操作系统支持此字体会带来移植问题.
//            Font font = new Font(bf, 10);
//            font.setStyle(Font.BOLD);
//            font.getBaseFont();
//
//            // 开始写入文本
//            over.beginText();
//            // 设置字体和大小
//            over.setFontAndSize(font.getBaseFont(), 8);
//            // 设置字体颜色
//            over.setColorFill(BaseColor.BLACK);
//            // 要输出的text 对齐方式 写的字 设置字体的输出位置 字体是否旋转
//            over.showTextAligned(0, value, offsetX, offsetY, 0);
//            over.endText();
//            over.restoreState();
//            stamper.setFormFlattening(true);
//            stamper.close();
//            reader.close();
//            //返回文件数据
//            pdfFileByte = outputStream.toByteArray();
//            //关闭流
//            inputStream.close();
//            outputStream.close();
//            System.out.println("success！");
//        }catch (Exception e){
//            String message = e.getMessage();
//            System.out.println("+++++++++++++++++++++++++" + message);
//            e.printStackTrace();
//        }
//        return pdfFileByte ;
//    }
//
//
//    /*
//     * @description: 将字符串转换为BufferedImage对象
//     * @param: [strs]
//     * @return: java.awt.image.BufferedImage
//     * @author: yanchengzhi
//     * @date: 2021/6/22 21:20
//     */
////    public static BufferedImage createImage(String[] strs,) {
////        // 设置背景宽高
////        int width = 200, height = 130;
////        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
////        // 获取图形上下文对象
////        Graphics graphics = image.getGraphics();
////        // 填充
////        graphics.fillRect(0, 0, width, height);
////        // 设定字体大小及样式
////        graphics.setFont(new java.awt.Font("宋体", java.awt.Font.BOLD,20));
////        // 字体颜色
////        graphics.setColor(Color.BLACK);
////        for (int i = 0; i < strs.length; i++) {
////            // 描绘字符串
////            graphics.drawString(strs[i], 35,  70);
////        }
////        graphics.dispose();
////        return image;
////    }
//
//
//    public byte[] insertContextBefore(byte[] pdfFile,String value,Float offsetX,Float offsetY, Integer pageNum){
//        byte[] pdfFileByte = null ;
//        try {
//            // 读取本地模板文件
//            ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfFile);
//            PdfReader reader = new PdfReader(inputStream);
//            // 合成后的文件
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            PdfStamper stamper = new PdfStamper(reader, outputStream);
//            // 获取要操作的指定页面，也可以通过获取pdf文件的总页数，遍历出来操作每一个页面
//            PdfContentByte over = stamper.getOverContent(pageNum);
//            // 这里也可以插入一张空白图片当成是要插入图片的背景，注意：位置和大小需要与待插入的图片一致
////		Image backgroundPic = Image.getInstance("G:\\test\\空白图片.jpg");
//            over.saveState();
//
//            // 这个字体是itext-asian.jar中自带的 所以不用考虑操作系统环境问题.
//            BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//            // baseFont不支持字体样式设定.但是font字体要求操作系统支持此字体会带来移植问题.
//            Font font = new Font(bf, 10);
//            font.setStyle(Font.BOLD);
//            font.getBaseFont();
//
//            // 开始写入文本
//            over.beginText();
//            // 设置字体和大小
//            over.setFontAndSize(font.getBaseFont(), 8);
//            // 设置字体颜色
//            over.setColorFill(BaseColor.BLACK);
//            // 要输出的text 对齐方式 写的字 设置字体的输出位置 字体是否旋转
//            over.showTextAligned(0, value, offsetX, offsetY, 0);
//            over.endText();
//            over.restoreState();
//            stamper.setFormFlattening(true);
//            stamper.close();
//            reader.close();
//            //返回文件数据
//            pdfFileByte = outputStream.toByteArray();
//            outputStream.close();
//            inputStream.close();
//            System.out.println("success！");
//        }catch (Exception e){
//            String message = e.getMessage();
//            System.out.println("+++++++++++++++++++++++++" + message);
//            e.printStackTrace();
//        }
//        return pdfFileByte ;
//    }
//
//
//
//
//}