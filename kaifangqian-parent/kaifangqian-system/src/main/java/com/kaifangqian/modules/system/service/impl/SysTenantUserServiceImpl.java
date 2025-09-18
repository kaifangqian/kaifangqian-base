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
import com.kaifangqian.modules.system.entity.SysTenantInfo;
import com.kaifangqian.modules.system.entity.SysTenantUser;
import com.kaifangqian.modules.system.entity.SysUser;
import com.kaifangqian.modules.system.enums.TenantStatus;
import com.kaifangqian.modules.system.enums.TenantType;
import com.kaifangqian.modules.system.mapper.SysTenantUserMapper;
import com.kaifangqian.modules.system.vo.JionRefuseTenantVO;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.vo.TenantUserInfo;
import com.kaifangqian.modules.system.service.ISysTenantInfoService;
import com.kaifangqian.modules.system.service.ISysTenantUserService;
import com.kaifangqian.modules.system.service.ISysUserService;
import com.kaifangqian.utils.MyStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import tech.powerjob.common.utils.CommonUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * @author zhenghuihan
 * @description 租户用户关系服务类
 * @createTime 2022/9/2 18:13
 */
@Service
public class SysTenantUserServiceImpl extends ServiceImpl<SysTenantUserMapper, SysTenantUser> implements ISysTenantUserService {

    @Resource
    private SysTenantUserMapper mapper;
    @Autowired
    private ISysTenantInfoService iSysTenantInfoService;
    @Autowired
    private ISysUserService iSysUserService;

    @Override
    public boolean checkUserTenant(String userId, String tenantId) {
        List<SysTenantUser> list = mapper.getUserTenantByUserIdAndTenantId(userId, tenantId);
        if (CollUtil.isNotEmpty(list)) {
            return true;
        }
        return false;
    }

    @Override
    public List<SysTenantInfo> getTenantsByUserId(String userId) {
        LambdaQueryWrapper<SysTenantUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysTenantUser::getUserId, userId).eq(SysTenantUser::getDeleteFlag, 0).and(q -> q.eq(SysTenantUser::getStatus, 1).or().eq(SysTenantUser::getStatus, 2));
        List<SysTenantUser> list = this.list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            Map<String, SysTenantUser> tenantUserMap = list.stream().collect(Collectors.toMap(SysTenantUser::getTenantId, Function.identity(), (i, j) -> i));
            List<String> tanantIds = list.stream().map(SysTenantUser::getTenantId).collect(Collectors.toList());

