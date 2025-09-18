/**
 * @description 签署控制枚举
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

/**
 * @Description: ControlChangeFlagEnum
 * @Package: com.kaifangqian.modules.opensign.enums
 * @ClassName: ControlChangeFlagEnum
 * @author: FengLai_Gong
 */
public enum ControlChangeFlagEnum {



    NECESSARY_AND_ADD("necessary_and_add","必须签署，可增加新的签署位置,默认值"),
    NECESSARY_NO_ADD("necessary_no_add","必须签署，不可增加新的签署位置"),
    NOT_NECESSARY("not_necessary","非必须签署"),

    ;

    private String type;

    private String name;

    ControlChangeFlagEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }


    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public static ControlChangeFlagEnum getByType(String type) {
        for (ControlChangeFlagEnum flag : ControlChangeFlagEnum.values()) {
            if (flag.getType().equals(type)) {
                return flag;
            }
        }
        return null;
    }
}