/**
 * @description 用户类型
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
package com.kaifangqian.external.enums;

/**
 * @author : wwt
 * create at: 2025/6/6
 */
public enum YundunAuthUserType {
    GROUP(2, "团队"),
    PERSONAL(1, "个人"),
    UNKNOWN(3, "未知");

    private Integer type;

    private String name;

    YundunAuthUserType(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public YundunAuthUserType setType(Integer type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public YundunAuthUserType setName(String name) {
        this.name = name;
        return this;
    }

    public static YundunAuthUserType getByType(Integer type) {
        for (YundunAuthUserType tenantType : YundunAuthUserType.values()) {
            if (tenantType.getType().equals(type)) {
                return tenantType;
            }
        }
        return YundunAuthUserType.UNKNOWN;
    }
}