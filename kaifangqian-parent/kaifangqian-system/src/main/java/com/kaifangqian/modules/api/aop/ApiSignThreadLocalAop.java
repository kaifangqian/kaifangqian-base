/**
 * @description 处理签署线程本地变量
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
package com.kaifangqian.modules.api.aop;

import com.alibaba.fastjson.JSONObject;
import com.kaifangqian.modules.api.entity.ApiNormalReq;
import com.kaifangqian.modules.api.exception.RequestParamsException;
import com.kaifangqian.modules.api.service.IApiNormalReqService;
import com.kaifangqian.modules.api.service.IApiRelationLinkService;
import com.kaifangqian.modules.system.entity.ApiDeveloperManage;
import com.kaifangqian.modules.system.entity.SysTenantUser;
import com.kaifangqian.modules.system.entity.SysUser;
import com.kaifangqian.common.constant.ApiCode;
import com.kaifangqian.common.constant.ApiConstants;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.system.vo.ProcTaskInfo;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.util.RequestHolder;
import com.kaifangqian.modules.opensign.dto.SignTaskInfo;
import com.kaifangqian.modules.opensign.dto.SignTaskThreadlocalVO;
import com.kaifangqian.modules.system.service.IApiDeveloperManageService;
import com.kaifangqian.modules.system.service.ISysTenantUserService;
import com.kaifangqian.modules.system.service.ISysUserService;
import com.kaifangqian.utils.MyStringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * create by zhenghuihan at 2022/3/16
 * @description 处理签署线程本地变量
 */
@Aspect
@Component
@Slf4j
@Data
public class ApiSignThreadLocalAop {

    @Autowired
    private IApiDeveloperManageService apiDeveloperManageService;
    @Autowired
    private ISysTenantUserService sysTenantUserService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IApiNormalReqService apiNormalReqService;
    @Autowired
    private IApiRelationLinkService apiRelationLinkService;

    private ThreadLocal<String> requestBodyCache = new ThreadLocal<>();

    /**
     * 切入点
     * 根据路径切入
     */
    @Pointcut("execution(public * com.kaifangqian.modules.api.controller.*Controller.*(..))")
    public void local() {
    }

