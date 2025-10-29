/**
 * @description 电子签署接口服务
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
package com.kaifangqian.modules.api.controller;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kaifangqian.modules.api.exception.RequestParamsException;
import com.kaifangqian.modules.api.vo.base.*;
import com.kaifangqian.modules.api.vo.request.*;
import com.kaifangqian.modules.api.vo.response.ContractTasksResponse;
import com.kaifangqian.modules.api.vo.response.PageUrlResponse;
import com.kaifangqian.modules.api.vo.response.SignResResponse;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.service.re.*;
import com.kaifangqian.modules.opensign.service.ru.*;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.enums.TenantAuthStatus;
import com.kaifangqian.modules.system.enums.TenantAuthType;
import com.kaifangqian.modules.system.enums.TenantType;
import com.kaifangqian.modules.system.service.*;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.base.entity.BaseEntity;
import com.kaifangqian.common.constant.ApiCode;
import com.kaifangqian.common.system.vo.ApiCommonRes;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.FileType;
import com.kaifangqian.common.util.FileTypeJudge;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.util.RsaUtils;
import com.kaifangqian.modules.api.aop.ApiSignThreadLocalAop;
import com.kaifangqian.modules.api.aop.CheckParamAspect;
import com.kaifangqian.modules.api.business.service.ContactSignPersonalProxyService;
import com.kaifangqian.modules.api.business.service.ContractDraftAndStartService;
import com.kaifangqian.modules.api.business.service.ValidService;
import com.kaifangqian.modules.api.entity.ApiCallbackPageUrl;
import com.kaifangqian.modules.api.business.service.ContractService;
import com.kaifangqian.modules.api.service.ApiCallbackPageUrlService;
import com.kaifangqian.modules.api.service.IApiCallbackService;
import com.kaifangqian.modules.opensign.constant.SignCommonConstant;
import com.kaifangqian.modules.opensign.service.business.PdfConvertService;
import com.kaifangqian.modules.opensign.service.business.PdfEncryptionService;
import com.kaifangqian.modules.opensign.service.business.RuBusinessService;
import com.kaifangqian.modules.opensign.service.business.RuSignService;
import com.kaifangqian.modules.opensign.service.template.SignTemplateControlService;
import com.kaifangqian.modules.opensign.service.template.SignTemplateRecordService;
import com.kaifangqian.modules.opensign.service.template.SignTemplateService;
import com.kaifangqian.modules.opensign.service.tool.SignFileService;
import com.kaifangqian.modules.opensign.utils.Base64;
import com.kaifangqian.modules.opensign.vo.base.sign.DocImageVo;
import com.kaifangqian.modules.opensign.vo.base.sign.TaskSearchFor3rdVO;
import com.kaifangqian.modules.storage.StorageService;
import com.kaifangqian.modules.storage.entity.AnnexStorage;
import com.kaifangqian.modules.storage.service.IAnnexStorageService;
import com.kaifangqian.utils.DateUtil;
import com.kaifangqian.utils.MyStringUtils;
import com.kaifangqian.utils.UUIDGenerator;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import com.kaifangqian.config.limit.annotation.Limit;
import com.kaifangqian.config.limit.annotation.LimitHandleType;
import com.kaifangqian.config.limit.annotation.LimitType;
import com.kaifangqian.config.limit.annotation.OperateType;

/**
 * @Description: ContractController
 * @Package: com.kaifangqian.modules.api.controller
 * @ClassName: ContractController
 * @author: FengLai_Gong
 * @Date: 2024/5/23
 */
@Slf4j
@RestController
@RequestMapping("/kaifangqian/openAPI/V2")
@ResrunLogModule(name = "开放签-openAPI")
// @Api(tags = "开放签-openAPI")
public class ContractController {

    @Autowired
    private RuBusinessService ruBusinessService ;


    @Autowired
    private SignReService reService ;
    @Autowired
    private SignReAuthService reAuthService ;
    @Autowired
    private SignReDocService reDocService ;
    @Autowired
    private SignReSignerService reSignerService ;
    @Autowired
    private SignReSenderService reSenderService ;
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
    private SignRuService ruService ;
    @Autowired
    private SignRuDocService ruDocService ;
    @Autowired
    private SignRuDocControlService ruDocControlService ;
    @Autowired
    private SignRuSignerService ruSignerService ;
    @Autowired
    private SignRuSenderService ruSenderService ;
    @Autowired
    private SignRuDocOperateService ruDocOperateService ;
    @Autowired
    private SignRuOperatorService ruOperatorService ;
    @Autowired
    private SignRuRelationService relationService ;
    @Autowired
    private RuSignService ruSignService ;

    @Autowired
    private SignFileService signFileService ;
    @Autowired
    private StorageService storageService;
    @Autowired
    private IAnnexStorageService iAnnexStorageService;


    @Autowired
    private ContractService contractService ;
    @Autowired
    private SignRuTaskService ruTaskService ;

    @Autowired
    private IApiDeveloperManageService apiDeveloperManageService ;
    @Autowired
    private ISysUserService sysUserService ;
    @Autowired
    private ISysTenantInfoService tenantInfoService ;
    @Autowired
    private ISysTenantUserService tenantUserService ;

    @Autowired
    private IApiCallbackService apiCallbackService ;

    @Autowired
    private ISysAppInfoService sysAppInfoService;

    @Autowired
    private ApiCallbackPageUrlService apiCallbackPageUrlService;

    @Autowired
    private ApiSignThreadLocalAop apiSignThreadLocalAop;

    @Autowired
    private CheckParamAspect checkParamAspect ;

    @Autowired
    private ITenantInfoExtendService tenantInfoExtendService;
    @Autowired
    private ITenantAuthLogService tenantAuthLogService;

    @Autowired
    private ContactSignPersonalProxyService contactSignPersonalProxyService ;
    @Autowired
    private ContractDraftAndStartService contractDraftAndStartService ;

