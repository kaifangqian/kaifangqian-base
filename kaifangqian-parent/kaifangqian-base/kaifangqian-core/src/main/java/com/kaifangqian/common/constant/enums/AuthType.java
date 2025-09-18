/**
 * @description 权限类型枚举
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
package com.kaifangqian.common.constant.enums;

/**
 * 权限类型枚举
 *
 * @author zhh
 */
public enum AuthType {
    // 角色
    ROLE("role", "角色"),
    //用户
    USER("user", "用户"),
    //部门
    DEPT("dept", "部门"),
    //多级主管
    SUPERIOR("superior", "多级主管"),
    //全部
    ALL("all", "全部");

    private String type;

    private String name;

    AuthType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public AuthType setType(String type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public AuthType setName(String name) {
        this.name = name;
        return this;
    }
}
