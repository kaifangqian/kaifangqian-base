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
 * @description: 租户扩展类型
 */
public enum TenantExtendType {
    TYPE1(1, "个人"),
    TYPE2(2, "企业"),
    TYPE3(3, "工作站"),
    TYPE4(4, "版权局"),
    TYPE5(5, "区县版权局"),
    ;

    private Integer type;

    private String name;

    TenantExtendType(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public TenantExtendType setType(Integer type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public TenantExtendType setName(String name) {
        this.name = name;
        return this;
    }
}