/**
 * @description 模版权限-数据对象
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
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * @Description: TemplateAuthVo
 * @Package: com.kaifangqian.modules.opensign.vo.base
 * @ClassName: TemplateAuthVo
 * @author: FengLai_Gong
 */
@Data
// @ApiModel("模版权限-数据对象")
public class TemplateAuthVo implements Serializable {

    private static final long serialVersionUID = 3062709848552403884L;

    // @ApiModelProperty("id")
    private String id ;

    // @ApiModelProperty("模版主表id")
    private String templateId;

    // @ApiModelProperty("权限类型，1管理员，2使用范围")
    private Integer authType ;

    // @ApiModelProperty("用户类型")
    private Integer userType ;

    // @ApiModelProperty("租户用户id")
    private String tenantUserId ;

    // @ApiModelProperty("租户用户名称")
    private String tenantUserName ;

}