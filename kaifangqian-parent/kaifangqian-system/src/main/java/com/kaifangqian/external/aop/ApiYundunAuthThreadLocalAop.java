/**
 * @description 签署线程本地变量,存储当前用户信息,完成数据交换的签名验签
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
package com.kaifangqian.external.aop;

import com.kaifangqian.modules.api.entity.ApiNormalReq;
import com.kaifangqian.modules.api.exception.RequestParamsException;
import com.kaifangqian.modules.api.service.IApiNormalReqService;
import com.kaifangqian.modules.api.service.IApiRelationLinkService;
import com.kaifangqian.modules.api.util.ApiSignature;
import com.kaifangqian.modules.api.util.SignGenerator;
import com.kaifangqian.common.constant.ApiCode;
import com.kaifangqian.common.constant.ApiConstants;
import com.kaifangqian.common.redis.util.RedisUtil;
import com.kaifangqian.common.system.vo.ProcTaskInfo;
import com.kaifangqian.common.util.RequestHolder;
import com.kaifangqian.modules.system.service.IApiDeveloperManageService;
import com.kaifangqian.modules.system.service.ISysTenantUserService;
import com.kaifangqian.modules.system.service.ISysUserService;
import com.kaifangqian.utils.MyStringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;
/**
 * @author : yxb
 * create at: 2025/6/6
 */
@Aspect
@Component
@Slf4j
@Data
public class ApiYundunAuthThreadLocalAop {

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
    @Autowired
    private RedisUtil redisUtil;
    @Value("${service.app-id}")
    private String clientAppId ;
    @Value("${service.yundun-service-public-key}")
    private String publicKey ;

    private ThreadLocal<String> requestBodyCache = new ThreadLocal<>();

    /**
     * 切入点
     * 根据路径切入
     */
    @Pointcut("execution(public * com.kaifangqian.external.auth.callback.*Controller.*(..))")
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
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        request.setAttribute(ApiConstants.FROM_TYPE, ApiConstants.FROM_API);

        // 获取请求体
        String sign = request.getHeader(ApiConstants.SIGN);
        String appId = request.getHeader(ApiConstants.APP_ID);
        String nonce = request.getHeader(ApiConstants.NONCE);
        String timestamp = request.getHeader(ApiConstants.TIMESTAMP);

        // 验证请求头
        if(MyStringUtils.isBlank(sign)) {
            throw new RequestParamsException(ApiCode.PARAM_MISSING, "sign" + ApiCode.PARAM_MISSING.getTemplate());
        }

        if(MyStringUtils.isBlank(appId)) {
            throw new RequestParamsException(ApiCode.PARAM_MISSING, "appId" + ApiCode.PARAM_MISSING.getTemplate());
        }

        if(!clientAppId.equals(appId)) {
            throw new RequestParamsException(ApiCode.TOKEN_INVALID, "appId" + ApiCode.TOKEN_INVALID.getTemplate());
        }

        if(MyStringUtils.isBlank(nonce)) {
            throw new RequestParamsException(ApiCode.PARAM_MISSING, "nonce" + ApiCode.PARAM_MISSING.getTemplate());
        }
        if(MyStringUtils.isBlank(timestamp)) {
            throw new RequestParamsException(ApiCode.PARAM_MISSING, "timestamp" + ApiCode.PARAM_MISSING.getTemplate());
        }

        long requestTime = Long.parseLong(timestamp);
        long currentTime = System.currentTimeMillis();
        if (Math.abs(currentTime - requestTime) > 300000) {
            throw new RequestParamsException(ApiCode.TIMESTAMP_INVALID, ApiCode.TIMESTAMP_INVALID.getTemplate());
        }
        Long nonceValExpire = redisUtil.getExpire(nonce);
        if(nonceValExpire > 0){
            throw new RequestParamsException(ApiCode.NONCE_REPEAT, ApiCode.NONCE_REPEAT.getTemplate());
        }

        String content = null;
        boolean signVerified = false;
        if (request.getMethod().equalsIgnoreCase("POST") || request.getMethod().equalsIgnoreCase("PUT")) {
            // 获取请求体中的JSON数据
            try {
                String data = getRequestBody(request);
                requestBodyCache.set(data);
                //content = cleanData(data);
                // 1. 计算biz_content
                String canonicalJson = null;
                String bizContent = null;
                String uri = request.getRequestURI();
                //校验token
                try {
                    //JSONObject jsonObject = JSONObject.parseObject(content);
                    if(data != null){
                        canonicalJson = SignGenerator.canonicalizeJson(data);
                        bizContent = SignGenerator.computeBizContent(canonicalJson);
                    }
                } catch (Exception e) {
                    //数据格式错误，不是json
                    throw new RequestParamsException(data, ApiCode.DATA_FORMAT_ERROR);
                }
                String signString = SignGenerator.buildSignString(appId, timestamp, nonce, uri, bizContent);
                System.out.println("callback待签名字符串:\n" + signString);
                signVerified = ApiSignature.check(signString, sign, publicKey, ApiConstants.CHARSET_UTF8, ApiConstants.SIGN_TYPE_RSA2);
            } catch (RequestParamsException e) {
                throw new RequestParamsException(e.getApiCode(), e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                //未知错误
                throw new RequestParamsException(ApiCode.UNKNOWN, ApiCode.UNKNOWN.getTemplate());
            }
        } else if (request.getMethod().equalsIgnoreCase("GET") || request.getMethod().equalsIgnoreCase("DELETE")) {
//            Map<String, String> params = new HashMap<String, String>();
//            Map<String, String[]> requestParams = request.getParameterMap();
//            for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
//                String name = (String) iter.next();
//                String[] values = (String[]) requestParams.get(name);
//                String valueStr = "";
//                for (int i = 0; i < values.length; i++) {
//                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
//                }
//                params.put(name, valueStr);
//            }
//            content = params.toString();
//            //校验token
//
//            orderNo = params.get(ApiConstants.YD_BIZ_CONTENT_ORDER_NO);
//            unionId = params.get(ApiConstants.YD_BIZ_CONTENT_UNOIN_ID);
//            if (MyStringUtils.isBlank(token)) {
//                //没有token
//                throw new RequestParamsException(token, orderNo, unionId, content, ApiCode.TOKEN_NULL);
//            }
//            ApiDeveloperManage developerManage = apiDeveloperManageService.getByToken(token);
//            if (developerManage == null) {
//                //token无效
//                throw new RequestParamsException(token, orderNo, unionId, content, ApiCode.TOKEN_INVALID);
//            }
//            if (developerManage.getStatus() != 1) {
//                //开发者被停用
//                throw new RequestParamsException(token, orderNo, unionId, content, ApiCode.DEVELOPER_STOP);
//            }
//            try {
//                //验签
////                signVerified = ApiSignature.check(params, sign, developerManage.getPublicKey(), ApiConstants.CHARSET_UTF8, ApiConstants.SIGN_TYPE_RSA2);
//            } catch (RequestParamsException e) {
//                throw new RequestParamsException(token, orderNo, unionId, content, e.getApiCode(), e.getMessage());
//            }
        }
//        //验证失败
        if (!signVerified) {
            throw new RequestParamsException(ApiCode.DATA_CHECK_ERROR,ApiCode.DATA_CHECK_ERROR.getTemplate());
        }
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
        //apiNormalReqService.recordNormalReq(req);
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
