/**
 * @description 用户意愿校验接口实现类，短信验证码、密码、人脸识别等验证
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
package com.kaifangqian.modules.opensign.service.confirm.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.kaifangqian.config.CommonConstants;
import com.kaifangqian.modules.opensign.constant.SignCommonConstant;
import com.kaifangqian.modules.opensign.service.business.RuBusinessService;
import com.kaifangqian.modules.opensign.service.business.vo.SignReportDocFileVo;
import com.kaifangqian.modules.opensign.service.business.vo.SignReportSignSubVO;
import com.kaifangqian.modules.opensign.service.confirm.ISignUserConfirmService;
import com.kaifangqian.modules.opensign.service.confirm.ISignUserConfirmStepService;
import com.kaifangqian.modules.opensign.service.confirm.IUserConfirmService;
import com.kaifangqian.modules.system.config.QCloudProperties;
import com.kaifangqian.modules.system.entity.SysUserConfig;
import com.kaifangqian.modules.system.enums.UpdatePasswordEnum;
import com.kaifangqian.modules.system.util.HttpUtil;
import com.kaifangqian.common.constant.CommonConstant;
import com.kaifangqian.common.redis.util.RedisUtil;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.DesensitizationUtils;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.dto.EmailDto;
import com.kaifangqian.dto.MessageDto;
import com.kaifangqian.entity.SysConfig;
import com.kaifangqian.enums.SysConfigType;
import com.kaifangqian.eunms.SendType;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.inteface.CommonToolsAPI;
import com.kaifangqian.modules.opensign.entity.SignRuTask;
import com.kaifangqian.modules.opensign.entity.SignUserConfirm;
import com.kaifangqian.modules.opensign.entity.SignUserConfirmStep;
import com.kaifangqian.modules.opensign.enums.ConfirmDataTypeEnum;
import com.kaifangqian.modules.opensign.enums.ConfirmOperateType;
import com.kaifangqian.modules.opensign.enums.ConfirmTypeEnum;
import com.kaifangqian.modules.opensign.service.ru.SignRuTaskService;
import com.kaifangqian.modules.opensign.vo.base.ConfirmPara;
import com.kaifangqian.modules.opensign.vo.base.FaceRerutnPara;
import com.kaifangqian.modules.opensign.vo.base.UserConfirmByCodeVO;
import com.kaifangqian.modules.opensign.vo.base.UserConfirmByPasswordVO;
import com.kaifangqian.modules.opensign.vo.request.ConfirmParaRequest;
import com.kaifangqian.modules.storage.service.IAnnexStorageService;
import com.kaifangqian.modules.system.service.ISysUserConfigService;
import com.kaifangqian.modules.system.service.IThirdServiceRecordService;
import com.kaifangqian.utils.MD5Util;
import com.kaifangqian.utils.MyStringUtils;
import com.kaifangqian.utils.SysMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : zhenghuihan
 * create at:  2024/4/2  15:14
 * @description:
 */
@Service
@Slf4j
public class UserConfirmServiceImpl implements IUserConfirmService {

    @Autowired
    private CommonToolsAPI commonToolsAPI;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SysMessageUtil sysMessageUtil;

    @Autowired
    private ISignUserConfirmService signUserConfirmService;

    @Autowired
    private ISignUserConfirmStepService signUserConfirmStepService;

    @Autowired
    private IThirdServiceRecordService thirdServiceRecordService;
    @Resource
    private QCloudProperties properties;
    @Autowired
    private SignRuTaskService signRuTaskService;
    @Autowired
    private IAnnexStorageService annexStorageService;
    @Autowired
    private ISysUserConfigService sysUserConfigService;
    @Autowired
    private RuBusinessService ruBusinessService;


