/**
 * @description 签署人实名认证错误类型枚举
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

public enum SignAuthCheckEnum {
    AUTH0(0, "信息错误"),
    AUTH1(1, "一致"),
    AUTH2(2, "账号不一致,目标账号存在"),
    AUTH3(3, "账号不一致，目标账号不存在"),
    AUTH4(4, "账号一致，身份不一致,个人身份存在"),
    AUTH5(5, "账号一致，身份不一致,个人身份不存在"),
    AUTH6(6, "账号一致，身份不一致,目标企业租户存在，但该账号不属于该企业"),

    AUTH7(7, "账号一致，身份不一致,目标企业租户存在，且该用户属于该企业"),
    AUTH8(8, "账号一致，身份不一致,目标企业租户不存在"),
    ;

    private Integer code;

    private String name;

    SignAuthCheckEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }


    public Integer getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }


}