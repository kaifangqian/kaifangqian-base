/**
 * @description 时间格式签章工具类
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

import com.kaifangqian.config.FileProperties;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Description: DateSealGenerateService
 * @Package: com.kaifangqian.modules.opensign.service.tool
 * @ClassName: DateSealGenerateService
 * @author: FengLai_Gong
 */
@Service
public class DateSealGenerateService {

    @Autowired
    private FileProperties fileProperties;


    public static void main(String[] args) {
        DateSealGenerateService service = new DateSealGenerateService();
        byte[] dateSeal = service.crateDateSeal("2023-12-12", 180, 40);
        try {
            IOUtils.write(dateSeal,new FileOutputStream(new File("/Users/gongfenglai/Desktop/test/image/1212cccc.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public byte[] crateDateSeal(String str, int width, int height){
        byte[] seal = null ;
        BufferedImage image = createImage(str, width, height);
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

    public BufferedImage createImage(String str, int width, int height) {
//        // 设置背景宽高
//        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
//        // 获取图形上下文对象
//        Graphics2D graphics = image.createGraphics();
//        // 设置更高的渲染提示以提高质量
//        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
//        graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
//        graphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
//        // 填充
////        graphics.fillRect(0, 0, width, height);
//        // 设定字体大小及样式
//        graphics.setFont(new Font("宋体", Font.BOLD,18));
//        // 字体颜色
//        graphics.setColor(Color.BLACK);
//        // 描绘字符串
//        graphics.drawString(str, 20,  28);
//        graphics.dispose();


        // 设置背景宽高
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        // 获取图形上下文对象
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        // 填充
//        graphics.fillRect(0, 0, width, height);
        // 设定字体大小及样式
        graphics.setFont(new Font("宋体", Font.BOLD,14));
        // 字体颜色
        graphics.setColor(Color.BLACK);

        // 计算字符串的宽度和高度，以实现居中对齐
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int stringWidth = fontMetrics.stringWidth(str);
        int stringHeight = fontMetrics.getAscent();

        // 计算居中位置
        int x = (width - stringWidth) / 2;
        int y = (height + stringHeight) / 2;

        // 描绘字符串（居中）
        graphics.drawString(str, x, y);
        graphics.dispose();

        return image;
    }
}