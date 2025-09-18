/**
 * @description 签署保存业务逻辑
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

import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.service.business.vo.*;
import com.kaifangqian.modules.opensign.service.re.SignReService;
import com.kaifangqian.modules.opensign.service.ru.*;
import com.kaifangqian.modules.opensign.service.template.SignTemplateControlService;
import com.kaifangqian.modules.opensign.service.template.SignTemplateRecordService;
import com.kaifangqian.modules.opensign.vo.base.sign.*;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.service.annex.AnnexImageService;
import com.kaifangqian.modules.opensign.service.business.vo.*;
import com.kaifangqian.modules.opensign.service.re.*;
import com.kaifangqian.modules.opensign.service.ru.*;
import com.kaifangqian.modules.opensign.service.seal.SignEntSealService;
import com.kaifangqian.modules.opensign.service.tool.SignFileService;
import com.kaifangqian.modules.opensign.vo.base.sign.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: RuSaveService
 * @Package: com.kaifangqian.modules.opensign.service.business
 * @ClassName: RuSaveService
 * @author: FengLai_Gong
 */
@Service
public class RuSaveService {

    @Autowired
    private SignReService reService ;

    @Autowired
    private SignRuService ruService ;
    @Autowired
    private SignRuDocControlService ruDocControlService ;
    @Autowired
    private SignRuSignerService signerService ;
    @Autowired
    private SignRuSenderService senderService ;
    @Autowired
    private SignRuDocService ruDocService ;
    @Autowired
    private SignRuDocOperateService ruDocOperateService ;

    @Autowired
    private SignTemplateRecordService signTemplateRecordService ;
    @Autowired
    private SignTemplateControlService signTemplateControlService ;

    @Autowired
    private SignFileService signFileService ;

    @Autowired
    private SignEntSealService entSealService ;


    @Autowired
    private AnnexImageService annexImageService ;


    @Autowired
    private SignRuDocControlPropertyService ruDocControlPropertyService ;


    @Autowired
    private SignRuSignConfirmService ruSignConfirmService ;

    @Autowired
    private RuDataService ruDataService ;


    /**
     * @Description #保存全部数据
     * @Param [vo]
     * @return java.lang.String
     **/
    public String save(BaseVo vo){

        RuSaveData saveData = new RuSaveData();
        //保存基本数据
        saveBase(saveData, vo.getBaseVo());
        String ruId = saveData.getRuId();
        String reId = saveData.getReId();
        //保存签署人
        SignerCalculateVo signerCalculateVo = calculateSignerList(vo.getSignerList(), ruId);
        //新增签署人
        addSignerList(signerCalculateVo.getAddSignerList(),saveData);
        //更新签署人
        updateSignerList(signerCalculateVo.getUpdateSignerList(),saveData);
        //删除签署人
        deleteSignerList(signerCalculateVo.getDeleteRuSignerList(),saveData);

        //计算签约文件变更内容
        DocFileCalculateVo docFileCalculateVo = calculateDocFileList(vo.getFileList(), ruId);
        //新增签约文件
        addDocFileList(docFileCalculateVo.getAddFileVoList(),ruId,saveData);
        //删除签约文件
        deleteDocFileList(docFileCalculateVo.getDeleteRuDocList(),ruId,saveData);
        //更新签约文件顺序
        updateDocFileOrderList(vo.getFileList(),saveData);

        //抄送方,附件
        createOtherFileAndCcer(saveData,vo.getCcerList(),vo.getOtherFileList());

        ruDataService.saveRuData(saveData);

        return saveData.getRuId() ;
    }

    public void createOtherFileAndCcer(RuSaveData saveData, List<DocCcerVo> ccerList , List<DocOtherFileVo> otherFileList ){
        //抄送方
        if(ccerList != null && ccerList.size() > 0){
            for(DocCcerVo ccerVo : ccerList){
                SignRuCcer ccer = new SignRuCcer();
                BeanUtils.copyProperties(ccerVo,ccer);
                ccer.setDeleteFlag(false);
                ccer.setId(null);
                ccer.setTenantUserId(ccerVo.getInternalCcerId());
                ccer.setCcerAddType(ccerVo.getCcerAddType());
                saveData.getCcerList().add(ccer);
            }
        }
        //附件
        if(otherFileList != null && otherFileList.size() > 0){
            for(DocOtherFileVo fileVo : otherFileList){
                saveData.getOtherFileList().add(fileVo.getAnnexId());
            }
        }
    }

