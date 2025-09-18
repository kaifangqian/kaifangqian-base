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
import com.kaifangqian.modules.system.entity.SysAppVersion;
import com.kaifangqian.modules.system.entity.SysAppVersionGroup;
import com.kaifangqian.modules.system.entity.SysAppVersionGroupPermission;
import com.kaifangqian.modules.system.mapper.SysAppVersionGroupMapper;
import com.kaifangqian.modules.system.vo.PermissionRuleVO;
import com.kaifangqian.modules.system.vo.SysAppVersionGroupPermissionReq;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.system.service.ISysAppVersionGroupPermissionService;
import com.kaifangqian.modules.system.service.ISysAppVersionGroupService;
import com.kaifangqian.modules.system.service.ISysAppVersionService;
import com.kaifangqian.utils.MyStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * @author zhenghuihan
 * @description 系统版本分组服务
 * @createTime 2022/9/2 17:40
 */
@Service
public class SysAppVersionGroupServiceImpl extends ServiceImpl<SysAppVersionGroupMapper, SysAppVersionGroup> implements ISysAppVersionGroupService {

    @Autowired
    private ISysAppVersionService iSysAppVersionService;
    @Autowired
    private ISysAppVersionGroupPermissionService iSysAppVersionGroupPermissionService;


    @Override
    public List<SysAppVersionGroup> listByEntity(SysAppVersionGroup query) {
        LambdaQueryWrapper<SysAppVersionGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MyStringUtils.isNotBlank(query.getAppVersionId()), SysAppVersionGroup::getAppVersionId, query.getAppVersionId())
                .eq(SysAppVersionGroup::getDeleteFlag, 0)
                .eq(query.getUseFlag() != null, SysAppVersionGroup::getUseFlag, query.getUseFlag())
                .eq(query.getInitFlag() != null, SysAppVersionGroup::getInitFlag, query.getInitFlag())
                .eq(MyStringUtils.isNotBlank(query.getAppId()), SysAppVersionGroup::getAppId, query.getAppId());

        return this.list(queryWrapper);
    }

    @Override
    public void saveExt(SysAppVersionGroup entity) {
        if (MyStringUtils.isNotBlank(entity.getAppVersionId())) {
            SysAppVersion sysAppVersion = iSysAppVersionService.getById(entity.getAppVersionId());
            if (sysAppVersion != null) {
                entity.setAppId(sysAppVersion.getAppInfoId());
                this.save(entity);

                //更新权限
                SysAppVersionGroupPermissionReq req = new SysAppVersionGroupPermissionReq();
                req.setGroupId(entity.getId());
                req.setRuleVOS(entity.getRuleVOS());
                iSysAppVersionGroupPermissionService.editExt(req);
            } else {
                throw new PaasException("版本ID错误");
            }

        } else {
            throw new PaasException("版本ID不能为空");
        }
    }

    @Override
    public void updateExt(SysAppVersionGroup entity) {
        entity.setAppId(null);
        this.updateById(entity);

        //更新权限
        SysAppVersionGroupPermissionReq req = new SysAppVersionGroupPermissionReq();
        req.setGroupId(entity.getId());
        req.setRuleVOS(entity.getRuleVOS());
        iSysAppVersionGroupPermissionService.editExt(req);
    }

    @Override
    public SysAppVersionGroup getByIdExt(String id) {
        SysAppVersionGroup sysAppVersionGroup = this.getById(id);
        if (sysAppVersionGroup == null) {
            throw new PaasException("未找到对应数据");
        }
        SysAppVersionGroupPermission req = new SysAppVersionGroupPermission();
        req.setGroupId(id);
        List<SysAppVersionGroupPermission> list = iSysAppVersionGroupPermissionService.listByEntity(req);
        if (CollUtil.isNotEmpty(list)) {
            List<PermissionRuleVO> ruleVOS = new ArrayList<>();
            Map<String, List<SysAppVersionGroupPermission>> map = list.stream().collect(Collectors.groupingBy(SysAppVersionGroupPermission::getPermissionId));
            for (Map.Entry<String, List<SysAppVersionGroupPermission>> entry : map.entrySet()) {
                PermissionRuleVO vo = new PermissionRuleVO();
                vo.setPermissionId(entry.getKey());
                List<SysAppVersionGroupPermission> val = entry.getValue();
                if (CollUtil.isNotEmpty(val)) {
                    vo.setRuleIds(val.stream().map(SysAppVersionGroupPermission::getPermissionDataId).collect(Collectors.toList()));
                }

                ruleVOS.add(vo);
            }
            sysAppVersionGroup.setRuleVOS(ruleVOS);
        }
        return sysAppVersionGroup;
    }

    @Override
    public void updateStatus(String id) {
        if (MyStringUtils.isNotBlank(id)) {
            SysAppVersionGroup sysAppVersionGroup = this.getById(id);
            if (sysAppVersionGroup != null) {
                if (sysAppVersionGroup.getUseFlag()) {
                    sysAppVersionGroup.setUseFlag(false);
                } else {
                    sysAppVersionGroup.setUseFlag(true);
                }
                this.updateById(sysAppVersionGroup);
            } else {
                throw new PaasException("id错误");
            }
        } else {
            throw new PaasException("id不能为空");
        }
    }
}