    @Autowired
    private ValidService validService ;

    @Autowired
    private PdfEncryptionService pdfEncryptionService ;
    @Autowired
    private PdfConvertService pdfConvertService ;


    // @ApiOperation(value = "获取业务线列表", notes = "获取业务线列表")
    @GetMapping("/signRes")
    @ResponseBody
    public ApiCommonRes<?> signRes(SignResRequest request) {

        ApiDeveloperManage apiDeveloperManage = apiDeveloperManageService.getByToken(request.getAppAuthToken());
        if(apiDeveloperManage == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,token权限不存在");
        }

        SysTenantInfo sysTenantInfo = tenantInfoService.getById(apiDeveloperManage.getTenantId());
        if(sysTenantInfo == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,token关联租户不存在");
        }

        SysUser sysUser = sysUserService.getUserByName(request.getContact());
        if(sysUser == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,contact：" + request.getContact() + "数据不存在");
        }
        SysTenantUser sysTenantUser = tenantUserService.getTenantUser(sysTenantInfo.getId(), sysUser.getId());
        if(sysTenantUser == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,contact：" + request.getContact() + "不存在于" + sysTenantInfo.getTenantName());
        }

        List<SignResResponse> responseList = new ArrayList<>();

        List<SignReAuth> auths = new ArrayList<>();
        //指定使用者列表
        QueryWrapper<SignReAuth> userTenantQueryWrapper = new QueryWrapper<>();
        userTenantQueryWrapper.lambda().eq(SignReAuth::getUserId,sysTenantUser.getId());
        userTenantQueryWrapper.lambda().eq(SignReAuth::getAuthType,SignReAuthTypeEnum.USER.getCode());
        userTenantQueryWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        List<SignReAuth> userTenantList = reAuthService.list(userTenantQueryWrapper);
        if(userTenantList != null && userTenantList.size() > 0){
            auths.addAll(userTenantList);
        }
        //未指定使用者列表
        QueryWrapper<SignReAuth> tenantQueryWrapper = new QueryWrapper<>();
        tenantQueryWrapper.lambda().isNull(SignReAuth::getUserId);
        tenantQueryWrapper.lambda().eq(SignReAuth::getTenantId,sysTenantUser.getTenantId());
        tenantQueryWrapper.lambda().eq(SignReAuth::getAuthType,SignReAuthTypeEnum.USER.getCode());
        tenantQueryWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        List<SignReAuth> tenantList = reAuthService.list(tenantQueryWrapper);
        if(tenantList != null && tenantList.size() > 0){
            auths.addAll(tenantList);
        }
        if(auths.size() == 0){
            return ApiCommonRes.ok(responseList) ;
        }
        List<String> reIdList = auths.stream().map(SignReAuth::getSignReId).collect(Collectors.toList());
        if(reIdList == null || reIdList.size() == 0){
            return ApiCommonRes.ok(responseList) ;
        }
        QueryWrapper<SignRe> signReQueryWrapper = new QueryWrapper<>();
        signReQueryWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        signReQueryWrapper.lambda().in(SignRe::getId,reIdList);
        signReQueryWrapper.lambda().eq(SignRe::getStatus,SignReStatusEnum.ENABLE.getCode());
        signReQueryWrapper.lambda().orderByDesc(BaseEntity::getUpdateTime);
        List<SignRe> signReList = reService.list(signReQueryWrapper);
        if(signReList != null && signReList.size() > 0){
            for(SignRe re : signReList){
                SignResResponse response = new SignResResponse();
                response.setSignReId(re.getId());
                response.setSignReName(re.getName());
                responseList.add(response);
            }
        }
        return ApiCommonRes.ok(responseList) ;
    }

    // @ApiOperation(value = "企业账号注册+实名认证", notes = "企业账号注册+实名认证")
    @PostMapping("/companyCreateAndAuth")
    @ResponseBody
    public ApiCommonRes<?> companyCreateAndAuth() {
        String cachedRequestBody = apiSignThreadLocalAop.getRequestBodyCache().get();
        CompanyCreateAndAuthRequest request = JSON.parseObject(cachedRequestBody, CompanyCreateAndAuthRequest.class);
        //校验参数
        validService.companyCreateAndAuthRequestValid(request);
        if(request.getIsMakeSeal() == null || request.getIsMakeSeal() != 1){
            request.setIsMakeSeal(0);
        }
        //创建企业租户
        EnterpriseRegister register = new EnterpriseRegister();
        //经办人账号相关数据
        register.setName(request.getAccount().getName());

        register.setAccount(request.getAccount().getContact());
        register.setContactType(request.getAccount().getContactType());
        if(request.getAccount().getContactType().equals("MOBILE")){
            register.setMobile(request.getAccount().getContact());
        }else if(request.getAccount().getContactType().equals("EMAIL")){
            register.setEmail(request.getAccount().getContact());
        }
        register.setCreditCode(request.getCreditCode());
        register.setLegalPerson(request.getLegalPerson());
        register.setIdentity(request.getIdentity());
        //企业租户相关数据
        register.setEnterpriseName(request.getCompanyName());
        //基础数据
        register.setAppAuthToken(request.getAppAuthToken());
        register.setOperatorAccount(request.getOperatorAccount());
        register.setUniqueCode(request.getUniqueCode());
        register.setIsMakeSeal(request.getIsMakeSeal());
        //设置线程变量
        LoginUser currentUser = new LoginUser();
        MySecurityUtils.THREAD_LOCAL.set(currentUser);
        //创建租户
        tenantInfoExtendService.enterpriseTenantRegisterForApi(register);

        return ApiCommonRes.ok();
    }





