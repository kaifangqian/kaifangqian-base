/**
 * @description 签署节点业务相关信息
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
package com.kaifangqian.modules.opensign.interceptor;

import com.kaifangqian.modules.opensign.service.business.RuSignFlowService;
import com.kaifangqian.modules.opensign.service.flow.IFlowService;
import com.kaifangqian.modules.opensign.service.ru.*;
import com.kaifangqian.modules.opensign.service.ru.*;
import com.kaifangqian.modules.system.service.ISysTenantUserService;
import com.kaifangqian.modules.system.service.ISysUserService;
import com.kaifangqian.utils.SysMessageUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class SignCommandContext {
    //实例ID
    private String signRuId;
    //任务ID
    private String taskId;
    //任务类型
    private String taskType;
    //节点用户类型
    private Integer userType;
    //用户节点ID
    private String userTaskId;

    //流程实例主表
    @Autowired
    private SignRuService signRuService;
    //流程实例-任务节点表
    @Autowired
    private SignRuTaskService signRuTaskService;
    //流程实例-抄送人表
    @Autowired
    private SignRuCcerService signRuCcerService;
    //流程实例-操作表
    @Autowired
    private SignRuOperatorService signRuOperatorService;
    //流程实例-签署主表
    @Autowired
    private SignRuSignerService signRuSignerService;
    //流程实例-内部签署表
    @Autowired
    private SignRuSenderService signRuSenderService;
    @Autowired
    private SignRuRelationService signRuRelationService;
    @Autowired
    private ISysTenantUserService sysTenantUserService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IFlowService flowService;
    @Autowired
    private RuSignFlowService ruSignFlowService;
    @Autowired
    private SysMessageUtil sysMessageUtil;
}