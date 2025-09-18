/**
 * @description 印章工具类
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
package com.kaifangqian.modules.opensign.seal;

import com.kaifangqian.utils.MyStringUtils;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

/**
 * @author : zhenghuihan
 * create at:  2023/12/8  17:25
 * @description:
 */
public class SealUtils {
    /**
     * 默认从10x10的位置开始画，防止左上部分画布装不下
     */
    private final static int INIT_BEGIN = 10;

    /**
     * 绘制圆弧形文字
     *
     * @param g2d          画笔
     * @param circleRadius 弧形半径
     * @param font         字体对象
     * @param isTop        是否字体在上部，否则在下部
     */
    private static void drawArcFont4Circle(Graphics2D g2d, int circleRadius, SealFont font, boolean isTop) {
        if (font == null) {
            return;
        }

        //1.字体长度
        int fontTextLen = font.getFontText().length();

        //2.字体大小，默认根据字体长度动态设定 TODO
        int fontSize = font.getFontSize() == null ? (55 - fontTextLen * 2) : font.getFontSize();

        //3.字体样式
        int fontStyle = font.isBold() ? Font.BOLD : Font.PLAIN;

        //4.构造字体
        Font f = getFont(font.getFontFamily(), fontStyle, fontSize);
        //new Font(font.getFontFamily(), fontStyle, fontSize);

        FontRenderContext context = g2d.getFontRenderContext();
        Rectangle2D rectangle = f.getStringBounds(font.getFontText(), context);

        //5.文字之间间距，默认动态调整
        double fontSpace;
        if (font.getFontSpace() != null) {
            fontSpace = font.getFontSpace();
        } else {
            if (fontTextLen == 1) {
                fontSpace = 0;
            } else {
                fontSpace = rectangle.getWidth() / (fontTextLen - 1) * 0.9;
            }
        }

        //6.距离外圈距离
        int marginSize = font.getMarginSize() == null ? INIT_BEGIN : font.getMarginSize();

        //7.写字
        double newRadius = circleRadius + rectangle.getY() - marginSize;
        double radianPerInterval = 2 * Math.asin(fontSpace / (2 * newRadius));

        double fix = 0.04;
        if (isTop) {
            fix = 0.18;
        }
        double firstAngle;
        if (!isTop) {
            if (fontTextLen % 2 == 1) {
                firstAngle = Math.PI + Math.PI / 2 - (fontTextLen - 1) * radianPerInterval / 2.0 - fix;
            } else {
                firstAngle = Math.PI + Math.PI / 2 - ((fontTextLen / 2.0 - 0.5) * radianPerInterval) - fix;
            }
        } else {
            if (fontTextLen % 2 == 1) {
                firstAngle = (fontTextLen - 1) * radianPerInterval / 2.0 + Math.PI / 2 + fix;
            } else {
                firstAngle = (fontTextLen / 2.0 - 0.5) * radianPerInterval + Math.PI / 2 + fix;
            }
        }
        double scaleX = 1 - (double) font.getScale() / 10; // 水平拉伸因子
        double scaleY = 1 + (double) font.getScale() / 10; // 垂直拉伸因子
        for (int i = 0; i < fontTextLen; i++) {
            double theta;
            double thetaX;
            double thetaY;

            if (!isTop) {
                theta = firstAngle + i * radianPerInterval;
                thetaX = newRadius * Math.sin(Math.PI / 2 - theta);
                thetaY = newRadius * Math.cos(theta - Math.PI / 2);
            } else {
                theta = firstAngle - i * radianPerInterval;
                thetaX = newRadius * Math.sin(Math.PI / 2 - theta);
                thetaY = newRadius * Math.cos(theta - Math.PI / 2);
            }

            AffineTransform transform = new AffineTransform();
            if (!isTop) {
                transform.rotate(Math.PI + Math.PI / 2 - theta);
                transform.scale(scaleX, scaleY);
            } else {
                transform.rotate(Math.PI / 2 - theta + Math.toRadians(8));
                transform.scale(scaleX, scaleY);
            }

            Font f2 = f.deriveFont(transform);

            g2d.setFont(f2);
            //System.out.println("字：" + font.getFontText().substring(i, i + 1) + "  X：" + (circleRadius + thetaX + INIT_BEGIN) + "    Y：" + (circleRadius - thetaY + INIT_BEGIN));
            g2d.drawString(font.getFontText().substring(i, i + 1), (float) (circleRadius + thetaX + INIT_BEGIN), (float) (circleRadius - thetaY + INIT_BEGIN));
        }
    }

