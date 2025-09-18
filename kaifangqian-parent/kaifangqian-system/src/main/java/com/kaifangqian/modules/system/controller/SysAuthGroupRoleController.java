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
import com.kaifangqian.modules.system.entity.SysRole;
import com.kaifangqian.modules.system.vo.SysAuthGroupRoleVO;
import com.kaifangqian.annotation.ResrunLogMethod;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.enums.OperateLogType;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.system.service.ISysAuthGroupRoleService;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


/**
 * @author zhenghuihan
 * @description 权限组-角色表
 * @createTime 2022/9/2 18:06
 */
// @Api(tags = "权限组-角色表")
@RestController
@RequestMapping("/system/sysAuthGroupRole")
@Slf4j
@ResrunLogModule(name = "权限组角色管理模块")
public class SysAuthGroupRoleController {

    @Autowired
    private ISysAuthGroupRoleService iSysAuthGroupRoleService;

    /**
     * 根据权限组查询角色
     */
    @ResrunLogMethod(name = "查询权限组中的角色列表", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/listRole", method = RequestMethod.GET)
    public Result<IPage<SysAuthGroupRoleVO>> listGroup(@RequestParam(name = "groupId", required = true) String groupId, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<SysAuthGroupRoleVO> page = new Page<SysAuthGroupRoleVO>(pageNo, pageSize);
        IPage<SysAuthGroupRoleVO> result = iSysAuthGroupRoleService.pageExt(groupId, page);
        return Result.OK(result);
    }

    /**
     * 批量新增角色
     *
     * @param req
     * @return
     */
    // @ApiOperation(value = "权限组-角色表-批量新增角色", notes = "权限组-角色表-批量新增角色")
    @PostMapping(value = "/add")
    @ResrunLogMethod(name = "单个权限组中批量增加角色", operateType = OperateLogType.OPERATE_TYPE_2)
    public Result<?> edit(@RequestBody SysAuthGroupRoleVO req) {
        iSysAuthGroupRoleService.addExt(req);
        return Result.OK("新增成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    @ResrunLogMethod(name = "批量删除权限组中的角色", operateType = OperateLogType.OPERATE_TYPE_4)
    public Result<SysRole> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        iSysAuthGroupRoleService.deleteBatchExt(Arrays.asList(ids.split(",")));
        return Result.OK();
    }
}
