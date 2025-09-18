/**
 * @description 业务线数据、权限、分类服务
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
package com.kaifangqian.modules.opensign.service.business;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.pdfbox.PdfboxService;
import com.kaifangqian.modules.opensign.service.business.vo.ReSaveData;
import com.kaifangqian.modules.opensign.service.business.vo.ReSaveDataSender;
import com.kaifangqian.modules.opensign.service.business.vo.ReSaveDataSigner;
import com.kaifangqian.modules.opensign.service.re.*;
import com.kaifangqian.modules.opensign.service.template.SignTemplateControlService;
import com.kaifangqian.modules.opensign.service.template.SignTemplateRecordService;
import com.kaifangqian.modules.opensign.service.template.SignTemplateService;
import com.kaifangqian.entity.GlobalIdConfig;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.enums.SignReSignerTypeEnum;
import com.kaifangqian.modules.opensign.enums.SignerTypeEnum;
import com.kaifangqian.modules.opensign.service.annex.AnnexImageService;
import com.kaifangqian.modules.opensign.service.re.*;
import com.kaifangqian.modules.opensign.service.seal.SignEntSealService;
import com.kaifangqian.modules.opensign.service.tool.SignFileService;
import com.kaifangqian.modules.system.service.ISysTenantInfoService;
import com.kaifangqian.modules.system.service.ISysTenantUserService;
import com.kaifangqian.service.IGlobalIdConfigInfoService;
import com.kaifangqian.service.IGlobalIdConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: ReDataService
 * @Package: com.kaifangqian.modules.opensign.service.business
 * @ClassName: ReDataService
 * @author: FengLai_Gong
 */
@Service
public class ReDataService {

    @Autowired
    private SignReService reService ;
    @Autowired
    private SignReDocService reDocService ;
    @Autowired
    private SignReSignerService signerService ;
    @Autowired
    private SignReSenderService senderService ;
    @Autowired
    private SignReCcerService ccerService ;
    @Autowired
    private SignReRuleService reRuleService ;
    @Autowired
    private SignReRuleDetailService reRuleDetailService ;

    @Autowired
    private IGlobalIdConfigService globalIdConfigService ;
    @Autowired
    private IGlobalIdConfigInfoService globalIdConfigInfoService ;

    @Autowired
    private SignReAuthService reAuthService ;


    @Autowired
    private SignReDocControlService reDocControlService ;

    @Autowired
    private SignReDocParamService reDocParamService ;

    @Autowired
    private SignTemplateService templateService ;
    @Autowired
    private SignTemplateRecordService templateRecordService;
    @Autowired
    private SignTemplateControlService templateControlService ;

    @Autowired
    private SignReAuthService authService ;

    @Autowired
    private ISysTenantUserService tenantUserService ;
    @Autowired
    private ISysTenantInfoService tenantInfoService ;
    @Autowired
    private SignEntSealService entSealService ;

    @Autowired
    private SignFileService signFileService ;
    @Autowired
    private AnnexImageService annexImageService ;

    @Autowired
    private SignReFolderService reFolderService ;
    @Autowired
    private SignReFolderRelationService reFolderRelationService ;

    @Autowired
    private AuthBusinessService authBusinessService ;

    @Autowired
    private SignReDocControlPropertyService reDocControlPropertyService;

    @Autowired
    private SignReSignConfirmService reSignConfirmService ;


    @Autowired
    private PdfboxService pdfboxService ;