    /**
     * 绘制椭圆弧形文字
     *
     * @param g2d    画笔
     * @param circle 外围圆
     * @param font   字体对象
     * @param isTop  是否字体在上部，否则在下部
     */
    private static void drawArcFont4Oval(Graphics2D g2d, SealCircle circle, SealFont font, boolean isTop) {
        if (font == null) {
            return;
        }
        float radiusX = circle.getWidth();
        float radiusY = circle.getHeight();
        float radiusWidth = radiusX + circle.getLineSize();
        float radiusHeight = radiusY + circle.getLineSize();

        //1.字体长度
        int fontTextLen = font.getFontText().length();

        //2.字体大小，默认根据字体长度动态设定
        int fontSize = font.getFontSize() == null ? 25 + (10 - fontTextLen) / 2 : font.getFontSize();

        //3.字体样式
        int fontStyle = font.isBold() ? Font.BOLD : Font.PLAIN;

        //4.构造字体
        Font f = getFont(font.getFontFamily(), fontStyle, fontSize);
        //new Font(font.getFontFamily(), fontStyle, fontSize);
        //5.总的角跨度
        float totalArcAng = 180;//(float) (font.getFontSpace() * fontTextLen);
        //5.总的角跨度
        if (12 < fontTextLen && fontTextLen <= 14) {
            totalArcAng = fontTextLen * 15;
        } else if (16 < fontTextLen && fontTextLen <= 20) {
            totalArcAng = 180 + 10 * (fontTextLen - 17);
        } else if (fontTextLen > 20) {
            // 当字体长度大于20时，保持与长度为20时相同的角跨度
            totalArcAng = 180 + 10 * (20 - 17);
        }
        if (!isTop) {
            totalArcAng = 120;
        }

        //6.从边线向中心的移动因子
        float minRat = 0.75f;

        //开始角度：上：-180   下：30
        double startAngle = isTop ? -90f - totalArcAng / 2f : 90f - totalArcAng / 2f;

        //每度设置多少个点
        double step = 0.5;

        int alCount = (int) Math.ceil(totalArcAng / step) + 1;

        //角度数组
        double[] angleArr = new double[alCount];
        //累计到最后一个点的直线距离数组
        double[] arcLenArr = new double[alCount];
        int num = 0;
        double accArcLen = 0.0;
        angleArr[num] = startAngle;
        arcLenArr[num] = accArcLen;
        num++;
        double angR = startAngle * Math.PI / 180.0;
        double lastX = radiusX * Math.cos(angR) + radiusWidth;
        double lastY = radiusY * Math.sin(angR) + radiusHeight;
        for (double i = startAngle + step; num < alCount; i += step) {
            angR = i * Math.PI / 180.0;
            double x = radiusX * Math.cos(angR) + radiusWidth, y = radiusY * Math.sin(angR) + radiusHeight;
            accArcLen += Math.sqrt((lastX - x) * (lastX - x) + (lastY - y) * (lastY - y));
            angleArr[num] = i;
            arcLenArr[num] = accArcLen;
            lastX = x;
            lastY = y;
            num++;
        }

        double scaleX = 1 - (double) font.getScale() / 10; // 水平拉伸因子
        double scaleY = 1 + (double) font.getScale() / 10; // 垂直拉伸因子

        //平均每个字到最后一个点的距离
        double arcPer = accArcLen / fontTextLen;
        for (int i = 0; i < fontTextLen; i++) {
            double arcL = i * arcPer + arcPer / 2.0;
            double ang = 0.0;
            for (int p = 0; p < arcLenArr.length - 1; p++) {
                if (arcLenArr[p] <= arcL && arcL <= arcLenArr[p + 1]) {
                    ang = (arcL >= ((arcLenArr[p] + arcLenArr[p + 1]) / 2.0)) ? angleArr[p + 1] : angleArr[p];
                    break;
                }
            }
            //角度转换π
            angR = (ang * Math.PI / 180f);
            //挨着圆的X坐标
            Float x = radiusX * (float) Math.cos(angR) + radiusWidth;
            //System.out.print("x0:" + x);
            //挨着圆的Y坐标
            Float y = radiusY * (float) Math.sin(angR) + radiusHeight;
            //System.out.print("  y0:" + y);
            double qxang = Math.atan2(radiusY * Math.cos(angR), -radiusX * Math.sin(angR));
            double fxang = qxang + Math.PI / 2.0;

            int subIndex = isTop ? i : fontTextLen - 1 - i;
            String c = font.getFontText().substring(subIndex, subIndex + 1);

            //获取文字高宽
            FontMetrics fm = sun.font.FontDesignMetrics.getMetrics(f);
            int w = fm.stringWidth(c), h = fm.getHeight();

            if (isTop) {
                x += h * minRat * (float) Math.cos(fxang);
                //System.out.print("    x1:" + x);
                y += h * minRat * (float) Math.sin(fxang);
                x += -w / 2f * (float) Math.cos(qxang);
                //System.out.println("     x2:" + x);
                y += -w / 2f * (float) Math.sin(qxang);
            } else {
                x += (h * minRat) * (float) Math.cos(fxang);
                y += (h * minRat) * (float) Math.sin(fxang);
                x += w / 2f * (float) Math.cos(qxang);
                y += w / 2f * (float) Math.sin(qxang);
            }

            // 旋转
            AffineTransform affineTransform = new AffineTransform();
            if (isTop) {
//                affineTransform.rotate(Math.toRadians((fxang * 180.0 / Math.PI - 90)), 0, 0);
//                affineTransform.scale(0.8, 1.2);
                affineTransform.rotate(Math.toRadians((fxang * 180.0 / Math.PI - 90)), 0, 0);
                affineTransform.scale(scaleX, scaleY);
            } else {
                affineTransform.rotate(Math.toRadians((fxang * 180.0 / Math.PI + 180 - 90)), 0, 0);
                affineTransform.scale(scaleX, scaleY);
            }
            Font f2 = f.deriveFont(affineTransform);
            g2d.setFont(f2);
            // System.out.println("字：" + c + "    X:" + x.intValue() + "    Y:" + y.intValue() + "    角度：" + ang);
            Integer fixX = 0;
            Integer fixY = 0;
//            if (ang <= -120) {
//
//            } else if (ang > -120 && ang < -40) {
//                fixX = -i / 4;
//                fixY = i / 8;
//            } else if (ang >= -40) {
//                fixX = -i / 2;
//                fixY = i / 4;
//            }

            g2d.drawString(c, x.intValue() + INIT_BEGIN - i + fixX, y.intValue() + INIT_BEGIN + fixY);
        }
    }

