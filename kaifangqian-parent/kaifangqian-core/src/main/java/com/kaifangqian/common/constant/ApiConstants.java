/**
 * @description 开放签业务静态变量数据
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
 * @author : zhenghuihan
 * create at:  2024/3/20  14:34
 */
public class ApiConstants {

    public static final String SIGN_TYPE = "signType";

    public static final String SIGN_TYPE_RSA = "RSA";

    /**
     * sha256WithRsa 算法请求类型
     */
    public static final String SIGN_TYPE_RSA2 = "RSA2";

    public static final String SHA_TYPE = "SHA1";

    public static final String SHA_TYPE256 = "SHA256";

    public static final String SIGN_TYPE_SM2 = "SM2";

    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    public static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";

    public static final String ENCRYPT_TYPE_AES = "AES";

    public static final String APP_ID = "appId";

    public static final String TIMESTAMP = "timestamp";

    public static final String NONCE = "nonce";

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Date默认时区
     **/
    public static final String DATE_TIMEZONE = "GMT+8";

    public static final String SIGN = "sign";

    public static final String APP_AUTH_TOKEN = "appAuthToken";

    public static final String OPERATOR_ACCOUNT = "operatorAccount";

    public static final String UNIQUE_CODE = "uniqueCode";

    public static final String YD_AUTH_TOKEN = "appId";

    public static final String YD_NONCE = "nonce";

    public static final String YD_BIZ_CONTENT_ORDER_NO = "orderNo";

    public static final String YD_BIZ_CONTENT_UNOIN_ID = "unionId";

    public static final String REQ_PARA = "reqPara";

    public static final String FROM_TYPE = "fromType";

    public static final String FROM_API = "api";

    public static final String FROM_WEB = "web";

    /**
     * UTF-8字符集
     **/
    public static final String CHARSET_UTF8 = "UTF-8";

    /**
     * GBK字符集
     **/
    public static final String CHARSET_GBK = "GBK";

    public static final String EXTEND_TYPE_USER = "USER";

    public static final String EXTEND_TYPE_TENANT = "TENANT";

    public static final String API_QUEUE_KEY = "api_message_queue";

//    public static final String API_PROCESSING_KEY = "api_processing_queue";

}