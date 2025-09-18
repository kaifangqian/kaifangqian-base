/**
 * @description 电子签服务（静默签管理、快捷签管理）业务逻辑接口实现
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
package com.kaifangqian.external.sign.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.kaifangqian.external.base.CommonResult;
import com.kaifangqian.external.sign.response.*;
import com.kaifangqian.modules.api.util.SignHeadersGenerator;
import com.kaifangqian.modules.cert.service.HttpUtils;
import com.kaifangqian.modules.system.entity.SysTenantUser;
import com.kaifangqian.modules.system.entity.SysUser;
import com.kaifangqian.modules.system.entity.TenantInfoExtend;
import com.kaifangqian.modules.system.enums.TenantType;
import com.kaifangqian.common.constant.YundunApiCode;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.external.sign.request.FastSignOpenInfoRequest;
import com.kaifangqian.external.sign.request.SilentSignOpenInfoRequest;
import com.kaifangqian.external.sign.response.*;
import com.kaifangqian.external.sign.service.SignServiceManageExternal;
import com.kaifangqian.modules.system.service.ISysTenantUserService;
import com.kaifangqian.modules.system.service.ITenantInfoExtendService;
import com.kaifangqian.utils.MyStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
/**
 * @author : yxb
 * create at: 2025/6/6
 */
@Service
@Slf4j
public class SignServiceManageExternalImpl implements SignServiceManageExternal {

    @Value("${service.app-id}")
    private String appId ;

    @Value("${service.yundun-service-private-key}")
    private String privateKey ;

    @Value("${service.manage.yundun-silent-get-info-url}")
    private String yundunSilentGetInfoUrl;

    @Value("${service.manage.yundun-silent-get-infos-url}")
    private String yundunSilentGetInfosUrl;

    @Value("${service.manage.yundun-silent-service-open-url}")
    private String yundunSilentServiceOpenUrl;

    @Value("${service.manage.yundun-silent-service-close-url}")
    private String yundunSilentServiceCloseUrl;

    @Value("${service.manage.yundun-fastsign-get-info-url}")
    private String yundunFastSignGetInfoUrl;

    @Value("${service.manage.yundun-fastsign-get-infos-url}")
    private String yundunFastSignGetInfosUrl;

    @Value("${service.manage.yundun-fastsign-service-open-url}")
    private String yundunFastSignServiceOpenUrl;

    @Value("${service.manage.yundun-fastsign-service-close-url}")
    private String yundunFastSignServiceCloseUrl;

    @Value("${service.manage.yundun-app-info-url}")
    private String yundunAppInfoServiceUrl;

    @Autowired
    private ITenantInfoExtendService tenantInfoExtendService;

    @Autowired
    private ISysTenantUserService sysTenantUserService;



    @Override
    public SignServiceOpenInfoResponse querySilentInfo() throws Exception {

        LoginUser loginUser = MySecurityUtils.getCurrentUser();

        SignServiceOpenInfoResponse signServiceOpenInfoResponse = null;

        String uri = "/yundun/api/v1/sign/silent/service/info?unionId="+loginUser.getTenantId();
        String querySilentInfoUrl = yundunSilentGetInfoUrl+"?unionId="+loginUser.getTenantId();

        // 构建请求头
        Map<String,String> headers = SignHeadersGenerator.generateSignHeaders(appId, null, uri, privateKey);

        String returnJson = null ;
        try {
            returnJson = HttpUtils.executeGetMap(querySilentInfoUrl, null, headers);
            log.info("Query silent info result: {}", returnJson);
            CommonResult<SignServiceOpenInfoResponse> result = JSONObject.parseObject(returnJson, new TypeReference<CommonResult<SignServiceOpenInfoResponse>>() {});
            if (result != null && result.getCode().equals(YundunApiCode.SUCCESS.getCode())) {
                signServiceOpenInfoResponse = result.getResult();
                if(result.getResult().getStatus() == null){
                    signServiceOpenInfoResponse.setStatus(0);
                }
            } else if(result != null && result.getCode().equals(YundunApiCode.SIGN_NO_SUBJECT_FOUND.getCode())){
                signServiceOpenInfoResponse = new SignServiceOpenInfoResponse();
                signServiceOpenInfoResponse.setStatus(-1);
                signServiceOpenInfoResponse.setResultMessage(result.getMessage());
                log.error("Response: {}", returnJson);
            }
        } catch (Exception e) {
            log.error("Error occurred while querying silent info.", e);
        }

        return signServiceOpenInfoResponse;

    }