    @Override
    public String sendMessage(String orderNo) {
        SysConfig sendMessageConfig = commonToolsAPI.getConfigByType(SysConfigType.SENDMESSAGE.getType());
        String key = UUID.randomUUID().toString();
        String code;
        if (sendMessageConfig != null && "true".equals(sendMessageConfig.getValue())) {
            code = RandomUtil.randomNumbers(6);
        } else {
            code = CommonConstants.DEFAULTCODE;
        }
        SignUserConfirm signUserConfirm = signUserConfirmService.getById(orderNo);
        if (signUserConfirm == null) {
            throw new PaasException("orderNo错误");
        }
        LoginUser user = MySecurityUtils.getCurrentUser();
        String phone = user.getPhone();
        if (MyStringUtils.isBlank(phone)) {
            throw new PaasException("未绑定手机号，不可以发送短信");
        }
        String realKey = MD5Util.MD5Encode(phone + key, "utf-8");
        redisUtil.set(realKey, code, 5 * 60);

        Map<String, String> para = new HashMap<>();
        para.put("code", code);
        MessageDto messageDto = new MessageDto();
        messageDto.setSendType(SendType.IMMEDIATELY);
        List<String> receivers = new ArrayList<>();
        receivers.add(phone);
        messageDto.setReceivers(receivers);
        messageDto.setTemplateCode("captcha");
        messageDto.setContentParaMap(para);
        sysMessageUtil.asyncSendMessage(messageDto);

        //第一步校验删除
        SignUserConfirmStep confirmStepTem = signUserConfirmStepService.getByOrderNo(orderNo, 1);
        if (confirmStepTem != null) {
            confirmStepTem.setDeleteFlag(true);
            signUserConfirmStepService.updateById(confirmStepTem);
        }

        //记录第一步校验
        SignUserConfirmStep confirmStep = new SignUserConfirmStep();
        confirmStep.setConfirmId(orderNo);
        confirmStep.setStep(1);
        confirmStep.setConfirmType(ConfirmTypeEnum.PHONE_EMAIL.getType());
        confirmStep.setDataType(ConfirmDataTypeEnum.CODE.getType());
        confirmStep.setSystemPara(code);
        signUserConfirmStepService.save(confirmStep);

        return key;
    }

    @Override
    public String sendEmail(String orderNo) {
        SignUserConfirm signUserConfirm = signUserConfirmService.getById(orderNo);
        if (signUserConfirm == null) {
            throw new PaasException("orderNo错误");
        }
        LoginUser user = MySecurityUtils.getCurrentUser();
        String email = user.getEmail();
        if (MyStringUtils.isBlank(email)) {
            throw new PaasException("未绑定邮箱，不可以发送邮件");
        }

        String key = UUID.randomUUID().toString();
        String code = RandomUtil.randomNumbers(4);
        String realKey = MD5Util.MD5Encode(email + key, "utf-8");
        redisUtil.set(realKey, code, 5 * 60);

        EmailDto emailDto = new EmailDto();
        emailDto.setSendType(SendType.IMMEDIATELY);
        List<String> receivers = new ArrayList<>();
        receivers.add(email);
        emailDto.setReceivers(receivers);
        emailDto.setTemplateCode("paas_check_email_code");
        Map<String, String> titlePara = new HashMap<>();
        titlePara.put("title", "验证码");
        emailDto.setTitleParaMap(titlePara);
        Map<String, String> contentPara = new HashMap<>();
        contentPara.put("code", code);
        emailDto.setContentParaMap(contentPara);

        sysMessageUtil.asyncSendEmail(emailDto);

        //第一步校验删除
        SignUserConfirmStep confirmStepTem = signUserConfirmStepService.getByOrderNo(orderNo, 1);
        if (confirmStepTem != null) {
            confirmStepTem.setDeleteFlag(true);
            signUserConfirmStepService.updateById(confirmStepTem);
        }

        //记录第一步校验
        SignUserConfirmStep confirmStep = new SignUserConfirmStep();
        confirmStep.setConfirmId(orderNo);
        confirmStep.setStep(1);
        confirmStep.setConfirmType(ConfirmTypeEnum.PHONE_EMAIL.getType());
        confirmStep.setDataType(ConfirmDataTypeEnum.CODE.getType());
        confirmStep.setSystemPara(code);
        signUserConfirmStepService.save(confirmStep);
        return key;
    }

