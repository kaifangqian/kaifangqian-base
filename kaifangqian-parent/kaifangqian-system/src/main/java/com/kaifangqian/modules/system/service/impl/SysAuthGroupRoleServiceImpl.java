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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.modules.system.entity.SysAuthGroupRole;
import com.kaifangqian.modules.system.entity.SysRole;
import com.kaifangqian.modules.system.mapper.SysAuthGroupRoleMapper;
import com.kaifangqian.modules.system.vo.SysAuthGroupRoleVO;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.system.service.ISysAuthGroupRoleService;
import com.kaifangqian.modules.system.service.ISysRoleService;
import com.kaifangqian.utils.MyStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.powerjob.common.utils.CommonUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhenghuihan
 * @description 权限组-角色表
 * @createTime 2022/9/2 18:16
 */
@Service
public class SysAuthGroupRoleServiceImpl extends ServiceImpl<SysAuthGroupRoleMapper, SysAuthGroupRole> implements ISysAuthGroupRoleService {

    @Autowired
    private ISysRoleService iSysRoleService;
    @Resource
    private SysAuthGroupRoleMapper mapper;

    @Override
    public IPage<SysAuthGroupRoleVO> pageExt(String groupId, Page<SysAuthGroupRoleVO> page) {
        return mapper.pageExt(page, groupId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addExt(SysAuthGroupRoleVO req) {
        if (CollUtil.isNotEmpty(req.getRoleIds())) {
            req.getRoleIds().forEach(r -> {
                SysAuthGroupRole groupRole = new SysAuthGroupRole();
                groupRole.setGroupId(req.getGroupId());
                groupRole.setRoleId(r);

                if (!checkExit(groupRole)) {
                    this.save(groupRole);
                }
            });
        }
    }

    @Override
    public void addExt2(SysAuthGroupRoleVO req) {
        if (CollUtil.isNotEmpty(req.getGroupIds())) {
            req.getGroupIds().forEach(r -> {
                SysAuthGroupRole groupRole = new SysAuthGroupRole();
                groupRole.setGroupId(r);
                groupRole.setRoleId(req.getRoleId());

                if (!checkExit(groupRole)) {
                    this.save(groupRole);
                }
            });
        }
    }

    boolean checkExit(SysAuthGroupRole groupRole) {
        valid(groupRole);
        SysRole sysRole = iSysRoleService.getById(groupRole.getRoleId());
        if (sysRole == null || MyStringUtils.isBlank(sysRole.getParentId())) {
            throw new PaasException("该角色不能添加到权限组");
        }

        LambdaQueryWrapper<SysAuthGroupRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysAuthGroupRole::getRoleId, groupRole.getRoleId()).eq(SysAuthGroupRole::getGroupId, groupRole.getGroupId());

        List<SysAuthGroupRole> reslut = this.list(queryWrapper);
        if (CollUtil.isNotEmpty(reslut)) {
            return true;
        } else {
            return false;
        }
    }

    void valid(SysAuthGroupRole groupRole) {
        CommonUtils.requireNonNull(groupRole.getGroupId(), "权限组ID不能为空");
        CommonUtils.requireNonNull(groupRole.getRoleId(), "角色ID不能为空");
    }

    @Override
    public void deleteBatchExt(List<String> ids) {
        this.removeByIds(ids);
    }

    @Override
    public void deleteByRoleIds(List<String> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return;
        }
        LambdaQueryWrapper<SysAuthGroupRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysAuthGroupRole::getRoleId, roleIds);

        this.remove(wrapper);
    }

    @Override
    public List<SysAuthGroupRole> getByRoleId(String roleId) {
        LambdaQueryWrapper<SysAuthGroupRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysAuthGroupRole::getRoleId, roleId);

        return this.list(wrapper);
    }
}
