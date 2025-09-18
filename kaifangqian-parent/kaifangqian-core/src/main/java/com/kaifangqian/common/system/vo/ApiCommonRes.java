/**
 * @description 通用返回
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
package com.kaifangqian.common.system.vo;

import com.kaifangqian.common.constant.ApiCode;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/**
 * @author : zhenghuihan
 * create at: 2023/12/26
 */
@Data
// @ApiModel(value = "接口返回对象", description = "接口返回对象")
public class ApiCommonRes<T> implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 返回代码
     */
    // @ApiModelProperty(value = "返回代码")
    private Integer code = 10000;

    /**
     * 返回处理消息
     */
    // @ApiModelProperty(value = "返回处理消息")
    private String message = "";

    /**
     * 返回数据对象 data
     */
    // @ApiModelProperty(value = "返回数据对象")
    private T result;

    /**
     * 时间戳
     */
    // @ApiModelProperty(value = "时间戳")
    private long timestamp = System.currentTimeMillis();

    public ApiCommonRes() {
    }

    public ApiCommonRes(T result) {
        this.result = result;
    }

    public ApiCommonRes(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiCommonRes(Integer code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public static <T> ApiCommonRes<T> error(ApiCode apiCode) {
        return new ApiCommonRes(apiCode.getCode(), apiCode.getTemplate(), null);
    }

    public static <T> ApiCommonRes<T> ok() {
        return new ApiCommonRes(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getTemplate());
    }

    public static <T> ApiCommonRes<T> ok(T data) {
        return new ApiCommonRes(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getTemplate(), data);
    }


    public static <T> ApiCommonRes<T> of(T data, ApiCode code) {
        return new ApiCommonRes(code.getCode(), code.getTemplate(), data);
    }


    public static <T> ApiCommonRes<T> of(ApiCode code) {
        return new ApiCommonRes(code.getCode(), code.getTemplate(), null);
    }

    public static <T> ApiCommonRes<T> of(Integer code, String message) {
        return new ApiCommonRes(code, message, null);
    }
}