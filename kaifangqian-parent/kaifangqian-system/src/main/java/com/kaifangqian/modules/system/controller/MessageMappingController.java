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

import com.alibaba.fastjson.JSONObject;
import com.kaifangqian.modules.opensign.entity.SignRuTask;
import com.kaifangqian.modules.opensign.service.ru.SignRuTaskService;
import com.kaifangqian.modules.system.entity.SysDepart;
import com.kaifangqian.modules.system.entity.SysUser;
import com.kaifangqian.annotation.ResrunLogMethod;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.constant.CommonConstant;
import com.kaifangqian.common.enums.OperateLogType;
import com.kaifangqian.common.redis.util.RedisUtil;
import com.kaifangqian.common.system.util.JwtUtil;
import com.kaifangqian.common.system.vo.LoginUserAuthInfo;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.entity.MessageShortMapping;
import com.kaifangqian.enums.SysConfigType;
import com.kaifangqian.inteface.CommonToolsAPI;
import com.kaifangqian.modules.system.service.ISysDepartService;
import com.kaifangqian.modules.system.service.ISysUserService;
import com.kaifangqian.service.MessageShortMappingService;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// @Api(tags = "第三方消息发送记录表")
@RestController
@RequestMapping("/mes/message_3rdRecord")
@Slf4j
@ResrunLogModule(name = "第三方消息管理模块")
public class MessageMappingController {
    @Autowired
    private MessageShortMappingService messageShortMappingService;

    @Autowired
    private SignRuTaskService signRuTaskService;
    @Autowired
    private ISysDepartService iSysDepartService;
    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private CommonToolsAPI commonToolsAPI;
    /**
     * 根据code获取参数
     *
     * @param code
     * @return
     */
    // @ApiOperation(value = "根据code获取参数", notes = "根据code获取参数")
    @GetMapping(value = "/getParaByCode")
    @ResrunLogMethod(name = "根据code获取参数", operateType = OperateLogType.OPERATE_TYPE_0)
    public Result<?> getParaByCode(@RequestParam(name = "code", required = true) String code) {
        MessageShortMapping mapping = messageShortMappingService.getByCode(code);
        if(mapping != null){
            JSONObject result = JSONObject.parseObject(mapping.getMsgPara());
            if(mapping.getNoLogin()){
                Date expireTime = mapping.getPageUrlExpireTime();
                //失效时间小于当前时间
                if( expireTime != null && expireTime.getTime() >= System.currentTimeMillis()){
                    Map<String,String> tokenResult = buildTempToken(result.getString("taskId"));
                    if(tokenResult != null){
                        result.putAll(tokenResult);
                        result.put("noLogin",true);
                    }else{
                        result.put("noLogin",false);
                    }
                }
            }else {
                result.put("noLogin",false);
            }
            return Result.OK("success", result);
        }else {
            return Result.error("参数错误");
        }

    }


    private  Map<String,String> buildTempToken(String taskId){
        try {
            SignRuTask ruTask =  signRuTaskService.getById(taskId);
            List<SysDepart> departs = iSysDepartService.queryUserDeparts(ruTask.getUserId(),ruTask.getTenantId());
            if(departs != null && departs.size()>0){
                LoginUserAuthInfo loginUserAuthInfo = new LoginUserAuthInfo();
                loginUserAuthInfo.setTenantId(ruTask.getTenantId());
                loginUserAuthInfo.setDepartId(departs.get(0).getId());
                loginUserAuthInfo.setOrgCode(departs.get(0).getId());

                double noOpv = commonToolsAPI.getConfigDoubleValueByType(SysConfigType.NOOPERATEKEEPALIVE.getType());
                double maxV = commonToolsAPI.getConfigDoubleValueByType(SysConfigType.MAXKEEPALIVE.getType());

                SysUser sysUser = iSysUserService.getById(ruTask.getUserId());
                String syspassword = sysUser.getPassword();
                String username = sysUser.getUsername();
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
                Map<String,String> result = new HashMap<>();
                result.put("authorizedToken", authorizedToken);
                result.put("appToken", appToken);
                return result;
            }

        }catch (Exception e){
            log.error("buildTempToken error",e);
        }
        return null;
    }

}
