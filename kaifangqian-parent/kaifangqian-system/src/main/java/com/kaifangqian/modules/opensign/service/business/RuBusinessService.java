/**
 * @description 签署详细数据服务
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
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaifangqian.common.constant.ApiCode;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.external.sign.request.SignOrderRequest;
import com.kaifangqian.external.sign.response.SignAppInfoResponse;
import com.kaifangqian.external.sign.response.SignServiceOpenInfoResponse;
import com.kaifangqian.external.sign.service.SignServiceExternal;
import com.kaifangqian.external.sign.service.SignServiceManageExternal;
import com.kaifangqian.modules.api.exception.RequestParamsException;
import com.kaifangqian.modules.cert.enums.CertHolderTypeEnum;
import com.kaifangqian.modules.cert.enums.CertTypeEnum;
import com.kaifangqian.modules.cert.service.CertBusinessService;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.pdfbox.PdfboxService;
import com.kaifangqian.modules.opensign.pdfbox.vo.CertificateInfo;
import com.kaifangqian.modules.opensign.service.business.vo.CompletedSignVo;
import com.kaifangqian.modules.opensign.service.business.vo.ControlQueryVo;
import com.kaifangqian.modules.opensign.service.business.vo.RuOperateData;
import com.kaifangqian.modules.opensign.service.business.vo.YundunSignPositionArrayData;
import com.kaifangqian.modules.opensign.service.re.*;
import com.kaifangqian.modules.opensign.service.ru.*;
import com.kaifangqian.modules.opensign.service.template.SignTemplateControlService;
import com.kaifangqian.modules.opensign.service.template.SignTemplateImageRecordService;
import com.kaifangqian.modules.opensign.service.template.SignTemplateRecordService;
import com.kaifangqian.modules.opensign.sign.PdfSignService;
import com.kaifangqian.modules.opensign.vo.base.sign.*;
import com.kaifangqian.modules.opensign.vo.response.ru.SignNodeConfigResponse;
import com.kaifangqian.modules.system.entity.SysTenantInfo;
import com.kaifangqian.modules.system.entity.SysTenantUser;
import com.kaifangqian.modules.system.enums.TenantStatus;
import com.kaifangqian.modules.system.enums.TenantType;
import com.kaifangqian.common.base.entity.BaseEntity;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.config.FileProperties;
import com.kaifangqian.entity.SysConfig;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.opensign.dto.SignTaskInfo;
import com.kaifangqian.modules.opensign.dto.SignTaskThreadlocalVO;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.service.annex.AnnexImageService;
import com.kaifangqian.modules.opensign.service.business.vo.*;
import com.kaifangqian.modules.opensign.service.re.*;
import com.kaifangqian.modules.opensign.service.ru.*;
import com.kaifangqian.modules.opensign.service.seal.SignEntSealService;
import com.kaifangqian.modules.opensign.service.tool.DateSealGenerateService;
import com.kaifangqian.modules.opensign.service.tool.SignFileService;
import com.kaifangqian.modules.opensign.service.tool.pojo.CertificateProperty;
import com.kaifangqian.modules.opensign.service.tool.pojo.ControlWidgetProperty;
import com.kaifangqian.modules.opensign.service.tool.pojo.RealPositionProperty;
import com.kaifangqian.modules.opensign.service.tool.pojo.SelectKeywords;
import com.kaifangqian.modules.opensign.utils.Base64;
import com.kaifangqian.modules.opensign.vo.base.sign.*;
import com.kaifangqian.modules.opensign.vo.request.ru.RunSubmitSignRequest;
import com.kaifangqian.modules.storage.entity.AnnexStorage;
import com.kaifangqian.modules.system.service.ISysTenantUserService;
import com.kaifangqian.service.ISysConfigService;
import com.kaifangqian.utils.MyStringUtils;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.mvel2.ast.Sign;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: RuBusinessService
 * @Package: com.kaifangqian.modules.opensign.service.business
 * @ClassName: RuBusinessService
 * @author: FengLai_Gong
 */
@Service
public class RuBusinessService {

    @Autowired
    private FileProperties fileProperties;

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
    private PdfSignService pdfSignService;

    @Autowired
    private SignRuTaskService ruTaskService ;
    @Autowired
    private SignRuSignTemporaryService ruSignTemporaryService ;

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private RuCallbackService ruCallbackService ;

    @Autowired
    private RuDataService ruDataService ;

    @Autowired
    private SignServiceExternal signServiceExternal ;

    @Autowired
    private SignServiceManageExternal signServiceManageExternal ;

    @Value("${service.app-id}")
    private String appId ;

    /**
     * @Description #全部详情数据
     * @Param [ruId]
     * @return com.kaifangqian.modules.opensign.vo.base.sign.BaseVo
     **/
    public BaseVo info(String ruId){
        //主数据
        SignRu signRu = ruService.getById(ruId);
        if(signRu == null) {
            return null ;
        }
        BaseVo vo = new BaseVo();
        buildBase(signRu,vo);
        //签署人
        vo.setSignerList(getSignerList(ruId));
        //签约文件
        vo.setFileList(getDocList(ruId));
        //抄送人
        vo.setCcerList(getCcerList(ruId));
        //附件列表
        vo.setOtherFileList(getOtherFileList(ruId));

        SysTenantUser startUser = tenantUserService.getById(signRu.getSysUserId());
        if(startUser != null){
            vo.setStartUserId(startUser.getId());
            vo.setStartUserName(startUser.getNickName());
        }


        return vo ;
    }

    public List<DocOtherFileVo> getOtherFileList(String ruId){
        List<DocOtherFileVo> otherFileList = new ArrayList<>();
        List<AnnexStorage> annexStorageList = signFileService.findByFatherIdAndDataCategoryList(SignFileEnum.SIGN_FILE_OTHER, ruId);
        if(annexStorageList != null && annexStorageList.size() > 0){
            for(AnnexStorage annexStorage : annexStorageList){
                DocOtherFileVo vo = new DocOtherFileVo();
                vo.setRealName(annexStorage.getRealName());
                vo.setAnnexId(annexStorage.getId());
                vo.setId(annexStorage.getId());
                otherFileList.add(vo);
            }
        }
        return otherFileList ;
    }

    /**
     * @Description #基础数据
     * @Param [signRe, vo]
     * @return void
     **/
    public void buildBase(SignRu signRu,BaseVo vo){
        DocBaseVo baseVo = new DocBaseVo();
        BeanUtils.copyProperties(signRu,baseVo);
        baseVo.setSignRuId(signRu.getId());
        baseVo.setCreateDate(signRu.getCreateTime());
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat secondFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(signRu.getExpireDate() != null){
            String expireDateString = dayFormat.format(signRu.getExpireDate());
            baseVo.setExpireDateString(expireDateString);
        }
        if(signRu.getStartTime() != null){
            String startDateString = secondFormat.format(signRu.getStartTime());
            baseVo.setStartDateString(startDateString);
        }
        if(signRu.getFinishTime() != null){
            String finishDateString = secondFormat.format(signRu.getFinishTime());
            baseVo.setFinishDateString(finishDateString);
        }
        if(signRu.getSignReId() != null && signRu.getSignReId().length() > 0){
            SignRe re = reService.getById(signRu.getSignReId());
            if(re != null){
                baseVo.setName(re.getName());
            }
        }
        Boolean downloadFlag = ruService.checkDownloadAuth(signRu.getId());
        if(downloadFlag){
            baseVo.setDownloadFileType(1);
        }else {
            baseVo.setDownloadFileType(2);
        }
        SignRe re = reService.getById(signRu.getSignReId());
        if(re != null){
            baseVo.setSubjectType(re.getSubjectType());
            baseVo.setCodeType(re.getCodeType());
        }
        vo.setBaseVo(baseVo);

//        List<DocApproveVo> approveVoList = new ArrayList<>();
//        vo.setApproveList(approveVoList);
    }

    /**
     * @Description #签约文件列表
     * @Param [signReId, vo]
     * @return void
     **/
    public List<DocFileVo> getDocList(String signRuId){
        List<DocFileVo> fileVoList = new ArrayList<>();
        List<SignRuDoc> docList = ruDocService.listByRuId(signRuId);
        if(docList != null && docList.size() > 0){
            for(SignRuDoc doc : docList){
                DocFileVo fileVo = new DocFileVo();

                BeanUtils.copyProperties(doc,fileVo);
                //文件名称
                fileVo.setName(doc.getDocName());

                List<SignRuDocOperate> signRuDocOperates = ruDocOperateService.listByParamCurrent(signRuId, doc.getId());
                if(signRuDocOperates != null && signRuDocOperates.size() > 0){
                    SignRuDocOperate signRuDocOperate = signRuDocOperates.get(0);
                    fileVo.setAnnexId(signRuDocOperate.getAnnexId());
                }
                fileVo.setDocPage(doc.getDocPage());
                if(doc.getDocOrder() != null){
                    fileVo.setDocOrder(doc.getDocOrder());
                }else {
                    fileVo.setDocOrder(0);
                }
                fileVoList.add(fileVo);
            }
            fileVoList = fileVoList.stream().sorted(Comparator.comparing(DocFileVo::getDocOrder)).collect(Collectors.toList());
        }
        return fileVoList ;
    }

    /**
     * @Description #图片列表
     * @Param [docFileId]
     * @return java.util.List<com.kaifangqian.modules.opensign.vo.base.sign.DocImageVo>
     **/
    public List<DocImageVo> getImageList(String docFileId) {
        List<DocImageVo> imageList = new ArrayList<>();

        SignRuDocOperate currentDocOperate = ruDocOperateService.getCurrentByDocId(docFileId);
        if(currentDocOperate == null){
            return imageList ;
        }

        List<AnnexImage> annexImageList = annexImageService.listByAnnexId(currentDocOperate.getAnnexId());
        if(annexImageList != null && annexImageList.size() > 0){
            for(AnnexImage image : annexImageList){
                DocImageVo imageVo = new DocImageVo();
                imageVo.setPage(image.getPage());
                imageVo.setAnnexId(image.getImageAnnexId());
                imageVo.setId(image.getId());
                imageVo.setImageWidth(image.getImageWidth());
                imageVo.setImageHeight(image.getImageHeight());
                //设置默认值
                if(imageVo.getImageWidth() == null){
                    imageVo.setImageWidth("595.3");
                }
                if(imageVo.getImageHeight() == null){
                    imageVo.setImageHeight("841.9");
                }
                imageList.add(imageVo);
            }
        }

        return imageList;
    }


    /**
     * @Description #签署人
     * @Param [signReId, vo]
     * @return void
     **/
    public List<DocSignerVo> getSignerList(String signRuId){
        List<DocSignerVo> signerVoList = new ArrayList<>();
        List<SignRuSigner> signerList = signerService.listByRuId(signRuId);

        if(signerList != null && signerList.size() > 0){
            //获取操作人
            List<SignRuOperator> operatorList = ruOperatorService.listByRuId(signRuId);
            Map<String, List<SignRuOperator>> operatorMap = null ;
            if(operatorList != null && operatorList.size() > 0){
                operatorMap = operatorList.stream().collect(Collectors.groupingBy(SignRuOperator::getSignerId));
            }
            List<SignRuSignConfirm> signConfirmList = ruSignConfirmService.listByParam(signRuId);
            List<String> confirmSignerIdList = new ArrayList<>();
            if(signConfirmList != null && signConfirmList.size() > 0){
                List<String> collect = signConfirmList.stream().map(SignRuSignConfirm::getSignerId).collect(Collectors.toList());
                if(collect != null){
                    confirmSignerIdList.addAll(collect);
                }
            }
            for(SignRuSigner signer : signerList){
                DocSignerVo signerVo = new DocSignerVo();
                BeanUtils.copyProperties(signer,signerVo);
                if(signer.getSignerUserId() != null && signer.getSignerUserId().length() > 0){
                    SysTenantUser tenantUser = tenantUserService.getById(signer.getSignerUserId());
                    if(tenantUser != null){
                        signerVo.setSingerUserId(signer.getSignerUserId());
                        signerVo.setSingerUserName(tenantUser.getNickName());
                    }
                }
                List<DocSenderVo> senderVoList = new ArrayList<>();
                if(SignerTypeEnum.SENDER.getCode().equals(signer.getSignerType()) || SignerTypeEnum.RECEIVER_ENT.getCode().equals(signer.getSignerType())){
                    //发起人
                    List<SignRuSender> senderList = senderService.listBySignerId(signer.getId());
                    if(senderList != null && senderList.size() > 0){
                        for(SignRuSender sender : senderList){
                            DocSenderVo senderVo = new DocSenderVo();
                            BeanUtils.copyProperties(sender,senderVo);
                            //发起方名称
                            if(sender.getSenderUserId() != null && sender.getSenderUserId().length() > 0){
                                SysTenantUser tenantUser = tenantUserService.getById(sender.getSenderUserId());
                                if(tenantUser != null){
                                    senderVo.setSenderUserId(sender.getSenderUserId());
                                    senderVo.setSenderUserName(tenantUser.getNickName());
                                }
                            }
                            //指定的签章名称
                            if(sender.getSenderSealId() != null && sender.getSenderSealId().length() > 0){
                                SignEntSeal entSeal = entSealService.getById(sender.getSenderSealId());
                                if(entSeal != null){
                                    senderVo.setSenderSealId(sender.getSenderSealId());
                                    senderVo.setSenderSealName(entSeal.getSealName());
                                }
                            }
                            if(operatorMap != null){
                                List<SignRuOperator> operatorSenderList = operatorMap.get(sender.getId());
                                if(operatorSenderList != null && operatorSenderList.size() > 0){
                                    for(SignRuOperator operator : operatorSenderList){
                                        if(operator.getOperateType().equals(OperateTypeEnum.WRITE.getCode())){
                                            senderVo.setWriteStatus(operator.getOperateStatus());
                                        }else if(operator.getOperateType().equals(OperateTypeEnum.SIGN.getCode())){
                                            senderVo.setSignStatus(operator.getOperateStatus());
                                        }else if(operator.getOperateType().equals(OperateTypeEnum.APPROVE.getCode())){
                                            senderVo.setSignStatus(operator.getOperateStatus());
                                        }
                                    }
                                }
                            }

                            SignRuSignConfirm signRuSignConfirm = ruSignConfirmService.getByParam(sender.getId(),signRuId);
                            if(signRuSignConfirm != null){
                                senderVo.setAgreeSkipWillingness(signRuSignConfirm.getAgreeSkipWillingness());
                                senderVo.setVerifyType(signRuSignConfirm.getConfirmType());
                                senderVo.setPersonalSignAuth(signRuSignConfirm.getPersonalSignAuth());
                                senderVo.setSealType(signRuSignConfirm.getSealType());
                            }

//                            if(confirmSignerIdList.contains(sender.getId())){
//                                senderVo.setConfirmType(1);
//                            }
                            senderVoList.add(senderVo);
                        }
                    }
                }else {
                    SignRuSignConfirm signRuSignConfirm = ruSignConfirmService.getByParam(signer.getId(),signRuId);
                    if(signRuSignConfirm != null){
                        signerVo.setAgreeSkipWillingness(signRuSignConfirm.getAgreeSkipWillingness());
                        signerVo.setVerifyType(signRuSignConfirm.getConfirmType());
                        signerVo.setPersonalSignAuth(signRuSignConfirm.getPersonalSignAuth());
                        signerVo.setSealType(signRuSignConfirm.getSealType());
                    }

//                    if(confirmSignerIdList.contains(signer.getId())){
//                        signerVo.setConfirmType(1);
//                    }
                }
                signerVo.setSenderList(senderVoList);
                if(operatorMap != null){
                    List<SignRuOperator> operatorSignerList = operatorMap.get(signer.getId());
                    if(operatorSignerList != null){
                        for(SignRuOperator operator : operatorSignerList){
                            if(operator.getOperateType().equals(OperateTypeEnum.WRITE.getCode())){
                                signerVo.setWriteStatus(operator.getOperateStatus());
                            }else if(operator.getOperateType().equals(OperateTypeEnum.SIGN.getCode())){
                                signerVo.setSignStatus(operator.getOperateStatus());
                            }else if(operator.getOperateType().equals(OperateTypeEnum.APPROVE.getCode())){
                                signerVo.setSignStatus(operator.getOperateStatus());
                            }
                        }
                    }
                }

                signerVoList.add(signerVo);
            }
        }

        return signerVoList ;
    }



