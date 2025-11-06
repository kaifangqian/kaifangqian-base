/**
 * @description 用户授权开通电子签服务
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
package com.kaifangqian.external.auth.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.kaifangqian.external.auth.request.*;
import com.kaifangqian.external.base.CommonRequest;
import com.kaifangqian.external.base.CommonResult;
import com.kaifangqian.modules.api.util.SignHeadersGenerator;
import com.kaifangqian.modules.cert.service.HttpUtils;
import com.kaifangqian.modules.opensign.enums.PersonalSealTypeEnum;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.enums.TenantAuthStatus;
import com.kaifangqian.modules.system.enums.TenantAuthType;
import com.kaifangqian.modules.system.enums.TenantType;
import com.kaifangqian.modules.system.service.*;
import com.kaifangqian.common.constant.YundunApiCode;
import com.kaifangqian.common.redis.util.RedisUtil;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.util.RsaUtils;
import com.kaifangqian.common.util.oConvertUtils;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.external.auth.service.IdentityAuthExternal;
import com.kaifangqian.external.auth.request.*;
import com.kaifangqian.external.auth.response.IdentityAuthInfoForGetResponse;
import com.kaifangqian.external.auth.response.IdentityAuthInfoForQueryResponse;
import com.kaifangqian.external.auth.response.IdentityAuthResponse;
import com.kaifangqian.external.enums.YunDunAuthStatus;
import com.kaifangqian.external.enums.YundunAuthUserType;
import com.kaifangqian.modules.opensign.constant.SignCommonConstant;
import com.kaifangqian.modules.opensign.entity.SignPersonSeal;
import com.kaifangqian.modules.opensign.enums.SealColorEnum;
import com.kaifangqian.modules.opensign.enums.SealStatusEnum;
import com.kaifangqian.modules.opensign.enums.SignFileEnum;
import com.kaifangqian.modules.opensign.service.business.EntSealBusinessService;
import com.kaifangqian.modules.opensign.service.business.SealStatusBusinessService;
import com.kaifangqian.modules.opensign.service.flow.IFlowService;
import com.kaifangqian.modules.opensign.service.seal.SignPersonSealService;
import com.kaifangqian.modules.opensign.service.tool.EntSealGenerateService;
import com.kaifangqian.modules.opensign.service.tool.PsersonSealGenerateService;
import com.kaifangqian.modules.opensign.service.tool.SignFileService;
import com.kaifangqian.modules.opensign.utils.Base64;
import com.kaifangqian.modules.opensign.vo.base.BusinessAuthVo;
import com.kaifangqian.modules.opensign.vo.request.EntSealSaveRequest;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.service.*;
import com.kaifangqian.utils.DateUtil;
import com.kaifangqian.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 * @author : yxb
 * create at: 2025/6/6
 */
@Service
@Slf4j
public class IdentityAuthExternalImpl implements IdentityAuthExternal {

    @Autowired
    private ITenantInfoExtendService tenantInfoExtendService;

    @Autowired
    private ISysTenantUserService sysTenantUserService;

    @Autowired
    private ISysTenantInfoService sysTenantInfoService;

    @Autowired
    private ISysDepartService sysDepartService;

    @Autowired
    private ITenantAuthLogService tenantAuthLogService;

    @Autowired
    private SealStatusBusinessService sealStatusBusinessService;

    @Autowired
    private SignPersonSealService personSealService;

    @Autowired
    private PsersonSealGenerateService psersonSealGenerateService;

    @Autowired
    private EntSealGenerateService entSealGenerateService;
    @Autowired
    private SignFileService signFileService ;

    @Autowired
    private EntSealBusinessService sealBusinessService ;

    @Autowired
    private IFlowService flowService;

    @Autowired
    private ISysUserService sysUserService ;

    @Autowired
    private ISysAppInfoService sysAppInfoService;

    @Autowired
    private RedisUtil redisUtil;

    @Value("${service.app-id}")
    private String appId ;

    @Value("${service.yundun-service-private-key}")
    private String privateKey ;


    @Value("${service.manage.yundun-auth-personal-url}")
    private String yundunPersonalAuthUrl ;

    @Value("${service.manage.yundun-auth-personal-update-url}")
    private String yundunPersonalAuthUpdateUrl ;

    @Value("${service.manage.yundun-auth-company-url}")
    private String yundunCompanyAuthUrl ;

    @Value("${service.manage.yundun-auth-company-update-url}")
    private String yundunCompanyAuthUpdateUrl ;

    @Value("${service.manage.yundun-auth-query-info-url}")
    private String yundunQueryAuthInfoUrl;

    @Value("${service.manage.yundun-auth-get-info-url}")
    private String getYundunGetAuthInfoUrl;

