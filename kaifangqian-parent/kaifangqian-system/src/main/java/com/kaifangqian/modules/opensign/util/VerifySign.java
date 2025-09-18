/**
 * @description 在线签名验签工具类
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
package com.kaifangqian.modules.opensign.util;

import com.kaifangqian.modules.opensign.enums.SignStatus;
import com.kaifangqian.modules.opensign.vo.base.SignPdfInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Store;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.Security;
import java.util.*;
import java.util.List;

/**
 * @Description: 在线签名验签工具类
 * @Package: com.kaifangqian.modules.opensign.service.doc.impl
 * @ClassName: VerifySign
 * @author: Fusion
 * CreateTime:  2023/8/22  10:53
 * @copyright 北京资源律动科技有限公司
 */
@Slf4j
public class VerifySign {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static SignPdfInfoVo getSignFromPdf(byte[] pdfByte){
        SignPdfInfoVo pdfSignInfo = new SignPdfInfoVo();
        try {
            //加载pdf文件
            PDDocument pdDocument = Loader.loadPDF(pdfByte);
            //获取pdf文件中所有种类的组件
            PDAcroForm acroForm = pdDocument.getDocumentCatalog().getAcroForm(null);

            List<PDField> fields = acroForm.getFields();
            //如果组件为空，则直接返回
            if(fields == null || fields.size() == 0){
                pdfSignInfo.setPdfSingResult(SignStatus.SIGN_STATUS_NOSIGNATURE.getCode());
                return pdfSignInfo ;
            }
            List<SignPdfInfoVo.SignatureDetail> signatureDetails = new ArrayList<>();
            for(PDField field : fields){
                SignPdfInfoVo.SignatureDetail signatureDetail = new SignPdfInfoVo.SignatureDetail();
                //判断当前组件是否为签名字段
                if (field instanceof PDSignatureField){
                    //如果类型为签名字段
                    COSDictionary value = field.getCOSObject().getCOSDictionary(COSName.V);
                    //根据签名字段创建签名对象
                    PDSignature pdSignature = new PDSignature(value);

                    byte[] origPDF = pdSignature.getSignedContent(pdfByte);
                    byte[] sig = pdSignature.getContents(pdfByte);
                    //创建证书对象
                    CMSSignedData cmsSignedData = new CMSSignedData(new CMSProcessableByteArray(origPDF), sig);
                    //获取证书详细信息
                    SignerInformationStore sis = cmsSignedData.getSignerInfos();
                    Collection signers = sis.getSigners();
                    Store certStore = cmsSignedData.getCertificates();
                    Iterator it = signers.iterator();
                    //遍历证书信息
                    while (it.hasNext()) {
                        SignerInformation signer = (SignerInformation) it.next();
                        Collection certCollection = certStore.getMatches(signer.getSID());
                        Iterator certIt = certCollection.iterator();
                        X509CertificateHolder cert = (X509CertificateHolder) certIt.next();
                        //证书序列号
                        signatureDetail.setSerialNumber(cert.getSerialNumber().toString(16));
                        String issuer = IETFUtils.valueToString(cert.getIssuer().getRDNs(BCStyle.CN)[0].getFirst().getValue());
                        // 颁发机构
                        signatureDetail.setUserDnName(issuer);
                        //证书有效期开始时间
                        signatureDetail.setValidStartTime(cert.getNotBefore());
                        //证书有效期结束时间
                        signatureDetail.setValidEndTime(cert.getNotAfter());
                        //证书持有者名称
                        String subject = IETFUtils.valueToString(cert.getSubject().getRDNs(BCStyle.CN)[0].getFirst().getValue());
                        signatureDetail.setCertName(subject);
                        //是否被篡改
                        try {
                            boolean bc = signer.verify(new JcaSimpleSignerInfoVerifierBuilder().build(cert));
                            signatureDetail.setValidate(bc);
                            if(bc){
                                pdfSignInfo.setPdfSingResult(SignStatus.SIGN_STATUS_RIGHT.getCode());
                            }else {
                                pdfSignInfo.setPdfSingResult(SignStatus.SIGN_STATUS_FIDDLE.getCode());
                            }
                        }catch (Exception e){
                            pdfSignInfo.setPdfSingResult(SignStatus.SIGN_STATUS_FIDDLE.getCode());
                        }

                    }
                    //签署时间
                    Date time = pdSignature.getSignDate().getTime();
                    signatureDetail.setSignTime(time);
                    //签名位置
                    String reason = pdSignature.getReason();
                    signatureDetail.setReason(reason);
                    //签名类型
//                    String location = pdSignature.getLocation();
//                    signatureDetail.setLocation(location);
                    signatureDetail.setLocation("可信时间认证机构");
                    //获取对应的签章图片
//                    List<PDAnnotationWidget> widgets = field.getWidgets();
//                    for(PDAnnotationWidget widget : widgets){
//                        PDPage page = widget.getPage();
//                        //先获取pdf文件的真实的宽和高
//                        float pdfHeight = page.getMediaBox().getHeight();
//                        float pdfWidth = page.getMediaBox().getWidth();
//                        PDFRenderer renderer = new PDFRenderer(pdDocument);
//                        //获取页码
//                        int pageIndex = pdDocument.getPages().indexOf(page);
//                        //将对应的页码转换成图片
//                        BufferedImage originalImage = renderer.renderImageWithDPI(pageIndex, 90f);
//                        //获取转换后的图片的宽和高
//                        int originalImageHeight = originalImage.getHeight();
//                        int originalImageWidth = originalImage.getWidth();
//                        //根据pdf文件真实宽高和转换后图片的宽高进行计算，得出比率
//                        BigDecimal heightRate = new BigDecimal(originalImageHeight).divide(new BigDecimal(pdfHeight), 4, RoundingMode.DOWN);
//                        BigDecimal widthRate = new BigDecimal(originalImageWidth).divide(new BigDecimal(pdfWidth), 4, RoundingMode.DOWN);
//
//                        //获取具体的签章位置坐标
//                        COSDictionary cosObject = field.getCOSObject();
//                        COSArray array = (COSArray)cosObject.getDictionaryObject(COSName.RECT);
//
//                        float[] floats = array.toFloatArray();
//                        for(int i = 0 ; i < floats.length ; i++){
//                            System.out.println(floats[i]);
//                        }
//                        float x = (int)floats[0];
//                        float width = (int)(floats[2] - floats[0]);
//                        float y = (int)floats[3];
//                        float height = (int)(floats[3] - floats[1]);
//                        //根据之前的比率，计算出在图片上签章的坐标
//                        int imageX = widthRate.multiply(new BigDecimal(x)).intValue() ;
//                        int imageWidth = widthRate.multiply(new BigDecimal(width)).intValue() ;
//                        int imageY = originalImageHeight - heightRate.multiply(new BigDecimal(y)).intValue() ;
//                        int imageHeight = heightRate.multiply(new BigDecimal(height)).intValue() ;
//
//                        // 切割图片
//                        BufferedImage subImage = originalImage.getSubimage(imageX, imageY, imageWidth, imageHeight);
//
//                        ByteArrayOutputStream stream =  new ByteArrayOutputStream();
//                        ImageIO.write(subImage, "png", stream);
//                        String s = Base64.getEncoder().encodeToString(stream.toByteArray());
//                        //图片base64
//                        signatureDetail.setSealBase64(s);
//                    }
                    signatureDetails.add(signatureDetail);
                }
            }
            pdfSignInfo.setSignatureDetails(signatureDetails);
        }catch (Exception e){
            pdfSignInfo.setPdfSingResult(SignStatus.SIGN_STATUS_ERROR.getCode());
            log.error("取PDF中数字签名信息失败", e.getMessage());
        }
        return pdfSignInfo ;
    }




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



