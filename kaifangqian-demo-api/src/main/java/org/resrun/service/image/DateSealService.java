package org.resrun.service.image;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: 日期签章业务
 * @Package: org.resrun.service.image
 * @ClassName: DateSealService
 * @copyright 北京资源律动科技有限公司
 */
@Service
public class DateSealService {


    /**
     * @Description #生成日期签章
     * @Param   date 日期值，可以为空，如果为空则会使用format生成当前时间的日期签章
     *          format 日期格式,
     *          width 签章宽,
     *          height签章高
     * @return byte[]
     **/
    public byte[] crateDateSeal(String date ,String format, int width, int height){
        byte[] seal = null ;
        String value = null ;
        if(date != null && date.length() > 0){
            value = date ;

        }else if(format != null && format.length() > 0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            value = simpleDateFormat.format(new Date());
        }
        if(value == null){
            return null ;
        }

        // 设置背景宽高
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文对象
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        // 填充
        graphics.fillRect(0, 0, width, height);
        // 设定字体大小及样式
        graphics.setFont(new Font("宋体", Font.BOLD,18));
        // 字体颜色
        graphics.setColor(Color.BLACK);
        // 描绘字符串
        graphics.drawString(value, 20,  28);
        graphics.dispose();
        if(image == null){
            return seal ;
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image,"png",byteArrayOutputStream);
            seal = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return seal ;
    }




}