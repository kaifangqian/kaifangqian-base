/**
 * @description 登录用户权限信息
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
package com.kaifangqian.common.system.vo;

import lombok.Data;

import java.io.Serializable;
/**
 * @author : zhenghuihan
 * create at:  2022/12/20  18:07
 */
@Data
public class LoginUserAuthInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 当前登录租户Id
     */
    private String tenantId;

    /**
     * 当前登录部门code
     */
    private String orgCode;

    /**
     * 当前登录部门id
     */
    private String departId;

    /**
     * 授权token
     */
    private String authorizedToken;

}