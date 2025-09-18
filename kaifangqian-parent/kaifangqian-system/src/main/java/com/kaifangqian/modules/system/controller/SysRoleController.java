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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kaifangqian.modules.system.entity.SysRole;
import com.kaifangqian.modules.system.vo.SysRoleUserVO;
import com.kaifangqian.modules.system.vo.SysUserRoleVO;
import com.kaifangqian.annotation.ResrunLogMethod;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.enums.OperateLogType;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.util.oConvertUtils;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.system.service.ISysRoleService;
import com.kaifangqian.modules.system.service.ISysUserRoleService;
import com.kaifangqian.utils.MyStringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/sys/role")
@Slf4j
@ResrunLogModule(name = "角色管理模块")
public class SysRoleController {
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysUserRoleService iSysUserRoleService;

    /**
     * <p>
     * 部门搜索功能方法,根据关键字模糊搜索角色
     * </p>
     *
     * @param keyWord
     * @return
     */
    @ResrunLogMethod(name = "关键字查询角色列表", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/searchMyBy", method = RequestMethod.GET)
    public Result<IPage<SysRole>> searchMyBy(@RequestParam(name = "keyWord", required = true) String keyWord, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        List<SysRole> roleList = sysRoleService.getMyRoles();
        List<String> roleIds = roleList.stream().map(SysRole::getId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(roleIds)) {
            QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().like(MyStringUtils.isNotBlank(keyWord), SysRole::getRoleName, keyWord)
                    .in(SysRole::getId, roleIds);
            Page<SysRole> page = new Page<SysRole>(pageNo, pageSize);
            IPage<SysRole> result = sysRoleService.page(page, queryWrapper);
            return Result.OK(result);
        }
        return Result.OK();
    }

    /**
     * 查询角色组
     */
    @ResrunLogMethod(name = "查询角色组列表", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/listGroup", method = RequestMethod.GET)
    public Result<List<SysRole>> listGroup() {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRole::getParentId, "")
                .eq(SysRole::getTenantId, loginUser.getTenantId());
        List<SysRole> result = sysRoleService.list(queryWrapper);
        return Result.OK(result);
    }


    /**
     * 所有角色树
     *
     * @return 所有角色树
     */
    @ResrunLogMethod(name = "查询所有角色树", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/allRoleTree", method = RequestMethod.GET)
    public Result<List<Tree<String>>> allRoleTree() {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        SysRole query = new SysRole();
        query.setTenantId(loginUser.getTenantId());
        List<SysRole> list = sysRoleService.getByEntity(query);
        return Result.OK(sysRoleService.getTreeList(list));
    }

    /**
     * 我的角色树
     *
     * @return 我的角色树
     */
    @ResrunLogMethod(name = "查询我的角色树", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/myRoleTree", method = RequestMethod.GET)
    public Result<List<Tree<String>>> myRoleTree() {
        List<SysRole> roleList = sysRoleService.getMyRoles();
        if (CollUtil.isNotEmpty(roleList)) {
            List<String> parentIds = roleList.stream().map(SysRole::getParentId).collect(Collectors.toList());
            List<SysRole> groupRoleList = sysRoleService.listByIds(parentIds);
            roleList.addAll(groupRoleList);
        }
        return Result.OK(sysRoleService.getTreeList(roleList));
    }

    /**
     * 我的角色组
     *
     * @return 我的角色组
     */
    @ResrunLogMethod(name = "查询我的角色组", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/myRoleGroup", method = RequestMethod.GET)
    public Result<?> myRoleGroup() {
        List<SysRole> roleList = sysRoleService.getMyRoles();
        if (CollUtil.isNotEmpty(roleList)) {
            List<String> parentIds = roleList.stream().map(SysRole::getParentId).collect(Collectors.toList());
            List<SysRole> groupRoleList = sysRoleService.listByIds(parentIds);
            return Result.OK(groupRoleList);
        }
        return Result.OK();
    }


    /**
     * 所有角色树（我的角色可以操作）（反显使用）
     *
     * @return 所有角色树（我的角色可以操作）（反显使用）
     */
    @ResrunLogMethod(name = "所有角色树（我的角色可以操作）（反显使用）", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/allRoleTreeForSelect", method = RequestMethod.GET)
    public Result<List<Tree<String>>> allRoleTreeForSelect() {
        SysRole query = new SysRole();
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        query.setTenantId(loginUser.getTenantId());
        List<SysRole> allList = sysRoleService.getByEntity(query);
        List<SysRole> myList = sysRoleService.getMyRoles();
        if (CollUtil.isNotEmpty(allList)) {
            if (CollUtil.isNotEmpty(myList)) {
                List<String> myListIds = myList.stream().map(SysRole::getId).collect(Collectors.toList());
                allList.forEach(r -> {
                    if (myListIds.contains(r.getId())) {
                        r.setOprateFlag(true);
                    }
                });
            }
        }

        return Result.OK(sysRoleService.getTreeListForSelect(allList));
    }

    /**
     * 校验角色编码唯一
     */
    @ResrunLogMethod(name = "校验角色编码是否重复", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/checkRoleCode", method = RequestMethod.GET)
    public Result<?> checkUsername(String id, String roleCode) {
        SysRole role = null;
        if (oConvertUtils.isNotEmpty(id)) {
            role = sysRoleService.getById(id);
        }
        SysRole newRole = sysRoleService.getOne(new QueryWrapper<SysRole>().lambda().eq(SysRole::getRoleCode, roleCode));
        if (newRole != null) {
            //如果根据传入的roleCode查询到信息了，那么就需要做校验了。
            if (role == null) {
                //role为空=>新增模式=>只要roleCode存在则返回false
                return Result.OK(true);
            } else if (!id.equals(newRole.getId())) {
                //否则=>编辑模式=>判断两者ID是否一致-
                return Result.OK(true);
            }
        }
        return Result.OK(false);
    }

    /**
     * 添加
     *
     * @param role
     * @return
     */
    @RequiresPermissions(value = {"role:add", "rolegroup:add"}, logical = Logical.OR)
    @ResrunLogMethod(name = "新增角色", operateType = OperateLogType.OPERATE_TYPE_2)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<SysRole> add(@RequestBody SysRole role) {
        sysRoleService.saveExt(role);
        return Result.OK();
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/queryById", method = RequestMethod.GET)
    public Result<SysRole> queryById(@RequestParam(name = "id", required = true) String id) {
        SysRole sysrole = sysRoleService.getByIdExt(id);
        return Result.OK(sysrole);
    }

    /**
     * 编辑
     *
     * @param role
     * @return
     */
    @RequiresPermissions(value = {"role:edit", "rolegroup:edit"}, logical = Logical.OR)
    @ResrunLogMethod(name = "编辑角色", operateType = OperateLogType.OPERATE_TYPE_3)
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<SysRole> edit(@RequestBody SysRole role) {
        sysRoleService.updateExt(role);
        return Result.OK();
    }

    /**
     * 根据角色id获取人员列表(用户可见)
     *
     * @param id
     * @return
     */
    @ResrunLogMethod(name = "查询角色人员列表", operateType = OperateLogType.OPERATE_TYPE_1)
    @GetMapping("/getUsersByRoleId")
    public Result<IPage<SysRoleUserVO>> getUsersByRoleId(@RequestParam(name = "id") String id, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<SysRoleUserVO> page = new Page<SysRoleUserVO>(pageNo, pageSize);
        IPage<SysRoleUserVO> sysUsers = sysRoleService.getUsersByRoleId(id, page);
        return Result.OK(sysUsers);
    }

    /**
     * 批量移除人员
     *
     * @param ids
     * @return
     */
    @RequiresPermissions({"role:removeuser"})
    @ResrunLogMethod(name = "批量移除角色中的成员", operateType = OperateLogType.OPERATE_TYPE_3)
    @RequestMapping(value = "/removeBatch", method = RequestMethod.DELETE)
    public Result<?> removeBatch(@RequestParam(name = "ids", required = true) String ids) {
        iSysUserRoleService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK();
    }

    /**
     * 角色批量新增人员
     *
     * @param sysUserRoleVO
     * @return
     */
    @RequiresPermissions({"role:adduser"})
    @ResrunLogMethod(name = "批量新增角色中的成员", operateType = OperateLogType.OPERATE_TYPE_2)
    @RequestMapping(value = "/addRoleUser", method = RequestMethod.POST)
    public Result<?> addRoleUser(@RequestBody SysUserRoleVO sysUserRoleVO) {
        iSysUserRoleService.saveExt(sysUserRoleVO);
        return Result.OK();
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @ResrunLogMethod(name = "批量删除角色", operateType = OperateLogType.OPERATE_TYPE_4)
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    @RequiresPermissions({"role:adduser"})
    public Result<SysRole> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        sysRoleService.deleteBatchRole(Arrays.asList(ids.split(",")));
        return Result.OK();
    }
}