    public void saveBase(RuSaveData saveData, DocBaseVo baseVo){
        //保存基本数据
        SignRu ru = ruService.getById(baseVo.getSignRuId());
        if(ru == null){
            throw new PaasException("数据不存在");
        }
        String signReId = ru.getSignReId();
        SignRe signRe = reService.getById(signReId);
        if(signRe == null){
            throw new PaasException("业务线数据不存在");
        }

        BeanUtils.copyProperties(baseVo,ru);
        ru.setDeleteFlag(false);
        if(baseVo.getExpireDate() != null){
            ru.setExpireDate(baseVo.getExpireDate());
        }
        saveData.setReId(signRe.getId());
        saveData.setRu(ru);
        saveData.setRuId(ru.getId());
    }


    /**
     * @Description #计算签署人变更列表
     * @Param [signerVoList, ruId]
     * @return com.kaifangqian.modules.opensign.service.business.vo.SignerCalculateVo
     **/
    public SignerCalculateVo calculateSignerList(List<DocSignerVo> signerVoList , String ruId){


        SignerCalculateVo vo = new SignerCalculateVo();
        //查询原有的联系人
        List<SignRuSigner> signerList = signerService.listByRuId(ruId);
        //新增签署人列表
        List<DocSignerVo> addSignerList = new ArrayList<>();
        //更新签署人列表
        List<DocSignerVo> updateSignerList = new ArrayList<>();
        //删除签署人列表
        List<SignRuSigner> deleteSignerList = new ArrayList<>();
        //签署人列表不为空
        if(signerVoList != null && signerVoList.size() > 0){
            if(signerList == null || signerList.size() == 0){
                //原有签署人为空，直接全部加入
                addSignerList.addAll(signerVoList);
            }else {
                //找出新增的签署人
                for(int i = 0 ; i < signerVoList.size() ; i++){
                    DocSignerVo signerVo = signerVoList.get(i);
                    if(signerVo.getId() == null || signerVo.getId().length() == 0){
                        addSignerList.add(signerVo);
                        signerVoList.remove(signerVo);
                        i--;
                    }
                }
                //遍历剩余的签署人vo数据
                if(signerVoList.size() > 0){
                    for(SignRuSigner signer : signerList){
                        Boolean delete = true ;
                        for(DocSignerVo signerVo : signerVoList){
                            if(signerVo.getId().equals(signer.getId())){
                                //找出更新的签署人
                                updateSignerList.add(signerVo);
                                delete = false;
                            }
                        }
                        if(delete){
                            //找出删除的签署人
                            deleteSignerList.add(signer);
                        }
                    }
                }else {
                    //如果传入的数据全部为新增，那么库中已有的则全部视为删除数据
                    deleteSignerList.addAll(signerList);
                }
            }
        }
        //签署人列表为空了
        else if(signerList != null && signerList.size() > 0) {
            deleteSignerList.addAll(signerList);
        }
        vo.setAddSignerList(addSignerList);
        vo.setUpdateSignerList(updateSignerList);
        vo.setDeleteRuSignerList(deleteSignerList);
        return vo;
    }



    /**
     * @Description #新增签署人列表
     * @Param [addSignerList]
     * @return void
     **/
    public void addSignerList(List<DocSignerVo> addSignerList ,RuSaveData saveData){
        //新增签署人
        if(addSignerList != null && addSignerList.size() > 0){

            //再新插入数据
            for(DocSignerVo signerVo : addSignerList){
                SignRuSigner signer = new SignRuSigner();

                BeanUtils.copyProperties(signerVo,signer);
                signer.setDeleteFlag(false);
                signer.setId(null);
                if(SignerTypeEnum.SENDER.getCode().equals(signerVo.getSignerType()) || SignerTypeEnum.RECEIVER_ENT.getCode().equals(signerVo.getSignerType())){
                    if(signerVo.getSenderList() != null && signerVo.getSenderList().size() > 0){
                        signer.setSignFlag(true);
                    }else {
                        signer.setSignFlag(false);
                    }
                    //发起人租户下用户id
                    signer.setSignerUserId(MySecurityUtils.getCurrentUser().getTenantUserId());
                }else {
                    signer.setSignFlag(true);
                }

                signer.setSignerExternalType(signerVo.getSignerExternalType());
                signer.setSignerExternalValue(signerVo.getSignerExternalValue());

                RuSaveDataSigner dataSigner = new RuSaveDataSigner();
                dataSigner.setRuSigner(signer);
                dataSigner.setConfirmType(signerVo.getConfirmType());
                dataSigner.setSignerType(signerVo.getSignerType());
                dataSigner.setAgreeSkipWillingness(signerVo.getAgreeSkipWillingness());
                dataSigner.setVerifyType(signerVo.getVerifyType());

                if(SignerTypeEnum.SENDER.getCode().equals(signerVo.getSignerType())){
                    //发起人
                    List<DocSenderVo> senderVoList = signerVo.getSenderList();
                    //新增发起方
                    addSenderList(senderVoList,signer.getId(),dataSigner);
                }
                else if(SignerTypeEnum.RECEIVER_ENT.getCode().equals(signerVo.getSignerType())){
                    //企业接收方
                    List<DocSenderVo> senderVoList = signerVo.getSenderList();
                    //新增企业接收方
                    addReceiveEntList(senderVoList,signer.getId(),dataSigner);
                }
                saveData.getAddSignerList().add(dataSigner);
            }
        }
    }