    @Override
    public Result<?> confirmByPhoneEmail(UserConfirmByCodeVO vo) {
        ConfirmParaRequest para = new ConfirmParaRequest();
        para.setOrderNo(vo.getOrderNo());
        para.setConfirmType(vo.getConfirmType());
        signUserConfirmService.confirmPara(para);
        SignUserConfirm signUserConfirm = signUserConfirmService.getById(vo.getOrderNo());
        if (signUserConfirm == null) {
            throw new PaasException("orderNo错误");
        }
        if (MyStringUtils.isBlank(signUserConfirm.getConfirmPara())) {
            throw new PaasException("订单未绑定数据，不可以校验");
        }
        ConfirmPara confirmPara = new Gson().fromJson(signUserConfirm.getConfirmPara(), ConfirmPara.class);
        if (confirmPara == null) {
            throw new PaasException("订单未绑定数据，不可以校验");
        }
        String key = null;
        if (ConfirmTypeEnum.DOUBLE.getType().equals(vo.getConfirmType())) {

        } else if (ConfirmTypeEnum.PHONE_EMAIL.getType().equals(vo.getConfirmType())) {

        } else {
            throw new PaasException("校验类型不支持");
        }
        if (UpdatePasswordEnum.PHONE.getType().equals(vo.getType())) {
            key = confirmPara.getPhone();
        } else if (UpdatePasswordEnum.EMIAL.getType().equals(vo.getType())) {
            key = confirmPara.getEmail();
        } else {
            throw new PaasException("类型不支持");
        }
        String realKey = MD5Util.MD5Encode(key + vo.getCaptchaKey(), "utf-8");
        Object checkCode = redisUtil.get(realKey);
        //记录结果、更新整体校验
        SignUserConfirmStep confirmStep = signUserConfirmStepService.getByOrderNo(vo.getOrderNo(), 1);
        if (confirmStep == null) {
            throw new PaasException("还未发送校验码");
        }
        confirmStep.setUserPara(vo.getCaptcha());
        confirmStep.setConfirmType(vo.getType());
        if (checkCode == null || !checkCode.toString().equals(vo.getCaptcha())) {
            confirmStep.setConfirmFlag(false);
            signUserConfirmStepService.updateById(confirmStep);
            signUserConfirmService.setFlag(vo.getOrderNo(), false);
            return Result.error("验证码错误");
        } else {
            if (ConfirmTypeEnum.DOUBLE.getType().equals(vo.getConfirmType())) {
                confirmStep.setUserPara(vo.getCaptcha() + "——" + vo.getPassword());
                if (MyStringUtils.isBlank(confirmPara.getPassword()) || !confirmPara.getPassword().equals(vo.getPassword())) {
                    confirmStep.setConfirmFlag(false);
                    signUserConfirmStepService.updateById(confirmStep);
                    signUserConfirmService.setFlag(vo.getOrderNo(), false);
                    return Result.error("签署密码错误");
                }
            }

            signUserConfirmService.setFlag(vo.getOrderNo(), true);
            confirmStep.setConfirmFlag(true);
            signUserConfirmStepService.updateById(confirmStep);
            //删除redis中的验证码
            redisUtil.del(realKey);
            return Result.OK();
        }
    }

    @Override
    public Result<?> confirmByPassword(UserConfirmByPasswordVO vo) {
        ConfirmParaRequest para = new ConfirmParaRequest();
        para.setOrderNo(vo.getOrderNo());
        para.setConfirmType(vo.getConfirmType());
        signUserConfirmService.confirmPara(para);
        SignUserConfirm signUserConfirm = signUserConfirmService.getById(vo.getOrderNo());
        if (signUserConfirm == null) {
            throw new PaasException("orderNo错误");
        }
        if (MyStringUtils.isBlank(signUserConfirm.getConfirmPara())) {
            throw new PaasException("订单未绑定数据，不可以校验");
        }
        ConfirmPara confirmPara = new Gson().fromJson(signUserConfirm.getConfirmPara(), ConfirmPara.class);
        if (confirmPara == null) {
            throw new PaasException("订单未绑定数据，不可以校验");
        }
        String key = null;
        if (ConfirmTypeEnum.PAASWORD.getType().equals(vo.getConfirmType())) {
            key = confirmPara.getPassword();
        } else {
            throw new PaasException("类型不支持");
        }
        //第一步校验删除
        SignUserConfirmStep confirmStepTem = signUserConfirmStepService.getByOrderNo(vo.getOrderNo(), 1);
        if (confirmStepTem != null) {
            confirmStepTem.setDeleteFlag(true);
            signUserConfirmStepService.updateById(confirmStepTem);
        }

        //记录结果、更新整体校验
        SignUserConfirmStep confirmStep = new SignUserConfirmStep();
        confirmStep.setConfirmId(vo.getOrderNo());
        confirmStep.setStep(1);
        confirmStep.setConfirmType(ConfirmTypeEnum.PAASWORD.getType());
        confirmStep.setDataType(ConfirmDataTypeEnum.PASSWORD.getType());
        confirmStep.setUserPara(vo.getPassword());
        confirmStep.setUpdateTime(new Date());
        if (key == null || !key.equals(vo.getPassword())) {
            confirmStep.setConfirmFlag(false);
            signUserConfirmStepService.save(confirmStep);
            signUserConfirmService.setFlag(vo.getOrderNo(), false);
            return Result.error("签署密码错误");
        } else {
            confirmStep.setConfirmFlag(true);
            signUserConfirmStepService.save(confirmStep);
            signUserConfirmService.setFlag(vo.getOrderNo(), true);
            return Result.OK();
        }
    }