    @Override
    public SilentSignServiceInfosResponse querySilentRecord() throws Exception {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();

        SilentSignServiceInfosResponse silentSignServiceInfosResponse = null;

        String uri = "/yundun/api/v1/sign/silent/service/list?unionId="+loginUser.getTenantId();
        String querySilentInfoRecordUrl = yundunSilentGetInfosUrl+"?unionId="+loginUser.getTenantId();

        // 构建请求头
        Map<String,String> headers = SignHeadersGenerator.generateSignHeaders(appId, null, uri, privateKey);

        String returnJson = null ;

        try {
            returnJson = HttpUtils.executeGetMap(querySilentInfoRecordUrl, null, headers);
            log.info("Query silent info result: {}", returnJson);
            CommonResult<SilentSignServiceInfosResponse> result = JSONObject.parseObject(returnJson, new TypeReference<CommonResult<SilentSignServiceInfosResponse>>() {});
            if (result != null && result.getCode().equals(YundunApiCode.SUCCESS.getCode())) {
                silentSignServiceInfosResponse = result.getResult();

                if(CollUtil.isNotEmpty(silentSignServiceInfosResponse.getSilentSignAuthorizes())){
                    for(SilentSignAuthorizeRecordResponse silentSignAuthorizeRecordResponse : silentSignServiceInfosResponse.getSilentSignAuthorizes()){
                        TenantInfoExtend tenantInfoExtendTemp = tenantInfoExtendService.getTenantInfoByTenantId(silentSignAuthorizeRecordResponse.getContactUnionId());
                        silentSignAuthorizeRecordResponse.setContactName(tenantInfoExtendTemp.getName());
                    }
                }
                silentSignServiceInfosResponse.setStatus(1);
            } else if(result != null && result.getCode().equals(YundunApiCode.SIGN_NO_SUBJECT_FOUND.getCode())){
                silentSignServiceInfosResponse = new SilentSignServiceInfosResponse();
                silentSignServiceInfosResponse.setStatus(0);
                silentSignServiceInfosResponse.setResultMessage(result.getMessage());
                log.error("Response: {}", returnJson);
            }
        } catch (Exception e) {
            log.error("Error occurred while querying silent info.", e);
        }

        return silentSignServiceInfosResponse;
    }

