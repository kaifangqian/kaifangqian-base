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
import com.kaifangqian.modules.storage.entity.AnnexStorage;
import com.kaifangqian.modules.storage.service.IAnnexStorageService;
import com.kaifangqian.modules.system.entity.SysTenantUser;
import com.kaifangqian.modules.system.entity.SysUser;
import com.kaifangqian.modules.system.entity.SysUserDepart;
import com.kaifangqian.modules.system.model.SysUserSearchModel;
import com.kaifangqian.modules.system.service.ISysDepartService;
import com.kaifangqian.modules.system.service.ISysTenantUserService;
import com.kaifangqian.modules.system.service.ISysUserDepartService;
import com.kaifangqian.modules.system.service.ISysUserService;
import com.kaifangqian.modules.system.util.ImageUtils;
import com.kaifangqian.modules.system.vo.SysUserDepVo;
import com.kaifangqian.modules.system.vo.SysUserEditDepartVO;
import com.kaifangqian.modules.system.vo.SysUserFrozenVO;
import com.kaifangqian.modules.system.vo.SysUserVO;
import com.kaifangqian.annotation.ResrunLogMethod;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.enums.OperateLogType;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.system.vo.*;
import com.kaifangqian.utils.IOUtils;
import com.kaifangqian.utils.MyFileUtil;
import lombok.extern.slf4j.Slf4j;
import com.kaifangqian.common.util.*;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.service.*;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @Author zhh
 * @since 2022-06-10
 */
@Slf4j
@RestController
@RequestMapping("/sys/user")
@ResrunLogModule(name = "用户管理模块")
public class SysUserController {
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysUserDepartService iSysUserDepartService;
    @Autowired
    private IAnnexStorageService iAnnexStorageService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private ISysTenantUserService iSysTenantUserService;

