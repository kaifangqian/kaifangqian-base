/**
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
package com.kaifangqian.modules.system.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.kaifangqian.config.CommonConstants;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.enums.*;
import com.kaifangqian.modules.system.model.SysLoginModel;
import com.kaifangqian.modules.system.service.*;
import com.kaifangqian.modules.system.util.RandImageUtil;
import com.kaifangqian.modules.system.vo.*;
import com.kaifangqian.annotation.ResrunLogMethod;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.aspect.LicenseBean;
import com.kaifangqian.common.constant.*;
import com.kaifangqian.common.enums.OperateLogType;
import com.kaifangqian.common.redis.util.RedisUtil;
import com.kaifangqian.common.system.util.JwtUtil;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.system.vo.LoginUserAuthInfo;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.config.ResrunJobConfig;
import com.kaifangqian.dto.EmailDto;
import com.kaifangqian.dto.MessageDto;
import com.kaifangqian.entity.SysConfig;
import com.kaifangqian.enums.SysConfigType;
import com.kaifangqian.eunms.SendType;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.external.sign.response.SignAppInfoResponse;
import com.kaifangqian.external.sign.service.SignServiceManageExternal;
import com.kaifangqian.external.sms.request.MsgRequest;
import com.kaifangqian.external.sms.service.SmsSendService;
import com.kaifangqian.inteface.CommonToolsAPI;
import com.kaifangqian.modules.opensign.constant.SignCommonConstant;
import com.kaifangqian.modules.opensign.service.flow.IFlowService;
import com.kaifangqian.modules.storage.service.IAnnexBase64Service;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.enums.*;
import com.kaifangqian.modules.system.vo.*;
import com.kaifangqian.service.ISysConfigService;
import com.kaifangqian.utils.*;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import com.kaifangqian.common.util.*;
import com.kaifangqian.modules.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author zhh
 */
@RestController
@RequestMapping("/sys")
// @Api(tags = "用户登录")
@Slf4j
@ResrunLogModule(name = "登录模块")
public class LoginController {
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private CommonToolsAPI commonToolsAPI;
    @Autowired
    private ISysUserDepartService iSysUserDepartService;
    @Autowired
    private RedisLuaScriptUtils redisLuaScriptUtils;
    @Autowired
    private JobUtils jobUtils;
    @Autowired
    private ResrunJobConfig resrunJobConfig;
    @Autowired
    private SysMessageUtil sysMessageUtil;
    @Autowired
    private ISysTenantUserService iSysTenantUserService;
    @Autowired
    private ISysTenantUserAppService iSysTenantUserAppService;
    @Autowired
    private ISysTenantInfoService sysTenantInfoService;
    @Autowired
    private ISysTenantInfoService iSysTenantInfoService;
    @Autowired
    private ISysAppInfoService iSysAppInfoService;
    @Autowired
    private IFlowService flowService;
    @Autowired
    private ISysAppInfoService sysAppInfoService;
    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private ITenantInfoExtendService tenantInfoExtendService;
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IAnnexBase64Service annexBase64Service;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private SignServiceManageExternal signServiceManageExternal;
    @Autowired
    private SmsSendService smsSendService;

    /**
     * 校验系统token有效性
     */
    // @ApiOperation("校验系统token有效性")
    @GetMapping(value = "/check")
    @ResrunLogMethod(name = "校验系统token有效性", operateType = OperateLogType.OPERATE_TYPE_0)
    public Result<String> check() {
        return Result.OK();
    }


    /**
     * 校验系统token有效性
     */
    // @ApiOperation("校验系统token有效性")
    @GetMapping(value = "/check2")
    @ResrunLogMethod(name = "校验系统token有效性", operateType = OperateLogType.OPERATE_TYPE_0)
    public Result<Boolean> check2(@RequestParam(name = "token", required = true) String token) {
        Object cacheToken = redisUtil.get(CommonConstant.PREFIX_USER_TOKEN + token);
        if (cacheToken != null) {
            return Result.OK(true);
        } else {
            return Result.OK(false);
        }
    }

    /**
     * 获取账号类型
     */
    // @ApiOperation("获取账号类型")
    @GetMapping(value = "/getAcountType")
    @ResrunLogMethod(name = "获取账号类型", operateType = OperateLogType.OPERATE_TYPE_0)
    public Result<String> getAcountType() {
        String acountType = "phone";
        SysConfig config = sysConfigService.getByType("system_account_type");
        if (config != null) {
            acountType = config.getValue();
        }
        return Result.OK("ok", acountType);
    }


