/**
 * @description 云盾实名认证，电子签服务开通
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
package com.kaifangqian.external.auth.controller;

import com.kaifangqian.external.auth.request.AuthUrlRequest;
import com.kaifangqian.external.auth.service.IdentityAuthExternal;
import com.kaifangqian.common.vo.Result;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author : yxb
 * create at: 2025/6/6
 */
// @Api(tags = "云盾实名认证")
@RestController
@RequestMapping("/yundun/auth")
@Slf4j
public class YundunAuthController {

    @Autowired
    private IdentityAuthExternal identityAuthExternal;

    /**
     * 个人实名认证
     * @return
     */
    // @ApiOperation(value = "个人实名认证", notes = "个人实名认证")
    @PostMapping(value = "/personal/add")
    public Result<?> personalAuth(@RequestBody AuthUrlRequest authUrlRequest) throws Exception {
        return Result.OK(identityAuthExternal.personalIdentityAuth(authUrlRequest.getCallbackPage()));
    }

    /**
     * 个人实名认证
     * @return
     */
    // @ApiOperation(value = "个人实名认证更新", notes = "个人实名认证更新")
    @PostMapping(value = "/personal/update")
    public Result<?> personalAuthUpdate(@RequestBody AuthUrlRequest authUrlRequest) throws Exception {
        return Result.OK(identityAuthExternal.personalIdentityAuthUpdate(authUrlRequest.getCallbackPage()));
    }

    /**
     * 企业实名认证
     * @return
     */
    // @ApiOperation(value = "企业实名认证", notes = "企业实名认证")
    @PostMapping(value = "/enterprise/add")
    public Result<?> enterpriseAuth(@RequestBody AuthUrlRequest authUrlRequest) throws Exception {
        return Result.OK(identityAuthExternal.companyIdentityAuth(authUrlRequest.getCallbackPage()));
    }

    /**
     * 企业实名认证
     * @return
     */
    // @ApiOperation(value = "企业实名认证更新", notes = "企业实名认证更新")
    @PostMapping(value = "/enterprise/update")
    public Result<?> enterpriseAuthUpdate(@RequestBody AuthUrlRequest authUrlRequest) throws Exception {
        return Result.OK(identityAuthExternal.companyIdentityAuthUpdate(authUrlRequest.getCallbackPage()));
    }

}
