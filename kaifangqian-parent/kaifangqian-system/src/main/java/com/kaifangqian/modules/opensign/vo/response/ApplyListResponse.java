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
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: OperateListResponse
 * @Package: com.kaifangqian.modules.opensign.vo.response
 * @ClassName: OperateListResponse
 * @author: FengLai_Gong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// @ApiModel("模版操作记录-返回对象")
public class ApplyListResponse implements Serializable {

    private static final long serialVersionUID = -3820442876877111513L;

    // @ApiModelProperty("主键")
    private String templateApplyId;

    // @ApiModelProperty("模板id")
    private String templateId ;


    // @ApiModelProperty("申请人租住用户id")
    private String sysTenantUserId ;


    // @ApiModelProperty("申请人租住用户名称")
    private String sysTenantUserName ;

    // @ApiModelProperty("操作类型")
    private Integer operateType ;

    // @ApiModelProperty("操作类型名称")
    private String operateName ;


    // @ApiModelProperty("申请时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime ;

    // @ApiModelProperty("申请状态（1、待提交，2、待重新提交，3、待审批，4、审批未通过，5、审批通过，6、作废）")
    private Integer applyStatus ;

    // @ApiModelProperty("申请状态名称（1、待提交，2、待重新提交，3、待审批，4、审批未通过，5、审批通过，6、作废）")
    private String applyName ;







}