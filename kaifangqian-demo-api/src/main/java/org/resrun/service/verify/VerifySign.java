//package org.resrun.service.verify;
//
//
//import com.itextpdf.text.pdf.*;
//import com.itextpdf.text.pdf.parser.ContentByteUtils;
//import com.itextpdf.text.pdf.parser.PdfContentStreamProcessor;
//import com.itextpdf.text.pdf.security.CertificateInfo;
//import com.itextpdf.text.pdf.security.PdfPKCS7;
//import org.resrun.enums.SignStatus;
//import org.resrun.service.pojo.SignPdfInfoVo;
//import lombok.extern.slf4j.Slf4j;
//import org.bouncycastle.asn1.ASN1InputStream;
//import org.bouncycastle.asn1.ASN1ObjectIdentifier;
//import org.bouncycastle.asn1.ASN1Sequence;
//import org.bouncycastle.asn1.x509.TBSCertificate;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//
//import javax.imageio.ImageIO;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.*;
//import java.lang.reflect.Field;
//import java.security.GeneralSecurityException;
//import java.security.Principal;
//import java.security.Security;
//import java.security.cert.X509Certificate;
//import java.util.ArrayList;
//import java.util.Base64;
//import java.util.List;
//import java.util.Set;
//
///**
// * @Description: 在线签名验签工具类
// * @Package: org.resrun.service.verify
// * @ClassName: VerifySign
// * @copyright 北京资源律动科技有限公司
// */
//@Slf4j
//public class VerifySign {
//
//    static {
//        Security.addProvider(new BouncyCastleProvider());
//    }
//
//    /**
//     * 验证PDF证书信息是否有效
//     * @throws IOException
//     * @throws GeneralSecurityException
//     */
//    public static Integer checkSignIsValid(String path) {
//        Integer signStatus = SignStatus.SIGN_STATUS_RIGHT.getCode();
//        try{
//
//            FileInputStream fis = new FileInputStream(path);
//            PdfReader reader = new PdfReader(fis);
//            int numberOfPages = reader.getNumberOfPages();
//
//            PdfReader pdfReader = new PdfReader(path);
//            AcroFields acroFields = pdfReader.getAcroFields();
//            List<String> namesList = acroFields.getSignatureNames();
//
//            if(namesList!=null && namesList.size()<=0){
//                signStatus=SignStatus.SIGN_STATUS_NOSIGNATURE.getCode();
//            }
//
//            for (String name : namesList) {
//                PdfDictionary signatureDict = acroFields.getSignatureDictionary(name);
//                //时间戳
//                String timestrap = signatureDict.getAsString(PdfName.M).toString().replace("D:","").substring(0,12);
//
//                PdfPKCS7 pdfPKCS7 = acroFields.verifySignature(name);
//
//                X509Certificate x509Certificate = pdfPKCS7.getSigningCertificate();
//                Principal principal = x509Certificate.getIssuerDN();
//                //证书颁发机构
//                String s = principal.toString().split("CN")[2].replace("=","");
//                //时间戳有效性
//                boolean flag = pdfPKCS7.verifyTimestampImprint();
//                //签署人姓名
//                String signerName = CertificateInfo.getSubjectFields(pdfPKCS7.getSigningCertificate()).getField("CN");  //证书信息
//                //文档是否被修改
//                boolean isChange = pdfPKCS7.verify();
//                if (isChange) {
//                    // log.info("有效签名");
//                    signStatus=SignStatus.SIGN_STATUS_RIGHT.getCode();
//                } else {
//                    // log.info("无效签名");
//                    signStatus=SignStatus.SIGN_STATUS_FIDDLE.getCode();
//                }
//                // log.info(signerName + "\t时间戳是否有效:" +flag + "\t" + timestrap + "\t颁发机构:" +s+ "\t是否被篡改:"+isChange);
//            }
//            return signStatus;
//
//        }catch (Exception e){
//            signStatus=SignStatus.SIGN_STATUS_ERROR.getCode();
//        }
//        return signStatus;
//    }
//
//
//
//    /**
//     * 获取PDF中数字签名信息
//     * @param file pdf文件
//     * @return     提取结果
//     */
//    public static SignPdfInfoVo getSignFromPdf(byte [] file) throws IOException {
//        SignPdfInfoVo pdfSignInfo = new SignPdfInfoVo();
////        String fileBasePath = System.getProperty("user.dir") + "/resrun-paas-system/src/main/resources/upload/";
////        File oldFile = new File(path);
//        try {
//            ByteArrayInputStream fis = new ByteArrayInputStream(file);
//            PdfReader reader = new PdfReader(fis);
//
////            String fileName = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
////            if (!fileBasePath.endsWith("/")) {
////                fileBasePath = fileBasePath + "/";
////            }
//
//
////            pdfSignInfo.setPdfName(path.substring(path.lastIndexOf("/") + 1));
////            pdfSignInfo.setPdfSize(String.valueOf(oldFile.length()));
//            Field rsaDataField = PdfPKCS7.class.getDeclaredField("RSAdata");
//            rsaDataField.setAccessible(true);
//
//            int numberOfPages = reader.getNumberOfPages();
//
//            int xrefSize = reader.getXrefSize();
//
//
////            listener.setBasePath(fileBasePath);
//
//            // 获取acro字段
//            AcroFields fields = reader.getAcroFields();
//            Rectangle pageSize = reader.getPageSize(numberOfPages );
//            // pdf 作用域尺寸（全屏）
//            // System.out.println("pdf rectangle: " + pageSize.getLeft() + "," + pageSize.getBottom() + "," + pageSize.getRight() + "," + pageSize.getTop());
//            // 获取签名名称
//            ArrayList<String> signatureNames = fields.getSignatureNames();
//            if(signatureNames!=null && signatureNames.size()<=0){
//                pdfSignInfo.setPdfSingResult(SignStatus.SIGN_STATUS_NOSIGNATURE.getCode());
//            }
//            for (String name : signatureNames) {
//                ExtImageRenderListener listener = new ExtImageRenderListener();
//                // 指定提供者 获取签名的pkcs7数据
//                PdfPKCS7 pkcs7 = fields.verifySignature(name);
//                X509Certificate certificate = pkcs7.getSigningCertificate();
//                SignPdfInfoVo.SignatureDetail info = new SignPdfInfoVo.SignatureDetail();
//                boolean isChange = pkcs7.verify();
//                if (isChange) {
//                    pdfSignInfo.setPdfSingResult(SignStatus.SIGN_STATUS_RIGHT.getCode());
//                } else {
//                    pdfSignInfo.setPdfSingResult(SignStatus.SIGN_STATUS_FIDDLE.getCode());
//                }
//
//                String[] appearanceStates = fields.getAppearanceStates(name);
//                // 表单域的位置
//                List<AcroFields.FieldPosition> fieldPositions = fields.getFieldPositions(name);
//                for (AcroFields.FieldPosition fieldPosition : fieldPositions) {
//                    Rectangle position = fieldPosition.position;
//
//                    info.pageNum = fieldPosition.page;
//                    info.llx = position.getLeft();
//                    info.lly = position.getBottom();
//                    info.urx = position.getRight();
//                    info.ury = position.getTop();
//                }
//
//                AcroFields.Item fieldItem = fields.getFieldItem(name);
//                int size = fieldItem.size();
//                for (int i = 0; i < size; i++) {
//                    PdfDictionary value = fieldItem.getValue(i);
//                    Set<PdfName> keys = value.getKeys();
//                    for (PdfName key : keys) {
//                        String keyName = PdfName.decodeName(new String(key.getBytes()));
//                        if ("Rect".equalsIgnoreCase(keyName)) {
//                            log.debug("签名域的坐标为：" + value.get(key));
//                        }
//                    }
//                    PdfObject pdfObject = value.get(PdfName.AP);
//                    if (pdfObject != null) {
//                        PdfDictionary dictionary = (PdfDictionary) pdfObject;
//                        PdfIndirectReference ref = (PdfIndirectReference) dictionary.get(PdfName.N);
//                        int number = ref.getNumber();
//
//                        // 根据ap 获取签章图片
//                        PdfObject pdfObjectRelease = reader.getPdfObject(number);
//                        if (pdfObjectRelease instanceof PdfStream) {
//                            PdfStream s = (PdfStream) pdfObjectRelease;
//                            PdfDictionary resources = s.getAsDict(PdfName.RESOURCES);
//                            listener.setI(number);
//                            try {
//
//                                PdfContentStreamProcessor processor = new PdfContentStreamProcessor(listener);
//                                processor.processContent(ContentByteUtils.getContentBytesFromContentObject(s), resources);
//                                if(listener.getSeal() != null && listener.getSeal().length>0){
//                                    byte [] seal = changeImgColor(listener.getSeal());
//                                    info.sealBase64 = Base64.getEncoder().encodeToString(seal);
//                                }
//                            } catch (Exception ignore) {
//                                ignore.printStackTrace();
//                                pdfSignInfo.setPdfSingResult(SignStatus.SIGN_STATUS_ERROR.getCode());
//                            }
//                        }
//                    }
//                }
////                certificate.getSignature()
////                TBSCertificate tbsCertificate = new TBSCertificate();
////                pkcs7.getSigningCertificate();
//                ByteArrayInputStream bIn = new ByteArrayInputStream(certificate.getTBSCertificate());
//                ASN1InputStream aIn = new ASN1InputStream(bIn);
//                ASN1Sequence sequence = (ASN1Sequence) aIn.readObject();
//                TBSCertificate tbs = TBSCertificate.getInstance(sequence);
//                ASN1ObjectIdentifier asn1ObjectIdentifierCN = new ASN1ObjectIdentifier("2.5.4.3");
//                ASN1ObjectIdentifier asn1ObjectIdentifierO = new ASN1ObjectIdentifier("2.5.4.10");
//                if(pkcs7.getTimeStampToken() == null){
//                    info.location = "本地时间";
//                }else{
//                    info.location = "可信时间认证机构";
//                }
//                // 签名信息
//                info.signName = name;
//                info.signTime = pkcs7.getSignDate().getTime();
//                info.validStartTime = certificate.getNotBefore();
//                info.validEndTime = certificate.getNotAfter();
////                info.certName = certificate.getSubjectDN().getName();
//                info.certName = tbs.getSubject().getRDNs(asn1ObjectIdentifierCN)[0].getFirst().getValue().toString();
////                info.serialNumber = String.valueOf(certificate.getSerialNumber());
//                info.serialNumber =  tbs.getSerialNumber().getValue().toString(16);
//                Base64.Encoder encoder = Base64.getEncoder();
//                info.publicKey = encoder.encodeToString(certificate.getPublicKey().getEncoded());
//                info.pubKeyFormat = certificate.getPublicKey().getFormat();
//                info.sigAlgName = certificate.getSigAlgName();
////                info.userDnName = certificate.getIssuerDN().getName();
//                info.userDnName  = tbs.getIssuer().getRDNs(asn1ObjectIdentifierCN)[0].getFirst().getValue().toString();
//                info.reason = pkcs7.getReason();
////                info.location = pkcs7.getLocation();
//                info.validate = pkcs7.verify();
//                Field digestAttrField = PdfPKCS7.class.getDeclaredField("digestAttr");
//                digestAttrField.setAccessible(true);
//                byte[] digestAttr = (byte[]) digestAttrField.get(pkcs7);
//                info.fileDigest = encoder.encodeToString(digestAttr);
//
//
//                pdfSignInfo.getSignatureDetails().add(info);
//
//            }
//        } catch (Exception e) {
//            pdfSignInfo.setPdfSingResult(SignStatus.SIGN_STATUS_ERROR.getCode());
//            log.error("取PDF中数字签名信息失败", e.getMessage());
//        }
//        return pdfSignInfo;
//    }
//
//
//
//    /**
//     * 将背景替换为透明
//     * @return
//     * @throws IOException the io exception
//     */
//    public static void changeImgColor(String path) throws IOException {
//
//        File file = new File(path);
//        String fileName = file.getName();
//        BufferedImage bi =  ImageIO.read(file);
//
//        BufferedImage image =  bi;
//        //将原图片的二进制转化为ImageIcon
//        ImageIcon imageIcon = new ImageIcon(image);
//        int width = imageIcon.getIconWidth();
//        int height = imageIcon.getIconHeight();
//
//        //图片缓冲流
//        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
//        Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();
//        graphics2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
//
//        int alpha = 255;
//
//        //这个背景底色的选择，我这里选择的是比较偏的位置，可以修改位置。背景色选择不知道有没有别的更优的方式（比如先过滤一遍获取颜色次数最多的，但是因为感觉做起来会比较复杂没去实现），如果有可以评论。
//        int RGB=bufferedImage.getRGB(width-1, height-1);
//
//        for(int i = bufferedImage.getMinX(); i < width; i++) {
//            for(int j = bufferedImage.getMinY(); j < height; j++) {
//
//                int rgb = bufferedImage.getRGB(i, j);
//
//                int r = (rgb & 0xff0000) >>16;
//                int g = (rgb & 0xff00) >> 8;
//                int b = (rgb & 0xff);
//                int R = (RGB & 0xff0000) >>16;
//                int G = (RGB & 0xff00) >> 8;
//                int B = (RGB & 0xff);
//                //a为色差范围值，渐变色边缘处理，数值需要具体测试，50左右的效果比较可以
//                int a = 45;
//                if(Math.abs(R-r) < a && Math.abs(G-g) < a && Math.abs(B-b) < a ) {
//                    alpha = 0;
//                } else {
//                    alpha = 255;
//                }
//                rgb = (alpha << 24)|(rgb & 0x00ffffff);
//                bufferedImage.setRGB(i,j,rgb);
//            }
//        }
//
//        String[] split = fileName.split("\\.");
//        fileName = split[0]+"(已转换)."+split[1];
//        ImageIO.write(bufferedImage, "png", new File(path));
//    }
//
//    /**
//     * 将背景替换为透明
//     * @return
//     * @throws IOException the io exception
//     */
//    public static byte[] changeImgColor(byte [] seal) throws IOException {
//
//        ByteArrayInputStream bis = new ByteArrayInputStream(seal);
////        String fileName = file.getName();
//        BufferedImage bi =  ImageIO.read(bis);
//
//        BufferedImage image =  bi;
//        //将原图片的二进制转化为ImageIcon
//        ImageIcon imageIcon = new ImageIcon(image);
//        int width = imageIcon.getIconWidth();
//        int height = imageIcon.getIconHeight();
//
//        //图片缓冲流
//        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
//        Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();
//        graphics2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
//
//        int alpha = 255;
//
//        //这个背景底色的选择，我这里选择的是比较偏的位置，可以修改位置。背景色选择不知道有没有别的更优的方式（比如先过滤一遍获取颜色次数最多的，但是因为感觉做起来会比较复杂没去实现），如果有可以评论。
//        int RGB=bufferedImage.getRGB(width-1, height-1);
//
//        for(int i = bufferedImage.getMinX(); i < width; i++) {
//            for(int j = bufferedImage.getMinY(); j < height; j++) {
//
//                int rgb = bufferedImage.getRGB(i, j);
//
//                int r = (rgb & 0xff0000) >>16;
//                int g = (rgb & 0xff00) >> 8;
//                int b = (rgb & 0xff);
//                int R = (RGB & 0xff0000) >>16;
//                int G = (RGB & 0xff00) >> 8;
//                int B = (RGB & 0xff);
//                //a为色差范围值，渐变色边缘处理，数值需要具体测试，50左右的效果比较可以
//                int a = 5;
//                if(Math.abs(R-r) < a && Math.abs(G-g) < a && Math.abs(B-b) < a ) {
//                    alpha = 0;
//                } else {
//                    alpha = 255;
//                }
//                rgb = (alpha << 24)|(rgb & 0x00ffffff);
//                bufferedImage.setRGB(i,j,rgb);
//            }
//        }
//
////        String[] split = fileName.split("\\.");
////        fileName = split[0]+"(已转换)."+split[1];
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        ImageIO.write(bufferedImage, "png", bos);
//        return bos.toByteArray();
//    }
//
//
//    public static void main(String[] args) throws IOException {
//        // checkSignIsValid("/Users/huaiyong/Desktop/B.pdf");
////        File file = new File("F://download//电信电子发票202308142220 (1).pdf");
////        File file = new File("F://download//版权登记申请代理委托合同.pdf");
//                File file = new File("F://download//签署文件-1678695004917124386-北京小轻新商贸中心.pdf");
//
//        FileInputStream fis = new FileInputStream(file);
//        ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
//        byte[] b = new byte[1000];
//        int n;
//        while ((n = fis.read(b)) != -1) {
//            bos.write(b, 0, n);
//        }
//        byte[] data = bos.toByteArray();
//         getSignFromPdf(data);
//        fis.close();
//        // getSignFromPdf("B.pdf");
//    }
//
//}
