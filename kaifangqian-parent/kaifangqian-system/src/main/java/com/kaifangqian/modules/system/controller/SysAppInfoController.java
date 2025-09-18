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

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.kaifangqian.modules.system.entity.SysAppInfo;
import com.kaifangqian.modules.system.enums.AppAndVersionType;
import com.kaifangqian.modules.system.vo.SysAppInfoVO;
import com.kaifangqian.common.system.query.QueryGenerator;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.system.service.ISysAppInfoService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;

// @Api(tags = "应用管理表")
@RestController
@RequestMapping("/system/sysAppInfo")
@Slf4j
public class SysAppInfoController {
    @Autowired
    private ISysAppInfoService sysAppInfoService;

    /**
     * 分页列表查询
     *
     * @param sysAppInfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    // @ApiOperation(value = "应用管理表-分页列表查询", notes = "应用管理表-分页列表查询")
    @GetMapping(value = "/list")
    @RequiresPermissions("system:develop")
    public Result<?> queryPageList(SysAppInfo sysAppInfo,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SysAppInfo> queryWrapper = QueryGenerator.initQueryWrapper(sysAppInfo, req.getParameterMap());
        Page<SysAppInfo> page = new Page<SysAppInfo>(pageNo, pageSize);
        IPage<SysAppInfo> pageList = sysAppInfoService.pageExt(page, queryWrapper);

        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param sysAppInfo
     * @return
     */
    // @ApiOperation(value = "应用管理表-添加", notes = "应用管理表-添加")
    @PostMapping(value = "/add")
    @RequiresPermissions("system:develop")
    public Result<?> add(@RequestBody SysAppInfo sysAppInfo) {
        sysAppInfoService.saveExt(sysAppInfo);
        return Result.OK("添加成功！");
    }

    /**
     * 应用-发布/停用
     *
     * @param sysAppInfo
     * @return
     */
    // @ApiOperation(value = " 应用-发布/停用", notes = " 应用-发布/停用")
    @PutMapping(value = "/updateStatus")
    @RequiresPermissions("system:develop")
    public Result<?> updateStatus(@RequestBody SysAppInfo sysAppInfo) {
        sysAppInfoService.updateStatus(sysAppInfo);
        return Result.OK("发布/停用成功！");
    }

    /**
     * 编辑
     *
     * @param sysAppInfo
     * @return
     */
    // @ApiOperation(value = "应用管理表-编辑", notes = "应用管理表-编辑")
    @PutMapping(value = "/edit")
    @RequiresPermissions("system:develop")
    public Result<?> edit(@RequestBody SysAppInfo sysAppInfo) {
        sysAppInfoService.updateExt(sysAppInfo);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    // @ApiOperation(value = "应用管理表-通过id删除", notes = "应用管理表-通过id删除")
    @DeleteMapping(value = "/delete")
    @RequiresPermissions("system:develop")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        sysAppInfoService.deleteExt(id);
        return Result.OK("删除成功!");
    }

//    /**
//     * 批量删除
//     *
//     * @param ids
//     * @return
//     */
//    // @ApiOperation(value = "应用管理表-批量删除", notes = "应用管理表-批量删除")
//    @DeleteMapping(value = "/deleteBatch")
//    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
//        this.sysAppInfoService.deleteBatchExt(Arrays.asList(ids.split(",")));
//        return Result.OK("批量删除成功!");
//    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    // @ApiOperation(value = "应用管理表-通过id查询", notes = "应用管理表-通过id查询")
    @GetMapping(value = "/queryById")
//    @RequiresPermissions("system:develop")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SysAppInfo sysAppInfo = sysAppInfoService.getById(id);
        if (sysAppInfo == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(sysAppInfo);
    }

    /**
     * 查询团体-所有内置应用
     *
     * @return
     */
    // @ApiOperation(value = "查询团体-所有内置应用", notes = "查询团体-所有内置应用")
    @GetMapping(value = "/listGroupAllDefault")
    public Result<?> listGroupAllDefault() {
        List<Integer> types = new ArrayList<>();
        types.add(AppAndVersionType.GROUP.getType());
        types.add(AppAndVersionType.ALL.getType());
        List<SysAppInfoVO> list = sysAppInfoService.listAllAppsByTypes(types, true);

        return Result.OK(list);
    }

    /**
     * 查询个人-所有内置应用
     *
     * @return
     */
    // @ApiOperation(value = "查询个人-所有内置应用", notes = "查询个人-所有内置应用")
    @GetMapping(value = "/listPersonalAllDefault")
    public Result<?> listPersonalAllDefault() {
        List<Integer> types = new ArrayList<>();
        types.add(AppAndVersionType.PERSONAL.getType());
        types.add(AppAndVersionType.ALL.getType());
        List<SysAppInfoVO> list = sysAppInfoService.listAllAppsByTypes(types, true);

        return Result.OK(list);
    }
}
