/**
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
package com.kaifangqian.config.limit;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.ImmutableList;
import com.kaifangqian.config.limit.annotation.LimitType;
import com.kaifangqian.config.limit.annotation.OperateType;
import com.kaifangqian.common.constant.CacheConstant;
import com.kaifangqian.common.constant.CommonConstant;
import com.kaifangqian.common.constant.TokenConstant;
import com.kaifangqian.common.redis.util.RedisUtil;
import com.kaifangqian.config.limit.annotation.Limit;
import com.kaifangqian.config.limit.annotation.LimitHandleType;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.util.SpringContextUtils;

import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.system.service.ISysUserService;
import com.kaifangqian.service.IMonitorBlacklistService;

import com.kaifangqian.utils.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WWT
 * @discription 频繁操作拦截
 */
@Slf4j
@Aspect
@Component
public class LimitAspect {

    @Autowired
    private LimitConfig config;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private IMonitorBlacklistService iMonitorBlacklistService;

    private final RedisTemplate<String, Object> redisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(LimitAspect.class);

    public LimitAspect(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Pointcut("@annotation(com.kaifangqian.config.limit.annotation.Limit)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        LoginUser currentUser = MySecurityUtils.getCurrentUser();

        HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method signatureMethod = signature.getMethod();
        Limit limit = signatureMethod.getAnnotation(Limit.class);
        OperateType operateType = limit.operateType();
        LimitType limitType = limit.limitType();
        String key = limit.key();
        long limitPeriod = limit.period();
        long limitCount = limit.count();
        String ip = IPUtil.getIpAddr(request);
        String requestPath = request.getRequestURI().replaceAll("/", "_");

        if (StringUtils.isEmpty(key)) {
            if (limitType == LimitType.IP) {
                key = ip;
            } else if(limitType == LimitType.TOKEN){
                key = currentUser.getId();
            }else {
                key = signatureMethod.getName();
            }
        }

        String luaScript = buildLuaScript();
        RedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
        String triggerKey = "limit:trigger:" +requestPath+":"+ key;
        Integer triggerCount = (Integer)redisUtil.get(triggerKey);
        if(triggerCount != null && limit.limitHandle() != LimitHandleType.NONE && triggerCount.intValue()>=config.getTriggerCount()){
            String userAgent = request.getHeader("User-Agent");
            log.info("[interface-trigger][{}->{}]，{}s内连续触发{},接口请求已被限制，User-Agent={}",request.getRequestURI(),key,config.getTriggerCount(),triggerCount,userAgent);
            throw new PaasException(999999,"请勿频繁操作");
        }

        if (OperateType.LIMIT.equals(operateType) || OperateType.ALL.equals(operateType)) {
            ImmutableList<String> limitKeys = ImmutableList.of(StringUtils.join(limit.prefix(), ":", key, "limit",requestPath));

            Long count = redisTemplate.execute(redisScript, limitKeys, limitCount, limitPeriod);
            if (null != count && count.intValue() <= limitCount) {
                return joinPoint.proceed();
            } else if(null != count && count.intValue() == limitCount+1) {

                long temCount = redisUtil.incr(triggerKey,1);
                if(temCount == 1){
                    redisUtil.expire(triggerKey,config.getTimeScoped());
                }else if(temCount == config.getTriggerCount()){
                    if(limit.limitHandle() == LimitHandleType.LOGOUT){
                        //登出
                        logout(request,currentUser);
                    }else if(limit.limitHandle() == LimitHandleType.LOGOUT_DISABLE){
                        //退出登录并禁用账户 加入黑名单
                        logoutDisable(request,currentUser,ip);
                    }else if(limit.limitHandle() == LimitHandleType.ADD_BLACKLIST){
                        logoutDisable(request,null,ip);
                    }
                }
                String userAgent = request.getHeader("User-Agent");
                log.info("[interface-trigger][{}->{}]，触发限制警告第【{}】次。User-Agent={}",request.getRequestURI(),key,temCount,userAgent);
                throw new PaasException(999999,"请勿频繁操作");
            }else{
                log.info("[interface-limit][{}->{}][{}s内触发了{}次限流]",request.getRequestURI(),key,limit.period(),count-limitCount);
                throw new PaasException(999999,"请勿频繁操作");
            }
        }
        return joinPoint.proceed();

//        if (config.getEnable()) {
//            String time = DateUtil.now();
//            String userName = "";
//            try {
//                userName = String.format("[userName={},userId={}]",currentUser.getUsername(),currentUser.getId());
//            } catch (Exception e) {
//                userName = clientId;
//            }
//            if (OperateType.WARNING.equals(operateType) || OperateType.ALL.equals(operateType)) {
//                ImmutableList<String> preWarningKeys = ImmutableList.of(StringUtils.join(limit.prefix(), ":", key, "pre", request.getRequestURI().replaceAll("/", "_")));
//                Number preCount = redisTemplate.execute(redisScript, preWarningKeys, limit.preCount(), limit.prePeriod());
//                if (null != preCount && preCount.intValue() <= limit.preCount()) {
//                    logger.info("第{}次访问key为 {}，描述为 [{}] 的接口", preCount.intValue(), preWarningKeys, limit.name());
//                } else {
//
////                    redisUtil.expire(preWarningKeys.get(0),0);
////                    logger.info("第{}次访问key为 {}，描述为 [{}] 的接口【预警】", preCount, preWarningKeys, limit.name());
//                    //预警
//                    String subject = "【预警】【" + limit.name() + "】模块高频操作";
//                    StringBuffer content = new StringBuffer();
//                    content.append("检测到【账号：");
//                    content.append(userName);
//                    content.append("】【IP：");
//                    content.append(ip);
//                    content.append("】在【");
//                    content.append(time);
//                    content.append("】触发了【");
//                    content.append(limit.name());
//                    content.append("】模块的规则：【");
//                    content.append(limit.prePeriod());
//                    content.append("秒钟操作");
//                    content.append(limit.preCount());
//                    content.append("次】，望知悉！");
//                    log.info(subject + content.toString());
////                    mailUtils.sendSimpleMailMessge(subject, content.toString());
//                }
//
//                ImmutableList<String> warningKeys = ImmutableList.of(StringUtils.join(limit.prefix(), ":", key, "warning", request.getRequestURI().replaceAll("/", "_")));
//                Number warnCount = redisTemplate.execute(redisScript, warningKeys, limit.Warncount(), limit.Warnperiod());
//                if (null != warnCount && warnCount.intValue() <= limit.Warncount()) {
//                    logger.info("第{}次访问key为 {}，描述为 [{}] 的接口", warnCount.intValue(), warningKeys, limit.name());
//                } else {
////                    logger.info("第{}次访问key为 {}，描述为 [{}] 的接口【告警】", warnCount, warningKeys, limit.name());
//                    //告警
//                    String subject = "【告警】【" + limit.name() + "】模块高频操作";
//                    StringBuffer content = new StringBuffer();
//                    content.append("检测到【账号：");
//                    content.append(userName);
//                    content.append("】【IP：");
//                    content.append(ip);
//                    content.append("】在【");
//                    content.append(time);
//                    content.append("】触发了【");
//                    content.append(limit.name());
//                    content.append("】模块的规则：【");
//                    content.append(limit.Warnperiod());
//                    content.append("秒钟操作");
//                    content.append(limit.Warncount());
//                    content.append("次】，望知悉！");
//                    log.error(subject + content.toString());
////                    mailUtils.sendSimpleMailMessge(subject, content.toString());
//                }
//            }
//        }
//
//        return joinPoint.proceed();
    }

    /**
     * 脚本
     */
    private String buildLuaScript() {
        return "local c" +
                "\nc = redis.call('get',KEYS[1])" +
//                "\nif c and tonumber(c) > tonumber(ARGV[1]) then" +
//                "\nreturn tonumber(c);" +
//                "\nend" +
                "\nc = redis.call('incr',KEYS[1])" +
                "\nif tonumber(c) == 1 then" +
                "\nredis.call('expire',KEYS[1],ARGV[2])" +
                "\nend" +
                "\nreturn c;";
    }

    private Map<String, Object> methodBefore(JoinPoint joinPoint) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        try {
            Object[] objs = joinPoint.getArgs();
            String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
            for (int i = 0; i < objs.length; i++) {
                if (objs[i] instanceof BindingResult || objs[i] instanceof HttpServletResponse || objs[i] instanceof Model) {
                    continue;
                } else if (objs[i] instanceof HttpServletRequest) {
                    HttpServletRequest request = (HttpServletRequest) objs[i];
                    Map<String, String[]> params = request.getParameterMap();
                    paramMap.put(argNames[i], params);
                } else {
                    paramMap.put(argNames[i], objs[i]);
                }
            }
        } catch (Exception e) {
            log.error("获取参数异常", e);
        }
        return paramMap;
    }

