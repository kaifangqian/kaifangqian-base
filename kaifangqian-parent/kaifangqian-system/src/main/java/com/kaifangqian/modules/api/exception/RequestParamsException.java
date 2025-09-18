/**
 * @description 异常接口，只有请求异常（未进入业务时）使用该exception抛出异常，业务中不可以使用。
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
package com.kaifangqian.modules.api.exception;

import com.kaifangqian.common.constant.ApiCode;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @create by zhenghuihan
 * @createTime 2024/3/21 18:25
 * @description 异常接口，只有请求异常（未进入业务时）使用该exception抛出异常，业务中不可以使用。
 */
@NoArgsConstructor
@Data
public class RequestParamsException extends RuntimeException {

    private ApiCode apiCode;

    private String token;

    private String reqPara;

    private String operatorAccount;

    private String uniqueCode;


    public RequestParamsException(String token, String operatorAccount, String uniqueCode, String reqPara, ApiCode apiCode, String msg) {
        super(msg);
        this.token = token;
        this.operatorAccount = operatorAccount;
        this.uniqueCode = uniqueCode;
        this.reqPara = reqPara;
        this.apiCode = apiCode;
    }

    public RequestParamsException(String token, String operatorAccount, String uniqueCode, String reqPara, ApiCode apiCode) {
        super(apiCode.getTemplate());
        this.token = token;
        this.operatorAccount = operatorAccount;
        this.uniqueCode = uniqueCode;
        this.reqPara = reqPara;
        this.apiCode = apiCode;
    }

    public RequestParamsException(String reqPara, ApiCode apiCode, String msg) {
        super(msg);
        this.apiCode = apiCode;
        this.reqPara = reqPara;
    }

    public RequestParamsException(String reqPara, ApiCode apiCode) {
        super(apiCode.getTemplate());
        this.apiCode = apiCode;
        this.reqPara = reqPara;
    }

    public RequestParamsException(ApiCode apiCode, String msg) {
        super(msg);
        this.apiCode = apiCode;
    }


    public RequestParamsException(ApiCode apiCode) {
        super(apiCode.getTemplate());
        this.apiCode = apiCode;
    }
}