    @Override
    public IdentityAuthResponse personalIdentityAuth(String callbackPage) throws Exception {

        // 返回接口信息
        CommonResult<IdentityAuthResponse> result = null;
        // 构建市民认证请求对象
        IdentityAuthResponse identityAuthResponse = new IdentityAuthResponse();
        // 获取当前登录用户
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        // 获取当前登录租户信息
        TenantInfoExtend tenantInfoExtend = tenantInfoExtendService.getTenantInfoByTenantId(loginUser.getTenantId());
        // 获取当前用户信息
        SysUser sysSignUser = sysTenantUserService.getTenantSysUser(loginUser.getTenantUserId());

        // 租户认证记录表
        if(tenantInfoExtend != null && tenantInfoExtend.getTenantType() != TenantType.PERSONAL.getType()){

            String operatorUnionId = sysTenantUserService.getPersonalTenantUser(loginUser.getId()).getTenantId();
            tenantInfoExtend = tenantInfoExtendService.getTenantInfoByTenantId(operatorUnionId);

            if(tenantInfoExtend == null){
                identityAuthResponse.setAuthStatus(-1);
                identityAuthResponse.setResultMessage("用户类型不匹配，当前企业用户不能使用个人实名认证功能");
                return identityAuthResponse;
            }
        }

        if(tenantInfoExtend != null && tenantInfoExtend.getAuthStatus() == TenantAuthStatus.STATUS2.getStatus()){
            identityAuthResponse.setAuthStatus(1);
            identityAuthResponse.setResultMessage("用户已实名，无需再次实名");
            return identityAuthResponse;
        }
        else if(tenantInfoExtend != null && tenantInfoExtend.getAuthStatus() == TenantAuthStatus.STATUS0.getStatus()){

            PersonalAuthInfoRequest personalAuthInfoRequest = new PersonalAuthInfoRequest();
            personalAuthInfoRequest.setUnionId(tenantInfoExtend.getTenantId());
            personalAuthInfoRequest.setAccount(sysSignUser.getUsername());
            // TODO 配置回调地址

            String token = MD5Util.MD5Encode(SignCommonConstant.PERSONALAUTHREGISTER+tenantInfoExtend.getTenantId(),"UTF-8");
            redisUtil.set(token,callbackPage);
            redisUtil.expire(token, SignCommonConstant.TWO_DAY);

            SysAppInfo sysAppInfo = sysAppInfoService.getById("490489ab-d8b4-414c-ad77-d856962c286f");
            String ydCallbackUrl = "";
            if (sysAppInfo != null) {
                ydCallbackUrl = sysAppInfo.getAppAddress() + "/#/callbackpage?state=" + token ;
            }

            personalAuthInfoRequest.setCallbackPage(ydCallbackUrl);
            personalAuthInfoRequest.setAuthCallbackUrl(sysAppInfo.getAppAddress() +"/resrun-paas/yundun/auth/callback");

            // 实名认证信息
            PersonalIdIdentInfo.PersonalIdIdentInfoBuilder builder = PersonalIdIdentInfo.builder()
                    .name(tenantInfoExtend.getName());

            if (oConvertUtils.isNotEmpty(tenantInfoExtend.getOrganizationNo())) {
                String decryptedOrgNo = RsaUtils.decryptByPrivateKey(SignCommonConstant.PRIVATEKEYSTRING, tenantInfoExtend.getOrganizationNo());// 解密组织编号
                builder.idCard(decryptedOrgNo);
            }

            if (oConvertUtils.isNotEmpty(tenantInfoExtend.getPhone())) {
                String decryptedPhone = RsaUtils.decryptByPrivateKey(SignCommonConstant.PRIVATEKEYSTRING, tenantInfoExtend.getPhone()); // 解密手机号
                builder.mobile(decryptedPhone);
            }

            PersonalIdIdentInfo personalIdIdentInfo = builder.build();

            personalAuthInfoRequest.setUserIdentInfo(personalIdIdentInfo);

            // 构建请求参数
            JSONObject params = (JSONObject)JSONObject.toJSON(personalAuthInfoRequest);

            String uri = "/yundun/api/v1/auth/person/link";

            // 构建请求头
            Map<String,String> headers = SignHeadersGenerator.generateSignHeaders(appId, params.toJSONString(), uri, privateKey);
            // 请求云盾实名认证
            String returnJson = null ;
            try {
                returnJson = HttpUtils.executePost(yundunPersonalAuthUrl, params.toJSONString(), headers);
                log.info("{} auth result:{}",loginUser.getRealname(),returnJson);
                result = JSONObject.parseObject(returnJson,new TypeReference<CommonResult<IdentityAuthResponse>>(){});
            }catch (Exception e) {
                log.error("{} http error：",yundunPersonalAuthUrl,e);
            }
            if (result != null){
                // 查询租户实名认证记录
                if (result.getCode().equals(YundunApiCode.SUCCESS.getCode())){
                    TenantAuthLog tenantAuthLogNew = tenantAuthLogService.getAuthLogByOrderNo(tenantInfoExtend.getTenantId(),result.getResult().getOrderNo());
                    if(tenantAuthLogNew == null){
                        tenantAuthLogNew = new TenantAuthLog();
                        tenantAuthLogNew.setTenantId(tenantInfoExtend.getTenantId());
                        tenantAuthLogNew.setExtendId(tenantInfoExtend.getId());
                        tenantAuthLogNew.setApplyTenantUser(loginUser.getTenantUserId());
                        tenantAuthLogNew.setOrderNo(result.getResult().getOrderNo());
                        tenantAuthLogNew.setYundunAuthUrl(result.getResult().getAuthUrl());
                        tenantAuthLogNew.setAuthStatus(TenantAuthStatus.STATUS0.getStatus());
                        tenantAuthLogNew.setAuthType(1);
                        tenantAuthLogService.save(tenantAuthLogNew);
                    }
                    identityAuthResponse.setAuthStatus(0);
                    identityAuthResponse.setAuthUrl(result.getResult().getAuthUrl());
                    identityAuthResponse.setOrderNo(result.getResult().getOrderNo());
                }else if(result.getCode().equals(YundunApiCode.AUTH_ALREADY_VERIFIED.getCode())){
                    // 如果认证通过重新查询并处理实名认证结果
                    if(tenantInfoExtend.getAuthStatus() == TenantAuthStatus.STATUS0.getStatus()){
                        identityAuthResponse.setAuthStatus(1);
                        identityAuthResponse.setResultMessage(result.getMessage()+"请联系统管理员，同步实名认证记录");
                    }else{
                        identityAuthResponse.setAuthStatus(1);
                        identityAuthResponse.setResultMessage(result.getMessage());
                    }
                }
            }
        }
        return identityAuthResponse;
    }