    @Override
    public SilentSignOpenServiceInfoResponse openSilentSignService(String callbackPage) throws Exception {

        CommonResult<SilentSignOpenServiceInfoResponse> result = null;

        SilentSignOpenServiceInfoResponse silentSignOpenServiceInfoResponse = new SilentSignOpenServiceInfoResponse();

        // 获取当前登录用户
        LoginUser loginUser = MySecurityUtils.getCurrentUser();

        // 获取当前登录租户信息
        TenantInfoExtend tenantInfoExtend = tenantInfoExtendService.getTenantInfoByTenantId(loginUser.getTenantId());
        if(tenantInfoExtend == null){
            throw new PaasException("租户不存在");
        }

        // 获取当前用户信息

        SilentSignOpenInfoRequest silentSignOpenInfoRequest = new SilentSignOpenInfoRequest();
        SysTenantUser sysTenantPersonalUser = null ;
        if(tenantInfoExtend.getTenantType() == TenantType.GROUP.getType()){
            sysTenantPersonalUser = sysTenantUserService.getPersonalTenantUser(loginUser.getId());
            silentSignOpenInfoRequest.setContactUnionId(sysTenantPersonalUser.getTenantId());
        }else if(tenantInfoExtend.getTenantType() == TenantType.PERSONAL.getType()) {
            silentSignOpenInfoRequest.setContactUnionId(tenantInfoExtend.getTenantId());
        }

        SysUser sysSignUser = sysTenantUserService.getTenantSysUser(loginUser.getTenantUserId());

        silentSignOpenInfoRequest.setUnionId(tenantInfoExtend.getTenantId());

        silentSignOpenInfoRequest.setCallbackPage(callbackPage);
        if(MyStringUtils.isNotBlank(sysSignUser.getEmail())){
            silentSignOpenInfoRequest.setEmail(sysSignUser.getEmail());
        }
        // 构建请求参数
        JSONObject params = (JSONObject)JSONObject.toJSON(silentSignOpenInfoRequest);

        // 构建URI
        String uri = "/yundun/api/v1/sign/silent/service/open";

        // 构建请求头
        Map<String,String> headers = SignHeadersGenerator.generateSignHeaders(appId, params.toJSONString(), uri, privateKey);

        String returnJson = null ;
        try {
            returnJson = HttpUtils.executePost(yundunSilentServiceOpenUrl, params.toJSONString(), headers);
            log.info("{} silent open result:{}",loginUser.getRealname(),returnJson);
            result = JSONObject.parseObject(returnJson,new TypeReference<CommonResult<SilentSignOpenServiceInfoResponse>>(){});
        }catch (Exception e) {
            log.error("{} http error：",yundunSilentServiceOpenUrl,e);
        }
        if (result != null){
            if (result.getCode().equals(YundunApiCode.SUCCESS.getCode())){
                silentSignOpenServiceInfoResponse.setStatus(0);
                silentSignOpenServiceInfoResponse.setOpenSignNoVerifyUrl(result.getResult().getOpenSignNoVerifyUrl());
            }else if(result.getCode().equals(YundunApiCode.SIGN_COMPANY_CONTACT_REQUIRED.getCode())){
                silentSignOpenServiceInfoResponse.setStatus(1);
                silentSignOpenServiceInfoResponse.setResultMessage(tenantInfoExtend.getName()+YundunApiCode.SIGN_COMPANY_CONTACT_REQUIRED.getTemplate());
            }else if(result.getCode().equals(YundunApiCode.SIGN_INVALID_AGENT.getCode())){
                silentSignOpenServiceInfoResponse.setStatus(1);
                String tenantName = null ;
                if(sysTenantPersonalUser != null){
                    // 获取当前登录企业租户的个人操作用户信息
                    TenantInfoExtend personalTenantInfoExtend = tenantInfoExtendService.getTenantInfoByTenantId(sysTenantPersonalUser.getTenantId());
                    if(personalTenantInfoExtend != null && MyStringUtils.isNotBlank(personalTenantInfoExtend.getName())){
                        tenantName = personalTenantInfoExtend.getName();
                    }
                }
                silentSignOpenServiceInfoResponse.setResultMessage(tenantName+YundunApiCode.SIGN_INVALID_AGENT.getTemplate());
            }else if(result.getCode().equals(YundunApiCode.SIGNER_NO_QUIET_SIGN_AUTH.getCode())){
                silentSignOpenServiceInfoResponse.setStatus(1);
                silentSignOpenServiceInfoResponse.setResultMessage(tenantInfoExtend.getName()+YundunApiCode.SIGNER_NO_QUIET_SIGN_AUTH.getTemplate());
            }else if(result.getCode().equals(YundunApiCode.SIGN_ALREADY_QUIET_SIGN.getCode())){
                silentSignOpenServiceInfoResponse.setStatus(1);
                silentSignOpenServiceInfoResponse.setResultMessage(tenantInfoExtend.getName()+YundunApiCode.SIGN_ALREADY_QUIET_SIGN.getTemplate());
            }else if(result.getCode().equals(YundunApiCode.SIGN_NO_SUBJECT_FOUND.getCode())){
                silentSignOpenServiceInfoResponse.setStatus(1);
                silentSignOpenServiceInfoResponse.setResultMessage(tenantInfoExtend.getName()+YundunApiCode.SIGN_NO_SUBJECT_FOUND.getTemplate()+"或未完成用户实名认证");
            }
        }
        return silentSignOpenServiceInfoResponse;
    }

