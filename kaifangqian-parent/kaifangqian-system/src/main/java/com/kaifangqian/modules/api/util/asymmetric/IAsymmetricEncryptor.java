/**
 * @description 电子签算法服务
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

import com.kaifangqian.exception.PaasException;

public interface IAsymmetricEncryptor {

    /**
     * 计算指定内容的签名
     *
     * @param content    待签名的原文
     * @param charset    待签名的原文的字符集编码
     * @param privateKey 私钥字符串
     * @return 签名字符串
     */
    String sign(String content, String charset, String privateKey) throws PaasException;

    /**
     * 验证指定内容的签名是否正确
     *
     * @param content   待校验的原文
     * @param charset   待校验的原文的字符集编码
     * @param publicKey 公钥字符串
     * @param sign      签名字符串
     * @return true：验证通过；false：验证不通过
     */
    boolean verify(String content, String charset, String publicKey, String sign) throws PaasException;

    /**
     * 对明文进行非对称加密
     *
     * @param plainText 明文字符串
     * @param charset   明文的字符集编码
     * @param publicKey 公钥字符串
     * @return 密文的Base64编码字符串
     */
    String encrypt(String plainText, String charset, String publicKey) throws PaasException;

    /**
     * 对密文进行非对称解密
     *
     * @param cipherTextBase64 密文Base64编码字符串
     * @param charset          明文的字符集编码
     * @param privateKey       私钥字符串
     * @return 明文
     */
    String decrypt(String cipherTextBase64, String charset, String privateKey) throws PaasException;

}