/**
 * @description 意愿检验数据
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

// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author : zhenghuihan
 * create at:  2022/8/1  17:01
 * @description:
 */
@Data
public class UserConfirmByCodeVO {

    @NotBlank(message = "校验类型不能为空")
    private String confirmType;

    @NotBlank(message = "类型不能为空")
    private String type;

    @NotBlank(message = "订单号不能为空")
    private String orderNo;

    @NotBlank(message = "校验码不能为空")
    // @ApiModelProperty(value = "验证码")
    private String captcha;

    @NotBlank(message = "校验码key不能为空")
    // @ApiModelProperty(value = "验证码key")
    private String captchaKey;

    // @ApiModelProperty(value = "签约密码")
    private String password;
}