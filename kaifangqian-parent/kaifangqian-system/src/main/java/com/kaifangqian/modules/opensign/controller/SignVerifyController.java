/**
 * @description 电子印章-文件验签
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
package com.kaifangqian.modules.opensign.controller;

import com.alibaba.fastjson.JSONObject;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.opensign.enums.SignStatus;
import com.kaifangqian.modules.opensign.service.verify.SignVerifyService;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.kaifangqian.config.limit.annotation.Limit;
import com.kaifangqian.config.limit.annotation.LimitHandleType;
import com.kaifangqian.config.limit.annotation.LimitType;
import com.kaifangqian.config.limit.annotation.OperateType;
/**
 * @Description: SignVerifyController
 * @Package: com.kaifangqian.modules.opensign.controller
 * @ClassName: SignVerifyController
 * @author: Fusion
 * CreateTime:  2023/8/20  9:53
 * @copyright 北京资源律动科技有限公司
 */
@Slf4j
@RestController
@RequestMapping("/sign/verify")
@ResrunLogModule(name = "文件验签")
// @Api(tags = "电子印章-文件验签")
public class SignVerifyController {

    @Autowired
    private SignVerifyService signVerifyService;

    //处理文件上传请求
    @PostMapping("/checkSign")
    // @ApiOperation(value = "文件验签", notes = "文件验签")
    @Limit(name = "文件验签", prefix = "limit",limitType= LimitType.IP, operateType = OperateType.ALL, count = 5,period=60,limitHandle = LimitHandleType.NONE)
    public Result<?> uploadFile(@RequestParam("file") MultipartFile file) {
        Result<JSONObject> result = new Result<>();
        try {
            result= signVerifyService.getImageFromPdf(file);
        } catch (Exception e) {
            log.error("文件验签失败", e.getMessage());
            return  Result.error(SignStatus.SIGN_STATUS_NOSIGNATURE.getMsg());
        }
        return  result;
    }




}
