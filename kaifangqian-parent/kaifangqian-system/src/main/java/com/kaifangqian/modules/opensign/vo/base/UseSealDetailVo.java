/**
 * @description 用印明细返回对象
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
 * @Description: UseSealDetailVo
 * @Package: com.kaifangqian.modules.opensign.service.cert
 * @ClassName: UseSealDetailVo
 * @author: Fusion
 * CreateTime:  2023/8/18  14:50
 * @copyright 北京资源律动科技有限公司
 */
// @ApiModel("用印明细返回对象")
@Data
public class UseSealDetailVo {

    // @ApiModelProperty(value = "用印部门")
    private String useSealDept;     //用印部门

    // @ApiModelProperty(value = "业务类型")
    private String businessType;    //业务类型

    // @ApiModelProperty(value = "用印方式")
    private String useSealWay;      //用印方式

    // @ApiModelProperty(value = "加盖印章类型")
    private String sceneType;       //加盖印章类型

    // @ApiModelProperty(value = "印章类型")
    private String sealType;        //印章类型

    // @ApiModelProperty(value = "用印次数")
    private Integer useSealCount;     //用印次数
}
