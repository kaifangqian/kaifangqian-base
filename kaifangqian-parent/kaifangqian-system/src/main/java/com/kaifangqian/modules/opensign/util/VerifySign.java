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

import com.alibaba.fastjson.JSONObject;
import com.kaifangqian.modules.opensign.enums.SignStatus;
import com.kaifangqian.modules.opensign.service.tool.WatermarkUtil;
import com.kaifangqian.modules.opensign.vo.base.SignPdfInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceEntry;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.cms.*;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.util.ASN1Dump;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSignerInfoVerifierBuilder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jcajce.provider.digest.SM3;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.tsp.*;
import org.bouncycastle.util.Store;
import org.ofdrw.gm.ses.v4.SES_Signature;
import org.ofdrw.gm.ses.v4.SESeal;
import org.ofdrw.gm.ses.v4.TBS_Sign;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.security.spec.X509EncodedKeySpec;
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
            if(acroForm == null){
                pdfSignInfo.setPdfSingResult(SignStatus.SIGN_STATUS_NOSIGNATURE.getCode());
                return pdfSignInfo ;
            }
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
                    PDAnnotationWidget widget = field.getWidgets().get(0);
                    byte[] origPDF = pdSignature.getSignedContent(pdfByte);
                    byte[] sig = pdSignature.getContents();
//                    pdSignature.getCOSObject().getCOSDictionary()
                    PDAppearanceDictionary appearanceDict = widget.getAppearance();

                    // 检查正常外观
                    PDAppearanceEntry normalAppearance = appearanceDict.getNormalAppearance();
                    PDAppearanceStream appearanceStream = normalAppearance.getAppearanceStream();
                    PDResources resources = appearanceStream.getResources();
                    Iterable<COSName> imageNames = resources.getXObjectNames();
                    int imageCount = 0;

                    for (COSName imageName : imageNames) {
                        if (resources.isImageXObject(imageName)) {
                            PDImageXObject image = (PDImageXObject) resources.getXObject(imageName);

                            BufferedImage bufferedImage = image.getImage();
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            ImageIO.write(bufferedImage, "png", outputStream);
                            byte [] watermarkSeal = WatermarkUtil.watermark(outputStream.toByteArray(),"仅用于文档验签",35,"png");
                            signatureDetail.setSealBase64(Base64.getEncoder().encodeToString(watermarkSeal));
                        }
                    }
                    try {
                        //创建证书对象
                        CMSSignedData cmsSignedData = new CMSSignedData(new CMSProcessableByteArray(origPDF), sig);
                        //获取证书详细信息
//                        SignedData.getInstance()
                        SignerInformationStore sis = cmsSignedData.getSignerInfos();
                        Collection signers = sis.getSigners();

                        Store certStore = cmsSignedData.getCertificates();
                        Iterator it = signers.iterator();
                        //遍历证书信息
                        while (it.hasNext()) {
                            SignerInformation signer = (SignerInformation) it.next();
                            AttributeTable unsignedAttrs =signer.getUnsignedAttributes();
                            if(unsignedAttrs == null){
                                signatureDetail.setLocation("本地时间戳");
                            }else{
                                Attribute timestampAttr = unsignedAttrs.get(
                                        PKCSObjectIdentifiers.id_aa_signatureTimeStampToken
                                );
                                ASN1Set attrValues = timestampAttr.getAttrValues();
                                if (attrValues.size() > 0) {
                                    // 解析时间戳令牌
                                    boolean v = checkTimeStampToken(attrValues.getObjectAt(0).toASN1Primitive().getEncoded());
                                    if (v) {
                                        signatureDetail.setLocation("可信时间认证机构");
                                    } else {
                                        signatureDetail.setLocation("时间戳校验异常");
                                    }
                                }
                            }
                            Collection certCollection = certStore.getMatches(signer.getSID());
                            Iterator certIt = certCollection.iterator();
                            X509CertificateHolder cert = (X509CertificateHolder) certIt.next();
                            String jceName = ComprehensiveOIDConverter.getAlgorithmName(cert.getSignatureAlgorithm().getAlgorithm().toString());
                            signatureDetail.setSigAlgName(jceName);
//                            cert.getSignatureAlgorithm().
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


                                SubjectPublicKeyInfo spki = cert.getSubjectPublicKeyInfo();
                                byte[] encodedKey = spki.getEncoded();
                                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
                                KeyFactory keyFactory = KeyFactory.getInstance(spki.getAlgorithm().getAlgorithm().getId());
                                PublicKey publicKey = keyFactory.generatePublic(keySpec);
                                boolean bc = signer.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(publicKey));

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


                    }catch (Exception e){
                        parseRawASN1(sig,origPDF,signatureDetail);
                        pdfSignInfo.setPdfSingResult(signatureDetail.getPdfSingResult());
                    }
                    //签署时间
                    Date time = pdSignature.getSignDate().getTime();
                    signatureDetail.setSignTime(time);
                    //签名位置
                    String reason = pdSignature.getReason();
                    signatureDetail.setReason(reason);
