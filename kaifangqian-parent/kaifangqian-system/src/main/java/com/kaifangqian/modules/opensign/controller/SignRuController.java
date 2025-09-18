/**
 * @description 业务线配置管理，业务线运行实例
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
package com.kaifangqian.modules.opensign.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaifangqian.config.CommonConstants;
import com.kaifangqian.modules.api.entity.ApiCallbackPageUrl;
import com.kaifangqian.modules.api.service.ApiCallbackPageUrlService;
import com.kaifangqian.modules.cert.enums.CertHolderTypeEnum;
import com.kaifangqian.modules.cert.enums.CertTypeEnum;
import com.kaifangqian.modules.cert.pojo.TenantCertInfo;
import com.kaifangqian.modules.cert.service.CertBusinessService;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.service.business.*;
import com.kaifangqian.modules.opensign.service.ru.*;
import com.kaifangqian.modules.opensign.vo.base.sign.*;
import com.kaifangqian.modules.opensign.vo.request.ru.*;
import com.kaifangqian.modules.opensign.vo.response.ru.*;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.enums.TenantAuthStatus;
import com.kaifangqian.modules.system.enums.TenantStatus;
import com.kaifangqian.modules.system.enums.TenantType;
import com.kaifangqian.modules.system.service.*;
import com.kaifangqian.modules.system.vo.TenantInfoDTO;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.base.entity.BaseEntity;
import com.kaifangqian.common.constant.CommonConstant;
import com.kaifangqian.common.redis.base.BaseMap;
import com.kaifangqian.common.redis.client.RedisMQClient;
import com.kaifangqian.common.redis.util.RedisUtil;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.DesensitizationUtils;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.util.oConvertUtils;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.config.FileProperties;
import com.kaifangqian.config.limit.annotation.Limit;
import com.kaifangqian.config.limit.annotation.LimitHandleType;
import com.kaifangqian.config.limit.annotation.LimitType;
import com.kaifangqian.config.limit.annotation.OperateType;
import com.kaifangqian.dto.EmailDto;
import com.kaifangqian.eunms.SendType;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.external.sign.request.SignOrderRequest;
import com.kaifangqian.external.sign.response.SignOrderServiceInfoResponse;
import com.kaifangqian.external.sign.response.SignOrderStatusInfoResponse;
import com.kaifangqian.external.sign.service.SignServiceExternal;
import com.kaifangqian.external.enums.SignOrdeStatusResponseTypeEnum;
import com.kaifangqian.modules.opensign.dto.SignTaskInfo;
import com.kaifangqian.modules.opensign.dto.SignTaskThreadlocalVO;
import com.kaifangqian.modules.opensign.dto.SignaturePicVO;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.pdfbox.PdfboxService;
import com.kaifangqian.modules.opensign.service.business.*;
import com.kaifangqian.modules.opensign.service.business.vo.CompletedSignVo;
import com.kaifangqian.modules.opensign.service.business.vo.ControlQueryVo;
import com.kaifangqian.modules.opensign.service.confirm.ISignUserConfirmService;
import com.kaifangqian.modules.opensign.service.confirm.IUserConfirmService;
import com.kaifangqian.modules.opensign.service.flow.IFlowService;
import com.kaifangqian.modules.opensign.service.flow.IInstanceTaskService;
import com.kaifangqian.modules.opensign.service.ru.*;
import com.kaifangqian.modules.opensign.service.seal.SignEntSealService;
import com.kaifangqian.modules.opensign.service.tool.SignFileService;
import com.kaifangqian.modules.opensign.vo.base.sign.*;
import com.kaifangqian.modules.opensign.vo.request.ru.*;
import com.kaifangqian.modules.opensign.vo.response.EntSealAuthorizedResponse;
import com.kaifangqian.modules.opensign.vo.response.EntSealListResponse;
import com.kaifangqian.modules.opensign.vo.response.RunCertificateVerifyResponse;
import com.kaifangqian.modules.opensign.vo.response.SignerIdentifyResponse;
import com.kaifangqian.modules.opensign.vo.response.ru.*;
import com.kaifangqian.modules.storage.entity.AnnexStorage;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.service.*;
import com.kaifangqian.utils.IPUtil;
import com.kaifangqian.utils.MD5Util;
import com.kaifangqian.utils.MyStringUtils;
import com.kaifangqian.utils.SysMessageUtil;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Description: SignReController
 * @Package: com.kaifangqian.modules.opensign.controller
 * @ClassName: SignReController
 * @author: FengLai_Gong
 * @Date: 2023/12/05 14:07
 */
@Slf4j
@RestController
@RequestMapping("/sign/ru")
@ResrunLogModule(name = "业务线运行实例")
// @Api(tags = "业务线运行实例")
public class SignRuController {

    @Autowired
    private FileProperties fileProperties;
    @Autowired
    private PdfboxService pdfboxService;

    @Autowired
    private SignRuService ruService;

    @Autowired
    private RuCreateService ruCreateService;

    @Autowired
    private RuBusinessService ruBusinessService;
    @Autowired
    private RuSaveService ruSaveService;
    @Autowired
    private SignRuDocService ruDocService;
    @Autowired
    private SignRuDocOperateService ruDocOperateService;
    @Autowired
    private RuSignService ruSignService;
    @Autowired
    private ReBusinessService reBusinessService;
    @Autowired
    private SignRuDocControlService ruDocControlService;
    @Autowired
    private IFlowService iFlowService;

    @Autowired
    private SignEntSealService entSealService;

    @Autowired
    private AuthBusinessService authBusinessService;

    @Autowired
    private SignRuOperatorService ruOperatorService;

    @Autowired
    private SignRuSignerService ruSignerService;
    @Autowired
    private SignRuSenderService ruSenderService;

    @Autowired
    private SignFileService signFileService;

    @Autowired
    private SignRuTaskService ruTaskService;

    @Autowired
    private ISysTenantInfoService tenantInfoService;
    @Autowired
    private ISysTenantUserService tenantUserService;

    @Autowired
    private ITenantInfoExtendService tenantInfoExtendService;

    @Autowired
    private CertBusinessService certBusinessService;
    @Autowired
    private ITenantCertificateService tenantCertificateService;
    @Autowired
    private CertificateInfoDao certificateInfoDao;
    @Autowired
    private ISysUserDepartService userDepartService;
    @Autowired
    private RedisUtil redisUtil;
    @Resource
    private RedisMQClient redisMQClient;
    @Autowired
    private IInstanceTaskService instanceTaskService;

    @Autowired
    private SignRuDocControlPropertyService ruDocControlPropertyService;

    @Autowired
    private SignRuSignTemporaryService ruSignTemporaryService;

    @Autowired
    private IUserConfirmService userConfirmService;
    @Autowired
    private ISignUserConfirmService signUserConfirmService;


    @Autowired
    private RuCallbackService ruCallbackService;
    @Autowired
    private ApiCallbackPageUrlService apiCallbackPageUrlService;

    @Autowired
    private ISysUserService userService;
    @Autowired
    private SignRuOperateRecordService ruOperateRecordService;
    @Autowired
    private SysMessageUtil sysMessageUtil;

    @Autowired
    private SignServiceExternal signServiceExternal;
    @Autowired
    private ISysAppInfoService sysAppInfoService;

    // @ApiOperation("业务线实例-清空回收站")
    @RequestMapping(value = "/cleanup", method = RequestMethod.POST)
    public Result<?> cleanup() {
        SignRu ru = new SignRu();
        ru.setStatus(SignRuStatusEnum.CLEAN_UP.getCode());
        ru.setDeleteFlag(true);
        QueryWrapper<SignRu> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignRu::getStatus,SignRuStatusEnum.DELETE.getCode());
        boolean update = ruService.update(ru, wrapper);
        if(update){
            return Result.OK();
        }else {
            return Result.error("操作失败") ;
        }
    }

    // @ApiOperation("业务线实例-彻底删除")
    @RequestMapping(value = "/completely/delete", method = RequestMethod.POST)
    public Result<?> completelyDelete(@RequestBody CompletelyDeleteRequest request) {
        if(request == null || request.getSignRuId() == null){
            return Result.error("参数缺失", null);
        }
        SignRu ru = new SignRu();
        ru.setId(request.getSignRuId());
        ru.setStatus(SignRuStatusEnum.CLEAN_UP.getCode());
        ru.setDeleteFlag(true);
        boolean update = ruService.updateById(ru);
        if(update){
            return Result.OK();
        }else {
            return Result.error("操作失败") ;
        }
    }


    // @ApiOperation("业务线实例-操作记录列表")
    @RequestMapping(value = "/list/operate/record", method = RequestMethod.GET)
    public Result<List<RuOperateRecordResponse>> listOperateRecord(@RequestParam("signRuId") String signRuId) {

        if (signRuId == null || signRuId.length() == 0) {
            return Result.error("参数缺失", null);
        }
        List<RuOperateRecordResponse> responseList = new ArrayList<>();

        QueryWrapper<SignRuOperateRecord> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignRuOperateRecord::getSignRuId, signRuId);
        wrapper.lambda().orderByAsc(SignRuOperateRecord::getOperateTime);

        List<SignRuOperateRecord> list = ruOperateRecordService.list(wrapper);
        if (list != null && list.size() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            for (SignRuOperateRecord record : list) {
                RuOperateRecordResponse response = new RuOperateRecordResponse();

                SysTenantInfo tenantInfo = tenantInfoService.getById(record.getTenantId());
                if (tenantInfo == null) {
                    continue;
                }
                if (tenantInfo.getTenantType() != null && tenantInfo.getTenantType() == TenantType.GROUP.getType()) {
                    if (record.getTenantUserId() != null) {
                        SysTenantUser tenantUser = tenantUserService.getById(record.getTenantUserId());
                        if (tenantUser != null) {
                            response.setOperatorName(tenantUser.getNickName() + "(" + tenantInfo.getTenantName() + ")");
                        }
                    } else {
                        response.setOperatorName(tenantInfo.getTenantName());
                    }
                } else {
                    SysTenantUser tenantUser = tenantUserService.getById(record.getTenantUserId());
                    if (tenantUser != null) {
                        response.setOperatorName(tenantUser.getNickName());
                    }
                }
                SignRecordOperateTypeEnum operateTypeEnum = SignRecordOperateTypeEnum.getByType(record.getOperateType());
                if (operateTypeEnum != null) {
                    response.setOperateType(operateTypeEnum.getName());
                }
                SignRecordActionTypeEnum actionTypeEnum = SignRecordActionTypeEnum.getByType(record.getActionType());
                if (actionTypeEnum != null) {
                    response.setActionType(actionTypeEnum.getName());
                }
                if (record.getOperateTime() != null) {
                    response.setOperateTime(sdf.format(record.getOperateTime()));
                }
                if (record.getConfirmOrderNo() != null) {
                    SignUserConfirm signUserConfirm = signUserConfirmService.getById(record.getConfirmOrderNo());
                    if (signUserConfirm != null && signUserConfirm.getConfirmType() != null) {
                        ConfirmTypeEnum confirmTypeEnum = ConfirmTypeEnum.getByType(signUserConfirm.getConfirmType());
                        if (confirmTypeEnum != null) {
                            response.setOperateNotes(confirmTypeEnum.getName());
                        }
                    }
                }
                if(record.getOperateReason() != null && record.getOperateReason().length() > 0){
                    if(response.getOperateNotes() != null && response.getOperateNotes().length() > 0){
                        String operateNotes = response.getOperateNotes();
                        response.setOperateNotes(operateNotes + ",原因：" + record.getOperateReason());
                    }else {
                        response.setOperateNotes("原因：" + record.getOperateReason());
                    }
                }
                responseList.add(response);
            }
        }

        return Result.OK(responseList);
    }

    // @ApiOperation("业务线实例-获取待办任务链接")
    @RequestMapping(value = "/copy/link", method = RequestMethod.GET)
    public Result<?> copyLink(@RequestParam("signerId") String signerId) {

        if (signerId == null || signerId.length() == 0) {
            return Result.error("参数缺失");
        }

        RuCopyLinkResponse response = new RuCopyLinkResponse();

        QueryWrapper<SignRuTask> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignRuTask::getUserTaskId, signerId);
        wrapper.lambda().eq(SignRuTask::getTaskStatus, 1);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag, false);
        List<SignRuTask> taskList = ruTaskService.list(wrapper);
        if (taskList == null || taskList.size() == 0) {
            return Result.error("数据不存在");
        }
        //任务id
        SignRuTask ruTask = taskList.get(0);
        if (ruTask == null) {
            return Result.OK(response);
        }
        response.setTaskId(ruTask.getId());
        if (ruTask.getUserType() == 1) {
            //发起方
            SysUser user = userService.getById(ruTask.getUserId());
            if (user == null) {
                return Result.OK(response);
            }
            if (user.getUsername() == null) {
                return Result.OK(response);
            }
            if (user.getUsername().contains("@")) {
                response.setEmail(user.getUsername());
            } else {
                response.setPhone(user.getUsername());
            }

        } else {
            //个人接收方或者企业接收方
            SignRuSigner signer = ruSignerService.getById(signerId);
            Integer externalType = null;
            String externalValue = null;
            if (signer == null) {
                SignRuSender sender = ruSenderService.getById(signerId);
                if (sender != null) {
                    externalType = sender.getSenderExternalType();
                    externalValue = sender.getSenderExternalValue();
                }
            } else {
                externalType = signer.getSignerExternalType();
                externalValue = signer.getSignerExternalValue();
            }
            if (externalType != null && externalValue != null) {
                if (externalType == SignRuSignerExternalTypeEnum.PHONE.getCode()) {
                    response.setPhone(externalValue);
                } else if (externalType == SignRuSignerExternalTypeEnum.EMAIL.getCode()) {
                    response.setEmail(externalValue);
                }
            }
        }


        return Result.OK(response);
    }


    // @ApiOperation("业务线实例-获取待办任务链接-回调地址")
    @RequestMapping(value = "/get/callbackPageUrl", method = RequestMethod.GET)
    public Result<?> getCallbackPageUrl(@RequestParam("callbackPageNo") String callbackPageNo) {
        ApiCallbackPageUrl apiCallbackPageUrl = apiCallbackPageUrlService.getById(callbackPageNo);
        if (apiCallbackPageUrl != null && apiCallbackPageUrl.getCallbackPageUrl() != null && apiCallbackPageUrl.getCallbackPageUrl().length() > 0) {
            return Result.OK("", apiCallbackPageUrl.getCallbackPageUrl());
        }
        return Result.OK("数据不存在", "数据不存在");
    }


    // @ApiOperation("业务线实例-企业签章-被授权人列表")
    @GetMapping("/entSeal/authorized/list")
    @ResponseBody
    public Result<?> authorizedList(@RequestParam("businessRelationId") String businessRelationId) {
        List<EntSealAuthorizedResponse> responseList = new ArrayList<>();
        //获取签章业务权限
        //必须包含是印章使用者
        List<Integer> businessTypeRoleList = new ArrayList<>();
        businessTypeRoleList.add(BusinessAuthTypeRole.SEAL_USER.getCode());
        //查询相关被授权人
        List<SysTenantUser> tenantUserList = authBusinessService.getTenantUserByBusinessRelationId(businessRelationId, businessTypeRoleList, BusinessAuthType.SEAL.getCode());

        if (tenantUserList != null && tenantUserList.size() > 0) {
            for (SysTenantUser tenantUser : tenantUserList) {
                EntSealAuthorizedResponse response = new EntSealAuthorizedResponse();
                response.setTenantId(tenantUser.getTenantId());
                response.setNickName(tenantUser.getNickName());
                response.setUserId(tenantUser.getUserId());
                response.setTenantUserId(tenantUser.getId());
                response.setId(tenantUser.getId());
                responseList.add(response);
            }

        }
        return Result.OK(responseList);
    }

    // @ApiOperation("业务线实例-被授权人-企业签章列表")
    @RequestMapping(value = "/entSeal/enabled/list/authorized", method = RequestMethod.GET)
    public Result<List<EntSealListResponse>> enabledListAuthorized() {


        List<EntSealListResponse> responseList = new ArrayList<>();

        //构建查询条件
        //必须包含是印章使用者
        List<Integer> businessTypeRoleList = new ArrayList<>();
        businessTypeRoleList.add(BusinessAuthTypeRole.SEAL_USER.getCode());
        List<String> businessIdList = authBusinessService.getBusinessIdList(BusinessAuthType.SEAL, businessTypeRoleList);
        if (businessIdList == null || businessIdList.size() == 0) {
            return Result.OK(responseList);
        }
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        QueryWrapper<SignEntSeal> wrapper = new QueryWrapper<>();
        wrapper.lambda().orderByDesc(BaseEntity::getCreateTime);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag, false);
        //租户隔离，租户下所有签章
        wrapper.lambda().eq(SignEntSeal::getSysTenantId, currentUser.getTenantId());
        wrapper.lambda().eq(SignEntSeal::getSealStatus, EntSealStatusEnum.ENABLED.getCode());
        wrapper.lambda().eq(SignEntSeal::getStatus, SealStatusEnum.ENABLE.getCode());
        //数据权限
        wrapper.lambda().in(SignEntSeal::getId, businessIdList);

        List<SignEntSeal> list = entSealService.list(wrapper);
        if (list == null || list.size() == 0) {
            return Result.OK(responseList);
        }
        for (SignEntSeal entSeal : list) {
            EntSealListResponse response = new EntSealListResponse();
            response.setSealId(entSeal.getId());
            response.setSealName(entSeal.getSealName());
            response.setSealStatus(entSeal.getSealStatus());
            AnnexStorage annexStorage = signFileService.findByFatherIdAndDataCategory(SignFileEnum.SEAL_FILE_ENT, entSeal.getId());
            if (annexStorage != null) {
                response.setAnnexId(annexStorage.getId());
            }
            response.setStatus(entSeal.getStatus());
            responseList.add(response);
        }
        return Result.OK(responseList);
    }

    // @ApiOperation("业务线实例-企业签章-列表")
    @RequestMapping(value = "/entSeal/enabled/list", method = RequestMethod.GET)
    public Result<List<EntSealListResponse>> enabledList() {


        List<EntSealListResponse> responseList = new ArrayList<>();

        //构建查询条件
        //必须包含是印章使用者
//        List<Integer> businessTypeRoleList = new ArrayList<>();
//        businessTypeRoleList.add(BusinessAuthTypeRole.USER.getCode());
//        List<String> businessIdList = authBusinessService.getBusinessIdList(BusinessAuthType.SEAL,businessTypeRoleList);
//        if(businessIdList == null || businessIdList.size() == 0){
//            return Result.OK(responseList);
//        }
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        QueryWrapper<SignEntSeal> wrapper = new QueryWrapper<>();
        wrapper.lambda().orderByDesc(BaseEntity::getCreateTime);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag, false);
        //租户隔离，租户下所有签章
        wrapper.lambda().eq(SignEntSeal::getSysTenantId, currentUser.getTenantId());
        wrapper.lambda().eq(SignEntSeal::getSealStatus, EntSealStatusEnum.ENABLED.getCode());
        wrapper.lambda().eq(SignEntSeal::getStatus, SealStatusEnum.ENABLE.getCode());
        //数据权限
