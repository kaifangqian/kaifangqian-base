/**
 * @description 业务线管理服务
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
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.pdfbox.PdfboxService;
import com.kaifangqian.modules.opensign.service.business.vo.*;
import com.kaifangqian.modules.opensign.service.re.*;
import com.kaifangqian.modules.opensign.service.template.SignTemplateControlService;
import com.kaifangqian.modules.opensign.service.template.SignTemplateRecordService;
import com.kaifangqian.modules.opensign.service.template.SignTemplateService;
import com.kaifangqian.modules.opensign.vo.base.sign.*;
import com.kaifangqian.modules.system.entity.SysTenantUser;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.constant.GlobalIdInfoType;
import com.kaifangqian.entity.GlobalIdConfig;
import com.kaifangqian.entity.GlobalIdConfigInfo;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.service.annex.AnnexImageService;
import com.kaifangqian.modules.opensign.service.business.vo.*;
import com.kaifangqian.modules.opensign.service.re.*;
import com.kaifangqian.modules.opensign.service.seal.SignEntSealService;
import com.kaifangqian.modules.opensign.service.tool.SignFileService;
import com.kaifangqian.modules.opensign.vo.base.sign.*;
import com.kaifangqian.modules.system.service.ISysTenantInfoService;
import com.kaifangqian.modules.system.service.ISysTenantUserService;
import com.kaifangqian.service.IGlobalIdConfigInfoService;
import com.kaifangqian.service.IGlobalIdConfigService;
import com.kaifangqian.vo.GlobalIdConfigModule;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: ReBusinessService
 * @Package: com.kaifangqian.modules.opensign.service.business
 * @ClassName: ReBusinessService
 * @author: FengLai_Gong
 */
@Service
public class ReBusinessService {


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


    @Autowired
    private ReDataService reDataService ;

    public String generateCode(String reId,List<SignRuSigner> singerList){
        return generateRule(reId,SignReRuleTypeEnum.SIGN_RE_CODE.getCode(),singerList);
    }

    public String generateSubject(String reId,List<SignRuSigner> singerList){
        return generateRule(reId,SignReRuleTypeEnum.SIGN_RE_SUBJECT.getCode(),singerList);
    }

    public String generateRule(String reId,Integer ruleType,List<SignRuSigner> singerList){
        String generateNum = null ;
        ReRuleVo ruleVo = getRuleVo(reId, ruleType);
        if(ruleVo != null && ruleVo.getDetailVoList() != null && ruleVo.getDetailVoList().size() > 0){
            List<ReRuleDetailVo> detailVoList = ruleVo.getDetailVoList();
            List<String> paramList = new ArrayList<>();
            detailVoList = detailVoList.stream().sorted(Comparator.comparing(ReRuleDetailVo::getContentOrder)).collect(Collectors.toList());
            for(ReRuleDetailVo detailVo : detailVoList){
                String param = null ;
                //业务线名称
                if(detailVo.getContentType().equals(SignReRuleDetailContentTypeEnum.BUSINESS_LINE_NAME.getCode())){
                    SignRe re = reService.getById(reId);
                    if(re != null){
                        paramList.add(re.getName());
                    }
                }
                //发起人名称
                else if(detailVo.getContentType().equals(SignReRuleDetailContentTypeEnum.SENDER_NAME.getCode())){
                    for(SignRuSigner signer : singerList){
                        if(signer.getSignerType().equals(SignerTypeEnum.SENDER.getCode())){
                            paramList.add(signer.getSignerName());
                        }
                    }
                }
                //接收方名称
                else if(detailVo.getContentType().equals(SignReRuleDetailContentTypeEnum.RECEIVER_NAME.getCode())){
                    String value = "";
                    for(SignRuSigner signer : singerList){
                        if(signer.getSignerType().equals(SignerTypeEnum.RECEIVER_PERSONAL.getCode())
                            || signer.getSignerType().equals(SignerTypeEnum.RECEIVER_ENT.getCode())){
                            if(value.length() == 0){
                                value = value + signer.getSignerName() ;
                            }else {
                                value = value + ruleVo.getLink() + signer.getSignerName() ;
                            }
                        }
                    }
                    if(value.length() > 0){
                        paramList.add(value);
                    }
                }
                if(param != null){
                    paramList.add(param);
                }
            }
            String[] paramArray = null ;
            if(paramList.size() > 0){
                paramArray = new String[paramList.size()];
                for(int i = 0 ; i < paramList.size() ; i++){
                    paramArray[i] = paramList.get(i);
                }
            }
            if(paramArray != null){
                generateNum = globalIdConfigService.getIdByConfigId(ruleVo.getRuleGenerateId(),paramArray);
            }else {
                generateNum = globalIdConfigService.getIdByConfigId(ruleVo.getRuleGenerateId());
            }

        }
        return  generateNum;
    }


    public String init(SysTenantUser tenantUser){
        ReSaveData data = new ReSaveData();
        //初始化业务线
        SignRe re = new SignRe();
        re.setName("默认");
        re.setCodeType(SignReRuleGenerateEnum.CUSTOM.getCode());
        re.setSubjectType(SignReRuleGenerateEnum.CUSTOM.getCode());
        re.setSignerType(SignReSignerTypeEnum.CUSTOM.getCode());
        re.setSignOrderType(SignOrderTypeEnum.ORDER.getCode());
        re.setCcedType(SignOnOrOffEnum.NO.getCode());
        re.setAddCcerType(SignOnOrOffEnum.YES.getCode());
        re.setExternalCcerType(SignOnOrOffEnum.YES.getCode());
        re.setInternalCcerType(SignOnOrOffEnum.YES.getCode());
        re.setCcedOpportunityType(SignCcedOpportunityTypeEnum.AFTER_START.getCode());
        re.setAddFileType(SignOnOrOffEnum.YES.getCode());
        re.setDeleteFileType(SignOnOrOffEnum.YES.getCode());
        re.setAddAnnexType(SignOnOrOffEnum.YES.getCode());
        re.setCaSignType(SignRuCaSignTypeEnum.CA.getCode());
        re.setBeforeSignApproveType(SignOnOrOffEnum.NO.getCode());
        re.setBeforeStartApproveType(SignOnOrOffEnum.NO.getCode());
        re.setDownloaderType(SignDownloaderTypeEnum.ALL.getCode());
        re.setDeleteFlag(false);
        re.setStatus(SignReStatusEnum.ENABLE.getCode());
        re.setErrorStatus(SignReErrorStatusEnum.NO.getCode());
        //控件变更状态，默认状态
        re.setControlChangeFlag(ControlChangeFlagEnum.NECESSARY_AND_ADD.getType());

        re.setSysTenantId(tenantUser.getTenantId());
        re.setSysUserId(tenantUser.getId());
        re.setSysAccountId(tenantUser.getUserId());
        data.setRe(re);
        //初始化业务线权限
        SignReAuth manageAuth = new SignReAuth();
        manageAuth.setTenantId(tenantUser.getTenantId());
        manageAuth.setUserId(tenantUser.getId());
        manageAuth.setAuthType(SignReAuthTypeEnum.MANAGER.getCode());
        manageAuth.setDeleteFlag(false);
        data.getAddAuthList().add(manageAuth);

        SignReAuth userAuth = new SignReAuth();
        userAuth.setSignReId(re.getId());
        userAuth.setAuthType(SignReAuthTypeEnum.USER.getCode());
        userAuth.setTenantId(tenantUser.getTenantId());
        userAuth.setDeleteFlag(false);
        data.getAddAuthList().add(userAuth);
        //初始化文件夹
        SignReFolder folder = new SignReFolder();
        folder.setName("全部");
        folder.setDeleteFlag(false);
        folder.setSysAccountId(tenantUser.getUserId());
        folder.setSysTenantId(tenantUser.getTenantId());
        folder.setSysUserId(tenantUser.getId());
        data.setReFolder(folder);

        //关联文件夹
        SignReFolderRelation relation = new SignReFolderRelation();
        relation.setDeleteFlag(false);
        relation.setSysTenantId(tenantUser.getTenantId());
        relation.setSysAccountId(tenantUser.getUserId());
        relation.setSysUserId(tenantUser.getId());
        data.setReFolderRelation(relation);
        //整体保存
        reDataService.createData(data);
        return data.getReId() ;
    }


