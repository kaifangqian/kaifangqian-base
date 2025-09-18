/**
 * @description 企业管理功能枚举
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
 * @Description: EntSealOperateTypeEnum
 * @Package: com.kaifangqian.modules.opensign.enums
 * @ClassName: EntSealOperateTypeEnum
 * @author: FengLai_Gong
 */
public enum EntSealOperateTypeEnum {


    MAKE(1,"制作"),
    EDIT(2,"编辑"),
    CHANGE(3,"章面变更"),
    ENABLED(4,"启用"),
    UN_ENABLED(5,"停用"),
    DIVESTED(6,"收缴"),
    DESTRUCTION(7,"销毁"),

    LIST(8,"查看列表"),
    INFO(9,"查看详情"),


    ;
    EntSealOperateTypeEnum(Integer code ,String name){
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