    /**
     * 后台生成图形验证码
     *
     * @param key
     */
    // @ApiOperation("获取验证码")
    @GetMapping(value = "/randomImage/{key}")
    @ResrunLogMethod(name = "获取验证码", operateType = OperateLogType.OPERATE_TYPE_0)
    public Result<?> randomImage(@PathVariable String key) {
        Result<Map<String, String>> res = new Result<>();
        try {
            String code = RandomUtil.randomString(CommonConstants.BASE_CHECK_CODES, 4);
            String lowerCaseCode = code.toLowerCase();
            String realKey = MD5Util.MD5Encode(lowerCaseCode + key, "utf-8");
            redisUtil.set(realKey, lowerCaseCode, 60);
            String base64 = RandImageUtil.generate(code);
            res.setSuccess(true);
            Map<String, String> data = new HashMap<>();
            data.put("captchaImg", base64);
            res.setResult(data);
        } catch (Exception e) {
            res.error500("获取验证码出错" + e.getMessage());
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 发送短信
     * 19508663	登录确认:login1
     * 19510614	异地登录验证:login2
     * 19515615	修改密码:editPassword
     * 19511633	注册:register
     *
     * @param type
     */
    // @ApiOperation("发送短信")
    @GetMapping(value = "/sendMessage")
    @ResrunLogMethod(name = "发送短信", operateType = OperateLogType.OPERATE_TYPE_0)
    public Result<?> sendMessage(String type, @RequestParam(name = "phone", required = true) String phone,@RequestParam String captchaVerification) {

        CaptchaVO vo = new CaptchaVO();
        vo.setCaptchaVerification(captchaVerification);
        ResponseModel responseModel = captchaService.verification(vo);
        if(!responseModel.isSuccess()){
            return Result.error("请完成安全验证后再发送验证码");
        }
        SysConfig sendMessageConfig = commonToolsAPI.getConfigByType(SysConfigType.SENDMESSAGE.getType());
        String key = UUID.randomUUID().toString();
        String code;
        if (sendMessageConfig != null && "true".equals(sendMessageConfig.getValue())) {
            code = RandomUtil.randomNumbers(6);
        } else {
            code = CommonConstants.DEFAULTCODE;
        }
        String realKey = MD5Util.MD5Encode(phone + key, "utf-8");
        redisUtil.set(realKey, code, 5 * 60);

        SysConfig isEcup = commonToolsAPI.getConfigByType("sms_channel");
        if(isEcup != null && "resrun".equals(isEcup.getValue()) && sendMessageConfig != null && "true".equals(sendMessageConfig.getValue())){
            MsgRequest msgRequest = new MsgRequest();
            msgRequest.setPhoneNumbers(phone);
            msgRequest.setTemplateName("captcha");
            Map<String, String> para = new HashMap<>();
            para.put("code", code);
            msgRequest.setParams(para);
            smsSendService.sendMsg(msgRequest);
        }else{
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
        }

        return Result.OK("短信发送成功", key);
    }

    /**
     * 发送邮件
     *
     * @param type
     */
    // @ApiOperation("发送邮件")
    @GetMapping(value = "/sendEmail")
    @ResrunLogMethod(name = "发送邮件", operateType = OperateLogType.OPERATE_TYPE_0)
    public Result<?> sendEmail(@RequestParam(name = "type", required = true) String type,
                               @RequestParam(name = "email", required = true) String email,
                               @RequestParam String captchaVerification) {

        CaptchaVO vo = new CaptchaVO();
        vo.setCaptchaVerification(captchaVerification);
        ResponseModel responseModel = captchaService.verification(vo);
        if(!responseModel.isSuccess()){
            return Result.error("请完成安全验证后再发送验证码");
        }
        String key = UUID.randomUUID().toString();
        String code = RandomUtil.randomNumbers(4);
        String realKey = MD5Util.MD5Encode(email + key, "utf-8");
        redisUtil.set(realKey, code, 5 * 60);

        //log.info(code);
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

        return Result.OK("邮件发送成功", key);
    }

    /**
     * 给登录用户发送短信
     *
     * @param type
     */
    // @ApiOperation("给登录用户发送短信")
    @GetMapping(value = "/sendMessageByToken")
    @ResrunLogMethod(name = "给登录用户发送短信", operateType = OperateLogType.OPERATE_TYPE_0)
    public Result<?> sendMessageByToken(@RequestParam(name = "type", required = true) String type,String captchaVerification) {
        String phone = null;
        LoginUser user = MySecurityUtils.getCurrentUser();
        if (user != null) {
            phone = user.getPhone();
            if (MyStringUtils.isBlank(phone)) {
                return Result.error("当前用户未绑定手机号");
            }
        } else {
            return Result.error("未登录用户");
        }

        CaptchaVO vo = new CaptchaVO();
        vo.setCaptchaVerification(captchaVerification);
        ResponseModel responseModel = captchaService.verification(vo);
        if(!responseModel.isSuccess()){
            return Result.error("请完成安全验证后再发送验证码");
        }

        String key = UUID.randomUUID().toString();
        SysConfig sendMessageConfig = commonToolsAPI.getConfigByType(SysConfigType.SENDMESSAGE.getType());
        String code;
        if (sendMessageConfig != null && "true".equals(sendMessageConfig.getValue())) {
            code = RandomUtil.randomNumbers(6);
        } else {
            code = CommonConstants.DEFAULTCODE;
        }
        String realKey = MD5Util.MD5Encode(phone + key, "utf-8");
        redisUtil.set(realKey, code, 5 * 60);

        SysConfig isEcup = commonToolsAPI.getConfigByType("sms_channel");
        if(isEcup != null && "resrun".equals(isEcup.getValue()) && sendMessageConfig != null && "true".equals(sendMessageConfig.getValue())){
            MsgRequest msgRequest = new MsgRequest();
            msgRequest.setPhoneNumbers(phone);
            msgRequest.setTemplateName("captcha");
            Map<String, String> para = new HashMap<>();
            para.put("code", code);
            msgRequest.setParams(para);
            smsSendService.sendMsg(msgRequest);
        }else{
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
        }
        return Result.OK("短信发送成功", key);
    }

    /**
     * 给当前用户发送邮件
     *
     * @param type
     */
    // @ApiOperation("给当前用户发送邮件")
    @GetMapping(value = "/sendEmailByToken")
    @ResrunLogMethod(name = "给当前用户发送邮件", operateType = OperateLogType.OPERATE_TYPE_0)
    public Result<?> sendEmail(@RequestParam(name = "type", required = true) String type,String captchaVerification) {
        String email = null;
        LoginUser user = MySecurityUtils.getCurrentUser();
        if (user != null) {
            email = user.getEmail();
            if (MyStringUtils.isBlank(email)) {
                return Result.error("当前用户未绑定邮箱");
            }
        } else {
            return Result.error("未登录用户");
        }

        CaptchaVO vo = new CaptchaVO();
        vo.setCaptchaVerification(captchaVerification);
        ResponseModel responseModel = captchaService.verification(vo);
        if(!responseModel.isSuccess()){
            return Result.error("请完成安全验证后再发送验证码");
        }

        String key = UUID.randomUUID().toString();
        String code = RandomUtil.randomNumbers(4);
        String realKey = MD5Util.MD5Encode(email + key, "utf-8");
        redisUtil.set(realKey, code, 5 * 60);

        log.info(code);
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

        return Result.OK("邮件发送成功", key);
    }

    /**
     * 通过用户名发送短信
     *
     * @param type
     */
    // @ApiOperation("通过用户名发送短信")
    @GetMapping(value = "/sendMessageByUsername")
    @ResrunLogMethod(name = "通过用户名发送短信", operateType = OperateLogType.OPERATE_TYPE_0)
    public Result<?> sendMessageByUsername(@RequestParam(name = "type", required = true) String type, @RequestParam(name = "username", required = true) String username,String captchaVerification) {
        CaptchaVO vo = new CaptchaVO();
        vo.setCaptchaVerification(captchaVerification);
        ResponseModel responseModel = captchaService.verification(vo);
        if(!responseModel.isSuccess()){
            return Result.error("请完成安全验证后再发送验证码");
        }

        SysUser sysUser = sysUserService.getUserByPhone(username);
        String phone = null;
        if (sysUser != null) {
            phone = sysUser.getPhone();
        }
        if (MyStringUtils.isBlank(phone)) {
            return Result.error("当前用户未绑定手机号");
        }


        String key = UUID.randomUUID().toString();
        SysConfig sendMessageConfig = commonToolsAPI.getConfigByType(SysConfigType.SENDMESSAGE.getType());
        String code;
        if (sendMessageConfig != null && "true".equals(sendMessageConfig.getValue())) {
            code = RandomUtil.randomNumbers(6);
        } else {
            code = CommonConstants.DEFAULTCODE;
        }
        String realKey = MD5Util.MD5Encode(phone + key, "utf-8");
        redisUtil.set(realKey, code, 5 * 60);

        SysConfig isEcup = commonToolsAPI.getConfigByType("sms_channel");
        if(isEcup != null && "resrun".equals(isEcup.getValue()) && sendMessageConfig != null && "true".equals(sendMessageConfig.getValue())){
            MsgRequest msgRequest = new MsgRequest();
            msgRequest.setPhoneNumbers(phone);
            msgRequest.setTemplateName("captcha");
            Map<String, String> para = new HashMap<>();
            para.put("code", code);
            msgRequest.setParams(para);
            smsSendService.sendMsg(msgRequest);
        }else{
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
        }



        return Result.OK("短信发送成功", key);
    }

    /**
     * 通过用户名发送邮件
     *
     * @param type
     */
    // @ApiOperation("通过用户名发送邮件")
    @GetMapping(value = "/sendEmailByUsername")
    @ResrunLogMethod(name = "通过用户名发送邮件", operateType = OperateLogType.OPERATE_TYPE_0)
    public Result<?> sendEmailByUsername(@RequestParam(name = "type", required = true) String type, @RequestParam(name = "username", required = true) String username,String captchaVerification) {
        CaptchaVO vo = new CaptchaVO();
        vo.setCaptchaVerification(captchaVerification);
        ResponseModel responseModel = captchaService.verification(vo);
        if(!responseModel.isSuccess()){
            return Result.error("请完成安全验证后再发送验证码");
        }
        SysUser sysUser = sysUserService.getUserByEmail(username);
        String email = null;
        if (sysUser != null) {
            email = sysUser.getEmail();
        }
        if (MyStringUtils.isBlank(email)) {
            return Result.error("当前用户未绑定邮箱");
        }


        String key = UUID.randomUUID().toString();
        String code = RandomUtil.randomNumbers(4);
        String realKey = MD5Util.MD5Encode(email + key, "utf-8");
        redisUtil.set(realKey, code, 5 * 60);

        log.info(code);
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

        return Result.OK("邮件发送成功", key);
    }

    /**
     * 图形验证码
     *
     * @param sysLoginModel
     * @return
     */
    @ResrunLogMethod(name = "校验验证码", operateType = OperateLogType.OPERATE_TYPE_0)
    @RequestMapping(value = "/checkCaptcha", method = RequestMethod.POST)
    public Result<?> checkCaptcha(@RequestBody SysLoginModel sysLoginModel) {
        String captcha = sysLoginModel.getCaptcha();
        String checkKey = sysLoginModel.getCheckKey();
        if (captcha == null) {
            return Result.error("验证码无效");
        }
        String lowerCaseCaptcha = captcha.toLowerCase();
        String realKey = MD5Util.MD5Encode(lowerCaseCaptcha + checkKey, "utf-8");
        Object checkCode = redisUtil.get(realKey);
        if (checkCode == null || !checkCode.equals(lowerCaseCaptcha)) {
            return Result.error("验证码错误");
        }
        return Result.OK();
    }

    // @ApiOperation("用户名密码登录接口")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResrunLogMethod(name = "用户名密码登录", operateType = OperateLogType.OPERATE_TYPE_7)
    public Result<?> login(@RequestBody SysLoginModel sysLoginModel, HttpServletRequest request) {
        Result<JSONObject> result = new Result<>();
        String username = sysLoginModel.getUsername();
        String password = sysLoginModel.getPassword();
        String captcha = sysLoginModel.getCaptcha();
        if (captcha == null) {
            result.error500("验证码不能为空");
            return result;
        }
        String lowerCaseCaptcha = captcha.toLowerCase();
        String realKey = MD5Util.MD5Encode(lowerCaseCaptcha + sysLoginModel.getCheckKey(), "utf-8");
        Object checkCode = redisUtil.get(realKey);
        if (checkCode == null) {
            result.error500("验证码超时");
            return result;
        }
        //当进入登录页时，有一定几率出现验证码错误
        if (!checkCode.toString().equals(lowerCaseCaptcha)) {
            result.error500("验证码错误");
            return result;
        }
        //删除redis中的验证码
        redisUtil.del(realKey);
        //1. 校验用户是否有效
        SysUser sysUser = sysUserService.getUserByName(username);
        result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }

        //2. 校验用户名或密码是否正确
        String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
        String syspassword = sysUser.getPassword();
        if (!syspassword.equals(userpassword)) {
            //3. 密码错误锁定账号
            Integer loginRepeatLimit = commonToolsAPI.getConfigIntegerValueByType(SysConfigType.LOGINREPEATLIMIT.getType());
            if (loginRepeatLimit > 0) {
                String key = CommonConstant.USER_FREEZE_PRE_KEY + username;
                if (!redisLuaScriptUtils.checkRepeatLimit(key, loginRepeatLimit, CommonConstant.USER_FREEZE_TIME * 60 * 60)) {
                    //冻结用户
                    sysUser.setStatus(CommonConstant.USER_FREEZE_AUTO);
                    Calendar calendar = Calendar.getInstance();
                    /* HOUR_OF_DAY 指示一天中的小时 */
                    calendar.setTime(new Date());
                    calendar.add(Calendar.HOUR_OF_DAY, CommonConstant.USER_FREEZE_TIME);
                    sysUser.setFreezeTime(calendar.getTime());
                    sysUserService.updateById(sysUser);
                    //解冻-通知调度服务解冻
                    jobUtils.runJob(13L, username, CommonConstant.USER_FREEZE_TIME * 1000 * 60 * 60);
                }
            }
            result.error500("用户名或密码错误");
            return result;
        }
        //4. 是否为异地验证
        SysConfig loginConfig = commonToolsAPI.getConfigByType(SysConfigType.OFFSITELOGIN.getType());
        String ip = IPUtil.getIp(request);
        String ipCity = IPUtil.getCityInfo(ip);
        if (loginConfig != null && TrueFalseConstant.trueStr.equals(loginConfig.getValue()) && MyStringUtils.isNotBlank(sysUser.getLastTimeLoadSite()) && !sysUser.getLastTimeLoadSite().equals(ipCity)) {
            result.setCode(HTTPCodeConstant.SECONDARY_VERIFICATION);
            result.setMessage("检测您账号异地登录，为保护您的账号安全，须进行短信验证码校验");
            JSONObject obj = new JSONObject();
            if(StringUtils.isNotBlank(sysUser.getPhone())){
                obj.put("phone", sysUser.getPhone());
            }else if(StringUtils.isNotBlank(sysUser.getEmail())){
                obj.put("email", sysUser.getEmail());
            }else{
                obj.put("phone", null);
            }
            result.setResult(obj);
            return result;
        } else {
            sysUser.setLastTimeLoadSite(ipCity);
            sysUserService.updateById(sysUser);
        }
        //5. 长时间未登录验证
        Integer longTimeNoLogin = commonToolsAPI.getConfigIntegerValueByType(SysConfigType.LONGTIMENOLOGIN.getType());
        if (longTimeNoLogin != 0 && sysUser.getLastTimeLoadTime() != null && DateUtil.between(DateUtil.date(), sysUser.getLastTimeLoadTime(), DateUnit.DAY) > longTimeNoLogin) {
            result.setCode(HTTPCodeConstant.SECONDARY_VERIFICATION);
            result.setMessage("由于您长时间未登录系统，为保护您的账号安全，须进行短信验证码校验");
            JSONObject obj = new JSONObject();
            obj.put("phone", DesensitizationUtils.getPhone(sysUser.getPhone()));
            result.setResult(obj);
            return result;
        } else {
            sysUser.setLastTimeLoadTime(new Date());
            sysUserService.updateById(sysUser);
        }

        //用户登录信息
        userInfo(sysUser, result, sysLoginModel.getAppCode());
        return result;
    }


    /**
     * 手机号登录接口
     *
     * @param
     * @return
     */
    // @ApiOperation("手机号登录接口")
    @PostMapping("/phoneLogin")
    @ResrunLogMethod(name = "手机号登录", operateType = OperateLogType.OPERATE_TYPE_7)
    public Result<?> phoneLogin(@Valid @RequestBody CaptchLoginVO vo, HttpServletRequest request) {
        if (MyStringUtils.isBlank(vo.getPhone())) {
            if (MyStringUtils.isNotBlank(vo.getUsername())) {
                SysUser sysUser = sysUserService.getUserByName(vo.getUsername());
                if (sysUser != null) {
                    vo.setPhone(sysUser.getPhone());
                }
                if (MyStringUtils.isBlank(vo.getPhone())) {
                    return Result.error("当前用户未绑定手机号");
                }
            } else {
                return Result.error("手机号不能为空");
            }
        }
        String realKey = MD5Util.MD5Encode(vo.getPhone() + vo.getCaptchaKey(), "utf-8");
        Object checkCode = redisUtil.get(realKey);
        if (checkCode == null || !checkCode.toString().equals(vo.getCaptcha())) {
            return Result.error("验证码错误");
        }
        //删除redis中的验证码
        redisUtil.del(realKey);

        //校验用户有效性
        SysUser sysUser = sysUserService.getUserByPhone(vo.getPhone());
        Result result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }

        //记录登录地点
        String ip = IPUtil.getIp(request);
        String ipCity = IPUtil.getCityInfo(ip);
        sysUser.setLastTimeLoadSite(ipCity);
        sysUser.setLastTimeLoadTime(new Date());
        sysUserService.updateById(sysUser);

        //用户信息
        userInfo(sysUser, result, vo.getAppCode());

        return result;
    }


