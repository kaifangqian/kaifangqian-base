/**
 * @description 签署通知标识
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
package com.kaifangqian.modules.opensign.dto;

import lombok.Data;

/**
 * @author : zhenghuihan
 * create at:  2024/6/28  10:25
 * @description:
 */
@Data
public class SignReNoticeVO {
    //业务线唯一标识
    private String signReId;
    //文件填写标识短信
    private Boolean writeTaskFlagPhone;
    //文件签署（发起方内部）标识短信
    private Boolean signTaskInFlagPhone;
    //文件签署（外部接收方）标识短信
    private Boolean signTaskOutFlagPhone;
    //文件抄送（发起方内部）标识短信
    private Boolean copyBeginFlagPhone;
    //文件抄送（外部）标识短信
    private Boolean copySignFlagPhone;

    //文件填写标识邮件
    private Boolean writeTaskFlagEmail;
    //文件签署（发起方内部）标识邮件
    private Boolean signTaskInFlagEmail;
    //文件签署（外部接收方）标识邮件
    private Boolean signTaskOutFlagEmail;
    //文件抄送（发起方内部）标识邮件
    private Boolean copyBeginFlagEmail;
    //文件抄送（外部）标识邮件
    private Boolean copySignFlagEmail;
    //文件审批标识短信
    private Boolean approvalTaskFlagPhone;
    //文件审批标识邮件
    private Boolean  approvalTaskFlagEmail;


}