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
package com.kaifangqian.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kaifangqian.modules.system.entity.SysTenantUser;
import com.kaifangqian.modules.system.entity.SysUser;
import com.kaifangqian.modules.system.model.SysUserSearchModel;
import com.kaifangqian.modules.system.vo.*;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.system.vo.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 根据关键字搜索相关的用户数据
     *
     * @param keyWord
     * @return
     */
    IPage<SysUserSearchModel> searchMyBy(String keyWord, List<String> departIds, Page<SysUserSearchModel> page);

    boolean checkRepeat(String checkType, String key, String userId);

    /**
     * 重置密码
     */
    void resetPassword(SysUserFrozenVO sysUser);

    /**
     * 根据旧密码修改密码
     */
    Result<?> updatePasswordByOldPassword(UserPasswordUpdateVO vo);

    /**
     * 根据验证码修改密码
     */
    Result<?> updatePasswordByCode(UserPasswordUpdateVO vo);

    /**
     * 根据验证码修改密码带token
     */
    Result<?> updatePasswordByCodeUserToken(UserPasswordUpdateVO vo);

    /**
     * 批量删除用户
     *
     * @param tenantUserIds
     * @return
     */
    void deleteBatchUsers(List<String> tenantUserIds);

    void frozenBatch(SysUserFrozenVO sysUserFrozenVO);

    void updateDepartBatch(SysUserEditDepartVO sysUser);

    SysUser getUserByName(String username);

    List<SysUser> getUserByStatus(Integer status);

    /**
     * 根据 userIds查询，查询用户所属部门的名称（多个部门名逗号隔开）
     *
     * @param
     * @return
     */
    Map<String, String> getDepNamesByUserIds(List<String> userIds);

    /**
     * 根据用户名设置部门ID
     *
     * @param username
     * @param orgCode
     */
    void updateUserDepart(String username, String orgCode);

    /**
     * 根据手机号获取用户名和密码
     */
    SysUser getUserByPhone(String phone);


    /**
     * 根据邮箱获取用户
     */
    SysUser getUserByEmail(String email);

    /**
     * 校验用户是否有效
     *
     * @param sysUser
     * @return
     */
    Result checkUserIsEffective(SysUser sysUser);


    boolean check(String username, String accountType);

    String getUsername(String username, String accountType);

    /**
     * 保存用户
     *
     * @param sysUser 用户
     */
    void saveUser(SysUserVO sysUser);

    void saveVisitor(SysUser sysUser);

    /**
     * 编辑用户
     *
     * @param sysUser 用户
     */
    void editUser(SysUserVO sysUser);

    List<SysUser> findByUserIds(List<String> userIds);

    List<SysUser> listAll();

    void userJion(UserJionSystemVO userJionSystemVO);

    Integer jionTenant(InviteUserVO inviteUserVO);

    void intiAuthOnTenant(String userId, String tenantId);

    //签章系统使用
    List<SysTenantUser> getTenantUsers(String name);
}
