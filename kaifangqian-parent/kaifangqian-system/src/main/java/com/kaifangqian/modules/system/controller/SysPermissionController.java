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
import cn.hutool.core.lang.tree.Tree;
import com.kaifangqian.modules.system.entity.SysAppInfo;
import com.kaifangqian.modules.system.entity.SysPermission;
import com.kaifangqian.modules.system.entity.SysTenantAppVersion;
import com.kaifangqian.modules.system.service.ISysAppInfoService;
import com.kaifangqian.modules.system.service.ISysPermissionService;
import com.kaifangqian.modules.system.service.ISysTenantAppVersionService;
import com.kaifangqian.modules.system.service.ISysUserRoleService;
import com.kaifangqian.modules.system.vo.AppSysPermissionVO;
import com.kaifangqian.modules.system.vo.UserAuthDataQueryVO;
import com.kaifangqian.modules.system.vo.UserPermissionVO;
import com.kaifangqian.annotation.ResrunLogMethod;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.constant.CommonConstant;
import com.kaifangqian.common.enums.OperateLogType;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.utils.MyStringUtils;
import lombok.extern.slf4j.Slf4j;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.service.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单权限表 前端控制器
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("/sys/permission")
@ResrunLogModule(name = "菜单管理模块")
public class SysPermissionController {

    @Autowired
    private ISysPermissionService sysPermissionService;
    @Autowired
    private ISysUserRoleService iSysUserRoleService;
    @Autowired
    private ISysAppInfoService iSysAppInfoService;
    @Autowired
    private ISysTenantAppVersionService iSysTenantAppVersionService;

    /**
     * 添加菜单
     *
     * @param permission
     * @return
     */
    @ResrunLogMethod(name = "新增菜单", operateType = OperateLogType.OPERATE_TYPE_2)
    @RequiresPermissions({"menu:add"})
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<?> add(@RequestBody SysPermission permission) {
        sysPermissionService.addPermission(permission);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑菜单
     *
     * @param permission
     * @return
     */
    @RequiresPermissions({"menu:edit"})
    @ResrunLogMethod(name = "编辑菜单", operateType = OperateLogType.OPERATE_TYPE_3)
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody SysPermission permission) {
        sysPermissionService.editPermission(permission);
        return Result.OK("修改成功！");
    }

