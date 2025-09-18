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
import com.kaifangqian.common.base.entity.BaseEntity;
import com.kaifangqian.modules.system.vo.PermissionRuleVO;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhenghuihan
 * @description 应用权限组表
 * @createTime 2022/9/2 18:07
 */
@Data
@TableName("sys_app_version_group")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
// @ApiModel(value = "sys_app_version_group对象", description = "应用权限组表")
public class SysAppVersionGroup extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    // @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 应用ID
     */
    // @ApiModelProperty(value = "应用ID")
    private String appId;
    /**
     * 应用版本ID
     */
    // @ApiModelProperty(value = "应用版本ID")
    private String appVersionId;
    /**
     * 应用版本名称
     */
    private transient String appVersionName;
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
     * 可用标识
     */
    // @ApiModelProperty(value = "可用标识")
    private Boolean useFlag;
    /**
     * 是否需要初始化给基础版本
     */
    // @ApiModelProperty(value = "是否需要初始化给基础版本")
    private Boolean initFlag;

    private transient List<PermissionRuleVO> ruleVOS;
}
