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

import java.util.List;

import cn.hutool.core.collection.CollUtil;
import com.kaifangqian.modules.opensign.constant.SignCommonConstant;
import com.kaifangqian.modules.opensign.service.flow.IFlowService;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.enums.TenantAuthStatus;
import com.kaifangqian.modules.system.enums.TenantType;
import com.kaifangqian.modules.system.service.*;
import com.kaifangqian.modules.system.vo.*;
import com.kaifangqian.annotation.ResrunLogMethod;
import com.kaifangqian.common.constant.CommonConstant;
import com.kaifangqian.common.enums.OperateLogType;
import com.kaifangqian.common.redis.util.RedisUtil;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.util.RsaUtils;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.modules.system.vo.*;
import com.kaifangqian.utils.MyStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;

// @Api(tags = "租户表")
@RestController
@RequestMapping("/system/sysTenantInfo")
@Slf4j
public class SysTenantInfoController {
    @Autowired
    private ISysTenantInfoService sysTenantInfoService;
    @Autowired
    private ISysTenantUserService iSysTenantUserService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ITenantInfoExtendService tenantInfoExtendService;
    @Autowired
    private ISysAuthGroupService iSysAuthGroupService;
    @Autowired
    private IFlowService flowService;
    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ISysDepartService sysDepartService;

    /**
     * 分页列表查询
     *
     * @param query
     * @param pageNo
     * @param pageSize
     * @return
     */
    // @ApiOperation(value = "租户表-分页列表查询", notes = "租户表-分页列表查询")
    @GetMapping(value = "/list")
    @RequiresPermissions("system:manage")
    public Result<?> queryPageList(SysTenantInfoQuery query, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<SysTenantInfoRes> page = new Page<SysTenantInfoRes>(pageNo, pageSize);
        IPage<SysTenantInfoRes> pageList = sysTenantInfoService.pageExt(page, query);
        return Result.OK(pageList);
    }

//    /**
//     * 开通租户--没有扩展表
//     *
//     * @param sysTenantInfoVO
//     * @return
//     */
//    // @ApiOperation(value = "开通租户", notes = "开通租户")
//    @PostMapping(value = "/addTenant")
//    public Result<?> addTenant(@RequestBody SysTenantInfoVO sysTenantInfoVO) {
//        sysTenantInfoService.addTenantAndAddApp(sysTenantInfoVO);
//        return Result.OK("开通租户成功！");
//    }

    /**
     * 停用启用租户
     *
     * @return
     */
    // @ApiOperation(value = "停用启用租户", notes = "停用启用租户")
    @PutMapping(value = "/updateTenantStatus")
    @RequiresPermissions("system:manage")
    public Result<?> updateTenantStatus(@RequestBody SysTenantInfo sysTenantInfo) {
        sysTenantInfoService.updateTenantStatus(sysTenantInfo.getId());
        return Result.OK("停用启用租户成功！");
    }

    /**
     * 查询租户已有应用列表
     *
     * @param tenantId
     * @return
     */
    // @ApiOperation(value = "查询租户已有应用列表", notes = "查询租户已有应用列表")
    @GetMapping(value = "/getTenantApps")
    @RequiresPermissions(value = {"system:manage","system:develop"},logical = Logical.OR)
    public Result<?> getTenantApps(@RequestParam(name = "tenantId", required = true) String tenantId) {
        List<SysTenantAppVersionVO> vos = sysTenantInfoService.getTenantApps(tenantId);
        return Result.OK(vos);
    }

