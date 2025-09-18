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

import com.kaifangqian.modules.system.entity.SysTenantInfo;
import com.kaifangqian.modules.system.entity.SysTenantUser;
import com.kaifangqian.modules.system.entity.SysUser;
import com.kaifangqian.modules.system.vo.JionRefuseTenantVO;
import com.kaifangqian.common.vo.TenantUserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
/**
 * @author zhenghuihan
 * @description 租户用户关系服务类
 * @createTime 2022/9/2 18:13
 */
public interface ISysTenantUserService extends IService<SysTenantUser> {

    //校验用户-租户是否合理:true：合理 false：不合理
    boolean checkUserTenant(String userId, String tenantId);

    List<SysTenantInfo> getTenantsByUserId(String userId);

    List<SysTenantInfo> getTenantsByUserIdAndAppId(String userId, String appId);

    SysTenantUser getTenantUser(String tenantId, String userId);


    SysTenantUser getActiviTenantUser(String tenantId, String userId);

    List<SysTenantUser> getByIds(List<String> ids);

    List<SysTenantUser> getTenantUsers(String name);

    List<JionRefuseTenantVO> getInviteList();

    void jionOrRefuseTenant(JionRefuseTenantVO vo);

    boolean checkPersonalTenant(String userId);

    List<TenantUserInfo> getAllTenantUsers();

    SysUser getTenantSysUser(String tenantUserId);

    SysTenantUser getPersonalTenantUser(String userId);

    List<SysTenantUser> getTenantSystemUsers();

    List<SysTenantUser> getTenantSystemUsersByTenantId(String companyTenantId);
}
