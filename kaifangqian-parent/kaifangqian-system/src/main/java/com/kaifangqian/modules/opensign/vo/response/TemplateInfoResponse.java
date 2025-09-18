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

import com.kaifangqian.modules.opensign.vo.base.BusinessAuthVo;
import com.kaifangqian.modules.opensign.vo.base.ImageVo;
import com.kaifangqian.modules.opensign.vo.base.TemplateControlVo;
import com.kaifangqian.modules.opensign.vo.base.TemplateInfo;
import com.kaifangqian.modules.opensign.vo.base.*;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: TemplateInfoResponse
 * @Package: com.kaifangqian.modules.opensign.vo.response
 * @ClassName: TemplateInfoResponse
 * @author: FengLai_Gong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// @ApiModel("模板详情返回对象")
public class TemplateInfoResponse implements Serializable {

    private static final long serialVersionUID = -817868828033884760L;

    // @ApiModelProperty("模板主数据")
    private TemplateInfo templateVo;
    // @ApiModelProperty("模板控件数据")
    private List<TemplateControlVo> templateControlVoList ;
    // @ApiModelProperty("模板图片数据")
    private List<ImageVo> imageVoList ;

//    // @ApiModelProperty("模板权限数据列表")
//    private List<TemplateAuthVo> authVoList ;

    // @ApiModelProperty("印章管理员列表")
    private List<BusinessAuthVo> managerList ;

    // @ApiModelProperty("印章使用者列表")
    private List<BusinessAuthVo> userList ;

}