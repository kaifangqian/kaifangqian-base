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
import com.fasterxml.jackson.annotation.JsonFormat;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author zhenghuihan
 * @description 编码校验规则
 * @createTime 2022/9/2 18:08
 */
@Data
@TableName("sys_check_rule")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
// @ApiModel(value = "sys_check_rule对象", description = "编码校验规则")
public class SysCheckRule {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    // @ApiModelProperty(value = "主键id")
    private String id;
    /**
     * 规则名称
     */
    // @ApiModelProperty(value = "规则名称")
    private String ruleName;
    /**
     * 规则Code
     */
    // @ApiModelProperty(value = "规则Code")
    private String ruleCode;
    /**
     * 规则JSON
     */
    // @ApiModelProperty(value = "规则JSON")
    private String ruleJson;
    /**
     * 规则描述
     */
    // @ApiModelProperty(value = "规则描述")
    private String ruleDescription;
    /**
     * 更新人
     */
    // @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    /**
     * 创建人
     */
    // @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
