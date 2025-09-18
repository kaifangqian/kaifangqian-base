/**
 * @description 控件签署应用范围枚举
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
 * @Description: KeywordSearchTypeEnum
 * @Package: com.kaifangqian.modules.opensign.enums
 * @ClassName: KeywordSearchTypeEnum
 * @author: FengLai_Gong
 */
public enum KeywordPropertyEnum {

    RELATION_DOC("relation_doc","控件属性-应用文档") ,
    PAGE_CONFIG("page_config","控件属性-应用页面") ,

    ALl("all","全部"),
    PART("part","部分"),

    ASC("asc","正序"),
    DESC("desc","倒序"),

    CUSTOM("custom","指定位"),

    ;


    private String  code  ;

    private String name ;

    KeywordPropertyEnum(String code, String name){
        this.code = code ;
        this.name = name;
    }


    public String getCode(){
        return this.code;
    }

    public String getName(){
        return this.name ;
    }

    public static KeywordPropertyEnum getValue(String code){
        KeywordPropertyEnum[] values = KeywordPropertyEnum.values();
        for(KeywordPropertyEnum keywordSearchTypeEnum : values){
            if(keywordSearchTypeEnum.getCode().equals(code)){
                return keywordSearchTypeEnum ;
            }
        }
        return null ;
    }
}