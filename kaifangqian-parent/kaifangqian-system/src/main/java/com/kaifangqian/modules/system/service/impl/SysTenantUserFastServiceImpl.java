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
import com.kaifangqian.modules.system.entity.SysAppInfo;
import com.kaifangqian.modules.system.entity.SysPermission;
import com.kaifangqian.modules.system.entity.SysTenantUserFast;
import com.kaifangqian.modules.system.mapper.SysTenantUserFastMapper;
import com.kaifangqian.modules.system.vo.SysTenantUserFastVO;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.service.ISysAppInfoService;
import com.kaifangqian.modules.system.service.ISysPermissionService;
import com.kaifangqian.modules.system.service.ISysTenantUserFastService;
import com.kaifangqian.utils.MyStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * @author zhenghuihan
 * @description 租户用户快捷操作服务类
 * @createTime 2022/9/2 18:13
 */
@Service
public class SysTenantUserFastServiceImpl extends ServiceImpl<SysTenantUserFastMapper, SysTenantUserFast> implements ISysTenantUserFastService {

    @Autowired
    private ISysAppInfoService iSysAppInfoService;
    @Autowired
    private ISysPermissionService iSysPermissionService;

    @Override
    public List<SysTenantUserFastVO> myFast() {
        List<SysTenantUserFastVO> list = new ArrayList<>();
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        LambdaQueryWrapper<SysTenantUserFast> queryWrapper = new LambdaQueryWrapper<SysTenantUserFast>();
        queryWrapper.eq(SysTenantUserFast::getTenantId, loginUser.getTenantId())
                .eq(SysTenantUserFast::getUserId, loginUser.getId());
        List<SysTenantUserFast> sysTenantUserFasts = this.list(queryWrapper);
        if (CollUtil.isNotEmpty(sysTenantUserFasts)) {
            List<String> appIds = sysTenantUserFasts.stream().map(SysTenantUserFast::getAppId).collect(Collectors.toList());
            List<SysAppInfo> sysAppInfos = iSysAppInfoService.listByIds(appIds);
            Map<String, SysAppInfo> appInfoMap = new HashMap<>();
            if (CollUtil.isNotEmpty(sysAppInfos)) {
                appInfoMap = sysAppInfos.stream().collect(Collectors.toMap(SysAppInfo::getId, Function.identity(), (i, j) -> i));
            }
            List<String> permissionIds = sysTenantUserFasts.stream().map(SysTenantUserFast::getPermissionId).collect(Collectors.toList());
            List<SysPermission> sysPermissions = iSysPermissionService.listByIds(permissionIds);
            Map<String, SysPermission> permissionMap = new HashMap<>();
            if (CollUtil.isNotEmpty(sysPermissions)) {
                permissionMap = sysPermissions.stream().collect(Collectors.toMap(SysPermission::getId, Function.identity(), (i, j) -> i));
            }
            for (SysTenantUserFast f : sysTenantUserFasts) {
                SysTenantUserFastVO vo = new SysTenantUserFastVO();
                vo.setAppId(f.getAppId());
                vo.setAppName(appInfoMap.get(f.getAppId()) != null ? appInfoMap.get(f.getAppId()).getAppName() : "");
                vo.setAppCode(appInfoMap.get(f.getAppId()) != null ? appInfoMap.get(f.getAppId()).getAppCode() : "");
                vo.setAppAddress(appInfoMap.get(f.getAppId()) != null ? appInfoMap.get(f.getAppId()).getAppAddress() : "");
                vo.setPermissionId(f.getPermissionId());
                vo.setPermissionName(permissionMap.get(f.getPermissionId()) != null ? permissionMap.get(f.getPermissionId()).getName() : "");
                vo.setPath(permissionMap.get(f.getPermissionId()) != null ? permissionMap.get(f.getPermissionId()).getPath() : "");
                vo.setFastIcon(permissionMap.get(f.getPermissionId()) != null ? permissionMap.get(f.getPermissionId()).getFastIcon() : "");
                vo.setJoinId(f.getJoinId());

                list.add(vo);
            }
        }
        return list;
    }

    @Override
    public void updateExt(List<SysTenantUserFastVO> sysTenantUserFastVOS) {
        //删除历史数据
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        LambdaQueryWrapper<SysTenantUserFast> deleteQuery = new LambdaQueryWrapper<SysTenantUserFast>();
        deleteQuery.eq(SysTenantUserFast::getTenantId, loginUser.getTenantId())
                .eq(SysTenantUserFast::getUserId, loginUser.getId());

        this.remove(deleteQuery);
        //新增数据
        if (CollUtil.isNotEmpty(sysTenantUserFastVOS)) {
            List<SysTenantUserFast> list = new ArrayList<>();
            sysTenantUserFastVOS.forEach(f -> {
                if (MyStringUtils.isBlank(f.getAppId()) || MyStringUtils.isBlank(f.getPermissionId())) {
                    throw new PaasException("传值错误");
                }
                SysTenantUserFast sysTenantUserFast = new SysTenantUserFast();
                sysTenantUserFast.setTenantId(loginUser.getTenantId());
                sysTenantUserFast.setUserId(loginUser.getId());
                sysTenantUserFast.setAppId(f.getAppId());
                sysTenantUserFast.setPermissionId(f.getPermissionId());
                sysTenantUserFast.setJoinId(f.getJoinId());

                list.add(sysTenantUserFast);
            });
            this.saveBatch(list);
        }
    }
}