//                    signatureDetail.setLocation("可信时间认证机构");
                    signatureDetails.add(signatureDetail);

                }
            }
            pdfSignInfo.setSignatureDetails(signatureDetails);
            if(signatureDetails.size()==0){
                pdfSignInfo.setPdfSingResult(SignStatus.SIGN_STATUS_NOSIGNATURE.getCode());
            }
        }catch (Exception e){
//            e.printStackTrace();
            pdfSignInfo.setPdfSingResult(SignStatus.SIGN_STATUS_ERROR.getCode());
            log.error("取PDF中数字签名信息失败", e.getMessage());
        }
//        pdfSignInfo.setPdfSingResult(SignStatus.SIGN_STATUS_RIGHT.getCode());
        return pdfSignInfo ;
    }


    private static void parseRawASN1(byte[] signatureContent,byte [] origPDF,SignPdfInfoVo.SignatureDetail signatureDetail) throws IOException, CertificateException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, TSPException, CMSException, OperatorCreationException {
        ASN1InputStream asn1InputStream = new ASN1InputStream(signatureContent);
        ASN1Primitive primitive = asn1InputStream.readObject();
        try {
            if (primitive instanceof ASN1Sequence) {
                ASN1Sequence sequence = (ASN1Sequence) primitive;
                Enumeration<?> e = sequence.getObjects();
                ASN1Sequence toSign = DLSequence.getInstance(e.nextElement());
                ASN1OctetString cert = DEROctetString.getInstance(e.nextElement());
                ASN1ObjectIdentifier signatureAlgID = ASN1ObjectIdentifier.getInstance(e.nextElement());
                ASN1BitString signature = DERBitString.getInstance(e.nextElement());
//
                Enumeration<?> eSign = toSign.getObjects();
                eSign.nextElement(); // version
//            eSign.nextElement(); // eseal
                ASN1Sequence eSeal = DLSequence.getInstance(eSign.nextElement());
                eSign.nextElement(); // timeInfo 签名时间
                ASN1BitString hashAsn1 = DERBitString.getInstance(eSign.nextElement()); // dataHash
                eSign.nextElement(); // propertyInfo
//            eSign.nextElement(); // extDatas

                byte[] verifyCert = cert.getOctets();
                CertificateFactory certFactory = CertificateFactory.getInstance("X.509", "BC");
                Certificate cert111 = certFactory.generateCertificate(new ByteArrayInputStream(verifyCert));
                System.out.println("ASN.1序列长度: " + sequence.size());


                SES_Signature ses_signature = SES_Signature.getInstance(signatureContent);
                if(ses_signature.getTimeStamp() == null){
//                    System.out.println("本地时间戳");
                    signatureDetail.setLocation("本地时间戳");
                }else{
                    boolean v = checkTimeStampToken(ses_signature.getTimeStamp().getOctets());
                    if (v) {
                        signatureDetail.setLocation("可信时间认证机构");
                    } else {
                        signatureDetail.setLocation("时间戳校验异常");
                    }
                }
                Signature sg = Signature.getInstance(
                        ses_signature.getSignatureAlgID().toString()
                        , new BouncyCastleProvider());
                MessageDigest digest = new SM3.Digest();
                byte[] dataHash = digest.digest(origPDF);
                sg.initVerify(cert111);
                ses_signature.getToSign().setDataHash(dataHash);
                TBS_Sign tbs_toSign = ses_signature.getToSign();
                sg.update(tbs_toSign.getEncoded("DER"));
                byte[] sigVal = ses_signature.getSignature().getBytes();

                boolean bc = sg.verify(sigVal);
                signatureDetail.setValidate(bc);
                if(bc){
                    signatureDetail.setPdfSingResult(SignStatus.SIGN_STATUS_RIGHT.getCode());
                }else {
                    signatureDetail.setPdfSingResult(SignStatus.SIGN_STATUS_FIDDLE.getCode());
                }


                X509CertificateHolder certHolder = new X509CertificateHolder(cert111.getEncoded());
                signatureDetail.setSerialNumber(certHolder.getSerialNumber().toString(16));
                String issuer = IETFUtils.valueToString(certHolder.getIssuer().getRDNs(BCStyle.CN)[0].getFirst().getValue());
                // 颁发机构
//                byte sealImg [] = tbs_toSign.getEseal().geteSealInfo().getPicture().getData().getOctets();
//                signatureDetail.setSealBase64(Base64.getEncoder().encodeToString(sealImg));
//                System.out.println(Base64.getEncoder().encodeToString(sealImg));
//                ASN1ObjectIdentifier objectIdentifier = new ASN1ObjectIdentifier(certHolder.getSignatureAlgorithm().getAlgorithm().toString());
                String jceName = ComprehensiveOIDConverter.getAlgorithmName(certHolder.getSignatureAlgorithm().getAlgorithm().toString());
                signatureDetail.setSigAlgName(jceName);
                signatureDetail.setUserDnName(issuer);
                //证书有效期开始时间
                signatureDetail.setValidStartTime(certHolder.getNotBefore());
                //证书有效期结束时间
                signatureDetail.setValidEndTime(certHolder.getNotAfter());
                //证书持有者名称
                String subject = IETFUtils.valueToString(certHolder.getSubject().getRDNs(BCStyle.CN)[0].getFirst().getValue());
                signatureDetail.setCertName(subject);
            }
        }catch (Exception e){
            signatureDetail.setPdfSingResult(SignStatus.SIGN_STATUS_FIDDLE.getCode());
            throw e;
        }
    }



    public static void main(String[] args) throws IOException {
        // checkSignIsValid("/Users/huaiyong/Desktop/B.pdf");
//        File file = new File("F://download//电信电子发票202308142220 (1).pdf");
//        File file = new File("F://download//版权登记申请代理委托合同.pdf");
                File file = new File("E://work//tem/pdfbox/sign-out-rsa.pdf");
//        File file = new File("E://work//tem/pdfbox/申请人-组庭通知书_2025_35.pdf");

        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
        byte[] b = new byte[1000];
        int n;
        while ((n = fis.read(b)) != -1) {
            bos.write(b, 0, n);
        }
        byte[] data = bos.toByteArray();
        SignPdfInfoVo vo = getSignFromPdf(data);
        System.out.println(JSONObject.toJSONString(vo));
        fis.close();
        // getSignFromPdf("B.pdf");
    }

    public static boolean checkTimeStampToken(byte [] timeTokenByte) throws CertificateException, OperatorCreationException, TSPException, IOException, NoSuchProviderException {

        ContentInfo timeStampInfo = ContentInfo.getInstance(timeTokenByte);
        TimeStampToken stampToken = new TimeStampToken(timeStampInfo);
        Store<X509CertificateHolder> certificates = stampToken.getCertificates();
        List<X509CertificateHolder> ss = (List<X509CertificateHolder>)certificates.getMatches(null);
        X509CertificateHolder certHolder = ss.iterator().next();
        SignerInformationVerifier verifier = new JcaSimpleSignerInfoVerifierBuilder()
                .setProvider("BC")
                .build(certHolder);
        try {
            stampToken.validate(verifier);
//            signatureDetail.setLocation("可信时间认证机构");
            return true;
        }catch (TSPException tse){
//            signatureDetail.setLocation("时间戳校验异常");
        }
        return false;
    }

}

