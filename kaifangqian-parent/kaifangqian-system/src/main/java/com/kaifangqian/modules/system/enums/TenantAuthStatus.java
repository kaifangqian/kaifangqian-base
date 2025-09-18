/**
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
package com.kaifangqian.modules.system.enums;

/**
 * @author : zhenghuihan
 * create at:  2022/9/1  15:33
 * @description: 租户审核状态
 */
public enum TenantAuthStatus {
    STATUS0(0, "未认证 "),
    STATUS1(1, "审核中"),
    STATUS2(2, "已认证"),
    STATUS3(3, "未通过"),

    ;

    private Integer status;

    private String name;

    TenantAuthStatus(Integer status, String name) {
        this.status = status;
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public TenantAuthStatus setType(Integer status) {
        this.status = status;
        return this;
    }

    public String getName() {
        return name;
    }

    public TenantAuthStatus setName(String name) {
        this.name = name;
        return this;
    }
}