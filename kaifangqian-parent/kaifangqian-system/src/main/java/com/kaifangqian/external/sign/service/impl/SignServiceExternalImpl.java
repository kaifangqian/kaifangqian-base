/**
 * @description 电子签业务逻辑接口实现
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

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.kaifangqian.external.base.CommonResult;
import com.kaifangqian.modules.api.util.SignHeadersGenerator;
import com.kaifangqian.modules.cert.service.HttpUtils;
import com.kaifangqian.modules.system.entity.SysAppInfo;
import com.kaifangqian.common.constant.YundunApiCode;
import com.kaifangqian.common.redis.util.RedisUtil;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.util.RsaUtils;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.external.sign.request.AutoSignDocumentRequest;
import com.kaifangqian.external.sign.request.SignCallbackRequest;
import com.kaifangqian.external.sign.request.SignOrderRequest;
import com.kaifangqian.external.sign.request.VerifySignDocumentRequest;
import com.kaifangqian.external.sign.response.AuthSignDocumentResponse;
import com.kaifangqian.external.sign.response.AutoSignDocumentResponse;
import com.kaifangqian.external.sign.response.SignOrderServiceInfoResponse;
import com.kaifangqian.external.sign.response.SignOrderStatusInfoResponse;
import com.kaifangqian.external.sign.service.SignServiceExternal;
import com.kaifangqian.external.enums.SignOrdeStatusResponseTypeEnum;
import com.kaifangqian.modules.opensign.constant.SignCommonConstant;
import com.kaifangqian.modules.opensign.entity.SignRuTask;
import com.kaifangqian.modules.opensign.entity.SignUserConfirm;
import com.kaifangqian.modules.opensign.service.confirm.ISignUserConfirmService;
import com.kaifangqian.modules.opensign.service.ru.SignRuTaskService;
import com.kaifangqian.modules.system.service.ISysAppInfoService;
import com.kaifangqian.utils.MD5Util;
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
public class SignServiceExternalImpl implements SignServiceExternal {

    @Autowired
    private SignRuTaskService ruTaskService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ISignUserConfirmService signUserConfirmService;

    @Autowired
    private ISysAppInfoService sysAppInfoService;

    @Value("${service.app-id}")
    private String appId ;

    @Value("${service.yundun-service-private-key}")
    private String privateKey ;

    @Value("${service.manage.yundun-auth-sign-url}")
    private String yundunAuthSignUrl;

    @Value("${service.manage.yundun-auth-sign-verify-url}")
    private String yundunAuthSignVerifyUrl;

    @Value("${service.manage.yundun-auth-sign-info-url}")
    private String yundunAuthSignInfoUrl;

    @Value("${service.manage.yundun-silent-sign-url}")
    private String yundunSilentSignUrl;


    @Override
    public SignOrderServiceInfoResponse generateSignOrderAndGetSignAuthUrl(SignOrderRequest signOrderRequest) throws Exception {

        CommonResult<SignOrderServiceInfoResponse> result = null;

        SignOrderServiceInfoResponse signOrderServiceInfoResponse = new SignOrderServiceInfoResponse();

        // 获取当前登录用户
        LoginUser loginUser = MySecurityUtils.getCurrentUser();

        // 获取签署任务
        SignRuTask signRuTask = ruTaskService.getById(signOrderRequest.getBizId());

        // 构建意愿校验签署确认参数
        SignUserConfirm signUserConfirm = signUserConfirmService.getByMainId(signOrderRequest.getBizId());
        String  verifyOrderNo = null;
        if(signUserConfirm == null){
            verifyOrderNo = signUserConfirmService.saveExt(signOrderRequest.getBizId());
        }else if(signUserConfirm != null){
            verifyOrderNo = signUserConfirm.getId();
        }

        //查询本地订单是否可以继续意愿校验签署
        if(signRuTask != null && MyStringUtils.isNotBlank(signRuTask.getOrderNo())){
            SignOrderStatusInfoResponse signOrderStatusInfoResponse = getSignOrderStatus(signRuTask.getOrderNo());
            if(signOrderStatusInfoResponse.getStatus() == 1){
                if(signOrderStatusInfoResponse.getOrderStatus() == SignOrdeStatusResponseTypeEnum.NO_AUTH.getCode() && signRuTask.getTaskStatus() == 1){
                    String token = MD5Util.MD5Encode(SignCommonConstant.VERIFY_SIGN_DOCUMENT+signRuTask.getId(),"UTF-8");

                    //String bizCallbackUrl = signOrderRequest.getCallbackPage()+"?orderNo="+verifyOrderNo+"&signRuId="+signOrderRequest.getContractId();
                    String bizCallbackUrl = signOrderRequest.getCallbackPage();
                    redisUtil.set(token,bizCallbackUrl);

                    signOrderServiceInfoResponse.setStatus(0);
                    signOrderServiceInfoResponse.setSignConfirmUrl(signRuTask.getYdAuthSignUrl());
                } else if(signOrderStatusInfoResponse.getOrderStatus() == SignOrdeStatusResponseTypeEnum.VERIFY_SUCCESS.getCode() && signRuTask.getTaskStatus() == 2){
                    signUserConfirmService.setFlag(signUserConfirm.getId(),true);

                    String token = RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING,SignCommonConstant.VERIFY_SIGN_DOCUMENT+signRuTask.getId());
                    //String bizCallbackUrl = signOrderRequest.getCallbackPage()+"?orderNo="+verifyOrderNo+"&signRuId="+signOrderRequest.getContractId();
                    String bizCallbackUrl = signOrderRequest.getCallbackPage();
                    redisUtil.set(token,bizCallbackUrl);

                    signOrderServiceInfoResponse.setStatus(1);
                    signOrderServiceInfoResponse.setResultMessage(SignOrdeStatusResponseTypeEnum.VERIFY_SUCCESS.getName());
                } else if(signOrderStatusInfoResponse.getOrderStatus() == SignOrdeStatusResponseTypeEnum.VERIFY_SUCCESS.getCode() && signRuTask.getTaskStatus() == 1){
                    sendSignOrder(signOrderRequest,verifyOrderNo,loginUser,signRuTask,signOrderServiceInfoResponse);
                    signUserConfirmService.setFlag(signUserConfirm.getId(),false);
                } else if(signOrderStatusInfoResponse.getOrderStatus() == SignOrdeStatusResponseTypeEnum.INVALID.getCode() && signRuTask.getTaskStatus() == 1){
                    sendSignOrder(signOrderRequest,verifyOrderNo,loginUser,signRuTask,signOrderServiceInfoResponse);
                    signUserConfirmService.setFlag(signUserConfirm.getId(),false);
                } else if(signOrderStatusInfoResponse.getOrderStatus() == SignOrdeStatusResponseTypeEnum.SIGN_DONE.getCode() && signRuTask.getTaskStatus() == 1){
                    sendSignOrder(signOrderRequest,verifyOrderNo,loginUser,signRuTask,signOrderServiceInfoResponse);
                    signUserConfirmService.setFlag(signUserConfirm.getId(),false);
                }
            }else{
                sendSignOrder(signOrderRequest,verifyOrderNo,loginUser,signRuTask,signOrderServiceInfoResponse);
                signUserConfirmService.setFlag(signUserConfirm.getId(),false);
            }
        }else {
            sendSignOrder(signOrderRequest,verifyOrderNo,loginUser,signRuTask,signOrderServiceInfoResponse);
        }
        return signOrderServiceInfoResponse;
    }

    @Override
    public AuthSignDocumentResponse submitAuthHashSign(VerifySignDocumentRequest verifySignDocumentRequest) throws Exception {
        CommonResult<AuthSignDocumentResponse> result = null;
        AuthSignDocumentResponse authSignDocumentResponse = null;

        // 获取当前登录用户
        LoginUser loginUser = MySecurityUtils.getCurrentUser();

        // 构建请求参数
        JSONObject params = (JSONObject)JSONObject.toJSON(verifySignDocumentRequest);

        // 构建请求头
        String uri = "/yundun/api/v1/sign/verify";
        Map<String,String> headers = SignHeadersGenerator.generateSignHeaders(appId, params.toJSONString(), uri, privateKey);

        String returnJson = null ;
        try {
            returnJson = HttpUtils.executePost(yundunAuthSignVerifyUrl, params.toJSONString(), headers);
            log.info("{} sign result:{}",loginUser.getRealname(),returnJson);
            result = JSONObject.parseObject(returnJson,new TypeReference<CommonResult<AuthSignDocumentResponse>>(){});
        }catch (Exception e) {
            log.error("{} http error：",yundunAuthSignVerifyUrl,e);
        }

        if (result != null) {
            if (result.getCode().equals(YundunApiCode.SUCCESS.getCode())) {
                authSignDocumentResponse = result.getResult();
                authSignDocumentResponse.setStatus(1);
            }else if(result.getCode().equals(YundunApiCode.SIGN_INTENT_NOT_FOUND.getCode())){
                authSignDocumentResponse.setStatus(0);
                authSignDocumentResponse.setResultMessage(result.getMessage());
            }else if(result.getCode().equals(YundunApiCode.SIGN_INTENT_UNVERIFIED.getCode())){
                authSignDocumentResponse.setStatus(0);
                authSignDocumentResponse.setResultMessage(result.getMessage());
            }else if(result.getCode().equals(YundunApiCode.SIGN_INTENT_EXPIRED.getCode())){
                authSignDocumentResponse.setStatus(0);
                authSignDocumentResponse.setResultMessage(result.getMessage());
            }else if(result.getCode().equals(YundunApiCode.SIGN_INTENT_ALREADY_SIGNED.getCode())){
                authSignDocumentResponse.setStatus(0);
                authSignDocumentResponse.setResultMessage(result.getMessage());
            }
        }

        return authSignDocumentResponse;

    }

    @Override
    public AutoSignDocumentResponse submitAutoHashSign(AutoSignDocumentRequest autoSignDocumentRequest) throws Exception {
        CommonResult<AutoSignDocumentResponse> result = null;
        AutoSignDocumentResponse autoSignDocumentResponse = null;

        // 获取当前登录用户
        // LoginUser loginUser = MySecurityUtils.getCurrentUser();

        // 构建请求参数
        JSONObject params = (JSONObject)JSONObject.toJSON(autoSignDocumentRequest);

        // 构建请求头
        String uri = "/yundun/api/v1/sign/silent/sign";
        Map<String,String> headers = SignHeadersGenerator.generateSignHeaders(appId, params.toJSONString(), uri, privateKey);

        String returnJson = null ;
        try {
            returnJson = HttpUtils.executePost(yundunSilentSignUrl, params.toJSONString(), headers);
            log.info("sign result:{}",returnJson);
            result = JSONObject.parseObject(returnJson,new TypeReference<CommonResult<AutoSignDocumentResponse>>(){});
        }catch (Exception e) {
            log.error("{} http error：",yundunSilentSignUrl,e);
        }
        if (result != null) {
            if (result.getCode().equals(YundunApiCode.SUCCESS.getCode())) {
                autoSignDocumentResponse = result.getResult();
                autoSignDocumentResponse.setStatus(1);
            }else{
                autoSignDocumentResponse.setStatus(0);
                autoSignDocumentResponse.setResultMessage(result.getMessage());
            }
        }

        return autoSignDocumentResponse;
    }

    @Override
    public SignOrderStatusInfoResponse getSignOrderStatus(String orderNo) throws Exception {

        SignOrderStatusInfoResponse signOrderStatusInfoResponse = null;


        String uri = "/yundun/api/v1/sign/order/status?orderNo="+orderNo;
        String quyerYundunAuthSignInfoUrl = yundunAuthSignInfoUrl+"?orderNo="+orderNo;

        // 构建请求头
        Map<String,String> headers = SignHeadersGenerator.generateSignHeaders(appId, null, uri, privateKey);

        String returnJson = null ;
        try {
            returnJson = HttpUtils.executeGetMap(quyerYundunAuthSignInfoUrl, null, headers);
            log.info("Query signOrder status info result: {}", returnJson);
            CommonResult<SignOrderStatusInfoResponse> result = JSONObject.parseObject(returnJson, new TypeReference<CommonResult<SignOrderStatusInfoResponse>>() {});
            if (result != null && result.getCode().equals(YundunApiCode.SUCCESS.getCode())) {
                signOrderStatusInfoResponse = result.getResult();
                signOrderStatusInfoResponse.setStatus(1);
            }else if(result != null && result.getCode().equals(YundunApiCode.SIGN_INTENT_NOT_FOUND.getCode())){
                signOrderStatusInfoResponse = new SignOrderStatusInfoResponse();
                signOrderStatusInfoResponse.setStatus(0);
                signOrderStatusInfoResponse.setResultMessage(result.getMessage());
            }
        } catch (Exception e) {
            log.error("Error occurred while querying signOrder status.", e);
        }

        return signOrderStatusInfoResponse;
    }

    @Override
    public Result<?> updateSignUserConfirmStatus(SignCallbackRequest request) throws Exception {
        SignUserConfirm confirm = signUserConfirmService.getByMainId(request.getBizId());
//        ConfirmParaRequest confirmPara = new ConfirmParaRequest();
//        confirmPara.setConfirmType(request.getFinalSignType());
//        confirmPara.setOrderNo(confirm.getId());
//        signUserConfirmService.confirmPara(confirmPara);
        if(request.getOrderStatus() == SignOrdeStatusResponseTypeEnum.VERIFY_SUCCESS.getCode()){
            signUserConfirmService.setFlag(confirm.getId(),true);
        }else if(request.getOrderStatus() == SignOrdeStatusResponseTypeEnum.INVALID.getCode()){
            signUserConfirmService.setFlag(confirm.getId(),false);
        }
        return Result.OK("success");
    }

    /**
     * 发送签署订单
     * @return
     */
    private void sendSignOrder(SignOrderRequest signOrderRequest, String verifyOrderNo,LoginUser loginUser,SignRuTask signRuTask,SignOrderServiceInfoResponse signOrderServiceInfoResponse) throws Exception {

        CommonResult<SignOrderServiceInfoResponse> result = null;

        String token = MD5Util.MD5Encode(SignCommonConstant.VERIFY_SIGN_DOCUMENT+signRuTask.getId(),"UTF-8");
        //String bizCallbackUrl = signOrderRequest.getCallbackPage()+"?orderNo="+verifyOrderNo+"&signRuId="+signOrderRequest.getContractId();
        String bizCallbackUrl = signOrderRequest.getCallbackPage();

        redisUtil.set(token,bizCallbackUrl);
        redisUtil.expire(token, SignCommonConstant.TWO_DAY);

        SysAppInfo sysAppInfo = sysAppInfoService.getById("490489ab-d8b4-414c-ad77-d856962c286f");
        String ydCallbackUrl = "";
        if (sysAppInfo != null) {
            ydCallbackUrl = sysAppInfo.getAppAddress() + "/#/callbackpage?state=" + token ;
        }
        signOrderRequest.setCallbackPage(ydCallbackUrl);

        // 构建请求参数
        JSONObject params = (JSONObject)JSONObject.toJSON(signOrderRequest);

        // 构建URI
        String uri = "/yundun/api/v1/sign/verify/link";

        // 构建请求头
        Map<String,String> headers = SignHeadersGenerator.generateSignHeaders(appId, params.toJSONString(), uri, privateKey);

        String returnJson = null ;
        try {
            returnJson = HttpUtils.executePost(yundunAuthSignUrl, params.toJSONString(), headers);
            log.info("{} auth sign result:{}",loginUser.getRealname(),returnJson);
            result = JSONObject.parseObject(returnJson,new TypeReference<CommonResult<SignOrderServiceInfoResponse>>(){});
        }catch (Exception e) {
            log.error("{} http error：",yundunAuthSignVerifyUrl,e);
        }

        if (result != null) {
            if (result.getCode().equals(YundunApiCode.SUCCESS.getCode())) {
                signRuTask.setOrderNo(result.getResult().getOrderNo());
                signRuTask.setYdAuthSignUrl(result.getResult().getSignConfirmUrl());
                ruTaskService.updateById(signRuTask);
                signOrderServiceInfoResponse.setStatus(0);
                signOrderServiceInfoResponse.setSignConfirmUrl(result.getResult().getSignConfirmUrl());
            }else if(result.getCode().equals(YundunApiCode.SIGNER_NO_AUTH.getCode())){
                signOrderServiceInfoResponse.setStatus(1);
                signOrderServiceInfoResponse.setResultMessage(result.getMessage());
            }else if(result.getCode().equals(YundunApiCode.SIGN_UNION_ID_MISMATCH.getCode())){
                signOrderServiceInfoResponse.setStatus(1);
                signOrderServiceInfoResponse.setResultMessage(result.getMessage());
            }else if(result.getCode().equals(YundunApiCode.SIGN_NO_SUBJECT_FOUND.getCode())){
                signOrderServiceInfoResponse.setStatus(1);
                signOrderServiceInfoResponse.setResultMessage(result.getMessage());
            }

        }
    }

}
