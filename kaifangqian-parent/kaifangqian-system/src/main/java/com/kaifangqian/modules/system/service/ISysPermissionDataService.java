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
import com.kaifangqian.modules.system.entity.SysPermissionData;
import com.kaifangqian.modules.system.vo.SysPermissionDataVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author zhenghuihan
 * @description 权限策略表
 * @createTime 2022/9/2 18:13
 */
public interface ISysPermissionDataService extends IService<SysPermissionData> {

    IPage<SysPermissionData> pageExt(Page<SysPermissionData> page, String permissionId);


    IPage<SysPermissionData> pageExt2(Page<SysPermissionData> page, String permissionId);

    void addDefault(String permissionId);

    void addCustom(SysPermissionDataVO dataVO);

    void addAllCustom(SysPermissionDataVO dataVO);

    void editCustom(SysPermissionDataVO dataVO);

    void deleteExt(String id);

    SysPermissionDataVO getByIdExt(String id);

    void deleteByPermissionId(String permissionId);

    List<SysPermissionData> listByPermissionId(String permissionId);

    List<SysPermissionData> listTenantRuleByPermissionIds(List<String> permissionIds);
}
