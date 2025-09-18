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
package com.kaifangqian.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.modules.system.entity.SysAuthGroupRole;
import com.kaifangqian.modules.system.entity.SysRole;
import com.kaifangqian.modules.system.mapper.SysRoleMapper;
import com.kaifangqian.modules.system.mapper.SysUserRoleMapper;
import com.kaifangqian.modules.system.service.*;
import com.kaifangqian.modules.system.vo.SysAuthGroupRoleVO;
import com.kaifangqian.modules.system.vo.SysRoleUserVO;
import com.kaifangqian.modules.system.vo.UserAuthDataQueryVO;
import com.kaifangqian.common.constant.StatusConstant;
import com.kaifangqian.common.constant.enums.AuthType;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.modules.system.service.*;
import com.kaifangqian.utils.MyStringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private ISysAuthGroupMemberService iSysAuthGroupMemberService;
    @Autowired
    private ISysUserRoleService iSysUserRoleService;
    @Autowired
    private ISysAuthGroupRoleService iSysAuthGroupRoleService;
    @Autowired
    private ISysDepartService sysDepartService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatchRole(List<String> roleIds) {
        List<SysRole> roles = new ArrayList<>();
        LoginUser loginUser = MySecurityUtils.getCurrentUser();

        roleIds.forEach(i -> {
            SysRole role = new SysRole();
            role.setId(i);
            role.setDeleteFlag(true);
            role.setDeleteTime(new Date());
            role.setDeleteBy(loginUser.getId());

            roles.add(role);
        });
        //1.删除角色
        if (CollUtil.isNotEmpty(roles)) {
            this.updateBatchById(roles);
        }
        //2.删除角色和用户关系
        iSysUserRoleService.deleteByRoleIds(roleIds);

        //3.删除角色和权限关系
        iSysAuthGroupMemberService.deleteByTenantIdAndTypeAndAuthIds(loginUser.getTenantId(), AuthType.ROLE, roleIds);

        //4.删除权限组-角色关系
        iSysAuthGroupRoleService.deleteByRoleIds(roleIds);

        return true;
    }

    @Override
    public IPage<SysRoleUserVO> getUsersByRoleId(String roleId, Page<SysRoleUserVO> page) {
        LoginUser user = MySecurityUtils.getCurrentUser();
        List<String> departIds = sysDepartService.queryDepartIdsByUsername(user.getUsername());
        return sysUserRoleMapper.getUsersByRoleId(page, roleId, departIds, user.getTenantId());
    }

    @Override
    public void saveExt(SysRole sysRole) {
        if (MyStringUtils.isBlank(sysRole.getParentId())) {
            sysRole.setParentId("");
        }
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        sysRole.setTenantId(loginUser.getTenantId());
        this.save(sysRole);

        //保存权限组-角色关系
        SysAuthGroupRoleVO req = new SysAuthGroupRoleVO();
        req.setGroupIds(sysRole.getAuthGroupIds());
        req.setRoleId(sysRole.getId());
        iSysAuthGroupRoleService.addExt2(req);
    }

    @Override
    public SysRole getByIdExt(String id) {
        SysRole result = getById(id);
        List<SysAuthGroupRole> groupRoles = iSysAuthGroupRoleService.getByRoleId(id);
        if (CollUtil.isNotEmpty(groupRoles)) {
            result.setAuthGroupIds(groupRoles.stream().map(SysAuthGroupRole::getGroupId).collect(Collectors.toList()));
        }
        return result;
    }

    @Override
    public void updateExt(SysRole sysRole) {
        sysRole.setTenantId(null);

        this.updateById(sysRole);
        //删除原来的关系
        List<String> roleIds = new ArrayList<>();
        roleIds.add(sysRole.getId());
        iSysAuthGroupRoleService.deleteByRoleIds(roleIds);
        //保存权限组-角色关系
        SysAuthGroupRoleVO req = new SysAuthGroupRoleVO();
        req.setGroupIds(sysRole.getAuthGroupIds());
        req.setRoleId(sysRole.getId());
        iSysAuthGroupRoleService.addExt2(req);
    }

    @Override
    public List<SysRole> getByEntity(SysRole sysRole) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getDeleteFlag, StatusConstant.DEL_FLAG_0).eq(MyStringUtils.isNotBlank(sysRole.getTenantId()), SysRole::getTenantId, sysRole.getTenantId()).eq(MyStringUtils.isNotBlank(sysRole.getParentId()), SysRole::getParentId, sysRole.getParentId());
        return this.list(wrapper);
    }

    @Override
    public List<Tree<String>> getTreeList(List<SysRole> list) {
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        } else {
            List<Tree<String>> treeList = TreeUtil.build(list, "", (object, tree) -> {
                tree.setId(object.getId());
                tree.setParentId(object.getParentId());
                tree.setName(object.getRoleName());

                tree.putExtra("title", object.getRoleName());
                tree.putExtra("oprateFlag", true);
                tree.putExtra("type", "role");
            });
            return treeList;
        }
    }

    @Override
    public List<Tree<String>> getTreeListForSelect(List<SysRole> list) {
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        } else {
            List<Tree<String>> treeList = TreeUtil.build(list, "", (object, tree) -> {
                tree.setId(object.getId());
                tree.setParentId(object.getParentId());
                tree.setName(object.getRoleName());

                tree.putExtra("title", object.getRoleName());
                tree.putExtra("oprateFlag", object.isOprateFlag());
                tree.putExtra("type", "role");
            });
            return treeList;
        }
    }

    @Override
    public List<SysRole> getMyRoles() {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        UserAuthDataQueryVO query = new UserAuthDataQueryVO();
        query.setTenantId(loginUser.getTenantId());
        query.setUserId(loginUser.getId());
        query.setRoleIds(iSysUserRoleService.getMyRoleIds());
        query.setDepartId(loginUser.getDepartId());

        return sysRoleMapper.getMyRoles(query);
    }


    @Override
    public List<SysRole> getTenantRoles() {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(SysRole::getDeleteFlag, 0).eq(SysRole::getTenantId, loginUser.getTenantId());

        return this.list(queryWrapper);
    }

    @Override
    public List<SysRole> findSysRoleByRoleIds(List<String> roleIds) {
        if (CollUtil.isNotEmpty(roleIds)) {
            QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
            wrapper.lambda().in(ObjectUtils.isNotEmpty(roleIds), SysRole::getId, roleIds);

            return sysRoleMapper.selectList(wrapper);
        } else {
            return new ArrayList<>();
        }
    }
}
