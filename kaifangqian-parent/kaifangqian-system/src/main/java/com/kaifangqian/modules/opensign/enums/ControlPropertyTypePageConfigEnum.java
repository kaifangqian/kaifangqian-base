/**
 * @description 控件属性类型-应用页面-枚举
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
 * @Description: 控件属性类型-应用页面-枚举
 * @Package: com.kaifangqian.modules.opensign.enums
 * @ClassName: ControlPropertyTypePageConfigEnum
 * @author: FengLai_Gong
 */
public enum ControlPropertyTypePageConfigEnum {


    ODD_NUMBER("odd_number","奇数页"),
    EVEN_NUMBER("even_number","偶数页"),
    ALL("all","全部页"),
    CUSTOM("custom","指定页"),

    ;

    private String  code  ;

    private String name ;

    ControlPropertyTypePageConfigEnum(String code, String name){
        this.code = code ;
        this.name = name;
    }


    public String getCode(){
        return this.code;
    }

    public String getName(){
        return this.name ;
    }

    public static ControlPropertyTypePageConfigEnum getValue(String code){
        ControlPropertyTypePageConfigEnum[] values = ControlPropertyTypePageConfigEnum.values();
        for(ControlPropertyTypePageConfigEnum pageConfigEnum : values){
            if(pageConfigEnum.getCode().equals(code)){
                return pageConfigEnum ;
            }
        }
        return null ;
    }

}