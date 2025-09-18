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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.modules.system.entity.SysTenantUserRecord;
import com.kaifangqian.modules.system.entity.SysUser;
import com.kaifangqian.annotation.ResrunLogMethod;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.enums.OperateLogType;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.system.service.ISysTenantUserRecordService;
// import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author zhh
 */
@RestController
@RequestMapping("/sysTenantUserRecord")
// @Api(tags = "租户用户申请记录")
@Slf4j
@ResrunLogModule(name = "租户用户申请记录")
public class SysTenantUserRecordController {

    @Autowired
    private ISysTenantUserRecordService sysTenantUserRecordService;

    /**
     * @param tenantUserRecord
     * @return
     * @功能：分页查询(分层查询)
     */
    @ResrunLogMethod(name = "列表", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<IPage<SysTenantUserRecord>> queryPageList(SysTenantUserRecord tenantUserRecord, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<SysTenantUserRecord> page = new Page<SysTenantUserRecord>(pageNo, pageSize);
        IPage<SysTenantUserRecord> pageList = sysTenantUserRecordService.pageExt(page, tenantUserRecord);
        return Result.OK(pageList);
    }

    @ResrunLogMethod(name = "审核", operateType = OperateLogType.OPERATE_TYPE_3)
    @RequestMapping(value = "/check", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<SysUser> edit(@RequestBody SysTenantUserRecord tenantUserRecord) {
        sysTenantUserRecordService.check(tenantUserRecord);
        return Result.OK();
    }

}