/**
 * @description 云盾实名认证、签署数据异步回调同步
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
package com.kaifangqian.external.auth.callback;

import com.alibaba.fastjson.JSON;
import com.kaifangqian.external.aop.ApiYundunAuthThreadLocalAop;
import com.kaifangqian.external.auth.request.AuthCallbackRequest;
import com.kaifangqian.external.sign.request.SignCallbackRequest;
import com.kaifangqian.external.auth.service.IdentityAuthExternal;
import com.kaifangqian.external.sign.service.SignServiceExternal;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : yxb
 * create at: 2025/6/6
 */
// @Api(tags = "云盾实名认证、签署数据异步回调同步")
@RestController
@RequestMapping("/yundun")
@Slf4j
public class YundunCallbackController {

    @Autowired
    private IdentityAuthExternal identityAuthExternal;

    @Autowired
    private SignServiceExternal signServiceExternal;

    @Autowired
    private ApiYundunAuthThreadLocalAop apiYundunAuthThreadLocalAop;

    // @ApiOperation(value = "实名认证异步回调", notes = "实名认证同步回调")
    @PostMapping(value = "/auth/callback")
    public String authCallback() throws Exception {
        String cachedRequestBody = apiYundunAuthThreadLocalAop.getRequestBodyCache().get();
        AuthCallbackRequest request = JSON.parseObject(cachedRequestBody, AuthCallbackRequest.class);

        return identityAuthExternal.updateIdentityAuth(request).getMessage();
    }

    // @ApiOperation(value = "意愿校验签署回调", notes = "意愿校验签署回调")
    @PostMapping(value = "/sign/callback")
    public String signCallback() throws Exception {
        String cachedRequestBody = apiYundunAuthThreadLocalAop.getRequestBodyCache().get();
        SignCallbackRequest request = JSON.parseObject(cachedRequestBody, SignCallbackRequest.class);

        return signServiceExternal.updateSignUserConfirmStatus(request).getMessage();
    }



}