    @Override
    public Result<?> queryIdentityAuthInfo(String orderNo) throws Exception {
        Result<?> updateResult = null;

        String uri = "/yundun/api/v1/auth/order/info?orderNo="+orderNo;
        String queryIdentityAuthUrl = yundunQueryAuthInfoUrl+"?orderNo="+orderNo;

        // 构建请求头
        Map<String,String> headers = SignHeadersGenerator.generateSignHeaders(appId, null, uri, privateKey);

        String returnJson = null ;
        try {
            returnJson = HttpUtils.executeGetMap(queryIdentityAuthUrl, null, headers);
            log.info("Query auth info result: {}", returnJson);
            CommonResult<AuthCallbackRequest> result = JSONObject.parseObject(returnJson, new TypeReference<CommonResult<AuthCallbackRequest>>() {});
            if (result != null && result.getCode().equals(YundunApiCode.SUCCESS.getCode())) {
                AuthCallbackRequest callbackRequest = result.getResult();
                // 处理回调请求，更新租户信息
                updateResult = updateIdentityAuth(callbackRequest);
                if (!"success".equals(updateResult.getMessage())) {
                    log.error(updateResult.getMessage()+"{}", callbackRequest.getOrderNo());
                }
                return updateResult;
            } else {
                log.error("未查询到用户实名认证信息. Response: {}", returnJson);
            }
        } catch (Exception e) {
            log.error("Error occurred while querying identity auth info.", e);
        }
        return updateResult;
    }

    @Override
    public Result<?> getIdentityAuthInfo(String unionId) throws Exception {
        Result<?> updateResult = null;

        String queryIdentityAuthUrl = getYundunGetAuthInfoUrl+"?unionId="+unionId;

        // 构建请求头
        Map<String,String> headers = SignHeadersGenerator.generateSignHeaders(appId, null, queryIdentityAuthUrl, privateKey);

        String returnJson = null ;
        try {
            returnJson = HttpUtils.executeGetMap(queryIdentityAuthUrl, null, headers);
            log.info("Query auth info result: {}", returnJson);
            CommonResult<AuthCallbackRequest> result = JSONObject.parseObject(returnJson, new TypeReference<CommonResult<AuthCallbackRequest>>() {});
            if (result != null && result.getCode().equals(YundunApiCode.SUCCESS.getCode())) {
                AuthCallbackRequest callbackRequest = result.getResult();
                // 处理回调请求，更新租户信息
                updateResult = updateIdentityAuth(callbackRequest);
                if (!"success".equals(updateResult.getMessage())) {
                    log.error(updateResult.getMessage()+"{}", callbackRequest.getOrderNo());
                }
                return updateResult;
            } else {
                log.error("未查询到用户实名认证信息. Response: {}", returnJson);
            }
        } catch (Exception e) {
            log.error("Error occurred while querying identity auth info.", e);
        }
        return updateResult;
    }

