/**
 * @description 业务线配置-填写参数对象
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
package com.kaifangqian.modules.opensign.vo.base.sign;

// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: DocParamVo
 * @Package: com.kaifangqian.modules.opensign.vo.base.sign
 * @ClassName: DocParamVo
 * @author: FengLai_Gong
 */
@Data
// @ApiModel("业务线配置-填写参数对象")
public class DocParamVo implements Serializable {

    private static final long serialVersionUID = -352142536984302186L;

    // @ApiModelProperty("'主键'")
    private String id ;

    // @ApiModelProperty("业务线主表id")
    private String signReId ;


    // @ApiModelProperty("'业务线配置-签约文件id'")
    private String signReDocId ;



    // @ApiModelProperty("'人员类型，1发起方，2接收方'")
    private Integer signerType ;

    // @ApiModelProperty("'签署方id，signerId或者senderId'")
    private String signerId ;

    // @ApiModelProperty("'接口参数名称'")
    private String interfaceParamName ;


}