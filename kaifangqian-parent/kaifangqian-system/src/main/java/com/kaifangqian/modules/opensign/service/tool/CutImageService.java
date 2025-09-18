/**
 * @description 骑缝章裁剪工具类
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

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: CutImageService
 * @Package: com.kaifangqian.modules.opensign.service.tool
 * @ClassName: CutImageService
 * @author: FengLai_Gong
 */
@Service
public class CutImageService {

    public List<byte[]> cutImage(byte[] originImage,int number){
        List<byte[]> imageList = new ArrayList<>();

        // 加载图像
        BufferedImage originalImage = null;
        int cols = number ;
        int rows = 1 ;
        try {
            originalImage = ImageIO.read(new ByteArrayInputStream(originImage));
            int tileSizeX = originalImage.getWidth() / cols;
            int tileSizeY = originalImage.getHeight() / rows;

            BufferedImage[] slicedImages = new BufferedImage[rows * cols];

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    slicedImages[i * cols + j] = originalImage.getSubimage(j * tileSizeX, i * tileSizeY, tileSizeX, tileSizeY);
                }
            }
            for(int i = 0 ; i < slicedImages.length ; i++){
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(slicedImages[i], "png", outputStream);
                imageList.add(outputStream.toByteArray());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        return imageList ;
    }
}