    /**
     * 将背景替换为透明
     * @return
     * @throws IOException the io exception
     * @author Fusion
     * @created 2023-08-22 10:25:10 Change img color.
     */
    public static void changeImgColor(String path) throws IOException {

        File file = new File(path);
        String fileName = file.getName();
        BufferedImage bi =  ImageIO.read(file);

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
                int a = 45;
                if(Math.abs(R-r) < a && Math.abs(G-g) < a && Math.abs(B-b) < a ) {
                    alpha = 0;
                } else {
                    alpha = 255;
                }
                rgb = (alpha << 24)|(rgb & 0x00ffffff);
                bufferedImage.setRGB(i,j,rgb);
            }
        }

        String[] split = fileName.split("\\.");
        fileName = split[0]+"(已转换)."+split[1];
        ImageIO.write(bufferedImage, "png", new File(path));
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


    public static void main(String[] args) throws IOException {
        // checkSignIsValid("/Users/huaiyong/Desktop/B.pdf");
//        File file = new File("F://download//电信电子发票202308142220 (1).pdf");
//        File file = new File("F://download//版权登记申请代理委托合同.pdf");
                File file = new File("F://download//签署文件-1678695004917124386-北京小轻新商贸中心.pdf");

        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
        byte[] b = new byte[1000];
        int n;
        while ((n = fis.read(b)) != -1) {
            bos.write(b, 0, n);
        }
        byte[] data = bos.toByteArray();
         getSignFromPdf(data);
        fis.close();
        // getSignFromPdf("B.pdf");
    }

}
