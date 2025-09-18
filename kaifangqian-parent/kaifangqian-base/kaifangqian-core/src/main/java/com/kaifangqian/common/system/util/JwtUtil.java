/**
 * @description JWT工具类
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
package com.kaifangqian.common.system.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaifangqian.common.constant.DataBaseConstant;
import com.kaifangqian.common.exception.Paas401Exception;
import com.kaifangqian.common.constant.CommonConstant;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.util.oConvertUtils;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;


import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.UUID;

/**
 * @author : zhenghuihan
 * create at: 2023/12/18
 */
@Slf4j
public class JwtUtil {

    //Token过期时间30分钟（用户登录过期时间是此时间的两倍，以token在reids缓存时间为准）
    public static final long EXPIRE_TIME = 30 * 60 * 1000;

    /**
     * @param response
     * @param code
     * @param errorMsg
     */
    public static void responseError(ServletResponse response, Integer code, String errorMsg) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        Result jsonResult = new Result(code, errorMsg);
        log.error(jsonResult.toString());
        OutputStream os = null;
        try {
            os = httpServletResponse.getOutputStream();
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            //httpServletResponse.setContentType("text/html;charset=UTF-8");
            httpServletResponse.setStatus(401);
            os.write(new ObjectMapper().writeValueAsString(jsonResult).getBytes("UTF-8"));
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            // 根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("username", username).build();
            // 效验TOKEN
            verifier.verify(token);
            return true;
        } catch (TokenExpiredException exception) {
            //TokenExpiredException 超时错误
            throw new AuthenticationException(CommonConstant.MAX_ALIVE_TOKEN_MSG);
        } catch (SignatureVerificationException e) {
            //SignatureVerificationException 密码错误
            throw new AuthenticationException(CommonConstant.TOKEN_IS_INVALID_MSG);
        } catch (InvalidClaimException exception) {
            //InvalidClaimException 用户名错误
            throw new AuthenticationException(CommonConstant.TOKEN_IS_INVALID_MSG);
        }
    }

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verifyAuth(String token, String username, String secret) {
        try {
            // 根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("username", username).build();
            // 效验TOKEN
            verifier.verify(token);
            return true;
        } catch (TokenExpiredException exception) {
            //TokenExpiredException 超时错误
            throw new Paas401Exception(CommonConstant.AUTH_MAX_ALIVE_TOKEN_MSG);
        } catch (SignatureVerificationException e) {
            //SignatureVerificationException 密码错误
            throw new Paas401Exception(CommonConstant.AUTH_TOKEN_IS_INVALID_MSG);
        } catch (InvalidClaimException exception) {
            //InvalidClaimException 用户名错误
            throw new Paas401Exception(CommonConstant.AUTH_TOKEN_IS_INVALID_MSG);
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名,5min后过期
     *
     * @param username 用户名
     * @param secret   用户的密码
     * @return 加密的token
     */
    public static String sign(String username, String secret) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create().withClaim("username", username).withExpiresAt(date).sign(algorithm);

    }


    /**
     * @param username    用户名
     * @param secret      用户的密码
     * @param invalidTime 失效时间
     * @return 加密的token
     */
    public static String sign(String username, String secret, long invalidTime) {
        Date date = new Date(System.currentTimeMillis() + invalidTime);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create().withClaim("username", username).withClaim("uuid", UUID.randomUUID().toString()).withExpiresAt(date).sign(algorithm);

    }

    /**
     * 根据request中的token获取用户账号
     *
     * @param request
     * @return
     * @throws PaasException
     */
    public static String getUserNameByToken(HttpServletRequest request) throws PaasException {
        String accessToken = request.getHeader("X-Access-Token");
        String username = getUsername(accessToken);
        if (oConvertUtils.isEmpty(username)) {
            throw new PaasException("未获取到用户");
        }
        return username;
    }

    /**
     * 从当前用户中获取变量
     *
     * @param key
     * @return
     */
    public static String getUserSystemData(String key) {
        // 获取登录用户信息
        LoginUser sysUser = MySecurityUtils.getCurrentUser();

        String moshi = "";
        if (key.indexOf("}") != -1) {
            moshi = key.substring(key.indexOf("}") + 1);
        }
        String returnValue = null;
        //针对特殊标示处理#{sysOrgCode}，判断替换
        if (key.contains("#{")) {
            key = key.substring(2, key.indexOf("}"));
        }
        //替换为系统登录账号ID
        if (key.equals(DataBaseConstant.SYS_USER_ID) || key.toLowerCase().equals(DataBaseConstant.SYS_USER_ID_TABLE)) {
            returnValue = sysUser.getId();
        }

        //替换为系统登录租户ID
        if (key.equals(DataBaseConstant.TENANT_ID) || key.toLowerCase().equals(DataBaseConstant.TENANT_ID_TABLE)) {
            returnValue = sysUser.getTenantId();
        }

        //替换为系统登录租户-用户ID
        if (key.equals(DataBaseConstant.TENANT_USER_ID) || key.toLowerCase().equals(DataBaseConstant.TENANT_USER_ID_TABLE)) {
            returnValue = sysUser.getTenantUserId();
        }

        //替换为系统登录用户真实名字
        else if (key.equals(DataBaseConstant.SYS_USER_REAL_NAME) || key.toLowerCase().equals(DataBaseConstant.SYS_USER_REAL_NAME_TABLE)) {
            returnValue = sysUser.getRealname();
        }

        //替换为系统用户登录所使用的机构编码
        else if (key.equals(DataBaseConstant.SYS_ORG_CODE) || key.toLowerCase().equals(DataBaseConstant.SYS_ORG_CODE_TABLE)) {
            returnValue = sysUser.getOrgCode();
        }
        //替换为当前系统时间(年月日)
        else if (key.equals(DataBaseConstant.SYS_DATE) || key.toLowerCase().equals(DataBaseConstant.SYS_DATE_TABLE)) {
            returnValue = DateUtil.formatDate();
        }
        //替换为当前系统时间（年月日时分秒）
        else if (key.equals(DataBaseConstant.SYS_TIME) || key.toLowerCase().equals(DataBaseConstant.SYS_TIME_TABLE)) {
            returnValue = DateUtil.now();
        }
        if (returnValue != null) {
            returnValue = returnValue + moshi;
        }
        return returnValue;
    }
}