    /**
     * @Description #更新签署人列表
     * @Param [updateSignerList]
     * @return void
     **/
    public void updateSignerList(List<DocSignerVo> updateSignerList,RuSaveData saveData){
        //更新签署人
        if(updateSignerList != null && updateSignerList.size() > 0){
            LoginUser currentUser = MySecurityUtils.getCurrentUser();

            for(DocSignerVo signerVo : updateSignerList){
                RuSaveDataSigner dataSigner = new RuSaveDataSigner();

                SignRuSigner signer = new SignRuSigner();
                BeanUtils.copyProperties(signerVo,signer);

                if(SignerTypeEnum.SENDER.getCode().equals(signer.getSignerType())){
                    signer.setSignerUserId(currentUser.getTenantUserId());
                    //计算发起人
                    SenderCalculateVo senderCalculateVo = calculateSenderList(signerVo.getSenderList(), signerVo.getId());
                    //新增发起方
                    addSenderList(senderCalculateVo.getAddSenderVoList(),signer.getId(),dataSigner);
                    //更新发起方
                    updateSenderList(senderCalculateVo.getUpdateSenderVoList(),signerVo.getId(),dataSigner);
                    //删除发起方
                    deleteSenderList(senderCalculateVo.getDeleteRuSenderList(),dataSigner,saveData);
                }else if(SignerTypeEnum.RECEIVER_ENT.getCode().equals(signer.getSignerType())){
                    //计算发起人
                    SenderCalculateVo senderCalculateVo = calculateSenderList(signerVo.getSenderList(), signerVo.getId());
                    //新增发起方
                    addReceiveEntList(senderCalculateVo.getAddSenderVoList(),signer.getId(),dataSigner);
                    //更新发起方
                    updateReceiveEntList(senderCalculateVo.getUpdateSenderVoList(),signerVo.getId(),dataSigner);
                    //删除发起方
                    deleteReceiveEntList(senderCalculateVo.getDeleteRuSenderList(),dataSigner,saveData);
                }

                signer.setSignerExternalType(signerVo.getSignerExternalType());
                signer.setSignerExternalValue(signerVo.getSignerExternalValue());

                dataSigner.setSignerType(signer.getSignerType());
                dataSigner.setConfirmType(signerVo.getConfirmType());
                dataSigner.setAgreeSkipWillingness(signerVo.getAgreeSkipWillingness());
                dataSigner.setVerifyType(signerVo.getVerifyType());
                dataSigner.setRuSigner(signer);
                saveData.getUpdateSignerList().add(dataSigner);
            }
        }
    }

