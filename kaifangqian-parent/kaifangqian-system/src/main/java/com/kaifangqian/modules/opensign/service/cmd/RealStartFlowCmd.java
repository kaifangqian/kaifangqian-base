/**
 * @description 真正发起流程命令
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

import com.kaifangqian.modules.opensign.interceptor.SignCommand;
import com.kaifangqian.modules.opensign.interceptor.SignCommandContext;
import com.kaifangqian.modules.opensign.service.flow.IFlowService;
import com.kaifangqian.modules.opensign.config.OpensignFlowIntiConfig;
import com.kaifangqian.modules.opensign.dto.TaskCmdInfo;
import com.kaifangqian.modules.opensign.entity.SignRu;
import com.kaifangqian.modules.opensign.enums.SignRuStatusEnum;
import com.kaifangqian.modules.opensign.service.ru.SignRuService;

/**
 * @author : zhenghuihan
 * create at:  2023/11/8  15:55
 * @description: 真正发起流程命令
 */
public class RealStartFlowCmd implements SignCommand<TaskCmdInfo> {

    @Override
    public TaskCmdInfo execute(SignCommandContext signCommandContext) {
        SignRuService signRuService = signCommandContext.getSignRuService();
        IFlowService flowService = signCommandContext.getFlowService();
        SignRu signRu = signRuService.getById(signCommandContext.getSignRuId());
        //修改状态
        signRu.setStatus(SignRuStatusEnum.START.getCode());
        signRuService.updateById(signRu);
        //发起人创建发起任务节点
        flowService.addStartFlowTask(signCommandContext.getSignRuId());
        //文件发起后抄送
        if (signRu != null) {
            if (signRu.getCcedOpportunityType() == 1) {
                signCommandContext.getFlowService().copyFlow(signCommandContext.getSignRuId(),1);
            }
        }
        String nextTaskType = OpensignFlowIntiConfig.taskMap.get(signCommandContext.getTaskType()).getNextTaskType();
        if (nextTaskType != null) {
            TaskCmdInfo result = new TaskCmdInfo();
            result.setTaskType(nextTaskType);
            //todo 如果修改驱动逻辑需要修改该步骤
            result.setOperate("initiateFillInTask");
            return result;
        } else {

            return null;
        }
    }
}