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
import com.kaifangqian.modules.system.entity.SysAuthGroup;
import com.kaifangqian.modules.system.entity.SysAuthGroupMember;
import com.kaifangqian.modules.system.entity.SysTenantUser;
import com.kaifangqian.modules.system.enums.AuthGroupType;
import com.kaifangqian.modules.system.mapper.SysAuthGroupMemberMapper;
import com.kaifangqian.modules.system.mapper.SysUserRoleMapper;
import com.kaifangqian.modules.system.service.ISysAuthGroupMemberService;
import com.kaifangqian.modules.system.service.ISysAuthGroupService;
import com.kaifangqian.modules.system.service.ISysTenantUserService;
import com.kaifangqian.modules.system.service.ISysUserRoleService;
import com.kaifangqian.modules.system.vo.*;
import com.kaifangqian.common.constant.enums.AuthType;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.service.*;
import com.kaifangqian.modules.system.vo.*;
import com.kaifangqian.utils.MyStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import tech.powerjob.common.utils.CommonUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhenghuihan
 * @description 权限组-成员表
 * @createTime 2022/9/2 18:13
 */
@Service
public class SysAuthGroupMemberServiceImpl extends ServiceImpl<SysAuthGroupMemberMapper, SysAuthGroupMember> implements ISysAuthGroupMemberService {

    @Resource
    private SysAuthGroupMemberMapper mapper;
    @Autowired
    private SysUserRoleMapper userRoleMapper;
    @Autowired
    private ISysAuthGroupService sysAuthGroupService;
    @Autowired
    private ISysTenantUserService sysTenantUserService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveExt(SysAuthGroupMemberList memberList) {
        if (CollUtil.isNotEmpty(memberList.getMemberList())) {
            LoginUser loginUser = MySecurityUtils.getCurrentUser();
            //新增数据
            memberList.getMemberList().forEach(m -> {
                m.setGroupId(memberList.getGroupId());
                if (MyStringUtils.isBlank(m.getDepartId())) {
                    m.setDepartId(null);
                }
                if (MyStringUtils.isBlank(memberList.getTenantId())) {
                    m.setTenantId(loginUser.getTenantId());
                } else {
                    m.setTenantId(memberList.getTenantId());
                }
                //校验空值
                valid(m);
                //校验是否存在
                if (!checkExit(m.getTenantId(), m.getGroupId(), m.getAuthType(), m.getAuthId())) {
                    this.save(m);
                }
            });
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveExt2(SysAuthGroupMemberList2 memberList) {
        if (CollUtil.isNotEmpty(memberList.getGroupIds())) {
            LoginUser loginUser = MySecurityUtils.getCurrentUser();
            memberList.getGroupIds().forEach(g -> {
                SysAuthGroupMember m = new SysAuthGroupMember();
                m.setAuthType(memberList.getAuthType());
                m.setAuthId(memberList.getAuthId());
                m.setGroupId(g);
                m.setTenantId(memberList.getTenantId());
                if (MyStringUtils.isBlank(memberList.getDepartId())) {
                    m.setDepartId(null);
                }
                if (MyStringUtils.isBlank(memberList.getTenantId())) {
                    m.setTenantId(loginUser.getTenantId());
                } else {
                    m.setTenantId(memberList.getTenantId());
                }

                //校验空值
                valid(m);
                //校验是否存在
                if (!checkExit(m.getTenantId(), m.getGroupId(), m.getAuthType(), m.getAuthId())) {
                    this.save(m);
                }
            });
        }
    }

    boolean checkExit(String tenantId, String groupId, String authType, String authId) {
        LambdaQueryWrapper<SysAuthGroupMember> wrapper = new LambdaQueryWrapper<SysAuthGroupMember>();
        wrapper.eq(MyStringUtils.isNotBlank(tenantId), SysAuthGroupMember::getTenantId, tenantId).eq(MyStringUtils.isNotBlank(groupId), SysAuthGroupMember::getGroupId, groupId).eq(MyStringUtils.isNotBlank(authType), SysAuthGroupMember::getAuthType, authType).eq(MyStringUtils.isNotBlank(authId), SysAuthGroupMember::getAuthId, authId);
        List<SysAuthGroupMember> result = this.list(wrapper);
        if (CollUtil.isNotEmpty(result)) {
            return true;
        } else {
            return false;
        }
    }

    private void valid(SysAuthGroupMember m) {
        CommonUtils.requireNonNull(m.getGroupId(), "权限组不能为空");
        CommonUtils.requireNonNull(m.getAuthType(), "权限类型不能为空");
        CommonUtils.requireNonNull(m.getAuthId(), "权限ID不能为空");
    }

    @Override
    public void removeExt(List<String> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            ids.forEach(i -> {
                checkRemove(i);
            });
        }
        this.removeByIds(ids);
    }

    void checkRemove(String id) {
        SysAuthGroupMember groupMember = this.getById(id);
        if (groupMember != null && groupMember.getAuthType().equals("user")) {
            SysAuthGroup authGroup = sysAuthGroupService.getById(groupMember.getGroupId());
            if (authGroup != null && authGroup.getGroupType().equals(AuthGroupType.ALL.getType()) && authGroup.getSystemFlag()) {
                SysTenantUser tenantUser = sysTenantUserService.getTenantUser(authGroup.getTenantId(), groupMember.getAuthId());
                if (tenantUser != null && tenantUser.getAddType().equals("system")) {
                    throw new PaasException("系统管理员不可以从全部权限中移除");
                }
            }
        }
    }

    @Override
    public IPage<SysAuthGroupMemberVO> pageList(Page<SysAuthGroupMemberVO> page, SysAuthGroupMember query) {
        if (MyStringUtils.isBlank(query.getGroupId())) {
            throw new PaasException("权限组ID不能为空");
        }
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        query.setTenantId(loginUser.getTenantId());
        return mapper.pageList(page, query);
    }

    @Override
    public IPage<SysAuthGroupMemberVO> listByAuthId(Page<SysAuthGroupMemberVO> page, SysAuthGroupMember query) {
        if (MyStringUtils.isBlank(query.getAuthId())) {
            throw new PaasException("权限ID不能为空");
        }
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        query.setTenantId(loginUser.getTenantId());

        return mapper.listByAuthId(page, query);
    }

    @Override
    public void deleteByTenantIdAndTypeAndAuthIds(String tennatId, AuthType type, List<String> authIds) {
        LambdaQueryWrapper<SysAuthGroupMember> wrapper = new LambdaQueryWrapper<SysAuthGroupMember>();
        wrapper.eq(SysAuthGroupMember::getAuthType, type.getType()).eq(SysAuthGroupMember::getTenantId, tennatId).in(CollUtil.isNotEmpty(authIds), SysAuthGroupMember::getAuthId, authIds);

        this.remove(wrapper);
    }

    @Override
    public List<UserAuthGroupVO> getLoginUserGroups() {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        UserAuthDataQueryVO query = new UserAuthDataQueryVO();
        query.setUserId(loginUser.getId());
        query.setRoleIds(userRoleMapper.getRoleIdsByUser(loginUser.getTenantId(), loginUser.getId()));
        query.setDepartId(loginUser.getDepartId());
        query.setTenantId(loginUser.getTenantId());

        return mapper.getLoginUserGroups(query);
    }
}
