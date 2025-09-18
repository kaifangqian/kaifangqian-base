/**
 * @description SM2加密
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
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.api.util.Base64;
import com.kaifangqian.utils.MyStringUtils;
import org.bouncycastle.jcajce.spec.SM2ParameterSpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Strings;

import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author : zhenghuihan
 * create at:  2024/3/20  14:22
 * @description:
 */
public class SM2Encryptor extends BaseAsymmetricEncryptor {

    //SM2算法默认用户ID，目前开放平台不会使用非默认用户ID
    public String DEFAULT_USER_ID = "1234567812345678";


    private static BouncyCastleProvider provider;

    static {
        provider = new BouncyCastleProvider();
        Security.addProvider(provider);
    }

    protected String getAsymmetricType() {
        return ApiConstants.SIGN_TYPE_SM2;
    }

    protected String doDecrypt(String cipherTextBase64, String charset, String privateKey) throws Exception {
        //加载私钥参数
        byte[] cipher = Base64.decodeBase64String(cipherTextBase64);
        byte[] privateKeyByte = Base64.decodeBase64String(privateKey);
        // 解析X509格式SM2私钥
        PrivateKey sm2PrivateKey = parsePKCS8PrivateKey(privateKeyByte);
        // 使用SM2私钥解密
        byte[] buf = sm2Decrypt(cipher, sm2PrivateKey);
        //将解密后的明文按指定字符集编码后返回
        try {
            String strContent = new String(buf, charset);
            return strContent;
        } catch (UnsupportedEncodingException e) {
            throw new PaasException(e);
        }
    }

    protected String doEncrypt(String plainText, String charset, String publicKey) throws Exception {
        // 解析PKCS8格式SM2私钥
        byte[] publicKeyByte = Base64.decodeBase64String(publicKey);
        PublicKey sm2PublicKey = parseX509PublicKey(publicKeyByte);
        byte[] plain = plainText.getBytes(charset);
        byte[] cipher = sm2Encrypt(plain, sm2PublicKey);
        //将密文Base64编码后返回
        String strContent = Base64.encodeBase64String(cipher);
        return strContent;

    }

    protected String doSign(String content, String charset, String privateKey) throws Exception {
        byte[] privateKeyByte = Base64.decodeBase64String(privateKey);
        PrivateKey sm2PrivateKey = parsePKCS8PrivateKey(privateKeyByte);
        byte[] message = content.getBytes(charset);
        byte[] signature = sm2Sign(message, sm2PrivateKey, null);

        //将签名结果转换为Base64
        String sign = Base64.encodeBase64String(signature);
        return sign;
    }

    protected boolean doVerify(String content, String charset, String publicKey, String sign) throws Exception {
        // 使用SM2公钥验签
        byte[] signature = Base64.decodeBase64String(sign);
        byte[] message = content.getBytes(charset);
        byte[] publicKeyByte = Base64.decodeBase64String(publicKey);
        PublicKey sm2PublicKey = parseX509PublicKey(publicKeyByte);
        boolean valid = sm2Verify(signature, message, sm2PublicKey, null);
        return valid;
    }


    private static byte[] sm2Encrypt(byte[] plain, PublicKey sm2PublicKey) throws PaasException {
        try {
            Cipher sm2CipherEngine = Cipher.getInstance("SM2", "BC");
            sm2CipherEngine.init(Cipher.ENCRYPT_MODE, sm2PublicKey);
            return sm2CipherEngine.doFinal(plain);
        } catch (Exception e) {
            throw new PaasException(e);
        }
    }

    private static byte[] sm2Decrypt(byte[] cipher, PrivateKey sm2PrivateKey) throws PaasException {
        try {
            Cipher sm2CipherEngine = Cipher.getInstance("SM2", "BC");
            sm2CipherEngine.init(Cipher.DECRYPT_MODE, sm2PrivateKey);
            return sm2CipherEngine.doFinal(cipher);
        } catch (Exception e) {
            throw new PaasException(e);
        }
    }

    private static byte[] sm2Sign(byte[] message, PrivateKey sm2PrivateKey, String sm2UserId) throws PaasException {
        try {
            String userId = "1234567812345678";
            if (!MyStringUtils.isEmpty(sm2UserId)) {
                userId = sm2UserId;
            }
            Signature sm2SignEngine = Signature.getInstance("SM3withSM2");
            sm2SignEngine.setParameter(new SM2ParameterSpec(
                    Strings.toByteArray(userId)));
            sm2SignEngine.initSign(sm2PrivateKey);
            sm2SignEngine.update(message);
            return sm2SignEngine.sign();
        } catch (Exception e) {
            throw new PaasException(e);
        }
    }

    private static boolean sm2Verify(byte[] signature, byte[] message, PublicKey publicKey, String sm2UserId) {
        try {
            String userId = "1234567812345678";
            if (!MyStringUtils.isEmpty(sm2UserId)) {
                userId = sm2UserId;
            }
            Signature sm2SignEngine = Signature.getInstance("SM3withSM2");
            sm2SignEngine.setParameter(new SM2ParameterSpec(Strings.toByteArray(userId)));
            sm2SignEngine.initVerify(publicKey);
            sm2SignEngine.update(message);
            return sm2SignEngine.verify(signature);
        } catch (Exception e) {
            return false;
        }
    }

    private static PublicKey parseX509PublicKey(byte[] x509PublicKey) throws PaasException {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(x509PublicKey);
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new PaasException(e);
        } catch (InvalidKeySpecException e) {
            throw new PaasException(e);
        }
    }

    private static PrivateKey parsePKCS8PrivateKey(byte[] pkcs8PriateKey) throws PaasException {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8PriateKey);
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new PaasException(e);
        } catch (InvalidKeySpecException e) {
            throw new PaasException(e);
        }
    }
}