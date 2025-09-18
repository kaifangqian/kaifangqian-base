/**
 * @description API接口合同签署截止时间
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
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: ContractExpireDateRequest
 * @Package: com.kaifangqian.modules.api.vo.request
 * @ClassName: ContractExpireDateRequest
 * @author: FengLai_Gong
 * @Date: 2024/3/23
 */
@Data
public class ContractExpireDateRequest extends ReqBaseVO implements Serializable {

    private static final long serialVersionUID = 5956914265620652243L;

    // @ApiModelProperty("合同id")
    private String contractId ;

    // @ApiModelProperty("签署截止时间，日期格式，精确到年月日，且时间不小于当前日期")
    private String expireDate ;



}