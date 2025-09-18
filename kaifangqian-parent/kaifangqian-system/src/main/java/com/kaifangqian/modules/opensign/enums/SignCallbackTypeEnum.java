/**
 * @description 回调事件类型
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
 * @Description: 回调事件类型
 * @Package: com.kaifangqian.modules.opensign.enums
 * @ClassName: SignCallbackTypeEnum
 * @author: FengLai_Gong
 */
public enum SignCallbackTypeEnum {


    SEND_SIGNING("SEND_SIGNING","发起签署"),
    SUBMIT_WRITE("SUBMIT_WRITE","填写"),
    REFUSAL_WRITE("REFUSAL_WRITE","拒填"),
    SUBMIT_SIGN("SUBMIT_SIGN","签署"),
    REFUSAL_SIGN("REFUSAL_SIGN","拒签"),
    RECALLED("RECALLED","撤回合同"),
    DELETE("DELETE","删除合同"),
    COMPLETE("COMPLETE","完成签署"),
    EXPIRED("EXPIRED","合同过期"),
    SIGN_FAILED("SIGN_FAILED","签署失败"),
    DOCUMENT_DELAY("DOCUMENT_DELAY","修改签署截止时间"),



    ;




    private String type;

    private String name;

    SignCallbackTypeEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }


    public String getName() {
        return name;
    }


    public static List<String> getSignList(){
        List<String> list = new ArrayList<>();
        list.add(SignCallbackTypeEnum.SUBMIT_SIGN.getType());
        list.add(SignCallbackTypeEnum.REFUSAL_SIGN.getType());
        list.add(SignCallbackTypeEnum.SIGN_FAILED.getType());
        return list ;
    }

    public static List<String> getWriteList(){
        List<String> list = new ArrayList<>();
        list.add(SignCallbackTypeEnum.SUBMIT_WRITE.getType());
        list.add(SignCallbackTypeEnum.REFUSAL_WRITE.getType());
        return list ;
    }

    public static List<String> getNormalList(){
        List<String> list = new ArrayList<>();
        list.add(SignCallbackTypeEnum.SEND_SIGNING.getType());
        list.add(SignCallbackTypeEnum.RECALLED.getType());
        list.add(SignCallbackTypeEnum.DELETE.getType());
        list.add(SignCallbackTypeEnum.COMPLETE.getType());
        list.add(SignCallbackTypeEnum.EXPIRED.getType());
        list.add(SignCallbackTypeEnum.DOCUMENT_DELAY.getType());
        return list ;
    }



}