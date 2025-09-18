/**
 * @description 开放签API接口码
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
package com.kaifangqian.common.constant;

/**
 * @Author: gfl
 */
public enum ApiCode implements IApiCode {
    SUCCESS(10000, "成功"),

    PARAM_MISSING(21000, "{0} 参数缺失"),

    PARAM_NULL(22000, "{0} 参数不能为空"),

    PARAM_FORMAT_ERROR(23000, "{0} 参数格式不正确,{reason}"),

    PARAM_SIZE_INVALID(23000, "{0} 参数格式不正确,长度不合法"),

    PARAM_INVALID(23001, "{0} 参数格式不正确,手机号不合法"),

    PARAM_TYPE_INVALID(24000, "{0} 参数类型不支持"),

    SIGN_CHARSET_ERROR(24001, "%s签名遭遇异常，请检查编码格式是否正确。 charset=%s"),

    DATA_FORMAT_ERROR(25000, "POST请求的数据结构错误只支持JSON结构。"),

    DATA_CHECK_ERROR(26000, "数据验签失败。"),

    BUSINESS_HANDLE_ERROR(31000, "业务处理失败,{reason}"),

    EXTERNAL_ACCOUNT_REPEAT(32000, "该账号已经被绑定。"),

    TARGET_COMPANY_UNREGISTERED(32001, "目标企业未注册。"),

    TARGET_USER_UNREGISTERED(32002, "目标用户未注册。"),

    TARGET_USER_LOG_OUT(32003, "目标用户已经注销。"),

    TARGET_ACCOUNT_LOG_OUT(32004, "账号已经注销。"),

    TIMESTAMP_INVALID(41001, "timestamp请求时间戳非法"),
    NONCE_REPEAT(41002, "nonce随机字符串重复"),

    TOKEN_NULL(40000, "token缺失"),

    TOKEN_INVALID(41000, "token无效"),

    DEVELOPER_STOP(42000, "开发者被停用"),

    ATTACK_AQL(51000, "请勿SQL攻击"),

    ATTACK_CROSS(52000, "请勿跨站点攻击"),

    UNKNOWN(91000, "服务器未知错误"),

    ;

    private Integer code;

    private String template;

    ApiCode(Integer code) {
        this.code = code;
    }

    ApiCode(Integer code, String template) {
        this.code = code;
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    public static ApiCode statOf(Integer code) {
        for (ApiCode state : values()) {
            if (state.code.equals(code)) {
                return state;
            }
        }
        return ApiCode.UNKNOWN;
    }
}