    /**
     * @Description #创建re整体数据、权限、文件夹
     * @Param [data]
     * @return void
     **/
    @Transactional(rollbackFor = Exception.class)
    public void createData(ReSaveData data){

        SignRe re = data.getRe();
        boolean save = reService.save(re);
        if(!save){
            throw new PaasException("创建业务线失败");
        }
        data.setReId(re.getId());

        List<SignReAuth> addAuthList = data.getAddAuthList();
        if(addAuthList.size() > 0){
            for(SignReAuth reAuth : addAuthList){
                if(reAuth.getSignReId() == null){
                    reAuth.setSignReId(re.getId());
                }
            }
            boolean saveAuth = authService.saveBatch(addAuthList);
            if(!saveAuth){
                throw new PaasException("创建业务线-设置默认权限失败");
            }
        }

        SignReFolder reFolder = data.getReFolder();
        if(reFolder != null){
            boolean saveFolder = reFolderService.save(reFolder);
            if(!saveFolder){
                throw new PaasException("创建文件夹失败");
            }
        }
        SignReFolderRelation reFolderRelation = data.getReFolderRelation();
        if(reFolderRelation != null){
            if(reFolderRelation.getSignReId() == null){
                reFolderRelation.setSignReId(re.getId());
            }
            if(reFolderRelation.getSignReFolderId() == null){
                reFolderRelation.setSignReFolderId(reFolder.getId());
            }
            boolean saveFolderRelation = reFolderRelationService.save(reFolderRelation);
            if(!saveFolderRelation){
                throw new PaasException("创建文件夹关联关系失败");
            }

        }

    }

