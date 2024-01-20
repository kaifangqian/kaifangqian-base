package org.resrun.api.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @Description: 企业签章业务
 * @Package: com.resrun.service.image
 * @ClassName: EntSealGenerateService
 * @copyright 北京资源律动科技有限公司
 */
@Service
public class EntSealGenerateService {



    /**
     * @Description #生成企业签章
     * @Param [topText, middleText]
     * @return byte[]
     **/
    public byte[] generateEntSeal(String topText,String middleText){

        byte[] bytes = export2pic("png", middleText,topText);
        return bytes;
    }




    /**
     * 印章名称距中心点偏移量,按照y轴方向
     */
    private int nameOffset = 50;
    /**
     * 印章宽度
     */
    private int width = 200;
    /**
     * 印章高度
     */
    private int height = 200;
    /**
     * 印章中心标志(默认为五角星)外接圆半径
     */
    private float radius = 30;
    /**
     * 印章名称颜色
     */
    private Color nameColor = Color.RED;
    /**
     * 印章所属单位
     */
//    private String firm;
    /**
     * 印章所属单位颜色
     */
    private Color firmColor = Color.RED;
    private float firmScale = 0.7F;
    /**
     * 边框线宽
     */
    private float borderWidth = 5F;
    /**
     * 边框颜色
     */
    private Color borderColor = Color.RED;
    /**
     * 印章标记(默认为五角星)线宽
     */
    private float signBorderWidth = 3F;
    /**
     * 印章标记颜色
     */
    private Color signBorderColor = Color.RED;
    /**
     * 印章标记填充颜色
     */
    private Color signFillColor = Color.RED;


    public void draw(Graphics2D g2d,String middleText,String topText) {
        // 把绘制起点挪到圆中心点
        g2d.translate(width / 2, height / 2);

        Stroke stroke = g2d.getStroke();// 旧的线性
        // 填充五角星
        Polygon polygon = getPentaclePoints(radius);
        if (signFillColor != null) {
            g2d.setColor(signFillColor);
            g2d.fill(polygon);
        }
        // 绘制五角星边框
        g2d.setStroke(new BasicStroke(signBorderWidth));
        g2d.setColor(signBorderColor);
        g2d.draw(polygon);

        // 绘制印章边框
        g2d.setFont(nameFont);
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(borderWidth));
        g2d.drawOval(-width / 2, -height / 2, width, height);
        g2d.setStroke(stroke);

        // 绘制印章名称
        g2d.setFont(nameFont);
        g2d.setColor(nameColor);   //g2d.setStroke(new BasicStroke(10F));
        FontMetrics fm = g2d.getFontMetrics();
        int h = fm.getHeight();// 名称高度
        int y = fm.getAscent() - h / 2;// 求得中心线经过字体的高度的一半时的字体的起绘点
        if(StringUtils.isNotEmpty(middleText)){

            int w = fm.stringWidth(middleText);// 名称宽度

            g2d.drawString(middleText, -w / 2, y + nameOffset);
        }


        // 绘制印章单位
        g2d.setFont(getFirmFont(topText));
        g2d.setColor(firmColor);
        fm = g2d.getFontMetrics();
        h = fm.getHeight();// 字高度

        int count = topText.length();// 字数
        int r = width / 2;// 半径,就假设此印章是个矩形,方便计算
        float angle;
        float start;

