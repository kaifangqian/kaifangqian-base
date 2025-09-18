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

import com.kaifangqian.modules.system.entity.SysAuthGroupPermission;
import com.kaifangqian.modules.system.vo.SysAuthGroupPermissionReq;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author zhenghuihan
 * @description 权限组-权限表
 * @createTime 2022/9/2 18:12
 */
public interface ISysAuthGroupPermissionService extends IService<SysAuthGroupPermission> {
    List<SysAuthGroupPermission> listByEntity(SysAuthGroupPermission sysAuthGroupPermission);

    void editExt(SysAuthGroupPermissionReq req);

    void addExt(SysAuthGroupPermissionReq req);

    void deleteByPermissionId(String permissionId);
}
