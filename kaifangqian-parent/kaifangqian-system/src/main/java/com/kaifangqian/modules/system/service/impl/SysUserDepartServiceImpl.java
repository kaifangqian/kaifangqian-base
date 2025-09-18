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
import com.kaifangqian.modules.system.entity.SysDepart;
import com.kaifangqian.modules.system.entity.SysUserDepart;
import com.kaifangqian.modules.system.mapper.SysUserDepartMapper;
import com.kaifangqian.modules.system.vo.SysDepartRoleVO;
import com.kaifangqian.modules.system.vo.SysDepartUserVO;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.vo.TenantUserInfo;
import com.kaifangqian.modules.system.service.ISysUserDepartService;
import com.kaifangqian.utils.MyStringUtils;
import org.springframework.stereotype.Service;
import tech.powerjob.common.utils.CommonUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户部门表实现类
 */
@Service
public class SysUserDepartServiceImpl extends ServiceImpl<SysUserDepartMapper, SysUserDepart> implements ISysUserDepartService {

    @Resource
    private SysUserDepartMapper mapper;


    @Override
    public void addExt(SysUserDepart sysUserDepart) {
        valid(sysUserDepart.getDepartId(), sysUserDepart.getUserId());
        if (checkAddOrUpdateUserDepart(sysUserDepart)) {
            //存在，更新
            this.updateById(sysUserDepart);
        } else {
            //不存在，新增
            this.save(sysUserDepart);
        }
    }

    boolean checkAddOrUpdateUserDepart(SysUserDepart sysUserDepart) {
        LambdaQueryWrapper<SysUserDepart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserDepart::getDepartId, sysUserDepart.getDepartId())
                .eq(SysUserDepart::getUserId, sysUserDepart.getUserId())
                .eq(SysUserDepart::getTenantId, sysUserDepart.getTenantId());

        List<SysUserDepart> result = this.list(wrapper);
        if (CollUtil.isNotEmpty(result)) {
            sysUserDepart.setId(result.get(0).getId());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void deleteByDepartIdAndUserId(String departId, String userId) {
        valid(departId, userId);
        LambdaQueryWrapper<SysUserDepart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserDepart::getDepartId, departId)
                .eq(SysUserDepart::getUserId, userId);

        this.remove(wrapper);
    }

    @Override
    public void deleteByTenantIdAndUserId(String tenantId, String userId) {
        valid("departId", userId);
        LambdaQueryWrapper<SysUserDepart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserDepart::getTenantId, tenantId)
                .eq(SysUserDepart::getUserId, userId);

        this.remove(wrapper);
    }

    void valid(String departId, String userId) {
        CommonUtils.requireNonNull(departId, "部门ID不能为空");
        CommonUtils.requireNonNull(userId, "用户ID不能为空");
    }

    @Override
    public void addUserByDepartId(String tennatId, String departId, List<String> userIds) {
        //新增普通用户
        if (CollUtil.isNotEmpty(userIds)) {
            List<SysUserDepart> list = new ArrayList<>();
            userIds.forEach(i -> {
                SysUserDepart userDepart = new SysUserDepart();
                userDepart.setDepartId(departId);
                userDepart.setTenantId(tennatId);
                userDepart.setManageFlag(false);
                userDepart.setUserId(i);

                list.add(userDepart);
            });

            this.saveBatch(list);
        }
    }

    @Override
    public void updateManageByDepartId(String departId, List<String> userIds) {
        //查询当前部门下的所有用户
        LambdaQueryWrapper<SysUserDepart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserDepart::getDepartId, departId);
        List<SysUserDepart> sysUserDeparts = this.baseMapper.selectList(queryWrapper);

        if (CollUtil.isEmpty(userIds)) {
            userIds = new ArrayList<>();
        }
        //更新历史数据
        if (CollUtil.isNotEmpty(sysUserDeparts)) {
            for (SysUserDepart d : sysUserDeparts) {
                if (userIds.contains(d.getUserId())) {
                    d.setManageFlag(true);
                    userIds.remove(d.getUserId());
                } else {
                    d.setManageFlag(false);
                }
            }

            this.updateBatchById(sysUserDeparts);
        }

        //新增主管用户
        if (CollUtil.isNotEmpty(userIds)) {
            List<SysUserDepart> list = new ArrayList<>();
            userIds.forEach(i -> {
                SysUserDepart userDepart = new SysUserDepart();
                userDepart.setDepartId(departId);
                userDepart.setManageFlag(true);
                userDepart.setUserId(i);

                list.add(userDepart);
            });

            this.saveBatch(list);
        }
    }

    @Override
    public List<SysUserDepart> getByEntity(SysUserDepart query) {
        LambdaQueryWrapper<SysUserDepart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MyStringUtils.isNotBlank(query.getDepartId()), SysUserDepart::getDepartId, query.getDepartId())
                .eq(query.getManageFlag() != null, SysUserDepart::getManageFlag, query.getManageFlag())
                .eq(MyStringUtils.isNotBlank(query.getUserId()), SysUserDepart::getUserId, query.getUserId())
                .eq(MyStringUtils.isNotBlank(query.getTenantId()), SysUserDepart::getTenantId, query.getTenantId());
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<SysDepart> getDepartsByUserId(String userId) {

        return mapper.getDepartsByUserId(userId);
    }

    @Override
    public void deleteByTenantIdAndUserIds(String tenantId, List<String> userIds) {
        if (CollUtil.isNotEmpty(userIds)) {
            LambdaQueryWrapper<SysUserDepart> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(SysUserDepart::getUserId, userIds)
                    .eq(SysUserDepart::getTenantId, tenantId);
            this.remove(wrapper);
        }
    }

    /**
     * 根据部门id查询用户信息
     */
    @Override
    public IPage<SysDepartUserVO> queryUserByDepId(String depId, Page<SysDepartUserVO> page) {
        LoginUser user = MySecurityUtils.getCurrentUser();
        return mapper.queryUserByDepId(page, depId, user.getTenantId());
    }

    /**
     * 根据部门id查询用户信息
     */
    @Override
    public IPage<SysDepartUserVO> getUserListByDepartId(String depId, Page<SysDepartUserVO> page) {
        LoginUser user = MySecurityUtils.getCurrentUser();
        return mapper.getUserListByDepartId(page, depId, user.getTenantId());
    }

    @Override
    public List<SysDepartRoleVO> getUserRolesByDepartId(String depId) {
        LoginUser user = MySecurityUtils.getCurrentUser();
        return mapper.getUserRolesByDepartId(depId, user.getTenantId());
    }

    @Override
    public List<SysUserDepart> getUserIdsByDeptIds(List<String> deptIds) {
        LambdaQueryWrapper<SysUserDepart> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysUserDepart::getDepartId, deptIds);
        return list(wrapper);
    }

    @Override
    public List<TenantUserInfo> getTenantUsersByDeptIds(List<String> departIds) {
        return mapper.getTenantUsersByDeptIds(departIds);
    }

    @Override
    public boolean checkUserDepart(String userId, String tenantId, String departId) {
        List<SysUserDepart> list = mapper.getUserDepartByUserIdAndDepartId(userId, tenantId, departId);
        if (CollUtil.isNotEmpty(list)) {
            return true;
        }
        return false;
    }
}
