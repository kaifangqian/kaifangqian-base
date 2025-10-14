/**
 * @description 签署相关数据服务
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

import com.kaifangqian.modules.cert.service.CertBusinessService;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.pdfbox.PdfboxService;
import com.kaifangqian.modules.opensign.service.business.vo.*;
import com.kaifangqian.modules.opensign.service.re.*;
import com.kaifangqian.modules.opensign.service.ru.*;
import com.kaifangqian.modules.opensign.service.template.SignTemplateControlService;
import com.kaifangqian.modules.opensign.service.template.SignTemplateImageRecordService;
import com.kaifangqian.modules.opensign.service.template.SignTemplateRecordService;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.service.annex.AnnexImageService;
import com.kaifangqian.modules.opensign.service.business.vo.*;
import com.kaifangqian.modules.opensign.service.re.*;
import com.kaifangqian.modules.opensign.service.ru.*;
import com.kaifangqian.modules.opensign.service.seal.SignEntSealService;
import com.kaifangqian.modules.opensign.service.tool.DateSealGenerateService;
import com.kaifangqian.modules.opensign.service.tool.SignFileService;
import com.kaifangqian.modules.system.service.ISysTenantUserService;
import com.kaifangqian.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Description: RuDataService
 * @Package: com.kaifangqian.modules.opensign.service.business
 * @ClassName: RuDataService
 * @author: FengLai_Gong
 */
@Service
public class RuDataService {

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
    private SignRuOperatorService ruOperatorService ;
    @Autowired
    private SignRuCcerService ccerService ;
    @Autowired
    private SignRuRelationService relationService ;

    @Autowired
    private SignTemplateRecordService signTemplateRecordService ;
    @Autowired
    private SignTemplateImageRecordService signTemplateImageRecordService ;
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
    private ReBusinessService reBusinessService ;

    @Autowired
    private SignFileService signFileService ;


    @Autowired
    private ISysTenantUserService tenantUserService ;


    @Autowired
    private RuSignService ruSignService ;

    @Autowired
    private SignEntSealService entSealService ;

    @Autowired
    private CertBusinessService certBusinessService ;

    @Autowired
    private AnnexImageService annexImageService ;

    @Autowired
    private DateSealGenerateService dateSealGenerateService ;

    @Autowired
    private SignRuDocControlPropertyService ruDocControlPropertyService ;


    @Autowired
    private SignRuSignConfirmService ruSignConfirmService ;
    @Autowired
    private SignReSignConfirmService reSignConfirmService ;

    @Autowired
    private PdfboxService pdfboxService ;

    @Autowired
    private SignRuTaskService ruTaskService ;
    @Autowired
    private SignRuSignTemporaryService ruSignTemporaryService ;

    @Autowired
    private ISysConfigService sysConfigService;


    @Autowired
    private RuCallbackService ruCallbackService ;


    @Autowired
    private SignRuOperateRecordService ruOperateRecordService ;

    @Autowired
    private SignRuOperateDocRecordService ruOperateDocRecordService ;

    @Autowired
    private SignRuKeywordService ruKeywordService ;
    @Autowired
    private SignRuKeywordPropertyService ruKeywordPropertyService ;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor ;




