/**
 * @description 签署业务线创建业务
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
import com.kaifangqian.modules.opensign.pdfbox.PdfboxService;
import com.kaifangqian.modules.opensign.service.business.vo.*;
import com.kaifangqian.modules.opensign.service.re.*;
import com.kaifangqian.modules.opensign.service.template.SignTemplateControlService;
import com.kaifangqian.modules.opensign.service.template.SignTemplateRecordService;
import com.kaifangqian.modules.opensign.vo.base.sign.*;
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

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Description: RuCreateService
 * @Package: com.kaifangqian.modules.opensign.service.business
 * @ClassName: RuCreateService
 * @author: FengLai_Gong
 */
@Service
public class RuCreateService {



    @Autowired
    private SignTemplateRecordService signTemplateRecordService ;
    @Autowired
    private SignTemplateControlService signTemplateControlService ;

    @Autowired
    private SignReDocParamService reDocParamService ;

    @Autowired
    private SignReService reService ;
    @Autowired
    private SignReDocService reDocService ;
    @Autowired
    private SignReDocControlService reDocControlService ;
    @Autowired
    private SignReDocControlPropertyService reDocControlPropertyService ;


    @Autowired
    private SignFileService signFileService ;

    @Autowired
    private SignEntSealService entSealService ;


    @Autowired
    private AnnexImageService annexImageService ;


    @Autowired
    private PdfboxService pdfboxService ;

    @Autowired
    private RuDataService ruDataService ;


    /**
     * @Description #新增全部数据
     * @Param [vo]
     * @return java.lang.String
     **/
    public String create(BaseVo vo){

        String signReId = vo.getBaseVo().getSignReId();
        if(signReId == null || signReId.length() == 0){
            throw new PaasException("业务线id不存在");
        }
         SignRe re = reService.getById(vo.getBaseVo().getSignReId());
        if(re == null){
            throw new PaasException("业务线不存在");
        }
        //创建整合数据
        RuCreateData createData = new RuCreateData();
        //保存基本数据
        createRu(createData,vo.getBaseVo(),re);
        //创建签署人相关数据
        createSignerList(createData,vo.getSignerList());
        //新增签约文件
        createDocList(createData, vo.getFileList());
        //控件数据
        createSignControlList(createData,re.getId(),createData.getReDocIdList());
        //抄送方和附件
        createOtherFileAndCcer(createData,vo.getCcerList(),vo.getOtherFileList() );
        //业务线签署人配置
        createData.setReSignerType(re.getSignerType());
        //创建业务线实例
        ruDataService.createRuData(createData);

        return createData.getRuId() ;
    }



    public void createRu(RuCreateData createData, DocBaseVo baseVo, SignRe re){
        SignRu ru = new SignRu();
        BeanUtils.copyProperties(baseVo,ru);
        if(baseVo.getSubjectType() == null || baseVo.getSubjectType().equals(SignReRuleGenerateEnum.CUSTOM.getCode())){
            if(baseVo.getSubject() == null || baseVo.getSubject().length() == 0){
                //根据业务线名称加上时间戳生成业务线实例主题
                ru.setSubject(System.currentTimeMillis() + "-" + re.getName());
            }else {
                ru.setSubject(baseVo.getSubject());
            }
        }else {
            ru.setSubject(System.currentTimeMillis() + "-" + re.getName());
        }
        if(ru.getControlChangeFlag() == null){
            if(re.getControlChangeFlag() == null){
                //控件变更状态，默认状态
                ru.setControlChangeFlag(ControlChangeFlagEnum.NECESSARY_AND_ADD.getType());
            }else {
                //控件变更状态，默认状态
                ru.setControlChangeFlag(re.getControlChangeFlag());
            }

        }

        //保存ru数据
        createData.setSignRu(ru);
    }


