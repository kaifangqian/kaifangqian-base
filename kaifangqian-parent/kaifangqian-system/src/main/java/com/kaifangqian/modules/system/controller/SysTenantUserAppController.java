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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.modules.system.entity.SysAppInfo;
import com.kaifangqian.modules.system.entity.SysTenantAppVersion;
import com.kaifangqian.modules.system.entity.SysUser;
import com.kaifangqian.modules.system.vo.NameAndIdForView;
import com.kaifangqian.modules.system.vo.SysTenantAppVersionVO;
import com.kaifangqian.modules.system.vo.TenantUserAppReq;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.system.service.ISysAppInfoService;
import com.kaifangqian.modules.system.service.ISysTenantAppVersionService;
import com.kaifangqian.modules.system.service.ISysTenantUserAppService;
import com.kaifangqian.modules.system.service.ISysUserService;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

// @Api(tags = "租户应用管理表")
@RestController
@RequestMapping("/system/sysTenantApp")
@Slf4j
public class SysTenantUserAppController {
    @Autowired
    private ISysTenantAppVersionService iSysTenantAppVersionService;
    @Autowired
    private ISysTenantUserAppService iSysTenantUserAppService;
    @Autowired
    private ISysAppInfoService iSysAppInfoService;
    @Autowired
    private ISysUserService iSysUserService;

    /**
     * 分页列表查询租户应用
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    // @ApiOperation(value = "分页列表查询租户应用", notes = "分页列表查询租户应用")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   TenantUserAppReq req) {
        Page<SysTenantAppVersionVO> page = new Page<SysTenantAppVersionVO>(pageNo, pageSize);
        IPage<SysTenantAppVersionVO> pageList = iSysTenantAppVersionService.pageExt(page, req);

        return Result.OK(pageList);
    }

    /**
     * 通过id查询详情
     *
     * @param id
     * @return
     */
    // @ApiOperation(value = "通过id查询详情", notes = "通过id查询详情")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SysTenantAppVersionVO result = new SysTenantAppVersionVO();
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        SysTenantAppVersion tenantAppVersion = iSysTenantAppVersionService.getById(id);
        if (tenantAppVersion != null) {
            result.setId(tenantAppVersion.getId());
            result.setStatus(tenantAppVersion.getStatus());
            result.setUseful(tenantAppVersion.getUseful());
            SysAppInfo sysAppInfo = iSysAppInfoService.getById(tenantAppVersion.getAppId());
            if (sysAppInfo != null) {
                result.setAppIcon(sysAppInfo.getAppIcon());
                result.setAppName(sysAppInfo.getAppName());
                result.setAppDesc(sysAppInfo.getAppDesc());
                result.setAppIconAddress(sysAppInfo.getAppIconAddress());
            }
        }
        if (!tenantAppVersion.getUseful()) {
            List<String> userIds = iSysTenantUserAppService.getByTenantAndAppId(loginUser.getTenantId(), tenantAppVersion.getAppId());
            if (CollUtil.isNotEmpty(userIds)) {
                List<SysUser> sysUsers = iSysUserService.findByUserIds(userIds);
                if (CollUtil.isNotEmpty(sysUsers)) {
                    List<NameAndIdForView> users = new ArrayList<>();
                    sysUsers.forEach(u -> {
                        NameAndIdForView vo = new NameAndIdForView();
                        vo.setId(u.getId());
                        vo.setName(u.getRealname());

                        users.add(vo);
                    });

                    result.setUsers(users);
                }
            }
        }
        return Result.OK(result);
    }

    /**
     * 停用 启用
     *
     * @param tenantAppVersion
     * @return
     */
    // @ApiOperation(value = " 应用-停用 启用", notes = " 应用-停用 启用")
    @PutMapping(value = "/updateStatus")
    public Result<?> updateStatus(@RequestBody SysTenantAppVersion tenantAppVersion) {
        iSysTenantAppVersionService.updateStatus(tenantAppVersion.getId());
        return Result.OK("启用/停用成功！");
    }

    /**
     * 修改范围
     *
     * @param sysTenantAppVersionVO
     * @return
     */
    // @ApiOperation(value = "修改范围", notes = "修改范围")
    @PutMapping(value = "/updateUseful")
    public Result<?> updateUseful(@RequestBody SysTenantAppVersionVO sysTenantAppVersionVO) {
        iSysTenantAppVersionService.updateUseful(sysTenantAppVersionVO);
        return Result.OK("修改范围成功！");
    }

}
