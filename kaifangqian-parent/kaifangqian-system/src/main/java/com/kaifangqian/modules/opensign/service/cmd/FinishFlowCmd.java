/**
 * @description 结束流程命令
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
import com.kaifangqian.dto.MailDto;
import com.kaifangqian.eunms.MesAuthType;
import com.kaifangqian.eunms.SendType;
import com.kaifangqian.modules.opensign.dto.TaskCmdInfo;
import com.kaifangqian.modules.opensign.entity.SignRu;
import com.kaifangqian.modules.opensign.enums.SignRuStatusEnum;
import com.kaifangqian.modules.opensign.service.ru.SignRuService;
import com.kaifangqian.utils.SysMessageUtil;

import java.util.*;

/**
 * @author : zhenghuihan
 * create at:  2023/11/8  15:55
 * @description: 结束流程命令
 */
public class FinishFlowCmd implements SignCommand<TaskCmdInfo> {

    @Override
    public TaskCmdInfo execute(SignCommandContext signCommandContext) {
        SignRuService signRuService = signCommandContext.getSignRuService();
        SysMessageUtil sysMessageUtil = signCommandContext.getSysMessageUtil();
        SignRu signRu = signRuService.getById(signCommandContext.getSignRuId());
        signRu.setStatus(SignRuStatusEnum.DONE.getCode());
        signRu.setFinishTime(new Date());
        signRuService.updateById(signRu);

        MailDto mailDto = new MailDto();
        mailDto.setSendType(SendType.IMMEDIATELY);

        Map<MesAuthType, List<String>> userMap = new HashMap<>();
        List<String> userIds = Arrays.asList(signRu.getSysUserId());
        userMap.put(MesAuthType.USER, userIds);
        mailDto.setReceivers(userMap);

        mailDto.setTemplateCode("sign_done");

        Map<String, String> titleParaMap = new HashMap<>();
        titleParaMap.put("contract", signRu.getSubject());
        mailDto.setTitleParaMap(titleParaMap);

        Map<String, String> contentParaMap = new HashMap<>();
        contentParaMap.put("contract", signRu.getSubject());
        mailDto.setContentParaMap(contentParaMap);

        Map<String, Map<String, String>> buttonParaMap = new HashMap<>();
        Map<String, String> para = new HashMap<>();
        para.put("__full__", "");
        para.put("from", "list");
        para.put("signRuId", signRu.getId());
        buttonParaMap.put("sign_complete", para);
        mailDto.setButtonParaMap(buttonParaMap);

        //发送通知
        sysMessageUtil.asyncSendMail(mailDto);

        //生成报告
        IFlowService flowService = signCommandContext.getFlowService();
        //flowService.signReportAndSave(signCommandContext.getSignRuId());

        return null;
    }
}