    /**
     * @Description #删除签署人列表
     * @Param [deleteSignerList]
     * @return void
     **/
    public void deleteSignerList(List<SignRuSigner> deleteSignerList,RuSaveData saveData){
        //删除签署人
        if(deleteSignerList != null && deleteSignerList.size() > 0){
            for(SignRuSigner signer : deleteSignerList){
                RuSaveDataSigner dataSigner = new RuSaveDataSigner();
                signer.setDeleteFlag(true);
                //如果是发起方
                if(SignerTypeEnum.SENDER.getCode().equals(signer.getSignerType())){
                    //获取原有发前方内部设置
                    List<SignRuSender> senderList = senderService.listBySignerId(signer.getId());
                    //删除发起人内部数据
                    deleteSenderList(senderList,dataSigner,saveData);
                }else if(SignerTypeEnum.RECEIVER_ENT.getCode().equals(signer.getSignerType())){
                    //获取原有企业接收方内部设置
                    List<SignRuSender> senderList = senderService.listBySignerId(signer.getId());
                    //删除企业接收方内部数据
                    deleteReceiveEntList(senderList,dataSigner,saveData);
                }
                dataSigner.setRuSigner(signer);
                dataSigner.setSignerType(signer.getSignerType());
                saveData.getDeleteSignerList().add(dataSigner);
                //当前签署人的签署控件
                List<SignRuDocControl> signRuDocControlList = ruDocControlService.listSignControlBySignerId(signer.getId());
                if(signRuDocControlList != null && signRuDocControlList.size() > 0){
                    List<String> collect = signRuDocControlList.stream().map(SignRuDocControl::getId).collect(Collectors.toList());
                    if(collect != null && collect.size() > 0){
                        saveData.getDeleteControlIdList().addAll(collect);
                    }
                }

            }
        }
    }


    public SenderCalculateVo calculateSenderList(List<DocSenderVo> senderVoList ,String signerId){
        SenderCalculateVo vo = new SenderCalculateVo();

        //获取原有发前方内部设置
        List<SignRuSender> senderList = senderService.listBySignerId(signerId);
        //新增发起方
        List<DocSenderVo> addSenderVoList = new ArrayList<>();
        //更新发起方
        List<DocSenderVo> updateSenderVoList = new ArrayList<>();
        //删除发起方
        List<SignRuSender> deleteSenderList = new ArrayList<>();
        //计算
        if(senderVoList != null && senderVoList.size() > 0){
            if(senderList == null && senderList.size() == 0){
                //原有发起方为空，之前全部新增
                addSenderVoList.addAll(senderVoList);
            }else {
                //找出新增的发起方
                for(int i = 0 ; i < senderVoList.size() ; i++){
                    DocSenderVo senderVo = senderVoList.get(i);
                    if(senderVo.getId() == null || senderVo.getId().length() == 0){
                        addSenderVoList.add(senderVo);
                        senderVoList.remove(senderVo);
                        i--;
                    }
                }
                //找出更新和删除的发起方
                if(senderVoList.size() > 0){
                    for(SignRuSender sender : senderList){
                        Boolean delete = true ;
                        for(DocSenderVo senderVo : senderVoList){
                            if(sender.getId().equals(senderVo.getId())){
                                delete = false;
                                //找出更新的发起方
                                updateSenderVoList.add(senderVo);
                            }
                        }
                        if(delete){
                            //找出删除的发起方
                            deleteSenderList.add(sender);
                        }
                    }
                }else {
                    //如果传入的数据全部为新增，那么库中已有的则全部视为删除数据
                    deleteSenderList.addAll(senderList);
                }
            }
        }
        //发起方列表为空
        else if(senderList != null && senderList.size() > 0){
            deleteSenderList.addAll(senderList);
        }

        vo.setAddSenderVoList(addSenderVoList);
        vo.setUpdateSenderVoList(updateSenderVoList);
        vo.setDeleteRuSenderList(deleteSenderList);
        return vo ;
    }


