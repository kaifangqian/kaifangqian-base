package com.kaifangqian.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.kaifangqian.modules.api.service.IApiNormalReqService;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.mapper.SysUserMapper;
import com.kaifangqian.modules.system.service.*;
import com.kaifangqian.modules.system.vo.UserAuthDataQueryVO;
import com.kaifangqian.modules.system.vo.UserPermissionDataRule;
import com.kaifangqian.common.api.CommonAPI;
import com.kaifangqian.common.constant.CommonConstant;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.util.oConvertUtils;
import com.kaifangqian.utils.MyStringUtils;
import lombok.extern.slf4j.Slf4j;
import com.kaifangqian.common.system.vo.*;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.mapper.*;
import com.kaifangqian.modules.system.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * [类功能描述：底层共通业务API，提供其他独立模块调用]
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

/**
 * @Author: zhh
 */
@Slf4j
@Service
public class CommonAPIImpl implements CommonAPI {
    @Resource
    private SysUserMapper userMapper;
    @Autowired
    private ISysUserRoleService iSysUserRoleService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private ISysPermissionService sysPermissionService;
    @Autowired
    private ISysPermissionDataRuleService sysPermissionDataRuleService;
    @Autowired
    private ISysTenantUserService iSysTenantUserService;
    @Autowired
    private ISysUserDepartService iSysUserDepartService;
    @Autowired
    private ISysTenantUserAppService iSysTenantUserAppService;
    @Autowired
    private ISysAppInfoService iSysAppInfoService;
    @Autowired
    private ISysTenantAppVersionService iSysTenantAppVersionService;
    @Autowired
    private IApiNormalReqService apiNormalReqService;


    @Override
    public LoginUser getUserByName(String username) {
        if (oConvertUtils.isEmpty(username)) {
            return null;
        }
        LoginUser loginUser = new LoginUser();
        SysUser sysUser = userMapper.getUserByName(username);
        if (sysUser == null) {
            return null;
        }
        BeanUtils.copyProperties(sysUser, loginUser);
        return loginUser;
    }

    @Override
    public List<List<List<List<SysPermissionDataRuleModel>>>> queryPermissionDataRule(String perms) {
        List<List<List<List<SysPermissionDataRuleModel>>>> result = new ArrayList<>();
        List<UserPermissionDataRule> dataRules = sysPermissionDataRuleService.queryUserPermissionDataRules(perms);
        if (CollUtil.isNotEmpty(dataRules)) {
            Map<String, List<UserPermissionDataRule>> groupRuleMap = dataRules.stream().collect(Collectors.groupingBy(UserPermissionDataRule::getGroupId));
            for (List<UserPermissionDataRule> dataValue : groupRuleMap.values()) {
                List<List<List<SysPermissionDataRuleModel>>> groupRuleList = new ArrayList<>();
                if (CollUtil.isNotEmpty(dataValue)) {
                    Map<String, List<UserPermissionDataRule>> dataRuleMap = dataValue.stream().collect(Collectors.groupingBy(UserPermissionDataRule::getPermissionDataId));
                    for (List<UserPermissionDataRule> ruleValue : dataRuleMap.values()) {
                        Map<String, List<UserPermissionDataRule>> ruleMap = ruleValue.stream().collect(Collectors.groupingBy(UserPermissionDataRule::getConditionGroup));
                        List<List<SysPermissionDataRuleModel>> ruleList = new ArrayList<>();
                        for (List<UserPermissionDataRule> rule : ruleMap.values()) {
                            ruleList.add(oConvertUtils.entityListToModelList(rule, SysPermissionDataRuleModel.class));
                        }
                        groupRuleList.add(ruleList);
                    }
                }
                result.add(groupRuleList);
            }
        }

        return result;
    }

    @Override
    public String getDepartIdByOrgCode(String orgCode) {
        if (MyStringUtils.isNotBlank(orgCode)) {
            List<String> orgCodes = new ArrayList<>();
            orgCodes.add(orgCode);
            List<SysDepart> sysDeparts = sysDepartService.getByOrgCodes(orgCodes);
            if (CollUtil.isNotEmpty(sysDeparts)) {
                return sysDeparts.get(0).getId();
            }
        }
        return null;
    }

    /**
     * 查询用户拥有的权限集合 common api 里面的接口实现
     *
     * @return
     */
    @Override
    public List<String> queryUserAuths() {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        UserAuthDataQueryVO query = new UserAuthDataQueryVO();
        query.setTenantId(loginUser.getTenantId());
        query.setUserId(loginUser.getId());
        query.setDepartId(loginUser.getDepartId());
        SysAppInfo sysAppInfo = iSysAppInfoService.getByAppCode(loginUser.getAppCode());
        if (sysAppInfo == null) {
            return new ArrayList<>();
        }
        query.setRoleIds(getMyRoleIds());
        query.setAppId(sysAppInfo.getId());
        SysTenantAppVersion sysTenantAppVersion = iSysTenantAppVersionService.getByTenantAndApp(loginUser.getTenantId(), sysAppInfo.getId());
        if (sysTenantAppVersion == null) {
            return new ArrayList<>();
        }
        query.setAppVersionId(sysTenantAppVersion.getAppVersionId());
        List<SysPermission> metaList = sysPermissionService.queryByUser(query);
        //按钮权限（用户拥有的权限集合）
        List<String> authList = metaList.stream()
                .filter((permission) -> permission.getMenuType().equals(CommonConstant.MENU_TYPE_2))
                .collect(() -> new ArrayList<String>(),
                        (list, permission) -> list.add(permission.getPerms()),
                        (list1, list2) -> list1.addAll(list2)
                );
        return authList;
    }

    @Override
    public List<String> getMyRoleIds() {
        return iSysUserRoleService.getMyRoleIds();
    }

    @Override
    public List<String> getDepartsByName(String username) {
        List<SysDepart> list = sysDepartService.queryDepartsByUsername(username);
        if (CollUtil.isNotEmpty(list)) {
            return list.stream().map(SysDepart::getId).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * 7-校验用户-租户是否合理:true：合理 false：不合理
     */
    @Override
    public boolean checkUserTenant(String userId, String tenantId) {
        return iSysTenantUserService.checkUserTenant(userId, tenantId);
    }

    /**
     * 8-校验(租户下)用户-部门是否合理:true：合理 false：不合理
     */
    @Override
    public boolean checkUserDepart(String userId, String tenantId, String departId) {
        return iSysUserDepartService.checkUserDepart(userId, tenantId, departId);
    }

    /**
     * 9-校验(租户下)用户-应用是否合理:true：合理 false：不合理
     */
    @Override
    public boolean checkUserTenantApp(String userId, String tenantId, String appCode) {
        return iSysTenantUserAppService.checkUserTenantApp(userId, tenantId, appCode);
    }

    @Override
    public String getTenantUserId(String tenantId, String userId) {
        SysTenantUser tenantUser = iSysTenantUserService.getTenantUser(tenantId, userId);
        if (tenantUser != null) {
            return tenantUser.getId();
        }
        return null;
    }

    @Override
    public void recordNormalRes(String res) {
        apiNormalReqService.recordNormalRes(res);
    }
}