    @Override
    public String getFaceUrl(String orderNo, String redirectUrl) {
        SignUserConfirm confirm = signUserConfirmService.getById(orderNo);
        if (confirm == null || MyStringUtils.isBlank(confirm.getMainId())) {
            throw new PaasException("订单号不存在");
        }
        if (MyStringUtils.isBlank(confirm.getConfirmPara())) {
            throw new PaasException("订单未绑定数据，不可以校验");
        }
        ConfirmPara confirmPara = new Gson().fromJson(confirm.getConfirmPara(), ConfirmPara.class);
        if (confirmPara == null) {
            throw new PaasException("订单未绑定数据，不可以校验");
        }
        if (MyStringUtils.isBlank(confirmPara.getName()) || MyStringUtils.isBlank(confirmPara.getIdCard())) {
            throw new PaasException("订单未绑定数据，不可以校验");
        }
        SignRuTask task = signRuTaskService.getById(confirm.getMainId());
        if (task == null || MyStringUtils.isBlank(task.getUserId())) {
            throw new PaasException("订单错误");
        }
        //1、合作方后台上传身份信息
        //1)生成签名
        String appId = commonToolsAPI.getConfigStringValueByType("qcloud_face_app_id");
        List<String> values1 = new ArrayList<>();
        values1.add(appId);
        values1.add(properties.getVersion());
        String nonce1 = UUID.randomUUID().toString().replace("-", "");
        values1.add(nonce1);
        values1.add(task.getUserId().replace("-", ""));

        Object ticketObj = redisUtil.get(CommonConstant.PREFIX_FACE_SIGN_TICKET);
        if (ticketObj == null) {
            throw new PaasException("ticket失效");
        }
        String signTicket = ticketObj.toString();
        String sign1 = sign(values1, signTicket);
        //2)合作方后台上传身份信息
        //String orderNo1 = UUID.randomUUID().toString().replace("-", "");
        String orderNo1 = orderNo;
        String getAdvFaceIdUrl = properties.getManage().getGetAdvFaceUrl() + "?orderNo=" + orderNo1;
        Map<String, String> bodyMap1 = new HashMap<>();
        bodyMap1.put("appId", appId);
        bodyMap1.put("orderNo", orderNo1);
        bodyMap1.put("name", confirmPara.getName());
        bodyMap1.put("idNo", confirmPara.getIdCard());
        bodyMap1.put("userId", task.getUserId().replace("-", ""));
        bodyMap1.put("version", properties.getVersion());
        bodyMap1.put("sign", sign1);
        bodyMap1.put("nonce", nonce1);
        String requestBody = JSON.toJSONString(bodyMap1);
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Content-Type", "application/json");

        String result1 = queryAndRecord(CommonConstant.HTTP_POST, getAdvFaceIdUrl, headMap, null, requestBody);
        if (MyStringUtils.isBlank(result1)) {
            throw new PaasException("请求第三方服务失败");
        }
        JSONObject result1Json = JSON.parseObject(result1);
        if (result1Json == null) {
            throw new PaasException("请求第三方服务失败");
        }
        String code1 = result1Json.getString("code");
        if (!"0".equals(code1)) {
            throw new PaasException(result1Json.getString("msg"));
        }
        JSONObject result1JsonResult = result1Json.getJSONObject("result");
        if (result1JsonResult == null) {
            throw new PaasException("请求第三方返回数据异常");
        }
        String faceId = result1JsonResult.getString("faceId");
        if (MyStringUtils.isBlank(faceId)) {
            throw new PaasException("请求第三方返回数据异常");
        }
        String optimalDomain = result1JsonResult.getString("optimalDomain");
        if (MyStringUtils.isBlank(optimalDomain)) {
            optimalDomain = "kyc.qcloud.com";
        }
        //2、启动 H5 人脸核身
        //1）生成签名
        //获取 NONCE ticket
        Object tokenObj = redisUtil.get(CommonConstant.PREFIX_FACE_ACCESS_TOKEN);
        if (tokenObj == null) {
            throw new PaasException("token失效");
        }
        Map<String, String> params1 = new HashMap<>();
        params1.put("app_id", appId);
        params1.put("access_token", tokenObj.toString());
        params1.put("type", "NONCE");
        params1.put("version", properties.getVersion());
        params1.put("user_id", task.getUserId().replace("-", ""));
        String result2 = queryAndRecord(CommonConstant.HTTP_GET, properties.getManage().getApiTicketUrl(), null, params1, null);
        if (MyStringUtils.isBlank(result2)) {
            throw new PaasException("请求第三方服务失败");
        }
        JSONObject result2Json = JSON.parseObject(result2);
        if (result2Json == null) {
            throw new PaasException("请求第三方服务失败");
        }
        String code2 = result2Json.getString("code");
        if (!"0".equals(code2)) {
            throw new PaasException(result2Json.getString("msg"));
        }
        JSONArray ticketsArray = result2Json.getJSONArray("tickets");
        if (CollUtil.isEmpty(ticketsArray)) {
            throw new PaasException("请求第三方返回数据异常");
        }
        JSONObject ticketObject = ticketsArray.getJSONObject(0);
        if (ticketObject == null) {
            throw new PaasException("请求第三方返回数据异常");
        }
        String nonceTicket = ticketObject.getString("value");
        if (MyStringUtils.isBlank(nonceTicket)) {
            throw new PaasException("请求第三方返回数据异常");
        }
        //生成签名
        String nonce2 = UUID.randomUUID().toString().replace("-", "");
        List<String> values2 = new ArrayList<>();
        values2.add(appId);
        values2.add(orderNo);
        values2.add(task.getUserId().replace("-", ""));
        values2.add(properties.getVersion());
        values2.add(faceId);
        values2.add(nonce2);
        String sign2 = sign(values2, nonceTicket);
        //2）启动 H5 人脸核身
        StringBuffer faceUrl = new StringBuffer();
        faceUrl.append("https://");
        faceUrl.append(optimalDomain);
        faceUrl.append("/api/web/login");
        faceUrl.append("?appId=");
        faceUrl.append(appId);
        faceUrl.append("&version=");
        faceUrl.append(properties.getVersion());
        faceUrl.append("&nonce=");
        faceUrl.append(nonce2);
        faceUrl.append("&orderNo=");
        faceUrl.append(orderNo);
        faceUrl.append("&faceId=");
        faceUrl.append(faceId);
        faceUrl.append("&url=");
        faceUrl.append(redirectUrl);
        faceUrl.append("&from=browser");
        faceUrl.append("&userId=");
        faceUrl.append(task.getUserId().replace("-", ""));
        faceUrl.append("&sign=");
        faceUrl.append(sign2);
        faceUrl.append("&redirectType=1");

        //第一步校验删除
        SignUserConfirmStep confirmStepTem = signUserConfirmStepService.getByOrderNo(orderNo, 1);
        if (confirmStepTem != null) {
            confirmStepTem.setDeleteFlag(true);
            signUserConfirmStepService.updateById(confirmStepTem);
        }
        //记录第一步校验
        SignUserConfirmStep confirmStep = new SignUserConfirmStep();
        confirmStep.setConfirmId(orderNo);
        confirmStep.setStep(1);
        confirmStep.setConfirmType(ConfirmTypeEnum.FACE.getType());
        confirmStep.setDataType(ConfirmDataTypeEnum.FILE.getType());
        confirmStep.setSystemPara(faceUrl.toString());
        signUserConfirmStepService.save(confirmStep);

        return faceUrl.toString();
    }


