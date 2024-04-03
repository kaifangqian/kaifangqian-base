package org.resrun.service.verify;

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
import org.resrun.enums.SignStatus;
import org.resrun.service.pojo.SignPdfInfoVo;
import org.resrun.service.pojo.SignatureDetail;
import java.security.Security;
import java.util.List;
import java.util.*;

/**
 * @Description: 在线签名验签工具类
 * @Package: com.resrun.modules.opensign.service.doc.impl
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
            List<SignatureDetail> signatureDetails = new ArrayList<>();
            for(PDField field : fields){
                SignatureDetail signatureDetail = new SignatureDetail();
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
                    signatureDetail.setLocation("可信时间认证机构");
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
}
