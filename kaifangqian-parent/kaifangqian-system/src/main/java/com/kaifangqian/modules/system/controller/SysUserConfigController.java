/**
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
package com.kaifangqian.modules.system.controller;

import com.kaifangqian.modules.system.entity.SysUserConfig;
import com.kaifangqian.modules.system.vo.SysUserConfigVO;
import com.kaifangqian.annotation.ResrunLogMethod;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.enums.OperateLogType;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.system.service.ISysUserConfigService;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author : zhenghuihan
 * create at:  2022/7/27  09:55
 * @description: 用户系统配置控制类
 */

// @Api(tags = "用户系统配置控制类")
@RestController
@RequestMapping("/sys/sysUserConfig")
@Slf4j
@ResrunLogModule(name = "用户系统配置控制类")
public class SysUserConfigController {

    @Autowired
    private ISysUserConfigService sysUserConfigService;

    @ResrunLogMethod(name = "查询系统配置信息", operateType = OperateLogType.OPERATE_TYPE_1)
    // @ApiOperation(value = "获取配置信息", notes = "获取配置信息")
    @GetMapping
    public Result<?> getConfig(@RequestParam(name = "type", required = true) String type) {
        SysUserConfig userConfig = sysUserConfigService.getConfigByType(null, type);
        return Result.OK(userConfig);
    }

    @ResrunLogMethod(name = "设置签约密码", operateType = OperateLogType.OPERATE_TYPE_3)
    // @ApiOperation(value = "设置签约密码", notes = "设置签约密码")
    @PostMapping("/updatePassword")
    public Result<?> updatePassword(@RequestBody SysUserConfigVO configVO) {
        sysUserConfigService.updatePasswordExt(configVO);
        return Result.OK();
    }

    @ResrunLogMethod(name = "设置签约顺序", operateType = OperateLogType.OPERATE_TYPE_3)
    // @ApiOperation(value = "设置签约顺序", notes = "设置签约顺序")
    @PostMapping("/updateType")
    public Result<?> updateType(@RequestBody SysUserConfig userConfig) {
        sysUserConfigService.updateTypeExt(userConfig);
        return Result.OK();
    }
}