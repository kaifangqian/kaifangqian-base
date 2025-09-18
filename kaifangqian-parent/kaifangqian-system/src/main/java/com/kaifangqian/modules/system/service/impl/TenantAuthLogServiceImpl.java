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

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.config.AnnexType;
import com.kaifangqian.modules.cert.service.CertBusinessService;
import com.kaifangqian.modules.opensign.constant.SignCommonConstant;
import com.kaifangqian.modules.opensign.enums.EntSealTypeEnum;
import com.kaifangqian.modules.opensign.enums.SealColorEnum;
import com.kaifangqian.modules.opensign.enums.SignFileEnum;
import com.kaifangqian.modules.opensign.service.business.EntSealBusinessService;
import com.kaifangqian.modules.opensign.service.business.SealStatusBusinessService;
import com.kaifangqian.modules.opensign.service.flow.IFlowService;
import com.kaifangqian.modules.opensign.service.tool.EntSealGenerateService;
import com.kaifangqian.modules.opensign.service.tool.SignFileService;
import com.kaifangqian.modules.opensign.utils.Base64;
import com.kaifangqian.modules.opensign.vo.base.BusinessAuthVo;
import com.kaifangqian.modules.opensign.vo.request.EntSealSaveRequest;
import com.kaifangqian.modules.storage.dto.StorageDto;
import com.kaifangqian.modules.storage.entity.AnnexStorage;
import com.kaifangqian.modules.storage.service.IAnnexStorageService;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.enums.TenantAuthRealItemType;
import com.kaifangqian.modules.system.enums.TenantAuthStatus;
import com.kaifangqian.modules.system.enums.TenantAuthType;
import com.kaifangqian.modules.system.mapper.TenantAuthLogMapper;
import com.kaifangqian.modules.system.service.*;
import com.kaifangqian.modules.system.vo.TenantAuthLogVO;
import com.kaifangqian.modules.system.vo.request.TenantAuthAuditRequest;
import com.kaifangqian.modules.system.vo.request.TenantAuthRequest;
import com.kaifangqian.common.util.RsaUtils;
import com.kaifangqian.dto.MailDto;
import com.kaifangqian.dto.MessageDto;
import com.kaifangqian.eunms.MesAuthType;
import com.kaifangqian.eunms.SendType;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.utils.SysMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 租户扩展信息历史记录表 服务实现类
 * </p>
 *
 * @author Administrator
 * @since 2023-10-10
 */
@Service
@Slf4j
public class TenantAuthLogServiceImpl extends ServiceImpl<TenantAuthLogMapper, TenantAuthLog> implements ITenantAuthLogService {

    @Autowired
    private ITenantInfoExtendService tenantInfoExtendService;
    @Autowired
    private ISysTenantUserService sysTenantUserService;
    @Autowired
    private IAnnexStorageService iAnnexStorageService;

    @Autowired
    private CertBusinessService certBusinessService;

    @Autowired
    private SealStatusBusinessService sealStatusBusinessService;
    @Autowired
    private IFlowService flowService;


