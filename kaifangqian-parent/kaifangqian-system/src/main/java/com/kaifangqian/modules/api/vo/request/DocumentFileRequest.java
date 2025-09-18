/**
 * @description API接口合同签署文件对象
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
import com.kaifangqian.modules.api.validation.ValidationSorts;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description: DocumentFileRequest
 * @Package: com.kaifangqian.modules.api.vo.request
 * @ClassName: DocumentFileRequest
 * @author: FengLai_Gong
 * @Date: 2025/3/27
 */
@Data
public class DocumentFileRequest extends ReqBaseVO implements Serializable {

    private static final long serialVersionUID = 7438239524766497990L;

    // @ApiModelProperty("签署文件的base64格式")
    private String file ;


    // @ApiModelProperty("文件名称")
    @NotNull(message = "21000", groups = ValidationSorts.SortA1.class)
    @NotBlank(message = "22000", groups = ValidationSorts.SortA1.class)
    private String fileName ;


    // @ApiModelProperty("文件后缀，如 .pdf")
    private String fileSuffix ;


}