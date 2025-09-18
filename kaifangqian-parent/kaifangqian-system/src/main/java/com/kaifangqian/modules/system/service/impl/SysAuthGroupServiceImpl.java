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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kaifangqian.modules.system.entity.SysAuthGroup;
import com.kaifangqian.modules.system.mapper.SysAuthGroupMapper;
import com.kaifangqian.common.constant.StatusConstant;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.modules.system.service.ISysAuthGroupService;
import com.kaifangqian.utils.MyStringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;
import java.util.List;

/**
 * @author zhenghuihan
 * @description 权限组表
 * @createTime 2022/9/2 18:16
 */
@Service
public class SysAuthGroupServiceImpl extends ServiceImpl<SysAuthGroupMapper, SysAuthGroup> implements ISysAuthGroupService {

    @Override
    public List<SysAuthGroup> listByEntity(SysAuthGroup sysAuthGroup) {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        LambdaQueryWrapper<SysAuthGroup> query = new LambdaQueryWrapper<SysAuthGroup>();
        query.eq(SysAuthGroup::getDeleteFlag, StatusConstant.DEL_FLAG_0)
                .eq(MyStringUtils.isNotBlank(sysAuthGroup.getParentId()), SysAuthGroup::getParentId, sysAuthGroup.getParentId())
                .eq(MyStringUtils.isBlank(sysAuthGroup.getParentId()), SysAuthGroup::getParentId, "")
                .eq(SysAuthGroup::getTenantId, loginUser.getTenantId())
                .like(MyStringUtils.isNotBlank(sysAuthGroup.getGroupName()), SysAuthGroup::getGroupName, sysAuthGroup.getGroupName());

        return this.list(query);
    }

    @Override
    public String saveExt(SysAuthGroup sysAuthGroup) {
        if (MyStringUtils.isBlank(sysAuthGroup.getParentId())) {
            sysAuthGroup.setParentId("");
        }
        this.save(sysAuthGroup);

        return sysAuthGroup.getId();
    }

    @Override
    public void deleteById(String id) {
        LoginUser user = MySecurityUtils.getCurrentUser();
        SysAuthGroup sysAuthGroup = new SysAuthGroup();
        sysAuthGroup.setId(id);
        sysAuthGroup.setDeleteFlag(true);
        sysAuthGroup.setDeleteBy(user.getUsername());
        sysAuthGroup.setDeleteTime(new Date());

        this.updateById(sysAuthGroup);
    }

    @Override
    public List<SysAuthGroup> getSystemGroup(String tenantId) {
        LambdaQueryWrapper<SysAuthGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MyStringUtils.isNotBlank(tenantId), SysAuthGroup::getTenantId, tenantId)
                .eq(SysAuthGroup::getSystemFlag, true)
                .ne(SysAuthGroup::getParentId, "");

        return this.list(queryWrapper);
    }

    @Override
    public List<SysAuthGroup> getSystemTypeGroups(Integer type) {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        LambdaQueryWrapper<SysAuthGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysAuthGroup::getGroupType, type)
                .eq(SysAuthGroup::getSystemFlag, true)
                .eq(SysAuthGroup::getTenantId, loginUser.getTenantId());

        return this.list(queryWrapper);
    }

    @Override
    public List<SysAuthGroup> getAllSystemTypeGroups(List<Integer> types) {
        LambdaQueryWrapper<SysAuthGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysAuthGroup::getGroupType, types);

        return this.list(queryWrapper);
    }
}
