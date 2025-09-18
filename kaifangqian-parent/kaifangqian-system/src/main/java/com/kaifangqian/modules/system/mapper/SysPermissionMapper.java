/**
 * Discription:菜单权限表Mapper
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

import com.kaifangqian.modules.system.vo.UserAuthDataQueryVO;
import org.apache.ibatis.annotations.Param;
import com.kaifangqian.modules.system.entity.SysPermission;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
    /**
     * 根据用户查询用户权限
     */
    List<SysPermission> queryByUser(@Param("query") UserAuthDataQueryVO query);

    List<SysPermission> listTenantAppAllMenu(@Param("tenantId") String tenantId, @Param("appId") String appId, @Param("parentId") String parentId, @Param("types") List<Integer> types);
}
