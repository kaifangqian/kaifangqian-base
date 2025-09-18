/**
 * @description API接口合同签署回调对象
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
package com.kaifangqian.modules.api.vo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : zhenghuihan
 * create at:  2024/4/11  15:00
 * @description:
 */
@Data
public class ApiCallbackVO implements Serializable {
    private static final long serialVersionUID = -3571860201015990086L;

    private String id;
    /**
     * 回调url
     */
    private String callbackUrl;
    /**
     * 状态0:未回调 1、回调1次不成功 2、回调2次不成功 、3、回调3次不成功 4、回调4次不成功 10、回调成功
     */
    private Integer status;
    /**
     * 请求参数
     */
    private String reqPara;
}