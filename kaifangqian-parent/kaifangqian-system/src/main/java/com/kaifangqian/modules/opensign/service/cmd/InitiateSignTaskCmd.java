/**
 * @description 初始化签署节点
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
import com.kaifangqian.modules.opensign.service.business.RuSignFlowService;
import com.kaifangqian.modules.opensign.service.flow.IFlowService;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.opensign.config.OpensignFlowIntiConfig;
import com.kaifangqian.modules.opensign.dto.TaskCmdInfo;
import com.kaifangqian.modules.opensign.entity.SignRu;
import com.kaifangqian.modules.opensign.entity.SignRuSigner;
import com.kaifangqian.modules.opensign.enums.SignRuStatusEnum;
import com.kaifangqian.modules.opensign.service.ru.SignRuService;
import com.kaifangqian.modules.opensign.service.ru.SignRuSignerService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : zhenghuihan
 * create at:  2023/11/8  15:55
 * @description: 初始化签署节点
 */
public class InitiateSignTaskCmd implements SignCommand<TaskCmdInfo> {

    @Override
    public TaskCmdInfo execute(SignCommandContext signCommandContext)  {
        //填写文档
        RuSignFlowService ruSignFlowService = signCommandContext.getRuSignFlowService();
        ruSignFlowService.write(signCommandContext.getSignRuId());

        SignRuService signRuService = signCommandContext.getSignRuService();
        SignRuSignerService signRuSignerService = signCommandContext.getSignRuSignerService();
        IFlowService flowService = signCommandContext.getFlowService();
        SignRu signRu = signRuService.getById(signCommandContext.getSignRuId());
        //修改实例状态
        signRu.setStatus(SignRuStatusEnum.SIGNING.getCode());
        signRuService.updateById(signRu);
        SignRuSigner query = new SignRuSigner();
        query.setSignRuId(signCommandContext.getSignRuId());
        List<SignRuSigner> signers = signRuSignerService.getByEntity(query);
        List<SignRuSigner> signer1s = new ArrayList<>();
        List<SignRuSigner> signer2s = new ArrayList<>();
        List<SignRuSigner> signer3s = new ArrayList<>();
        if (CollUtil.isNotEmpty(signers)) {
            signers.forEach(s -> {
                if (s.getSignerType() == 1) {
                    signer1s.add(s);
                } else if (s.getSignerType() == 2) {
                    signer2s.add(s);
                } else if (s.getSignerType() == 3) {
                    signer3s.add(s);
                }
            });
        } else {
            throw new PaasException("签署方不能为空");
        }
        //无序签署
        if (signRu.getSignOrderType() == 2) {
            //初始化外部用户节点
            if (CollUtil.isNotEmpty(signer2s)) {
                //处理外部个人用户节点：生成签署任务、实例操作任务修改状态
                flowService.computePersonalOutSignTask(signer2s, signCommandContext.getSignRuId());
            }
            if (CollUtil.isNotEmpty(signer3s)) {
                //处理外部企业用户节点：生成签署任务、实例操作任务修改状态
                flowService.computeCompanyOutSignTask(signer3s, null, signCommandContext.getSignRuId());
            }
            //运行计算内部用户节点任务
            return getNextCmd(signCommandContext);
        }
        //顺序签署
        if (signRu.getSignOrderType() == 1) {
            if (signers.get(0).getSignerType() == 1) {
                //运行计算内部用户节点任务
                return getNextCmd(signCommandContext);
            } else if (signers.get(0).getSignerType() == 2) {
                List<SignRuSigner> newList = new ArrayList<>();
                newList.add(signers.get(0));
                //处理外部个人用户节点：生成签署任务、实例操作任务修改状态
                flowService.computePersonalOutSignTask(newList, signCommandContext.getSignRuId());
            } else if (signers.get(0).getSignerType() == 3) {
                List<SignRuSigner> newList = new ArrayList<>();
                newList.add(signers.get(0));
                //处理外部企业用户节点：生成签署任务、实例操作任务修改状态
                flowService.computeCompanyOutSignTask(newList, null, signCommandContext.getSignRuId());
            }
        }
        return null;
    }

    TaskCmdInfo getNextCmd(SignCommandContext signCommandContext) {
        String nextTaskType = OpensignFlowIntiConfig.taskMap.get(signCommandContext.getTaskType()).getNextTaskType();
        if (nextTaskType != null) {
            TaskCmdInfo result = new TaskCmdInfo();
            result.setTaskType(nextTaskType);
            //todo 如果修改驱动逻辑需要修改该步骤
            result.setOperate("signActivitiFlow");
            return result;
        }
        return null;
    }
}