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
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.modules.api.service.IApiRelationLinkService;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.enums.TenantAuthStatus;
import com.kaifangqian.modules.system.enums.TenantType;
import com.kaifangqian.modules.system.enums.UpdatePasswordEnum;
import com.kaifangqian.modules.system.mapper.SysUserMapper;
import com.kaifangqian.modules.system.model.SysUserSearchModel;
import com.kaifangqian.modules.system.service.*;
import com.kaifangqian.modules.system.vo.*;
import com.kaifangqian.common.constant.*;
import com.kaifangqian.common.constant.enums.AuthType;
import com.kaifangqian.common.redis.util.RedisUtil;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.util.RsaUtils;
import com.kaifangqian.common.util.oConvertUtils;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.enums.SysConfigType;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.inteface.CommonToolsAPI;
import com.kaifangqian.modules.opensign.constant.SignCommonConstant;
import com.kaifangqian.modules.opensign.service.flow.IFlowService;
import com.kaifangqian.modules.system.service.*;
import com.kaifangqian.modules.system.vo.*;
import com.kaifangqian.service.ISysUserPasswordHisService;
import com.kaifangqian.utils.*;
import lombok.extern.slf4j.Slf4j;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private ISysUserDepartService iSysUserDepartService;
    @Autowired
    private ISysUserPasswordHisService iSysUserPasswordHisService;
    @Autowired
    private CommonToolsAPI commonToolsAPI;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ISysAuthGroupMemberService iSysAuthGroupMemberService;
    @Autowired
    private ISysUserRoleService iSysUserRoleService;
    @Autowired
    private ISysTenantUserService iSysTenantUserService;
    @Autowired
    private ISysTenantUserAppService iSysTenantUserAppService;
    @Autowired
    private ISysTenantInfoService iSysTenantInfoService;
    @Autowired
    private ISysDepartService iSysDepartService;
    @Autowired
    private ISysAppInfoService sysAppInfoService;
    @Autowired
    private ISysAuthGroupService iSysAuthGroupService;
    @Autowired
    private ISysTenantUserRecordService sysTenantUserRecordService;
    @Autowired
    private ISysTenantConfigService sysTenantConfigService;
    @Autowired
    private IFlowService flowService;
    @Autowired
    private IApiRelationLinkService apiRelationLinkService;
    @Autowired
    private ITenantInfoExtendService tenantInfoExtendService;


    @Override
    public IPage<SysUserSearchModel> searchMyBy(String keyWord, List<String> departIds, Page<SysUserSearchModel> page) {
        LoginUser user = MySecurityUtils.getCurrentUser();
        return sysUserMapper.queryDepartAllUsers(page, keyWord, departIds, user.getTenantId());
    }

    @Override
    public boolean checkRepeat(String checkType, String key, String userId) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getDeleteFlag, StatusConstant.DEL_FLAG_0).ne(MyStringUtils.isNotBlank(userId), SysUser::getId, userId);
        if ("username".equals(checkType)) {
            wrapper.eq(SysUser::getUsername, key);
            List<SysUser> users = this.list(wrapper);
            if (CollUtil.isNotEmpty(users)) {
                return true;
            } else {
                return false;
            }
        } else if ("phone".equals(checkType)) {
            wrapper.eq(SysUser::getPhone, key);
            List<SysUser> users = this.list(wrapper);
            if (CollUtil.isNotEmpty(users)) {
                return true;
            } else {
                return false;
            }
        } else if ("email".equals(checkType)) {
            wrapper.eq(SysUser::getEmail, key);
            List<SysUser> users = this.list(wrapper);
            if (CollUtil.isNotEmpty(users)) {
                return true;
            } else {
                return false;
            }
        } else {
            throw new PaasException("校验类型不存在");
        }
    }

    @Override
    public void resetPassword(SysUserFrozenVO sysUser) {
        if (CollUtil.isNotEmpty(sysUser.getIds())) {
            List<SysUser> users = new ArrayList<>();
            sysUser.getIds().forEach(i -> {
                SysUser user = this.getById(i);
                if (user != null) {
                    String password = PasswordUtil.encrypt(user.getUsername(), sysUser.getPassword(), user.getSalt());
                    user.setPassword(password);
                    user.setUpdatePasswordTime(new Date());

                    users.add(user);
                }
            });
            if (CollUtil.isNotEmpty(users)) {
                this.updateBatchById(users);
            }
        }
    }

    @Override
    public Result<?> updatePasswordByOldPassword(UserPasswordUpdateVO vo) {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        SysUser user = this.getById(loginUser.getId());
        String passwordEncode = PasswordUtil.encrypt(user.getUsername(), vo.getOldpassword(), user.getSalt());
        if (!user.getPassword().equals(passwordEncode)) {
            return Result.error("旧密码输入错误!");
        }
        if (!vo.getNewpassword().equals(vo.getConfirmpassword())) {
            return Result.error("两次输入密码不一致!");
        }
        String password = PasswordUtil.encrypt(user.getUsername(), vo.getNewpassword(), user.getSalt());
        return updatePassword(user.getId(), password, vo.getPasswordLevel());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> updatePasswordByCode(UserPasswordUpdateVO vo) {
        String key = null;
        if (UpdatePasswordEnum.PHONE.getType().equals(vo.getType())) {
            key = vo.getPhone();
        } else if (UpdatePasswordEnum.EMIAL.getType().equals(vo.getType())) {
            key = vo.getEmail();
        }
        String realKey = MD5Util.MD5Encode(key + vo.getCaptchaKey(), "utf-8");
        Object checkCode = redisUtil.get(realKey);
        if (checkCode == null || !checkCode.toString().equals(vo.getCaptcha())) {
            return Result.error("验证码错误");
        }
        //删除redis中的验证码
        redisUtil.del(realKey);
        SysUser user = null;
        if (UpdatePasswordEnum.PHONE.getType().equals(vo.getType())) {
            user = this.getUserByPhone(vo.getPhone());
        } else if (UpdatePasswordEnum.EMIAL.getType().equals(vo.getType())) {
            user = this.getUserByEmail(vo.getEmail());
        }
        if (user != null) {
            String password = PasswordUtil.encrypt(user.getUsername(), vo.getNewpassword(), user.getSalt());
            return updatePassword(user.getId(), password, vo.getPasswordLevel());
        } else {
            return Result.error("用户不存在");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> updatePasswordByCodeUserToken(UserPasswordUpdateVO vo) {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        String key = null;
        if (UpdatePasswordEnum.PHONE.getType().equals(vo.getType())) {
            key = loginUser.getPhone();
        } else if (UpdatePasswordEnum.EMIAL.getType().equals(vo.getType())) {
            key = loginUser.getEmail();
        }
        String realKey = MD5Util.MD5Encode(key + vo.getCaptchaKey(), "utf-8");
        Object checkCode = redisUtil.get(realKey);
        if (checkCode == null || !checkCode.toString().equals(vo.getCaptcha())) {
            return Result.error("验证码错误");
        }
        //删除redis中的验证码
        redisUtil.del(realKey);
        SysUser user = this.getById(loginUser.getId());
        if (user != null) {
            String password = PasswordUtil.encrypt(user.getUsername(), vo.getNewpassword(), user.getSalt());
            return updatePassword(user.getId(), password, vo.getPasswordLevel());
        } else {
            return Result.error("用户不存在");
        }
    }

    private Result<?> updatePassword(String userId, String password, String passwordLevel) {
        Integer limit = commonToolsAPI.getConfigIntegerValueByType(SysConfigType.PASSWOERREPEATLIMIT.getType());

        if (limit == 0 || !iSysUserPasswordHisService.isRepeatPassword(userId, password, limit)) {
            iSysUserPasswordHisService.addPassword(userId, password);

            this.sysUserMapper.update(new SysUser().setPassword(password).setUpdatePasswordTime(new Date()).setPasswordLevel(passwordLevel), new LambdaQueryWrapper<SysUser>().eq(SysUser::getId, userId));

            return Result.OK("密码修改成功!");
        } else {
            return Result.error("密码与前" + limit + "次重复，请设置新的密码");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatchUsers(List<String> tenantUserIds) {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        List<SysTenantUser> sysTenantUsers = iSysTenantUserService.getByIds(tenantUserIds);
        if (CollUtil.isNotEmpty(sysTenantUsers)) {
            sysTenantUsers.forEach(u -> {
                checkSystemUser2(u);
                u.setDeleteFlag(true);
                u.setDeleteTime(new Date());
                u.setDeleteBy(loginUser.getId());
            });
            //1.删除用户
            iSysTenantUserService.updateBatchById(sysTenantUsers);

            List<String> userIds = sysTenantUsers.stream().map(SysTenantUser::getUserId).collect(Collectors.toList());
            //2.删除用户部门
            iSysUserDepartService.deleteByTenantIdAndUserIds(loginUser.getTenantId(), userIds);

            //3.删除用户角色
            iSysUserRoleService.deleteByTenantIdAndUserIds(loginUser.getTenantId(), userIds);

            //4.删除用户权限
            iSysAuthGroupMemberService.deleteByTenantIdAndTypeAndAuthIds(loginUser.getTenantId(), AuthType.USER, userIds);

            //5.删除租户用户应用
            iSysTenantUserAppService.deleteByTenantIdUserIds(loginUser.getTenantId(), userIds);

            //6.删除租户-用户外部关联关系
            apiRelationLinkService.removeBySystemIds(ApiConstants.EXTEND_TYPE_USER, tenantUserIds);
        }
    }

    @Override
    public void frozenBatch(SysUserFrozenVO frozenVO) {
        List<SysTenantUser> users = new ArrayList<>();
        if (CollUtil.isNotEmpty(frozenVO.getIds())) {
            frozenVO.getIds().forEach(i -> {
                checkSystemUser(i);
                SysTenantUser user = new SysTenantUser();
                user.setId(i);
                user.setStatus(frozenVO.getStatus());

                users.add(user);
            });
        }
        if (CollUtil.isNotEmpty(users)) {
            iSysTenantUserService.updateBatchById(users);
        }
    }

    void checkSystemUser(String tenantUserId) {
        SysTenantUser tenantUser = iSysTenantUserService.getById(tenantUserId);
        if (tenantUser.getAddType().equals("system")) {
            throw new PaasException("系统管理员账号不可以冻结");
        }
    }

    void checkSystemUser2(SysTenantUser tenantUser) {
        if (tenantUser.getAddType().equals("system")) {
            throw new PaasException("系统管理员账号不可以删除");
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateDepartBatch(SysUserEditDepartVO sysUser) {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        if (CollUtil.isNotEmpty(sysUser.getIds())) {
            //1、校验
            if (CollUtil.isEmpty(sysUser.getNewDepartIds())) {
                throw new PaasException("目标部门不能为空");
            }
            if (MyStringUtils.isBlank(sysUser.getHisDepartId())) {
                throw new PaasException("当前操作部门不能为空");
            }
            List<SysTenantUser> sysTenantUsers = iSysTenantUserService.getByIds(sysUser.getIds());
            if (CollUtil.isNotEmpty(sysTenantUsers)) {
                List<String> userIds = sysTenantUsers.stream().map(SysTenantUser::getUserId).collect(Collectors.toList());
                //判断是否需要移除
                if (!sysUser.getNewDepartIds().contains(sysUser.getHisDepartId())) {
                    //2、删除历史部门
                    userIds.forEach(i -> {
                        iSysUserDepartService.deleteByDepartIdAndUserId(sysUser.getHisDepartId(), i);
                    });
                }

                //3、新增新的部门
                userIds.forEach(u -> {
                    sysUser.getNewDepartIds().forEach(d -> {
                        SysUserDepart userDepart = new SysUserDepart(loginUser.getTenantId(), u, d, false);

                        iSysUserDepartService.addExt(userDepart);
                    });
                });
            }
        }
    }

    @Override
    public SysUser getUserByName(String username) {
        return sysUserMapper.getUserByName(username);
    }

    @Override
    public List<SysUser> getUserByStatus(Integer status) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getStatus, status);

        return this.list(wrapper);
    }

    @Override
    public Map<String, String> getDepNamesByUserIds(List<String> userIds) {
        List<SysUserDepVo> list = this.baseMapper.getDepNamesByUserIds(userIds);

        Map<String, String> res = new HashMap<String, String>();
        list.forEach(item -> {
            if (res.get(item.getUserId()) == null) {
                res.put(item.getUserId(), item.getDepartName());
            } else {
                res.put(item.getUserId(), res.get(item.getUserId()) + "," + item.getDepartName());
            }
        });
        return res;
    }

    @Override
    @CacheEvict(value = {CacheConstant.SYS_USERS_CACHE}, key = "#username")
    public void updateUserDepart(String username, String orgCode) {
        baseMapper.updateUserDepart(username, orgCode);
    }

    @Override
    public SysUser getUserByPhone(String phone) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getPhone, phone).eq(SysUser::getDeleteFlag, 0);
        List<SysUser> users = this.list(queryWrapper);
        if (CollUtil.isNotEmpty(users)) {
            return users.get(0);
        }
        return null;
    }


    @Override
    public SysUser getUserByEmail(String email) {
        return sysUserMapper.getUserByEmail(email);
    }

    /**
     * 校验用户是否有效
     *
     * @param sysUser
     * @return
     */
    @Override
    public Result<?> checkUserIsEffective(SysUser sysUser) {
        Result<?> result = new Result<Object>();
        //情况1：根据用户信息查询，该用户不存在
        if (sysUser == null) {
            result.error500("用户不存在，请使用验证码登录");
            return result;
        }
        //情况2：根据用户信息查询，该用户已注销
        if (StatusConstant.DEL_FLAG_1.equals(sysUser.getDeleteFlag())) {
            result.error500("该用户已注销");
            return result;
        }
        //情况3：根据用户信息查询，该用户已冻结
        if (CommonConstant.USER_FREEZE.equals(sysUser.getStatus())) {
            result.error500("该用户已冻结");
            return result;
        }
        //情况4：根据用户信息查询，该用户已自动冻结，等待一定时间后恢复
        if (CommonConstant.USER_FREEZE_AUTO.equals(sysUser.getStatus())) {
            result.error500("该用户已冻结，请到" + DateUtil.formatTime(sysUser.getFreezeTime()) + "再试。");
            return result;
        }
        return result;
    }

    @Override
    public boolean check(String username, String accountType) {
        SysUser userTem = getUserByName(username);
        if (userTem == null) {
            if (accountType.equals(UpdatePasswordEnum.PHONE.getType())) {
                userTem = getUserByPhone(username);
            } else if (accountType.equals(UpdatePasswordEnum.EMIAL.getType())) {
                userTem = getUserByEmail(username);
            } else {
                throw new PaasException("账号类型不正确");
            }
            if (userTem != null) {
                return false;
            }
            return true;
        } else {
            return true;
        }
    }

    @Override
    public String getUsername(String username, String accountType) {
        SysUser userTem = getUserByName(username);
        if (userTem == null) {
            if (accountType.equals(UpdatePasswordEnum.PHONE.getType())) {
                userTem = getUserByPhone(username);
            } else if (accountType.equals(UpdatePasswordEnum.EMIAL.getType())) {
                userTem = getUserByEmail(username);
            } else {
                throw new PaasException("账号类型不正确");
            }
            if (userTem != null) {
                return userTem.getUsername();
            }
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(SysUserVO sysUser) {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        if (MyStringUtils.isBlank(sysUser.getUsername())) {
            throw new PaasException("账号不能为空");
        }
        if (MyStringUtils.isBlank(sysUser.getAccountType())) {
            throw new PaasException("账号类型不能为空");
        }
        String userId = null;
        SysUser userTem = getUserByName(sysUser.getUsername());
        if (userTem == null) {
            if (sysUser.getAccountType().equals(UpdatePasswordEnum.PHONE.getType())) {
                userTem = getUserByPhone(sysUser.getUsername());
            } else if (sysUser.getAccountType().equals(UpdatePasswordEnum.EMIAL.getType())) {
                userTem = getUserByEmail(sysUser.getUsername());
            } else {
                throw new PaasException("账号类型不正确");
            }
            if (userTem == null) {
                SysUser user = new SysUser();
                if (sysUser.getAccountType().equals(UpdatePasswordEnum.PHONE.getType())) {
                    user.setPhone(sysUser.getUsername());
                } else if (sysUser.getAccountType().equals(UpdatePasswordEnum.EMIAL.getType())) {
                    user.setEmail(sysUser.getUsername());
                }
                user.setRealname(sysUser.getNickName() != null ? sysUser.getNickName() : sysUser.getUsername());
                user.setUsername(sysUser.getUsername());
                String password = MD5Util.MD5Encode(RandomUtil.randomString(16), "UTF-8");
                if (MyStringUtils.isBlank(sysUser.getPassword())) {
                    user.setPassword(password);
                } else {
                    user.setPassword(sysUser.getPassword());
                }
                String salt = oConvertUtils.randomGen(8);
                user.setSalt(salt);
                String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), salt);
                user.setPassword(passwordEncode);
                user.setStatus(0);
                user.setUpdatePasswordTime(new Date());
                //step.1 保存用户
                this.save(user);
                userId = user.getId();
            } else {
                userId = userTem.getId();
                //判断租户下是否已经存在该用户，不可以重复添加
                SysTenantUser tenantUser = iSysTenantUserService.getTenantUser(loginUser.getTenantId(), userId);
                if (tenantUser != null) {
                    throw new PaasException("租户下已经添加该用户，不可以重复添加");
                }
            }
        } else {
            userId = userTem.getId();
            //判断租户下是否已经存在该用户，不可以重复添加
            SysTenantUser tenantUser = iSysTenantUserService.getTenantUser(loginUser.getTenantId(), userId);
            if (tenantUser != null) {
                throw new PaasException("租户下已经添加该用户，不可以重复添加");
            }
        }

//        //step.2 保存角色
//        if (CollUtil.isNotEmpty(sysUser.getRoleIds())) {
//            for (String i : sysUser.getRoleIds()) {
//                SysUserRole userRole = new SysUserRole(userId, i, loginUser.getTenantId());
//                sysUserRoleMapper.insert(userRole);
//            }
//        }

        //step.3 保存所属部门
        if (CollUtil.isNotEmpty(sysUser.getDepartIds())) {
            for (String i : sysUser.getDepartIds()) {
                SysUserDepart userDeaprt = new SysUserDepart(loginUser.getTenantId(), userId, i, false);
                iSysUserDepartService.addExt(userDeaprt);
            }
        }
        //step.4 保存管理部门
        if (CollUtil.isNotEmpty(sysUser.getManageDepartIds())) {
            for (String i : sysUser.getManageDepartIds()) {
                SysUserDepart userDeaprt = new SysUserDepart(loginUser.getTenantId(), userId, i, true);
                iSysUserDepartService.addExt(userDeaprt);
            }
        }

        //step.5 新增租户-用户
        SysTenantUser tenantUser = new SysTenantUser();
        tenantUser.setTenantId(loginUser.getTenantId());
        tenantUser.setUserId(userId);
        tenantUser.setNickName(sysUser.getNickName());
        tenantUser.setStatus(0);
        tenantUser.setAddType("user");
        iSysTenantUserService.save(tenantUser);

        //发送提醒
        iSysDepartService.sendInviteMes(tenantUser.getId());
    }

    void checkUserRepeat(SysUserVO sysUser) {
        if (MyStringUtils.isNotBlank(sysUser.getUsername())) {
            if (checkRepeat("username", sysUser.getUsername(), sysUser.getId())) {
                throw new PaasException("用户名不可以重复");
            }
        }
        if (MyStringUtils.isNotBlank(sysUser.getPhone())) {
            if (checkRepeat("phone", sysUser.getPhone(), sysUser.getId())) {
                throw new PaasException("手机号不可以重复");
            }
        }
        if (MyStringUtils.isNotBlank(sysUser.getUsername())) {
            if (checkRepeat("email", sysUser.getEmail(), sysUser.getId())) {
                throw new PaasException("邮箱不可以重复");
            }
        }
    }

    @Override
    public void saveVisitor(SysUser sysUser) {
        sysUser.setStatus(1);
        sysUser.setUpdatePasswordTime(new Date());
        sysUser.setUserType("visitor");
        sysUser.setDeleteFlag(TrueFalseConstant.falseboo);
        sysUser.setUsername(sysUser.getPhone());
        sysUser.setRealname(sysUser.getPhone());
        String salt = oConvertUtils.randomGen(8);
        sysUser.setSalt(salt);
        String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), sysUser.getPassword(), salt);
        sysUser.setPassword(passwordEncode);
        //校验用户
        SysUserVO sysUserVO = new SysUserVO();
        sysUserVO.setUsername(sysUser.getUsername());
        sysUserVO.setPhone(sysUser.getPhone());
        sysUserVO.setEmail(sysUser.getEmail());
        checkUserRepeat(sysUserVO);
        //step.1 保存用户
        this.save(sysUser);
        //step.2 保存部门
        // 临时用户租户id确认
        SysUserDepart userDeaprt = new SysUserDepart("", sysUser.getId(), "e0ca385b-1d8d-4e8d-8df5-42bb65798171", true);
        iSysUserDepartService.addExt(userDeaprt);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editUser(SysUserVO sysUser) {
        //step.1 修改租户用户信息
        if (MyStringUtils.isBlank(sysUser.getId())) {
            throw new PaasException("唯一标识不能为空");
        }
        SysTenantUser sysTenantUser = iSysTenantUserService.getById(sysUser.getId());
        if (sysTenantUser != null) {
            sysTenantUser.setNickName(sysUser.getNickName());
            sysTenantUser.setEmail(sysUser.getEmail());
            iSysTenantUserService.updateById(sysTenantUser);

//            //step.2 修改角色
//            //处理用户角色 先删后加
//            sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, loginUser.getId()));
//            if (CollUtil.isNotEmpty(sysUser.getRoleIds())) {
//                sysUser.getRoleIds().forEach(i -> {
//                    SysUserRole userRole = new SysUserRole(loginUser.getId(), i, loginUser.getTenantId());
//                    sysUserRoleMapper.insert(userRole);
//                });
//            }

            //step.3 修改部门
            //先删后加
            iSysUserDepartService.deleteByTenantIdAndUserId(sysTenantUser.getTenantId(), sysTenantUser.getUserId());
            //保存所属部门
            if (CollUtil.isNotEmpty(sysUser.getDepartIds())) {
                sysUser.getDepartIds().forEach(i -> {
                    SysUserDepart userDeaprt = new SysUserDepart(sysTenantUser.getTenantId(), sysTenantUser.getUserId(), i, false);
                    iSysUserDepartService.addExt(userDeaprt);
                });
            }
            //保存管理部门
            if (CollUtil.isNotEmpty(sysUser.getManageDepartIds())) {
                sysUser.getManageDepartIds().forEach(i -> {
                    SysUserDepart userDeaprt = new SysUserDepart(sysTenantUser.getTenantId(), sysTenantUser.getUserId(), i, true);
                    iSysUserDepartService.addExt(userDeaprt);
                });
            }
        }
    }

    @Override
    public List<SysUser> findByUserIds(List<String> userIds) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysUser::getId, userIds);
        return sysUserMapper.selectList(queryWrapper);
    }

    @Override
    public List<SysUser> listAll() {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getDeleteFlag, false);

        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void userJion(UserJionSystemVO vo) {
        if (MyStringUtils.isBlank(vo.getPhone()) && MyStringUtils.isBlank(vo.getEmail())) {
            throw new PaasException("账号不能为空");
        }
        String channal = "";
        if (MyStringUtils.isNotBlank(vo.getPhone())) {
            channal = vo.getPhone();
        } else if (MyStringUtils.isNotBlank(vo.getEmail())) {
            channal = vo.getEmail();
        }
        String realKey = MD5Util.MD5Encode(channal + vo.getCaptchaKey(), "utf-8");
        Object checkCode = redisUtil.get(realKey);
        if (checkCode == null || !checkCode.toString().equals(vo.getCaptcha())) {
            throw new PaasException("验证码错误");
        }
        //维护用户信息
        String userId = null;
        SysUser userTem = null;
        userTem = getUserByName(channal);
        if (userTem == null) {
            if (MyStringUtils.isNotBlank(vo.getPhone())) {
                userTem = getUserByPhone(vo.getPhone());
            } else if (MyStringUtils.isNotBlank(vo.getEmail())) {
                userTem = getUserByEmail(vo.getEmail());
            }
            if (userTem == null) {

                userTem = new SysUser();
                userTem.setRealname(vo.getRealName());
                userTem.setUsername(channal);
                if (MyStringUtils.isBlank(vo.getPassword())) {
                    String password = MD5Util.MD5Encode(RandomUtil.randomString(16), "UTF-8");
                    userTem.setPassword(password);
                } else {
                    userTem.setPassword(vo.getPassword());
                }
                String salt = oConvertUtils.randomGen(8);
                userTem.setSalt(salt);
                String passwordEncode = PasswordUtil.encrypt(userTem.getUsername(), userTem.getPassword(), salt);
                userTem.setPassword(passwordEncode);
                userTem.setStatus(1);
                userTem.setUpdatePasswordTime(new Date());
                userTem.setPhone(vo.getPhone());
                userTem.setEmail(vo.getEmail());
                //step.1 保存用户
                this.save(userTem);
                userId = userTem.getId();

                //创建个人租户
                CaptchLoginVO captchLoginVO = new CaptchLoginVO();
                captchLoginVO.setPhone(vo.getPhone());
                captchLoginVO.setEmail(vo.getEmail());
                this.addPersonalTenant(captchLoginVO);
                //解析数据库已有数据做绑定表:sign_ru_task
                //绑定手机号：账号ID
                if (MyStringUtils.isNotBlank(vo.getPhone())) {
                    flowService.bindOutUserTask(null, vo.getPhone(), null, null, null, userId, null, null, "userJion");
                } else if (MyStringUtils.isNotBlank(vo.getEmail())) {
                    flowService.bindOutUserTask(null, null, vo.getEmail(), null, null, userId, null, null, "userJion");
                }

            } else {
                userTem.setRealname(vo.getRealName());
                this.updateById(userTem);
                userId = userTem.getId();
            }
        } else {
            userTem.setRealname(vo.getRealName());
            this.updateById(userTem);
            userId = userTem.getId();
        }
        //处理 租户-用户信息
        if (MyStringUtils.isBlank(vo.getRedisKey())) {
            throw new PaasException("传值错误");
        }
        Object userVo = redisUtil.get(CommonConstant.PREFIX_INVITE_USER_CODE + vo.getRedisKey());
        if (userVo == null) {
            throw new PaasException("传值错误或邀请已失效");
        }
        InviteUserVO inviteUserVO = (InviteUserVO) userVo;
        String checkFlag = sysTenantConfigService.getConfigByType(inviteUserVO.getTenantId(), "tenant_user_check").getValue();
        //个人邀请
        if (MyStringUtils.isNotBlank(inviteUserVO.getPhone())) {
            if (inviteUserVO.getPhone().equals(vo.getPhone())) {
                //用户生效
                if (userTem.getStatus() == 0) {
                    userTem.setStatus(1);
                    this.updateById(userTem);
                }
                //租户下用户生效
                SysTenantUser tenantUser = iSysTenantUserService.getTenantUser(inviteUserVO.getTenantId(), userId);
                if (tenantUser != null) {
                    if (tenantUser.getStatus() == 0) {
                        tenantUser.setStatus(1);
                        iSysTenantUserService.updateById(tenantUser);
                        //解析数据库已有数据做绑定表:sign_ru_task
                        //绑定租户ID+账号ID：租户账号ID
                        flowService.bindOutUserTask(null, null, null, tenantUser.getTenantId(), tenantUser.getUserId(), tenantUser.getUserId(), tenantUser.getTenantId(), tenantUser.getId(), "userJion");
                    } else {
                        throw new PaasException("用户已经加入过该企业,不可以重复加入");
                    }
                } else {
                    throw new PaasException("链接已经失效");
                }
            } else {
                throw new PaasException("手机号不可以修改");
            }
        } else if (MyStringUtils.isNotBlank(inviteUserVO.getEmail())) {
            if (inviteUserVO.getEmail().equals(vo.getEmail())) {
                //用户生效
                if (userTem.getStatus() == 0) {
                    userTem.setStatus(1);
                    this.updateById(userTem);
                }
                //租户下用户生效
                SysTenantUser tenantUser = iSysTenantUserService.getTenantUser(inviteUserVO.getTenantId(), userId);
                if (tenantUser != null) {
                    if (tenantUser.getStatus() == 0) {
                        tenantUser.setStatus(1);
                        iSysTenantUserService.updateById(tenantUser);
                        //解析数据库已有数据做绑定表:sign_ru_task
                        //绑定租户ID+账号ID：租户账号ID
                        flowService.bindOutUserTask(null, null, null, tenantUser.getTenantId(), tenantUser.getUserId(), tenantUser.getUserId(), tenantUser.getTenantId(), tenantUser.getId(), "userJion");
                    } else {
                        throw new PaasException("用户已经加入过该企业,不可以重复加入");
                    }
                } else {
                    throw new PaasException("链接已经失效");
                }
            } else {
                throw new PaasException("邮箱不可以修改");
            }
        } else if (MyStringUtils.isNotBlank(inviteUserVO.getDepartId())) {
            //部门邀请
            //用户生效
            if (userTem.getStatus() == 0) {
                userTem.setStatus(1);
                this.updateById(userTem);
            }
            //租户下用户生效
            SysTenantUser tenantUser = iSysTenantUserService.getTenantUser(inviteUserVO.getTenantId(), userId);
            if (tenantUser == null) {
                tenantUser = new SysTenantUser();
                tenantUser.setTenantId(inviteUserVO.getTenantId());
                tenantUser.setUserId(userId);
                //可用
                if ("false".equals(checkFlag)) {
                    tenantUser.setStatus(1);
                } else if ("true".equals(checkFlag)) {
                    tenantUser.setStatus(-2);
                }
                tenantUser.setAddType("user");
                tenantUser.setNickName(vo.getRealName());

                iSysTenantUserService.save(tenantUser);
                //增加申请记录
                if ("true".equals(checkFlag)) {
                    addTenantUserRecord(userId, inviteUserVO.getTenantId(), tenantUser.getId(), vo.getRealName(), userTem.getPhone(), userTem.getEmail());
                } else if ("false".equals(checkFlag)) {
                    //解析数据库已有数据做绑定表:sign_ru_task
                    //绑定租户ID+账号ID：租户账号ID
                    flowService.bindOutUserTask(null, null, null, tenantUser.getTenantId(), tenantUser.getUserId(), tenantUser.getUserId(), tenantUser.getTenantId(), tenantUser.getId(), "userJion");
                }
            } else {
                if (tenantUser.getStatus() == 0) {
                    tenantUser.setStatus(1);
                    iSysTenantUserService.updateById(tenantUser);
                } else {
                    throw new PaasException("用户已经加入过该企业,不可以重复加入");
                }
            }
            //租户下部门维护
            SysUserDepart userDeaprt = new SysUserDepart(inviteUserVO.getTenantId(), userId, inviteUserVO.getDepartId(), false);
            iSysUserDepartService.addExt(userDeaprt);
        }

        //处理租户下用户应用的初始化
        intiAuthOnTenant(userId, inviteUserVO.getTenantId());
    }

    @Override
    public void intiAuthOnTenant(String userId, String tenantId) {
        List<SysAuthGroup> sysAuthGroups = iSysAuthGroupService.getSystemGroup(tenantId);
        String baseGroupId = null;
        if (CollUtil.isNotEmpty(sysAuthGroups)) {
            for (SysAuthGroup g : sysAuthGroups) {
                if (g.getGroupType() == 2) {
                    baseGroupId = g.getId();
                }
            }
        }
        if (baseGroupId != null) {
            //维护权限组-用户
            SysAuthGroupMemberList2 memberList = new SysAuthGroupMemberList2();
            memberList.setAuthId(userId);
            memberList.setAuthType(AuthType.USER.getType());
            memberList.setTenantId(tenantId);
            List<String> groups = new ArrayList<>();
            groups.add(baseGroupId);
            memberList.setGroupIds(groups);
            iSysAuthGroupMemberService.saveExt2(memberList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer jionTenant(InviteUserVO inviteUserVO) {
        Integer flag = null;
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        if (MyStringUtils.isBlank(inviteUserVO.getInvitationCode())) {
            throw new PaasException("邀请码不能为空");
        }
        SysTenantInfo sysTenantInfo = iSysTenantInfoService.getByInvitationCode(inviteUserVO.getInvitationCode());
        if (sysTenantInfo == null) {
            throw new PaasException("邀请码错误");
        }
        String checkFlag = sysTenantConfigService.getConfigByType(sysTenantInfo.getId(), "tenant_user_check").getValue();
        //租户下用户生效
        SysTenantUser tenantUser = iSysTenantUserService.getTenantUser(sysTenantInfo.getId(), loginUser.getId());
        if (tenantUser == null) {
            tenantUser = new SysTenantUser();
            tenantUser.setTenantId(sysTenantInfo.getId());
            tenantUser.setUserId(loginUser.getId());
            //可用
            if ("false".equals(checkFlag)) {
                flag = 1;
                tenantUser.setStatus(1);
            } else if ("true".equals(checkFlag)) {
                flag = 0;
                tenantUser.setStatus(-2);
            }
            tenantUser.setAddType("user");
            tenantUser.setNickName(loginUser.getRealname());

            iSysTenantUserService.save(tenantUser);

            if ("true".equals(checkFlag)) {
                //增加申请记录
                addTenantUserRecord(loginUser.getId(), sysTenantInfo.getId(), tenantUser.getId(), loginUser.getRealname(), loginUser.getPhone(), loginUser.getEmail());
            } else if ("false".equals(checkFlag)) {
                //解析数据库已有数据做绑定表:sign_ru_task
                //绑定租户ID+用户ID：租户账号ID
                flowService.bindOutUserTask(null, null, null, sysTenantInfo.getId(), loginUser.getId(), tenantUser.getUserId(), tenantUser.getTenantId(), tenantUser.getId(), "jionTenant");
            }


        } else {
            if (tenantUser.getStatus() == 0) {
                tenantUser.setStatus(1);
                iSysTenantUserService.updateById(tenantUser);
            } else {
                throw new PaasException("用户已经加入过该企业,不可以重复加入");
            }
        }
        //租户下部门维护
        //租户下根部门
        SysDepart query = new SysDepart();
        query.setTenantId(sysTenantInfo.getId());
        List<SysDepart> departs = iSysDepartService.getByEntity(query);
        if (CollUtil.isNotEmpty(departs)) {
            SysUserDepart userDeaprt = new SysUserDepart(sysTenantInfo.getId(), loginUser.getId(), departs.get(0).getId(), false);
            iSysUserDepartService.addExt(userDeaprt);
        }

        //处理租户下用户应用的初始化
        intiAuthOnTenant(loginUser.getId(), sysTenantInfo.getId());

        return flag;
    }

    void addTenantUserRecord(String userId, String tenantId, String tenantUserId, String nickName, String phone, String email) {
        SysTenantUserRecord query = new SysTenantUserRecord();
        query.setUserId(userId);
        query.setTenantId(tenantId);
        SysTenantUserRecord entity = sysTenantUserRecordService.getByEntity(query);
        if (entity != null) {
            sysTenantUserRecordService.removeById(entity);
        }
        SysTenantUserRecord record = new SysTenantUserRecord();
        record.setUserId(userId);
        record.setTenantId(tenantId);
        record.setTenantUserId(tenantUserId);
        record.setNickName(nickName);
        record.setPhone(phone);
        record.setEmail(email);
        record.setApplyTime(new Date());
        record.setStatus(0);

        sysTenantUserRecordService.save(record);
    }

    private void addPersonalTenant(CaptchLoginVO vo) {
        //开通租户
        SysTenantInfoVO sysTenantInfoVO = new SysTenantInfoVO();
        if (MyStringUtils.isNotBlank(vo.getPhone())) {
            sysTenantInfoVO.setUsername(vo.getPhone());
            sysTenantInfoVO.setRealName(vo.getPhone());
            sysTenantInfoVO.setPhone(vo.getPhone());
            sysTenantInfoVO.setTenantName(vo.getPhone());
        } else {
            sysTenantInfoVO.setUsername(vo.getEmail());
            sysTenantInfoVO.setRealName(vo.getEmail());
            sysTenantInfoVO.setEmail(vo.getEmail());
            sysTenantInfoVO.setTenantName(vo.getEmail());
        }
        sysTenantInfoVO.setPassword(vo.getPassword());
        sysTenantInfoVO.setTenantType(TenantType.PERSONAL.getType());

        SysAddTenantInfoVO sysAddTenantInfoVO = iSysTenantInfoService.addTenantAndAddApp(sysTenantInfoVO);

        String tenantId = sysAddTenantInfoVO.getTenantId();

        //维护租户扩展信息
        TenantInfoExtend tenantInfoExtend = new TenantInfoExtend();
        tenantInfoExtend.setTenantId(tenantId);
        tenantInfoExtend.setTenantType(TenantType.PERSONAL.getType());
        String phone = vo.getPhone();
        try {
            phone = RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING, phone);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tenantInfoExtend.setPhone(phone);
        String email = vo.getEmail();
        try {
            email = RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING, email);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tenantInfoExtend.setEmail(email);
        //未认证
        tenantInfoExtend.setAuthStatus(TenantAuthStatus.STATUS0.getStatus());
        tenantInfoExtendService.save(tenantInfoExtend);
    }

    @Override
    public List<SysTenantUser> getTenantUsers(String name) {
        return iSysTenantUserService.getTenantUsers(name);
    }
}