    @Transactional(rollbackFor = Exception.class)
    public void createRuData(RuCreateData ruCreateData){

        //主数据
        SignRu ru = ruCreateData.getSignRu();
        ru.setDeleteFlag(false);
        ru.setStatus(SignRuStatusEnum.DRAFT.getCode());
        if(ru.getControlChangeFlag() == null){
            ru.setControlChangeFlag(ControlChangeFlagEnum.NECESSARY_AND_ADD.getType());
        }
        boolean saveRu = ruService.save(ru);
        if(!saveRu){
            throw new PaasException("保存主数据失败");
        }
        String ruId = ru.getId() ;
        ruCreateData.setRuId(ruId);
        //签署人
        List<RuDataSigner> singerList = ruCreateData.getSignerList();
        if(singerList.size() > 0){
            for(RuDataSigner dataSinger : singerList){
                SignRuSigner ruSigner = dataSinger.getRuSigner();
                ruSigner.setSignRuId(ruId);
                ruSigner.setDeleteFlag(false);
                boolean saveSigner = signerService.save(ruSigner);
                if(!saveSigner){
                    //异常处理
                    throw new PaasException("保存签署人" + ruSigner.getSignerName() + "失败");
                }
                if(dataSinger.getReSignerId() != null){
                    //关联关系
                    ruCreateData.getReSigner2RuSingerMap().put(dataSinger.getReSignerId(),ruSigner.getId());
                }


                if(SignerTypeEnum.SENDER.getCode().equals(dataSinger.getSignerType()) || SignerTypeEnum.RECEIVER_ENT.getCode().equals(dataSinger.getSignerType())){
                    //发起方
                    List<RuDataSender> addSenderList = dataSinger.getAddSenderList();
                    if(addSenderList.size() > 0){
                        for(RuDataSender dataSender : addSenderList){
                            SignRuSender ruSender = dataSender.getRuSender();
                            if(ruSender != null){
                                ruSender.setSignerId(ruSigner.getId());
                                ruSender.setDeleteFlag(false);
                                boolean b = senderService.save(ruSender);
                                if(!b){
                                    throw new PaasException("保存发起方" + ruSender.getSenderName() + "失败");
                                }
                                if(dataSender.getReSenderId() != null){
                                    //关联关系
                                    ruCreateData.getReSigner2RuSingerMap().put(dataSender.getReSenderId(),ruSender.getId());
                                }

                                //发起方
                                ruSignConfirmService.save(ruSender.getId(),ruId,dataSinger.getSignerType(),dataSender.getAgreeSkipWillingness(),dataSender.getVerifyType(),dataSender.getPersonalSignAuth());
//                                if(dataSender.getConfirmType() != null && dataSender.getConfirmType() == 1){
//                                    //开启人脸识别
//                                    ruSignConfirmService.save(ruSender.getId(),ruId,dataSinger.getSignerType());
//                                }
                            }

                        }
                    }
                }else {
                    //个人接收方
                    ruSignConfirmService.save(ruSigner.getId(),ruId,SignerTypeEnum.RECEIVER_PERSONAL.getCode(),dataSinger.getAgreeSkipWillingness(),dataSinger.getVerifyType(),dataSinger.getPersonalSignAuth());
//                    if(dataSinger.getConfirmType() != null && dataSinger.getConfirmType() == 1){
//                        //开启人脸识别
//                        ruSignConfirmService.save(ruSigner.getId(),ruId,SignerTypeEnum.RECEIVER_PERSONAL.getCode());
//                    }
                }
            }
        }
        //签署文档
        List<RuDataDoc> docList = ruCreateData.getDocList();
        if(docList.size() > 0){
            for(RuDataDoc dataDoc : docList){
                SignRuDoc ruDoc = dataDoc.getRuDoc();
                ruDoc.setSignRuId(ruId);
                ruDoc.setDeleteFlag(false);
                ruDoc.setDocPage(dataDoc.getDocPage());
                if(ruDoc.getDocPage() == null || ruDoc.getDocPage() == 0){
                    throw new PaasException("保存签约文件无页码");
                }
                boolean saveRuDoc = ruDocService.save(ruDoc);
                if(!saveRuDoc){
                    //异常处理
                    throw new PaasException("保存签约文件失败");
                }
                if(dataDoc.getReDocId() != null && dataDoc.getReDocId().length() > 0){
                    ruCreateData.getReDoc2RuDocMap().put(dataDoc.getReDocId(),ruDoc.getId());
                }
                SignRuDocOperate ruDocOperate = dataDoc.getRuDocOperate();
                ruDocOperate.setDocId(ruDoc.getId());
                ruDocOperate.setSignRuId(ruId);
                ruDocOperate.setDeleteFlag(false);
                boolean saveDocOperate = ruDocOperateService.save(ruDocOperate);
                if(!saveDocOperate){
                    //处理异常
                    throw new PaasException("保存模板文件失败");
                }
                //填写控件
                List<RuDataControl> writeControlList = dataDoc.getWriteControlList();
                if(writeControlList.size() > 0){
                    for(RuDataControl writeControl : writeControlList){
                        String reSignerId = writeControl.getReSignerId();
                        String ruSignerId = ruCreateData.getReSigner2RuSingerMap().get(reSignerId);
                        SignRuDocControl ruDocControl = writeControl.getRuDocControl();
                        if(ruDocControl.getId() == null){
                            ruDocControl.setId(UUID.randomUUID().toString());
                        }
                        ruDocControl.setSignerId(ruSignerId);
                        ruDocControl.setSignRuDocId(ruDoc.getId());
                        ruDocControl.setSignRuId(ruId);

                        ruDocControl.setDeleteFlag(false);
                        boolean saveControl = ruDocControlService.save(ruDocControl);
                        if(!saveControl){
                            throw new PaasException("保存控件失败");
                        }
                    }
                }
            }
        }
        //保存外层填写控件
        List<RuDataControl> writeControlList = ruCreateData.getWriteControlList();
        if(writeControlList.size() > 0){
            for(RuDataControl ruDataControl : writeControlList){
                SignRuDocControl ruDocControl = ruDataControl.getRuDocControl();
                if(ruDocControl.getId() == null){
                    ruDocControl.setId(UUID.randomUUID().toString());
                }
                ruDocControl.setDeleteFlag(false);
                boolean saveControl = ruDocControlService.save(ruDocControl);
                if(!saveControl){
                    throw new PaasException("保存控件失败");
                }
            }
        }

        //签署控件
        List<RuDataControl> signControlList = ruCreateData.getSignControlList();
        if(signControlList.size() > 0){
            for(RuDataControl signControl : signControlList) {

                SignRuDocControl ruDocControl = signControl.getRuDocControl();
                if(ruDocControl.getSignerId() == null){
                    String reSignerId = signControl.getReSignerId();
                    if (reSignerId == null || reSignerId.length() == 0) {
                        throw new PaasException("签署控件归属人丢失");
                    }
                    String ruSignerId = ruCreateData.getReSigner2RuSingerMap().get(reSignerId);
                    if (ruSignerId == null || ruSignerId.length() == 0) {
                        throw new PaasException("签署控件归属人丢失");
                    }
                    ruDocControl.setSignerId(ruSignerId);
                }
                ruDocControl.setSignRuId(ruId);
                if(ruDocControl.getId() == null){
                    ruDocControl.setId(UUID.randomUUID().toString());
                }

                ruDocControl.setDeleteFlag(false);
                boolean saveControl = ruDocControlService.save(ruDocControl);
                if(!saveControl){
                    throw new PaasException("保存控件失败");
                }
                List<RuDataControlProperty> dataControlPropertyList = signControl.getDataControlPropertyList();
                for(RuDataControlProperty dataControlProperty : dataControlPropertyList) {
                    SignRuDocControlProperty ruDocControlProperty = dataControlProperty.getRuDocControlProperty();
                    if(ruDocControlProperty.getPropertyType().equals(ControlPropertyTypeEnum.RELATION_DOC.getCode())){
                        if(ruDocControlProperty.getPropertyValue() == null){
                            String reDocId = dataControlProperty.getReDocId();
                            if(reDocId != null && reDocId.length() > 0 && ruCreateData.getReDoc2RuDocMap().size() > 0){
                                String ruDocId = ruCreateData.getReDoc2RuDocMap().get(reDocId);
                                if(ruDocId == null || ruDocId.length() == 0){
                                    throw new PaasException("签署控件归属文档丢失");
                                }
                                ruDocControlProperty.setPropertyValue(ruDocId);
                            }
                        }

                    }

                    ruDocControlProperty.setControlId(ruDocControl.getId());
                    ruDocControlProperty.setRuId(ruId);
                    if(ruDocControlProperty.getId() == null){
                        ruDocControlProperty.setId(UUID.randomUUID().toString());
                    }

                    boolean saveControlProperty = ruDocControlPropertyService.save(ruDocControlProperty);
                    if(!saveControlProperty){
                        throw new PaasException("保存控件属性失败");
                    }
                }
            }
        }
        //抄送人列表
        List<SignRuCcer> ccerList = ruCreateData.getCcerList();
        if(ccerList.size() > 0){
            //先删除
            ccerService.deleteByRuId(ruCreateData.getRuId());
            for(SignRuCcer ccer : ccerList){
                ccer.setSignRuId(ruId);
                ccer.setDeleteFlag(false);
                ccerService.save(ccer);
            }
        }
        //附件列表
        List<String> otherFileList = ruCreateData.getOtherFileList();
        if(otherFileList.size() > 0){
            for(String annexId : otherFileList){
                signFileService.updateAnnexStorage(SignFileEnum.SIGN_FILE_OTHER,ru.getId(),annexId);
            }
        }
        //签署人类型
        if(ruCreateData.getReSignerType() != null && ruCreateData.getReSignerType() == SignReSignerTypeEnum.CUSTOM.getCode()){
            //清空填写控件的关联人
            ruDocControlService.resetWriteControlList(ru.getId());
            //删除签署控件
            ruDocControlService.deleteSignControlList(ru.getId());
            //删除签署控件属性
            ruDocControlPropertyService.deleteByRuId(ru.getId());
        }

        if(ruCreateData.getRuSignControlList() != null && ruCreateData.getRuSignControlList().size() > 0){
            List<SignRuDocControl> ruSignControlList = ruCreateData.getRuSignControlList();
            Date now = new Date();
            for(SignRuDocControl ruDocControl : ruSignControlList){
                ruDocControl.setSignRuId(ruCreateData.getRuId());
                ruDocControl.setCreateTime(now);
            }
            ruDocControlService.saveBatch(ruSignControlList);
        }
        if(ruCreateData.getRuSignControlPropertyList() != null && ruCreateData.getRuSignControlPropertyList().size() > 0){
            List<SignRuDocControlProperty> ruSignControlPropertyList = ruCreateData.getRuSignControlPropertyList();
            for(SignRuDocControlProperty ruDocControlProperty : ruSignControlPropertyList){
                ruDocControlProperty.setRuId(ruCreateData.getRuId());
            }
            ruDocControlPropertyService.saveBatch(ruSignControlPropertyList);
        }
        if(ruCreateData.getRuKeywordList() != null && ruCreateData.getRuKeywordList().size() > 0){
            List<SignRuKeyword> ruKeywordList = ruCreateData.getRuKeywordList();
            for(SignRuKeyword signRuKeyword : ruKeywordList){
                signRuKeyword.setRuId(ruCreateData.getRuId());
            }
            ruKeywordService.saveBatch(ruKeywordList);
        }
        if(ruCreateData.getRuKeywordPropertyList() != null && ruCreateData.getRuKeywordPropertyList().size() > 0){
            List<SignRuKeywordProperty> ruKeywordPropertyList = ruCreateData.getRuKeywordPropertyList();
            for(SignRuKeywordProperty signRuKeywordProperty : ruKeywordPropertyList){
                signRuKeywordProperty.setRuId(ruCreateData.getRuId());
            }
            ruKeywordPropertyService.saveBatch(ruKeywordPropertyList);
        }

    }


