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

import com.kaifangqian.modules.system.entity.SysPermissionData;
import com.kaifangqian.modules.system.vo.SysPermissionDataVO;
import com.kaifangqian.annotation.ResrunLogMethod;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.enums.OperateLogType;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.system.service.ISysPermissionDataService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;

/**
 * @author zhenghuihan
 * @description 权限策略表
 * @createTime 2022/9/2 18:07
 */
// @Api(tags = "权限策略表")
@RestController
@RequestMapping("/system/sysPermissionData")
@Slf4j
@ResrunLogModule(name = "权限策略管理模块")
public class SysPermissionDataController {
    @Autowired
    private ISysPermissionDataService sysPermissionDataService;

    /**
     * 分页列表查询全局的权限策略
     *
     * @param permissionId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ResrunLogMethod(name = "分页查询权限策略", operateType = OperateLogType.OPERATE_TYPE_1)
    // @ApiOperation(value = "权限策略表-分页列表查询", notes = "权限策略表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(@RequestParam(name = "permissionId", required = true) String permissionId,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<SysPermissionData> page = new Page<SysPermissionData>(pageNo, pageSize);
        IPage<SysPermissionData> pageList = sysPermissionDataService.pageExt(page, permissionId);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询全局+租户的权限策略
     *
     * @param permissionId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ResrunLogMethod(name = "分页查询权限策略", operateType = OperateLogType.OPERATE_TYPE_1)
    // @ApiOperation(value = "权限策略表-分页列表查询", notes = "权限策略表-分页列表查询")
    @GetMapping(value = "/listTenant")
    public Result<?> listTenant(@RequestParam(name = "permissionId", required = true) String permissionId,
                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<SysPermissionData> page = new Page<SysPermissionData>(pageNo, pageSize);
        IPage<SysPermissionData> pageList = sysPermissionDataService.pageExt2(page, permissionId);
        return Result.OK(pageList);
    }

    /**
     * 添加全局
     *
     * @param dataVO
     * @return
     */
    @ResrunLogMethod(name = "新增全局权限策略", operateType = OperateLogType.OPERATE_TYPE_2)
    // @ApiOperation(value = "权限策略表-添加全局", notes = "权限策略表-添加全局")
    @PostMapping(value = "/addAll")
    @RequiresPermissions({"authploy:add"})
    public Result<?> addAll(@RequestBody SysPermissionDataVO dataVO) {
        sysPermissionDataService.addAllCustom(dataVO);
        return Result.OK("添加成功！");
    }

    /**
     * 添加租户
     *
     * @param dataVO
     * @return
     */
    @ResrunLogMethod(name = "新增租户权限策略", operateType = OperateLogType.OPERATE_TYPE_2)
    // @ApiOperation(value = "权限策略表-添加租户", notes = "权限策略表-添加租户")
    @PostMapping(value = "/add")
    @RequiresPermissions({"authploy:add"})
    public Result<?> add(@RequestBody SysPermissionDataVO dataVO) {
        sysPermissionDataService.addCustom(dataVO);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param dataVO
     * @return
     */
    @ResrunLogMethod(name = "编辑权限策略", operateType = OperateLogType.OPERATE_TYPE_3)
    // @ApiOperation(value = "权限策略表-编辑", notes = "权限策略表-编辑")
    @PutMapping(value = "/edit")
    @RequiresPermissions({"authploy:edit"})
    public Result<?> edit(@RequestBody SysPermissionDataVO dataVO) {
        sysPermissionDataService.editCustom(dataVO);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @ResrunLogMethod(name = "删除权限策略", operateType = OperateLogType.OPERATE_TYPE_4)
    // @ApiOperation(value = "权限策略表-通过id删除", notes = "权限策略表-通过id删除")
    @DeleteMapping(value = "/delete")
    @RequiresPermissions({"authploy:delete"})
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        sysPermissionDataService.deleteExt(id);
        return Result.OK("删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ResrunLogMethod(name = "查询权限策略详情", operateType = OperateLogType.OPERATE_TYPE_1)
    // @ApiOperation(value = "权限策略表-通过id查询", notes = "权限策略表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SysPermissionDataVO dataVO = sysPermissionDataService.getByIdExt(id);
        return Result.OK(dataVO);
    }
}
