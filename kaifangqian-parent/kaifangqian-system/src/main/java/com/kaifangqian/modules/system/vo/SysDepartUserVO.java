/**
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
package com.kaifangqian.modules.system.vo;

import lombok.Data;

/**
 * @author : zhenghuihan
 * create at:  2022/6/27  19:07
 * @description:部门用户信息
 */
@Data
public class SysDepartUserVO {
    private String id;

    private String tenantUserId;

    private String username;

    private String realname;

    private String nickName;

    private String phone;

    private String email;

    private String orgCode;

    /**
     * 状态(0:未激活 1：正常  2：冻结 ）
     */
    private Integer status;

    /**
     * 是否主管
     */
    private Boolean manageFlag;

    private String roleNames;

    private String roleName;

    private Integer count;

    private String type = "user";

    private String addType;

    /**
     * 头像缩略图
     */
    private String avatarBase64;
}