    /**
     * @Description #权限
     * @Param [signReId, vo]
     * @return void
     **/
    public List<DocOperatorVo> getOperatorList(String signReId){
        List<DocOperatorVo> operatorVoList = new ArrayList<>();
        List<SignRuOperator> operatorList = ruOperatorService.listByRuId(signReId);
        if(operatorList != null && operatorList.size() > 0){
            for(SignRuOperator operator : operatorList){
                DocOperatorVo vo = new DocOperatorVo();
                BeanUtils.copyProperties(operator,vo);
                operatorVoList.add(vo);
            }
        }
        return operatorVoList ;
    }

    /**
     * @Description #抄送人
     * @Param [signReId, vo]
     * @return void
     **/
    public List<DocCcerVo> getCcerList(String signReId){
        List<DocCcerVo> ccerVoList = new ArrayList<>();
        List<SignRuCcer> ccerList = ccerService.listByRuId(signReId);
        if(ccerList != null && ccerList.size() > 0){

            for(SignRuCcer ccer : ccerList){
                DocCcerVo ccerVo = new DocCcerVo();
                BeanUtils.copyProperties(ccer,ccerVo);
                if(ccer.getTenantUserId() != null && ccer.getTenantUserId().length() > 0){
                    SysTenantUser tenantUser = tenantUserService.getById(ccer.getTenantUserId());
                    if(tenantUser != null){
                        ccerVo.setInternalCcerId(tenantUser.getId());
                        ccerVo.setInternalCcerName(tenantUser.getNickName());
                    }
                }

                ccerVoList.add(ccerVo);
            }

        }
        return ccerVoList ;
    }

    /**
     * @Description #填充控件签署人类型
     * @Param [controlList, ruId]
     * @return void
     **/
    public void fillControlSignerType(List<DocControlVo> controlList ,String ruId) {
        List<DocControlVo> needToFillList = null;
        //先判断是否需要填充
        for (DocControlVo controlVo : controlList) {
            if (controlVo.getSignerType() == null) {
                if (needToFillList == null) {
                    needToFillList = new ArrayList<>();
                }
                needToFillList.add(controlVo);
            }
        }
        //需要填充
        if (needToFillList != null && needToFillList.size() > 0) {
            //判断是否有签署人
            List<SignRuSigner> signerList = signerService.listByRuId(ruId);
            //如果没有签署人，直接返回
            if (signerList == null || signerList.size() == 0) {
                return;
            }
            //转化成map
            Map<String, Integer> signerMap = signerList.stream().collect(Collectors.toMap(SignRuSigner::getId, SignRuSigner::getSignerType));
            if (signerMap == null || signerMap.size() == 0) {
                return;
            }
            //获取发起方列表，可以没有发起方的
            List<String> senderIdList = null;
            for (SignRuSigner signer : signerList) {
                if (signer.getSignerType().equals(SignerTypeEnum.SENDER.getCode()) || signer.getSignerType().equals(SignerTypeEnum.RECEIVER_ENT.getCode())) {
                    List<SignRuSender> senderList = senderService.listBySignerId(signer.getId());
                    if (senderList != null && senderList.size() > 0) {
                        senderIdList = senderList.stream().map(SignRuSender::getId).collect(Collectors.toList());
                        ;
                    }
                }
            }
            for (DocControlVo controlVo : needToFillList) {
                //如果是填写控件
                if (ControlTypeEnum.getWriteList().contains(controlVo.getControlType())) {
                    Integer signerType = signerMap.get(controlVo.getSignerId());
                    if (signerType == null) {
                        controlVo.setSignerType(signerType);
                    }
                }
                //如果是签署控件
                else if (ControlTypeEnum.getSignList().contains(controlVo.getControlType())) {
                    Integer signerType = signerMap.get(controlVo.getSignerId());

                    if (signerType != null) {
                        controlVo.setSignerType(signerType);
                    } else {
                        if (senderIdList != null && senderIdList.contains(controlVo.getSignerId())) {
                            controlVo.setSignerType(SignerTypeEnum.SENDER.getCode());
                        }
                    }
                }
            }
        }
    }

    /**
     * @Description #自动签署控件校验
     * @Param [signerList, controlList, ruId]
     * @return java.lang.Boolean
     **/
    public Boolean checkAutoSignErrorStatus(List<SignRuSigner> signerList,List<SignRuDocControl> docControlList){
        //校验是否存在自动签署
        Boolean autoSignFlag = false;
        Boolean autoSignErrorStatus = false ;
        List<String> autoSignSenderIdList = new ArrayList<>();
        for (SignRuSigner signer : signerList) {
            if (signer.getSignerType().equals(SignerTypeEnum.SENDER.getCode())) {
                List<SignRuSender> senderList = senderService.listBySignerId(signer.getId());
                if (senderList != null || senderList.size() > 0) {
                    for (SignRuSender sender : senderList) {
                        if (sender.getSenderType().equals(SenderTypeEnum.ENTERPRISE.getCode())) {
                            if (sender.getSenderSignType().equals(SenderSignTypeEnum.AUTO.getCode())) {
                                autoSignFlag = true;
                                autoSignSenderIdList.add(sender.getId());
                            }
                        }
                    }
                }

            }
        }
        //如果存在自动签
        if (autoSignFlag) {
            if(autoSignSenderIdList.size() == 0){
                //如果自动签章的签署人为空
                autoSignErrorStatus = true ;
            }
            //如果没有签署控件
            if (docControlList == null || docControlList.size() == 0) {
                autoSignErrorStatus = true ;
            }
            List<SignRuDocControl> signControlList = new ArrayList<>();
            //遍历所有控件，找出企业签章自动签署控件
            for(SignRuDocControl controlVo : docControlList){
                if(controlVo.getControlType().equals(ControlTypeEnum.SEAL.getName()) ||
                        controlVo.getControlType().equals(ControlTypeEnum.CHOP_STAMP.getName())){
                    signControlList.add(controlVo);
                }
            }
            if(signControlList.size() == 0){
                autoSignErrorStatus = true ;
            }else {
                //获取企业签章自动签署控件中对应的签署人id
                List<String> autoSignSignerIdList = signControlList.stream().map(SignRuDocControl::getSignerId).collect(Collectors.toList());
                //如果签署人id为空则异常
                if(autoSignSignerIdList == null || autoSignSignerIdList.size() == 0){
                    autoSignErrorStatus = true ;
                }
                //如果企业签章自动签署签署人id不包含在企业签章自动签署控件签署人id列表中
                for(String autoSignSignerId : autoSignSenderIdList){
                    if(!autoSignSignerIdList.contains(autoSignSignerId)){
                        autoSignErrorStatus = true ;
                    }
                }
            }

        }
        return autoSignErrorStatus ;
    }

    /**
     * @Description #保存控件接口
     * @Param [controlList, deleteIdList, ruId]
     * @return void
     **/
    @Transactional(rollbackFor = Exception.class)
    public List<SignRuDocControl> saveControlList(List<DocControlVo> controlList ,List<String> deleteIdList,String ruId,String signerId){
        List<SignRuDocControl> docControlList = new ArrayList<>();

        if(controlList != null && controlList.size() > 0){
            for(DocControlVo controlVo : controlList){
                SignRuDocControl control = new SignRuDocControl();
                BeanUtils.copyProperties(controlVo,control);
                control.setSignRuId(ruId);
                control.setDeleteFlag(false);
                if(control.getFormat() == null || control.getFormat().length() == 0){
                    if(control.getControlType().equals(ControlTypeEnum.SIGN_DATE.getName()) || control.getControlType().equals(ControlTypeEnum.DATE.getName())){
                        control.setFormat("yyyy年MM月dd日");
                    }
                }
                if(signerId != null && signerId.length() > 0){
                    if(control.getSignerId() == null || control.getSignerId().length() == 0){
                        control.setSignerId(signerId);
                    }
                }

                boolean b = ruDocControlService.saveOrUpdate(control);
                if(!b){
                    throw new PaasException("控件保存失败");
                }
                if(ControlTypeEnum.getSignList().contains(control.getControlType())){
                    //保存控件属性
                    List<ControlPropertyVo> propertyVoList = controlVo.getPropertyVoList();
                    if(propertyVoList != null && propertyVoList.size() > 0){
                        //先删除控件属性
                        ruDocControlPropertyService.deleteByControlId(control.getId());
                        List<SignRuDocControlProperty> propertyList = new ArrayList<>();
                        for(ControlPropertyVo propertyVo : propertyVoList){
                            SignRuDocControlProperty property = new SignRuDocControlProperty();
                            property.setPropertyValue(propertyVo.getPropertyValue());
                            property.setPropertyType(propertyVo.getPropertyType());
                            property.setControlId(control.getId());
                            property.setRuId(ruId);
                            propertyList.add(property);
                        }
                        //再插入新的控件属性
                        ruDocControlPropertyService.saveBatch(propertyList);
                    }
                }
                docControlList.add(control);
            }

        }

        if(deleteIdList != null && deleteIdList.size() > 0){
            for(String id : deleteIdList){
                SignRuDocControl control = new SignRuDocControl();
                control.setDeleteFlag(true);
                control.setId(id);
                ruDocControlService.updateById(control);
                ruDocControlPropertyService.deleteByControlId(id);
            }
        }
        return docControlList ;
    }

