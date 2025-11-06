/**
 * @description 电子签署保存并发起
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

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.api.exception.RequestParamsException;
import com.kaifangqian.modules.api.vo.base.*;
import com.kaifangqian.modules.api.vo.request.*;
import com.kaifangqian.modules.opensign.dto.SignTaskInfo;
import com.kaifangqian.modules.opensign.dto.SignTaskThreadlocalVO;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.service.business.vo.*;
import com.kaifangqian.modules.opensign.vo.response.ru.SubmitResponse;
import com.kaifangqian.modules.system.entity.ApiDeveloperManage;
import com.kaifangqian.modules.system.entity.SysTenantInfo;
import com.kaifangqian.modules.system.entity.SysTenantUser;
import com.kaifangqian.modules.system.entity.SysUser;
import com.kaifangqian.common.base.entity.BaseEntity;
import com.kaifangqian.common.constant.ApiCode;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.modules.storage.entity.AnnexStorage;
import com.kaifangqian.utils.IPUtil;
import com.kaifangqian.utils.MyStringUtils;
import com.kaifangqian.utils.UUIDGenerator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: ContractDraftAndStartService
 * @Package: com.kaifangqian.modules.api.business.service
 * @ClassName: ContractDraftAndStartService
 * @author: FengLai_Gong
 * @Date: 2024/5/23 10:09
 */
@Service
public class ContractDraftAndStartService extends ContractService {


    /**
     * @Description #业务线数据保存业务线实例并发起
     * @Param [request]
     * @return java.lang.String
     **/
    public String contractDraftAndStart(ContractDraftRequest request){

        if(request.getSend() == null){
            throw new RequestParamsException(ApiCode.PARAM_MISSING,"是否直接发起参数缺失");
        }
        //创建整合数据
        RuCreateData createData = new RuCreateData();
//        //业务操作传递对象
//        ContractVo contractVo = new ContractVo();
        //校验操作人
        checkAndBuildOperator(request,createData);

        //校验到期时间
        checkAndBuildExpireDate(request,createData);

        //校验并且构建业务线实例数据
        checkAndBuildBase(request,createData);

        //校验并且构建业务线实例签约文件数据
        checkAndBuildDocList(request,createData);

        //校验并且构建业务线实例签署人及其签署控件数据
        checkAndBuildSignerList(request,createData);

        //校验模板数据
        checkAndBuildTemplate(request,createData);

        //保存数据
        ruDataService.createRuData(createData);
        String startRuId = createData.getRuId() ;
        if(request.getSend().equals("1")){
            //发起业务线实例
            startRuId = ruBusinessService.startForApi(createData.getSignRu().getId(),createData.getTenantUser().getId());
            if(startRuId == null || startRuId.length() == 0){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,发起失败");
            }
            try {
                //生成主题和编号
                ruBusinessService.startGenerateSubject(startRuId);
            }catch (Exception e){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,生成主题和编号失败");
            }

            Date operateTime = new Date();
            //发起流程
            iFlowService.complete(startRuId, RuFlowEnum.INITIATE_FLOW.getName());
            //回调发起
            LoginUser currentUser = MySecurityUtils.getCurrentUser();
            ruCallbackService.callback(startRuId,null, SignCallbackTypeEnum.SEND_SIGNING);
            //查询是否还有任务
            Boolean allTaskComplete = ruService.allTaskComplete(startRuId);
            if(allTaskComplete && (request.getAutoFinish() == null || request.getAutoFinish() ==  SignFinishTypeEnum.AUTO_FINISH.getCode())){
                //完成签署
                ruCallbackService.callback(startRuId,null,SignCallbackTypeEnum.COMPLETE);
            }
            //操作记录
            SignRuOperateRecord ruOperateRecord = new SignRuOperateRecord();
            ruOperateRecord.setSignRuId(startRuId);
            ruOperateRecord.setAccountId(currentUser.getId());
            ruOperateRecord.setTenantId(currentUser.getTenantId());
            ruOperateRecord.setTenantUserId(currentUser.getTenantUserId());
            ruOperateRecord.setOperateType(SignRecordOperateTypeEnum.START.getType());
            ruOperateRecord.setActionType(SignRecordActionTypeEnum.START.getType());
            ruOperateRecord.setOperateTime(operateTime);
//        ruOperateRecord.setAddrIp(IPUtil.getIpAddr(httpServletRequest));
            ruOperateRecordService.save(ruOperateRecord);
        }
        //清空本地变量
        MySecurityUtils.THREAD_LOCAL.remove();
        //返回
        return startRuId ;
    }

    /**
     * @Description #添加签署节点
     * @Param [request]
     * @return void
     **/
    @Transactional(rollbackFor = Exception.class)
    public void addSignNode(ContractDraftRequest request){

        String contractId = request.getContractId();

        // 接口参数校验
        if(contractId == null || contractId.length() == 0){
            throw new RequestParamsException(ApiCode.PARAM_MISSING,"contractId参数缺失");
        }
        if(contractId.length() > 64){
            throw new RequestParamsException(ApiCode.PARAM_SIZE_INVALID,"contractId参数格式不正确，长度不合法");
        }
        if(contractId.contains(" ")){
            throw new RequestParamsException(ApiCode.PARAM_SIZE_INVALID,"contractId参数格式不正确，不能包含空格");
        }

        //获取业务线实例数据，判断业务线实例数据是否为空，判断业务线实例数据状态是否为签署中
        SignRu ru = ruService.getById(request.getContractId());



        if (ru == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,合同id：" + request.getContractId() + "数据不存在");
        }else if (ru != null && ru.getStatus() == SignRuStatusEnum.DONE.getCode()){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,合同id：" + request.getContractId() + "已完成签署，不能继续追加签署方");
        }else if(ru != null && (ru.getAutoFinish() == null || ru.getAutoFinish() == SignFinishTypeEnum.AUTO_FINISH.getCode())){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"追加签署方接口仅支持签署完成类型为手动结束，如需追加签署方，请检查合同发起接口autoFinish字段值是否为0");
        }

        ApiDeveloperManage apiDeveloperManage = apiDeveloperManageService.getByToken(request.getAppAuthToken());

        if(apiDeveloperManage == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,token权限不存在");
        }

        if(!ru.getSysTenantId().equals(apiDeveloperManage.getTenantId())){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,合同id：" + request.getContractId() + "资源不属于对接方，无操作权限");
        }

        //创建整合数据
        RuCreateData createData = new RuCreateData();

        createData.setRuId(ru.getId());


        SysTenantInfo tenantInfo = tenantInfoService.getById(apiDeveloperManage.getTenantId());
        if(tenantInfo == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,账号不存在");
        }
        //传递保存租户数据
        createData.setTenantInfo(tenantInfo);

        // TODO 签署文件读取
        //解析签署位置集合数据，追加到签署人原有签署控件上，保存控件数据
        //校验签约文件数据
        List<SignRuDoc> docList = ruDocService.listByRuId(ru.getId());
        if(docList == null || docList.size() == 0){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签约文件不存在");
        }
        List<SignRuDocOperate> ruDocOperateList = ruDocOperateService.listByRuId(ru.getId());
        if(ruDocOperateList == null || ruDocOperateList.size() == 0){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签约文件不存在");
        }

        // 构建代签署文档关联信息
        if (request.getSignerList() != null && request.getSignerList().size() > 0){
            for (ContractSigner signer : request.getSignerList()) {
                List<ContractPositionParam> positionParamList = null;

                if(SignerTypeEnum.SENDER.getApiName().equals(signer.getSignerType()) || SignerTypeEnum.RECEIVER_ENT.getApiName().equals(signer.getSignerType())){
                    for (ContractInternalNode internalNode : signer.getInternalNodeList()){
                        positionParamList = internalNode.getPositionParamList();

                        if (!internalNode.getNodeType().equals(SenderTypeEnum.APPROVER.getApiName())){
                            buildDocRelationInfo(docList,ruDocOperateList,positionParamList,createData);
                        }
                    }
                }else if(SignerTypeEnum.RECEIVER_PERSONAL.getApiName().equals(signer.getSignerType())){
                    positionParamList = signer.getPositionParamList();
                    buildDocRelationInfo(docList,ruDocOperateList,positionParamList,createData);
                }
            }
        }

        //校验并且构建业务线实例签署人及其签署控件数据
        checkAndBuildSignerList(request,createData);

        //保存签署人数据
        ruBusinessService.saveRuSignerForApi(createData);

