/**
 * @description 企业印章申请状态枚举
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
 * @Description: EntSealApplyStatusEnums
 * @Package: com.kaifangqian.modules.opensign.enums
 * @ClassName: EntSealApplyStatusEnums
 * @author: FengLai_Gong
 */
public enum EntSealApplyStatusEnum {



    INIT(0,"初始数据"),

    TO_BE_SUMMIT(1,"待提交"),

    TO_BE_RE_SUMMIT(2,"待重新提交"),

    TO_BE_APPROVAL(3,"待审批"),

    APPROVAL_FAILED(4,"审批未通过"),

    APPROVAL_SUCCESS(5,"审批通过"),

    CANCELED(6,"作废"),

    ;


    EntSealApplyStatusEnum(Integer code ,String name){
        this.code = code;
        this.name = name;
    }

    private Integer code ;
    private String name ;

    public String getName(){
        return this.name;
    }

    public Integer getCode(){
        return this.code;
    }

}