//        wrapper.lambda().in(SignEntSeal::getId,businessIdList);

        List<SignEntSeal> list = entSealService.list(wrapper);
        if (list == null || list.size() == 0) {
            return Result.OK(responseList);
        }
        for (SignEntSeal entSeal : list) {
            EntSealListResponse response = new EntSealListResponse();
            response.setSealId(entSeal.getId());
            response.setSealName(entSeal.getSealName());
            response.setSealStatus(entSeal.getSealStatus());
            AnnexStorage annexStorage = signFileService.findByFatherIdAndDataCategory(SignFileEnum.SEAL_FILE_ENT, entSeal.getId());
            if (annexStorage != null) {
                response.setAnnexId(annexStorage.getId());
            }
            response.setStatus(entSeal.getStatus());
            responseList.add(response);
        }
        return Result.OK(responseList);
    }


    // @ApiOperation("业务线实例-全部数据详情")
    @RequestMapping(value = "/info/base", method = RequestMethod.GET)
    public Result<BaseVo> infoBase(@RequestParam("signRuId") String signRuId) {
        BaseVo info = ruBusinessService.info(signRuId);
        if (info == null) {
            return Result.error("数据不存在", null);
        }
        return Result.OK(info);
    }

    // @ApiOperation("业务线实例-短信链接数据详情")
    @RequestMapping(value = "/info/link", method = RequestMethod.GET)
    public Result<InfoLinkResponse> infoLink(@RequestParam("signRuId") String signRuId, @RequestParam(value = "signTaskId", required = false) String signTaskId) {
        SignRu signRu = ruService.getById(signRuId);
        if (signRu == null) {
            return Result.error("数据不存在", null);
        }

        SignRuTask signRuTask = ruTaskService.getById(signTaskId);

        List<SignRuDoc> docList = ruDocService.listByRuId(signRuId);

        InfoLinkResponse response = new InfoLinkResponse();

        // 设置任务签署状态
        if (signRuTask != null){
            response.setTaskStatus(signRuTask.getTaskStatus());
            response.setCheckMenuType(signRuTask.getCheckMenuType());
        }
        // 获取签署文件名称
        List<String> signFileNames = new ArrayList<>();
        if(CollUtil.isNotEmpty(docList)){
            for (SignRuDoc doc : docList){
                signFileNames.add(doc.getDocName());
            }
        }
        response.setSignFileNames(signFileNames);
        response.setFileSum(docList.size());

        response.setSubject(signRu.getSubject());
        response.setDocNo(signRu.getCode());
        response.setExpireDate(signRu.getExpireDate());
        response.setSignStatus(signRu.getStatus());

        List<SignRuSigner> signerList = ruSignerService.listByRuId(signRuId);

        List<SignRuSignerResponse> signers = new ArrayList<>();
        if (signerList != null && signerList.size() > 0) {
            String signerName = "";
            for (int i = 0; i < signerList.size(); i++) {
                SignRuSigner signer = signerList.get(i);
                signers.add(new SignRuSignerResponse(signer.getSignerType(), signer.getSignerName()));

            }
            SysTenantInfo tenantInfo = sysTenantInfoService.getById(signRu.getSysTenantId());
            if (tenantInfo != null) {
                response.setSenderName(tenantInfo.getTenantName());
            }
            response.setSignerName(signerName);
            response.setSigners(signers);
        }
        return Result.OK(response);
    }

    @Autowired
    private ISysTenantInfoService sysTenantInfoService;


    // @ApiOperation("业务线实例-签署人数据详情（发起人和接收人）")
    @RequestMapping(value = "/list/signer", method = RequestMethod.GET)
    public Result<List<DocSignerVo>> listSigner(@RequestParam("signRuId") String signRuId) {
        List<DocSignerVo> signerList = ruBusinessService.getSignerList(signRuId);
        return Result.OK(signerList);
    }

    // @ApiOperation("业务线实例-抄送人数据详情")
    @RequestMapping(value = "/list/ccer", method = RequestMethod.GET)
    public Result<List<DocCcerVo>> listCcer(@RequestParam("signRuId") String signRuId) {
        List<DocCcerVo> ccerList = ruBusinessService.getCcerList(signRuId);
        return Result.OK(ccerList);
    }


    // @ApiOperation("业务线实例-签约文件及图片详情")
    @RequestMapping(value = "/list/file", method = RequestMethod.GET)
    public Result<List<DocFileVo>> listFile(@RequestParam("signRuId") String signRuId) {
        List<DocFileVo> docList = ruBusinessService.getDocList(signRuId);
        return Result.OK(docList);
    }

    // @ApiOperation("业务线实例-图片列表，根据签约文件id获取")
    @RequestMapping(value = "/list/image", method = RequestMethod.GET)
    public Result<List<DocImageVo>> listImage(@RequestParam("signFileId") String signFileId) {
        List<DocImageVo> imageList = ruBusinessService.getImageList(signFileId);
        return Result.OK(imageList);
    }


    //添加填写、签署、填写和签署等状态
    // @ApiOperation("业务线实例-操作人数据详情")
    @RequestMapping(value = "/list/operator", method = RequestMethod.GET)
    public Result<List<DocOperatorVo>> listOperator(@RequestParam("signRuId") String signRuId) {
        List<DocOperatorVo> operatorList = ruBusinessService.getOperatorList(signRuId);
        return Result.OK(operatorList);
    }

    // @ApiOperation("业务线实例-签署人数据操作状态详情（发起人和接收人）")
    @RequestMapping(value = "/list/operator/status", method = RequestMethod.GET)
    public Result<List<DocSignerVo>> listOperatorStatus(@RequestParam("signRuId") String signRuId) {

        List<DocSignerVo> signerList = ruBusinessService.getSignerList(signRuId);

        return Result.OK(signerList);
    }

    // @ApiOperation("业务线实例-签署人数据,签署顺序、操作状态详情（发起人和接收人）")
    @RequestMapping(value = "/schedule", method = RequestMethod.GET)
    public Result<SignRuScheduleResponse> schedule(@RequestParam("signRuId") String signRuId) {

        if (signRuId == null || signRuId.length() == 0) {
            return Result.error("参数缺失", null);
        }
        SignRu ru = ruService.getById(signRuId);
        if (ru == null) {
            return Result.error("数据不存在", null);
        }
        SignRuScheduleResponse response = new SignRuScheduleResponse();
        response.setSignOrderType(ru.getSignOrderType());
        List<DocSignerVo> signerList = ruBusinessService.getSignerList(signRuId);
        if(signerList == null){
            signerList = new ArrayList<>();
        }
        response.setSignerVoList(signerList);
        return Result.OK(response);
    }

    // @ApiOperation("业务线实例-签署人数据操作状态详情（发起人和接收人）")
    @RequestMapping(value = "/info/operator/status", method = RequestMethod.GET)
    public Result<InfoOperatorStatusResponse> infoOperatorStatus(@RequestParam("signRuId") String signRuId) {
        SignTaskThreadlocalVO threadlocalVO = SignTaskInfo.THREAD_LOCAL.get();
        if (signRuId == null || signRuId.length() == 0) {
            if (threadlocalVO.getSignRuId() == null || threadlocalVO.getSignRuId().length() == 0) {
                return Result.error("数据不存在", null);
            }
            signRuId = threadlocalVO.getSignRuId();
        }
        SignRu ru = ruService.getById(signRuId);
        if (ru == null) {
            return Result.error("数据不存在", null);
        }
        String taskId = threadlocalVO.getTaskId();
        InfoOperatorStatusResponse response = new InfoOperatorStatusResponse();
        response.setCheckStatus(instanceTaskService.checkAuth(taskId));

        response.setRuStatus(ru.getStatus());
        SignRuTask ruTask = ruTaskService.getById(taskId);
        if (ruTask != null) {
            response.setOperatorStatus(ruTask.getTaskStatus());
        }
        return Result.OK(response);
    }

    // @ApiOperation("业务线实例-详情查看权限")
    @RequestMapping(value = "/info/view/check", method = RequestMethod.GET)
    public Result<Boolean> infoCheck(@RequestParam("signRuId") String signRuId) {
        return Result.OK(ruService.checkViewAuth(signRuId));
    }

    // @ApiOperation("业务线实例-签署人数据操作状态详情（发起人和接收人）")
    @RequestMapping(value = "/info/operator/check", method = RequestMethod.GET)
    public Result<CheckStatusResponse> infoCheckStatus() {
        SignTaskThreadlocalVO threadlocalVO = SignTaskInfo.THREAD_LOCAL.get();
        CheckStatusResponse response = new CheckStatusResponse();
        String taskId = threadlocalVO.getTaskId();
        Integer checkStatus = instanceTaskService.checkAuth(taskId);
        response.setCheckStatus(checkStatus);
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        if (MyStringUtils.isNotBlank(loginUser.getUsername())) {
            if (loginUser.getUsername().contains("@")) {
                response.setCurrentUsername(DesensitizationUtils.getEmail(loginUser.getUsername()));
            } else {
                response.setCurrentUsername(DesensitizationUtils.getPhone(loginUser.getUsername()));
            }
        }
        SignRuTask signRuTask = ruTaskService.getById(taskId);
        if (signRuTask != null) {
            response.setTargetTenantName(signRuTask.getTenantName());
            if (MyStringUtils.isNotBlank(signRuTask.getPhone())) {
                response.setTargetUserPhone(DesensitizationUtils.getPhone(signRuTask.getPhone()));
            } else if (MyStringUtils.isNotBlank(signRuTask.getEmail())) {
                response.setTargetUserEmail(DesensitizationUtils.getEmail(signRuTask.getEmail()));
            }
        }

        return Result.OK(response);
    }

    // @ApiOperation("业务线实例-控件数据详情")
    @RequestMapping(value = "/list/control", method = RequestMethod.GET)
    public Result<ControlDataVo> listControl(DocControlListVo request) {
        if (request.getSignRuId() == null || request.getSignRuId().length() == 0) {
            return Result.error("参数缺失", null);
        }
        ControlDataVo controlDataVo = new ControlDataVo();
        SignRu signRu = ruService.getById(request.getSignRuId());
        if (signRu == null) {
            return Result.error("数据不存在", null);
        }
        if (signRu.getControlChangeFlag() == null) {
            controlDataVo.setControlChangeFlag(ControlChangeFlagEnum.NECESSARY_AND_ADD.getType());
        } else {
            controlDataVo.setControlChangeFlag(signRu.getControlChangeFlag());
        }

        List<DocControlVo> controlVoList = new ArrayList<>();
        //查询控件
        ControlQueryVo queryVo = new ControlQueryVo();
        queryVo.setSignRuId(request.getSignRuId());
        queryVo.setSignerId(request.getSignerId());
        List<SignRuDocControl> docControlList = ruDocControlService.listByParam(queryVo);
        if (docControlList == null || docControlList.size() == 0) {
            controlDataVo.setControlList(controlVoList);
            return Result.OK(controlDataVo);
        }
        //校验ruId列表
        List<String> ruIdList = docControlList.stream().map(SignRuDocControl::getSignRuId).distinct().collect(Collectors.toList());
        if (ruIdList == null || ruIdList.size() == 0 || ruIdList.size() > 1) {
            return Result.error("查询包含其他实例的控件", null);
        }
        //获取控件属性数据
        List<String> controlIdList = docControlList.stream().map(SignRuDocControl::getId).collect(Collectors.toList());
        List<SignRuDocControlProperty> propertyList = ruDocControlPropertyService.listByControlIdList(controlIdList);
        Map<String, List<SignRuDocControlProperty>> propertyMap = null;
        if (propertyList != null && propertyList.size() > 0) {
            propertyMap = propertyList.stream().collect(Collectors.groupingBy(SignRuDocControlProperty::getControlId));
        }

        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        String signerId = null;

        String ruId = ruIdList.get(0);
        SignRuTask query = new SignRuTask();
        query.setSignRuId(ruId);
//        query.setTaskType(TaskTypeEnum.WRITE_TASK.getCode());
        query.setTenantUserId(currentUser.getTenantUserId());
        List<SignRuTask> taskList = ruTaskService.getByEntity(query);
        if (taskList != null && taskList.size() > 0) {
            SignRuTask ruTask = taskList.get(0);
            signerId = ruTask.getUserTaskId();
        }

        if (signerId == null) {
            SignRuSigner signerQuery = new SignRuSigner();
            signerQuery.setSignRuId(ruId);
            signerQuery.setSignerUserId(currentUser.getTenantUserId());
            signerQuery.setSignerType(SignerTypeEnum.SENDER.getCode());
            List<SignRuSigner> signerList = ruSignerService.getByEntity(signerQuery);
            if (signerList != null && signerList.size() > 0) {
                SignRuSigner signer = signerList.get(0);
                signerId = signer.getId();
            }
        }

        //转换控件数据
        for (SignRuDocControl control : docControlList) {
            DocControlVo vo = new DocControlVo();
            BeanUtils.copyProperties(control, vo);
            if (signerId != null && control.getSignerId() != null && signerId.equals(control.getSignerId())) {
                vo.setIsMineFlag(1);
            } else {
                vo.setIsMineFlag(2);
            }
            if (propertyMap != null && propertyMap.containsKey(control.getId())) {
                List<SignRuDocControlProperty> properties = propertyMap.get(control.getId());
                if (properties != null && properties.size() > 0) {
                    List<ControlPropertyVo> propertyVoList = new ArrayList<>();
                    for (SignRuDocControlProperty property : properties) {
                        ControlPropertyVo propertyVo = new ControlPropertyVo();
                        BeanUtils.copyProperties(property, propertyVo);
                        propertyVoList.add(propertyVo);
                    }
                    vo.setPropertyVoList(propertyVoList);
                }
            }
            controlVoList.add(vo);
        }
        if (controlVoList != null && controlVoList.size() > 0) {
            for (DocControlVo controlVo : controlVoList) {
                if (controlVo.getControlOrder() == null) {
                    controlVo.setControlOrder(0);
                }
            }
            controlVoList = controlVoList.stream().sorted(Comparator.comparing(DocControlVo::getControlOrder)).collect(Collectors.toList());
        }
        controlDataVo.setControlList(controlVoList);


        return Result.OK(controlDataVo);
    }

    // @ApiOperation("业务线实例-审批数据详情")
    @RequestMapping(value = "/list/approve", method = RequestMethod.GET)
    public Result<List<DocApproveVo>> listApprove(@RequestParam("signRuId") String signRuId) {
        return null;
    }


    // @ApiOperation("业务线实例-发起前-更新实例签署截止时间")
    @RequestMapping(value = "/start/save/expireDate", method = RequestMethod.POST)
    public Result<?> startSaveExpireDate(@RequestBody StartSaveExpireDateRequest request) {
        if (request == null || request.getSignRuId() == null || request.getSignRuId().length() == 0 || request.getExpireDate() == null) {
            return Result.error("参数缺失");
        }
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        SignRu ru = ruService.getById(request.getSignRuId());
        if (ru == null) {
            return Result.error("实例不存在");
        }
        if (ru.getStatus().equals(SignRuStatusEnum.DRAFT.getCode()) || ru.getStatus().equals(SignRuStatusEnum.APPROVING.getCode()) || ru.getStatus().equals(SignRuStatusEnum.APPROVE_FAILED.getCode()) || ru.getStatus().equals(SignRuStatusEnum.DELETE.getCode())) {

            boolean b = ruService.updateExpireDate(request.getExpireDate(), ru.getId());
            if (!b) {
                return Result.error("操作失败");
            }
        } else if (ru.getStatus().equals(SignRuStatusEnum.WRITING.getCode()) || ru.getStatus().equals(SignRuStatusEnum.SIGNING.getCode())) {
            if (ru.getExpireDate() != null) {
                return Result.error("签署截止日期已设置，实例运行中不能修改");
            }
            boolean b = ruService.updateExpireDate(request.getExpireDate(), ru.getId());
            if (!b) {
                return Result.error("操作失败");
            }
        } else {
            return Result.error("实例已完成，不能修改");
        }
        //修改签署截止时间
        ruCallbackService.callback(ru.getId(), null, SignCallbackTypeEnum.DOCUMENT_DELAY);
        return Result.OK();
    }


    // @ApiOperation("业务线实例-发起前-保存基本数据")
    @RequestMapping(value = "/start/save/base", method = RequestMethod.POST)
    @Limit(name = "保存业务线实例", prefix = "limit",limitType= LimitType.TOKEN, operateType = OperateType.ALL, count = 5,period=10,limitHandle = LimitHandleType.NONE)
    public Result<?> startSaveBase(@RequestBody BaseVo request) {
        if (request == null || request.getBaseVo() == null) {
            return Result.error("参数缺失");
        }

        if (request.getSignerList() != null && request.getSignerList().size() > 0) {
            for (DocSignerVo docSignerVo : request.getSignerList()) {
                if (docSignerVo.getSignerType() == null) {
                    return Result.error("签署人类型-参数缺失");
                }
                if (docSignerVo.getSignerType().equals(SignerTypeEnum.SENDER.getCode())) {
                    if (docSignerVo.getSenderList() != null && docSignerVo.getSenderList().size() > 0) {
                        for (DocSenderVo senderVo : docSignerVo.getSenderList()) {
                            if (senderVo.getSenderSignType() == null) {
                                return Result.error("发起方类型-参数缺失");
                            }
                        }
                    }
                } else if (docSignerVo.getSignerType().equals(SignerTypeEnum.RECEIVER_ENT.getCode())) {
                    if (docSignerVo.getSenderList() != null && docSignerVo.getSenderList().size() > 0) {
                        for (DocSenderVo senderVo : docSignerVo.getSenderList()) {
                            if (senderVo.getSenderSignType() == null) {
                                return Result.error("企业接收方类型-参数缺失");
                            }
                        }
                    }
                }
            }
        }
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        if (currentUser == null || currentUser.getTenantUserId() == null || currentUser.getTenantUserId().length() == 0) {
            return Result.error("当前用户不存在");
        }
        String signRuId = request.getBaseVo().getSignRuId();
        if (signRuId != null && signRuId.length() > 0) {
            //如果reid为空字符串，则设置为null，不更新
            if (request.getBaseVo().getSignReId() != null && request.getBaseVo().getSignReId().length() == 0) {
                request.getBaseVo().setSignReId(null);
            }
            signRuId = ruSaveService.save(request);
        } else {
            signRuId = ruCreateService.create(request);
        }

        if (signRuId == null) {
            return Result.error("保存失败");
        }

        return Result.OK("保存成功", signRuId);
    }

    // @ApiOperation("业务线实例-发起前-保存签署位置及参数")
    @RequestMapping(value = "/start/save/sign", method = RequestMethod.POST)
    public Result<?> startSaveSign(@RequestBody StartSaveSignRequest request) {
        if (request == null || request.getSignRuId() == null) {
            return Result.error("参数缺失");
        }

        SignRu signRu = ruService.getById(request.getSignRuId());
        if (signRu == null) {
            return Result.error("数据不存在");
        }
        for (DocControlVo controlVo : request.getControlList()) {
            if (controlVo.getSignerId() == null || controlVo.getSignerId().length() == 0) {
                if (ControlTypeEnum.getWriteList().contains(controlVo.getControlType())) {
                    throw new PaasException("填写控件未全部指定签署人");
                } else if (ControlTypeEnum.getSignList().contains(controlVo.getControlType())) {
                    throw new PaasException("签署控件未全部指定签署人");
                }
            }

        }
        if (request.getControlChangeFlag() != null) {
            signRu.setControlChangeFlag(request.getControlChangeFlag());
            boolean updateRu = ruService.updateById(signRu);
            if (!updateRu) {
                throw new PaasException("控件变更状态更新失败");
            }
        } else {
            if (signRu.getControlChangeFlag() == null) {
                signRu.setControlChangeFlag(ControlChangeFlagEnum.NECESSARY_AND_ADD.getType());
                boolean updateRu = ruService.updateById(signRu);
                if (!updateRu) {
                    throw new PaasException("控件变更状态更新失败");
                }
            }
        }

        List<SignRuSigner> signerList = ruSignerService.listByRuId(request.getSignRuId());
        if (signerList == null || signerList.size() == 0) {
            throw new PaasException("未设置签署人");
        }
        //只需要校验基本参数即可
        if (request.getControlList() != null && request.getControlList().size() > 0) {
            String message = ruBusinessService.verifyControl(request.getControlList());
            if (message != null) {
                return Result.error(message);
            }
            //补充控件来源类型,发起时设置
            controlOriginTypeFill(request.getControlList(), ControlOriginTypeEnum.START);
            //补充控件签署人类型
            ruBusinessService.fillControlSignerType(request.getControlList(), request.getSignRuId());
        }
        for (DocControlVo controlVo : request.getControlList()) {
            if (controlVo.getSignerId() == null || controlVo.getSignerId().length() == 0) {
                if (ControlTypeEnum.getWriteList().contains(controlVo.getControlType())) {
                    throw new PaasException("填写控件未全部指定签署人");
                } else if (ControlTypeEnum.getSignList().contains(controlVo.getControlType())) {
                    throw new PaasException("签署控件未全部指定签署人");
                }
            }
        }
        //保存控件
        ruBusinessService.saveControlList(request.getControlList(), request.getDeleteIdList(), request.getSignRuId(), null);
        //自动签署控件校验
        List<SignRuDocControl> signRuDocControlList = ruDocControlService.listSignControlList(request.getSignRuId());
        Boolean checkAutoSignErrorStatus = ruBusinessService.checkAutoSignErrorStatus(signerList, signRuDocControlList);
        if (checkAutoSignErrorStatus) {
            return Result.error("自动盖章的签署节点未指定签署位置");
        }
        return Result.OK();
    }

    // @ApiOperation("业务线实例-发起前-保存文档填写参数")
    @RequestMapping(value = "/start/save/write", method = RequestMethod.POST)
    public Result<?> startSaveWrite(@RequestBody StartSaveWriteRequest request, HttpServletRequest httpServletRequest) {
        if (request == null || request.getSignRuId() == null) {
            return Result.error("参数缺失");
        }
        if ((request.getControlList() == null || request.getControlList().size() == 0) && (request.getDeleteIdList() == null || request.getDeleteIdList().size() == 0)) {
            return Result.error("参数缺失");
        }
        LoginUser currentUser = MySecurityUtils.getCurrentUser();

        SignRu signRu = ruService.getById(request.getSignRuId());
        if (signRu == null) {
            return Result.error("数据不存在");
        }
        if (request.getControlChangeFlag() != null) {
            signRu.setControlChangeFlag(request.getControlChangeFlag());
            boolean updateRu = ruService.updateById(signRu);
            if (!updateRu) {
                throw new PaasException("控件变更状态更新失败");
            }
        } else {
            if (signRu.getControlChangeFlag() == null) {
                signRu.setControlChangeFlag(ControlChangeFlagEnum.NECESSARY_AND_ADD.getType());
                boolean updateRu = ruService.updateById(signRu);
                if (!updateRu) {
                    throw new PaasException("控件变更状态更新失败");
                }
            }
        }

        List<SignRuSigner> signerList = ruSignerService.listByRuId(request.getSignRuId());
        if (signerList == null || signerList.size() == 0) {
            throw new PaasException("未设置签署人");
        }
        //只需要校验基本参数即可
        if (request.getControlList() != null && request.getControlList().size() > 0) {
            String message = ruBusinessService.verifyControl(request.getControlList());
            if (message != null) {
                return Result.error(message);
            }
            //补充控件签署人类型
            ruBusinessService.fillControlSignerType(request.getControlList(), request.getSignRuId());
        }
        //保存控件
        ruBusinessService.saveControlList(request.getControlList(), request.getDeleteIdList(), request.getSignRuId(), null);

        List<SignRuDocControl> signRuDocControlList = ruDocControlService.listSignControlList(request.getSignRuId());
        //自动签署控件校验
        Boolean checkAutoSignErrorStatus = ruBusinessService.checkAutoSignErrorStatus(signerList, signRuDocControlList);
        if (checkAutoSignErrorStatus) {
            return Result.error("自动盖章的签署节点未指定签署位置");
        }
        SignTaskThreadlocalVO threadlocalVO = SignTaskInfo.THREAD_LOCAL.get();
        if (threadlocalVO == null || threadlocalVO.getTaskId() == null || threadlocalVO.getTaskId().length() == 0) {
            //操作记录
            QueryWrapper<SignRuOperateRecord> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(SignRuOperateRecord::getSignRuId, signRu.getId());
            wrapper.lambda().eq(SignRuOperateRecord::getAccountId, currentUser.getId());
            wrapper.lambda().eq(SignRuOperateRecord::getTenantId, currentUser.getTenantId());
            wrapper.lambda().eq(BaseEntity::getDeleteFlag, false);
            List<SignRuOperateRecord> list = ruOperateRecordService.list(wrapper);
            if (list != null && list.size() > 0) {
                //操作记录
                SignRuOperateRecord ruOperateRecord = list.get(0);
                if (ruOperateRecord != null) {
                    ruOperateRecord.setSignRuId(signRu.getId());
                    ruOperateRecord.setAccountId(currentUser.getId());
                    ruOperateRecord.setTenantId(currentUser.getTenantId());
                    ruOperateRecord.setTenantUserId(currentUser.getTenantUserId());
                    ruOperateRecord.setOperateType(SignRecordOperateTypeEnum.WRITE.getType());
                    ruOperateRecord.setActionType(SignRecordActionTypeEnum.SUBMIT_WRITE.getType());
                    ruOperateRecord.setOperateTime(new Date());
                    ruOperateRecord.setIpAddr(IPUtil.getIpAddr(httpServletRequest));
//            ruOperateRecord.setTaskId(threadlocalVO.getTaskId());
//            ruOperateRecord.setConfirmOrderNo(request.getSignConfirmOrderNo());
                    ruOperateRecordService.updateById(ruOperateRecord);
                }
            } else {
                //操作记录
                SignRuOperateRecord ruOperateRecord = new SignRuOperateRecord();
                ruOperateRecord.setSignRuId(signRu.getId());
                ruOperateRecord.setAccountId(currentUser.getId());
                ruOperateRecord.setTenantId(currentUser.getTenantId());
                ruOperateRecord.setTenantUserId(currentUser.getTenantUserId());
                ruOperateRecord.setOperateType(SignRecordOperateTypeEnum.WRITE.getType());
                ruOperateRecord.setActionType(SignRecordActionTypeEnum.SUBMIT_WRITE.getType());
                ruOperateRecord.setOperateTime(new Date());
                ruOperateRecord.setIpAddr(IPUtil.getIpAddr(httpServletRequest));
//            ruOperateRecord.setTaskId(threadlocalVO.getTaskId());
//            ruOperateRecord.setConfirmOrderNo(request.getSignConfirmOrderNo());
                ruOperateRecordService.save(ruOperateRecord);
            }
        }


        return Result.OK();
    }


    // @ApiOperation("业务线实例-发起前-是否填写")
    @RequestMapping(value = "/start/is/write", method = RequestMethod.GET)
    public Result<?> startIsWrite(@RequestParam("signRuId") String signRuId) {

        //创建查询对象
        ControlQueryVo queryVo = new ControlQueryVo();
        //业务线实例id
        queryVo.setSignRuId(signRuId);
//        //签署方类型为发起方
//        queryVo.setSignerType(SignerTypeEnum.SENDER.getCode());
        //有填写控件
        queryVo.setControlTypeList(ControlTypeEnum.getWriteList());
        //查询控件
        Integer count = ruDocControlService.countByParam(queryVo);
        //转换控件数据
        if (count != null && count > 0) {
            return Result.OK(1);
        }
        return Result.OK(2);
    }


    // @ApiOperation("业务线实例-发起前-删除")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<?> delete(DeleteRequest request) {
        if (request == null || request.getSignRuId() == null || request.getSignRuId().length() == 0) {
            return Result.error("参数缺失");
        }
        SignRu ru = ruService.getById(request.getSignRuId());
        if (ru == null) {
            return Result.error("实例不存在");
        }
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        SignTaskThreadlocalVO threadlocalVO = SignTaskInfo.THREAD_LOCAL.get();
        if (!currentUser.getTenantUserId().equals(ru.getSysUserId())) {
            return Result.error("当前操作者非当前实例发起方，不能操作");
        }
        if (SignRuStatusEnum.DRAFT.getCode().equals(ru.getStatus()) || SignRuStatusEnum.APPROVE_FAILED.getCode().equals(ru.getStatus()) || SignRuStatusEnum.REVOKE.getCode().equals(ru.getStatus())) {
            ru.setStatus(SignRuStatusEnum.DELETE.getCode());
            ru.setDeleteFlag(true);
            boolean b = ruService.updateById(ru);
            if (!b) {
                return Result.error("操作失败");
            }
        } else {
            SignRuStatusEnum statusEnum = SignRuStatusEnum.getByCode(ru.getStatus());
            if (statusEnum == null) {
                return Result.error("实例异常，状态不存在");
            }
            return Result.error("实例状态为" + statusEnum.getName() + ",不可删除");
        }
        //删除回调
        ruCallbackService.callback(ru.getId(), threadlocalVO.getTaskId(), SignCallbackTypeEnum.DELETE);
        return Result.OK();
    }

    // @ApiOperation("业务线实例-发起前-撤回")
    @RequestMapping(value = "/revoke", method = RequestMethod.POST)
    public Result<?> revoke(@RequestBody RevokeRequest request, HttpServletRequest httpServletRequest) {

        if (request == null || request.getSignRuId() == null || request.getSignRuId().length() == 0) {
            return Result.error("参数缺失");
        }
//        if (request.getSignConfirmOrderNo() == null || request.getSignConfirmOrderNo().length() == 0) {
//            return Result.error("参数缺失");
//        }
        if(request.getComment() == null || request.getComment().length() == 0){
            return Result.error("参数缺失");
        }
        SignRu ru = ruService.getById(request.getSignRuId());
        if (ru == null) {
            return Result.error("实例不存在");
        }
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
//        SignTaskThreadlocalVO threadlocalVO = SignTaskInfo.THREAD_LOCAL.get();
        if (!currentUser.getTenantUserId().equals(ru.getSysUserId())) {
            return Result.error("当前操作者非当前实例发起方，不能操作");
        }
//        SignRuTask ruTask = ruTaskService.getById(request.getTaskId());
//        if (ruTask == null) {
//            return Result.error("任务不存在");
//        }
        //意愿校验结果
//        Boolean checkSign = userConfirmService.checkSign(request.getSignConfirmOrderNo(), ru.getId(), ConfirmOperateType.REVOKE.getType());
//        if (!checkSign) {
//            return Result.error("意愿校验未通过");
//        }

        if (SignRuStatusEnum.APPROVING.getCode().equals(ru.getStatus()) || SignRuStatusEnum.WRITING.getCode().equals(ru.getStatus()) ||
                SignRuStatusEnum.SIGNING.getCode().equals(ru.getStatus()) ) {
//        if (SignRuStatusEnum.APPROVING.getCode().equals(ru.getStatus()) || SignRuStatusEnum.WRITING.getCode().equals(ru.getStatus())) {
            ru.setStatus(SignRuStatusEnum.REVOKE.getCode());
            boolean b = ruService.updateById(ru);
            if (!b) {
                return Result.error("操作失败");
            }
        } else {
            SignRuStatusEnum statusEnum = SignRuStatusEnum.getByCode(ru.getStatus());
            if (statusEnum == null) {
                return Result.error("实例异常，状态不存在");
            }
            return Result.error("实例状态为" + statusEnum.getName() + ",不可撤回");
        }
        //操作记录
        SignRuOperateRecord ruOperateRecord = new SignRuOperateRecord();
        ruOperateRecord.setSignRuId(ru.getId());
        ruOperateRecord.setAccountId(currentUser.getId());
        ruOperateRecord.setTenantId(currentUser.getTenantId());
        ruOperateRecord.setTenantUserId(currentUser.getTenantUserId());
        ruOperateRecord.setOperateType(SignRecordOperateTypeEnum.REVOKE.getType());
        ruOperateRecord.setActionType(SignRecordActionTypeEnum.REVOKE.getType());
        ruOperateRecord.setOperateTime(new Date());
        ruOperateRecord.setIpAddr(IPUtil.getIpAddr(httpServletRequest));
        ruOperateRecord.setTaskId(request.getTaskId());
        ruOperateRecord.setConfirmOrderNo(request.getSignConfirmOrderNo());
        ruOperateRecord.setOperateReason(request.getComment());
        ruOperateRecordService.save(ruOperateRecord);
        //撤回合同
        ruCallbackService.callback(ru.getId(), "", SignCallbackTypeEnum.RECALLED);
        return Result.OK();
    }


    // @ApiOperation("业务线实例-发起前-恢复")
    @RequestMapping(value = "/restore", method = RequestMethod.POST)
    public Result<?> restore(@RequestBody RestoreRequest request) {


        if (request == null || request.getSignRuId() == null || request.getSignRuId().length() == 0) {
            return Result.error("参数缺失");
        }
        SignRu ru = ruService.getById(request.getSignRuId());
        if (ru == null) {
            return Result.error("实例不存在");
        }
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        if (!currentUser.getTenantUserId().equals(ru.getSysUserId())) {
            return Result.error("当前操作者非当前实例发起方，不能操作");
        }
        if (SignRuStatusEnum.DELETE.getCode().equals(ru.getStatus())) {
            ru.setStatus(SignRuStatusEnum.DRAFT.getCode());
            ru.setDeleteFlag(false);
            boolean b = ruService.updateById(ru);
            if (!b) {
                return Result.error("操作失败");
            }
        } else {
            SignRuStatusEnum statusEnum = SignRuStatusEnum.getByCode(ru.getStatus());
            if (statusEnum == null) {
                return Result.error("实例异常，状态不存在");
            }
            return Result.error("实例状态为" + statusEnum.getName() + ",不可恢复");
        }
        return Result.OK();
    }


    // @ApiOperation("业务线实例-发起前-发起")
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @Limit(name = "发起业务线实例", prefix = "limit",limitType= LimitType.TOKEN, operateType = OperateType.ALL, count = 5,period=60,limitHandle = LimitHandleType.LOGOUT)
    public Result<?> start(@RequestBody StartRequest request, HttpServletRequest httpServletRequest) {
        if (request == null) {
            return Result.error("参数缺失");
        }
        if (request.getSignRuId() == null || request.getSignRuId().length() == 0) {
            return Result.error("实例主数据id-参数缺失");
        }
        SignRu ru = ruService.getById(request.getSignRuId());
        if (ru == null) {
            return Result.error("数据不存在");
        }
        if (ru.getStatus() == null || ru.getStatus() != SignRuStatusEnum.DRAFT.getCode()) {
            return Result.error("签约文件已发起，无需重复发送");
        }
        //设置rediskey
        String key = "ru_start_" + ru.getId();
        //查询key是否存在
        if (redisUtil.hasKey(key)) {
            return Result.error("请勿重复提交");
        }
        //如果不存在，则设置key，5秒钟失效
        redisUtil.set(key, "", 1l);
        //发起时间
        Date operateTime = new Date();
        //发起前校验各项参数，签约文件，签署控件，填写控件，发起人填写值是否都已经填写等等
        String start = ruBusinessService.start(request.getSignRuId());
        if (start == null) {
            return Result.error("发起失败");
        }
        try {
            //发起成功，更改主题和编号
            ruBusinessService.startGenerateSubject(request.getSignRuId());
        } catch (Exception e) {
        }
        //发起签署
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        //操作记录
        SignRuOperateRecord ruOperateRecord = new SignRuOperateRecord();
        ruOperateRecord.setSignRuId(ru.getId());
        ruOperateRecord.setAccountId(currentUser.getId());
        ruOperateRecord.setTenantId(currentUser.getTenantId());
        ruOperateRecord.setTenantUserId(currentUser.getTenantUserId());
        ruOperateRecord.setOperateType(SignRecordOperateTypeEnum.START.getType());
        ruOperateRecord.setActionType(SignRecordActionTypeEnum.START.getType());
        ruOperateRecord.setOperateTime(operateTime);
        ruOperateRecord.setIpAddr(IPUtil.getIpAddr(httpServletRequest));
        ruOperateRecordService.save(ruOperateRecord);

        iFlowService.complete(start, RuFlowEnum.INITIATE_FLOW.getName());

        ruCallbackService.callback(ru.getId(), null, SignCallbackTypeEnum.SEND_SIGNING);
        //查询是否还有任务
        Boolean allTaskComplete = ruService.allTaskComplete(ru.getId());
        if (allTaskComplete) {
            //完成签署
            ruCallbackService.callback(ru.getId(), null, SignCallbackTypeEnum.COMPLETE);
        }

        return Result.OK();
    }


    // @ApiOperation("业务线实例-运行中-获取签署人身份")
    @RequestMapping(value = "/run/signer/identify", method = RequestMethod.GET)
    public Result<SignerIdentifyResponse> runSignerIdentify(@RequestParam(value = "ruId") String ruId) {


        SignTaskThreadlocalVO threadlocalVO = SignTaskInfo.THREAD_LOCAL.get();

        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        SignerIdentifyResponse response = new SignerIdentifyResponse();


        SignRuTask query = new SignRuTask();
        query.setDeleteFlag(false);
        query.setSignRuId(threadlocalVO.getSignRuId());
        query.setTenantUserId(currentUser.getTenantUserId());
        query.setTaskType(TaskTypeEnum.SIGN_TASK.getCode());
        query.setTaskStatus(1);
        List<SignRuTask> ruTaskList = ruTaskService.getByEntity(query);
        if (ruTaskList == null || ruTaskList.size() == 0) {
            return Result.error("非当前业务操作人", null);
        }
        SignRuTask signRuTask = ruTaskList.get(0);
        if (SignerTypeEnum.SENDER.getCode().equals(signRuTask.getUserType()) || SignerTypeEnum.RECEIVER_ENT.getCode().equals(signRuTask.getUserType())) {
            SignRuSender sender = ruSenderService.getById(signRuTask.getUserTaskId());
            if (sender == null) {
                return Result.error("当前业务无该操作人", null);
            }

            response.setSignerId(signRuTask.getUserTaskId());
            if (SenderTypeEnum.ENTERPRISE.getCode().equals(sender.getSenderType())) {
                response.setSealId(sender.getSenderSealId());
                response.setType(2);
            } else {
                response.setType(1);
            }

        } else if (SignerTypeEnum.RECEIVER_PERSONAL.getCode().equals(signRuTask.getUserType())) {
            response.setSignerId(signRuTask.getUserTaskId());
            response.setType(1);
        }

        return Result.OK(response);


    }


    // @ApiOperation("业务线实例-运行中-验证企业签章是否有效")
    @RequestMapping(value = "/run/seal/verify", method = RequestMethod.GET)
    public Result<?> runSealVerify(@RequestParam(value = "sealId") String sealId) {


        SignEntSeal entSeal = entSealService.getById(sealId);

        if (entSeal == null) {
            return Result.error("签署不存在");
        }
        if (entSeal.getStatus() == null || entSeal.getStatus().equals(SealStatusEnum.UN_ENABLE.getCode())) {
            return Result.error("“" + entSeal.getSealName() + "”印章目前处于禁用状态，无法使用该印章，请联系印章管理员。", null);
        }

        if (entSeal.getSealStatus() != EntSealStatusEnum.ENABLED.getCode()) {
            EntSealStatusEnum[] values = EntSealStatusEnum.values();
            String msg = "";
            for (EntSealStatusEnum entSealStatusEnum : values) {
                if (entSeal.getSealStatus() == entSealStatusEnum.getCode()) {
                    msg = entSealStatusEnum.getName();
                }
            }
            return Result.error("“" + entSeal.getSealName() + "”印章目前处于" + msg + "状态，无法使用该印章，请联系印章管理员。", null);
        }
        //必须包含是印章使用者
        List<Integer> businessTypeRoleList = new ArrayList<>();
        businessTypeRoleList.add(BusinessAuthTypeRole.SEAL_USER.getCode());
        List<String> businessIdList = authBusinessService.getBusinessIdList(BusinessAuthType.SEAL, businessTypeRoleList);
        if (businessIdList == null || !businessIdList.contains(sealId)) {
            return Result.error("“" + entSeal.getSealName() + "”印章的授权已收回，请联系印章管理员为您授权该印章的使用权限。", null);
        }

        AnnexStorage annex = signFileService.findByFatherIdAndDataCategory(SignFileEnum.SEAL_FILE_ENT, entSeal.getId());
        if (annex == null) {
            return Result.error("签署图片不存在");
        }
        return Result.OK("", annex.getId());
    }

    // @ApiOperation("业务线实例-运行中-验证证书")
    @RequestMapping(value = "/run/certificate/verify", method = RequestMethod.GET)
    public Result<RunCertificateVerifyResponse> runCertificateVerify() {
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        SignTaskThreadlocalVO threadlocalVO = SignTaskInfo.THREAD_LOCAL.get();
        if (threadlocalVO == null) {
            return Result.error("实例不存在", null);
        }
        String signRuId = threadlocalVO.getSignRuId();
        SignRu ru = ruService.getById(signRuId);
        if (ru == null) {
            return Result.error("实例不存在", null);
        }
        if (ru.getCaSignType() == null) {
            return Result.error("实例未设置使用证书方式", null);
        }
        RunCertificateVerifyResponse response = new RunCertificateVerifyResponse();

        //默认当前应该使用的证书为个人租户证书
        Integer tenantType = TenantType.PERSONAL.getType();
        Integer holderType = CertHolderTypeEnum.PERSONAL.getCode();
        if (threadlocalVO.getUserType().equals(SignerTypeEnum.SENDER.getCode()) || threadlocalVO.getUserType().equals(SignerTypeEnum.RECEIVER_ENT.getCode())) {
            SignRuSender sender = ruSenderService.getById(threadlocalVO.getUserTaskId());
            if (sender == null) {
                return Result.error("签署人不存在", null);
            }
            if (sender.getSenderType().equals(SenderTypeEnum.ENTERPRISE.getCode())) {
                //如果为发起人，并且是组织盖章，则使用企业租户证书
                tenantType = TenantType.GROUP.getType();
                holderType = CertHolderTypeEnum.ENTERPRISE.getCode();
            }else{
                tenantType = TenantType.GROUP.getType();
            }
            // 获取企业下个人用户
            SysTenantUser sysTenantUserTemp = tenantUserService.getPersonalTenantUser(currentUser.getId());
            String operatorUnionId = "";
            if(sysTenantUserTemp != null){
                operatorUnionId = sysTenantUserTemp.getTenantId();
            }

            TenantInfoExtend operatorTenantInfo = tenantInfoExtendService.getTenantInfoByTenantId(operatorUnionId);

            SysTenantUser sysTenantUser = null ;
            if(operatorTenantInfo != null){
                sysTenantUser = tenantUserService.getTenantUser(currentUser.getTenantId(),currentUser.getId());
                response.setSignerAuthStatus(operatorTenantInfo.getAuthStatus());
                if(sysTenantUser.getAddType().equals("system")){
                    response.setSignerIsCompanyManager(1);
                }else{
                    response.setSignerIsCompanyManager(0);
                }
            }else{
                response.setSignerAuthStatus(0);
                response.setSignerIsCompanyManager(0);
            }
            response.setSignerTenantId(operatorUnionId);
        }
        response.setHolderType(holderType);
        //如果不使用证书，则直接返回
        if (SignRuCaSignTypeEnum.NO_USE_CA.getCode().equals(ru.getCaSignType())) {
            response.setReturnMsg(VerifyCertificateEnum.NO_USE_CERTIFICATE.getName());
            response.setReturnCode(VerifyCertificateEnum.NO_USE_CERTIFICATE.getCode());
            return Result.OK(response);
        }
        String tenantId = null;
        CertTypeEnum certTypeEnum = null;
        //获取个人或者企业的租户id
        SysTenantInfo tenantInfo = tenantInfoService.getById(currentUser.getTenantId());
        if (tenantInfo == null) {
            return Result.error("租户不存在", null);
        }
        if (tenantType.equals(TenantType.PERSONAL.getType())) {
            //个人
            if (tenantInfo.getTenantType().equals(TenantType.PERSONAL.getType())) {
                //如果当前登陆的就是个人租户身份，则直接赋值
                tenantId = tenantInfo.getId();

                TenantInfoExtend operatorTenantInfo = tenantInfoExtendService.getTenantInfoByTenantId(tenantId);
                if(operatorTenantInfo != null ){
                    response.setSignerAuthStatus(operatorTenantInfo.getAuthStatus());
                }else{
                    response.setSignerAuthStatus(0);
                }
            } else {
                //找到该租户的个人租户
                List<SysTenantInfo> tenantInfoList = tenantUserService.getTenantsByUserId(currentUser.getId());
                if (tenantInfoList == null || tenantInfoList.size() == 0) {
                    return Result.error("租户不存在", null);
                }
                for (SysTenantInfo tenant : tenantInfoList) {
                    if (tenant.getTenantType().equals(TenantType.PERSONAL.getType()) && tenant.getTenantStatus().equals(TenantStatus.ENABLE.getType())) {
                        tenantId = tenant.getId();
                    }
                }
            }
        } else if (tenantType.equals(TenantType.GROUP.getType())) {
            //团体
            if (tenantInfo.getTenantType().equals(TenantType.GROUP.getType())) {
                //如果当前登陆的就是企业租户身份，则直接赋值
                tenantId = tenantInfo.getId();
            } else {
                //使用该实例创建时所属的企业租户id
                tenantId = ru.getSysTenantId();
            }
        }

        if (tenantId == null || tenantId.length() == 0) {
            response.setReturnMsg(VerifyCertificateEnum.NO_HAVE_CERTIFICATE.getName());
            response.setReturnCode(VerifyCertificateEnum.NO_HAVE_CERTIFICATE.getCode());
            return Result.OK(response);
        }
        //如果是使用CA证书
        if (SignRuCaSignTypeEnum.CA.getCode().equals(ru.getCaSignType())) {
            certTypeEnum = CertTypeEnum.CA_TEST;
        }
        //如果是使用平台放篡改证书
        else if (SignRuCaSignTypeEnum.SYSTEM_CA.getCode().equals(ru.getCaSignType())) {
            certTypeEnum = CertTypeEnum.SYSTEM;
        }
        //根据系统配置，计算真正需要的证书类型
        certTypeEnum = certBusinessService.calculateCertType(certTypeEnum.getCode(), holderType);
        if (certTypeEnum == null) {
            return Result.error("实例使用证书方式异常", null);
        }
        response.setCertType(certTypeEnum.getCode());
        response.setTenantId(tenantId);
        //获取部门id，用于实名认证
        if (!certTypeEnum.getCode().equals(CertTypeEnum.SYSTEM.getCode())) {
            SysUserDepart query = new SysUserDepart();
            query.setUserId(currentUser.getId());
            query.setTenantId(tenantId);
            List<SysUserDepart> userDepartList = userDepartService.getByEntity(query);
            if (userDepartList != null && userDepartList.size() > 0) {
                SysUserDepart sysUserDepart = userDepartList.get(0);
                if (sysUserDepart != null) {
                    response.setDepartId(sysUserDepart.getDepartId());
                }
            }
        }

        //先查询是否有有效证书
        Integer countEnabledCert = tenantCertificateService.countEnabledCert(tenantId, certTypeEnum);
        if (countEnabledCert != null && countEnabledCert > 0) {
            //获取证书
            TenantCertificate enabledCert = tenantCertificateService.getEnabledCert(tenantId, certTypeEnum);
            if (enabledCert != null) {
                CertificateInfo certificateInfo = certificateInfoDao.getById(enabledCert.getCertId());
                if (certificateInfo == null) {
                    response.setReturnMsg(VerifyCertificateEnum.UN_ENABLE_CERTIFICATE.getName());
                    response.setReturnCode(VerifyCertificateEnum.UN_ENABLE_CERTIFICATE.getCode());
                    return Result.OK(response);
                } else {
                    //如果证书过期，重新颁发证书
                    Date termOfValidityEndTime = certificateInfo.getTermOfValidityEndTime();
                    if (termOfValidityEndTime != null && termOfValidityEndTime.before(new Date())) {
                        response.setReturnMsg(VerifyCertificateEnum.UN_ENABLE_CERTIFICATE.getName());
                        response.setReturnCode(VerifyCertificateEnum.UN_ENABLE_CERTIFICATE.getCode());
                        return Result.OK(response);
                    } else {
                        response.setReturnMsg(VerifyCertificateEnum.ENABLE_CERTIFICATE.getName());
                        response.setReturnCode(VerifyCertificateEnum.ENABLE_CERTIFICATE.getCode());
                        response.setIssueOrg(certificateInfo.getIssueOrg());
                        return Result.OK(response);
                    }
                }
            }
        }
        //如果没有有效证书，再查询是否有过证书
        Integer countCert = tenantCertificateService.countCert(tenantId, certTypeEnum);
        if (countCert != null && countCert > 0) {
            response.setReturnMsg(VerifyCertificateEnum.UN_ENABLE_CERTIFICATE.getName());
            response.setReturnCode(VerifyCertificateEnum.UN_ENABLE_CERTIFICATE.getCode());
            return Result.OK(response);
        }

        response.setReturnMsg(VerifyCertificateEnum.NO_HAVE_CERTIFICATE.getName());
        response.setReturnCode(VerifyCertificateEnum.NO_HAVE_CERTIFICATE.getCode());
        return Result.OK(response);

    }

    // @ApiOperation("业务线实例-运行中-更新证书")
    @RequestMapping(value = "/run/certificate/update", method = RequestMethod.POST)
    public Result<?> runCertificateUpdate(@RequestBody RunCertificateUpdateRequest request) {
        if (request == null || request.getTenantId() == null || request.getTenantId().length() == 0 || request.getCertType() == null || request.getHolderType() == null) {
            return Result.error("参数缺失");
        }

        String tenantCertificateId = null;
        Integer holderType = request.getHolderType();
        Integer certType = request.getCertType();
        String tenantId = request.getTenantId();
        TenantInfoDTO tenantInfoExt = tenantInfoExtendService.getTenantInfoExt(tenantId);

        //如果需要使用CA证书，则需要进行实名认证

        if (!certType.equals(CertTypeEnum.SYSTEM.getCode())) {

            if (tenantInfoExt == null || tenantInfoExt.getAuthStatus() != TenantAuthStatus.STATUS2.getStatus()) {
                if (request.getHolderType().equals(CertHolderTypeEnum.PERSONAL.getCode())) {
                    //个人证书
                    return Result.error("您当前未完成实名认证，无法更新证书");
                } else if (request.getHolderType().equals(CertHolderTypeEnum.ENTERPRISE.getCode())) {
                    //企业ca证书
                    return Result.error("您当前的企业未完成实名认证，无法更新证书");
                }

            }

        }
        //证书所需数据
        TenantCertInfo tenantCertInfo = new TenantCertInfo();
        tenantCertInfo.setTenantId(tenantId);
        tenantCertInfo.setHolderType(holderType);
        tenantCertInfo.setCertType(certType);
        if (holderType.equals(CertHolderTypeEnum.PERSONAL.getCode())) {
            tenantCertInfo.setName(tenantInfoExt.getName());
            tenantCertInfo.setPhone(tenantInfoExt.getPhone());
            tenantCertInfo.setIdCardNumber(tenantInfoExt.getOrganizationNo());
        } else if (holderType.equals(CertHolderTypeEnum.ENTERPRISE.getCode())) {
            tenantCertInfo.setEntName(tenantInfoExt.getName());
            tenantCertInfo.setUscc(tenantInfoExt.getOrganizationNo());
        }
        tenantCertificateId = certBusinessService.generateTenantCert(tenantCertInfo);
        if (tenantCertificateId == null || tenantCertificateId.length() == 0) {
            return Result.error("更新证书失败");
        }
        TenantCertificate tenantCertificate = tenantCertificateService.getById(tenantCertificateId);
        String certId = tenantCertificate.getCertId();
        CertificateInfo certificateInfo = certificateInfoDao.getById(certId);
        if (certificateInfo == null || certificateInfo.getTermOfValidityEndTime() == null) {
            return Result.error("更新证书失败");
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(certificateInfo.getTermOfValidityEndTime());
        return Result.OK("", format);
    }

    // @ApiOperation("业务线实例-运行中-是否需要签署意愿校验")
    @RequestMapping(value = "/run/sign/confirm", method = RequestMethod.GET)
    public Result<RunSignConfirmResponse> runSignConfirm(String operateType) {
        LoginUser currentUser = MySecurityUtils.getCurrentUser();

        if (operateType == null || operateType.length() == 0) {
            return Result.error("参数缺失", null);
        }

        //获取操作类型
        ConfirmOperateType confirmOperateType = ConfirmOperateType.getByType(operateType);
        if (confirmOperateType == null) {
            return Result.error("不存在该操作", null);
        }
        RunSignConfirmResponse response = new RunSignConfirmResponse();
        if (ConfirmOperateType.SUBMIT_SIGN.getType().equals(operateType)) {
            SignTaskThreadlocalVO threadlocalVO = SignTaskInfo.THREAD_LOCAL.get();
            //获取业务线配置的证书类型
            String signRuId = threadlocalVO.getSignRuId();
            SignRu ru = ruService.getById(signRuId);
            if (ru == null) {
                return Result.error("实例不存在", null);
            }
            if (ru.getCaSignType() == null) {
                return Result.error("实例未设置使用证书方式", null);
            }
            //是有需要人脸
            boolean faceNeedFlag = false;
            Boolean certSysConfig = certBusinessService.getCertSysConfig();
            if (operateType.equals(ConfirmOperateType.SUBMIT_SIGN.getType())) {
                //必须是签署
                if (ru.getCaSignType().equals(SignRuCaSignTypeEnum.CA.getCode())) {
                    //必须是使用证书
                    if (certSysConfig != null && certSysConfig) {
                        //使用真实CA证书
                        faceNeedFlag = true;
                    }
                }
            }
            //获取系统意愿校验配置
            String myConfirmType = userConfirmService.getMyConfirmType();
            if (faceNeedFlag) {
                //如果是签署,需要校验是否有人脸
                String taskId = threadlocalVO.getTaskId();
                Boolean signConfirm = ruBusinessService.signConfirm(taskId);
                if (signConfirm) {
                    response.setConfirmType(ConfirmTypeEnum.FACE.getType());
                } else {
                    response.setConfirmType(myConfirmType);
                }
            } else {
                //使用系统配置校验方式
                response.setConfirmType(myConfirmType);
            }
        } else {
            //获取系统意愿校验配置
            String myConfirmType = userConfirmService.getMyConfirmType();
            response.setConfirmType(myConfirmType);
        }


        //获取个人实名状态
        Integer personalAccountStatus = null;
        SysTenantUser tenantUser = tenantUserService.getPersonalTenantUser(currentUser.getId());
        if (tenantUser == null) {
            //没有个人租户
            personalAccountStatus = -1;
        } else {
            //有个人租户，判断实名状态
            TenantInfoDTO tenantInfoExt = tenantInfoExtendService.getTenantInfoExt(tenantUser.getTenantId());
            if (tenantInfoExt == null) {
                personalAccountStatus = TenantAuthStatus.STATUS0.getStatus();
            } else {
                if (tenantInfoExt.getAuthStatus() == null) {
                    personalAccountStatus = TenantAuthStatus.STATUS0.getStatus();
                } else {
                    personalAccountStatus = tenantInfoExt.getAuthStatus();
                }
            }
        }
        response.setPersonalAccountStatus(personalAccountStatus);
        return Result.OK(response);
    }


    // @ApiOperation("业务线实例-运行中-提交填写")
    @RequestMapping(value = "/run/submit/write", method = RequestMethod.POST)
    public Result<?> runSubmitWrite(@RequestBody RunSubmitWriteRequest request, HttpServletRequest httpServletRequest) {
        if (request == null || request.getSignRuId() == null || request.getSignRuId().length() == 0) {
            return Result.error("参数缺失");
        }
//        if (request.getSignConfirmOrderNo() == null || request.getSignConfirmOrderNo().length() == 0) {
//            return Result.error("参数缺失");
//        }
        SignTaskThreadlocalVO threadlocalVO = SignTaskInfo.THREAD_LOCAL.get();
        String taskId = threadlocalVO.getTaskId();
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        //校验是否已经完成
        SignRu ru = ruService.getById(threadlocalVO.getSignRuId());
        if (ru == null || ru.getStatus() == null) {
            return Result.error("实例不存在", null);
        }
        //意愿校验结果
//        Boolean checkSign = userConfirmService.checkSign(request.getSignConfirmOrderNo(), threadlocalVO.getTaskId(), ConfirmOperateType.SUBMIT_WRITE.getType());
//        if (!checkSign) {
//            return Result.error("意愿校验未通过");
//        }
        //校验状态
        if (!ru.getStatus().equals(SignRuStatusEnum.WRITING.getCode())) {
            verifyRu(ru.getStatus(), ru.getId());
        }


        SignRuTask query = new SignRuTask();
        query.setDeleteFlag(false);
        query.setSignRuId(threadlocalVO.getSignRuId());
        query.setTenantUserId(currentUser.getTenantUserId());
        query.setTaskType(TaskTypeEnum.WRITE_TASK.getCode());
        List<SignRuTask> ruTaskList = ruTaskService.getByEntity(query);
        if (ruTaskList == null || ruTaskList.size() == 0) {
            return Result.error("非当前业务操作人", null);
        }
        SignRuTask signRuTask = ruTaskList.get(0);
        if (signRuTask.getTaskStatus() == 2) {
            return Result.error("您已办理完成，无需重复操作", null);
        }
        if ((request.getControlList() == null || request.getControlList().size() == 0) && (request.getDeleteIdList() == null || request.getDeleteIdList().size() == 0)) {
            return Result.error("参数缺失");
        }
        if (request.getControlList() != null && request.getControlList().size() > 0) {
            String message = ruBusinessService.verifyControl(request.getControlList());
            if (message != null) {
                return Result.error(message);
            }
            //补充控件来源类型,操作时设置
            controlOriginTypeFill(request.getControlList(), ControlOriginTypeEnum.OPERATION);
        }
        //设置rediskey
        String key = "ru_write_" + threadlocalVO.getSignRuId() + "_" + currentUser.getTenantUserId();
        //查询key是否存在
        if (redisUtil.hasKey(key)) {
            return Result.error("请勿重复提交");
        }
        //如果不存在，则设置key，5秒钟失效
        redisUtil.set(key, "", 1l);
        //保存控件填写值
        ruBusinessService.saveControlList(request.getControlList(), request.getDeleteIdList(), request.getSignRuId(), null);
        //保存记录
        //操作记录
        SignRuOperateRecord ruOperateRecord = new SignRuOperateRecord();
        ruOperateRecord.setSignRuId(ru.getId());
        ruOperateRecord.setAccountId(currentUser.getId());
        ruOperateRecord.setTenantId(currentUser.getTenantId());
        ruOperateRecord.setTenantUserId(currentUser.getTenantUserId());
        ruOperateRecord.setOperateType(SignRecordOperateTypeEnum.WRITE.getType());
        ruOperateRecord.setActionType(SignRecordActionTypeEnum.SUBMIT_WRITE.getType());
        ruOperateRecord.setOperateTime(new Date());
        ruOperateRecord.setIpAddr(IPUtil.getIpAddr(httpServletRequest));
        ruOperateRecord.setTaskId(threadlocalVO.getTaskId());
        ruOperateRecord.setConfirmOrderNo(request.getSignConfirmOrderNo());
        ruOperateRecordService.save(ruOperateRecord);
        //驱动
        iFlowService.complete(null, RuFlowEnum.APPROVE.getName());
        //填写
        ruCallbackService.callback(ru.getId(), taskId, SignCallbackTypeEnum.SUBMIT_WRITE);
        //查询下一个任务
        SignRuTask myTask = ruService.getMyTask(ru.getId());
        if (myTask != null) {
            SubmitResponse response = new SubmitResponse();
            response.setTaskId(myTask.getId());
            if (myTask.getTaskType().equals(TaskTypeEnum.SIGN_TASK.getCode())) {
                response.setTaskType("sign");
            } else if (myTask.getTaskType().equals(TaskTypeEnum.WRITE_TASK.getCode())) {
                response.setTaskType("write");
            }
            response.setRuId(ru.getId());
            return Result.OK(response);
        }

        //查询是否还有任务
        Boolean allTaskComplete = ruService.allTaskComplete(ru.getId());
        if (allTaskComplete) {
            //完成签署
            ruCallbackService.callback(ru.getId(), null, SignCallbackTypeEnum.COMPLETE);
        }
        return Result.OK();
    }


    // @ApiOperation("业务线实例-运行中-拒绝填写")
    @RequestMapping(value = "/run/reject/write", method = RequestMethod.POST)
    public Result<?> runRejectWrite(@RequestBody RunRejectWriteRequest request, HttpServletRequest httpServletRequest) {
//        if (request.getSignConfirmOrderNo() == null || request.getSignConfirmOrderNo().length() == 0) {
//            return Result.error("参数缺失");
//        }
        SignTaskThreadlocalVO threadlocalVO = SignTaskInfo.THREAD_LOCAL.get();
        String taskId = threadlocalVO.getTaskId();

        //校验是否已经完成
        SignRu ru = ruService.getById(threadlocalVO.getSignRuId());
        if (ru == null || ru.getStatus() == null) {
            return Result.error("实例不存在", null);
        }
        //意愿校验结果
//        Boolean checkSign = userConfirmService.checkSign(request.getSignConfirmOrderNo(), threadlocalVO.getTaskId(), ConfirmOperateType.REJECT_WRITE.getType());
//        if (!checkSign) {
//            return Result.error("意愿校验未通过");
//        }
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        SignRuTask query = new SignRuTask();
        query.setDeleteFlag(false);
        query.setSignRuId(threadlocalVO.getSignRuId());
        query.setTenantUserId(currentUser.getTenantUserId());
        query.setTaskType(TaskTypeEnum.WRITE_TASK.getCode());
        List<SignRuTask> ruTaskList = ruTaskService.getByEntity(query);
        if (ruTaskList == null || ruTaskList.size() == 0) {
            return Result.error("非当前业务操作人", null);
        }
        SignRuTask signRuTask = ruTaskList.get(0);
        if (signRuTask.getTaskStatus() == 2) {
            return Result.error("您已办理完成，无需重复操作", null);
        }
        //校验状态
        if (!ru.getStatus().equals(SignRuStatusEnum.WRITING.getCode())) {
            verifyRu(ru.getStatus(), ru.getId());
        }
        //操作记录
        SignRuOperateRecord ruOperateRecord = new SignRuOperateRecord();
        ruOperateRecord.setSignRuId(ru.getId());
        ruOperateRecord.setAccountId(currentUser.getId());
        ruOperateRecord.setTenantId(currentUser.getTenantId());
        ruOperateRecord.setTenantUserId(currentUser.getTenantUserId());
        ruOperateRecord.setOperateType(SignRecordOperateTypeEnum.WRITE.getType());
        ruOperateRecord.setActionType(SignRecordActionTypeEnum.REJECT_WRITE.getType());
        ruOperateRecord.setOperateTime(new Date());
        ruOperateRecord.setIpAddr(IPUtil.getIpAddr(httpServletRequest));
        ruOperateRecord.setTaskId(threadlocalVO.getTaskId());
        ruOperateRecord.setConfirmOrderNo(request.getSignConfirmOrderNo());
        ruOperateRecord.setOperateReason(request.getComment());
        ruOperateRecordService.save(ruOperateRecord);
        //驱动
        iFlowService.complete(null, RuFlowEnum.REJECT.getName());
        //拒填
        ruCallbackService.callback(ru.getId(), taskId, SignCallbackTypeEnum.REFUSAL_WRITE);

//        SignRuTask ruTask = ruTaskService.getById(taskId);
//        if (ruTask != null && ruTask.getTaskStatus() != null && ruTask.getTaskStatus() == 2) {
//
//        }

        return Result.OK();
    }

    // @ApiOperation("业务线实例-运行中-提交签署")
    @RequestMapping(value = "/run/submit/sign", method = RequestMethod.POST)
    public Result<?> runSubmitSign(@RequestBody RunSubmitSignRequest request) throws Exception {
        if (request == null || request.getSignRuId() == null) {
            return Result.error("参数缺失");
        }
        if (request.getSignConfirmOrderNo() == null || request.getSignConfirmOrderNo().length() == 0) {
            return Result.error("参数缺失");
        }
        if ((request.getControlList() == null || request.getControlList().size() == 0)
                && (request.getDeleteIdList() == null || request.getDeleteIdList().size() == 0)) {
            return Result.error("参数缺失");
        }
        if (request.getControlList() != null && request.getControlList().size() > 0) {
            String message = ruBusinessService.verifyControl(request.getControlList());
            if (message != null) {
                return Result.error(message);
            }
            for (DocControlVo controlVo : request.getControlList()) {
                if (controlVo.getControlType().equals(ControlTypeEnum.SIGN_DATE.getName())) {
                    if (controlVo.getFormat() == null || controlVo.getFormat().length() == 0) {
                        throw new PaasException("签署日期控件-时间格式不能为空");
                    }
                    try {
                        SimpleDateFormat signDateFormat = new SimpleDateFormat(controlVo.getFormat());
                        signDateFormat.format(new Date());
                    } catch (Exception e) {
                        return Result.error("签署日期控件-时间格式错误：" + controlVo.getFormat());
                    }
                }
            }
            //补充控件来源类型,操作时设置
            controlOriginTypeFill(request.getControlList(), ControlOriginTypeEnum.OPERATION);
        }
        //校验订单号
//        SignRuSignTemporary temporary = ruSignTemporaryService.getByOrderNo(request.getSignConfirmOrderNo());
//        if (temporary != null) {
//            return Result.error("订单号已存在", null);
//        }
        //校验业务线实例
        checkBeforeSign();
        //保存签署临时数据
        SignOrderRequest signOrderRequest = ruBusinessService.submitSign(request);

        SysAppInfo sysAppInfo = sysAppInfoService.getById("490489ab-d8b4-414c-ad77-d856962c286f");
        signOrderRequest.setCallbackPage(request.getCallbackPage());
        signOrderRequest.setSignCallbackUrl(sysAppInfo.getAppAddress() +"/resrun-paas/yundun/sign/callback");

        //请求云盾执行意愿校验
        SignOrderServiceInfoResponse signOrderServiceInfoResponse = signServiceExternal.generateSignOrderAndGetSignAuthUrl(signOrderRequest);

        return Result.OK(signOrderServiceInfoResponse);
    }


    // @ApiOperation("业务线实例-运行中-提交签署-移动端")
    @RequestMapping(value = "/run/phone/submit/sign", method = RequestMethod.POST)
    public Result<?> runPhoneSubmitSign(@RequestBody RunSubmitSignRequest request) throws Exception {
        if (request == null || request.getSignRuId() == null) {
            return Result.error("参数缺失");
        }
        if (request.getSignConfirmOrderNo() == null || request.getSignConfirmOrderNo().length() == 0) {
            return Result.error("参数缺失");
        }
        if ((request.getControlList() == null || request.getControlList().size() == 0)
                && (request.getDeleteIdList() == null || request.getDeleteIdList().size() == 0)) {
            return Result.error("参数缺失");
        }
        if (request.getControlList() != null && request.getControlList().size() > 0) {
            String message = ruBusinessService.verifyControl(request.getControlList());
            if (message != null) {
                return Result.error(message);
            }
            for (DocControlVo controlVo : request.getControlList()) {
                if (controlVo.getControlType().equals(ControlTypeEnum.SIGN_DATE.getName())) {
                    if (controlVo.getFormat() == null || controlVo.getFormat().length() == 0) {
                        return Result.error("签署日期控件-时间格式不能为空");
                    }
                    try {
                        SimpleDateFormat signDateFormat = new SimpleDateFormat(controlVo.getFormat());
                        signDateFormat.format(new Date());
                    } catch (Exception e) {
                        return Result.error("签署日期控件-时间格式错误：" + controlVo.getFormat());
                    }
                }
            }
            //补充控件来源类型,操作时设置
            controlOriginTypeFill(request.getControlList(), ControlOriginTypeEnum.OPERATION);
        }
        //校验订单号
//        SignRuSignTemporary temporary = ruSignTemporaryService.getByOrderNo(request.getSignConfirmOrderNo());
//        if (temporary != null) {
//            return Result.error("订单号已存在", null);
//        }
        //校验业务线实例
        checkBeforeSign();

        //保存签署临时数据
        SignOrderRequest signOrderRequest = ruBusinessService.submitSign(request);

        SysAppInfo sysAppInfo = sysAppInfoService.getById("490489ab-d8b4-414c-ad77-d856962c286f");
        signOrderRequest.setCallbackPage(request.getCallbackPage());
        signOrderRequest.setSignCallbackUrl(sysAppInfo.getAppAddress()+"/resrun-paas/yundun/sign/callback");

        //请求云盾执行意愿校验
        SignOrderServiceInfoResponse signOrderServiceInfoResponse = signServiceExternal.generateSignOrderAndGetSignAuthUrl(signOrderRequest);

        return Result.OK(signOrderServiceInfoResponse);
    }

    // @ApiOperation("业务线实例-运行中-完成签署")
    @RequestMapping(value = "/run/completed/sign", method = RequestMethod.POST)
    public Result<?> runCompletedSign(@RequestBody CompletedSignRequest request, HttpServletRequest httpServletRequest) {
        if (request == null || request.getSignConfirmOrderNo() == null || request.getSignConfirmOrderNo().length() == 0) {
            return Result.error("参数缺失");
        }
        //获取本地变量
        SignTaskThreadlocalVO signTaskThreadlocalVO = SignTaskInfo.THREAD_LOCAL.get();
        String signRuId = signTaskThreadlocalVO.getSignRuId();
        String taskId = signTaskThreadlocalVO.getTaskId();
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        //业务线实例
        SignRu signRu = ruService.getById(signRuId);
        if (signRu == null) {
            throw new PaasException("业务线实例不存在");
        }
        //签署前再次校验
        Boolean checkSign = userConfirmService.checkSign(request.getSignConfirmOrderNo(), signTaskThreadlocalVO.getTaskId(), ConfirmOperateType.SUBMIT_SIGN.getType());
        if (!checkSign) {
            try {
                SignOrderStatusInfoResponse signOrderStatusInfoResponse = signServiceExternal.getSignOrderStatus(signTaskThreadlocalVO.getSignOrderNo());

                if(signOrderStatusInfoResponse.getStatus() == 1){
                    signOrderStatusInfoResponse.setTaskId(signTaskThreadlocalVO.getTaskId());
                    if(signOrderStatusInfoResponse.getOrderStatus() == SignOrdeStatusResponseTypeEnum.NO_AUTH.getCode()){
                        return Result.error("签署意愿校验未通过-签署失败",signOrderStatusInfoResponse);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        checkBeforeSign();
        //获取签署临时数据
        SignRuSignTemporary temporary = ruSignTemporaryService.getByParam(request.getSignConfirmOrderNo(),
                signTaskThreadlocalVO.getSignRuId(), signTaskThreadlocalVO.getTaskId());

        if (temporary == null) {
            return Result.error("订单号不存在");
        }
        if (temporary.getStatus() == null || temporary.getStatus() == SignRuSignTemporaryStatus.FINISHED.getCode()) {
            return Result.error("签署已办理完成");
        }
        if (temporary.getParams() == null || temporary.getParams().length() == 0) {
            return Result.error("签署数据异常");
        }
        CompletedSignVo vo = null;
        try {
            vo = JSON.parseObject(temporary.getParams(), CompletedSignVo.class);
        } catch (Exception e) {

        }
        if (vo == null) {
            return Result.error("签署数据异常");
        }
        //设置rediskey
        String key = "ru_sign_" + signTaskThreadlocalVO.getSignRuId() + "_" + currentUser.getTenantUserId();
        //查询key是否存在
        if (redisUtil.hasKey(key)) {
            return Result.error("请勿重复提交");
        }
        //如果不存在，则设置key，5秒钟失效
        redisUtil.set(key, "", 10l);
        //进行签署
        String ipAddr = IPUtil.getIpAddr(httpServletRequest);
        String ruId = ruBusinessService.completedSign(vo, temporary.getId(), ipAddr);
        //驱动
        iFlowService.complete(null, RuFlowEnum.APPROVE.getName());
        //签署
        ruCallbackService.callback(signRuId, taskId, SignCallbackTypeEnum.SUBMIT_SIGN);
        //查询下一个任务
        SubmitResponse response = new SubmitResponse();
        response.setRuId(ruId);
        SignRuTask myTask = ruService.getMyTask(ruId);
        if (myTask != null) {
            response.setTaskId(myTask.getId());
            if (myTask.getTaskType().equals(TaskTypeEnum.SIGN_TASK.getCode())) {
                response.setTaskType("sign");
            } else if (myTask.getTaskType().equals(TaskTypeEnum.WRITE_TASK.getCode())) {
                response.setTaskType("write");
            }
        }
        //查询是否还有任务
        Boolean allTaskComplete = ruService.allTaskComplete(ruId);
        if (allTaskComplete) {
            //完成签署
            ruCallbackService.callback(signRuId, null, SignCallbackTypeEnum.COMPLETE);
        }
        return Result.OK(response);
    }


    /**
     * @return void
     * @Description #校验业务线实例
     * @Param []
     **/
    public void checkBeforeSign() {
        //校验是否已经完成
        SignTaskThreadlocalVO threadlocalVO = SignTaskInfo.THREAD_LOCAL.get();
        //当前登录租户
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        //校验是否已经完成
        SignRu ru = ruService.getById(threadlocalVO.getSignRuId());
        if (ru == null || ru.getStatus() == null) {
            throw new PaasException("实例不存在");
        }
        //校验状态
        if (!ru.getStatus().equals(SignRuStatusEnum.SIGNING.getCode())) {
            verifyRu(ru.getStatus(), ru.getId());
        }
        SignRuTask query = new SignRuTask();
        query.setDeleteFlag(false);
        query.setSignRuId(threadlocalVO.getSignRuId());
        query.setTenantUserId(currentUser.getTenantUserId());
        query.setTaskType(TaskTypeEnum.SIGN_TASK.getCode());
        List<SignRuTask> ruTaskList = ruTaskService.getByEntity(query);
        if (ruTaskList == null || ruTaskList.size() == 0) {
            throw new PaasException("非当前业务操作人");
        }

        Boolean finishFlag = true;
        for (SignRuTask ruTask : ruTaskList) {
            if (ruTask.getTaskStatus() == null || ruTask.getTaskStatus() == 1) {
                finishFlag = false;
            }
        }
        if (finishFlag) {
            throw new PaasException("您已办理完成，无需重复操作");
        }

    }

    // @ApiOperation("业务线实例-运行中-拒绝签署")
    @RequestMapping(value = "/run/reject/sign", method = RequestMethod.POST)
    public Result<?> runRejectSign(@RequestBody RunRejectSignRequest request, HttpServletRequest httpServletRequest) {

//        if (request.getSignConfirmOrderNo() == null || request.getSignConfirmOrderNo().length() == 0) {
//            return Result.error("参数缺失");
//        }

        //校验是否已经完成
        SignTaskThreadlocalVO threadlocalVO = SignTaskInfo.THREAD_LOCAL.get();
        String signRuId = threadlocalVO.getSignRuId();
        String taskId = threadlocalVO.getTaskId();
        //校验是否已经完成
        SignRu ru = ruService.getById(threadlocalVO.getSignRuId());
        if (ru == null || ru.getStatus() == null) {
            return Result.error("实例不存在", null);
        }
        //意愿校验结果
//        Boolean checkSign = userConfirmService.checkSign(request.getSignConfirmOrderNo(), threadlocalVO.getTaskId(), ConfirmOperateType.REJECT_SIGN.getType());
//        if (!checkSign) {
//            return Result.error("意愿校验未通过");
//        }

        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        SignRuTask query = new SignRuTask();
        query.setDeleteFlag(false);
        query.setSignRuId(threadlocalVO.getSignRuId());
        query.setTenantUserId(currentUser.getTenantUserId());
        query.setTaskType(TaskTypeEnum.SIGN_TASK.getCode());
        List<SignRuTask> ruTaskList = ruTaskService.getByEntity(query);
        if (ruTaskList == null || ruTaskList.size() == 0) {
            return Result.error("非当前业务操作人", null);
        }
        Boolean finishFlag = true;
        for (SignRuTask ruTask : ruTaskList) {
            if (ruTask.getTaskStatus() == null || ruTask.getTaskStatus() == 1) {
                finishFlag = false;
            }
        }
        if (finishFlag) {
            return Result.error("您已办理完成，无需重复操作", null);
        }
        //校验状态
        if (!ru.getStatus().equals(SignRuStatusEnum.SIGNING.getCode())) {
            verifyRu(ru.getStatus(), ru.getId());
        }
        //操作记录
        SignRuOperateRecord ruOperateRecord = new SignRuOperateRecord();
        ruOperateRecord.setSignRuId(ru.getId());
        ruOperateRecord.setAccountId(currentUser.getId());
        ruOperateRecord.setTenantId(currentUser.getTenantId());
        ruOperateRecord.setTenantUserId(currentUser.getTenantUserId());

        if (threadlocalVO.getUserType() != null && threadlocalVO.getUserType() == SignerTypeEnum.RECEIVER_PERSONAL.getCode()) {
            ruOperateRecord.setOperateType(SignRecordOperateTypeEnum.PRIVATE_SIGN.getType());
        } else {
            SignRuSender sender = ruSenderService.getById(threadlocalVO.getUserTaskId());
            if (sender != null && sender.getSenderType() != null && sender.getSenderType() == SenderTypeEnum.ENTERPRISE.getCode()) {
                ruOperateRecord.setOperateType(SignRecordOperateTypeEnum.ENT_SIGN.getType());
            } else {
                ruOperateRecord.setOperateType(SignRecordOperateTypeEnum.PRIVATE_SIGN.getType());
            }
        }

        ruOperateRecord.setActionType(SignRecordActionTypeEnum.REJECT_SIGN.getType());
        ruOperateRecord.setOperateTime(new Date());
        ruOperateRecord.setIpAddr(IPUtil.getIpAddr(httpServletRequest));
        ruOperateRecord.setTaskId(threadlocalVO.getTaskId());
        ruOperateRecord.setConfirmOrderNo(request.getSignConfirmOrderNo());
        ruOperateRecord.setOperateReason(request.getComment());
        ruOperateRecordService.save(ruOperateRecord);
        //驱动
        iFlowService.complete(null, RuFlowEnum.REJECT.getName());
        //拒签
        ruCallbackService.callback(signRuId, taskId, SignCallbackTypeEnum.REFUSAL_SIGN);
//        SignRuTask ruTask = ruTaskService.getById(taskId);
//        if (ruTask != null && ruTask.getTaskStatus() != null && ruTask.getTaskStatus() == 2) {
//
//        }

        return Result.OK();
    }


    /**
     * @return void
     * @Description #补充控件来源类型
     * @Param [controlVoList, originTypeEnum]
     **/
    public void controlOriginTypeFill(List<DocControlVo> controlVoList, ControlOriginTypeEnum originTypeEnum) {
        for (DocControlVo controlVo : controlVoList) {
            if (controlVo.getOriginType() == null) {
                controlVo.setOriginType(originTypeEnum.getCode());
            }
        }
    }

    public void verifyRu(Integer status, String ruId) {
        String operatorName = null;
        //已拒填
        if (status.equals(SignRuStatusEnum.REJECT_WRITE.getCode())) {
            SignRuOperator query = new SignRuOperator();
            query.setSignRuId(ruId);
            query.setOperateStatus(OperateStatusEnum.REJECT.getCode());
            query.setOperateType(OperateTypeEnum.WRITE.getCode());
            query.setDeleteFlag(false);

            List<SignRuOperator> operatorList = ruOperatorService.getByEntity(query);
            if (operatorList != null && operatorList.size() > 0 && operatorList.get(0) != null) {
                SignRuOperator operator = operatorList.get(0);
                if (operator.getSignerType().equals(SignerTypeEnum.SENDER.getCode())) {
                    SysTenantUser tenantUser = tenantUserService.getById(operator.getOperateUserId());
                    if (tenantUser != null) {
                        operatorName = tenantUser.getNickName();
                    }
                } else if (operator.getSignerType().equals(SignerTypeEnum.RECEIVER_PERSONAL.getCode())) {
                    operatorName = operator.getOperateName();
                }
            }
            if (operatorName != null) {
                throw new PaasException("该文件已被" + operatorName + "拒填，无法继续操作");
            } else {
                throw new PaasException("该文件已拒填，无法继续操作");
            }

        }
        //已拒签
        else if (status.equals(SignRuStatusEnum.REJECT_SIGN.getCode())) {
            SignRuOperator query = new SignRuOperator();
            query.setSignRuId(ruId);
            query.setOperateStatus(OperateStatusEnum.REJECT.getCode());
            query.setOperateType(OperateTypeEnum.SIGN.getCode());
            query.setDeleteFlag(false);
            List<SignRuOperator> operatorList = ruOperatorService.getByEntity(query);
            if (operatorList != null && operatorList.size() > 0 && operatorList.get(0) != null) {
                SignRuOperator operator = operatorList.get(0);
                if (operator.getSignerType().equals(SignerTypeEnum.SENDER.getCode())) {
                    SysTenantUser tenantUser = tenantUserService.getById(operator.getOperateUserId());
                    if (tenantUser != null) {
                        operatorName = tenantUser.getNickName();
                    }
                } else if (operator.getSignerType().equals(SignerTypeEnum.RECEIVER_PERSONAL.getCode())) {
                    operatorName = operator.getOperateName();
                }
            }
            if (operatorName != null) {
                throw new PaasException("该文件已被" + operatorName + "拒签，无法继续操作");
            } else {
                throw new PaasException("该文件已拒签，无法继续操作");
            }
        }
        //已失效
        else if (status.equals(SignRuStatusEnum.EXPIRE.getCode())) {
            throw new PaasException("该文件已失效，无法继续操作");
        }
        //已撤回
        else if (status.equals(SignRuStatusEnum.REVOKE.getCode())) {
            throw new PaasException("该文件已被发起人撤回，无法继续操作");
        }
        //已完成
        else if (status.equals(SignRuStatusEnum.DONE.getCode())) {
            throw new PaasException("该文件已签署完成，无法继续操作");
        } else {
            throw new PaasException("该文件状态异常，无法继续操作");
        }
    }


    // @ApiOperation("获取手写二维码key")
    @RequestMapping(value = "/sign/getKey", method = RequestMethod.GET)
    public Result<?> getKey() {
        String pre = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 4).toUpperCase();
        String last = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 9).toUpperCase();
        String key = pre + "-" + last;
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        //设置token缓存有效时间
        redisUtil.set(CommonConstant.PREFIX_QR_CODE + key, loginUser.getId());
        redisUtil.expire(CommonConstant.PREFIX_QR_CODE + key, CommonConstants.QRCODEVALID * 60);
        return Result.OK("ok", key);
    }

    // @ApiOperation("校验手写二维码key")
    @RequestMapping(value = "/sign/checkKey", method = RequestMethod.GET)
    public Result<?> checkKey(@RequestParam("key") String key) {
        Object cacheToken = redisUtil.get(CommonConstant.PREFIX_QR_CODE + key);
        if (oConvertUtils.isNotEmpty(cacheToken)) {
            BaseMap baseMap = new BaseMap();
            baseMap.put("userId", cacheToken + key);
            redisMQClient.sendMessage("signatureScanListener", baseMap);
            return Result.OK(true);
        }
        return Result.OK(false);
    }


    // @ApiOperation("提交图片")
    @RequestMapping(value = "/sign/signature", method = RequestMethod.POST)
    public Result<?> signature(@Valid @RequestBody SignaturePicVO vo) {
        Object cacheToken = redisUtil.get(CommonConstant.PREFIX_QR_CODE + vo.getKey());
        if (oConvertUtils.isEmpty(cacheToken)) {
            return Result.error("二维码已经失效");
        }
        redisUtil.del(CommonConstant.PREFIX_QR_CODE + vo.getKey());
        //2、放到消息队列(生产者-消费者模式)
        BaseMap baseMap = new BaseMap();
        baseMap.put("userId", cacheToken + vo.getKey());
        baseMap.put("signature", vo.getSignature());
        redisMQClient.sendMessage("signatureListener", baseMap);
        return Result.OK();
    }

    // @ApiOperation("业务线实例-自测-下载文件")
    @RequestMapping(value = "/file/download", method = RequestMethod.GET)
    public Result<?> fileDownload(@RequestParam("annexId") String annexId) {
        byte[] fileByte = signFileService.getByteById(annexId);
        if (fileByte != null) {
            try {
                IOUtils.write(fileByte, new FileOutputStream(new File("/Users/gongfenglai/Desktop/test/opensign/" + System.currentTimeMillis() + ".pdf")));
            } catch (Exception e) {

            }

        }
        return Result.OK();
    }

//    // @ApiOperation("业务线实例-下载文件")
//    @RequestMapping(value = "/doc/file/download", method = RequestMethod.GET)
//    public Result<?> docFileDownload(@RequestParam("ruId") String ruId,@RequestParam("ruDocId") String ruDocId, HttpServletResponse response) {
//        if(ruId == null || ruId.length() == 0 || ruDocId == null || ruDocId.length() == 0){
//            return Result.error("参数缺失");
//        }
//        SignRu signRu = ruService.getById(ruId);
//        if(signRu == null){
//            return Result.error("数据不存在");
//        }
//        SignRuDoc ruDoc = ruDocService.getById(ruDocId);
//        if(ruDoc == null){
//            return Result.error("数据不存在");
//        }
//        SignRuDocOperate current = ruDocOperateService.getCurrentByDocId(ruDocId);
//        if(current == null || current.getAnnexId() == null || current.getAnnexId().length() == 0){
//            return Result.error("数据不存在");
//        }
//        byte[] pdfByte = signFileService.getByteById(current.getAnnexId());
//
//        AnnexStorage annex = signFileService.getAnnexById(current.getAnnexId());
//        if(pdfByte == null || annex == null){
//            return Result.error("数据不存在");
//        }
//
//        byte[] sign = ruBusinessService.signDownloadFile(pdfByte, signRu.getStatus());
//        //下载文件
//        OutputStream outputStream = null;
//        try {
//            String fileNameencode = URLEncoder.encode(annex.getRealName(), "UTF-8");
//
//            response.setCharacterEncoding("utf-8");
//            response.setContentType("application/octet-stream");
//            response.setHeader("Content-Disposition", "attachment;fileName=" + fileNameencode);
//            response.setHeader("fileName", fileNameencode);
//            response.setHeader("Access-Control-Expose-Headers", "fileName");
//
//            outputStream = response.getOutputStream();
//            org.apache.tomcat.util.http.fileupload.IOUtils.copy(new ByteArrayInputStream(sign), outputStream);
//            outputStream.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (outputStream != null) {
//                try {
//                    outputStream.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return Result.OK();
//    }

    // @ApiOperation("业务线实例-下载文件")
    @RequestMapping(value = "/doc/file/download", method = RequestMethod.GET)
    @Limit(name = "业务线实例-文件下载", prefix = "limit",limitType= LimitType.TOKEN, operateType = OperateType.ALL, count = 5,period=60,limitHandle = LimitHandleType.NONE)
    public Result<?> docFileDownloadList(@RequestParam("signRuId") String signRuId, @RequestParam("ruDocIdList") List<String> ruDocIdList,
                                         @RequestParam(value = "sendEmail",required = false) String sendEmail,@RequestParam(value = "email",required = false) String email,@RequestParam(value = "remarks",required = false) String remarks, HttpServletResponse response) {
        if (signRuId == null || signRuId.length() == 0) {
            return Result.error("参数缺失");
        }
        SignRu signRu = ruService.getById(signRuId);
        if (signRu == null) {
            return Result.error("数据不存在");
        }
//        SignRuDoc ruDoc = ruDocService.getById(ruDocId);
//        if(ruDoc == null){
//            return Result.error("数据不存在");
//        }
        List<SignRuDocOperate> currentList = ruDocOperateService.listByParamCurrent(signRuId, ruDocIdList);
        if (currentList == null || currentList.size() == 0) {
            return Result.error("数据不存在");
        }
        List<String> annexIdList = currentList.stream().map(SignRuDocOperate::getAnnexId).collect(Collectors.toList());
        if (annexIdList == null || annexIdList.size() == 0) {
            return Result.error("数据不存在");
        }

        String fileName = null;
        File zipFile = null;
        byte[] downloadByte = null;
        OSS ossClient = null;
        if (annexIdList.size() > 1) {
            //批量打包下载
            // 压缩流
            ZipOutputStream zos = null;
            String zipFileName = signRu.getSubject() + System.currentTimeMillis();

            try {
                zipFile = File.createTempFile(zipFileName, ".zip");
                FileOutputStream fos = new FileOutputStream(zipFile);
//                zos = new ZipOutputStream(fos, Charset.forName("GBK"));
                zos = new ZipOutputStream(fos, Charset.forName("UTF-8"));
                Map<String, Integer> fileNameMap = new HashMap<>();
                for (String annexId : annexIdList) {
                    byte[] pdfByte = signFileService.getByteById(annexId);

                    AnnexStorage annex = signFileService.getAnnexById(annexId);
                    if (pdfByte == null || annex == null) {
                        return Result.error("数据不存在");
                    }
                    String realName = annex.getRealName();
                    Integer fileNameCount = null;
                    if (fileNameMap.containsKey(realName)) {
                        fileNameCount = fileNameMap.get(realName);
                        fileNameCount = fileNameCount + 1;
                    } else {
                        fileNameCount = 1;
                    }
                    fileNameMap.put(realName, fileNameCount);
                    if (fileNameCount > 1) {
                        realName = realName.replace(".pdf", "(" + fileNameCount + ").pdf");
                    }
                    byte[] sign = ruBusinessService.signDownloadFile(pdfByte, signRu.getStatus());
                    zos.putNextEntry(new ZipEntry(realName));
                    zos.write(sign);
                    // 当前文件写完，写入下一个文件
                    zos.closeEntry();
                }
                fileName = zipFile.getName();
                zos.close();
                fos.close();
                downloadByte = org.apache.commons.io.IOUtils.toByteArray(new FileInputStream(zipFile));

            } catch (Exception e) {
                return Result.error("下载失败");
            } finally {
                if (ossClient != null) {
                    ossClient.shutdown();
                }
            }
        } else {
            //单独下载
            try {
                byte[] pdfByte = signFileService.getByteById(annexIdList.get(0));

                AnnexStorage annex = signFileService.getAnnexById(annexIdList.get(0));
                if (pdfByte == null || annex == null) {
                    return Result.error("数据不存在");
                }
                fileName = annex.getRealName();
                downloadByte = ruBusinessService.signDownloadFile(pdfByte, signRu.getStatus());

            } catch (Exception e) {
                return Result.error("下载失败");
            } finally {
                if (ossClient != null) {
                    ossClient.shutdown();
                }
            }
        }
        if(fileName == null || downloadByte == null){
            return Result.error("下载失败");
        }
        //发送邮件
        if(sendEmail != null && email != null){

            List<SignRuSigner> signerList = ruSignerService.listByRuId(signRu.getId());
            String senderName  = null;
            String signerName = "";
            for(SignRuSigner ruSigner : signerList){
                if(ruSigner.getSignerType() == SignerTypeEnum.SENDER.getCode()){
                    senderName = ruSigner.getSignerName();
                }else {
                    signerName = signerName + ruSigner.getSignerName() + "," ;
                }
            }
            if(senderName == null){
                return Result.error("下载失败,发起人数据不存在");
            }
            if(signerName != null){
                if(signerName.endsWith(",")){
                    signerName = signerName.substring(0, signerName.length() - 1);
                }
            }
            String key = UUID.randomUUID().toString();
            String code = RandomUtil.randomNumbers(4);
            String realKey = MD5Util.MD5Encode(email + key, "utf-8");
            redisUtil.set(realKey, code, 5 * 60);

            EmailDto emailDto = new EmailDto();
            emailDto.setSendType(SendType.IMMEDIATELY);
            //接收人
            List<String> receivers = new ArrayList<>();
            receivers.add(email);
            emailDto.setReceivers(receivers);
            //模板编号
            emailDto.setTemplateCode("email_contract_file");
            //标题参数
            Map<String, String> titlePara = new HashMap<>();
            titlePara.put("contract", signRu.getSubject());
            emailDto.setTitleParaMap(titlePara);
            //内容参数
            Map<String, String> contentPara = new HashMap<>();
            if(remarks != null){
                contentPara.put("remarks", remarks);
            }else {
                contentPara.put("remarks", "");
            }

            contentPara.put("subject", signRu.getSubject());
            contentPara.put("senderName", senderName);
            if(signerName != null){
                contentPara.put("signerName", signerName);
            }else {
                contentPara.put("signerName", "");
            }
            emailDto.setContentParaMap(contentPara);
            //附件
            Map<String,byte[]> attachmentMap = new HashMap<>();
            attachmentMap.put(fileName,downloadByte);
            emailDto.setAttachments(attachmentMap);

            try {
                sysMessageUtil.asyncSendEmail(emailDto);
            }catch (Exception e){
                return Result.error("发送失败");
            }
            return Result.OK();
        }else {
            //下载文件
            OutputStream outputStream = null;
            try {
                String fileNameencode = URLEncoder.encode(fileName, "UTF-8");

                response.setCharacterEncoding("utf-8");
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment;fileName=" + fileNameencode);
                response.setHeader("fileName", fileNameencode);
                response.setHeader("Access-Control-Expose-Headers", "fileName");

                outputStream = response.getOutputStream();
                org.apache.tomcat.util.http.fileupload.IOUtils.copy(new ByteArrayInputStream(downloadByte), outputStream);
                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return Result.OK();
        }

    }


    // @ApiOperation("业务线实例-查询签署报告是否存在")
    @RequestMapping(value = "/report/file/exist", method = RequestMethod.GET)
    public Result<?> reportFileExist(@RequestParam("signRuId") String signRuId) {

        if (signRuId == null || signRuId.length() == 0) {
            return Result.error("参数缺失");
        }
        SignRu signRu = ruService.getById(signRuId);
        if (signRu == null) {
            return Result.error("数据不存在");
        }
        AnnexStorage annex = signFileService.findByFatherIdAndDataCategory(SignFileEnum.SIGN_FILE_REPORT, signRu.getId());
        if (annex != null) {
            //有报告
            return Result.OK(1);
        } else {
            //无报告
            return Result.OK(0);
        }

    }

    // @ApiOperation("业务线实例-下载签署报告")
    @RequestMapping(value = "/report/file/download", method = RequestMethod.GET)
    @Limit(name = "业务线实例-下载签署报告", prefix = "limit",limitType= LimitType.TOKEN, operateType = OperateType.ALL, count = 1,period=30,limitHandle = LimitHandleType.NONE)
    public Result<?> reportFileDownload(@RequestParam("signRuId") String signRuId, HttpServletResponse response) {

        if (signRuId == null || signRuId.length() == 0) {
            return Result.error("参数缺失");
        }
        SignRu signRu = ruService.getById(signRuId);
        if (signRu == null) {
            return Result.error("数据不存在");
        }
        byte[] reportByte = null;
        String fileName = null;
        AnnexStorage annex = signFileService.findByFatherIdAndDataCategory(SignFileEnum.SIGN_FILE_REPORT, signRu.getId());
        if (annex != null) {
            reportByte = signFileService.getByteById(annex.getId());
            fileName = annex.getRealName();
        }
        if (reportByte == null || fileName == null) {
            return Result.error("数据不存在");
        }
        //下载文件
        OutputStream outputStream = null;
        try {
            String fileNameencode = URLEncoder.encode(fileName, "UTF-8");

            response.setCharacterEncoding("utf-8");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileNameencode);
            response.setHeader("fileName", fileNameencode);
            response.setHeader("Access-Control-Expose-Headers", "fileName");

            outputStream = response.getOutputStream();
            org.apache.tomcat.util.http.fileupload.IOUtils.copy(new ByteArrayInputStream(reportByte), outputStream);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.OK();
    }


}