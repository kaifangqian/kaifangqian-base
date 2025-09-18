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
package com.kaifangqian.modules.system.controller;

import cn.hutool.core.collection.CollUtil;
import com.kaifangqian.modules.system.entity.SysAuthGroup;
import com.kaifangqian.modules.system.enums.AuthGroupType;
import com.kaifangqian.modules.system.vo.UserAuthGroupVO;
import com.kaifangqian.annotation.ResrunLogMethod;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.enums.OperateLogType;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.system.service.ISysAuthGroupMemberService;
import com.kaifangqian.modules.system.service.ISysAuthGroupService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author zhenghuihan
 * @description 权限组表
 * @createTime 2022/9/2 18:06
 */
// @Api(tags = "权限组表")
@RestController
@RequestMapping("/system/sysAuthGroup")
@Slf4j
@ResrunLogModule(name = "权限组管理模块")
public class SysAuthGroupController {
    @Autowired
    private ISysAuthGroupService sysAuthGroupService;
    @Autowired
    private ISysAuthGroupMemberService sysAuthGroupMemberService;

    /**
     * 列表查询
     *
     * @param sysAuthGroup
     * @return
     */
    // @ApiOperation(value = "权限组表-分页列表查询", notes = "权限组表-分页列表查询")
    @GetMapping(value = "/list")
    @ResrunLogMethod(name = "查询权限组列表", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<List<SysAuthGroup>> list(SysAuthGroup sysAuthGroup) {
        List<SysAuthGroup> list = sysAuthGroupService.listByEntity(sysAuthGroup);
        return Result.OK(list);
    }


    // @ApiOperation(value = "当前用户的权限组", notes = "当前用户的权限组")
    @GetMapping(value = "/listMy")
    @ResrunLogMethod(name = "当前用户的权限组", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<List<UserAuthGroupVO>> listMy() {
        List<UserAuthGroupVO> result = new ArrayList<>();
        List<UserAuthGroupVO> list = sysAuthGroupMemberService.getLoginUserGroups();
        List<String> groupIds = new ArrayList<>();
        if (CollUtil.isNotEmpty(list)) {
            groupIds = list.stream().map(UserAuthGroupVO::getId).collect(Collectors.toList());
        }
        List<SysAuthGroup> systemGroups = sysAuthGroupService.getSystemTypeGroups(AuthGroupType.ALL.getType());
        if (CollUtil.isNotEmpty(systemGroups)) {
            SysAuthGroup sysGroup = systemGroups.get(0);
            if (groupIds.contains(sysGroup.getId())) {
                result.addAll(list);
            } else {
                UserAuthGroupVO sysGroupVO = new UserAuthGroupVO();
                sysGroupVO.setId(sysGroup.getId());
                sysGroupVO.setGroupName(sysGroup.getGroupName());
                sysGroupVO.setGroupDesc(sysGroup.getGroupDesc());
                sysGroupVO.setMyFlag(false);
                result.add(sysGroupVO);
                result.addAll(list);
            }
        } else {
            result.addAll(list);
        }

        return Result.OK(result);
    }

    /**
     * 添加
     *
     * @param sysAuthGroup
     * @return
     */
    @RequiresPermissions(value = {"authcategory:add", "authgroup:add"}, logical = Logical.OR)
    // @ApiOperation(value = "权限组表-添加", notes = "权限组表-添加")
    @PostMapping(value = "/add")
    @ResrunLogMethod(name = "新增权限组", operateType = OperateLogType.OPERATE_TYPE_2)
    public Result<?> add(@RequestBody SysAuthGroup sysAuthGroup) {
        sysAuthGroup.setSystemFlag(false);
        sysAuthGroup.setGroupType(AuthGroupType.USERADD.getType());
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        sysAuthGroup.setTenantId(loginUser.getTenantId());
        sysAuthGroupService.saveExt(sysAuthGroup);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param sysAuthGroup
     * @return
     */
    @RequiresPermissions(value = {"authcategory:edit", "authgroup:edit"}, logical = Logical.OR)
    // @ApiOperation(value = "权限组表-编辑", notes = "权限组表-编辑")
    @PutMapping(value = "/edit")
    @ResrunLogMethod(name = "编辑权限组", operateType = OperateLogType.OPERATE_TYPE_3)
    public Result<?> edit(@RequestBody SysAuthGroup sysAuthGroup) {
        sysAuthGroup.setTenantId(null);
        sysAuthGroup.setSystemFlag(null);
        sysAuthGroup.setGroupType(null);
        sysAuthGroupService.updateById(sysAuthGroup);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    // @ApiOperation(value = "权限组表-通过id查询", notes = "权限组表-通过id查询")
    @GetMapping(value = "/queryById")
    @ResrunLogMethod(name = "查询权限组详情", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SysAuthGroup sysAuthGroup = sysAuthGroupService.getById(id);
        if (sysAuthGroup == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(sysAuthGroup);
    }
}