    /**
     * @Description #新增发起方
     * @Param [addSenderVoList]
     * @return void
     **/
    public void addSenderList(List<DocSenderVo> addSenderVoList,String signerId,RuSaveDataSigner dataSigner){
        if(addSenderVoList != null && addSenderVoList.size() > 0){
            for(DocSenderVo senderVo : addSenderVoList){
                SignRuSender sender = new SignRuSender();
                BeanUtils.copyProperties(senderVo,sender);
                sender.setSignerId(signerId);
                sender.setDeleteFlag(false);
                //经办人
                if(senderVo.getSenderType() != null && senderVo.getSenderType().equals(SenderTypeEnum.OPERATOR.getCode())){
                    sender.setSenderUserId(MySecurityUtils.getCurrentUser().getTenantUserId());
                }
                //组织签章
                if(senderVo.getSenderType() != null && senderVo.getSenderType().equals(SenderTypeEnum.ENTERPRISE.getCode())){
                    if(senderVo.getSenderSealId() == null || senderVo.getSenderSealId().length() == 0){
                        throw new PaasException("组织签章必须指定签章");
                    }
                    SignEntSeal entSeal = entSealService.getById(senderVo.getSenderSealId());
                    if(entSeal == null){
                        throw new PaasException("组织签章指定的签章不存在");
                    }
                    if(entSeal.getStatus() == null || entSeal.getStatus().equals(SealStatusEnum.UN_ENABLE.getCode())){
                        throw new PaasException("组织签章指定的签章已禁用");
                    }
                }

                sender.setSenderExternalType(senderVo.getSenderExternalType());
                sender.setSenderExternalValue(senderVo.getSenderExternalValue());

                RuSaveDataSender dataSender = new RuSaveDataSender();
                dataSender.setSender(sender);
                dataSender.setConfirmType(senderVo.getConfirmType());
                dataSender.setAgreeSkipWillingness(senderVo.getAgreeSkipWillingness());
                dataSender.setVerifyType(senderVo.getVerifyType());
                dataSigner.getAddSenderList().add(dataSender);
            }
        }
    }


    /**
     * @Description #新增企业接收方
     * @Param [addSenderVoList]
     * @return void
     **/
    public void addReceiveEntList(List<DocSenderVo> addSenderVoList,String signerId,RuSaveDataSigner dataSigner){
        if(addSenderVoList != null && addSenderVoList.size() > 0){
            for(DocSenderVo senderVo : addSenderVoList){
                SignRuSender sender = new SignRuSender();
                BeanUtils.copyProperties(senderVo,sender);
                sender.setSignerId(signerId);
                sender.setDeleteFlag(false);

                sender.setSenderExternalType(senderVo.getSenderExternalType());
                sender.setSenderExternalValue(senderVo.getSenderExternalValue());

                RuSaveDataSender dataSender = new RuSaveDataSender();
                dataSender.setSender(sender);
                dataSender.setConfirmType(senderVo.getConfirmType());
                dataSender.setAgreeSkipWillingness(senderVo.getAgreeSkipWillingness());
                dataSender.setVerifyType(senderVo.getVerifyType());
                dataSigner.getAddSenderList().add(dataSender);
            }
        }
    }

    /**
     * @Description #更新发起方
     * @Param [updateSenderVoList]
     * @return void
     **/
    public void updateSenderList(List<DocSenderVo> updateSenderVoList,String signerId,RuSaveDataSigner dataSigner){
        if(updateSenderVoList != null && updateSenderVoList.size() > 0){
            for(DocSenderVo senderVo : updateSenderVoList){
                RuSaveDataSender dataSender = new RuSaveDataSender();

                SignRuSender sender = new SignRuSender();
                BeanUtils.copyProperties(senderVo,sender);
                sender.setSignerId(signerId);
                sender.setDeleteFlag(false);
                if(senderVo.getSenderType() != null && senderVo.getSenderType().equals(SenderTypeEnum.OPERATOR.getCode())){
                    sender.setSenderUserId(MySecurityUtils.getCurrentUser().getTenantUserId());
                }
                //组织签章
                if(senderVo.getSenderType() != null && senderVo.getSenderType().equals(SenderTypeEnum.ENTERPRISE.getCode())){
                    if(senderVo.getSenderSealId() == null || senderVo.getSenderSealId().length() == 0){
                        throw new PaasException("组织签章必须指定签章");
                    }
                    SignEntSeal entSeal = entSealService.getById(senderVo.getSenderSealId());
                    if(entSeal == null){
                        throw new PaasException("组织签章指定的签章不存在");
                    }
                    if(entSeal.getStatus() == null || entSeal.getStatus().equals(SealStatusEnum.UN_ENABLE.getCode())){
                        throw new PaasException("组织签章指定的签章已禁用");
                    }

                }

                sender.setSenderExternalType(senderVo.getSenderExternalType());
                sender.setSenderExternalValue(senderVo.getSenderExternalValue());

                dataSender.setSender(sender);
                dataSender.setConfirmType(senderVo.getConfirmType());
                dataSender.setAgreeSkipWillingness(senderVo.getAgreeSkipWillingness());
                dataSender.setVerifyType(senderVo.getVerifyType());
                dataSigner.getUpdateSenderList().add(dataSender);
            }
        }
    }

