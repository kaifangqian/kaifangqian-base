package org.resrun.api.service;

import com.alibaba.fastjson.JSONObject;
import org.resrun.api.config.ApiClientConfig;
import org.resrun.api.enums.APIResultEnum;
import org.resrun.api.service.pojo.ApplyCert;
import org.resrun.api.service.pojo.ApplyCertData;
import org.resrun.api.service.pojo.CertificationInfo;
import org.resrun.api.utils.Base64;
import org.resrun.api.utils.HttpUtils;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.resrun.api.utils.SystemCertIuss;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.security.auth.x500.X500Principal;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * @Description: CertHttpService
 * @Package: org.resrun.modules.cert.service
 * @ClassName: CertHttpService
 * @author: FengLai_Gong
 */
@Service
public class CertHttpService {

    @Autowired
    private ApiClientConfig config;


    public static final String keyName = "myKey";

    private static KeyPairGenerator kpg = null;

    public ApplyCert applyCert(byte[] p10,String uniqueCode){
        if(config.getProd()){
            return certIndividual(p10,uniqueCode);
        }else{
            try {
                //本地签发证书
                ApplyCertData applyCertData =  SystemCertIuss.issuance(p10);
                ApplyCert applyCert = new ApplyCert();
                applyCert.setCode(APIResultEnum.SUCCESS.getCode());
                applyCert.setUniqueCode(uniqueCode);
                applyCert.setData(applyCertData);
                return applyCert;
            }catch (Exception e){
                throw new RuntimeException("本地证书签发异常",e);
            }
        }
    }

    private ApplyCert certIndividual(byte[] p10, String uniqueCode){
        ApplyCert cert = null ;
        if(p10 != null && p10 != null){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token",config.getToken());
            jsonObject.put("p10", Base64.encode(p10));
            jsonObject.put("uniqueCode", uniqueCode);
            String returnJson = null ;
            try {
                returnJson  = HttpUtils.executePost(config.getCertApplyUrl(), jsonObject.toJSONString(), new HashMap<>());
                if(returnJson != null && returnJson.length() > 0){
                    cert = JSONObject.parseObject(returnJson, ApplyCert.class);
                    return cert ;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cert ;
    }

//    public static P10Result generateP10(String dn) throws IOException, NoSuchAlgorithmException, SignatureException, NoSuchProviderException, InvalidKeyException {
//
//        try (ByteArrayOutputStream keystoreFile =  new ByteArrayOutputStream()){
//            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
//            String signAlgorithm = "SHA256WithRSA"; // 设置证书的加密方式
//            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//
//            //cfca需要2048长度的秘钥
//            kpg.initialize(2048);
//            KeyPair keyPair = kpg.generateKeyPair();
//            PublicKey pubKey = keyPair.getPublic();
//
//            X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
//            X500Principal xp = new X500Principal(dn);
//            certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
//            // 设置颁发者
//            certGen.setIssuerDN(xp);
//            // 设置使用者
//            // 设置有效期
//            certGen.setNotBefore(new Date());
//
//            Calendar calendar = Calendar.getInstance();
//            Date date = new Date(System.currentTimeMillis());
//            calendar.setTime(date);
//            calendar.add(Calendar.YEAR, +1);
//            certGen.setNotAfter(calendar.getTime());
//
//            certGen.setSubjectDN(xp);
//            // 公钥
//            certGen.setPublicKey(pubKey);
//            certGen.setSignatureAlgorithm(signAlgorithm);
//
//            PKCS10CertificationRequest p10CertificationRequest = new PKCS10CertificationRequest(signAlgorithm, xp,
//                    keyPair.getPublic(), null, keyPair.getPrivate());
//            p10CertificationRequest.getEncoded();
//
//            return new P10Result(p10CertificationRequest.getEncoded(),keyPair.getPublic().getEncoded());
//        }
//
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
            store.setKeyEntry(keyName, keyPair.getPrivate(), certificationInfo.getPassword().toCharArray(), new Certificate[] { cert });
            PKCS10CertificationRequest p10CertificationRequest = new PKCS10CertificationRequest(signAlgorithm, xp,
                    keyPair.getPublic(), null, keyPair.getPrivate());
            // 得到p10格式数据
            certificationInfo.setP10(p10CertificationRequest.getEncoded());
            keystoreFile = new ByteArrayOutputStream();
            store.store(keystoreFile, certificationInfo.getPassword().toCharArray()); // 获取生成数字证书

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
            List<Certificate> chains = (List<Certificate>) cf.generateCertificates(caFis);// 获取ca颁发的证书
            Collections.reverse(chains);


            KeyStore outputKeyStore = KeyStore.getInstance("JKS");
            outputKeyStore.load(null, null);
            Key key = inputKeyStore.getKey(info.getKeyName(), info.getPassword().toCharArray());
            outputKeyStore.setKeyEntry("root", key, info.getPassword().toCharArray(), chains.toArray(new Certificate[chains.size()]));

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputKeyStore.store(outputStream,info.getPassword().toCharArray());
            info.setCertFile(outputStream.toByteArray());

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static byte [] coverToPfx(byte [] jks, String password) {
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            KeyStore inputKeyStore = KeyStore.getInstance("JKS");
            ByteArrayInputStream inputStream = new ByteArrayInputStream(jks);
            inputKeyStore.load(inputStream, password.toCharArray());

            KeyStore outputKeyStore = KeyStore.getInstance("PKCS12","BC");

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