    /**
     * @Description #保存re整体数据
     * @Param [data]
     * @return void
     **/
    @Transactional(rollbackFor = Exception.class)
    public void saveData(ReSaveData data){

        //更新re主数据
        SignRe re = data.getRe();
        if(re != null){
            boolean updateRe = reService.updateById(re);
            if(!updateRe){
                throw new PaasException("操作失败");
            }
            data.setReId(re.getId());
        }
        //新增文档
        if(data.getAddReDocList().size() > 0){
            for(SignReDoc reDoc : data.getAddReDocList()){
                boolean saveRuDoc = reDocService.save(reDoc);
                if(!saveRuDoc){
                    //异常处理
                    throw new PaasException("保存签约文件失败");
                }
            }
        }
        //更新模板页码
        if(data.getUpdateTemplateRecordList().size() > 0){
            List<SignTemplateRecord> updateTemplateRecordList = data.getUpdateTemplateRecordList();
            for(SignTemplateRecord r : updateTemplateRecordList){
                templateRecordService.updateById(r);
            }
        }
        //删除文档
        if(data.getDeleteDocIdList().size() > 0){
            //签约文件
            reDocService.deleteByParam(data.getDeleteDocIdList(),data.getReId());
        }
        //更新文档顺序
        if(data.getUpdateDocOrderList().size() > 0){
            for(SignReDoc reDoc : data.getUpdateDocOrderList()){
                reDocService.updateById(reDoc);
            }
        }
        //添加签署人
        if(data.getAddSignerList().size() > 0){
            for(ReSaveDataSigner signer : data.getAddSignerList()){
                addSigner(signer,data.getReId());
            }
        }
        //更新签署人
        if(data.getUpdateSignerList().size() > 0){
            for(ReSaveDataSigner signer : data.getUpdateSignerList()){
                updateSigner(signer,data.getReId());
            }
        }
        //删除签署人
        if(data.getDeleteSignerList().size() > 0){
            for(ReSaveDataSigner dataSigner : data.getDeleteSignerList()){
                deleteSigner(dataSigner,data.getReId());
            }
        }
        //控件相关
        if(data.getSignerType() != null && data.getSignerType().equals(SignReSignerTypeEnum.CUSTOM.getCode())){
            //如果是发起时自定义签署人，则删除所有相关控件
            reDocParamService.deleteByRe(re.getId());
            reDocControlService.deleteByReId(re.getId());
            reDocControlPropertyService.deleteByReId(re.getId());

        }else {
            //删除文档相关的控件数据
            if(data.getDeleteDocIdList().size() > 0){
                List<String> collect = data.getDeleteDocIdList().stream().distinct().collect(Collectors.toList());
                //填写控件关联关系
                reDocParamService.deleteByParam(collect,data.getReId());
            }
            if(data.getDeleteReDocParamIdList().size() > 0){
                List<String> collect = data.getDeleteReDocParamIdList().stream().distinct().collect(Collectors.toList());
                for(String deleteDocParamId : collect){
                    reDocParamService.removeById(deleteDocParamId);
                }
            }
            if(data.getDeleteControlIdList().size() > 0){
                List<String> collect = data.getDeleteControlIdList().stream().distinct().collect(Collectors.toList());
                for(String deleteControlId : collect){
                    reDocControlService.deleteById(deleteControlId);
                    reDocControlPropertyService.deleteByControlId(deleteControlId);
                }
            }
            //如果是预设流程，则删除计算后的控件
            if(data.getDeleteControlPropertyIdList().size() > 0){
                List<String> collect = data.getDeleteControlPropertyIdList().stream().distinct().collect(Collectors.toList());
                for(String deleteControlPropertyId : collect){
                    reDocControlPropertyService.deleteById(deleteControlPropertyId);
                }
            }

            if(data.getDeletePropertyByControlIdList().size() > 0){
                List<String> collect = data.getDeletePropertyByControlIdList().stream().distinct().collect(Collectors.toList());
                for(String deleteByControlId : collect){
                    reDocControlPropertyService.deleteByControlId(deleteByControlId);
                }
            }
        }


        //更新抄送人
        if(data.getCcerList().size() > 0){
            updateCcerList(data.getCcerList(),data.getReId());
        }

        //编号单号规则
        if(data.getCodeReRule() != null && data.getCodeDetails() != null && data.getCodeModule() != null){

            String globalIdConfigId = null ;
            if(data.getCodeModule().getId() != null){
                globalIdConfigService.updateExt(data.getCodeModule());
                globalIdConfigId = data.getCodeModule().getId();
            }else {
                globalIdConfigService.saveExt(data.getCodeModule());
                GlobalIdConfig globalIdConfig = getByIdKey(data.getCodeModule().getIdKey());
                if(globalIdConfig == null){
                    throw new PaasException("单号规则保存失败");
                }
                globalIdConfigId = globalIdConfig.getId();


            }
            data.getCodeReRule().setRuleGenerateId(globalIdConfigId);
            reRuleService.saveOrUpdate(data.getCodeReRule());

            //删除原有数据
            reRuleDetailService.deleteByRuleId(data.getCodeReRule().getId());
            for(SignReRuleDetail detail : data.getCodeDetails()){
                detail.setRuleId(data.getCodeReRule().getId());
            }
            reRuleDetailService.saveBatch(data.getCodeDetails());
        }
        //主题单号规则
        if(data.getSubjectReRule() != null && data.getSubjectDetails() != null && data.getSubjectModule() != null){
            String globalIdConfigId = null ;
            if(data.getSubjectModule().getId() != null){
                globalIdConfigService.updateExt(data.getSubjectModule());
                globalIdConfigId = data.getSubjectModule().getId();
            }else {
                globalIdConfigService.saveExt(data.getSubjectModule());
                GlobalIdConfig globalIdConfig = getByIdKey(data.getSubjectModule().getIdKey());
                if(globalIdConfig == null){
                    throw new PaasException("单号规则保存失败");
                }
                globalIdConfigId = globalIdConfig.getId();
            }
            data.getSubjectReRule().setRuleGenerateId(globalIdConfigId);
            reRuleService.saveOrUpdate(data.getSubjectReRule());
            //删除原有数据
            reRuleDetailService.deleteByRuleId(data.getSubjectReRule().getId());
            for(SignReRuleDetail detail : data.getSubjectDetails()){
                detail.setRuleId(data.getSubjectReRule().getId());
            }
            reRuleDetailService.saveBatch(data.getSubjectDetails());
        }
    }


