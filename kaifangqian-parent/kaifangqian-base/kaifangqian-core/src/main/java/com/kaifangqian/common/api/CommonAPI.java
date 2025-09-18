/**
 * [类功能描述：底层共用API接口]
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
package com.kaifangqian.common.api;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.system.vo.SysPermissionDataRuleModel;
import com.kaifangqian.common.system.vo.*;

import java.util.List;


/**
 * @Author: zhh
 */
public interface CommonAPI {

    /**
     * 1查询用户权限信息
     */
    List<String> queryUserAuths();

    /**
     * 2-查询用户数据权限
     */
    List<List<List<List<SysPermissionDataRuleModel>>>> queryPermissionDataRule(String perms);

    /**
     * 3-根据部门code查询部门ID
     */
    String getDepartIdByOrgCode(String orgCode);

    /**
     * 4-根据用户账号查询用户信息
     */
    LoginUser getUserByName(String username);

    /**
     * 5-根据用户账号查询角色IDS
     */
    List<String> getMyRoleIds();

    /**
     * 6-根据用户账号查询部门IDS
     */
    List<String> getDepartsByName(String username);

    /**
     * 7-校验用户-租户是否合理:true：合理 false：不合理
     */
    boolean checkUserTenant(String userId, String tenantId);


    /**
     * 8-校验(租户下)用户-部门是否合理:true：合理 false：不合理
     */
    boolean checkUserDepart(String userId, String tenantId, String departId);

    /**
     * 9-校验(租户下)用户-应用是否合理:true：合理 false：不合理
     */
    boolean checkUserTenantApp(String userId, String tenantId, String appCode);

    /**
     * 10-根据租户ID和账号ID获取用户ID
     */
    String getTenantUserId(String tenantId, String userId);

    void recordNormalRes(String res);
}
