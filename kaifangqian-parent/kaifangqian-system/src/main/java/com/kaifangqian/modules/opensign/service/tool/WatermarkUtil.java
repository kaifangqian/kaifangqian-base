/**
 * @description PDF水印工具类
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

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.resizers.configurations.ScalingMode;
import org.apache.commons.io.IOUtils;

import javax.imageio.*;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * @Description: WatermarkUtil
 * @Package: com.kaifangqian.modules.opensign.service.tool
 * @ClassName: WatermarkUtil
 * @author: FengLai_Gong
 */
@Slf4j
public class WatermarkUtil {


    public static void main(String[] args) {
        try {
            byte[] bytes = IOUtils.toByteArray(new FileInputStream(new File("/Users/kaifangqian/test/personal0826.jpg")));
//            byte[] bytes = IOUtils.toByteArray(new FileInputStream(new File("/Users/kaifangqian/file/zyld.png")));
//            byte[] bytes = IOUtils.toByteArray(new FileInputStream(new File("/Users/kaifangqian/test/zyld_water10.jpg")));

            byte[] watermark = watermark(bytes, "仅用于签署记录报告","/Users/kaifangqian/file/simsun.ttf");
//            byte[] watermark = watermark(bytes, "仅用于签署记录报告",45);
//            byte[] watermark = test(bytes, "仅用于签署记录报告");
//            byte[] watermark = watermark(bytes, "仅用于签署记录报告",100,10);

//            byte[] resize = resize(bytes,100);
//            byte[] out = resize2(bytes,100);


//            byte[] watermark = watermark(resize, "仅用于签署记录报告",10);


//            IOUtils.write(watermark,new FileOutputStream(new File("/Users/kaifangqian/test/zyld_water32.png")));


//            byte[] transToJPG = transToJPG(bytes);
//            IOUtils.write(transToJPG,new FileOutputStream(new File("/Users/kaifangqian/test/trans02.jpg")));

//            byte[] resize = resize5(bytes, 100);
//            resizeImage("/Users/kaifangqian/file/zyld.png","/Users/kaifangqian/test/zyld_water54.jpg", 100,100);
            IOUtils.write(watermark,new FileOutputStream(new File("/Users/kaifangqian/test/0826——0004.jpg")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static byte[] resizeAndWaterMark(byte[] imageByte,Integer newImageWidth,String context,Integer fontSize){
        byte[] resize = resize2(imageByte, newImageWidth);
        byte[] watermark = watermark(resize, context,fontSize);

        return watermark ;
    }


    public static byte[] resize4(byte[] imageByte,Integer newImageWidth){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            // 读取图片
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageByte));

            int height = originalImage.getHeight();
            int width = originalImage.getWidth();
            int newImageHeight = new BigDecimal(height).divide(new BigDecimal(width),2, RoundingMode.HALF_UP).multiply(new BigDecimal(newImageWidth)).intValue();

//            Image scaledImage = originalImage.getScaledInstance(newImageWidth, newImageHeight, BufferedImage.SCALE_SMOOTH);

            // 创建一个新的BufferedImage，设置类型和尺寸
            BufferedImage resizedImage = new BufferedImage(newImageWidth, newImageHeight, BufferedImage.TYPE_INT_RGB);
//            Graphics2D graphics = resizedImage.createGraphics();
//            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            // 绘制并改变图片尺寸
//            graphics.drawImage(originalImage, 0, 0,newImageWidth,newImageHeight,null);


            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);

            ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("jpg").next();
            imageWriter.setOutput(imageOutputStream);

            ImageWriteParam writeParam = imageWriter.getDefaultWriteParam();

            writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            writeParam.setCompressionQuality(0.9f);

            java.util.List<BufferedImage> list = new ArrayList<>();
            list.add(resizedImage);
            // 写入新的图片文件
            imageWriter.write(null,new IIOImage(originalImage,list,null),writeParam);

            imageOutputStream.close();
            imageWriter.dispose();

            System.out.println("图片尺寸已调整。");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray() ;


    }



    public static byte[] resize3(byte[] imageByte,Integer newImageWidth){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            // 读取图片
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageByte));

            int height = originalImage.getHeight();
            int width = originalImage.getWidth();

            int newImageHeight = new BigDecimal(height).divide(new BigDecimal(width),2, RoundingMode.HALF_UP).multiply(new BigDecimal(newImageWidth)).intValue();
            // 创建一个新的BufferedImage，设置类型和尺寸
            BufferedImage resizedImage = new BufferedImage(newImageWidth, newImageHeight, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D graphics = resizedImage.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            // 绘制并改变图片尺寸
            graphics.drawImage(originalImage.getScaledInstance(newImageWidth, newImageHeight, BufferedImage.SCALE_SMOOTH), 0, 0, null);


            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);

            ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("jpg").next();

            imageWriter.setOutput(imageOutputStream);


            ImageWriteParam writeParam = imageWriter.getDefaultWriteParam();

            writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            writeParam.setCompressionQuality(0.9f);

            // 写入新的图片文件
            imageWriter.write(null,new IIOImage(resizedImage,null,null),writeParam);

