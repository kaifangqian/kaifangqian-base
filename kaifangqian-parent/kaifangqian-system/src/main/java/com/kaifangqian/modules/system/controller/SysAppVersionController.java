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

import javax.servlet.http.HttpServletRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import com.kaifangqian.modules.system.entity.SysAppVersion;
import com.kaifangqian.modules.system.entity.SysPermission;
import com.kaifangqian.modules.system.vo.SysAppVersionPermissionVO;
import com.kaifangqian.annotation.ResrunLogMethod;
import com.kaifangqian.common.enums.OperateLogType;
import com.kaifangqian.common.system.query.QueryGenerator;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.system.service.ISysAppVersionPermissionService;
import com.kaifangqian.modules.system.service.ISysAppVersionService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.modules.system.service.ISysPermissionService;
import com.kaifangqian.utils.MyStringUtils;
import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;

import java.util.Iterator;
import java.util.List;

// @Api(tags = "应用版本表")
@RestController
@RequestMapping("/system/sysAppVersion")
@Slf4j
public class SysAppVersionController {
    @Autowired
    private ISysAppVersionService sysAppVersionService;
    @Autowired
    private ISysAppVersionPermissionService iSysAppVersionPermissionService;
    @Autowired
    private ISysPermissionService sysPermissionService;

