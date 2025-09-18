/**
 * @description 证书文件属性类
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

/**
 * @Description: 证书文件属性类
 * @Package: com.kaifangqian.modules.sign.pojo
 * @ClassName: CertificateProperty
 * @author: FengLai_Gong
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CertificateProperty implements Serializable {

    private static final long serialVersionUID = -2073805779543816269L;

    private  byte[] certFile;
    /** 证书的类型 比如：PKCS12和jks*/
    private  String certType;
    /** 证书密码 */
    private  String password;
    //租户证书关联表id
    private String tenantCertificateId ;
    //租户证书类型
    private Integer tenantCertificateType ;
    //证书表id
    private String certificateInfoId ;
    //证书归属租户id
    private String tenantId ;


}