    public String addSigner(ReSaveDataSigner signer,String reId){
        SignReSigner reSigner = signer.getReSigner();
        if(reSigner == null){
            return null ;
        }
        boolean saveSigner = signerService.saveOrUpdate(reSigner);
        if(!saveSigner){
            //异常处理
            throw new PaasException("保存签署人" + reSigner.getSignerName() + "失败");
        }
        //个人
        if(SignerTypeEnum.RECEIVER_PERSONAL.getCode().equals(signer.getSignerType())){
//            if(signer.getConfirmType() == 1){
//                reSignConfirmService.save(reSigner.getId(),reId,SignerTypeEnum.RECEIVER_PERSONAL.getCode());
//            }
            reSignConfirmService.save(reSigner.getId(),reId,SignerTypeEnum.RECEIVER_PERSONAL.getCode(),signer.getAgreeSkipWillingness(),signer.getVerifyType());
        }else if(SignerTypeEnum.SENDER.getCode().equals(signer.getSignerType())){
            reSignConfirmService.save(reSigner.getId(),reId,SignerTypeEnum.SENDER.getCode(),signer.getAgreeSkipWillingness(),signer.getVerifyType());
        }else if(SignerTypeEnum.RECEIVER_ENT.getCode().equals(signer.getSignerType())){
            reSignConfirmService.save(reSigner.getId(),reId,SignerTypeEnum.RECEIVER_ENT.getCode(),signer.getAgreeSkipWillingness(),signer.getVerifyType());
        }

        //内部签署人
        if(signer.getAddSenderList().size() > 0){
            addSenderList(signer.getAddSenderList(),reSigner.getId(),reId);
        }
        return reSigner.getId();

    }

    public void updateSigner(ReSaveDataSigner signer,String reId){
        SignReSigner reSigner = signer.getReSigner();
        if(reSigner == null){
            return ;
        }
        boolean updateSigner = signerService.updateById(reSigner);
        if(!updateSigner){
            throw new PaasException("保存签署人" + reSigner.getSignerName() + "失败");
        }

        if(SignerTypeEnum.RECEIVER_PERSONAL.getCode().equals(signer.getSignerType())){
            //个人
            updateConfirm(reSigner.getId(),reId,SignerTypeEnum.RECEIVER_PERSONAL.getCode(),signer.getConfirmType(),signer.getAgreeSkipWillingness(),signer.getVerifyType());
        }else if(SignerTypeEnum.SENDER.getCode().equals(signer.getSignerType())){
            updateConfirm(reSigner.getId(),reId,SignerTypeEnum.SENDER.getCode(),signer.getConfirmType(),signer.getAgreeSkipWillingness(),signer.getVerifyType());
        }else if(SignerTypeEnum.RECEIVER_ENT.getCode().equals(signer.getSignerType())){
            updateConfirm(reSigner.getId(),reId,SignerTypeEnum.RECEIVER_ENT.getCode(),signer.getConfirmType(),signer.getAgreeSkipWillingness(),signer.getVerifyType());
        }
        //新增内部节点
        addSenderList(signer.getAddSenderList(),reSigner.getId(),reId);
        //更新内部节点
        updateSenderList(signer.getUpdateSenderList(),reSigner.getId(),reId);
        //删除内部节点
        deleteSenderList(signer.getDeleteSenderList(),reId);
    }

    public void deleteSigner(ReSaveDataSigner dataSigner,String reId){
        SignReSigner signer = dataSigner.getReSigner();
        if(signer == null){
            return;
        }
        signer.setDeleteFlag(true);
        //删除签署人
        boolean b = signerService.updateById(signer);
        if(!b){
            throw new PaasException("删除签署人失败");
        }
        if(SignerTypeEnum.RECEIVER_PERSONAL.getCode().equals(signer.getSignerType())){
            //删除接收人的签署控件
            reDocControlService.deleteSignControlList(signer.getId(),reId);
            //个人
            reSignConfirmService.delete(signer.getId(),reId);
        }
        //清空接收人和签署人的填写控件关联关系
        reDocControlService.resetWriteControlList(signer.getId(),signer.getSignerType(),reId);
        //删除内部节点
        deleteSenderList(dataSigner.getDeleteSenderList(),reId);
    }


