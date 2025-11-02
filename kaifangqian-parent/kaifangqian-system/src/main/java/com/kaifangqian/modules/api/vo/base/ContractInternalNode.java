/**
 * @description API接口合同内部节点对象
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
import java.util.List;

/**
 * @Description: SignInternalNode
 * @Package: com.kaifangqian.modules.api.vo.base
 * @ClassName: SignInternalNode
 * @author: FengLai_Gong
 * @Date: 2024/03/17
 */
@Data
// @ApiModel("内部签署节点")
public class ContractInternalNode implements Serializable {

    private static final long serialVersionUID = 7177160027813408013L;

    // @ApiModelProperty("节点类型：ENTERPRISE_SEAL-组织签章、AGENT_SIGN-经办人签字、LEGAL_PERSON_SIGN-法人签字、PERSONAL_SIGN-个人签名")
    private String nodeType ;

    // @ApiModelProperty("节点签署顺序")
    private String signerOrder ;

    // @ApiModelProperty("自动盖章,AUTO_SIGN")
    private String autoSign ;

    // @ApiModelProperty("印章ID")
    private String sealId ;

    // @ApiModelProperty("签署人")
    private ContractUser signer ;

    // @ApiModelProperty("签署位置集合")
    private List<ContractPositionParam> positionParamList ;

    // @ApiModelProperty("意愿校验方式,人脸FACE")
    private String signConfirm ;

    // @ApiModelProperty("意愿校验方式,CAPTCHA,PASSWORD,DOUBLE,FACE")
    private String verifyType;

    // @ApiModelProperty("是否免意愿快捷签署")
    private Integer agreeSkipWillingness;

    //value字段值为：required（须实名认证）、allowed（允许不实名认证）、not_required（无需实名认证）
    private String personalSignAuth ;

    // @ApiModelProperty("不限制：NOLIMIT；个人签名方式：TEMPLATE：模板生成、HAND：手写签名")
    private String sealType ;

//    // @ApiModelProperty("签署人姓名")
//    private String name ;

//    // @ApiModelProperty("签署人手机号")
//    private String mobile ;
}