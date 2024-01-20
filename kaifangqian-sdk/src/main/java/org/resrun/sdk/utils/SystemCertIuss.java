package org.resrun.sdk.utils;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.resrun.sdk.service.pojo.ApplyCertData;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.security.spec.RSAPublicKeySpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SystemCertIuss {

    private static String algorithmSignature = "SHA256withRSA";


    /**
     *  本地签发证书
     * @param p10
     * @return
     * @throws Exception
     */
    public static ApplyCertData issuance(byte [] p10) throws Exception{

        ApplyCertData applyCert = new ApplyCertData();
        PKCS10CertificationRequest req = new PKCS10CertificationRequest(p10);
        KeyStore store = getRootCert();
        KeyStore.PrivateKeyEntry rootPK = (KeyStore.PrivateKeyEntry) store.getEntry("root",
                new KeyStore.PasswordProtection("123456".toCharArray()));
        X509Certificate rootCert = (X509Certificate) rootPK.getCertificate();

        X500Name issuer = X500Name.getInstance(rootCert.getSubjectX500Principal().getEncoded());

        PrivateKey signPrivateKey  = rootPK.getPrivateKey();



        //证书序列号
        String serial = UUID.randomUUID().toString().replaceAll("-","");
        BigInteger serialBig = str2BigInteger(serial);

        //证书的起始时间
        Date startDate = new Date(System.currentTimeMillis());
        //签发有效期默认一天
        Date expireDate = new Date(System.currentTimeMillis() + 86400000L);

        List<Certificate> chains = new ArrayList<>();

        Certificate [] rootChains = rootPK.getCertificateChain();
        for (int i=0;i<rootChains.length;i++){
            chains.add(rootChains[i]);
        }

        //解析P10中的公钥
        SubjectPublicKeyInfo pkInfo = req.getSubjectPublicKeyInfo();
        RSAKeyParameters rsa = (RSAKeyParameters) PublicKeyFactory.createKey(pkInfo);
        RSAPublicKeySpec rsaSpec = new RSAPublicKeySpec(rsa.getModulus(), rsa.getExponent());
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey rsaPub = kf.generatePublic(rsaSpec);


        Certificate cert = generateV3(issuer, req.getSubject(),serialBig, startDate,expireDate,rsaPub,
                signPrivateKey
                , null);

        chains.add(cert);

        CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
        try {
            CMSProcessableByteArray msg = new CMSProcessableByteArray("".getBytes());

            JcaCertStore jcaCertStore = new JcaCertStore(chains);
            gen.addCertificates(jcaCertStore);

            CMSSignedData signedData = gen.generate(msg);
            SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDDHHmmss");

            applyCert.setCertNO(serial);
            applyCert.setCertSubject(issuer.toString());
            applyCert.setCertSN(serial);
            applyCert.setCertValidityNotBefore(sdf.format(startDate));
            applyCert.setCertValidityNotAfter(sdf.format(expireDate));
            applyCert.setCertValidity(1);
            applyCert.setP7b(Base64.encode(signedData.getEncoded()));


            return applyCert;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Certificate generateV3(X500Name issuer, X500Name subject,
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


    /**
     * 获取根本地根证书
     * @return
     * @throws KeyStoreException
     * @throws IOException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     */
    private static KeyStore getRootCert() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        KeyStore store = KeyStore.getInstance("JKS");
        InputStream inputStream = SystemCertIuss.class.getClassLoader()
                .getResourceAsStream("root/root.jks");
        store.load(inputStream, "123456".toCharArray());
        return  store;
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
}
