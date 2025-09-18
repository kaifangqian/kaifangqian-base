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
import com.kaifangqian.modules.system.entity.SysAppInfo;
import com.kaifangqian.modules.system.entity.SysAppVersion;
import com.kaifangqian.modules.system.enums.AppAndVersionStatus;
import com.kaifangqian.modules.system.enums.AppAndVersionType;
import com.kaifangqian.modules.system.mapper.SysAppVersionMapper;
import com.kaifangqian.modules.system.vo.SysAppVersionPermissionVO;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.system.service.ISysAppInfoService;
import com.kaifangqian.modules.system.service.ISysAppVersionPermissionService;
import com.kaifangqian.modules.system.service.ISysAppVersionService;
import com.kaifangqian.utils.MyStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @author zhenghuihan
 * @description 系统版本分组服务
 * @createTime 2022/9/2 17:40
 */
@Service
public class SysAppVersionServiceImpl extends ServiceImpl<SysAppVersionMapper, SysAppVersion> implements ISysAppVersionService {

    @Autowired
    private ISysAppInfoService iSysAppInfoService;
    @Autowired
    private ISysAppVersionPermissionService iSysAppVersionPermissionService;

    @Override
    public List<SysAppVersion> getByAppIds(List<String> appIds, List<Integer> types, Integer versionStatus) {
        LambdaQueryWrapper<SysAppVersion> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(SysAppVersion::getDeleteFlag, 0)
                .eq(versionStatus != null, SysAppVersion::getVersionStatus, versionStatus)
                .in(CollUtil.isNotEmpty(appIds), SysAppVersion::getAppInfoId, appIds)
                .in(CollUtil.isNotEmpty(types), SysAppVersion::getVersionType, types);

        return this.list(queryWrapper);
    }

    @Override
    public void saveExt(SysAppVersion sysAppVersion) {
        checkDate(sysAppVersion);
        sysAppVersion.setVersionStatus(AppAndVersionStatus.DRAFT.getType());

        this.save(sysAppVersion);

        //权限维护
        SysAppVersionPermissionVO vo = new SysAppVersionPermissionVO();
        vo.setAppVersionId(sysAppVersion.getId());
        vo.setPermissions(sysAppVersion.getPermissions());
        iSysAppVersionPermissionService.editVersionPermissions(vo);
    }

    @Override
    public void updateExt(SysAppVersion sysAppVersion) {
        checkDate(sysAppVersion);
        if (MyStringUtils.isNotBlank(sysAppVersion.getId())) {
            SysAppVersion appVersion = this.getById(sysAppVersion.getId());
            if (!AppAndVersionStatus.DRAFT.getType().equals(appVersion.getVersionStatus())) {
                throw new PaasException("非草稿状态的不可以编辑");
            }
            sysAppVersion.setVersionStatus(null);

            this.updateById(sysAppVersion);

            //权限维护
            SysAppVersionPermissionVO vo = new SysAppVersionPermissionVO();
            vo.setAppVersionId(sysAppVersion.getId());
            vo.setPermissions(sysAppVersion.getPermissions());
            iSysAppVersionPermissionService.editVersionPermissions(vo);
        }
    }

    void checkDate(SysAppVersion sysAppVersion) {
        if (MyStringUtils.isBlank(sysAppVersion.getAppInfoId()) || MyStringUtils.isBlank(sysAppVersion.getVersionCode())) {
            throw new PaasException("信息不全");
        }
        SysAppInfo sysAppInfo = iSysAppInfoService.getById(sysAppVersion.getAppInfoId());
        if (!AppAndVersionType.ALL.getType().equals(sysAppInfo.getAppType())) {
            if (!sysAppInfo.getAppType().equals(sysAppVersion.getVersionType())) {
                throw new PaasException("版本类型不支持");
            }
        }

        SysAppVersion query = new SysAppVersion();
        query.setVersionCode(sysAppVersion.getVersionCode());
        List<SysAppVersion> list = getByEntity(query);
        if (CollUtil.isNotEmpty(list)) {
            SysAppVersion appVersion = list.get(0);
            if (appVersion != null) {
                if (MyStringUtils.isBlank(sysAppVersion.getId())) {
                    throw new PaasException("数据不满足唯一性要求");
                } else {
                    if (!appVersion.getId().equals(sysAppVersion.getId())) {
                        throw new PaasException("数据不满足唯一性要求");
                    }
                }
            }
        }
    }

    List<SysAppVersion> getByEntity(SysAppVersion sysAppVersion) {
        LambdaQueryWrapper<SysAppVersion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MyStringUtils.isNotBlank(sysAppVersion.getVersionCode()), SysAppVersion::getVersionCode, sysAppVersion.getVersionCode());
        queryWrapper.eq(SysAppVersion::getDeleteFlag, 0);

        return list(queryWrapper);
    }


    @Override
    public void removeExt(String id) {
        SysAppVersion sysAppVersion = this.getById(id);
        if (sysAppVersion != null) {
            if (!AppAndVersionStatus.DRAFT.getType().equals(sysAppVersion.getVersionStatus())) {
                throw new PaasException("非草稿状态不可以删除");
            }
            LoginUser loginUser = MySecurityUtils.getCurrentUser();
            sysAppVersion.setDeleteFlag(true);
            sysAppVersion.setDeleteTime(new Date());
            sysAppVersion.setDeleteBy(loginUser.getId());

            this.updateById(sysAppVersion);
        }
    }

    @Override
    public void updateStatus(String id) {
        if (MyStringUtils.isNotBlank(id)) {
            SysAppVersion appVersion = this.getById(id);
            if (appVersion != null) {
                SysAppInfo sysAppInfo = iSysAppInfoService.getById(appVersion.getAppInfoId());
                if (sysAppInfo.getAppStatus().equals(AppAndVersionStatus.DRAFT.getType())) {
                    throw new PaasException("草稿状态的应用不可以发布版本");
                }
                appVersion.setVersionStatus(AppAndVersionStatus.PUBLISHED.getType());

                this.updateById(appVersion);
            }
        }
    }

    @Override
    public void removeBatchExt(List<String> ids) {
        List<SysAppVersion> appVersions = new ArrayList<>();
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        ids.forEach(i -> {
            SysAppVersion sysAppVersion = new SysAppVersion();
            sysAppVersion.setId(i);
            sysAppVersion.setDeleteFlag(true);
            sysAppVersion.setDeleteTime(new Date());
            sysAppVersion.setDeleteBy(loginUser.getId());

            appVersions.add(sysAppVersion);
        });
        //1.删除APP-版本
        if (CollUtil.isNotEmpty(appVersions)) {
            this.updateBatchById(appVersions);
        }

        //todo 是否全部清除？？
        //3.删除版本-权限

        //4.删除租户-app

        //5.删除用户-app
    }

    @Override
    public List<SysAppVersion> getByIds(List<String> ids) {
        LambdaQueryWrapper<SysAppVersion> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(SysAppVersion::getDeleteFlag, 0)
                .eq(SysAppVersion::getVersionStatus, AppAndVersionStatus.PUBLISHED.getType())
                .in(CollUtil.isNotEmpty(ids), SysAppVersion::getId, ids);

        return this.list(queryWrapper);
    }
}
