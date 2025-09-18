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
package com.kaifangqian.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaifangqian.modules.cert.enums.CertTypeEnum;
import com.kaifangqian.modules.cert.enums.TenantCertStatusEnum;
import com.kaifangqian.modules.system.entity.TenantCertificate;
import com.kaifangqian.modules.system.mapper.TenantCertificateMapper;
import com.kaifangqian.modules.system.service.ITenantCertificateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 租户证书关联表 服务实现类
 * </p>
 *
 * @author Administrator
 * @since 2023-10-10
 */
@Service
public class TenantCertificateServiceImpl extends ServiceImpl<TenantCertificateMapper, TenantCertificate> implements ITenantCertificateService {

    @Override
    public List<TenantCertificate> getCertList(String tenantId, CertTypeEnum certTypeEnum){
        QueryWrapper<TenantCertificate> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TenantCertificate::getTenantId,tenantId);
        wrapper.lambda().eq(TenantCertificate::getCertType,certTypeEnum.getCode());
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public Integer countEnabledCert(String tenantId, CertTypeEnum certTypeEnum) {
        QueryWrapper<TenantCertificate> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TenantCertificate::getTenantId,tenantId);
        wrapper.lambda().eq(TenantCertificate::getCertType,certTypeEnum.getCode());
        wrapper.lambda().eq(TenantCertificate::getCertStatus, TenantCertStatusEnum.ENABLE.getCode());

        return this.baseMapper.selectCount(wrapper).intValue();
    }

    @Override
    public Integer countCert(String tenantId, CertTypeEnum certTypeEnum) {
        QueryWrapper<TenantCertificate> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TenantCertificate::getTenantId,tenantId);
        wrapper.lambda().eq(TenantCertificate::getCertType,certTypeEnum.getCode());

        return this.baseMapper.selectCount(wrapper).intValue();
    }

    @Override
    public TenantCertificate getEnabledCert(String tenantId, CertTypeEnum certTypeEnum) {
        QueryWrapper<TenantCertificate> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TenantCertificate::getTenantId,tenantId);
        wrapper.lambda().eq(TenantCertificate::getCertType,certTypeEnum.getCode());
        wrapper.lambda().eq(TenantCertificate::getCertStatus, TenantCertStatusEnum.ENABLE.getCode());
        List<TenantCertificate> tenantCertificates = this.baseMapper.selectList(wrapper);
        if(tenantCertificates != null && tenantCertificates.size() > 0){
            return tenantCertificates.get(0);
        }
        return null;
    }

    @Override
    public List<TenantCertificate> getEnabledCertList(String tenantId, CertTypeEnum certTypeEnum) {
        QueryWrapper<TenantCertificate> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TenantCertificate::getTenantId,tenantId);
        wrapper.lambda().eq(TenantCertificate::getCertType,certTypeEnum.getCode());
        wrapper.lambda().eq(TenantCertificate::getCertStatus, TenantCertStatusEnum.ENABLE.getCode());
        List<TenantCertificate> tenantCertificates = this.baseMapper.selectList(wrapper);
        return tenantCertificates ;
    }

    @Override
    public void unable(String tenantId, Integer certType) {
        QueryWrapper<TenantCertificate> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TenantCertificate::getTenantId,tenantId);
        wrapper.lambda().eq(TenantCertificate::getCertType,certType);
        wrapper.lambda().eq(TenantCertificate::getCertStatus, TenantCertStatusEnum.ENABLE.getCode());

        TenantCertificate certificate = new TenantCertificate();
        certificate.setCertStatus(TenantCertStatusEnum.UN_ENABLE.getCode());

        baseMapper.update(certificate,wrapper);
    }
}
