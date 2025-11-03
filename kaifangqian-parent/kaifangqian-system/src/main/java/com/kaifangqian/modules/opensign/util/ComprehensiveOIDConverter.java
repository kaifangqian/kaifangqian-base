package com.kaifangqian.modules.opensign.util;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;

import java.util.HashMap;
import java.util.Map;

public class ComprehensiveOIDConverter {

    private static final Map<String, String> OID_TO_NAME = new HashMap<>();

    static {
        // 初始化所有已知OID映射
        initializeMappings();
    }

    private static void initializeMappings() {
        // PKCS#1 算法
        OID_TO_NAME.put(PKCSObjectIdentifiers.rsaEncryption.getId(), "RSA");
        OID_TO_NAME.put(PKCSObjectIdentifiers.md2WithRSAEncryption.getId(), "MD2withRSA");
        OID_TO_NAME.put(PKCSObjectIdentifiers.md4WithRSAEncryption.getId(), "MD4withRSA");
        OID_TO_NAME.put(PKCSObjectIdentifiers.md5WithRSAEncryption.getId(), "MD5withRSA");
        OID_TO_NAME.put(PKCSObjectIdentifiers.sha1WithRSAEncryption.getId(), "SHA1withRSA");
        OID_TO_NAME.put(PKCSObjectIdentifiers.sha224WithRSAEncryption.getId(), "SHA224withRSA");
        OID_TO_NAME.put(PKCSObjectIdentifiers.sha256WithRSAEncryption.getId(), "SHA256withRSA");
        OID_TO_NAME.put(PKCSObjectIdentifiers.sha384WithRSAEncryption.getId(), "SHA384withRSA");
        OID_TO_NAME.put(PKCSObjectIdentifiers.sha512WithRSAEncryption.getId(), "SHA512withRSA");

        // ECDSA 算法
        OID_TO_NAME.put(X9ObjectIdentifiers.ecdsa_with_SHA1.getId(), "SHA1withECDSA");
        OID_TO_NAME.put(X9ObjectIdentifiers.ecdsa_with_SHA224.getId(), "SHA224withECDSA");
        OID_TO_NAME.put(X9ObjectIdentifiers.ecdsa_with_SHA256.getId(), "SHA256withECDSA");
        OID_TO_NAME.put(X9ObjectIdentifiers.ecdsa_with_SHA384.getId(), "SHA384withECDSA");
        OID_TO_NAME.put(X9ObjectIdentifiers.ecdsa_with_SHA512.getId(), "SHA512withECDSA");

        // 国密算法 (GM/T)
        OID_TO_NAME.put(GMObjectIdentifiers.sm2sign_with_sm3.getId(), "SM2withSM3");
        OID_TO_NAME.put(GMObjectIdentifiers.sm2encrypt.getId(), "SM2");
        OID_TO_NAME.put(GMObjectIdentifiers.sm3.getId(), "SM3");
//        OID_TO_NAME.put(GMObjectIdentifiers.sm4.getId(), "SM4");

        // 其他常见算法
        OID_TO_NAME.put("1.2.840.10040.4.1", "DSA");
        OID_TO_NAME.put("1.2.840.10040.4.3", "SHA1withDSA");
        OID_TO_NAME.put("1.3.14.3.2.26", "SHA1");
        OID_TO_NAME.put(NISTObjectIdentifiers.id_sha256.getId(), "SHA256");
        OID_TO_NAME.put(NISTObjectIdentifiers.id_sha384.getId(), "SHA384");
        OID_TO_NAME.put(NISTObjectIdentifiers.id_sha512.getId(), "SHA512");
    }

    public static String getAlgorithmName(String oid) {
        // 首先检查映射表
        String name = OID_TO_NAME.get(oid);
        if (name != null) {
            return name;
        }

        // 尝试使用Bouncy Castle
        try {
            ASN1ObjectIdentifier objId = new ASN1ObjectIdentifier(oid);
            return objId.getId(); // 至少返回OID字符串
        } catch (Exception e) {
            return "未知算法: " + oid;
        }
    }

    public static void main(String[] args) {
        // 测试示例
        String[] testOIDs = {
                "1.2.840.113549.1.1.1",
                "1.2.840.113549.1.1.11",
                "1.2.156.10197.1.501",
                "1.2.840.10045.4.3.2"
        };

        for (String oid : testOIDs) {
            System.out.println(oid + " -> " + getAlgorithmName(oid));
        }
    }
}