    public void createSignerList(RuCreateData createData, List<DocSignerVo> signerList){
        //新增签署人
        if(signerList == null || signerList.size() == 0){
            return;
        }
        //再新插入数据
        for(DocSignerVo signerVo : signerList){
            RuDataSigner dataSinger = new RuDataSigner();
            dataSinger.setReSignerId(signerVo.getId());

            SignRuSigner signer = new SignRuSigner();
            BeanUtils.copyProperties(signerVo,signer);
            signer.setDeleteFlag(false);

            if(SignerTypeEnum.SENDER.getCode().equals(signerVo.getSignerType())){
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
            signer.setId(null);
            dataSinger.setRuSigner(signer);
            if(SignerTypeEnum.SENDER.getCode().equals(signerVo.getSignerType())){
                //发起人
                List<DocSenderVo> senderVoList = signerVo.getSenderList();
                //新增发起方
                if(senderVoList != null && senderVoList.size() > 0){
                    for(DocSenderVo senderVo : senderVoList){
                        RuDataSender dataSender = new RuDataSender();
                        dataSender.setReSenderId(senderVo.getId());
                        SignRuSender sender = new SignRuSender();
                        BeanUtils.copyProperties(senderVo,sender);
                        sender.setSignerId(signer.getId());
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
                        sender.setId(null);
                        sender.setSenderExternalType(senderVo.getSenderExternalType());
                        sender.setSenderExternalValue(senderVo.getSenderExternalValue());

                        dataSender.setRuSender(sender);
                        dataSender.setConfirmType(senderVo.getConfirmType());
                        dataSender.setAgreeSkipWillingness(senderVo.getAgreeSkipWillingness());
                        dataSender.setVerifyType(senderVo.getVerifyType());

                        if (senderVo.getSenderType() != null && !senderVo.getSenderType().equals(SenderTypeEnum.ENTERPRISE.getCode())){
                            dataSender.setPersonalSignAuth(senderVo.getPersonalSignAuth());
                        }

                        dataSinger.getAddSenderList().add(dataSender);
                    }
                }
                dataSinger.setSignerType(SignerTypeEnum.SENDER.getCode());
            }else if(SignerTypeEnum.RECEIVER_ENT.getCode().equals(signerVo.getSignerType())){
                //企业接收方
                List<DocSenderVo> senderVoList = signerVo.getSenderList();
                if(senderVoList != null && senderVoList.size() > 0){
                    for(DocSenderVo senderVo : senderVoList){
                        RuDataSender dataSender = new RuDataSender();
                        dataSender.setReSenderId(senderVo.getId());

                        SignRuSender sender = new SignRuSender();
                        BeanUtils.copyProperties(senderVo,sender);
                        sender.setSignerId(signer.getId());
                        sender.setDeleteFlag(false);
                        sender.setId(null);

                        sender.setSenderExternalType(senderVo.getSenderExternalType());
                        sender.setSenderExternalValue(senderVo.getSenderExternalValue());

                        dataSender.setRuSender(sender);
                        dataSender.setConfirmType(senderVo.getConfirmType());
                        dataSender.setAgreeSkipWillingness(senderVo.getAgreeSkipWillingness());
                        dataSender.setVerifyType(senderVo.getVerifyType());
                        if (senderVo.getSenderType() != null && !senderVo.getSenderType().equals(SenderTypeEnum.ENTERPRISE.getCode())){
                            dataSender.setPersonalSignAuth(senderVo.getPersonalSignAuth());
                        }
                        dataSinger.getAddSenderList().add(dataSender);
                    }
                }
                dataSinger.setSignerType(SignerTypeEnum.RECEIVER_ENT.getCode());
            }else {
                dataSinger.setSignerType(SignerTypeEnum.RECEIVER_PERSONAL.getCode());
                dataSinger.setConfirmType(signerVo.getConfirmType());
                dataSinger.setAgreeSkipWillingness(signerVo.getAgreeSkipWillingness());
                dataSinger.setVerifyType(signerVo.getVerifyType());
                dataSinger.setPersonalSignAuth(signerVo.getPersonalSignAuth());
            }
            createData.getSignerList().add(dataSinger);
        }
    }

    public void createDocList(RuCreateData createData,List<DocFileVo> fileList){
        if(fileList == null || fileList.size() == 0){
            return;
        }
        for(DocFileVo fileVo : fileList){

            RuDataDoc dataDoc = new RuDataDoc();

            SignRuDoc ruDoc = new SignRuDoc();
            ruDoc.setDocName(fileVo.getName());
            ruDoc.setDocType(fileVo.getDocType());
            ruDoc.setDocOriginId(fileVo.getDocOriginId());
            ruDoc.setOriginType(fileVo.getOriginType());
            ruDoc.setDeleteFlag(false);
            ruDoc.setId(null);
            ruDoc.setDocOrder(fileVo.getDocOrder());

            dataDoc.setRuDoc(ruDoc);
            dataDoc.setReDocId(fileVo.getId());
            dataDoc.setOriginId(fileVo.getDocOriginId());
            dataDoc.setDocType(fileVo.getDocType());
            dataDoc.setOriginType(fileVo.getOriginType());

            if(fileVo.getId() != null && fileVo.getId().length() > 0){
                createData.getReDocIdList().add(fileVo.getId());
            }

            if(SignFileTypeEnum.TEMPLATE.getCode().equals(fileVo.getDocType())){
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
                String newFileAnnexId = signFileService.copyAnnexStorage(current.getAnnexId());
                if(newFileAnnexId == null){
                    //处理异常
                    throw new PaasException("保存模板文件失败");
                }
                ruDocOperate.setAnnexId(newFileAnnexId);
                ruDocOperate.setDeleteFlag(false);
                ruDocOperate.setId(null);
                //图片数据
                signFileService.copyFileImage(current.getAnnexId(),newFileAnnexId);
                Integer docPage = annexImageService.countByAnnexId(newFileAnnexId);
                //文档操作数据和页码
                dataDoc.setRuDocOperate(ruDocOperate);
                dataDoc.setDocPage(docPage);
                //模板控件
                createWriteControlList(dataDoc,fileVo.getDocOriginId(),fileVo.getId());

            } else if(SignFileTypeEnum.UPLOAD.getCode().equals(fileVo.getDocType())){
                if(SignOriginTypeEnum.RE.getCode().equals(fileVo.getOriginType())){
                    //来自业务线的上传，查询业务线配置的temporary数据
                    SignReDoc reDoc = reDocService.getById(fileVo.getDocOriginId());
                    if(reDoc == null){
                        continue;
                    }
                    SignRuDocOperate ruDocOperate = new SignRuDocOperate();
                    ruDocOperate.setDocId(ruDoc.getId());
                    ruDocOperate.setIsCurrent(SignCurrentEnum.IS_CURRENT.getCode());
                    String newFileAnnexId = signFileService.copyAnnexStorage(reDoc.getAnnexId());
                    ruDocOperate.setAnnexId(newFileAnnexId);
                    ruDocOperate.setDeleteFlag(false);
                    ruDocOperate.setId(null);
                    //图片数据
                    signFileService.copyFileImage(reDoc.getAnnexId(),newFileAnnexId);
                    Integer docPage = annexImageService.countByAnnexId(newFileAnnexId);
                    //文档操作数据和页码
                    dataDoc.setRuDocOperate(ruDocOperate);
                    dataDoc.setDocPage(docPage);

                }else if(SignOriginTypeEnum.UPLOAD.getCode().equals(fileVo.getOriginType())){
                    //来自自行上传的上传文件，只有真实文件id
                    SignRuDocOperate ruDocOperate = new SignRuDocOperate();
                    ruDocOperate.setDocId(ruDoc.getId());
                    ruDocOperate.setIsCurrent(SignCurrentEnum.IS_CURRENT.getCode());
                    ruDocOperate.setAnnexId(fileVo.getAnnexId());
                    ruDocOperate.setDeleteFlag(false);
                    ruDocOperate.setId(null);
                    //文件页码
                    byte[] pdfByte = signFileService.getByteById(fileVo.getAnnexId());
                    if(pdfByte == null){
                        throw new PaasException("签约文件异常");
                    }
                    Integer docPage = pdfboxService.getPdfPage(pdfByte);
                    //文档操作数据和页码
                    dataDoc.setRuDocOperate(ruDocOperate);
                    dataDoc.setDocPage(docPage);
                }
            }
            createData.getDocList().add(dataDoc);
        }
    }

    public void createWriteControlList(RuDataDoc dataDoc,String templateId,String reDocId){
        //获取模板控件
        List<SignTemplateControl> list = signTemplateControlService.getList(templateId);
        if (list == null || list.size() == 0) {
            return;
        }
        List<SignReDocParam> paramList = reDocParamService.listByReDocId(reDocId);
        //保存模板控件参数
        for (SignTemplateControl templateControl : list) {
            RuDataControl dataControl = new RuDataControl();

            SignRuDocControl ruDocControl = new SignRuDocControl();
            BeanUtils.copyProperties(templateControl, ruDocControl);
            //维护业务线配置中配置好的关系
            if (paramList != null && paramList.size() > 0) {
                for (SignReDocParam param : paramList) {
                    if (templateControl.getInterfaceParamName().equals(param.getInterfaceParamName())) {
                        //对应-业务线配置签署人id
                        dataControl.setReSignerId(param.getSignerId());
                    }
                }
            }
            //封装控件其他参数
            ruDocControl.setControlType(templateControl.getType());
            if (reDocId == null) {
                ruDocControl.setOriginType(ControlOriginTypeEnum.START.getCode());
            } else {
                ruDocControl.setOriginType(ControlOriginTypeEnum.RE.getCode());
            }

            ruDocControl.setInterfaceParamName(templateControl.getInterfaceParamName());
            ruDocControl.setDeleteFlag(false);
            ruDocControl.setId(null);

            dataControl.setReDocId(reDocId);
            dataControl.setRuDocControl(ruDocControl);
            dataDoc.getWriteControlList().add(dataControl);
        }
    }

    public void createSignControlList(RuCreateData createData ,String reId,List<String> reDocIdList){


        if(reDocIdList.size() == 0){
            return;
        }

        //获取该业务线配置的签约文件配置的控件
        List<SignReDocControl> signReDocControlList = reDocControlService.listByParam(reId);
        if(signReDocControlList != null && signReDocControlList.size() > 0){
            List<String> reControlIdList = signReDocControlList.stream().map(SignReDocControl::getId).collect(Collectors.toList());
            Map<String, List<SignReDocControlProperty>> rePropertyMap = null ;
            if(reControlIdList != null && reControlIdList.size() > 0){
                List<SignReDocControlProperty> reDocControlPropertyList = reDocControlPropertyService.listByControlIdList(reControlIdList);
                if(reDocControlPropertyList != null && reDocControlPropertyList.size() > 0){
                    rePropertyMap = reDocControlPropertyList.stream().collect(Collectors.groupingBy(SignReDocControlProperty::getControlId));
                }
            }
            for(SignReDocControl reDocControl : signReDocControlList){


                if(rePropertyMap != null && rePropertyMap.containsKey(reDocControl.getId())){
                    List<SignReDocControlProperty> reProperties = rePropertyMap.get(reDocControl.getId());
                    if(reProperties != null && reProperties.size() > 0){
                        Boolean controlAddFlag = false ;
                        for(SignReDocControlProperty reDocControlProperty : reProperties){
                            if(reDocControlProperty.getPropertyType().equals(ControlPropertyTypeEnum.RELATION_DOC.getCode())){
                                if(reDocIdList.contains(reDocControlProperty.getPropertyValue())){
                                    //是否包含当前的reDocId
                                    controlAddFlag  = true ;
                                }
                            }
                        }
                        if(controlAddFlag){
                            RuDataControl dataControl = new RuDataControl();

                            SignRuDocControl ruDocControl = new SignRuDocControl();
                            BeanUtils.copyProperties(reDocControl,ruDocControl);
                            ruDocControl.setOriginType(ControlOriginTypeEnum.RE.getCode());
                            ruDocControl.setDeleteFlag(false);
                            ruDocControl.setId(UUID.randomUUID().toString());



                            for(SignReDocControlProperty reDocControlProperty : reProperties){
                                if(reDocControlProperty.getPropertyType().equals(ControlPropertyTypeEnum.RELATION_DOC.getCode())){
                                    if(reDocIdList.contains(reDocControlProperty.getPropertyValue())){

                                        RuDataControlProperty dataControlProperty = new RuDataControlProperty();

                                        SignRuDocControlProperty ruDocControlProperty = new SignRuDocControlProperty();
                                        ruDocControlProperty.setControlId(ruDocControl.getId());
                                        ruDocControlProperty.setPropertyType(reDocControlProperty.getPropertyType());

                                        dataControlProperty.setReDocId(reDocControlProperty.getPropertyValue());
                                        dataControlProperty.setRuDocControlProperty(ruDocControlProperty);
                                        dataControl.getDataControlPropertyList().add(dataControlProperty);
                                    }

                                }else {
                                    RuDataControlProperty dataControlProperty = new RuDataControlProperty();

                                    SignRuDocControlProperty ruDocControlProperty = new SignRuDocControlProperty();
                                    ruDocControlProperty.setControlId(ruDocControl.getId());
                                    ruDocControlProperty.setPropertyType(reDocControlProperty.getPropertyType());
                                    ruDocControlProperty.setPropertyValue(reDocControlProperty.getPropertyValue());

                                    dataControlProperty.setRuDocControlProperty(ruDocControlProperty);
                                    dataControl.getDataControlPropertyList().add(dataControlProperty);

                                }

                            }
                            //签署人id关联关系
                            ruDocControl.setSignerId(null);
                            dataControl.setReSignerId(reDocControl.getSignerId());

                            dataControl.setRuDocControl(ruDocControl);
                            createData.getSignControlList().add(dataControl);
                        }

                    }
                }

            }
        }
    }

    public void createOtherFileAndCcer(RuCreateData createData, List<DocCcerVo> ccerList , List<DocOtherFileVo> otherFileList ){
        //抄送方
        if(ccerList != null && ccerList.size() > 0){
            for(DocCcerVo ccerVo : ccerList){
                SignRuCcer ccer = new SignRuCcer();
                BeanUtils.copyProperties(ccerVo,ccer);
                ccer.setDeleteFlag(false);
                ccer.setId(null);
                ccer.setTenantUserId(ccerVo.getInternalCcerId());
                ccer.setCcerAddType(ccerVo.getCcerAddType());
                createData.getCcerList().add(ccer);
            }
        }
        //附件
        if(otherFileList != null && otherFileList.size() > 0){
            for(DocOtherFileVo fileVo : otherFileList){
                createData.getOtherFileList().add(fileVo.getAnnexId());
            }
        }
    }
}