/**
 * @description 业务线实例主表
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
package com.kaifangqian.modules.opensign.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.kaifangqian.common.base.entity.BaseAuthEntity;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 业务线实例主表
 * @Package: com.kaifangqian.modules.opensign.entity
 * @ClassName: SignRu
 * @author: FengLai_Gong
 */
@Data
@TableName("sign_ru")
// @ApiModel("业务线实例主表")
public class SignRu extends BaseAuthEntity implements Serializable {

    private static final long serialVersionUID = 3427159219338431715L;

    // @ApiModelProperty("'主键'")
    private String id;

    // @ApiModelProperty("''业务线ID''")
    private String signReId;

    // @ApiModelProperty("''文件编号''")
    private String code;

    // @ApiModelProperty("'文件主题'")
    private String subject;

    // @ApiModelProperty("'签署截止时间'")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireDate;

    // @ApiModelProperty("签署人类型，1自定义、2预设流程")
    private Integer signerType;

    // @ApiModelProperty("签署顺序类型，1有序签署、2无序签署")
    private Integer signOrderType;

    // @ApiModelProperty("是否抄送，1为是，2为否")
    private Integer ccedType;

    // @ApiModelProperty("'抄送时机，1为文件发起时，2为文件签署完成时'")
    private Integer ccedOpportunityType;

    // @ApiModelProperty("'是否支持内部抄送，1为是，2为否'")
    private Integer internalCcerType;

    // @ApiModelProperty("'是否支持外部抄送，1为是，2为否'")
    private Integer externalCcerType;

    // @ApiModelProperty("'是否允许添加抄送人，1为是，2为否'")
    private Integer addCcerType;

    // @ApiModelProperty("'是否允许添加签约文件，1为是，2为否'")
    private Integer addFileType;

    // @ApiModelProperty("'是否允许删除签约文件，1为是，2为否'")
    private Integer deleteFileType;

    // @ApiModelProperty("'是否允许添加附件，1为是，2为否'")
    private Integer addAnnexType;

    // @ApiModelProperty("'使用证书签署方式，1使用ca证书，2使用防篡改证书，3不使用证书'")
    private Integer caSignType;

    // @ApiModelProperty("'发起前是否有审批，1为是，2为否'")
    private Integer beforeStartApproveType;

    // @ApiModelProperty("'发起前审批流程id'")
    private String beforeStartApproveId;

    // @ApiModelProperty("'签署前是否有审批，1为是，2为否'")
    private Integer beforeSignApproveType;

    // @ApiModelProperty("'签署前审批流程id'")
    private String beforeSignApproveId;

    // @ApiModelProperty("'异常状态，1为是，2为否'")
    private Integer errorStatus;

    // @ApiModelProperty("状态'")
    private Integer status;

    // @ApiModelProperty("发起时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    // @ApiModelProperty("结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;

    // @ApiModelProperty("控件变更状态")
    private String controlChangeFlag ;


}