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

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import com.kaifangqian.modules.system.entity.SysPermission;
import com.kaifangqian.modules.system.entity.SysPermissionData;
import com.kaifangqian.modules.system.mapper.SysPermissionMapper;
import com.kaifangqian.modules.system.service.*;
import com.kaifangqian.modules.system.vo.PermisionRuleVO;
import com.kaifangqian.modules.system.vo.UserAuthDataQueryVO;
import com.kaifangqian.common.constant.StatusConstant;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.system.service.*;
import com.kaifangqian.utils.MyStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @Author zhh
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    @Resource
    private SysPermissionMapper sysPermissionMapper;
    @Resource
    private ISysPermissionDataService iSysPermissionDataService;
    @Autowired
    private ISysAuthGroupPermissionService iSysAuthGroupPermissionService;
    @Autowired
    private ISysPermissionDataRuleService iSysPermissionDataRuleService;
    @Autowired
    private ISysAppInfoService iSysAppInfoService;
    @Autowired
    private ISysAppVersionPermissionService iSysAppVersionPermissionService;


    @Override
    public SysPermission getByAppIdAndPerm(String appId, String perm) {
        LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<SysPermission>();
        query.eq(SysPermission::getPerms, perm);
        query.eq(SysPermission::getAppId, appId);
        List<SysPermission> permissions = this.list(query);
        if (CollUtil.isNotEmpty(permissions)) {
            return permissions.get(0);
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addPermission(SysPermission sysPermission) {
        if (sysPermission.getMenuType() == null) {
            throw new PaasException("菜单类型不能为空");
        }
        checkMenu(sysPermission, sysPermission.getMenuType());

        if (MyStringUtils.isNotBlank(sysPermission.getFastIconAddress())) {
            String base64 = iSysAppInfoService.getBase64ByFileId(sysPermission.getFastIconAddress());

            sysPermission.setFastIcon(base64);
        }

        this.save(sysPermission);

        if (sysPermission.getRuleFlag() != null && sysPermission.getRuleFlag().equals(1)) {
            iSysPermissionDataService.addDefault(sysPermission.getId());
        }
    }

    //校验菜单、按钮、目录结构是否正确：目录-菜单-按钮
    private void checkMenu(SysPermission sysPermission, Integer inputMenuType) {
        if (MyStringUtils.isBlank(sysPermission.getAppId())) {
            throw new PaasException("所属应用不能为空");
        }
        if (MyStringUtils.isBlank(sysPermission.getParentId())) {
            sysPermission.setParentId("");
        } else {
            SysPermission permission = this.getById(sysPermission.getParentId());
            if (permission == null) {
                throw new PaasException("上级目录不存在");
            } else {
                if (inputMenuType == 1 && permission.getMenuType() != 0) {
                    throw new PaasException("菜单只能添加在目录下面");
                } else if (inputMenuType == 2 && permission.getMenuType() != 1) {
                    throw new PaasException("按钮只能添加在菜单下面");
                } else if (inputMenuType == 0 && permission.getMenuType() != 0) {
                    throw new PaasException("目录只能添加在目录下面");
                }
            }
        }
        if (MyStringUtils.isNotBlank(sysPermission.getPerms())) {
            SysPermission permission = getByAppIdAndPerm(sysPermission.getAppId(), sysPermission.getPerms());
            if (permission != null) {
                if (MyStringUtils.isBlank(sysPermission.getId())) {
                    throw new PaasException("数据不满足唯一性要求");
                } else {
                    if (!permission.getId().equals(sysPermission.getId())) {
                        throw new PaasException("数据不满足唯一性要求");
                    }
                }
            }
        }
    }

    @Override
    public void editPermission(SysPermission sysPermission) {
        SysPermission permission = this.getById(sysPermission.getId());
        if (permission == null) {
            throw new PaasException("数据不存在");
        } else {
            checkMenu(sysPermission, permission.getMenuType());
        }

        //不可更新的字段
        sysPermission.setRuleFlag(null);
        sysPermission.setMenuType(null);

        SysPermission tem = this.getById(sysPermission.getId());
        if (MyStringUtils.isNotBlank(sysPermission.getFastIconAddress())) {
            if (!sysPermission.getFastIconAddress().equals(tem.getFastIconAddress())) {
                String base64 = iSysAppInfoService.getBase64ByFileId(sysPermission.getFastIconAddress());

                sysPermission.setFastIcon(base64);
            }
        } else {
            sysPermission.setFastIcon("");
        }

        this.updateById(sysPermission);
    }

    /**
     * 逻辑删除
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deletePermissionLogical(String id) {
        //删除菜单
        LoginUser user = MySecurityUtils.getCurrentUser();
        SysPermission sysPermission = this.getById(id);
        if (sysPermission == null) {
            throw new PaasException("未找到菜单信息");
        }
        sysPermission.setDeleteFlag(true);
        sysPermission.setDeleteTime(new Date());
        sysPermission.setDeleteBy(user.getUsername());
        this.updateById(sysPermission);

        //删除sys_permission_data
        List<SysPermissionData> permissionDatas = iSysPermissionDataService.listByPermissionId(id);
        iSysPermissionDataService.deleteByPermissionId(id);

        //删除sys_auth_group_permission
        iSysAuthGroupPermissionService.deleteByPermissionId(id);

        //删除sys_permission_data_rule
        List<String> SysPermissionDataIds = permissionDatas.stream().map(SysPermissionData::getId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(SysPermissionDataIds)) {
            iSysPermissionDataRuleService.deleteByDataIds(SysPermissionDataIds);
        }
    }

    @Override
    public List<SysPermission> listByEntity(SysPermission sysPermission) {
        LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<SysPermission>();
        query.eq(SysPermission::getDeleteFlag, StatusConstant.DEL_FLAG_0)
                .eq(sysPermission.getMenuType() != null, SysPermission::getMenuType, sysPermission.getMenuType())
                .eq(sysPermission.getParentId() != null, SysPermission::getParentId, sysPermission.getParentId())
                .eq(sysPermission.getAppId() != null, SysPermission::getAppId, sysPermission.getAppId())

                .orderByAsc(SysPermission::getSortNo);

        return this.list(query);
    }

    @Override
    public List<SysPermission> listByTypes(String appId, List<Integer> types) {
        LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<SysPermission>();
        query.eq(SysPermission::getDeleteFlag, StatusConstant.DEL_FLAG_0)
                .eq(SysPermission::getAppId, appId)
                .in(CollUtil.isNotEmpty(types), SysPermission::getMenuType, types)
                .orderByAsc(SysPermission::getSortNo);

        return this.list(query);
    }

    @Override
    public List<Tree<String>> getTreeList(List<SysPermission> list) {
        List<Tree<String>> treeList = TreeUtil.build(list, "", (object, tree) -> {
            tree.setId(object.getId());
            tree.setParentId(object.getParentId());
            tree.setName(object.getName());
            tree.setWeight(object.getSortNo());

            tree.putExtra("menuType", object.getMenuType());
            tree.putExtra("path", object.getPath());
            tree.putExtra("perms", object.getPerms());
            tree.putExtra("component", object.getComponent());
            tree.putExtra("icon", object.getIcon());
            tree.putExtra("routeFlag", object.isRouteFlag());
            tree.putExtra("hiddenFlag", object.isHiddenFlag());
            tree.putExtra("keepAliveFlag", object.isKeepAliveFlag());
            tree.putExtra("internalOrExternal", object.isInternalOrExternal());
            tree.putExtra("description", object.getDescription());
            tree.putExtra("status", object.getStatus());
            tree.putExtra("ruleFlag", object.getRuleFlag());
            tree.putExtra("sortNo", object.getSortNo());
            tree.setWeight(object.getSortNo());
            tree.putExtra("fastFlag", object.getFastFlag());
            tree.putExtra("fastIconAddress", object.getFastIconAddress());
            tree.putExtra("authVisible", object.getAuthVisible());
        });

        return treeList;
    }

    @Override
    public List<SysPermission> queryByUser(UserAuthDataQueryVO query) {
        return this.sysPermissionMapper.queryByUser(query);
    }

    @Override
    public List<SysPermission> listTenantAppAllMenu(String tenantId, String appId, List<Integer> types) {
        return sysPermissionMapper.listTenantAppAllMenu(tenantId, appId, null, types);
    }

    @Override
    public List<PermisionRuleVO> listButtonAndAuth(String permissionId) {
        List<PermisionRuleVO> result = new ArrayList<>();
        SysPermission permission = this.getById(permissionId);
        if (permission != null && permission.getMenuType() == 1) {
            LoginUser loginUser = MySecurityUtils.getCurrentUser();
            List<SysPermission> list = sysPermissionMapper.listTenantAppAllMenu(loginUser.getTenantId(), null, permissionId, null);
            if (CollUtil.isNotEmpty(list)) {
                List<String> permissionIds = list.stream().map(SysPermission::getId).collect(Collectors.toList());
                List<SysPermissionData> permissionDatas = iSysPermissionDataService.listTenantRuleByPermissionIds(permissionIds);
                Map<String, List<SysPermissionData>> permissionDataMap = new HashMap<>();
                if (CollUtil.isNotEmpty(permissionDatas)) {
                    permissionDataMap = permissionDatas.stream().collect(Collectors.groupingBy(SysPermissionData::getPermissionId));
                }
                for (SysPermission p : list) {
                    PermisionRuleVO permisionRuleVO = new PermisionRuleVO();
                    permisionRuleVO.setId(p.getId());
                    permisionRuleVO.setName(p.getName());
                    permisionRuleVO.setRules(permissionDataMap.get(p.getId()));

                    result.add(permisionRuleVO);
                }
            }
        }
        return result;
    }

    @Override
    public List<PermisionRuleVO> listButtonAndAuth2(String versionId, String permissionId) {
        List<PermisionRuleVO> result = new ArrayList<>();
        SysPermission permission = this.getById(permissionId);
        if (permission != null && permission.getMenuType() == 1) {
            List<String> versionPermissionIds = iSysAppVersionPermissionService.listVersionPermissions(versionId);
            SysPermission query = new SysPermission();
            query.setParentId(permissionId);
            List<SysPermission> listAll = listByEntity(query);
            if (CollUtil.isNotEmpty(listAll) && CollUtil.isNotEmpty(versionPermissionIds)) {
                List<SysPermission> list = new ArrayList<>();
                listAll.forEach(p -> {
                    if (versionPermissionIds.contains(p.getId())) {
                        list.add(p);
                    }
                });
                if (CollUtil.isNotEmpty(list)) {
                    List<String> permissionIds = list.stream().map(SysPermission::getId).collect(Collectors.toList());
                    List<SysPermissionData> permissionDatas = iSysPermissionDataService.listTenantRuleByPermissionIds(permissionIds);
                    Map<String, List<SysPermissionData>> permissionDataMap = new HashMap<>();
                    if (CollUtil.isNotEmpty(permissionDatas)) {
                        permissionDataMap = permissionDatas.stream().collect(Collectors.groupingBy(SysPermissionData::getPermissionId));
                    }
                    for (SysPermission p : list) {
                        PermisionRuleVO permisionRuleVO = new PermisionRuleVO();
                        permisionRuleVO.setId(p.getId());
                        permisionRuleVO.setName(p.getName());
                        permisionRuleVO.setRules(permissionDataMap.get(p.getId()));

                        result.add(permisionRuleVO);
                    }
                }
            }
        }
        return result;
    }
}
