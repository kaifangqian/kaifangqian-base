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

import com.kaifangqian.modules.system.entity.SysTenantConfig;
import com.kaifangqian.annotation.ResrunLogMethod;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.enums.OperateLogType;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.system.service.ISysTenantConfigService;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author : zhenghuihan
 * create at:  2022/7/27  09:55
 * @description: 租户系统配置控制类
 */

// @Api(tags = "租户系统配置控制类")
@RestController
@RequestMapping("/sys/sysTenantConfig")
@Slf4j
@ResrunLogModule(name = "租户系统配置控制类")
public class SysTenantConfigController {

    @Autowired
    private ISysTenantConfigService sysTenantConfigService;

    @ResrunLogMethod(name = "查询系统配置信息", operateType = OperateLogType.OPERATE_TYPE_1)
    // @ApiOperation(value = "获取配置信息", notes = "获取配置信息")
    @GetMapping
    public Result<?> getConfig(@RequestParam(name = "type", required = true) String type) {
        SysTenantConfig sysTenantConfig = sysTenantConfigService.getConfigByType(null, type);
        return Result.OK(sysTenantConfig);
    }

    @ResrunLogMethod(name = "更新系统配置信息", operateType = OperateLogType.OPERATE_TYPE_3)
    // @ApiOperation(value = "更新配置信息", notes = "更新配置信息")
    @PostMapping
    public Result<?> update(@RequestBody SysTenantConfig sysTenantConfig) {
        sysTenantConfigService.updateById(sysTenantConfig);
        return Result.OK();
    }
}