    /**
     * 分页列表查询
     *
     * @param sysAppVersion
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    // @ApiOperation(value = "应用版本表-分页列表查询", notes = "应用版本表-分页列表查询")
    @GetMapping(value = "/list")
    @RequiresPermissions("system:develop")
    public Result<?> queryPageList(SysAppVersion sysAppVersion,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        if (MyStringUtils.isBlank(sysAppVersion.getAppInfoId())) {
            return Result.error("appInfoId不能为空");
        }
        QueryWrapper<SysAppVersion> queryWrapper = QueryGenerator.initQueryWrapper(sysAppVersion, req.getParameterMap());
        Page<SysAppVersion> page = new Page<SysAppVersion>(pageNo, pageSize);
        IPage<SysAppVersion> pageList = sysAppVersionService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param sysAppVersion
     * @return
     */
    // @ApiOperation(value = "应用版本表-添加", notes = "应用版本表-添加")
    @PostMapping(value = "/add")
    @RequiresPermissions("system:develop")
    public Result<?> add(@RequestBody SysAppVersion sysAppVersion) {
        sysAppVersionService.saveExt(sysAppVersion);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param sysAppVersion
     * @return
     */
    // @ApiOperation(value = "应用版本表-编辑", notes = "应用版本表-编辑")
    @PutMapping(value = "/edit")
    @RequiresPermissions("system:develop")
    public Result<?> edit(@RequestBody SysAppVersion sysAppVersion) {
        sysAppVersionService.updateExt(sysAppVersion);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    // @ApiOperation(value = "应用版本表-通过id删除", notes = "应用版本表-通过id删除")
    @DeleteMapping(value = "/delete")
    @RequiresPermissions("system:develop")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        sysAppVersionService.removeExt(id);
        return Result.OK("删除成功!");
    }

    /**
     * 应用版本-发布
     *
     * @param sysAppVersion
     * @return
     */
    // @ApiOperation(value = " 应用-发布", notes = " 应用-发布")
    @PutMapping(value = "/updateStatus")
    @RequiresPermissions("system:develop")
    public Result<?> updateStatus(@RequestBody SysAppVersion sysAppVersion) {
        sysAppVersionService.updateStatus(sysAppVersion.getId());
        return Result.OK("发布成功！");
    }

//    /**
//     * 批量删除
//     *
//     * @param ids
//     * @return
//     */
//    // @ApiOperation(value = "应用版本表-批量删除", notes = "应用版本表-批量删除")
//    @DeleteMapping(value = "/deleteBatch")
//    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
//        this.sysAppVersionService.removeBatchExt(Arrays.asList(ids.split(",")));
//        return Result.OK("批量删除成功!");
//    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    // @ApiOperation(value = "应用版本表-通过id查询", notes = "应用版本表-通过id查询")
    @GetMapping(value = "/queryById")
    @RequiresPermissions("system:develop")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SysAppVersion sysAppVersion = sysAppVersionService.getById(id);
        sysAppVersion.setPermissions(iSysAppVersionPermissionService.listVersionPermissions(id));
        if (sysAppVersion == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(sysAppVersion);
    }

    /**
     * 查询应用版本-权限列表
     *
     * @param appVersionId
     * @return
     */
    // @ApiOperation(value = "查询应用版本-权限列表", notes = "查询应用版本-权限列表")
    @GetMapping(value = "/listVersionPermissions")
    @ResrunLogMethod(name = "查询应用版本-权限列表", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequiresPermissions("system:develop")
    public Result<?> listVersionPermissions(@RequestParam(name = "appVersionId", required = true) String appVersionId) {
        return Result.OK(iSysAppVersionPermissionService.listVersionPermissions(appVersionId));
    }

    /**
     * 查询应用版本-菜单树  目录+菜单+按钮
     *
     * @param appVersionId
     * @return
     */
    // @ApiOperation(value = "查询应用版本-菜单树", notes = "查询应用版本-菜单树")
    @GetMapping(value = "/listVersionMenuTree")
    @RequiresPermissions("system:develop")
    public Result<?> listVersionMenuTree(@RequestParam(name = "appVersionId", required = true) String appVersionId) {
        List<String> permissionIds = iSysAppVersionPermissionService.listVersionPermissions(appVersionId);
        if (CollUtil.isNotEmpty(permissionIds)) {
            List<SysPermission> list = sysPermissionService.listByIds(permissionIds);
            if (CollUtil.isNotEmpty(list)) {
                List<Tree<String>> tree = sysPermissionService.getTreeList(list);
                return Result.OK(tree);
            }
        }
        return Result.OK();
    }

    /**
     * 查询应用版本-菜单树  目录+菜单
     *
     * @param appVersionId
     * @return
     */
    // @ApiOperation(value = "查询应用版本-菜单树", notes = "查询应用版本-菜单树")
    @GetMapping(value = "/listVersionMenuTree2")
    @RequiresPermissions("system:develop")
    public Result<?> listVersionMenuTree2(@RequestParam(name = "appVersionId", required = true) String appVersionId) {
        List<String> permissionIds = iSysAppVersionPermissionService.listVersionPermissions(appVersionId);
        if (CollUtil.isNotEmpty(permissionIds)) {
            List<SysPermission> list = sysPermissionService.listByIds(permissionIds);
            if (CollUtil.isNotEmpty(list)) {
                Iterator<SysPermission> iterator = list.iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().getMenuType() == 2) {
                        iterator.remove();
                    }
                }
                List<Tree<String>> tree = sysPermissionService.getTreeList(list);
                return Result.OK(tree);
            }
        }
        return Result.OK();
    }

    /**
     * 查询当前版本菜单下所有的按钮以及功能权限
     *
     * @return
     */
    @ResrunLogMethod(name = "查询当前版本菜单下所有的按钮以及功能权限", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/listButtonAndAuth2", method = RequestMethod.GET)
    @RequiresPermissions("system:develop")
    public Result<?> listButtonAndAuth2(@RequestParam(name = "versionId", required = true) String versionId, @RequestParam(name = "permissionId", required = true) String permissionId) {
        return Result.OK(sysPermissionService.listButtonAndAuth2(versionId, permissionId));
    }


    /**
     * 编辑应用版本-权限列表
     *
     * @param vo
     * @return
     */
    // @ApiOperation(value = "编辑应用版本-权限列表", notes = "编辑应用版本-权限列表")
    @PostMapping(value = "/editVersionPermissions")
    @RequiresPermissions("system:develop")
    @ResrunLogMethod(name = "编辑应用版本-权限列表", operateType = OperateLogType.OPERATE_TYPE_3)
    public Result<?> editVersionPermissions(@RequestBody SysAppVersionPermissionVO vo) {
        iSysAppVersionPermissionService.editVersionPermissions(vo);
        return Result.OK("编辑成功!");
    }
}
