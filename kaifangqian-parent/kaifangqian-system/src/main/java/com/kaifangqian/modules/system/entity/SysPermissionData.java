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
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhenghuihan
 * @description 权限策略表
 * @createTime 2022/9/2 18:08
 */
@Data
@TableName("sys_permission_data")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
// @ApiModel(value = "sys_permission_data对象", description = "权限策略表")
public class SysPermissionData implements Serializable {
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
    // @ApiModelProperty(value = "租户id")
    private String tenantId;
    /**
     * 权限菜单id
     */
    // @ApiModelProperty(value = "权限菜单id")
    private String permissionId;
    /**
     * 权限类型
     */
    // @ApiModelProperty(value = "权限类型")
    private String dataType;
    /**
     * 名称
     */
    // @ApiModelProperty(value = "名称")
    private String dataName;
    /**
     * 描述
     */
    // @ApiModelProperty(value = "描述")
    private String dataDesc;
    /**
     * 默认标识1默认 2非默认
     */
    // @ApiModelProperty(value = "默认标识1默认 2非默认")
    private Integer defaultFlag;
    /**
     * 排序
     */
    // @ApiModelProperty(value = "排序")
    private Integer orderNo;
    /**
     * 创建人
     */
    // @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // @ApiModelProperty(value = "创建日期")
    private Date createTime;
}
