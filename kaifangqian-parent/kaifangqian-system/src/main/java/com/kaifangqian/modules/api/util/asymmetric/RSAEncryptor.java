/**
 * @description RSA加密
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
package com.kaifangqian.modules.api.util.asymmetric;

import com.kaifangqian.common.constant.ApiConstants;
import com.kaifangqian.modules.api.util.StreamUtil;
import com.kaifangqian.utils.MyStringUtils;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.*;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author : zhenghuihan
 * create at:  2024/3/20  14:21
 * @description:
 */
public class RSAEncryptor extends BaseAsymmetricEncryptor {
    /**
     * RSA最大加密明文大小(1024/8-11=117)
     */
    private static final int MAX_ENCRYPT_BLOCK_SIZE = 117;

    /**
     * RSA最大解密密文大小(1024/8=128)
     */
    private static final int MAX_DECRYPT_BLOCK_SIZE = 128;

    protected String getSignAlgorithm() {
        return ApiConstants.SIGN_ALGORITHMS;
    }

    protected String getAsymmetricType() {
        return ApiConstants.SIGN_TYPE_RSA;
    }

    protected int getMaxDecryptBlockSize() {
        return MAX_DECRYPT_BLOCK_SIZE;
    }

    protected int getMaxEncryptBlockSize() {
        return MAX_ENCRYPT_BLOCK_SIZE;
    }

    protected String doDecrypt(String cipherTextBase64, String charset, String privateKey) throws Exception {
        int maxDecrypt = getMaxDecryptBlockSize();
        PrivateKey priKey = getPrivateKeyFromPKCS8(ApiConstants.SIGN_TYPE_RSA, new ByteArrayInputStream(privateKey.getBytes()));
        Cipher cipher = Cipher.getInstance(ApiConstants.SIGN_TYPE_RSA);
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        byte[] encryptedData = MyStringUtils.isEmpty(charset) ? Base64.decodeBase64(cipherTextBase64.getBytes()) : Base64.decodeBase64(cipherTextBase64.getBytes(charset));
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > maxDecrypt) {
                cache = cipher.doFinal(encryptedData, offSet, maxDecrypt);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * maxDecrypt;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();

        return MyStringUtils.isEmpty(charset) ? new String(decryptedData) : new String(decryptedData, charset);

    }

    protected String doEncrypt(String plainText, String charset, String publicKey) throws Exception {
        int maxEncrypt = getMaxEncryptBlockSize();
        PublicKey pubKey = getPublicKeyFromX509(ApiConstants.SIGN_TYPE_RSA, new ByteArrayInputStream(publicKey.getBytes()));
        Cipher cipher = Cipher.getInstance(ApiConstants.SIGN_TYPE_RSA);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] data = MyStringUtils.isEmpty(charset) ? plainText.getBytes() : plainText.getBytes(charset);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > maxEncrypt) {
                cache = cipher.doFinal(data, offSet, maxEncrypt);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * maxEncrypt;
        }
        byte[] encryptedData = Base64.encodeBase64(out.toByteArray());
        out.close();

        return MyStringUtils.isEmpty(charset) ? new String(encryptedData) : new String(encryptedData, charset);
    }

    protected String doSign(String content, String charset, String privateKey) throws Exception {
        PrivateKey priKey = getPrivateKeyFromPKCS8(ApiConstants.SIGN_TYPE_RSA, new ByteArrayInputStream(privateKey.getBytes()));

        Signature signature = Signature.getInstance(getSignAlgorithm());

        signature.initSign(priKey);

        if (MyStringUtils.isEmpty(charset)) {
            signature.update(content.getBytes());
        } else {
            signature.update(content.getBytes(charset));
        }

        byte[] signed = signature.sign();

        return new String(Base64.encodeBase64(signed));
    }

    protected boolean doVerify(String content, String charset, String publicKey, String sign) throws Exception {
        PublicKey pubKey = getPublicKeyFromX509("RSA", new ByteArrayInputStream(publicKey.getBytes()));

        Signature signature = Signature.getInstance(getSignAlgorithm());

        signature.initVerify(pubKey);

        if (MyStringUtils.isEmpty(charset)) {
            signature.update(content.getBytes());
        } else {
            signature.update(content.getBytes(charset));
        }

        return signature.verify(java.util.Base64.getDecoder().decode(sign));
        //return signature.verify(Base64.decodeBase64(sign.getBytes()));
    }

    public static PrivateKey getPrivateKeyFromPKCS8(String algorithm, InputStream ins) throws Exception {
        if (ins == null || MyStringUtils.isEmpty(algorithm)) {
            return null;
        }

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        byte[] encodedKey = StreamUtil.readText(ins).getBytes();

        encodedKey = Base64.decodeBase64(encodedKey);

        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
    }

    public static PublicKey getPublicKeyFromX509(String algorithm, InputStream ins) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        StringWriter writer = new StringWriter();
        StreamUtil.io(new InputStreamReader(ins), writer);

        byte[] encodedKey = writer.toString().getBytes();

        encodedKey = Base64.decodeBase64(encodedKey);

        return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
    }
}