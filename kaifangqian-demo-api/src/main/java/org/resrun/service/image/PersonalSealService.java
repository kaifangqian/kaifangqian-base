package org.resrun.service.image;

import org.resrun.enums.SealColorEnum;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @Description: 个人签章业务
 * @Package: org.resrun.service.image
 * @ClassName: PersonalSealService
 * @copyright 北京资源律动科技有限公司
 */
public class PersonalSealService {


    private static final int WIDTH = 200;// 图片宽度
    private static final int HEIGHT = 200;// 图片高度






    /**
     * @Description #生成正方形带方框的签章图片
     * @Param [name]
     * @return java.awt.image.BufferedImage
     **/
    public static BufferedImage drawSquareFrame(String name, Integer color) throws Exception {

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



}