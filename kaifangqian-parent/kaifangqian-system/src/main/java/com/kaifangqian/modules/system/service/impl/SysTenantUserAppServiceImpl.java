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
import com.kaifangqian.modules.system.entity.SysTenantUserApp;
import com.kaifangqian.modules.system.mapper.SysTenantUserAppMapper;
import com.kaifangqian.modules.system.service.ISysTenantUserAppService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/**
 * @author zhenghuihan
 * @description 租户用户关系管理服务类
 * @createTime 2022/9/2 18:13
 */
@Service
public class SysTenantUserAppServiceImpl extends ServiceImpl<SysTenantUserAppMapper, SysTenantUserApp> implements ISysTenantUserAppService {

    @Resource
    private SysTenantUserAppMapper mapper;

    @Override
    public boolean checkUserTenantApp(String userId, String tenantId, String appCode) {
        List<SysTenantUserApp> list = mapper.getAppIdsByUser(userId, tenantId, appCode);
        if (CollUtil.isNotEmpty(list)) {
            return true;
        }
        return false;
    }

    @Override
    public List<SysAppInfo> queryUserApps(String userId, String tenantId) {
        return mapper.queryUserApps(userId, tenantId);
    }

    @Override
    public String saveExt(SysTenantUserApp sysTenantUserApp) {
        this.save(sysTenantUserApp);
        return sysTenantUserApp.getId();
    }

    @Override
    public void deleteByTenantIdUserIds(String tenantId, List<String> userIds) {
        LambdaQueryWrapper<SysTenantUserApp> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysTenantUserApp::getTenantId, tenantId)
                .in(SysTenantUserApp::getUserId, userIds);

        this.remove(queryWrapper);
    }

    @Override
    public List<String> getByTenantAndAppId(String tenantId, String appId) {
        LambdaQueryWrapper<SysTenantUserApp> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysTenantUserApp::getTenantId, tenantId)
                .eq(SysTenantUserApp::getAppId, appId);
        List<SysTenantUserApp> list = this.list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            return list.stream().map(SysTenantUserApp::getUserId).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public void deleteByTenantAndAppId(String tenantId, String appId) {
        LambdaQueryWrapper<SysTenantUserApp> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysTenantUserApp::getTenantId, tenantId)
                .eq(SysTenantUserApp::getAppId, appId);

        this.remove(queryWrapper);
    }
}
