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

// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author : zhenghuihan
 * create at:  2022/12/26  16:45
 * @description: 开通租户VO
 */
@Data
public class SysTenantInfoVO {
    /**
     * 主键
     */
    // @ApiModelProperty(value = "租户主键")
    private String id;
    /**
     * 租户类型 1团体 2个人
     */
    // @ApiModelProperty(value = "租户类型")
    private Integer tenantType;
     /**
     * 租户名称
     */
    // @ApiModelProperty(value = "租户名称")
    private String tenantName;
    /**
     * 租户状态
     */
    // @ApiModelProperty(value = "租户状态1:启用 2停用")
    private Integer tenantStatus;
    /**
     * 租户描述
     */
    // @ApiModelProperty(value = "租户描述")
    private String tenantDesc;

    /**
     * 姓名
     */
    private String realName;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;

    /**
     * 初始化密码
     */
    private String password;

    /**
     * 授权内置应用列表
     */
    private List<String> inVersionIds;

    /**
     * 授权业务应用列表
     */
    private List<String> outVersionIds;
}