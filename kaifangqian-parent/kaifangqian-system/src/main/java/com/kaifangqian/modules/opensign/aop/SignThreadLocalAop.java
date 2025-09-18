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
package com.kaifangqian.modules.opensign.aop;

import com.kaifangqian.modules.opensign.dto.SignTaskInfo;
import com.kaifangqian.modules.opensign.dto.SignTaskThreadlocalVO;
import com.kaifangqian.modules.opensign.entity.SignRuTask;
import com.kaifangqian.modules.opensign.service.ru.SignRuTaskService;
import com.kaifangqian.common.system.vo.ProcTaskInfo;
import com.kaifangqian.common.util.RequestHolder;
import com.kaifangqian.utils.MyStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


/**
 * create by zhenghuihan at 2022/3/16
 *
 * @param
 * @description 处理签署线程本地变量
 * @return
 * @throws
 */
@Aspect
@Component
@Slf4j
public class SignThreadLocalAop {

    @Autowired
    private SignRuTaskService signRuTaskService;

    /**
     * 切入点
     * 根据路径切入
     */
    @Pointcut("execution(public * com.kaifangqian.modules.opensign.*.*Controller.*(..))")
    public void local() {
    }

    /**
     * 前置操作
     *
     * @param
     */
    @Before("local()")
    public void before() {
        SignTaskInfo.THREAD_LOCAL.remove();
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        String taskId = request.getHeader("Sign-Task-Id");
        SignTaskThreadlocalVO threadlocalVO = new SignTaskThreadlocalVO();
        if (MyStringUtils.isNotBlank(taskId)) {
            SignRuTask signRuTask = signRuTaskService.getById(taskId);
            if (signRuTask != null) {
                threadlocalVO.setTaskId(taskId);
                threadlocalVO.setTaskType(signRuTask.getTaskType());
                threadlocalVO.setSignRuId(signRuTask.getSignRuId());
                threadlocalVO.setUserType(signRuTask.getUserType());
                threadlocalVO.setUserTaskId(signRuTask.getUserTaskId());
                threadlocalVO.setSignOrderNo(signRuTask.getOrderNo());
            }
        }
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
    @AfterReturning("local()")
    public void afterReturning(JoinPoint point) {
        ProcTaskInfo.THREAD_LOCAL.remove();
    }
}