        if(count>1 && count <=10){
            angle = 20f;// 字间角度
            start = 90+(360 - angle*(count-1))/2;// 以x轴正向为0,顺时针旋转
        }else if(count > 10){
            angle = (360 - firmAngle) / (count-1);// 字间角度
            start = 90+(360 - angle*(count-1))/2;// 以x轴正向为0,顺时针旋转
        }else{
            angle = 0f;// 字间角度
            start = 90+(360 - angle*(2-1))/2;// 以x轴正向为0,顺时针旋转
        }
        double vr = Math.toRadians(90);// 垂直旋转弧度
        char[] chars = topText.toCharArray();
        for (int i = 0; i < count; i++) {
            char c = chars[i];// 需要绘制的字符
            int cw = fm.charWidth(c);// 此字符宽度
            float a = start + angle * i;// 现在角度
            double radians = Math.toRadians(a);
            g2d.rotate(radians);// 旋转坐标系,让要绘制的字符处于x正轴
            float x = r - h;// 绘制字符的x坐标为半径减去字高度
            g2d.translate(x, 0);// 移动到此位置,此时字和x轴垂直
            g2d.rotate(vr);// 旋转90度,让字平行于x轴
            g2d.scale(firmScale, 1);// 缩放字体宽度
            g2d.drawString(String.valueOf(c), -cw / 2, 0);// 此点为字的中心点
            // 将所有设置还原,等待绘制下一个
            g2d.scale(1 / firmScale, 1);
            g2d.rotate(-vr);
            g2d.translate(-x, 0);
            g2d.rotate(-radians);
        }
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    /**
     * 获取具有指定半径外接圆的五角星顶点
     *
     * @param radius
     *            圆半径
     */
    private Polygon getPentaclePoints(float radius) {
        if (radius <= 0)
            return null;
        float lradius = radius * 0.381966f;// 根据radius求内圆半径
        double halfpi = Math.PI / 180f;
        Point[] points = new Point[10];
        for (int i = 0; i < points.length; i++) {
            if (i % 2 == 1)
                points[i] = new Point(
                        (int) (Math.sin(halfpi * 36 * i) * radius),
                        (int) (Math.cos(halfpi * 36 * i) * radius));
            else
                points[i] = new Point(
                        (int) (Math.sin(halfpi * 36 * i) * lradius),
                        (int) (Math.cos(halfpi * 36 * i) * lradius));
        }
        Polygon polygon = new Polygon();
        for (Point p : points) {
            polygon.addPoint(p.x, p.y);
        }
        return polygon;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


    private Font nameFont = new Font("宋体", Font.PLAIN, 19);

    private Font getFirmFont(String topText){
        Font font = null;
        int len = topText.length();
        if(len==1){
            setFirmAngle(355);
            font = new Font("宋体", Font.PLAIN, 25);
        }else if(len>1 && len<=3){
            setFirmAngle(310);
            font = new Font("宋体", Font.PLAIN, 25);
        }else if(len>3 && len<=6){
            setFirmAngle(250);
            font = new Font("宋体", Font.PLAIN, 25);
        }else if(len>6 && len <=10){
            setFirmAngle(200);
            font = new Font("宋体", Font.PLAIN, 25);
        }else if(len>10 && len<=13){
            setFirmAngle(180);
            font = new Font("宋体", Font.PLAIN, 25);
        }
        else if(len>13 && len<=20){
            font = new Font("宋体", Font.PLAIN, 25);
            setFirmAngle(120);
        }else if(len>20 && len <= 25){
            font = new Font("宋体", Font.PLAIN, 23);

            setFirmAngle(80);
        }else if(len>25 && len < 30){
            setFirmAngle(80);
            font = new Font("宋体", Font.PLAIN, 19);
        }else if(len>=30 && len <= 40){
            setFirmAngle(80);
            font = new Font("宋体", Font.PLAIN, 19);
        }else{
            setFirmAngle(10);
            font = new Font("宋体", Font.PLAIN, 17);
        }
        return font;
    }



    private int firmAngle;
    public void setFirmAngle(int firmAngle){
        this.firmAngle = firmAngle;
    }





    /**
     * 导出此印章为透明背景的图片字节数组.
     *
     * @param format
     *            图片类型,如果为null,则默认为png
     * @return 数组
     * @throws FileNotFoundException
     * @throws IOException
     *             写出图像数据出现问题
     */
    public byte[] export2pic(String format,String middleText,String topText)  {
        int fix = 5;// 宽高修正,如果宽高就为图片宽高,可能边框线被切割
        BufferedImage bi = new BufferedImage(getWidth() + fix * 2, getHeight()
                + fix * 2, 3);


        Graphics2D g2d = bi.createGraphics();
        //防锯齿状毛刺算法
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.translate(fix, fix);
        draw(g2d,middleText,topText);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, format == null ? "png" : format, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(topText+"：生成企业签章失败",e);
        }finally {
            try {
                if(baos!=null)
                    baos.close();
            } catch (IOException e) {
            }
        }

    }




}