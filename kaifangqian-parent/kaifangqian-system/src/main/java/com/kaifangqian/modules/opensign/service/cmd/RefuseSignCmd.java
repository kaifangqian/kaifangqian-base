/**
 * @description 拒绝签署命令
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
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.dto.MailDto;
import com.kaifangqian.eunms.MesAuthType;
import com.kaifangqian.eunms.SendType;
import com.kaifangqian.modules.opensign.dto.TaskCmdInfo;
import com.kaifangqian.modules.opensign.entity.SignRu;
import com.kaifangqian.modules.opensign.entity.SignRuOperator;
import com.kaifangqian.modules.opensign.entity.SignRuTask;
import com.kaifangqian.modules.opensign.enums.SignRuStatusEnum;
import com.kaifangqian.modules.opensign.service.ru.SignRuOperatorService;
import com.kaifangqian.modules.opensign.service.ru.SignRuService;
import com.kaifangqian.modules.opensign.service.ru.SignRuTaskService;
import com.kaifangqian.utils.SysMessageUtil;

import java.util.*;

/**
 * @author : zhenghuihan
 * create at:  2023/11/8  15:55
 * @description: 拒绝签署命令
 */
public class RefuseSignCmd implements SignCommand<TaskCmdInfo> {

    @Override
    public TaskCmdInfo execute(SignCommandContext signCommandContext) {
        SignRuService signRuService = signCommandContext.getSignRuService();
        SignRuTaskService signRuTaskService = signCommandContext.getSignRuTaskService();
        SignRuOperatorService signRuOperatorService = signCommandContext.getSignRuOperatorService();
        SysMessageUtil sysMessageUtil = signCommandContext.getSysMessageUtil();
        //修改实例状态
        SignRu signRu = signRuService.getById(signCommandContext.getSignRuId());
        signRu.setStatus(SignRuStatusEnum.REJECT_SIGN.getCode());
        signRuService.updateById(signRu);
        //修改任务状态
        SignRuTask signRuTask = signRuTaskService.getById(signCommandContext.getTaskId());
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        signRuTask.setCompleteUserId(loginUser.getId());
        signRuTask.setCompleteTenantId(loginUser.getTenantId());
        signRuTask.setCompleteTenantUserId(loginUser.getTenantUserId());
        signRuTask.setTaskStatus(2);
        signRuTask.setCheckMenuType("reject");
        signRuTask.setCheckTime(new Date());
        signRuTaskService.updateById(signRuTask);
        //修改实例-用户操作表任务状态
        SignRuOperator query = new SignRuOperator();
        query.setSignRuId(signCommandContext.getSignRuId());
        query.setOperateType(2);
        query.setSignerType(signCommandContext.getUserType());
        query.setSignerId(signCommandContext.getUserTaskId());
        List<SignRuOperator> operators = signRuOperatorService.getByEntity(query);
        if (CollUtil.isNotEmpty(operators)) {
            operators.forEach(o -> {
                o.setOperateStatus(4);
                o.setOperateTime(new Date());
            });

            signRuOperatorService.updateBatchById(operators);
        }
        //todo 停止运行中的流程(activiti审批流)

        MailDto mailDto = new MailDto();
        mailDto.setSendType(SendType.IMMEDIATELY);

        Map<MesAuthType, List<String>> userMap = new HashMap<>();
        List<String> userIds = Arrays.asList(signRu.getSysUserId());
        userMap.put(MesAuthType.USER, userIds);
        mailDto.setReceivers(userMap);

        mailDto.setTemplateCode("opensign_sign_reject");

        Map<String, String> titleParaMap = new HashMap<>();
        titleParaMap.put("contract", signRu.getSubject());
        titleParaMap.put("signer", loginUser.getRealname());
        mailDto.setTitleParaMap(titleParaMap);

        Map<String, String> contentParaMap = new HashMap<>();
        contentParaMap.put("contract", signRu.getSubject());
        contentParaMap.put("signer", loginUser.getRealname());
        mailDto.setContentParaMap(contentParaMap);

        Map<String, Map<String, String>> buttonParaMap = new HashMap<>();
        Map<String, String> para = new HashMap<>();
        para.put("__full__", "");
        para.put("isDetail", "true");
        para.put("signReId", signRu.getSignReId());
        para.put("signRuId", signRu.getId());
        para.put("type", "");
        para.put("from", "list");
        buttonParaMap.put("sign_reject", para);
        mailDto.setButtonParaMap(buttonParaMap);

        //发送通知
        sysMessageUtil.asyncSendMail(mailDto);
        return null;
    }
}