            imageOutputStream.close();
            imageWriter.dispose();

            System.out.println("图片尺寸已调整。");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray() ;


    }




    public static byte[] resize2(byte[] imageByte,Integer newImageWidth){

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            // 读取图片
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageByte));

            int height = originalImage.getHeight();

            System.out.println("图片原始的高：" + height);
            int width = originalImage.getWidth();
            System.out.println("图片原始的宽：" + width);

            int newImageHeight = new BigDecimal(height).divide(new BigDecimal(width),2, RoundingMode.HALF_UP).multiply(new BigDecimal(newImageWidth)).intValue();
            // 创建一个新的BufferedImage，设置类型和尺寸
            BufferedImage resizedImage = new BufferedImage(newImageWidth, newImageHeight, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D graphics = resizedImage.createGraphics();

//            graphics.

            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            // 绘制并改变图片尺寸
//            Image scaledInstance = originalImage.getScaledInstance(newImageWidth, newImageHeight, BufferedImage.SCALE_SMOOTH);
            graphics.drawImage(originalImage, 0, 0, newImageWidth,newImageHeight,0,0,width,height,null);
//            graphics.drawImage(originalImage, 0, 0,newImageWidth,newImageHeight,null);

            // 写入新的图片文件
            ImageIO.write(resizedImage, "JPEG", outputStream);
//            ImageIO.write(resizedImage, "png", outputStream);

            System.out.println("图片尺寸已调整。");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray() ;
    }

    public static byte[] resize5(byte[] imageByte,Integer newImageWidth){

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            // 读取图片
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageByte));

            int height = originalImage.getHeight();
            int width = originalImage.getWidth();
            int newImageHeight = new BigDecimal(height).divide(new BigDecimal(width),2, RoundingMode.HALF_UP).multiply(new BigDecimal(newImageWidth)).intValue();

            Thumbnails.of(new ByteArrayInputStream(imageByte)).
//                        size(newImageWidth,newImageHeight).
                        // 是否保持比例
//                        keepAspectRatio(false).
                        scalingMode(ScalingMode.BILINEAR).
                        scalingMode(ScalingMode.BICUBIC).
                        scalingMode(ScalingMode.PROGRESSIVE_BILINEAR).
                    // 图片缩放90%, 不能和size()一起使用
//                        scale(newImageWidth,newImageHeight).
                        scale(0.2f).
                    // 图片质量压缩到50%
                        outputQuality(1d).
                        outputFormat("jpg").
                        toOutputStream(outputStream);

            System.out.println("图片尺寸已调整。");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray() ;

    }


    public static byte[] transToJPG(byte[] imageByte){

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            // 读取图片
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageByte));

            int height = originalImage.getHeight();
            System.out.println("转换之前的高：" + height);
            int width = originalImage.getWidth();
            System.out.println("转换之前的宽：" + width);

            BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);

            Graphics2D graphics = resizedImage.createGraphics();
            graphics.drawImage(originalImage, 0, 0 ,null);


            System.out.println("转换之前的高：" + resizedImage.getHeight());
            System.out.println("转换之前的宽：" + resizedImage.getWidth());

            // 写入新的图片文件
            ImageIO.write(resizedImage, "JPEG", outputStream);

            System.out.println("图片尺寸已调整。");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray() ;
    }

    public static void resizeImage(String srcFilePath, String destFilePath, int targetWidth, int targetHeight) {
        try {
            // 读取源图片
            BufferedImage originalImage = ImageIO.read(new File(srcFilePath));

            // 计算缩放比例
            double scaleX = (double) targetWidth / originalImage.getWidth();
            double scaleY = (double) targetHeight / originalImage.getHeight();

            // 创建一个新的BufferedImage，用于存放缩放后的图片
            int scaledWidth = (int) (originalImage.getWidth() * scaleX);
            int scaledHeight = (int) (originalImage.getHeight() * scaleY);
            BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, originalImage.getType());

            // 获取Graphics2D对象，用于绘制缩放后的图片
            Graphics2D g2d = scaledImage.createGraphics();

            // 设置渲染提示
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 绘制缩放后的图片
            g2d.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);

            // 释放Graphics2D对象占用的资源
            g2d.dispose();

            // 写入缩放后的图片到文件
            ImageIO.write(scaledImage, "jpg", new File(destFilePath));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static byte[] watermark(byte[] image,String context,Integer fontSize){
        byte[] waterMark = image ;

        try {
            context = new String(context.getBytes(StandardCharsets.UTF_8),StandardCharsets.UTF_8);
            BufferedImage read = ImageIO.read(new ByteArrayInputStream(image));

            int imgWidth = read.getWidth(null);
            int imgHeight = read.getHeight(null);
            BufferedImage bufImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_4BYTE_ABGR);
//            BufferedImage bufImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
            // 加水印：
            // 创建画笔
            Graphics2D graphics = bufImg.createGraphics();
            //消除锯齿
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 绘制原始图片
            graphics.drawImage(read, 0, 0, imgWidth, imgHeight, null);

            // 添加文字水印：
            // 根据图片的背景设置水印颜色
            graphics.setColor(Color.GRAY);
            // 设置字体  画笔字体样式为微软雅黑，加粗，文字大小为45pt
            graphics.setFont(new Font("宋体", Font.BOLD, fontSize));
            // 设置水印的坐标(为原图片中间位置)
            int x = (imgWidth - getWatermarkLength(context, graphics)) / 2;
            int y = imgHeight / 2;
            // 画出水印 第一个参数是水印内容，第二个参数是x轴坐标，第三个参数是y轴坐标
            graphics.drawString(context, x, y);

            graphics.dispose();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            ImageIO.write(bufImg, "png", outputStream);
            ImageIO.write(bufImg, "jpg", outputStream);

            waterMark = outputStream.toByteArray();
        } catch (Exception e) {

        }


        return waterMark ;
    }

    public static byte[] watermark(byte[] image,String context,String fontPath){
        byte[] waterMark = image ;

        try {
            context = new String(context.getBytes(StandardCharsets.UTF_8),StandardCharsets.UTF_8);

//            Image src = Toolkit.getDefaultToolkit().createImage(image);
//            BufferedImage read = toBufferedImage(src);
            BufferedImage read = ImageIO.read(new ByteArrayInputStream(image));

            int imgWidth = read.getWidth(null);
            int imgHeight = read.getHeight(null);

            char[] chars = context.toCharArray();
            int charSize = chars.length ;
            double size = (imgWidth / charSize) - (imgWidth / charSize * 0.1) ;

            System.out.println("图片宽度：" + imgWidth);
            System.out.println("图片高度：" + imgHeight);
            System.out.println("字体大小：" + size);
            float fontSize = new BigDecimal(size).floatValue() ;
            System.out.println("字体大小：" + fontSize);

            int y = new BigDecimal(imgHeight).subtract(new BigDecimal(imgHeight).subtract(new BigDecimal(fontSize)).divide(new BigDecimal(2))).intValue();
            System.out.println("y：" + y);

            BufferedImage bufImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_4BYTE_ABGR);
//            BufferedImage bufImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
            // 加水印：
            // 创建画笔
            Graphics2D graphics = bufImg.createGraphics();
            //消除锯齿
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 绘制原始图片
            graphics.drawImage(read, 0, 0, imgWidth, imgHeight, null);

            // 添加文字水印：
            // 根据图片的背景设置水印颜色
            graphics.setColor(Color.GRAY);
            // 设置字体  画笔字体样式为微软雅黑，加粗，文字大小为45pt

            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath));
            font = font.deriveFont(Font.BOLD, fontSize); // 设置字体大小和样式
            graphics.setFont(font);

