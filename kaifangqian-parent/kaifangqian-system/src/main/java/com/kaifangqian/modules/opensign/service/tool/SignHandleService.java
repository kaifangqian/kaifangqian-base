//package com.kaifangqian.modules.opensign.service.tool;
//
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Image;
//import com.itextpdf.text.Rectangle;
//import com.itextpdf.text.pdf.AcroFields;
//import com.itextpdf.text.pdf.PdfReader;
//import com.itextpdf.text.pdf.PdfSignatureAppearance;
//import com.itextpdf.text.pdf.PdfStamper;
//import com.itextpdf.text.pdf.security.*;
//import com.kaifangqian.exception.PaasException;
//import com.kaifangqian.modules.cert.enums.CertificateType;
//import com.kaifangqian.modules.cert.pojo.GenerateCertificateInfo;
//import com.kaifangqian.modules.cert.tool.GenerateRootCertificate;
//import com.kaifangqian.modules.opensign.service.tool.pojo.*;
//import com.kaifangqian.utils.IOUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//import org.springframework.stereotype.Service;
//
//import java.io.*;
//import java.security.GeneralSecurityException;
//import java.security.KeyStore;
//import java.security.PrivateKey;
//import java.security.Security;
//import java.security.cert.Certificate;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @Description: 文件签署工具服务类
// * @Package: com.kaifangqian.modules.sign.service.tool
// * @ClassName: SignUtilService
// * @author: FengLai_Gong
// */
//@Slf4j
//@Service
//public class SignHandleService {
//
//
//
//
//    //是否判断校验不校验PDF页码
//    private boolean inspect = true;
//
//    private int certificationLevel = PdfSignatureAppearance.NOT_CERTIFIED;
//
//    private PdfSignatureAppearance.RenderingMode renderingMode = PdfSignatureAppearance.RenderingMode.GRAPHIC;
//
//    private String hashAlgorithm = DigestAlgorithms.SHA256;
//
//    private MakeSignature.CryptoStandard cryptoStandard = MakeSignature.CryptoStandard.CMS;
//
//    private String reason = "防伪造防篡改数字校验"; //原因
//
//    private String location; //位置
//
//    private TSAClient tsaClient; //时间戳服务
//
//    private OcspClient ocspClient;
//
//    public static void main(String[] args) {
//        SignHandleService detached = new SignHandleService();
//
//        try {
//            byte[] pdfFile = IOUtils.toByteArray(new File("/Users/gongfenglai/Desktop/test/pdf/开源工具版说明.pdf"));
//            byte[] signBadge = IOUtils.toByteArray(new File("/Users/gongfenglai/Desktop/test/image/1212cccc.png"));
////            byte[] signBadge = IOUtils.toByteArray(new File("/Users/gongfenglai/Desktop/test/seal/7891.png"));
//
////            byte[] certFile = IOUtils.toByteArray(new File("/Users/gongfenglai/Desktop/test/1669884951129.pfx"));
//
//
//
//            CertificateProperty certificateProperty = new CertificateProperty();
//            try {
//                String rootSubject = "C=CN,ST=北京,L=北京,O=开放签,OU=产品部,CN=开放签_Test";
//                GenerateCertificateInfo rootCert = GenerateRootCertificate.instance(rootSubject, CertificateType.RSA)
//                        .generateCertificate(null, "123456", 100);
//
//                if(rootCert == null || rootCert.getJks() == null){
//                    throw new PaasException("业务线实例-生成根证书失败");
//                }
//                BaseCertificateInfo userCert = new BaseCertificateInfo();
//                userCert.setAlias("root");
//                userCert.setPassword("123456");
//                KeyStore store = KeyStore.getInstance("JKS");
//                store.load(new ByteArrayInputStream(rootCert.getJks()), "123456".toCharArray());
//                userCert.setCert(store);
//                String x500Name = "C=CN,ST=北京,L=北京,O=开放签,OU=产品部,CN=aaa@bbb@18612345678" ;
//                GenerateCertificateInfo userGenerateCert = GenerateRootCertificate.instance(x500Name, CertificateType.RSA)
//                        .generateCertificate(userCert, "123456", 100);
//                if(userGenerateCert == null || userGenerateCert.getPfx() == null){
//                    throw new PaasException("业务线实例-生成证书失败");
//                }
//                certificateProperty.setCertFile(userGenerateCert.getPfx());
//                certificateProperty.setCertType("PKCS12");
//                certificateProperty.setPassword("123456");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            RealPositionProperty position = new RealPositionProperty();
//
//            position.setStartx(200f);
//            position.setEndx(380f);
//            position.setStarty(200f);
//            position.setEndy(240f);
//            position.setPageNum(1);
//
//            byte[] bytes = detached.signingContract(pdfFile, signBadge, certificateProperty, position);
//            OutputStream fos = new FileOutputStream(new File("/Users/gongfenglai/Desktop/test/opensign/1212-001.pdf"));
//            org.apache.commons.io.IOUtils.write(bytes, fos);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
////
////    public List<PositionProperty> getPosition(byte[] filebytes, SignProperties signProperties, Integer userType){
////        // 签名的位置获取
////        List<PositionProperty> positions = null;
////        try {
////            Integer width = Integer.parseInt(signProperties.getSignatureX());
////            Integer height = Integer.parseInt(signProperties.getSignatureY());
////            //如果是个人章，显示为企业章的一半大小
////            if(userType == 1){
////                positions = SignPositonCalculate.getAllPositionByKeyWords(filebytes, signProperties.getKeyWords(), width/2,height/2);
////            }else{
////                positions = SignPositonCalculate.getAllPositionByKeyWords(filebytes, signProperties.getKeyWords(), width,height);
////            }
////            if (positions == null || positions.size() == 0) {
////                // 签署位置获取失败 返回结果
////                throw new RuntimeException("签署位置获取失败");
////            }
////        } catch (Exception e) {
////            //签署位置获取失败 返回结果
////            throw new RuntimeException("签署位置获取失败");
////        }
////        return positions ;
////    }
//
//
//    public byte[] signingContract(byte[] pdfFile, byte[] signBadge, CertificateProperty cert,
//                                  List<RealPositionProperty> positions) throws GeneralSecurityException, IOException, DocumentException {
//
//
//        byte[] sn = pdfFile;
//        for (RealPositionProperty position : positions) {
//            sn = signingContract(sn, signBadge, cert, position);
//        }
//        return sn;
//    }
//
//    /**
//     * 签署合同
//     * @param pdfFile
//     * @param signBadge
//     * @param cert
//     * @param position
//     * @return
//     * @throws GeneralSecurityException
//     * @throws IOException
//     * @throws DocumentException
//     * @auther zzk
//     * 2018年6月27日上午11:07:34
//     */
//    public byte[] signingContract(byte[] pdfFile, byte[] signBadge, CertificateProperty cert,
//                                  RealPositionProperty position) throws GeneralSecurityException, IOException, DocumentException {
//        System.setProperty("javax.xml.parsers.DocumentBuilderFactory","com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
//        Security.addProvider(new BouncyCastleProvider());
//        //1、解析证书
//        // Java 安全属性文件中指定的默认 keystore 类型；如果不存在此类属性，则返回字符串 "jks"。 PKCS12
//        KeyStore ks = KeyStore.getInstance(cert.getCertType());
//        try {
//            char[] chars = cert.getPassword().toCharArray();
//            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(cert.getCertFile());
//            ks.load(byteArrayInputStream, chars);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        // 获取keystore中的所有别名
//        String alias = (String) ks.aliases().nextElement();
//        // 返回：请求的密钥， 入力参数：别名，用于恢复密钥的密码
//        PrivateKey pk = (PrivateKey) ks.getKey(alias, cert.getPassword().toCharArray());
//        // 证书链（按用户证书在前，根证书授权在后的顺序）
//        Certificate[] chain = ks.getCertificateChain(alias);
//
//        byte[] signedFileByte = null ;
//        PdfReader reader = null ;
//        ByteArrayOutputStream signedFile = null ;
//        PdfStamper stamper = null ;
//        try {
//            //2、读取PDF文件
//            reader = new PdfReader(pdfFile);
//            signedFile = new ByteArrayOutputStream();
//            stamper = PdfStamper.createSignature(reader, signedFile, '\0', null, true);
//            //3、给签署属性服务
//            PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
//            if (signBadge == null || position == null) {
//                appearance.setCertificationLevel(certificationLevel);
//            } else {
//                int pageNum = 0;
//                if (inspect) {
//                    //如果检查就会抛出检查异常
//                    pageNum = position.getPageNum();
//                    if (pageNum == 0)
//                        throw new IllegalArgumentException("Pdf page number must be greater than one....!!!");
//                } else {
//                    pageNum = position.getPageNum() <= 0 ? 1 : position.getPageNum();
//                }
//                appearance.setVisibleSignature(new Rectangle(position.getStartx(), position.getStarty(), position.getEndx(), position.getEndy()), pageNum, null);
//                // 添加签章图片
//                Image img = Image.getInstance(signBadge);
//                appearance.setSignatureGraphic(img);
//                appearance.setImageScale(-1);
//                appearance.setCertificationLevel(certificationLevel);
//                appearance.setRenderingMode(renderingMode);
//            }
//            appearance.setReason(reason);
//            appearance.setLocation(location);
//            //4、调用签署  Creating the signature
//            ExternalSignature pks = new PrivateKeySignature(pk, hashAlgorithm, BouncyCastleProvider.PROVIDER_NAME);
//            ExternalDigest digest = new BouncyCastleDigest();
//            MakeSignature.signDetached(appearance, digest, pks, chain, null, ocspClient, tsaClient, 0, cryptoStandard);
//            signedFileByte = signedFile.toByteArray();
//        } catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            // 关闭流
//            if (stamper != null) stamper.close();
//            if (signedFile != null) signedFile.close();
//            if (reader != null) reader.close();
//        }
//        return signedFileByte ;
//    }
//
//
//    public boolean isInspect() {
//        return inspect;
//    }
//
//    public void setInspect(boolean inspect) {
//        this.inspect = inspect;
//    }
//
//    public int getCertificationLevel() {
//        return certificationLevel;
//    }
//
//    public void setCertificationLevel(int certificationLevel) {
//        this.certificationLevel = certificationLevel;
//    }
//
//    public PdfSignatureAppearance.RenderingMode getRenderingMode() {
//        return renderingMode;
//    }
//
//    public void setRenderingMode(PdfSignatureAppearance.RenderingMode renderingMode) {
//        this.renderingMode = renderingMode;
//    }
//
//    public String getHashAlgorithm() {
//        return hashAlgorithm;
//    }
//
//    public void setHashAlgorithm(String hashAlgorithm) {
//        this.hashAlgorithm = hashAlgorithm;
//    }
//
//    public MakeSignature.CryptoStandard getCryptoStandard() {
//        return cryptoStandard;
//    }
//
//    public void setCryptoStandard(MakeSignature.CryptoStandard cryptoStandard) {
//        this.cryptoStandard = cryptoStandard;
//    }
//
//    public String getReason() {
//        return reason;
//    }
//
//    public void setReason(String reason) {
//        this.reason = reason;
//    }
//
//    public String getLocation() {
//        return location;
//    }
//
//    public void setLocation(String location) {
//        this.location = location;
//    }
//
//    public TSAClient getTsaClient() {
//        return tsaClient;
//    }
//
//    public void setTsaClient(TSAClient tsaClient) {
//        this.tsaClient = tsaClient;
//    }
//
//    public OcspClient getOcspClient() {
//        return ocspClient;
//    }
//
//    public void setOcspClient(OcspClient ocspClient) {
//        this.ocspClient = ocspClient;
//    }
//
//
//}