    /**
     * <p>
     * 部门搜索功能方法,根据关键字模糊搜索用户
     * </p>
     *
     * @param keyWord
     * @return
     */
    @ResrunLogMethod(name = "关键字查询用户列表", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/searchMyBy", method = RequestMethod.GET)
    public Result<IPage<SysUserSearchModel>> searchMyBy(@RequestParam(name = "keyWord", required = true) String keyWord, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        LoginUser user = MySecurityUtils.getCurrentUser();
        List<String> departIds = sysDepartService.queryDepartIdsByUsername(user.getUsername());
        Page<SysUserSearchModel> page = new Page<SysUserSearchModel>(pageNo, pageSize);
        IPage<SysUserSearchModel> result = sysUserService.searchMyBy(keyWord, departIds, page);
        return Result.OK(result);
    }

//    @RequiresPermissions("user:check")
    @ResrunLogMethod(name = "检测用户", operateType = OperateLogType.OPERATE_TYPE_2)
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public Result<?> check(String username, String accountType) {
        boolean result = sysUserService.check(username, accountType);
        String mes = "ok";
        if (!result) {
            String account = sysUserService.getUsername(username, accountType);
            mes = username + "被" + account + "账号绑定，请添加" + account;
        }
        return Result.OK(mes, result);
    }

    @RequiresPermissions("user:add")
    @ResrunLogMethod(name = "新增用户", operateType = OperateLogType.OPERATE_TYPE_2)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<?> add(@RequestBody SysUserVO sysUser) {
        sysUserService.saveUser(sysUser);
        return Result.OK();
    }


    //根据SysTenantUser id 查询详情
    @RequestMapping(value = "/tenantUserInfo", method = RequestMethod.GET)
    @ResrunLogMethod(name = "查询用户详情", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<?> tenantUserinfo(@RequestParam(name = "id", required = true) String id) {
        SysTenantUser sysTenantUser = iSysTenantUserService.getById(id);
        SysUserVO userVO = new SysUserVO();
        userVO.setId(id);
        if (sysTenantUser != null) {
            userVO.setNickName(sysTenantUser.getNickName());
            SysUser sysUser = sysUserService.getById(sysTenantUser.getUserId());
            userVO.setUsername(sysUser.getUsername());
            userVO.setPhone(sysUser.getPhone());
            userVO.setEmail(sysTenantUser.getEmail());
            //部门
            SysUserDepart query = new SysUserDepart();
            query.setTenantId(sysTenantUser.getTenantId());
            query.setUserId(sysTenantUser.getUserId());
            List<SysUserDepart> sysUserDeparts = iSysUserDepartService.getByEntity(query);
            if (CollUtil.isNotEmpty(sysUserDeparts)) {
                List<String> departIds = sysUserDeparts.stream().map(SysUserDepart::getDepartId).collect(Collectors.toList());
                List<String> manageDepartIds = sysUserDeparts.stream().filter((depart) -> depart.getManageFlag()).map(SysUserDepart::getDepartId).collect(Collectors.toList());
                userVO.setDepartIds(departIds);
                userVO.setManageDepartIds(manageDepartIds);
            }
            //角色
//            List<SysUserRole> sysUserRoles = iSysUserRoleService.queryByUserIdAndTenantId(sysTenantUser.getUserId(), sysTenantUser.getTenantId());
//            if (CollUtil.isNotEmpty(sysUserRoles)) {
//                List<String> roleIds = sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
//                userVO.setRoleIds(roleIds);
//            }
        }
        return Result.OK(userVO);
    }

    //sysUser id 查询详情
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    @ResrunLogMethod(name = "查询用户详情", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<?> userInfo(@RequestParam(name = "id", required = true) String id) {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        SysUserVO userVO = new SysUserVO();
        SysTenantUser tenantUser = iSysTenantUserService.getTenantUser(loginUser.getTenantId(), id);
        if (tenantUser != null) {
            userVO.setId(tenantUser.getId());
            userVO.setNickName(tenantUser.getNickName());
        }
        SysUser sysUser = sysUserService.getById(id);
        userVO.setPhone(sysUser.getPhone());
        //部门
        SysUserDepart query = new SysUserDepart();
        query.setTenantId(loginUser.getTenantId());
        query.setUserId(id);
        List<SysUserDepart> sysUserDeparts = iSysUserDepartService.getByEntity(query);
        if (CollUtil.isNotEmpty(sysUserDeparts)) {
            List<String> departIds = sysUserDeparts.stream().map(SysUserDepart::getDepartId).collect(Collectors.toList());
            List<String> manageDepartIds = sysUserDeparts.stream().filter((depart) -> depart.getManageFlag()).map(SysUserDepart::getDepartId).collect(Collectors.toList());
            userVO.setDepartIds(departIds);
            userVO.setManageDepartIds(manageDepartIds);
        }
        //角色
//            List<SysUserRole> sysUserRoles = iSysUserRoleService.queryByUserIdAndTenantId(sysTenantUser.getUserId(), sysTenantUser.getTenantId());
//            if (CollUtil.isNotEmpty(sysUserRoles)) {
//                List<String> roleIds = sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
//                userVO.setRoleIds(roleIds);
//            }

        return Result.OK(userVO);
    }

    @RequiresPermissions("user:edit")
    @ResrunLogMethod(name = "编辑用户", operateType = OperateLogType.OPERATE_TYPE_3)
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<SysUser> edit(@RequestBody SysUserVO sysUser) {
        sysUserService.editUser(sysUser);
        return Result.OK();
    }

    /**
     * 批量删除用户
     */
    @RequiresPermissions({"user:deletebatch"})
    @ResrunLogMethod(name = "批量删除用户", operateType = OperateLogType.OPERATE_TYPE_3)
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        List<String> tenantUserIdList = Arrays.asList(ids.split(","));
        this.sysUserService.deleteBatchUsers(tenantUserIdList);
        return Result.OK("批量删除用户成功");
    }

    /**
     * 调整部门
     *
     * @return
     */
    @RequiresPermissions({"user:changedepart"})
    @ResrunLogMethod(name = "用户批量调整部门", operateType = OperateLogType.OPERATE_TYPE_3)
    @RequestMapping(value = "/updateDepartBatch", method = RequestMethod.PUT)
    public Result<?> updateDepartBatch(@RequestBody SysUserEditDepartVO editDepartVO) {
        sysUserService.updateDepartBatch(editDepartVO);
        return Result.OK();
    }

    /**
     * 冻结&解冻用户
     *
     * @return
     */
    @RequiresPermissions(value = {"user:freeze", "user:unfreeze"}, logical = Logical.OR)
    @ResrunLogMethod(name = "冻结、解冻用户", operateType = OperateLogType.OPERATE_TYPE_3)
    @RequestMapping(value = "/frozenBatch", method = RequestMethod.PUT)
    public Result<?> frozenBatch(@RequestBody SysUserFrozenVO user) {
        sysUserService.frozenBatch(user);
        return Result.OK();
    }


    @ResrunLogMethod(name = "更新用户真实名字", operateType = OperateLogType.OPERATE_TYPE_3)
    @RequestMapping(value = "/updateRealname", method = RequestMethod.POST)
    public Result<?> updateRealname(@RequestBody SysUserVO sysUser) {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        SysUser user = new SysUser();
        user.setId(loginUser.getId());
        user.setRealname(sysUser.getRealname());

        sysUserService.updateById(user);
        return Result.OK();
    }

    @ResrunLogMethod(name = "更新用户头像", operateType = OperateLogType.OPERATE_TYPE_3)
    @RequestMapping(value = "/uploadAvatar", method = RequestMethod.POST)
    public Result<?> uploadAvatar(@RequestParam("file") MultipartFile file) {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        AnnexStorage annexStorage = new AnnexStorage();
        //文件上传
        String id = iAnnexStorageService.create(annexStorage, file);

        //压缩文件
        String base64 = null;
        String base64Pre = null;
        if (!file.isEmpty()) {
            //创建临时文件
            File fileTem = null;
            InputStream inputStream = null;
            try {
                String suffix = MyFileUtil.getExtensionName(file.getOriginalFilename());
                base64Pre = Base64Utils.getBase64Pre(suffix);
                fileTem = File.createTempFile(UUID.randomUUID().toString(), "." + suffix);
                inputStream = file.getInputStream();
                ImageUtils.compressImg(inputStream, fileTem, 80, 80);
                byte[] bytes = IOUtils.toByteArray(fileTem);
                base64 = Base64.getEncoder().encodeToString(bytes).trim();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileTem.exists()) {
                    fileTem.delete();
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        //用户更新
        SysUser user = new SysUser();
        user.setId(loginUser.getId());
        user.setAvatar(id);
        user.setAvatarBase64(base64 == null ? null : base64Pre + base64.replaceAll("\n", "").replaceAll("\r", ""));

        sysUserService.updateById(user);
        return Result.OK();
    }

    // 下面暂时没用

    /**
     * @param checkType：username/phone/email
     * @create by zhenghuihan
     * @createTime 2022/8/31 15:46
     * @description 校验是否重复 true: 已存在 false：未存在
     */
    @ResrunLogMethod(name = "校验用户唯一性字段", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/checkExist", method = RequestMethod.GET)
    public Result<?> checkRepeat(@RequestParam(name = "type", required = true) String checkType, @RequestParam(name = "key", required = true) String key, String userId) {
        Map<String, Boolean> result = new HashMap<>();
        result.put("checkResult", sysUserService.checkRepeat(checkType, key, userId));
        return Result.OK(result);
    }

    /**
     * 密码重置
     */
    @RequiresPermissions({"user:resetpassword"})
    @ResrunLogMethod(name = "重置密码", operateType = OperateLogType.OPERATE_TYPE_3)
    @RequestMapping(value = "/resetPassword", method = RequestMethod.PUT)
    public Result<?> resetPassword(@RequestBody SysUserFrozenVO sysUser) {
        sysUserService.resetPassword(sysUser);
        return Result.OK();
    }

    /**
     * 查询所有账号列表
     *
     * @return
     */
    @GetMapping("/getAllUsers")
    @ResrunLogMethod(name = "查询所有账号列表", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<List<SysUserDepVo>> getAllUsers() {
        List<SysUser> sysUsers = sysUserService.listAll();
        List<SysUserDepVo> sysUserDepVos = new ArrayList<>();
        if (CollUtil.isNotEmpty(sysUsers)) {
            sysUsers.forEach(u -> {
                SysUserDepVo vo = new SysUserDepVo();
                vo.setUserId(u.getId());
                vo.setUsername(u.getUsername());
                vo.setRealname(u.getRealname());

                sysUserDepVos.add(vo);
            });
        }

        return Result.OK(sysUserDepVos);
    }
}
