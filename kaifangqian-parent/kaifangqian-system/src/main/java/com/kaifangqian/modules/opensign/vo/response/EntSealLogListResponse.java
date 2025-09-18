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

import com.fasterxml.jackson.annotation.JsonFormat;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: EntSealLogListResponse
 * @Package: com.kaifangqian.modules.opensign.vo.response
 * @ClassName: EntSealLogListResponse
 * @author: FengLai_Gong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// @ApiModel("印章日志列表返回对象")
public class EntSealLogListResponse implements Serializable {

    // @ApiModelProperty("主键")
    private String id;

    // @ApiModelProperty("印章id")
    private String sealId ;

    // @ApiModelProperty("印章名称")
    private String sealName ;

    // @ApiModelProperty("印章样式（1、公章；2、圆形章；3、椭圆形；）")
    private Integer sealStyle ;

    // @ApiModelProperty("印章类型（1、公章；2、财务专用章；3、合同专用章；4、人事专用章；5、其他）")
    private Integer sealType ;

    // @ApiModelProperty("创建类型（1、模板创建；2、上传创建）")
    private Integer createType ;

    // @ApiModelProperty("操作日志类型")
    private Integer operateType ;

//    // @ApiModelProperty("印章状态（制作中、制作失败、已停用、已启用、已收缴、已销毁）")
//    private Integer sealStatus ;

    // @ApiModelProperty("申请状态（待提交、待重新提交、待审批、审批未通过、审批通过、作废）")
    private Integer applyStatus ;

    // @ApiModelProperty("申请人")
    private String applierName ;

    // @ApiModelProperty("申请时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime ;


}