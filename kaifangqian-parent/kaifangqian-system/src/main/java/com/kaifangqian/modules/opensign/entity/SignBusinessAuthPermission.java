/**
 * @description 业务数据权限组表
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
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: SignBusinessAuthPermission
 * @Package: com.kaifangqian.modules.opensign.entity
 * @ClassName: SignBusinessAuthPermission
 * @author: FengLai_Gong
 */
@Data
@TableName("sign_business_auth_permission")
// @ApiModel("业务数据权限组表")
public class SignBusinessAuthPermission implements Serializable {

    private static final long serialVersionUID = -7457083301818487886L;

    @TableId(type = IdType.ASSIGN_ID)
    // @ApiModelProperty("主键")
    private String id;

    // @ApiModelProperty("业务类型，1为签章，2为模板，3为文档，4为业务线")
    private Integer businessType ;

    // @ApiModelProperty("业务类型角色，具体数值参照枚举类")
    private Integer businessTypeRole ;

    // @ApiModelProperty("业务权限值")
    private Integer permissionCode ;

    // @ApiModelProperty("业务权限值")
    private String permissionValue ;

}