    /**
     * @Description #新建业务线配置
     * @Param [name 业务线配置名称, folderId分组id]
     * @return java.lang.String
     **/
    @Transactional(rollbackFor = Exception.class)
    public String create(String name,String folderId){
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        ReSaveData data = new ReSaveData();
        SignRe re = new SignRe();
        re.setName(name);
        //默认配置
        re.setCodeType(SignReRuleGenerateEnum.CUSTOM.getCode());
        re.setSubjectType(SignReRuleGenerateEnum.CUSTOM.getCode());
        re.setSignerType(SignReSignerTypeEnum.CUSTOM.getCode());
        re.setSignOrderType(SignOrderTypeEnum.ORDER.getCode());
        re.setCcedType(SignOnOrOffEnum.NO.getCode());
        re.setAddCcerType(SignOnOrOffEnum.YES.getCode());
        re.setExternalCcerType(SignOnOrOffEnum.YES.getCode());
        re.setInternalCcerType(SignOnOrOffEnum.YES.getCode());
        re.setCcedOpportunityType(SignCcedOpportunityTypeEnum.AFTER_START.getCode());
        re.setAddFileType(SignOnOrOffEnum.YES.getCode());
        re.setDeleteFileType(SignOnOrOffEnum.YES.getCode());
        re.setAddAnnexType(SignOnOrOffEnum.YES.getCode());
        re.setCaSignType(SignRuCaSignTypeEnum.CA.getCode());
        re.setBeforeSignApproveType(SignOnOrOffEnum.NO.getCode());
        re.setBeforeStartApproveType(SignOnOrOffEnum.NO.getCode());
        re.setDownloaderType(SignDownloaderTypeEnum.ALL.getCode());
        re.setErrorStatus(SignReErrorStatusEnum.NO.getCode());
        re.setDeleteFlag(false);
        re.setStatus(SignReStatusEnum.UN_ENABLE.getCode());
        //控件变更状态，默认状态
        re.setControlChangeFlag(ControlChangeFlagEnum.NECESSARY_AND_ADD.getType());
        data.setRe(re);
        //设置分组
        SignReFolderRelation relation = new SignReFolderRelation();
        relation.setSignReFolderId(folderId);
        relation.setDeleteFlag(false);
        data.setReFolderRelation(relation);
        //设置默认权限
        List<SignReAuth> addAuthList = data.getAddAuthList();
        //管理员权限
        SignReAuth manageAuth = new SignReAuth();
        manageAuth.setAuthType(SignReAuthTypeEnum.MANAGER.getCode());
        manageAuth.setSignReId(re.getId());
        manageAuth.setUserId(currentUser.getTenantUserId());
        manageAuth.setTenantId(currentUser.getTenantId());
        manageAuth.setDeleteFlag(false);
        addAuthList.add(manageAuth);
        //使用权限
        SignReAuth userAuth = new SignReAuth();
        userAuth.setAuthType(SignReAuthTypeEnum.USER.getCode());
        userAuth.setSignReId(re.getId());
        userAuth.setUserId(null);
        userAuth.setTenantId(currentUser.getTenantId());
        userAuth.setDeleteFlag(false);
        addAuthList.add(userAuth);

        //整体保存
        reDataService.createData(data);
        return data.getReId() ;
    }

    /**
     * @Description #TODO方法说明
     * @Param [request]
     * @return void
     **/
    public String save(BaseVo request){
        DocBaseVo baseVo = request.getBaseVo();

        String reId = baseVo.getSignReId();
        SignRe re = reService.getById(reId);
        if(re == null){
            throw new PaasException("业务线不存在");
        }
        //创建整体更新数据
        ReSaveData saveData = new ReSaveData();
        //整理封装re主数据
        BeanUtils.copyProperties(baseVo,re);
        re.setStatus(SignReStatusEnum.UN_ENABLE.getCode());
        if(re.getControlChangeFlag() == null){
            //控件变更状态，默认状态
            re.setControlChangeFlag(ControlChangeFlagEnum.NECESSARY_AND_ADD.getType());
        }

        saveData.setRe(re);
        //设置签署人类型
        saveData.setSignerType(re.getSignerType());
        //更新签约文件
        saveDocList(request.getFileList(),reId,saveData);
        //签署人
        saveSignerList(request.getSignerList(),reId,saveData);
        //抄送人
        saveCcerList(request.getCcerList(), reId,saveData);
        //单号配置
        if(SignReRuleGenerateEnum.RULE.getCode().equals(baseVo.getCodeType())){
            saveReRule(baseVo.getCodeTypeRuleVo(), reId,saveData);
        }
        if(SignReRuleGenerateEnum.RULE.getCode().equals(baseVo.getSubjectType())){
            saveReRule(baseVo.getSubjectTypeRuleVo(), reId,saveData);
        }
        reDataService.saveData(saveData);
        return reId ;
    }



    public void saveReRule(ReRuleVo vo,String reId,ReSaveData saveData){
        if(vo == null){
            return;
        }
        if(vo.getRuleType() == null){
            throw new PaasException("单号规则-规则类型-参数缺失");
        }
        if(!vo.getRuleType().equals(SignReRuleTypeEnum.SIGN_RE_CODE.getCode()) && !vo.getRuleType().equals(SignReRuleTypeEnum.SIGN_RE_SUBJECT.getCode())){
            throw new PaasException("单号规则-规则类型-错误");
        }
        if(vo.getDetailVoList() == null || vo.getDetailVoList().size() == 0){
            throw new PaasException("单号规则-规则细节列表-参数缺失");
        }
        Integer datetimeCount = 0 ;
        Integer serialNumCount = 0 ;
        Integer textCount = 0 ;
        Integer timeStampCount = 0 ;
        Integer businessLineNameCount = 0 ;
        Integer senderNameCount = 0 ;
        Integer receiverNameCount = 0 ;

        for(ReRuleDetailVo detailVo : vo.getDetailVoList()){
            if(detailVo.getContentType() == null || detailVo.getContentType().length() == 0){
                throw new PaasException("单号规则-内容类型-参数缺失");
            }
            if(detailVo.getContentOrder() == null){
                throw new PaasException("单号规则-顺序-参数缺失");
            }
            if(!SignReRuleDetailContentTypeEnum.TIMESTAMP.getCode().equals(detailVo.getContentType())
                    && !SignReRuleDetailContentTypeEnum.SENDER_NAME.getCode().equals(detailVo.getContentType())
                    && !SignReRuleDetailContentTypeEnum.RECEIVER_NAME.getCode().equals(detailVo.getContentType())){
                if(detailVo.getContent() == null || detailVo.getContent().length() == 0){
                    throw new PaasException("单号规则-内容-参数缺失");
                }
            }
            if(SignReRuleDetailContentTypeEnum.SERIAL_NUM.getCode().equals(detailVo.getContentType())){
                if(detailVo.getContentLength() == null){
                    throw new PaasException("单号规则-序列号内容长度-参数缺失");
                }
            }
            if(detailVo.getContentType() == SignReRuleDetailContentTypeEnum.DATETIME.getCode()){
                datetimeCount = datetimeCount + 1 ;
            }else if(detailVo.getContentType() == SignReRuleDetailContentTypeEnum.SERIAL_NUM.getCode()){
                serialNumCount = serialNumCount + 1 ;
            }else if(detailVo.getContentType() == SignReRuleDetailContentTypeEnum.TEXT.getCode()){
                textCount = textCount + 1 ;
            }else if(detailVo.getContentType() == SignReRuleDetailContentTypeEnum.TIMESTAMP.getCode()){
                timeStampCount = timeStampCount + 1 ;
            }else if(detailVo.getContentType() == SignReRuleDetailContentTypeEnum.BUSINESS_LINE_NAME.getCode()){
                businessLineNameCount = businessLineNameCount + 1 ;
            }else if(detailVo.getContentType() == SignReRuleDetailContentTypeEnum.SENDER_NAME.getCode()){
                senderNameCount = senderNameCount + 1 ;
            }else if(detailVo.getContentType() == SignReRuleDetailContentTypeEnum.RECEIVER_NAME.getCode()){
                receiverNameCount = receiverNameCount + 1;
            }
        }
        if(datetimeCount > 1 || serialNumCount > 1 || textCount > 1 || timeStampCount > 1
                || businessLineNameCount > 1 || senderNameCount > 1 || receiverNameCount > 1){
            throw new PaasException("单号规则-重复");
        }


        //生成业务线单号配置
        SignReRule reRule = reRuleService.getById(vo.getId());
        if(reRule == null){
            reRule = new SignReRule();
        }
        //生成或更新系统单号配置
        GlobalIdConfigModule globalIdConfigModule = generateGlobalConfig(vo, reRule.getRuleGenerateId());
        reRule.setId(vo.getId());
        reRule.setSignReId(reId);
        reRule.setRuleType(vo.getRuleType());
        reRule.setLink(vo.getLink());
        reRule.setDeleteFlag(false);
        //新增现有数据
        List<SignReRuleDetail> details = new ArrayList<>();
        for(ReRuleDetailVo detailVo : vo.getDetailVoList()){
            SignReRuleDetail detail = new SignReRuleDetail();
            detail.setRuleId(reRule.getId());
            detail.setContent(detailVo.getContent());
            detail.setContentType(detailVo.getContentType());
            detail.setContentLength(detailVo.getContentLength());
            detail.setContentOrder(detailVo.getContentOrder());
            detail.setDeleteFlag(false);
            details.add(detail);
        }

        if(vo.getRuleType().equals(SignReRuleTypeEnum.SIGN_RE_CODE.getCode())){
            saveData.setCodeReRule(reRule);
            saveData.setCodeDetails(details);
            saveData.setCodeModule(globalIdConfigModule);
        }else if(vo.getRuleType().equals(SignReRuleTypeEnum.SIGN_RE_SUBJECT.getCode())){
            saveData.setSubjectReRule(reRule);
            saveData.setSubjectDetails(details);
            saveData.setSubjectModule(globalIdConfigModule);
        }
    }

    public GlobalIdConfigModule generateGlobalConfig(ReRuleVo vo,String originGlobalConfigId){
        GlobalIdConfigModule module = null ;
        if(originGlobalConfigId != null && originGlobalConfigId.length() > 0){
            module  = globalIdConfigService.getInfoById(originGlobalConfigId);
        }
        if(module != null && module.getId() != null){
            module = updateGlobalIdConfig(module,vo);
        }else {
            module = addGlobalIdConfig(vo);
        }

        return module ;
    }

