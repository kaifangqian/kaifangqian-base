/**
 * @description 意愿验证枚举
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

public enum ConfirmTypeEnum {

    PAASWORD("password", "签署密码"),

    PHONE_EMAIL("phone_email", "验证码"),

    FACE("face", "人脸识别认证"),

    DOUBLE("double", "双重校验（验证码+签署密码）"),

    NONE("none", "不需要"),
    ;

    private String type;

    private String name;

    ConfirmTypeEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }


    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public static ConfirmTypeEnum getByType(String type) {
        for (ConfirmTypeEnum confirmType : ConfirmTypeEnum.values()) {
            if (confirmType.getType().equals(type)) {
                return confirmType;
            }
        }
        return null;
    }

}