    @Override
    public IdentityAuthResponse personalIdentityAuthUpdate(String callbackPage) throws Exception {

        // 返回接口信息
        CommonResult<IdentityAuthResponse> result = null;

        IdentityAuthResponse identityAuthResponse = new IdentityAuthResponse();

        // 获取当前登录用户
        LoginUser loginUser = MySecurityUtils.getCurrentUser();

        // 获取当前登录租户信息
        TenantInfoExtend tenantInfoExtend = tenantInfoExtendService.getTenantInfoByTenantId(loginUser.getTenantId());

        // 获取当前用户信息
        SysUser sysSignUser = sysTenantUserService.getTenantSysUser(loginUser.getTenantUserId());

        if(tenantInfoExtend != null && tenantInfoExtend.getTenantType() != TenantType.PERSONAL.getType()){
            identityAuthResponse.setAuthStatus(-1);
            identityAuthResponse.setResultMessage("用户类型不匹配，当前企业用户不能使用个人实名认证功能");
            return identityAuthResponse;
        }else if(tenantInfoExtend != null && tenantInfoExtend.getAuthStatus() == TenantAuthStatus.STATUS2.getStatus()){
            PersonalAuthUpdateInfoRequest personalAuthUpdateInfoRequest = new PersonalAuthUpdateInfoRequest();

            personalAuthUpdateInfoRequest.setUnionId(tenantInfoExtend.getTenantId());

            // TODO 配置回调地址
            String token = MD5Util.MD5Encode(SignCommonConstant.PERSONALAUTHUPDATE+tenantInfoExtend.getTenantId(),"UTF-8");
            redisUtil.set(token,callbackPage);
            redisUtil.expire(token, SignCommonConstant.TWO_DAY);

            SysAppInfo sysAppInfo = sysAppInfoService.getById("490489ab-d8b4-414c-ad77-d856962c286f");
            String ydCallbackUrl = "";
            if (sysAppInfo != null) {
                ydCallbackUrl = sysAppInfo.getAppAddress() + "/#/callbackpage?state=" + token ;
            }
            personalAuthUpdateInfoRequest.setCallbackPage(ydCallbackUrl);
            personalAuthUpdateInfoRequest.setAuthCallbackUrl(sysAppInfo.getAppAddress() +"/resrun-paas/yundun/auth/callback");

            // 构建请求参数
            JSONObject params = (JSONObject)JSONObject.toJSON(personalAuthUpdateInfoRequest);

            // 构建请求头
            String uri = "/yundun/api/v1/auth/person/update/link";
            Map<String,String> headers = SignHeadersGenerator.generateSignHeaders(appId, params.toJSONString(), uri, privateKey);

            String returnJson = null ;
            try {
                returnJson = HttpUtils.executePost(yundunPersonalAuthUpdateUrl, params.toJSONString(), headers);
                log.info("{} auth result:{}",loginUser.getRealname(),returnJson);
                result = JSONObject.parseObject(returnJson,new TypeReference<CommonResult<IdentityAuthResponse>>(){});

                if(result != null && result.getCode().equals(YundunApiCode.SUCCESS.getCode())){
                    identityAuthResponse = result.getResult();
                }

            }catch (Exception e) {
                log.error("{} http error：",yundunPersonalAuthUrl,e);
            }
            if (result != null) {
                // 查询租户实名认证记录
                if (result.getCode().equals(YundunApiCode.SUCCESS.getCode())){
                    TenantAuthLog tenantAuthLogNew = tenantAuthLogService.getAuthLogByOrderNo(tenantInfoExtend.getTenantId(),result.getResult().getOrderNo());

                    if(tenantAuthLogNew == null){
                        tenantAuthLogNew = new TenantAuthLog();
                        tenantAuthLogNew.setTenantId(tenantInfoExtend.getTenantId());
                        tenantAuthLogNew.setExtendId(tenantInfoExtend.getId());
                        tenantAuthLogNew.setApplyTenantUser(loginUser.getTenantUserId());
                        tenantAuthLogNew.setOrderNo(result.getResult().getOrderNo());
                        tenantAuthLogNew.setYundunAuthUrl(result.getResult().getAuthUrl());
                        tenantAuthLogNew.setAuthStatus(TenantAuthStatus.STATUS0.getStatus());
                        tenantAuthLogNew.setAuthType(2);
                        tenantAuthLogService.save(tenantAuthLogNew);
                    }

                    identityAuthResponse.setAuthStatus(0);
                    identityAuthResponse.setAuthUrl(result.getResult().getAuthUrl());
                    identityAuthResponse.setOrderNo(result.getResult().getOrderNo());
                }else if(result.getCode().equals(YundunApiCode.AUTH_NO_SUBJECT.getCode())){
                    // 如果认证通过重新查询并处理实名认证结果
                    identityAuthResponse.setAuthStatus(1);
                    identityAuthResponse.setResultMessage(result.getMessage());
                }
            }

        }else if(tenantInfoExtend != null && tenantInfoExtend.getAuthStatus() == TenantAuthStatus.STATUS0.getStatus()){
            identityAuthResponse.setAuthUrl(callbackPage);
            identityAuthResponse.setResultMessage("主体信息未实名认证，无法进行认证变更");
        }
        return identityAuthResponse;
    }

