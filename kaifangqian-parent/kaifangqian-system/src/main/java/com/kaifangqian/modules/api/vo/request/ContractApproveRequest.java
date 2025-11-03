/**
 * @description API合同审批请求对象
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
import com.kaifangqian.modules.api.vo.base.*;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: ContractApproveRequest
 * @Package: com.kaifangqian.modules.api.vo.request
 * @ClassName: ContractDraftRequest
 * @author: resrun_Y
 * @Date: 2025/10/30
 */
@Data
public class ContractApproveRequest extends ReqBaseVO implements Serializable {

    private static final long serialVersionUID = -7293700917961018375L;

    //基本信息
    // @ApiModelProperty("合同id")
    private String contractId;

    // 审批人
    // @ApiModelProperty("审批人")
    private ContractUser signer;

    // 审批原因
    // @ApiModelProperty("审批原因")
    private String comment;

    // 合同审批状态0,未通过；1，通过
    // @ApiModelProperty("合同审批状态0,不通过；1，通过")
    private Integer approval;

}