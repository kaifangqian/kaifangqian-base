/**
 * @description 数据权限切面处理类
 * 当被请求的方法有注解PermissionData时,会在往当前request中写入数据权限信息
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
package com.kaifangqian.common.aspect;

import cn.hutool.core.collection.CollUtil;
import com.kaifangqian.common.system.util.PaasDataAutorUtils;
import com.kaifangqian.common.system.vo.SysPermissionDataRuleModel;
import com.kaifangqian.common.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import com.kaifangqian.common.api.CommonAPI;
import com.kaifangqian.common.aspect.annotation.PermissionData;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author: zhh
 * @DateTime 2022/6/6 14:08
 */
@Aspect
@Component
@Slf4j
public class PermissionDataAspect {

    @Resource
    private CommonAPI commonAPI;

    @Pointcut("@annotation(com.kaifangqian.common.aspect.annotation.PermissionData)")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object arround(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        PermissionData pd = method.getAnnotation(PermissionData.class);
        String perms = pd.perms();
        //查询数据权限信息
        List<List<List<List<SysPermissionDataRuleModel>>>> dataRules = commonAPI.queryPermissionDataRule(perms);
        if (CollUtil.isNotEmpty(dataRules)) {
            //临时存储
            PaasDataAutorUtils.installDataSearchConditon(request, dataRules);
        }
        return point.proceed();
    }
}
