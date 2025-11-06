/**
 * @description 签署操作记录-动作类型枚举
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
 * @Description: 签署操作记录-动作类型
 * @Package: com.kaifangqian.modules.opensign.enums
 * @ClassName: SignRecordActionTypeEnum
 * @author: FengLai_Gong
 */
public enum SignRecordActionTypeEnum {


    START("start","发起合同"),
    SUBMIT_WRITE("submit_write","提交填写"),
    REJECT_WRITE("reject_write","拒绝填写"),
    SUBMIT_SIGN("submit_sign","签署"),
    REJECT_SIGN("reject_sign","拒绝签署"),
    AUTO_SIGN("auto_sign","自动盖章"),
    AUTH_SIGN("auth_sign","授权签署"),
    REVOKE("revoke","撤销合同"),
    APPROVE_CHECK("approve_check","审批通过"),
    REJECT_CHECK("reject_check","审批未通过"),

    ;

    private String type;

    private String name;

    SignRecordActionTypeEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }


    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public static SignRecordActionTypeEnum getByType(String type){
        for(SignRecordActionTypeEnum typeEnum : SignRecordActionTypeEnum.values()){
            if(type != null && type.equals(typeEnum.getType())){
                return typeEnum ;
            }
        }
        return null ;
    }
}