    public void startGenerateSubject(String ruId){

        //单号生成
        SignRu ru = ruService.getById(ruId);
        if(ru == null){
            throw new PaasException("业务线实例不存在");
        }
        //获取签署人列表
        List<SignRuSigner> singerList = signerService.listByRuId(ruId);
        if(singerList == null || singerList.size() == 0){
            throw new PaasException("签署人为空");
        }
        SignRe re = reService.getById(ru.getSignReId());
        if(re == null){
            throw new PaasException("业务线配置不存在");
        }

        if(re.getCodeType() != null && re.getCodeType() == SignReRuleGenerateEnum.RULE.getCode()){
            ru.setCode(reBusinessService.generateCode(re.getId(),singerList));
        }
        if(re.getSubjectType() != null && re.getSubjectType().equals(SignReRuleGenerateEnum.RULE.getCode())){
            //单号生成
            ru.setSubject(reBusinessService.generateSubject(re.getId(),singerList));
        }else {
            if(ru.getSubject() == null || ru.getSubject().length() == 0){
                ru.setSubject(System.currentTimeMillis() + "-" + re.getName());
            }
        }
        boolean b = ruService.updateById(ru);
        if(!b){
            throw new PaasException("发起失败-单号生成失败");
        }

    }


//    @Transactional(rollbackFor = Exception.class)
    public String start(String ruId){
        LoginUser currentUser = MySecurityUtils.getCurrentUser();


        //获取签约文件列表
        List<SignRuDoc> docList = ruDocService.listByRuId(ruId);
        if(docList == null || docList.size() == 0){
            throw new PaasException("签约文件为空");
        }
        //获取所有控件类型列表
        List<String> writeList = ControlTypeEnum.getWriteList();
        Boolean haveWrite = false ;
        //获取控件列表
        ControlQueryVo controlQueryVo = new ControlQueryVo();
        controlQueryVo.setSignRuId(ruId);
        List<SignRuDocControl> controlList = ruDocControlService.listByParam(controlQueryVo);
        if(controlList != null && controlList.size() > 0){

            //校验控件
            for(SignRuDocControl signRuDocControl : controlList){
//                if(signRuDocControl.getSignRuDocId() ==  null || signRuDocControl.getSignRuDocId().length() == 0){
//                    throw new PaasException("控件关联签约文档为空");
//                }
                if(signRuDocControl.getControlType() == null || signRuDocControl.getControlType().length() == 0){
                    throw new PaasException("控件类型为空");
                }
                if(signRuDocControl.getSignerId() == null || signRuDocControl.getSignerId().length() == 0){
                    if(ControlTypeEnum.getWriteList().contains(signRuDocControl.getControlType())){
                        if(signRuDocControl.getIsRequired() != null && signRuDocControl.getIsRequired().equals(ControlIsRequiredEnum.IS.getCode())){
                            throw new PaasException("模板参数填写方未全部设置");
                        }
                    }else {
                        throw new PaasException("签署方未全部设置");
                    }
                }

                if(signRuDocControl.getControlType().equals(ControlTypeEnum.SIGN_DATE.getName())){
                    if(signRuDocControl.getFormat() == null || signRuDocControl.getFormat().length() == 0){
                        throw new PaasException("签署日期控件-时间格式不能为空");
                    }
                    try {
                        SimpleDateFormat signDateFormat = new SimpleDateFormat(signRuDocControl.getFormat());
                        signDateFormat.format(new Date());
                    }catch (Exception e){
                        throw new PaasException("签署日期控件-时间格式错误：" + signRuDocControl.getFormat());
                    }
                }
                if(writeList.contains(signRuDocControl.getControlType())){
                    //如果是填写控件
                    haveWrite = true;
                }

            }
        }
        //主数据
        SignRu ru = ruService.getById(ruId);
        if(ru == null){
            throw new PaasException("业务线实例不存在");
        }
        //获取签署人列表
        List<SignRuSigner> singerList = signerService.listByRuId(ruId);
        if(singerList == null || singerList.size() == 0){
            throw new PaasException("签署人为空");
        }


        //根据发起人、接受人、控件列表计算操作人
        List<SignRuOperator> operatorList = new ArrayList<>();
        //实例关联人
        List<SignRuRelation> relationList = new ArrayList<>();
        for(SignRuSigner signer : singerList){
            if(signer.getSignerType().equals(SignerTypeEnum.SENDER.getCode())){
                for(SignRuDocControl signRuDocControl : controlList){
                    if(signRuDocControl.getSignerId() != null && signRuDocControl.getSignerId().equals(signer.getId())){
                        if(writeList.contains(signRuDocControl.getControlType())){
                            //如果为必填项，则必须有值
                            if(signRuDocControl.getIsRequired() != null && signRuDocControl.getIsRequired().equals(ControlIsRequiredEnum.IS.getCode())){
                                if(signRuDocControl.getValue() == null || signRuDocControl.getValue().length() == 0){
                                    throw new PaasException("文档参数内容未填写完成，请完善");
                                }
                                //如果是时间控件
                                if(signRuDocControl.getControlType().equals(ControlTypeEnum.DATE.getName())){
                                    //验证控件参数
                                    SimpleDateFormat character = new SimpleDateFormat(signRuDocControl.getFormat());
                                    SimpleDateFormat number = new SimpleDateFormat("yyyy-MM-dd");
                                    String value = signRuDocControl.getValue();
                                    try {
                                        Date parse = number.parse(value);
                                        character.format(parse);
                                    }catch (Exception e){
                                        throw new PaasException("时间控件日期解析错误，请填写正确时间格式" + value);
                                    }
                                }
                            }
                        }
                    }
                }
                //获取发起人
                List<SignRuSender> senderList = senderService.listBySignerId(signer.getId());
                if(senderList != null && senderList.size() > 0){
                    for(SignRuSender sender : senderList){
                        if(sender.getSenderType().equals(SenderTypeEnum.ENTERPRISE.getCode())){
                            //必须设定签章
                            if(sender.getSenderSealId() == null || sender.getSenderSealId().length() == 0){
                                throw new PaasException("组织签章未指定签章");
                            }
                            //如果没有指定签署人，则为自动盖章，必须有相关的签章
                            if(sender.getSenderSignType().equals(SenderSignTypeEnum.AUTO.getCode())){

                                Boolean enterpriseControlFlag = true;
                                for(SignRuDocControl signRuDocControl : controlList){
                                    //必须是企业签章
                                    if(signRuDocControl.getSignerId() != null && signRuDocControl.getSignerId().equals(sender.getId())){
                                        if(ControlTypeEnum.SEAL.getName().equals(signRuDocControl.getControlType()) ||
                                                ControlTypeEnum.CHOP_STAMP.getName().equals(signRuDocControl.getControlType())){
                                            enterpriseControlFlag = false ;
                                        }
                                    }
                                }
                                if(enterpriseControlFlag){
                                    throw new PaasException("自动盖章的签署节点未指定签署位置");
                                }

                                try {
                                    SignServiceOpenInfoResponse signServiceOpenInfoResponse = signServiceManageExternal.querySilentInfo();
                                    if(signServiceOpenInfoResponse.getStatus() == 0){
                                        throw new PaasException("发起方企业未开通静默签署授权，或静默授权已到期，导致签署流程异常，请联系发起方企业。");
                                    }
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        //发起方签署操作人
                        SignRuOperator senderSignOperator = new SignRuOperator();
                        BeanUtils.copyProperties(sender,senderSignOperator);
                        senderSignOperator.setOperateName(sender.getSenderName());
                        senderSignOperator.setSignRuId(ruId);
                        senderSignOperator.setOperateOrder(sender.getSenderOrder());
                        senderSignOperator.setSignerType(SignerTypeEnum.SENDER.getCode());
                        senderSignOperator.setSignerId(sender.getId());
                        if (sender.getSenderType().equals(SenderTypeEnum.APPROVER.getCode())){
                            senderSignOperator.setOperateType(OperateTypeEnum.APPROVE.getCode());
                        }else{
                            senderSignOperator.setOperateType(OperateTypeEnum.SIGN.getCode());
                        }
                        senderSignOperator.setOperateUserId(sender.getSenderUserId());
                        senderSignOperator.setDeleteFlag(false);
                        senderSignOperator.setId(null);
                        senderSignOperator.setOperateStatus(OperateStatusEnum.NOT_FINISHED.getCode());
                        operatorList.add(senderSignOperator);
                    }
                }

                //发送人关联人
                SignRuRelation ruRelation = new SignRuRelation();
                ruRelation.setSignRuId(ruId);
                ruRelation.setRelationType(RelationTypeEnum.SENDER.getCode());
                ruRelation.setTenantUserId(currentUser.getTenantUserId());
                ruRelation.setTaskLinkType("system");
                ruRelation.setDeleteFlag(false);
                relationList.add(ruRelation);
            }else if(signer.getSignerType().equals(SignerTypeEnum.RECEIVER_PERSONAL.getCode())){
                //接收方签署操作人
                SignRuOperator signerSignOperator = new SignRuOperator();
                BeanUtils.copyProperties(signer,signerSignOperator);
                signerSignOperator.setOperateType(OperateTypeEnum.SIGN.getCode());
                signerSignOperator.setOperateName(signer.getSignerName());
                signerSignOperator.setSignRuId(ruId);
                signerSignOperator.setOperateOrder(signer.getSignerOrder());
                signerSignOperator.setSignerId(signer.getId());
                signerSignOperator.setSignerType(SignerTypeEnum.RECEIVER_PERSONAL.getCode());
                signerSignOperator.setOperateUserId(signer.getSignerUserId());
                signerSignOperator.setOperateExternalType(signer.getSignerExternalType());
                signerSignOperator.setOperateExternalValue(signer.getSignerExternalValue());
                signerSignOperator.setDeleteFlag(false);
                signerSignOperator.setId(null);
                signerSignOperator.setOperateStatus(OperateStatusEnum.NOT_FINISHED.getCode());
                operatorList.add(signerSignOperator);
            }else if(signer.getSignerType().equals(SignerTypeEnum.RECEIVER_ENT.getCode())){
//                for(SignRuDocControl signRuDocControl : controlList){
//                    if(signRuDocControl.getSignerId().equals(signer.getId())){
//                        if(writeList.contains(signRuDocControl.getControlType())){
//                            //如果为必填项，则必须有值
//                            if(signRuDocControl.getIsRequired() != null && signRuDocControl.getIsRequired().equals(ControlIsRequiredEnum.IS.getCode())){
//                                if(signRuDocControl.getValue() == null || signRuDocControl.getValue().length() == 0){
//                                    throw new PaasException("文档参数内容未填写完成，请完善");
//                                }
//                                //如果是时间控件
//                                if(signRuDocControl.getControlType().equals(ControlTypeEnum.DATE.getName())){
//                                    String value = signRuDocControl.getValue();
//                                    try {
//                                        Date parse = number.parse(value);
//                                        character.format(parse);
//                                    }catch (Exception e){
//                                        throw new PaasException("时间控件日期解析错误，请填写正确时间格式" + value);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
                //获取企业接收方
                List<SignRuSender> senderList = senderService.listBySignerId(signer.getId());
                if(senderList != null && senderList.size() > 0){
                    for(SignRuSender sender : senderList){
                        //发起方签署操作人
                        SignRuOperator senderSignOperator = new SignRuOperator();
                        BeanUtils.copyProperties(sender,senderSignOperator);
                        senderSignOperator.setOperateName(sender.getSenderName());
                        senderSignOperator.setSignRuId(ruId);
                        senderSignOperator.setOperateOrder(sender.getSenderOrder());
                        senderSignOperator.setSignerType(SignerTypeEnum.RECEIVER_ENT.getCode());
                        senderSignOperator.setSignerId(sender.getId());
                        senderSignOperator.setOperateType(OperateTypeEnum.SIGN.getCode());
                        senderSignOperator.setOperateUserId(sender.getSenderUserId());
                        senderSignOperator.setDeleteFlag(false);
                        senderSignOperator.setId(null);
                        senderSignOperator.setOperateStatus(OperateStatusEnum.NOT_FINISHED.getCode());

                        //添加租户名称
                        senderSignOperator.setTenantName(signer.getSignerName());
                        senderSignOperator.setOperateExternalType(sender.getSenderExternalType());
                        senderSignOperator.setOperateExternalValue(sender.getSenderExternalValue());
                        operatorList.add(senderSignOperator);
                    }
                }
            }
            if(haveWrite){
                //填写操作人
                SignRuOperator signerWriteOperator = new SignRuOperator();
                BeanUtils.copyProperties(signer,signerWriteOperator);
                signerWriteOperator.setOperateType(OperateTypeEnum.WRITE.getCode());
                signerWriteOperator.setOperateName(signer.getSignerName());
                signerWriteOperator.setSignRuId(ruId);
                signerWriteOperator.setOperateOrder(signer.getSignerOrder());
                //发起方还是接收方
                if(SignerTypeEnum.RECEIVER_PERSONAL.getCode().equals(signer.getSignerType())){
                    signerWriteOperator.setSignerType(SignerTypeEnum.RECEIVER_PERSONAL.getCode());
                }else if(SignerTypeEnum.SENDER.getCode().equals(signer.getSignerType())){
                    signerWriteOperator.setSignerType(SignerTypeEnum.SENDER.getCode());
                }else if(SignerTypeEnum.RECEIVER_ENT.getCode().equals(signer.getSignerType())){
                    signerWriteOperator.setSignerType(SignerTypeEnum.RECEIVER_ENT.getCode());
                    signerWriteOperator.setTenantName(signer.getSignerName());
//                    signerWriteOperator.setOperateExternalType(signer.getSignerExternalType());
//                    signerWriteOperator.setOperateExternalValue(signer.getSignerExternalValue());
                }
                signerWriteOperator.setSignerId(signer.getId());
                if(signer.getSignerExternalType() != null && signer.getSignerExternalValue() != null && signer.getSignerExternalValue().length() > 0){
                    signerWriteOperator.setOperateExternalType(signer.getSignerExternalType());
                    signerWriteOperator.setOperateExternalValue(signer.getSignerExternalValue());
                }else {
                    signerWriteOperator.setOperateUserId(signer.getSignerUserId());
                }

                signerWriteOperator.setDeleteFlag(false);
                signerWriteOperator.setId(null);
                //判定是否需要无需填写
                for(SignRuDocControl control : controlList){
                    if(writeList.contains(control.getControlType()) && control.getSignerId() != null && control.getSignerId().equals(signer.getId())){
                        //填写控件中包含该签署人，状态设置为未完成
                        signerWriteOperator.setOperateStatus(OperateStatusEnum.NOT_FINISHED.getCode());
                    }
                }
                if(signerWriteOperator.getOperateStatus() == null){
                    //填写控件中不包含该签署人，状态设置为无需完成
                    signerWriteOperator.setOperateStatus(OperateStatusEnum.NO_NEED_FINISH.getCode());
                }
                operatorList.add(signerWriteOperator);
            }
        }
        if(operatorList.size() == 0){
            throw new PaasException("操作人为空");
        }
        ruDataService.saveStartData(operatorList,relationList,ru);

        return ruId ;
    }




    public String startForApi(String ruId,String tenantUserId){

        //获取签约文件列表
        List<SignRuDoc> docList = ruDocService.listByRuId(ruId);
        if(docList == null || docList.size() == 0){
            throw new PaasException("签约文件为空");
        }
        //获取所有控件类型列表
        List<String> writeList = ControlTypeEnum.getWriteList();
        Boolean haveWrite = false ;
        //获取控件列表
        ControlQueryVo controlQueryVo = new ControlQueryVo();
        controlQueryVo.setSignRuId(ruId);
        List<SignRuDocControl> controlList = ruDocControlService.listByParam(controlQueryVo);
        if(controlList != null && controlList.size() > 0){

            //校验控件
            for(SignRuDocControl signRuDocControl : controlList){
//                if(signRuDocControl.getSignRuDocId() ==  null || signRuDocControl.getSignRuDocId().length() == 0){
//                    throw new PaasException("控件关联签约文档为空");
//                }
                if(signRuDocControl.getControlType() == null || signRuDocControl.getControlType().length() == 0){
                    throw new PaasException("控件类型为空");
                }
                if(signRuDocControl.getSignerId() == null || signRuDocControl.getSignerId().length() == 0){
                    if(ControlTypeEnum.getWriteList().contains(signRuDocControl.getControlType())){
                        if(signRuDocControl.getIsRequired() != null && signRuDocControl.getIsRequired().equals(ControlIsRequiredEnum.IS.getCode())){
                            throw new PaasException("模板参数填写方未全部设置");
                        }
                    }else {
                        throw new PaasException("签署方未全部设置");
                    }
                }

                if(signRuDocControl.getControlType().equals(ControlTypeEnum.SIGN_DATE.getName())){
                    if(signRuDocControl.getFormat() == null || signRuDocControl.getFormat().length() == 0){
                        throw new PaasException("签署日期控件-时间格式不能为空");
                    }
                    try {
                        SimpleDateFormat signDateFormat = new SimpleDateFormat(signRuDocControl.getFormat());
                        signDateFormat.format(new Date());
                    }catch (Exception e){
                        throw new PaasException("签署日期控件-时间格式错误：" + signRuDocControl.getFormat());
                    }
                }
                if(writeList.contains(signRuDocControl.getControlType())){
                    //如果是填写控件
                    haveWrite = true;
                }

            }
        }

        //获取签署人列表
        List<SignRuSigner> singerList = signerService.listByRuId(ruId);
        if(singerList == null || singerList.size() == 0){
            throw new PaasException("签署人为空");
        }
        //单号生成
        SignRu ru = ruService.getById(ruId);
        if(ru == null){
            throw new PaasException("业务线实例不存在");
        }

        //根据发起人、接受人、控件列表计算操作人
        List<SignRuOperator> operatorList = new ArrayList<>();
        //实例关联人
        List<SignRuRelation> relationList = new ArrayList<>();
        for(SignRuSigner signer : singerList){
            if(signer.getSignerType().equals(SignerTypeEnum.SENDER.getCode())){
                for(SignRuDocControl signRuDocControl : controlList){
                    if(signRuDocControl.getSignerId() != null && signRuDocControl.getSignerId().equals(signer.getId())){
                        if(writeList.contains(signRuDocControl.getControlType())){
                            //如果为必填项，则必须有值
                            if(signRuDocControl.getIsRequired() != null && signRuDocControl.getIsRequired().equals(ControlIsRequiredEnum.IS.getCode())){
                                if(signRuDocControl.getValue() == null || signRuDocControl.getValue().length() == 0){
                                    throw new PaasException("文档参数内容未填写完成，请完善");
                                }
                                //如果是时间控件
                                if(signRuDocControl.getControlType().equals(ControlTypeEnum.DATE.getName())){
                                    String value = signRuDocControl.getValue();
                                    try {
                                        //验证控件参数
                                        SimpleDateFormat character = new SimpleDateFormat(signRuDocControl.getFormat());
                                        SimpleDateFormat number = new SimpleDateFormat("yyyy-MM-dd");
                                        Date parse = number.parse(value);
                                        character.format(parse);
                                    }catch (Exception e){
                                        throw new PaasException("时间控件日期解析错误，请填写正确时间格式" + value);
                                    }
                                }
                            }
                        }
                    }
                }
                //获取发起人
                List<SignRuSender> senderList = senderService.listBySignerId(signer.getId());
                if(senderList != null && senderList.size() > 0){
                    for(SignRuSender sender : senderList){
                        if(sender.getSenderType().equals(SenderTypeEnum.ENTERPRISE.getCode())){
                            //必须设定签章
                            if(sender.getSenderSealId() == null || sender.getSenderSealId().length() == 0){
                                throw new PaasException("组织签章未指定签章");
                            }
                            //如果没有指定签署人，则为自动盖章，必须有相关的签章
                            if(sender.getSenderSignType().equals(SenderSignTypeEnum.AUTO.getCode())){

                                Boolean enterpriseControlFlag = true;
                                for(SignRuDocControl signRuDocControl : controlList){
                                    //必须是企业签章
                                    if(signRuDocControl.getSignerId() != null && signRuDocControl.getSignerId().equals(sender.getId()) && ControlTypeEnum.SEAL.getName().equals(signRuDocControl.getControlType())){
                                        enterpriseControlFlag = false ;
                                    }
                                }
                                if(enterpriseControlFlag){
                                    throw new PaasException("自动盖章的签署节点未指定签署位置");
                                }
                            }
                        }
                        //发起方签署操作人
                        SignRuOperator senderSignOperator = new SignRuOperator();
                        BeanUtils.copyProperties(sender,senderSignOperator);
                        senderSignOperator.setOperateName(sender.getSenderName());
                        senderSignOperator.setSignRuId(ruId);
                        senderSignOperator.setOperateOrder(sender.getSenderOrder());
                        senderSignOperator.setSignerType(SignerTypeEnum.SENDER.getCode());
                        senderSignOperator.setSignerId(sender.getId());
                        if (sender.getSenderType().equals(SenderTypeEnum.APPROVER.getCode())){
                            senderSignOperator.setOperateType(OperateTypeEnum.APPROVE.getCode());
                        } else{
                            senderSignOperator.setOperateType(OperateTypeEnum.SIGN.getCode());
                        }
                        senderSignOperator.setOperateUserId(sender.getSenderUserId());
                        senderSignOperator.setDeleteFlag(false);
                        senderSignOperator.setId(null);
                        senderSignOperator.setOperateStatus(OperateStatusEnum.NOT_FINISHED.getCode());
                        operatorList.add(senderSignOperator);
                    }
                }

                //发送人关联人
                SignRuRelation ruRelation = new SignRuRelation();
                ruRelation.setSignRuId(ruId);
                ruRelation.setRelationType(RelationTypeEnum.SENDER.getCode());
                ruRelation.setTenantUserId(tenantUserId);
                ruRelation.setTaskLinkType("system");
                ruRelation.setDeleteFlag(false);
                relationList.add(ruRelation);
            }else if(signer.getSignerType().equals(SignerTypeEnum.RECEIVER_PERSONAL.getCode())){
                //接收方签署操作人
                SignRuOperator signerSignOperator = new SignRuOperator();
                BeanUtils.copyProperties(signer,signerSignOperator);
                signerSignOperator.setOperateType(OperateTypeEnum.SIGN.getCode());
                signerSignOperator.setOperateName(signer.getSignerName());
                signerSignOperator.setSignRuId(ruId);
                signerSignOperator.setOperateOrder(signer.getSignerOrder());
                signerSignOperator.setSignerId(signer.getId());
                signerSignOperator.setSignerType(SignerTypeEnum.RECEIVER_PERSONAL.getCode());
                signerSignOperator.setOperateUserId(signer.getSignerUserId());
                signerSignOperator.setOperateExternalType(signer.getSignerExternalType());
                signerSignOperator.setOperateExternalValue(signer.getSignerExternalValue());
                signerSignOperator.setDeleteFlag(false);
                signerSignOperator.setId(null);
                signerSignOperator.setOperateStatus(OperateStatusEnum.NOT_FINISHED.getCode());
                operatorList.add(signerSignOperator);
            }else if(signer.getSignerType().equals(SignerTypeEnum.RECEIVER_ENT.getCode())){
                //获取企业接收方
                List<SignRuSender> senderList = senderService.listBySignerId(signer.getId());
                if(senderList != null && senderList.size() > 0){
                    for(SignRuSender sender : senderList){
                        //发起方签署操作人
                        SignRuOperator senderSignOperator = new SignRuOperator();
                        BeanUtils.copyProperties(sender,senderSignOperator);
                        senderSignOperator.setOperateName(sender.getSenderName());
                        senderSignOperator.setSignRuId(ruId);
                        senderSignOperator.setOperateOrder(sender.getSenderOrder());
                        senderSignOperator.setSignerType(SignerTypeEnum.RECEIVER_ENT.getCode());
                        senderSignOperator.setSignerId(sender.getId());
                        senderSignOperator.setOperateType(OperateTypeEnum.SIGN.getCode());
                        senderSignOperator.setOperateUserId(sender.getSenderUserId());
                        senderSignOperator.setDeleteFlag(false);
                        senderSignOperator.setId(null);
                        senderSignOperator.setOperateStatus(OperateStatusEnum.NOT_FINISHED.getCode());

                        //添加租户名称
                        senderSignOperator.setTenantName(signer.getSignerName());
                        senderSignOperator.setOperateExternalType(sender.getSenderExternalType());
                        senderSignOperator.setOperateExternalValue(sender.getSenderExternalValue());
                        operatorList.add(senderSignOperator);
                    }
                }
            }
            if(haveWrite){
                //填写操作人
                SignRuOperator signerWriteOperator = new SignRuOperator();
                BeanUtils.copyProperties(signer,signerWriteOperator);
                signerWriteOperator.setOperateType(OperateTypeEnum.WRITE.getCode());
                signerWriteOperator.setOperateName(signer.getSignerName());
                signerWriteOperator.setSignRuId(ruId);
                signerWriteOperator.setOperateOrder(signer.getSignerOrder());
                //发起方还是接收方
                if(SignerTypeEnum.RECEIVER_PERSONAL.getCode().equals(signer.getSignerType())){
                    signerWriteOperator.setSignerType(SignerTypeEnum.RECEIVER_PERSONAL.getCode());
                }else if(SignerTypeEnum.SENDER.getCode().equals(signer.getSignerType())){
                    signerWriteOperator.setSignerType(SignerTypeEnum.SENDER.getCode());
                }else if(SignerTypeEnum.RECEIVER_ENT.getCode().equals(signer.getSignerType())){
                    signerWriteOperator.setSignerType(SignerTypeEnum.RECEIVER_ENT.getCode());
                    signerWriteOperator.setTenantName(signer.getSignerName());
                    signerWriteOperator.setOperateExternalType(signer.getSignerExternalType());
                    signerWriteOperator.setOperateExternalValue(signer.getSignerExternalValue());
                }
                signerWriteOperator.setSignerId(signer.getId());
                signerWriteOperator.setOperateUserId(signer.getSignerUserId());
                signerWriteOperator.setOperateExternalType(signer.getSignerExternalType());
                signerWriteOperator.setOperateExternalValue(signer.getSignerExternalValue());
                signerWriteOperator.setDeleteFlag(false);
                signerWriteOperator.setId(null);
                //判定是否需要无需填写
                for(SignRuDocControl control : controlList){
                    if(writeList.contains(control.getControlType()) && control.getSignerId() != null && control.getSignerId().equals(signer.getId())){
                        //填写控件中包含该签署人，状态设置为未完成
                        signerWriteOperator.setOperateStatus(OperateStatusEnum.NOT_FINISHED.getCode());
                    }
                }
                if(signerWriteOperator.getOperateStatus() == null){
                    //填写控件中不包含该签署人，状态设置为无需完成
                    signerWriteOperator.setOperateStatus(OperateStatusEnum.NO_NEED_FINISH.getCode());
                }
                operatorList.add(signerWriteOperator);
            }
        }
        if((ru.getAutoFinish() == null || ru.getAutoFinish() == SignFinishTypeEnum.AUTO_FINISH.getCode()) && operatorList.size() == 0){
            throw new PaasException("操作人为空");
        }

        if (operatorList.size() > 0) {
            ruDataService.saveStartData(operatorList,relationList,ru);
        }

        return ruId ;
    }

    public void saveRuSignerForApi(RuCreateData ruCreateData){
        //签署人
        List<RuDataSigner> singerListByApi = ruCreateData.getSignerList();

        //获取签署人列表
        List<SignRuSigner> singerList = new ArrayList<SignRuSigner>();

        //企业内部签署人列表
        List<SignRuSender> senderList = new ArrayList<SignRuSender>();

        //单号生成
        SignRu ru = ruService.getById(ruCreateData.getRuId());
        if(ru == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务线实例不存在");
        }

        if(ru.getStatus() == SignRuStatusEnum.DONE.getCode()){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"合同已经完成签署，不能继续追加签署人");
        }

        if(singerListByApi.size() > 0){
            for(RuDataSigner dataSinger : singerListByApi){

                if(SignerTypeEnum.SENDER.getCode().equals(dataSinger.getSignerType()) || SignerTypeEnum.RECEIVER_ENT.getCode().equals(dataSinger.getSignerType())){

                    //查询当前用户
                    SignRuSigner query = new SignRuSigner();
                    query.setSignRuId(ruCreateData.getRuId());
                    query.setSignerName(dataSinger.getRuSigner().getSignerName());

                    List<SignRuSigner> ruSigners = signerService.getByEntityForApi(query);
                    if(CollUtil.isNotEmpty(ruSigners)){

                        for (int i = 0; i < ruSigners.size(); i++){
                            boolean isAddSigner = false;
                            if(ru.getSignOrderType() == SignOrderTypeEnum.NO_ORDER.getCode()){
                                isAddSigner = true;
                            }else if(ru.getSignOrderType() == SignOrderTypeEnum.ORDER.getCode()){
                                // 获取企业签署人的签署方列表
                                SignRuSigner nextSignerQuery = new SignRuSigner();
                                nextSignerQuery.setSignRuId(ruCreateData.getRuId());
                                nextSignerQuery.setSignerOrder(ruSigners.get(i).getSignerOrder()+1);
                                // 查询下一个签署人
                                List<SignRuSigner> ruNextSigners = signerService.getByEntityForApi(nextSignerQuery);

                                if (CollUtil.isNotEmpty(ruNextSigners)){
                                    for(SignRuSigner ruNextSigner : ruNextSigners){
                                        if(ruNextSigner.getSignerType() == SignerTypeEnum.RECEIVER_ENT.getCode() || ruNextSigner.getSignerType() == SignerTypeEnum.SENDER.getCode()){
                                            List<SignRuSender> senderOfNextEntList = senderService.listBySignerId(ruNextSigner.getId());
                                            if(CollUtil.isNotEmpty(senderOfNextEntList)){
                                                for (SignRuSender senderOfEnt : senderOfNextEntList){
                                                    SignRuOperator opqQuery = new SignRuOperator();
                                                    opqQuery.setSignRuId(ruNextSigner.getSignRuId());
                                                    opqQuery.setSignerId(senderOfEnt.getId());
                                                    List<SignRuOperator> signRuOperators = ruOperatorService.getByEntity(opqQuery);
                                                    if(CollUtil.isNotEmpty(signRuOperators)){
                                                        for(SignRuOperator ruOperator : signRuOperators){
                                                            //如果企业签署人还有未完成和代办操作，则可以继续添加签署人
                                                            if(ruOperator.getOperateStatus() == OperateStatusEnum.WAIT_TO_FINISH.getCode()|| ruOperator.getOperateStatus() == OperateStatusEnum.FINISHED.getCode()){
                                                                isAddSigner = false;
                                                                break;
                                                            }
                                                            isAddSigner = true;
                                                        }
                                                    }
                                                    if(isAddSigner == false){
                                                        break;
                                                    }
                                                }
                                            }
                                        }else if(ruNextSigner.getSignerType() == SignerTypeEnum.RECEIVER_PERSONAL.getCode()){
                                            SignRuOperator opqQuery = new SignRuOperator();
                                            opqQuery.setSignRuId(ruNextSigner.getSignRuId());
                                            opqQuery.setSignerId(ruNextSigner.getId());
                                            List<SignRuOperator> signRuOperators = ruOperatorService.getByEntity(opqQuery);
                                            if(CollUtil.isNotEmpty(signRuOperators)){
                                                for(SignRuOperator ruOperator : signRuOperators){
                                                    //如果企业签署人还有未完成和代办操作，则可以继续添加签署人
                                                    if(ruOperator.getOperateStatus() == OperateStatusEnum.WAIT_TO_FINISH.getCode() || ruOperator.getOperateStatus() == OperateStatusEnum.FINISHED.getCode()){
                                                        isAddSigner = false;
                                                        break;
                                                    }
                                                    isAddSigner = true;
                                                }
                                            }
                                        }

                                    }
                                }else{
                                    isAddSigner = true;
                                }
                            }
                            // 如果有待完成或者未完成操作，则可以添加签署人
                            if(isAddSigner){
                                //发起方
                                List<RuDataSender> addSenderList = dataSinger.getAddSenderList();
                                if(addSenderList.size() > 0){
                                    for(RuDataSender dataSender : addSenderList){
                                        SignRuSender ruSender = dataSender.getRuSender();
                                        if(ruSender != null){
                                            ruSender.setSignerId(ruSigners.get(i).getId());
                                            ruSender.setDeleteFlag(false);
                                            boolean b = senderService.save(ruSender);
                                            if(!b){
                                                throw new PaasException("保存发起方" + ruSender.getSenderName() + "失败");
                                            }
                                            if(dataSender.getReSenderId() != null){
                                                //关联关系
                                                ruCreateData.getReSigner2RuSingerMap().put(dataSender.getReSenderId(),ruSender.getId());
                                            }
                                            //保存企业签署人信息
                                            senderList.add(ruSender);
                                            //发起方-保存签署意愿校验信息
                                            ruSignConfirmService.save(ruSender.getId(),ruCreateData.getRuId(),dataSinger.getSignerType(),dataSender.getAgreeSkipWillingness(),dataSender.getVerifyType(),dataSender.getPersonalSignAuth(),dataSender.getSealType());
                                        }
                                    }
                                }
                                singerList.add(ruSigners.get(i));
                            }else{
                                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"签署人" + ruSigners.get(i).getSignerName() + "已完成签署任务，不能继续追加签署人。");
                            }
                        }
                    }else{
                        SignRuSigner ruSigner = dataSinger.getRuSigner();
                        ruSigner.setSignRuId(ruCreateData.getRuId());
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
                                    //保存企业签署人信息
                                    senderList.add(ruSender);
                                    //发起方-保存签署意愿校验信息
                                    ruSignConfirmService.save(ruSender.getId(),ruCreateData.getRuId(),dataSinger.getSignerType(),dataSender.getAgreeSkipWillingness(),dataSender.getVerifyType(),dataSender.getPersonalSignAuth(),dataSender.getSealType());
                                }
                            }
                        }
                        singerList.add(ruSigner);
                    }
                } else {
                    SignRuSigner ruSigner = dataSinger.getRuSigner();
                    ruSigner.setSignRuId(ruCreateData.getRuId());
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
                    //个人接收方
                    ruSignConfirmService.save(ruSigner.getId(),ruCreateData.getRuId(),SignerTypeEnum.RECEIVER_PERSONAL.getCode(),dataSinger.getAgreeSkipWillingness(),dataSinger.getVerifyType(),dataSinger.getPersonalSignAuth(),dataSinger.getSealType());

                    singerList.add(ruSigner);
                }
            }
        }

        // = signerService.listByRuId(ruCreateData.getRuId());
        if(singerList == null || singerList.size() == 0){
            throw new PaasException("签署人为空");
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
                ruDocControl.setSignRuId(ru.getId());
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
                    ruDocControlProperty.setRuId(ru.getId());
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

        //根据发起人、接受人、控件列表计算操作人
        List<SignRuOperator> operatorList = new ArrayList<>();
        //实例关联人
        List<SignRuRelation> relationList = new ArrayList<>();

        //获取控件列表
        ControlQueryVo controlQueryVo = new ControlQueryVo();
        controlQueryVo.setSignRuId(ru.getId());
        List<SignRuDocControl> controlList = ruDocControlService.listByParam(controlQueryVo);

        for(SignRuSigner signer : singerList){
            if(signer.getSignerType().equals(SignerTypeEnum.SENDER.getCode())){
                //获取发起人
                //List<SignRuSender> senderList = senderService.listBySignerId(signer.getId());
                if(senderList != null && senderList.size() > 0){
                    for(SignRuSender sender : senderList){
                        if(sender.getSignerId().equals(signer.getId())){
                            if(sender.getSenderType().equals(SenderTypeEnum.ENTERPRISE.getCode())){
                                //必须设定签章
                                if(sender.getSenderSealId() == null || sender.getSenderSealId().length() == 0){
                                    throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"组织签章未指定签章");
                                }
                                //如果没有指定签署人，则为自动盖章，必须有相关的签章
                                if(sender.getSenderSignType().equals(SenderSignTypeEnum.AUTO.getCode())){
                                    boolean enterpriseControlFlag = true;
                                    for(SignRuDocControl signRuDocControl : controlList){
                                        //必须是企业签章
                                        if(signRuDocControl.getSignerId() != null && signRuDocControl.getSignerId().equals(sender.getId()) && ControlTypeEnum.SEAL.getName().equals(signRuDocControl.getControlType())){
                                            enterpriseControlFlag = false ;
                                        }
                                    }
                                    if(enterpriseControlFlag){
                                        throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"自动盖章的签署节点未指定签署位置");

                                    }
                                }
                            }
                            //发起方签署操作人
                            SignRuOperator senderSignOperator = new SignRuOperator();
                            BeanUtils.copyProperties(sender,senderSignOperator);
                            senderSignOperator.setOperateName(sender.getSenderName());
                            senderSignOperator.setSignRuId(ru.getId());
                            senderSignOperator.setOperateOrder(sender.getSenderOrder());
                            senderSignOperator.setSignerType(SignerTypeEnum.SENDER.getCode());
                            senderSignOperator.setSignerId(sender.getId());

                            if (sender.getSenderType().equals(SenderTypeEnum.APPROVER.getCode())){
                                senderSignOperator.setOperateType(OperateTypeEnum.APPROVE.getCode());
                            } else{
                                senderSignOperator.setOperateType(OperateTypeEnum.SIGN.getCode());
                            }
                            senderSignOperator.setOperateUserId(sender.getSenderUserId());
                            senderSignOperator.setDeleteFlag(false);
                            senderSignOperator.setId(null);
                            senderSignOperator.setOperateStatus(OperateStatusEnum.NOT_FINISHED.getCode());
                            operatorList.add(senderSignOperator);
                        }
                    }
                }
            }else if(signer.getSignerType().equals(SignerTypeEnum.RECEIVER_PERSONAL.getCode())){
                //接收方签署操作人
                SignRuOperator signerSignOperator = new SignRuOperator();
                BeanUtils.copyProperties(signer,signerSignOperator);
                signerSignOperator.setOperateType(OperateTypeEnum.SIGN.getCode());
                signerSignOperator.setOperateName(signer.getSignerName());
                signerSignOperator.setSignRuId(ru.getId());
                signerSignOperator.setOperateOrder(signer.getSignerOrder());
                signerSignOperator.setSignerId(signer.getId());
                signerSignOperator.setSignerType(SignerTypeEnum.RECEIVER_PERSONAL.getCode());
                signerSignOperator.setOperateUserId(signer.getSignerUserId());
                signerSignOperator.setOperateExternalType(signer.getSignerExternalType());
                signerSignOperator.setOperateExternalValue(signer.getSignerExternalValue());
                signerSignOperator.setDeleteFlag(false);
                signerSignOperator.setId(null);
                signerSignOperator.setOperateStatus(OperateStatusEnum.NOT_FINISHED.getCode());
                operatorList.add(signerSignOperator);
            }else if(signer.getSignerType().equals(SignerTypeEnum.RECEIVER_ENT.getCode())){
                //获取企业接收方
                if(senderList != null && senderList.size() > 0){
                    for(SignRuSender sender : senderList){
                        if(sender.getSignerId().equals(signer.getId())){
                            //发起方签署操作人
                            SignRuOperator senderSignOperator = new SignRuOperator();
                            BeanUtils.copyProperties(sender,senderSignOperator);
                            senderSignOperator.setOperateName(sender.getSenderName());
                            senderSignOperator.setSignRuId(ru.getId());
                            senderSignOperator.setOperateOrder(sender.getSenderOrder());
                            senderSignOperator.setSignerType(SignerTypeEnum.RECEIVER_ENT.getCode());
                            senderSignOperator.setSignerId(sender.getId());
                            senderSignOperator.setOperateType(OperateTypeEnum.SIGN.getCode());
                            senderSignOperator.setOperateUserId(sender.getSenderUserId());
                            senderSignOperator.setDeleteFlag(false);
                            senderSignOperator.setId(null);
                            senderSignOperator.setOperateStatus(OperateStatusEnum.NOT_FINISHED.getCode());

                            //添加租户名称
                            senderSignOperator.setTenantName(signer.getSignerName());
                            senderSignOperator.setOperateExternalType(sender.getSenderExternalType());
                            senderSignOperator.setOperateExternalValue(sender.getSenderExternalValue());
                            operatorList.add(senderSignOperator);
                        }
                    }
                }
            }
        }
        if(operatorList.size() == 0){
            throw new PaasException("操作人为空");
        }
        ruDataService.saveStartData(operatorList,relationList,ru);
    }

    /**
     * @Description #业务线实例整体填写
     * @Param [ruId]
     * @return void
     **/
    public void write(String ruId){
        //获取业务线实例
        SignRu signRu = ruService.getById(ruId);
        if(signRu == null){
            throw new PaasException("业务线实例不存在");
        }
        //获取所有填写控件
        ControlQueryVo controlQueryVo = new ControlQueryVo();
        controlQueryVo.setControlTypeList(ControlTypeEnum.getWriteList());
        controlQueryVo.setSignRuId(ruId);
        List<SignRuDocControl> controlList = ruDocControlService.listByParam(controlQueryVo);
        if(controlList == null || controlList.size() == 0){
//            throw new PaasException("业务线实例-填写控件不存在");
            //如果没有填写控件，则直接返回
            return;
        }

        //创建真实填写控件列表
        List<SignRuDocControl> writeControlList = new ArrayList<>();

        for(SignRuDocControl control : controlList){
            if(control.getSignRuDocId() == null || control.getSignRuDocId().length() == 0){
                throw new PaasException("业务线实例-控件没有关联的签约文件");
            }
            if(control.getControlType() == null){
                throw new PaasException("填写控件类型异常");
            }
            if(control.getIsRequired() != null && control.getIsRequired().equals(ControlIsRequiredEnum.IS.getCode())){
                if(control.getValue() == null || control.getValue().length() == 0){
                    throw new PaasException("文档参数内容未填写完成，请完善");
                }
                if(control.getControlType().equals(ControlTypeEnum.CHECKBOX.getName()) || control.getControlType().equals(ControlTypeEnum.RADIO.getName())
                    || control.getControlType().equals(ControlTypeEnum.DROPDOWN_LIST.getName())){
                    if(control.getProperties() == null || control.getProperties().length() == 0){
                        throw new PaasException("文档参数内容未填写完成，请完善");
                    }
                    try {
                        List<ControlWidgetProperty> widgetPropertyList = JSON.parseArray(control.getProperties(), ControlWidgetProperty.class);
                        if(widgetPropertyList == null || widgetPropertyList.size() == 0){
                            throw new PaasException("填写控件数据异常");
                        }
                        List<String> uidList = widgetPropertyList.stream().map(ControlWidgetProperty::getUid).collect(Collectors.toList());
                        if(uidList == null || uidList.size() == 0){
                            throw new PaasException("填写控件数据异常");
                        }
                        Boolean uidErrorFlag = true ;
                        for(String uid : uidList){
                            if(control.getValue().contains(uid)){
                                uidErrorFlag = false ;
                            }
                        }
                        if(uidErrorFlag){
                            throw new PaasException("填写控件数据异常");
                        }
                    }catch (Exception e){
                        throw new PaasException("填写控件数据异常");
                    }
                }else if(control.getControlType().equals(ControlTypeEnum.DATE.getName())){
                    if(control.getFormat() == null || control.getFormat().length() == 0){
                        throw new PaasException("时间填写控件格式为空了");
                    }
                    if(!control.getFormat().equals("YYYY年MM月DD日") && !control.getFormat().equals("YYYY-MM-DD") && !control.getFormat().equals("YYYY/MM/DD")){
                        throw new PaasException("时间填写控件格式异常");
                    }
                }
            }else {
                //如果是非必填项
                if(control.getValue() == null || control.getValue().length() == 0){
                    //直接跳过
                    continue;
                }
                if(control.getControlType().equals(ControlTypeEnum.CHECKBOX.getName()) || control.getControlType().equals(ControlTypeEnum.RADIO.getName())
                        || control.getControlType().equals(ControlTypeEnum.DROPDOWN_LIST.getName())){
                    if(control.getProperties() == null || control.getProperties().length() == 0){
                       continue;
                    }
                    try {
                        List<ControlWidgetProperty> widgetPropertyList = JSON.parseArray(control.getProperties(), ControlWidgetProperty.class);
                        if(widgetPropertyList == null || widgetPropertyList.size() == 0){
                            continue;
                        }
                        List<String> uidList = widgetPropertyList.stream().map(ControlWidgetProperty::getUid).collect(Collectors.toList());
                        if(uidList == null || uidList.size() == 0){
                            continue;
                        }
                        Boolean uidErrorFlag = true ;
                        for(String uid : uidList){
                            if(control.getValue().contains(uid)){
                                uidErrorFlag = false ;
                            }
                        }
                        if(uidErrorFlag){
                            continue;
                        }
                    }catch (Exception e){
                        throw new PaasException("填写控件数据异常");
                    }
                }else if(control.getControlType().equals(ControlTypeEnum.DATE.getName())){
                    if(control.getFormat() == null || control.getFormat().length() == 0){
                        continue;
                    }
                    if(!control.getFormat().equals("YYYY年MM月DD日") && !control.getFormat().equals("YYYY-MM-DD") && !control.getFormat().equals("YYYY/MM/DD")){
                        continue;
                    }
                }
            }
            //如果是时间控件
            if(control.getControlType().equals(ControlTypeEnum.DATE.getName())){
                String value = control.getValue();
                //验证控件参数
                SimpleDateFormat character = null ;
                if(control.getFormat().equals("YYYY年MM月DD日")){
                    character = new SimpleDateFormat("yyyy年MM月dd日");
                }else if(control.getFormat().equals("YYYY-MM-DD")){
                    character = new SimpleDateFormat("yyyy-MM-dd");
                }else if(control.getFormat().equals("YYYY/MM/DD")){
                    character = new SimpleDateFormat("yyyy/MM/dd");
                }
                SimpleDateFormat number = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date parse = number.parse(value);
                    String format = character.format(parse);
                    control.setValue(format);
                }catch (Exception e){
                    throw new PaasException("时间控件日期解析错误，请填写正确时间格式" + value);
                }
            }
            //添加到填写控件列表
            writeControlList.add(control);
        }
        if(writeControlList.size() == 0){
            //如果最终没有填写控件，或者没有必填项且所有非必填项都未填写，则直接返回
            return;
        }
        //填写操作
        operate(writeControlList,signRu,OperateTypeEnum.WRITE,null, null,null,null,null);

    }

    /**
     * @Description #流程驱动-自动签署
     * @Param [id, signerType]
     * @return void
     **/
    public void autoSign(String id , Integer signerType){
        SignRuSigner signer = null ;
        SignRuSender sender = null ;
        String personalSignAuthType = null;
        if(SignerTypeEnum.SENDER.getCode().equals(signerType)){
            //获取发起人-签署人-数据
            sender = senderService.getById(id);
            if(sender == null){
                throw new PaasException("业务线实例-签署人不存在");
            }
            signer = signerService.getById(sender.getSignerId());
        }
        if(signer == null || sender == null){
            throw new PaasException("业务线实例-签署人不存在");
        }
        //获取业务线数据
        SignRu signRu = ruService.getById(signer.getSignRuId());
        if(signRu == null){
            throw new PaasException("业务线实例不存在");
        }

        SignRuSignConfirm confirm = ruSignConfirmService.getByParam(sender.getId(),signRu.getId());

        if(confirm != null && MyStringUtils.isNotBlank(confirm.getPersonalSignAuth())){
            personalSignAuthType = confirm.getPersonalSignAuth();
        }else {
            confirm = ruSignConfirmService.getByParam(signer.getId(),signRu.getId());
            if(confirm != null && MyStringUtils.isNotBlank(confirm.getPersonalSignAuth())){
                personalSignAuthType = confirm.getPersonalSignAuth();
            }
        }

        //准备签章数据
        byte[] entSealByte = null ;
        if(sender.getSenderSealId() == null || sender.getSenderSealId().length() == 0){
            throw new PaasException("业务线实例-签章不存在");
        }
        SignEntSeal entSeal = entSealService.getById(sender.getSenderSealId());
        if(entSeal == null){
            throw new PaasException("业务线实例-签章不存在");
        }
        entSealByte = signFileService.getByteByFatherIdAndDataCategory(SignFileEnum.SEAL_FILE_ENT, entSeal.getId());
        if(entSealByte == null){
            throw new PaasException("业务线实例-签章不存在");
        }
        //准备签约控件和签约文件数据
        ControlQueryVo controlQueryVo = new ControlQueryVo();
        controlQueryVo.setSignRuId(signer.getSignRuId());
        controlQueryVo.setSignerId(sender.getId());
        controlQueryVo.setControlTypeList(ControlTypeEnum.getSignList());
        List<SignRuDocControl> docControlList = ruDocControlService.listByParam(controlQueryVo);
        if(docControlList == null || docControlList.size() == 0){
            throw new PaasException("业务线实例-签署位置未设置");
        }
        //准备证书数据
        CertificateProperty certificateProperty = null ;
        CertTypeEnum certTypeEnum = null ;
        OperateTypeEnum operateTypeEnum = null ;
        if(SignRuCaSignTypeEnum.CA.getCode().equals(signRu.getCaSignType())){
            certTypeEnum = certBusinessService.calculateCertType(CertTypeEnum.CA_TEST.getCode(), CertHolderTypeEnum.ENTERPRISE.getCode());
            operateTypeEnum = OperateTypeEnum.SIGN ;
        }else if(SignRuCaSignTypeEnum.SYSTEM_CA.getCode().equals(signRu.getCaSignType())){
            certTypeEnum = CertTypeEnum.SYSTEM ;
            operateTypeEnum = OperateTypeEnum.SIGN ;
        }else if(SignRuCaSignTypeEnum.NO_USE_CA.getCode().equals(signRu.getCaSignType())){
            certTypeEnum = null ;
            operateTypeEnum = OperateTypeEnum.SIGN_NO_CERT ;
        }
//        if(certTypeEnum != null){
//            certificateProperty = certBusinessService.findAndGenerateCertProperty(signRu.getSysTenantId(), certTypeEnum,CertHolderTypeEnum.ENTERPRISE.getCode());
//            if(certificateProperty == null){
//                throw new PaasException("业务线实例-企业证书不存在");
//            }
//        }
        //操作记录
        SignRuOperateRecord operateRecord = null ;
        if(signRu.getCaSignType() == SignRuCaSignTypeEnum.CA.getCode() || signRu.getCaSignType() == SignRuCaSignTypeEnum.SYSTEM_CA.getCode()){
            operateRecord = new SignRuOperateRecord();
            operateRecord.setSignRuId(signRu.getId());
            //operateRecord.setCertId(certificateProperty.getCertificateInfoId());
            operateRecord.setConfirmOrderNo(null);
            operateRecord.setTaskId(null);
            operateRecord.setAccountId(null);
            operateRecord.setTenantId(signRu.getSysTenantId());
            operateRecord.setTenantUserId(null);
            operateRecord.setOperateType(SignRecordOperateTypeEnum.ENT_SIGN.getType());
            operateRecord.setActionType(SignRecordActionTypeEnum.AUTO_SIGN.getType());
            operateRecord.setOperateTime(new Date());
        }

        try {
            SignServiceOpenInfoResponse signServiceOpenInfoResponse = signServiceManageExternal.querySilentInfo();
            if(signServiceOpenInfoResponse.getStatus() == 0){
                throw new PaasException("发起方企业未开通静默签署授权，或静默授权已到期，导致签署流程异常，请联系发起方企业。");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //签署操作
        operate(docControlList,signRu,operateTypeEnum,signRu.getSysTenantId(),entSealByte,operateRecord, SignTypeEnum.AUTO_SIGN.getCode(),personalSignAuthType);

        QueryWrapper<SignRuTask> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignRuTask::getSignRuId,signRu.getId());
        wrapper.lambda().eq(SignRuTask::getUserTaskId,sender.getId());
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().orderByDesc(BaseEntity::getCreateTime);
        List<SignRuTask> taskList = ruTaskService.list(wrapper);
        if(taskList != null && taskList.size() > 0){
            SignRuTask ruTask = taskList.get(0);
            if(ruTask != null){
                ruCallbackService.callback(signRu.getId(),ruTask.getId(),SignCallbackTypeEnum.SUBMIT_SIGN);
            }
        }

    }

    /**
     * @Description #获取签署节点配置
     * @Param []
     * @return com.kaifangqian.modules.opensign.vo.SignNodeConfigResponse
     **/
    public SignNodeConfigResponse getSignNodeConfig() {

        SignTaskThreadlocalVO threadlocalVO = SignTaskInfo.THREAD_LOCAL.get();
        if (threadlocalVO == null) {
            throw new PaasException("业务线实例不存在");
        }
        String signRuId = threadlocalVO.getSignRuId();
        SignRu ru = ruService.getById(signRuId);
        if (ru == null) {
            throw new PaasException("业务线实例不存在");
        }
        SignNodeConfigResponse signNodeConfigResponse = new SignNodeConfigResponse();

        if(threadlocalVO.getUserType().equals(SignerTypeEnum.SENDER.getCode()) || threadlocalVO.getUserType().equals(SignerTypeEnum.RECEIVER_ENT.getCode())) {
            SignRuSender sender = senderService.getById(threadlocalVO.getUserTaskId());

            SignRuSignConfirm confirm = ruSignConfirmService.getByParam(sender.getId(), signRuId);

            if (confirm != null && MyStringUtils.isNotBlank(confirm.getPersonalSignAuth())){
                signNodeConfigResponse.setPersonalSignAuth(confirm.getPersonalSignAuth());
            }else{
                signNodeConfigResponse.setPersonalSignAuth(PersonalSignAuthTypeEnum.REQUIRED.getType());
            }

            if (confirm != null && MyStringUtils.isNotBlank(confirm.getSealType())){
                signNodeConfigResponse.setSealType(confirm.getSealType());
            }

        }else {
            SignRuSigner signer = signerService.getById(threadlocalVO.getUserTaskId());
            // 获取签署人确认信息
            SignRuSignConfirm confirm = ruSignConfirmService.getByParam(signer.getId(), signRuId);
            // 获取平台个人实名认证节点配置
            if (confirm != null && MyStringUtils.isNotBlank(confirm.getPersonalSignAuth())){
                signNodeConfigResponse.setPersonalSignAuth(confirm.getPersonalSignAuth());
            }else{
                signNodeConfigResponse.setPersonalSignAuth(PersonalSignAuthTypeEnum.REQUIRED.getType());
            }

            if (confirm != null && MyStringUtils.isNotBlank(confirm.getSealType())){
                signNodeConfigResponse.setSealType(confirm.getSealType());
            }
        }

        return signNodeConfigResponse;
    }

    /**
     * @Description #提交签署
     * @Param [ruId]
     * @return void
     **/
    public SignOrderRequest submitSign(RunSubmitSignRequest submitSignRequest){

        // 构建云盾签署请求参数
        SignOrderRequest signOrderRequest = new SignOrderRequest();

        String signRuId = submitSignRequest.getSignRuId();
        //获取业务线实例
        SignRu signRu = ruService.getById(signRuId);
        if(signRu == null){
            throw new PaasException("业务线实例不存在");
        }
        //准备签章数据
        byte[] sealByte = null ;
        if(submitSignRequest.getEntSealId() != null && submitSignRequest.getEntSealId().length() > 0){
            SignEntSeal entSeal = entSealService.getById(submitSignRequest.getEntSealId());
            if(entSeal == null){
                throw new PaasException("业务线实例-签章不存在");
            }
            sealByte = signFileService.getByteByFatherIdAndDataCategory(SignFileEnum.SEAL_FILE_ENT, entSeal.getId());

        }else if(submitSignRequest.getPrivateSeal() != null && submitSignRequest.getPrivateSeal().length() > 0){
            sealByte = Base64.decode(submitSignRequest.getPrivateSeal());
        }
        if(sealByte == null){
            throw new PaasException("业务线实例-签章不存在");
        }
        //获取签署证书类型和操作类型
        //默认当前应该使用的证书为个人租户证书
        SignTaskThreadlocalVO threadlocalVO = SignTaskInfo.THREAD_LOCAL.get();
        Integer tenantType = TenantType.PERSONAL.getType() ;
        Integer holderType = CertHolderTypeEnum.PERSONAL.getCode();
        signOrderRequest.setTenantType("PERSONAL");

        if(threadlocalVO.getUserType().equals(SignerTypeEnum.SENDER.getCode()) || threadlocalVO.getUserType().equals(SignerTypeEnum.RECEIVER_ENT.getCode())){
            SignRuSender sender = senderService.getById(threadlocalVO.getUserTaskId());

            SignRuSignConfirm confirm = ruSignConfirmService.getByParam(sender.getId(),signRuId);

            if(sender == null){
                throw new PaasException("签署人不存在") ;
            }
            if(sender.getSenderType().equals(SenderTypeEnum.ENTERPRISE.getCode())){
                //如果为发起人，并且是组织盖章，则使用企业租户证书
                tenantType = TenantType.GROUP.getType() ;
                holderType = CertHolderTypeEnum.ENTERPRISE.getCode();
                signOrderRequest.setTenantType("COMPANY");
            }
            if(confirm != null){
                if(MyStringUtils.isNotBlank(confirm.getConfirmType())){
                    signOrderRequest.setVerifyTypes(Arrays.asList(confirm.getConfirmType()));
                }else{
                    signOrderRequest.setVerifyTypes(Arrays.asList("CAPTCHA","PASSWORD","DOUBLE","FACE"));
                }
                if(confirm.getAgreeSkipWillingness() != null && confirm.getAgreeSkipWillingness() == 1){
                    signOrderRequest.setAgreeSkipWillingness("true");
                }else{
                    signOrderRequest.setAgreeSkipWillingness("false");
                }
            }else{
                signOrderRequest.setVerifyTypes(Arrays.asList("CAPTCHA","PASSWORD","DOUBLE","FACE"));
            }

            // 个人签署节点实名认证逻辑判断
            if(tenantType != TenantType.GROUP.getType()){
                signOrderRequest.setPersonalSignAuth(this.setSignNodeConfig(confirm,signRu));
            }
        }else{
            SignRuSigner signer = signerService.getById(threadlocalVO.getUserTaskId());
            // 获取签署人确认信息
            SignRuSignConfirm confirm = ruSignConfirmService.getByParam(signer.getId(),signRuId);

            if(confirm != null){
                //个人签署意愿认证类型逻辑判断
                if(MyStringUtils.isNotBlank(confirm.getConfirmType())){
                    signOrderRequest.setVerifyTypes(Arrays.asList(confirm.getConfirmType()));
                }else{
                    signOrderRequest.setVerifyTypes(Arrays.asList("CAPTCHA","PASSWORD","DOUBLE","FACE"));
                }
                //个人签署是否跳过意愿认证逻辑判断
                if(confirm.getAgreeSkipWillingness() != null && confirm.getAgreeSkipWillingness() == 1){
                    signOrderRequest.setAgreeSkipWillingness("true");
                }else{
                    signOrderRequest.setAgreeSkipWillingness("false");
                }
            }else{
                signOrderRequest.setVerifyTypes(Arrays.asList("CAPTCHA","PASSWORD","DOUBLE","FACE"));
            }
            // 个人签署节点实名认证逻辑判断
            signOrderRequest.setPersonalSignAuth(this.setSignNodeConfig(confirm,signRu));
        }
        CertTypeEnum certTypeEnum = null ;
        OperateTypeEnum operateTypeEnum = null ;
        if(SignRuCaSignTypeEnum.CA.getCode().equals(signRu.getCaSignType())){
            certTypeEnum = certBusinessService.calculateCertType(CertTypeEnum.CA_TEST.getCode(), holderType);
            operateTypeEnum = OperateTypeEnum.SIGN ;
        }else if(SignRuCaSignTypeEnum.SYSTEM_CA.getCode().equals(signRu.getCaSignType())){
            certTypeEnum = CertTypeEnum.SYSTEM ;
            operateTypeEnum = OperateTypeEnum.SIGN ;
        }else if(SignRuCaSignTypeEnum.NO_USE_CA.getCode().equals(signRu.getCaSignType())){
            certTypeEnum = null ;
            operateTypeEnum = OperateTypeEnum.SIGN_NO_CERT ;
        }
        if(operateTypeEnum == null){
            throw new PaasException("业务线实例-无法确定签署操作类型");
        }

        //找到该租户的个人租户
        String certTenantId = null ;
        if(certTypeEnum != null){
            LoginUser currentUser = MySecurityUtils.getCurrentUser();
            if(tenantType.equals(TenantType.PERSONAL.getType())){
                List<SysTenantInfo> tenantInfoList = tenantUserService.getTenantsByUserId(currentUser.getId());
                if(tenantInfoList == null || tenantInfoList.size() == 0){
                    throw new PaasException("租户不存在");
                }
                for(SysTenantInfo tenant : tenantInfoList){
                    if(tenant.getTenantType().equals(TenantType.PERSONAL.getType()) && tenant.getTenantStatus().equals(TenantStatus.ENABLE.getType())){
                        certTenantId = tenant.getId();
                    }
                }
                signOrderRequest.setUnionId(certTenantId);
                signOrderRequest.setOperatorUnionId(certTenantId);
                signOrderRequest.setClientUsername(currentUser.getUsername());
            }else if(tenantType.equals(TenantType.GROUP.getType())){
                LoginUser loginUser = MySecurityUtils.getCurrentUser();

                certTenantId = loginUser.getTenantId();
                // 获取企业下个人用户
                String operatorUnionId = tenantUserService.getPersonalTenantUser(loginUser.getId()).getTenantId();
                signOrderRequest.setUnionId(certTenantId);
                signOrderRequest.setOperatorUnionId(operatorUnionId);
                signOrderRequest.setClientUsername(loginUser.getUsername());
            }
            if(certTenantId == null || holderType == null){
                throw new PaasException("业务线实例-证书不存在");
            }

        }

        //保存控件数据，证书数据，签章数据，订单号，taskId等等临时数据
        CompletedSignVo vo = new CompletedSignVo();
        //基本数据
        vo.setSignRuId(signRu.getId());
        vo.setSignConfirmOrderNo(submitSignRequest.getSignConfirmOrderNo());
        vo.setTaskId(threadlocalVO.getTaskId());
        //保存签章图片为临时数据
        String sealAnnexId = signFileService.saveAnnexStorage(sealByte, SignFileEnum.SEAL_FILE_TEMPORARY, submitSignRequest.getSignConfirmOrderNo());
        if(sealAnnexId == null || sealAnnexId.length() == 0){
            throw new PaasException("业务线实例-签章图片无法保存-提交签署失败");
        }
        vo.setSealAnnexId(sealAnnexId);
        //证书数据
        vo.setCertTenantId(certTenantId);
        vo.setCertHolderType(holderType);
        if(certTypeEnum != null){
            vo.setCertType(certTypeEnum.getCode());
        }
        //当前操作人
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        vo.setUserId(currentUser.getId());
        vo.setTenantId(currentUser.getTenantId());
        vo.setTenantUserId(currentUser.getTenantUserId());
        //操作类型
        vo.setOperateType(operateTypeEnum.getCode());
        //控件数据
        vo.setControlList(submitSignRequest.getControlList());
        //转换成json数据
        String jsonString = JSON.toJSONString(vo);
        if(jsonString == null || jsonString.length() == 0){
            throw new PaasException("业务线实例-提交签署失败");
        }

        SignRuSignTemporary temporary = ruSignTemporaryService.getByOrderNo(submitSignRequest.getSignConfirmOrderNo());
        if(temporary == null){
            temporary = new SignRuSignTemporary();
        }
        //保存临时签署数据
        temporary.setSignRuId(signRu.getId());
        temporary.setSignConfirmOrderNo(submitSignRequest.getSignConfirmOrderNo());
        temporary.setTaskId(threadlocalVO.getTaskId());
        temporary.setParams(jsonString);
        temporary.setStatus(SignRuSignTemporaryStatus.PROGRESSING.getCode());

        boolean saveTemporary = ruSignTemporaryService.saveOrUpdate(temporary);
        if(!saveTemporary){
            throw new PaasException("业务线实例-提交签署失败");
        }


        signOrderRequest.setBizId(threadlocalVO.getTaskId());
        signOrderRequest.setContractId(signRu.getId());
        signOrderRequest.setContractName(signRu.getSubject());

        return signOrderRequest;
    }



    /**
     * @Description #完成签署
     * @Param [signConfirmOrderNo]
     * @return void
     **/
    public String completedSign(CompletedSignVo vo,String temporaryId,String ipAddr){
        //业务线实例
        SignRu signRu = ruService.getById(vo.getSignRuId());
        if(signRu == null){
            throw new PaasException("业务线实例不存在");
        }
        String personalSignAuthType = null;

        //获取签章
        byte[] sealByte = signFileService.getByteById(vo.getSealAnnexId());
        if(sealByte == null){
            throw new PaasException("业务线实例-签章不存在");
        }
        //控件数据
        List<DocControlVo> controlList = vo.getControlList();
        if(controlList == null || controlList.size() == 0){
            throw new PaasException("业务线实例-控件不存在");
        }
        //操作类型
        Integer operateType = vo.getOperateType();
        OperateTypeEnum operateTypeEnum = OperateTypeEnum.getByCode(operateType);
        //保存控件数据
        SignTaskThreadlocalVO signTaskThreadlocalVO = SignTaskInfo.THREAD_LOCAL.get();
        List<SignRuDocControl> docControlList = saveControlList(controlList, null, signRu.getId(),signTaskThreadlocalVO.getUserTaskId());
        //签署记录
        SignRuOperateRecord operateRecord = null ;
        if(signRu.getCaSignType() == SignRuCaSignTypeEnum.CA.getCode() || signRu.getCaSignType() == SignRuCaSignTypeEnum.SYSTEM_CA.getCode()){
            operateRecord = new SignRuOperateRecord();
            operateRecord.setSignRuId(signRu.getId());
            operateRecord.setConfirmOrderNo(vo.getSignConfirmOrderNo());
            operateRecord.setTaskId(vo.getTaskId());
            operateRecord.setAccountId(vo.getUserId());
            operateRecord.setTenantId(vo.getTenantId());
            operateRecord.setTenantUserId(vo.getTenantUserId());

            if(signTaskThreadlocalVO.getUserType() != null && signTaskThreadlocalVO.getUserType() == SignerTypeEnum.RECEIVER_PERSONAL.getCode()) {
                operateRecord.setOperateType(SignRecordOperateTypeEnum.PRIVATE_SIGN.getType());
                SignRuSigner signer = signerService.getById(signTaskThreadlocalVO.getUserTaskId());
                // 获取签署人确认信息,并计算签署人的实名认证类型
                if (signer != null){
                    SignRuSignConfirm confirm = ruSignConfirmService.getByParam(signer.getId(),vo.getSignRuId());
                    if(confirm != null && MyStringUtils.isNotBlank(confirm.getPersonalSignAuth())){
                        personalSignAuthType = this.setSignNodeConfig(confirm,signRu);
                    }
                }
            }else {
                SignRuSender sender = senderService.getById(signTaskThreadlocalVO.getUserTaskId());
                if(sender != null && sender.getSenderType() != null && sender.getSenderType() == SenderTypeEnum.ENTERPRISE.getCode()){
                    operateRecord.setOperateType(SignRecordOperateTypeEnum.ENT_SIGN.getType());
                }else {
                    // SignRuSigner signer = signerService.getById(signTaskThreadlocalVO.getUserTaskId());
                    // 获取签署人确认信息，,并计算签署人的实名认证类型
                    if (sender != null && sender.getSenderType() != null && sender.getSenderType() != SenderTypeEnum.ENTERPRISE.getCode()){
                        SignRuSignConfirm confirm = ruSignConfirmService.getByParam(sender.getId(),vo.getSignRuId());
                        if(confirm != null && MyStringUtils.isNotBlank(confirm.getPersonalSignAuth())){
                            personalSignAuthType = this.setSignNodeConfig(confirm,signRu);
                        }
                    }
                    operateRecord.setOperateType(SignRecordOperateTypeEnum.PRIVATE_SIGN.getType());
                }
            }

            operateRecord.setActionType(SignRecordActionTypeEnum.SUBMIT_SIGN.getType());
            operateRecord.setIpAddr(ipAddr);
            operateRecord.setOperateTime(new Date());
        }
        //签署操作
        operate(docControlList,signRu,operateTypeEnum,vo.getCertTenantId(),sealByte,operateRecord,SignTypeEnum.AUTH_SIGN.getCode(),personalSignAuthType);
        //签署完成，修改状态
        SignRuSignTemporary temporary = new SignRuSignTemporary();
        temporary.setId(temporaryId);
        temporary.setStatus(SignRuSignTemporaryStatus.FINISHED.getCode());
        boolean updateTemporary = ruSignTemporaryService.updateById(temporary);
        if(!updateTemporary){
            throw new PaasException("业务线实例-签署失败");
        }
        //清除临时文件
        signFileService.delete(vo.getSealAnnexId());

        return signRu.getId() ;
    }


    /**
     * @Description #文件操作，填写或者签署
     * @Param []
     * @return void
     **/
    public void operate(List<SignRuDocControl> controlList,SignRu signRu,OperateTypeEnum operateTypeEnum, String certHolderTenantId, byte[] entSealByte, SignRuOperateRecord operateRecord,String signType,String personalSignAuthType) {

        if(OperateTypeEnum.WRITE.getCode().equals(operateTypeEnum.getCode())){
            //根据签约文件进行分组
            Map<String, List<SignRuDocControl>> docControlMap = controlList.stream().collect(Collectors.groupingBy(SignRuDocControl::getSignRuDocId));
            //获取所有需要填写的签约文件id列表
            List<String> docIdList = docControlMap.keySet().stream().collect(Collectors.toList());
            List<SignRuDocOperate> docOperateList = ruDocOperateService.listByParamCurrent(signRu.getId(), docIdList);
            if(docOperateList == null || docOperateList.size() == 0){
                throw new PaasException("业务线实例-没有签约文件");
            }
            Map<String, SignRuDocOperate> docOperateMap = docOperateList.stream().collect(Collectors.toMap(SignRuDocOperate::getDocId, o -> o, (k1, k2) -> k1));
            //获取最新签约操作文件列表
            List<String> annexIdList = docOperateList.stream().map(SignRuDocOperate::getAnnexId).collect(Collectors.toList());

            if(annexIdList == null || annexIdList.size() == 0){
                throw new PaasException("业务线实例-没有签约文件");
            }
            //获取真实签约操作文件数据
            Map<String,byte[]> docFileByteMap = new HashMap<>();
            for(String annexId : annexIdList){
                byte[] docFileByte = signFileService.getByteById(annexId);
                if(docFileByte == null || docFileByte.length == 0){
                    throw new PaasException("业务线实例-没有签约文件");
                }
                docFileByteMap.put(annexId,docFileByte);
            }
            Map<String,byte[]> newDocFileByteMap = new HashMap<>();
            //循环进行填写
            for(String docId : docIdList){
                List<SignRuDocControl> docControlList = docControlMap.get(docId);
                if(docControlList == null || docControlList.size() == 0){
                    continue;
                }
                SignRuDocOperate ruDocOperate = docOperateMap.get(docId);
                if(ruDocOperate == null || ruDocOperate.getAnnexId() == null){
                    continue;
                }
                byte[] docFileByte = docFileByteMap.get(ruDocOperate.getAnnexId());
                if(docFileByte == null){
                    continue;
                }
                long beforeOperateFile = System.currentTimeMillis();
                byte[] newDocFileByte = null ;
                if(OperateTypeEnum.WRITE.getCode().equals(operateTypeEnum.getCode())){
                    //签约文件进行填写
                    newDocFileByte = ruSignService.write(docControlList, docFileByte);
                    if(newDocFileByte == null){
                        throw new PaasException("业务线实例-填写失败");
                    }
                    newDocFileByteMap.put(docId,newDocFileByte);
                }
                long afterOperateFile = System.currentTimeMillis();
                System.out.println("耗时：操作文件：" + (afterOperateFile - beforeOperateFile) + "毫秒");
            }
            //保存最新签约文件及转图片等业务
            saveWriteNewOperateFile(newDocFileByteMap,docOperateMap);
        }else if(OperateTypeEnum.SIGN.getCode().equals(operateTypeEnum.getCode()) || OperateTypeEnum.SIGN_NO_CERT.getCode().equals(operateTypeEnum.getCode())){
            byte[] signSeal = null ;
            //最新操作文档map，key为docId，value为SignRuDocOperate对象
            Map<String, SignRuDocOperate> docOperateMap = new HashMap<>();
            //最新生成的签约文件可能会进行循环签署，创建map保存最新签约文件和docid的关系,key是docId，value是新生成的签约文件byte数组
            Map<String,byte[]> newDocFileByteMap = new HashMap<>();
            //控件关联文档map，key是控件id，value是该控件关联的文档的id列表
            Map<String,List<String>> controlRelationDocMap = new HashMap<>();
            //控件和控件属性关联map，key是控件id，value是该控件归属的属性列表
            Map<String,List<SignRuDocControlProperty>> controlPropertyMap = new HashMap<>();
            //用于存储云盾的签署结果,key是docId，value是签署结果
            Map<String, String> docSignHashMap =  new HashMap<>();
            //第一次循环遍历，获取控件对应的所有doc
            for(SignRuDocControl signRuDocControl : controlList){
                //获取控件属性
                List<SignRuDocControlProperty> propertyList = ruDocControlPropertyService.listByControlId(signRuDocControl.getId());
                if(propertyList == null || propertyList.size() == 0){
                    throw new PaasException("签署失败，控件属性缺失");
                }
                //保存关联关系
                controlPropertyMap.put(signRuDocControl.getId(),propertyList);
                //应用文档id列表
                List<String> ruDocIdList = new ArrayList<>();
                for(SignRuDocControlProperty controlProperty : propertyList){
                    if(controlProperty.getPropertyType().equals(ControlPropertyTypeEnum.RELATION_DOC.getCode())){
                        //获取关联的文档
                        ruDocIdList.add(controlProperty.getPropertyValue());
                    }
                }
                if(ruDocIdList.size() == 0){
                    throw new PaasException("签署失败，控件属性数据异常");
                }
                //保存控件和关联文档的关系
                controlRelationDocMap.put(signRuDocControl.getId(),ruDocIdList);
            }
            if(controlPropertyMap.size() == 0){
                throw new PaasException("签署失败，控件属性缺失");
            }
            if(controlRelationDocMap.size() == 0){
                throw new PaasException("业务线实例-没有签约文件");
            }
            //创建全部的控件关联的全部的文档
            List<String> ruDocIdList = new ArrayList<>();
            for(String key : controlRelationDocMap.keySet()){
                List<String> list = controlRelationDocMap.get(key);
                if(list != null && list.size() > 0){
                    ruDocIdList.addAll(list);
                }
            }
            if(ruDocIdList.size() == 0){
                throw new PaasException("业务线实例-没有签约文件");
            }
            //获取全部的文档最新文件
            List<SignRuDocOperate> docOperateList = ruDocOperateService.listByParamCurrent(signRu.getId(), ruDocIdList);
            if(docOperateList == null || docOperateList.size() == 0){
                throw new PaasException("业务线实例-没有签约文件");
            }


            SignRuTask signRuTask = ruTaskService.getById(operateRecord.getTaskId());

            String orderNo = null;
            if(signRuTask != null && MyStringUtils.isNotBlank(signRuTask.getOrderNo())){
                orderNo = signRuTask.getOrderNo();
            }


            for(SignRuDocOperate ruDocOperate : docOperateList){
                //存储关联关系
                docOperateMap.put(ruDocOperate.getDocId(),ruDocOperate);
                //获取文件
                byte[] docFileByte = signFileService.getByteById(ruDocOperate.getAnnexId());
                if(docFileByte == null || docFileByte.length == 0){
                    throw new PaasException("业务线实例-没有签约文件");
                }
                //存储关联关系
                newDocFileByteMap.put(ruDocOperate.getDocId(),docFileByte);

            }

            if(docOperateMap.size() == 0){
                throw new PaasException("业务线实例-没有签约文件");
            }
            if(newDocFileByteMap.size() == 0){
                throw new PaasException("业务线实例-没有签约文件");
            }

            // 再次遍历所有控件，进行真实签署,
            List<YundunSignPositionArrayData> yundunSignPositionArrayDatas = new ArrayList<>();
            for(SignRuDocControl signRuDocControl : controlList){
                if(signRuDocControl.getControlType().equals(ControlTypeEnum.SIGN_DATE.getName())
                        || signRuDocControl.getControlType().equals(ControlTypeEnum.DATE.getName())){
                    signRuDocControl.setValue(getDateFormat(signRuDocControl.getFormat()));
                    //如果是时间签章，则生成图片
                    int width = 180;
                    Integer height = 40;
                    if(MyStringUtils.isNotBlank(signRuDocControl.getWidth()) && MyStringUtils.isNotBlank(signRuDocControl.getHeight())){
                        width = Integer.valueOf(signRuDocControl.getWidth());
                        height = Integer.valueOf(signRuDocControl.getHeight());
                    }
                    signSeal = dateSealGenerateService.crateDateSeal(signRuDocControl.getValue(), width, height);
                }else {
                    signSeal = entSealByte ;
                }
                //获取该控件对应的属性列表
                List<SignRuDocControlProperty> propertyList = controlPropertyMap.get(signRuDocControl.getId());
                if(propertyList == null || propertyList.size() == 0){
                    throw new PaasException("签署失败，控件属性缺失");
                }
                ControlPropertyTypePageConfigEnum pageConfigEnum = null ;
                String pageConfigValue = null ;
                for(SignRuDocControlProperty controlProperty : propertyList){
                    if(controlProperty.getPropertyType().equals(ControlPropertyTypeEnum.PAGE_CONFIG.getCode())){
                        //如果不是指定页面
                        ControlPropertyTypePageConfigEnum value = ControlPropertyTypePageConfigEnum.getValue(controlProperty.getPropertyValue());
                        if(value != null){
                            pageConfigEnum = value ;
                            pageConfigValue = controlProperty.getPropertyValue();
                        }
                    }
                }
                if(pageConfigEnum == null){
                    throw new PaasException("签署失败，控件属性数据异常");
                }
                if(pageConfigEnum.getCode().equals(ControlPropertyTypePageConfigEnum.CUSTOM.getCode())){
                    //如果是指定页
                    for(SignRuDocControlProperty controlProperty : propertyList){
                        if(controlProperty.getPropertyType().equals(ControlPropertyTypePageConfigEnum.CUSTOM.getCode())){
                            //解析应用到的页面
                            pageConfigValue = controlProperty.getPropertyValue() ;
                        }
                    }
                }
                if(pageConfigValue == null || pageConfigValue.length() == 0){
                    throw new PaasException("签署失败，控件属性数据异常");
                }
                //获取该控件关联的文档列表
                List<String> controlDocIdList = controlRelationDocMap.get(signRuDocControl.getId());
                if(controlDocIdList == null || controlDocIdList.size() == 0){
                    throw new PaasException("业务线实例-没有签约文件");
                }
                //循环文件获取签名位置
                for(String docId : controlDocIdList){
                    //获取该文档对应的最新文件数组
                    byte[] docFileByte = newDocFileByteMap.get(docId) ;
                    if(docFileByte == null || docFileByte.length == 0){
                        throw new PaasException("业务线实例-没有签约文件");
                    }
                    if(OperateTypeEnum.SIGN.getCode().equals(operateTypeEnum.getCode())){
                        YundunSignPositionArrayData yundunSignPositionArrayData = new YundunSignPositionArrayData();
                        yundunSignPositionArrayData.setDocId(docId);
                        yundunSignPositionArrayData.setYundunSignPositionDataList(ruSignService.generateSignPosition(signRuDocControl, docFileByte, signSeal,pageConfigEnum,pageConfigValue));
                        yundunSignPositionArrayDatas.add(yundunSignPositionArrayData);

                    }else if(OperateTypeEnum.SIGN_NO_CERT.getCode().equals(operateTypeEnum.getCode())){
                        YundunSignPositionArrayData yundunSignPositionArrayData = new YundunSignPositionArrayData();
                        yundunSignPositionArrayData.setDocId(docId);
                        yundunSignPositionArrayData.setYundunSignPositionDataList(ruSignService.generateSignPosition(signRuDocControl, docFileByte, signSeal,pageConfigEnum,pageConfigValue));
                        yundunSignPositionArrayDatas.add(yundunSignPositionArrayData);
                    }
                }
            }

            SignAppInfoResponse signAppInfoResponse;

            try{
                signAppInfoResponse = signServiceManageExternal.querySignAppInfo();
            } catch (Exception e) {
                throw new PaasException("业务线实例-未开通开放签签署服务");
            }

            // 构建签署参数
            PdfSignVoInfo pdfSignVoInfo = new PdfSignVoInfo();
            pdfSignVoInfo.setSignRu(signRu);
            pdfSignVoInfo.setAppName(signAppInfoResponse.getAppName());
            pdfSignVoInfo.setAppId(appId);
            pdfSignVoInfo.setNewDocFileByteMap(newDocFileByteMap);
            pdfSignVoInfo.setEntSealByte(entSealByte);
            pdfSignVoInfo.setOrderNo(orderNo);
            pdfSignVoInfo.setCertHolderTenantId(certHolderTenantId);
            pdfSignVoInfo.setYundunSignPositionArrayDatas(yundunSignPositionArrayDatas);
            pdfSignVoInfo.setSignType(signType);
            pdfSignVoInfo.setPersonalSignAuthType(personalSignAuthType);

            // 执行签署
            newDocFileByteMap = pdfSignService.signWithYundunHash(pdfSignVoInfo);

            if(newDocFileByteMap.size() == 0 || docOperateMap.size() == 0){
                //签署失败回调
                signFiledCallback();
                throw new PaasException("业务线实例-签署失败");
            }
            //保存最新的签约文件
            for(String docId : newDocFileByteMap.keySet()){
                byte[] newDocFileByte = newDocFileByteMap.get(docId);
                if(newDocFileByte == null || newDocFileByte.length == 0){
                    //签署失败回调
                    signFiledCallback();
                    throw new PaasException("业务线实例-签署失败");
                }
                //获取上一个操作文件记录
                SignRuDocOperate ruDocOperate = docOperateMap.get(docId);
                if(ruDocOperate == null){
                    //签署失败回调
                    signFiledCallback();
                    throw new PaasException("业务线实例-签署失败");
                }
            }
            if(operateRecord != null){
                String imageAnnexId = signFileService.saveAnnexStorage(entSealByte, SignFileEnum.SIGN_FILE_IMAGE, null);
                operateRecord.setImageAnnexId(imageAnnexId);
            }
            //保存最新签约文件及转图片等业务
            saveSignNewOperateFile(newDocFileByteMap,docOperateMap,operateRecord);

        }
    }

    public void signFiledCallback(){
        SignTaskThreadlocalVO signTaskThreadlocalVO = SignTaskInfo.THREAD_LOCAL.get();
        String signRuId = signTaskThreadlocalVO.getSignRuId();
        String taskId = signTaskThreadlocalVO.getTaskId();
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        //修改签署截止时间
        ruCallbackService.callback(signRuId,taskId,SignCallbackTypeEnum.SIGN_FAILED);
    }




    /**
     * @Description #保存最新操作之后的签约文件，转图片，存储图片数据
     * @Param [newDocFileByte 最新文件, ruDocOperate 原签约文件操作记录数据]
     * @return void
     **/
    public void saveWriteNewOperateFile(Map<String,byte[]> newDocFileByteMap,Map<String, SignRuDocOperate> docOperateMap){

        for(String docId : newDocFileByteMap.keySet()){
            byte[] newDocFileByte = newDocFileByteMap.get(docId);
            if(newDocFileByte == null || newDocFileByte.length == 0){
                //签署失败回调
                signFiledCallback();
                throw new PaasException("业务线实例-填写失败");
            }

            //获取上一个操作文件记录
            SignRuDocOperate ruDocOperate = docOperateMap.get(docId);
            if(ruDocOperate == null){
                //签署失败回调
                signFiledCallback();
                throw new PaasException("业务线实例-填写失败");
            }

            AnnexStorage annex = signFileService.getAnnexById(ruDocOperate.getAnnexId());
            if(annex == null){
                throw new PaasException("业务线实例-保存签约文件失败");
            }
            String newAnnexId = signFileService.saveAnnexStorage(newDocFileByte, SignFileEnum.SIGN_FILE_MAIN, null,annex.getRealName());
            if(newAnnexId == null){
                throw new PaasException("业务线实例-保存签约文件失败");
            }
            SignRuDocOperate newRuDocOperate = new SignRuDocOperate();
            newRuDocOperate.setSignRuId(ruDocOperate.getSignRuId());
            newRuDocOperate.setDocId(ruDocOperate.getDocId());
            newRuDocOperate.setIsCurrent(SignCurrentEnum.IS_CURRENT.getCode());
            newRuDocOperate.setAnnexId(newAnnexId);
            newRuDocOperate.setDeleteFlag(false);

            //构建保存操作数据对象
            List<RuOperateData> operateDataList = new ArrayList<>();
            RuOperateData operateData = new RuOperateData();
            operateData.setOldRuDocOperate(ruDocOperate);
            operateData.setNewRuDocOperate(newRuDocOperate);
            operateData.setNewDocFileByte(newDocFileByte);
            operateData.setNewAnnexId(newAnnexId);

            operateDataList.add(operateData);
            //统一保存
            ruDataService.saveOperateData(operateDataList,null);
        }

    }


    public void saveSignNewOperateFile(Map<String,byte[]> newDocFileByteMap,Map<String, SignRuDocOperate> docOperateMap,SignRuOperateRecord operateRecord){
        List<RuOperateData> operateDataList = new ArrayList<>();

        //保存最新的签约文件
        for(String docId : newDocFileByteMap.keySet()){
            byte[] newDocFileByte = newDocFileByteMap.get(docId);
            if(newDocFileByte == null || newDocFileByte.length == 0){
                //签署失败回调
                signFiledCallback();
                throw new PaasException("业务线实例-签署失败");
            }
            //获取上一个操作文件记录
            SignRuDocOperate ruDocOperate = docOperateMap.get(docId);
            if(ruDocOperate == null){
                //签署失败回调
                signFiledCallback();
                throw new PaasException("业务线实例-签署失败");
            }
            //保存最新签约文件及转图片等业务
            AnnexStorage annex = signFileService.getAnnexById(ruDocOperate.getAnnexId());
            if(annex == null){
                throw new PaasException("业务线实例-保存签约文件失败");
            }

            String newAnnexId = signFileService.saveAnnexStorage(newDocFileByte, SignFileEnum.SIGN_FILE_MAIN, null,annex.getRealName());
            if(newAnnexId == null){
                throw new PaasException("业务线实例-保存签约文件失败");
            }

            SignRuDocOperate newRuDocOperate = new SignRuDocOperate();
            newRuDocOperate.setSignRuId(ruDocOperate.getSignRuId());
            newRuDocOperate.setDocId(ruDocOperate.getDocId());
            newRuDocOperate.setIsCurrent(SignCurrentEnum.IS_CURRENT.getCode());
            newRuDocOperate.setAnnexId(newAnnexId);
            newRuDocOperate.setDeleteFlag(false);

            RuOperateData operateData = new RuOperateData();
            operateData.setOldRuDocOperate(ruDocOperate);
            operateData.setNewRuDocOperate(newRuDocOperate);
            operateData.setNewDocFileByte(newDocFileByte);
            operateData.setNewAnnexId(newAnnexId);

            operateDataList.add(operateData);

        }

        //统一保存
        ruDataService.saveOperateData(operateDataList,operateRecord);
    }

    public String verifyControl(List<DocControlVo> controlVoList) {

        for (DocControlVo controlVo : controlVoList) {

            if (controlVo.getControlType() == null || controlVo.getControlType().length() == 0) {
                return "控件类型-参数缺失";
            }
            if (controlVo.getOffsetX() == null || controlVo.getOffsetX().length() == 0) {
                return "控件X坐标(左上角)-参数缺失";
            }
            if (controlVo.getOffsetY() == null || controlVo.getOffsetY().length() == 0) {
                return "控件Y坐标(左上角)-参数缺失";
            }
            if (controlVo.getWidth() == null || controlVo.getWidth().length() == 0) {
                return "控件宽度-参数缺失";
            }
            if (controlVo.getHeight() == null || controlVo.getHeight().length() == 0) {
                return "控件高度-参数缺失";
            }

            if (controlVo.getPageWidth() == null || controlVo.getPageWidth().length() == 0) {
                return "当前文件页面宽度-参数缺失";
            }
            if (controlVo.getPageHeight() == null || controlVo.getPageHeight().length() == 0) {
                return "当前文件页面高度-参数缺失";
            }
            if(ControlTypeEnum.getSignList().contains(controlVo.getControlType())){
                if(controlVo.getPropertyVoList() == null || controlVo.getPropertyVoList().size() == 0){
                    return "控件属性-参数缺失";
                }
                Boolean relationDocStatusError = true ;
                Boolean pageConfigStatusError = true ;
                List<ControlPropertyVo> propertyVoList = controlVo.getPropertyVoList();
                for(ControlPropertyVo propertyVo : propertyVoList){
                    if(propertyVo.getPropertyType() == null || propertyVo.getPropertyType().length() == 0){
                        return "控件属性-属性类型-参数缺失";
                    }
                    //应用文档
                    if(ControlPropertyTypeEnum.RELATION_DOC.getCode().equals(propertyVo.getPropertyType()) && propertyVo.getPropertyValue() != null){
                        relationDocStatusError = false ;
                    }
                    //应用页面
                    if(ControlPropertyTypeEnum.PAGE_CONFIG.getCode().equals(propertyVo.getPropertyType()) && propertyVo.getPropertyValue() != null){
                        pageConfigStatusError = false ;
                    }
                }
                if(relationDocStatusError){
                    return "控件属性-应用文档-参数缺失";
                }
                if(pageConfigStatusError){
                    return "控件属性-应用页面-参数缺失";
                }
            }else {
                if (controlVo.getPage() == null) {
                    return "控件所属页码-参数缺失";
                }
                if (controlVo.getSignRuDocId() == null || controlVo.getSignRuDocId().length() == 0) {
                    return "业务线实例-签约文件id-参数缺失";
                }
                if(controlVo.getControlType().equals(ControlTypeEnum.LINE_TEXT.getName()) || controlVo.getControlType().equals(ControlTypeEnum.MULTILINE_TEXT.getName())
                        || controlVo.getControlType().equals(ControlTypeEnum.DATE.getName()) || controlVo.getControlType().equals(ControlTypeEnum.NUMBER.getName())
                        ||controlVo.getControlType().equals(ControlTypeEnum.DROPDOWN_LIST.getName())){
                    if(controlVo.getFontFamily() == null || controlVo.getFontFamily().length() == 0){
                        return "控件字体缺失";
                    }
                    if(controlVo.getFontSize() == null){
                        return "控件字体大小缺失";
                    }
                }
            }
        }
        return null;
    }

    public String getDateFormat(String controlFormat){
        String format = null;
        if(controlFormat == null || controlFormat.length() == 0){
            format = "yyyy年MM月dd日";
        }else if("YYYY/MM/DD".equals(controlFormat) || "yyyy/MM/dd".equals(controlFormat)){
            format = "yyyy/MM/dd";
        }else if("YYYY-MM-DD".equals(controlFormat) || "yyyy-MM-dd".equals(controlFormat)){
            format = "yyyy-MM-dd";
        }else if("yyyy年MM月dd日".equals(controlFormat) || "YYYY年MM月DD日".equals(controlFormat)){
            format = "yyyy年MM月dd日";
        }else {
            format = "yyyy年MM月dd日";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateValue = sdf.format(new Date());
        return dateValue ;
    }


    /**
     * @Description #获取系统级签署意愿校验（目前只有人脸）
     * @Param []
     * @return java.lang.Boolean
     **/
    public Boolean getSystemConfirmType(){
        Boolean signConfirmType = false ;
        SysConfig config = sysConfigService.getByType("sign_confirm_type");
        if (config != null && config.getValue() != null) {
            if(config.getValue().equals("true")){
                signConfirmType = true ;
            }
        }
        return signConfirmType ;
    }

    /**
     * @Description #获取系统级个人签署节点实名认证配置
     * @Param []
     * @return java.lang.Boolean
     **/
    public String getSystemPersonalSignAuthType(){
        String personalSignAuthTypeValue = null ;
        SysConfig config = sysConfigService.getByType("personal_sign_auth");
        if (config != null && MyStringUtils.isNotBlank(config.getValue())) {
            personalSignAuthTypeValue = config.getValue();
        }else {
            personalSignAuthTypeValue = PersonalSignAuthTypeEnum.REQUIRED.getType();
        }
        return personalSignAuthTypeValue ;
    }


    public Boolean signConfirm(String taskId){

        SignRuTask ruTask = ruTaskService.getById(taskId);
        if(ruTask == null){
            throw new PaasException("任务不存在");
        }

        Boolean systemConfirmType = getSystemConfirmType();
        if(!systemConfirmType){
            //如果系统级关闭了人脸校验，则直接返回
            return false ;
        }

        SignRu signRu = ruService.getById(ruTask.getSignRuId());
        if(signRu == null){
            throw new PaasException("业务线实例不存在");
        }

        SignRe signRe = reService.getById(signRu.getSignReId());
        if(signRe == null){
            throw new PaasException("业务线配置不存在");
        }

        if(signRe.getCaSignType() == null || signRe.getCaSignType().equals(SignRuCaSignTypeEnum.SYSTEM_CA.getCode()) ||
                signRe.getCaSignType().equals(SignRuCaSignTypeEnum.NO_USE_CA.getCode()) ){
            //如果是防篡改证书或不使用证书，则直接返回
            return false ;
        }
        Boolean certSysConfig = certBusinessService.getCertSysConfig();
        if(certSysConfig == null || !certSysConfig){
            //使用测试CA证书
            return false ;
        }

        Integer userType = ruTask.getUserType();
        String signerId = null ;
        if(userType == SignerTypeEnum.RECEIVER_PERSONAL.getCode()){
            SignRuSigner signer = signerService.getById(ruTask.getUserTaskId());
            if(signer != null){
                signerId = signer.getId();
            }
        }else {
            SignRuSender sender = senderService.getById(ruTask.getUserTaskId());
            if(sender != null){
                signerId = sender.getId();
            }
        }
        if(signerId == null){
            throw new PaasException("签署人不存在");
        }
        SignRuSignConfirm ruSignConfirm = ruSignConfirmService.getByParam(signerId, ruTask.getSignRuId());
        if(ruSignConfirm != null){
            return true ;
        }else {
            return false ;
        }
    }

    public byte[] signDownloadFile(byte[] pdfFile,Integer status){

        if(status == SignRuStatusEnum.DRAFT.getCode() || status == SignRuStatusEnum.DONE.getCode()){
            return pdfFile ;
        }
        //文档数据
        Float realPdfHeight = null ;
        Float realPdfWidth = null ;
        try {
            PDDocument document = Loader.loadPDF(pdfFile);
            //将pdf文件读入PdfReader工具类
            PDPage page = document.getPage(0);
            if(page == null){
                throw new PaasException("pdf文件解析失败，无页码");
            }
            PDRectangle mediaBox = page.getMediaBox();
            if(mediaBox == null){
                throw new PaasException("pdf文件解析失败");
            }
            //获取真实pdf文件指定页的真实文档宽高
            realPdfHeight= mediaBox.getHeight();
            realPdfWidth = mediaBox.getWidth();
            if(page.getRotation() == 90 || page.getRotation() == 270){
                realPdfWidth = mediaBox.getHeight();
                realPdfHeight = mediaBox.getWidth();
            }
        } catch (Exception e) {
            throw new PaasException("pdf文件解析失败");
        }

        //图片数据
        SignRuStatusEnum code = SignRuStatusEnum.getCode(status);
        String path = fileProperties.getPath().getPath() + code.getType() + ".png";
        byte[] entSealByte = null;
        try {
            entSealByte = IOUtils.toByteArray(new FileInputStream(new File(path)));
        } catch (Exception e) {
            throw new PaasException("签章数据不存在");
        }
        if(entSealByte == null){
            throw new PaasException("签章数据不存在");
        }
        BufferedImage image = null ;
        try {
            image = ImageIO.read(new ByteArrayInputStream(entSealByte));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //证书数据
        byte[] certByte = null ;
        try {
            String certPath = fileProperties.getPath().getPath() + "download_file_ca.pfx";
            certByte = IOUtils.toByteArray(new FileInputStream(new File(certPath)));
        } catch (Exception e) {
            throw new PaasException("证书数据不存在");
        }
        if(certByte == null){
            throw new PaasException("证书数据不存在");
        }
        CertificateInfo certInfo = new CertificateInfo();
        certInfo.setCertType(CertificateInfo.CertTypeEnum.PKCS12);
        certInfo.setPassword("123456");
        certInfo.setCert(certByte);
        //位置数据
        RealPositionProperty realPositionProperty = new RealPositionProperty();
        realPositionProperty.setStartx(realPdfWidth - 130);
        realPositionProperty.setEndx(realPdfWidth - 10);
        realPositionProperty.setStarty(realPdfHeight - 130);
        realPositionProperty.setEndy(realPdfHeight - 10);
        realPositionProperty.setRealPdfHeight(realPdfHeight);
        realPositionProperty.setRealPdfWidth(realPdfWidth);
        realPositionProperty.setPageNum(0);
        realPositionProperty.setControlType(ControlTypeEnum.SEAL.getName());
        //进行签署
        byte[] sign = pdfboxService.sign(pdfFile, entSealByte, certInfo, realPositionProperty);


        return sign ;
    }

    public byte[] signReportFile(byte[] pdfFile){


        //文档数据
        Float realPdfHeight = null ;
        Float realPdfWidth = null ;
        try {

            PDDocument document = Loader.loadPDF(pdfFile);
            //将pdf文件读入PdfReader工具类
            PDPage page = document.getPage(0);
            if(page == null){
                throw new PaasException("pdf文件解析失败，无页码");
            }
            PDRectangle mediaBox = page.getMediaBox();
            if(mediaBox == null){
                throw new PaasException("pdf文件解析失败");
            }
            //获取真实pdf文件指定页的真实文档宽高
            realPdfHeight= mediaBox.getHeight();
            realPdfWidth = mediaBox.getWidth();
            if(page.getRotation() == 90 || page.getRotation() == 270){
                realPdfWidth = mediaBox.getHeight();
                realPdfHeight = mediaBox.getWidth();
            }
        } catch (Exception e) {
            throw new PaasException("pdf文件解析失败");
        }

        //图片数据
        String path = fileProperties.getPath().getPath() + "sign_report.png";
        byte[] entSealByte = null;
        try {
            entSealByte = IOUtils.toByteArray(new FileInputStream(new File(path)));
        } catch (Exception e) {
            throw new PaasException("签章数据不存在");
        }
        if(entSealByte == null){
            throw new PaasException("签章数据不存在");
        }
        BufferedImage image = null ;
        try {
            image = ImageIO.read(new ByteArrayInputStream(entSealByte));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //证书数据
        byte[] certByte = null ;
        try {
            String certPath = fileProperties.getPath().getPath() + "download_file_ca.pfx";
            certByte = IOUtils.toByteArray(new FileInputStream(new File(certPath)));
        } catch (Exception e) {
            throw new PaasException("证书数据不存在");
        }
        if(certByte == null){
            throw new PaasException("证书数据不存在");
        }
        CertificateInfo certInfo = new CertificateInfo();
        certInfo.setCertType(CertificateInfo.CertTypeEnum.PKCS12);
        certInfo.setPassword("123456");
        certInfo.setCert(certByte);
        //位置数据
        RealPositionProperty realPositionProperty = new RealPositionProperty();
        try {
            float[] floats = new SelectKeywords().selectKeyword(pdfFile, "报告生成时间");
            if(floats != null && floats.length >= 2){
                realPositionProperty.setStartx(floats[0]);
                realPositionProperty.setEndx(floats[0] + 130);
                realPositionProperty.setStarty(floats[1] - 65);
                realPositionProperty.setEndy(floats[1] - 195);
                int pageNum = (int)floats[2];
                if(pageNum > 0){
                    pageNum = pageNum - 1;
                }
                realPositionProperty.setPageNum(pageNum);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        realPositionProperty.setRealPdfHeight(realPdfHeight);
        realPositionProperty.setRealPdfWidth(realPdfWidth);

        realPositionProperty.setControlType(ControlTypeEnum.SEAL.getName());
        //进行签署
        byte[] sign = pdfboxService.sign(pdfFile, entSealByte, certInfo, realPositionProperty);
//        try {
//            IOUtils.write(sign,new FileOutputStream(new File("/Users/kaifangqian/test/0705_report.pdf")));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        return sign ;
    }

    /**
     * 根据系统配置、签署实例、签署节点校验并设置签署节点配置
     * @param confirm
     * @param signRu
     */
    private String setSignNodeConfig(SignRuSignConfirm confirm,SignRu signRu){

        // 获取平台个人实名认证节点配置
        String personalSignAuth = this.getSystemPersonalSignAuthType();

        // 默认设置为需实名
        String finalPersonalSignAuth = PersonalSignAuthTypeEnum.REQUIRED.getType();

        // 获取平台级个人实名类型配置，如果配置了，则使用配置的，否则设置为需实名
        if (MyStringUtils.isNotBlank(personalSignAuth)) {
            // 如果平台配置为无须实名或需实名，则直接使用平台的配置
            if (personalSignAuth.equals(PersonalSignAuthTypeEnum.NOT_REQUIRED.getType()) ||
                personalSignAuth.equals(PersonalSignAuthTypeEnum.REQUIRED.getType())) {
                finalPersonalSignAuth = personalSignAuth;
            }
            // 如果平台配置为允许不实名认证，则判断签署实例的实名认证要求
            else if (personalSignAuth.equals(PersonalSignAuthTypeEnum.ALLOWED.getType())) {
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