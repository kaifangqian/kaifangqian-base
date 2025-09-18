/**
 * @description 业务权限对象
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
package com.kaifangqian.modules.opensign.vo.base;

// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: BusinessAuthVo
 * @Package: com.kaifangqian.modules.opensign.vo.base
 * @ClassName: BusinessAuthVo
 * @author: FengLai_Gong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// @ApiModel("业务权限对象")
public class BusinessAuthVo implements Serializable {

    private static final long serialVersionUID = 8555184952265272469L;

    // @ApiModelProperty("授权类型，1用户(租户下用户)，2部门，3角色")
    private Integer authType ;

    // @ApiModelProperty("权限关联id，租户下用户id或者部门ID或者角色ID")
    private String authRelationId ;

    // @ApiModelProperty("权限关联id的名称，租户下用户名称或者部门名称或者角色名称")
    private String authRelationName ;


}