    @Override
    public Page<TenantAuthLogVO> getTenantAuthLog(Page<TenantAuthLog> page, TenantAuthRequest authRequest) {
        Page<TenantAuthLogVO> resultPage = new Page();
        String extendId = null;
        if (StringUtils.isNotEmpty(authRequest.getTenantId())) {
            TenantInfoExtend extend = tenantInfoExtendService.getOne(Wrappers.lambdaQuery(TenantInfoExtend.class).eq(TenantInfoExtend::getTenantId, authRequest.getTenantId()))
                    //.equals(authRequest.getStartTime() != null,TenantInfoExtend::getc,authRequest.getTenantId(),)
                    ;
            if (extend == null) {
                return resultPage;
            }
            extendId = extend.getId();
        }
        //eq(extend != null,TenantAuthLog::getExtendId,extend.getId())
        page = page(page, Wrappers.lambdaQuery(TenantAuthLog.class).eq(extendId != null, TenantAuthLog::getExtendId, extendId).like(StringUtils.isNotEmpty(authRequest.getName()), TenantAuthLog::getName, authRequest.getName()).eq(authRequest.getAuthStatus() != null, TenantAuthLog::getAuthStatus, authRequest.getAuthStatus()).ge(authRequest.getStartTime() != null, TenantAuthLog::getApplyTime, authRequest.getStartTime()).le(authRequest.getEndTime() != null, TenantAuthLog::getApplyTime, authRequest.getEndTime())
                //租户类型
                .eq(authRequest.getTenantType() != null, TenantAuthLog::getTenantType, authRequest.getTenantType()).orderByDesc(TenantAuthLog::getApplyTime));

        resultPage.setCurrent(page.getCurrent());
        resultPage.setTotal(page.getTotal());
        resultPage.setSize(page.getSize());
        List<TenantAuthLogVO> authLogVOS = new ArrayList<>();
        for (TenantAuthLog item : page.getRecords()) {
            TenantAuthLogVO authLogVO = new TenantAuthLogVO();
            authLogVO.setApplyTime(item.getApplyTime());
            authLogVO.setAuthType(item.getAuthType());
            authLogVO.setName(item.getName());
            authLogVO.setRealItem(item.getRealItem());
            authLogVO.setCorporation(item.getCorporation());
            authLogVO.setCheckTime(item.getCheckTime());
            authLogVO.setId(item.getId());
            authLogVO.setAuthStatus(item.getAuthStatus());
            if (StringUtils.isNotEmpty(item.getCheckUser())) {
                SysUser auditUser = sysTenantUserService.getTenantSysUser(item.getCheckUser());
                authLogVO.setCheckUser(auditUser.getRealname());
            }
            if (StringUtils.isNotEmpty(item.getApplyTenantUser())) {
                SysUser sysUser = sysTenantUserService.getTenantSysUser(item.getApplyTenantUser());
                authLogVO.setApplyUser(sysUser.getRealname());
            }
            authLogVOS.add(authLogVO);
        }
        resultPage.setRecords(authLogVOS);
        return resultPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean authAudit(TenantAuthAuditRequest authRequest, String checkUser) throws Exception {
        TenantAuthLog authLog = getById(authRequest.getLogId());
        if (authLog == null || authLog.getAuthStatus() != TenantAuthStatus.STATUS1.getStatus()) {
            return false;
        }
        authLog.setCheckTime(new Date());
        authLog.setCheckMsg(authRequest.getCheckMsg());
        authLog.setAuthStatus(authRequest.getAuthStatus() ? TenantAuthStatus.STATUS2.getStatus() : TenantAuthStatus.STATUS3.getStatus());
        authLog.setCheckUser(checkUser);
        TenantInfoExtend extend = new TenantInfoExtend();
        extend.setId(authLog.getExtendId());
        extend.setAuthStatus(authLog.getAuthStatus());
        extend.setAuthId(authLog.getId());
        SysTenantUser sysTenantUser = iSysTenantUserService.getById(authLog.getApplyTenantUser());
        if (authRequest.getAuthStatus()) {
//            TenantCertInfo certInfo = new TenantCertInfo();
//            certInfo.setTenantId(authLog.getTenantId());
//            certInfo.setEntName(authLog.getName());
//            certInfo.setUscc(authLog.getOrganizationNo());
//            certBusinessService.generateTenantCert(CertTypeEnum.CA_ROOT,new TenantCertInfo());
//            String certId = certBusinessService.generateTenantCert(CertTypeEnum.CA_ENTERPRISE,certInfo);
//            if(StringUtils.isEmpty(certId)){
//                log.error("CA证书申请失败：tenantId={}",authLog.getTenantId());
//                throw new Exception("CA证书制作失败");
//            }
            //重新实名认证 修改印章状态为禁用
            if (authLog.getAuthType() == TenantAuthType.CHANGE.getType()) {
                List<Integer> enableSealType = new ArrayList<>();
                if (authLog.getRealItem().intValue() == TenantAuthRealItemType.Item2.getType()) {
                    enableSealType.add(EntSealTypeEnum.LEGAL_PERSON.getCode());
                } else if (authLog.getRealItem().intValue() == TenantAuthRealItemType.Item3.getType()) {
                    enableSealType.add(EntSealTypeEnum.COMPANY_SEAL.getCode());
                    enableSealType.add(EntSealTypeEnum.FINANCE_SEAL.getCode());
                    enableSealType.add(EntSealTypeEnum.CONTRACT_SEAL.getCode());
                    enableSealType.add(EntSealTypeEnum.PERSONNEL_SEAL.getCode());
                    enableSealType.add(EntSealTypeEnum.OTHER.getCode());
                }
                if (enableSealType.size() > 0) {
                    sealStatusBusinessService.enableEntSeal(authLog.getTenantId(), enableSealType);
                }
            }
            if(authLog.getAuthType().equals(TenantAuthType.CREATED.getType()) &&authLog.getAutoSeal() != null && authLog.getAutoSeal()){
                generateEntDefaultSeal(authLog.getName(),sysTenantUser);
            }
        }

        authAuditSendMsg(authLog.getAuthType(), authRequest.getAuthStatus(), sysTenantUser, authLog.getCheckMsg(), authLog);
        tenantInfoExtendService.updateById(extend);

        iSysTenantUserService.update(Wrappers.lambdaUpdate(SysTenantUser.class).eq(SysTenantUser::getId, sysTenantUser.getId()).set(SysTenantUser::getNickName, authLog.getName()));
        iSysTenantInfoService.update(Wrappers.lambdaUpdate(SysTenantInfo.class).eq(SysTenantInfo::getId, authLog.getTenantId()).set(SysTenantInfo::getTenantName, authLog.getName()));
        return updateById(authLog);
    }

    @Autowired
    private EntSealGenerateService sealGenerateService ;
    @Autowired
    private SignFileService signFileService ;

    @Autowired
    private EntSealBusinessService sealBusinessService ;

    @Transactional(rollbackFor = Exception.class)
    public void generateEntDefaultSeal(String topText, SysTenantUser sysTenantUser){
        String sealBase64 = sealGenerateService.generateEntCircleSeal(topText, "公章", null,true);
        String replace = sealBase64.replace("data:image/png;base64,", "");
        byte[] decode = Base64.decode(replace);
        String annexId = signFileService.saveAnnexStorage(decode, SignFileEnum.SEAL_FILE_ENT, null);
        EntSealSaveRequest request = new EntSealSaveRequest();
        request.setColor(SealColorEnum.RED.getCode());
        request.setDescription("实名认证通过系统自动生成印章");
        request.setSealName(topText);
        request.setMiddleText("公章");
        request.setCreateType(1);
        request.setSealType(1);
        request.setAnnexId(annexId);

//        request
//        request
        BusinessAuthVo businessAuthVo = new BusinessAuthVo();

        businessAuthVo.setAuthType(1);
        businessAuthVo.setAuthRelationId(sysTenantUser.getId());
        businessAuthVo.setAuthRelationName(sysTenantUser.getNickName());
        List<BusinessAuthVo> authVos = new ArrayList<>(1);
        authVos.add(businessAuthVo);

        request.setUserList(authVos);
        request.setManagerList(authVos);
//        request.setAuditorList(authVos);
        sealBusinessService.sysGeneratesave(request,sysTenantUser.getTenantId(),sysTenantUser.getId(),"");
    }

    @Autowired
    private SysMessageUtil sysMessageUtil;
    @Autowired
    private ISysTenantInfoService iSysTenantInfoService;
    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private ISysTenantUserService iSysTenantUserService;

    private void authAuditSendMsg(Integer authType, Boolean authStatus, SysTenantUser sysTenantUser, String checkMsg, TenantAuthLog authLog) {

//        SysTenantUser sysTenantUser = iSysTenantUserService.getById(tenantUserId);
        if (sysTenantUser == null) {
            return;
        }
//        SysTenantInfo sysTenantInfo = iSysTenantInfoService.getById(sysTenantUser.getTenantId());
//        if(sysTenantInfo == null){
//            return;
//        }
        SysUser sysUser = iSysUserService.getById(sysTenantUser.getUserId());
        if (sysUser == null) {
            return;
        }
        //首次认证
        if (authType == TenantAuthType.CREATED.getType()) {
            //发送短信
            Map<String, String> para = new HashMap<>();
            para.put("companyName", authLog.getName());
            para.put("reason", checkMsg);
            MessageDto messageDto = new MessageDto();
            messageDto.setSendType(SendType.IMMEDIATELY);
            List<String> receivers = new ArrayList<>();
            receivers.add(sysUser.getPhone());
            messageDto.setReceivers(receivers);
            messageDto.setTemplateCode(authStatus ? "entAuthSuccess" : "entAuthFaild");
            messageDto.setContentParaMap(para);
            sysMessageUtil.asyncSendMessage(messageDto);

//                //发送站内信
            MailDto mailDto = new MailDto();
            mailDto.setSendType(SendType.IMMEDIATELY);
            Map<MesAuthType, List<String>> userMap = new HashMap<>();
            List<String> userIds = new ArrayList<>();
            userIds.add(sysTenantUser.getId());
            userMap.put(MesAuthType.USER, userIds);
            mailDto.setReceivers(userMap);

            mailDto.setTemplateCode(authStatus ? "ent_auth_success" : "ent_auth_faild");

            Map<String, String> contentParaMap = new HashMap<>();
            contentParaMap.put("companyName", authLog.getName());
            contentParaMap.put("reason", checkMsg);

            Map<String, Map<String, String>> buttonParaMap = new HashMap<>();
            Map<String, String> para1 = new HashMap<>();
            para1.put("1", "1");
            buttonParaMap.put("re_auth", para1);
            mailDto.setButtonParaMap(buttonParaMap);

            mailDto.setTitleParaMap(contentParaMap);
            mailDto.setContentParaMap(contentParaMap);

            //发送通知
            sysMessageUtil.asyncSendMail(mailDto);
        } else if (authType == TenantAuthType.CHANGE.getType()) {
            //重新认证
            //TODO 需求尚未明确
        }
    }

    @Override
    public TenantAuthLogVO getAuthLogById(String logId) {

        TenantAuthLog temp = this.getById(logId);
        if (temp == null) {
            return new TenantAuthLogVO();
        }
        TenantAuthLogVO tenantInfoDTO = new TenantAuthLogVO();
        tenantInfoDTO.setId(temp.getId());
        tenantInfoDTO.setAuthType(temp.getAuthStatus());
        tenantInfoDTO.setName(temp.getName());
//        if (MyStringUtils.isNotBlank(temp.getOrganizationNo())) {
//            tenantInfoDTO.setOrganizationNo(DesensitizationUtils.around(temp.getOrganizationNo(), 6, 4));
//        }
        String organizationNo = temp.getOrganizationNo();
        try {
            organizationNo = RsaUtils.decryptByPrivateKey(SignCommonConstant.PRIVATEKEYSTRING, organizationNo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tenantInfoDTO.setOrganizationNo(organizationNo);
        tenantInfoDTO.setCreateTime(temp.getCreateTime());
        tenantInfoDTO.setCorporation(temp.getCorporation());
        tenantInfoDTO.setAuthType(temp.getAuthType());
        tenantInfoDTO.setTenantId(temp.getTenantId());
        String corporationNo = temp.getCorporationNo();
        try {
            corporationNo = RsaUtils.decryptByPrivateKey(SignCommonConstant.PRIVATEKEYSTRING, corporationNo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tenantInfoDTO.setCorporationNo(corporationNo);
        String phone = temp.getPhone();
        try {
            phone = RsaUtils.decryptByPrivateKey(SignCommonConstant.PRIVATEKEYSTRING, phone);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tenantInfoDTO.setPhone(phone);
        tenantInfoDTO.setTenantType(temp.getTenantType());
        AnnexStorage query = new AnnexStorage();
        query.setFatherId(temp.getId());
        List<AnnexStorage> files = iAnnexStorageService.getByEntity(query);
        if (CollUtil.isNotEmpty(files)) {
            files.forEach(f -> {
                StorageDto storageDto = new StorageDto();
                storageDto.setId(f.getId());
                storageDto.setRealName(f.getRealName());
                if (f.getDataCategory().equals(AnnexType.IDPIC1)) {
                    tenantInfoDTO.setIdCardFace(storageDto);
                } else if (f.getDataCategory().equals(AnnexType.IDPIC2)) {
                    tenantInfoDTO.setIdCardEmblem(storageDto);
                } else if (f.getDataCategory().equals(AnnexType.ORGANIZATIONPIC)) {
                    tenantInfoDTO.setBusinessLicense(storageDto);
                } else if (f.getDataCategory().equals(AnnexType.AUTHORIZEBOOK)) {
                    tenantInfoDTO.setAuthorizeBook(storageDto);
                }
            });
        }

        return tenantInfoDTO;
    }

    @Override
    public TenantAuthLog getAuthLogByOrderNo(String tenantId,String orderNo) {
        LambdaQueryWrapper<TenantAuthLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantAuthLog::getTenantId, tenantId).eq(TenantAuthLog::getOrderNo, orderNo).eq(TenantAuthLog::getDeleteFlag, 0);
        List<TenantAuthLog> list = list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public TenantAuthLog getAuthLogByNoAuth(String tenantId) {
        LambdaQueryWrapper<TenantAuthLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantAuthLog::getTenantId, tenantId).eq(TenantAuthLog::getAuthStatus, TenantAuthStatus.STATUS0.getStatus()).eq(TenantAuthLog::getDeleteFlag, 0).orderByDesc(TenantAuthLog::getCreateTime);
        List<TenantAuthLog> list = list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public TenantAuthLog getAuthLogByNoAuthUpdate(String tenantId) {
        LambdaQueryWrapper<TenantAuthLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantAuthLog::getTenantId, tenantId).eq(TenantAuthLog::getAuthStatus, TenantAuthStatus.STATUS0.getStatus()).eq(TenantAuthLog::getAuthType, 2).eq(TenantAuthLog::getDeleteFlag, 0).orderByDesc(TenantAuthLog::getCreateTime);
        List<TenantAuthLog> list = list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }


}
