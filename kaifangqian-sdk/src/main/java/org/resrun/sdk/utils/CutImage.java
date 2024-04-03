package org.resrun.sdk.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: CutImageService
 * @Package: com.resrun.modules.opensign.service.tool
 * @ClassName: CutImageService
 * @author: FengLai_Gong
 */
public class CutImage {

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