    @Override
    public SilentSignOpenServiceInfoResponse closeSilentSignService() throws Exception {

        CommonResult<SilentSignOpenServiceInfoResponse> result = null;

        SilentSignOpenServiceInfoResponse silentSignOpenServiceInfoResponse = new SilentSignOpenServiceInfoResponse();

        // 获取当前登录用户
        LoginUser loginUser = MySecurityUtils.getCurrentUser();

        // 获取当前登录租户信息
        TenantInfoExtend tenantInfoExtend = tenantInfoExtendService.getTenantInfoByTenantId(loginUser.getTenantId());

        // 获取当前用户信息
        SysUser sysSignUser = sysTenantUserService.getTenantSysUser(loginUser.getTenantUserId());
        SilentSignOpenInfoRequest silentSignOpenInfoRequest = new SilentSignOpenInfoRequest();
        silentSignOpenInfoRequest.setUnionId(tenantInfoExtend.getTenantId());

        // 构建请求参数
        JSONObject params = (JSONObject)JSONObject.toJSON(silentSignOpenInfoRequest);

        // 构建URI
        String uri = "/yundun/api/v1/sign/silent/service/close";

        // 构建请求头
        Map<String,String> headers = SignHeadersGenerator.generateSignHeaders(appId, params.toJSONString(), uri, privateKey);

        String returnJson = null ;
        try {
            returnJson = HttpUtils.executePost(yundunSilentServiceCloseUrl, params.toJSONString(), headers);
            log.info("{} silent open result:{}",loginUser.getRealname(),returnJson);
            result = JSONObject.parseObject(returnJson,new TypeReference<CommonResult<SilentSignOpenServiceInfoResponse>>(){});
        }catch (Exception e) {
            log.error("{} http silent error：",yundunSilentServiceCloseUrl,e);
        }

        if (result != null){
            if (result.getCode().equals(YundunApiCode.SUCCESS.getCode())){
                silentSignOpenServiceInfoResponse.setStatus(0);
                silentSignOpenServiceInfoResponse.setResultMessage(result.getMessage());
            }else if(result.getCode().equals(YundunApiCode.SIGN_NO_CLOSE_QUIET_SIGN.getCode())){
                // 如果认证通过重新查询并处理实名认证结果
                silentSignOpenServiceInfoResponse.setStatus(1);
                silentSignOpenServiceInfoResponse.setResultMessage(result.getMessage());
            }
        }
        return silentSignOpenServiceInfoResponse;

    }

    @Override
    public SignServiceOpenInfoResponse queryFastSignInfo() throws Exception {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();

        SignServiceOpenInfoResponse signServiceOpenInfoResponse = null;

        String uri = "/yundun/api/v1/sign/willingnessFree/service/info?unionId="+loginUser.getTenantId();
        String queryFastSignInfoUrl = yundunFastSignGetInfoUrl+"?unionId="+loginUser.getTenantId();

        // 构建请求头
        Map<String,String> headers = SignHeadersGenerator.generateSignHeaders(appId, null, uri, privateKey);

        String returnJson = null ;
        try {
            returnJson = HttpUtils.executeGetMap(queryFastSignInfoUrl, null, headers);
            log.info("Query fastSignService info result: {}", returnJson);
            CommonResult<SignServiceOpenInfoResponse> result = JSONObject.parseObject(returnJson, new TypeReference<CommonResult<SignServiceOpenInfoResponse>>() {});
            if (result != null && result.getCode().equals(YundunApiCode.SUCCESS.getCode())) {
                signServiceOpenInfoResponse = result.getResult();
                if(result.getResult().getStatus() == null){
                    signServiceOpenInfoResponse.setStatus(0);
                }
            } else if(result != null && result.getCode().equals(YundunApiCode.SIGN_NO_SUBJECT_FOUND.getCode())){
                signServiceOpenInfoResponse = new SignServiceOpenInfoResponse();
                signServiceOpenInfoResponse.setStatus(-1);
                signServiceOpenInfoResponse.setResultMessage(result.getMessage());
                log.error("Response: {}", returnJson);
            }
        } catch (Exception e) {
            log.error("Error occurred while querying fastSignService info.", e);
        }

        return signServiceOpenInfoResponse;
    }

    @Override
    public SignServiceOpenInfoResponse queryFastSignInfo(String tenantId) throws Exception {
        SignServiceOpenInfoResponse signServiceOpenInfoResponse = null;

        String uri = "";
        String queryFastSignInfoUrl = yundunFastSignGetInfoUrl+"?unionId="+tenantId;

        // 构建请求头
        Map<String,String> headers = SignHeadersGenerator.generateSignHeaders(appId, null, queryFastSignInfoUrl, privateKey);

        String returnJson = null ;
        try {
            returnJson = HttpUtils.executeGetMap(queryFastSignInfoUrl, null, headers);
            log.info("Query fastSignService info result: {}", returnJson);
            CommonResult<SignServiceOpenInfoResponse> result = JSONObject.parseObject(returnJson, new TypeReference<CommonResult<SignServiceOpenInfoResponse>>() {});
            if (result != null && result.getCode().equals(YundunApiCode.SUCCESS.getCode())) {
                signServiceOpenInfoResponse = result.getResult();
                if(result.getResult().getStatus() == null){
                    signServiceOpenInfoResponse.setStatus(0);
                }
            } else if(result != null && result.getCode().equals(YundunApiCode.SIGN_NO_SUBJECT_FOUND.getCode())){
                signServiceOpenInfoResponse = new SignServiceOpenInfoResponse();
                signServiceOpenInfoResponse.setStatus(-1);
                signServiceOpenInfoResponse.setResultMessage(result.getMessage());
                log.error("Response: {}", returnJson);
            }
        } catch (Exception e) {
            log.error("Error occurred while querying fastSignService info.", e);
        }

        return signServiceOpenInfoResponse;
    }

