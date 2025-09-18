/**
 * [类功能描述：shiro权限校验配置]
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

import com.kaifangqian.config.shiro.filters.CustomShiroFilterFactoryBean;
import com.kaifangqian.common.util.oConvertUtils;
import com.kaifangqian.config.shiro.filters.JwtFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.IRedisManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisClusterManager;
import org.crazycake.shiro.RedisManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.util.StringUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.*;

/**
 * @Author: zhh
 */
@Slf4j
@Configuration
public class ShiroConfig {

    @Value("${paas.shiro.excludeUrls}")
    private String excludeUrls;
    @Resource
    private LettuceConnectionFactory lettuceConnectionFactory;


    /**
     * Filter Chain定义说明
     * <p>
     * 1、一个URL可以配置多个Filter，使用逗号分隔
     * 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     */
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        CustomShiroFilterFactoryBean shiroFilterFactoryBean = new CustomShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        if (oConvertUtils.isNotEmpty(excludeUrls)) {
            String[] permissionUrl = excludeUrls.split(",");
            for (String url : permissionUrl) {
                filterChainDefinitionMap.put(url, "anon");
            }
        }
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/sys/randomImage/**", "anon"); //登录验证码接口排除
        filterChainDefinitionMap.put("/sys/checkCaptcha", "anon"); //登录验证码接口排除
        filterChainDefinitionMap.put("/sys/getAcountType", "anon"); //获取账号类型接口排除
        filterChainDefinitionMap.put("/sys/sendMessage", "anon"); //发送短信接口排除
        filterChainDefinitionMap.put("/sys/sendEmail", "anon"); //发送邮件接口排除
        filterChainDefinitionMap.put("/sys/updatePassword", "anon"); //修改密码接口排除
        filterChainDefinitionMap.put("/sys/sendMessageByUsername", "anon"); //修改密码接口排除
        filterChainDefinitionMap.put("/sys/sendEmailByUsername", "anon"); //修改密码接口排除
        filterChainDefinitionMap.put("/sys/phoneLogin", "anon"); //手机登录接口排除
        filterChainDefinitionMap.put("/sys/visitorLogin", "anon"); //游客登录接口排除
        filterChainDefinitionMap.put("/sys/findUsername", "anon"); //找回用户名接口排除
        filterChainDefinitionMap.put("/api/sysConfig/passwordMinimumLen", "anon"); //获取密码最小长度接口排除
        filterChainDefinitionMap.put("/api/sysConfig/passwordComposition", "anon"); //获取密码组成接口排除
        filterChainDefinitionMap.put("/sys/login", "anon"); //登录接口排除
        filterChainDefinitionMap.put("/sys/codeRegisterLogin", "anon"); //登录注册接口排除
        filterChainDefinitionMap.put("/sys/userJion", "anon"); //用户加入系统或租户接口排除
        filterChainDefinitionMap.put("/sys/personalTenantRegister", "anon"); //个人租户用户注册接口欧排除
        filterChainDefinitionMap.put("/file/downloadFileStream/*", "anon");//文件下载测试
        filterChainDefinitionMap.put("/file/downloadFileBase/*", "anon");//文件下载测试
        filterChainDefinitionMap.put("/system/sysTenantInfo/getTenantNameByKey", "anon");//根据key获取租户名称
        filterChainDefinitionMap.put("/system/tenantInfoExtend/tenantRegister", "anon");//注册
        filterChainDefinitionMap.put("/sys/sysTextConfig/info", "anon");//协议
        filterChainDefinitionMap.put("/sys/article/type/list", "anon");//文章分类列表
        filterChainDefinitionMap.put("/sys/article/listNoAuth", "anon");//文章列表
        filterChainDefinitionMap.put("/sys/article/info", "anon");//文章详情
        filterChainDefinitionMap.put("/sign/verify/checkSign", "anon");//电子印章-文件验签
        filterChainDefinitionMap.put("/sign/ru/sign/checkKey", "anon");//校验额二维码key
        filterChainDefinitionMap.put("/sign/ru/sign/signature", "anon");//推送二维码图片
        filterChainDefinitionMap.put("/sign/ru/info/link", "anon");//业务线实例短线链接详情
        filterChainDefinitionMap.put("/mes/message_3rdRecord/getParaByCode", "anon");//短连接获取参数
        filterChainDefinitionMap.put("/sys/check2", "anon"); //测试接口
        filterChainDefinitionMap.put("/api/test/**", "anon"); //测试
        filterChainDefinitionMap.put("/kaifangqian/openAPI/V2/**", "anon"); //api接口
        filterChainDefinitionMap.put("/openAPI/V2/**", "anon"); //api接口
        filterChainDefinitionMap.put("/sys/listApp", "anon"); //获取应用列表
        filterChainDefinitionMap.put("/sys/websiteTitle", "anon"); //获取应用标题
        filterChainDefinitionMap.put("/sys/websiteCopyright", "anon"); //获取copyRight
        filterChainDefinitionMap.put("/sys/websiteConfig", "anon");//网站配置
        filterChainDefinitionMap.put("/file/downloadFileBase64Type/*", "anon");//获取logo
        filterChainDefinitionMap.put("/yundun/auth/callback", "anon");  //实名认证更新回调
        filterChainDefinitionMap.put("/yundun/sign/callback", "anon");  //签署变更更新回调
        filterChainDefinitionMap.put("/yundun/callback/page", "anon");  //回调页面逻辑处理
        filterChainDefinitionMap.put("/doc.html", "anon");
        filterChainDefinitionMap.put("/**/*.js", "anon");
        filterChainDefinitionMap.put("/**/*.css", "anon");
        filterChainDefinitionMap.put("/**/*.html", "anon");
        filterChainDefinitionMap.put("/**/*.svg", "anon");
        filterChainDefinitionMap.put("/**/*.pdf", "anon");
        filterChainDefinitionMap.put("/**/*.jpg", "anon");
        filterChainDefinitionMap.put("/**/*.png", "anon");
        filterChainDefinitionMap.put("/**/*.ico", "anon");
        filterChainDefinitionMap.put("/**/*.ttf", "anon");
        filterChainDefinitionMap.put("/**/*.woff", "anon");
        filterChainDefinitionMap.put("/**/*.woff2", "anon");
        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/swagger**/**", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/v2/**", "anon");
        filterChainDefinitionMap.put("/websocket/**", "anon"); //websocket排除
        filterChainDefinitionMap.put("/actuator/**", "anon"); //性能监控
        filterChainDefinitionMap.put("/testApi/**", "anon"); //API测试
        filterChainDefinitionMap.put("/captcha/get", "anon"); //验证码模块
        filterChainDefinitionMap.put("/captcha/check", "anon"); //验证码模块
        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new HashMap<String, Filter>(1);
        filterMap.put("jwt", new JwtFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        // <!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边
        filterChainDefinitionMap.put("/**", "jwt");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(ShiroRealm myRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myRealm);
        /*
         * 关闭shiro自带的session，详情见文档
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        //自定义缓存实现,使用redis
        securityManager.setCacheManager(redisCacheManager());
        return securityManager;
    }

    /**
     * 下面的代码是添加注解支持
     *
     * @return
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        /**
         * 解决重复代理问题 github#994
         * 添加前缀判断 不匹配 任何Advisor
         */
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        defaultAdvisorAutoProxyCreator.setAdvisorBeanNamePrefix("_no_advisor");
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    public RedisCacheManager redisCacheManager() {
        log.info("===============(1)创建缓存管理器RedisCacheManager");
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        //redis中针对不同用户缓存(此处的id需要对应user实体中的id字段,用于唯一标识)
        redisCacheManager.setPrincipalIdFieldName("userAuthId");
        //用户权限信息缓存时间
        redisCacheManager.setExpire(200000);
        return redisCacheManager;
    }

    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public IRedisManager redisManager() {
        log.info("===============(2)创建RedisManager,连接Redis..");
        IRedisManager manager;
        // redis 单机支持，在集群为空，或者集群无机器时候使用 add by jzyadmin@163.com
        if (lettuceConnectionFactory.getClusterConfiguration() == null || lettuceConnectionFactory.getClusterConfiguration().getClusterNodes().isEmpty()) {
            RedisManager redisManager = new RedisManager();
            redisManager.setHost(lettuceConnectionFactory.getHostName() + ":" + lettuceConnectionFactory.getPort());
//            redisManager.setPort(lettuceConnectionFactory.getPort());
            redisManager.setDatabase(lettuceConnectionFactory.getDatabase());
            redisManager.setTimeout(0);
            if (!StringUtils.isEmpty(lettuceConnectionFactory.getPassword())) {
                redisManager.setPassword(lettuceConnectionFactory.getPassword());
            }
            manager = redisManager;
        } else {
            // redis集群支持，优先使用集群配置
            RedisClusterManager redisManager = new RedisClusterManager();
            Set<HostAndPort> portSet = new HashSet<>();
            lettuceConnectionFactory.getClusterConfiguration().getClusterNodes().forEach(node -> portSet.add(new HostAndPort(node.getHost(), node.getPort())));
            if (oConvertUtils.isNotEmpty(lettuceConnectionFactory.getPassword())) {
                JedisCluster jedisCluster = new JedisCluster(portSet, 2000, 2000, 5, lettuceConnectionFactory.getPassword(), new GenericObjectPoolConfig());
                redisManager.setPassword(lettuceConnectionFactory.getPassword());
                redisManager.setJedisCluster(jedisCluster);
            } else {
                JedisCluster jedisCluster = new JedisCluster(portSet);
                redisManager.setJedisCluster(jedisCluster);
            }
            manager = redisManager;
        }
        return manager;
    }
}