    /**
     * 停用-启用租户应用
     *
     * @param sysTenantAppVersion
     * @return
     */
    // @ApiOperation(value = "停用-启用租户应用", notes = "停用-启用租户应用")
    @PutMapping(value = "/updateTenantAppStatus")
    @RequiresPermissions("system:manage")
    public Result<?> updateTenantAppStatus(@RequestBody SysTenantAppVersion sysTenantAppVersion) {
        sysTenantInfoService.updateTenantAppStatus(sysTenantAppVersion.getId());
        return Result.OK("停用启用租户应用成功！");
    }

//    /**
//     * 查询租户应用版本
//     *
//     * @param sysTenantAppVersion
//     * @return
//     */
//    // @ApiOperation(value = "查询租户应用版本", notes = "查询租户应用版本")
//    @GetMapping(value = "/getTenantAppVersions")
//    public Result<?> getTenantAppVersions(SysTenantAppVersion sysTenantAppVersion) {
//        List<SysAppVersion> versions = sysTenantInfoService.getTenantAppVersions(sysTenantAppVersion);
//        return Result.OK(versions);
//    }

//    /**
//     * 修改租户应用版本
//     *
//     * @param sysTenantAppVersion
//     * @return
//     */
//    // @ApiOperation(value = "修改租户应用版本", notes = "修改租户应用版本")
//    @PutMapping(value = "/updateTenantAppVersion")
//    public Result<?> updateTenantAppVersion(@RequestBody SysTenantAppVersion sysTenantAppVersion) {
//        sysTenantInfoService.updateTenantAppVersion(sysTenantAppVersion);
//        return Result.OK("修改租户应用版本成功！");
//    }

    /**
     * 查询所有的应用列表
     *
     * @param tenantId
     * @return
     */
    // @ApiOperation(value = "查询所有的应用列表", notes = "查询所有的应用列表")
    @GetMapping(value = "/getAllApps")
    @RequiresPermissions("system:develop")
    public Result<?> getAllApps(@RequestParam(name = "tenantId", required = true) String tenantId) {
        List<SysAppInfoVO> vos = sysTenantInfoService.getAllApps(tenantId);
        return Result.OK(vos);
    }

    /**
     * 新增团体租户-应用
     *
     * @param sysTenantInfoVO
     * @return
     */
    // @ApiOperation(value = "新增团体租户-应用", notes = "新增团体租户-应用")
    @PutMapping(value = "/addGroupTenantAppVersion")
    @RequiresPermissions("system:develop")
    public Result<?> addGroupTenantAppVersion(@RequestBody SysTenantInfoVO sysTenantInfoVO) {
        String tenantId = sysTenantInfoVO.getId();
        List<SysAuthGroup> sysAuthGroups = iSysAuthGroupService.getSystemGroup(tenantId);
        String allGroupId = null;
        String baseGroupId = null;
        String pId = null;
        if (CollUtil.isNotEmpty(sysAuthGroups)) {
            for (SysAuthGroup g : sysAuthGroups) {
                if (g.getGroupType() == 1) {
                    pId = g.getParentId();
                    allGroupId = g.getId();
                }
                if (g.getGroupType() == 2) {
                    baseGroupId = g.getId();
                }
            }
        }
        if (allGroupId != null && baseGroupId != null) {
            sysTenantInfoService.addGroupTenantAppVersion(pId, allGroupId, baseGroupId, sysTenantInfoVO);
        }
        return Result.OK("新增租户应用成功！");
    }

    /**
     * 租户-应用商城
     *
     * @return
     */
    // @ApiOperation(value = "租户-应用商城", notes = "租户-应用商城")
    @GetMapping(value = "/getTenantAllApps")
    public Result<?> getTenantAllApps() {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        List<SysAppInfoVO> vos = sysTenantInfoService.getAllApps(loginUser.getTenantId());
        return Result.OK(vos);
    }

