/**
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
package com.kaifangqian.modules.system.service;

import com.kaifangqian.modules.cert.enums.CertTypeEnum;
import com.kaifangqian.modules.system.entity.TenantCertificate;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 租户证书关联表 服务类
 * </p>
 *
 * @author Administrator
 * @since 2023-10-10
 */
public interface ITenantCertificateService extends IService<TenantCertificate> {


    /**
     * @Description #根据证书类型获取证书列表
     * @Param [tenantId, certTypeEnum]
     * @return java.util.List<com.kaifangqian.modules.system.entity.TenantCertificate>
     **/
    List<TenantCertificate> getCertList(String tenantId, CertTypeEnum certTypeEnum);

    /**
     * @Description #根据证书类型和租户id，统计有效证书数量
     * @Param [tenantId, certTypeEnum]
     * @return java.lang.Integer
     **/
    Integer countEnabledCert(String tenantId, CertTypeEnum certTypeEnum);

    /**
     * @Description #根据证书类型和租户id，统计所有证书数量
     * @Param [tenantId, certTypeEnum]
     * @return java.lang.Integer
     **/
    Integer countCert(String tenantId, CertTypeEnum certTypeEnum);

    /**
     * @Description #根据证书类型和租户id，获取有效证书
     * @Param [tenantId, certTypeEnum]
     * @return com.kaifangqian.modules.system.entity.TenantCertificate
     **/
    TenantCertificate getEnabledCert(String tenantId, CertTypeEnum certTypeEnum);

    List<TenantCertificate> getEnabledCertList(String tenantId, CertTypeEnum certTypeEnum);

    /**
     * @Description #
     * @Param [tenantId, certTypeEnum]
     * @return void
     **/
    void unable(String tenantId,  Integer certType);

}
