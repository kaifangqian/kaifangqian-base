/**
 * @description 用印统计返回对象
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
import lombok.Data;

/**
 * @Description: SealStatisticsVo
 * @Package: com.kaifangqian.modules.opensign.service.cert
 * @ClassName: SealStatisticsVo
 * @author: Fusion
 * CreateTime:  2023/8/18  13:53
 * @copyright 北京资源律动科技有限公司
 */

// @ApiModel("用印统计返回对象")
@Data
public class UseSealStatisticsVo {
    // @ApiModelProperty(value = "总用印次数")
    private Integer totalUseSealsCount = 0;
    // @ApiModelProperty(value = "电子章用印次数")
    private Integer electronicUseSealCount = 0;
    // @ApiModelProperty(value = "物理章用印次数")
    private Integer physicsUseSealCount = 0;
    // @ApiModelProperty(value = "接口用印次数")
    private Integer interfaceUseSealCount = 0;
}