    @Override
    public IdentityAuthResponse companyIdentityAuth(String callbackPage) throws Exception {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();

        // 返回接口信息
        CommonResult<IdentityAuthResponse> result = null;
        // 构建企业实名返回信息
        IdentityAuthResponse identityAuthResponse = new IdentityAuthResponse();
        // 获取当前登录企业租户信息
        TenantInfoExtend tenantInfoExtend = tenantInfoExtendService.getTenantInfoByTenantId(loginUser.getTenantId());
        // 获取当前用户信息
        SysTenantUser personalTenantUser = sysTenantUserService.getPersonalTenantUser(loginUser.getId());

        // 租户认证记录表
        if(tenantInfoExtend != null && tenantInfoExtend.getTenantType() != TenantType.GROUP.getType()){
            identityAuthResponse.setAuthStatus(-1);
            identityAuthResponse.setResultMessage("用户类型不匹配，当前个人用户不能使用企业实名认证功能");
            return identityAuthResponse;
        }
        else if(tenantInfoExtend != null && tenantInfoExtend.getAuthStatus() == TenantAuthStatus.STATUS2.getStatus()){
            identityAuthResponse.setAuthStatus(1);
            identityAuthResponse.setResultMessage("该用户已实名，无需再次实名");
            return identityAuthResponse;
        }
        else if(tenantInfoExtend != null && tenantInfoExtend.getAuthStatus() == TenantAuthStatus.STATUS0.getStatus()){
            CompanyAuthInfoRequest companyAuthInfoRequest = new CompanyAuthInfoRequest();
            companyAuthInfoRequest.setUnionId(tenantInfoExtend.getTenantId());
            companyAuthInfoRequest.setContactUnionId(personalTenantUser.getTenantId());
            // TODO 配置回调地址

            String token = MD5Util.MD5Encode(SignCommonConstant.COMPANYAUTHREGISTER+tenantInfoExtend.getTenantId(),"UTF-8");
            redisUtil.set(token,callbackPage);
            redisUtil.expire(token, SignCommonConstant.TWO_DAY);

            SysAppInfo sysAppInfo = sysAppInfoService.getById("490489ab-d8b4-414c-ad77-d856962c286f");
            String ydCallbackUrl = "";
            if (sysAppInfo != null) {
                ydCallbackUrl = sysAppInfo.getAppAddress() + "/#/callbackpage?state=" + token ;
            }

            companyAuthInfoRequest.setCallbackPage(ydCallbackUrl);
            companyAuthInfoRequest.setAuthCallbackUrl(sysAppInfo.getAppAddress() +"/resrun-paas/yundun/auth/callback");

            CompanyIdentInfo companyIdentInfo = new CompanyIdentInfo();
            companyIdentInfo.setCompanyName(tenantInfoExtend.getName());
            if(oConvertUtils.isNotEmpty(tenantInfoExtend.getOrganizationNo())) {
                String decryptedOrgNo = RsaUtils.decryptByPrivateKey(SignCommonConstant.PRIVATEKEYSTRING, tenantInfoExtend.getOrganizationNo());// 解密组织编号
                companyIdentInfo.setCreditCode(decryptedOrgNo);
            }
            companyIdentInfo.setLegalRepName(tenantInfoExtend.getCorporation());

            companyAuthInfoRequest.setCompanyIdentInfo(companyIdentInfo);

            JSONObject params = (JSONObject)JSONObject.toJSON(companyAuthInfoRequest);

            // 生成请求签名头信息
            String uri = "/yundun/api/v1/auth/company/link";
            Map<String,String> headers = SignHeadersGenerator.generateSignHeaders(appId, params.toJSONString(), uri, privateKey);

            String returnJson = null ;
            try {
                returnJson = HttpUtils.executePost(yundunCompanyAuthUrl, params.toJSONString(), headers);
                log.info("{} auth result:{}",tenantInfoExtend.getName(),returnJson);
                result = JSONObject.parseObject(returnJson,new TypeReference<CommonResult<IdentityAuthResponse>>(){});
            }catch (Exception e) {
                log.error("{} http error：",yundunPersonalAuthUrl,e);
            }

            if (result != null){
                if (result.getCode().equals(YundunApiCode.SUCCESS.getCode())){

                    TenantAuthLog tenantAuthLogNew = tenantAuthLogService.getAuthLogByOrderNo(tenantInfoExtend.getTenantId(),result.getResult().getOrderNo());

                    if(tenantAuthLogNew == null){
                        tenantAuthLogNew = new TenantAuthLog();
                        tenantAuthLogNew.setTenantId(tenantInfoExtend.getTenantId());
                        tenantAuthLogNew.setExtendId(tenantInfoExtend.getId());
                        tenantAuthLogNew.setApplyTenantUser(loginUser.getTenantUserId());
                        tenantAuthLogNew.setOrderNo(result.getResult().getOrderNo());
                        tenantAuthLogNew.setYundunAuthUrl(result.getResult().getAuthUrl());
                        tenantAuthLogNew.setAuthStatus(TenantAuthStatus.STATUS0.getStatus());
                        tenantAuthLogNew.setAuthType(1);
                        tenantAuthLogService.save(tenantAuthLogNew);
                    }

                    identityAuthResponse.setAuthStatus(0);
                    identityAuthResponse.setAuthUrl(result.getResult().getAuthUrl());
                    identityAuthResponse.setOrderNo(result.getResult().getOrderNo());

                }else if(result.getCode().equals(YundunApiCode.AUTH_ALREADY_VERIFIED.getCode())){

                    if(tenantInfoExtend.getAuthStatus() == TenantAuthStatus.STATUS0.getStatus()){
                        identityAuthResponse.setAuthStatus(1);
                        identityAuthResponse.setResultMessage(result.getMessage()+"请联系统系管理员，同步实名认证记录");
                    }else{
                        identityAuthResponse.setAuthStatus(1);
                        identityAuthResponse.setResultMessage(result.getMessage());
                    }
                }else if(result.getCode().equals(YundunApiCode.AUTH_NO_AGENT.getCode())){
                    // 经办人未认证返回错误信息
                    identityAuthResponse.setAuthStatus(1);
                    identityAuthResponse.setResultMessage("为保证企业认证真实有效，请先完成个人实名认证");
                }
            }
        }

        return identityAuthResponse;
    }

