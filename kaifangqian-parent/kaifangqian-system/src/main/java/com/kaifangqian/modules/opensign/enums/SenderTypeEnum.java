/**
 * @description 发起方签署角色类型枚举
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
 * @Description: SenderTypeEnum
 * @Package: com.kaifangqian.modules.opensign.enums
 * @ClassName: SenderTypeEnum
 * @author: FengLai_Gong
 */
public enum SenderTypeEnum {


    OPERATOR(1,"经办人签字","AGENT_SIGN"),
    LEGAL_PERSON(2,"法人签字","LEGAL_PERSON_SIGN"),
    PERSONAL(3,"个人签字","PERSONAL_SIGN"),
    ENTERPRISE(4,"组织签章","ENTERPRISE_SEAL"),

    ;
    private Integer code  ;

    private String name ;

    private String apiName ;
    public String getApiName(){
        return this.apiName;
    }

    SenderTypeEnum(Integer code, String name){
        this.code = code ;
        this.name = name;
    }

    SenderTypeEnum(Integer code, String name,String apiName){
        this.code = code ;
        this.name = name;
        this.apiName = apiName;
    }


    public Integer getCode(){
        return this.code;
    }

    public String getName(){
        return this.name ;
    }


    public static SenderTypeEnum getByCode(Integer code){
        SenderTypeEnum[] values = SenderTypeEnum.values();
        for(SenderTypeEnum senderTypeEnum : values){
            if(code == senderTypeEnum.getCode()){
                return senderTypeEnum ;
            }
        }
        return null ;
    }


}