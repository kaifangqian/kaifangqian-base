/**
 * @description API接口合同签署方
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

// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: SignSigner
 * @Package: com.kaifangqian.modules.api.vo.base
 * @ClassName: SignSigner
 * @author: FengLai_Gong
 * @Date: 2024/3/17
 */
@Data
public class ContractSigner implements Serializable {

    private static final long serialVersionUID = 8243426762947446031L;

    // @ApiModelProperty("签署方类型：发起方SENDER、企业接收方RECEIVER_ENT、个人接收方RECEIVER_PERSONAL")
    private String signerType ;

    // @ApiModelProperty("签署方名称，签署方类型为企业，填写企业名称，签署方类型为个人，填写个人姓名")
    private String signerName ;

    // @ApiModelProperty("签署顺序（业务线设置的顺序）")
    private String signerOrder ;

    // @ApiModelProperty("内部签署节点")
    private List<ContractInternalNode> internalNodeList ;

//    // @ApiModelProperty("姓名-个人签署")
//    private String name ;

//    // @ApiModelProperty("手机号-个人签署")
//    private String mobile ;

    // @ApiModelProperty("个人接收方信息")
    private ContractUser receiver ;

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
}