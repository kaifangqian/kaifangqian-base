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
package com.kaifangqian.modules.opensign.vo.response;

// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: EntSealAuthorizedResponse
 * @Package: com.kaifangqian.modules.opensign.vo.response
 * @ClassName: EntSealAuthorizedResponse
 * @author: FengLai_Gong
 */
// @ApiModel("企业签章授权人数据")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntSealAuthorizedResponse implements Serializable {

    private static final long serialVersionUID = 1432669025971390480L;

    // @ApiModelProperty(value = "租户下用户ID")
    private String tenantUserId ;

    // @ApiModelProperty(value = "租户下用户ID")
    private String id ;

    // @ApiModelProperty(value = "租户ID")
    private String tenantId;

    // @ApiModelProperty(value = "用户id")
    private String userId;

    // @ApiModelProperty(value = "用户别称")
    private String nickName;




}