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
 * @Description: EntSealListResponse
 * @Package: com.kaifangqian.modules.opensign.vo.response
 * @ClassName: EntSealListResponse
 * @author: FengLai_Gong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// @ApiModel("企业印章列表返回对象")
public class EntSealListResponse implements Serializable {

    private static final long serialVersionUID = -7474517752461766579L;

    // @ApiModelProperty("印章id")
    private String sealId ;
    // @ApiModelProperty("印章名称")
    private String sealName ;
    // @ApiModelProperty("附件id")
    private String annexId ;

    // @ApiModelProperty("印章状态（制作中、制作失败、已停用、已启用、已收缴、已销毁）")
    private Integer sealStatus ;

    // @ApiModelProperty("1有效，2失效")
    private Integer status ;
}