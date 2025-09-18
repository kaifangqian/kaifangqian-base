/**
 * @description 签名文件状态枚举
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
package com.kaifangqian.modules.opensign.enums;


/**
 * @Description: 签名文件状态枚举
 * @Package: com.kaifangqian.modules.opensign.service.cert
 * @ClassName: SignStatus
 * @author: Fusion
 * CreateTime:  2023/8/19  14:50
 * @copyright 北京资源律动科技有限公司
 */

public enum SignStatus {
    SIGN_STATUS_NOSIGNATURE("PDF文件未发现任何数字签名信息",1),
    SIGN_STATUS_ERROR("PDF签名验证时发生错误，签名包含不正确的、无法识别的、已损坏的或可疑的数据",2),
    SIGN_STATUS_FIDDLE("PDF中存在数字证书，但文档被篡改，进入下一步，展示验证结果",3),
    SIGN_STATUS_RIGHT("PDF中存在数字证书，文档未被篡改，进入下一步，展示验证结果",4),

    ;

    private String msg;
    private Integer code;
    SignStatus(String msg,Integer code){
        this.msg = msg;
        this.code = code;
    }

    public Integer getCode(){
        return code;
    }
    public String getMsg(){
        return msg;
    }
}
