/**
 * @description 用户签章授权表
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
import com.kaifangqian.common.base.entity.BaseEntity;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("user_seal_auth")
// @ApiModel("用户签章授权表")
public class UserSealAuth extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -2546725642070245357L;

    // @ApiModelProperty("'主键'")
    private String id;

    // @ApiModelProperty("租户-用户主表id")
    private String tenantUserId;

    // @ApiModelProperty("租户主表id")
    private String tenantId;

    // @ApiModelProperty("业务线主表id")
    private String signReId;

    // @ApiModelProperty("授权日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date authTime;

    // @ApiModelProperty("签名ID")
    private String sealId;

    // @ApiModelProperty("授权状态1：已授权，0：未授权")
    private Integer authStatus;

    @NotBlank(message = "校验类型不能为空")
    // @ApiModelProperty("校验类型不能为空")
    private String confirmType;

    // @ApiModelProperty("类型:phone.email")
    private String type;

    // @ApiModelProperty(value = "验证码")
    private transient String captcha;

    // @ApiModelProperty(value = "验证码key")
    private transient String captchaKey;

    // @ApiModelProperty(value = "签约密码")
    private transient String password;

    // @ApiModelProperty("对应值")
    private String typeValue;

    // @ApiModelProperty("用户提交值")
    private String userPara;
}