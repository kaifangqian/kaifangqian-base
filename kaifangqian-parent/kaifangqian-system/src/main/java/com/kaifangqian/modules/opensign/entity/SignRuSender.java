/**
 * @description 发起方内部设置
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
import com.kaifangqian.common.base.entity.BaseEntity;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: SignRuDocImage
 * @Package: com.kaifangqian.modules.opensign.entity
 * @ClassName: SignRuDocControl
 * @author: FengLai_Gong
 */
@Data
@TableName("sign_ru_sender")
// @ApiModel("发起方内部设置")
public class SignRuSender extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 5748186824870227438L;

    // @ApiModelProperty("'主键'")
    private String id;

    // @ApiModelProperty("'签署方主表id'")
    private String signerId;

    // @ApiModelProperty("'顺序'")
    private Integer senderOrder;

    // @ApiModelProperty("'发起方类型'")
    private Integer senderType;

    // @ApiModelProperty("'发起方名称'")
    private String senderName;

    // @ApiModelProperty("'发起方指定签章id'")
    private String senderSealId;

    // @ApiModelProperty("'发起方盖章方式，1自动盖章，2指定位置盖章，3审批'")
    private Integer senderSignType;

    // @ApiModelProperty("'发起方id，租户下用户id'")
    private String senderUserId;

    // @ApiModelProperty("'外部签署人类型，1手机号，2邮箱号'")
    private Integer senderExternalType;

    // @ApiModelProperty("'外部签署人接受值'")
    private String senderExternalValue;
}