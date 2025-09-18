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
package com.kaifangqian.modules.opensign.vo.request;

// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: PersonSealGenerateRequest
 * @Package: com.kaifangqian.modules.opensign.vo.request
 * @ClassName: PersonSealGenerateRequest
 * @author: FengLai_Gong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// @ApiModel("个人签名生成请求对象")
public class PersonSealGenerateParamRequest implements Serializable {


    // @ApiModelProperty("颜色,1红色，2蓝色，3黑色")
    private Integer color ;
    // @ApiModelProperty("签章文字添加内容，1为无添加，2为添加印字，3为添加之印")
    private Integer addContext ;
    // @ApiModelProperty("签章形状，1为长方形无框，2长方形有框，3为正方形无框，4为正方形有框")
    private Integer shapeStyle ;
}