    public GlobalIdConfigModule updateGlobalIdConfig(GlobalIdConfigModule module,ReRuleVo vo){
        //更新系统单号配置
        module.setLink(vo.getLink());

        List<GlobalIdConfigInfo> globalidConfigInfoList = new ArrayList<>();
        for(ReRuleDetailVo detailVo : vo.getDetailVoList()){
            GlobalIdConfigInfo globalIdConfigInfo = new GlobalIdConfigInfo();
            if(SignReRuleDetailContentTypeEnum.BUSINESS_LINE_NAME.getCode().equals(detailVo.getContentType()) ||
                    SignReRuleDetailContentTypeEnum.SENDER_NAME.getCode().equals(detailVo.getContentType()) ||
                    SignReRuleDetailContentTypeEnum.RECEIVER_NAME.getCode().equals(detailVo.getContentType()) ){
                globalIdConfigInfo.setRuleType(GlobalIdInfoType.BUSINESS.getType());
            }else {
                globalIdConfigInfo.setRuleType(detailVo.getContentType());
            }
            globalIdConfigInfo.setRuleContent(detailVo.getContent());
            globalIdConfigInfo.setOrderNum(detailVo.getContentOrder());
            globalIdConfigInfo.setRuleLength(detailVo.getContentLength());

            globalIdConfigInfo.setConfigId(module.getId());

            globalidConfigInfoList.add(globalIdConfigInfo);
        }
        module.setGlobalidConfigInfoList(globalidConfigInfoList);

        return module ;
    }

    public GlobalIdConfigModule addGlobalIdConfig(ReRuleVo vo){
        //生成系统单号配置
        GlobalIdConfigModule globalIdConfigModule = new GlobalIdConfigModule();
        String idKey = UUID.randomUUID().toString();
        globalIdConfigModule.setIdKey(idKey);
        globalIdConfigModule.setIdName(UUID.randomUUID().toString());
        globalIdConfigModule.setLink(vo.getLink());
        List<GlobalIdConfigInfo> globalidConfigInfoList = new ArrayList<>();
        for(ReRuleDetailVo detailVo : vo.getDetailVoList()){
            GlobalIdConfigInfo globalIdConfigInfo = new GlobalIdConfigInfo();
            if(SignReRuleDetailContentTypeEnum.BUSINESS_LINE_NAME.getCode().equals(detailVo.getContentType()) ||
                    SignReRuleDetailContentTypeEnum.SENDER_NAME.getCode().equals(detailVo.getContentType()) ||
                    SignReRuleDetailContentTypeEnum.RECEIVER_NAME.getCode().equals(detailVo.getContentType()) ){
                globalIdConfigInfo.setRuleType(GlobalIdInfoType.BUSINESS.getType());
            }else {
                globalIdConfigInfo.setRuleType(detailVo.getContentType());
            }
            globalIdConfigInfo.setRuleContent(detailVo.getContent());
            globalIdConfigInfo.setOrderNum(detailVo.getContentOrder());
            globalIdConfigInfo.setRuleLength(detailVo.getContentLength());
            globalidConfigInfoList.add(globalIdConfigInfo);
        }
        globalIdConfigModule.setGlobalidConfigInfoList(globalidConfigInfoList);

        return globalIdConfigModule ;
    }

    /**
     * @Description #保存签约文件
     * @Param [fileList, reId]
     * @return void
     **/
    public void saveDocList(List<DocFileVo> fileList,String reId,ReSaveData data){
        //计算签约文件
        DocFileCalculateVo docFileCalculateVo = calculateDocFileList(fileList, reId);
        //新增签约文件
        addDocFileList(docFileCalculateVo.getAddFileVoList(),reId,data);
        //删除签约文件
        deleteDocFileList(docFileCalculateVo.getDeleteReDocList(),reId,data);
        //更新签约文件顺序
        for(DocFileVo docFileVo : fileList){
            if(docFileVo.getId() != null){
                SignReDoc reDoc = new SignReDoc();
                reDoc.setId(docFileVo.getId());
                reDoc.setDocOrder(docFileVo.getDocOrder());
                data.getUpdateDocOrderList().add(reDoc);
            }
        }

    }

    /**
     * @Description #保存签署人
     * @Param [signerList, reId]
     * @return void
     **/
    public String saveSignerList(List<DocSignerVo> signerList,String reId,ReSaveData data){
        SignerCalculateVo signerCalculateVo = calculateSignerList(signerList, reId);
        //新增
        addSignerList(signerCalculateVo.getAddSignerList(),reId,data);
        //修改
        updateSignerList(signerCalculateVo.getUpdateSignerList(),reId,data);
        //删除
        deleteSignerList(signerCalculateVo.getDeleteReSignerList(),data);

        return reId ;
    }


    /**
     * @Description #保存抄送人
     * @Param [ccerList, reId]
     * @return void
     **/
    public void saveCcerList(List<DocCcerVo> ccerVoList, String reId, ReSaveData saveData){
        if(ccerVoList != null && ccerVoList.size() > 0){
            for(DocCcerVo ccerVo : ccerVoList){
                SignReCcer ccer = new SignReCcer();
                BeanUtils.copyProperties(ccerVo,ccer);
                ccer.setSignReId(reId);
                ccer.setDeleteFlag(false);
                ccer.setId(null);
                saveData.getCcerList().add(ccer);
            }
        }
    }

