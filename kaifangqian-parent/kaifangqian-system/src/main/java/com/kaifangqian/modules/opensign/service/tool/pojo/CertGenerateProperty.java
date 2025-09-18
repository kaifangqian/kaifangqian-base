/**
 * @description 证书生成所需的参数和证书生成后的相关属性
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
package com.kaifangqian.modules.opensign.service.tool.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 证书生成所需的参数和证书生成后的相关属性
 * @Package: com.kaifangqian.modules.sign.service.tool.pojo
 * @ClassName: CertGenerateProperty
 * @author: FengLai_Gong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertGenerateProperty implements Serializable {

    private static final long serialVersionUID = 4186449536291972943L;


    //certDn证书的标题如：CN=某某,OU=身份证号或企业证件号,O=哪家公司帮某某申请,L=省份,ST=企业城市,C=所在国家
    private String certDn ;
    //数字证书的别名默认mykey
    private String keyName = "mykey" ;
    //秘钥长度
    private Integer keySize = 2048;
    //生成证书的加密算法
    private String certAlgorithm = "SHA1WithRSA" ;
    //生成密钥对的加密算法
    private String KeyPairGeneratorAlgorithm  = "RSA";
    //证书有效期起始时间
    private Date startTime ;
    //证书有效期截止时间
    private Date endTime ;

    //根证书文件
    private byte[] rootCert ;
    //根证书密码
    private String rootPassword ;




    //p10文件
    private byte[] p10;
    //证书序列号
    private String serialNumber ;
    //keystore文件
    private byte[] keystoreFile ;
    //新生成原始证书密码
    private String password ;
    //新生成原始证书文件
    private byte[] cert ;
    //生成的p7b文件
    private byte[] p7bFile ;
    //生成的jks文件
    private byte[] jksFile ;
    //将原始证书认证后最终生成的pfx文件
    private byte[] pfxFile ;



}