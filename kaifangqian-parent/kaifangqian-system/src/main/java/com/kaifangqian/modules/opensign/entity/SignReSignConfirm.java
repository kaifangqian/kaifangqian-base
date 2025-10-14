/**
 * @description 业务线配置-签署意愿数据表
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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: SignReSignConfirm
 * @Package: com.kaifangqian.modules.opensign.entity
 * @ClassName: SignReSignConfirm
 * @author: FengLai_Gong
 */
@Data
@TableName("sign_re_sign_confirm")
// @ApiModel("业务线配置-签署意愿数据表")
public class SignReSignConfirm implements Serializable {

    private static final long serialVersionUID = -1798958738287193145L;

    @TableId(type = IdType.ASSIGN_ID)
    // @ApiModelProperty("主键")
    private String id;

    // @ApiModelProperty("'业务线主表id'")
    private String signReId ;

    // @ApiModelProperty("'签署方类型，1发起方，2接收方'")
    private Integer signerType ;

    // @ApiModelProperty("'signer表id或者sender表id'")
    private String signerId ;

    // @ApiModelProperty("'是否为快速签署，1是，0否'")
    private Integer agreeSkipWillingness;

    // @ApiModelProperty("校验类型")
    private String confirmType ;

    //value字段值为：required（须实名认证）、allowed（允许不实名认证）、not_required（无需实名认证）
    private String personalSignAuth ;

}