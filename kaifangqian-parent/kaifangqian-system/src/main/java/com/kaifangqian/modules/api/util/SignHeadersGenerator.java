/**
 * @description API请求头签名
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
package com.kaifangqian.modules.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaifangqian.common.util.oConvertUtils;

import java.util.HashMap;
import java.util.Map;

public class SignHeadersGenerator {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {

    }

    /**
     * 完成请求信息签名，返回签名头信息
     */
    public static Map<String,String> generateSignHeaders(String appId, String jsonParams, String uri, String privateKey) throws Exception {
        // 1. 计算biz_content
        String canonicalJson = null;
        String bizContent = null;
        if(jsonParams != null){
            canonicalJson = SignGenerator.canonicalizeJson(jsonParams);
            bizContent = SignGenerator.computeBizContent(canonicalJson);
        }

        // 2. 组装签名字符串（含URL编码）
        String timestamp = System.currentTimeMillis()+"";
        String nonce = oConvertUtils.randomGen(16);

        String signString = SignGenerator.buildSignString(appId, timestamp, nonce, uri, bizContent);

        Map<String,String> headers = new HashMap<>();
        headers.put("appId",appId);
        headers.put("timestamp",timestamp);
        headers.put("nonce", nonce);

        // 计算签名值
        String signVal = ApiSignature.sign(signString,null, privateKey,"RSA2");
        headers.put("sign", signVal);

        return headers;
    }


}