    /**
     * 新增个人租户-应用
     *
     * @param sysTenantInfoVO
     * @return
     */
//    // @ApiOperation(value = "新增个人租户-应用", notes = "新增个人租户-应用")
//    @PutMapping(value = "/addPersonalTenantAppVersion")
//    public Result<?> addPersonalTenantAppVersion(@RequestBody SysTenantInfoVO sysTenantInfoVO) {
//        sysTenantInfoService.addPersonalTenantAppVersion(sysTenantInfoVO);
//        return Result.OK("新增租户应用成功！");
//    }

//    /**
//     * 用户被邀请加入的租户列表
//     *
//     * @return
//     */
//    // @ApiOperation(value = "用户被邀请加入的租户列表", notes = "用户被邀请加入的租户列表")
//    @GetMapping(value = "/getInviteList")
//    public Result<?> getInviteList() {
//        return Result.OK(iSysTenantUserService.getInviteList());
//    }
//
//    /**
//     * 用户确认、拒绝加入租户--登录系统后面对邀请用户同意或拒绝加入
//     *
//     * @return
//     */
//    // @ApiOperation(value = "用户确认、拒绝加入租户", notes = "用户确认、拒绝加入租户")
//    @PutMapping(value = "/jionOrRefuseTenant")
//    public Result<?> jionOrRefuseTenant(JionRefuseTenantVO vo) {
//        iSysTenantUserService.jionOrRefuseTenant(vo);
//        return Result.OK();
//    }

    /**
     * 用户加入已有企业--用户主动申请加入（通过企业邀请码）
     *
     * @return
     */
    //@RequiresPermissions(value = {"user:jionTenant"})
    @ResrunLogMethod(name = "用户加入已有企业", operateType = OperateLogType.OPERATE_TYPE_3)
    @RequestMapping(value = "/jionTenant", method = RequestMethod.PUT)
    public Result<?> jionTenant(@RequestBody InviteUserVO inviteUserVO) {
        return Result.OK(sysUserService.jionTenant(inviteUserVO));
    }

    /**
     * 开通个人租户
     *
     * @return
     */
    @ResrunLogMethod(name = "开通个人租户", operateType = OperateLogType.OPERATE_TYPE_3)
    @RequestMapping(value = "/openPersonalTenant", method = RequestMethod.PUT)
    public Result<?> openPersonalTenant() {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        SysTenantInfoVO sysTenantInfoVO = new SysTenantInfoVO();
        sysTenantInfoVO.setPhone(loginUser.getPhone());
        sysTenantInfoVO.setUsername(loginUser.getUsername());
        sysTenantInfoVO.setRealName(loginUser.getRealname());
        sysTenantInfoVO.setTenantType(TenantType.PERSONAL.getType());

        SysAddTenantInfoVO sysAddTenantInfoVO = sysTenantInfoService.addTenantAndAddApp(sysTenantInfoVO);

        String tenantId = sysAddTenantInfoVO.getTenantId();

        //维护租户扩展信息
        TenantInfoExtend tenantInfoExtend = new TenantInfoExtend();
        tenantInfoExtend.setTenantId(tenantId);
        tenantInfoExtend.setTenantType(TenantType.PERSONAL.getType());
//        tenantInfoExtend.setName(loginUser.getPhone());
        String phone = loginUser.getPhone();
        try {
            phone = RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING, phone);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tenantInfoExtend.setPhone(phone);
        //未认证
        tenantInfoExtend.setAuthStatus(TenantAuthStatus.STATUS0.getStatus());
        tenantInfoExtendService.save(tenantInfoExtend);

        //解析数据库已有数据做绑定表：sign_ru_relation sign_ru_task
        if (MyStringUtils.isNotBlank(loginUser.getPhone())) {
            SysUser sysUser = iSysUserService.getUserByPhone(loginUser.getPhone());
            if (sysUser != null) {
                SysTenantUser tenantUser = iSysTenantUserService.getPersonalTenantUser(sysUser.getId());
                if (tenantUser != null) {
                    //解析数据库已有数据做绑定表：sign_ru_relation
                    flowService.bindOutUserRelation(loginUser.getPhone(), null, tenantUser.getId(), "openPersonalTenant");
                    //解析数据库已有数据做绑定表:sign_ru_task
                    //绑定手机号+租户名（个人租户）：租户ID，租户账号ID
                    flowService.bindOutUserTask("个人租户", loginUser.getPhone(), null, null, null, sysUser.getId(), tenantId, tenantUser.getId(), "openPersonalTenant");
                }
            }
        }
        if (MyStringUtils.isNotBlank(loginUser.getEmail())) {
            SysUser sysUser = iSysUserService.getUserByEmail(loginUser.getEmail());
            if (sysUser != null) {
                SysTenantUser tenantUser = iSysTenantUserService.getPersonalTenantUser(sysUser.getId());
                if (tenantUser != null) {
                    //解析数据库已有数据做绑定表：sign_ru_relation
                    flowService.bindOutUserRelation(null, loginUser.getEmail(), tenantUser.getId(), "openPersonalTenant");
                    //解析数据库已有数据做绑定表:sign_ru_task
                    //绑定邮箱+租户名（个人租户）：租户ID，租户账号ID
                    flowService.bindOutUserTask("个人租户", null, loginUser.getEmail(), null, null, sysUser.getId(), tenantId, tenantUser.getId(), "openPersonalTenant");
                }
            }
        }
        //获取部门ID
        List<SysDepart> departs = sysDepartService.queryUserDeparts(loginUser.getId(), tenantId);
        String departId = null;
        if (CollUtil.isNotEmpty(departs)) {
            departId = departs.get(0).getId();
        }

        return Result.OK("", departId);
    }