    /**
     * @Description #更新企业接收方
     * @Param [updateSenderVoList]
     * @return void
     **/
    public void updateReceiveEntList(List<DocSenderVo> updateSenderVoList,String signerId,RuSaveDataSigner dataSigner){
        if(updateSenderVoList != null && updateSenderVoList.size() > 0){
            for(DocSenderVo senderVo : updateSenderVoList){
                SignRuSender sender = new SignRuSender();
                BeanUtils.copyProperties(senderVo,sender);
                sender.setSignerId(signerId);
                sender.setDeleteFlag(false);

                sender.setSenderExternalType(senderVo.getSenderExternalType());
                sender.setSenderExternalValue(senderVo.getSenderExternalValue());

                RuSaveDataSender dataSender = new RuSaveDataSender();
                dataSender.setSender(sender);
                dataSender.setConfirmType(senderVo.getConfirmType());
                dataSender.setAgreeSkipWillingness(senderVo.getAgreeSkipWillingness());
                dataSender.setVerifyType(senderVo.getVerifyType());
                dataSigner.getUpdateSenderList().add(dataSender);
            }
        }
    }

    /**
     * @Description #删除发起方
     * @Param [deleteSenderList]
     * @return void
     **/
    public void deleteSenderList(List<SignRuSender> deleteSenderList,RuSaveDataSigner dataSigner,RuSaveData saveData){
        if(deleteSenderList != null && deleteSenderList.size() > 0){
            for(SignRuSender sender : deleteSenderList){
                sender.setDeleteFlag(true);
                RuSaveDataSender dataSender = new RuSaveDataSender();
                dataSender.setSender(sender);
                dataSigner.getDeleteSenderList().add(dataSender);
                //当前签署人的签署控件
                List<SignRuDocControl> signRuDocControlList = ruDocControlService.listSignControlBySignerId(sender.getId());
                if(signRuDocControlList != null && signRuDocControlList.size() > 0){
                    List<String> collect = signRuDocControlList.stream().map(SignRuDocControl::getId).collect(Collectors.toList());
                    if(collect != null && collect.size() > 0){
                        saveData.getDeleteControlIdList().addAll(collect);
                    }
                }
            }

        }
    }

    /**
     * @Description #删除企业接收方
     * @Param [deleteSenderList]
     * @return void
     **/
    public void deleteReceiveEntList(List<SignRuSender> deleteSenderList,RuSaveDataSigner dataSigner,RuSaveData saveData){
        if(deleteSenderList != null && deleteSenderList.size() > 0){
            for(SignRuSender sender : deleteSenderList){
                sender.setDeleteFlag(true);
                RuSaveDataSender dataSender = new RuSaveDataSender();
                dataSender.setSender(sender);
                dataSigner.getDeleteSenderList().add(dataSender);
                //当前签署人的签署控件
                List<SignRuDocControl> signRuDocControlList = ruDocControlService.listSignControlBySignerId(sender.getId());
                if(signRuDocControlList != null && signRuDocControlList.size() > 0){
                    List<String> collect = signRuDocControlList.stream().map(SignRuDocControl::getId).collect(Collectors.toList());
                    if(collect != null && collect.size() > 0){
                        saveData.getDeleteControlIdList().addAll(collect);
                    }
                }
            }
        }
    }


    /**
     * @Description #计算签约文件变更内容
     * @Param []
     * @return com.kaifangqian.modules.opensign.service.business.vo.DocFileCalculateVo
     **/
    public DocFileCalculateVo calculateDocFileList(List<DocFileVo> fileVoList , String ruId){
        DocFileCalculateVo vo = new DocFileCalculateVo();
        //获取签约文件列表
        List<SignRuDoc> docList = ruDocService.listByRuId(ruId);
        //新增签约文件列表
        List<DocFileVo> addFileVoList = new ArrayList<>();
        //删除签约文件列表
        List<SignRuDoc> deleteDocList = new ArrayList<>();
        //签约文件列表不为空
        if(fileVoList != null && fileVoList.size() > 0) {
            if (docList == null || docList.size() == 0) {
                //如果库中已有的签约文件列表为空，则全部新增
                addFileVoList.addAll(fileVoList);
            } else {
                //对比计算那些是新增、删除的
                //如果库中不为空
                //找出新增的签约文件
                for (int i = 0; i < fileVoList.size(); i++) {
                    DocFileVo fileVo = fileVoList.get(i);
                    if (fileVo.getId() == null || fileVo.getId().length() == 0) {
                        addFileVoList.add(fileVo);
                        fileVoList.remove(fileVo);
                        i--;
                    }
                }
                if (fileVoList.size() > 0) {
                    //找出删除的文件
                    for (SignRuDoc ruDoc : docList) {
                        //假设该库中签约文件应该被删除
                        Boolean delete = true;
                        for (DocFileVo fileVo : fileVoList) {
                            if (ruDoc.getId().equals(fileVo.getId())) {
                                //如果新上传的签约文件中包含该库中签约文件，则不应该被删除
                                delete = false;
                            }
                        }
                        if (delete) {
                            //说明新上传的签约文件中不包含该库中签约文件，则需要被删除
                            deleteDocList.add(ruDoc);
                        }
                    }
                }else {
                    //如果传入的数据全部为新增，那么库中已有的则全部视为删除数据
                    deleteDocList.addAll(docList);
                }
            }
        }
        //签约文件列表为空了
        else if(docList != null && docList.size() > 0){
            //获取签约文件列表
            deleteDocList.addAll(docList);
        }
        vo.setDeleteRuDocList(deleteDocList);
        vo.setAddFileVoList(addFileVoList);
        return vo;
    }


