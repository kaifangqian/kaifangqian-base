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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.modules.system.entity.ApiDeveloperManage;
import com.kaifangqian.modules.system.entity.SysTenantInfo;
import com.kaifangqian.common.system.query.QueryGenerator;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.system.service.IApiDeveloperManageService;
import com.kaifangqian.modules.system.service.ISysTenantInfoService;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// @Api(tags = "开发者管理")
@RestController
@RequestMapping("/system/developerManage")
@Slf4j
public class ApiDeveloperManageController {
    @Autowired
    private IApiDeveloperManageService apiDeveloperManageService;
    @Autowired
    private ISysTenantInfoService sysTenantInfoService;

    /**
     * 分页列表查询
     *
     * @param apiDeveloperManage
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    // @ApiOperation(value = "应用管理表-分页列表查询", notes = "应用管理表-分页列表查询")
    @GetMapping(value = "/list")
    @RequiresPermissions(value = {"system:manage"})
    public Result<?> queryPageList(ApiDeveloperManage apiDeveloperManage, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<ApiDeveloperManage> queryWrapper = QueryGenerator.initQueryWrapper(apiDeveloperManage, req.getParameterMap());
        Page<ApiDeveloperManage> page = new Page<ApiDeveloperManage>(pageNo, pageSize);
        IPage<ApiDeveloperManage> pageList = apiDeveloperManageService.page(page, queryWrapper);
        List<ApiDeveloperManage> manageList = pageList.getRecords();
        if (CollUtil.isNotEmpty(manageList)) {
            List<String> tanantIds = manageList.stream().map(ApiDeveloperManage::getTenantId).collect(Collectors.toList());
            List<SysTenantInfo> tenantInfos = sysTenantInfoService.getByIds(tanantIds);
            if (CollUtil.isNotEmpty(tenantInfos)) {
                Map<String, SysTenantInfo> tenantInfoMap = tenantInfos.stream().collect(Collectors.toMap(SysTenantInfo::getId, t -> t, (k1, k2) -> k1));
                manageList.forEach(m -> {
                    m.setTenantName(tenantInfoMap.get(m.getTenantId()) != null ? tenantInfoMap.get(m.getTenantId()).getTenantName() : "");
                });
            }
        }

        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param apiDeveloperManage
     * @return
     */
    // @ApiOperation(value = "应用管理表-添加", notes = "应用管理表-添加")
    @PostMapping(value = "/add")
    @RequiresPermissions(value = {"system:manage"})
    public Result<?> add(@RequestBody ApiDeveloperManage apiDeveloperManage) {
        apiDeveloperManageService.saveExt(apiDeveloperManage);
        return Result.OK("添加成功！");
    }

    /**
     * 应用-发布/停用
     *
     * @param apiDeveloperManage
     * @return
     */
    // @ApiOperation(value = " 应用-发布/停用", notes = " 应用-发布/停用")
    @PutMapping(value = "/updateStatus")
    @RequiresPermissions(value = {"system:manage"})
    public Result<?> updateStatus(@RequestBody ApiDeveloperManage apiDeveloperManage) {
        apiDeveloperManageService.updateStatus(apiDeveloperManage);
        return Result.OK("发布/停用成功！");
    }

    /**
     * 编辑
     *
     * @param apiDeveloperManage
     * @return
     */
    // @ApiOperation(value = "应用管理表-编辑", notes = "应用管理表-编辑")
    @PutMapping(value = "/edit")
    @RequiresPermissions(value = {"system:manage"})
    public Result<?> edit(@RequestBody ApiDeveloperManage apiDeveloperManage) {
        apiDeveloperManageService.updateExt(apiDeveloperManage);
        return Result.OK("编辑成功!");
    }
}
