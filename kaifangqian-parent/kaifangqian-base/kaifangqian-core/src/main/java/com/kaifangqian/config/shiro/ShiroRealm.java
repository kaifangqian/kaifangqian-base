/**
 * [类功能描述：用户登录鉴权和获取用户授权]
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
package com.kaifangqian.config.shiro;

import com.kaifangqian.common.constant.CommonConstant;
import com.kaifangqian.common.exception.Paas401Exception;
import com.kaifangqian.common.redis.util.RedisUtil;
import com.kaifangqian.common.system.util.JwtUtil;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.system.vo.LoginUserAuthInfo;
import com.kaifangqian.common.util.SpringContextUtils;
import com.kaifangqian.common.util.oConvertUtils;
import com.kaifangqian.enums.SysConfigType;
import com.kaifangqian.inteface.CommonToolsAPI;
import com.kaifangqian.utils.MyStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import com.kaifangqian.common.api.CommonAPI;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: zhh
 */
@Component
@Slf4j
public class ShiroRealm extends AuthorizingRealm {
    @Lazy
    @Resource
    private CommonAPI commonAPI;

    @Lazy
    @Resource
    private RedisUtil redisUtil;

    @Lazy
    @Resource
    private CommonToolsAPI commonToolsAPI;
//    @Lazy
//    @Resource
//    private LicenseBean licenseBean;


    /**
     * 必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 权限信息认证(包括角色以及权限)是用户访问controller的时候才进行验证(redis存储的此处权限信息)
     * 触发检测用户权限时才会调用此方法，例如checkRole,checkPermission
     *
     * @param principals 身份信息
     * @return AuthorizationInfo 权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("===============Shiro权限认证开始============ [ roles、permissions]==========");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 设置用户拥有的权限集合，比如“sys:role:add,sys:user:add”
        List<String> permissionSet = commonAPI.queryUserAuths();
        info.addStringPermissions(permissionSet);
        log.info("===============Shiro权限认证成功==============");
        return info;
    }

    /**
     * 所有请求之前都要验证一下
     * 用户信息认证是在用户进行登录的时候进行验证(不存redis)
     * 也就是说验证用户输入的账号和密码是否正确，错误抛出异常
     *
     * @param auth 用户登录的账号密码信息
     * @return 返回封装了用户信息的 AuthenticationInfo 实例
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
//        //授权期限
//        Authorization grant = licenseBean.getGrant();
//        if (grant.getLicenseExpire().before(new Date())) {
//            JwtUtil.responseError(SpringContextUtils.getHttpServletResponse(), 500, "系统授权已过期，请联系平台管理员");
//        }
        log.debug("===============Shiro身份认证开始============doGetAuthenticationInfo==========");
        String token = (String) auth.getCredentials();
        if (token == null) {
            log.info("————————身份认证失败——————————IP地址:  " + oConvertUtils.getIpAddrByRequest(SpringContextUtils.getHttpServletRequest()));
            throw new AuthenticationException("token为空!");
        }
        // 校验token有效性
        LoginUser loginUser = null;
        try {
            loginUser = this.checkUserTokenIsEffect(token);
            loginUser.setToken(token);
        } catch (AuthenticationException e) {
            JwtUtil.responseError(SpringContextUtils.getHttpServletResponse(), 401, e.getMessage());
            return null;
        } catch (Paas401Exception e) {
            JwtUtil.responseError(SpringContextUtils.getHttpServletResponse(), 402, e.getMessage());
            return null;
        }
        return new SimpleAuthenticationInfo(loginUser, token, getName());
    }

    /**
     * 校验token的有效性
     *
     * @param token
     */
    public LoginUser checkUserTokenIsEffect(String token) throws AuthenticationException {
        //解密获得username，用于和数据库进行对比
        String username = JwtUtil.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("token非法无效!");
        }

        //查询用户信息
        log.debug("———校验token是否有效————checkUserTokenIsEffect——————— " + token);
        LoginUser loginUser = commonAPI.getUserByName(username);
        if (loginUser == null) {
            throw new AuthenticationException("用户不存在!");
        }
        //校验是否含有必要信息（用户Code）
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String appCode = request.getHeader(CommonConstant.HTTP_HEAD_APP_CODE);
        if (MyStringUtils.isBlank(appCode)) {
            throw new AuthenticationException("应用code不能为空");
        }
        loginUser.setAppCode(appCode);

