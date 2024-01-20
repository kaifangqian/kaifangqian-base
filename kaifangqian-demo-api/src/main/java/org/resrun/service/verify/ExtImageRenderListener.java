//package org.resrun.service.verify;
//
//import com.itextpdf.text.pdf.PdfDictionary;
//import com.itextpdf.text.pdf.parser.ImageRenderInfo;
//import com.itextpdf.text.pdf.parser.PdfImageObject;
//import com.itextpdf.text.pdf.parser.RenderListener;
//import com.itextpdf.text.pdf.parser.TextRenderInfo;
//import lombok.Data;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @Description: 图像监听类
// * @Package: org.resrun.service.verify
// * @ClassName: ExtImageRenderListener
// * @copyright 北京资源律动科技有限公司
// */
//@Slf4j
//@Data
//public class ExtImageRenderListener implements RenderListener {
//    private int i;
//    private String basePath;
//
//    private byte [] seal;
//    @Override
//    public void beginTextBlock() {
//
//    }
//
//    public void setBasePath(String basePath) {
//        if (basePath.endsWith("/")) {
//            this.basePath = basePath;
//        } else {
//            this.basePath = basePath + "/";
//        }
//    }
//
//    @Override
//    public void renderText(TextRenderInfo renderInfo) {
//    }
//
//    @Override
//    public void endTextBlock() {
//    }
//    PdfDictionary resources;
//
//    List<List<Integer>> numbers = new ArrayList<>();
//
//
//    @SneakyThrows
//    @Override
//    public void renderImage(ImageRenderInfo renderInfo) {
//        PdfImageObject image = renderInfo.getImage();
//        if (image == null) {
//            log.debug("Image {} could not be read", renderInfo.getRef().getNumber());
//            return;
//        }
//        seal = image.getImageAsBytes();
//    }
//}
//