    /**
     * 前置操作
     *
     * @param
     */
    @Before("local()")
    public void before() {
        requestBodyCache.remove();
        MySecurityUtils.THREAD_LOCAL.remove();
        ProcTaskInfo.THREAD_LOCAL.remove();
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        request.setAttribute(ApiConstants.FROM_TYPE, ApiConstants.FROM_API);
        String path =request.getRequestURI() ;
        String sign = request.getHeader(ApiConstants.SIGN);
        String operatorAccount = null;
        String uniqueCode = null;
        String token = null;
        String content = null;
        boolean signVerified = false;
        if (request.getMethod().equalsIgnoreCase("POST") || request.getMethod().equalsIgnoreCase("PUT")) {
            // 获取请求体中的JSON数据
            try {
                String data = getRequestBody(request);
                requestBodyCache.set(data);
                content = cleanData(data);
                //校验token
                try {
                    JSONObject jsonObject = JSONObject.parseObject(data);
                    token = jsonObject.getString(ApiConstants.APP_AUTH_TOKEN);
                    operatorAccount = jsonObject.getString(ApiConstants.OPERATOR_ACCOUNT);
                    uniqueCode = jsonObject.getString(ApiConstants.UNIQUE_CODE);
                } catch (Exception e) {
                    //数据格式错误，不是json
                    throw new RequestParamsException(data, ApiCode.DATA_FORMAT_ERROR);
                }
                if (MyStringUtils.isBlank(token)) {
                    //没有token
                    throw new RequestParamsException(token, operatorAccount, uniqueCode, data, ApiCode.TOKEN_NULL);
                }
                ApiDeveloperManage developerManage = apiDeveloperManageService.getByToken(token);
                if (developerManage == null) {
                    //token无效
                    throw new RequestParamsException(token, operatorAccount, uniqueCode, data, ApiCode.TOKEN_INVALID);
                }
                if (developerManage.getStatus() != 1) {
                    //开发者被停用
                    throw new RequestParamsException(token, operatorAccount, uniqueCode, data, ApiCode.DEVELOPER_STOP);
                }
                //验签
                //signVerified = ApiSignature.check(data, sign, developerManage.getPublicKey(), ApiConstants.CHARSET_UTF8, ApiConstants.SIGN_TYPE_RSA2);
            } catch (RequestParamsException e) {
                throw new RequestParamsException(token, operatorAccount, uniqueCode, content, e.getApiCode(), e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                //未知错误
                throw new RequestParamsException(token, operatorAccount, uniqueCode, content, ApiCode.UNKNOWN);
            }
        } else if (request.getMethod().equalsIgnoreCase("GET") || request.getMethod().equalsIgnoreCase("DELETE")) {
            Map<String, String> params = new HashMap<String, String>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                params.put(name, valueStr);
            }
            content = params.toString();
            //校验token
            token = params.get(ApiConstants.APP_AUTH_TOKEN);
            operatorAccount = params.get(ApiConstants.OPERATOR_ACCOUNT);
            uniqueCode = params.get(ApiConstants.UNIQUE_CODE);
            if (MyStringUtils.isBlank(token)) {
                //没有token
                throw new RequestParamsException(token, operatorAccount, uniqueCode, content, ApiCode.TOKEN_NULL);
            }
            ApiDeveloperManage developerManage = apiDeveloperManageService.getByToken(token);
            if (developerManage == null) {
                //token无效
                throw new RequestParamsException(token, operatorAccount, uniqueCode, content, ApiCode.TOKEN_INVALID);
            }
            if (developerManage.getStatus() != 1) {
                //开发者被停用
                throw new RequestParamsException(token, operatorAccount, uniqueCode, content, ApiCode.DEVELOPER_STOP);
            }
            try {
                //验签
//                signVerified = ApiSignature.check(params, sign, developerManage.getPublicKey(), ApiConstants.CHARSET_UTF8, ApiConstants.SIGN_TYPE_RSA2);
            } catch (RequestParamsException e) {
                throw new RequestParamsException(token, operatorAccount, uniqueCode, content, e.getApiCode(), e.getMessage());
            }
        }
//        //验证失败
//        if (!signVerified) {
//            throw new RequestParamsException(token, operatorAccount, uniqueCode, content, ApiCode.DATA_CHECK_ERROR);
//        }

        if (MyStringUtils.isNotBlank(operatorAccount)) {
            //初始化用户数据
            String tenantUserId = apiRelationLinkService.getSystemIdByExAccount(token, ApiConstants.EXTEND_TYPE_USER, operatorAccount);
            if (MyStringUtils.isNotBlank(tenantUserId)) {
                SysTenantUser tenantUser = sysTenantUserService.getById(tenantUserId);
                if (tenantUser != null) {
                    LoginUser loginUser = new LoginUser();
                    SysUser sysUser = sysUserService.getById(tenantUser.getUserId());
                    if (sysUser != null) {
                        BeanUtils.copyProperties(sysUser, loginUser);
                    }
                    loginUser.setTenantId(tenantUser.getTenantId());
                    loginUser.setTenantUserId(tenantUser.getId());
                    MySecurityUtils.THREAD_LOCAL.set(loginUser);
                }
            }

        }else if (MyStringUtils.isBlank(operatorAccount) && path.equals("/resrun-paas/kaifangqian/openAPI/V2/contract/recall")) {
            ApiDeveloperManage developerManage = apiDeveloperManageService.getByToken(token);
            LoginUser loginUser = new LoginUser();
            loginUser.setTenantId(developerManage.getTenantId());
            MySecurityUtils.THREAD_LOCAL.set(loginUser);
        }

        //初始化数据
        request.setAttribute(ApiConstants.APP_AUTH_TOKEN, token);
        request.setAttribute(ApiConstants.UNIQUE_CODE, uniqueCode);
        request.setAttribute(ApiConstants.OPERATOR_ACCOUNT, operatorAccount);
        request.setAttribute(ApiConstants.REQ_PARA, content);

        SignTaskThreadlocalVO threadlocalVO = new SignTaskThreadlocalVO();
        SignTaskInfo.THREAD_LOCAL.set(threadlocalVO);
    }

    /**
     * 环绕操作
     *
     * @param point 切入点
     * @return 原方法返回值
     * @throws Throwable 异常信息
     */
    @Around("local()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        return point.proceed();
    }

    /**
     * 后置操作
     */
    @AfterReturning(value = "local()", returning = "obj")
    public void afterReturning(JoinPoint point, Object obj) {
        ApiNormalReq req = new ApiNormalReq();
        if(obj != null){
            req.setResPara(obj.toString());
        }
        apiNormalReqService.recordNormalReq(req);
        ProcTaskInfo.THREAD_LOCAL.remove();
        requestBodyCache.remove();
    }


    private String getRequestBody(HttpServletRequest request) throws IOException {
        try (BufferedReader reader = request.getReader()) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

    private String cleanData(String data) {
        // 处理数据，删除或替换换行符和转义符
        String cleanedData = data.replace("\n", "").replace("\r", ""); // 删除换行符
        cleanedData = cleanedData.replace("\\n", "\n").replace("\\r", "\r"); // 还原转义符
        if (cleanedData.length() > 1024) {
            cleanedData = cleanedData.substring(0, 1024);
        }
        return cleanedData;
    }
}
