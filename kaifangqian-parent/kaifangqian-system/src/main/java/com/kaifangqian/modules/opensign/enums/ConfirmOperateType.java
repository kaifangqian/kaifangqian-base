/**
 * @description 签署业务功能类型枚举
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
 * @Description: 需要意愿校验的操作类型
 * @Package: com.kaifangqian.modules.opensign.enums
 * @ClassName: ConfirmOperateType
 * @author: FengLai_Gong
 */
public enum ConfirmOperateType {

    SUBMIT_WRITE("submit_write","填写"),

    REJECT_WRITE("reject_write","拒填"),

    SUBMIT_SIGN("submit_sign","签署"),

    REJECT_SIGN("reject_sign","拒签"),

    REVOKE("revoke","撤销"),

    ;



    private String type;

    private String name;

    ConfirmOperateType(String type, String name) {
        this.type = type;
        this.name = name;
    }


    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public static ConfirmOperateType getByType(String type) {
        for (ConfirmOperateType confirmOperateType : ConfirmOperateType.values()) {
            if (confirmOperateType.getType().equals(type)) {
                return confirmOperateType;
            }
        }
        return null;
    }
}