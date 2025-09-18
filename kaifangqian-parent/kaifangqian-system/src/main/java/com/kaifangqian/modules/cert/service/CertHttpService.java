package com.kaifangqian.modules.cert.service;

import com.alibaba.fastjson.JSONObject;
import com.kaifangqian.modules.cert.pojo.ApplyCert;
import com.kaifangqian.modules.cert.pojo.CertificationInfo;
import com.kaifangqian.modules.opensign.utils.Base64;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.security.auth.x500.X500Principal;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * @Description: CertHttpService
 * @Package: com.kaifangqian.modules.cert.service
 * @ClassName: CertHttpService
 * @author: FengLai_Gong
 */
@Service
public class CertHttpService {

    @Value("${service.token}")
    private String token ;
    @Value("${service.manage.cert-apply-url}")
    private String certApplyUrl ;
    @Value("${service.manage.individual-cert-apply-url}")
    private String individualCertApplyUrl ;
    @Value("${service.manage.enterprise-cert-apply-url}")
    private String enterpriseCertApplyUrl ;

    public static final String password = "123456";

    public static final String keyName = "myKey";

    private static KeyPairGenerator kpg = null;

    public ApplyCert certIndividual(CertificationInfo info){
        ApplyCert cert = null ;
        if(info != null && info.getP10() != null){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token",token);
            jsonObject.put("p10",Base64.encode(info.getP10()));
            jsonObject.put("uniqueCode",UUID.randomUUID().toString());
            String returnJson = null ;
            try {
                returnJson  = HttpUtils.executePost(individualCertApplyUrl, jsonObject.toJSONString(), new HashMap<>());
                if(returnJson != null && returnJson.length() > 0){
                    cert = JSONObject.parseObject(returnJson, ApplyCert.class);
                    return cert ;
                }
                System.out.println(returnJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cert ;
    }

    public ApplyCert certEnterprise(CertificationInfo info){
        ApplyCert cert = null ;
        if(info != null && info.getP10() != null){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token",token);
            jsonObject.put("p10",Base64.encode(info.getP10()));
            jsonObject.put("uniqueCode",UUID.randomUUID().toString());
            String returnJson = null ;
            try {
                returnJson  = HttpUtils.executePost(enterpriseCertApplyUrl, jsonObject.toJSONString(), new HashMap<>());
                if(returnJson != null && returnJson.length() > 0){
                    cert = JSONObject.parseObject(returnJson, ApplyCert.class);
                    return cert ;
                }
                System.out.println(returnJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cert ;
    }


    public ApplyCert certEvent(CertificationInfo info){
        ApplyCert cert = null ;
        if(info != null && info.getP10() != null){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token",token);
            jsonObject.put("p10",Base64.encode(info.getP10()));
            jsonObject.put("uniqueCode",UUID.randomUUID().toString());
            String returnJson = null ;
            try {
                returnJson  = HttpUtils.executePost(certApplyUrl, jsonObject.toJSONString(), new HashMap<>());
                if(returnJson != null && returnJson.length() > 0){
                    cert = JSONObject.parseObject(returnJson, ApplyCert.class);
                    return cert ;
                }
                System.out.println(returnJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cert ;
    }


//
//    public ShortTermVo applyCert(CertificationInfo info){
//        ShortTermVo shortTermVo = null ;
//        if(info != null && info.getP10() != null){
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("token",zrhyToken);
//            jsonObject.put("p10",Base64.encode(info.getP10()));
//            String returnJson = null ;
//            try {
//                returnJson  = HttpUtils.executePost(zrhyUrl, jsonObject.toJSONString(), new HashMap<>());
//                if(returnJson != null && returnJson.length() > 0){
//                    shortTermVo = JSONObject.parseObject(returnJson, ShortTermVo.class);
//                    return shortTermVo ;
//                }
//                System.out.println(returnJson);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return shortTermVo ;
//    }


//    public static void main(String[] args) {
//        CertHttpService certHttpService = new CertHttpService();
//        String dn = "C=CN,ST=北京,L=北京,O=开放签,OU=产品部,CN=开放签_Test@测试证书" ;
//        CertificationInfo info = new CertificationInfo();
//        info.setCertDn(dn);
//        info.setKeyName(keyName);
//        info.setPassword(password);
//        info = certHttpService.generateP10(info);
//
//        if(info != null && info.getP10() != null){
//            String token = "fafagasgasgas";
//            String url = "http://192.168.0.201:8090/service/cert/apply" ;
////            String url = "http://192.168.0.201:8090/service/cert/event" ;
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("token",token);
//            jsonObject.put("p10",Base64.encode(info.getP10()));
//            String returnJson = null ;
//            try {
////                IOUtils.write(info.getJksFile(),new FileOutputStream(new File("/Users/gongfenglai/Desktop/test/cert/1229.jks")));
//                returnJson  = HttpUtils.executePost(url, jsonObject.toJSONString(), new HashMap<>());
//                if(returnJson != null && returnJson.length() > 0){
//                    JSONObject returnObject = JSONObject.parseObject(returnJson);
////                    String p7b = (String)returnObject.get("p7b");
////                    if(p7b == null || p7b.length() == 0){
////                        System.out.println("p7b为空了");
////                    }
////                    byte[] p7bByte = Base64.decode(p7b);
////                    IOUtils.write(p7bByte,new FileOutputStream(new File("/Users/gongfenglai/Desktop/test/cert/1229.p7b")));
////                    if(p7bByte == null){
////                        System.out.println("p7b为空了");
////                    }
////                    info.setP7bFile(p7bByte);
////                    certHttpService.p7bToJks(info);
////                    byte[] pfx = CertificateUtils.coverToPfx(info.getCertFile(), info.getPassword());
////                    info.setPfxFile(pfx);
////
////                    if(info.getPfxFile() == null){
////                        System.out.println("失败了");
////                    }
////                    IOUtils.write(info.getPfxFile(),new FileOutputStream(new File("/Users/gongfenglai/Desktop/test/cert/1229-02.pfx")));
//                }
//                System.out.println(returnJson);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//    }







    public CertificationInfo generateP10(CertificationInfo certificationInfo) {
        try {
            X509Certificate cert = null;
            ByteArrayOutputStream keystoreFile = null;
            String signAlgorithm = "SHA1WithRSA"; // 设置证书的加密方式
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            KeyStore store = KeyStore.getInstance("JKS");
//            KeyStore store = KeyStore.getInstance(KeyStore.getDefaultType());
            store.load(null, null);
            kpg = KeyPairGenerator.getInstance("RSA");// 设置证书的加密方式
            //cfca需要2048长度的秘钥
            kpg.initialize(2048);
            KeyPair keyPair = kpg.generateKeyPair();
            PublicKey pubKey = keyPair.getPublic();
            // 私钥
            PrivateKey priKey = keyPair.getPrivate();
            X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
            X500Principal xp = new X500Principal(certificationInfo.getCertDn());
//		System.out.println("证书标题：" + certDn + "---" + keyName);
            certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
            // 设置颁发者
            certGen.setIssuerDN(xp);
            // 设置使用者
            // 设置有效期
            certGen.setNotBefore(new Date());

            Calendar calendar = Calendar.getInstance();
            Date date = new Date(System.currentTimeMillis());
            calendar.setTime(date);
            calendar.add(Calendar.YEAR, +1);
            certGen.setNotAfter(calendar.getTime());

            certGen.setSubjectDN(xp);
            // 公钥
            certGen.setPublicKey(pubKey);
            certGen.setSignatureAlgorithm(signAlgorithm);
            cert = certGen.generateX509Certificate(keyPair.getPrivate(), "BC");
            // 将证书信息写入到jks中
            store.setKeyEntry(keyName, keyPair.getPrivate(), password.toCharArray(), new Certificate[] { cert });
            PKCS10CertificationRequest p10CertificationRequest = new PKCS10CertificationRequest(signAlgorithm, xp,
                    keyPair.getPublic(), null, keyPair.getPrivate());
            // 得到p10格式数据
            certificationInfo.setP10(p10CertificationRequest.getEncoded());
            keystoreFile = new ByteArrayOutputStream();
            store.store(keystoreFile, password.toCharArray()); // 获取生成数字证书

            certificationInfo.setJksFile(keystoreFile.toByteArray());

            if (keystoreFile != null)
                keystoreFile.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return certificationInfo ;

    }



    public static void p7bToJks(CertificationInfo info) {
        try {

            KeyStore inputKeyStore = KeyStore.getInstance("JKS");
            InputStream jksInput = new ByteArrayInputStream(info.getJksFile());
            inputKeyStore.load(jksInput, info.getPassword().toCharArray());



            InputStream caFis = new ByteArrayInputStream(info.getP7bFile());
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
//            List<X509Certificate> chains = (List<X509Certificate>) cf.generateCertificates(caFis);// 获取ca颁发的证书
//            Certificate[] certChain = inputKeyStore.getCertificateChain("mykey");
//            Collections.reverse(chains);
            List<X509Certificate> chains =  CertificateSort.sort((List<X509Certificate>) cf.generateCertificates(caFis));


            KeyStore outputKeyStore = KeyStore.getInstance("JKS");
            outputKeyStore.load(null, null);
            Key key = inputKeyStore.getKey(info.getKeyName(), info.getPassword().toCharArray());
            outputKeyStore.setKeyEntry("root", key, password.toCharArray(), chains.toArray(new Certificate[chains.size()]));

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputKeyStore.store(outputStream,password.toCharArray());
            info.setCertFile(outputStream.toByteArray());

        }catch (Exception e){

        }

    }

//    public CertificationInfo certZQWriteInJks(CertificationInfo info)  {
//
//        try {
//            // 加载jks文件
//            InputStream in = new ByteArrayInputStream(info.getJksFile());
////            InputStream in = new ByteArrayInputStream(info.getP7bFile());
//            // ByteOutputStream byteOutputStream = new ByteOutputStream();
//            KeyStore ks = KeyStore.getInstance("JKS");// 设置类型
//            ks.load(in, info.getPassword().toCharArray());
//            // 获取别名为name的私钥
//            PrivateKeyEntry key = (PrivateKeyEntry) ks.getEntry(info.getKeyName(), new PasswordProtection(info.getPassword().toCharArray()));
//
//            InputStream caFis = new ByteArrayInputStream(info.getP7bFile());// 加载ca发放证书
//            CertificateFactory cf = CertificateFactory.getInstance("X.509");
//            List<Certificate> chains = (List<Certificate>) cf.generateCertificates(caFis);// 获取ca颁发的证书
////            //用户证书要放在证书链的最前面
////            List<Certificate> chains = new ArrayList<>();
////            chains.add(0,cert);
//            ByteArrayOutputStream keystoreFile = null;
//            byte[] file = null;
//            try {
//                // 生成一个新的keystore
//                KeyStore store = KeyStore.getInstance("JKS");
//                store.load(null, info.getPassword().toCharArray());
//                // 将ca证书写入本地密钥库的私钥
//                Certificate[] descChains = new Certificate[chains.size()];
//                int a = 0 ;
//                for(int i = chains.size() - 1; i > 0  ; i--){
//                    descChains[a] = chains.get(i);
//                    a++ ;
//                }
//                store.setKeyEntry(info.getKeyName(), key.getPrivateKey(), info.getPassword().toCharArray(), descChains);
//                keystoreFile = new ByteArrayOutputStream();
//                store.store(keystoreFile, info.getPassword().toCharArray());
//                file = keystoreFile.toByteArray();
//                info.setJksFile(file);
////                info.setCertFile(file);
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                if (keystoreFile != null) {
//                    keystoreFile.close();
//                }
//                if (in != null) {
//                    in.close();
//                }
//                if (caFis != null) {
//                    caFis.close();
//                }
//            }
//        }catch (Exception e){
//
//        }
//
//        return info;
//    }

//    public byte[] covertJSKToPFX(CertificationInfo info) {
//
//
//        ByteArrayInputStream fis = null;
//        ByteArrayOutputStream keystoreFile = null;
//        OutputStream out = null;
//        byte[] file = null;
//        try {
//            // 设置KeyStore类型
//            KeyStore inputKeyStore = KeyStore.getInstance("JKS");
//            fis = new ByteArrayInputStream(info.getCertFile());
////            fis = new ByteArrayInputStream(info.getCertFile());
//            // 加载数字证书
//            inputKeyStore.load(fis, info.getPassword().toCharArray());
//            // 设置需要导出的数字证书类型
////            KeyStore outputKeyStore = KeyStore.getInstance("PKCS12","BC");
//            KeyStore outputKeyStore = KeyStore.getInstance("PKCS12");
//            outputKeyStore.load(null, info.getPassword().toCharArray());
//            // 获取数字证书里面所有证书链别名 一般只有一个
//            Enumeration enums = inputKeyStore.aliases();
//            // 遍历 取出证书里别enums里的别名证书
//            while (enums.hasMoreElements()) {
//                String keyAlias = (String) enums.nextElement();
//                if (inputKeyStore.isKeyEntry(keyAlias)) {
//                    Key key = inputKeyStore.getKey(keyAlias, info.getPassword().toCharArray());
//                    Certificate[] certChain = inputKeyStore.getCertificateChain(keyAlias);
//                    outputKeyStore.setKeyEntry("", key, info.getPassword().toCharArray(), certChain);
////                    outputKeyStore.setKeyEntry(keyAlias, key, info.getPassword().toCharArray(), certChain);
//                }
//            }
//            keystoreFile = new ByteArrayOutputStream();
//            outputKeyStore.store(keystoreFile, info.getPassword().toCharArray());
//            file = keystoreFile.toByteArray();
//            info.setPfxFile(file);
//        } catch (Throwable e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (fis != null) {
//                    fis.close();
//                }
//                if (keystoreFile != null) {
//                    keystoreFile.close();
//                }
//                if (out != null) {
//                    out.close();
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return file;
//    }

//    public byte[] covertJSKToPFX(CertificationInfo info) {
//
//
//        ByteArrayInputStream fis = null;
//        ByteArrayOutputStream keystoreFile = null;
//        OutputStream out = null;
//        byte[] file = null;
//        try {
//            // 设置KeyStore类型
//            KeyStore inputKeyStore = KeyStore.getInstance("JKS");
//            fis = new ByteArrayInputStream(info.getJksFile());
//            // 加载数字证书
//            inputKeyStore.load(fis, info.getPassword().toCharArray());
//            // 设置需要导出的数字证书类型
//            KeyStore outputKeyStore = KeyStore.getInstance("PKCS12");
//            // 获取数字证书里面所有证书链别名 一般只有一个
//            Enumeration enums = inputKeyStore.aliases();
//            // 遍历 取出证书里别enums里的别名证书
//            while (enums.hasMoreElements()) {
//                String keyAlias = (String) enums.nextElement();
//                outputKeyStore.load(null, info.getPassword().toCharArray());
//                if (inputKeyStore.isKeyEntry(keyAlias)) {
//                    Key key = inputKeyStore.getKey(keyAlias, info.getPassword().toCharArray());
//                    Certificate[] certChain = inputKeyStore.getCertificateChain(keyAlias);
//                    outputKeyStore.setKeyEntry(keyAlias, key, info.getPassword().toCharArray(), certChain);
//                }
//                keystoreFile = new ByteArrayOutputStream();
//                outputKeyStore.store(keystoreFile, info.getPassword().toCharArray());
//                outputKeyStore.deleteEntry(keyAlias);
//                file = keystoreFile.toByteArray();
//                info.setPfxFile(file);
//            }
//        } catch (Throwable e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (fis != null) {
//                    fis.close();
//                }
//                if (keystoreFile != null) {
//                    keystoreFile.close();
//                }
//                if (out != null) {
//                    out.close();
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return file;
//    }

}