//            graphics.setFont(new Font("宋体", Font.BOLD, fontSize));
            // 设置水印的坐标(为原图片中间位置)
            int x = (imgWidth - getWatermarkLength(context, graphics)) / 2;
//            int y = imgHeight / 2;
            // 画出水印 第一个参数是水印内容，第二个参数是x轴坐标，第三个参数是y轴坐标
            graphics.drawString(context, x, y);

            graphics.dispose();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufImg, "png", outputStream);
//            ImageIO.write(bufImg, "jpg", outputStream);

            waterMark = outputStream.toByteArray();
        } catch (Exception e) {

        }


        return waterMark ;
    }

    private static int getWatermarkLength(String watermarkContent, Graphics2D graphics) {
        return graphics.getFontMetrics(graphics.getFont()).charsWidth(watermarkContent.toCharArray(), 0, watermarkContent.length());
    }

    public static BufferedImage toBufferedImage(Image p_w_picpath) {  

        if (p_w_picpath instanceof BufferedImage) {  

            return (BufferedImage) p_w_picpath;  

        }  

        // This code ensures that all the pixels in the p_w_picpath are loaded  

        p_w_picpath = new ImageIcon(p_w_picpath).getImage();

        BufferedImage bp_w_picpath = null;  

        GraphicsEnvironment ge = GraphicsEnvironment  

                .getLocalGraphicsEnvironment();  

        try {  

            int transparency = Transparency.OPAQUE;  

            GraphicsDevice gs = ge.getDefaultScreenDevice();  

            GraphicsConfiguration gc = gs.getDefaultConfiguration();  

            bp_w_picpath = gc.createCompatibleImage(p_w_picpath.getWidth(null),  

                                        p_w_picpath.getHeight(null), transparency);  

        } catch (HeadlessException e) {  

            // The system does not have a screen  

        }  

        if (bp_w_picpath == null) {  

            // Create a buffered p_w_picpath using the default color model  

            int type = BufferedImage.TYPE_INT_RGB;  

            bp_w_picpath = new BufferedImage(p_w_picpath.getWidth(null),  

                                        p_w_picpath.getHeight(null), type);  

        }  

        // Copy p_w_picpath to buffered p_w_picpath  

        Graphics g = bp_w_picpath.createGraphics();  

        // Paint the p_w_picpath onto the buffered p_w_picpath  

        g.drawImage(p_w_picpath, 0, 0, null);  

        g.dispose();  

        return bp_w_picpath;  

    }
}