    /**
     * 生成印章图片
     *
     * @param conf 配置文件
     * @return BufferedImage对象
     * @throws Exception 异常
     */
    private static BufferedImage buildSeal(SealConfiguration conf) throws Exception {

        //1.画布
        BufferedImage bi = new BufferedImage(conf.getWidth(), conf.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

        //2.画笔
        Graphics2D g2d = bi.createGraphics();

        //2.1抗锯齿设置
        //文本不抗锯齿，否则圆中心的文字会被拉长
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        //其他图形抗锯齿
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(hints);

        //2.2设置背景透明度
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 0));

        //2.3填充矩形
        g2d.fillRect(0, 0, conf.getWidth(), conf.getHeight());

        //2.4重设透明度，开始画图
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1));

        //2.5设置画笔颜色
        g2d.setPaint(conf.getBackgroudColor());

        //3.画边线圆
        if (conf.getBorderCircle() != null) {
            drawCicle(g2d, conf.getBorderCircle(), INIT_BEGIN, INIT_BEGIN);
        } else {
            throw new Exception("BorderCircle can not null！");
        }


        // 添加小缺口
        Random random = new Random();
        g2d.setColor(new Color(255, 0, 0, 0));

        Integer ww = 4;

        for (int i = 0; i < 3; i++) {
            g2d.setStroke(new BasicStroke(random.nextInt(ww)));
            int startX = random.nextInt(conf.getBorderCircle().getWidth());
            int startY = random.nextInt(200 - conf.getBorderCircle().getHeight());
            int endX = 10 + 2 * random.nextInt(conf.getBorderCircle().getWidth());
            int endY = 60 + 2 * random.nextInt(conf.getBorderCircle().getHeight());

            g2d.drawLine(startX, startY, endX, endY);
        }

        for (int i = 0; i < 3; i++) {
            g2d.setStroke(new BasicStroke(random.nextInt(ww)));
            int startX = 200 + random.nextInt(conf.getBorderCircle().getWidth());
            int startY = random.nextInt(200 - conf.getBorderCircle().getHeight());
            int endX = 10 + 2 * random.nextInt(conf.getBorderCircle().getWidth());
            int endY = 60 + 2 * random.nextInt(conf.getBorderCircle().getHeight());

            g2d.drawLine(startX, startY, endX, endY);
        }

        for (int i = 0; i < 3; i++) {
            g2d.setStroke(new BasicStroke(random.nextInt(ww)));
            int startX = random.nextInt(conf.getBorderCircle().getWidth());
            int startY = 340 + random.nextInt(200 - conf.getBorderCircle().getHeight());
            int endX = 10 + 2 * random.nextInt(conf.getBorderCircle().getWidth());
            int endY = 60 + 2 * random.nextInt(conf.getBorderCircle().getHeight());

            g2d.drawLine(startX, startY, endX, endY);
        }

        for (int i = 0; i < 3; i++) {
            g2d.setStroke(new BasicStroke(random.nextInt(ww)));
            int startX = 200 + random.nextInt(conf.getBorderCircle().getWidth());
            int startY = 340 + random.nextInt(200 - conf.getBorderCircle().getHeight());
            int endX = 10 + 2 * random.nextInt(conf.getBorderCircle().getWidth());
            int endY = 60 + 2 * random.nextInt(conf.getBorderCircle().getHeight());

            g2d.drawLine(startX, startY, endX, endY);
        }
        g2d.setColor(conf.getBackgroudColor());

        // 添加小缺口结束

        int borderCircleWidth = conf.getBorderCircle().getWidth();
        int borderCircleHeight = conf.getBorderCircle().getHeight();

        //6.画弧形主文字
        if (borderCircleHeight != borderCircleWidth) {
            drawArcFont4Oval(g2d, conf.getBorderCircle(), conf.getMainFont(), true);
        } else {
            drawArcFont4Circle(g2d, borderCircleHeight, conf.getMainFont(), true);
        }

        //7.画弧形副文字
        if (borderCircleHeight != borderCircleWidth) {
            drawArcFont4Oval(g2d, conf.getBorderCircle(), conf.getViceFont(), false);
        } else {
            drawArcFont4Circle(g2d, borderCircleHeight, conf.getViceFont(), false);
        }

        //8.画中心字
        drawCenterFont(g2d, (borderCircleWidth + INIT_BEGIN) * 2, (borderCircleHeight + INIT_BEGIN) * 2, conf.getCenterFont());

        boolean centerFlag = conf.getCenterFont() == null || MyStringUtils.isBlank(conf.getCenterFont().getFontText());
        //9.画抬头文字
        drawTitleFont(g2d, (borderCircleWidth + INIT_BEGIN) * 2, (borderCircleHeight + INIT_BEGIN) * 2, conf.getTitleFont(), centerFlag);

        g2d.dispose();
        return bi;
    }

    static Font getFont(String fontFamily, int fontStyle, int fontSize) {
        String path = getFontPath(fontFamily);
        ClassPathResource fontResource = new ClassPathResource("font/" + path);
        // 设置文字
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, fontResource.getInputStream());
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        font = font.deriveFont(fontStyle, fontSize);
        return font;
    }

    static String getFontPath(String name) {
        String path = "";
        if (name.equals("仿宋体")) {
            path = "STFANGSO.TTF";
        } else if (name.equals("方正黑体")) {
            path = "STHeiti.ttf";
        } else if (name.equals("楷体")) {
            path = "SIMSUNB.TTF";
        } else if (name.equals("宋体")) {
            path = "STSONG.TTF";
        } else if (name.equals("Arial")) {
            path = "Arial.ttf";
        }
        return path;
    }


    /**
     * 画文字
     *
     * @param g2d          画笔
     * @param circleWidth  边线圆宽度
     * @param circleHeight 边线圆高度
     * @param font         字体对象
     */
    private static void drawCenterFont(Graphics2D g2d, int circleWidth, int circleHeight, SealFont font) {
        if (font == null) {
            return;
        }

        //1.字体长度
        int fontTextLen = font.getFontText().length();

        //2.字体大小，默认根据字体长度动态设定
        int fontSize = font.getFontSize() == null ? (55 - fontTextLen * 2) : font.getFontSize();

        //3.字体样式
        int fontStyle = font.isBold() ? Font.BOLD : Font.PLAIN;

        //4.构造字体
        Font f = getFont(font.getFontFamily(), fontStyle, fontSize);
        // new Font(font.getFontFamily(), fontStyle, fontSize);
        g2d.setFont(f);

        FontRenderContext context = g2d.getFontRenderContext();
        FontMetrics fm = g2d.getFontMetrics();
        String[] fontTexts = font.getFontText().split("\n");
        if (fontTexts.length > 1) {
            int y = 0;
            for (String fontText : fontTexts) {
                y += Math.abs(f.getStringBounds(fontText, context).getHeight());
            }
            //5.设置上边距
            float marginSize = INIT_BEGIN + (float) (circleHeight / 2 - y / 2);
            for (String fontText : fontTexts) {
                Rectangle2D rectangle2D = f.getStringBounds(fontText, context);
                g2d.drawString(fontText, (float) (circleWidth / 2 - rectangle2D.getCenterX() + 1), marginSize);
                marginSize += Math.abs(rectangle2D.getHeight());
            }
        } else {
            Rectangle2D rectangle2D = f.getStringBounds(font.getFontText(), context);
            //5.设置上边距，默认在中心
            float marginSize = font.getMarginSize() == null ? (float) (circleHeight / 2 - rectangle2D.getCenterY()) : (float) (circleHeight / 2 - rectangle2D.getCenterY()) + (float) font.getMarginSize();
            //g2d.drawString(font.getFontText(), 125 - 35, 125+35);
            float x = 0.0f;
            //一个字符表示中间星 这里不减10会出现歪的现象
            if (font.getFontText().length() == 1) {
                x = (float) (circleWidth / 2 - rectangle2D.getCenterX());
                g2d.drawString(font.getFontText(), x, marginSize - 20);
            } else {
                String firstText = font.getFontText().substring(0, 1);
                double scaleX = 1 - (double) font.getScale() / 10; // 水平拉伸因子
                double scaleY = 1 + (double) font.getScale() / 10; // 垂直拉伸因子
                g2d.scale(scaleX, scaleY);
                x = (float) (font.getLeftSpace() + circleWidth / 2 - rectangle2D.getCenterX() + ((font.getFontText().length() - 1) * (fm.stringWidth(firstText) * 0.2 - font.getFontSpace())) / 2 + font.getScale() * font.getFontText().length() * fm.stringWidth(firstText) / 10);
                float y = marginSize - font.getScale() * fm.stringWidth(firstText) / 2;
                for (int i = 0; i < fontTextLen; i++) {
                    if (i > 0) {
                        x += fm.stringWidth(font.getFontText().substring(i - 1, i)) * 0.8 + font.getFontSpace();
                    }
                    //System.out.println("字：" + font.getFontText().substring(i, i + 1) + "x:" + x + "y" + y);
                    g2d.drawString(font.getFontText().substring(i, i + 1), x, y);
                }
            }
        }
    }


    /**
     * 画文字
     *
     * @param g2d          画笔
     * @param circleWidth  边线圆宽度
     * @param circleHeight 边线圆高度
     * @param font         字体对象
     */
    private static void drawTitleFont(Graphics2D g2d, int circleWidth, int circleHeight, SealFont font, boolean centerFlag) {
        if (font == null) {
            return;
        }

        //1.字体长度
        int fontTextLen = font.getFontText().length();

        //2.字体大小，默认根据字体长度动态设定
        int fontSize = font.getFontSize() == null ? (55 - fontTextLen * 2) : font.getFontSize();

        //3.字体样式
        int fontStyle = font.isBold() ? Font.BOLD : Font.PLAIN;

        //4.构造字体
        Font f = getFont(font.getFontFamily(), fontStyle, fontSize);
        // new Font(font.getFontFamily(), fontStyle, fontSize);
        g2d.setFont(f);

        FontRenderContext context = g2d.getFontRenderContext();
        FontMetrics fm = g2d.getFontMetrics();
        String[] fontTexts = font.getFontText().split("\n");
        if (fontTexts.length > 1) {
            int y = 0;
            for (String fontText : fontTexts) {
                y += Math.abs(f.getStringBounds(fontText, context).getHeight());
            }
            //5.设置上边距
            float marginSize = INIT_BEGIN + (float) (circleHeight / 2 - y / 2);
            for (String fontText : fontTexts) {
                Rectangle2D rectangle2D = f.getStringBounds(fontText, context);
                g2d.drawString(fontText, (float) (circleWidth / 2 - rectangle2D.getCenterX() + 1), marginSize);
                marginSize += Math.abs(rectangle2D.getHeight());
            }
        } else {
            Rectangle2D rectangle2D = f.getStringBounds(font.getFontText(), context);
            //5.设置上边距，默认在中心
            float marginSize = font.getMarginSize() == null ? (float) (circleHeight / 2 - rectangle2D.getCenterY()) : (float) (circleHeight / 2 - rectangle2D.getCenterY()) + (float) font.getMarginSize();
            //g2d.drawString(font.getFontText(), 125 - 35, 125+35);
            float x = 0.0f;
            //一个字符表示中间星 这里不减10会出现歪的现象
            if (font.getFontText().length() == 1) {
                x = (float) (circleWidth / 2 - rectangle2D.getCenterX());
                g2d.drawString(font.getFontText(), x, marginSize - 20);
            } else {
                if (circleWidth == circleHeight) {
                    String firstText = font.getFontText().substring(0, 1);
                    double scaleX = 1 - (double) font.getScale() / 10; // 水平拉伸因子
                    double scaleY = 1 + (double) font.getScale() / 10; // 垂直拉伸因子
                    g2d.scale(scaleX, scaleY);
                    x = (float) (font.getLeftSpace() + circleWidth / 2 - rectangle2D.getCenterX() + ((font.getFontText().length() - 1) * (fm.stringWidth(firstText) * 0.2 - font.getFontSpace())) / 2 + font.getScale() * font.getFontText().length() * fm.stringWidth(firstText) / 10);
                    float y = marginSize - font.getScale() * fm.stringWidth(firstText) / 2;
//                    System.out.println(fm.stringWidth(firstText));
//                    System.out.println(fm.getHeight());
                    for (int i = 0; i < fontTextLen; i++) {
                        //System.out.println("字：" + font.getFontText().substring(i, i + 1) + "x:" + (float) (x + i * (fm.stringWidth(firstText) * 0.8 + font.getFontSpace())) + "y" + y);
                        g2d.drawString(font.getFontText().substring(i, i + 1), (float) (x + i * (fm.stringWidth(firstText) * 0.8 + font.getFontSpace())), y);
                    }
                } else {
                    String firstText = font.getFontText().substring(0, 1);
                    double scaleX = 1 - (double) font.getScale() / 10; // 水平拉伸因子
                    double scaleY = 1 + (double) font.getScale() / 10; // 垂直拉伸因子
                    float leftSpace = 0;
                    if (centerFlag) {
                        g2d.scale(scaleX, scaleY);
                        leftSpace = 3;
                    }
                    x = leftSpace + (float) (font.getLeftSpace() + circleWidth / 2 - rectangle2D.getCenterX() + ((font.getFontText().length() - 1) * (fm.stringWidth(firstText) * 0.2 - font.getFontSpace())) / 2 + font.getScale() * font.getFontText().length() * fm.stringWidth(firstText) / 10);
                    float y = marginSize - font.getScale() * fm.stringWidth(firstText) / 2;
                    for (int i = 0; i < fontTextLen; i++) {
                        g2d.drawString(font.getFontText().substring(i, i + 1), (float) (x + i * (fm.stringWidth(firstText) * 0.8 + font.getFontSpace())), y);
                    }
                }
            }

        }
    }


    /**
     * 画圆
     *
     * @param g2d    画笔
     * @param circle 圆配置对象
     */
    private static void drawCicle(Graphics2D g2d, SealCircle circle, int x, int y) {
        if (circle == null) {
            return;
        }

        //1.圆线条粗细默认是圆直径的1/35
        int lineSize = circle.getLineSize() == null ? circle.getHeight() * 2 / (35) : circle.getLineSize();

        //2.画圆
        g2d.setStroke(new BasicStroke(lineSize));
        g2d.drawOval(x, y, circle.getWidth() * 2, circle.getHeight() * 2);
    }

    /**
     * 生成印章图片，并保存到指定路径
     *
     * @param conf 配置文件F
     * @throws Exception 异常
     */
    public static String buildAndStoreSeal(SealConfiguration conf) throws Exception {
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(buildBytes(buildSeal(conf)));
    }

    /**
     * 生成印章图片的byte数组
     *
     * @param image BufferedImage对象
     * @return byte数组
     * @throws Exception
     */
    private static byte[] buildBytes(BufferedImage image) throws Exception {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //bufferedImage转为byte数组
        ImageIO.write(image, "png", outStream);
        return outStream.toByteArray();
    }
}