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
import com.kaifangqian.common.base.entity.BaseEntity;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 菜单权限表
 * </p>
 *
 * @Author zhh
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysPermission extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 类型（0：目录；1：菜单 ；2：按钮权限）
     */
    private Integer menuType;

    /**
     * 父id
     */
    private String parentId;

    /**
     * 菜单排序
     */
    private Integer sortNo;
    /**
     * 路径
     */
    private String path;
    /**
     * 组件
     */
    private String component;
    /**
     * 菜单权限编码，例如：“sys:schedule:list,sys:schedule:info”,多个逗号隔开
     */
    private String perms;

    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 是否路由菜单: 0:不是  1:是（默认值1）
     */
    private boolean routeFlag;
    /**
     * 是否隐藏路由菜单: 0否,1是（默认值0）
     */
    private boolean hiddenFlag;
    /**
     * 是否缓存页面: 0:不是  1:是（默认值1）
     */
    private boolean keepAliveFlag;
    /**
     * 外链菜单打开方式 0/内部打开 1/外部打开
     */
    private boolean internalOrExternal;
    /**
     * 描述
     */
    private String description;
    /**
     * 按钮权限状态(0无效1有效)
     */
    private Integer status;
    /**
     * 是否配置菜单的数据权限 1是0否 默认0
     */
    private Integer ruleFlag;
    /**
     * 权限表id
     */
    private String formTableId;

    /**
     * 配置权限时是否可见1可见0不可见
     */
    private Boolean authVisible;
    /**
     * 快捷图标缩略图
     */
    private String fastIcon;
    /**
     * 快捷图标地址
     */
    private String fastIconAddress;
    /**
     * 快捷开关
     */
    private Boolean fastFlag;
}
