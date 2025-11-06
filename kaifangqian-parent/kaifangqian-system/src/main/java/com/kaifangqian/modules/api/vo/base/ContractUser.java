/**
 * @description API接口合同操作人
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
package com.kaifangqian.modules.api.vo.base;

// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: ContractUser
 * @Package: com.kaifangqian.modules.api.vo.base
 * @ClassName: ContractUser
 * @author: FengLai_Gong
 * @Date: 2024/03/19
 */
@Data
// @ApiModel("操作人")
public class ContractUser implements Serializable {

    private static final long serialVersionUID = 862003493609163515L;

//    @NotNull(message = "NotNull",groups = ValidationSorts.SortA1.class)
//    @NotBlank(message = "NotBlank",groups = ValidationSorts.SortA1.class)
    // @ApiModelProperty("姓名")
    private String name ;

    // @ApiModelProperty("联系类型")
    private String contactType ;

    // @ApiModelProperty("联系方式")
    private String contact ;

}