    @Override
    public Boolean checkSign(String orderNo, String taskId, String operateType) {
        if (MyStringUtils.isBlank(orderNo) || MyStringUtils.isBlank(taskId) || MyStringUtils.isBlank(operateType)) {
            return false;
        }
        SignUserConfirm confirm = signUserConfirmService.getById(orderNo);
        if (confirm == null || !taskId.equals(confirm.getMainId())) {
            return false;
        }
        if (operateType.equals(ConfirmOperateType.SUBMIT_SIGN.getType())) {
            if (ruBusinessService.signConfirm(confirm.getMainId())) {
                if (!ConfirmTypeEnum.FACE.getType().equals(confirm.getConfirmType())) {
                    return false;
                }
            }
        }
        if (confirm.getFinalConfirmFlag() != null) {
            return confirm.getFinalConfirmFlag();
        } else if (ConfirmTypeEnum.FACE.getType().equals(confirm.getConfirmType())) {
            return updateFaceResult(orderNo);
        } else {
            return false;
        }
    }

    public static String sign(List<String> values, String ticket) { //values传ticket外的其他参数
        if (values == null) {
            throw new NullPointerException("values is null");
        }
        values.removeAll(Collections.singleton(null));// remove null
        values.add(ticket);
        Collections.sort(values);
        StringBuilder sb = new StringBuilder();
        for (String s : values) {
            sb.append(s);
        }
        return Hashing.sha1().hashString(sb, Charsets.UTF_8).toString().toUpperCase();
    }