    /**
     * 所有目录+菜单+按钮
     *
     * @return
     */
    @ResrunLogMethod(name = "查询所有目录、菜单、按钮树", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<List<Tree<String>>> list(@RequestParam(name = "appId", required = true) String appId) {
        List<Integer> types = new ArrayList<>();
        types.add(0);
        types.add(1);
        types.add(2);
        List<SysPermission> list = sysPermissionService.listByTypes(appId, types);
        List<Tree<String>> tree = sysPermissionService.getTreeList(list);

        return Result.OK(tree);
    }

    /**
     * 所有目录+菜单
     *
     * @return
     */
    @ResrunLogMethod(name = "查询所有目录、菜单树", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/listMenu", method = RequestMethod.GET)
    public Result<List<Tree<String>>> listMenu(@RequestParam(name = "appId", required = true) String appId) {
        List<Integer> types = new ArrayList<>();
        types.add(0);
        types.add(1);
        List<SysPermission> list = sysPermissionService.listByTypes(appId, types);
        List<Tree<String>> tree = sysPermissionService.getTreeList(list);

        return Result.OK(tree);
    }

    /**
     * 所有目录
     *
     * @return
     */
    @ResrunLogMethod(name = "查询所有目录树", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/listCatalogue", method = RequestMethod.GET)
    public Result<List<Tree<String>>> listCatalogue(@RequestParam(name = "appId", required = true) String appId) {
        List<Integer> types = new ArrayList<>();
        types.add(0);
        List<SysPermission> list = sysPermissionService.listByTypes(appId, types);
        List<Tree<String>> tree = sysPermissionService.getTreeList(list);

        return Result.OK(tree);
    }

    /**
     * 系统菜单列表(分层查询)
     *
     * @return
     */
    @ResrunLogMethod(name = "分层查询菜单列表", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/getMenuList", method = RequestMethod.GET)
    public Result<List<SysPermission>> getMenuList(String appId, String parentId) {
        if (MyStringUtils.isBlank(appId) && MyStringUtils.isBlank(parentId)) {
            throw new PaasException("请求数据不完整");
        }
        SysPermission query = new SysPermission();
        if (MyStringUtils.isBlank(parentId)) {
            query.setParentId("");
            query.setAppId(appId);
        } else {
            query.setParentId(parentId);
        }
        List<SysPermission> list = sysPermissionService.listByEntity(query);
        return Result.OK(list);
    }

    /**
     * 查询用户拥有的菜单目录权限
     *
     * @return
     */
    @ResrunLogMethod(name = "查询我的菜单、权限", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/getUserPermission", method = RequestMethod.GET)
    public Result<?> getUserPermission(String appCode) {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        UserAuthDataQueryVO query = new UserAuthDataQueryVO();
        query.setUserId(loginUser.getId());
        query.setDepartId(loginUser.getDepartId());
        query.setTenantId(loginUser.getTenantId());
        if (MyStringUtils.isBlank(appCode)) {
            appCode = loginUser.getAppCode();
        }
        SysAppInfo sysAppInfo = iSysAppInfoService.getByAppCode(appCode);
        if (sysAppInfo == null) {
            return Result.error("应用不可用");
        }
        query.setRoleIds(iSysUserRoleService.getMyRoleIds());
        query.setAppId(sysAppInfo.getId());
        SysTenantAppVersion sysTenantAppVersion = iSysTenantAppVersionService.getByTenantAndApp(loginUser.getTenantId(), sysAppInfo.getId());
        if (sysTenantAppVersion == null) {
            return Result.error("应用不可用");
        }
        query.setAppVersionId(sysTenantAppVersion.getAppVersionId());

        List<SysPermission> metaList = sysPermissionService.queryByUser(query);
        //按钮权限（用户拥有的权限集合）
        List<String> authList = metaList.stream()
                .filter((permission) -> permission.getMenuType().equals(CommonConstant.MENU_TYPE_2))
                .collect(() -> new ArrayList<String>(),
                        (list, permission) -> list.add(permission.getPerms()),
                        (list1, list2) -> list1.addAll(list2)
                );
        //菜单目录树
        List<SysPermission> treeList = metaList.stream()
                .filter((permission) -> permission.getMenuType().equals(CommonConstant.MENU_TYPE_0)
                        || permission.getMenuType().equals(CommonConstant.MENU_TYPE_1))
                .collect(Collectors.toList());
        List<Tree<String>> tree = sysPermissionService.getTreeList(treeList);

        UserPermissionVO menu = new UserPermissionVO();
        menu.setAuthList(authList);
        menu.setMenuTree(tree);

        return Result.OK(menu);
    }


    /**
     * 查询我的菜单目录
     *
     * @return
     */
    @ResrunLogMethod(name = "查询我的菜单查询我的菜单目录", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/getMyMenu", method = RequestMethod.GET)
    public Result<?> getMyMenu() {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        UserAuthDataQueryVO query = new UserAuthDataQueryVO();
        query.setUserId(loginUser.getId());
        query.setDepartId(loginUser.getDepartId());
        query.setTenantId(loginUser.getTenantId());
        query.setRoleIds(iSysUserRoleService.getMyRoleIds());

        List<SysPermission> metaList = sysPermissionService.queryByUser(query);
        //菜单目录树
        List<SysPermission> menuList = metaList.stream()
                .filter((permission) -> permission.getMenuType().equals(CommonConstant.MENU_TYPE_0)
                        || permission.getMenuType().equals(CommonConstant.MENU_TYPE_1))
                .collect(Collectors.toList());

        List<AppSysPermissionVO> appSysPermissionVOS = getAppSysPermissionVOS(menuList);
        return Result.OK(appSysPermissionVOS);
    }

    private List<AppSysPermissionVO> getAppSysPermissionVOS(List<SysPermission> menuList) {
        List<AppSysPermissionVO> appSysPermissionVOS = new ArrayList<>();
        if (CollUtil.isNotEmpty(menuList)) {
            Map<String, List<SysPermission>> appMap = menuList.stream().collect(Collectors.groupingBy(SysPermission::getAppId));
            if (CollUtil.isNotEmpty(appMap)) {
                for (Map.Entry<String, List<SysPermission>> entry : appMap.entrySet()) {
                    List<Tree<String>> tree = sysPermissionService.getTreeList(entry.getValue());
                    AppSysPermissionVO appSysPermissionVO = new AppSysPermissionVO();
                    appSysPermissionVO.setAppId(entry.getKey());
                    SysAppInfo sysAppInfo = iSysAppInfoService.getById(entry.getKey());
                    if (sysAppInfo != null) {
                        appSysPermissionVO.setAppName(sysAppInfo.getAppName());
                    }
                    appSysPermissionVO.setPermissions(tree);

                    appSysPermissionVOS.add(appSysPermissionVO);
                }
            }
        }
        return appSysPermissionVOS;
    }


    /**
     * 删除菜单
     *
     * @param id
     * @return
     */
    @ResrunLogMethod(name = "删除菜单", operateType = OperateLogType.OPERATE_TYPE_4)
    @RequiresPermissions({"menu:delete"})
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        sysPermissionService.deletePermissionLogical(id);
        return Result.OK("删除成功!");
    }

    /**
     * 获取租户应用的所有目录+菜单+按钮
     *
     * @return
     */
    @ResrunLogMethod(name = "获取租户应用的所有目录+菜单+按钮", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/listAllTenantAppMenu", method = RequestMethod.GET)
    public Result<?> listAllTenantAppMenu(String appId) {
        List<Integer> types = new ArrayList<>();
        types.add(0);
        types.add(1);
        types.add(2);
        return getResult(appId, types);
    }

    /**
     * 获取租户应用的所有目录+菜单
     *
     * @return
     */
    @ResrunLogMethod(name = "获取租户应用的所有目录+菜单", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/listTenantAppMenu", method = RequestMethod.GET)
    public Result<?> listTenantAppMenu(String appId) {
        List<Integer> types = new ArrayList<>();
        types.add(0);
        types.add(1);
        return getResult(appId, types);
    }

    private Result<?> getResult(String appId, List<Integer> types) {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        List<SysPermission> list = sysPermissionService.listTenantAppAllMenu(loginUser.getTenantId(), appId, types);

        List<AppSysPermissionVO> appSysPermissionVOS = getAppSysPermissionVOS(list);

        return Result.OK(appSysPermissionVOS);
    }

    /**
     * 查询菜单下所有的按钮以及功能权限
     *
     * @return
     */
    @ResrunLogMethod(name = "查询菜单下所有的按钮以及功能权限", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/listButtonAndAuth", method = RequestMethod.GET)
    public Result<?> listButtonAndAuth(@RequestParam(name = "permissionId", required = true) String permissionId) {
        return Result.OK(sysPermissionService.listButtonAndAuth(permissionId));
    }

}
