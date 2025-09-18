/**
 * @description 印章审计返回对象
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
package com.kaifangqian.modules.opensign.vo.base;

// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: SealAuditVo
 * @Package: com.kaifangqian.modules.opensign.service.cert
 * @ClassName: SealAuditVo
 * @author: Fusion
 * CreateTime:  2023/8/18  14:50
 * @copyright 北京资源律动科技有限公司
 */
@Data
// @ApiModel("印章审计返回对象")
public class SealAuditVo {

    // @ApiModelProperty(value = "印章名称")
    private String sealName;                //印章名称

    // @ApiModelProperty(value = "印章类型")
    private String sealType;                //印章类型

    // @ApiModelProperty(value = "制作（新制）完成时间")
    private String createTime;             //制作（新制）完成时间

    // @ApiModelProperty(value = "变更数量")
    private Integer changeCount;           //变更数量

    // @ApiModelProperty(value = "停用数量")
    private Integer stopCount;             //停用数量

    // @ApiModelProperty(value = "激活数量")
    private Integer activateCount;         //激活数量

    // @ApiModelProperty(value = "收缴数量")
    private Integer collectionCount;       //收缴数量

    // @ApiModelProperty(value = "激活数量")
    private Integer destructionCount;      //激活数量

    // @ApiModelProperty(value = "当前状态")
    private String sealStatus;              //当前状态

}
