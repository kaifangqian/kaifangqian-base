/**
 * @description 开放签云盾接口码
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
 * @author: yxb
 * @date: 2025/6/6
 */
public enum YundunApiCode implements IApiCode {
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
    VERIFY_FAILED(20000, "验签失败"),
    AUTH_ALREADY_VERIFIED(31100, "已认证，无需再认证"),
    AUTH_NO_AGENT(31101, "企业经办人未认证，无法代理企业进行认证"),
    AUTH_NO_SUBJECT(31102, "认证主体未认证，无法进行认证变更"),
    AUTH_ORDER_NOT_FOUND(31103, "实名认证订单不存在"),
    AUTH_SUBJECT_NOT_FOUND(31104, "认证主体不存在"),
    AUTH_INVALID_AGENT(31105, "{0}不是企业认证的经办人，无法进行企业认证变更"),
    AUTH_MOBILE_NOT_MATCH(31106, "请输入使用当前用户实名认证的手机号"),
    EXTERNAL_ACCOUNT_REPEAT(32000, "该账号已经被绑定。"),
    TARGET_COMPANY_UNREGISTERED(32001, "目标企业未注册。"),
    TARGET_USER_UNREGISTERED(32002, "目标用户未注册。"),
    TARGET_USER_LOG_OUT(32003, "目标用户已经注销。"),
    TARGET_ACCOUNT_LOG_OUT(32004, "账号已经注销。"),
    SIGN_PARTY_NO_AUTH(32100, "签署方未完成认证，无法签署"),
    SIGN_UNION_ID_MISMATCH(32101, "unionId与tenantType类型不匹配"),
    SIGNER_NO_AUTH(32102, "签署人未完成认证，无法签署"),
    SIGN_INTENT_NOT_FOUND(32103, "签署意愿订单不存在"),
    SIGN_INTENT_UNVERIFIED(32104, "签署意愿订单未完成验证，无法提交签署"),
    SIGN_INTENT_EXPIRED(32105, "签署意愿订单失效，无法提交签署"),
    SIGN_INTENT_ALREADY_SIGNED(32106, "签署意愿订单已签署，不能重复签署"),
    SIGN_NO_QUIET_SIGN(32107, "签署方未开通静默签署服务或服务授权过期，无法进行静默签署"),
    SIGNER_NO_QUIET_SIGN_AUTH(32108, "{0}签署方未完成认证，无法开通静默签署服务"),
    SIGN_INVALID_AGENT(32109, "{0}不是企业经办人，无法代理企业开通静默签署服务"),
    SIGN_COMPANY_CONTACT_REQUIRED(32110, "{0}签署方是企业类型，contactUnionId不能为空"),
    SIGN_ALREADY_QUIET_SIGN(32111, "{0}签署方已开通静默签署服务，无需再开通"),
    SIGN_NO_CLOSE_QUIET_SIGN(32112, "签署方未开通静默签署服务或服务授权过期，无需关闭"),
    SIGN_NO_SUBJECT_FOUND(32113, "签署方不存在"),
    SIGNER_NO_AUTH_INVALID_AGENT(32114, "{0}企业经办人未实名认证，无法代理企业开通静默签署服务"),
    SIGNER_NO_AUTH_FOR_WILLINGNESS(32200, "{0}未完成认证，无法开通免意愿快捷签署服务"),
    NO_PERSONAL_ACCOUNT(32201, "{0}非个人账号，无法开通免意愿快捷签署服务"),
    OPEN_QUIET_SIGN_ALREADY(32202, "已开通免意愿快捷签署服务，且服务有效，无需再开通"),
    NO_OPEN_QUIET_SIGN_FOUND(32203, "未开通免意愿快捷签署服务或服务授权过期，无需关闭"),
    TOKEN_NULL(40000, "appId无效"),
    TOKEN_INVALID(41000, "token无效"),
    TIMESTAMP_INVALID(41001, "timestamp请求时间戳非法"),
    NONCE_REPEAT(41002, "nonce随机字符串重复"),
    NO_FEE(41003, "无费用"),
    DEVELOPER_STOP(42000, "开发者被停用"),
    APP_ID_NO_PERMISSION(42000, "appId中未授权该接口调用权限"),
    CALL_TIMEOUT(43000, "调用超时"),
    ATTACK_AQL(51000, "请勿SQL攻击"),
    ATTACK_CROSS(52000, "请勿跨站点攻击"),
    UNKNOWN(91000, "服务器未知错误"),
    ;

    private Integer code;

    private String template;

    YundunApiCode(Integer code) {
        this.code = code;
    }

    YundunApiCode(Integer code, String template) {
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

    public static YundunApiCode statOf(Integer code) {
        for (YundunApiCode state : values()) {
            if (state.code.equals(code)) {
                return state;
            }
        }
        return YundunApiCode.UNKNOWN;
    }
}