    @Override
    public IdentityAuthResponse companyIdentityAuthUpdate(String callbackPage) throws Exception {

        LoginUser loginUser = MySecurityUtils.getCurrentUser();

        // 返回接口信息
        CommonResult<IdentityAuthResponse> result = null;

        IdentityAuthResponse identityAuthResponse = new IdentityAuthResponse();

        // 获取当前登录租户信息
        TenantInfoExtend tenantInfoExtend = tenantInfoExtendService.getTenantInfoByTenantId(loginUser.getTenantId());

        // 获取当前用户信息
        SysTenantUser personalTenantUser = sysTenantUserService.getPersonalTenantUser(loginUser.getId());

        // 租户认证记录表
        if(tenantInfoExtend != null && tenantInfoExtend.getTenantType() != TenantType.GROUP.getType()){
            identityAuthResponse.setAuthStatus(-1);
            identityAuthResponse.setResultMessage("用户类型不匹配，当前个人用户不能使用企业实名认证功能");
            return identityAuthResponse;
        }
        else if(tenantInfoExtend != null && tenantInfoExtend.getAuthStatus() == TenantAuthStatus.STATUS2.getStatus()){
            CompanyAuthUpdateInfoRequest companyAuthInfoRequest = new CompanyAuthUpdateInfoRequest();
            companyAuthInfoRequest.setUnionId(tenantInfoExtend.getTenantId());
            companyAuthInfoRequest.setContactUnionId(personalTenantUser.getTenantId());

            String token = MD5Util.MD5Encode(SignCommonConstant.COMPANYAUTHUPDATE+tenantInfoExtend.getTenantId(),"UTF-8");
            redisUtil.set(token,callbackPage);
            redisUtil.expire(token, SignCommonConstant.TWO_DAY);

            SysAppInfo sysAppInfo = sysAppInfoService.getById("490489ab-d8b4-414c-ad77-d856962c286f");
            String ydCallbackUrl = "";
            if (sysAppInfo != null) {
                ydCallbackUrl = sysAppInfo.getAppAddress() + "/#/callbackpage?state=" + token ;
            }
            companyAuthInfoRequest.setCallbackPage(ydCallbackUrl);
            companyAuthInfoRequest.setAuthCallbackUrl(sysAppInfo.getAppAddress() +"/resrun-paas/yundun/auth/callback");

            JSONObject params = (JSONObject)JSONObject.toJSON(companyAuthInfoRequest);

            // 构建请求头
            String uri = "/yundun/api/v1/auth/company/update/link";
            Map<String,String> headers = SignHeadersGenerator.generateSignHeaders(appId, params.toJSONString(), uri, privateKey);

            String returnJson = null ;
            try {
                returnJson = HttpUtils.executePost(yundunCompanyAuthUpdateUrl, params.toJSONString(), headers);
                log.info("{} auth result:{}",loginUser.getRealname(),returnJson);
                result = JSONObject.parseObject(returnJson,new TypeReference<CommonResult<IdentityAuthResponse>>(){});
            }catch (Exception e) {
                log.error("{} http error：",yundunCompanyAuthUpdateUrl,e);
            }

            if (result != null){
                if (result.getCode().equals(YundunApiCode.SUCCESS.getCode())){

                    TenantAuthLog tenantAuthLogNew = tenantAuthLogService.getAuthLogByOrderNo(tenantInfoExtend.getTenantId(),result.getResult().getOrderNo());

                    if(tenantAuthLogNew == null){
                        tenantAuthLogNew = new TenantAuthLog();
                        tenantAuthLogNew.setTenantId(tenantInfoExtend.getTenantId());
                        tenantAuthLogNew.setExtendId(tenantInfoExtend.getId());
                        tenantAuthLogNew.setApplyTenantUser(loginUser.getTenantUserId());
                        tenantAuthLogNew.setOrderNo(result.getResult().getOrderNo());
                        tenantAuthLogNew.setYundunAuthUrl(result.getResult().getAuthUrl());
                        tenantAuthLogNew.setAuthStatus(TenantAuthStatus.STATUS0.getStatus());
                        tenantAuthLogNew.setAuthType(2);
                        tenantAuthLogService.save(tenantAuthLogNew);
                    }

                    identityAuthResponse.setAuthStatus(0);
                    identityAuthResponse.setAuthUrl(result.getResult().getAuthUrl());
                    identityAuthResponse.setOrderNo(result.getResult().getOrderNo());
                }else if(result.getCode().equals(YundunApiCode.AUTH_NO_SUBJECT.getCode())){
                    // 如果认证通过重新查询并处理实名认证结果
                    identityAuthResponse.setAuthStatus(1);
                    identityAuthResponse.setResultMessage(result.getMessage());
                }else if(result.getCode().equals(YundunApiCode.AUTH_INVALID_AGENT.getCode())){
                    // 如果认证通过重新查询并处理实名认证结果
                    identityAuthResponse.setAuthStatus(1);
                    identityAuthResponse.setResultMessage(result.getMessage());
                }
            }
        }else if(tenantInfoExtend != null && tenantInfoExtend.getAuthStatus() == TenantAuthStatus.STATUS0.getStatus()){
            identityAuthResponse.setAuthUrl(callbackPage);
            identityAuthResponse.setResultMessage("主体信息未实名认证，无法进行认证变更");
        }

        return identityAuthResponse;
    }

    @Override
    public CommonResult<IdentityAuthInfoForQueryResponse> queryIdentityAuthInfo(CommonRequest<AuthOrderInfoRequest> request) {
        return null;
    }

    @Override
    public CommonResult<IdentityAuthInfoForGetResponse> getIdentityAuthInfo(CommonRequest<AuthOrderInfoRequest> request) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> updateIdentityAuth(AuthCallbackRequest request) throws Exception {
        TenantInfoExtend tenantInfoExtend = tenantInfoExtendService.getTenantInfoByTenantId(request.getUnionId());
        if(tenantInfoExtend != null && tenantInfoExtend.getAuthStatus() == TenantAuthStatus.STATUS0.getStatus() && request != null && request.getOrderStatus() == YunDunAuthStatus.SUCCESS.getType() && request.getBizType() == YunDunAuthStatus.FRISTVERIFY.getType()){
            if(request.getOrderStatus() == YunDunAuthStatus.SUCCESS.getType()){
                TenantAuthLog tenantAuthLog = tenantAuthLogService.getAuthLogByOrderNo(request.getUnionId(),request.getOrderNo());
                if(tenantAuthLog != null && tenantAuthLog.getAuthStatus() == TenantAuthStatus.STATUS2.getStatus()){
                    return Result.OK("success");
                }
            }
            updateTenantInfo(tenantInfoExtend,request);
            return Result.OK("success");
        }
        else if(tenantInfoExtend != null && tenantInfoExtend.getAuthStatus() == TenantAuthStatus.STATUS2.getStatus() && request != null && request.getOrderStatus() == YunDunAuthStatus.SUCCESS.getType() && request.getBizType() == YunDunAuthStatus.UPDATE.getType()){
            if(request.getOrderStatus() == YunDunAuthStatus.SUCCESS.getType()){
                TenantAuthLog tenantAuthLog = tenantAuthLogService.getAuthLogByOrderNo(request.getUnionId(),request.getOrderNo());
                if(tenantAuthLog != null && tenantAuthLog.getAuthStatus() == TenantAuthStatus.STATUS2.getStatus()){
                    return Result.OK("success");
                }
            }
            updateTenantInfo(tenantInfoExtend,request);
            return Result.OK("success");
        }
        else if(tenantInfoExtend != null && (request.getOrderStatus() == YunDunAuthStatus.INVALID.getType() || request.getOrderStatus() == YunDunAuthStatus.CANCELED.getType() || request.getOrderStatus() == YunDunAuthStatus.PROCESSING.getType())){
            TenantAuthLog tenantAuthLog = tenantAuthLogService.getAuthLogByOrderNo(request.getUnionId(),request.getOrderNo());
            tenantAuthLog.setOrderStatus(request.getOrderStatus());
            tenantAuthLogService.updateById(tenantAuthLog);
            return Result.OK("success");
        }else {
            return Result.error("未查询到实名认证信息");
        }
    }

