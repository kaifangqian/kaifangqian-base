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

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author : zhenghuihan
 * create at:  2022/12/26  16:45
 * @description: 开通租户VO
 */
@Data
public class SysTenantInfoExtendVO {

    private String id;
    private String tenantId;
    private String departId;
    /**
     * 租户类型（个人、企业）
     */
    private Integer tenantType;
    /**
     * 姓名或企业名称
     */
    private String name;
    /**
     * 认证状态：未认证、认证审核中、认证审核失败、认证成功
     */
    private Integer authStatus;

    private Integer tenantStatus;

    private Integer myJobCount;
}