        //判断用户状态
        if (loginUser.getStatus() != 1) {
            throw new AuthenticationException("账号已被锁定,请联系管理员!");
        }

        //校验token有效性(账号密码是否错误)
        JwtUtil.verify(token, username, loginUser.getPassword());

        //校验token是否超时失效
        Object cacheToken = redisUtil.get(CommonConstant.PREFIX_USER_TOKEN + token);
        if (oConvertUtils.isNotEmpty(cacheToken)) {
            LoginUserAuthInfo loginUserAuthInfo = (LoginUserAuthInfo) cacheToken;
            //校验授权token（主token）是否有效
            if (MyStringUtils.isNotBlank(loginUserAuthInfo.getAuthorizedToken())) {
                // 校验token有效性(账号密码是否错误)
                JwtUtil.verifyAuth(loginUserAuthInfo.getAuthorizedToken(), username, loginUser.getPassword());
                checkAuthorizedToken(loginUserAuthInfo.getAuthorizedToken());
            }
            //校验是否含有必要信息（租户ID+部门ID）
            if (MyStringUtils.isBlank(loginUserAuthInfo.getTenantId()) || MyStringUtils.isBlank(loginUserAuthInfo.getDepartId())) {
                throw new AuthenticationException("登录信息错误");
            }
            loginUser.setTenantId(loginUserAuthInfo.getTenantId());
            loginUser.setDepartId(loginUserAuthInfo.getDepartId());
            loginUser.setOrgCode(loginUserAuthInfo.getOrgCode());
            loginUser.setUserAuthId(loginUser.getId() + loginUser.getTenantId() + loginUser.getAppCode() + loginUser.getDepartId());
            loginUser.setTenantUserId(commonAPI.getTenantUserId(loginUser.getTenantId(), loginUser.getId()));

            //校验租户是否合理：租户是否可用、用户是否拥有该租户
            if (!commonAPI.checkUserTenant(loginUser.getId(), loginUser.getTenantId())) {
                throw new AuthenticationException("租户错误");
            }
            //校验部门是否合理：部门是否可用、用户是否属于该部门
            if (!commonAPI.checkUserDepart(loginUser.getId(), loginUser.getTenantId(), loginUser.getDepartId())) {
                throw new AuthenticationException("部门错误");
            }
            //校验应用是否合理
            if (!commonAPI.checkUserTenantApp(loginUser.getId(), loginUser.getTenantId(), loginUser.getAppCode())) {
                throw new AuthenticationException("应用错误");
            }

            //重新更新token失效时间
            double noOpv = commonToolsAPI.getConfigDoubleValueByType(SysConfigType.NOOPERATEKEEPALIVE.getType());
            redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, (long) (noOpv * 60 * 60));
            log.debug("——————————用户在线操作，更新token保证不掉线—————————jwtTokenRefresh——————— " + token);

            return loginUser;
        }
        throw new AuthenticationException(CommonConstant.TOKEN_IS_INVALID_MSG);
    }

    void checkAuthorizedToken(String authorizedToken) {
        //校验token是否超时失效
        Object cacheToken = redisUtil.get(CommonConstant.PREFIX_USER_TOKEN + authorizedToken);
        if (oConvertUtils.isNotEmpty(cacheToken)) {
            //重新更新token失效时间
            double noOpv = commonToolsAPI.getConfigDoubleValueByType(SysConfigType.NOOPERATEKEEPALIVE.getType());
            redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + authorizedToken, (long) (noOpv * 60 * 60));
            log.debug("——————————用户在线操作，更新token保证不掉线—————————jwtTokenRefresh——————— " + authorizedToken);
        } else {
            throw new Paas401Exception(CommonConstant.AUTH_TOKEN_IS_INVALID_MSG);
        }
    }

    /**
     * 清除当前用户的权限认证缓存
     *
     * @param principals 权限信息
     */
    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

}