    // @ApiOperation(value = "个人账号注册+实名认证", notes = "个人账号注册+实名认证")
    @PostMapping("/personCreateAndAuth")
    @ResponseBody
    public ApiCommonRes<?> personCreateAndAuth() {

        String cachedRequestBody = apiSignThreadLocalAop.getRequestBodyCache().get();
        PersonCreateAndAuthRequest request = JSON.parseObject(cachedRequestBody, PersonCreateAndAuthRequest.class);

        checkParamAspect.checkPublicParams(request);
        //校验参数
        validService.personalCreateAndAuthRequestValid(request);

        ContractUser account = request.getAccount();

        //校验、注册、开通个人租户
        PersonalRegister register = new PersonalRegister();
        register.setName(account.getName());
        register.setAccount(account.getContact());
        register.setContactType(account.getContactType());
        if(account.getContactType().equals("MOBILE")){
            register.setMobile(account.getContact());
        }else if(account.getContactType().equals("EMAIL")){
            register.setEmail(account.getContact());
        }

        String personalTenantId = tenantInfoExtendService.personalTenantRegisterForApi(register);
        if(personalTenantId == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,开通租户失败");
        }
        String orgNo = null ;
        String phone = null ;
        try {
            orgNo = RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING, request.getIdentity());
            if(account.getContactType().equals("MOBILE")){
                phone = RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING, account.getContact());
            }else if(MyStringUtils.isNotBlank(request.getMobile())){
                phone = RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING, request.getMobile());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String authLogId = UUIDGenerator.generate();
        TenantInfoExtend infoExtend = tenantInfoExtendService.getOne(Wrappers.lambdaQuery(TenantInfoExtend.class).eq(TenantInfoExtend::getTenantId, personalTenantId));
        if (infoExtend == null) {
            infoExtend = new TenantInfoExtend();
        }
        infoExtend.setName(account.getName());
        infoExtend.setOrganizationNo(orgNo);
        infoExtend.setPhone(phone);
        if(account.getContactType().equals("EMAIL")){
            infoExtend.setEmail(account.getContact());
        }
        if(infoExtend.getAuthStatus() != null && infoExtend.getAuthStatus() == TenantAuthStatus.STATUS2.getStatus()){
            infoExtend.setAuthStatus(TenantAuthStatus.STATUS2.getStatus());
        }else{
            infoExtend.setAuthStatus(TenantAuthStatus.STATUS0.getStatus());
        }

        infoExtend.setTenantId(personalTenantId);
        infoExtend.setAuthType(TenantAuthType.CREATED.getType());
        infoExtend.setAuthId(authLogId);
        infoExtend.setTenantType(TenantType.PERSONAL.getType());
        infoExtend.setIdCardType(request.getIdCardType());
        if(MyStringUtils.isNotBlank(request.getBankCard())){
            infoExtend.setBankCard(request.getBankCard());
        }
        tenantInfoExtendService.saveOrUpdate(infoExtend);

