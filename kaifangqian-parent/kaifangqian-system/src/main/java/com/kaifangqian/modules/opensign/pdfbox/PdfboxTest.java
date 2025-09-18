package com.kaifangqian.modules.opensign.pdfbox;

import com.kaifangqian.modules.opensign.pdfbox.vo.FormField;
import com.kaifangqian.utils.IOUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdfwriter.compress.CompressParameters;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.util.Store;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * @Description: PdfboxTest
 * @Package: com.kaifangqian.modules.opensign.pdfbox
 * @ClassName: PdfboxTest
 * @author: FengLai_Gong
 */
public class PdfboxTest {

    public static void main(String[] args) {
        try {
//            byte[] bytes = IOUtils.toByteArray(new File("/Users/gongfenglai/Desktop/test/pdf/testContract01.pdf"));
//            byte[] bytes = IOUtils.toByteArray(new File("/Users/gongfenglai/Desktop/test/seal/1210.png"));
//            byte[] bytes = IOUtils.toByteArray(new File("/Users/gongfenglai/Desktop/test/cert/0112-01.pfx"));
//            String encode = Base64.encode(bytes);
//            System.out.println(encode);
//            save();
            write();
//            write2();
//            insertImage();
//            verify();

//            verifySignPosition();
//            verifySig();
//            cutImage() ;
//            verifyFinally();
//            changeImg();

//            getCertificateInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save(){

        try {
            byte[] pdfByte = IOUtils.toByteArray(new File("/Users/gongfenglai/Desktop/test/pdf/0112.pdf"));
//            byte[] pdfByte = IOUtils.toByteArray(new File("/Users/gongfenglai/Desktop/test/pdf/write-02.pdf"));
            //加载pdf文件
//            MemoryUsageSetting memoryUsageSetting = MemoryUsageSetting.setupTempFileOnly();
            PDDocument pdDocument = Loader.loadPDF(pdfByte);
            //获取pdf文件第一页
            PDPage page = pdDocument.getPage(0);


            ByteArrayOutputStream out = new ByteArrayOutputStream();

//            COSStream cosout = new COSStream();
//            OutputStream outputStream = cosout.createOutputStream(COSName.FLATE_DECODE);


            pdDocument.save(out);
//            pdDocument.save(out, CompressParameters.DEFAULT_COMPRESSION);
//            pdDocument.save(outputStream);
//            pdDocument.saveIncremental(out);
//            pdDocument.saveIncrementalForExternalSigning(out);
            pdDocument.close();

//            List<PDSignatureField> signatureFields = pdDocument.getSignatureFields();
//            for(PDSignatureField signatureField : signatureFields){
//                PDSeedValue seedValue = signatureField.getSeedValue();
//                seedValue.
//            }
            org.apache.commons.io.IOUtils.write(out.toByteArray(),new FileOutputStream(new File("/Users/gongfenglai/Desktop/test/pdf/save-10.pdf")));

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public static void write(){
        try {
            byte[] bytes = IOUtils.toByteArray(new File("/Users/gongfenglai/Desktop/test/pdf/0112.pdf"));
            CreatedPDFForm createdPDFForm = new CreatedPDFForm(bytes);

            List<FormField> fields = new ArrayList<>();
            FormField formField = new FormField();
            formField.setOffsetX("80");
            formField.setOffsetY("500");
            formField.setWidth("100");
            formField.setHeight("50");
            formField.setPage(0);
            formField.setTextAlign("left");

            formField.setInterfaceParamName("axscdfvgb");

            formField.setValue("asdfghjkwertyuio");
            fields.add(formField);
            //设置字体
            File fontFile = new File("/Users/gongfenglai/Desktop/test/ttf/STSONG.TTF");
            InputStream fontInputStream = new FileInputStream(fontFile);

            byte[] out = createdPDFForm.addFormsTTF(fields,fontInputStream);

            org.apache.commons.io.IOUtils.write(out,new FileOutputStream(new File("/Users/gongfenglai/Desktop/test/pdf/write-09.pdf")));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void write2(){

        try {
            byte[] pdfByte = IOUtils.toByteArray(new File("/Users/gongfenglai/Desktop/test/pdf/0112.pdf"));
//            byte[] pdfByte = IOUtils.toByteArray(new File("/Users/gongfenglai/Desktop/test/pdf/write-02.pdf"));
            //加载pdf文件
//            MemoryUsageSetting memoryUsageSetting = MemoryUsageSetting.setupTempFileOnly();
            PDDocument pdDocument = Loader.loadPDF(pdfByte);
            //获取pdf文件第一页
            PDPage page = pdDocument.getPage(0);
            //创建page数据流对象
            PDPageContentStream contentStream = new PDPageContentStream(pdDocument,page,PDPageContentStream.AppendMode.APPEND,true,true);
            //设置字体
            File fontFile = new File("/Users/gongfenglai/Desktop/test/ttf/STSONG.TTF");
            InputStream fontInputStream = new FileInputStream(fontFile);
            PDType0Font font = PDType0Font.load(pdDocument, fontInputStream,false);
            contentStream.setFont(font,12);
            //开始写入
            contentStream.beginText();
            //写入文字
            contentStream.newLineAtOffset(100,600);
            contentStream.showText("齈黷鱸鱷鸗");
            //结束写入
            contentStream.endText();
            contentStream.close();

            ByteArrayOutputStream out = new ByteArrayOutputStream();

//            COSStream cosout = new COSStream();
//            OutputStream outputStream = cosout.createOutputStream(COSName.FLATE_DECODE);


//            pdDocument.save(out);
            pdDocument.save(out, CompressParameters.DEFAULT_COMPRESSION);
//            pdDocument.save(outputStream);
//            pdDocument.saveIncremental(out);
//            pdDocument.saveIncrementalForExternalSigning(out);
            pdDocument.close();

//            List<PDSignatureField> signatureFields = pdDocument.getSignatureFields();
//            for(PDSignatureField signatureField : signatureFields){
//                PDSeedValue seedValue = signatureField.getSeedValue();
//                seedValue.
//            }
            org.apache.commons.io.IOUtils.write(out.toByteArray(),new FileOutputStream(new File("/Users/gongfenglai/Desktop/test/pdf/write-01.pdf")));

        } catch (IOException e) {
            e.printStackTrace();
        }



    }


    public static void insertImage(){

        try {
            byte[] pdfByte = IOUtils.toByteArray(new File("/Users/gongfenglai/Desktop/test/pdf/0112.pdf"));
            byte[] sealByte = IOUtils.toByteArray(new File("/Users/gongfenglai/Desktop/test/image/e0f209137275d9b1.png"));
            //加载pdf文件
            PDDocument pdDocument = Loader.loadPDF(pdfByte);
            //获取pdf文件第一页
            PDPage page = pdDocument.getPage(0);
            PDRectangle cropBox = page.getCropBox();
//            PDPage page = new PDPage();
            //创建page数据流对象
            PDPageContentStream contentStream = new PDPageContentStream(pdDocument,page,PDPageContentStream.AppendMode.APPEND,true,false);
            //创建要插入的图片数据
            PDImageXObject imageXObject = PDImageXObject.createFromByteArray(pdDocument, sealByte, "aaa");
            contentStream.drawImage(imageXObject,100,100,180,180);
            contentStream.close();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            pdDocument.save(out);
            pdDocument.close();

            org.apache.commons.io.IOUtils.write(out.toByteArray(),new FileOutputStream(new File("/Users/gongfenglai/Desktop/test/pdf/insert-image-10.pdf")));

        } catch (IOException e) {
            e.printStackTrace();
        }



    }


    public static void verify(){

        try {
            byte[] pdfByte = IOUtils.toByteArray(new File("/Users/gongfenglai/Desktop/test/pdf/0112.pdf"));
            //加载pdf文件
            PDDocument pdDocument = Loader.loadPDF(pdfByte);

            List<PDSignature> signatureDictionaries = pdDocument.getSignatureDictionaries();


            byte[] origPDF = pdDocument.getSignatureDictionaries().get(0).getSignedContent(pdfByte);
            byte[] signature = pdDocument.getSignatureDictionaries().get(0).getContents(pdfByte);


            for(PDSignature pdSignature : signatureDictionaries){
                COSDictionary cosObject = pdSignature.getCOSObject();

                Calendar signDate = pdSignature.getSignDate();
                System.out.println(signDate);
                String name = pdSignature.getName();
                System.out.println(name);

                int[] byteRange = pdSignature.getByteRange();
                System.out.println(byteRange);

            }




        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public static void cutImage() {
        try {
            byte[] pdfByte = IOUtils.toByteArray(new File("/Users/gongfenglai/Desktop/test/pdf/verify02.pdf"));
            //加载pdf文件
            PDDocument pdDocument = Loader.loadPDF(pdfByte);
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            BufferedImage originalImage = renderer.renderImageWithDPI(0, 90f);
            // 切割图片
            BufferedImage subImage = originalImage.getSubimage(520, 50, 190, 190);
            ByteArrayOutputStream stream =  new ByteArrayOutputStream();
            ImageIO.write(subImage, "png", stream);
            byte[] bytes = stream.toByteArray();
            org.apache.commons.io.IOUtils.write(bytes,new FileOutputStream(new File("/Users/gongfenglai/Desktop/test/pdf/cut01.png")));

            byte[] bytes1 = changeImgColor(bytes);
            org.apache.commons.io.IOUtils.write(bytes1,new FileOutputStream(new File("/Users/gongfenglai/Desktop/test/pdf/cut02.png")));

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void verifySignPosition() {
        try {
//            byte[] pdfByte = IOUtils.toByteArray(new File("/Users/gongfenglai/Desktop/test/pdf/verify02.pdf"));
            byte[] pdfByte = IOUtils.toByteArray(new File("/Users/gongfenglai/Desktop/test/pdf/企业授权确认书.pdf"));
            //加载pdf文件
            PDDocument pdDocument = Loader.loadPDF(pdfByte);


            PDAcroForm acroForm = pdDocument.getDocumentCatalog().getAcroForm(null);

            List<PDField> fields = acroForm.getFields();

            int num = 0 ;
            for(PDField field : fields){


                List<PDAnnotationWidget> widgets = field.getWidgets();
                for(PDAnnotationWidget widget : widgets){
                    PDPage page = widget.getPage();

                    float pdfHeight = page.getMediaBox().getHeight();
                    float pdfWidth = page.getMediaBox().getWidth();
                    System.out.println("pdfHeight : " + pdfHeight);
                    System.out.println("pdfWidth : " + pdfWidth);


                    PDFRenderer renderer = new PDFRenderer(pdDocument);
                    int pageIndex = pdDocument.getPages().indexOf(page);
                    BufferedImage originalImage = renderer.renderImageWithDPI(pageIndex, 90f);

                    int originalImageHeight = originalImage.getHeight();
                    int originalImageWidth = originalImage.getWidth();
                    System.out.println("originalImageHeight : " + originalImageHeight);
                    System.out.println("originalImageWidth : " + originalImageWidth);

                    BigDecimal heightRate = new BigDecimal(originalImageHeight).divide(new BigDecimal(pdfHeight), 4, RoundingMode.DOWN);
                    BigDecimal widthRate = new BigDecimal(originalImageWidth).divide(new BigDecimal(pdfWidth), 4, RoundingMode.DOWN);


                    COSDictionary cosObject = field.getCOSObject();
                    COSArray array = (COSArray)cosObject.getDictionaryObject(COSName.RECT);

                    float[] floats = array.toFloatArray();
                    for(int i = 0 ; i < floats.length ; i++){
                        System.out.println(floats[i]);
                    }
                    float x = (int)floats[0];
                    float width = (int)(floats[2] - floats[0]);
                    float y = (int)floats[3];
                    float height = (int)(floats[3] - floats[1]);

                    int imageX = widthRate.multiply(new BigDecimal(x)).intValue() ;
                    System.out.println("imageX : " + imageX);
                    int imageWidth = widthRate.multiply(new BigDecimal(width)).intValue() ;
                    System.out.println("imageWidth : " + imageWidth);
                    int imageY = originalImageHeight - heightRate.multiply(new BigDecimal(y)).intValue() ;
                    System.out.println("imageY : " + imageY);
                    int imageHeight = heightRate.multiply(new BigDecimal(height)).intValue() ;
                    System.out.println("imageHeight : " + imageHeight);

                    // 切割图片
                    BufferedImage subImage = originalImage.getSubimage(imageX, imageY, imageWidth, imageHeight);


                    ByteArrayOutputStream stream =  new ByteArrayOutputStream();
                    ImageIO.write(subImage, "png", stream);
                    num++ ;
                    org.apache.commons.io.IOUtils.write(stream.toByteArray(),new FileOutputStream(new File("/Users/gongfenglai/Desktop/test/pdf/sub" + num + ".png")));

                }


            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void verifySig(){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            byte[] pdfByte = IOUtils.toByteArray(new File("/Users/gongfenglai/Desktop/test/pdf/verify02.pdf"));
            //加载pdf文件
            PDDocument pdDocument = Loader.loadPDF(pdfByte);

//            PDPage page = pdDocument.getPage(0);

            List<PDSignature> signatureDictionaries = pdDocument.getSignatureDictionaries();
            for(PDSignature pdSignature : signatureDictionaries){

//                COSDictionary cosObject = pdSignature.getCOSObject();
//                COSObject cosObject1 = cosObject.getCOSObject(COSName.PAGE);
//                COSBase object = cosObject1.getObject();

//                int[] byteRange = pdSignature.getByteRange();
//                for(int i = 0 ; i < byteRange.length ; i++){
//                    System.out.println("位置 : " + byteRange[i]);
//                }


                byte[] origPDF = pdSignature.getSignedContent(pdfByte);
                byte[] sig = pdSignature.getContents(pdfByte);
                //获取证书对象
                CMSSignedData cmsSignedData = new CMSSignedData(new CMSProcessableByteArray(origPDF), sig);
                SignerInformationStore sis = cmsSignedData.getSignerInfos();
                Collection signers = sis.getSigners();
                Store certStore = cmsSignedData.getCertificates();
                Iterator it = signers.iterator();
                while (it.hasNext()) {
                    SignerInformation signer = (SignerInformation) it.next();
                    Collection certCollection = certStore.getMatches(signer.getSID());
                    Iterator certIt = certCollection.iterator();
                    X509CertificateHolder cert = (X509CertificateHolder) certIt.next();
                    System.out.println("X509CertificateHolder=======================输出信息");

                    System.out.println("证书序列号：" + cert.getSerialNumber().toString(16));
                    String issuer = IETFUtils.valueToString(cert.getIssuer().getRDNs(BCStyle.CN)[0].getFirst().getValue());
                    System.out.println("颁发机构：" + issuer);
                    System.out.println("证书有效期开始时间：" + simpleDateFormat.format(cert.getNotBefore()));
                    System.out.println("证书有效期结束时间：" + simpleDateFormat.format(cert.getNotAfter()));



                    String subject = IETFUtils.valueToString(cert.getSubject().getRDNs(BCStyle.CN)[0].getFirst().getValue());
                    System.out.println("subject" + subject);


                    boolean bc = signer.verify(new JcaSimpleSignerInfoVerifierBuilder().build(cert));
                    System.out.println("是否篡改" + bc);

                }
                Date time = pdSignature.getSignDate().getTime();
                System.out.println("签署时间：" + simpleDateFormat.format(time));
                String name = pdSignature.getName();
                System.out.println("签署人：" + name);


            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public static void verifyFinally(){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            byte[] pdfByte = IOUtils.toByteArray(new File("/Users/gongfenglai/Desktop/test/pdf/verify02.pdf"));
//            byte[] pdfByte = IOUtils.toByteArray(new File("/Users/gongfenglai/Desktop/test/pdf/企业授权确认书.pdf"));
            //加载pdf文件
            PDDocument pdDocument = Loader.loadPDF(pdfByte);


            PDAcroForm acroForm = pdDocument.getDocumentCatalog().getAcroForm(null);

            List<PDField> fields = acroForm.getFields();

            int num = 0 ;
            for(PDField field : fields){


                if (field instanceof PDSignatureField){
                    COSDictionary value = field.getCOSObject().getCOSDictionary(COSName.V);
                    PDSignature pdSignature = new PDSignature(value);

                    byte[] origPDF = pdSignature.getSignedContent(pdfByte);
                    byte[] sig = pdSignature.getContents(pdfByte);
                    //获取证书对象
                    CMSSignedData cmsSignedData = new CMSSignedData(new CMSProcessableByteArray(origPDF), sig);
                    SignerInformationStore sis = cmsSignedData.getSignerInfos();
                    Collection signers = sis.getSigners();
                    Store certStore = cmsSignedData.getCertificates();
                    Iterator it = signers.iterator();
                    while (it.hasNext()) {
                        SignerInformation signer = (SignerInformation) it.next();
                        Collection certCollection = certStore.getMatches(signer.getSID());
                        Iterator certIt = certCollection.iterator();
                        X509CertificateHolder cert = (X509CertificateHolder) certIt.next();
                        System.out.println("X509CertificateHolder=======================输出信息");

                        System.out.println("证书序列号：" + cert.getSerialNumber().toString(16));
                        String issuer = IETFUtils.valueToString(cert.getIssuer().getRDNs(BCStyle.CN)[0].getFirst().getValue());
                        System.out.println("颁发机构：" + issuer);
                        System.out.println("证书有效期开始时间：" + simpleDateFormat.format(cert.getNotBefore()));
                        System.out.println("证书有效期结束时间：" + simpleDateFormat.format(cert.getNotAfter()));



                        String subject = IETFUtils.valueToString(cert.getSubject().getRDNs(BCStyle.CN)[0].getFirst().getValue());
                        System.out.println("subject" + subject);

                        try {
                            boolean bc = signer.verify(new JcaSimpleSignerInfoVerifierBuilder().build(cert));
                            System.out.println("是否篡改" + bc);
                        }catch (Exception e){
                            System.out.println("验签失败");
                            e.printStackTrace();
                        }


                    }
                    Date time = pdSignature.getSignDate().getTime();
                    System.out.println("签署时间：" + simpleDateFormat.format(time));
                    String name = pdSignature.getName();
                    System.out.println("签署人：" + name);


                }

                List<PDAnnotationWidget> widgets = field.getWidgets();
                for(PDAnnotationWidget widget : widgets){
                    PDPage page = widget.getPage();

                    float pdfHeight = page.getMediaBox().getHeight();
                    float pdfWidth = page.getMediaBox().getWidth();
                    System.out.println("pdfHeight : " + pdfHeight);
                    System.out.println("pdfWidth : " + pdfWidth);


                    PDFRenderer renderer = new PDFRenderer(pdDocument);
                    int pageIndex = pdDocument.getPages().indexOf(page);
                    BufferedImage originalImage = renderer.renderImageWithDPI(pageIndex, 90f);

                    int originalImageHeight = originalImage.getHeight();
                    int originalImageWidth = originalImage.getWidth();
                    System.out.println("originalImageHeight : " + originalImageHeight);
                    System.out.println("originalImageWidth : " + originalImageWidth);

                    BigDecimal heightRate = new BigDecimal(originalImageHeight).divide(new BigDecimal(pdfHeight), 4, RoundingMode.DOWN);
                    BigDecimal widthRate = new BigDecimal(originalImageWidth).divide(new BigDecimal(pdfWidth), 4, RoundingMode.DOWN);


                    COSDictionary cosObject = field.getCOSObject();
                    COSArray array = (COSArray)cosObject.getDictionaryObject(COSName.RECT);

                    float[] floats = array.toFloatArray();
                    for(int i = 0 ; i < floats.length ; i++){
                        System.out.println(floats[i]);
                    }
                    float x = (int)floats[0];
                    float width = (int)(floats[2] - floats[0]);
                    float y = (int)floats[3];
                    float height = (int)(floats[3] - floats[1]);

                    int imageX = widthRate.multiply(new BigDecimal(x)).intValue() ;
                    System.out.println("imageX : " + imageX);
                    int imageWidth = widthRate.multiply(new BigDecimal(width)).intValue() ;
                    System.out.println("imageWidth : " + imageWidth);
                    int imageY = originalImageHeight - heightRate.multiply(new BigDecimal(y)).intValue() ;
                    System.out.println("imageY : " + imageY);
                    int imageHeight = heightRate.multiply(new BigDecimal(height)).intValue() ;
                    System.out.println("imageHeight : " + imageHeight);

                    // 切割图片
                    BufferedImage subImage = originalImage.getSubimage(imageX, imageY, imageWidth, imageHeight);


                    ByteArrayOutputStream stream =  new ByteArrayOutputStream();
                    ImageIO.write(subImage, "png", stream);
                    num++ ;
                    org.apache.commons.io.IOUtils.write(stream.toByteArray(),new FileOutputStream(new File("/Users/gongfenglai/Desktop/test/pdf/sub" + num + ".png")));

                }


            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
//    public static void insertImage2(){
//
//        try {
//            byte[] pdfByte = IOUtils.toByteArray(new File("/Users/gongfenglai/Desktop/test/pdf/0112.pdf"));
//            byte[] sealByte = IOUtils.toByteArray(new File("/Users/gongfenglai/Desktop/test/image/e0f209137275d9b1.png"));
//            //加载pdf文件
//            PDDocument pdDocument = Loader.loadPDF(pdfByte);
//            //获取pdf文件第一页
//            PDPage page = pdDocument.getPage(1);
//
//            //创建要插入的图片数据
//            PDImageXObject imageXObject = PDImageXObject.createFromFile("/Users/gongfenglai/Desktop/test/image/e0f209137275d9b1.png",pdDocument);
//            //创建page数据流对象
//            PDPageContentStream contentStream = new PDPageContentStream(pdDocument,page);
//            contentStream.drawImage(imageXObject,100,100,50,50);
//            contentStream.close();
//
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            pdDocument.save(out);
//            pdDocument.close();
//
//            org.apache.commons.io.IOUtils.write(out.toByteArray(),new FileOutputStream(new File("/Users/gongfenglai/Desktop/test/pdf/insert-image-07.pdf")));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//
//    }



    public static void changeImg(){
        try {
//            byte[] sealByte = IOUtils.toByteArray(new File("/Users/gongfenglai/Desktop/test/pdf/sub1.png"));

            BufferedImage bufferedImage = ImageIO.read(new File("/Users/gongfenglai/Desktop/test/pdf/sub1.png"));

            int alpha = 255;
            String removeRgb = null;
            // 遍历Y轴的像素
            for (int y = bufferedImage.getMinY(); y < bufferedImage.getHeight(); y++) {
                // 遍历X轴的像素
                for (int x = bufferedImage.getMinX(); x < bufferedImage.getWidth(); x++) {
                    int rgb = bufferedImage.getRGB(x, y);
                    // 取图片边缘颜色作为对比对象
                    if (y == bufferedImage.getMinY() && x == bufferedImage.getMinX()) {
                        removeRgb = convertRgbStr(rgb);
                    }
                    // 设置为透明背景
                    if (removeRgb.equals(convertRgbStr(rgb))) {
                        alpha = 0;
                    } else {
                        alpha = 255;
                    }
                    rgb = (alpha << 24) | (rgb & 0x00ffffff);
                    bufferedImage.setRGB(x, y, rgb);
                }
            }
            ImageIO.write(bufferedImage, "png", new File("/Users/gongfenglai/Desktop/test/pdf/output3.png"));

//            org.apache.commons.io.IOUtils.write(clip,new FileOutputStream(new File("/Users/gongfenglai/Desktop/test/pdf/output2.png")));
        }catch (Exception e){

        }
    }



    public static void removeColor(){

        try {


        }catch (Exception e){

        }



    }

    public static String convertRgbStr(int color) {
        int red = (color & 0xff0000) >> 16;// 获取color(RGB)中R位
        int green = (color & 0x00ff00) >> 8;// 获取color(RGB)中G位
        int blue = (color & 0x0000ff);// 获取color(RGB)中B位
        return red + "," + green + "," + blue;
    }


    /**
     * 将背景替换为透明
     * @return
     * @throws IOException the io exception
     * @author Fusion
     * @created 2023-08-22 10:25:10 Change img color.
     */
    public static byte[] changeImgColor(byte [] seal) throws IOException {

        ByteArrayInputStream bis = new ByteArrayInputStream(seal);
//        String fileName = file.getName();
        BufferedImage bi =  ImageIO.read(bis);

        BufferedImage image =  bi;
        //将原图片的二进制转化为ImageIcon
        ImageIcon imageIcon = new ImageIcon(image);
        int width = imageIcon.getIconWidth();
        int height = imageIcon.getIconHeight();

        //图片缓冲流
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();
        graphics2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());

        int alpha = 255;

        //这个背景底色的选择，我这里选择的是比较偏的位置，可以修改位置。背景色选择不知道有没有别的更优的方式（比如先过滤一遍获取颜色次数最多的，但是因为感觉做起来会比较复杂没去实现），如果有可以评论。
        int RGB=bufferedImage.getRGB(width-1, height-1);

        for(int i = bufferedImage.getMinX(); i < width; i++) {
            for(int j = bufferedImage.getMinY(); j < height; j++) {

                int rgb = bufferedImage.getRGB(i, j);

                int r = (rgb & 0xff0000) >>16;
                int g = (rgb & 0xff00) >> 8;
                int b = (rgb & 0xff);
                int R = (RGB & 0xff0000) >>16;
                int G = (RGB & 0xff00) >> 8;
                int B = (RGB & 0xff);
                //a为色差范围值，渐变色边缘处理，数值需要具体测试，50左右的效果比较可以
                int a = 5;
                if(Math.abs(R-r) < a && Math.abs(G-g) < a && Math.abs(B-b) < a ) {
                    alpha = 0;
                } else {
                    alpha = 255;
                }
                rgb = (alpha << 24)|(rgb & 0x00ffffff);
                bufferedImage.setRGB(i,j,rgb);
            }
        }

//        String[] split = fileName.split("\\.");
//        fileName = split[0]+"(已转换)."+split[1];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", bos);
        return bos.toByteArray();
    }

    public static byte[] clip(byte[] originByte, Integer colorRange){
        ByteArrayInputStream inputStream = new ByteArrayInputStream(originByte);
//        BufferedImage image = ImageIO.read(new File("/Users/gongfenglai/Desktop/test/D00DCCC0-0D96-4485-BD20-9B1BE3E3EBEC.png"));
        BufferedImage image = null;
        try {
            image = ImageIO.read(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 高度和宽度
        int height = image.getHeight();
        int width = image.getWidth();

        // 生产背景透明和内容透明的图片
        ImageIcon imageIcon = new ImageIcon(image);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics(); // 获取画笔
        g2D.drawImage(imageIcon.getImage(), 0, 0, null); // 绘制Image的图片，使用了imageIcon.getImage()，目的就是得到image,直接使用image就可以的

        int alpha = 0; // 图片透明度
        // 外层遍历是Y轴的像素
        for (int y = bufferedImage.getMinY(); y < bufferedImage.getHeight(); y++) {
            // 内层遍历是X轴的像素
            for (int x = bufferedImage.getMinX(); x < bufferedImage.getWidth(); x++) {
                int rgb = bufferedImage.getRGB(x, y);
                // 对当前颜色判断是否在指定区间内
                if (colorInRange(rgb,colorRange)){
                    // #AARRGGBB 最前两位为透明度
                    rgb = (alpha << 24) | (rgb & 0x00ffffff);
                    bufferedImage.setRGB(x, y, rgb);
                    alpha = 0;
                }else{
                    // 设置为不透明
                    alpha = 255;
                    // #AARRGGBB 最前两位为透明度
                    rgb = (alpha << 24) | (0xff0000);
                    bufferedImage.setRGB(x, y, rgb);
                }

            }
        }
        // 绘制设置了RGB的新图片,这一步感觉不用也可以只是透明地方的深浅有变化而已，就像蒙了两层的感觉
        g2D.drawImage(bufferedImage, 0, 0, null);

        // 生成图片为PNG
//        ImageIO.write(bufferedImage, "png", new File("/Users/gongfenglai/Desktop/test/new_ent4.png"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    // 判断是背景还是内容
    public static boolean colorInRange(int color , Integer colorRange) {
        int red = (color & 0xff0000) >> 16;// 获取color(RGB)中R位
        int green = (color & 0x00ff00) >> 8;// 获取color(RGB)中G位
        int blue = (color & 0x0000ff);// 获取color(RGB)中B位
        // 通过RGB三分量来判断当前颜色是否在指定的颜色区间内
        if (red >= colorRange && green >= colorRange && blue >= colorRange){
            return true;
        }
        return false;
    }


}