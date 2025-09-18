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
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kaifangqian.common.base.entity.BaseEntity;
import lombok.Data;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@TableName("sys_app_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
// @ApiModel(value = "sys_app_info对象", description = "应用管理表")
public class SysAppInfo extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    // @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 名称
     */
    // @ApiModelProperty(value = "名称")
    private String appName;
    /**
     * code
     */
    // @ApiModelProperty(value = "code")
    private String appCode;
    /**
     * 图标
     */
    // @ApiModelProperty(value = "图标")
    private String appIcon;
    /**
     * 图标地址
     */
    // @ApiModelProperty(value = "图标地址")
    private String appIconAddress;
    /**
     * 类型：1团体 2个人 0不限制
     */
    // @ApiModelProperty(value = "类型")
    private Integer appType;
    /**
     * 状态
     */
    // @ApiModelProperty(value = "状态:0：草稿 1已启用 2 未启用")
    private Integer appStatus;
    /**
     * 描述
     */
    // @ApiModelProperty(value = "描述")
    private String appDesc;
    /**
     * 地址
     */
    // @ApiModelProperty(value = "地址")
    private String appAddress;
    /**
     * 首页
     */
    // @ApiModelProperty(value = "首页")
    private String appFront;
    /**
     * 是否内置应用1是 0 否
     */
    // @ApiModelProperty(value = "是否默认应用1是 0 否")
    private Boolean defaultFlag;
    /**
     * 是否给新用户初始化 1是 0 否
     */
    // @ApiModelProperty(value = "是否全员可见")
    private Boolean useful;

    private transient List<SysAppVersion> versionList;

    private transient boolean selectFlag;
}