        return ApiCommonRes.ok();

    }





    // @ApiOperation(value = "上传本地签约文件", notes = "上传本地签约文件")
    @PostMapping("/document/file")
    @ResponseBody
    @Limit(name = "上传本地签约文件", prefix = "limit",limitType= LimitType.IP, operateType = OperateType.ALL, count = 5,period = 1,limitHandle = LimitHandleType.NONE)
    public ApiCommonRes<ContractDocumentFile> documentFile() {

        String cachedRequestBody = apiSignThreadLocalAop.getRequestBodyCache().get();
        DocumentFileRequest request = JSON.parseObject(cachedRequestBody, DocumentFileRequest.class);


        checkParamAspect.checkPublicParams(request);
        //校验参数
        validService.documentFileRequestValid(request);


        //base64解码
        byte[] decode = null ;
        try {
            decode  = Base64.decode(request.getFile());
        }catch (Exception e){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,base64解析失败");
        }
        if(decode == null || decode.length == 0){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,base64解析失败");
        }
        //校验文件类型
        byte[] fileByte = decode ;
        FileType fileType = null;
        try {
            fileType = FileTypeJudge.getType(fileByte);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(fileType == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"不支持的文件类型");
        }
        //只能是pdf、excel、word、
        if(!fileType.getValue().equals(FileType.PDF.getValue()) && !fileType.getValue().equals(FileType.OFFICE_OLD.getValue()) && !fileType.getValue().equals(FileType.OFFICE_NEW.getValue())){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"不支持的文件类型");
        }
        byte[] pdfByte = null ;
        //如果是excel、word，查看是否配置的转换
        if(fileType.getValue().equals(FileType.OFFICE_OLD.getValue()) || fileType.getValue().equals(FileType.OFFICE_NEW.getValue())){
            //判读是否开始office转换
            Boolean convert = pdfConvertService.isConvert();
            if(!convert){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"不支持的文件类型");
            }
            //开始转换
            if(request.getFileSuffix().equals(".doc") || request.getFileSuffix().equals(".docx")){
                //word转pdf
                pdfByte = pdfConvertService.wordConvertPdf(fileByte);
            }else if(request.getFileSuffix().equals(".xls") || request.getFileSuffix().equals(".xlsx")){
                //excel转pdf
                pdfByte = pdfConvertService.excelConvertPdf(fileByte);
            }
            if(pdfByte == null){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"文件转换失败");
            }
        }else {
            pdfByte = fileByte ;
        }
        if(pdfByte == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"文件不存在或损坏");
        }
        //保存签约文件
        String annexId = signFileService.saveAnnexStorage(pdfByte, request.getFileName(), ".pdf", SignFileEnum.SIGN_FILE_MAIN);
        if(annexId == null || annexId.length() == 0){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,保存签约文件失败");
        }
        //转图片
        List<DocImageVo> voList = signFileService.saveAndConvertImage(annexId, pdfByte);
        if(voList == null || voList.size() == 0){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签约文件图片转换失败");
        }
        //返回
        ContractDocumentFile contractDocumentFile = new ContractDocumentFile();
        contractDocumentFile.setDocId(annexId);
        return ApiCommonRes.ok(contractDocumentFile);
    }

    // @ApiOperation(value = "上传文件（公共）", notes = "上传文件（公共）")
    @PostMapping("/annex/file")
    @ResponseBody
    @Limit(name = "上传文件", prefix = "limit",limitType= LimitType.IP, operateType = OperateType.ALL, count = 5,period=1,limitHandle = LimitHandleType.NONE)
    public ApiCommonRes<ContractAnnexFile> annexFile() {

        String cachedRequestBody = apiSignThreadLocalAop.getRequestBodyCache().get();
        AnnexFileRequest request = JSON.parseObject(cachedRequestBody, AnnexFileRequest.class);

        validService.annexFileRequestValid(request);
        //base64解码
        byte[] decode = null ;
        try {
            decode  = Base64.decode(request.getFile());
        }catch (Exception e){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,base64解析失败");
        }
        if(decode == null || decode.length == 0){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,base64解析失败");
        }
        //保存附件文件
        String annexId = signFileService.saveAnnexStorage(decode, request.getFileName(), request.getFileSuffix(), SignFileEnum.SIGN_FILE_OTHER);
        if(annexId == null || annexId.length() == 0){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,保存合同附件失败");
        }
        //返回
        ContractAnnexFile contractAnnexFile = new ContractAnnexFile();
        contractAnnexFile.setFileId(annexId);
        return ApiCommonRes.ok(contractAnnexFile);
    }

    // @ApiOperation(value = "合同创建草稿-发起", notes = "合同创建草稿-发起")
    @PostMapping("/contract/draft")
    @ResponseBody
    @Limit(name = "合同创建草稿", prefix = "limit",limitType= LimitType.IP, operateType = OperateType.ALL, count = 5,period=1,limitHandle = LimitHandleType.NONE)
    public ApiCommonRes<SignContract> contractDraft() {

        String cachedRequestBody = apiSignThreadLocalAop.getRequestBodyCache().get();
        ContractDraftRequest request = JSON.parseObject(cachedRequestBody, ContractDraftRequest.class);

        request.setSendType(ContractSendTypeEnum.API.getType());
        String contractId = contractDraftAndStartService.contractDraftAndStart(request);
        if(contractId == null || contractId.length() == 0){
            return ApiCommonRes.error(ApiCode.UNKNOWN);
        }
        SignContract signContract = new SignContract();
        signContract.setContractId(contractId);

        return ApiCommonRes.ok(signContract);
    }



    // @ApiOperation(value = "授权签署", notes = "授权签署")
    @PostMapping("/contract/sign/personal/proxy")
    @ResponseBody
    public ApiCommonRes<?> contactSignPersonalProxy()  {
        String cachedRequestBody = apiSignThreadLocalAop.getRequestBodyCache().get();
        ContractSignPersonalProxy contractSignPersonalProxy = JSON.parseObject(cachedRequestBody, ContractSignPersonalProxy.class);

        contactSignPersonalProxyService.contactSignPersonalProxy(contractSignPersonalProxy);

        return ApiCommonRes.ok() ;
    }

    // @ApiOperation(value = "添加合同签署节点", notes = "添加合同签署节点")
    @PostMapping("/contract/signer/add")
    @ResponseBody
    public ApiCommonRes<?> addContractSignNode()  {
        String cachedRequestBody = apiSignThreadLocalAop.getRequestBodyCache().get();
        ContractDraftRequest request = JSON.parseObject(cachedRequestBody, ContractDraftRequest.class);

        contractDraftAndStartService.addSignNode(request);

        SignContract signContract = new SignContract();
        signContract.setContractId(request.getContractId());

        return ApiCommonRes.ok(signContract) ;
    }

    // @ApiOperation(value = "合同签署审批", notes = "合同签署审批")
    @PostMapping("/contract/sign/approve")
    @ResponseBody
    public ApiCommonRes<?> contractSignApprove()  {
        String cachedRequestBody = apiSignThreadLocalAop.getRequestBodyCache().get();
        ContractDraftRequest request = JSON.parseObject(cachedRequestBody, ContractDraftRequest.class);

        contractDraftAndStartService.addSignNode(request);

        SignContract signContract = new SignContract();
        signContract.setContractId(request.getContractId());

        return ApiCommonRes.ok(signContract) ;
    }

    @PostMapping("/contract/finish")
    @ResponseBody
    public ApiCommonRes<?> completeSign()  {
        String cachedRequestBody = apiSignThreadLocalAop.getRequestBodyCache().get();
        ContractCompleteRequest request = JSON.parseObject(cachedRequestBody, ContractCompleteRequest.class);

        contractDraftAndStartService.completeSign(request);

        SignContract signContract = new SignContract();
        signContract.setContractId(request.getContractId());

        return ApiCommonRes.ok(signContract) ;
    }




