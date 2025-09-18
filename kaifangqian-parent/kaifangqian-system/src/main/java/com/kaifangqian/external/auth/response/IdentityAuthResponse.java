/**
 * @description 电子签服务开通，个人实名开通业务逻辑处理
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
package com.kaifangqian.external.auth.response;

import lombok.Data;
/**
 * @author : yxb
 * create at: 2025/6/6
 */
@Data
public class IdentityAuthResponse {

    private String orderNo;

    private String authUrl;

    /**
     * 认证页面逻辑状态,1：已实名认证，本地刷新页面；0:未实名，打开云盾认证页面;-1:云盾订单返回的实名主体与当前登录人不一致，或用户类型不一致
     */
    private Integer authStatus;

    private String resultMessage;

}