    @Transactional(rollbackFor = Exception.class)
    public void saveRuData(RuSaveData ruSaveData){
        SignRu ru = ruSaveData.getRu();
        boolean saveRu = ruService.updateById(ru);
        if(!saveRu){
            throw new PaasException("保存主数据失败");
        }
        ruSaveData.setRuId(ru.getId());
        String ruId = ru.getId();
        //签署人列表
        addSignerList(ruSaveData.getAddSignerList(),ruId);
        updateSignerList(ruSaveData.getUpdateSignerList(), ruId);
        deleteSignerList(ruSaveData.getDeleteSignerList(), ruId);
        //文件列表
        addDocList(ruSaveData,ruId);
        deleteDocList(ruSaveData,ruId);
        //更新文件顺序
        updateDocOrderList(ruSaveData);
        //控件列表
        deleteControlList(ruSaveData);
        //抄送人列表
        saveCcerList(ruSaveData,ruId);
        //附件列表
        saveOtherFileList(ruSaveData,ruId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveControlList(RuCreateData ruCreateData){
        if(ruCreateData.getRuSignControlList() != null && ruCreateData.getRuSignControlList().size() > 0){
            List<SignRuDocControl> ruSignControlList = ruCreateData.getRuSignControlList();
            Date now = new Date();
            for(SignRuDocControl ruDocControl : ruSignControlList){
                ruDocControl.setSignRuId(ruCreateData.getRuId());
                ruDocControl.setCreateTime(now);
            }
            ruDocControlService.saveBatch(ruSignControlList);
        }
        if(ruCreateData.getRuSignControlPropertyList() != null && ruCreateData.getRuSignControlPropertyList().size() > 0){
            List<SignRuDocControlProperty> ruSignControlPropertyList = ruCreateData.getRuSignControlPropertyList();
            for(SignRuDocControlProperty ruDocControlProperty : ruSignControlPropertyList){
                ruDocControlProperty.setRuId(ruCreateData.getRuId());
            }
            ruDocControlPropertyService.saveBatch(ruSignControlPropertyList);
        }
        if(ruCreateData.getRuKeywordList() != null && ruCreateData.getRuKeywordList().size() > 0){
            List<SignRuKeyword> ruKeywordList = ruCreateData.getRuKeywordList();
            for(SignRuKeyword signRuKeyword : ruKeywordList){
                signRuKeyword.setRuId(ruCreateData.getRuId());
            }
            ruKeywordService.saveBatch(ruKeywordList);
        }
        if(ruCreateData.getRuKeywordPropertyList() != null && ruCreateData.getRuKeywordPropertyList().size() > 0){
            List<SignRuKeywordProperty> ruKeywordPropertyList = ruCreateData.getRuKeywordPropertyList();
            for(SignRuKeywordProperty signRuKeywordProperty : ruKeywordPropertyList){
                signRuKeywordProperty.setRuId(ruCreateData.getRuId());
            }
            ruKeywordPropertyService.saveBatch(ruKeywordPropertyList);
        }
    }

    public void deleteControlList(RuSaveData ruSaveData){
        List<String> deleteControlIdList = ruSaveData.getDeleteControlIdList();
        if(deleteControlIdList.size() > 0){
            for(String controlId : deleteControlIdList){
                ruDocControlService.deleteById(controlId);
                ruDocControlPropertyService.deleteByControlId(controlId);
            }
        }
        List<String> deleteControlPropertyIdList = ruSaveData.getDeleteControlPropertyIdList();
        if(deleteControlPropertyIdList.size() > 0){
            for(String deleteControlPropertyId : deleteControlPropertyIdList){
                ruDocControlPropertyService.deleteById(deleteControlPropertyId);
            }
        }
        List<String> deleteControlPropertyByControlIdList = ruSaveData.getDeleteControlPropertyByControlIdList();
        if(deleteControlPropertyByControlIdList.size() > 0){
            for(String deleteControlPropertyByControlId : deleteControlPropertyByControlIdList){
                ruDocControlPropertyService.deleteByControlId(deleteControlPropertyByControlId);
            }
        }
    }

    public void addDocList(RuSaveData ruSaveData,String ruId){
        List<RuDataDoc> addDocList = ruSaveData.getAddDocList();
        if(addDocList.size() > 0){
            for(RuDataDoc dataDoc : addDocList){
                SignRuDoc ruDoc = dataDoc.getRuDoc();
                ruDoc.setSignRuId(ruId);
                boolean saveRuDoc = ruDocService.save(ruDoc);
                if(!saveRuDoc){
                    //异常处理
                    throw new PaasException("保存签约文件失败");
                }
                SignRuDocOperate ruDocOperate = dataDoc.getRuDocOperate();
                ruDocOperate.setSignRuId(ruId);
                ruDocOperate.setDocId(ruDoc.getId());
                boolean saveDocOperate = ruDocOperateService.save(ruDocOperate);
                if(!saveDocOperate){
                    //处理异常
                    throw new PaasException("保存模板文件失败");
                }

                List<RuDataControl> writeControlList = dataDoc.getWriteControlList();
                if(writeControlList.size() > 0){
                    List<SignRuDocControl> writeList = new ArrayList<>();
                    for(RuDataControl writeControl : writeControlList){

                        SignRuDocControl ruDocControl = writeControl.getRuDocControl();

                        ruDocControl.setSignRuId(ruId);
                        ruDocControl.setSignRuDocId(ruDoc.getId());
                        writeList.add(ruDocControl);
                    }
                    if(writeList.size() > 0){
                        boolean b = ruDocControlService.saveBatch(writeList);
                        if(!b){
                            throw new PaasException("保存模板控件失败");
                        }
                    }
                }
            }
        }
    }


    public void deleteDocList(RuSaveData ruSaveData,String ruId){
        List<String> deleteDocIdList = ruSaveData.getDeleteDocIdList();
        if(deleteDocIdList.size() > 0){
            //签约文件
            ruDocService.deleteByParam(deleteDocIdList,ruId);
            //签约操作文件记录
            ruDocOperateService.deleteByParam(deleteDocIdList,ruId);
            //如果是模板，可能存在填写控件，这一步用来删除填写控件
            ruDocControlService.deleteByParam(deleteDocIdList,ruId);
        }

    }

    public void updateDocOrderList(RuSaveData ruSaveData){
        if(ruSaveData.getUpdateDocOrderList().size() > 0) {
            for(SignRuDoc doc : ruSaveData.getUpdateDocOrderList()){
                ruDocService.updateById(doc);
            }
        }
    }


    public void addSignerList(List<RuSaveDataSigner> addSignerList,String ruId){
        if(addSignerList.size() > 0){
            for(RuSaveDataSigner dataSigner : addSignerList){
                SignRuSigner ruSigner = dataSigner.getRuSigner();
                ruSigner.setSignRuId(ruId);
                //保存签署人
                boolean saveSigner = signerService.saveOrUpdate(ruSigner);
                if(!saveSigner){
                    //异常处理
                    throw new PaasException("保存签署人" + ruSigner.getSignerName() + "失败");
                }
                if(SignerTypeEnum.SENDER.getCode().equals(dataSigner.getSignerType())){
                    addSenderList(dataSigner.getAddSenderList(),ruId,SignerTypeEnum.SENDER.getCode(),ruSigner.getId());
                }else if(SignerTypeEnum.RECEIVER_ENT.getCode().equals(dataSigner.getSignerType())){
                    addSenderList(dataSigner.getAddSenderList(),ruId,SignerTypeEnum.RECEIVER_ENT.getCode(),ruSigner.getId());
                }else {
                    //签署校验意愿
                    ruSignConfirmService.save(ruSigner.getId(),ruId,SignerTypeEnum.RECEIVER_PERSONAL.getCode(),dataSigner.getAgreeSkipWillingness(),dataSigner.getVerifyType(),dataSigner.getPersonalSignAuth());
//                    if(dataSigner.getConfirmType() != null && dataSigner.getConfirmType() == 1){
//                        ruSignConfirmService.save(ruSigner.getId(),ruId,SignerTypeEnum.RECEIVER_PERSONAL.getCode());
//                    }
                }
            }
        }
    }

    public void updateSignerList(List<RuSaveDataSigner> updateSignerList,String ruId){
        if(updateSignerList.size() > 0){
            for(RuSaveDataSigner dataSigner : updateSignerList){
                SignRuSigner ruSigner = dataSigner.getRuSigner();
                ruSigner.setSignRuId(ruId);
                boolean b = signerService.updateById(ruSigner);
                if(!b){
                    throw new PaasException("保存签署人" + ruSigner.getSignerName() + "失败");
                }
                if(SignerTypeEnum.SENDER.getCode().equals(dataSigner.getSignerType())){
                    //新增
                    addSenderList(dataSigner.getAddSenderList(),ruId,SignerTypeEnum.SENDER.getCode(),ruSigner.getId());
                    //更新
                    updateSenderList(dataSigner.getUpdateSenderList(),ruId,SignerTypeEnum.SENDER.getCode());
                    //删除
                    deleteSenderList(dataSigner.getDeleteSenderList(),ruId);
                }
                else if(SignerTypeEnum.RECEIVER_ENT.getCode().equals(dataSigner.getSignerType())){
                    //新增
                    addSenderList(dataSigner.getAddSenderList(),ruId,SignerTypeEnum.RECEIVER_ENT.getCode(),ruSigner.getId());
                    //更新
                    updateSenderList(dataSigner.getUpdateSenderList(),ruId,SignerTypeEnum.RECEIVER_ENT.getCode());
                    //删除
                    deleteSenderList(dataSigner.getDeleteSenderList(),ruId);
                }
                else {
                    //签署校验意愿
                    updateConfirm(ruSigner.getId(),ruId,SignerTypeEnum.RECEIVER_PERSONAL.getCode(),dataSigner.getConfirmType(),dataSigner.getAgreeSkipWillingness(),dataSigner.getVerifyType(),dataSigner.getPersonalSignAuth());
//                    if(dataSigner.getConfirmType() != null && dataSigner.getConfirmType() == 1){
//                        //更新个人签署意愿
//                        updateConfirm(ruSigner.getId(),ruId,SignerTypeEnum.RECEIVER_PERSONAL.getCode(),dataSigner.getConfirmType());
//                    }
                }
            }
        }
    }

    public void deleteSignerList(List<RuSaveDataSigner> deleteSignerList,String ruId){
        if(deleteSignerList.size() > 0){
            for(RuSaveDataSigner dataSigner : deleteSignerList){

                SignRuSigner ruSigner = dataSigner.getRuSigner();
                //删除签署人
                boolean b = signerService.updateById(ruSigner);
                if(!b){
                    throw new PaasException("删除签署人失败");
                }
                //清空接收人和签署人的填写控件关联关系
                ruDocControlService.resetWriteControlList(ruSigner.getId(),ruSigner.getSignerType(),ruId);
                if(SignerTypeEnum.SENDER.getCode().equals(ruSigner.getSignerType()) || SignerTypeEnum.RECEIVER_ENT.getCode().equals(ruSigner.getSignerType())){

                    deleteSenderList(dataSigner.getDeleteSenderList(),ruId);
                }else {
//                    //删除接收人的签署控件
//                    ruDocControlService.deleteSignControlList(ruSigner.getId(),ruId);
                    //删除个人签署意愿
                    ruSignConfirmService.delete(ruSigner.getId(),ruId);
                }


            }
        }
    }

    public void addSenderList(List<RuSaveDataSender> addSenderList,String ruId,Integer signerType,String signerId){
        if(addSenderList.size() > 0){
            for(RuSaveDataSender dataSender : addSenderList){
                SignRuSender sender = dataSender.getSender();
                sender.setSignerId(signerId);
                boolean b = senderService.saveOrUpdate(sender);
                if(!b){
                    throw new PaasException("保存发起方" + sender.getSenderName() + "失败");
                }
                ruSignConfirmService.save(sender.getId(),ruId,signerType,dataSender.getAgreeSkipWillingness(),dataSender.getVerifyType(),dataSender.getPersonalSignAuth());
//                if(dataSender.getConfirmType() != null && dataSender.getConfirmType() == 1){
//                    ruSignConfirmService.save(sender.getId(),ruId,signerType);
//                }
            }
        }
    }

    public void updateSenderList(List<RuSaveDataSender> updateSenderList,String ruId,Integer signerType){
        if(updateSenderList.size() > 0){
            for(RuSaveDataSender dataSender : updateSenderList){
                SignRuSender sender = dataSender.getSender();
                boolean updateSender = senderService.saveOrUpdate(sender);
                if(!updateSender){
                    throw new PaasException("保存企业接收方" + sender.getSenderName() + "失败");
                }
                //更新签署意愿
                updateConfirm(sender.getId(),ruId,signerType,dataSender.getConfirmType(),dataSender.getAgreeSkipWillingness(),dataSender.getVerifyType(),dataSender.getPersonalSignAuth());
            }
        }
    }

    public void deleteSenderList(List<RuSaveDataSender> deleteSenderList,String ruId){
        if(deleteSenderList.size() > 0){
            for(RuSaveDataSender dataSender : deleteSenderList){
                SignRuSender sender = dataSender.getSender();
                sender.setDeleteFlag(true);
//                //删除发起人的签署控件
//                ruDocControlService.deleteSignControlList(sender.getId(),ruId);
                //删除签署意愿
                ruSignConfirmService.delete(sender.getId(),ruId);
                //删除所有发起人
                senderService.updateById(sender);
            }
        }
    }

    public void saveCcerList(RuSaveData ruSaveData,String ruId){
        List<SignRuCcer> ccerList = ruSaveData.getCcerList();
        if(ccerList.size() > 0){
            //先删除
            ccerService.deleteByRuId(ruId);
            for(SignRuCcer ccer : ccerList){
                ccer.setSignRuId(ruId);
                ccerService.save(ccer);
            }
        }
    }

    public void saveOtherFileList(RuSaveData ruSaveData,String ruId){
        List<String> otherFileList = ruSaveData.getOtherFileList();
        if(otherFileList.size() > 0){
            signFileService.deleteByFatherIdAndDataCategory(SignFileEnum.SIGN_FILE_OTHER,ruId);
            for(String annexId : otherFileList){
                signFileService.updateAnnexStorage(SignFileEnum.SIGN_FILE_OTHER,ruId,annexId);
            }
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void saveStartData(List<SignRuOperator> operatorList, List<SignRuRelation> relationList , SignRu ru){

        boolean b = ruService.updateById(ru);
        if(!b){
            throw new PaasException("发起失败-单号生成失败");
        }
        //先删除之前的操作人
        ruOperatorService.deleteByRuId(ru.getId());
        //插入
        boolean saveBatchOperatorList = ruOperatorService.saveBatch(operatorList);
        if(!saveBatchOperatorList){
            throw new PaasException("操作人保存失败");
        }
        //抄送人
        if(relationList.size() > 0){
            boolean saveBatchRelationList = relationService.saveBatch(relationList);
            if(!saveBatchRelationList){
                throw new PaasException("关联人保存失败");
            }
        }
    }





    @Transactional(rollbackFor = Exception.class)
    public void saveOperateData(List<RuOperateData> operateDataList,SignRuOperateRecord operateRecord){
        String operateRecordId = null ;
        if(operateRecord != null){
            boolean save = ruOperateRecordService.save(operateRecord);
            if(save){
                operateRecordId = operateRecord.getId();
            }
        }

        for(RuOperateData operateData : operateDataList){

            SignRuDocOperate ruDocOperate = operateData.getOldRuDocOperate();
            SignRuDocOperate newRuDocOperate = operateData.getNewRuDocOperate();
            String newAnnexId = operateData.getNewAnnexId();
            byte[] newDocFileByte = operateData.getNewDocFileByte();

            boolean saveOperate = ruDocOperateService.save(newRuDocOperate);
            if(!saveOperate){
                throw new PaasException("业务线实例-保存签约文件失败");
            }
            //将原有签约文件操作置为非最新
            ruDocOperate.setIsCurrent(SignCurrentEnum.NOT_CURRENT.getCode());
            boolean updateOperate = ruDocOperateService.updateById(ruDocOperate);
            if(!updateOperate){
                throw new PaasException("业务线实例-保存签约文件失败");
            }
            if(operateRecordId != null){
                SignRuOperateDocRecord operateDocRecord = new SignRuOperateDocRecord();
                operateDocRecord.setOperateRecordId(operateRecordId);
                operateDocRecord.setDocId(newRuDocOperate.getDocId());
                operateDocRecord.setPreviousDocOperateId(ruDocOperate.getId());
                operateDocRecord.setPreviousDocOperateAnnexId(ruDocOperate.getAnnexId());
                operateDocRecord.setCurrentDocOperateId(newRuDocOperate.getId());
                operateDocRecord.setCurrentDocOperateAnnexId(newRuDocOperate.getAnnexId());
                ruOperateDocRecordService.save(operateDocRecord);
            }
            //转图片并且保存图片数据
            threadPoolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    signFileService.saveAndConvertImage(newAnnexId,newDocFileByte);
                }
            });

        }


    }


    public void updateConfirm(String signerId,String signRuId,Integer signerType,Integer confirmType,Integer isFastSign,String verifyType,String personalSignAuth){
//        if(confirmType != null && confirmType == 1){
//            SignRuSignConfirm ruSignConfirm = ruSignConfirmService.getByParam(signerId, signRuId);
//            if(ruSignConfirm == null){
//                ruSignConfirmService.save(signerId,signRuId,signerType,isFastSign,verifyType);
//            }
//        }else {
//            ruSignConfirmService.delete(signerId,signRuId);
//        }
        SignRuSignConfirm ruSignConfirm = ruSignConfirmService.getByParam(signerId, signRuId);
        if(ruSignConfirm == null){
            ruSignConfirmService.save(signerId,signRuId,signerType,isFastSign,verifyType,personalSignAuth);
        }else{
            ruSignConfirm.setConfirmType(verifyType);
            ruSignConfirm.setAgreeSkipWillingness(isFastSign);
            ruSignConfirm.setPersonalSignAuth(personalSignAuth);
            ruSignConfirmService.updateById(ruSignConfirm);
        }
    }




}