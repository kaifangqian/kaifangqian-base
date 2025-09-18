/**
 * @description 云盾签署管理服务
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
package com.kaifangqian.external.sign.controller;

import com.kaifangqian.external.auth.request.AuthUrlRequest;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.external.sign.service.SignServiceManageExternal;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author : yxb
 * create at: 2025/6/6
 */
// @Api(tags = "云盾签署管理服务")
@RestController
@RequestMapping("/yundun/sign")
@Slf4j
public class YundunSignServiceManageController {

    @Autowired
    private SignServiceManageExternal signServiceManageExternal;

    /**
     * 签署服务管理-查询静默签署开通情况：
     * @return
     */
    // @ApiOperation(value = "查询静默签署开通情况", notes = "查询静默签署开通情况")
    @GetMapping(value = "/silent/service/info/query")
    public Result<?> silentInfoQuery() throws Exception {
        return Result.OK(signServiceManageExternal.querySilentInfo());
    }

    /**
     * 签署服务管理-查询静默签署开通记录：
     * @return
     */
    // @ApiOperation(value = "查询静默签署开通记录", notes = "查询静默签署开通记录")
    @GetMapping(value = "/silent/service/record/query")
    public Result<?> silentRecordQuery() throws Exception {
        return Result.OK(signServiceManageExternal.querySilentRecord());
    }

    /**
     * 签署服务管理-开通静默签署服务：
     * @return
     */
    // @ApiOperation(value = "开通静默签署服务", notes = "开通静默签署服务")
    @PostMapping(value = "/silent/service/open")
    public Result<?> openSilent(@RequestBody AuthUrlRequest authUrlRequest) throws Exception {
        return Result.OK(signServiceManageExternal.openSilentSignService(authUrlRequest.getCallbackPage()));
    }

    /**
     * 签署服务管理-关闭静默签署服务：
     * @return
     */
    // @ApiOperation(value = "关闭静默签署服务", notes = "关闭静默签署服务")
    @PostMapping(value = "/silent/service/close")
    public Result<?> closeSilent() throws Exception {
        return Result.OK(signServiceManageExternal.closeSilentSignService());
    }


    /**
     * 签署服务管理-查询快捷签署开通情况：
     * @return
     */
    // @ApiOperation(value = "查询快捷签署开通情况", notes = "查询快捷签署开通情况")
    @GetMapping(value = "/willingnesss/service/info/query")
    public Result<?> fastSignInfoQuery() throws Exception {
        return Result.OK(signServiceManageExternal.queryFastSignInfo());
    }

    /**
     * 签署服务管理-查询快捷签署开通记录：
     * @return
     */
    // @ApiOperation(value = "查询快捷签署开通记录", notes = "查询快捷签署开通记录")
    @GetMapping(value = "/willingnesss/service/record/query")
    public Result<?> fastSignRecordQuery() throws Exception {
        return Result.OK(signServiceManageExternal.queryFastSignRecord());
    }


    /**
     * 签署服务管理-开通快捷签署服务：
     * @return
     */
    // @ApiOperation(value = "开通快捷签署服务", notes = "开通快捷签署服务")
    @PostMapping(value = "/willingnesss/service/open")
    public Result<?> openFastSign(@RequestBody AuthUrlRequest authUrlRequest) throws Exception {
        return Result.OK(signServiceManageExternal.openFastSignService(authUrlRequest.getCallbackPage()));
    }

    /**
     * 签署服务管理-关闭快捷签署服务：
     * @return
     */
    // @ApiOperation(value = "关闭快捷签署服务", notes = "关闭快捷签署服务")
    @PostMapping(value = "/willingnesss/service/close")
    public Result<?> closeFastSign() throws Exception {
        return Result.OK(signServiceManageExternal.closeFastSignService());
    }
}
