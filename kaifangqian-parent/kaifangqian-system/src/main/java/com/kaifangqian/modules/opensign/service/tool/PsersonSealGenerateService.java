/**
 * @description 个人印章处理工具服务类
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

import com.kaifangqian.modules.opensign.enums.SealColorEnum;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: 签章处理工具服务类
 * @Package: com.kaifangqian.modules.sign.service.impl
 * @ClassName: SealUtilServiceImpl
 * @author: FengLai_Gong
 */
@Service
public class PsersonSealGenerateService {

    private static final int WIDTH = 200;// 图片宽度
    private static final int HEIGHT = 200;// 图片高度


    public static void main(String[] args) {
        try {
            //BufferedImage bufferedImage = drawRectangleFrame("一二三四五", SealColorEnum.RED.getCode());
//            BufferedImage bufferedImage = drawCircleSeal("研发部","2025.07.07","张三", SealColorEnum.RED.getCode());
//            ImageIO.write(bufferedImage,"png",new FileOutputStream(new File("F:\\sealtest\\P-01.png")));

            generateSeal("课长", "李爱兵", Color.RED, 200, "F:\\sealtest\\finance_seal45.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @Description #生成正方形带方框的签章图片
     * @Param [name]
     * @return java.awt.image.BufferedImage
     **/
    public static BufferedImage drawSquareFrame(String name,Integer color) throws Exception {

        int imageWidth = 166 ;
        int imageHeight = 166 ;
        Color allColor = Color.RED ;
        if(SealColorEnum.RED.getCode().equals(color)){
            allColor = Color.RED ;
        }else if(SealColorEnum.BLUE.getCode().equals(color)){
            allColor = Color.BLUE;
        }else if(SealColorEnum.BLACK.getCode().equals(color)){
            allColor = Color.BLACK;
        }

        // 生成正方形图片，宽高都是166,imageType为3是生成透明图片
        BufferedImage bi = new BufferedImage(imageWidth, imageHeight, 3);
        // 设置属性
        Graphics2D g2d = bi.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.drawImage(bi, 0, 0, imageWidth, imageHeight, Color.WHITE,null);不能是白色，不然图片就不透明了
        g2d.drawImage(bi, 0, 0, imageWidth, imageHeight,null);

        // 线宽
        g2d.setStroke(new BasicStroke(8F));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(allColor);
        g2d.drawRect(1, 1, imageWidth - 3 , imageHeight - 3);


        char[] charArray = name.toCharArray();
        g2d.setFont(new Font("楷体", Font.LAYOUT_LEFT_TO_RIGHT, 70));// 写入签名
        g2d.setColor(allColor);
        if (charArray.length == 3) {
            // 设置字体
            g2d.drawString(new String(new char[] { charArray[0] }), 80, 68); // 写文字
            g2d.drawString(new String(new char[] { charArray[1] }), 80, 148); // 写文字
            g2d.drawString(new String(new char[] { charArray[2] }), 5, 108); // 写文字

        } else {
            // 设置字体
            g2d.drawString(new String(new char[] { charArray[0] }), 80, 68); // 写文字
            g2d.drawString(new String(new char[] { charArray[1] }), 80, 148); // 写文字
            g2d.drawString(new String(new char[] { charArray[2] }), 5, 68); // 写文字
            g2d.drawString(new String(new char[] { charArray[3] }), 5, 148); // 写文字
        }


        return bi;


    }

    /**
     * @Description #生成正方形不带方框的签章图片
     * @Param [name]
     * @return java.awt.image.BufferedImage
     **/
    public static BufferedImage drawSquareNoFrame(String name,Integer color) throws Exception {

        int imageWidth = 166 ;
        int imageHeight = 166 ;
        Color allColor = Color.RED ;
        if(SealColorEnum.RED.getCode().equals(color)){
            allColor = Color.RED ;
        }else if(SealColorEnum.BLUE.getCode().equals(color)){
            allColor = Color.BLUE;
        }else if(SealColorEnum.BLACK.getCode().equals(color)){
            allColor = Color.BLACK;
        }

        // 生成正方形图片，宽高都是166,imageType为3是生成透明图片
        BufferedImage bi = new BufferedImage(imageWidth, imageHeight, 3);
        // 设置属性
        Graphics2D g2d = bi.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.drawImage(bi, 0, 0, imageWidth, imageHeight, Color.WHITE,null);不能是白色，不然图片就不透明了
        g2d.drawImage(bi, 0, 0, imageWidth, imageHeight,null);

        char[] charArray = name.toCharArray();
        g2d.setFont(new Font("楷体", Font.LAYOUT_LEFT_TO_RIGHT, 70));// 写入签名
        g2d.setColor(allColor);
        if (charArray.length == 3) {
            // 设置字体
            g2d.drawString(new String(new char[] { charArray[0] }), 80, 68); // 写文字
            g2d.drawString(new String(new char[] { charArray[1] }), 80, 148); // 写文字
            // 设置字体
            g2d.drawString(new String(new char[] { charArray[2] }), 5, 108); // 写文字
        } else {
            // 设置字体
            g2d.drawString(new String(new char[] { charArray[0] }), 80, 68); // 写文字
            g2d.drawString(new String(new char[] { charArray[1] }), 80, 148); // 写文字
            g2d.drawString(new String(new char[] { charArray[2] }), 5, 68); // 写文字
            g2d.drawString(new String(new char[] { charArray[3] }), 5, 148); // 写文字
        }


        return bi;


    }

    /**
     * @Description #生成长方形带方框的签章图片
     * @Param [name]
     * @return java.awt.image.BufferedImage
     **/
    public static BufferedImage drawRectangleFrame(String name,Integer color) throws Exception {

        Color allColor = Color.RED ;
        if(SealColorEnum.RED.getCode().equals(color)){
            allColor = Color.RED ;
        }else if(SealColorEnum.BLUE.getCode().equals(color)){
            allColor = Color.BLUE;
        }else if(SealColorEnum.BLACK.getCode().equals(color)){
            allColor = Color.BLACK;
        }


        if (name == null) {
            return null;
        }
        char[] charArray = name.toCharArray();
        if (charArray.length <= 0) {
            return null;
        }
        int length = charArray.length;
        int step = 260;
        int width = step * length;
        // 图片长度
        BufferedImage bi = new BufferedImage(width, step, 3);
        Graphics2D g2d = bi.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.drawImage(bi, 0, 0, width, step, Color.WHITE,null);不能是白色，不然图片就不透明了
        g2d.drawImage(bi, 0, 0, width, step, null);

        // 线宽
        g2d.setStroke(new BasicStroke(8F));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(allColor);
        g2d.drawRect(1, 1, width - 5, step - 5);
        // 设置字体
        g2d.setFont(new Font("楷体", Font.LAYOUT_LEFT_TO_RIGHT, 230));// 写入签名
        // g2d.scale(0.9F, 1);// 缩放字体宽度
        int x = 0;
        for (char c : charArray) {

            g2d.drawString(new String(new char[] { c }), x, 210); // 写文字
            x = x + step;
        }
        return bi;
    }

    /**
     * @Description #生成长方形不带方框的签章图片
     * @Param [name]
     * @return java.awt.image.BufferedImage
     **/
    public static BufferedImage drawRectangleNoFrame(String name,Integer color) throws Exception {

        if (name == null) {
            return null;
        }
        char[] charArray = name.toCharArray();
        if (charArray.length <= 0) {
            return null;
        }
        Color allColor = Color.RED ;
        if(SealColorEnum.RED.getCode().equals(color)){
            allColor = Color.RED ;
        }else if(SealColorEnum.BLUE.getCode().equals(color)){
            allColor = Color.BLUE;
        }else if(SealColorEnum.BLACK.getCode().equals(color)){
            allColor = Color.BLACK;
        }
        int length = charArray.length;
        int step = 260;
        int width = step * length;
        // 图片长度
        BufferedImage bi = new BufferedImage(width, step, 3);
        Graphics2D g2d = bi.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //绘制图片
//        g2d.drawImage(bi, 0, 0, width, step, Color.WHITE,null); 不能是白色，不然图片就不透明了
        g2d.drawImage(bi, 0, 0, width, step, null);


        // 设置字体
        g2d.setFont(new Font("楷体", Font.LAYOUT_LEFT_TO_RIGHT, 230));
        g2d.setColor(allColor);
        int x = 0;
        for (char c : charArray) {

            g2d.drawString(new String(new char[] { c }), x, 210); // 写文字
            x = x + step;
        }
        return bi;
    }


    public static void generateSeal(String department, String name, Color color, int size, String outputPath) {
        // 创建透明背景的图像
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // 设置抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 设置透明背景
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, size, size);
        g2d.setComposite(AlphaComposite.Src);

        // 计算中心点和半径
        int centerX = size / 2;
        int centerY = size / 2;
        int radius = (int) (size * 0.4);

        // 绘制圆形边框（加粗）
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke((float) (size * 0.010))); // 减小边框粗细比例，使线条更细致
        g2d.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

        // 绘制两条横线将圆形分为三部分
        int lineY1 = centerY - (int) (radius * 0.3);
        int lineY2 = centerY + (int) (radius * 0.3);
        int lineXStart = centerX - (int) (radius * 0.93);
        int lineXEnd = centerX + (int) (radius * 0.93);

        g2d.drawLine(lineXStart, lineY1, lineXEnd, lineY1);
        g2d.drawLine(lineXStart, lineY2, lineXEnd, lineY2);

        // 设置文字样式
        g2d.setColor(color);
        g2d.setFont(new Font("宋体", Font.BOLD, (int) (size * 0.08)));

        // 绘制上部文字（部门）
        int deptNameLength = department.length();
        int minDeptNameLength = 2;
        int maxDeptNameLength = 5;
        int topNameActualLength = Math.min(Math.max(deptNameLength, minDeptNameLength), maxDeptNameLength);
        String topText = department.substring(0, topNameActualLength);

        double topNameBaseScale = 0.10;
        double topNameScaleStep = 0.02;
        double topDynamicScale = topNameBaseScale + (maxDeptNameLength - topNameActualLength) * topNameScaleStep;
        int topFontSize = (int) (size * topDynamicScale);

        g2d.setFont(new Font("宋体", Font.PLAIN, topFontSize));
        drawCenteredText(g2d, topText, centerX, centerY - (int) (radius * 0.6), size);

        // 绘制中部日期
        String dateStr = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
        int dateFontSize = (int) (size * 0.14);
        g2d.setFont(new Font("宋体", Font.PLAIN, dateFontSize));
        drawCenteredText(g2d, dateStr, centerX, centerY, size);

        // 绘制下部文字（姓名）
        int nameLength = name.length();
        int minNameLength = 2;
        int maxNameLength = 5;
        int actualLength = Math.min(Math.max(nameLength, minNameLength), maxNameLength);
        String bottomText = name.substring(0, actualLength);

        double baseScale = 0.10;
        double scaleStep = 0.02;
        double dynamicScale = baseScale + (maxNameLength - actualLength) * scaleStep;
        int bottomFontSize = (int) (size * dynamicScale);

        g2d.setFont(new Font("宋体", Font.PLAIN, bottomFontSize));
        drawCenteredText(g2d, bottomText, centerX, centerY + (int) (radius * 0.6), size);
        
        g2d.dispose();

        // 保存图像
        try {
            ImageIO.write(image, "png", new File(outputPath));
            System.out.println("印章生成成功: " + outputPath);
        } catch (IOException e) {
            System.err.println("保存印章图片失败: " + e.getMessage());
        }
    }

    private static void drawCenteredText(Graphics2D g2d, String text, int centerX, int centerY, int size) {
        if (text == null || text.isEmpty()) return;

        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        // 计算绘制位置（居中）
        int x = centerX - textWidth / 2;
        int y = centerY - textHeight / 2 + fm.getAscent();

        g2d.drawString(text, x, y);
    }



}