    /**
     * @Description #保存新增的签约文件
     * @Param [addFileVoList, ruId]
     * @return void
     **/
    public void addDocFileList(List<DocFileVo> addFileVoList,String ruId,RuSaveData saveData){
        //新增签约文件
        if(addFileVoList != null && addFileVoList.size() > 0){

            for(DocFileVo fileVo : addFileVoList){
                RuDataDoc dataDoc = new RuDataDoc();

                SignRuDoc ruDoc = new SignRuDoc();
                ruDoc.setDocName(fileVo.getName());
                ruDoc.setSignRuId(ruId);
                ruDoc.setDocType(fileVo.getDocType());
                ruDoc.setDocOriginId(fileVo.getDocOriginId());
                ruDoc.setOriginType(fileVo.getOriginType());
                ruDoc.setDeleteFlag(false);
                ruDoc.setId(null);
                ruDoc.setDocOrder(fileVo.getDocOrder());

                dataDoc.setReDocId(fileVo.getId());
                dataDoc.setDocType(fileVo.getDocType());
                dataDoc.setOriginId(fileVo.getDocOriginId());
                dataDoc.setOriginType(fileVo.getOriginType());

                if(SignFileTypeEnum.TEMPLATE.getCode().equals(fileVo.getDocType()) && SignOriginTypeEnum.UPLOAD.getCode().equals(fileVo.getOriginType())){
                    //如果是模板，不管是业务线还是自行选择模板，都需要复制模板数据
                    SignTemplateRecord current = signTemplateRecordService.getCurrent(fileVo.getDocOriginId());
                    if(current == null){
                        //处理异常
                        throw new PaasException("保存模板文件失败");
                    }
                    //复制模版文件记录数据，转化成业务线实例文件记录数据
                    SignRuDocOperate ruDocOperate = new SignRuDocOperate();
                    ruDocOperate.setDocId(ruDoc.getId());
                    ruDocOperate.setIsCurrent(SignCurrentEnum.IS_CURRENT.getCode());
                    ruDocOperate.setSignRuId(ruId);
                    String newFileAnnexId = signFileService.copyAnnexStorage(current.getAnnexId());
                    if(newFileAnnexId == null){
                        //处理异常
                        throw new PaasException("保存模板文件失败");
                    }
                    ruDocOperate.setAnnexId(newFileAnnexId);
                    ruDocOperate.setDeleteFlag(false);
                    ruDocOperate.setId(null);

                    dataDoc.setRuDocOperate(ruDocOperate);
                    //图片数据
                    signFileService.copyFileImage(current.getAnnexId(),newFileAnnexId);
                    Integer docPage = annexImageService.countByAnnexId(newFileAnnexId);
                    ruDoc.setDocPage(docPage);
                    //控件数据
                    List<SignTemplateControl> templateControlList = signTemplateControlService.getList(fileVo.getDocOriginId());

                    if(templateControlList != null && templateControlList.size() > 0){
                        for(SignTemplateControl templateControl : templateControlList){

                            RuDataControl ruDataControl = new RuDataControl();

                            SignRuDocControl ruDocControl = new SignRuDocControl();
                            BeanUtils.copyProperties(templateControl,ruDocControl);
                            ruDocControl.setSignRuId(ruId);
                            ruDocControl.setSignRuDocId(ruDoc.getId());
                            ruDocControl.setControlType(templateControl.getType());
                            ruDocControl.setDeleteFlag(false);
                            ruDocControl.setId(null);
                            ruDocControl.setOriginType(ControlOriginTypeEnum.START.getCode());

                            ruDataControl.setRuDocControl(ruDocControl);
                            dataDoc.getWriteControlList().add(ruDataControl);
                        }

                    }
                }
                else if(SignFileTypeEnum.UPLOAD.getCode().equals(fileVo.getDocType()) && SignOriginTypeEnum.UPLOAD.getCode().equals(fileVo.getOriginType())){
                    //来自自行上传的上传文件，只有真实文件id
                    SignRuDocOperate ruDocOperate = new SignRuDocOperate();
                    ruDocOperate.setDocId(ruDoc.getId());
                    ruDocOperate.setIsCurrent(SignCurrentEnum.IS_CURRENT.getCode());
                    ruDocOperate.setSignRuId(ruId);
                    ruDocOperate.setAnnexId(fileVo.getAnnexId());
                    ruDocOperate.setDeleteFlag(false);
                    ruDocOperate.setId(null);
                    dataDoc.setRuDocOperate(ruDocOperate);
                    //转换图片
                    Integer docPage = annexImageService.countByAnnexId(fileVo.getAnnexId());
                    ruDoc.setDocPage(docPage);
                }
                dataDoc.setRuDoc(ruDoc);
                saveData.getAddDocList().add(dataDoc);
            }

        }

    }

