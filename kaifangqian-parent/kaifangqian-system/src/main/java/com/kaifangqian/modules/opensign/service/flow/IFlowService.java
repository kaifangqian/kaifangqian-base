/**
 * @description 流程驱动服务接口类
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
package com.kaifangqian.modules.opensign.service.flow;

import com.kaifangqian.modules.opensign.dto.TaskCmdInfo;
import com.kaifangqian.modules.opensign.entity.SignRuOperator;
import com.kaifangqian.modules.opensign.entity.SignRuSigner;

import java.util.List;

/**
 * @author : zhenghuihan
 * create at:  2023/11/8  15:11
 * @description: 流程驱动服务接口类
 */
public interface IFlowService {
    /**
     * 驱动流程:instanceId:实例ID taskId:任务ID operate：操作
     */
    void complete(String instanceId, String operate);

    void computeFillInTask(List<SignRuOperator> list, Integer type, String signRuId);

    void addStartFlowTask(String signRuId);

    void computePersonalOutSignTask(List<SignRuSigner> signers, String signRuId);

    void computeCompanyOutSignTask(List<SignRuSigner> signers, String taskId, String signRuId) ;

    TaskCmdInfo computeCompanyOutAndNextSignTask(String taskId, String signRuId) ;

    TaskCmdInfo computeInAndNextSignTask(String taskId, String signRuId) ;

    void computeInSignTask(String taskId, String signRuId);

    /**
     * @description type:1 发起时，2 完成签署时
     */
    void copyFlow(String signRuId, Integer type);

    /**
     * 解析数据库已有数据做绑定表：sign_ru_task
     */
    void bindOutUserTask(String tenantName, String phone, String email, String fromTenantId, String fromUserId, String userId, String tenantId, String tenantUserId, String linkType);

    /**
     * 解析数据库已有数据做绑定表：sign_ru_task
     */
    void bindOutUserTaskAll(String phone, String email, String linkType);

    /**
     * 解析数据库已有数据做绑定表：sign_ru_task
     */
    void bindTenantUserTaskAll(String tenantId, String linkType);

    /**
     * 解析数据库已有数据做绑定表：sign_ru_relation
     */
    void bindOutUserRelation(String phone, String email, String tenantUserId, String linkType);

    void signReportAndSave(String ruId);
}