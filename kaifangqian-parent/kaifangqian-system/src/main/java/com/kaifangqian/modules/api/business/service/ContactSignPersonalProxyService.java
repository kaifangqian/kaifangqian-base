/**
 * @description 个人用户签署代理服务
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
package com.kaifangqian.modules.api.business.service;

import com.kaifangqian.external.sign.request.SignOrderRequest;
import com.kaifangqian.modules.api.exception.RequestParamsException;
import com.kaifangqian.modules.cert.enums.CertHolderTypeEnum;
import com.kaifangqian.modules.cert.enums.CertTypeEnum;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.system.entity.ApiDeveloperManage;
import com.kaifangqian.modules.system.entity.SysTenantUser;
import com.kaifangqian.modules.system.entity.SysUser;
import com.kaifangqian.common.constant.ApiCode;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.api.vo.base.ContractPositionParam;
import com.kaifangqian.modules.api.vo.base.ContractRelationDoc;
import com.kaifangqian.modules.api.vo.base.ContractSignPersonalProxy;
import com.kaifangqian.modules.api.vo.base.ContractUser;
import com.kaifangqian.modules.opensign.dto.SignTaskInfo;
import com.kaifangqian.modules.opensign.dto.SignTaskThreadlocalVO;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.service.business.vo.RuCreateData;
import com.kaifangqian.modules.opensign.service.business.vo.RuDataDoc;
import com.kaifangqian.modules.storage.entity.AnnexStorage;
import com.kaifangqian.utils.MyStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: ContactSignPersonalProxyService
 * @Package: com.kaifangqian.modules.api.business.service
 * @ClassName: ContactSignPersonalProxyService
 * @author: FengLai_Gong
 * @Date: 2024/5/23 10:09
 */
@Service
public class ContactSignPersonalProxyService extends ContractService {


