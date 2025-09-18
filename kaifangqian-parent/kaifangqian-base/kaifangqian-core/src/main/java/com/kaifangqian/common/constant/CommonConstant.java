/**
 * @description 通告常量
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
 * @author: zhh
 * @date: 2022-05-31
 */
public interface CommonConstant {

    /**
     * 通告对象类型（USER:指定用户，ALL:全体用户）
     */
    public static final String MSG_TYPE_UESR = "USER";
    public static final String MSG_TYPE_ALL = "ALL";

    public static final String UNPUBLISHED_STATUS_0 = "0";    //未发布

    public static final String PUBLISHING_STATUS_1 = "1";     //发布中，等开始时间后发布

    public static final String PUBLISHED_STATUS_2 = "2";        //已发布

    public static final String REVOKE_STATUS_3 = "3";            //撤销

    /**
     * 登录用户Shiro权限缓存KEY前缀
     */
    public static String PREFIX_USER_SHIRO_CACHE = "shiro:cache:com.kaifangqian.config.shiro.ShiroRealm.authorizationCache:";
    /**
     * 登录用户Token令牌缓存KEY前缀
     */
    public static final String PREFIX_USER_TOKEN = "prefix_user_token_";

    /**
     * 二维码前缀
     */
    public static final String PREFIX_QR_CODE = "prefix_qr_code_";
    /**
     * 0：一级菜单：目录
     */
    public static final Integer MENU_TYPE_0 = 0;
    /**
     * 1：子菜单
     */
    public static final Integer MENU_TYPE_1 = 1;
    /**
     * 2：按钮权限
     */
    public static final Integer MENU_TYPE_2 = 2;

    /**
     * 是否配置菜单的数据权限 1是0否
     */
    public static final Integer RULE_FLAG_0 = 0;
    public static final Integer RULE_FLAG_1 = 1;

    /**
     * 是否用户已被冻结 1正常(解冻) 2冻结（手动冻结） 3自动冻结（密码错误）
     */
    public static final Integer USER_UNFREEZE = 1;
    public static final Integer USER_FREEZE = 2;
    public static final Integer USER_FREEZE_AUTO = 3;

    //小时
    public static final Integer USER_FREEZE_TIME = 1;
    public static final String USER_FREEZE_PRE_KEY = "userFreeze";

    public final static String TOKEN_IS_INVALID_MSG = "应用Token失效，请重新授权!";

    public final static String MAX_ALIVE_TOKEN_MSG = "应用Token最大登录保持时间已到，请重新授权！!";

    public final static String AUTH_TOKEN_IS_INVALID_MSG = "授权Token失效，请重新登录!";

    public final static String AUTH_MAX_ALIVE_TOKEN_MSG = "授权Token最大登录保持时间已到，请重新登录！!";

    public final static String USER_LACK_INFO = "缺少必要信息!";

    public final static String USER_INFO_ERROR = "用户信息错误!";

    public final static String HTTP_HEAD_APP_CODE = "resrun-app-code";

    public final static String USER_INFO_USELESS = "用户不可用!";

    public static final String BASE_CHECK_CODES = "0123456789qwertyuiplkjhgfdsazxcvbnmQWERTYUPLKJHGFDSAZXCVBNM";

    public static String PREFIX_INVITE_USER_CODE = "sys:cache:inviteUser";

    /**
     * 二维码前缀
     */
    public static final String PREFIX_FACE_CODE = "prefix_face_code_";

    /**
     * TOKEN
     */
    public static final String PREFIX_FACE_ACCESS_TOKEN = "prefix_face_access_token";

    /**
     * TOKEN
     */
    public static final String PREFIX_FACE_SIGN_TICKET = "prefix_face_sign_ticket";


    public static final String HTTP_GET= "get";

    public static final String HTTP_POST= "post";

}
