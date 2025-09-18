/**
 * @description API接口合同签署方个人用户
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

import com.kaifangqian.modules.api.base.ReqBaseVO;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: ContractAuthSign
 * @Package: com.kaifangqian.modules.api.vo.base
 * @ClassName: ContractAuthSign
 * @author: FengLai_Gong
 * @Date: 2024/3/23
 */
@Data
// @ApiModel("授权签署")
public class ContractSignPersonalProxy extends ReqBaseVO implements Serializable {

    private static final long serialVersionUID = 2595219430047501375L;

    // @ApiModelProperty("合同ID")
    private String contractId ;

    // @ApiModelProperty("PERSON-个人用户签署方；EMPLOYEE-企业签署方下的个人签字节点")
    private String signatureSubjectType ;

//    // @ApiModelProperty("节点类型：ENTERPRISE_SEAL-组织签章、AGENT_SIGN-经办人签字、LEGAL_PERSON_SIGN-法人签字、PERSONAL_SIGN-个人签名")
//    private String nodeType ;

    // @ApiModelProperty("签署人")
    private ContractUser signer ;

    // @ApiModelProperty("签署位置集合")
    private List<ContractPositionParam> positionParamList ;



}