    /**
     * @Description #授权签署
     * @Param [contractSignPersonalProxy]
     * @return void
     **/
    public void contactSignPersonalProxy(ContractSignPersonalProxy contractSignPersonalProxy)  {
        //校验操作人
        ApiDeveloperManage apiDeveloperManage = apiDeveloperManageService.getByToken(contractSignPersonalProxy.getAppAuthToken());
        if(apiDeveloperManage == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,token权限不存在");
        }
        String tenantId = apiDeveloperManage.getTenantId();
        if(contractSignPersonalProxy == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,参数缺失");
        }
        if(contractSignPersonalProxy.getSignatureSubjectType() == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,个人签名主体类型参数缺失");
        }
        if(!contractSignPersonalProxy.getSignatureSubjectType().equals("PERSON") && !contractSignPersonalProxy.getSignatureSubjectType().equals("EMPLOYEE")){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,个人签名主体类型参数异常");
        }

        //校验业务线实例数据
        SignRu signRu = ruService.getById(contractSignPersonalProxy.getContractId());
        if(signRu == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,合同不存在");
        }
        if(signRu.getStatus() == null || signRu.getStatus() != SignRuStatusEnum.SIGNING.getCode()){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,合同签署状态异常");
        }
        String reId  = signRu.getSignReId() ;

        //校验签署人数据
        ContractUser contractUser = contractSignPersonalProxy.getSigner();
        checkContractUser(contractUser,"签署人");
        SysUser sysUser = sysUserService.getUserByName(contractUser.getContact());
        if(sysUser == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签署人不存在");
        }
        //任务线程临时变量
        String signTaskThreadSignRuId = null ;
        String signTaskThreadTaskId = null ;
        String signTaskThreadTaskType = null ;
        String signTaskThreadUserTaskId = null ;
        Integer signTaskThreadUserType = null ;
        //用户变量数据
        String entTenantId = null ;
        String entTenantUserId = null ;
        String userId = sysUser.getId() ;
        String tenantUserId = null ;
        String personalTenantId = null ;
        String personalTenantUserId = null ;
        SysTenantUser personalTenantUser = null ;
        if(contractSignPersonalProxy.getSignatureSubjectType().equals("EMPLOYEE")){
            //发起人内部节点
            //获取签署人当前租户下用户id
            SysTenantUser tenantUser = tenantUserService.getTenantUser(apiDeveloperManage.getTenantId(), userId);
            if(tenantUser == null){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签署人不存在");
            }
            tenantUserId = tenantUser.getId();
            entTenantId = tenantUser.getTenantId();
            entTenantUserId = tenantUser.getId() ;
            //获取签署人个人租户id
            personalTenantUser = tenantUserService.getPersonalTenantUser(userId);
            if(personalTenantUser == null){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签署人不存在");
            }
            personalTenantId = personalTenantUser.getTenantId();
            personalTenantUserId = personalTenantUser.getId();
        }else{
            //个人签署方
            personalTenantUser = tenantUserService.getPersonalTenantUser(userId);
            if(personalTenantUser == null){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签署人不存在");
            }
            tenantUserId = personalTenantUser.getId();
            personalTenantId = personalTenantUser.getTenantId();
            personalTenantUserId = personalTenantUser.getId();
        }
        if(tenantUserId == null || personalTenantId == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签署人不存在");
        }
        //获取个人签章授权
        UserSealAuth userSealAuth = userSealAuthService.getSealId(personalTenantUserId, reId);
        if(userSealAuth == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签章授权不存在");
        }
        SignPersonSeal signPersonSeal = personSealService.getById(userSealAuth.getSealId());
        if(signPersonSeal == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签章数据不存在");
        }
        AnnexStorage annex = signFileService.findByFatherIdAndDataCategory(SignFileEnum.SEAL_FILE_PERSON, signPersonSeal.getId());
        if(annex == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签章数据不存在");
        }
        byte[] sealByte = signFileService.getByteById(annex.getId());
        if(sealByte == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签章数据不存在");
        }
        //根据任务数据，查询当前操作人的数据，可能有多条
        SignRuTask task = new SignRuTask();
        task.setSignRuId(signRu.getId());
        task.setTenantUserId(tenantUserId);
        task.setTaskStatus(1);
        task.setTaskType(TaskTypeEnum.SIGN_TASK.getCode());
        List<SignRuTask> taskList = ruTaskService.getByEntity(task);
        if(taskList == null || taskList.size() == 0){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签署人当前无签署任务");
        }
        List<String> signerIdList = taskList.stream().map(SignRuTask::getUserTaskId).collect(Collectors.toList());
        //签署人类型
        String signerNodeType = "";
        //找到对应的签署人数据
        String signerId = null ;
        String taskId = null ;
        if(contractSignPersonalProxy.getSignatureSubjectType().equals("EMPLOYEE")){
            List<SignRuSender> senderList = ruSenderService.listByIds(signerIdList);
            if(senderList == null || senderList.size() == 0){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签署人不存在");
            }
            Integer minSignerOrder = null ;
            for(SignRuSender sender : senderList){
                if(sender.getSenderType().equals(SenderTypeEnum.ENTERPRISE)){
                    throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,个人签名主体类型不能包含企业签章");
                }
                if(minSignerOrder == null){
                    minSignerOrder = sender.getSenderOrder() ;
                    signerId = sender.getId();
                    if(sender.getSenderType() == SenderTypeEnum.OPERATOR.getCode()){
                        signerNodeType = "AGENT_SIGN";
                    }else if(sender.getSenderType() == SenderTypeEnum.LEGAL_PERSON.getCode()){
                        signerNodeType = "LEGAL_PERSON_SIGN";
                    }else if(sender.getSenderType() == SenderTypeEnum.PERSONAL.getCode()){
                        signerNodeType = "PERSONAL_SIGN";
                    }else if(sender.getSenderType() == SenderTypeEnum.ENTERPRISE.getCode()){
                        signerNodeType = "ENTERPRISE_SEAL";
                    }
                }
                if(sender.getSenderOrder() < minSignerOrder){
                    minSignerOrder = sender.getSenderOrder() ;
                    signerId = sender.getId();
                    if(sender.getSenderType() == SenderTypeEnum.OPERATOR.getCode()){
                        signerNodeType = "AGENT_SIGN";
                    }else if(sender.getSenderType() == SenderTypeEnum.LEGAL_PERSON.getCode()){
                        signerNodeType = "LEGAL_PERSON_SIGN";
                    }else if(sender.getSenderType() == SenderTypeEnum.PERSONAL.getCode()){
                        signerNodeType = "PERSONAL_SIGN";
                    }else if(sender.getSenderType() == SenderTypeEnum.ENTERPRISE.getCode()){
                        signerNodeType = "ENTERPRISE_SEAL";
                    }
                }
            }
        }else {
            List<SignRuSigner> signerList = ruSignerService.listByIds(signerIdList);
            if(signerList == null || signerList.size() == 0){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签署人不存在");
            }
            Integer minSignerOrder = null ;
            for(SignRuSigner signer : signerList){
                if(minSignerOrder == null){
                    minSignerOrder = signer.getSignerOrder() ;
                    signerId = signer.getId();
                }
                if(signer.getSignerOrder() < minSignerOrder){
                    minSignerOrder = signer.getSignerOrder() ;
                    signerId = signer.getId();
                }
            }
            signerNodeType = "RECEIVER_PERSONAL";
        }
        if(signerId == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签署人不存在");
        }
        for(SignRuTask ruTask : taskList){
            if(ruTask.getUserTaskId().equals(signerId)){
                taskId = ruTask.getId();
                signTaskThreadSignRuId = signRu.getId();
                signTaskThreadTaskId = ruTask.getId();
                signTaskThreadTaskType = ruTask.getTaskType();
                signTaskThreadUserTaskId = ruTask.getUserTaskId();
                signTaskThreadUserType = ruTask.getUserType();
            }
        }
        if(taskId == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签署人签署任务不存在");
        }

        //解析签署位置集合数据，追加到签署人原有签署控件上，保存控件数据
        //校验签约文件数据
        List<SignRuDoc> docList = ruDocService.listByRuId(signRu.getId());
        if(docList == null || docList.size() == 0){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签约文件不存在");
        }
        List<SignRuDocOperate> ruDocOperateList = ruDocOperateService.listByRuId(signRu.getId());
        if(ruDocOperateList == null || ruDocOperateList.size() == 0){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签约文件不存在");
        }

        //校验签署位置合集数据
        List<ContractPositionParam> positionParamList = contractSignPersonalProxy.getPositionParamList();
        if(positionParamList != null && positionParamList.size() > 0) {
            //创建整合数据
            RuCreateData createData = new RuCreateData();
            createData.setRuId(signRu.getId());
            for(ContractPositionParam positionParam : positionParamList){
                //校验参数
                checkPositionParam(positionParam,signerNodeType);
            }
            //整合关联文档数据
            for(ContractPositionParam positionParam : positionParamList){
                List<ContractRelationDoc> relationDocList = positionParam.getRelationDocList();
                for(ContractRelationDoc contractRelationDoc : relationDocList){
                    String docId = null ;
                    SignRuDoc ruDataRuDoc = null ;
                    byte[] fileByte = null ;
                    String annexId = null ;
                    if(contractRelationDoc.getDocType().equals("1")){
                        //上传的
                        for(SignRuDoc ruDoc : docList){
                            if(ruDoc.getDocOriginId().equals(contractRelationDoc.getDocId())){
                                ruDataRuDoc = ruDoc ;
                                docId = ruDoc.getId();
                                for(SignRuDocOperate ruDocOperate : ruDocOperateList){
                                    if(ruDocOperate.getDocId().equals(docId) && ruDocOperate.getIsCurrent() == SignCurrentEnum.IS_CURRENT.getCode()){
                                        annexId = ruDocOperate.getAnnexId();
                                    }
                                }
                            }
                        }
//                        for(SignRuDocOperate ruDocOperate : ruDocOperateList){
//                            if(ruDocOperate.getAnnexId().equals(contractRelationDoc.getDocId())){
//                                docId = ruDocOperate.getDocId() ;
//                                annexId = ruDocOperate.getAnnexId();
//                                for(SignRuDoc ruDoc : docList){
//                                    if(ruDoc.getId().equals(docId)){
//                                        ruDataRuDoc = ruDoc ;
//                                    }
//                                }
//                            }
//                        }
                    }else {
                        //模板
                        for(SignRuDoc ruDoc : docList){
                            if(ruDoc.getDocOriginId().equals(contractRelationDoc.getDocId())) {
                                docId = ruDoc.getId() ;
                                ruDataRuDoc = ruDoc ;
                                Date time = null ;
                                for(SignRuDocOperate ruDocOperate : ruDocOperateList){
                                    if(ruDocOperate.getDocId().equals(ruDoc.getId()) && ruDocOperate.getIsCurrent() == SignCurrentEnum.NOT_CURRENT.getCode()){
                                        if(time == null){
                                            time = ruDocOperate.getCreateTime();
                                            annexId = ruDocOperate.getAnnexId();
                                        }
                                        if(ruDocOperate.getCreateTime().before(time)){
                                            time = ruDocOperate.getCreateTime();
                                            annexId = ruDocOperate.getAnnexId();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if(annexId == null){
                        throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,关联文档不存在");
                    }
                    fileByte = signFileService.getByteById(annexId);
                    if(docId == null || ruDataRuDoc == null || fileByte == null){
                        throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,关联文档不存在");
                    }
                    //关联关系
                    createData.getAddDocAnnex2RuDocMap().put(contractRelationDoc.getDocId(),docId);
                    //文档数据
                    RuDataDoc ruDataDoc = new RuDataDoc();
                    ruDataDoc.setRuDoc(ruDataRuDoc);
                    ruDataDoc.setFileByte(fileByte);
                    createData.getDocList().add(ruDataDoc);
                }
            }
            //查找控件数据
            if(positionParamList != null && positionParamList.size() > 0) {
                for(ContractPositionParam positionParam : positionParamList){
                    //整合数据
                    if(positionParam.getSignPositionType().equals("KEYWORD")){
                        //关键字
                        buildKeySignControl(positionParam,createData,signerId,ControlOriginTypeEnum.OPERATION.getCode());
                    }else {
                        //设定坐标
                        buildPositionControl(positionParam,createData,signerId,ControlOriginTypeEnum.OPERATION.getCode());
                    }
                }
            }
            //保存数据
            ruDataService.saveControlList(createData);
        }

        //根据签署人的顺序进行签署
        //获取该签署人证书数据，签章数据，文件数据，控件数据
        //获取证书
        String certTenantId = personalTenantId ;
        CertTypeEnum certTypeEnum = null;
        Integer certHolderType = CertHolderTypeEnum.PERSONAL.getCode();
        //如果是使用CA证书
        if (SignRuCaSignTypeEnum.CA.getCode().equals(signRu.getCaSignType())) {
            certTypeEnum = CertTypeEnum.CA_TEST;
        }
        //如果是使用平台放篡改证书
        else if (SignRuCaSignTypeEnum.SYSTEM_CA.getCode().equals(signRu.getCaSignType())) {
            certTypeEnum = CertTypeEnum.SYSTEM;
        }
        //根据系统配置，计算真正需要的证书类型
        certTypeEnum = certBusinessService.calculateCertType(certTypeEnum.getCode(), certHolderType);
        if (certTypeEnum == null) {
            throw new PaasException("实例使用证书方式异常", null);
        }
//        //准备证书数据
//        CertificateProperty certificateProperty = certBusinessService.findAndGenerateCertProperty(certTenantId, certTypeEnum,certHolderType);
//        if(certificateProperty == null){
//            throw new PaasException("业务线实例-证书不存在");
//        }
        //控件数据
        List<SignRuDocControl> controlList = ruDocControlService.listSignControlBySignerId(signerId);
        if(controlList == null || controlList.size() == 0){
            throw new PaasException("业务线实例-控件不存在");
        }
        //操作类型
        Integer operateType = OperateTypeEnum.SIGN.getCode() ;
        OperateTypeEnum operateTypeEnum = OperateTypeEnum.getByCode(operateType);
        //保存控件数据
        //签署记录
        SignRuOperateRecord operateRecord = null ;
        if(signRu.getCaSignType() == SignRuCaSignTypeEnum.CA.getCode()){
            operateRecord = new SignRuOperateRecord();
            operateRecord.setSignRuId(signRu.getId());
            //operateRecord.setCertId(certificateProperty.getCertificateInfoId());
            operateRecord.setConfirmOrderNo(null);
            operateRecord.setTaskId(taskId);
            operateRecord.setAccountId(userId);
            operateRecord.setTenantId(apiDeveloperManage.getTenantId());
            operateRecord.setTenantUserId(tenantUserId);
            operateRecord.setOperateType(SignRecordOperateTypeEnum.PRIVATE_SIGN.getType());
            operateRecord.setActionType(SignRecordActionTypeEnum.SUBMIT_SIGN.getType());
            operateRecord.setIpAddr(null);
            operateRecord.setOperateTime(new Date());
        }

        // 获取平台个人签署认证方式
        String personalSignAuth = null;


        // 计算个人签署认证方式

        if(MyStringUtils.isNotBlank(contractUser.getPersonalSignAuth()) && contractUser.getPersonalSignAuth().equals(PersonalSignAuthTypeEnum.NOT_REQUIRED.getType())){
            // String personalSignAuthPlatform = ruBusinessService.getSystemPersonalSignAuthType();
            personalSignAuth = PersonalSignAuthTypeEnum.NOT_REQUIRED.getType();
        }


        //签署操作
        ruBusinessService.operate(controlList,signRu,operateTypeEnum,personalTenantId,sealByte,operateRecord,SignTypeEnum.AUTO_SIGN.getCode(),personalSignAuth);
        SignTaskThreadlocalVO threadlocalVO = new SignTaskThreadlocalVO();
        threadlocalVO.setSignRuId(signTaskThreadSignRuId);
        threadlocalVO.setTaskId(signTaskThreadTaskId);
        threadlocalVO.setTaskType(signTaskThreadTaskType);
        threadlocalVO.setUserTaskId(signTaskThreadUserTaskId);
        threadlocalVO.setUserType(signTaskThreadUserType);
        SignTaskInfo.THREAD_LOCAL.set(threadlocalVO);
        //存储本地线程变量
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(sysUser, loginUser);
        if(contractSignPersonalProxy.getSignatureSubjectType().equals("EMPLOYEE")){
            loginUser.setTenantId(entTenantId);
            loginUser.setTenantUserId(entTenantUserId);
        }else {
            loginUser.setTenantId(personalTenantId);
            loginUser.setTenantUserId(personalTenantUserId);
        }

        MySecurityUtils.THREAD_LOCAL.set(loginUser);
        //驱动
        iFlowService.complete(null, RuFlowEnum.APPROVE.getName());
        //签署
        ruCallbackService.callback(signRu.getId(),taskId, SignCallbackTypeEnum.SUBMIT_SIGN);
        SignTaskInfo.THREAD_LOCAL.remove();
        MySecurityUtils.THREAD_LOCAL.remove();
    }
    /**
     * 根据系统配置、签署实例、签署节点校验并设置签署节点配置
     * @param confirm
     * @param signRu
     */
    private String setSignNodeConfig(ContractUser confirm, String personalSignAuthPlatform, SignRu signRu){

        // 默认设置为需实名
        String finalPersonalSignAuth = PersonalSignAuthTypeEnum.REQUIRED.getType();

        // 获取平台级个人实名类型配置，如果配置了，则使用配置的，否则设置为需实名
        if (MyStringUtils.isNotBlank(personalSignAuthPlatform)) {
            // 如果平台配置为无须实名或需实名，则直接使用平台的配置
            if (personalSignAuthPlatform.equals(PersonalSignAuthTypeEnum.NOT_REQUIRED.getType()) ||
                    personalSignAuthPlatform.equals(PersonalSignAuthTypeEnum.REQUIRED.getType())) {
                finalPersonalSignAuth = personalSignAuthPlatform;
            }
            // 如果平台配置为允许不实名认证，则判断签署实例的实名认证要求
            else if (personalSignAuthPlatform.equals(PersonalSignAuthTypeEnum.ALLOWED.getType())) {
                if (signRu != null && MyStringUtils.isNotBlank(signRu.getPersonalSignAuth())) {
                    // 如果签署实例为无须实名或需实名，则直接签署实例的配置
                    if (signRu.getPersonalSignAuth().equals(PersonalSignAuthTypeEnum.NOT_REQUIRED.getType()) ||
                            signRu.getPersonalSignAuth().equals(PersonalSignAuthTypeEnum.REQUIRED.getType())) {
                        finalPersonalSignAuth = signRu.getPersonalSignAuth();
                    }
                    // 如果签署实例为允许不实名认证，则判断签署任务的实名认证要求
                    else if (signRu.getPersonalSignAuth().equals(PersonalSignAuthTypeEnum.ALLOWED.getType())) {
                        // 判断签署任务的实名认证要求是否为空，为空则直接配置为需实名。
                        if (confirm != null && MyStringUtils.isNotBlank(confirm.getPersonalSignAuth())) {
                            // 判断用户选择的实名认证要求,如果签署任务为无须实名或需实名，则直接使用签署任务的配置，否则直接配置为需实名
                            if (confirm.getPersonalSignAuth().equals(PersonalSignAuthTypeEnum.REQUIRED.getType()) ||
                                    confirm.getPersonalSignAuth().equals(PersonalSignAuthTypeEnum.NOT_REQUIRED.getType())) {
                                finalPersonalSignAuth = confirm.getPersonalSignAuth();
                            }
                        }
                    }
                }else if (confirm != null && MyStringUtils.isNotBlank(confirm.getPersonalSignAuth())) {
                    // 判断用户选择的实名认证要求,如果签署任务为无须实名或需实名，则直接使用签署任务的配置，否则直接配置为需实名
                    if (confirm.getPersonalSignAuth().equals(PersonalSignAuthTypeEnum.REQUIRED.getType()) ||
                            confirm.getPersonalSignAuth().equals(PersonalSignAuthTypeEnum.NOT_REQUIRED.getType())) {
                        finalPersonalSignAuth = confirm.getPersonalSignAuth();
                    }
                }
            }
        }
        return finalPersonalSignAuth;
    }
}