    /**
     * 验证码登录注册接口
     *
     * @param
     * @return
     */
    // @ApiOperation("验证码登录注册接口")
    @PostMapping("/codeRegisterLogin")
    @ResrunLogMethod(name = "验证码登录注册接口", operateType = OperateLogType.OPERATE_TYPE_7)
    public Result<?> phoneRegisterLogin(@Valid @RequestBody CaptchLoginVO vo, HttpServletRequest request) {
        if (MyStringUtils.isBlank(vo.getPhone()) && MyStringUtils.isBlank(vo.getEmail())) {
            return Result.error("验证码通道不能为空");
        }
        if (MyStringUtils.isBlank(vo.getCaptchaKey()) || MyStringUtils.isBlank(vo.getCaptcha())) {
            return Result.error("校验码不能为空");
        }
        String channel = "";
        if (MyStringUtils.isNotBlank(vo.getPhone())) {
            channel = vo.getPhone();
        } else {
            channel = vo.getEmail();
        }
        String realKey = MD5Util.MD5Encode(channel + vo.getCaptchaKey(), "utf-8");
        Object checkCode = redisUtil.get(realKey);
        if (checkCode == null || !checkCode.toString().equals(vo.getCaptcha())) {
            return Result.error("验证码错误");
        }
        //删除redis中的验证码
        redisUtil.del(realKey);

        //校验用户有效性
        SysUser sysUser = null;
        if (MyStringUtils.isNotBlank(vo.getPhone())) {
            sysUser = sysUserService.getUserByPhone(vo.getPhone());
        } else {
            sysUser = sysUserService.getUserByEmail(vo.getEmail());
        }
        if (sysUser == null) {
            sysUser = addPersonalTenant(vo);
        } else {
            if (MyStringUtils.isBlank(vo.getAppCode())) {
                throw new PaasException(CommonConstant.USER_LACK_INFO);
            }
            SysAppInfo sysAppInfo = iSysAppInfoService.getByAppCode(vo.getAppCode());
            List<SysTenantInfo> tenants = iSysTenantUserService.getTenantsByUserIdAndAppId(sysUser.getId(), sysAppInfo.getId());
            if (CollUtil.isEmpty(tenants)) {
                sysUser = addPersonalTenant(vo);
            }
        }
        Result result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }

