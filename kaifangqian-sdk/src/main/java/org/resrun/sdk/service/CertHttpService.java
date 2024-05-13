package org.resrun.sdk.service;

import com.alibaba.fastjson.JSONObject;
import org.resrun.sdk.config.SDKClientConfig;
import org.resrun.sdk.enums.APIResultEnum;
import org.resrun.sdk.service.pojo.ApplyCert;
import org.resrun.sdk.service.pojo.ApplyCertData;
import org.resrun.sdk.service.pojo.CertificationInfo;
import org.resrun.sdk.utils.Base64;
import org.resrun.sdk.utils.HttpUtils;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.resrun.sdk.utils.SystemCertIuss;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired(required = false)
    private SDKClientConfig config;

    public static final String keyName = "myKey";

    private static KeyPairGenerator kpg = null;

    public ApplyCert applyCert(byte[] p10,String uniqueCode){
        if(config != null && config.getProd()){
            return certIndividual(p10,uniqueCode);
        }else{
            try {
                //本地生成证书
                ApplyCertData applyCertData =  SystemCertIuss.issuance(p10);
                ApplyCert applyCert = new ApplyCert();
                applyCert.setCode(APIResultEnum.SUCCESS.getCode());
                applyCert.setUniqueCode(uniqueCode);
                applyCert.setData(applyCertData);
                return applyCert;
            }catch (Exception e){
                throw new RuntimeException("生成本地证书异常",e);
            }
        }
    }

    public ApplyCert certIndividual(byte[] p10,String uniqueCode){
        ApplyCert cert = null ;
        if(p10 != null && p10 != null){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token",config.getToken());
            jsonObject.put("p10", Base64.encode(p10));
            jsonObject.put("uniqueCode", uniqueCode);
            String returnJson = null ;
            try {
                returnJson  = HttpUtils.executePost(config.getCertApplyUrl(), jsonObject.toJSONString(), new HashMap<String,String>());
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

    public CertificationInfo generateP10(CertificationInfo certificationInfo) {
        try {
            X509Certificate cert = null;
            ByteArrayOutputStream keystoreFile = null;
            String signAlgorithm = "SHA256withRSA"; // 设置证书的加密方式
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
}