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

import com.kaifangqian.modules.opensign.vo.base.BusinessAuthVo;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: EntSealSaveRequest
 * @Package: com.kaifangqian.modules.opensign.vo.request
 * @ClassName: EntSealSaveRequest
 * @author: FengLai_Gong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// @ApiModel("印章新增请求对象")
public class EntSealEditRequest implements Serializable {


    private static final long serialVersionUID = -262548305395622229L;


    // @ApiModelProperty("主键")
    private String id;

    // @ApiModelProperty("印章管理员id，印章所属系统租户下用户id")
    private String adminId ;

    // @ApiModelProperty("印章名称")
    private String sealName ;

    // @ApiModelProperty("印章类型（1、公章；2、财务专用章；3、合同专用章；4、人事专用章；5、其他）")
    private Integer sealType ;

    // @ApiModelProperty("用途说明")
    private String description ;


    // @ApiModelProperty("印章管理员列表")
    private List<BusinessAuthVo> managerList ;

    // @ApiModelProperty("印章使用者列表")
    private List<BusinessAuthVo> userList ;

    // @ApiModelProperty("印章审计者列表")
    private List<BusinessAuthVo> auditorList ;



}