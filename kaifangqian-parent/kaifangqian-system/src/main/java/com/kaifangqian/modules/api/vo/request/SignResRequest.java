/**
 * @description 获取业务线列表
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
package com.kaifangqian.modules.api.vo.request;

import com.kaifangqian.modules.api.base.ReqBaseVO;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: SignResRequest
 * @Package: com.kaifangqian.modules.api.vo.request
 * @ClassName: SignResRequest
 * @author: FengLai_Gong
 * @Date: 2024/3/4 14:05
 */
@Data
// @ApiModel("获取业务线列表")
public class SignResRequest extends ReqBaseVO implements Serializable {

    private static final long serialVersionUID = 2718430929268123520L;

//    // @ApiModelProperty("法人单位名称，完整的法人单位名称，与工商注册信息一致")
//    private String companyName ;

    // @ApiModelProperty("办理人账号，办理人需要在该法人单位下，支持邮箱和手机")
    private String contact ;
}