/**
 * @description 业务数据授权表
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
package com.kaifangqian.modules.opensign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kaifangqian.common.base.entity.BaseEntity;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 业务数据授权表
 * @Package: com.kaifangqian.modules.opensign.entity
 * @ClassName: SignBusinessAuth
 * @author: FengLai_Gong
 */
@Data
@TableName("sign_business_auth")
// @ApiModel("业务数据授权表")
public class SignBusinessAuth extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 8306502747078624108L;

    @TableId(type = IdType.ASSIGN_ID)
    // @ApiModelProperty("主键")
    private String id;

    // @ApiModelProperty("业务类型，1为签章，2为模板，3为文档，4为业务线")
    private Integer businessType ;

    // @ApiModelProperty("业务类型角色，具体数值参照枚举类")
    private Integer businessTypeRole ;

    // @ApiModelProperty("业务关联数据id")
    private String businessRelationId ;

    // @ApiModelProperty("授权类型，1用户(租户下用户)，2部门，3角色")
    private Integer authType ;

    // @ApiModelProperty("权限关联id，租户下用户id或者部门ID或者角色ID")
    private String authRelationId ;

    // @ApiModelProperty("印章所属系统租户id")
    private String sysTenantId ;

    // @ApiModelProperty("印章所属系统账号id")
    private String sysAccountId ;

    // @ApiModelProperty("印章所属系统租户下用户id")
    private String sysUserId ;

    // @ApiModelProperty("印章所属系统部门编码")
    private String sysOrgCode ;

    // @ApiModelProperty("印章所属系统部门id")
    private String sysDeptId ;



}