    @ResrunLogMethod(name = "获取我的租户列表", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/listMyTenant", method = RequestMethod.GET)
    public Result<?> listMyTenant() {
        List<SysTenantInfoExtendVO> list = tenantInfoExtendService.listMyTenant();
        return Result.OK(list);
    }

    /**
     * 根据key获取租户名称
     *
     * @return
     */
    // @ApiOperation(value = "根据key获取租户名称", notes = "根据key获取租户名称")
    @GetMapping(value = "/getTenantNameByKey")
    public Result<?> getTenantNameByKey(@RequestParam(name = "redisKey", required = true) String redisKey) {
        Object userVo = redisUtil.get(CommonConstant.PREFIX_INVITE_USER_CODE + redisKey);
        if (userVo == null) {
            throw new PaasException("传值错误或邀请已失效");
        }
        InviteUserVO inviteUserVO = (InviteUserVO) userVo;
        SysTenantInfo sysTenantInfo = sysTenantInfoService.getById(inviteUserVO.getTenantId());
        if (sysTenantInfo != null) {
            inviteUserVO.setTenantName(sysTenantInfo.getTenantName());
            return Result.OK("成功", inviteUserVO);
        } else {
            throw new PaasException("传值错误或邀请已失效");
        }
    }


    /**
     * 查询租户实名认证状态
     *
     * @param tenantId
     * @return
     */
    // @ApiOperation(value = "查询租户实名认证状态", notes = "查询租户实名认证状态")
    @GetMapping(value = "/getTenantAuthStatus")
    public Result<?> getTenantAuthStatus(@RequestParam(name = "tenantId", required = true) String tenantId) {

        TenantInfoDTO tenantInfoExt = tenantInfoExtendService.getTenantInfoExt(tenantId);
        if (tenantInfoExt == null || tenantInfoExt.getAuthStatus() == null) {
            return Result.OK(TenantAuthStatus.STATUS0.getStatus());
        }
        return Result.OK(tenantInfoExt.getAuthStatus());
    }


    @ResrunLogMethod(name = "设置企业类型", operateType = OperateLogType.OPERATE_TYPE_3)
    @RequestMapping(value = "/setTenantBType", method = RequestMethod.PUT)
    @RequiresPermissions("system:manage")
    public Result<?> setTenantBType(@RequestBody TenantSysBTypeVO vo) {
        tenantInfoExtendService.updateSysBType(vo);
        return Result.OK();
    }

}
