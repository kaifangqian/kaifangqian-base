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
import com.kaifangqian.modules.system.entity.SysTenantAppVersion;
import com.kaifangqian.modules.system.entity.SysTenantUser;
import com.kaifangqian.modules.system.entity.SysTenantUserApp;
import com.kaifangqian.modules.system.enums.TenantAppStatus;
import com.kaifangqian.modules.system.mapper.SysTenantAppVersionMapper;
import com.kaifangqian.modules.system.vo.NameAndIdForView;
import com.kaifangqian.modules.system.vo.SysTenantAppVersionVO;
import com.kaifangqian.modules.system.vo.TenantUserAppReq;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.system.service.ISysTenantAppVersionService;
import com.kaifangqian.modules.system.service.ISysTenantUserAppService;
import com.kaifangqian.modules.system.service.ISysTenantUserService;
import com.kaifangqian.utils.MyStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
/**
 * @author zhenghuihan
 * @description 租户应用服务类
 * @createTime 2022/9/2 18:13
 */
@Service
public class SysTenantAppVersionServiceImpl extends ServiceImpl<SysTenantAppVersionMapper, SysTenantAppVersion> implements ISysTenantAppVersionService {

    @Resource
    private SysTenantAppVersionMapper mapper;
    @Autowired
    private ISysTenantUserAppService iSysTenantUserAppService;
    @Autowired
    private ISysTenantUserService sysTenantUserService;

    @Override
    public SysTenantAppVersion getByTenantAndApp(String tenantId, String appId) {
        LambdaQueryWrapper<SysTenantAppVersion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysTenantAppVersion::getTenantId, tenantId);
        queryWrapper.eq(SysTenantAppVersion::getAppId, appId);
        queryWrapper.eq(SysTenantAppVersion::getDeleteFlag, 0);
        queryWrapper.eq(SysTenantAppVersion::getStatus, 1);

        return this.getOne(queryWrapper);
    }

    @Override
    public String saveExt(SysTenantAppVersion sysTenantAppVersion) {
        sysTenantAppVersion.setStatus(TenantAppStatus.ENABLE.getType());

        this.save(sysTenantAppVersion);

        return sysTenantAppVersion.getId();
    }

    @Override
    public List<SysTenantAppVersion> getByTenantId(String tenantId) {
        LambdaQueryWrapper<SysTenantAppVersion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysTenantAppVersion::getTenantId, tenantId);
        queryWrapper.eq(SysTenantAppVersion::getDeleteFlag, 0);

        return this.list(queryWrapper);
    }

    @Override
    public IPage<SysTenantAppVersionVO> pageExt(Page<SysTenantAppVersionVO> page, TenantUserAppReq req) {
        LoginUser user = MySecurityUtils.getCurrentUser();
        req.setTenantId(user.getTenantId());

        return mapper.pageExt(page, req);
    }

    @Override
    public void updateStatus(String id) {
        if (MyStringUtils.isNotBlank(id)) {
            SysTenantAppVersion tenantAppVersion = this.getById(id);
            if (tenantAppVersion != null) {
                if (TenantAppStatus.DISABLE.getType().equals(tenantAppVersion.getStatus())) {
                    throw new PaasException("该应用已经被平台停用");
                } else if (TenantAppStatus.ENABLE.getType().equals(tenantAppVersion.getStatus())) {
                    tenantAppVersion.setStatus(TenantAppStatus.STOP.getType());
                } else if (TenantAppStatus.STOP.getType().equals(tenantAppVersion.getStatus())) {
                    tenantAppVersion.setStatus(TenantAppStatus.ENABLE.getType());
                }

                this.updateById(tenantAppVersion);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUseful(SysTenantAppVersionVO sysTenantAppVersionVO) {
        if (MyStringUtils.isNotBlank(sysTenantAppVersionVO.getId())) {
            SysTenantAppVersion tenantAppVersion = this.getById(sysTenantAppVersionVO.getId());
            if (sysTenantAppVersionVO.getUseful() != null) {
                tenantAppVersion.setUseful(sysTenantAppVersionVO.getUseful());

                this.updateById(tenantAppVersion);
                if (!sysTenantAppVersionVO.getUseful()) {
                    updateTenantAppUsers(sysTenantAppVersionVO);
                }
            } else {
                if (!tenantAppVersion.getUseful()) {
                    updateTenantAppUsers(sysTenantAppVersionVO);
                }
            }
        }
    }

    void updateTenantAppUsers(SysTenantAppVersionVO sysTenantAppVersionVO) {
        if (CollUtil.isEmpty(sysTenantAppVersionVO.getUsers())) {
            throw new PaasException("用户列表不能为空");
        }
        //增加管理员
        List<SysTenantUser> systemUsers = sysTenantUserService.getTenantSystemUsers();
        if (CollUtil.isNotEmpty(systemUsers)) {
            List<NameAndIdForView> admins = new ArrayList<>();
            systemUsers.forEach(s -> {
                NameAndIdForView admin = new NameAndIdForView();
                admin.setId(s.getUserId());
                admins.add(admin);
            });
            sysTenantAppVersionVO.getUsers().addAll(admins);
        }

        SysTenantAppVersion tenantAppVersion = this.getById(sysTenantAppVersionVO.getId());
        LoginUser loginUser = MySecurityUtils.getCurrentUser();

        //删除以前的数据
        iSysTenantUserAppService.deleteByTenantAndAppId(loginUser.getTenantId(), tenantAppVersion.getAppId());

        //新增新数据
        if (CollUtil.isNotEmpty(sysTenantAppVersionVO.getUsers())) {
            List<SysTenantUserApp> list = new ArrayList<>();
            sysTenantAppVersionVO.getUsers().forEach(u -> {
                SysTenantUserApp tenantUserApp = new SysTenantUserApp();
                tenantUserApp.setTenantId(loginUser.getTenantId());
                tenantUserApp.setUserId(u.getId());
                tenantUserApp.setAppId(tenantAppVersion.getAppId());
                tenantUserApp.setAppVersionId(tenantAppVersion.getAppVersionId());

                list.add(tenantUserApp);
            });

            iSysTenantUserAppService.saveBatch(list);
        }
    }
}
