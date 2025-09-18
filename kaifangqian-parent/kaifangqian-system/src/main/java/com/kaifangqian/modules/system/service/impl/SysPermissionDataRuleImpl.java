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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import cn.hutool.core.collection.CollUtil;
import com.kaifangqian.modules.form.entity.OnlTableField;
import com.kaifangqian.modules.form.service.IOnlTableFieldService;
import com.kaifangqian.modules.system.entity.SysAppInfo;
import com.kaifangqian.modules.system.entity.SysPermission;
import com.kaifangqian.modules.system.entity.SysPermissionDataRule;
import com.kaifangqian.modules.system.mapper.SysPermissionDataRuleMapper;
import com.kaifangqian.modules.system.vo.SysPermissionDataVO;
import com.kaifangqian.modules.system.vo.UserAuthDataQueryVO;
import com.kaifangqian.modules.system.vo.UserPermissionDataRule;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.modules.system.service.ISysAppInfoService;
import com.kaifangqian.modules.system.service.ISysPermissionDataRuleService;
import com.kaifangqian.modules.system.service.ISysPermissionService;
import com.kaifangqian.modules.system.service.ISysUserRoleService;
import com.kaifangqian.utils.MyStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <p>
 * 菜单权限规则  服务实现类
 * </p>
 */
@Service
public class SysPermissionDataRuleImpl extends ServiceImpl<SysPermissionDataRuleMapper, SysPermissionDataRule>
        implements ISysPermissionDataRuleService {

    @Resource
    private SysPermissionDataRuleMapper mapper;
    @Autowired
    private ISysPermissionService iSysPermissionService;
    @Autowired
    private IOnlTableFieldService onlTableFieldService;
    @Autowired
    private ISysUserRoleService iSysUserRoleService;
    @Autowired
    private ISysAppInfoService iSysAppInfoService;


    @Override
    public void updateExt(SysPermissionDataVO dataVO) {
        //删除历史数据
        deleteByDataId(dataVO.getId());
        //新增数据
        List<SysPermissionDataRule> list = new ArrayList<>();
        if (CollUtil.isNotEmpty(dataVO.getRules())) {
            dataVO.getRules().forEach(r -> {
                SysPermissionDataRule rule = new SysPermissionDataRule();
                BeanUtils.copyProperties(r, rule);
                if (MyStringUtils.isNotBlank(r.getRuleColumnId())) {
                    OnlTableField onlTableField = onlTableFieldService.getById(r.getRuleColumnId());
                    if (onlTableField != null && MyStringUtils.isNotBlank(onlTableField.getDbFieldName())) {
                        rule.setRuleColumn(MyStringUtils.toCamelCase(onlTableField.getDbFieldName()));
                    }
                }
                rule.setDataId(dataVO.getId());

                list.add(rule);
            });
        }

        if (CollUtil.isNotEmpty(list)) {
            this.saveBatch(list);
        }
    }

    @Override
    public List<SysPermissionDataRule> getByEntity(SysPermissionDataRule query) {
        LambdaQueryWrapper<SysPermissionDataRule> queryWrapper = new LambdaQueryWrapper<SysPermissionDataRule>();
        queryWrapper.eq(MyStringUtils.isNotBlank(query.getDataId()), SysPermissionDataRule::getDataId, query.getDataId());

        return mapper.selectList(queryWrapper);
    }

    @Override
    public void deleteByDataId(String dataId) {
        LambdaQueryWrapper<SysPermissionDataRule> query = new LambdaQueryWrapper<SysPermissionDataRule>();
        query.eq(SysPermissionDataRule::getDataId, dataId);

        this.remove(query);
    }

    @Override
    public void deleteByDataIds(List<String> dataIds) {
        LambdaQueryWrapper<SysPermissionDataRule> query = new LambdaQueryWrapper<SysPermissionDataRule>();
        query.in(SysPermissionDataRule::getDataId, dataIds);

        this.remove(query);
    }

    @Override
    public List<UserPermissionDataRule> queryUserPermissionDataRules(String perms) {
        LoginUser user = MySecurityUtils.getCurrentUser();
        SysAppInfo sysAppInfo = iSysAppInfoService.getByAppCode(user.getAppCode());
        if (sysAppInfo != null) {
            SysPermission permission = iSysPermissionService.getByAppIdAndPerm(sysAppInfo.getId(), perms);
            if (permission != null) {
                LoginUser loginUser = MySecurityUtils.getCurrentUser();
                UserAuthDataQueryVO query = new UserAuthDataQueryVO();
                query.setUserId(loginUser.getId());
                query.setRoleIds(iSysUserRoleService.getMyRoleIds());
                query.setDepartId(loginUser.getDepartId());
                query.setTenantId(loginUser.getTenantId());

                query.setPermissionId(permission.getId());

                return mapper.queryUserPermissionDataRules(query);
            }
        }
        return null;
    }
}
