package org.resrun.service.cert;

import org.resrun.sdk.service.pojo.BaseCertificateInfo;
import org.resrun.service.pojo.GenerateCertificateInfo;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * @Description: 证书业务
 * @Package: org.resrun.service.cert
 * @ClassName: CertService
 * @copyright 北京资源律动科技有限公司
 */
@Service
public class CertService {


    //SHA256WITHRSA

    private String algorithmSignature = "SHA256withRSA";

    private String password = "123456" ;



    /**
     * 设置证书签名算法  SHA1withRSA  SHA256withRSA
     * @param algorithmSignature
     */
    public void setAlgorithmSignature(String algorithmSignature){
        this.algorithmSignature = algorithmSignature;
    }


    /**
     * 证书类型
     * @param lifespan 单位年
     *
     * @return
     */
    public GenerateCertificateInfo generateCertificate(BaseCertificateInfo root, String subject,int lifespan) throws Exception {
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
        Date expireDate = new Date(System.currentTimeMillis() + (86400000L * 365 * lifespan));

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
        certificateInfo.setAlgorithm("RSA");
        certificateInfo.setAlgorithmSignature(algorithmSignature);
        certificateInfo.setPassword(password);
        certificateInfo.setCertFileType("jks");
        certificateInfo.setSerial(serial);
        certificateInfo.setJks(outputStream.toByteArray());
        certificateInfo.setPfx(coverToPfx(certificateInfo.getJks(),certificateInfo.getPassword()));

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


//    public static void main(String[] args) throws Exception {
////
//        KeyStore store = loadJKS("Ting111");
//        BaseCertificateInfo baseCertificateInfo = new BaseCertificateInfo();
//        baseCertificateInfo.setAlias("root");
//        baseCertificateInfo.setCert(store);
//        baseCertificateInfo.setPassword("123456");
//        String x500Name = "CN=Ting222,OU=研发部,O=资源律动,L=BeiJing,ST=BeiJing,C=CN";
//
//        GenerateCertificateInfo certificateInfo = GenerateRootCertificate.instance(x500Name, CertificateType.RSA).generateCertificate(baseCertificateInfo,"123456",10);
//        FileUtils.writeByteArrayToFile(new File("C:\\Users\\Administrator\\Desktop\\tem\\cert\\Ting222.jks"), certificateInfo.getJks());
//
//    }

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

    public byte [] coverToPfx(byte [] jks, String password) {
        try {

            KeyStore inputKeyStore = KeyStore.getInstance("JKS");
            ByteArrayInputStream inputStream = new ByteArrayInputStream(jks);
            inputKeyStore.load(inputStream, password.toCharArray());

            KeyStore outputKeyStore = KeyStore.getInstance("PKCS12");

            outputKeyStore.load(null, password.toCharArray());

            Enumeration enums = inputKeyStore.aliases();

            while (enums.hasMoreElements()) { //   we   are   readin   just   one   certificate.

                String keyAlias = (String) enums.nextElement();

                if (inputKeyStore.isKeyEntry(keyAlias)) {
                    Key key = inputKeyStore.getKey(keyAlias, password.toCharArray());
                    Certificate[] certChain = inputKeyStore
                            .getCertificateChain(keyAlias);

                    outputKeyStore.setKeyEntry("", key, password
                            .toCharArray(), certChain);
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            outputKeyStore.store(out, password.toCharArray());
            out.close();
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static KeyStore loadJKS(String name) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        KeyStore store = KeyStore.getInstance("JKS");
        File file = new File(""+name+".jks");
        store.load(new FileInputStream(file), "123456".toCharArray());
        return store;
    }


}