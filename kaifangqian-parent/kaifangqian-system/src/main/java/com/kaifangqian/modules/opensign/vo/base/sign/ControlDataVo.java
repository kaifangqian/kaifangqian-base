/**
 * @description 控件数据
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
package com.kaifangqian.modules.opensign.vo.base.sign;

// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: ControlDataVo
 * @Package: com.kaifangqian.modules.opensign.vo.base.sign
 * @ClassName: ControlDataVo
 * @author: FengLai_Gong
 */
// @ApiModel("控件数据")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControlDataVo implements Serializable {


    private static final long serialVersionUID = -3448600153873319534L;

    // @ApiModelProperty("控件变更状态")
    private String controlChangeFlag ;

    // @ApiModelProperty("控件列表数")
    private List<DocControlVo> controlList ;
}