    public void addSenderList(List<ReSaveDataSender> addSenderList, String singerId, String reId){
        //发起方
        if(addSenderList != null && addSenderList.size() > 0){
            for(ReSaveDataSender sender : addSenderList){
                SignReSender reSender = sender.getSender();
                reSender.setSignerId(singerId);
                boolean saveSender = senderService.saveOrUpdate(reSender);
                if(!saveSender){
                    throw new PaasException("保存发起方" + reSender.getSenderName() + "失败");
                }
//                if(sender.getConfirmType() == 1){
//                    reSignConfirmService.save(reSender.getId(),reId, sender.getSignerType(), sender.getAgreeSkipWillingness(), sender.getVerifyType());
//                }
                reSignConfirmService.save(reSender.getId(),reId, sender.getSignerType(), sender.getAgreeSkipWillingness(), sender.getVerifyType());
            }
        }
    }

    public void updateSenderList(List<ReSaveDataSender> updateSenderList,String singerId,String reId){
        if(updateSenderList != null && updateSenderList.size() > 0) {
            for (ReSaveDataSender sender : updateSenderList) {
                SignReSender reSender = sender.getSender();
                reSender.setSignerId(singerId);
                boolean b = senderService.saveOrUpdate(reSender);
                if(!b){
                    throw new PaasException("保存发起人" + reSender.getSenderName() + "失败");
                }
                updateConfirm(reSender.getId(),reId,reSender.getSenderType(),sender.getConfirmType(),sender.getAgreeSkipWillingness(),sender.getVerifyType());
            }
        }
    }

    public void deleteSenderList(List<SignReSender> deleteSenderList,String reId){
        if(deleteSenderList != null && deleteSenderList.size() > 0){
            for(SignReSender sender : deleteSenderList){
                //删除发起人的签署控件
                reDocControlService.deleteSignControlList(sender.getId(),reId);
                //删除所有签署意愿
                reSignConfirmService.delete(sender.getId(),reId);
                //删除所有发起人
                senderService.updateById(sender);
            }

        }

    }

    public void updateConfirm(String signerId,String signReId,Integer signerType,Integer confirmType,Integer isFastSign,String verifyType){
//        if(confirmType == 1){
//            SignReSignConfirm reSignConfirm = reSignConfirmService.getByParam(signerId, signReId);
//            if(reSignConfirm == null){
//                reSignConfirmService.save(signerId,signReId,signerType);
//            }
//        }else {
//            reSignConfirmService.delete(signerId,signReId);
//        }
        SignReSignConfirm reSignConfirm = reSignConfirmService.getByParam(signerId, signReId);
        if(reSignConfirm == null){
            reSignConfirmService.save(signerId,signReId,signerType,isFastSign,verifyType);
        }else{
            reSignConfirm.setAgreeSkipWillingness(isFastSign);
            reSignConfirm.setConfirmType(verifyType);
            reSignConfirmService.updateById(reSignConfirm);
        }
    }


    public void updateCcerList(List<SignReCcer> ccerList, String reId){
        //删除
        ccerService.deleteByReId(reId);
        //批量新增
        ccerService.saveBatch(ccerList);
    }

    private GlobalIdConfig getByIdKey(String idKey) {
        LambdaQueryWrapper<GlobalIdConfig> wrapper = new LambdaQueryWrapper();
        wrapper.eq(GlobalIdConfig::getIdKey, idKey);
        List<GlobalIdConfig> configs = globalIdConfigService.list(wrapper);
        return CollUtil.isNotEmpty(configs) ? (GlobalIdConfig)configs.get(0) : null;
    }

}