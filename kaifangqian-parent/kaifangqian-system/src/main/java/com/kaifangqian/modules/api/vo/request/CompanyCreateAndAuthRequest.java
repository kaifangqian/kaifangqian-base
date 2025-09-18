/**
 * @description 企业账号的注册和实名请求对象
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
import com.kaifangqian.modules.api.vo.base.ContractUser;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: CompanyCreateAndAuthRequest
 * @Package: com.kaifangqian.modules.api.vo.request
 * @ClassName: CompanyCreateAndAuthRequest
 * @author: FengLai_Gong
 * @Date: 2024/03/27
 */
@Data
// @ApiModel("企业账号的注册和实名请求对象")
public class CompanyCreateAndAuthRequest extends ReqBaseVO implements Serializable {

    private static final long serialVersionUID = -3808597798868457919L;

    // @ApiModelProperty("账号")
    private ContractUser account ;

    // @ApiModelProperty("企业名称或组织机构名称")
    private String companyName ;

    // @ApiModelProperty("企业证件号，多证合一后建议传递统一社会信用代码")
    private String creditCode ;

    // @ApiModelProperty("法人姓名")
    private String legalPerson ;

    // @ApiModelProperty("法人证件号(身份证号)")
    private String identity ;

    // @ApiModelProperty("是否为企业自动生成印章，0：否，1：是,不传递，默认不生成印章")
    private Integer isMakeSeal ;

}