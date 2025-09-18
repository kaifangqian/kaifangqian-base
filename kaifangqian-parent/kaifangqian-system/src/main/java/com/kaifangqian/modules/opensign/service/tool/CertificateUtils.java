/**
 * @description 证书工具类
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
package com.kaifangqian.modules.opensign.service.tool;

import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.Enumeration;

public class CertificateUtils {

    /**
     * 生成证书主题信息
     *
     * @param C  Country Name (国家代号),eg: CN
     * @param ST State or Province Name (洲或者省份),eg: Beijing
     * @param L  Locality Name (城市名),eg: Beijing
     * @param O  Organization Name (可以是公司名称),eg: xxxxxx网络技术有限公司
     * @param OU Organizational Unit Name (可以是单位部门名称)
     * @param CN Common Name (服务器ip或者域名或者公司名称),eg: 192.168.30.71 or www.baidu.com or xxxxxx网络技术有限公司
     * @return X500Name Subject
     */
    public static String buildSubject(String C, String ST, String L,
                                        String O, String OU, String CN) {
        X500NameBuilder x500NameBuilder = new X500NameBuilder();
        x500NameBuilder.addRDN(BCStyle.C, C);
        x500NameBuilder.addRDN(BCStyle.ST, ST);
        x500NameBuilder.addRDN(BCStyle.L, L);
        if(O != null)
            x500NameBuilder.addRDN(BCStyle.O, O);
        if(OU != null)
            x500NameBuilder.addRDN(BCStyle.OU, OU);
        x500NameBuilder.addRDN(BCStyle.CN, CN);
        System.out.println(x500NameBuilder.build().toASN1Primitive().toString());
        return x500NameBuilder.build().toString();
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
