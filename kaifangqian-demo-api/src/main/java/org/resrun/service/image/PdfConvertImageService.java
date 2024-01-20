package org.resrun.service.image;

import org.apache.pdfbox.Loader;
import org.resrun.service.pojo.ConvertImage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: pdf文件转图片业务
 * @Package: org.resrun.service.image
 * @ClassName: Pdf2ImageService
 * @copyright 北京资源律动科技有限公司
 */
@Service
public class PdfConvertImageService {

    private Float resolution = 90f ;
    /**图片格式为jpg类型**/
    public static final String IMG_JPE = "jpg";
    /**图片格式为png类型**/
    public static final String IMG_PNG = "png";

    public List<ConvertImage> convertImage(byte[] signFileByte){
        if(signFileByte == null){
            return null ;
        }
        List<ConvertImage> responseList = new ArrayList<>();
        PDDocument doc = null;
        try {
            //加载pdf文件
            doc = Loader.loadPDF(signFileByte);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            if(pageCount == 0){
                return responseList;
            }

            for (int i = 0; i < pageCount; ++i) {
                int page = i ;
                BufferedImage image = renderer.renderImageWithDPI(page, resolution);
                ByteArrayOutputStream stream =  new ByteArrayOutputStream();
                ImageIO.write(image, IMG_PNG, stream);
                byte[] bytes = stream.toByteArray();
                if(bytes != null){
                    ConvertImage response = new ConvertImage();
                    response.setImageByte(bytes);
                    response.setPage(page);
                    responseList.add(response);

                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return responseList ;
    }


}