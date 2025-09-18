/**
 * [类功能描述：mybatis拦截器，自动注入创建人、创建时间、修改人、修改时间]
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
package com.kaifangqian.config.mybatis;

import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.oConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @Author zhh
 */
@Slf4j
@Component
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class MybatisInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        String sqlId = mappedStatement.getId();
        log.debug("------sqlId------" + sqlId);
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        Object parameter = invocation.getArgs()[1];
        log.debug("------sqlCommandType------" + sqlCommandType);

        if (parameter == null) {
            return invocation.proceed();
        }
        LoginUser sysUser = this.getLoginUser();
        if (SqlCommandType.INSERT == sqlCommandType) {
            Field[] fields = oConvertUtils.getAllFields(parameter);
            for (Field field : fields) {
                log.debug("------field.name------" + field.getName());
                try {
                    if ("createBy".equals(field.getName())) {
                        field.setAccessible(true);
                        Object local_createBy = field.get(parameter);
                        field.setAccessible(false);
                        if (local_createBy == null || local_createBy.equals("")) {
                            if (sysUser != null) {
                                // 登录人账号
                                field.setAccessible(true);
                                field.set(parameter, sysUser.getUsername());
                                field.setAccessible(false);
                            }
                        }
                    }
                    // 注入创建时间
                    if ("createTime".equals(field.getName())) {
                        field.setAccessible(true);
                        Object local_createDate = field.get(parameter);
                        field.setAccessible(false);
                        if (local_createDate == null || local_createDate.equals("")) {
                            field.setAccessible(true);
                            field.set(parameter, new Date());
                            field.setAccessible(false);
                        }
                    }
                    //注入所属账号ID
                    if ("sysAccountId".equals(field.getName())) {
                        field.setAccessible(true);
                        Object local_sysAccountId = field.get(parameter);
                        field.setAccessible(false);
                        if (local_sysAccountId == null || local_sysAccountId.equals("")) {
                            if (sysUser != null) {
                                field.setAccessible(true);
                                field.set(parameter, sysUser.getId());
                                field.setAccessible(false);
                            }
                        }
                    }
                    //注入租户ID
                    if ("sysTenantId".equals(field.getName())) {
                        field.setAccessible(true);
                        Object local_sysTenantId = field.get(parameter);
                        field.setAccessible(false);
                        if (local_sysTenantId == null || local_sysTenantId.equals("")) {
                            if (sysUser != null) {
                                field.setAccessible(true);
                                field.set(parameter, sysUser.getTenantId());
                                field.setAccessible(false);
                            }
                        }
                    }
                    //注入所属租户-用户ID
                    if ("sysUserId".equals(field.getName())) {
                        field.setAccessible(true);
                        Object local_sysUserId = field.get(parameter);
                        field.setAccessible(false);
                        if (local_sysUserId == null || local_sysUserId.equals("")) {
                            if (sysUser != null) {
                                field.setAccessible(true);
                                field.set(parameter, sysUser.getTenantUserId());
                                field.setAccessible(false);
                            }
                        }
                    }
                    //注入部门编码
                    if ("sysOrgCode".equals(field.getName())) {
                        field.setAccessible(true);
                        Object local_sysOrgCode = field.get(parameter);
                        field.setAccessible(false);
                        if (local_sysOrgCode == null || local_sysOrgCode.equals("")) {
                            if (sysUser != null) {
                                field.setAccessible(true);
                                field.set(parameter, sysUser.getOrgCode());
                                field.setAccessible(false);
                            }
                        }
                    }

                    // 测试ID
                    if ("id".equals(field.getName())) {
                        // 如果类型是String的话，生成uuid。long的话用mybatis的雪花算法生成ID
                        if (field.getGenericType().toString().equals(
                                "class java.lang.String")) {
                            // TODO: 2022/3/16 ID生成方式可以优化
                            field.setAccessible(true);
                            Object id = field.get(parameter);
                            field.setAccessible(false);
                            if (ObjectUtils.isEmpty(id)) {
                                id = UUID.randomUUID().toString();
                                //设置主数据ID
//                                ThreadlocalVO threadlocalVO = ProcTaskInfo.THREAD_LOCAL.get();
//                                if (threadlocalVO != null && StringUtils.isNotBlank(threadlocalVO.getTaskIdACt())) {
//                                    threadlocalVO.setId((String) id);
//                                }
                            }
                            field.setAccessible(true);
                            field.set(parameter, id);
                            field.setAccessible(false);
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        if (SqlCommandType.UPDATE == sqlCommandType) {
            Field[] fields = null;
            if (parameter instanceof ParamMap) {
                ParamMap<?> p = (ParamMap<?>) parameter;
                if (p.containsKey("et")) {
                    parameter = p.get("et");
                } else {
                    parameter = p.get("param1");
                }
                if (parameter == null) {
                    return invocation.proceed();
                }
                fields = oConvertUtils.getAllFields(parameter);
            } else {
                fields = oConvertUtils.getAllFields(parameter);
            }

            for (Field field : fields) {
                log.debug("------field.name------" + field.getName());
                try {
                    if ("updateBy".equals(field.getName())) {
                        //获取登录用户信息
                        if (sysUser != null) {
                            // 登录账号
                            field.setAccessible(true);
                            field.set(parameter, sysUser.getUsername());
                            field.setAccessible(false);
                        }
                    }
                    if ("updateTime".equals(field.getName())) {
                        field.setAccessible(true);
                        field.set(parameter, new Date());
                        field.setAccessible(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    private LoginUser getLoginUser() {
        LoginUser sysUser = null;
        try {
            sysUser = SecurityUtils.getSubject().getPrincipal() != null ? (LoginUser) SecurityUtils.getSubject().getPrincipal() : null;
        } catch (Exception e) {
            sysUser = null;
        }
        return sysUser;
    }
}
