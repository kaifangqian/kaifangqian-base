/**
 * @description 云盾同步回调页面跳转业务逻辑
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

import com.kaifangqian.external.auth.response.CallbakcPageResponse;
import com.kaifangqian.common.redis.util.RedisUtil;
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
// @Api(tags = "云盾同步回调业务逻辑")
@RestController
@RequestMapping("/yundun/callback")
@Slf4j
public class YundunCallbackPageController {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 云盾回调逻辑处理
     * @return
     */
    // @ApiOperation(value = "云盾同步回到跳转页面逻辑", notes = "云盾同步回到跳转页面逻辑")
    @GetMapping(value = "/page")
    public Result<?> callbackPage(String token) throws Exception {
        String callbackPage = (String) redisUtil.get(token);
        CallbakcPageResponse callbackPageResponse = new CallbakcPageResponse();
        callbackPageResponse.setToken(callbackPage);
        return Result.OK(callbackPageResponse);
    }

}