    @Override
    public String queryAndRecord(String httpType, String url, Map<String, String> headers, Map<String, String> params, String requestBody) {
        String result = null;
        String reqPara = null;
        try {
            if (CommonConstant.HTTP_GET.equals(httpType)) {
                reqPara = CollUtil.isNotEmpty(params) ? params.toString() : "" + (CollUtil.isNotEmpty(headers) ? headers.toString() : "");
                result = HttpUtil.sendGet(url, headers, params);
            } else if (CommonConstant.HTTP_POST.equals(httpType)) {
                reqPara = MyStringUtils.isNotEmpty(requestBody) ? requestBody : "" + (CollUtil.isNotEmpty(headers) ? headers.toString() : "");
                result = HttpUtil.sendPost(url, headers, requestBody);
            }
            thirdServiceRecordService.saveThirdRecord("QCloud", url, reqPara, true, result, UUID.randomUUID().toString());
        } catch (Exception e) {
            e.printStackTrace();
            thirdServiceRecordService.saveThirdRecord("QCloud", url, reqPara, false, MyStringUtils.isNotBlank(result) ? result : e.getMessage(), UUID.randomUUID().toString());
            throw new PaasException("第三方接口请求失败");
        }
        return result;
    }

    @Override
    public void getQCloudToken() {
        String appId = commonToolsAPI.getConfigStringValueByType("qcloud_face_app_id");
        String secret = commonToolsAPI.getConfigStringValueByType("qcloud_face_secret");
        if (MyStringUtils.isNotBlank(appId)) {
            Map<String, String> params1 = new HashMap<>();
            params1.put("app_id", appId);
            params1.put("secret", secret);
            params1.put("grant_type", "client_credential");
            params1.put("version", properties.getVersion());
            String result1 = queryAndRecord(CommonConstant.HTTP_GET, properties.getManage().getAccessTokenUrl(), null, params1, null);
            if (MyStringUtils.isNotBlank(result1)) {
                log.info("tencent access:{}",result1);
                JSONObject jsonObject1 = JSONObject.parseObject(result1);
                String accessToken = jsonObject1.getString("access_token");
                String code1 = jsonObject1.getString("code");
                if ("0".equals(code1) && MyStringUtils.isNotBlank(accessToken)) {
                    redisUtil.set(CommonConstant.PREFIX_FACE_ACCESS_TOKEN, accessToken);
                    redisUtil.expire(CommonConstant.PREFIX_FACE_ACCESS_TOKEN, 30 * 60);
                    Map<String, String> params2 = new HashMap<>();
                    params2.put("app_id", appId);
                    params2.put("access_token", accessToken);
                    params2.put("type", "SIGN");
                    params2.put("version", properties.getVersion());
                    String result2 = queryAndRecord(CommonConstant.HTTP_GET, properties.getManage().getApiTicketUrl(), null, params2, null);
                    if (MyStringUtils.isNotBlank(result2)) {
                        log.info("tencent ticket:{}",result2);
                        JSONObject jsonObject2 = JSONObject.parseObject(result2);
                        String code2 = jsonObject1.getString("code");
                        if ("0".equals(code2)) {
                            JSONArray ticketsArray = jsonObject2.getJSONArray("tickets");
                            if (ticketsArray != null && ticketsArray.size() > 0) {
                                JSONObject ticketObject = ticketsArray.getJSONObject(0);
                                String signTicket = ticketObject.getString("value");
                                if (signTicket != null) {
                                    redisUtil.set(CommonConstant.PREFIX_FACE_SIGN_TICKET, signTicket);
                                    redisUtil.expire(CommonConstant.PREFIX_FACE_SIGN_TICKET, 30 * 60);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean updateFaceResult(String orderNo) {
        if (MyStringUtils.isBlank(orderNo)) {
            return false;
        }
        SignUserConfirm confirm = signUserConfirmService.getById(orderNo);
        if (confirm == null) {
            return false;
        }
        if (confirm.getFinalConfirmFlag() != null) {
            return confirm.getFinalConfirmFlag();
        }
        String result = getFaceResult(orderNo, "0");
        if (MyStringUtils.isBlank(result)) {
            throw new PaasException("请求第三方服务失败");
        }
        JSONObject resultJson = JSON.parseObject(result);
        if (resultJson == null) {
            throw new PaasException("请求第三方服务失败");
        }
        String code = resultJson.getString("code");
        boolean flag = false;
        if ("0".equals(code)) {
            flag = true;
        }
        //更新子数据
        SignUserConfirmStep step = signUserConfirmStepService.getByOrderNo(orderNo, 1);
        if (step != null) {
            String liveRate = resultJson.getString("liveRate");
            String similarity = resultJson.getString("similarity");
            FaceRerutnPara para = new FaceRerutnPara();
            para.setSimilarity(similarity);
            para.setLiveRate(liveRate);
            step.setUserPara(new Gson().toJson(para));
            step.setConfirmFlag(flag);
            signUserConfirmStepService.updateById(step);
        }
        //更新主数据
        confirm.setFinalConfirmFlag(flag);
        signUserConfirmService.updateById(confirm);

        return flag;
    }

    public String getFaceResult(String orderNo, String getFile) {
        String appId = commonToolsAPI.getConfigStringValueByType("qcloud_face_app_id");
        //1)生成签名
        List<String> values = new ArrayList<>();
        values.add(appId);
        values.add(properties.getVersion());
        String nonce = UUID.randomUUID().toString().replace("-", "");
        values.add(nonce);
        values.add(orderNo);
        Object ticketObj = redisUtil.get(CommonConstant.PREFIX_FACE_SIGN_TICKET);
        if (ticketObj == null) {
            throw new PaasException("ticket失效");
        }
        String signTicket = ticketObj.toString();
        String sign = sign(values, signTicket);
        //2)合作方后台上传身份信息
        String queryFaceRecordUrl = properties.getManage().getQueryFaceRecordUrl() + "?orderNo=" + orderNo;
        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("appId", appId);
        bodyMap.put("version", properties.getVersion());
        bodyMap.put("nonce", nonce);
        bodyMap.put("orderNo", orderNo);
        bodyMap.put("sign", sign);
        bodyMap.put("getFile", getFile);
        String requestBody = JSON.toJSONString(bodyMap);
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Content-Type", "application/json");
        String result = queryAndRecord(CommonConstant.HTTP_POST, queryFaceRecordUrl, headMap, null, requestBody);
        return result;
    }

    @Override
    public void bindFaceFile() {
        List<SignUserConfirmStep> steps = signUserConfirmStepService.getUnbindData(ConfirmTypeEnum.FACE.getType());
        if (CollUtil.isNotEmpty(steps)) {
            steps.forEach(s -> {
                String result = getFaceResult(s.getConfirmId(), "1");
                if (MyStringUtils.isNotBlank(result)) {
                    JSONObject resultJson = JSON.parseObject(result);
                    if (resultJson != null) {
                        String code = resultJson.getString("code");
                        if ("66660011".equals(code)) {
                            s.setBindDataFlag(2);
                        } else {
                            JSONObject jsonResult = resultJson.getJSONObject("result");
                            if (jsonResult != null) {
                                String photoBase64 = jsonResult.getString("photo");
                                String videoBase64 = jsonResult.getString("video");
                                if (MyStringUtils.isNotBlank(photoBase64) || MyStringUtils.isNotBlank(videoBase64)) {
                                    if (MyStringUtils.isNotBlank(photoBase64)) {
                                        //存图片
                                        annexStorageService.create(photoBase64, s.getId(), "face_photo", UUID.randomUUID() + ".jpg");
                                    }
                                    if (MyStringUtils.isNotBlank(videoBase64)) {
                                        //存视频
                                        annexStorageService.create(photoBase64, s.getId(), "face_video", UUID.randomUUID() + ".mp4");
                                    }

                                    s.setBindDataFlag(1);
                                }
                            }
                        }
                    }
                }
            });
        }
        signUserConfirmStepService.updateBatchById(steps);
    }

    @Override
    public String getMyConfirmType() {
        LoginUser user = MySecurityUtils.getCurrentUser();
        SysUserConfig config = sysUserConfigService.getConfigByType(user.getId(), SignCommonConstant.USERSIGNCONFIRMTYPEKEY);
        if (MyStringUtils.isBlank(config.getValue())) {
            return ConfirmTypeEnum.PHONE_EMAIL.getType();
        }

        return config.getValue();
    }

    @Override
    public SignReportSignSubVO getConfirmInfoByTaskId(String taskId) {
        SignUserConfirm userConfirm = signUserConfirmService.getByMainId(taskId);
        if (userConfirm == null) {
            return null;
        }
        SignUserConfirmStep userConfirmStep = signUserConfirmStepService.getByConfirmId(userConfirm.getId());
        if (userConfirmStep == null) {
            return null;
        }
        String confirmType = userConfirm.getConfirmType();


        SignReportSignSubVO signSubVO = new SignReportSignSubVO();
        signSubVO.setTitle("签署意愿校验");
        List<SignReportDocFileVo> subReport = new ArrayList<>();
        SignReportDocFileVo vo1 = new SignReportDocFileVo();
        vo1.setValueFirst("校验类型：");
        vo1.setValueSecond(ConfirmTypeEnum.getByType(confirmType).getName());
        subReport.add(vo1);

        if (ConfirmTypeEnum.FACE.getType().equals(confirmType)) {
            SignReportDocFileVo face1 = new SignReportDocFileVo();
            face1.setValueFirst("人脸识别校验通道：");
            face1.setValueSecond("腾讯云人脸识别校验");
            subReport.add(face1);
        }

        SignReportDocFileVo vo2 = new SignReportDocFileVo();
        vo2.setValueFirst("签署校验时间：");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        vo2.setValueSecond(format.format(userConfirmStep.getUpdateTime()));
        subReport.add(vo2);

        String confirmPara = userConfirm.getConfirmPara();
        if (MyStringUtils.isNotBlank(confirmPara)) {
            ConfirmPara confirm = JSON.parseObject(confirmPara, ConfirmPara.class);
            if (ConfirmTypeEnum.DOUBLE.getType().equals(confirmType) || ConfirmTypeEnum.PHONE_EMAIL.getType().equals(confirmType)) {
                String value = "";
                if (UpdatePasswordEnum.PHONE.getType().equals(userConfirmStep.getConfirmType())) {
                    if (MyStringUtils.isNotBlank(confirm.getPhone())) {
                        value = DesensitizationUtils.getPhone(confirm.getPhone());
                    }
                } else {
                    if (MyStringUtils.isNotBlank(confirm.getEmail())) {
                        value = DesensitizationUtils.getEmail(confirm.getEmail());
                    }
                }
                SignReportDocFileVo vo4 = new SignReportDocFileVo();
                vo4.setValueFirst("签署校验信息：");
                vo4.setValueSecond("接收验证码通道：" + value + "; 验证码：" + userConfirmStep.getSystemPara());
                subReport.add(vo4);
            }
        }

        if (ConfirmTypeEnum.FACE.getType().equals(confirmType)) {
            String userPara = userConfirmStep.getUserPara();
            if (MyStringUtils.isNotBlank(userPara)) {
                FaceRerutnPara para = JSON.parseObject(userPara, FaceRerutnPara.class);

                SignReportDocFileVo face2 = new SignReportDocFileVo();
                face2.setValueFirst("活体检测分数：");
                face2.setValueSecond(para.getLiveRate());
                subReport.add(face2);

                SignReportDocFileVo face3 = new SignReportDocFileVo();
                face3.setValueFirst("人脸比对分数：");
                face3.setValueSecond(para.getSimilarity());
                subReport.add(face3);
            }
        }

        signSubVO.setSubReport(subReport);

        return signSubVO;
    }
}