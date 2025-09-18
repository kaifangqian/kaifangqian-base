/**
 * @description 签署操作记录-操作类型
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
 * @Description: 签署操作记录-操作类型
 * @Package: com.kaifangqian.modules.opensign.enums
 * @ClassName: SignRecordOperateTypeEnum
 * @author: FengLai_Gong
 */
public enum SignRecordOperateTypeEnum {

    START("start","发起合同"),
    WRITE("write","填写"),
    PRIVATE_SIGN("private_sign","个人签名"),
    ENT_SIGN("ent_sign","组织签章"),
    REVOKE("revoke","撤销合同"),
    ;

    private String type;

    private String name;

    SignRecordOperateTypeEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }


    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public static SignRecordOperateTypeEnum getByType(String type){
        for(SignRecordOperateTypeEnum typeEnum : SignRecordOperateTypeEnum.values()){
            if(type != null && type.equals(typeEnum.getType())){
                return typeEnum ;
            }
        }
        return null ;
    }

}