    @Override
    public FastSignServiceInfosResponse queryFastSignRecord() throws Exception {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();

        FastSignServiceInfosResponse fastSignServiceInfosResponse = null;

        String uri = "/yundun/api/v1/sign/willingnessFree/service/list?unionId="+loginUser.getTenantId();
        String queryFastSignInfosRecordUrl = yundunFastSignGetInfosUrl+"?unionId="+loginUser.getTenantId();

        // 构建请求头
        Map<String,String> headers = SignHeadersGenerator.generateSignHeaders(appId, null, uri, privateKey);

        String returnJson = null ;

        try {
            returnJson = HttpUtils.executeGetMap(queryFastSignInfosRecordUrl, null, headers);
            log.info("Query fastSignService info result: {}", returnJson);
            CommonResult<FastSignServiceInfosResponse> result = JSONObject.parseObject(returnJson, new TypeReference<CommonResult<FastSignServiceInfosResponse>>() {});
            if (result != null && result.getCode().equals(YundunApiCode.SUCCESS.getCode())) {
                fastSignServiceInfosResponse = result.getResult();
                fastSignServiceInfosResponse.setStatus(1);
            } else if(result != null && result.getCode().equals(YundunApiCode.SIGN_NO_SUBJECT_FOUND.getCode())){
                fastSignServiceInfosResponse = new FastSignServiceInfosResponse();
                fastSignServiceInfosResponse.setStatus(0);
                fastSignServiceInfosResponse.setResultMessage(result.getMessage());
                log.error("Response: {}", returnJson);
            }
        } catch (Exception e) {
            log.error("Error occurred while querying fastSignService info.", e);
        }

        return fastSignServiceInfosResponse;
    }

    @Override
    public FastSignOpenServiceInfoResponse openFastSignService(String callbackPage) throws Exception {
        CommonResult<FastSignOpenServiceInfoResponse> result = null;

        FastSignOpenServiceInfoResponse fastSignOpenServiceInfoResponse = new FastSignOpenServiceInfoResponse();

        // 获取当前登录用户
        LoginUser loginUser = MySecurityUtils.getCurrentUser();

        // 获取当前登录租户信息
        TenantInfoExtend tenantInfoExtend = tenantInfoExtendService.getTenantInfoByTenantId(loginUser.getTenantId());

        // 获取当前用户信息
        SysUser sysSignUser = sysTenantUserService.getTenantSysUser(loginUser.getTenantUserId());
        FastSignOpenInfoRequest fastSignOpenInfoRequest = new FastSignOpenInfoRequest();
        fastSignOpenInfoRequest.setUnionId(tenantInfoExtend.getTenantId());
        fastSignOpenInfoRequest.setCallbackPage(callbackPage);

        // 构建请求参数
        JSONObject params = (JSONObject)JSONObject.toJSON(fastSignOpenInfoRequest);

        String uri = "/yundun/api/v1/sign/willingnessFree/service/open";

        // 构建请求头
        Map<String,String> headers = SignHeadersGenerator.generateSignHeaders(appId, params.toJSONString(), uri, privateKey);

        String returnJson = null ;
        try {
            returnJson = HttpUtils.executePost(yundunFastSignServiceOpenUrl, params.toJSONString(), headers);
            log.info("{} fastSignService open result:{}",loginUser.getRealname(),returnJson);
            result = JSONObject.parseObject(returnJson,new TypeReference<CommonResult<FastSignOpenServiceInfoResponse>>(){});
        }catch (Exception e) {
            log.error("{} http fastSignService error：",yundunSilentServiceOpenUrl,e);
        }

        if (result != null){
            if (result.getCode().equals(YundunApiCode.SUCCESS.getCode())){
                fastSignOpenServiceInfoResponse.setStatus(0);
                fastSignOpenServiceInfoResponse.setOpenUrl(result.getResult().getOpenUrl());
            }else if(result.getCode().equals(YundunApiCode.SIGNER_NO_AUTH_FOR_WILLINGNESS.getCode())){
                fastSignOpenServiceInfoResponse.setStatus(1);
                fastSignOpenServiceInfoResponse.setResultMessage(tenantInfoExtend.getName()+YundunApiCode.SIGNER_NO_AUTH_FOR_WILLINGNESS.getTemplate());
            }else if(result.getCode().equals(YundunApiCode.NO_PERSONAL_ACCOUNT.getCode())){
                fastSignOpenServiceInfoResponse.setStatus(1);
                fastSignOpenServiceInfoResponse.setResultMessage(tenantInfoExtend.getName()+YundunApiCode.NO_PERSONAL_ACCOUNT.getTemplate());
            }else if(result.getCode().equals(YundunApiCode.OPEN_QUIET_SIGN_ALREADY.getCode())){
                fastSignOpenServiceInfoResponse.setStatus(1);
                fastSignOpenServiceInfoResponse.setResultMessage(result.getMessage());
            }else if(result.getCode().equals(YundunApiCode.SIGN_NO_SUBJECT_FOUND.getCode())){
                fastSignOpenServiceInfoResponse.setStatus(1);
                fastSignOpenServiceInfoResponse.setResultMessage(tenantInfoExtend.getName()+YundunApiCode.SIGN_NO_SUBJECT_FOUND.getTemplate()+"或未完成用户实名认证");
            }
        }
        return fastSignOpenServiceInfoResponse;
    }