    /**
     * @Description #计算签署人变更列表
     * @Param [signerVoList, ruId]
     * @return com.kaifangqian.modules.opensign.service.business.vo.SignerCalculateVo
     **/
    public SignerCalculateVo calculateSignerList(List<DocSignerVo> signerVoList , String ruId){


        SignerCalculateVo vo = new SignerCalculateVo();
        //查询原有的联系人
        List<SignReSigner> signerList = signerService.listByReId(ruId);
        //新增签署人列表
        List<DocSignerVo> addSignerList = new ArrayList<>();
        //更新签署人列表
        List<DocSignerVo> updateSignerList = new ArrayList<>();
        //删除签署人列表
        List<SignReSigner> deleteSignerList = new ArrayList<>();
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
                    for(SignReSigner signer : signerList){
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
        vo.setDeleteReSignerList(deleteSignerList);
        return vo;
    }



    /**
     * @Description #新增签署人列表
     * @Param [addSignerList]
     * @return void
     **/
    public void addSignerList(List<DocSignerVo> addSignerList ,String reId,ReSaveData data){
        //新增签署人
        if(addSignerList != null && addSignerList.size() > 0){
            //再新插入数据
            for(DocSignerVo signerVo : addSignerList){
                ReSaveDataSigner dataSigner = new ReSaveDataSigner();

                SignReSigner signer = new SignReSigner();
                BeanUtils.copyProperties(signerVo,signer);
                signer.setSignReId(reId);
                signer.setDeleteFlag(false);
                signer.setId(null);

                dataSigner.setReSigner(signer);
                if(SignerTypeEnum.SENDER.getCode().equals(signerVo.getSignerType())){
                    dataSigner.setSignerType(SignerTypeEnum.SENDER.getCode());
                    //发起人
                    List<DocSenderVo> senderVoList = signerVo.getSenderList();
                    //新增发起方
                    addSenderList(senderVoList,dataSigner);
                }else if(SignerTypeEnum.RECEIVER_ENT.getCode().equals(signerVo.getSignerType())){
                    dataSigner.setSignerType(SignerTypeEnum.RECEIVER_ENT.getCode());
                    //企业接收方
                    List<DocSenderVo> senderVoList = signerVo.getSenderList();
                    //新增企业接收方
                    addReceiveEntList(senderVoList,dataSigner);
                }else {
                    dataSigner.setConfirmType(signerVo.getConfirmType());
                    dataSigner.setVerifyType(signerVo.getVerifyType());
                    dataSigner.setAgreeSkipWillingness(signerVo.getAgreeSkipWillingness());
                    dataSigner.setSignerType(SignerTypeEnum.RECEIVER_PERSONAL.getCode());
                    dataSigner.setPersonalSignAuth(signerVo.getPersonalSignAuth());
                    dataSigner.setSealType(signerVo.getSealType());
                }
                data.getAddSignerList().add(dataSigner);
            }
        }
    }


    /**
     * @Description #更新签署人列表
     * @Param [updateSignerList]
     * @return void
     **/
    public void updateSignerList(List<DocSignerVo> updateSignerList,String signReId,ReSaveData data){
        //更新签署人
        if(updateSignerList != null && updateSignerList.size() > 0){
            LoginUser currentUser = MySecurityUtils.getCurrentUser();

            for(DocSignerVo signerVo : updateSignerList){
                ReSaveDataSigner dataSigner = new ReSaveDataSigner();
                SignReSigner signer = new SignReSigner();
                BeanUtils.copyProperties(signerVo,signer);
                if(SignerTypeEnum.SENDER.getCode().equals(signer.getSignerType())){
                    dataSigner.setSignerType(SignerTypeEnum.SENDER.getCode());
                    signer.setSignerUserId(currentUser.getTenantUserId());
                    //计算发起人
                    SenderCalculateVo senderCalculateVo = calculateSenderList(signerVo.getSenderList(), signerVo.getId());
                    //新增发起方
                    addSenderList(senderCalculateVo.getAddSenderVoList(),dataSigner);
                    //更新发起方
                    updateSenderList(senderCalculateVo.getUpdateSenderVoList(),dataSigner);
                    //删除发起方
                    deleteSenderList(senderCalculateVo.getDeleteReSenderList(),dataSigner,data);
                }else if(SignerTypeEnum.RECEIVER_ENT.getCode().equals(signer.getSignerType())){
                    dataSigner.setSignerType(SignerTypeEnum.RECEIVER_ENT.getCode());
                    signer.setSignerUserId(currentUser.getTenantUserId());
                    //计算企业接收方
                    SenderCalculateVo senderCalculateVo = calculateSenderList(signerVo.getSenderList(), signerVo.getId());
                    //新增企业接收方
                    addReceiveEntList(senderCalculateVo.getAddSenderVoList(),dataSigner);
                    //更新企业接收方
                    updateReceiveEntList(senderCalculateVo.getUpdateSenderVoList(),dataSigner);
                    //删除企业接收方
                    deleteReceiveEntList(senderCalculateVo.getDeleteReSenderList(),dataSigner,data);
                }else {
                    dataSigner.setConfirmType(signerVo.getConfirmType());
                    dataSigner.setVerifyType(signerVo.getVerifyType());
                    dataSigner.setAgreeSkipWillingness(signerVo.getAgreeSkipWillingness());
                    dataSigner.setSignerType(SignerTypeEnum.RECEIVER_PERSONAL.getCode());
                    dataSigner.setPersonalSignAuth(signerVo.getPersonalSignAuth());
                    dataSigner.setSealType(signerVo.getSealType());
                }
                dataSigner.setReSigner(signer);
                data.getUpdateSignerList().add(dataSigner);
            }
        }
    }

    /**
     * @Description #删除签署人列表
     * @Param [deleteSignerList]
     * @return void
     **/
    public void deleteSignerList(List<SignReSigner> deleteSignerList,ReSaveData data){
        //删除签署人
        if(deleteSignerList != null && deleteSignerList.size() > 0){
            for(SignReSigner signer : deleteSignerList){
                ReSaveDataSigner dataSigner = new ReSaveDataSigner();
                signer.setDeleteFlag(true);
                dataSigner.setReSigner(signer);
                //如果是发起方
                if(SignerTypeEnum.SENDER.getCode().equals(signer.getSignerType())){
                    dataSigner.setSignerType(SignerTypeEnum.SENDER.getCode());
                    //获取原有发前方内部设置
                    List<SignReSender> senderList = senderService.listBySignerId(signer.getId());
                    //删除发起人内部数据
                    deleteSenderList(senderList,dataSigner,data);
                }else if(SignerTypeEnum.RECEIVER_ENT.getCode().equals(signer.getSignerType())){
                    dataSigner.setSignerType(SignerTypeEnum.RECEIVER_ENT.getCode());
                    //获取原有企业接收方内部设置
                    List<SignReSender> senderList = senderService.listBySignerId(signer.getId());
                    //删除企业接收方内部数据
                    deleteReceiveEntList(senderList,dataSigner,data);
                }else {
                    dataSigner.setSignerType(SignerTypeEnum.RECEIVER_PERSONAL.getCode());
                }
                //删除签署控件和填写控件
                List<SignReDocControl> signReDocControlList = reDocControlService.listBySignerId(signer.getId());
                if(signReDocControlList != null && signReDocControlList.size() > 0){
                    List<String> controlIdList = signReDocControlList.stream().map(SignReDocControl::getId).collect(Collectors.toList());
                    if(controlIdList != null && controlIdList.size() > 0){
                        data.getDeleteControlIdList().addAll(controlIdList);
                    }
                }
                List<SignReDocParam> paramList = reDocParamService.listByReSignerId(signer.getId());
                if(paramList != null && paramList.size() > 0){
                    List<String> paramIdList = paramList.stream().map(SignReDocParam::getId).collect(Collectors.toList());
                    if(paramIdList != null && paramIdList.size() > 0){
                        data.getDeleteReDocParamIdList().addAll(paramIdList);
                    }
                }


                data.getDeleteSignerList().add(dataSigner);
            }

        }
    }


    public SenderCalculateVo calculateSenderList(List<DocSenderVo> senderVoList ,String signerId){
        SenderCalculateVo vo = new SenderCalculateVo();

        //获取原有发前方内部设置
        List<SignReSender> senderList = senderService.listBySignerId(signerId);
        //新增发起方
        List<DocSenderVo> addSenderVoList = new ArrayList<>();
        //更新发起方
        List<DocSenderVo> updateSenderVoList = new ArrayList<>();
        //删除发起方
        List<SignReSender> deleteSenderList = new ArrayList<>();
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
                    for(SignReSender sender : senderList){
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
        vo.setDeleteReSenderList(deleteSenderList);
        return vo ;
    }


    /**
     * @Description #新增发起方
     * @Param [addSenderVoList]
     * @return void
     **/
    public void addSenderList(List<DocSenderVo> addSenderVoList,ReSaveDataSigner dataSigner){
        if(addSenderVoList != null && addSenderVoList.size() > 0){
            for(DocSenderVo senderVo : addSenderVoList){
                ReSaveDataSender dataSender = new ReSaveDataSender();
                SignReSender sender = new SignReSender();
                BeanUtils.copyProperties(senderVo,sender);
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
                dataSender.setSender(sender);
                dataSender.setConfirmType(senderVo.getConfirmType());
                dataSender.setSignerType(SignerTypeEnum.SENDER.getCode());
                dataSender.setVerifyType(senderVo.getVerifyType());
                dataSender.setAgreeSkipWillingness(senderVo.getAgreeSkipWillingness());
                dataSender.setPersonalSignAuth(senderVo.getPersonalSignAuth());
                dataSender.setSealType(senderVo.getSealType());
                dataSigner.getAddSenderList().add(dataSender);
            }
        }
    }

    /**
     * @Description #新增企业接收方
     * @Param [addSenderVoList]
     * @return void
     **/
    public void addReceiveEntList(List<DocSenderVo> addSenderVoList,ReSaveDataSigner dataSigner){
        if(addSenderVoList != null && addSenderVoList.size() > 0){
            for(DocSenderVo senderVo : addSenderVoList){
                ReSaveDataSender dataSender = new ReSaveDataSender();
                SignReSender sender = new SignReSender();
                BeanUtils.copyProperties(senderVo,sender);
                sender.setDeleteFlag(false);
                dataSender.setSender(sender);
                dataSender.setConfirmType(senderVo.getConfirmType());
                dataSender.setSignerType(SignerTypeEnum.RECEIVER_ENT.getCode());
                dataSender.setVerifyType(senderVo.getVerifyType());
                dataSender.setAgreeSkipWillingness(senderVo.getAgreeSkipWillingness());
                dataSender.setPersonalSignAuth(senderVo.getPersonalSignAuth());
                dataSender.setSealType(senderVo.getSealType());
                dataSigner.getAddSenderList().add(dataSender);
            }
        }
    }

    /**
     * @Description #更新发起方
     * @Param [updateSenderVoList]
     * @return void
     **/
    public void updateSenderList(List<DocSenderVo> updateSenderVoList,ReSaveDataSigner dataSigner){
        if(updateSenderVoList != null && updateSenderVoList.size() > 0){

            for(DocSenderVo senderVo : updateSenderVoList){
                ReSaveDataSender dataSender = new ReSaveDataSender();
                SignReSender sender = new SignReSender();
                BeanUtils.copyProperties(senderVo,sender);
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
                dataSender.setSender(sender);
                dataSender.setConfirmType(senderVo.getConfirmType());
                dataSender.setSignerType(SignerTypeEnum.SENDER.getCode());
                dataSender.setVerifyType(senderVo.getVerifyType());
                dataSender.setAgreeSkipWillingness(senderVo.getAgreeSkipWillingness());
                dataSender.setPersonalSignAuth(senderVo.getPersonalSignAuth());
                dataSender.setSealType(senderVo.getSealType());
                dataSigner.getUpdateSenderList().add(dataSender);

            }
        }
    }

    /**
     * @Description #更新企业接收方
     * @Param [updateSenderVoList]
     * @return void
     **/
    public void updateReceiveEntList(List<DocSenderVo> updateSenderVoList,ReSaveDataSigner dataSigner){
        if(updateSenderVoList != null && updateSenderVoList.size() > 0){
            for(DocSenderVo senderVo : updateSenderVoList){
                ReSaveDataSender dataSender = new ReSaveDataSender();

                SignReSender sender = new SignReSender();
                BeanUtils.copyProperties(senderVo,sender);
                sender.setDeleteFlag(false);

                dataSender.setSender(sender);
                dataSender.setConfirmType(senderVo.getConfirmType());
                dataSender.setSignerType(SignerTypeEnum.RECEIVER_ENT.getCode());
                dataSender.setVerifyType(senderVo.getVerifyType());
                dataSender.setAgreeSkipWillingness(senderVo.getAgreeSkipWillingness());
                dataSender.setPersonalSignAuth(senderVo.getPersonalSignAuth());
                dataSender.setSealType(senderVo.getSealType());
                dataSigner.getUpdateSenderList().add(dataSender);
            }
        }
    }

    /**
     * @Description #删除发起方
     * @Param [deleteSenderList]
     * @return void
     **/
    public void deleteSenderList(List<SignReSender> deleteSenderList,ReSaveDataSigner dataSigner,ReSaveData data){
        if(deleteSenderList != null && deleteSenderList.size() > 0){
            for(SignReSender sender : deleteSenderList){
                sender.setDeleteFlag(true);
                dataSigner.getDeleteSenderList().add(sender);

                //删除签署控件和填写控件
                List<SignReDocControl> signReDocControlList = reDocControlService.listBySignerId(sender.getId());
                if(signReDocControlList != null && signReDocControlList.size() > 0){
                    List<String> controlIdList = signReDocControlList.stream().map(SignReDocControl::getId).collect(Collectors.toList());
                    if(controlIdList != null && controlIdList.size() > 0){
                        data.getDeleteControlIdList().addAll(controlIdList);
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
    public void deleteReceiveEntList(List<SignReSender> deleteSenderList,ReSaveDataSigner dataSigner,ReSaveData data){
        if(deleteSenderList != null && deleteSenderList.size() > 0){
            for(SignReSender sender : deleteSenderList){
                sender.setDeleteFlag(true);
                dataSigner.getDeleteSenderList().add(sender);

                //删除签署控件和填写控件
                List<SignReDocControl> signReDocControlList = reDocControlService.listBySignerId(sender.getId());
                if(signReDocControlList != null && signReDocControlList.size() > 0){
                    List<String> controlIdList = signReDocControlList.stream().map(SignReDocControl::getId).collect(Collectors.toList());
                    if(controlIdList != null && controlIdList.size() > 0){
                        data.getDeleteControlIdList().addAll(controlIdList);
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
    public DocFileCalculateVo calculateDocFileList(List<DocFileVo> fileVoList , String reId){
        DocFileCalculateVo vo = new DocFileCalculateVo();
        //获取签约文件列表
        List<SignReDoc> docList = reDocService.listByReId(reId);
        //新增签约文件列表
        List<DocFileVo> addFileVoList = new ArrayList<>();
        //删除签约文件列表
        List<SignReDoc> deleteDocList = new ArrayList<>();
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
                    for (SignReDoc ruDoc : docList) {
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
        vo.setDeleteReDocList(deleteDocList);
        vo.setAddFileVoList(addFileVoList);
        return vo;
    }


    /**
     * @Description #保存新增的签约文件
     * @Param [addFileVoList, ruId]
     * @return void
     **/
    public void addDocFileList(List<DocFileVo> addFileVoList,String reId,ReSaveData data){
        //新增签约文件
        if(addFileVoList != null && addFileVoList.size() > 0){
            //校验参数
            for(DocFileVo fileVo : addFileVoList){
                if(fileVo.getDocType() == null || fileVo.getDocType() == 0){
                    throw new PaasException("签约文件类型-参数缺失");
                }
                if(SignFileTypeEnum.TEMPLATE.getCode().equals(fileVo.getDocType())){
                    if(fileVo.getDocOriginId() == null || fileVo.getDocOriginId().length() == 0){
                        throw new PaasException("模板签约文件来源id-参数缺失");
                    }
                }
            }
            //保存
            for(DocFileVo fileVo : addFileVoList){
                SignReDoc reDoc = new SignReDoc();
                reDoc.setDocName(fileVo.getName());
                reDoc.setSignReId(reId);
                reDoc.setDocType(fileVo.getDocType());
                reDoc.setDeleteFlag(false);
                reDoc.setId(null);
                if(SignFileTypeEnum.TEMPLATE.getCode().equals(fileVo.getDocType())){
                    reDoc.setDocOriginId(fileVo.getDocOriginId());
                    SignTemplateRecord current = templateRecordService.getCurrent(fileVo.getDocOriginId());
                    if(current != null){
                        if(current.getDocPage() != null && current.getDocPage() != 0){
                            reDoc.setDocPage(current.getDocPage());
                        }else {
                            //获取图片页数
                            byte[] pdfByte = signFileService.getByteById(current.getAnnexId());
                            if(pdfByte == null){
                                throw new PaasException("签约文件异常");
                            }
                            Integer pdfPage = pdfboxService.getPdfPage(pdfByte);
                            reDoc.setDocPage(pdfPage);
                            //更新模板页码
                            List<SignTemplateRecord> updateTemplateRecordList = data.getUpdateTemplateRecordList();
                            SignTemplateRecord r = new SignTemplateRecord();
                            r.setId(current.getId());
                            r.setDocPage(pdfPage);
                            updateTemplateRecordList.add(r);

                        }
                    }
                } else if(SignFileTypeEnum.UPLOAD.getCode().equals(fileVo.getDocType())){
                    reDoc.setAnnexId(fileVo.getAnnexId());
                    //获取图片页数
                    byte[] pdfByte = signFileService.getByteById(fileVo.getAnnexId());
                    if(pdfByte == null){
                        throw new PaasException("签约文件异常");
                    }
                    Integer pdfPage = pdfboxService.getPdfPage(pdfByte);
                    reDoc.setDocPage(pdfPage);
                }
                reDoc.setDocOrder(fileVo.getDocOrder());
                List<SignReDoc> addReDocList = data.getAddReDocList();
                addReDocList.add(reDoc);

            }

        }

    }

    /**
     * @Description #删除签约文件
     * @Param [docList, ruId]
     * @return void
     **/
    public void deleteDocFileList(List<SignReDoc> docList,String reId,ReSaveData data){
        if(docList != null && docList.size() > 0){
            List<String> deleteDocIdList = docList.stream().map(SignReDoc::getId).collect(Collectors.toList());
            data.getDeleteDocIdList().addAll(deleteDocIdList);

            //控件
            List<SignReDocControl> signReDocControlList = reDocControlService.listByParam(reId);
            if(signReDocControlList != null && signReDocControlList.size() > 0){
                for(SignReDocControl signReDocControl : signReDocControlList){
                    List<SignReDocControlProperty> properties = reDocControlPropertyService.listByControlId(signReDocControl.getId());
                    if(properties != null && properties.size() > 0){
                        Integer relationDocCount = 0 ;
                        Integer deleteCount = 0 ;
                        Boolean relationDocStatus = false ;
                        for(SignReDocControlProperty property : properties){
                            if(property.getPropertyType().equals(ControlPropertyTypeEnum.RELATION_DOC.getCode())){
                                relationDocCount = relationDocCount + 1;
                                if(deleteDocIdList.contains(property.getPropertyValue())){
                                    data.getDeleteControlPropertyIdList().add(property.getId());
                                    relationDocStatus = true ;
                                    deleteCount = deleteCount + 1 ;
                                }
                            }
                        }
                        if(relationDocStatus){
                            if(relationDocCount == deleteCount){
                                data.getDeleteControlIdList().add(signReDocControl.getId());
                                data.getDeletePropertyByControlIdList().add(signReDocControl.getId());
                            }
                        }
                    }
                }
            }


        }
    }

    /**
     * @Description #保存控件接口
     * @Param [controlList, deleteIdList, ruId]
     * @return void
     **/
    @Transactional(rollbackFor = Exception.class)
    public List<SignReDocControl> saveControlList(List<DocControlVo> controlList ,List<String> deleteIdList,String reId){
        List<SignReDocControl> docControlList = new ArrayList<>();

        if(controlList != null && controlList.size() > 0){
            List<SignReDocParam> paramList = new ArrayList<>();
            for(DocControlVo controlVo : controlList){
                //填写控件
                if(ControlTypeEnum.getWriteList().contains(controlVo.getControlType())){
                    SignReDocParam param = new SignReDocParam();
                    param.setSignerId(controlVo.getSignerId());
                    param.setSignerType(controlVo.getSignerType());
                    param.setSignReId(reId);
                    param.setSignReDocId(controlVo.getSignReDocId());
                    param.setInterfaceParamName(controlVo.getInterfaceParamName());
                    param.setDeleteFlag(false);
                    paramList.add(param);
                }
                //签署控件
                else if(ControlTypeEnum.getSignList().contains(controlVo.getControlType())){
                    SignReDocControl control = new SignReDocControl();
                    BeanUtils.copyProperties(controlVo,control);
                    control.setSignReId(reId);
                    control.setDeleteFlag(false);
                    boolean b = reDocControlService.saveOrUpdate(control);
                    if (!b){
                        throw new PaasException("控件保存失败");
                    }
                    //保存控件属性
                    List<ControlPropertyVo> propertyVoList = controlVo.getPropertyVoList();
                    if(propertyVoList != null && propertyVoList.size() > 0){
                        //先删除控件属性
                        reDocControlPropertyService.deleteByControlId(control.getId());
                        List<SignReDocControlProperty> propertyList = new ArrayList<>();
                        for(ControlPropertyVo propertyVo : propertyVoList){
                            SignReDocControlProperty property = new SignReDocControlProperty();
                            property.setControlId(control.getId());
                            property.setReId(reId);
                            property.setPropertyType(propertyVo.getPropertyType());
                            property.setPropertyValue(propertyVo.getPropertyValue());
                            propertyList.add(property);
                        }
                        //再插入新的控件属性
                        reDocControlPropertyService.saveBatch(propertyList);
                    }
                    docControlList.add(control);
                }

            }
            if(paramList.size() > 0){
                reDocParamService.deleteByRe(reId);
                reDocParamService.saveBatch(paramList);
            }
        }
        if(deleteIdList != null && deleteIdList.size() > 0){
            for(String id : deleteIdList){
                SignReDocControl control = new SignReDocControl();
                control.setDeleteFlag(true);
                control.setId(id);
                reDocControlService.updateById(control);
                reDocControlPropertyService.deleteByControlId(id);
            }
        }
        return docControlList ;
    }

    /**
     * @Description #保存权限数据
     * @Param [vo]
     * @return java.lang.String
     **/
    public String saveAuth(ReAuthVo vo){
        SignRe re = reService.getById(vo.getSignReId());
        if(re == null){
            throw new PaasException("业务线不存在");
        }
        re.setDownloaderType(vo.getDownloaderType());
        boolean b = reService.updateById(re);
        if(!b){
            throw new PaasException("操作失败");
        }
        List<DocAuthVo> authVoList = vo.getAuthList();

        if(authVoList != null && authVoList.size() > 0){
            authService.deleteByReId(re.getId());
            List<SignReAuth> authList = new ArrayList<>();
            for(DocAuthVo authVo : authVoList){
                SignReAuth auth = new SignReAuth();
                auth.setSignReId(re.getId());
                auth.setDeleteFlag(false);
                auth.setAuthType(authVo.getAuthType());
                auth.setTenantId(MySecurityUtils.getCurrentUser().getTenantId());
                auth.setUserId(authVo.getUserId());
                authList.add(auth);
            }
            boolean authListSave = authService.saveBatch(authList);
            if(!authListSave){
                throw new PaasException("操作失败");
            }
        }
        List<Integer> authList = new ArrayList<>();
        authList.add(SignReAuthTypeEnum.USER.getCode());
        //如果没有使用者了，默认创造一个使用者，方便使用列表查询
        Integer count = authService.countByParam(re.getId(), null, null, authList);
        if(count == null || count == 0){
            SignReAuth auth = new SignReAuth();
            auth.setSignReId(re.getId());
            auth.setDeleteFlag(false);
            auth.setAuthType(SignReAuthTypeEnum.USER.getCode());
            auth.setTenantId(MySecurityUtils.getCurrentUser().getTenantId());
            boolean save = authService.save(auth);
            if(!save){
                throw new PaasException("操作失败");
            }
        }

        return re.getId() ;
    }

    /**
     * @Description #全部详情数据
     * @Param [signReId]
     * @return com.kaifangqian.modules.opensign.vo.base.sign.BaseVo
     **/
    public BaseVo info(String signReId){

        BaseVo vo = new BaseVo();
        //基础数据
        vo.setBaseVo(getDocBase(signReId));
        //签约文件
        vo.setFileList(getDocList(signReId));
        //签署人
        vo.setSignerList(getSignerList(signReId));
        //抄送人
        vo.setCcerList(getCcerList(signReId));

        return vo ;
    }

    /**
     * @Description #全部详情数据
     * @Param [signReId]
     * @return com.kaifangqian.modules.opensign.vo.base.sign.BaseVo
     **/
    public BaseVo info2Ru(String signReId){

        BaseVo vo = new BaseVo();
        //基础数据
        DocBaseVo docBase = getDocBase2Ru(signReId);
        if(docBase == null){
            throw new PaasException("业务线数据不存在");
        }
        vo.setBaseVo(docBase);
        //签约文件
        vo.setFileList(getDocList(signReId));
        //预设签署人
        if(docBase.getSignerType() != null && docBase.getSignerType().equals(SignReSignerTypeEnum.RULE.getCode())){
            //签署人
            vo.setSignerList(getSignerList(signReId));
        }
        //开启抄送
        if(docBase.getCcedType() != null && docBase.getCcedType().equals(SignOnOrOffEnum.YES.getCode())){
            //抄送人
            vo.setCcerList(getCcerList(signReId));
        }

        return vo ;
    }

    /**
     * @Description #基础数据
     * @Param [signRe, vo]
     * @return void
     **/
    public DocBaseVo getDocBase(String signReId){
        //主数据
        SignRe signRe = reService.getById(signReId);
        if(signRe == null){
            throw new PaasException("业务线配置不存在");
        }
        DocBaseVo baseVo = new DocBaseVo();
        BeanUtils.copyProperties(signRe,baseVo);
        baseVo.setSignReId(signRe.getId());
        //文件编号
        baseVo.setCodeTypeRuleVo(getRuleVo(signReId,SignReRuleTypeEnum.SIGN_RE_CODE.getCode()));
        //文件主题
        baseVo.setSubjectTypeRuleVo(getRuleVo(signReId,SignReRuleTypeEnum.SIGN_RE_SUBJECT.getCode()));
        return baseVo ;
    }

    public DocBaseVo getDocBase2Ru(String signReId){
        //主数据
        SignRe signRe = reService.getById(signReId);
        if(signRe == null){
            throw new PaasException("业务线配置不存在");
        }
        DocBaseVo baseVo = new DocBaseVo();
        BeanUtils.copyProperties(signRe,baseVo);
        baseVo.setSignReId(signRe.getId());
        //文件编号
        if(SignReRuleGenerateEnum.RULE.getCode().equals(signRe.getCodeType())){
            baseVo.setCodeTypeRuleVo(getRuleVo(signReId,SignReRuleTypeEnum.SIGN_RE_CODE.getCode()));
        }
        //文件主题
        if(SignReRuleGenerateEnum.RULE.getCode().equals(signRe.getSubjectType())){
            baseVo.setSubjectTypeRuleVo(getRuleVo(signReId,SignReRuleTypeEnum.SIGN_RE_SUBJECT.getCode()));
        }
        return baseVo ;
    }

    /**
     * @Description #获取单号生成规则数据
     * @Param [signReId, ruleType]
     * @return com.kaifangqian.modules.opensign.vo.base.sign.ReRuleVo
     **/
    public ReRuleVo getRuleVo(String signReId,Integer ruleType ){
        ReRuleVo vo = new ReRuleVo();
        List<ReRuleDetailVo> detailVoList = new ArrayList<>();

        List<SignReRule> signReRules = reRuleService.listByReId(signReId, ruleType);
        if(signReRules == null || signReRules.size() == 0){
            return vo;
        }
        SignReRule reRule = signReRules.get(0);
        if(reRule == null){
            return vo;
        }
        BeanUtils.copyProperties(reRule,vo);
        List<SignReRuleDetail> details = reRuleDetailService.listByRuleId(reRule.getId());
        if(details != null && details.size() > 0){
            for(SignReRuleDetail detail : details){
                ReRuleDetailVo detailVo = new ReRuleDetailVo();
                BeanUtils.copyProperties(detail,detailVo);
                detailVoList.add(detailVo);
            }
        }
        vo.setDetailVoList(detailVoList);
        return vo ;
    }

    /**
     * @Description #签约文件
     * @Param [signReId, vo]
     * @return void
     **/
    public List<DocFileVo> getDocList(String signReId){
        List<DocFileVo> fileVoList = new ArrayList<>();
        List<SignReDoc> docList = reDocService.listByReId(signReId);
        if(docList != null && docList.size() > 0){
            for(SignReDoc doc : docList){
                DocFileVo fileVo = new DocFileVo();
                BeanUtils.copyProperties(doc,fileVo);
                if(SignFileTypeEnum.UPLOAD.getCode().equals(doc.getDocType())){
                    //上传
                    fileVo.setName(doc.getDocName());
                    fileVo.setAnnexId(doc.getAnnexId());
                    fileVo.setDocType(SignFileTypeEnum.UPLOAD.getCode());
                    fileVo.setDocOriginId(doc.getId());
                    fileVo.setParamType(SignFileParamTypeEnum.NO.getCode());

                }else if(SignFileTypeEnum.TEMPLATE.getCode().equals(doc.getDocType())){
                    //模板
                    SignTemplate template = templateService.getById(doc.getDocOriginId());
                    if(template != null){
                        SignTemplateRecord current = templateRecordService.getCurrent(template.getId());
                        if(current != null && template != null){
                            fileVo.setName(template.getTemplateName());
                            fileVo.setAnnexId(current.getAnnexId());
                            fileVo.setDocType(SignFileTypeEnum.TEMPLATE.getCode());
                            fileVo.setDocOriginId(template.getId());
                            if(template.getTemplateType() != null && SignFileParamTypeEnum.IS.getCode().equals(template.getTemplateType())){
                                fileVo.setParamType(SignFileParamTypeEnum.IS.getCode());
                            }else {
                                fileVo.setParamType(SignFileParamTypeEnum.NO.getCode());
                            }
                        }
                    }
                }
                if(doc.getDocOrder() != null){
                    fileVo.setDocOrder(doc.getDocOrder());
                }else {
                    fileVo.setDocOrder(0);
                }
                fileVo.setDocPage(doc.getDocPage());
                fileVo.setOriginType(SignOriginTypeEnum.RE.getCode());
                fileVoList.add(fileVo);
            }
            fileVoList = fileVoList.stream().sorted(Comparator.comparing(DocFileVo::getDocOrder)).collect(Collectors.toList());
        }
        return fileVoList ;
    }

    /**
     * @Description #签署人
     * @Param [signReId, vo]
     * @return void
     **/
    public List<DocSignerVo> getSignerList(String signReId){
        List<DocSignerVo> signerVoList = new ArrayList<>();
        List<SignReSigner> signerList = signerService.listByReId(signReId);
        if(signerList != null && signerList.size() > 0){
            List<SignReSignConfirm> signConfirmList = reSignConfirmService.listByParam(signReId);
            List<String> confirmSignerIdList = new ArrayList<>() ;
            if(signConfirmList != null && signConfirmList.size() > 0){
                List<String> collect = signConfirmList.stream().map(SignReSignConfirm::getSignerId).collect(Collectors.toList());
                if(collect != null){
                    confirmSignerIdList.addAll(collect);
                }
            }
            //遍历签署人
            for(SignReSigner signer : signerList){
                DocSignerVo signerVo = new DocSignerVo();
                BeanUtils.copyProperties(signer,signerVo);
                if(signer.getSignerUserId() != null && signer.getSignerUserId().length() > 0){
                    SysTenantUser tenantUser = tenantUserService.getById(signer.getSignerUserId());
                    if(tenantUser != null){
                        signerVo.setSingerUserId(tenantUser.getId());
                        signerVo.setSingerUserName(tenantUser.getNickName());
                    }
                }
                if(SignerTypeEnum.SENDER.getCode().equals(signer.getSignerType()) || SignerTypeEnum.RECEIVER_ENT.getCode().equals(signer.getSignerType())){
                    List<DocSenderVo> senderVoList = new ArrayList<>();
                    //发起方
                    List<SignReSender> senderList = senderService.listBySignerId(signer.getId());
                    if(senderList != null && senderList.size() > 0){
                        for(SignReSender sender : senderList){
                            DocSenderVo senderVo = new DocSenderVo();
                            BeanUtils.copyProperties(sender,senderVo);
                            if(sender.getSenderUserId() != null && sender.getSenderUserId().length() > 0){
                                SysTenantUser tenantUser = tenantUserService.getById(sender.getSenderUserId());
                                if(tenantUser != null){
                                    senderVo.setSenderUserId(tenantUser.getId());
                                    senderVo.setSenderUserName(tenantUser.getNickName());
                                }
                            }
                            //指定的签章名称
                            if(sender != null && sender.getSenderType().equals(SenderTypeEnum.ENTERPRISE.getCode())){
                                if(sender.getSenderSealId() != null && sender.getSenderSealId().length() > 0){
                                    SignEntSeal entSeal = entSealService.getById(sender.getSenderSealId());
                                    if(entSeal != null){
                                        senderVo.setSenderSealId(sender.getSenderSealId());
                                        senderVo.setSenderSealName(entSeal.getSealName());
                                    }
                                }
                            }

                            //获取验证方式
                            SignReSignConfirm signReSignConfirm = reSignConfirmService.getByParam(sender.getId(),signReId);
                            if(signReSignConfirm != null){
                                senderVo.setAgreeSkipWillingness(signReSignConfirm.getAgreeSkipWillingness());
                                senderVo.setVerifyType(signReSignConfirm.getConfirmType());
                                senderVo.setPersonalSignAuth(signReSignConfirm.getPersonalSignAuth());
                                senderVo.setSealType(signReSignConfirm.getSealType());
                            }

//                            if(confirmSignerIdList.contains(sender.getId())){
//                                //开启人脸校验
//                                senderVo.setConfirmType(1);
//                            }
                            senderVoList.add(senderVo);
                        }
                    }
                    signerVo.setSenderList(senderVoList);
                }else {
//                    if(confirmSignerIdList.contains(signer.getId())){
//                        //开启人脸校验
//                        signerVo.setConfirmType(1);
//                    }
                    //获取验证方式
                    SignReSignConfirm signReSignConfirm = reSignConfirmService.getByParam(signer.getId(),signReId);
                    if(signReSignConfirm != null){
                        signerVo.setAgreeSkipWillingness(signReSignConfirm.getAgreeSkipWillingness());
                        signerVo.setVerifyType(signReSignConfirm.getConfirmType());
                        signerVo.setPersonalSignAuth(signReSignConfirm.getPersonalSignAuth());
                        signerVo.setSealType(signReSignConfirm.getSealType());
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
    public ReAuthVo getAuth(String signReId){
        SignRe re = reService.getById(signReId);
        if(re == null){
            throw new PaasException("业务线不存在");
        }
        ReAuthVo vo = new ReAuthVo();
        vo.setDownloaderType(re.getDownloaderType());
        List<DocAuthVo> docAuthVoList = new ArrayList<>();
        List<SignReAuth> authList = authService.listByReId(signReId);
        if(authList != null && authList.size() > 0){
            for(SignReAuth signReAuth : authList){
                if(SignReAuthTypeEnum.USER.getCode().equals(signReAuth.getAuthType())){
                    if(signReAuth.getUserId() == null || signReAuth.getUserId().length() == 0){
                        continue;
                    }
                }
                DocAuthVo authVo = new DocAuthVo();
                BeanUtils.copyProperties(signReAuth,authVo);
                SysTenantUser tenantUser = tenantUserService.getById(signReAuth.getUserId());
                if(tenantUser != null){
                    authVo.setUserId(tenantUser.getId());
                    authVo.setUserName(tenantUser.getNickName());
                }
                docAuthVoList.add(authVo);
            }
        }
        vo.setAuthList(docAuthVoList);
        return vo ;
    }




    public List<DocApproveVo> getApproveList(String signReId){
        SignRe re = reService.getById(signReId);
        if(re == null){
            throw new PaasException("业务线不存在");
        }
        List<DocApproveVo> approveVoList = new ArrayList<>();
        if(re.getBeforeSignApproveType() != null){
            DocApproveVo vo = new DocApproveVo();
            vo.setApproveId(re.getBeforeSignApproveId());
            vo.setApproveType(re.getBeforeSignApproveType());
            vo.setSignReId(re.getId());
            approveVoList.add(vo);
        }
        if(re.getBeforeStartApproveType() != null){
            DocApproveVo vo = new DocApproveVo();
            vo.setApproveId(re.getBeforeStartApproveId());
            vo.setApproveType(re.getBeforeStartApproveType());
            vo.setSignReId(re.getId());
            approveVoList.add(vo);
        }
        return approveVoList ;
    }

    /**
     * @Description #抄送人
     * @Param [signReId, vo]
     * @return void
     **/
    public List<DocCcerVo> getCcerList(String signReId){
        List<DocCcerVo> ccerVoList = new ArrayList<>();
        List<SignReCcer> ccerList = ccerService.listByReId(signReId);
        if(ccerList != null && ccerList.size() > 0){

            for(SignReCcer ccer : ccerList){
                DocCcerVo ccerVo = new DocCcerVo();
                BeanUtils.copyProperties(ccer,ccerVo);
                if(ccer.getInternalCcerId() != null && ccer.getInternalCcerId().length() > 0){
                    SysTenantUser tenantUser = tenantUserService.getById(ccer.getInternalCcerId());
                    if(tenantUser != null){
                        ccerVo.setInternalCcerId(tenantUser.getId());
                        ccerVo.setInternalCcerName(tenantUser.getNickName());
                    }
                }
                ccerVo.setCcerAddType(SignCcerAddTypeEnum.RE.getCode());
                ccerVoList.add(ccerVo);
            }

        }
        return ccerVoList ;
    }

    /**
     * @Description #控件列表
     * @Param [signReId]
     * @return java.util.List<com.kaifangqian.modules.opensign.vo.base.sign.DocControlVo>
     **/
    public List<DocControlVo> getDocControlList(DocControlListVo request){
        List<DocControlVo> controlVoList = new ArrayList<>();
        List<SignReDoc> reDocList = reDocService.listByReId(request.getSignReId());
        if(reDocList == null || reDocList.size() == 0){
            return controlVoList ;
        }
        //模板填写控件
        List<String> docOriginIdList = new ArrayList<>();
        Map<String,String> docTemplateRelationMap = new HashMap<>();
        for(SignReDoc reDoc : reDocList){
            if(SignFileTypeEnum.TEMPLATE.getCode().equals(reDoc.getDocType())){
                docOriginIdList.add(reDoc.getDocOriginId());
                docTemplateRelationMap.put(reDoc.getDocOriginId(),reDoc.getId());
            }
        }
        if(docOriginIdList.size() > 0){
            List<SignTemplateControl> controlList = templateControlService.getList(docOriginIdList);
            List<SignReDocParam> paramList = reDocParamService.listByReId(request.getSignReId());
            Map<String, String> signerMap = null ;
            if(paramList != null && paramList.size() > 0) {
                signerMap = paramList.stream().collect(Collectors.toMap(SignReDocParam::getInterfaceParamName, SignReDocParam::getSignerId, (k1, k2) -> k1));
            }
            if(controlList != null && controlList.size() > 0){
                for(SignTemplateControl control : controlList){
                    DocControlVo docControlVo  = new DocControlVo();
                    BeanUtils.copyProperties(control,docControlVo);
                    docControlVo.setControlType(control.getType());
                    docControlVo.setSignReId(request.getSignReId());
                    docControlVo.setSignReDocId(docTemplateRelationMap.get(control.getTemplateId()));
                    docControlVo.setOriginType(ControlOriginTypeEnum.RE.getCode());
                    if(signerMap != null && control.getInterfaceParamName() != null){
                        String signerId = signerMap.get(control.getInterfaceParamName());
                        if(signerId != null){
                            docControlVo.setSignerId(signerId);
                        }
                    }
                    controlVoList.add(docControlVo);
                }
            }
        }

        //签署控件
        List<SignReDocControl> signReDocControls = reDocControlService.listByParam(request.getSignReId());
        if(signReDocControls != null && signReDocControls.size() > 0){
            List<String> controlIds = signReDocControls.stream().map(SignReDocControl::getId).collect(Collectors.toList());
            Map<String, List<SignReDocControlProperty>> propertyMap = null ;
            if(controlIds != null && controlIds.size() > 0){
                List<SignReDocControlProperty> propertyList = reDocControlPropertyService.listByControlIdList(controlIds);
                if(propertyList != null && propertyList.size() > 0){
                    propertyMap = propertyList.stream().collect(Collectors.groupingBy(SignReDocControlProperty::getControlId));
                }
            }
            for(SignReDocControl reDocControl : signReDocControls){
                DocControlVo docControlVo  = new DocControlVo();
                BeanUtils.copyProperties(reDocControl,docControlVo);
                docControlVo.setOriginType(ControlOriginTypeEnum.RE.getCode());
                if(propertyMap != null && propertyMap.containsKey(docControlVo.getId())){
                    List<SignReDocControlProperty> properties = propertyMap.get(docControlVo.getId());
                    if(properties != null && properties.size() > 0){
                        List<ControlPropertyVo> propertyVoList = new ArrayList<>();
                        for(SignReDocControlProperty property : properties){
                            ControlPropertyVo propertyVo = new ControlPropertyVo();
                            BeanUtils.copyProperties(property,propertyVo);
                            propertyVoList.add(propertyVo);
                        }
                        docControlVo.setPropertyVoList(propertyVoList);
                    }
                }
                controlVoList.add(docControlVo);
            }
        }
        return controlVoList ;
    }

    private GlobalIdConfig getByIdKey(String idKey) {
        LambdaQueryWrapper<GlobalIdConfig> wrapper = new LambdaQueryWrapper();
        wrapper.eq(GlobalIdConfig::getIdKey, idKey);
        List<GlobalIdConfig> configs = globalIdConfigService.list(wrapper);
        return CollUtil.isNotEmpty(configs) ? (GlobalIdConfig)configs.get(0) : null;
    }


    public void checkReErrorStatus(String reId){
        if(reId == null || reId.length() == 0){
            return  ;
        }
        SignRe re = reService.getById(reId);
        if(re == null){
            return ;
        }
        //获取签约文件列表
        List<SignReDoc> docList = reDocService.listByReId(re.getId());
        //获取签署人数据
        List<DocSignerVo> signerList = getSignerList(reId);
        //如果是签署人自定义设置，或者没有签约文件，或者没有签署人，则没有异常
        if(re.getSignerType().equals(SignReSignerTypeEnum.CUSTOM.getCode()) || docList == null || docList.size() == 0 || signerList == null || signerList.size() == 0){
            re.setErrorStatus(SignReErrorStatusEnum.NO.getCode());
            boolean b = reService.updateById(re);
            if(!b){
                throw new PaasException("校验业务线是否异常失败");
            }
            return ;
        }
        //获取业务线签署相关控件数据
        List<SignReDocControl> signReDocControlList = reDocControlService.listByParam(reId);
        //获取业务线填写关联关系相关控件数据
        List<SignReDocParam> paramList = reDocParamService.listByReId(reId);
        //计算填写异常状态
        Boolean writeErrorStatus = checkWriteErrorStatus(docList, paramList, signerList);
        //计算自动签署异常状态
        Boolean autoSignErrorStatus = checkAutoSignErrorStatus(signerList, signReDocControlList);
        //设定异常状态
        Integer errorStatus = null ;
        if(writeErrorStatus && autoSignErrorStatus){
            //全部异常
            errorStatus = SignReErrorStatusEnum.ALL_ERROR.getCode() ;
        }
        else if(writeErrorStatus){
            //签署控件关联性存在异常
            errorStatus = SignReErrorStatusEnum.WRITE_ERROR.getCode();

        }
        else if(autoSignErrorStatus){
            //自动签存在异常
            errorStatus = SignReErrorStatusEnum.AUTO_SIGN_ERROR.getCode();
        }
        //保存异常状态
        if(errorStatus != null){
            re.setErrorStatus(errorStatus);
            boolean b = reService.updateById(re);
            if(!b){
                throw new PaasException("校验业务线是否异常失败");
            }
            return;
        }
        //没有异常，更新为正常状态
        re.setErrorStatus(SignReErrorStatusEnum.NO.getCode());
        boolean b = reService.updateById(re);
        if(!b){
            throw new PaasException("校验业务线是否异常失败");
        }

    }




//    /**
//     * @Description #校验自动签署签章权限
//     * @Param []
//     * @return java.lang.Boolean
//     **/
//    public Boolean checkAutoSignAuth(List<DocSignerVo> signerList){
//
//        if(signerList != null && signerList.size() > 0){
//            for(DocSignerVo signerVo : signerList){
//                if(SignerTypeEnum.SENDER.getCode().equals(signerVo.getSignerType())){
//                    List<DocSenderVo> senderList = signerVo.getSenderList();
//                    if(senderList != null && senderList.size() > 0){
//                        for(DocSenderVo senderVo : senderList){
//                            //企业签章
//                            if(SenderTypeEnum.ENTERPRISE.getCode().equals(senderVo.getSenderType())){
//                                //如果是自动签署
//                                if(SenderSignTypeEnum.AUTO.getCode().equals(senderVo.getSenderSignType())){
//
//                                }else {
//
//                                }
//                                //确定存在自动签
//                                String senderUserId = senderVo.getSenderUserId();
//                                if(senderUserId == null || senderUserId.length() == 0){
//                                    return true ;
//                                }
//                                String senderSealId = senderVo.getSenderSealId();
//                                if(senderSealId == null || senderSealId.length() == 0){
//                                    return true ;
//                                }
//                                //校验权限
//                                List<Integer> businessTypeRoleList = new ArrayList<>();
//                                businessTypeRoleList.add(BusinessAuthTypeRole.TEMPLATE_USER.getCode());
//                                List<SysTenantUser> tenantUserByBusinessRelationId = authBusinessService.getTenantUserByBusinessRelationId(senderSealId, businessTypeRoleList, BusinessAuthType.SEAL.getCode());
//                                if(tenantUserByBusinessRelationId == null || tenantUserByBusinessRelationId.size() == 0){
//                                    return true ;
//                                }
//                                List<String> tenantUserIdList = tenantUserByBusinessRelationId.stream().map(SysTenantUser::getId).collect(Collectors.toList());
//                                if(tenantUserIdList == null || tenantUserIdList.size() == 0){
//                                    return true ;
//                                }
//                                if(!tenantUserIdList.contains(senderUserId)){
//                                    return true ;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return false ;
//    }

    /**
     * @Description #校验自动签署相关参数是否有异常
     * @Param [signerList, signReDocControlList]
     * @return java.lang.Boolean
     **/
    public Boolean checkAutoSignErrorStatus(List<DocSignerVo> signerList,List<SignReDocControl> signReDocControlList){
        //校验是否存在自动签署
        Boolean autoSignFlag = false ;
        List<String> autoSignSenderIdList = new ArrayList<>() ;
        Boolean autoSignErrorStatus = false ;

        if(signerList != null && signerList.size() > 0){
            for(DocSignerVo signerVo : signerList){
                if(SignerTypeEnum.SENDER.getCode().equals(signerVo.getSignerType())){
                    List<DocSenderVo> senderList = signerVo.getSenderList();
                    if(senderList != null && senderList.size() > 0){
                        for(DocSenderVo senderVo : senderList){
                            if(SenderTypeEnum.ENTERPRISE.getCode().equals(senderVo.getSenderType())
                                    && SenderSignTypeEnum.AUTO.getCode().equals(senderVo.getSenderSignType())){
                                //确定存在自动签
                                autoSignFlag = true ;
                                autoSignSenderIdList.add(senderVo.getId());
                            }
                        }
                    }
                }
            }
        }
        //如果存在自动签
        if(autoSignFlag){
            if(autoSignSenderIdList.size() == 0){
                //如果自动签章的签署人为空
                autoSignErrorStatus = true ;
            }
            if(signReDocControlList == null || signReDocControlList.size() == 0){
                //如果没有签署控件
                autoSignErrorStatus = true ;
            }
            List<SignReDocControl> signControlList = new ArrayList<>();
            for(SignReDocControl signReDocControl : signReDocControlList){
                if(signReDocControl.getControlType() != null){
                    if(ControlTypeEnum.SEAL.getName().equals(signReDocControl.getControlType())
                            || ControlTypeEnum.CHOP_STAMP.getName().equals(signReDocControl.getControlType())){
                        signControlList.add(signReDocControl);
                    }
                }
            }
            if(signControlList.size() == 0){
                //企业签章自动签署控件为0，
                autoSignErrorStatus = true ;
            }else {
                //获取企业签章自动签署控件中对应的签署人id
                List<String> autoSignSignerIdList = signControlList.stream().map(SignReDocControl::getSignerId).collect(Collectors.toList());
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
     * @Description #校验填写控件是否有异常
     * @Param [docList, paramList, signerList]
     * @return java.lang.Boolean
     **/
    public Boolean checkWriteErrorStatus(List<SignReDoc> docList,List<SignReDocParam> paramList,List<DocSignerVo> signerList){
        //校验是否所有填写控件都关联的签署人
        Boolean writeErrorStatus = false ;
        List<String> templateIdList = null;
        for(SignReDoc reDoc : docList){
            //如果是模板类型，获取模板id
            if(reDoc.getDocType().equals(SignFileTypeEnum.TEMPLATE.getCode())){
                if(templateIdList == null){
                    templateIdList = new ArrayList<>() ;
                }
                templateIdList.add(reDoc.getDocOriginId());
            }
        }
        //如果存在模板再校验填写参数
        if(templateIdList != null && templateIdList.size() > 0){
            Integer templateControlCount = templateControlService.count(templateIdList);
            if(templateControlCount != null && templateControlCount > 0){
                if(paramList == null || paramList.size() == 0 || paramList.size() != templateControlCount){
                    //如果业务线填写控件关联参数的数量和模板的填写控件数量对不上
                    writeErrorStatus = true ;
                } else {
                    List<String> signerIdList = signerList.stream().map(DocSignerVo::getId).collect(Collectors.toList());
                    if(signerIdList == null || signerIdList.size() == 0){
                        writeErrorStatus = true ;
                    }
                    for(SignReDocParam reDocParam : paramList){
                        if(reDocParam.getSignerId() == null || reDocParam.getSignerId().length() == 0){
                            //如果业务线填写控件关联参数中指定的签署人为空
                            writeErrorStatus = true ;
                        }
                        if(!signerIdList.contains(reDocParam.getSignerId())){
                            //如果业务线填写控件关联参数中指定的签署人不包含在现有签署人id列表中
                            writeErrorStatus = true ;
                        }
                    }
                }
            }
        }

        return writeErrorStatus ;
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
                if (controlVo.getSignReDocId() == null || controlVo.getSignReDocId().length() == 0) {
                    return "业务线-签约文件id-参数缺失";
                }
                if (controlVo.getPage() == null) {
                    return "控件所属页码-参数缺失";
                }
            }

        }
        return null;
    }





}