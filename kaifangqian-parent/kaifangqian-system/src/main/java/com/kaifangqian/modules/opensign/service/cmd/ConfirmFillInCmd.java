/**
 * @description 确认填写命令
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
package com.kaifangqian.modules.opensign.service.cmd;

import cn.hutool.core.collection.CollUtil;
import com.kaifangqian.modules.opensign.interceptor.SignCommand;
import com.kaifangqian.modules.opensign.interceptor.SignCommandContext;
import com.kaifangqian.modules.opensign.service.flow.IFlowService;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.modules.opensign.config.OpensignFlowIntiConfig;
import com.kaifangqian.modules.opensign.dto.TaskCmdInfo;
import com.kaifangqian.modules.opensign.entity.SignRu;
import com.kaifangqian.modules.opensign.entity.SignRuOperator;
import com.kaifangqian.modules.opensign.entity.SignRuTask;
import com.kaifangqian.modules.opensign.service.ru.SignRuOperatorService;
import com.kaifangqian.modules.opensign.service.ru.SignRuService;
import com.kaifangqian.modules.opensign.service.ru.SignRuTaskService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : zhenghuihan
 * create at:  2023/11/8  15:55
 * @description: 确认填写命令
 */
public class ConfirmFillInCmd implements SignCommand<TaskCmdInfo> {

    @Override
    public TaskCmdInfo execute(SignCommandContext signCommandContext) {
        SignRuTaskService signRuTaskService = signCommandContext.getSignRuTaskService();
        SignRuOperatorService signRuOperatorService = signCommandContext.getSignRuOperatorService();
        IFlowService flowService = signCommandContext.getFlowService();
        //修改任务状态
        SignRuTask signRuTask = signRuTaskService.getById(signCommandContext.getTaskId());
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        signRuTask.setCompleteUserId(loginUser.getId());
        signRuTask.setCompleteTenantId(loginUser.getTenantId());
        signRuTask.setCompleteTenantUserId(loginUser.getTenantUserId());
        signRuTask.setTaskStatus(2);
        signRuTask.setCheckMenuType("approve");
        signRuTask.setCheckTime(new Date());
        signRuTaskService.updateById(signRuTask);
        //修改实例-用户操作表任务状态
        SignRuOperator query = new SignRuOperator();
        query.setSignRuId(signCommandContext.getSignRuId());
        query.setOperateType(1);
        query.setSignerType(signCommandContext.getUserType());
        query.setSignerId(signCommandContext.getUserTaskId());
        List<SignRuOperator> operators = signRuOperatorService.getByEntity(query);
        if (CollUtil.isNotEmpty(operators)) {
            operators.forEach(o -> {
                o.setOperateStatus(3);
                o.setOperateTime(new Date());
            });

            signRuOperatorService.updateBatchById(operators);
        }
        //计算后续工作
        SignRuService signRuService = signCommandContext.getSignRuService();
        SignRu signRu = signRuService.getById(signCommandContext.getSignRuId());
        if (signRu.getSignOrderType() == 2) {
            //无序时
            SignRuTask ruQuery = new SignRuTask();
            ruQuery.setSignRuId(signCommandContext.getSignRuId());
            ruQuery.setTaskType(signCommandContext.getTaskType());
            ruQuery.setTaskStatus(1);
            List<SignRuTask> list = signRuTaskService.getByEntity(ruQuery);
            if (CollUtil.isEmpty(list)) {
                return getNextCmd(signCommandContext);
            }
        } else if (signRu.getSignOrderType() == 1) {
            //有序时
            SignRuOperator Opquery = new SignRuOperator();
            Opquery.setSignRuId(signCommandContext.getSignRuId());
            Opquery.setOperateType(1);
            Opquery.setOperateStatus(1);
            List<SignRuOperator> list = signRuOperatorService.getByEntity(Opquery);
            if (CollUtil.isNotEmpty(list)) {
                List<SignRuOperator> newList = new ArrayList<>();
                newList.add(list.get(0));
                flowService.computeFillInTask(newList, 2, signCommandContext.getSignRuId());
                return null;
            }
            return getNextCmd(signCommandContext);
        }
        return null;
    }

    TaskCmdInfo getNextCmd(SignCommandContext signCommandContext) {
        String nextTaskType = OpensignFlowIntiConfig.taskMap.get(signCommandContext.getTaskType()).getNextTaskType();
        if (nextTaskType != null) {
            TaskCmdInfo result = new TaskCmdInfo();
            result.setTaskType(nextTaskType);
            //todo 如果修改驱动逻辑需要修改该步骤
            result.setOperate("initiateSignTask");
            return result;
        } else {

            return null;
        }
    }
}