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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.modules.system.entity.SysRole;
import com.kaifangqian.modules.system.entity.SysUserRole;
import com.kaifangqian.modules.system.mapper.SysUserRoleMapper;
import com.kaifangqian.modules.system.vo.SysUserRoleVO;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.vo.TenantUserInfo;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.system.service.ISysRoleService;
import com.kaifangqian.modules.system.service.ISysUserRoleService;
import com.kaifangqian.utils.MyStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import tech.powerjob.common.utils.CommonUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private ISysRoleService iSysRoleService;


    @Override
    public List<SysUserRole> queryByRoleIds(List<String> roleIds) {
        if (roleIds != null && roleIds.size() > 0) {
            LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(SysUserRole::getRoleId, roleIds);
            List<SysUserRole> userRoles = sysUserRoleMapper.selectList(queryWrapper);
            return userRoles;
        }
        return null;
    }

    @Override
    public List<SysUserRole> queryByUserIdAndTenantId(String userId, String tenantId) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, userId)
                .eq(SysUserRole::getTenantId, tenantId);

        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(queryWrapper);
        return userRoles;
    }

    @Override
    public List<SysRole> getRolesByUserId(String userId) {
        return sysUserRoleMapper.getRolesByUserId(userId);
    }

    @Override
    public void saveExt(SysUserRoleVO sysUserRoleVO) {
        if (CollUtil.isNotEmpty(sysUserRoleVO.getUserIds()) && MyStringUtils.isNotBlank(sysUserRoleVO.getRoleId())) {
            LoginUser loginUser = MySecurityUtils.getCurrentUser();
            List<SysUserRole> userRoles = new ArrayList<>();
            sysUserRoleVO.getUserIds().forEach(u -> {
                SysUserRole userRole = new SysUserRole();
                userRole.setRoleId(sysUserRoleVO.getRoleId());
                userRole.setUserId(u);
                userRole.setTenantId(loginUser.getTenantId());

                //校验空
                valid(userRole);
                //校验是否存在
                if (!checkExit(userRole.getRoleId(), userRole.getUserId(), userRole.getTenantId())) {
                    userRoles.add(userRole);
                }
            });
            this.saveBatch(userRoles);
        }
    }

    boolean checkExit(String roleId, String userId, String tenantId) {
        SysRole sysRole = iSysRoleService.getById(roleId);
        if (sysRole == null || MyStringUtils.isBlank(sysRole.getParentId())) {
            throw new PaasException("该角色不可以绑定用户");
        }
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MyStringUtils.isNotBlank(roleId), SysUserRole::getRoleId, roleId)
                .eq(MyStringUtils.isNotBlank(userId), SysUserRole::getUserId, userId)
                .eq(MyStringUtils.isNotBlank(tenantId), SysUserRole::getTenantId, tenantId);

        List<SysUserRole> reslut = this.list(queryWrapper);
        if (CollUtil.isNotEmpty(reslut)) {
            return true;
        } else {
            return false;
        }
    }

    void valid(SysUserRole userRole) {
        CommonUtils.requireNonNull(userRole.getRoleId(), "角色ID不能为空");
        CommonUtils.requireNonNull(userRole.getUserId(), "用户ID不能为空");
    }

    @Override
    public void deleteByTenantIdAndUserIds(String tenantId, List<String> userIds) {
        if (CollUtil.isNotEmpty(userIds)) {
            LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(SysUserRole::getUserId, userIds)
                    .eq(SysUserRole::getTenantId, tenantId);
            this.remove(wrapper);
        }
    }

    @Override
    public void deleteByRoleIds(List<String> roleIds) {
        if (CollUtil.isNotEmpty(roleIds)) {
            LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(SysUserRole::getRoleId, roleIds);
            this.remove(wrapper);
        }
    }

    @Override
    public List<String> getMyRoleIds() {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();

        return sysUserRoleMapper.getRoleIdsByUser(loginUser.getTenantId(), loginUser.getId());
    }

    @Override
    public List<TenantUserInfo> getTenantUsersByRoleIds(List<String> roleIds) {
        return sysUserRoleMapper.getTenantUsersByRoleIds(roleIds);
    }
}
