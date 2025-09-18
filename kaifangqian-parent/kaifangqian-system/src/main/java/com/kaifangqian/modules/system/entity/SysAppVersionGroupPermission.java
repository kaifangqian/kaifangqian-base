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
package com.kaifangqian.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author zhenghuihan
 * @description 权限组-权限表
 * @createTime 2022/9/2 18:08
 */
@Data
@TableName("sys_app_version_group_permission")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
// @ApiModel(value = "sys_app_version_group_permission对象", description = "权限组-权限表")
public class SysAppVersionGroupPermission implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    // @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 权限组ID
     */
    // @ApiModelProperty(value = "权限组ID")
    private String groupId;
    /**
     * 菜单id
     */
    // @ApiModelProperty(value = "菜单id")
    private String permissionId;
    /**
     * 菜单权限
     */
    // @ApiModelProperty(value = "菜单权限")
    private String permissionPerms;
    /**
     * 权限策略id
     */
    // @ApiModelProperty(value = "权限策略id")
    private String permissionDataId;
}
