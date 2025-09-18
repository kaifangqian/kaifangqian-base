package com.kaifangqian.modules.cert.tool;

import com.kaifangqian.modules.cert.enums.CertificateType;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.opensign.service.tool.CertificateUtils;
import com.kaifangqian.modules.opensign.service.tool.pojo.BaseCertificateInfo;
import com.kaifangqian.modules.cert.pojo.GenerateCertificateInfo;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class GenerateRootCertificate {

    //SHA256WITHRSA


    private String subject;

    private String algorithmSignature = "SHA256withRSA";

    private CertificateType certificateType;

    private GenerateRootCertificate(String subject,CertificateType certificateType){
        this.subject = subject;
        this.certificateType = certificateType;
    }


    /**
     * @param subject 证书主题
     *                可通过 CertificateUtils.buildSubject进行生成
     */
    public static GenerateRootCertificate instance(String subject,CertificateType certificateType){
        return new GenerateRootCertificate(subject,certificateType);
    }


    /**
     * 设置证书签名算法  SHA1withRSA  SHA256withRSA
     * @param algorithmSignature
     */
    public void setAlgorithmSignature(String algorithmSignature){
        this.algorithmSignature = algorithmSignature;
    }


    /**
     * 证书类型
     * @param password
     * @param time 时间数量
     * @param timeType 时间单位类型，1是天，2是年
     *
     * @return
     */
    public GenerateCertificateInfo generateCertificate(BaseCertificateInfo root, String password, int time,int timeType) throws Exception {
        X500Name issuer = new X500Name(subject);
        KeyStore.PrivateKeyEntry rootPK = null;
        PrivateKey signPrivateKey = null;
        List<Certificate> chains = new ArrayList<>(5);
        if(root != null){
            rootPK = (KeyStore.PrivateKeyEntry) root.getCert().getEntry(root.getAlias(),
                    new KeyStore.PasswordProtection(root.getPassword().toCharArray()));
            X509Certificate rootCert = (X509Certificate) rootPK.getCertificate();

            issuer = X500Name.getInstance(rootCert.getSubjectX500Principal().getEncoded());

            signPrivateKey = rootPK.getPrivateKey();

            Certificate [] rootChains = rootPK.getCertificateChain();
            for (int i=0;i<rootChains.length;i++){
                chains.add(rootChains[i]);
            }
        }

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair keyPair = kpg.generateKeyPair();

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);

        Date startDate = new Date(System.currentTimeMillis());
        Date expireDate = null ;
        if(timeType == 1){
            //天数
            expireDate  = new Date(System.currentTimeMillis() + (86400000L * time));
        }else if(timeType == 2){
            //年数
            expireDate  = new Date(System.currentTimeMillis() + (86400000L * 365 * time));
        }else {
            //默认天数
            expireDate  = new Date(System.currentTimeMillis() + (86400000L * time));
        }


        String serial = UUID.randomUUID().toString().replaceAll("-","");
        BigInteger serialBig = str2BigInteger(serial);

        if(signPrivateKey == null){
            signPrivateKey = keyPair.getPrivate();
        }
        Certificate cert = generateV3(issuer, new X500Name(subject),serialBig, startDate,expireDate, keyPair.getPublic(),
                signPrivateKey
                , null);

        //用户证书要放在证书链的最前面
        chains.add(0,cert);

        keyStore.setKeyEntry("root", keyPair.getPrivate(), password.toCharArray(), chains.toArray(new Certificate[chains.size()]));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        keyStore.store(outputStream,password.toCharArray());


        GenerateCertificateInfo certificateInfo = new GenerateCertificateInfo();
        certificateInfo.setAlgorithm(certificateType.name());
        certificateInfo.setAlgorithmSignature(algorithmSignature);
        certificateInfo.setPassword(password);
        certificateInfo.setCertFileType("jks");
        certificateInfo.setSerial(serial);
        certificateInfo.setJks(outputStream.toByteArray());
        certificateInfo.setPfx(CertificateUtils.coverToPfx(certificateInfo.getJks(),certificateInfo.getPassword()));

        certificateInfo.setTermOfValidityStartTime(startDate);
        certificateInfo.setTermOfValidityEndTime(expireDate);

        return certificateInfo;
    }

    public Certificate generateV3(X500Name issuer, X500Name subject,
                                         BigInteger serial, Date startDate, Date expireDate,
                                         PublicKey publicKey, PrivateKey privKey, List<Extension> extensions) throws IOException, CertificateException, OperatorCreationException {

        X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(
                issuer, serial, startDate, expireDate,
                subject, publicKey);
        ContentSigner sigGen = new JcaContentSignerBuilder(algorithmSignature).build(privKey);
        //privKey是CA的私钥，publicKey是待签名的公钥，那么生成的证书就是被CA签名的证书。
        if (extensions != null){
            for (Extension ext : extensions) {
                builder.addExtension(ext.getExtnId(), ext.isCritical(),ext.getExtnValue());
            }
        }
        X509CertificateHolder holder = builder.build(sigGen);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream is1 = new ByteArrayInputStream(holder.toASN1Structure()
                .getEncoded());
        X509Certificate theCert = (X509Certificate) cf.generateCertificate(is1);
        is1.close();
        return theCert;
    }


    public static void main(String[] args) throws Exception {
//
//        KeyStore store = loadJKS("Ting111");
//        BaseCertificateInfo baseCertificateInfo = new BaseCertificateInfo();
//        baseCertificateInfo.setAlias("root");
//        baseCertificateInfo.setCert(store);
//        baseCertificateInfo.setPassword("123456");
//        String x500Name = "CN=电子签章系统,OU=产品部,O=电子签,L=BeiJing,ST=BeiJing,C=CN";
        String x500Name = "C=CN,ST=北京,L=北京,O=电子签,OU=产品部,CN=平台防篡改证书";

        byte[] rootCert = IOUtils.toByteArray(new FileInputStream(new File("/Users/kaifangqian/file/download_file_root_ca.jks")));
        if (rootCert == null) {
            throw new PaasException("没有根证书");
        }
        BaseCertificateInfo baseCertificateInfo = new BaseCertificateInfo();
        baseCertificateInfo.setAlias("root");
        baseCertificateInfo.setCert(loadJks(rootCert, "123456"));
        baseCertificateInfo.setPassword("123456");

//        GenerateCertificateInfo certificateInfo = GenerateRootCertificate.instance(x500Name,CertificateType.RSA).generateCertificate(null,"123456",10,2);
        GenerateCertificateInfo certificateInfo = GenerateRootCertificate.instance(x500Name,CertificateType.RSA).generateCertificate(baseCertificateInfo,"123456",10,2);
//        FileUtils.writeByteArrayToFile(new File("C:\\Users\\Administrator\\Desktop\\tem\\cert\\Ting222.jks"), certificateInfo.getJks());
        IOUtils.write(certificateInfo.getPfx(),new FileOutputStream(new File("/Users/kaifangqian/file/download_file_ca.pfx")));
//        IOUtils.write(certificateInfo.getJks(),new FileOutputStream(new File("/Users/kaifangqian/file/download_file_root_ca.jks")));

    }

    private static BigInteger str2BigInteger(String str){
        StringBuffer sb = new StringBuffer();
        //将字符串转换为字符数组
        char ch[] = str.toCharArray();
        for(int i = 0; i < ch.length; i++) {
            String hexString = Integer.toHexString(ch[i]);
            sb.append(hexString);
        }
        return new BigInteger(sb.toString());
    }

    private static String big2String(String str){
        String result = new String();
        char[] charArray = str.toString().toCharArray();
        for(int i = 0; i < charArray.length; i=i+2) {
            String st = ""+charArray[i]+""+charArray[i+1];
            char ch1 = (char)Integer.parseInt(st, 16);
            result = result + ch1;
        }
        return result;
    }



    public static KeyStore loadJKS(String name) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        KeyStore store = KeyStore.getInstance("JKS");
        File file = new File(""+name+".jks");
        store.load(new FileInputStream(file), "123456".toCharArray());
        return store;
    }

    public static KeyStore loadJks(byte[] certByte, String password) {
        KeyStore store = null;
        try {
            store = KeyStore.getInstance("JKS");
            ByteArrayInputStream inputStream = new ByteArrayInputStream(certByte);
            store.load(inputStream, password.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return store;
    }

}
