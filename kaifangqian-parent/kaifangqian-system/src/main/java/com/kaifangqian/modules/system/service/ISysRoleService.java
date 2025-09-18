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

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kaifangqian.modules.system.entity.SysRole;
import com.kaifangqian.modules.system.vo.SysRoleUserVO;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 批量删除角色
     *
     * @param roleIds
     * @return
     */
    boolean deleteBatchRole(List<String> roleIds);

    /**
     * 根据部门id查询用户信息
     *
     * @param roleId
     * @return
     */
    IPage<SysRoleUserVO> getUsersByRoleId(String roleId, Page<SysRoleUserVO> page);

    void saveExt(SysRole sysRole);

    SysRole getByIdExt(String id);

    void updateExt(SysRole sysRole);

    List<SysRole> getByEntity(SysRole sysRole);

    List<Tree<String>> getTreeList(List<SysRole> list);

    List<Tree<String>> getTreeListForSelect(List<SysRole> list);

    List<SysRole> getMyRoles();

    //签章系统使用
    List<SysRole> getTenantRoles();

    List<SysRole> findSysRoleByRoleIds(List<String> roleIds);
}