//    // @ApiOperation(value = "获取合同草稿编辑页面", notes = "获取合同草稿编辑页面")
//    @GetMapping("/contract/modifyUrl")
//    public ApiCommonRes<PageUrlResponse> contractModifyUrl(ContractModifyUrlRequest request) {
//
//
//        return ApiCommonRes.ok();
//    }


    // @ApiOperation(value = "获取合同待办任务", notes = "获取合同待办任务")
    @GetMapping("/contract/tasks")
    @ResponseBody
    public ApiCommonRes<ContractTasksResponse> contractTasks(ContractTasksRequest request) {

        String contractId = request.getContractId();
        if(contractId == null || contractId.length() == 0){
            return ApiCommonRes.of(ApiCode.PARAM_MISSING.getCode(), MessageFormat.format(ApiCode.PARAM_MISSING.getTemplate(),"contractId"));
        }
        SignRu signRu = ruService.getById(request.getContractId());
        if(signRu == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,contractId：" + request.getContractId() + "数据不存在");
        }
        ContractTasksResponse response = new ContractTasksResponse();
        SignRuStatusEnum ruStatusEnum = SignRuStatusEnum.getCode(signRu.getStatus());
        if(ruStatusEnum == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,contractId：" + request.getContractId() + "状态异常");
        }
        response.setStatus(ruStatusEnum.getType());
        List<ContractTodoTask> todoTaskList = new ArrayList<>();
        if(!SignRuStatusEnum.getDoneList().contains(signRu.getStatus())){
            SignRuTask query = new SignRuTask();
            query.setSignRuId(signRu.getId());
            query.setTaskStatus(1);
            query.setDeleteFlag(false);
            List<SignRuTask> taskList = ruTaskService.getByEntity(query);
            if(taskList != null && taskList.size() > 0){
                for(SignRuTask ruTask : taskList){
                    ContractTodoTask task = new ContractTodoTask();
                    task.setTaskId(ruTask.getId());
                    //任务类型
                    if(ruTask.getTaskType().equals(TaskTypeEnum.SIGN_TASK.getCode())){
                        task.setTaskType("SIGN");
                    }else if(ruTask.getTaskType().equals(TaskTypeEnum.WRITE_TASK.getCode())){
                        task.setTaskType("WRITE");
                    }else if(ruTask.getTaskType().equals(TaskTypeEnum.APPROVE_TASK.getCode())){
                        task.setTaskType("APPROVE");
                    }
                    else {
                        continue;
                    }
                    String tenantId = ruTask.getTenantId();
                    SysTenantInfo tenantInfo = tenantInfoService.getById(tenantId);
                    //任务办理方类型
                    if(ruTask.getUserType() == SignerTypeEnum.SENDER.getCode()){
                        task.setPartyType("SENDER");
                    }else if(ruTask.getUserType() == SignerTypeEnum.RECEIVER_PERSONAL.getCode()){
                        task.setPartyType("PERSONAL");
                        SignRuSigner signer = ruSignerService.getById(ruTask.getUserTaskId());
                        if(signer != null){
                            ContractUser user = new ContractUser();
                            if(signer.getSignerExternalType() != null && signer.getSignerExternalType() == SignRuSignerExternalTypeEnum.PHONE.getCode()){
                                user.setContactType("MOBILE");
                            }else if(signer.getSignerExternalType() != null && signer.getSignerExternalType() == SignRuSignerExternalTypeEnum.EMAIL.getCode()){
                                user.setContactType("EMAIL");
                            }
                            user.setContact(signer.getSignerExternalValue());

                            user.setName(signer.getSignerName());
                            task.setParty(user);
                        }
                    }else if(ruTask.getUserType() == SignerTypeEnum.RECEIVER_ENT.getCode()){
                        task.setPartyType("ENTERPRISE");
                        SignRuSender ruSender = ruSenderService.getById(ruTask.getUserTaskId());
                        if(ruSender != null){
                            ContractUser user = new ContractUser();
                            user.setContact(ruSender.getSenderExternalValue());
                            if(ruSender.getSenderExternalType() != null && ruSender.getSenderExternalType() == SignRuSignerExternalTypeEnum.PHONE.getCode()){
                                user.setContactType("MOBILE");
                            }else if(ruSender.getSenderExternalType() != null && ruSender.getSenderExternalType() == SignRuSignerExternalTypeEnum.EMAIL.getCode()){
                                user.setContactType("EMAIL");
                            }
                            user.setName(ruSender.getSenderName());
                            task.setParty(user);
                        }
                    }
                    //办理方名称
                    if(tenantInfo != null){
                        task.setPartyName(tenantInfo.getTenantName());
                    }
                    //办理人账号唯一标识
                    task.setPartyAccount(ruTask.getTenantUserId());


                    todoTaskList.add(task);
                }
            }

        }
        response.setTodoTaskList(todoTaskList);
        return ApiCommonRes.ok(response);
    }


    // @ApiOperation(value = "获取待办任务链接", notes = "获取待办任务链接")
    @GetMapping("/contract/taskUrl")
    @ResponseBody
    public ApiCommonRes<PageUrlResponse> contractTaskUrl(ContractTaskUrlRequest request) {


        String contractId = request.getContractId();
        if(contractId == null || contractId.length() == 0){
            throw new RequestParamsException(ApiCode.PARAM_MISSING, "contractId参数缺失");
        }
        String taskId = request.getTaskId();
        if(taskId == null || taskId.length() == 0){
            throw new RequestParamsException(ApiCode.PARAM_MISSING, "taskId参数缺失");
        }
        SignRu signRu = ruService.getById(contractId);
        if(signRu == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,contractId：" + contractId + "数据不存在");
        }
        SignRuTask ruTask = ruTaskService.getById(taskId);
        if(ruTask == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,taskId：" + taskId + "数据不存在");
        }
//        String tenantUserId = ruTask.getTenantUserId();
//        SysTenantUser tenantUser = tenantUserService.getById(tenantUserId);
//        if(tenantUser == null){
//            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,经办人不存在");
//        }
//        SysUser sysUser = sysUserService.getById(tenantUser.getUserId());
//        if(sysUser == null){
//            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,经办人不存在");
//        }
        String taskType = "" ;
        if(ruTask.getTaskType().equals(TaskTypeEnum.SIGN_TASK.getCode())){
            taskType = "sign";
        }else if(ruTask.getTaskType().equals(TaskTypeEnum.WRITE_TASK.getCode())){
            taskType = "write";
        }
        String addrUrl = "";
        SysAppInfo sysAppInfo = sysAppInfoService.getById("490489ab-d8b4-414c-ad77-d856962c286f");
        if(sysAppInfo != null && sysAppInfo.getAppAddress() != null){
            addrUrl = sysAppInfo.getAppAddress();
        }
        String pageUrl = addrUrl + "/#/contract/detail/base?" +
                "taskType=" + taskType +
                "&phone=" + ruTask.getPhone() +
                "&signRuId=" + signRu.getId() +
                "&taskId=" + ruTask.getId() ;
        if(request.getCallbackPage() != null && request.getCallbackPage().length() > 0){

            ApiCallbackPageUrl callbackPageUrl = new ApiCallbackPageUrl();
            callbackPageUrl.setCallbackPageUrl(request.getCallbackPage() );
            callbackPageUrl.setCreateTime(new Date());
            boolean save = apiCallbackPageUrlService.save(callbackPageUrl);
            if(save){
                pageUrl = pageUrl + "&callbackPageNo=" + callbackPageUrl.getId() ;
            }
//            try {
//                String encodeUrl = URLEncoder.encode(request.getCallbackPage(), "UTF-8");
//                pageUrl = pageUrl + "&callbackPage=" + encodeUrl ;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
        PageUrlResponse pageUrlResponse = new PageUrlResponse();
        pageUrlResponse.setPageUrl(pageUrl);
        return ApiCommonRes.ok(pageUrlResponse);
    }


    // @ApiOperation(value = "获取合同短链接", notes = "获取合同短链接")
    @GetMapping("/contract/pageUrl")
    @ResponseBody
    public ApiCommonRes<PageUrlResponse> contractPageUrl(ContractPageUrlRequest request) {
        ApiDeveloperManage apiDeveloperManage = apiDeveloperManageService.getByToken(request.getAppAuthToken());
        if(apiDeveloperManage == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,token权限不存在");
        }
//        ContractUser party = request.getParty();
//        if(party == null){
//            throw new RequestParamsException(ApiCode.PARAM_MISSING, "办理人参数缺失");
//        }
//        if(party.getContact() == null){
//            throw new RequestParamsException(ApiCode.PARAM_MISSING, "联系方式参数缺失");
//        }
//        if(party.getContactType() == null){
//            throw new RequestParamsException(ApiCode.PARAM_MISSING, "联系方式参数缺失");
//        }
        if(request.getContact() == null){
            throw new RequestParamsException(ApiCode.PARAM_MISSING, "账号参数缺失");
        }
//        SysUser sysUser = sysUserService.getUserByName(request.getContact());
//        if(sysUser == null){
//            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,账号不存在");
//        }
//        SysTenantInfo tenantInfo = tenantInfoService.getById(apiDeveloperManage.getTenantId());
//        if(tenantInfo == null){
//            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,账号不存在");
//        }
//        SysTenantUser tenantUser = tenantUserService.getTenantUser(apiDeveloperManage.getTenantId(), sysUser.getId());
//        if(tenantUser == null){
//            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,经办人不存在");
//        }
//        String contractId = request.getContractId();
        if(request.getContractId() == null || request.getContractId().length() == 0){
            throw new RequestParamsException(ApiCode.PARAM_MISSING, "contractId参数缺失");
        }
//        String taskId = request.getTaskId();
//        if(taskId == null || taskId.length() == 0){
//            throw new RequestParamsException(ApiCode.PARAM_MISSING, "taskId参数缺失");
//        }
//        SignRu signRu = ruService.getById(contractId);
//        if(signRu == null){
//            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,contractId：" + contractId + "数据不存在");
//        }
//        SignRuTask ruTask = ruTaskService.getById(taskId);
//        if(ruTask == null){
//            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,taskId：" + taskId + "数据不存在");
//        }

        TaskSearchFor3rdVO taskSearchFor3rdVO = new TaskSearchFor3rdVO();
        if(request.getNoLogin()==null){
            request.setNoLogin(false);
        }
        //判断免登录时间   有效期最长 72小时=3天
        if(request.getNoLogin()){
            Date expireTime = null;
            if(StringUtils.isNotEmpty(request.getPageUrlExpireTime())){
                try {
                    expireTime = DateUtil.parseDate(request.getPageUrlExpireTime(),"yyyy-MM-dd HH:mm:ss");
                }catch (Exception e){
                    throw new RequestParamsException(ApiCode.PARAM_FORMAT_ERROR,"pageUrlExpireTime格式不正确：yyyy-MM-dd HH:mm:ss");
                }
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH,3);

            if(expireTime == null){
                taskSearchFor3rdVO.setPageUrlExpireTime(calendar.getTime());
            }else if(expireTime.getTime() > calendar.getTime().getTime()){
                //如果超过72小时就设置 72小时
                taskSearchFor3rdVO.setPageUrlExpireTime(calendar.getTime());
            }else {

                taskSearchFor3rdVO.setPageUrlExpireTime(expireTime);
            }
        }

        taskSearchFor3rdVO.setNoLogin(request.getNoLogin());
        taskSearchFor3rdVO.setSignRuId(request.getContractId());
        taskSearchFor3rdVO.setTaskId(request.getTaskId());
        taskSearchFor3rdVO.setUsername(request.getContact());
        taskSearchFor3rdVO.setTenantName(request.getPartyName());

        String codeFor3rd = ruTaskService.getCodeFor3rd(taskSearchFor3rdVO);
        if(codeFor3rd == null || codeFor3rd.length() == 0){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"无法获取合同短链接，请检查请求参数");
        }
        String addrUrl = "";
        SysAppInfo sysAppInfo = sysAppInfoService.getById("490489ab-d8b4-414c-ad77-d856962c286f");
        if(sysAppInfo != null && sysAppInfo.getAppAddress() != null){
            addrUrl = sysAppInfo.getAppAddress();
        }
        String pageUrl = addrUrl + "/#/contract/detail/base?" ;
        PageUrlResponse pageUrlResponse = new PageUrlResponse();

//        if(apiDeveloperManage.getCallbackUrl() != null && apiDeveloperManage.getCallbackUrl().length() > 0){
//            codeFor3rd = codeFor3rd + "&callbackPage=" + apiDeveloperManage.getCallbackUrl();
//        }
        pageUrl = pageUrl + "code=" + codeFor3rd;
        pageUrlResponse.setPageUrl(pageUrl);

        return ApiCommonRes.ok(pageUrlResponse);
    }