    @Override
    public FastSignOpenServiceInfoResponse closeFastSignService() throws Exception {
        CommonResult<FastSignOpenServiceInfoResponse> result = null;

        FastSignOpenServiceInfoResponse fastSignOpenServiceInfoResponse = new FastSignOpenServiceInfoResponse();

        // 获取当前登录用户
        LoginUser loginUser = MySecurityUtils.getCurrentUser();

        // 获取当前登录租户信息
        TenantInfoExtend tenantInfoExtend = tenantInfoExtendService.getTenantInfoByTenantId(loginUser.getTenantId());

        // 获取当前用户信息
        SysUser sysSignUser = sysTenantUserService.getTenantSysUser(loginUser.getTenantUserId());
        FastSignOpenInfoRequest fastSignOpenInfoRequest = new FastSignOpenInfoRequest();
        fastSignOpenInfoRequest.setUnionId(tenantInfoExtend.getTenantId());

        // 构建请求参数
        JSONObject params = (JSONObject)JSONObject.toJSON(fastSignOpenInfoRequest);

        String uri = "/yundun/api/v1/sign/willingnessFree/service/close";

        // 构建请求头
        Map<String,String> headers = SignHeadersGenerator.generateSignHeaders(appId, params.toJSONString(), uri, privateKey);

        String returnJson = null ;
        try {
            returnJson = HttpUtils.executePost(yundunFastSignServiceCloseUrl, params.toJSONString(), headers);
            log.info("{} fastSignService open result:{}",loginUser.getRealname(),returnJson);
            result = JSONObject.parseObject(returnJson,new TypeReference<CommonResult<FastSignOpenServiceInfoResponse>>(){});
        }catch (Exception e) {
            log.error("{} http fastSignService error：",yundunSilentServiceCloseUrl,e);
        }

        if (result != null){
            if (result.getCode().equals(YundunApiCode.SUCCESS.getCode())){
                fastSignOpenServiceInfoResponse.setStatus(0);
                fastSignOpenServiceInfoResponse.setResultMessage(result.getMessage());
            }else if(result.getCode().equals(YundunApiCode.NO_OPEN_QUIET_SIGN_FOUND.getCode())){
                // 如果认证通过重新查询并处理实名认证结果
                fastSignOpenServiceInfoResponse.setStatus(1);
                fastSignOpenServiceInfoResponse.setResultMessage(result.getMessage());
            }
        }
        return fastSignOpenServiceInfoResponse;
    }

    @Override
    public SignAppInfoResponse querySignAppInfo() throws Exception {

        SignAppInfoResponse signAppInfoResponse = new SignAppInfoResponse();

        String uri = "/yundun/api/v1/sign/app/info";

        // 构建请求头
        Map<String,String> headers = SignHeadersGenerator.generateSignHeaders(appId, null, uri, privateKey);

        String returnJson = null ;

        try {
            returnJson = HttpUtils.executeGetMap(yundunAppInfoServiceUrl, null, headers);

            CommonResult<SignAppInfoResponse> result = JSONObject.parseObject(returnJson, new TypeReference<CommonResult<SignAppInfoResponse>>() {});
            if (result != null && result.getCode().equals(YundunApiCode.SUCCESS.getCode())) {
                signAppInfoResponse = result.getResult();
            }
        } catch (Exception e) {
            log.error("Error occurred while querying kaifangqian-sign-app info.", e);
        }

        return signAppInfoResponse;
    }
}
