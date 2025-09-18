/**
 * @description 业务线发起方据对象
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
 * @Description: DocSenderVo
 * @Package: com.kaifangqian.modules.opensign.vo.base.sign
 * @ClassName: DocSenderVo
 * @author: FengLai_Gong
 */
@Data
// @ApiModel("业务线发起方据对象")
public class DocSenderVo implements Serializable {

    private static final long serialVersionUID = 8170537268496301279L;

    // @ApiModelProperty("'主键'")
    private String id ;

    // @ApiModelProperty("'签署方主表id'")
    private String signerId ;

    // @ApiModelProperty("'顺序'")
    private Integer senderOrder ;

    // @ApiModelProperty("'发起方类型'")
    private Integer senderType ;

    // @ApiModelProperty("'发起方名称'")
    private String senderName ;

    // @ApiModelProperty("'发起方指定签章id'")
    private String senderSealId ;

    // @ApiModelProperty("'发起方指定签章名称'")
    private String senderSealName ;

    // @ApiModelProperty("'发起方盖章方式，1自动盖章，2指定位置盖章'")
    private Integer senderSignType ;

    // @ApiModelProperty("'发起方id，租户下用户id'")
    private String senderUserId ;

    // @ApiModelProperty("租户下用户名称")
    private String senderUserName ;

    // @ApiModelProperty("填写状态，0无需填写，1为未填写，2为待填写，3已填写，4已拒填")
    private Integer writeStatus = -1;

    // @ApiModelProperty("签署状态，1为未签署，2为待签署，3已签署，4已拒签")
    private Integer signStatus = -1 ;

    // @ApiModelProperty("'外部签署人类型，1手机号，2邮箱号'")
    private Integer senderExternalType;

    // @ApiModelProperty("'外部签署人接受值'")
    private String senderExternalValue;

    // @ApiModelProperty("校验类型，0是关闭校验，1是开启校验，默认是0")
    private Integer confirmType = 0;

    // @ApiModelProperty("意愿校验类型：CAPTCHA，PASSWORD，DOUBLE，FACE;")
    private String verifyType;

    // @ApiModelProperty("是否为快速签署，1是，0否")
    private Integer agreeSkipWillingness = 0;




}