//    // @ApiOperation(value = "获取合同详情链接", notes = "获取合同详情链接")
//    @GetMapping("/contract/viewUrl")
//    public ApiCommonRes<PageUrlResponse> contractViewUrl(ContractViewUrlRequest request) {
//
//        return ApiCommonRes.ok();
//    }



//    // @ApiOperation(value = "设置签署截止日期", notes = "设置签署截止日期")
//    @PutMapping("/contract/expireDate")
//    public ApiCommonRes<?> contractExpireDate(@RequestBody ContractExpireDateRequest request) {
//
//
//        return ApiCommonRes.ok();
//    }

//    // @ApiOperation(value = "合同撤回", notes = "合同撤回")
//    @PutMapping("/contract/revoke")
//    public ApiCommonRes<?> contractRevoke(@RequestBody ContractRevokeRequest request) {
//        return ApiCommonRes.ok();
//    }


//    // @ApiOperation(value = "合同删除", notes = "合同删除")
//    @DeleteMapping("/contract")
//    public ApiCommonRes<?> contract(ContractDeleteRequest request) {
//        return ApiCommonRes.ok();
//    }


    // @ApiOperation(value = "下载签约文件", notes = "下载签约文件")
    @GetMapping("/contract/document/download")
    @Limit(name = "下载签约文件", prefix = "limit",limitType= LimitType.IP, operateType = OperateType.ALL, count = 5,period=1,limitHandle = LimitHandleType.NONE)
    public void contractDocumentDownload(ContractDocumentDownloadRequest request, HttpServletResponse response) {

        String contractId = request.getContractId();
        if(contractId == null || contractId.length() == 0){
            throw new RequestParamsException(ApiCode.PARAM_MISSING, "contractId参数缺失");
        }
        SignRu signRu = ruService.getById(request.getContractId());
        if(signRu == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,contractId：" + request.getContractId() + "数据不存在");
        }
//        ContractUser operator = request.getOperator();
//        if(operator == null || operator.getName() == null || operator.getContact() == null || operator.getContactType() == null){
//            throw new RequestParamsException(ApiCode.PARAM_MISSING,"发起人参数缺失");
//        }
        ApiDeveloperManage apiDeveloperManage = apiDeveloperManageService.getByToken(request.getAppAuthToken());
        if(apiDeveloperManage == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,token权限不存在");
        }
        String contact = request.getContact();
        SysUser sysUser = sysUserService.getUserByName(contact);
        if(sysUser == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,账号不存在");
        }
        SysTenantInfo tenantInfo = tenantInfoService.getById(apiDeveloperManage.getTenantId());
        if(tenantInfo == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,账号不存在");
        }
        List<SignRuSigner> signerList = ruSignerService.listByRuId(signRu.getId());
        if(signerList == null || signerList.size() == 0){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签署人不存在");
        }
        SignRe signRe = reService.getById(signRu.getSignReId());
        if(signRe == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,业务线id：" + signRu.getSignReId() + "数据不存在");
        }

        Boolean downloadAuthStatus = false ;

        SysTenantUser tenantUser = tenantUserService.getTenantUser(apiDeveloperManage.getTenantId(), sysUser.getId());
        if(tenantUser != null){

            List<Integer> authTypeList = new ArrayList<>();
            if(signRe.getDownloaderType() != null && (signRe.getDownloaderType() == SignDownloaderTypeEnum.VIEWER.getCode() || signRe.getDownloaderType() == SignDownloaderTypeEnum.ALL.getCode())){
                authTypeList.add(SignReAuthTypeEnum.VIEWER.getCode());
            }
            authTypeList.add(SignReAuthTypeEnum.DOWNLOADER.getCode());
            List<SignReAuth> authList = reAuthService.listByParam(tenantUser.getId(),authTypeList);
            if(authList != null && authList.size() > 0){
                downloadAuthStatus = true ;
//                break;
//                for(SignReAuth reAuth : authList){
//                    if(reAuth.getUserId() != null && reAuth.getUserId().equals(tenantUser.getId())){
//                        downloadAuthStatus = true ;
//                        break;
//                    }
//                }
            }
            if(signRu.getSysUserId() != null && tenantUser.getId().equals(signRu.getSysUserId())){
                downloadAuthStatus = true ;
            }
            if(!downloadAuthStatus){
                if(signRe.getDownloaderType() != null && (signRe.getDownloaderType() == SignDownloaderTypeEnum.PARTICIPANTS.getCode() || signRe.getDownloaderType() == SignDownloaderTypeEnum.ALL.getCode())){
                    for(SignRuSigner ruSigner : signerList){
                        if(ruSigner.getSignerType() != null && ruSigner.getSignerType().equals(SignerTypeEnum.SENDER.getCode())){
                            List<SignRuSender> ruSenderList = ruSenderService.listBySignerId(ruSigner.getId());
                            if(ruSenderList != null && ruSenderList.size() > 0){
                                for(SignRuSender sender : ruSenderList){
                                    if(sender.getSenderUserId() != null && sender.getSenderUserId().equals(tenantUser.getId())){
                                        downloadAuthStatus = true ;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

            }

        }else {
            if(signRe.getDownloaderType() != null && (signRe.getDownloaderType() == SignDownloaderTypeEnum.PARTICIPANTS.getCode() || signRe.getDownloaderType() == SignDownloaderTypeEnum.ALL.getCode()) ){
                for(SignRuSigner ruSigner : signerList){
                    if(ruSigner.getSignerType() != null && ruSigner.getSignerType().equals(SignerTypeEnum.RECEIVER_PERSONAL.getCode())){
                        if(ruSigner.getSignerExternalValue() != null && ruSigner.getSignerExternalValue().equals(contact)){
                            downloadAuthStatus = true ;
                            break;
                        }
                    }else if(ruSigner.getSignerType() != null && ruSigner.getSignerType().equals(SignerTypeEnum.RECEIVER_ENT.getCode())){
                        if(ruSigner.getSignerExternalValue() != null && ruSigner.getSignerExternalValue().equals(contact)){
                            downloadAuthStatus = true ;
                            break;
                        }
                    }
                }
            }

        }
        if(!downloadAuthStatus){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,operator不是合同参与人或业务线中指定的签约文件下载人，无下载权限");
        }

        List<SignRuDoc> docList = ruDocService.listByRuId(signRu.getId());
        if(docList == null || docList.size() == 0){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签约文件数据不存在");
        }
        List<String> ruDocIdList = docList.stream().map(SignRuDoc::getId).collect(Collectors.toList());
        if(ruDocIdList == null || ruDocIdList.size() == 0){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签约文件数据不存在");
        }

        List<SignRuDocOperate> ruDocOperateList = ruDocOperateService.listByParamCurrent(signRu.getId(), ruDocIdList);
        if(ruDocOperateList == null || ruDocOperateList.size() == 0){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签约文件数据不存在");
        }
        if(ruDocOperateList.size() != ruDocIdList.size()){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签约文件数据不存在");
        }
        List<String> annexIdList = ruDocOperateList.stream().map(SignRuDocOperate::getAnnexId).collect(Collectors.toList());
        if(annexIdList == null || annexIdList.size() ==0){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签约文件数据不存在");
        }

        String fileName = null ;
        File zipFile = null ;
        byte[] downloadByte = null ;
        OSS ossClient = null ;
        if(annexIdList.size() > 1){
            //批量打包下载
            // 压缩流
            ZipOutputStream zos = null;
            String zipFileName = signRu.getSubject() + System.currentTimeMillis();
            try {
                ossClient = storageService.getOSSClient();
                zipFile = File.createTempFile(zipFileName,".zip");
                FileOutputStream fos = new FileOutputStream(zipFile);
//                zos = new ZipOutputStream(fos, Charset.forName("GBK"));
                zos = new ZipOutputStream(fos, Charset.forName("UTF-8"));
                for(String annexId : annexIdList){
                    AnnexStorage annex = signFileService.getAnnexById(annexId);
                    if(annex == null){
                        throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签约文件数据不存在");
                    }
                    //阿里云下载
                    InputStream inputStream = storageService.loadAsInputStream(annex.getPath(), ossClient);
                    if(inputStream == null){
                        throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签约文件数据不存在");
                    }
                    byte[] bytes = org.apache.commons.io.IOUtils.toByteArray(inputStream);
                    byte[] signDownloadFile = ruBusinessService.signDownloadFile(bytes, signRu.getStatus());
                    zos.putNextEntry(new ZipEntry(annex.getRealName()));
                    zos.write(signDownloadFile);
                    inputStream.close();
                    // 当前文件写完，写入下一个文件
                    zos.closeEntry();
                }
                fileName = zipFile.getName();
                zos.close();
                fos.close();
                downloadByte = org.apache.commons.io.IOUtils.toByteArray(new FileInputStream(zipFile));

            } catch (Exception e) {
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,e.getMessage());
            }finally {
                if (ossClient != null) {
                    ossClient.shutdown();
                }
            }
        }else {
            //单独下载
            try {
                ossClient = storageService.getOSSClient();
                String annexId = annexIdList.get(0);
                AnnexStorage annex = signFileService.getAnnexById(annexId);
                if(annex == null){
                    throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签约文件数据不存在");
                }
                fileName = annex.getRealName() ;
                //阿里云下载
                InputStream inputStream = storageService.loadAsInputStream(annex.getPath(), ossClient);
                if(inputStream == null){
                    throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败,签约文件数据不存在");
                }
                byte[] bytes = IOUtils.toByteArray(inputStream);
                byte[] signDownloadFile = ruBusinessService.signDownloadFile(bytes, signRu.getStatus());
                downloadByte = signDownloadFile ;

            }catch (Exception e){
                throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,e.getMessage());
            }finally {
                if (ossClient != null) {
                    ossClient.shutdown();
                }
            }


        }

        OutputStream outputStream = null;
        try {
            String fileNameencode = URLEncoder.encode(fileName, "UTF-8");
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileNameencode);
            response.setHeader("fileName", fileNameencode);
            response.setHeader("Access-Control-Expose-Headers", "fileName");

            outputStream = response.getOutputStream();
//            IOUtils.copy(downloadInputStream, outputStream);
            outputStream.write(downloadByte);
            outputStream.flush();
            if (zipFile != null && zipFile.exists() && zipFile.isFile() && fileName.endsWith(".zip")) {
                // 将生成的服务器端文件删除
                zipFile.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        return ApiCommonRes.ok();
    }

//    // @ApiOperation(value = "下载合同附件", notes = "下载合同附件")
//    @GetMapping("/contract/annex/download")
//    public ApiCommonRes<?> contractAnnexDownload(ContractAnnexDownloadRequest request) {
//
//        return ApiCommonRes.ok();
//    }


//    public static void main(String[] args) {
//        try {
//            FileInputStream fis = new FileInputStream(new File("/Users/gongfenglai/Desktop/test/pdf/testContract01.pdf"));
//            byte[] bytes = IOUtils.toByteArray(fis);
//            String encode = Base64.encode(bytes);
//            System.out.println(encode);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

}