    /**
     * 客户端api接口请求限制
     *
     * @param objectMap
     * @param clientId
     * @return
     */
    private String getParamValue(Map<String, Object> objectMap, String clientId) {
        if (ObjectUtil.isEmpty(objectMap)) {
            return null;
        }
        JSONObject jsonObject = JSONUtil.parseObj(objectMap.get("request"));
        if (ObjectUtil.isEmpty(jsonObject)) {
            return null;
        }
        String clientIdOrMacAddress = jsonObject.getStr(clientId);
        if (ObjectUtil.isEmpty(clientIdOrMacAddress)) {
            clientIdOrMacAddress = jsonObject.getStr("macAddr");
        }
        return clientIdOrMacAddress;
    }


    private void logout(HttpServletRequest request,LoginUser sysUser){
        if(sysUser != null){
            String token = request.getHeader(TokenConstant.X_ACCESS_TOKEN);
            //清空用户登录Token缓存
            redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + token);
            //清空用户登录Shiro权限缓存
            redisUtil.removeAll(CommonConstant.PREFIX_USER_SHIRO_CACHE + sysUser.getId() + sysUser.getTenantId() + sysUser.getAppCode());
            //清空用户的缓存信息（包括部门信息），例如sys:cache:user::<username>
            redisUtil.del(String.format("%s::%s", CacheConstant.SYS_USERS_CACHE, sysUser.getUsername()));
            //调用shiro的logout
            SecurityUtils.getSubject().logout();
        }
        log.info("触发登出操作");
    }

    private void logoutDisable(HttpServletRequest request,LoginUser sysUser,String ip){
//        iMonitorBlacklistService.addBlackList(ip);
//        if(sysUser != null){
//            iSysUserService.disableUser(sysUser.getId());
//            logout(request,sysUser);
//        }
//
//        log.info("触发登出+禁用账户+ip黑名单操作");
    }
}
