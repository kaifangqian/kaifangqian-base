/**
 * @description 签署通知类型枚举
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

public enum ReNoticeTypeEnum {

    WRITE_TASK_PHONE("write_task_phone", "文件填写短信通知"),
    SIGN_TASK_IN_PHONE("sign_task_in_phone", "文件签署（发起方内部）短信通知"),
    SIGN_TASK_OUT_PHONE("sign_task_out_phone", "文件签署（外部接收方）短信通知"),
    COPY_BEGIN_PHONE("copy_begin_phone", "文件抄送（发起方内部）短信通知"),
    COPY_SIGN_PHONE("copy_sign_phone", "文件抄送（外部）短信通知"),
    WRITE_TASK_EMAIL("write_task_email", "文件填写邮件通知"),
    SIGN_TASK_IN_EMAIL("sign_task_in_email", "文件签署（发起方内部）邮件通知"),
    SIGN_TASK_OUT_EMAIL("sign_task_out_email", "文件签署（外部接收方）邮件通知"),
    COPY_BEGIN_EMAIL("copy_begin_email", "文件抄送（发起方内部）邮件通知"),
    COPY_SIGN_EMAIL("copy_sign_email", "文件抄送（外部）邮件通知"),

    ;

    private String type;

    private String name;

    ReNoticeTypeEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }


    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }


}