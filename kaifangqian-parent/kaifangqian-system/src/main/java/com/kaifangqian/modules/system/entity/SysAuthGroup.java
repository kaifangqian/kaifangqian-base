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

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kaifangqian.common.base.entity.BaseEntity;
import lombok.Data;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhenghuihan
 * @description 权限组表
 * @createTime 2022/9/2 18:07
 */
@Data
@TableName("sys_auth_group")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
// @ApiModel(value = "sys_auth_group对象", description = "权限组表")
public class SysAuthGroup extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    // @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 父id
     */
    // @ApiModelProperty(value = "父id")
    private String parentId;
    /**
     * 名称
     */
    // @ApiModelProperty(value = "名称")
    private String groupName;
    /**
     * 描述
     */
    // @ApiModelProperty(value = "描述")
    private String groupDesc;
    /**
     * 系统内置权限组标识 1:系统 0用户
     */
    // @ApiModelProperty(value = "系统内置权限组标识")
    private Boolean systemFlag;
    /**
     * 类型：0未知   1总权限 2基础权限 3：初始化权限 4、自定义权限
     */
    // @ApiModelProperty(value = "类型：0未知   1总权限 2基础权限 3：初始化权限 4、自定义权限")
    private Integer groupType;
}