    public void updateDocFileOrderList(List<DocFileVo> fileVoList,RuSaveData saveData){
        for(DocFileVo fileVo : fileVoList){
            if(fileVo.getId() != null){
                SignRuDoc doc = new SignRuDoc();
                doc.setId(fileVo.getId());
                doc.setDocOrder(fileVo.getDocOrder());
                saveData.getUpdateDocOrderList().add(doc);
            }
        }
    }

    /**
     * @Description #删除签约文件
     * @Param [docList, ruId]
     * @return void
     **/
    public void deleteDocFileList(List<SignRuDoc> docList,String ruId,RuSaveData saveData){
        if(docList != null && docList.size() > 0){
            List<String> deleteDocIdList = docList.stream().map(SignRuDoc::getId).collect(Collectors.toList());

            saveData.getDeleteDocIdList().addAll(deleteDocIdList);

            //填写控件
            List<SignRuDocControl> writeControlList = ruDocControlService.listWriteByDocList(deleteDocIdList);
            if(writeControlList != null && writeControlList.size() > 0){
                List<String> collect = writeControlList.stream().map(SignRuDocControl::getId).collect(Collectors.toList());
                if(collect != null && collect.size() > 0){
                    saveData.getDeleteControlIdList().addAll(collect);
                }
            }

            //签署控件
            List<SignRuDocControl> signRuDocControlList = ruDocControlService.listSignControlList(ruId);
            if(signRuDocControlList != null && signRuDocControlList.size() > 0){
                for(SignRuDocControl ruDocControl : signRuDocControlList){

                    saveData.getDeleteControlIdList().add(ruDocControl.getId());

                    List<SignRuDocControlProperty> propertyList = ruDocControlPropertyService.listByControlId(ruDocControl.getId());
                    if(propertyList != null && propertyList.size() > 0){
                        Integer relationDocCount = 0 ;
                        Integer deleteCount = 0 ;
                        Boolean relationDocStatus = false ;
                        for(SignRuDocControlProperty property : propertyList){
                            if(property.getPropertyType().equals(ControlPropertyTypeEnum.RELATION_DOC.getCode())){
                                relationDocCount = relationDocCount + 1;
                                if(deleteDocIdList.contains(property.getPropertyValue())){

                                    saveData.getDeleteControlPropertyIdList().add(property.getId());

                                    relationDocStatus = true ;
                                    deleteCount = deleteCount + 1 ;
                                }
                            }
                        }
                        if(relationDocStatus) {
                            if (relationDocCount == deleteCount) {
                                saveData.getDeleteControlIdList().add(ruDocControl.getId());
                                saveData.getDeleteControlPropertyByControlIdList().add(ruDocControl.getId());
                            }
                        }
                    }
                }
            }

        }
    }
}