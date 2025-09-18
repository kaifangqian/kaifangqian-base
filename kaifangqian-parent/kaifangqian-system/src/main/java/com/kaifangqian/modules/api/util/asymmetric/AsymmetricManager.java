/**
 * @description 电子签名类型
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
package com.kaifangqian.modules.api.util.asymmetric;

import com.kaifangqian.common.constant.ApiConstants;
import com.kaifangqian.exception.PaasException;

/**
 * @author : zhenghuihan
 * create at:  2024/3/20  14:19
 * @description:
 */
public class AsymmetricManager {

    public static IAsymmetricEncryptor getByName(String type) throws PaasException {
        if (ApiConstants.SIGN_TYPE_RSA.equals(type)) {
            return new RSAEncryptor();
        }
        if (ApiConstants.SIGN_TYPE_RSA2.equals(type)) {
            return new RSA2Encryptor();
        }
        if (ApiConstants.SIGN_TYPE_SM2.equals(type)) {
            return new SM2Encryptor();
        }
        return new RSA2Encryptor();
    }

}