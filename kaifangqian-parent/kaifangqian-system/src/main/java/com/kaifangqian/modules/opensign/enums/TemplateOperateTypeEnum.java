/**
 * @description 模板操作类型枚举
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
public enum TemplateOperateTypeEnum {


    MAKE(1,"模版制作保存"),
    CHANGE(2,"模版变更保存"),
    EDIT(3,"模版编辑"),
    ENABLED(4,"模板启用"),
    UN_ENABLED(5,"模板停用"),

    REMOVE(6,"模板移动"),
    PARAM_DOWNLOAD(7,"模板参数下载"),


    ;

    public static String getName(Integer code) {
        TemplateOperateTypeEnum[] values = TemplateOperateTypeEnum.values();
        for(TemplateOperateTypeEnum templateOperateTypeEnum : values){
            if(templateOperateTypeEnum.getCode().equals(code)){
                return templateOperateTypeEnum.getName();
            }
        }
        return null ;
    }

    TemplateOperateTypeEnum(Integer code , String name){
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