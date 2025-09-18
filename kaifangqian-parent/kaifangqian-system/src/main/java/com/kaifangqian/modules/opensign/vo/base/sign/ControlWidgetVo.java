/**
 * @description 填写控件属性
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
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: ControlWidgetVo
 * @Package: com.kaifangqian.modules.opensign.vo.base.sign
 * @ClassName: ControlWidgetVo
 * @author: FengLai_Gong
 */
// @ApiModel("填写控件属性")
@Data
public class ControlWidgetVo implements Serializable {

    private static final long serialVersionUID = -1432720126169390026L;

    private Integer x ;

    private Integer y ;

    private Integer w ;

    private Integer h ;

    private Integer p ;

    private String n ;

    private Boolean v ;

    private Boolean click ;

    private String uid ;


}