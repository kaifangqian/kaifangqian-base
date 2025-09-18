package com.kaifangqian.modules.opensign.service.cert.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.modules.cert.enums.CertTypeEnum;
import com.kaifangqian.modules.opensign.service.cert.SignCertInfoService;
import com.kaifangqian.common.base.entity.BaseEntity;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.modules.opensign.entity.SignCertInfo;
import com.kaifangqian.modules.opensign.mapper.SignCertInfoMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: SignCertInfoServiceImpl
 * @Package: com.kaifangqian.modules.opensign.service.cert.impl
 * @ClassName: SignCertInfoServiceImpl
 * @author: FengLai_Gong
 */
@Service
public class SignCertInfoServiceImpl extends ServiceImpl<SignCertInfoMapper, SignCertInfo> implements SignCertInfoService {



    @Override
    public SignCertInfo getValidateCert() {
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        QueryWrapper<SignCertInfo> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignCertInfo::getSysTenantId,currentUser.getTenantId());
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().eq(SignCertInfo::getValidateStatus,1);
        wrapper.lambda().eq(SignCertInfo::getHolderType,2);
        List<SignCertInfo> signCertInfos = this.baseMapper.selectList(wrapper);
        if(signCertInfos == null || signCertInfos.size() == 0){
            return null;
        }
        return signCertInfos.get(0);
    }

    @Override
    public SignCertInfo getByHolderType(Integer holderType) {
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        QueryWrapper<SignCertInfo> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().eq(SignCertInfo::getValidateStatus,1);
        wrapper.lambda().eq(SignCertInfo::getHolderType,holderType);
        List<SignCertInfo> signCertInfos = this.baseMapper.selectList(wrapper);
        if(signCertInfos == null || signCertInfos.size() == 0){
            return null;
        }
        return signCertInfos.get(0);
    }

    @Override
    public SignCertInfo getEntCertInfo(String tenantId, CertTypeEnum certTypeEnum) {
        QueryWrapper<SignCertInfo> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().eq(SignCertInfo::getValidateStatus,1);
        wrapper.lambda().eq(SignCertInfo::getHolderType, certTypeEnum.getCode());
        wrapper.lambda().eq(SignCertInfo::getSysTenantId,tenantId);
        List<SignCertInfo> signCertInfos = this.baseMapper.selectList(wrapper);
        if(signCertInfos == null || signCertInfos.size() == 0){
            return null;
        }
        return signCertInfos.get(0);
    }

    @Override
    public SignCertInfo getPersonCertInfo(String tenantUserId, CertTypeEnum certTypeEnum) {
        QueryWrapper<SignCertInfo> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().eq(SignCertInfo::getValidateStatus,1);
        wrapper.lambda().eq(SignCertInfo::getHolderType, certTypeEnum.getCode());
        wrapper.lambda().eq(SignCertInfo::getSysUserId,tenantUserId);
        List<SignCertInfo> signCertInfos = this.baseMapper.selectList(wrapper);
        if(signCertInfos == null || signCertInfos.size() == 0){
            return null;
        }
        return signCertInfos.get(0);
    }
}