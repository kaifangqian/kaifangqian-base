package com.kaifangqian.modules.opensign.service.cert;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaifangqian.modules.cert.enums.CertTypeEnum;
import com.kaifangqian.modules.opensign.entity.SignCertInfo;

/**
 * @Description: SignCertInfoService
 * @Package: com.kaifangqian.modules.opensign.service.cert
 * @ClassName: SignCertInfoService
 * @author: FengLai_Gong
 */
public interface SignCertInfoService extends IService<SignCertInfo> {

    SignCertInfo getValidateCert();


    SignCertInfo getByHolderType(Integer holderType);

    SignCertInfo getEntCertInfo(String tenantId, CertTypeEnum certTypeEnum);

    SignCertInfo getPersonCertInfo(String tenantUserId, CertTypeEnum certTypeEnum);

}