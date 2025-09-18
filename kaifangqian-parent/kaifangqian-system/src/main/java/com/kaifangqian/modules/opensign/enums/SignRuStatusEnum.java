/**
 * @description 签署状态类型
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

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: SignRuStatusEnum
 * @Package: com.kaifangqian.modules.opensign.enums
 * @ClassName: SignRuStatusEnum
 * @author: FengLai_Gong
 */
public enum SignRuStatusEnum {


    DRAFT(1,"草稿","DRAFT"),
    APPROVING(2,"发起审批中","APPROVING"),
    APPROVE_FAILED(3,"发起审批不通过","APPROVE_FAILED"),
    DELETE(4,"已删除","DELETED"),
    WRITING(5,"填写中","WRITING"),
    REJECT_WRITE(6,"已拒填","REFUSAL_WRIT"),
    SIGNING(7,"签署中","SIGNING"),
    REJECT_SIGN(8,"已拒签","REFUSAL_SIGN"),
    EXPIRE(9,"已失效","EXPIRED"),
    REVOKE(10,"已撤回","RECALLED"),
    DONE(11,"已完成","COMPLETE"),

    START(12,"已发起","START"),

    CLEAN_UP(-1,"已清除","CLEAN_UP"),

    ;
    private Integer code  ;

    private String name ;

    private String type ;

    SignRuStatusEnum(Integer code, String name){
        this.code = code ;
        this.name = name;
    }

    SignRuStatusEnum(Integer code, String name,String type){
        this.code = code ;
        this.name = name;
        this.type = type ;
    }


    public Integer getCode(){
        return this.code;
    }

    public String getName(){
        return this.name ;
    }

    public String getType(){
        return this.type ;
    }

    public static SignRuStatusEnum getByCode(Integer code){
        SignRuStatusEnum[] values = SignRuStatusEnum.values();
        for(SignRuStatusEnum signRuStatusEnum : values){
            if(signRuStatusEnum.getCode().equals(code)){
                return signRuStatusEnum ;
            }
        }
        return null ;
    }

    public static List<Integer> getDoneList(){
        List<Integer> list = new ArrayList<>();
        list.add(SignRuStatusEnum.DELETE.getCode());
        list.add(SignRuStatusEnum.REJECT_WRITE.getCode());
        list.add(SignRuStatusEnum.REJECT_SIGN.getCode());
        list.add(SignRuStatusEnum.EXPIRE.getCode());
        list.add(SignRuStatusEnum.REVOKE.getCode());
        list.add(SignRuStatusEnum.DONE.getCode());
        return list ;
    }

    public static SignRuStatusEnum getCode(Integer code){
        for(SignRuStatusEnum ruStatusEnum : SignRuStatusEnum.values()){
            if(ruStatusEnum.getCode() == code){
                return ruStatusEnum ;
            }
        }
        return null ;
    }


}