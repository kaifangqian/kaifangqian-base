/**
 * @description API接口合同签署任务回调地址对象
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
 * @Description: ContractTaskUrlRequest
 * @Package: com.kaifangqian.modules.api.vo.request
 * @ClassName: ContractTaskUrlRequest
 * @author: FengLai_Gong
 * @Date: 2024/3/19
 */
@Data
public class ContractTaskUrlRequest extends ReqBaseVO implements Serializable {

    private static final long serialVersionUID = -5626208610194753317L;

    // @ApiModelProperty("办理人账号唯一标识，个人任务：个人账号唯一标识；企业任务：企业下对应的员工唯一标识")
    private String operatorAccount ;

    // @ApiModelProperty("合同id")
    private String contractId ;

    // @ApiModelProperty("任务id")
    private String taskId ;

    // @ApiModelProperty("链接类型，h5、pc")
    private String linkType ;

    // @ApiModelProperty("同步回调地址，任务完成后的页面跳转地址，如果无，则停留在任务页面")
    private String callbackPage ;

}