            List<SysTenantInfo> tenantInfos = iSysTenantInfoService.getByIds(tanantIds);
            if (CollUtil.isNotEmpty(tenantInfos)) {
                tenantInfos.forEach(t -> {
                    if (tenantUserMap.get(t.getId()).getStatus() == 2) {
                        t.setTenantStatus(TenantStatus.DISABLE.getType());
                    }
                });
            }
            return tenantInfos;
        }

        return new ArrayList<>();
    }

    @Override
    public List<SysTenantInfo> getTenantsByUserIdAndAppId(String userId, String appId) {
        List<SysTenantUser> list = mapper.getTenantsByUserIdAndAppId(userId, appId);
        if (CollUtil.isNotEmpty(list)) {
            Map<String, SysTenantUser> tenantUserMap = list.stream().collect(Collectors.toMap(SysTenantUser::getTenantId, Function.identity(), (i, j) -> i));
            List<String> tanantIds = list.stream().map(SysTenantUser::getTenantId).collect(Collectors.toList());

            List<SysTenantInfo> tenantInfos = iSysTenantInfoService.getByIds(tanantIds);
            if (CollUtil.isNotEmpty(tenantInfos)) {
                tenantInfos.forEach(t -> {
                    if (tenantUserMap.get(t.getId()).getStatus() == 2) {
                        t.setTenantStatus(TenantStatus.DISABLE.getType());
                    }
                });
            }
            return tenantInfos;
        }

        return new ArrayList<>();
    }

    @Override
    public SysTenantUser getTenantUser(String tenantId, String userId) {
        LambdaQueryWrapper<SysTenantUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MyStringUtils.isNotBlank(tenantId), SysTenantUser::getTenantId, tenantId).eq(MyStringUtils.isNotBlank(userId), SysTenantUser::getUserId, userId).eq(SysTenantUser::getDeleteFlag, 0);

        List<SysTenantUser> list = list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }

        return null;
    }

    @Override
    public SysTenantUser getActiviTenantUser(String tenantId, String userId) {
        List<Integer> status = new ArrayList<>();
        status.add(1);
        status.add(2);
        LambdaQueryWrapper<SysTenantUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MyStringUtils.isNotBlank(tenantId), SysTenantUser::getTenantId, tenantId)
                .eq(MyStringUtils.isNotBlank(userId), SysTenantUser::getUserId, userId)
                .in(SysTenantUser::getStatus, status)
                .eq(SysTenantUser::getDeleteFlag, 0);

        List<SysTenantUser> list = list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }

        return null;
    }

    @Override
    public List<SysTenantUser> getByIds(List<String> ids) {
        LambdaQueryWrapper<SysTenantUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysTenantUser::getId, ids);

        return this.list(queryWrapper);
    }

    @Override
    public List<SysTenantUser> getTenantUsers(String name) {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        LambdaQueryWrapper<SysTenantUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysTenantUser::getTenantId, loginUser.getTenantId()).like(MyStringUtils.isNotBlank(name), SysTenantUser::getNickName, name.trim()).gt(SysTenantUser::getStatus, 0);

        return this.list(queryWrapper);
    }

    @Override
    public List<JionRefuseTenantVO> getInviteList() {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        List<JionRefuseTenantVO> result = new ArrayList<>();
        LambdaQueryWrapper<SysTenantUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysTenantUser::getUserId, loginUser.getId()).eq(SysTenantUser::getStatus, 0).eq(SysTenantUser::getDeleteFlag, 0);

        List<SysTenantUser> tenantUsers = this.list(queryWrapper);
        if (CollUtil.isNotEmpty(tenantUsers)) {
            List<String> tenantIds = tenantUsers.stream().map(SysTenantUser::getTenantId).collect(Collectors.toList());
            List<SysTenantInfo> tenantInfos = iSysTenantInfoService.getByIds(tenantIds);
            Map<String, SysTenantInfo> tenantInfoMap = tenantInfos.stream().collect(Collectors.toMap(SysTenantInfo::getId, Function.identity(), (i, j) -> i));
            tenantUsers.forEach(t -> {
                JionRefuseTenantVO vo = new JionRefuseTenantVO();
                vo.setId(t.getId());
                vo.setTanantName(tenantInfoMap.get(t.getId()) == null ? null : tenantInfoMap.get(t.getId()).getTenantName());

                result.add(vo);
            });
        }

        return result;
    }

    @Override
    public void jionOrRefuseTenant(JionRefuseTenantVO vo) {
        ckeckDate(vo);
        SysTenantUser sysTenantUser = this.getById(vo.getId());
        if (sysTenantUser != null && sysTenantUser.getStatus() == 0) {
            LoginUser loginUser = MySecurityUtils.getCurrentUser();
            if (sysTenantUser.getUserId().equals(loginUser.getId())) {
                if (vo.getStatus() == -1 || vo.getStatus() == 1) {
                    sysTenantUser.setStatus(vo.getStatus());

                    this.updateById(sysTenantUser);
                    if (vo.getStatus() == 1) {
                        iSysUserService.intiAuthOnTenant(sysTenantUser.getUserId(), sysTenantUser.getTenantId());
                    }
                }
            }
        }
    }

    @Override
    public boolean checkPersonalTenant(String userId) {
        boolean flag = false;
        List<SysTenantInfo> tenantInfos = getTenantsByUserId(userId);
        if (CollUtil.isNotEmpty(tenantInfos)) {
            for (SysTenantInfo t : tenantInfos) {
                if (TenantType.PERSONAL.getType().equals(t.getTenantType())) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    @Override
    public List<TenantUserInfo> getAllTenantUsers() {
        return mapper.getAllTenantUsers();
    }

    @Override
    public SysUser getTenantSysUser(String tenantUserId) {
        SysTenantUser tenantUser = getById(tenantUserId);
        return iSysUserService.getById(tenantUser.getUserId());
    }

    @Override
    public SysTenantUser getPersonalTenantUser(String userId) {
        return mapper.getPersonalTenantUser(userId);
    }

    @Override
    public List<SysTenantUser> getTenantSystemUsers() {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        LambdaQueryWrapper<SysTenantUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysTenantUser::getTenantId, loginUser.getTenantId()).eq(SysTenantUser::getDeleteFlag, false).eq(SysTenantUser::getAddType, "system");

        return list(queryWrapper);
    }

    @Override
    public List<SysTenantUser> getTenantSystemUsersByTenantId(String companyTenantId) {
        LambdaQueryWrapper<SysTenantUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysTenantUser::getTenantId, companyTenantId).eq(SysTenantUser::getDeleteFlag, false).eq(SysTenantUser::getAddType, "system");

        return list(queryWrapper);
    }


    void ckeckDate(JionRefuseTenantVO vo) {
        CommonUtils.requireNonNull(vo.getId(), "id不能为空");
        CommonUtils.requireNonNull(vo.getStatus(), "状态不能为空");
    }
}
