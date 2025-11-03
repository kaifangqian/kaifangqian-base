/**
 * @description 签署节点类型
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
package com.kaifangqian.modules.opensign.enums;

public enum TaskTypeEnum {
    START_FLOW("start_flow", "发起节点"),
    INITIATE_FLOW("initiate_flow", "初始化流程"),
    B_START_TASK("b_start_task", "发起前审批节点"),
    REAL_START_FLOW("real_start_flow", "真正开始流程"),
    INITIATE_WRITE_TASK("initiate_write_task", "初始化填写节点"),
    WRITE_TASK("write_task", "填写节点"),
    B_SIGN_TASK("b_sign_task", "签署前审批节点"),
    INITIATE_SIGN_TASK("initiate_sign_task", "初始化签署节点"),
    SIGN_TASK("sign_task", "签署节点"),
    APPROVE_TASK("approve_task", "审批节点"),
    FINISH_FLOW("finish_flow", "结束流程"),
    ;

    private String type;

    private String name;

    TaskTypeEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }


    public String getCode() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }
}