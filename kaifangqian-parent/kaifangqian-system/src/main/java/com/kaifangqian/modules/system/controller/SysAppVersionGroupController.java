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

import cn.hutool.core.collection.CollUtil;
import com.kaifangqian.modules.system.entity.SysAppVersion;
import com.kaifangqian.modules.system.entity.SysAppVersionGroup;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.system.service.ISysAppVersionGroupService;
import com.kaifangqian.modules.system.service.ISysAppVersionService;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// @Api(tags = "应用权限组表")
@RestController
@RequestMapping("/system/SysAppVersionGroup")
@Slf4j
public class SysAppVersionGroupController {

    @Autowired
    private ISysAppVersionGroupService iSysAppVersionGroupService;
    @Autowired
    private ISysAppVersionService iSysAppVersionService;

    /**
     * 列表查询
     *
     * @param sysAppVersionGroup
     * @return
     */
    // @ApiOperation(value = "权限组表-分页列表查询", notes = "权限组表-分页列表查询")
    @GetMapping(value = "/list")
    @RequiresPermissions("system:develop")
    public Result<List<SysAppVersionGroup>> list(SysAppVersionGroup sysAppVersionGroup) {
        List<SysAppVersionGroup> list = iSysAppVersionGroupService.listByEntity(sysAppVersionGroup);
        if (CollUtil.isNotEmpty(list)) {
            List<String> appVersionIds = list.stream().map(SysAppVersionGroup::getAppVersionId).collect(Collectors.toList());
            List<SysAppVersion> sysAppVersions = iSysAppVersionService.listByIds(appVersionIds);
            if (CollUtil.isNotEmpty(sysAppVersions)) {
                Map<String, SysAppVersion> appVersionMap = sysAppVersions.stream().collect(Collectors.toMap(SysAppVersion::getId, Function.identity(), (i, j) -> i));
                list.forEach(l -> {
                    l.setAppVersionName(appVersionMap.get(l.getAppVersionId()) != null ? appVersionMap.get(l.getAppVersionId()).getVersionName() : "未知版本");
                });
            }
        }
        return Result.OK(list);
    }

    /**
     * 添加
     *
     * @param sysAppVersionGroup
     * @return
     */
    // @ApiOperation(value = "权限组表-添加", notes = "权限组表-添加")
    @PostMapping(value = "/add")
    @RequiresPermissions("system:develop")
    public Result<?> add(@RequestBody SysAppVersionGroup sysAppVersionGroup) {
        iSysAppVersionGroupService.saveExt(sysAppVersionGroup);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param sysAppVersionGroup
     * @return
     */
    // @ApiOperation(value = "权限组表-编辑", notes = "权限组表-编辑")
    @PutMapping(value = "/edit")
    @RequiresPermissions("system:develop")
    public Result<?> edit(@RequestBody SysAppVersionGroup sysAppVersionGroup) {
        iSysAppVersionGroupService.updateExt(sysAppVersionGroup);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    // @ApiOperation(value = "权限组表-通过id查询", notes = "权限组表-通过id查询")
    @GetMapping(value = "/queryById")
    @RequiresPermissions("system:develop")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        return Result.OK(iSysAppVersionGroupService.getByIdExt(id));
    }

    /**
     * 启用-停用
     *
     * @param sysAppVersionGroup
     * @return
     */
    // @ApiOperation(value = " 启用-停用", notes = "启用-停用")
    @PutMapping(value = "/updateStatus")
    @RequiresPermissions("system:develop")
    public Result<?> updateStatus(@RequestBody SysAppVersionGroup sysAppVersionGroup) {
        iSysAppVersionGroupService.updateStatus(sysAppVersionGroup.getId());
        return Result.OK("发布成功！");
    }
}