        //驱动
        iFlowService.complete(ru.getId(), "initiateSignTask");
        //ruCallbackService.callback(ru.getId(),null, SignCallbackTypeEnum.ADD_SIGN_NODE);

    }

    /**
     * @Description #签署完成
     * @Param [request]
     * @return void
     **/
    @Transactional(rollbackFor = Exception.class)
    public void completeSign(ContractCompleteRequest request){

        //获取业务线实例数据，判断业务线实例数据是否为空，判断业务线实例数据状态是否为签署中
        SignRu ru = ruService.getById(request.getContractId());

        if (ru == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,合同id：" + request.getContractId() + "数据不存在");
        }

        if (ru.getAutoFinish() == null || ru.getAutoFinish() == SignFinishTypeEnum.AUTO_FINISH.getCode()){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,合同id：" + request.getContractId() + "的签署结束类型为系统自动结束，不能手动结束。");
        }

        ApiDeveloperManage apiDeveloperManage = apiDeveloperManageService.getByToken(request.getAppAuthToken());

        if(apiDeveloperManage == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,token权限不存在");
        }

        if(!ru.getSysTenantId().equals(apiDeveloperManage.getTenantId())){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,合同id：" + request.getContractId() + "资源不属于对接方，无操作权限");
        }

        //查询是否还有任务
        Boolean allTaskComplete = ruService.allTaskComplete(ru.getId());
        if (allTaskComplete) {
            //完成签署
            iFlowService.complete(ru.getId(), "finishFlow");
            ruCallbackService.callback(ru.getId(), null, SignCallbackTypeEnum.COMPLETE);
        }else{
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,合同id：" + request.getContractId() + "还存在待完成的签署任务，不能手动结束。");
        }

    }

    /**
     * 签署撤回
     * @param request
     */
    @Transactional(rollbackFor = Exception.class)
    public void recallSign(ContractRecallRequest request){

        //获取业务线实例数据，判断业务线实例数据是否为空，判断业务线实例数据状态是否为签署中
        SignRu ru = ruService.getById(request.getContractId());

        if (ru == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,合同id：" + request.getContractId() + "数据不存在");
        }

        ApiDeveloperManage apiDeveloperManage = apiDeveloperManageService.getByToken(request.getAppAuthToken());

        if(apiDeveloperManage == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,token权限不存在");
        }

        if(!ru.getSysTenantId().equals(apiDeveloperManage.getTenantId())){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,合同id：" + request.getContractId() + "资源不属于对接方，无操作权限");
        }

        if (SignRuStatusEnum.APPROVING.getCode().equals(ru.getStatus()) || SignRuStatusEnum.WRITING.getCode().equals(ru.getStatus()) ||
                SignRuStatusEnum.SIGNING.getCode().equals(ru.getStatus()) ) {
            ru.setStatus(SignRuStatusEnum.REVOKE.getCode());
            boolean b = ruService.updateById(ru);
            if (!b) {
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"合同签署撤回失败");
            }
        }else {
            SignRuStatusEnum statusEnum = SignRuStatusEnum.getByCode(ru.getStatus());
            if (statusEnum == null) {
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"实例异常，状态不存在");
            }

            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"实例状态为"+ statusEnum.getName() + ",不可撤回");
        }

        //操作记录
        SignRuOperateRecord ruOperateRecord = new SignRuOperateRecord();
        ruOperateRecord.setSignRuId(ru.getId());
        ruOperateRecord.setTenantId(apiDeveloperManage.getTenantId());
        ruOperateRecord.setOperateType(SignRecordOperateTypeEnum.REVOKE.getType());
        ruOperateRecord.setActionType(SignRecordActionTypeEnum.REVOKE.getType());
        ruOperateRecord.setOperateTime(new Date());
        ruOperateRecord.setOperateReason("通过API撤销");
        ruOperateRecordService.save(ruOperateRecord);
        //撤回合同
        ruCallbackService.callback(ru.getId(), "", SignCallbackTypeEnum.RECALLED);

    }

    /**
     * @Description #合同审批API接口业务处理
     * @Param [request]
     * @return void
     **/
    @Transactional(rollbackFor = Exception.class)
    public void approveSign(ContractApproveRequest request){

        ApiDeveloperManage developerManage = apiDeveloperManageService.getByToken(request.getAppAuthToken());

        if(developerManage == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,token权限不存在");
        }

        //获取业务线实例数据，判断业务线实例数据是否为空，判断业务线实例数据状态是否为签署中
        SignRu ru = ruService.getById(request.getContractId());

        if (ru == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,合同id：" + request.getContractId() + "数据不存在");
        }

        if (!ru.getSysTenantId().equals(developerManage.getTenantId())){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,合同id：" + request.getContractId() + "资源不属于对接方，无操作权限");
        }

        // 状态合同状态是否未签署中，仅有签署中合同可进行合同审批
        if (ru.getStatus() != null && ru.getStatus() != SignRuStatusEnum.SIGNING.getCode()){
            // 获取合同当前状态名称
            SignRuStatusEnum  ruStatusEnum = SignRuStatusEnum.getByCode(ru.getStatus());
            String ruStatus = null;
            if (ruStatusEnum != null){
                ruStatus = ruStatusEnum.getName();
            }
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,合同审批仅支持签署中的合同，合同id："+request.getContractId()+"状态为："+ruStatus);
        }

        // 校验合同审批人信息
        checkContractUser(request.getSigner(),"合同审批人");

        SysUser currentUser = sysUserService.getUserByName(request.getSigner().getContact());

        if (currentUser == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,合同审批人：" + request.getSigner().getName() + "数据不存在");
        }

        //SysTenantUser tenantUser = tenantUserService.getPersonalTenantUser(currentUser.getId());

        SysTenantUser tenantUser = tenantUserService.getTenantUser(developerManage.getTenantId(), currentUser.getId());

        if (tenantUser == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,合同审批人：" + request.getSigner().getName() + "数据不存在");
        }

        //查询审批用户的审批任务
        SignRuTask query = new SignRuTask();
        query.setDeleteFlag(false);
        query.setSignRuId(ru.getId());
        query.setTenantUserId(tenantUser.getId());
        query.setTaskType(TaskTypeEnum.APPROVE_TASK.getCode());
        List<SignRuTask> ruTaskList = ruTaskService.getByEntity(query);
        if (ruTaskList == null || ruTaskList.size() == 0) {
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,当前用户无审批任务");
        }

        List<String> signerIdList = ruTaskList.stream().map(SignRuTask::getUserTaskId).collect(Collectors.toList());

        String taskId = null;
        SignRuSender signRuSender = null;
        String signTaskThreadTaskType = null;
        String signTaskThreadUserTaskId = null;
        Integer signTaskThreadUserType = null;

        List<SignRuSender> senderList = ruSenderService.listByIds(signerIdList);
        if(senderList == null || senderList.size() == 0){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签署人不存在");
        }

        //String signerId = null;
        //获取代办人信息
        for(SignRuSender sender : senderList){
            SignRuOperator sroQuery = new SignRuOperator();
            sroQuery.setSignRuId(ru.getId());
            sroQuery.setSignerId(sender.getId());
            sroQuery.setOperateStatus(OperateStatusEnum.WAIT_TO_FINISH.getCode());
            List<SignRuOperator> operator = ruOperatorService.getByEntity(sroQuery);
            if(operator != null && operator.size() > 0){
                signRuSender = sender;
                break;
            }
        }

        if(signRuSender == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签署人不存在");
        }

        //通过代办人，获取代办任务
        Boolean finishFlag = true;
        for (SignRuTask ruTask : ruTaskList) {
            if(ruTask.getUserTaskId().equals(signRuSender.getId())){
                taskId = ruTask.getId();

                signTaskThreadTaskType = ruTask.getTaskType();
                signTaskThreadUserTaskId = ruTask.getUserTaskId();
                signTaskThreadUserType = ruTask.getUserType();

                if (ruTask.getTaskStatus() == TaskStatusEnum.DONE.getCode()){
                    finishFlag = true;
                } else {
                    finishFlag = false;
                }
            }
        }
        if (finishFlag) {
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,合同id：" + request.getContractId() + "已办理完成，无需重复操作");
        }

        //操作记录
        SignRuOperateRecord ruOperateRecord = new SignRuOperateRecord();
        ruOperateRecord.setSignRuId(ru.getId());
        ruOperateRecord.setAccountId(tenantUser.getUserId());
        ruOperateRecord.setTenantId(tenantUser.getTenantId());
        ruOperateRecord.setTenantUserId(tenantUser.getId());

        if (signRuSender != null && signRuSender.getSenderType() != null && signRuSender.getSenderType() == SenderTypeEnum.ENTERPRISE.getCode()) {
            ruOperateRecord.setOperateType(SignRecordOperateTypeEnum.ENT_SIGN.getType());
        }else if (signRuSender != null && signRuSender.getSenderType() != null && signRuSender.getSenderType() == SenderTypeEnum.APPROVER.getCode()) {
            ruOperateRecord.setOperateType(SignRecordOperateTypeEnum.APPROVE.getType());
        }else {
            ruOperateRecord.setOperateType(SignRecordOperateTypeEnum.PRIVATE_SIGN.getType());
        }

        if (request.getApproval() != null && request.getApproval() == 1){
            ruOperateRecord.setActionType(SignRecordActionTypeEnum.APPROVE_CHECK.getType());
        }else if (request.getApproval() != null && request.getApproval() == 0){
            ruOperateRecord.setActionType(SignRecordActionTypeEnum.REJECT_CHECK.getType());
        }
        ruOperateRecord.setOperateTime(new Date());
        ruOperateRecord.setIpAddr("");
        ruOperateRecord.setTaskId(taskId);
        ruOperateRecord.setConfirmOrderNo("");
        ruOperateRecord.setOperateReason(request.getComment());
        ruOperateRecordService.save(ruOperateRecord);

        //存储签署任务本地线程变量
        SignTaskThreadlocalVO threadlocalVO = new SignTaskThreadlocalVO();
        threadlocalVO.setSignRuId(ru.getId());
        threadlocalVO.setTaskId(taskId);
        threadlocalVO.setTaskType(signTaskThreadTaskType);
        threadlocalVO.setUserTaskId(signTaskThreadUserTaskId);
        threadlocalVO.setUserType(signTaskThreadUserType);
        SignTaskInfo.THREAD_LOCAL.set(threadlocalVO);

        //存储用户信息本地线程变量
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(currentUser, loginUser);
        loginUser.setTenantId(tenantUser.getTenantId());
        loginUser.setTenantUserId(tenantUser.getId());
        MySecurityUtils.THREAD_LOCAL.set(loginUser);

        //执行签署驱动
        if (request.getApproval() != null && request.getApproval() == 1){
            //驱动
            iFlowService.complete(ru.getId(), RuFlowEnum.APPROVE.getName());
            //签署
            ruCallbackService.callback(ru.getId(), taskId, SignCallbackTypeEnum.SUBMIT_APPROVE);

            //查询是否还有任务
            Boolean allTaskComplete = ruService.allTaskComplete(ru.getId());
            if (allTaskComplete) {
                //完成签署
                ruCallbackService.callback(ru.getId(), null, SignCallbackTypeEnum.COMPLETE);
            }
        }else if (request.getApproval() != null && request.getApproval() == 0){
            //驱动
            iFlowService.complete(ru.getId(), RuFlowEnum.REJECT.getName());
            //拒签
            ruCallbackService.callback(ru.getId(), taskId, SignCallbackTypeEnum.SUBMIT_REFUSAL);
        }
        SignTaskInfo.THREAD_LOCAL.remove();
        MySecurityUtils.THREAD_LOCAL.remove();

    }

    public void checkAndBuildSignControlList(ContractPositionParam positionParam, RuCreateData createData, String signerId, String signerNodeType){
        if(positionParam == null){
            return;
        }
        //校验参数
        checkPositionParam(positionParam,signerNodeType);
        //整合数据
        if(positionParam.getSignPositionType().equals("KEYWORD")){
            //关键字
            buildKeySignControl(positionParam,createData,signerId,ControlOriginTypeEnum.START.getCode());
        }else {
            //设定坐标
            buildPositionControl(positionParam,createData,signerId,ControlOriginTypeEnum.START.getCode());
        }

    }

    public void buildReSignControlList(RuCreateData createData,String reSignerId,String ruSignerId){
        List<SignReDocControl> signReDocControlList = reDocControlService.listByReIdAndSignerId(createData.getReId(), reSignerId);
        if(signReDocControlList != null && signReDocControlList.size() > 0){
            for(SignReDocControl reDocControl : signReDocControlList){
                SignRuDocControl signRuDocControl = new SignRuDocControl();
                BeanUtils.copyProperties(reDocControl,signRuDocControl);
                signRuDocControl.setSignerId(ruSignerId);
                signRuDocControl.setId(UUIDGenerator.generate());
                signRuDocControl.setOriginType(ControlOriginTypeEnum.RE.getCode());
                List<SignReDocControlProperty> rePropertyList = reDocControlPropertyService.listByControlId(reDocControl.getId());
                if(rePropertyList == null || rePropertyList.size() == 0){
                    throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,原有签署控件数据异常");
                }
                for(SignReDocControlProperty reProperty : rePropertyList){
                    SignRuDocControlProperty ruProperty = new SignRuDocControlProperty();
                    ruProperty.setId(UUIDGenerator.generate());
                    ruProperty.setRuId(createData.getRuId());
                    ruProperty.setControlId(signRuDocControl.getId());
                    ruProperty.setPropertyType(reProperty.getPropertyType());
                    if(reProperty.getPropertyType().equals(ControlPropertyTypeEnum.RELATION_DOC.getCode())){
                        String ruDocId = createData.getReDoc2RuDocMap().get(reProperty.getPropertyValue());
                        if(ruDocId == null){
                            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,原有签署控件关联文件异常");
                        }
                        ruProperty.setPropertyValue(ruDocId);
                    }else {
                        ruProperty.setPropertyValue(reProperty.getPropertyValue());
                    }
                    //整合数据
                    createData.getRuSignControlPropertyList().add(ruProperty);
                }
                //整合数据
                createData.getRuSignControlList().add(signRuDocControl);
            }
        }
    }









    /**
     * @Description #保存业务线实例签约文件数据和附件数据
     * @Param [request, contractVo]
     * @return void
     **/
    public void checkAndBuildDocList(ContractDraftRequest request,RuCreateData createData){

        SignRu signRu = createData.getSignRu();

        List<SignReDoc> signReDocList = reDocService.listByReId(request.getSignReId());
//        if(signReDocList == null || signReDocList.size() == 0){
//            if(request.getAnnexList() == null || request.getAnnexList().size() == 0){
//                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签约文件未设置");
//            }
//        }

        if(signReDocList != null && signReDocList.size() > 0){
            //新建签约文件列表
            for(SignReDoc reDoc : signReDocList){

                RuDataDoc dataDoc = new RuDataDoc();

                SignRuDoc ruDoc = new SignRuDoc();
                ruDoc.setId(UUIDGenerator.generate());
                ruDoc.setSignRuId(signRu.getId());
                ruDoc.setDocType(reDoc.getDocType());
                ruDoc.setDocName(reDoc.getDocName());

                ruDoc.setOriginType(SignOriginTypeEnum.RE.getCode());
                ruDoc.setDeleteFlag(false);
                if(SignFileTypeEnum.TEMPLATE.getCode().equals(reDoc.getDocType())){
                    //如果是上传模版，则以模板id作为来源id
                    ruDoc.setDocOriginId(reDoc.getDocOriginId());

                    //如果是模板，不管是业务线还是自行选择模板，都需要复制模板数据
                    SignTemplateRecord current = templateRecordService.getCurrent(reDoc.getDocOriginId());
                    if(current == null){
                        //处理异常
                        throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,保存模板文件失败");
                    }

                    createData.getTemplateIdList().add(reDoc.getDocOriginId());
                    createData.getTemplateId2RuDocMap().put(reDoc.getDocOriginId(),ruDoc.getId());

                    //复制模版文件记录数据，转化成业务线实例文件记录数据
                    SignRuDocOperate ruDocOperate = new SignRuDocOperate();
                    ruDocOperate.setDocId(ruDoc.getId());
                    ruDocOperate.setIsCurrent(SignCurrentEnum.IS_CURRENT.getCode());
                    ruDocOperate.setSignRuId(signRu.getId());
                    String newFileAnnexId = signFileService.copyAnnexStorage(current.getAnnexId());
                    if(newFileAnnexId == null){
                        //处理异常
                        throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,保存模板文件失败");
                    }
                    ruDocOperate.setAnnexId(newFileAnnexId);
                    ruDocOperate.setDeleteFlag(false);
                    ruDocOperate.setId(UUIDGenerator.generate());
                    //pdf文件数据
                    byte[] fileServiceByteById = signFileService.getByteById(newFileAnnexId);
                    dataDoc.setFileByte(fileServiceByteById);
                    //图片数据
                    signFileService.copyFileImage(current.getAnnexId(),newFileAnnexId);
                    Integer docPage = annexImageService.countByAnnexId(newFileAnnexId);
                    if(docPage == null || docPage == 0){
                        docPage = pdfboxService.getPdfPage(fileServiceByteById);
                    }
                    ruDoc.setDocPage(docPage);
                    dataDoc.setDocPage(docPage);
                    dataDoc.setRuDocOperate(ruDocOperate);

                } else if(SignFileTypeEnum.UPLOAD.getCode().equals(reDoc.getDocType())){
                    //如果是上传文件，则以文件的annexId作为来源id
                    ruDoc.setDocOriginId(reDoc.getAnnexId());
                    //来自业务线的上传，查询业务线配置的temporary数据
                    SignRuDocOperate ruDocOperate = new SignRuDocOperate();
                    ruDocOperate.setId(UUIDGenerator.generate());
                    ruDocOperate.setDocId(ruDoc.getId());
                    ruDocOperate.setIsCurrent(SignCurrentEnum.IS_CURRENT.getCode());
                    ruDocOperate.setSignRuId(signRu.getId());
                    String newFileAnnexId = signFileService.copyAnnexStorage(reDoc.getAnnexId());
                    ruDocOperate.setAnnexId(newFileAnnexId);
                    ruDocOperate.setDeleteFlag(false);
                    ruDocOperate.setId(null);
                    dataDoc.setRuDocOperate(ruDocOperate);
                    //pdf文件数据
                    byte[] fileServiceByteById = signFileService.getByteById(newFileAnnexId);
                    dataDoc.setFileByte(fileServiceByteById);
                    //图片数据
                    signFileService.copyFileImage(reDoc.getAnnexId(),newFileAnnexId);
                    Integer docPage = annexImageService.countByAnnexId(newFileAnnexId);
                    if(docPage == null || docPage == 0){
                        docPage = pdfboxService.getPdfPage(fileServiceByteById);
                    }
                    ruDoc.setDocPage(docPage);
                    dataDoc.setDocPage(docPage);
                    dataDoc.setRuDocOperate(ruDocOperate);
                    createData.getAddDocAnnex2RuDocMap().put(reDoc.getAnnexId(),ruDoc.getId());

                }
                dataDoc.setReDocId(reDoc.getId());
                dataDoc.setDocType(reDoc.getDocType());
                dataDoc.setOriginId(reDoc.getDocOriginId());
                dataDoc.setOriginType(SignOriginTypeEnum.RE.getCode());
                dataDoc.setRuDoc(ruDoc);

                createData.getReDoc2RuDocMap().put(reDoc.getId(),ruDoc.getId());

                createData.getDocList().add(dataDoc);
            }
        }

        //追加在业务线实例上的签约文件
        List<ContractDocumentFile> signDocumentList = request.getSignDocumentList();
        if(signDocumentList != null && signDocumentList.size() > 0){
            for(ContractDocumentFile documentFile : signDocumentList){

                RuDataDoc dataDoc = new RuDataDoc();
                AnnexStorage annex = signFileService.getAnnexById(documentFile.getDocId());
                if(annex == null){
                    throw new RequestParamsException(ApiCode.PARAM_MISSING,"签约文件不存在");
                }
                //创造签约文件数据
                SignRuDoc ruDoc = new SignRuDoc();
                ruDoc.setId(UUIDGenerator.generate());
//                ruDoc.setDocName(annex.getRealName());
                ruDoc.setDocName(annex.getName());
                ruDoc.setSignRuId(signRu.getId());
                ruDoc.setDocType(SignFileTypeEnum.UPLOAD.getCode());
                ruDoc.setOriginType(SignOriginTypeEnum.UPLOAD.getCode());
                ruDoc.setDeleteFlag(false);
                //如果是追加的上传文件，则以documentFile的docId作为来源id
                ruDoc.setDocOriginId(documentFile.getDocId());
                createData.getAddDocAnnexId().add(documentFile.getDocId());
                createData.getAddDocAnnex2RuDocMap().put(documentFile.getDocId(),ruDoc.getId());

                //创建文件操作记录数据
                SignRuDocOperate ruDocOperate = new SignRuDocOperate();
                ruDocOperate.setId(UUIDGenerator.generate());
                ruDocOperate.setDocId(ruDoc.getId());
                ruDocOperate.setIsCurrent(SignCurrentEnum.IS_CURRENT.getCode());
                ruDocOperate.setSignRuId(signRu.getId());
                ruDocOperate.setAnnexId(documentFile.getDocId());
                ruDocOperate.setDeleteFlag(false);

                //pdf文件数据
                byte[] fileServiceByteById = signFileService.getByteById(documentFile.getDocId());
                dataDoc.setFileByte(fileServiceByteById);
                Integer docPage = annexImageService.countByAnnexId(documentFile.getDocId());
                if(docPage == null || docPage == 0){
                    docPage = pdfboxService.getPdfPage(fileServiceByteById);
                }
                ruDoc.setDocPage(docPage);
                dataDoc.setDocPage(docPage);
                dataDoc.setRuDoc(ruDoc);
                dataDoc.setRuDocOperate(ruDocOperate);
                dataDoc.setDocType(SignFileTypeEnum.UPLOAD.getCode());
                dataDoc.setOriginType(SignOriginTypeEnum.UPLOAD.getCode());

                createData.getDocList().add(dataDoc);
            }
        }

        List<ContractAnnexFile> annexList = request.getAnnexList();
        if(annexList != null && annexList.size() > 0){
            for(ContractAnnexFile annexFile : annexList){
                createData.getOtherFileList().add(annexFile.getFileId());
            }
        }
    }







    /**
     * @Description #校验到期时间
     * @Param [request, contractVo]
     * @return void
     **/
    public void checkAndBuildExpireDate(ContractDraftRequest request,RuCreateData createData ){

        if(request.getExpireDate() != null && request.getExpireDate().length() > 0){
            Date expireDate = null ;
            SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat second = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String todayFormat = day.format(new Date()) + " 00:00:00";
            Date today = null ;
            try {
                expireDate = day.parse(request.getExpireDate());
                today = second.parse(todayFormat);
            }catch (Exception e){
                throw new RequestParamsException(ApiCode.PARAM_SIZE_INVALID,"expireDate参数格式不正确，时间格式不正确");
            }
            if(expireDate == null){
                throw new RequestParamsException(ApiCode.PARAM_SIZE_INVALID,"expireDate参数格式不正确，时间格式不正确");
            }
            if(expireDate.before(today)){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签署截止日期:" + request.getExpireDate() + "不能小于当前日期");
            }
            createData.setExpireDate(expireDate);
        }
    }



    public void checkAndBuildTemplate(ContractDraftRequest request, RuCreateData createData){
        //如果不存在模版
        if(createData.getTemplateIdList().size() == 0){
            return;
        }
        //如果不存在填写控件
        List<SignTemplateControl> templateControlList = templateControlService.getList(createData.getTemplateIdList());
        if(templateControlList == null || templateControlList.size() == 0){
            return;
        }
        //判断是否存在必填项
        boolean isRequiredFlag = false ;
        for(SignTemplateControl templateControl : templateControlList){
            if(templateControl.getIsRequired() != null && templateControl.getIsRequired() == ControlIsRequiredEnum.IS.getCode()){
                isRequiredFlag = true ;
            }

        }
        List<ContractTemplate> signTemplateParamList = request.getSignTemplateParamList();
        if(signTemplateParamList == null || signTemplateParamList.size() == 0){
            //如果存在必填项，但是没有相关的模板参数
            if(isRequiredFlag){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,业务线模板必填参数缺失");
            }
            return;
        }
        //获取业务线配置的填写控件关联人
        List<SignReDocParam> paramList = reDocParamService.listByReId(createData.getReId());

        //创建填写控件数据
        for(SignTemplateControl templateControl : templateControlList){
            RuDataControl ruDataControl = new RuDataControl();

            SignRuDocControl ruDocControl = new SignRuDocControl();
            BeanUtils.copyProperties(templateControl,ruDocControl);
            ruDocControl.setSignRuId(createData.getRuId());
            //封装控件其他参数
            ruDocControl.setControlType(templateControl.getType());
            ruDocControl.setOriginType(ControlOriginTypeEnum.RE.getCode());
            ruDocControl.setInterfaceParamName(templateControl.getInterfaceParamName());
            ruDocControl.setDeleteFlag(false);
            ruDocControl.setId(UUIDGenerator.generate());
            //归属人
            String reSignerId = null ;
            if(paramList != null && paramList.size() > 0){
                for(SignReDocParam signReDocParam : paramList){
                    if(signReDocParam.getInterfaceParamName().equals(ruDocControl.getInterfaceParamName())){
                        reSignerId = signReDocParam.getSignerId();
                    }
                }
            }
            String ruSignerId = null ;
            if(reSignerId != null){
                ruSignerId = createData.getReSigner2RuSingerMap().get(reSignerId);
            }
            if(ruSignerId == null){
                //查出发起人
                for(RuDataSigner signer : createData.getSignerList()){
                    if(signer.getSignerType() == SignerTypeEnum.SENDER.getCode()){
                        ruSignerId = signer.getRuSigner().getId();
                    }
                }
            }
            ruDocControl.setSignerId(ruSignerId);
            //填写值
            for(ContractTemplate template : signTemplateParamList){
                List<ContractTemplateParam> templateParamList = template.getTemplateParamList();
                if(templateParamList != null && templateParamList.size() > 0){
                    for(ContractTemplateParam param : templateParamList){
                        if(param.getParamKey().equals(ruDocControl.getInterfaceParamName())){
                            if(ruDocControl.getControlType() != null & ruDocControl.getControlType().equals(ControlTypeEnum.IMAGE.getName())){
                                byte[] decode = Base64.getDecoder().decode(param.getParamValue());
                                String annexId = signFileService.saveAnnexStorage(decode, SignFileEnum.SIGN_FILE_IMAGE, null);
                                ruDocControl.setValue(annexId);
                            }else {
                                ruDocControl.setValue(param.getParamValue());
                            }
                        }
                    }
                }
            }
            //归属文档
            String ruDocId = createData.getTemplateId2RuDocMap().get(templateControl.getTemplateId());
            ruDocControl.setSignRuDocId(ruDocId);

            ruDataControl.setRuDocControl(ruDocControl);
            createData.getWriteControlList().add(ruDataControl);
        }

    }

    public void checkAndBuildSignerList(ContractDraftRequest request,RuCreateData createData){
        SignRe re = reService.getById(request.getSignReId());

        if(re != null && re.getSignerType() == SignReSignerTypeEnum.RULE.getCode()){
            //校验签署人列表
            List<SignReSigner> reSignerList = reSignerService.listByReId(request.getSignReId());
            if(reSignerList != null && reSignerList.size() > 0){
                //如果业务线设置了签署人，则必须按照已设置的签署人进行处理
                existReSignerList(request,createData,reSignerList);
            }else {
                //业务线未设置签署人，则全部新增签署人
                notExistReSignerList(request,createData);
            }

        }else {
            //业务线未设置签署人，则全部新增签署人
            notExistReSignerList(request,createData);
        }

    }


    /**
     * @Description #校验业务线签署人列表数据
     * @Param [request, contractVo]
     * @return void
     **/
    public void existReSignerList(ContractDraftRequest request,RuCreateData createData,List<SignReSigner> reSignerList){
        //发起人
        SignReSigner reSignerSender = null ;
        String reSignerSenderId = null ;
        //个人接收人列表
        List<SignReSigner> reSingerReceiverPersonalList = new ArrayList<>();
        //企业接收人列表
        List<SignReSigner> reSingerReceiverEntList = new ArrayList<>();
        //将各类签署人分类出来
        for(SignReSigner signer : reSignerList){
            if(signer.getSignerType() == SignerTypeEnum.SENDER.getCode()){
                reSignerSender = signer ;
                reSignerSenderId = signer.getId();
            }else if(signer.getSignerType() == SignerTypeEnum.RECEIVER_PERSONAL.getCode()){
                reSingerReceiverPersonalList.add(signer);
            }else if(signer.getSignerType() == SignerTypeEnum.RECEIVER_ENT.getCode()){
                reSingerReceiverEntList.add(signer);
            }
        }
//        if(reSignerSender == null || reSignerSenderId == null || reSignerSenderId.length() == 0){
//            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,业务线发起人不存在");
//        }


        List<ContractSigner> contractSignerList = request.getSignerList();
        Map<String, ContractSigner> contractOrderMap = null;
        Integer reReceiverCount = 0 ;
        if(reSingerReceiverPersonalList.size() > 0 || reSingerReceiverEntList.size() > 0){
            if(contractSignerList == null || contractSignerList.size() == 0){
                throw new RequestParamsException(ApiCode.PARAM_MISSING,"签署方信息缺失");
            }
            reReceiverCount = reSingerReceiverPersonalList.size() + reSingerReceiverEntList.size() ;
            if(reReceiverCount != contractSignerList.size()){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签署方与业务线配置签署方数据不一致，无法发起");
            }
            contractOrderMap = contractSignerList.stream().collect(Collectors.toMap(ContractSigner::getSignerOrder, c -> c,(k1, k2) -> k1));
        }
        createData.setContractOrderMap(contractOrderMap);
        if(reSignerSender != null && reSignerSenderId != null){
            //整合发起方数据
            saveSender(reSignerSender,createData);
        }

        //业务线存在其他接收方
        if(reReceiverCount > 0){
            if(contractOrderMap == null || contractOrderMap.size() == 0){
                throw new RequestParamsException(ApiCode.PARAM_MISSING,"签署方信息缺失");
            }
            //个人接收方
            if(reSingerReceiverPersonalList.size() > 0 ){
                //校验个人接收方
                checkPersonal(reSingerReceiverPersonalList,createData);
                //整合个人接收方数据
                savePersonalList(reSingerReceiverPersonalList,createData);
            }
            //企业接收方
            if(reSingerReceiverEntList.size() > 0){
                //校验企业接收方
                checkEnt(reSingerReceiverEntList,createData);
                //整合企业接收方数据
                saveEntList(reSingerReceiverEntList,createData);
            }
        }

    }

    public void notExistReSignerList(ContractDraftRequest request,RuCreateData createData){
        List<ContractSigner> signerList = request.getSignerList();

        for(ContractSigner signer : signerList) {
            //校验签署人数据
            checkContractSigner(signer);

            if(signer.getSignerType().equals("SENDER")){

                if(request.getAutoFinish() !=null && request.getAutoFinish() == SignFinishTypeEnum.MANUAL_FINISH.getCode() && CollUtil.isEmpty(signer.getInternalNodeList())){

                }else{
                    //校验
                    checkSender(signer);
                }

                //整合数据
                saveSender(signer,createData);
            }else if(signer.getSignerType().equals("RECEIVER_ENT")){
                //校验
                checkEnt(signer);
                //整合数据
                saveEnt(signer,createData);
            }else if(signer.getSignerType().equals("RECEIVER_PERSONAL")){
                //校验
                checkPersonal(signer);
                //整合数据
                savePersonalList(signer,createData);
            }
        }
    }

    public void addRuSignerList(ContractSignNodeRequest request, RuCreateData createData){
        List<ContractSigner> signerList = request.getSignerList();

        for(ContractSigner signer : signerList) {
            //校验签署人数据
            checkContractSigner(signer);

            if(signer.getSignerType().equals("SENDER")){
                //校验
                checkSender(signer);
                //整合数据
                saveSender(signer,createData);
            }else if(signer.getSignerType().equals("RECEIVER_ENT")){
                //校验
                checkEnt(signer);
                //整合数据
                saveEnt(signer,createData);
            }else if(signer.getSignerType().equals("RECEIVER_PERSONAL")){
                //校验
                checkPersonal(signer);
                //整合数据
                savePersonalList(signer,createData);
            }
        }
    }


    public void checkSender(ContractSigner contractUserSender){
        checkContractSigner(contractUserSender,"SENDER");
        List<ContractInternalNode> internalNodeList = contractUserSender.getInternalNodeList();

        if(internalNodeList == null || internalNodeList.size() == 0){
            throw new RequestParamsException(ApiCode.PARAM_MISSING,"内部签署节点参数缺失");
        }
        for(ContractInternalNode node : internalNodeList){

            checkInternalNode(node);

        }
    }

    public void checkPersonal(List<SignReSigner> reSingerReceiverPersonalList,RuCreateData createData){
        for(SignReSigner personalReSigner : reSingerReceiverPersonalList) {
            ContractSigner contractSigner = createData.getContractOrderMap().get(personalReSigner.getSignerOrder() + "");
            checkPersonal(contractSigner);
        }
    }

    public void checkPersonal(ContractSigner contractSigner){
        if(contractSigner == null){
            throw new RequestParamsException(ApiCode.PARAM_MISSING,"个人签署方信息缺失");
        }
        if(contractSigner.getSignerType() == null){
            throw new RequestParamsException(ApiCode.PARAM_MISSING,"个人签署方类型参数缺失");
        }
        if(!contractSigner.getSignerType().equals("RECEIVER_PERSONAL")){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,个人签署方类型错误");
        }
        ContractUser receiver = contractSigner.getReceiver();
        checkContractUser(receiver,"个人接收方");
    }

    public void checkEnt(List<SignReSigner> reSingerReceiverEntList,RuCreateData createData){
        for(SignReSigner entReSigner : reSingerReceiverEntList){
            ContractSigner contractSigner = createData.getContractOrderMap().get(entReSigner.getSignerOrder() + "");
            //校验签署人主数据
            checkContractSigner(contractSigner,"RECEIVER_ENT");
            //内部节点
            List<ContractInternalNode> internalNodeList = contractSigner.getInternalNodeList();
            checkInternalNodeList(internalNodeList,entReSigner.getId());
        }

    }

    public void checkEnt(ContractSigner contractEnt){
        //校验签署人主数据
        checkContractSigner(contractEnt,"RECEIVER_ENT");

        List<ContractInternalNode> internalNodeList = contractEnt.getInternalNodeList();
        if(internalNodeList == null || internalNodeList.size() == 0){
            throw new RequestParamsException(ApiCode.PARAM_MISSING,"内部签署节点参数缺失");
        }
        //校验签署人内部节点
        for(ContractInternalNode node : internalNodeList){
            checkInternalNode(node);
        }
    }


    public void checkContractSigner(ContractSigner contractSigner,String signerType){
        if(contractSigner == null){
            throw new RequestParamsException(ApiCode.PARAM_MISSING,"签署方信息缺失");
        }
        if(contractSigner.getSignerType() == null){
            throw new RequestParamsException(ApiCode.PARAM_MISSING,"签署方类型参数缺失");
        }
        if(!contractSigner.getSignerType().equals(signerType)){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签署方类型错误");
        }
    }

    public void checkContractSigner(ContractSigner contractSigner){
        if(contractSigner == null){
            throw new RequestParamsException(ApiCode.PARAM_MISSING,"签署方信息缺失");
        }
        if(contractSigner.getSignerType() == null){
            throw new RequestParamsException(ApiCode.PARAM_MISSING,"签署方类型参数缺失");
        }

    }

    public void checkInternalNodeList(List<ContractInternalNode> internalNodeList ,String signerId){
        if(internalNodeList == null || internalNodeList.size() == 0){
            throw new RequestParamsException(ApiCode.PARAM_MISSING,"内部签署节点参数缺失");
        }
        Map<String, ContractInternalNode> internalNodeSignOrderMap = internalNodeList.stream().collect(Collectors.toMap(ContractInternalNode::getSignerOrder, inter -> inter, (k1, k2) -> k1));
        if(internalNodeSignOrderMap == null || internalNodeSignOrderMap.size() == 0){
            throw new RequestParamsException(ApiCode.PARAM_MISSING,"内部签署节点参数缺失");
        }
        List<SignReSender> reSenderList = reSenderService.listBySignerId(signerId);
        if(reSenderList == null || reSenderList.size() == 0){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,业务线企业签署方未配置内部签署节点");
        }
        //校验签署人内部节点
        for(SignReSender reSender : reSenderList){

            ContractInternalNode node = internalNodeSignOrderMap.get(reSender.getSenderOrder() + "");

            checkInternalNode(node);
        }
    }

    public void checkInternalNode(ContractInternalNode node){
        if(node == null){
            throw new RequestParamsException(ApiCode.PARAM_MISSING,"内部签署节点参数缺失");
        }
        if(node.getNodeType() == null || node.getNodeType().length() == 0){
            throw new RequestParamsException(ApiCode.PARAM_MISSING,"内部签署节点-节点类型-参数缺失");
        }
        if(node.getSignerOrder() == null || node.getSignerOrder().length() == 0){
            throw new RequestParamsException(ApiCode.PARAM_MISSING,"内部签署节点-节点签署顺序-参数缺失");
        }

        ContractUser signer = node.getSigner();
        //判断是否未自动签署
        Boolean autoSignFlag = false ;
        if(node.getNodeType().equals("ENTERPRISE_SEAL")){
            if(node.getAutoSign() != null && node.getAutoSign().equals("AUTO_SIGN")){
                autoSignFlag = true ;
            }
        }
        if(!autoSignFlag){
            //非自动签署，则进行校验
            checkContractUser(signer,"内部签署节点");
        }

    }



    public void saveSender(SignReSigner reSignerSender,RuCreateData createData){


        //业务线实例发起人
        RuDataSigner dataSigner = new RuDataSigner();

        SignRuSigner ruSigner = new SignRuSigner();
        ruSigner.setId(UUIDGenerator.generate());
        ruSigner.setSignRuId(createData.getRuId());
        ruSigner.setSignerType(SignerTypeEnum.SENDER.getCode());
        ruSigner.setSignerOrder(reSignerSender.getSignerOrder());
        //发起人名称使用企业租户名称
        ruSigner.setSignerName(createData.getTenantInfo().getTenantName());
        ruSigner.setDeleteFlag(false);
        ruSigner.setSignerUserId(createData.getTenantUser().getId());
        //签署人id关联关系
        createData.getReSigner2RuSingerMap().put(reSignerSender.getId(),ruSigner.getId());
        dataSigner.setRuSigner(ruSigner);
        dataSigner.setReSignerId(reSignerSender.getId());
        dataSigner.setSignerType(reSignerSender.getSignerType());
        List<SignReSender> reSenderList = reSenderService.listBySignerId(reSignerSender.getId());
        if(reSenderList != null && reSenderList.size() > 0){
            Map<String, ContractInternalNode> nodeMap = null ;
            Map<String, ContractSigner> contractOrderMap = createData.getContractOrderMap();
            if(contractOrderMap != null){
                ContractSigner contractSigner = contractOrderMap.get((reSignerSender.getSignerOrder() + ""));
                if(contractSigner != null){
                    List<ContractInternalNode> internalNodeList = contractSigner.getInternalNodeList();
                    if(internalNodeList != null && internalNodeList.size() > 0){
                        nodeMap = internalNodeList.stream().collect(Collectors.toMap(ContractInternalNode::getSignerOrder, n -> n, (k1, k2) -> k1));
                    }
                }
            }

            for(SignReSender reSender : reSenderList){

                RuDataSender dataSender = new RuDataSender();

                SignRuSender ruSender = new SignRuSender();
                ruSender.setId(UUIDGenerator.generate());

                ruSender.setSignerId(ruSigner.getId());
                ruSender.setSenderName(reSender.getSenderName());
                ruSender.setSenderOrder(reSender.getSenderOrder());
                ruSender.setSenderType(reSender.getSenderType());
                ruSender.setSenderSignType(reSender.getSenderSignType());
                if(reSender.getSenderType().equals(SenderTypeEnum.OPERATOR.getCode())){
                    ruSender.setSenderUserId(createData.getTenantUser().getId());
                }else if(reSender.getSenderType().equals(SenderTypeEnum.ENTERPRISE.getCode())){
                    ruSender.setSenderSealId(reSender.getSenderSealId());
                    ruSender.setSenderUserId(reSender.getSenderUserId());
                }else {
                    ruSender.setSenderUserId(reSender.getSenderUserId());
                }
                //签署人id关联关系
                createData.getReSigner2RuSingerMap().put(reSender.getId(),ruSender.getId());
                ruSender.setDeleteFlag(false);
                if(nodeMap != null){
                    ContractInternalNode node = nodeMap.get(reSender.getSenderOrder() + "");
                    if(node != null){
                        //人脸,兼容V2版本接口服务
                        dataSender.setAgreeSkipWillingness(node.getAgreeSkipWillingness());

                        if(node.getNodeType() != null && !node.getNodeType().equals(SenderTypeEnum.ENTERPRISE.getApiName())){
                            dataSender.setPersonalSignAuth(node.getPersonalSignAuth());
                            if(MyStringUtils.isNotBlank(node.getSealType())){
                                dataSender.setSealType(node.getSealType());
                            }
                        }

                        String signConfirm = node.getSignConfirm();
                        if(signConfirm != null && signConfirm.equals("FACE")){
                            dataSender.setVerifyType("FACE");
                        }else {
                            dataSender.setVerifyType(node.getVerifyType());
                        }
                        if(node.getPositionParamList() != null && node.getPositionParamList().size() > 0){
                            for(ContractPositionParam positionParam : node.getPositionParamList()){
                                if (createData.getDocList() != null && createData.getDocList().size() == 1){
                                    if (positionParam.getRelationDocList() == null || positionParam.getRelationDocList().size() == 0){
                                        List<ContractRelationDoc> relationDocList = getContractRelationDocs(createData);
                                        positionParam.setRelationDocList(relationDocList);
                                    }
                                }else if (createData.getDocList() != null && createData.getDocList().size() > 1){
                                    if (positionParam.getRelationDocList() == null || positionParam.getRelationDocList().size() == 0){
                                        List<ContractRelationDoc> relationDocList = getContractRelationDocs(createData);
                                        positionParam.setRelationDocList(relationDocList);
                                    }
                                }
                                checkAndBuildSignControlList(positionParam,createData,ruSender.getId(),node.getNodeType());
                            }
                        }
                    }
                }
                //查询是否存在业务线设置的签署控件
                buildReSignControlList(createData, reSender.getId(),ruSender.getId());



                dataSender.setRuSender(ruSender);
                dataSender.setReSenderId(reSender.getId());
                dataSigner.getAddSenderList().add(dataSender);
            }
        }
        createData.getSignerList().add(dataSigner);
    }

    public void saveSender(ContractSigner sender,RuCreateData createData){
        //业务线实例发起人
        RuDataSigner dataSigner = new RuDataSigner();

        SignRuSigner ruSigner = new SignRuSigner();

        ruSigner.setId(UUIDGenerator.generate());

        ruSigner.setSignRuId(createData.getRuId());
        ruSigner.setSignerType(SignerTypeEnum.SENDER.getCode());
        int signerOrder = Integer.parseInt(sender.getSignerOrder());

        ruSigner.setSignerOrder(signerOrder);
        //发起人名称使用企业租户名称
        if (createData.getTenantInfo() != null){
            ruSigner.setSignerName(createData.getTenantInfo().getTenantName());
        }else{
            ruSigner.setSignerName(sender.getSignerName());
        }

        ruSigner.setDeleteFlag(false);

        if(createData.getTenantUser() != null){
            ruSigner.setSignerUserId(createData.getTenantUser().getId());
        }


        dataSigner.setSignerType(SignerTypeEnum.SENDER.getCode());

        List<ContractInternalNode> internalNodeList = sender.getInternalNodeList();

        if(CollUtil.isNotEmpty(internalNodeList)){
            ruSigner.setSignFlag(true);
            for(ContractInternalNode node : internalNodeList){

                int signOrder = Integer.parseInt(node.getSignerOrder());

                Boolean autoSignFlag = false ;

                RuDataSender dataSender = new RuDataSender();

                SignRuSender ruSender = new SignRuSender();

                ruSender.setId(UUIDGenerator.generate());
                ruSender.setSignerId(ruSigner.getId());
                ruSender.setSenderOrder(signOrder);

                if(node.getNodeType().equals("AGENT_SIGN")){
                    ruSender.setSenderType(SenderTypeEnum.OPERATOR.getCode());
                    ruSender.setSenderName(SenderTypeEnum.OPERATOR.getName());
                    ruSender.setSenderSignType(SenderSignTypeEnum.APPOINT.getCode());
                }else if(node.getNodeType().equals("ENTERPRISE_SEAL")){
                    ruSender.setSenderType(SenderTypeEnum.ENTERPRISE.getCode());
                    ruSender.setSenderName(SenderTypeEnum.ENTERPRISE.getName());
                    if(node.getAutoSign() != null && node.getAutoSign().equals("AUTO_SIGN")){
                        ruSender.setSenderSignType(SenderSignTypeEnum.AUTO.getCode());
                        autoSignFlag = true ;
                    }else {
                        ruSender.setSenderSignType(SenderSignTypeEnum.APPOINT.getCode());
                    }
                    ruSender.setSenderSealId(node.getSealId());
                }else if(node.getNodeType().equals("LEGAL_PERSON_SIGN")){
                    ruSender.setSenderType(SenderTypeEnum.LEGAL_PERSON.getCode());
                    ruSender.setSenderName(SenderTypeEnum.LEGAL_PERSON.getName());
                    ruSender.setSenderSignType(SenderSignTypeEnum.APPOINT.getCode());
                }else if(node.getNodeType().equals("PERSONAL_SIGN")){
                    ruSender.setSenderType(SenderTypeEnum.PERSONAL.getCode());
                    ruSender.setSenderName(SenderTypeEnum.PERSONAL.getName());
                    ruSender.setSenderSignType(SenderSignTypeEnum.APPOINT.getCode());
                }else if(node.getNodeType().equals("APPROVER_CHECK")){
                    ruSender.setSenderType(SenderTypeEnum.APPROVER.getCode());
                    ruSender.setSenderName(SenderTypeEnum.APPROVER.getName());
                    ruSender.setSenderSignType(SenderSignTypeEnum.APPOINT.getCode());
                }

                //非自动签署需要指定签署人
                if(!autoSignFlag){
                    ContractUser signer = node.getSigner();
                    SysUser sysUser = sysUserService.getUserByName(signer.getContact());
                    if(sysUser == null){
                        throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,账号不存在");
                    }
                    //传递保存租户数据
                    if(createData.getTenantInfo() != null){
                        SysTenantUser tenantUser = tenantUserService.getTenantUser(createData.getTenantInfo().getId(), sysUser.getId());
                        if(tenantUser == null){
                            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,经办人不存在");
                        }
                        ruSender.setSenderUserId(tenantUser.getId());
                    }

                }
                ruSender.setDeleteFlag(false);
                dataSender.setAgreeSkipWillingness(node.getAgreeSkipWillingness());
                if(MyStringUtils.isNotBlank(node.getNodeType()) && !node.getNodeType().equals(SenderTypeEnum.ENTERPRISE.getApiName())){
                    String personalSignAuthPlatform = ruBusinessService.getSystemPersonalSignAuthType();
                    String personalSignAuth = setSignNodeConfig(node.getPersonalSignAuth(), personalSignAuthPlatform);
                    dataSender.setPersonalSignAuth(personalSignAuth);
                    dataSender.setSealType(node.getSealType());
                }
                //人脸
                String signConfirm = node.getSignConfirm();
                if(signConfirm != null && signConfirm.equals("FACE")){
                    dataSender.setVerifyType("FACE");
                }else {
                    dataSender.setVerifyType(node.getVerifyType());
                }
                //控件数据
                if(node.getPositionParamList() != null && node.getPositionParamList().size() > 0){
                    for(ContractPositionParam positionParam : node.getPositionParamList()){
                        if (createData.getDocList() != null && createData.getDocList().size() == 1){
                            if (positionParam.getRelationDocList() == null || positionParam.getRelationDocList().size() == 0){
                                List<ContractRelationDoc> relationDocList = getContractRelationDocs(createData);
                                positionParam.setRelationDocList(relationDocList);
                            }
                        }else if (createData.getDocList() != null && createData.getDocList().size() > 1){
                            if (positionParam.getRelationDocList() == null || positionParam.getRelationDocList().size() == 0){
                                List<ContractRelationDoc> relationDocList = getContractRelationDocs(createData);
                                positionParam.setRelationDocList(relationDocList);
                            }
                        }
                        checkAndBuildSignControlList(positionParam,createData,ruSender.getId(),node.getNodeType());
                    }
                }
                dataSender.setRuSender(ruSender);
                dataSigner.getAddSenderList().add(dataSender);
            }
        }else{
            ruSigner.setSignFlag(false);
        }
        dataSigner.setRuSigner(ruSigner);

        createData.getSignerList().add(dataSigner);
    }

    public void savePersonalList(ContractSigner personalSinger,RuCreateData createData){

        RuDataSigner dataSigner = new RuDataSigner();

        int signerOrder =0;
        if(MyStringUtils.isNotBlank(personalSinger.getSignerOrder())){
            signerOrder = Integer.parseInt(personalSinger.getSignerOrder());
        }

        SignRuSigner ruSigner = new SignRuSigner();

        ruSigner.setId(UUIDGenerator.generate());

        ruSigner.setSignRuId(createData.getRuId());
        ruSigner.setSignerType(SignerTypeEnum.RECEIVER_PERSONAL.getCode());
        ruSigner.setSignerOrder(signerOrder);
        ContractUser receiver = personalSinger.getReceiver();
        if(receiver != null){
            ruSigner.setSignerName(receiver.getName());
            if(receiver.getContactType().equals("MOBILE")){
                ruSigner.setSignerExternalType(SignRuSignerExternalTypeEnum.PHONE.getCode());
            }else if(receiver.getContactType().equals("EMAIL")){
                ruSigner.setSignerExternalType(SignRuSignerExternalTypeEnum.EMAIL.getCode());
            }else {
                ruSigner.setSignerExternalType(SignRuSignerExternalTypeEnum.PHONE.getCode());
            }
            ruSigner.setSignerExternalValue(receiver.getContact());
        }
        ruSigner.setDeleteFlag(false);
        dataSigner.setAgreeSkipWillingness(personalSinger.getAgreeSkipWillingness());

        String personalSignAuthPlatform = ruBusinessService.getSystemPersonalSignAuthType();
        String personalSignAuth = setSignNodeConfig(personalSinger.getPersonalSignAuth(), personalSignAuthPlatform);
        dataSigner.setPersonalSignAuth(personalSignAuth);
        dataSigner.setSealType(personalSinger.getSealType());
        //人脸
        String signConfirm = personalSinger.getSignConfirm();
        if(signConfirm != null && signConfirm.equals("FACE")){
            dataSigner.setVerifyType("FACE");
        }else {
            dataSigner.setVerifyType(personalSinger.getVerifyType());
        }
        //控件数据
        if(personalSinger.getPositionParamList() != null && personalSinger.getPositionParamList().size() > 0){
            for(ContractPositionParam contractPositionParam : personalSinger.getPositionParamList()){
                if (createData.getDocList() != null && createData.getDocList().size() == 1){
                    if (contractPositionParam.getRelationDocList() == null || contractPositionParam.getRelationDocList().size() == 0){
                        List<ContractRelationDoc> relationDocList = getContractRelationDocs(createData);
                        contractPositionParam.setRelationDocList(relationDocList);
                    }
                }else if (createData.getDocList() != null && createData.getDocList().size() > 1){
                    if (contractPositionParam.getRelationDocList() == null || contractPositionParam.getRelationDocList().size() == 0){
                        List<ContractRelationDoc> relationDocList = getContractRelationDocs(createData);
                        contractPositionParam.setRelationDocList(relationDocList);
                    }
                }

                checkAndBuildSignControlList(contractPositionParam,createData,ruSigner.getId(),personalSinger.getSignerType());
            }
        }

        dataSigner.setRuSigner(ruSigner);
        dataSigner.setSignerType(SignerTypeEnum.RECEIVER_PERSONAL.getCode());
        createData.getSignerList().add(dataSigner);
    }


    public void savePersonalList(List<SignReSigner> reSingerReceiverPersonalList,RuCreateData createData){
        Map<String, ContractSigner> contractOrderMap = createData.getContractOrderMap();
        if(contractOrderMap == null){
            return;
        }
        for(SignReSigner reSigner : reSingerReceiverPersonalList){

            ContractSigner contractSigner = contractOrderMap.get((reSigner.getSignerOrder() + ""));
            if(contractSigner != null){

                RuDataSigner dataSigner = new RuDataSigner();

                SignRuSigner ruSigner = new SignRuSigner();
                ruSigner.setId(UUIDGenerator.generate());
                ruSigner.setSignRuId(createData.getRuId());
                ruSigner.setSignerType(SignerTypeEnum.RECEIVER_PERSONAL.getCode());
                ruSigner.setSignerOrder(reSigner.getSignerOrder());
                ContractUser receiver = contractSigner.getReceiver();
                if(receiver != null){
                    ruSigner.setSignerName(receiver.getName());
                    if(receiver.getContactType().equals("MOBILE")){
                        ruSigner.setSignerExternalType(SignRuSignerExternalTypeEnum.PHONE.getCode());
                    }else if(receiver.getContactType().equals("EMAIL")){
                        ruSigner.setSignerExternalType(SignRuSignerExternalTypeEnum.EMAIL.getCode());
                    }else {
                        ruSigner.setSignerExternalType(SignRuSignerExternalTypeEnum.PHONE.getCode());
                    }
                    ruSigner.setSignerExternalValue(receiver.getContact());
                }
                ruSigner.setDeleteFlag(false);
                //签署人id关联关系
                createData.getReSigner2RuSingerMap().put(reSigner.getId(),ruSigner.getId());
                //控件数据
                if(contractSigner.getPositionParamList() != null && contractSigner.getPositionParamList().size() > 0){
                    for(ContractPositionParam positionParam : contractSigner.getPositionParamList()){
                        if (createData.getDocList() != null && createData.getDocList().size() == 1){
                            if (positionParam.getRelationDocList() == null || positionParam.getRelationDocList().size() == 0){
                                List<ContractRelationDoc> relationDocList = getContractRelationDocs(createData);
                                positionParam.setRelationDocList(relationDocList);
                            }
                        }else if (createData.getDocList() != null && createData.getDocList().size() > 1){
                            if (positionParam.getRelationDocList() == null || positionParam.getRelationDocList().size() == 0){
                                List<ContractRelationDoc> relationDocList = getContractRelationDocs(createData);
                                positionParam.setRelationDocList(relationDocList);
                            }
                        }
                        checkAndBuildSignControlList(positionParam,createData,ruSigner.getId(),contractSigner.getSignerType());
                    }

                }
                dataSigner.setAgreeSkipWillingness(contractSigner.getAgreeSkipWillingness());
                dataSigner.setPersonalSignAuth(contractSigner.getPersonalSignAuth());

                if(MyStringUtils.isNotBlank(contractSigner.getSealType())){
                    dataSigner.setSealType(contractSigner.getSealType());
                }

                //人脸
                String signConfirm = contractSigner.getSignConfirm();
                if(signConfirm != null && signConfirm.equals("FACE")){
                    dataSigner.setVerifyType("FACE");
                }else {
                    dataSigner.setVerifyType(contractSigner.getVerifyType());
                }
                //查询是否存在业务线设置的签署控件
                buildReSignControlList(createData, reSigner.getId(),ruSigner.getId());

                dataSigner.setRuSigner(ruSigner);
                dataSigner.setReSignerId(reSigner.getId());
                dataSigner.setSignerType(reSigner.getSignerType());
                createData.getSignerList().add(dataSigner);
            }
        }
    }

    public void saveEnt(ContractSigner entSigner,RuCreateData createData){
        RuDataSigner dataSigner = new RuDataSigner();

        int signerOrder =0;
        if(MyStringUtils.isNotBlank(entSigner.getSignerOrder())){
            signerOrder = Integer.parseInt(entSigner.getSignerOrder());
        }

        SignRuSigner ruSigner = new SignRuSigner();
        ruSigner.setId(UUIDGenerator.generate());
        ruSigner.setSignRuId(createData.getRuId());
        ruSigner.setSignerType(SignerTypeEnum.RECEIVER_ENT.getCode());
        ruSigner.setSignerOrder(signerOrder);
        ruSigner.setSignerName(entSigner.getSignerName());

        ruSigner.setDeleteFlag(false);

        List<ContractInternalNode> internalNodeList = entSigner.getInternalNodeList();
        if(internalNodeList != null && internalNodeList.size() > 0){
            ruSigner.setSignFlag(true);
            for(ContractInternalNode node : internalNodeList){

                RuDataSender dataSender = new RuDataSender();

                int signOrder = Integer.parseInt(node.getSignerOrder());

                SignRuSender ruSender = new SignRuSender();

                ruSender.setId(UUIDGenerator.generate());

                ruSender.setSignerId(ruSigner.getId());
                ruSender.setSenderOrder(signOrder);
                ruSender.setSenderSignType(SenderSignTypeEnum.APPOINT.getCode());

                if(node.getNodeType().equals("AGENT_SIGN")){
                    ruSender.setSenderType(SenderTypeEnum.OPERATOR.getCode());
                    ruSender.setSenderName(SenderTypeEnum.OPERATOR.getName());
                    ruSender.setSenderSignType(SenderSignTypeEnum.APPOINT.getCode());
                }else if(node.getNodeType().equals("ENTERPRISE_SEAL")){
                    ruSender.setSenderType(SenderTypeEnum.ENTERPRISE.getCode());
                    ruSender.setSenderName(SenderTypeEnum.ENTERPRISE.getName());
                    ruSender.setSenderSignType(SenderSignTypeEnum.APPOINT.getCode());
                }else if(node.getNodeType().equals("LEGAL_PERSON_SIGN")){
                    ruSender.setSenderType(SenderTypeEnum.LEGAL_PERSON.getCode());
                    ruSender.setSenderName(SenderTypeEnum.LEGAL_PERSON.getName());
                    ruSender.setSenderSignType(SenderSignTypeEnum.APPOINT.getCode());
                }else if(node.getNodeType().equals("PERSONAL_SIGN")){
                    ruSender.setSenderType(SenderTypeEnum.PERSONAL.getCode());
                    ruSender.setSenderName(SenderTypeEnum.PERSONAL.getName());
                    ruSender.setSenderSignType(SenderSignTypeEnum.APPOINT.getCode());
                }
                ContractUser contractUser = node.getSigner();
                if(contractUser != null){
                    ruSender.setSenderName(contractUser.getName());
                    if(contractUser.getContactType().equals("MOBILE")){
                        //内部节点
                        ruSender.setSenderExternalType(SignRuSignerExternalTypeEnum.PHONE.getCode());
                        //外部签署人
                        ruSigner.setSignerExternalType(SignRuSignerExternalTypeEnum.PHONE.getCode());
                    }else if(contractUser.getContactType().equals("EMAIL")){
                        //内部节点
                        ruSender.setSenderExternalType(SignRuSignerExternalTypeEnum.EMAIL.getCode());
                        //外部签署人
                        ruSigner.setSignerExternalType(SignRuSignerExternalTypeEnum.EMAIL.getCode());
                    }else {
                        //内部节点
                        ruSender.setSenderExternalType(SignRuSignerExternalTypeEnum.PHONE.getCode());
                        //外部签署人
                        ruSigner.setSignerExternalType(SignRuSignerExternalTypeEnum.PHONE.getCode());
                    }
                    //内部节点
                    ruSender.setSenderExternalValue(contractUser.getContact());
                    //外部签署人
                    ruSigner.setSignerExternalValue(contractUser.getContact());
                }
                ruSender.setDeleteFlag(false);

                dataSender.setAgreeSkipWillingness(node.getAgreeSkipWillingness());
                //计算签署人的实名认证类型
                if(MyStringUtils.isNotBlank(node.getNodeType()) && !node.getNodeType().equals(SenderTypeEnum.ENTERPRISE.getApiName())){
                    String personalSignAuthPlatform = ruBusinessService.getSystemPersonalSignAuthType();
                    String personalSignAuth = setSignNodeConfig(node.getPersonalSignAuth(), personalSignAuthPlatform);
                    dataSender.setPersonalSignAuth(personalSignAuth);
                    dataSender.setSealType(node.getSealType());
                }
                //人脸
                String signConfirm = node.getSignConfirm();
                if(signConfirm != null && signConfirm.equals("FACE")){
                    dataSender.setVerifyType("FACE");
                }else{
                    dataSender.setVerifyType(node.getVerifyType());
                }
                //控件数据
                if(node.getPositionParamList() != null && node.getPositionParamList().size() > 0){
                    for(ContractPositionParam positionParam : node.getPositionParamList()){

                        if (createData.getDocList() != null && createData.getDocList().size() == 1){
                            if (positionParam.getRelationDocList() == null || positionParam.getRelationDocList().size() == 0){
                                List<ContractRelationDoc> relationDocList = getContractRelationDocs(createData);
                                positionParam.setRelationDocList(relationDocList);
                            }
                        }else if (createData.getDocList() != null && createData.getDocList().size() > 1){
                            if (positionParam.getRelationDocList() == null || positionParam.getRelationDocList().size() == 0){
                                List<ContractRelationDoc> relationDocList = getContractRelationDocs(createData);
                                positionParam.setRelationDocList(relationDocList);
                            }
                        }

                        checkAndBuildSignControlList(positionParam,createData,ruSender.getId(),node.getNodeType());
                    }
                }

                dataSender.setRuSender(ruSender);
//                dataSender.setReSenderId(reSender.getId());
                dataSigner.getAddSenderList().add(dataSender);
            }
        }else{
            ruSigner.setSignFlag(false);
        }

        dataSigner.setRuSigner(ruSigner);
//        dataSigner.setReSignerId(reSingerReceiverEnt.getId());
        dataSigner.setSignerType(SignerTypeEnum.RECEIVER_ENT.getCode());
        createData.getSignerList().add(dataSigner);
    }

    public void saveEntList(List<SignReSigner> reSingerReceiverEntList,RuCreateData createData){
        Map<String, ContractSigner> contractOrderMap = createData.getContractOrderMap();
        if(contractOrderMap == null){
            return;
        }
        for(SignReSigner reSingerReceiverEnt : reSingerReceiverEntList){
            ContractSigner contractSigner = contractOrderMap.get((reSingerReceiverEnt.getSignerOrder() + ""));
            if(contractSigner != null){

                RuDataSigner dataSigner = new RuDataSigner();

                SignRuSigner ruSigner = new SignRuSigner();
                ruSigner.setId(UUIDGenerator.generate());
                ruSigner.setSignRuId(createData.getRuId());
                ruSigner.setSignerType(SignerTypeEnum.RECEIVER_ENT.getCode());
                ruSigner.setSignerOrder(reSingerReceiverEnt.getSignerOrder());
                ruSigner.setSignerName(contractSigner.getSignerName());
                ruSigner.setDeleteFlag(false);

                //签署人id关联关系
                createData.getReSigner2RuSingerMap().put(reSingerReceiverEnt.getId(),ruSigner.getId());

                List<SignReSender> reSenderList = reSenderService.listBySignerId(reSingerReceiverEnt.getId());
                if(reSenderList != null && reSenderList.size() > 0){
                    List<ContractInternalNode> internalNodeList = contractSigner.getInternalNodeList();
                    if(internalNodeList != null && internalNodeList.size() > 0){
                        Map<String, ContractInternalNode> nodeMap = internalNodeList.stream().collect(Collectors.toMap(ContractInternalNode::getSignerOrder, n -> n, (k1, k2) -> k1));
                        for(SignReSender reSender : reSenderList){

                            RuDataSender dataSender = new RuDataSender();

                            SignRuSender ruSender = new SignRuSender();
                            ruSender.setId(UUIDGenerator.generate());

                            ruSender.setSignerId(ruSigner.getId());
                            ruSender.setSenderOrder(reSender.getSenderOrder());
                            ruSender.setSenderType(reSender.getSenderType());

                            ruSender.setSenderSignType(SenderSignTypeEnum.APPOINT.getCode());

                            ruSender.setSenderSealId(reSender.getSenderSealId());
                            ruSender.setSenderUserId(reSender.getSenderUserId());
                            ContractInternalNode contractInternalNode = nodeMap.get((reSender.getSenderOrder() + ""));
                            if(contractInternalNode != null){
                                ContractUser contractUser = contractInternalNode.getSigner();
                                if(contractUser != null){
                                    ruSender.setSenderName(contractUser.getName());
                                    if(contractUser.getContactType().equals("MOBILE")){
                                        //内部节点
                                        ruSender.setSenderExternalType(SignRuSignerExternalTypeEnum.PHONE.getCode());
                                        //外部签署人
                                        ruSigner.setSignerExternalType(SignRuSignerExternalTypeEnum.PHONE.getCode());
                                    }else if(contractUser.getContactType().equals("EMAIL")){
                                        //内部节点
                                        ruSender.setSenderExternalType(SignRuSignerExternalTypeEnum.EMAIL.getCode());
                                        //外部签署人
                                        ruSigner.setSignerExternalType(SignRuSignerExternalTypeEnum.EMAIL.getCode());
                                    }else {
                                        //内部节点
                                        ruSender.setSenderExternalType(SignRuSignerExternalTypeEnum.PHONE.getCode());
                                        //外部签署人
                                        ruSigner.setSignerExternalType(SignRuSignerExternalTypeEnum.PHONE.getCode());
                                    }
                                    //内部节点
                                    ruSender.setSenderExternalValue(contractUser.getContact());
                                    //外部签署人
                                    ruSigner.setSignerExternalValue(contractUser.getContact());

                                }

                                dataSigner.setAgreeSkipWillingness(contractInternalNode.getAgreeSkipWillingness());
                                if(contractInternalNode.getNodeType() != null && !contractInternalNode.getNodeType().equals(SenderTypeEnum.ENTERPRISE.getName())){
                                    dataSigner.setPersonalSignAuth(contractInternalNode.getPersonalSignAuth());
                                    if(MyStringUtils.isNotBlank(contractInternalNode.getSealType())){
                                        dataSigner.setSealType(contractInternalNode.getSealType());
                                    }
                                }

                                //人脸
                                String signConfirm = contractInternalNode.getSignConfirm();
                                if(signConfirm != null && signConfirm.equals("FACE")){
                                    dataSender.setVerifyType("FACE");
                                }else{
                                    dataSender.setVerifyType(contractInternalNode.getVerifyType());
                                }
                                if(contractInternalNode.getPositionParamList() != null && contractInternalNode.getPositionParamList().size() > 0){
                                    for(ContractPositionParam positionParam : contractInternalNode.getPositionParamList()){
                                        if (createData.getDocList() != null && createData.getDocList().size() == 1){
                                            if (positionParam.getRelationDocList() == null || positionParam.getRelationDocList().size() == 0){
                                                List<ContractRelationDoc> relationDocList = getContractRelationDocs(createData);
                                                positionParam.setRelationDocList(relationDocList);
                                            }
                                        }else if (createData.getDocList() != null && createData.getDocList().size() > 1){
                                            if (positionParam.getRelationDocList() == null || positionParam.getRelationDocList().size() == 0){
                                                List<ContractRelationDoc> relationDocList = getContractRelationDocs(createData);
                                                positionParam.setRelationDocList(relationDocList);
                                            }
                                        }
                                        checkAndBuildSignControlList(positionParam,createData,ruSender.getId(),contractInternalNode.getNodeType());
                                    }
                                }
                            }
                            ruSender.setDeleteFlag(false);
                            //签署人id关联关系
                            createData.getReSigner2RuSingerMap().put(reSender.getId(),ruSender.getId());

                            //查询是否存在业务线设置的签署控件
                            buildReSignControlList(createData, reSender.getId(),ruSender.getId());

                            dataSender.setRuSender(ruSender);
                            dataSender.setReSenderId(reSender.getId());
                            dataSigner.getAddSenderList().add(dataSender);
                        }
                    }
                }

                dataSigner.setRuSigner(ruSigner);
                dataSigner.setReSignerId(reSingerReceiverEnt.getId());
                dataSigner.setSignerType(reSingerReceiverEnt.getSignerType());
                createData.getSignerList().add(dataSigner);
            }
        }
    }

    private static @NotNull List<ContractRelationDoc> getContractRelationDocs(RuCreateData createData) {
        List<ContractRelationDoc> relationDocList = new ArrayList<>();
        ContractRelationDoc relationDoc =new ContractRelationDoc();
        relationDoc.setDocId(createData.getDocList().get(0).getRuDoc().getId());
        relationDoc.setDocType(String.valueOf(createData.getDocList().get(0).getRuDoc().getDocType()));
        relationDocList.add(relationDoc);
        return relationDocList;
    }


    /**
     * @Description #校验业务线主数据
     * @Param [request, contractVo]
     * @return void
     **/
    public void checkAndBuildBase(ContractDraftRequest request,RuCreateData createData){
        //校验业务线
        String signReId = request.getSignReId();
        if(signReId == null || signReId.length() == 0){
            throw new RequestParamsException(ApiCode.PARAM_MISSING,"signReId参数缺失");
        }
        if(signReId.length() > 64){
            throw new RequestParamsException(ApiCode.PARAM_SIZE_INVALID,"signReId参数格式不正确，长度不合法");
        }
        if(signReId.contains(" ")){
            throw new RequestParamsException(ApiCode.PARAM_SIZE_INVALID,"signReId参数格式不正确，不能包含空格");
        }
        SignRe signRe = reService.getById(signReId);
        if(signRe == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,业务线id：" + request.getSignReId() + "数据不存在");
        }
        if(signRe.getSysTenantId() == null || !signRe.getSysTenantId().equals(createData.getTenantInfo().getId())){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,发起方企业无权使用该业务线发起签署");
        }
        //权限数据
        List<SignReAuth> auths = new ArrayList<>();
        //未指定使用者列表
        QueryWrapper<SignReAuth> tenantQueryWrapper = new QueryWrapper<>();
        tenantQueryWrapper.lambda().isNull(SignReAuth::getUserId);
        tenantQueryWrapper.lambda().eq(SignReAuth::getTenantId,createData.getTenantInfo().getId());
        tenantQueryWrapper.lambda().eq(SignReAuth::getAuthType,SignReAuthTypeEnum.USER.getCode());
        tenantQueryWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        List<SignReAuth> tenantList = reAuthService.list(tenantQueryWrapper);
        if(tenantList != null && tenantList.size() > 0){
            auths.addAll(tenantList);
        }
        //指定使用者列表
        QueryWrapper<SignReAuth> userTenantQueryWrapper = new QueryWrapper<>();
        userTenantQueryWrapper.lambda().eq(SignReAuth::getUserId,createData.getTenantUser().getId());
        userTenantQueryWrapper.lambda().eq(SignReAuth::getAuthType,SignReAuthTypeEnum.USER.getCode());
        userTenantQueryWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        List<SignReAuth> userTenantList = reAuthService.list(userTenantQueryWrapper);
        if(userTenantList != null && userTenantList.size() > 0){
            auths.addAll(userTenantList);
        }
        if(auths.size() == 0){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,经办人无权使用该业务线发起签署");
        }
        List<String> reIdList = auths.stream().map(SignReAuth::getSignReId).collect(Collectors.toList());
        if(reIdList == null || reIdList.size() == 0){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,经办人无权使用该业务线发起签署");
        }
        if(!reIdList.contains(signRe.getId())){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,经办人无权使用该业务线发起签署");
        }
        if(signRe.getStatus() != SignReStatusEnum.ENABLE.getCode()){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务线状态为停用或异常，无法使用");
        }
        Integer errorStatus = signRe.getErrorStatus();
        if(errorStatus == null || errorStatus != SignReErrorStatusEnum.NO.getCode()){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务线状态为停用或异常，无法使用");
        }
        //创建业务线实例数据
        SignRu signRu = new SignRu();
        //构造id
        String signRuId = UUIDGenerator.generate();
        signRu.setId(signRuId);
        signRu.setSubject(request.getSubject());
        signRu.setCode(request.getSn());
        //主数据
        signRu.setSignerType(signRe.getSignerType());

        signRu.setCcedType(signRe.getCcedType());
        signRu.setCcedOpportunityType(signRe.getCcedOpportunityType());
        signRu.setInternalCcerType(signRe.getInternalCcerType());
        signRu.setExternalCcerType(signRe.getExternalCcerType());
        signRu.setAddCcerType(signRe.getAddCcerType());
        signRu.setAddFileType(signRe.getAddFileType());
        signRu.setDeleteFileType(signRe.getDeleteFileType());
        signRu.setAddAnnexType(signRe.getAddAnnexType());
        signRu.setCaSignType(signRe.getCaSignType());
        signRu.setBeforeStartApproveId(signRe.getBeforeStartApproveId());
        signRu.setBeforeStartApproveType(signRe.getBeforeStartApproveType());
        signRu.setBeforeSignApproveId(signRe.getBeforeSignApproveId());
        signRu.setBeforeSignApproveType(signRe.getBeforeSignApproveType());
        //截止时间
        signRu.setExpireDate(createData.getExpireDate());
        //业务线配置id
        signRu.setSignReId(signRe.getId());
        signRu.setDeleteFlag(false);
        //当前操作人数据
        signRu.setSysUserId(createData.getTenantUser().getId());
        signRu.setSysTenantId(createData.getTenantUser().getTenantId());
        signRu.setSysAccountId(createData.getTenantUser().getUserId());

        //自动完成类型
        if (request.getAutoFinish() != null){
            if (request.getAutoFinish().equals(SignFinishTypeEnum.AUTO_FINISH.getCode())){
                signRu.setAutoFinish(SignFinishTypeEnum.AUTO_FINISH.getCode());
            }else if (request.getAutoFinish().equals(SignFinishTypeEnum.MANUAL_FINISH.getCode())){
                signRu.setAutoFinish(SignFinishTypeEnum.MANUAL_FINISH.getCode());
            }else {
                signRu.setAutoFinish(SignFinishTypeEnum.AUTO_FINISH.getCode());
            }
        }else {
            signRu.setAutoFinish(SignFinishTypeEnum.AUTO_FINISH.getCode());
        }
        //签署顺序
        if(request.getSignOrderType() != null){
            if(request.getSignOrderType().equals("ORDER")){
                signRu.setSignOrderType(SignOrderTypeEnum.ORDER.getCode());
            }else if(request.getSignOrderType().equals("NO_ORDER")){
                signRu.setSignOrderType(SignOrderTypeEnum.NO_ORDER.getCode());
            }
        }
        if(signRu.getSignOrderType() == null){
            if(signRe.getSignOrderType() != null) {
                signRu.setSignOrderType(signRe.getSignOrderType());
            }else {
                signRu.setSignOrderType(SignOrderTypeEnum.ORDER.getCode());
            }
        }
        //控件变更状态
        if(request.getControlChangeFlag() != null){
            if(request.getControlChangeFlag().equals("NECESSARY_NO_ADD")){
                signRu.setControlChangeFlag(ControlChangeFlagEnum.NECESSARY_NO_ADD.getType());
            }else if(request.getControlChangeFlag().equals("NECESSARY_AND_ADD")){
                signRu.setControlChangeFlag(ControlChangeFlagEnum.NECESSARY_AND_ADD.getType());
            }else if(request.getControlChangeFlag().equals("NOT_NECESSARY")){
                signRu.setControlChangeFlag(ControlChangeFlagEnum.NOT_NECESSARY.getType());
            }
        }
        if(signRu.getControlChangeFlag() == null){
            if(signRe.getControlChangeFlag() != null){
                signRu.setControlChangeFlag(signRe.getControlChangeFlag());
            }else {
                signRu.setControlChangeFlag(ControlChangeFlagEnum.NECESSARY_AND_ADD.getType());
            }
        }

        signRu.setSendType(request.getSendType());

        signRu.setStatus(SignRuStatusEnum.DRAFT.getCode());

        createData.setSignRu(signRu);
        createData.setRuId(signRuId);
        createData.setReId(signRe.getId());
    }



    /**
     * @Description #校验操作人
     * @Param [request, contractVo]
     * @return void
     **/
    public void checkAndBuildOperator(ContractDraftRequest request,RuCreateData createData){
        ContractUser operator = request.getOperator();
        if(operator == null || operator.getName() == null || operator.getContact() == null || operator.getContactType() == null){
            throw new RequestParamsException(ApiCode.PARAM_MISSING,"发起人参数缺失");
        }
        ApiDeveloperManage apiDeveloperManage = apiDeveloperManageService.getByToken(request.getAppAuthToken());
        if(apiDeveloperManage == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,token权限不存在");
        }
        String contact = operator.getContact();
        SysUser sysUser = sysUserService.getUserByName(contact);
        if(sysUser == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,账号不存在");
        }
        //传递保存账号数据
        createData.setSysUser(sysUser);
        SysTenantInfo tenantInfo = tenantInfoService.getById(apiDeveloperManage.getTenantId());
        if(tenantInfo == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,账号不存在");
        }
        //传递保存租户数据
        createData.setTenantInfo(tenantInfo);
        SysTenantUser tenantUser = tenantUserService.getTenantUser(apiDeveloperManage.getTenantId(), sysUser.getId());
        if(tenantUser == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,经办人不存在");
        }
        //传递保存租户下用户数据
        createData.setTenantUser(tenantUser);
        //存储本地线程变量
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(sysUser, loginUser);
        loginUser.setTenantId(tenantUser.getTenantId());
        loginUser.setTenantUserId(tenantUser.getId());
        MySecurityUtils.THREAD_LOCAL.set(loginUser);

    }

    /**
     * 根据系统配置、签署实例、签署节点校验并设置签署节点配置
     * @param personalSignAuthNode
     * @param personalSignAuthPlatform
     */
    private String setSignNodeConfig(String personalSignAuthNode, String personalSignAuthPlatform){

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
                if (MyStringUtils.isNotBlank(personalSignAuthNode)) {
                    // 如果签署实例为无须实名或需实名，则直接签署实例的配置
                    if (personalSignAuthNode.equals(PersonalSignAuthTypeEnum.NOT_REQUIRED.getType()) ||
                            personalSignAuthNode.equals(PersonalSignAuthTypeEnum.REQUIRED.getType())) {
                        finalPersonalSignAuth = personalSignAuthNode;
                    }
                }
            }
        }
        return finalPersonalSignAuth;
    }

    private void buildDocRelationInfo(List<SignRuDoc> docList,List<SignRuDocOperate> ruDocOperateList,List<ContractPositionParam> positionParamList, RuCreateData createData){
        if (positionParamList != null && positionParamList.size() > 0){
            for (ContractPositionParam positionParam : positionParamList) {
                if (docList.size() == 1){
                    if (positionParam.getRelationDocList() == null || positionParam.getRelationDocList().size() == 0){
                        List<ContractRelationDoc> relationDocList = new ArrayList<>();
                        ContractRelationDoc relationDoc =new ContractRelationDoc();
                        relationDoc.setDocId(docList.get(0).getDocOriginId());
                        relationDoc.setDocType(String.valueOf(docList.get(0).getDocType()));
                        relationDocList.add(relationDoc);
                        positionParam.setRelationDocList(relationDocList);
                    }
                }else if (docList.size() > 1){
                    if (positionParam.getRelationDocList() == null || positionParam.getRelationDocList().size() == 0){
                        List<ContractRelationDoc> relationDocList = new ArrayList<>();
                        ContractRelationDoc relationDoc =new ContractRelationDoc();
                        relationDoc.setDocId(docList.get(0).getDocOriginId());
                        relationDoc.setDocType(String.valueOf(docList.get(0).getDocType()));
                        relationDocList.add(relationDoc);
                        positionParam.setRelationDocList(relationDocList);
                    }
                }

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
        }
    }


}