/**
 * @description 证书主题信息
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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: CertSubjectProperty
 * @Package: com.kaifangqian.modules.opensign.service.tool.pojo
 * @ClassName: CertSubjectProperty
 * @author: FengLai_Gong
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CertSubjectProperty implements Serializable {

    //   C  Country Name (国家代号),eg: CN
    private String c ;

    //    ST State or Province Name (洲或者省份),eg: Beijing
    private String st ;
    //  CN Common Name (服务器ip或者域名),eg: 192.168.30.71 or www.baidu.com
    //1）个人：【姓名】@【身份证号码】@【手机号】
    //2）企业：【企业名称】@【统一社会信用代码】
    private String cn ;

    //   L  Locality Name (城市名),eg: Beijing
    private String l ;

    //   O  Organization Name (可以是公司名称),eg: 北京资源律动科技有限公司
    private String o ;
    //   OU Organizational Unit Name (可以是单位部门名称)
    private String ou ;

    //    STREET  streetAddress
    private String street ;
    //    DC      domainComponent
    private String dc ;
    //    UID     userid
    private String uid ;


    // 1为个人，2为企业
    private Integer certType ;









}