    /**
     * 更新租户实名认证信息
     * @param tenantInfoExtend
     * @param request
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    private void updateTenantInfo(TenantInfoExtend tenantInfoExtend, AuthCallbackRequest request) throws Exception {
        TenantAuthLog tenantAuthLogNew = tenantAuthLogService.getAuthLogByOrderNo(tenantInfoExtend.getTenantId(),request.getOrderNo());
        SysTenantInfo sysTenantInfo = sysTenantInfoService.getById(tenantInfoExtend.getTenantId());
        SysDepart sysDepart = sysDepartService.getByTenantId(tenantInfoExtend.getTenantId());
        SysTenantUser sysTenantUser = sysTenantUserService.getById(tenantAuthLogNew.getApplyTenantUser());
        SysUser sysUser = sysTenantUserService.getTenantSysUser(sysTenantUser.getId());
        if(tenantAuthLogNew == null){
            tenantAuthLogNew = new TenantAuthLog();
        }

        if(request.getUserType() == YundunAuthUserType.PERSONAL.getType()){
            tenantInfoExtend.setName(request.getName());
            sysTenantInfo.setTenantName(request.getName());
            tenantAuthLogNew.setName(request.getName());
            tenantInfoExtend.setTenantType(TenantType.PERSONAL.getType());
            tenantAuthLogNew.setTenantType(TenantType.PERSONAL.getType());
            tenantInfoExtend.setOrganizationNo(RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING,request.getIdCard()));
            tenantAuthLogNew.setOrganizationNo(RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING,request.getIdCard()));
            tenantAuthLogNew.setCreateBy(request.getUnionId());
            tenantAuthLogNew.setPersonVerifyMethod(request.getPersonVerifyMethod());
            if(request.getBizType() == TenantAuthType.CHANGE.getType()){
                sealStatusBusinessService.changePersonSealStatus(tenantInfoExtend.getTenantId(), SealStatusEnum.UN_ENABLE);
                generatePersonDefaultSeal(request.getName(),sysDepart,tenantInfoExtend);
            }else if(request.getBizType() == TenantAuthType.CREATED.getType()){
                generatePersonDefaultSeal(request.getName(),sysDepart,tenantInfoExtend);
            }
            sysUser.setRealname(request.getName());
            sysUserService.updateById(sysUser);
        }else if (request.getUserType() == YundunAuthUserType.GROUP.getType()){
            sysTenantInfo.setTenantName(request.getCompanyName());
            tenantAuthLogNew.setName(request.getCompanyName());
            tenantInfoExtend.setTenantType(TenantType.GROUP.getType());
            tenantAuthLogNew.setTenantType(TenantType.GROUP.getType());
            tenantInfoExtend.setOrganizationNo(RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING,request.getCreditCode()));
            tenantAuthLogNew.setOrganizationNo(RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING,request.getCreditCode()));
            tenantInfoExtend.setCorporation(request.getName());
            tenantAuthLogNew.setCorporation(request.getName());
            tenantInfoExtend.setCorporationNo(RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING,request.getIdCard()));
            tenantAuthLogNew.setCorporationNo(RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING,request.getIdCard()));
            tenantInfoExtend.setSysType(String.valueOf(request.getCompanyType()));
            tenantAuthLogNew.setCreateBy(request.getContactUnionId());
            tenantAuthLogNew.setCompanyVerifyMethod(request.getCompanyVerifyMethod());
            if(sysDepart != null){
                sysDepart.setDepartName(request.getCompanyName());
                sysDepartService.updateById(sysDepart);
            }
            if(request.getBizType() == TenantAuthType.CHANGE.getType() && !request.getCompanyName().equals(tenantInfoExtend.getName())){
                sealStatusBusinessService.disableEntSeal(tenantInfoExtend.getTenantId());
                generateEntDefaultSeal(request.getCompanyName(),sysTenantUser,sysDepart.getId());
            }else if(request.getBizType() == TenantAuthType.CREATED.getType()){
                generateEntDefaultSeal(request.getCompanyName(),sysTenantUser,sysDepart.getId());
            }
            tenantInfoExtend.setName(request.getCompanyName());
        }
        tenantAuthLogNew.setTenantId(tenantInfoExtend.getTenantId());
        tenantAuthLogNew.setExtendId(tenantInfoExtend.getId());
        tenantInfoExtend.setAuthStatus(TenantAuthStatus.STATUS2.getStatus());
        tenantAuthLogNew.setAuthStatus(TenantAuthStatus.STATUS2.getStatus());
        tenantInfoExtend.setAuthType(request.getBizType());
        tenantAuthLogNew.setAuthType(request.getBizType());

        tenantInfoExtend.setPhone(RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING,request.getMobile()));
        tenantAuthLogNew.setPhone(RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING,request.getMobile()));
        //tenantInfoExtend.setTenantType(request.getUserType());
        tenantAuthLogNew.setOrderNo(request.getOrderNo());

        tenantAuthLogNew.setRealItem(request.getBizType());
        tenantAuthLogNew.setSysTenantId(request.getUnionId());
        tenantAuthLogNew.setCreateTime(DateUtil.str2Date(request.getCreateTime(), DateUtil.yyyymmddhhmmss.get()));
        tenantInfoExtend.setIdCardType(request.getIdCardType());
        tenantAuthLogNew.setIdCardType(request.getIdCardType());
        tenantAuthLogNew.setTenantId(tenantInfoExtend.getTenantId());
        tenantAuthLogNew.setVerifyTime(DateUtil.str2Date(request.getVerifyTime(), DateUtil.yyyymmddhhmmss.get()));
        tenantAuthLogNew.setOrderStatus(request.getOrderStatus());
        tenantAuthLogService.saveOrUpdate(tenantAuthLogNew);

        tenantInfoExtend.setAuthId(tenantAuthLogNew.getId());
        tenantInfoExtend.setUpdateTime(new Date());
        tenantInfoExtend.setVerifyTime(DateUtil.str2Date(request.getVerifyTime(), DateUtil.yyyymmddhhmmss.get()));

        tenantInfoExtendService.updateById(tenantInfoExtend);

        sysTenantInfoService.updateById(sysTenantInfo);

        bindTaskData(tenantInfoExtend.getName(), tenantInfoExtend.getTenantId());
    }

    private void bindTaskData(String tenantName, String tenantId) {
        //解析数据库已有数据做绑定表:sign_ru_task
        //绑定企业名：租户ID
        flowService.bindOutUserTask(tenantName, null, null, null, null, null, tenantId, null, "tenantCheck");
        //绑定租户ID：(查询用户ID)租户账号ID
        flowService.bindTenantUserTaskAll(tenantId, "tenantCheck");
    }

    /**
     * 生成企业默认印章
     * @param topText
     * @param sysTenantUser
     */
    @Transactional(rollbackFor = Exception.class)
    public void generateEntDefaultSeal(String topText, SysTenantUser sysTenantUser, String departId){
        String sealBase64 = entSealGenerateService.generateEntCircleSeal(topText, "公章", null,true);
        String replace = sealBase64.replace("data:image/png;base64,", "");
        byte[] decode = Base64.decode(replace);
        String annexId = signFileService.saveAnnexStorage(decode, SignFileEnum.SEAL_FILE_ENT, null);
        EntSealSaveRequest request = new EntSealSaveRequest();
        request.setColor(SealColorEnum.RED.getCode());
        request.setDescription("实名认证通过系统自动生成印章");
        request.setSealName(topText);
        request.setMiddleText("公章");
        request.setCreateType(1);
        request.setSealType(1);
        request.setAnnexId(annexId);

        BusinessAuthVo businessAuthVo = new BusinessAuthVo();

        businessAuthVo.setAuthType(1);
        businessAuthVo.setAuthRelationId(sysTenantUser.getId());
        businessAuthVo.setAuthRelationName(sysTenantUser.getNickName());
        List<BusinessAuthVo> authVos = new ArrayList<>(1);
        authVos.add(businessAuthVo);
        request.setUserList(authVos);
        request.setManagerList(authVos);
        sealBusinessService.sysGeneratesave(request,sysTenantUser.getTenantId(),sysTenantUser.getId(),departId);
    }

