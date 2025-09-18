/**
 * @description 用户意愿确认
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

import cn.hutool.core.util.RandomUtil;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.kaifangqian.config.CommonConstants;
import com.kaifangqian.annotation.ResrunLogMethod;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.constant.CommonConstant;
import com.kaifangqian.common.enums.OperateLogType;
import com.kaifangqian.common.redis.base.BaseMap;
import com.kaifangqian.common.redis.client.RedisMQClient;
import com.kaifangqian.common.redis.util.RedisUtil;
import com.kaifangqian.common.util.oConvertUtils;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.opensign.service.confirm.ISignUserConfirmService;
import com.kaifangqian.modules.opensign.service.confirm.IUserConfirmService;
import com.kaifangqian.modules.opensign.vo.base.UserConfirmByCodeVO;
import com.kaifangqian.modules.opensign.vo.base.UserConfirmByPasswordVO;
import com.kaifangqian.modules.opensign.vo.request.ConfirmParaRequest;
import com.kaifangqian.modules.opensign.vo.request.ru.CompanyStasticsVO;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Description: SignVerifyController
 * @Package: com.kaifangqian.modules.opensign.controller
 * @ClassName: SignVerifyController
 * @author: Fusion
 * CreateTime:  2023/8/20  9:53
 * @copyright 北京资源律动科技有限公司
 */
@Slf4j
@RestController
@RequestMapping("/user/confirm")
@ResrunLogModule(name = "用户意愿确认")
// @Api(tags = "用户意愿确认")
public class UserConfirmController {

    @Autowired
    private ISignUserConfirmService signUserConfirmService;

    @Autowired
    private IUserConfirmService userConfirmService;

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    private RedisMQClient redisMQClient;

    @Autowired
    private CaptchaService captchaService;


    // @ApiOperation(value = "获取订单号", notes = "获取订单号")
    @GetMapping(value = "/getOrderNo")
    @ResrunLogMethod(name = "获取订单号", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<?> getOrderNo(@RequestParam(required = true, value = "mainId") String mainId) {
        String orderNo = signUserConfirmService.saveExt(mainId);
        return Result.OK("success", orderNo);
    }

    // @ApiOperation(value = "提交补充参数", notes = "提交补充参数")
    @PostMapping(value = "/confirmPara")
    @ResrunLogMethod(name = "提交补充参数", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<CompanyStasticsVO> confirmPara(@RequestBody ConfirmParaRequest confirmPara) {
        signUserConfirmService.confirmPara(confirmPara);
        return Result.OK();
    }

    /**
     * 发送短信
     *
     * @param orderNo
     */
    // @ApiOperation("发送短信")
    @GetMapping(value = "/sendMessage")
    @ResrunLogMethod(name = "发送短信", operateType = OperateLogType.OPERATE_TYPE_0)
    public Result<?> sendMessage(@RequestParam(name = "orderNo", required = true) String orderNo,String captchaVerification) {
        CaptchaVO vo = new CaptchaVO();
        vo.setCaptchaVerification(captchaVerification);
        ResponseModel responseModel = captchaService.verification(vo);
        if(!responseModel.isSuccess()){
            return Result.error("请完成安全验证后再发送验证码");
        }
        return Result.OK("短信发送成功", userConfirmService.sendMessage(orderNo));
    }

    /**
     * 发送邮件
     *
     * @param orderNo
     */
    // @ApiOperation("发送邮件")
    @GetMapping(value = "/sendEmail")
    @ResrunLogMethod(name = "发送邮件", operateType = OperateLogType.OPERATE_TYPE_0)
    public Result<?> sendEmail(@RequestParam(name = "orderNo", required = true) String orderNo,String captchaVerification) {
        CaptchaVO vo = new CaptchaVO();
        vo.setCaptchaVerification(captchaVerification);
        ResponseModel responseModel = captchaService.verification(vo);
        if(!responseModel.isSuccess()){
            return Result.error("请完成安全验证后再发送验证码");
        }
        return Result.OK("邮件发送成功", userConfirmService.sendEmail(orderNo));
    }

    /**
     * 意愿校验-短信邮件、双重
     */
    @PostMapping("/confirmByPhoneEmail")
    @ResrunLogMethod(name = "意愿校验-短信邮件、双重", operateType = OperateLogType.OPERATE_TYPE_3)
    public Result<?> confirmByPhoneEmail(@Valid @RequestBody UserConfirmByCodeVO vo) {
        return userConfirmService.confirmByPhoneEmail(vo);
    }

    /**
     * 意愿校验-签约密码
     */
    @PostMapping("/confirmByPassword")
    @ResrunLogMethod(name = "意愿校验-签约密码", operateType = OperateLogType.OPERATE_TYPE_3)
    public Result<?> confirmByPassword(@Valid @RequestBody UserConfirmByPasswordVO vo) {
        return userConfirmService.confirmByPassword(vo);
    }

    // @ApiOperation("获取扫脸连接")
    @RequestMapping(value = "/getFaceUrl", method = RequestMethod.GET)
    public Result<?> getFaceUrl(@RequestParam String orderNo, String redirectUrl) {
        return Result.OK("ok", userConfirmService.getFaceUrl(orderNo, redirectUrl));
    }

    // @ApiOperation("获取扫脸二维码key")
    @RequestMapping(value = "/getKey", method = RequestMethod.GET)
    public Result<?> getKey() {
        String pre = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 4).toUpperCase();
        String last = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 9).toUpperCase();
        String key = pre + "-" + last;
        //设置token缓存有效时间
        redisUtil.set(CommonConstant.PREFIX_FACE_CODE + key, key);
        redisUtil.expire(CommonConstant.PREFIX_FACE_CODE + key, CommonConstants.FACECODEVALID * 60);
        return Result.OK("ok", key);
    }

    // @ApiOperation("校验扫脸二维码key，并推送已经扫码标识")
    @RequestMapping(value = "/checkKey", method = RequestMethod.GET)
    public Result<?> checkKey(@RequestParam("key") String key) {
        Object cacheToken = redisUtil.get(CommonConstant.PREFIX_FACE_CODE + key);
        if (oConvertUtils.isNotEmpty(cacheToken)) {
            BaseMap baseMap = new BaseMap();
            baseMap.put("userId", cacheToken.toString());
            redisMQClient.sendMessage("faceScanListener", baseMap);
            return Result.OK(true);
        }
        return Result.OK(false);
    }

    // @ApiOperation("校验扫脸二维码key，并推送扫码结果")
    @RequestMapping(value = "/checkKeyAndPushResult", method = RequestMethod.GET)
    public Result<?> checkKeyAndPushResult(@RequestParam("key") String key, @RequestParam("code") String code, @RequestParam("orderNo") String orderNo) {
        Object cacheToken = redisUtil.get(CommonConstant.PREFIX_FACE_CODE + key);
        if (oConvertUtils.isNotEmpty(cacheToken)) {
            BaseMap baseMap = new BaseMap();
            baseMap.put("userId", cacheToken.toString());
            baseMap.put("code", code);
            if ("0".equals(code)) {
                userConfirmService.updateFaceResult(orderNo);
            }
            redisMQClient.sendMessage("faceResultListener", baseMap);
            return Result.OK(true);
        }
        return Result.OK(false);
    }

//    // @ApiOperation("测试关联数据")
//    @RequestMapping(value = "/testData", method = RequestMethod.GET)
//    public Result<?> testData() {
//        userConfirmService.bindFaceFile();
//        return Result.OK();
//    }
}