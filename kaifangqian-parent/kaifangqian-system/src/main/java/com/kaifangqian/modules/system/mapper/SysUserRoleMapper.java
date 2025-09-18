/**
 * Discription:用户角色表Mapper
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
package com.kaifangqian.modules.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.common.vo.TenantUserInfo;
import com.kaifangqian.modules.system.entity.SysRole;
import com.kaifangqian.modules.system.vo.SysRoleUserVO;
import org.apache.ibatis.annotations.Param;
import com.kaifangqian.modules.system.entity.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    IPage<SysRoleUserVO> getUsersByRoleId(Page page, @Param("roleId") String roleId, @org.apache.ibatis.annotations.Param("departIds") List<String> departIds, @Param("tenantId") String tenantId);

    List<String> getRoleIdsByUser(@Param("tenantId") String tenantId, @Param("userId") String userId);

    List<SysRole> getRolesByUserId(@Param("userId") String userId);

    List<TenantUserInfo> getTenantUsersByRoleIds(@org.apache.ibatis.annotations.Param("roleIds") List<String> roleIds);
}