    /**
     * 生成个人默认印章
     * @param name
     */
    @Transactional(rollbackFor = Exception.class)
    private void generatePersonDefaultSeal(String name, SysDepart depart, TenantInfoExtend tenantInfoExtend) {
        BufferedImage bufferedImage = null;
        byte[] sealByte = null;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            bufferedImage = psersonSealGenerateService.drawRectangleNoFrame(name, SealColorEnum.RED.getCode());
            ImageIO.write(bufferedImage, "png", output);
            sealByte = output.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //LoginUser currentUser = MySecurityUtils.getCurrentUser();
        SignPersonSeal personSeal = new SignPersonSeal();
        personSeal.setSealName("默认签名");
        personSeal.setSysTenantId(tenantInfoExtend.getTenantId());
        //personSeal.setSysUserId(tenantInfoExtend.getSysUserId());
        //personSeal.setSysAccountId(tenantInfoExtend.getSysAccountId());
        personSeal.setSysOrgCode(depart.getOrgCode());
        personSeal.setSysDeptId(depart.getId());
        personSeal.setDeleteFlag(false);
        personSeal.setSealType(PersonalSealTypeEnum.TEMPLATE.getType());

        boolean save = personSealService.save(personSeal);
        String annexId = signFileService.saveAnnexStorage(sealByte, SignFileEnum.SEAL_FILE_PERSON, personSeal.getId());
//        signFileService.updateAnnexStorage(SignFileEnum.SEAL_FILE_PERSON, personSeal.getId(), annexId);
    }

}