        //记录登录地点
        String ip = IPUtil.getIp(request);
        String ipCity = IPUtil.getCityInfo(ip);
        sysUser.setLastTimeLoadSite(ipCity);
        sysUser.setLastTimeLoadTime(new Date());
        sysUserService.updateById(sysUser);

        //用户信息
        userInfo(sysUser, result, vo.getAppCode());

        return result;
    }

    private SysUser addPersonalTenant(CaptchLoginVO vo) {
        SysUser sysUser = null;
        //开通租户
        SysTenantInfoVO sysTenantInfoVO = new SysTenantInfoVO();
        if (MyStringUtils.isNotBlank(vo.getPhone())) {
            sysTenantInfoVO.setUsername(vo.getPhone());
            sysTenantInfoVO.setRealName(vo.getPhone());
            sysTenantInfoVO.setPhone(vo.getPhone());
            sysTenantInfoVO.setTenantName(vo.getPhone());
        } else {
            sysTenantInfoVO.setUsername(vo.getEmail());
            sysTenantInfoVO.setRealName(vo.getEmail());
            sysTenantInfoVO.setEmail(vo.getEmail());
            sysTenantInfoVO.setTenantName(vo.getEmail());
        }
        sysTenantInfoVO.setPassword(vo.getPassword());
        sysTenantInfoVO.setTenantType(TenantType.PERSONAL.getType());

        SysAddTenantInfoVO sysAddTenantInfoVO = sysTenantInfoService.addTenantAndAddApp(sysTenantInfoVO);

        String tenantId = sysAddTenantInfoVO.getTenantId();

        //维护租户扩展信息
        TenantInfoExtend tenantInfoExtend = new TenantInfoExtend();
        tenantInfoExtend.setTenantId(tenantId);
        tenantInfoExtend.setTenantType(TenantType.PERSONAL.getType());
        String phone = vo.getPhone();
        try {
            phone = RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING, phone);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tenantInfoExtend.setPhone(phone);
        String email = vo.getEmail();
        try {
            email = RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING, email);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tenantInfoExtend.setEmail(email);
        //未认证
        tenantInfoExtend.setAuthStatus(TenantAuthStatus.STATUS0.getStatus());
        tenantInfoExtendService.save(tenantInfoExtend);

        //解析数据库已有数据做绑定表：sign_ru_relation sign_ru_task
        if (MyStringUtils.isNotBlank(vo.getPhone())) {
            sysUser = iSysUserService.getUserByPhone(vo.getPhone());
        } else if (MyStringUtils.isNotBlank(vo.getEmail())) {
            sysUser = iSysUserService.getUserByEmail(vo.getEmail());
        }
        if (sysUser != null) {
            SysTenantUser tenantUser = iSysTenantUserService.getPersonalTenantUser(sysUser.getId());
            if (tenantUser != null) {
                if (MyStringUtils.isNotBlank(vo.getPhone())) {
                    //解析数据库已有数据做绑定表：sign_ru_relation
                    flowService.bindOutUserRelation(vo.getPhone(), null, tenantUser.getId(), "register");
                    //解析数据库已有数据做绑定表：sign_ru_task
                    //绑定手机号：账号ID
                    flowService.bindOutUserTask(null, vo.getPhone(), null, null, null, sysUser.getId(), null, null, "register");
                    //绑定手机号+租户名（个人租户）：租户ID，租户账号ID
                    flowService.bindOutUserTask("个人租户", vo.getPhone(), null, null, null, tenantUser.getUserId(), tenantUser.getTenantId(), tenantUser.getId(), "register");
                } else if (MyStringUtils.isNotBlank(vo.getEmail())) {
                    //解析数据库已有数据做绑定表：sign_ru_relation
                    flowService.bindOutUserRelation(null, vo.getEmail(), tenantUser.getId(), "register");
                    //解析数据库已有数据做绑定表：sign_ru_task
                    //绑定手机号：账号ID
                    flowService.bindOutUserTask(null, null, vo.getEmail(), null, null, sysUser.getId(), null, null, "register");
                    //绑定手机号+租户名（个人租户）：租户ID，租户账号ID
                    flowService.bindOutUserTask("个人租户", null, vo.getEmail(), null, null, tenantUser.getUserId(), tenantUser.getTenantId(), tenantUser.getId(), "register");
                }
            }
        }
        return sysUser;
    }

    /**
     * 游客登录接口
     *
     * @param
     * @return
     */
    // @ApiOperation("游客登录接口")
    @PostMapping("/visitorLogin")
    @ResrunLogMethod(name = "游客登录接口", operateType = OperateLogType.OPERATE_TYPE_7)
    public Result<?> visitorLogin(@Valid @RequestBody CaptchLoginVO vo, HttpServletRequest request) {
        String realKey = MD5Util.MD5Encode(vo.getPhone() + vo.getCaptchaKey(), "utf-8");
        Object checkCode = redisUtil.get(realKey);
        if (checkCode == null || !checkCode.toString().equals(vo.getCaptcha())) {
            return Result.error("验证码错误");
        }
        //删除redis中的验证码
        redisUtil.del(realKey);

        //校验用户有效性
        SysUser sysUser = sysUserService.getUserByPhone(vo.getPhone());
        if (sysUser == null) {
            //注册
            sysUser = new SysUser();
            sysUser.setPhone(vo.getPhone());
            sysUser.setPassword(vo.getPassword());
            sysUserService.saveVisitor(sysUser);
        }
        Result result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }

        //记录登录地点
        String ip = IPUtil.getIp(request);
        String ipCity = IPUtil.getCityInfo(ip);
        sysUser.setLastTimeLoadSite(ipCity);
        sysUser.setLastTimeLoadTime(new Date());
        sysUserService.updateById(sysUser);

        //用户信息
        userInfo(sysUser, result, vo.getAppCode());

        return result;
    }

    /**
     * 获取用户的租户列表
     *
     * @return
     */
    @ResrunLogMethod(name = "获取用户的租户-部门列表", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/getMyTenantDeparts", method = RequestMethod.GET)
    public Result<?> getMyTenants() {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        List<SysTenantInfo> tenants = iSysTenantUserService.getTenantsByUserId(loginUser.getId());
        return getUserTenantDeparts(loginUser, tenants);
    }

    private Result<?> getUserTenantDeparts(LoginUser loginUser, List<SysTenantInfo> tenants) {
        List<UserTenantDepartVO> UserTenantDepartVO = new ArrayList<>();
        if (CollUtil.isNotEmpty(tenants)) {
            for (SysTenantInfo info : tenants) {
                List<SysDepart> departs = sysDepartService.queryUserDeparts(loginUser.getId(), info.getId());
                if (CollUtil.isNotEmpty(departs)) {
                    UserTenantDepartVO userTenantDepartVO = new UserTenantDepartVO();
                    userTenantDepartVO.setTenantId(info.getId());
                    userTenantDepartVO.setTenantName(info.getTenantName());
                    userTenantDepartVO.setTenantType(info.getTenantType());
                    if (TenantStatus.ENABLE.getType().equals(info.getTenantStatus())) {
                        userTenantDepartVO.setUseFlag(true);
                    }

                    List<UserDepartVO> departVOS = new ArrayList<>();
                    departs.forEach(d -> {
                        UserDepartVO userDepartVO = new UserDepartVO();
                        userDepartVO.setDepartId(d.getId());
                        userDepartVO.setDepartName(d.getDepartName());
                        userDepartVO.setTenantId(d.getTenantId());
                        if (d.getId().equals(loginUser.getDepartId())) {
                            userDepartVO.setSelectFlag(true);
                            userTenantDepartVO.setSelectFlag(true);
                        }

                        departVOS.add(userDepartVO);
                    });
                    TenantInfoExtend tenantInfoExtend = tenantInfoExtendService.getTenantInfoByTenantId(info.getId());
                    if(tenantInfoExtend != null && tenantInfoExtend.getAuthStatus() != null){
                        userTenantDepartVO.setAuthStatus(tenantInfoExtend.getAuthStatus());
                    }else{
                        userTenantDepartVO.setAuthStatus(TenantAuthStatus.STATUS0.getStatus());
                    }
                    userTenantDepartVO.setDeparts(departVOS);
                    userTenantDepartVO.setDepartId(departVOS.get(0).getDepartId());


                    UserTenantDepartVO.add(userTenantDepartVO);
                }
            }
        }

        return Result.OK(UserTenantDepartVO);
    }


    /**
     * 获取用户当前应用的租户列表
     *
     * @return
     */
    @ResrunLogMethod(name = "获取用户当前应用的租户列表", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/getMyAppTenantDeparts", method = RequestMethod.GET)
    public Result<?> getMyAppTenantDeparts(String appCode) {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        if (MyStringUtils.isBlank(appCode)) {
            appCode = loginUser.getAppCode();
        }
        SysAppInfo sysAppInfo = iSysAppInfoService.getByAppCode(appCode);
        if (sysAppInfo != null) {
            List<SysTenantInfo> tenants = iSysTenantUserService.getTenantsByUserIdAndAppId(loginUser.getId(), sysAppInfo.getId());
            return getUserTenantDeparts(loginUser, tenants);
        } else {
            throw new PaasException("应用不存在");
        }
    }

    /**
     * 切换用户租户、部门
     *
     * @param userAuthInfo
     * @return
     */
    @ResrunLogMethod(name = "切换用户租户、部门", operateType = OperateLogType.OPERATE_TYPE_0)
    @RequestMapping(value = "/updateTenantDepart", method = RequestMethod.PUT)
    public Result<?> updateTenantDepart(@RequestBody LoginUserAuthInfo userAuthInfo) {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        if (MyStringUtils.isBlank(userAuthInfo.getDepartId())) {
            return Result.error("信息不全，切换失败！");
        }
        SysDepart sysDepart = sysDepartService.getById(userAuthInfo.getDepartId());
        if (sysDepart == null) {
            return Result.error("信息错误，切换失败！");
        }
        userAuthInfo.setOrgCode(sysDepart.getOrgCode());
        userAuthInfo.setTenantId(sysDepart.getTenantId());
        if (!iSysTenantUserService.checkUserTenant(loginUser.getId(), sysDepart.getTenantId())) {
            return Result.error("租户错误，切换失败！");
        }
        Object cacheToken = redisUtil.get(CommonConstant.PREFIX_USER_TOKEN + loginUser.getToken());
        if (oConvertUtils.isNotEmpty(cacheToken)) {
            LoginUserAuthInfo temp = (LoginUserAuthInfo) cacheToken;
            if (MyStringUtils.isNotBlank(temp.getAuthorizedToken())) {
                userAuthInfo.setAuthorizedToken(temp.getAuthorizedToken());
            } else {
                throw new PaasException("授权token错误");
            }
        } else {
            throw new PaasException("登录超时");
        }
//        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + loginUser.getToken(), userAuthInfo);
//        //更新最后登录部门信息
//        SysUser sysUser = new SysUser();
//        sysUser.setId(loginUser.getId());
//        sysUser.setDepartId(userAuthInfo.getDepartId());
//        sysUserService.updateById(sysUser);
//
//        return Result.OK();

        //失效以前的token --  暂时不失效，防止无法比较token
        //redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + loginUser.getToken());
        //生成新的token
        //更新当前用户登录信息
        double noOpv = commonToolsAPI.getConfigDoubleValueByType(SysConfigType.NOOPERATEKEEPALIVE.getType());
        double maxV = commonToolsAPI.getConfigDoubleValueByType(SysConfigType.MAXKEEPALIVE.getType());
        //生成token
        String token = JwtUtil.sign(loginUser.getUsername(), loginUser.getPassword(), (long) (maxV * 60 * 60 * 1000));
        //设置token缓存有效时间
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, userAuthInfo);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, (long) (noOpv * 60 * 60));

        //更新最后登录部门信息
        SysUser sysUser = new SysUser();
        sysUser.setId(loginUser.getId());
        sysUser.setDepartId(userAuthInfo.getDepartId());
        sysUserService.updateById(sysUser);

        return Result.OK("ok", token);
    }

    /**
     * 获取当前租户下用户的应用列表
     *
     * @return
     */
    @ResrunLogMethod(name = "获取当前租户下用户的应用列表", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/getMyTenantAPPs", method = RequestMethod.GET)
    public Result<List<SysAppInfo>> getMyTenantAPPs() {
        LoginUser user = MySecurityUtils.getCurrentUser();
        List<SysAppInfo> apps = iSysTenantUserAppService.queryUserApps(user.getId(), user.getTenantId());
        if (CollUtil.isNotEmpty(apps)) {
            Iterator<SysAppInfo> it = apps.iterator();
            while (it.hasNext()) {
                SysAppInfo a = it.next();
                if (TenantAppStatus.ENABLE.getType().equals(a.getAppStatus())) {
                    a.setSelectFlag(true);
                }
                if (a.getAppCode().equals("app_00001")) {
                    it.remove();
                }
            }
        }
        return Result.OK(apps);
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/user/getUserInfo")
    @ResrunLogMethod(name = "获取用户信息", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<SysUserVO> getUserInfo() {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        //查询用户信息
        SysUser sysUser = sysUserService.getById(loginUser.getId());
        SysUserVO sysUserVO = new SysUserVO();
        sysUserVO.setId(loginUser.getId());
        sysUserVO.setUsername(loginUser.getUsername());
        sysUserVO.setRealname(loginUser.getRealname());
        sysUserVO.setPhone(DesensitizationUtils.getPhone(loginUser.getPhone()));
        if (loginUser.getUsername().equals(loginUser.getPhone())) {
            sysUserVO.setPhoneEditFlag(false);
        } else {
            sysUserVO.setPhoneEditFlag(true);
        }
        sysUserVO.setEmail(DesensitizationUtils.getEmail(loginUser.getEmail()));
        if (loginUser.getUsername().equals(loginUser.getEmail())) {
            sysUserVO.setEmailEditFlag(false);
        } else {
            sysUserVO.setEmailEditFlag(true);
        }
        sysUserVO.setCreateTime(loginUser.getCreateTime());
        sysUserVO.setPasswordLevel(sysUser.getPasswordLevel());
        sysUserVO.setAvatar(sysUser.getAvatar());
        sysUserVO.setInitUserInfo(sysUser.isInitUserInfo());
        sysUserVO.setTenantUserId(loginUser.getTenantUserId());
        SysDepart sysDepart = sysDepartService.getById(loginUser.getDepartId());
        sysUserVO.setLoginDepartId(loginUser.getDepartId());
        if (sysDepart != null) {
            sysUserVO.setLoginDepartName(sysDepart.getDepartName());
        }

        SysTenantInfo tenantInfo = sysTenantInfoService.getById(loginUser.getTenantId());
        sysUserVO.setLoginTenantId(loginUser.getTenantId());
        if (tenantInfo != null) {
            sysUserVO.setLoginTenantName(tenantInfo.getTenantName());
        }

        TenantInfoExtend tenantInfoExtend = tenantInfoExtendService.getTenantInfoByTenantId(loginUser.getTenantId());
        if (tenantInfoExtend != null) {
            sysUserVO.setSysType(MyStringUtils.isBlank(tenantInfoExtend.getSysType()) ? "base" : tenantInfoExtend.getSysType());
        }

        Integer passwordInvalidTime = commonToolsAPI.getConfigIntegerValueByType(SysConfigType.PASSWORDINVALIDTIME.getType());
        if (passwordInvalidTime == 0 || (sysUser.getUpdatePasswordTime() != null && DateUtil.between(DateUtil.date(), sysUser.getUpdatePasswordTime(), DateUnit.DAY) <= passwordInvalidTime)) {
            sysUserVO.setPasswordEditFlag(false);
        } else {
            sysUserVO.setPasswordEditFlag(true);
        }
        List<SysDepart> sysDeparts = iSysUserDepartService.getDepartsByUserId(loginUser.getId());
        if (CollUtil.isNotEmpty(sysDeparts)) {
            List<String> departNames = sysDeparts.stream().map(SysDepart::getDepartName).collect(Collectors.toList());
            sysUserVO.setDepartNames(departNames);
        }
        TenantType tenantType = iSysTenantInfoService.getByTenantId(loginUser.getTenantId());
        if (tenantType != null) {
            sysUserVO.setLoginTenantType(tenantType.getType());
        } else {
            sysUserVO.setLoginTenantType(TenantType.UNKNOWN.getType());
        }
        //角色信息
//        List<SysRole> sysRoles = iSysUserRoleService.getRolesByUserId(loginUser.getId());
//        if (CollUtil.isNotEmpty(sysRoles)) {
//            List<String> roleNames = sysRoles.stream().map(SysRole::getRoleName).collect(Collectors.toList());
//            sysUserVO.setRoleNames(roleNames);
//        }
        sysUserVO.setJobAppId(resrunJobConfig.getAppId());
        //是否开通个人租户
        sysUserVO.setPersonalTenantFlag(iSysTenantUserService.checkPersonalTenant(loginUser.getId()));
        //补充公告信息
        //iSysAnnouncementSendService.computeAndSupplementAnnouncement();
        //获取当前用户的权限组
        //sysUserVO.setAuthGroups(iSysAuthGroupMemberService.getLoginUserGroups());

        //临时-存放实名认证服务信息
        sysUserVO.setAuthInfo(sysAppInfoService.getById("70588803-52e4-ss3d-a61f-0a68e1febd72"));

        //清空用户登录Shiro权限缓存
        redisUtil.removeAll(CommonConstant.PREFIX_USER_SHIRO_CACHE + loginUser.getId() + loginUser.getTenantId() + loginUser.getAppCode());
        return Result.OK(sysUserVO);
    }

    /**
     * 获取新token
     */
    @GetMapping("/user/getNewToken")
    @ResrunLogMethod(name = "获取新token", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<?> getNewToken(String departId) {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        Object cacheToken = redisUtil.get(CommonConstant.PREFIX_USER_TOKEN + loginUser.getToken());
        if (oConvertUtils.isNotEmpty(cacheToken)) {
            LoginUserAuthInfo temp = (LoginUserAuthInfo) cacheToken;
            if (MyStringUtils.isBlank(temp.getAuthorizedToken())) {
                if (MyStringUtils.isBlank(departId)) {
                    departId = loginUser.getDepartId();
                }
                LoginUserAuthInfo loginUserAuthInfo = new LoginUserAuthInfo();
                loginUserAuthInfo.setDepartId(departId);
                SysDepart sysDepart = sysDepartService.getById(departId);
                if (sysDepart != null) {
                    loginUserAuthInfo.setOrgCode(sysDepart.getOrgCode());
                    loginUserAuthInfo.setTenantId(sysDepart.getTenantId());
                } else {
                    throw new PaasException("登录部门不存在");
                }
                loginUserAuthInfo.setAuthorizedToken(loginUser.getToken());
                double noOpv = commonToolsAPI.getConfigDoubleValueByType(SysConfigType.NOOPERATEKEEPALIVE.getType());
                double maxV = commonToolsAPI.getConfigDoubleValueByType(SysConfigType.MAXKEEPALIVE.getType());
                //生成token
                String token = JwtUtil.sign(loginUser.getUsername(), loginUser.getPassword(), (long) (maxV * 60 * 60 * 1000));
                //设置token缓存有效时间
                redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, loginUserAuthInfo);
                redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, (long) (noOpv * 60 * 60));
                return Result.OK("ok", token);
            } else {
                throw new PaasException("授权token错误");
            }
        } else {
            throw new PaasException("登录超时");
        }
    }

    /**
     * 校验该token与请求头token的登录信息是否一致
     */
    @GetMapping("/user/checkToken")
    @ResrunLogMethod(name = "校验该token与请求头token的登录信息是否一致", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<?> checkToken(@RequestParam(name = "token", required = true) String token) {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        Object cacheToken = redisUtil.get(CommonConstant.PREFIX_USER_TOKEN + token);
        Object cacheTokenHead = redisUtil.get(CommonConstant.PREFIX_USER_TOKEN + loginUser.getToken());
        if (oConvertUtils.isNotEmpty(cacheToken) && oConvertUtils.isNotEmpty(cacheTokenHead)) {
            LoginUserAuthInfo loginUserAuthInfoToken = (LoginUserAuthInfo) cacheToken;
            LoginUserAuthInfo loginUserAuthInfoHead = (LoginUserAuthInfo) cacheTokenHead;
            if (loginUserAuthInfoToken.getTenantId().equals(loginUserAuthInfoHead.getTenantId()) && loginUserAuthInfoToken.getDepartId().equals(loginUserAuthInfoHead.getDepartId())) {
                return Result.OK(true);
            } else {
                return Result.OK(false);
            }
        } else {
            throw new PaasException("token无效");
        }
    }


    /**
     * 退出登录
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/logout")
    @ResrunLogMethod(name = "登出", operateType = OperateLogType.OPERATE_TYPE_8)
    public Result<Object> logout(HttpServletRequest request) {
        //用户退出逻辑
        String token = request.getHeader(TokenConstant.X_ACCESS_TOKEN);
        if (oConvertUtils.isEmpty(token)) {
            return Result.error("退出登录失败！");
        }
        LoginUser sysUser = MySecurityUtils.getCurrentUser();
        if (sysUser != null) {
            log.info(" 用户名:  " + sysUser.getRealname() + ",退出成功！ ");
            //清空用户登录Token缓存
            redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + token);
            //清空用户登录Shiro权限缓存
            redisUtil.removeAll(CommonConstant.PREFIX_USER_SHIRO_CACHE + sysUser.getId() + sysUser.getTenantId() + sysUser.getAppCode());
            //清空用户的缓存信息（包括部门信息），例如sys:cache:user::<username>
            redisUtil.del(String.format("%s::%s", CacheConstant.SYS_USERS_CACHE, sysUser.getUsername()));
            //调用shiro的logout
            SecurityUtils.getSubject().logout();
            return Result.OK("退出登录成功！");
        } else {
            return Result.error("Token无效!");
        }
    }

    /**
     * 修改密码/忘记密码(不带token)
     */
    @PostMapping("/updatePassword")
    @ResrunLogMethod(name = "修改密码（登录前）", operateType = OperateLogType.OPERATE_TYPE_3)
    public Result<?> updatePassword(@Valid @RequestBody UserPasswordUpdateVO vo) {
        if (UpdatePasswordEnum.PHONE.getType().equals(vo.getType()) || UpdatePasswordEnum.EMIAL.getType().equals(vo.getType())) {
            return sysUserService.updatePasswordByCode(vo);
        }

        return Result.error("修改类型不支持");
    }

    /**
     * 修改密码/忘记密码(带token)
     */
    @PostMapping("/updatePasswordToken")
    @ResrunLogMethod(name = "修改密码（登录后）", operateType = OperateLogType.OPERATE_TYPE_3)
    public Result<?> updatePasswordToken(@Valid @RequestBody UserPasswordUpdateVO vo) {
        if (UpdatePasswordEnum.PASSWORD.getType().equals(vo.getType())) {
            return sysUserService.updatePasswordByOldPassword(vo);
        } else if (UpdatePasswordEnum.PHONE.getType().equals(vo.getType()) || UpdatePasswordEnum.EMIAL.getType().equals(vo.getType())) {
            return sysUserService.updatePasswordByCodeUserToken(vo);
        }

        return Result.error("修改类型不支持");
    }

    /**
     * 绑定手机号
     */
    @PostMapping("/bindPhone")
    @ResrunLogMethod(name = "绑定手机号", operateType = OperateLogType.OPERATE_TYPE_3)
    public Result<?> bindPhone(@RequestBody BindUnbindUserInfoVO vo) {
        String realKey = MD5Util.MD5Encode(vo.getPhone() + vo.getCaptchaKey(), "utf-8");
        Object checkCode = redisUtil.get(realKey);
        if (checkCode == null || !checkCode.toString().equals(vo.getCaptcha())) {
            return Result.error("验证码错误");
        }
        //删除redis中的验证码
        redisUtil.del(realKey);

        //校验手机号唯一
        if (sysUserService.checkRepeat("phone", vo.getPhone(), null)) {
            return Result.error("手机号已经绑定过用户");
        }

        SysUser user = new SysUser();
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        user.setId(loginUser.getId());
        user.setPhone(vo.getPhone());

        sysUserService.updateById(user);

        //解析数据库已有数据做绑定表：sign_ru_relation sign_ru_task
        SysTenantUser tenantUser = iSysTenantUserService.getPersonalTenantUser(user.getId());

        if (tenantUser != null) {
            //解析数据库已有数据做绑定表：sign_ru_relation
            flowService.bindOutUserRelation(vo.getPhone(), null, tenantUser.getId(), "bind");
        }
        //解析数据库已有数据做绑定表：sign_ru_task
        //绑定手机号：账号ID
        flowService.bindOutUserTask(null, vo.getPhone(), null, null, null, loginUser.getId(), null, null, "bind");
        //绑定手机号：(账号ID、租户ID(不为空)联合查询)租户账号ID
        flowService.bindOutUserTaskAll(vo.getPhone(), null, "bind");

        return Result.OK();
    }

    /**
     * 解绑手机号
     */
    @PostMapping("/unbindPhone")
    @ResrunLogMethod(name = "解绑手机号", operateType = OperateLogType.OPERATE_TYPE_3)
    public Result<?> unbindPhone(@RequestBody BindUnbindUserInfoVO vo) {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        checkEditAuth(vo, loginUser);
        SysUser user = sysUserService.getById(loginUser.getId());
        if (MyStringUtils.isBlank(user.getPhone())) {
            return Result.error("未绑定手机号");
        }
        if (user.getUsername().equals(user.getPhone())) {
            return Result.error("注册绑定的手机号不可以解绑");
        }
        user.setPhone("");

        sysUserService.updateById(user);

        return Result.OK();
    }

    private void checkEditAuth(BindUnbindUserInfoVO vo, LoginUser loginUser) {
        if (UpdatePasswordEnum.PASSWORD.getType().equals(vo.getType())) {
            SysUser user = sysUserService.getById(loginUser.getId());
            String passwordEncode = PasswordUtil.encrypt(user.getUsername(), vo.getPassword(), user.getSalt());
            if (!user.getPassword().equals(passwordEncode)) {
                throw new PaasException("密码输入错误!");
            }
        } else if (UpdatePasswordEnum.PHONE.getType().equals(vo.getType()) || UpdatePasswordEnum.EMIAL.getType().equals(vo.getType())) {
            String key = null;
            if (UpdatePasswordEnum.PHONE.getType().equals(vo.getType())) {
                key = loginUser.getPhone();
            } else if (UpdatePasswordEnum.EMIAL.getType().equals(vo.getType())) {
                key = loginUser.getEmail();
            }
            String realKey = MD5Util.MD5Encode(key + vo.getCaptchaKey(), "utf-8");
            Object checkCode = redisUtil.get(realKey);
            if (checkCode == null || !checkCode.toString().equals(vo.getCaptcha())) {
                throw new PaasException("验证码错误");
            }
            //删除redis中的验证码
            redisUtil.del(realKey);
        }
    }

    /**
     * 绑定邮箱
     */
    @PostMapping("/bindEmail")
    @ResrunLogMethod(name = "绑定邮箱", operateType = OperateLogType.OPERATE_TYPE_3)
    public Result<?> bindEmail(@RequestBody BindUnbindUserInfoVO vo) {
        String realKey = MD5Util.MD5Encode(vo.getEmail() + vo.getCaptchaKey(), "utf-8");
        Object checkCode = redisUtil.get(realKey);
        if (checkCode == null || !checkCode.toString().equals(vo.getCaptcha())) {
            return Result.error("验证码错误");
        }
        //删除redis中的验证码
        redisUtil.del(realKey);

        //校验邮箱唯一
        if (sysUserService.checkRepeat("email", vo.getEmail(), null)) {
            return Result.error("邮箱已经绑定过用户");
        }

        SysUser user = new SysUser();
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        user.setId(loginUser.getId());
        user.setEmail(vo.getEmail());

        sysUserService.updateById(user);

        //解析数据库已有数据做绑定表：sign_ru_relation sign_ru_task
        SysTenantUser tenantUser = iSysTenantUserService.getPersonalTenantUser(user.getId());
        if (tenantUser != null) {
            //解析数据库已有数据做绑定表：sign_ru_relation
            flowService.bindOutUserRelation(null, vo.getEmail(), tenantUser.getId(), "bind");
        }
        //解析数据库已有数据做绑定表：sign_ru_task
        //绑定邮箱：账号ID
        flowService.bindOutUserTask(null, null, vo.getEmail(), null, null, loginUser.getId(), null, null, "bind");
        //绑定邮箱：(账号ID、租户ID(不为空)联合查询)租户账号ID
        flowService.bindOutUserTaskAll(null, vo.getEmail(), "bind");

        return Result.OK();
    }

    /**
     * 解绑邮箱
     */
    @PostMapping("/unbindEmail")
    @ResrunLogMethod(name = "解绑邮箱", operateType = OperateLogType.OPERATE_TYPE_3)
    public Result<?> unbindEmail(@RequestBody BindUnbindUserInfoVO vo) {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        checkEditAuth(vo, loginUser);
        SysUser user = sysUserService.getById(loginUser.getId());
        if (MyStringUtils.isBlank(user.getEmail())) {
            return Result.error("未绑定邮箱");
        }
        if (user.getUsername().equals(user.getEmail())) {
            return Result.error("注册绑定的邮箱不可以解绑");
        }
        user.setEmail("");

        sysUserService.updateById(user);

        return Result.OK();
    }

    /**
     * 找回用户名
     */
    @GetMapping("/findUsername")
    @ResrunLogMethod(name = "找回用户名", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<?> findUsername(BindUnbindUserInfoVO vo) {
        String key = null;
        if (UpdatePasswordEnum.PHONE.getType().equals(vo.getType())) {
            key = vo.getPhone();
        } else if (UpdatePasswordEnum.EMIAL.getType().equals(vo.getType())) {
            key = vo.getEmail();
        }
        String realKey = MD5Util.MD5Encode(key + vo.getCaptchaKey(), "utf-8");
        Object checkCode = redisUtil.get(realKey);
        if (checkCode == null || !checkCode.toString().equals(vo.getCaptcha())) {
            return Result.error("验证码错误");
        }
        //删除redis中的验证码
        redisUtil.del(realKey);

        SysUser user = null;
        if (UpdatePasswordEnum.PHONE.getType().equals(vo.getType())) {
            user = sysUserService.getUserByPhone(vo.getPhone());
        } else if (UpdatePasswordEnum.EMIAL.getType().equals(vo.getType())) {
            user = sysUserService.getUserByEmail(vo.getEmail());
        }
        if (user != null) {
            return Result.OK("success", user.getUsername());
        } else {
            return Result.error("用户不存在");
        }
    }

    /**
     * 用户加入系统或租户--登录系统前面对邀请加入（个人邀请+部门邀请）通过邀请链接加入
     *
     * @return
     */
    @ResrunLogMethod(name = "用户加入系统或租户", operateType = OperateLogType.OPERATE_TYPE_3)
    @RequestMapping(value = "/userJion", method = RequestMethod.PUT)
    public Result<?> userJion(@RequestBody UserJionSystemVO userJionSystemVO) {
        sysUserService.userJion(userJionSystemVO);
        return Result.OK();
    }

//    /**
//     * 个人租户注册--没有租户扩展表
//     *
//     * @param userJionSystemVO
//     * @return
//     */
//    // @ApiOperation(value = "个人租户注册", notes = "个人租户注册")
//    @PutMapping(value = "/personalTenantRegister")
//    public Result<?> personalTenantRegister(@RequestBody UserJionSystemVO userJionSystemVO) {
//        sysTenantInfoService.personalTenantRegister(userJionSystemVO);
//        return Result.OK("注册成功！");
//    }

    /**
     * 用户信息
     *
     * @param sysUser
     * @param result
     * @return
     */
    private Result<JSONObject> userInfo(SysUser sysUser, Result<JSONObject> result, String appCode) {
        String syspassword = sysUser.getPassword();
        String username = sysUser.getUsername();
        // 获取用户部门信息
        JSONObject obj = new JSONObject();
        LoginUserAuthInfo loginUserAuthInfo = new LoginUserAuthInfo();
        //默认绑定第一个租户及部门-
        //返回租户列表(判断是否一个租户，否：返回列表，是：返回标识)
        //-根据租户返回部门列表（判断是否一个部门，否：返回列表，是：返回标识）-
        //进入应用门户，返回应用列表
        //List<SysTenantInfo> tenants = iSysTenantUserService.getTenantsByUserId(sysUser.getId());

        if (MyStringUtils.isBlank(appCode)) {
            throw new PaasException(CommonConstant.USER_LACK_INFO);
        }
        SysAppInfo sysAppInfo = iSysAppInfoService.getByAppCode(appCode);
        List<SysTenantInfo> tenants = iSysTenantUserService.getTenantsByUserIdAndAppId(sysUser.getId(), sysAppInfo.getId());

        if (CollUtil.isEmpty(tenants)) {
            throw new PaasException(CommonConstant.USER_INFO_USELESS);
        }

        List<UserTenantDepartVO> UserTenantDepartVO = new ArrayList<>();
        for (SysTenantInfo info : tenants) {
            List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId(), info.getId());
            if (CollUtil.isNotEmpty(departs)) {
                UserTenantDepartVO userTenantDepartVO = new UserTenantDepartVO();
                userTenantDepartVO.setTenantType(info.getTenantType());
                userTenantDepartVO.setTenantId(info.getId());
                userTenantDepartVO.setTenantName(info.getTenantName());
                if (TenantStatus.ENABLE.getType().equals(info.getTenantStatus())) {
                    userTenantDepartVO.setUseFlag(true);
                }

                List<UserDepartVO> departVOS = new ArrayList<>();
                departs.forEach(d -> {
                    UserDepartVO userDepartVO = new UserDepartVO();
                    userDepartVO.setDepartId(d.getId());
                    userDepartVO.setDepartName(d.getDepartName());
                    userDepartVO.setTenantId(d.getTenantId());
                    if (MyStringUtils.isBlank(sysUser.getDepartId())) {
                        if (MyStringUtils.isBlank(loginUserAuthInfo.getTenantId())) {
                            if (TenantStatus.ENABLE.getType().equals(info.getTenantStatus())) {
                                loginUserAuthInfo.setTenantId(info.getId());
                                loginUserAuthInfo.setDepartId(d.getId());
                                loginUserAuthInfo.setOrgCode(d.getOrgCode());
                            }
                            userDepartVO.setSelectFlag(true);
                            sysUser.setDepartId(d.getId());
                            userTenantDepartVO.setSelectFlag(true);

                            sysUserService.updateById(sysUser);
                        }
                    } else if (sysUser.getDepartId().equals(d.getId())) {
                        if (TenantStatus.ENABLE.getType().equals(info.getTenantStatus())) {
                            loginUserAuthInfo.setTenantId(info.getId());
                            loginUserAuthInfo.setDepartId(d.getId());
                            loginUserAuthInfo.setOrgCode(d.getOrgCode());
                        }
                        userDepartVO.setSelectFlag(true);
                        userTenantDepartVO.setSelectFlag(true);
                    }

                    departVOS.add(userDepartVO);
                });
                userTenantDepartVO.setDeparts(departVOS);
                userTenantDepartVO.setDepartId(departVOS.get(0).getDepartId());

                UserTenantDepartVO.add(userTenantDepartVO);
            }
        }

        //上次登录部门被移除后
        if (MyStringUtils.isBlank(loginUserAuthInfo.getTenantId())) {
            for (SysTenantInfo info : tenants) {
                if (MyStringUtils.isBlank(loginUserAuthInfo.getTenantId())) {
                    if (TenantStatus.ENABLE.getType().equals(info.getTenantStatus())) {
                        List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId(), info.getId());
                        if (CollUtil.isNotEmpty(departs)) {
                            for (SysDepart d : departs) {
                                if (MyStringUtils.isBlank(loginUserAuthInfo.getTenantId())) {
                                    loginUserAuthInfo.setTenantId(info.getId());
                                    loginUserAuthInfo.setDepartId(d.getId());
                                    loginUserAuthInfo.setOrgCode(d.getOrgCode());
                                    sysUser.setDepartId(d.getId());

                                    sysUserService.updateById(sysUser);
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    break;
                }
            }
        }

        if (UserTenantDepartVO.size() == 0) {
            throw new PaasException(CommonConstant.USER_INFO_USELESS);
        }

        obj.put("user_tenant_depart", UserTenantDepartVO);

        double noOpv = commonToolsAPI.getConfigDoubleValueByType(SysConfigType.NOOPERATEKEEPALIVE.getType());
        double maxV = commonToolsAPI.getConfigDoubleValueByType(SysConfigType.MAXKEEPALIVE.getType());
        //生成授权token
        long time = System.currentTimeMillis();
        String authorizedToken = JwtUtil.sign(username, syspassword, (long) (maxV * 60 * 60 * 1000));
        //设置token缓存有效时间
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + authorizedToken, loginUserAuthInfo);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + authorizedToken, (long) (noOpv * 60 * 60));
        //生成应用token
        String appToken = JwtUtil.sign(username, syspassword, (long) (maxV * 60 * 60 * 1000));
        //设置token缓存有效时间
        loginUserAuthInfo.setAuthorizedToken(authorizedToken);
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + appToken, loginUserAuthInfo);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + appToken, (long) (noOpv * 60 * 60));
        obj.put("authorizedToken", authorizedToken);
        obj.put("appToken", appToken);
        result.setResult(obj);
        result.setMessage("success");

        return result;
    }

    @RequestMapping(value = "/listApp", method = RequestMethod.GET)
    public Result<List<AppInfoVO>> listApp() {
        return Result.OK(sysAppInfoService.getAll());
    }


    // @ApiOperation(value = "获取项目标题", notes = "获取项目标题")
    @GetMapping("/websiteTitle")
    public Result<?> websiteTitle() {
        try {
            SignAppInfoResponse signAppInfoResponse = signServiceManageExternal.querySignAppInfo();
            return Result.OK("ok", signAppInfoResponse.getAppName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // @ApiOperation(value = "获取copyright", notes = "获取copyright")
    @GetMapping("/websiteCopyright")
    public Result<?> websiteCopyright() {
        return Result.OK("ok", commonToolsAPI.getConfigStringValueByType("website_copyright"));
    }

    private static final String BASE64PNG_PRE = "data:image/png;base64,";

    // @ApiOperation(value = "获取copyright", notes = "获取copyright")
    @GetMapping("/websiteConfig")
    public Result<?> websiteConfig() {
        Map<String,String> result = new HashMap<>();
        try {
            SignAppInfoResponse signAppInfoResponse = signServiceManageExternal.querySignAppInfo();
            result.put("website_title",signAppInfoResponse.getAppName());
            result.put("website_white_logo",BASE64PNG_PRE + signAppInfoResponse.getAppLogo());
            result.put("website_other_logo",BASE64PNG_PRE + signAppInfoResponse.getAppLogo());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return Result.OK("ok", result);
    }

    @Lazy
    @Resource
    private LicenseBean licenseBean;

    // @ApiOperation(value = "获取系统限制", notes = "获取系统限制")
    @GetMapping("/systemLimit")
    public Result<?> systemLimit() {
        return Result.OK("ok", licenseBean.getGrant());
    }

}